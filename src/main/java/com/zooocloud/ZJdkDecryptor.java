package com.zooocloud;

import com.zooocloud.key.ZKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

/**
 * JDK内置解密算法的解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 14:01
 */
public class ZJdkDecryptor implements ZDecryptor {
    private final String algorithm;

    public ZJdkDecryptor(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void decrypt(ZKey key, File src, File dest) throws IOException {
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("could not make directory: " + dest.getParentFile());
        }
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(key, in, out);
        }
    }

    @Override
    public void decrypt(ZKey key, InputStream in, OutputStream out) throws IOException {
        CipherInputStream cis = null;
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm));
            cis = new CipherInputStream(in, cipher);
            ZKit.transfer(cis, out);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            ZKit.close(cis);
        }
    }

    @Override
    public InputStream decrypt(ZKey key, InputStream in) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm));
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public OutputStream decrypt(ZKey key, OutputStream out) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm));
            return new CipherOutputStream(out, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
