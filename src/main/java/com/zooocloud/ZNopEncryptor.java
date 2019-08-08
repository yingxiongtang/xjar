package com.zooocloud;

import com.zooocloud.key.ZKey;

import java.io.*;

/**
 * 无操作加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/23 11:27
 */
public class ZNopEncryptor implements ZEncryptor {

    @Override
    public void encrypt(ZKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            encrypt(key, fis, fos);
        }
    }

    @Override
    public void encrypt(ZKey key, InputStream in, OutputStream out) throws IOException {
        ZKit.transfer(in, out);
    }

    @Override
    public InputStream encrypt(ZKey key, InputStream in) throws IOException {
        return in;
    }

    @Override
    public OutputStream encrypt(ZKey key, OutputStream out) throws IOException {
        return out;
    }
}
