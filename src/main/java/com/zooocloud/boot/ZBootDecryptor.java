package com.zooocloud.boot;

import com.zooocloud.*;
import com.zooocloud.jar.ZJarAllEntryFilter;
import com.zooocloud.jar.ZJarDecryptor;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * Spring-Boot JAR包解密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:27
 */
public class ZBootDecryptor extends ZEntryDecryptor<JarArchiveEntry> implements ZDecryptor, ZConstants {
    private final int level;

    public ZBootDecryptor(ZDecryptor xEncryptor) {
        this(xEncryptor, new ZJarAllEntryFilter());
    }

    public ZBootDecryptor(ZDecryptor zDecryptor, ZEntryFilter<JarArchiveEntry> filter) {
        this(zDecryptor, Deflater.DEFLATED, filter);
    }

    public ZBootDecryptor(ZDecryptor xEncryptor, int level) {
        this(xEncryptor, level, new ZJarAllEntryFilter());
    }

    public ZBootDecryptor(ZDecryptor zDecryptor, int level, ZEntryFilter<JarArchiveEntry> filter) {
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
            ZJarDecryptor zJarDecryptor = new ZJarDecryptor(zDecryptor, level, filter);
            JarArchiveEntry entry;
            while ((entry = zis.getNextJarEntry()) != null) {
                if (entry.getName().startsWith(ZJAR_SRC_DIR)
                        || entry.getName().endsWith(ZJAR_INF_DIR)
                        || entry.getName().endsWith(ZJAR_INF_DIR + ZJAR_INF_IDX)
                ) {
                    continue;
                }
                // DIR ENTRY
                if (entry.isDirectory()) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                }
                // META-INF/MANIFEST.MF
                else if (entry.getName().equals(META_INF_MANIFEST)) {
                    Manifest manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Boot-Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Main-Class", mainClass);
                        attributes.remove(new Attributes.Name("Boot-Main-Class"));
                    }
                    ZKit.removeKey(attributes);
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    manifest.write(nos);
                }
                // BOOT-INF/classes/**
                else if (entry.getName().startsWith(BOOT_INF_CLASSES)) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    ZBootJarArchiveEntry zBootJarArchiveEntry = new ZBootJarArchiveEntry(entry);
                    boolean filtered = filtrate(zBootJarArchiveEntry);
                    ZDecryptor decryptor = filtered ? zDecryptor : xNopDecryptor;
                    try (OutputStream eos = decryptor.decrypt(key, nos)) {
                        ZKit.transfer(nis, eos);
                    }
                }
                // BOOT-INF/lib/**
                else if (entry.getName().startsWith(BOOT_INF_LIB)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                    zJarDecryptor.decrypt(key, nis, cos);
                    JarArchiveEntry jar = new JarArchiveEntry(entry.getName());
                    jar.setMethod(JarArchiveEntry.STORED);
                    jar.setSize(bos.size());
                    jar.setTime(entry.getTime());
                    jar.setCrc(cos.getChecksum().getValue());
                    zos.putArchiveEntry(jar);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                    ZKit.transfer(bis, nos);
                }
                // OTHER
                else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    ZKit.transfer(nis, nos);
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
