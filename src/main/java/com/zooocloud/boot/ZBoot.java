package com.zooocloud.boot;

import com.zooocloud.*;
import com.zooocloud.key.ZKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.*;
import java.util.zip.Deflater;

/**
 * Spring-Boot JAR包加解密工具类，在不提供过滤器的情况下会加密BOOT-INF/下的所有资源，及包括项目本身的资源和依赖jar资源。
 *
 * @author CK 364656329@qq.com
 * 2018/11/26 11:11
 */
public class ZBoot implements ZConstants {
    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param zKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, ZKey zKey) throws Exception {
        encrypt(new File(src), new File(dest), zKey);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param zKey 密钥
     * @param mode 加密模式
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, ZKey zKey, int mode) throws Exception {
        encrypt(new File(src), new File(dest), zKey, mode);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param zKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, ZKey zKey) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, zKey);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param zKey 密钥
     * @param mode 加密模式
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, ZKey zKey, int mode) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, zKey, mode);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in   原文包输入流
     * @param out  加密包输出流
     * @param zKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, ZKey zKey) throws Exception {
        ZBootEncryptor zBootEncryptor = new ZBootEncryptor(new ZJdkEncryptor(zKey.getAlgorithm()));
        zBootEncryptor.encrypt(zKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in   原文包输入流
     * @param out  加密包输出流
     * @param zKey 密钥
     * @param mode 加密模式
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, ZKey zKey, int mode) throws Exception {
        ZBootEncryptor zBootEncryptor = new ZBootEncryptor(new ZJdkEncryptor(zKey.getAlgorithm()), Deflater.DEFLATED, mode);
        zBootEncryptor.encrypt(zKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param zKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, ZKey zKey, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(new File(src), new File(dest), zKey, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param zKey   密钥
     * @param mode   加密模式
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, ZKey zKey, int mode, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(new File(src), new File(dest), zKey, mode, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param zKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, ZKey zKey, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, zKey, filter);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param zKey   密钥
     * @param mode   加密模式
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, ZKey zKey, int mode, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, zKey, mode, filter);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in     原文包输入流
     * @param out    加密包输出流
     * @param zKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, ZKey zKey, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        ZBootEncryptor zBootEncryptor = new ZBootEncryptor(new ZJdkEncryptor(zKey.getAlgorithm()), filter);
        zBootEncryptor.encrypt(zKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in     原文包输入流
     * @param out    加密包输出流
     * @param zKey   密钥
     * @param mode   加密模式
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, ZKey zKey, int mode, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        ZBootEncryptor zBootEncryptor = new ZBootEncryptor(new ZJdkEncryptor(zKey.getAlgorithm()), Deflater.DEFLATED, mode, filter);
        zBootEncryptor.encrypt(zKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, int keysize) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, int keysize, int ivsize) throws Exception {
        encrypt(new File(src), new File(dest), password, algorithm, keysize, ivsize);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, password, algorithm, keysize, ivsize);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password) throws Exception {
        encrypt(in, out, password, DEFAULT_ALGORITHM);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm) throws Exception {
        encrypt(in, out, password, algorithm, DEFAULT_KEYSIZE);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize) throws Exception {
        encrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize) throws Exception {
        ZBootEncryptor zBootEncryptor = new ZBootEncryptor(new ZJdkEncryptor(algorithm));
        ZKey zKey = ZKit.key(algorithm, keysize, ivsize, password);
        zBootEncryptor.encrypt(zKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, int keysize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, int keysize, int ivsize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(new File(src), new File(dest), password, algorithm, keysize, ivsize, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, password, algorithm, keysize, ivsize, filter);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter    过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        ZBootEncryptor zBootEncryptor = new ZBootEncryptor(new ZJdkEncryptor(algorithm), filter);
        ZKey zKey = ZKit.key(algorithm, keysize, ivsize, password);
        zBootEncryptor.encrypt(zKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param zKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, ZKey zKey) throws Exception {
        decrypt(new File(src), new File(dest), zKey);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param zKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, ZKey zKey) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(in, out, zKey);
        }
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in   加密包输入流
     * @param out  解密包输出流
     * @param zKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, ZKey zKey) throws Exception {
        ZBootDecryptor zBootDecryptor = new ZBootDecryptor(new ZJdkDecryptor(zKey.getAlgorithm()));
        zBootDecryptor.decrypt(zKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param zKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, ZKey zKey, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(new File(src), new File(dest), zKey, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param zKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, ZKey zKey, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(in, out, zKey, filter);
        }
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in     加密包输入流
     * @param out    解密包输出流
     * @param zKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, ZKey zKey, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        ZBootDecryptor zBootDecryptor = new ZBootDecryptor(new ZJdkDecryptor(zKey.getAlgorithm()), filter);
        zBootDecryptor.decrypt(zKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, int keysize) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, int keysize, int ivsize) throws Exception {
        decrypt(new File(src), new File(dest), password, algorithm, keysize, ivsize);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(in, out, password, algorithm, keysize, ivsize);
        }
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password) throws Exception {
        decrypt(in, out, password, DEFAULT_ALGORITHM);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm) throws Exception {
        decrypt(in, out, password, algorithm, DEFAULT_KEYSIZE);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize) throws Exception {
        decrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize) throws Exception {
        ZBootDecryptor zBootDecryptor = new ZBootDecryptor(new ZJdkDecryptor(algorithm));
        ZKey zKey = ZKit.key(algorithm, keysize, ivsize, password);
        zBootDecryptor.decrypt(zKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, int keysize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, int keysize, int ivsize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(new File(src), new File(dest), password, algorithm, keysize, ivsize, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(in, out, password, algorithm, keysize, ivsize, filter);
        }
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter    过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize, ZEntryFilter<JarArchiveEntry> filter) throws Exception {
        ZBootDecryptor zBootDecryptor = new ZBootDecryptor(new ZJdkDecryptor(algorithm), filter);
        ZKey zKey = ZKit.key(algorithm, keysize, ivsize, password);
        zBootDecryptor.decrypt(zKey, in, out);
    }

}
