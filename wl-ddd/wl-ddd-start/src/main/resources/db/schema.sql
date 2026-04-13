-- wl-ddd 脚手架示例建表SQL

CREATE DATABASE IF NOT EXISTS wl_ddd DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE wl_ddd;

-- 订单主表
CREATE TABLE IF NOT EXISTS t_order (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    status      INT          NOT NULL DEFAULT 0 COMMENT '订单状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消',
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    currency    VARCHAR(8)   NOT NULL DEFAULT 'CNY' COMMENT '币种',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单行项表
CREATE TABLE IF NOT EXISTS t_order_line_item (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_id    BIGINT       NOT NULL COMMENT '订单ID',
    item_id     BIGINT       NOT NULL COMMENT '商品ID',
    item_name   VARCHAR(256) NOT NULL DEFAULT '' COMMENT '商品名称',
    quantity    INT          NOT NULL DEFAULT 1 COMMENT '数量',
    price       DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
    currency    VARCHAR(8)   NOT NULL DEFAULT 'CNY' COMMENT '币种',
    PRIMARY KEY (id),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单行项表';
