package com.zooocloud.dir;

import com.zooocloud.ZDecryptor;
import com.zooocloud.ZEntryDecryptor;
import com.zooocloud.ZEntryFilter;
import com.zooocloud.key.ZKey;

import java.io.File;
import java.io.IOException;

/**
 * 文件夹解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:23
 */
public class ZDirDecryptor extends ZEntryDecryptor<File> implements ZDecryptor {

    public ZDirDecryptor(ZDecryptor xEncryptor) {
        this(xEncryptor, null);
    }

    public ZDirDecryptor(ZDecryptor zDecryptor, ZEntryFilter<File> filter) {
        super(zDecryptor, filter);
    }

    @Override
    public void decrypt(ZKey key, File src, File dest) throws IOException {
        if (src.isFile()) {
            ZDecryptor decryptor = filtrate(src) ? zDecryptor : xNopDecryptor;
            decryptor.decrypt(key, src, dest);
        } else if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                decrypt(key, files[i], new File(dest, files[i].getName()));
            }
        }
    }
}
