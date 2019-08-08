package com.zooocloud.filter;

import com.zooocloud.ZEntryFilter;

/**
 * 非门逻辑过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:21
 */
public class ZNotEntryFilter<E> implements ZEntryFilter<E> {
    private final ZEntryFilter<E> delegate;

    public ZNotEntryFilter(ZEntryFilter<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean filtrate(E entry) {
        return !delegate.filtrate(entry);
    }
}
