package com.waein.types.dp;

/**
 * 可识别的对象接口，拥有唯一ID
 */
public interface Identifiable<ID extends Identifier> {
    ID getId();
}
