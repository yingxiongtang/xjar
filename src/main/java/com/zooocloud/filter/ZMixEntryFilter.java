package com.zooocloud.filter;

import com.zooocloud.ZEntryFilter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 混合过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/4 15:20
 */
public abstract class ZMixEntryFilter<E> implements ZEntryFilter<E> {
    protected final Set<ZEntryFilter<? extends E>> filters;

    protected ZMixEntryFilter() {
        this(null);
    }

    protected ZMixEntryFilter(Collection<? extends ZEntryFilter<? extends E>> filters) {
        this.filters = filters != null ? new LinkedHashSet<>(filters) : new LinkedHashSet<ZEntryFilter<? extends E>>();
    }

    public boolean add(ZEntryFilter<? extends E> filter) {
        return filters.add(filter);
    }

    public boolean remove(ZEntryFilter<? extends E> filter) {
        return filters.remove(filter);
    }

    public abstract ZMixEntryFilter<E> mix(ZEntryFilter<? extends E> filter);
}
