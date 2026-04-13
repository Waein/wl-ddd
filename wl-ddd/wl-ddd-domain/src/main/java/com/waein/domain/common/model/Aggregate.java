package com.waein.domain.common.model;

import com.waein.types.dp.Identifier;

/**
 * 聚合根的Marker接口
 * Aggregate Root是Repository操作的基本单位
 */
public interface Aggregate<ID extends Identifier> extends Entity<ID> {

}
