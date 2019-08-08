package com.zooocloud.zip;

import com.zooocloud.*;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.zip.Deflater;

/**
 * ZIP压缩包加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:27
 */
public class ZZipEncryptor extends ZEntryEncryptor<ZipArchiveEntry> implements ZEncryptor {
    private final int level;

    public ZZipEncryptor(ZEncryptor zEncryptor) {
        this(zEncryptor, null);
    }

    public ZZipEncryptor(ZEncryptor zEncryptor, ZEntryFilter<ZipArchiveEntry> filter) {
        this(zEncryptor, Deflater.DEFLATED, filter);
    }

    public ZZipEncryptor(ZEncryptor zEncryptor, int level) {
        this(zEncryptor, level, null);
    }

    public ZZipEncryptor(ZEncryptor zEncryptor, int level, ZEntryFilter<ZipArchiveEntry> filter) {
        super(zEncryptor, filter);
        this.level = level;
    }

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
                ZEncryptor encryptor = filtrate(entry) ? this : xNopEncryptor;
                try (OutputStream eos = encryptor.encrypt(key, nos)) {
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
