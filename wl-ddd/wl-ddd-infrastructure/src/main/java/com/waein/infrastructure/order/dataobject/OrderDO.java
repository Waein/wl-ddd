package com.waein.infrastructure.order.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据对象 - 仅作为数据库物理表的映射
 * DO不参与业务逻辑，字段和数据库表一一对应
 */
@Data
@TableName("t_order")
public class OrderDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer status;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
