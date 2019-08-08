package com.zooocloud;

import com.zooocloud.key.ZKey;

import java.io.*;
import java.net.URI;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Spring-Boot 启动器
 *
 * @author CK 364656329@qq.com
 * 2019/4/14 10:28
 */
public class ZLauncher implements ZConstants {
    public final String[] args;
    public final ZDecryptor zDecryptor;
    public final ZEncryptor zEncryptor;
    public final ZKey zKey;

    public ZLauncher(String... args) throws Exception {
        this.args = args;
        String algorithm = DEFAULT_ALGORITHM;
        int keysize = DEFAULT_KEYSIZE;
        int ivsize = DEFAULT_IVSIZE;
        String password = null;
        String keypath = null;
        for (String arg : args) {
            if (arg.toLowerCase().startsWith(ZJAR_ALGORITHM)) {
                algorithm = arg.substring(ZJAR_ALGORITHM.length());
            }
            if (arg.toLowerCase().startsWith(ZJAR_KEYSIZE)) {
                keysize = Integer.valueOf(arg.substring(ZJAR_KEYSIZE.length()));
            }
            if (arg.toLowerCase().startsWith(ZJAR_IVSIZE)) {
                ivsize = Integer.valueOf(arg.substring(ZJAR_IVSIZE.length()));
            }
            if (arg.toLowerCase().startsWith(ZJAR_PASSWORD)) {
                password = arg.substring(ZJAR_PASSWORD.length());
            }
            if (arg.toLowerCase().startsWith(ZJAR_KEYFILE)) {
                keypath = arg.substring(ZJAR_KEYFILE.length());
            }
        }

        ProtectionDomain domain = this.getClass().getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URI location = (source == null ? null : source.getLocation().toURI());
        String filepath = (location == null ? null : location.getSchemeSpecificPart());
        if (filepath != null) {
            File file = new File(filepath);
            JarFile jar = new JarFile(file, false);
            Manifest manifest = jar.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            if (attributes.getValue(ZJAR_ALGORITHM_KEY) != null) {
                algorithm = attributes.getValue(ZJAR_ALGORITHM_KEY);
            }
            if (attributes.getValue(ZJAR_KEYSIZE_KEY) != null) {
                keysize = Integer.valueOf(attributes.getValue(ZJAR_KEYSIZE_KEY));
            }
            if (attributes.getValue(ZJAR_IVSIZE_KEY) != null) {
                ivsize = Integer.valueOf(attributes.getValue(ZJAR_IVSIZE_KEY));
            }
            if (attributes.getValue(ZJAR_PASSWORD_KEY) != null) {
                password = attributes.getValue(ZJAR_PASSWORD_KEY);
            }
        }

        Properties key = null;
        File keyfile = null;
        if (keypath != null) {
            String path = ZKit.absolutize(keypath);
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                keyfile = file;
                try (InputStream in = new FileInputStream(file)) {
                    key = new Properties();
                    key.load(in);
                }
            } else {
                throw new FileNotFoundException("could not find key file at path: " + file.getCanonicalPath());
            }
        } else {
            String path = ZKit.absolutize("zjar.key");
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                keyfile = file;
                try (InputStream in = new FileInputStream(file)) {
                    key = new Properties();
                    key.load(in);
                }
            }
        }

        String hold = null;
        if (key != null) {
            Set<String> names = key.stringPropertyNames();
            for (String name : names) {
                switch (name.toLowerCase()) {
                    case ZJAR_KEY_ALGORITHM:
                        algorithm = key.getProperty(name);
                        break;
                    case ZJAR_KEY_KEYSIZE:
                        keysize = Integer.valueOf(key.getProperty(name));
                        break;
                    case ZJAR_KEY_IVSIZE:
                        ivsize = Integer.valueOf(key.getProperty(name));
                        break;
                    case ZJAR_KEY_PASSWORD:
                        password = key.getProperty(name);
                        break;
                    case ZJAR_KEY_HOLD:
                        hold = key.getProperty(name);
                    default:
                        break;
                }
            }
        }

        // 不保留密钥文件
        if (hold == null || !Arrays.asList("true", "1", "yes", "y").contains(hold.trim().toLowerCase())) {
            if (keyfile != null && keyfile.exists() && !keyfile.delete() && keyfile.exists()) {
                throw new IOException("could not delete key file: " + keyfile.getCanonicalPath());
            }
        }

        if (password == null && System.console() != null) {
            Console console = System.console();
            char[] chars = console.readPassword("password:");
            password = new String(chars);
        }
        if (password == null) {
            System.out.print("password:");
            Scanner scanner = new Scanner(System.in);
            password = scanner.nextLine();
        }
        this.zDecryptor = new ZJdkDecryptor(algorithm);
        this.zEncryptor = new ZJdkEncryptor(algorithm);
        this.zKey = ZKit.key(algorithm, keysize, ivsize, password);
    }

}
