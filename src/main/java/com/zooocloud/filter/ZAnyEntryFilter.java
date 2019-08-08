package com.zooocloud.filter;

import com.zooocloud.ZEntryFilter;

import java.util.Collection;

/**
 * ANY逻辑混合过滤器，即任意一个过滤器满足时就满足，当没有过滤器的时候则认为没有过滤器满足，也就是不满足。
 *
 * @author CK 364656329@qq.com
 * 2018/12/4 15:26
 */
public class ZAnyEntryFilter<E> extends ZMixEntryFilter<E> implements ZEntryFilter<E> {

    public ZAnyEntryFilter() {
        super(null);
    }

    public ZAnyEntryFilter(Collection<? extends ZEntryFilter<? extends E>> filters) {
        super(filters);
    }

    @Override
    public ZAnyEntryFilter<E> mix(ZEntryFilter<? extends E> filter) {
        add(filter);
        return this;
    }

    @Override
    public boolean filtrate(E entry) {
        ZEntryFilter[] filters = this.filters.toArray(new ZEntryFilter[0]);
        for (ZEntryFilter filter : filters) {
            if (filter.filtrate(entry)) {
                return true;
            }
        }
        return false;
    }
}
