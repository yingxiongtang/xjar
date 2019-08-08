package com.zooocloud.zip;

import com.zooocloud.*;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.zip.Deflater;

/**
 * ZIP压缩包解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:27
 */
public class ZZipDecryptor extends ZEntryDecryptor<ZipArchiveEntry> implements ZDecryptor {
    private final int level;

    public ZZipDecryptor(ZDecryptor xEncryptor) {
        this(xEncryptor, null);
    }

    public ZZipDecryptor(ZDecryptor zDecryptor, ZEntryFilter<ZipArchiveEntry> filter) {
        this(zDecryptor, Deflater.DEFLATED, filter);
    }

    public ZZipDecryptor(ZDecryptor xEncryptor, int level) {
        this(xEncryptor, level, null);
    }

    public ZZipDecryptor(ZDecryptor zDecryptor, int level, ZEntryFilter<ZipArchiveEntry> filter) {
        super(zDecryptor, filter);
        this.level = level;
    }

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
        ZipArchiveInputStream zis = null;
        ZipArchiveOutputStream zos = null;
        try {
            zis = new ZipArchiveInputStream(in);
            zos = new ZipArchiveOutputStream(out);
            zos.setLevel(level);
            ZUnclosedOutputStream nos = new ZUnclosedOutputStream(zos);
            ZipArchiveEntry entry;
            while ((entry = zis.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                zos.putArchiveEntry(new ZipArchiveEntry(entry.getName()));
                ZDecryptor decryptor = filtrate(entry) ? this : xNopDecryptor;
                try (OutputStream eos = decryptor.decrypt(key, nos)) {
                    ZKit.transfer(zis, eos);
                }
                zos.closeArchiveEntry();
            }
            zos.finish();
        } finally {
            ZKit.close(zis);
            ZKit.close(zos);
        }
    }

}
