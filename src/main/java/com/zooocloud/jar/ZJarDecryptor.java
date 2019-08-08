package com.zooocloud.jar;

import com.zooocloud.*;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.Deflater;

/**
 * 普通JAR包解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:27
 */
public class ZJarDecryptor extends ZEntryDecryptor<JarArchiveEntry> implements ZDecryptor, ZConstants {
    private final int level;

    public ZJarDecryptor(ZDecryptor xEncryptor) {
        this(xEncryptor, new ZJarAllEntryFilter());
    }

    public ZJarDecryptor(ZDecryptor zDecryptor, ZEntryFilter<JarArchiveEntry> filter) {
        this(zDecryptor, Deflater.DEFLATED, filter);
    }

    public ZJarDecryptor(ZDecryptor xEncryptor, int level) {
        this(xEncryptor, level, new ZJarAllEntryFilter());
    }

    public ZJarDecryptor(ZDecryptor zDecryptor, int level, ZEntryFilter<JarArchiveEntry> filter) {
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
        JarArchiveInputStream zis = null;
        JarArchiveOutputStream zos = null;
        try {
            zis = new JarArchiveInputStream(in);
            zos = new JarArchiveOutputStream(out);
            zos.setLevel(level);
            ZUnclosedInputStream nis = new ZUnclosedInputStream(zis);
            ZUnclosedOutputStream nos = new ZUnclosedOutputStream(zos);
            JarArchiveEntry entry;
            while ((entry = zis.getNextJarEntry()) != null) {
                if (entry.getName().startsWith(ZJAR_SRC_DIR)
                        || entry.getName().endsWith(ZJAR_INF_DIR)
                        || entry.getName().endsWith(ZJAR_INF_DIR + ZJAR_INF_IDX)
                ) {
                    continue;
                }
                if (entry.isDirectory()) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                } else if (entry.getName().equals(META_INF_MANIFEST)) {
                    Manifest manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Jar-Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Main-Class", mainClass);
                        attributes.remove(new Attributes.Name("Jar-Main-Class"));
                    }
                    ZKit.removeKey(attributes);
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    manifest.write(nos);
                } else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filtrate(entry);
                    ZDecryptor decryptor = filtered ? zDecryptor : xNopDecryptor;
                    try (OutputStream eos = decryptor.decrypt(key, nos)) {
                        ZKit.transfer(nis, eos);
                    }
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
