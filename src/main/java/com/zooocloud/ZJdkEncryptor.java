package com.zooocloud;

import com.zooocloud.key.ZKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

/**
 * JDK内置加密算法的加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 14:01
 */
public class ZJdkEncryptor implements ZEncryptor {
    private final String algorithm;

    public ZJdkEncryptor(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void encrypt(ZKey key, File src, File dest) throws IOException {
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("could not make directory: " + dest.getParentFile());
        }
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(key, in, out);
        }
    }

    @Override
    public void encrypt(ZKey key, InputStream in, OutputStream out) throws IOException {
        CipherInputStream cis = null;
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncryptKey(), algorithm));
            cis = new CipherInputStream(in, cipher);
            ZKit.transfer(cis, out);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            ZKit.close(cis);
        }
    }

    @Override
    public InputStream encrypt(ZKey key, InputStream in) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncryptKey(), algorithm));
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public OutputStream encrypt(ZKey key, OutputStream out) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncryptKey(), algorithm));
            return new CipherOutputStream(out, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
