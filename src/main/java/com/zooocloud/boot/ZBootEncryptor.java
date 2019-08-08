package com.zooocloud.boot;

import com.zooocloud.*;
import com.zooocloud.jar.ZJarAllEntryFilter;
import com.zooocloud.jar.ZJarEncryptor;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * Spring-Boot JAR包加密器
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 15:27
 */
public class ZBootEncryptor extends ZEntryEncryptor<JarArchiveEntry> implements ZEncryptor, ZConstants {
    private final Map<String, String> map = new HashMap<>();

    {
        final String jarLauncher = "org.springframework.boot.loader.JarLauncher";
        final String warLauncher = "org.springframework.boot.loader.WarLauncher";
        final String extLauncher = "org.springframework.boot.loader.PropertiesLauncher";
        map.put(jarLauncher, "com.zooocloud.boot.ZJarLauncher");
        map.put(warLauncher, "com.zooocloud.boot.ZWarLauncher");
        map.put(extLauncher, "com.zooocloud.boot.ZExtLauncher");
    }

    private final int level;
    private final int mode;

    public ZBootEncryptor(ZEncryptor zEncryptor) {
        this(zEncryptor, new ZJarAllEntryFilter());
    }

    public ZBootEncryptor(ZEncryptor zEncryptor, ZEntryFilter<JarArchiveEntry> filter) {
        this(zEncryptor, Deflater.DEFLATED, filter);
    }

    public ZBootEncryptor(ZEncryptor zEncryptor, int level) {
        this(zEncryptor, level, new ZJarAllEntryFilter());
    }

    public ZBootEncryptor(ZEncryptor zEncryptor, int level, ZEntryFilter<JarArchiveEntry> filter) {
        this(zEncryptor, level, MODE_NORMAL, filter);
    }

    public ZBootEncryptor(ZEncryptor zEncryptor, int level, int mode) {
        this(zEncryptor, level, mode, new ZJarAllEntryFilter());
    }

    public ZBootEncryptor(ZEncryptor zEncryptor, int level, int mode, ZEntryFilter<JarArchiveEntry> filter) {
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
            ZJarEncryptor zJarEncryptor = new ZJarEncryptor(zEncryptor, level, filter);
            JarArchiveEntry entry;
            Manifest manifest = null;
            while ((entry = zis.getNextJarEntry()) != null) {
                System.out.println("======================= " + entry.getName());

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
                    manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Boot-Main-Class", mainClass);
                        attributes.putValue("Main-Class", map.get(mainClass));
                    }
                    if ((mode & FLAG_DANGER) == FLAG_DANGER) {
                        ZKit.retainKey(key, attributes);
                    }
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
                    if (filtered) {
                        indexes.add(zBootJarArchiveEntry.getName());
                    }
                    ZEncryptor encryptor = filtered ? zEncryptor : xNopEncryptor;
                    try (OutputStream eos = encryptor.encrypt(key, nos)) {
                        ZKit.transfer(nis, eos);
                    }
                }
                // BOOT-INF/lib/**
                else if (entry.getName().startsWith(BOOT_INF_LIB)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                    zJarEncryptor.encrypt(key, nis, cos);
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

            if (!indexes.isEmpty()) {
                JarArchiveEntry zjarInfDir = new JarArchiveEntry(BOOT_INF_CLASSES + ZJAR_INF_DIR);
                zjarInfDir.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(zjarInfDir);
                zos.closeArchiveEntry();

                JarArchiveEntry zjarInfIdx = new JarArchiveEntry(BOOT_INF_CLASSES + ZJAR_INF_DIR + ZJAR_INF_IDX);
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
