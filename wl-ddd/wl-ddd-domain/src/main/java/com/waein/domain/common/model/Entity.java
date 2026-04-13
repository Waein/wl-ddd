package com.waein.domain.common.model;

import com.waein.types.dp.Identifiable;
import com.waein.types.dp.Identifier;

/**
 * 实体类的Marker接口
 * Entity拥有ID属性，同时包含业务行为，生命周期仅存在于内存中
 */
public interface Entity<ID extends Identifier> extends Identifiable<ID> {

}
