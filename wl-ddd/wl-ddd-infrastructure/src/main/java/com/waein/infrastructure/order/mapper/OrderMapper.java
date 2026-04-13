package com.waein.infrastructure.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.waein.infrastructure.order.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单DAO - MyBatis Mapper
 * Data Mapper只负责SQL封装和数据库连接，操作对象为DO
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDO> {

}
