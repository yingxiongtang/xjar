package com.zooocloud.dir;

import com.zooocloud.ZEncryptor;
import com.zooocloud.ZEntryEncryptor;
import com.zooocloud.ZEntryFilter;
import com.zooocloud.key.ZKey;

import java.io.File;
import java.io.IOException;

/**
 * 文件夹加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:01
 */
public class ZDirEncryptor extends ZEntryEncryptor<File> implements ZEncryptor {

    public ZDirEncryptor(ZEncryptor zEncryptor) {
        this(zEncryptor, null);
    }

    public ZDirEncryptor(ZEncryptor zEncryptor, ZEntryFilter<File> filter) {
        super(zEncryptor, filter);
    }

    @Override
    public void encrypt(ZKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            ZEncryptor encryptor = filtrate(src) ? zEncryptor : xNopEncryptor;
            encryptor.encrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                encrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }

}
