package com.zooocloud.filter;

import com.zooocloud.ZEntryFilter;

import java.util.Collection;

/**
 * ALL逻辑混合过滤器，即所有过滤器都满足的时候才满足，只要有一个过滤器不满足就立刻返回不满足，如果没有过滤器的时候则认为所有过滤器都满足。
 *
 * @author CK 364656329@qq.com
 * 2018/12/4 15:26
 */
public class ZAllEntryFilter<E> extends ZMixEntryFilter<E> implements ZEntryFilter<E> {

    public ZAllEntryFilter() {
        super(null);
    }

    public ZAllEntryFilter(Collection<? extends ZEntryFilter<? extends E>> filters) {
        super(filters);
    }

    @Override
    public ZAllEntryFilter<E> mix(ZEntryFilter<? extends E> filter) {
        add(filter);
        return this;
    }

    @Override
    public boolean filtrate(E entry) {
        ZEntryFilter[] filters = this.filters.toArray(new ZEntryFilter[0]);
        for (ZEntryFilter filter : filters) {
            if (!filter.filtrate(entry)) {
                return false;
            }
        }
        return true;
    }
}
