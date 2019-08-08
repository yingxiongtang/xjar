package com.zooocloud.jar;

import com.zooocloud.*;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.Deflater;

/**
 * 普通JAR包加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:27
 */
public class ZJarEncryptor extends ZEntryEncryptor<JarArchiveEntry> implements ZEncryptor, ZConstants {
    private final int level;
    private final int mode;

    public ZJarEncryptor(ZEncryptor zEncryptor) {
        this(zEncryptor, new ZJarAllEntryFilter());
    }

    public ZJarEncryptor(ZEncryptor zEncryptor, ZEntryFilter<JarArchiveEntry> filter) {
        this(zEncryptor, Deflater.DEFLATED, filter);
    }

    public ZJarEncryptor(ZEncryptor zEncryptor, int level) {
        this(zEncryptor, level, new ZJarAllEntryFilter());
    }

    public ZJarEncryptor(ZEncryptor zEncryptor, int level, ZEntryFilter<JarArchiveEntry> filter) {
        this(zEncryptor, level, MODE_NORMAL, filter);
    }

    public ZJarEncryptor(ZEncryptor zEncryptor, int level, int mode) {
        this(zEncryptor, level, mode, new ZJarAllEntryFilter());
    }

    public ZJarEncryptor(ZEncryptor zEncryptor, int level, int mode, ZEntryFilter<JarArchiveEntry> filter) {
        super(zEncryptor, filter);
        this.level = level;
        this.mode = mode;
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
        JarArchiveInputStream zis = null;
        JarArchiveOutputStream zos = null;
        Set<String> indexes = new LinkedHashSet<>();
        try {
            zis = new JarArchiveInputStream(in);
            zos = new JarArchiveOutputStream(out);
            zos.setLevel(level);
            ZUnclosedInputStream nis = new ZUnclosedInputStream(zis);
            ZUnclosedOutputStream nos = new ZUnclosedOutputStream(zos);
            JarArchiveEntry entry;
            Manifest manifest = null;
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
                    manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Jar-Main-Class", mainClass);
                        attributes.putValue("Main-Class", "com.zooocloud.jar.ZJarLauncher");
                    }
                    if ((mode & FLAG_DANGER) == FLAG_DANGER) {
                        ZKit.retainKey(key, attributes);
                    }
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    manifest.write(nos);
                } else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filtrate(entry);
                    if (filtered) {
                        indexes.add(entry.getName());
                    }
                    ZEncryptor encryptor = filtered ? zEncryptor : xNopEncryptor;
                    try (OutputStream eos = encryptor.encrypt(key, nos)) {
                        ZKit.transfer(nis, eos);
                    }
                }
                zos.closeArchiveEntry();
            }

            if (!indexes.isEmpty()) {
                JarArchiveEntry zjarInfDir = new JarArchiveEntry(ZJAR_INF_DIR);
                zjarInfDir.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(zjarInfDir);
                zos.closeArchiveEntry();

                JarArchiveEntry zjarInfIdx = new JarArchiveEntry(ZJAR_INF_DIR + ZJAR_INF_IDX);
                zjarInfIdx.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(zjarInfIdx);
                for (String index : indexes) {
                    zos.write(index.getBytes());
                    zos.write(CRLF.getBytes());
                }
                zos.closeArchiveEntry();
            }

            String mainClass = manifest != null && manifest.getMainAttributes() != null ? manifest.getMainAttributes().getValue("Main-Class") : null;
            if (mainClass != null) {
                ZInjector.inject(zos);
            }

            zos.finish();
        } finally {
            ZKit.close(zis);
            ZKit.close(zos);
        }
    }

}
