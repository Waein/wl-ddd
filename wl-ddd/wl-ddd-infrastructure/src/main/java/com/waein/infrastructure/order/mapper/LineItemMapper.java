package com.waein.infrastructure.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.waein.infrastructure.order.dataobject.LineItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 行项DAO - MyBatis Mapper
 */
@Mapper
public interface LineItemMapper extends BaseMapper<LineItemDO> {

    default List<LineItemDO> selectByOrderId(Long orderId) {
        return selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<LineItemDO>()
                        .eq(LineItemDO::getOrderId, orderId)
        );
    }

    default void deleteByOrderId(Long orderId) {
        delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<LineItemDO>()
                        .eq(LineItemDO::getOrderId, orderId)
        );
    }
}
