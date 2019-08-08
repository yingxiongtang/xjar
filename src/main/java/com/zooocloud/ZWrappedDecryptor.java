package com.zooocloud;

import com.zooocloud.key.ZKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 包装的解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:22
 */
public abstract class ZWrappedDecryptor implements ZDecryptor {
    protected final ZDecryptor zDecryptor;

    protected ZWrappedDecryptor(ZDecryptor zDecryptor) {
        this.zDecryptor = zDecryptor;
    }

    @Override
    public void decrypt(ZKey key, File src, File dest) throws IOException {
        zDecryptor.decrypt(key, src, dest);
    }

    @Override
    public void decrypt(ZKey key, InputStream in, OutputStream out) throws IOException {
        zDecryptor.decrypt(key, in, out);
    }

    @Override
    public InputStream decrypt(ZKey key, InputStream in) throws IOException {
        return zDecryptor.decrypt(key, in);
    }

    @Override
    public OutputStream decrypt(ZKey key, OutputStream out) throws IOException {
        return zDecryptor.decrypt(key, out);
    }
}
