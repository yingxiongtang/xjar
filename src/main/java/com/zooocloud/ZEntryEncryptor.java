package com.zooocloud;

/**
 * 记录可过滤的加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/23 20:38
 */
public abstract class ZEntryEncryptor<E> extends ZWrappedEncryptor implements ZEncryptor, ZEntryFilter<E> {
    protected final ZEntryFilter<E> filter;
    protected final ZNopEncryptor xNopEncryptor = new ZNopEncryptor();

    protected ZEntryEncryptor(ZEncryptor zEncryptor) {
        this(zEncryptor, null);
    }

    protected ZEntryEncryptor(ZEncryptor zEncryptor, ZEntryFilter<E> filter) {
        super(zEncryptor);
        this.filter = filter;
    }

    @Override
    public boolean filtrate(E entry) {
        return filter == null || filter.filtrate(entry);
    }
}
