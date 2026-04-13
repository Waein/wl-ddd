# wl-ddd

基于阿里技术专家殷浩 DDD 系列文章搭建的 **六边形架构（Hexagonal Architecture）** 脚手架项目。

## 架构概览

```
                        ┌──────────────────────────────────┐
                        │           wl-ddd-start           │  SpringBoot 启动
                        └──────────┬───────────┬───────────┘
                                   │           │
                   ┌───────────────▼──┐  ┌─────▼────────────────┐
                   │   wl-ddd-web     │  │ wl-ddd-infrastructure │  具体实现(MyBatis/MySQL/Redis)
                   │   Controller     │  │ Repository Impl       │
                   └────────┬─────────┘  │ Data Converter        │
                            │            │ DO / Mapper            │
                            │            └─────────┬──────────────┘
                   ┌────────▼─────────┐            │
                   │ wl-ddd-application│◄───────────┘  依赖注入
                   │ App Service / DTO │
                   │ DTO Assembler     │
                   └────────┬──────────┘
                            │
                   ┌────────▼──────────┐
                   │  wl-ddd-domain    │  纯 POJO，零框架依赖
                   │  Entity / Agg Root│
                   │  Repository 接口   │
                   │  Domain Service   │
                   └────────┬──────────┘
                            │
                   ┌────────▼──────────┐
                   │  wl-ddd-types     │  纯 POJO，零依赖
                   │  Domain Primitive │
                   │  Value Object     │
                   └───────────────────┘
```

**越内层代码演进越快，越外层代码越稳定。**

## 模块说明

| 模块 | 职责 | 框架依赖 | 对应 DDD 概念 |
|------|------|---------|-------------|
| `wl-ddd-types` | Domain Primitive / Value Object | 无 | DP: `OrderId`, `Money`, `Quantity` 等自校验值对象 |
| `wl-ddd-domain` | Entity, Aggregate Root, Repository 接口, Domain Service | 无 | 领域模型核心，包含全部业务逻辑 |
| `wl-ddd-application` | Application Service, DTO, DTO Assembler | Spring Context | 编排层，不含计算逻辑 |
| `wl-ddd-infrastructure` | Repository 实现, DO, Mapper, Data Converter | MyBatis Plus, MySQL | 基础设施层，对接数据库/中间件 |
| `wl-ddd-web` | Controller, 全局异常处理 | Spring MVC | 接入层 |
| `wl-ddd-start` | SpringBoot 启动类, 配置文件, SQL 脚本 | Spring Boot | 应用入口 |

## 核心设计原则

来源：殷浩详解 DDD 系列

### 1. Domain Primitive（第一讲）

将隐性概念显性化——`String phone` 变为 `PhoneNumber`，在构造时自校验，消灭散落各处的校验代码。

```java
// 只要创建成功，一定是合法的
OrderId orderId = new OrderId(123L);   // <=0 会抛 ValidationException
Money price = Money.ofCny(new BigDecimal("99.90"));
```

### 2. 六边形应用架构（第二讲）

- **领域层（Domain）** 零框架依赖，可 100% 单元测试
- **应用层（Application）** 仅做编排，逻辑 delegate 到 Domain
- **基础设施层（Infrastructure）** 通过依赖注入接入，可随时替换

### 3. Repository 模式（第三讲）

- 接口在 Domain 层，使用中性语义 `find` / `save` / `remove`
- 实现在 Infrastructure 层，内部完成 Entity ↔ DO 转换
- Entity 和 DO 严格分离：DO 映射数据库，Entity 承载业务逻辑

### 4. 领域层设计规范（第四讲）

- **Entity** 封装单对象有状态行为（`Order.pay()`, `Order.cancel()`）
- **Domain Service** 封装跨对象业务逻辑
- **Value Object** 不可变，自校验，封装无状态计算

## 三种模型对象

| | DO | Entity | DTO |
|---|---|---|---|
| 目的 | 数据库表映射 | 业务逻辑 | 适配业务场景 |
| 所在层 | Infrastructure | Domain | Application |
| 命名 | `XxxDO` | `Xxx` | `XxxDTO` / `XxxCommand` |
| 转化器 | `DataConverter` | — | `DtoAssembler` |
| 是否可序列化 | 不需要 | 不需要 | 需要 |

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 1. 初始化数据库

```bash
mysql -u root -p < wl-ddd-start/src/main/resources/db/schema.sql
```

### 2. 修改配置

编辑 `wl-ddd-start/src/main/resources/application.yml`，配置数据库连接信息。

### 3. 编译运行

```bash
mvn clean package -DskipTests
java -jar wl-ddd-start/target/wl-ddd-start-1.0.0-SNAPSHOT.jar
```

### 4. 测试接口

```bash
# 创建订单
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {"itemId": 101, "itemName": "商品A", "quantity": 2, "price": 99.90},
      {"itemId": 102, "itemName": "商品B", "quantity": 1, "price": 49.90}
    ]
  }'

# 查询订单
curl http://localhost:8080/api/v1/orders/1

# 支付订单
curl -X POST http://localhost:8080/api/v1/orders/1/pay

# 取消订单
curl -X POST http://localhost:8080/api/v1/orders/1/cancel
```

## 新增业务域指南

以新增 `Product`（商品）领域为例：

```
1. wl-ddd-types     → 新增 ProductId, ProductName 等 DP
2. wl-ddd-domain    → 新增 Product Entity, ProductRepository 接口
3. wl-ddd-application → 新增 ProductApplicationService, ProductDTO
4. wl-ddd-infrastructure → 新增 ProductDO, ProductMapper, ProductRepositoryImpl
5. wl-ddd-web       → 新增 ProductController
```

始终遵循：**Domain 层不依赖 Infrastructure，通过接口+依赖注入解耦。**

## 参考文章

- [殷浩详解DDD 第一讲 - Domain Primitive](https://developer.aliyun.com/article/713097)
- [殷浩详解DDD 第二讲 - 应用架构](https://developer.aliyun.com/article/715802)
- [殷浩详解DDD 第三讲 - Repository模式](https://developer.aliyun.com/article/758292)
- [阿里技术专家详解DDD 第四讲 - 领域层设计规范](https://developer.aliyun.com/article/792244)
