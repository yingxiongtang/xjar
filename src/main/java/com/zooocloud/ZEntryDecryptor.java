package com.zooocloud;

/**
 * 记录可过滤的解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/23 20:38
 */
public abstract class ZEntryDecryptor<E> extends ZWrappedDecryptor implements ZDecryptor, ZEntryFilter<E> {
    protected final ZEntryFilter<E> filter;
    protected final ZNopDecryptor xNopDecryptor = new ZNopDecryptor();

    protected ZEntryDecryptor(ZDecryptor zDecryptor) {
        this(zDecryptor, null);
    }

    protected ZEntryDecryptor(ZDecryptor zDecryptor, ZEntryFilter<E> filter) {
        super(zDecryptor);
        this.filter = filter;
    }

    @Override
    public boolean filtrate(E entry) {
        return filter == null || filter.filtrate(entry);
    }
}
