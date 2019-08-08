package com.zooocloud;

import com.zooocloud.key.ZKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 包装的加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:05
 */
public abstract class ZWrappedEncryptor implements ZEncryptor {
    protected final ZEncryptor zEncryptor;

    protected ZWrappedEncryptor(ZEncryptor zEncryptor) {
        this.zEncryptor = zEncryptor;
    }

    @Override
    public void encrypt(ZKey key, File src, File dest) throws IOException {
        zEncryptor.encrypt(key, src, dest);
    }

    @Override
    public void encrypt(ZKey key, InputStream in, OutputStream out) throws IOException {
        zEncryptor.encrypt(key, in, out);
    }

    @Override
    public InputStream encrypt(ZKey key, InputStream in) throws IOException {
        return zEncryptor.encrypt(key, in);
    }

    @Override
    public OutputStream encrypt(ZKey key, OutputStream out) throws IOException {
        return zEncryptor.encrypt(key, out);
    }
}
