package com.waein.domain.common.repository;

import com.waein.domain.common.model.Aggregate;
import com.waein.types.dp.Identifier;

/**
 * Repository基础接口
 * 使用中性的 find/save/remove 语义，避免和底层实现(insert/select/update/delete)耦合
 *
 * @param <T>  聚合根类型
 * @param <ID> ID类型
 */
public interface Repository<T extends Aggregate<ID>, ID extends Identifier> {

    /**
     * 将一个Aggregate附属到Repository，让它变为可追踪
     */
    void attach(T aggregate);

    /**
     * 解除一个Aggregate的追踪
     */
    void detach(T aggregate);

    /**
     * 通过ID寻找Aggregate
     */
    T find(ID id);

    /**
     * 将一个Aggregate从Repository移除
     */
    void remove(T aggregate);

    /**
     * 保存一个Aggregate（新增或更新）
     */
    void save(T aggregate);
}
