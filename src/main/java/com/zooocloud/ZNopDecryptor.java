package com.zooocloud;

import com.zooocloud.key.ZKey;

import java.io.*;

/**
 * 无操作解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/23 11:27
 */
public class ZNopDecryptor implements ZDecryptor {

    @Override
    public void decrypt(ZKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            decrypt(key, fis, fos);
        }
    }

    @Override
    public void decrypt(ZKey key, InputStream in, OutputStream out) throws IOException {
        ZKit.transfer(in, out);
    }

    @Override
    public InputStream decrypt(ZKey key, InputStream in) throws IOException {
        return in;
    }

    @Override
    public OutputStream decrypt(ZKey key, OutputStream out) throws IOException {
        return out;
    }
}
