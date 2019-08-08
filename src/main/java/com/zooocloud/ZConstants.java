package com.zooocloud;

/**
 * 常量表
 *
 * @author CK 364656329@qq.com
 * 2018/11/24 9:17
 */
public interface ZConstants {
    String BOOT_INF_CLASSES = "BOOT-INF/classes/";
    String BOOT_INF_LIB = "BOOT-INF/lib/";

    String WEB_INF_CLASSES = "WEB-INF/classes/";
    String WEB_INF_LIB = "WEB-INF/lib/";

    String META_INF_MANIFEST = "META-INF/MANIFEST.MF";
    String ZJAR_SRC_DIR = ZConstants.class.getPackage().getName().replace('.', '/') + "/";
    String ZJAR_INF_DIR = "ZJAR-INF/";
    String ZJAR_INF_IDX = "INDEXES.IDX";
    String CRLF = System.getProperty("line.separator");

    String ZJAR_ALGORITHM = "--zjar.algorithm=";
    String ZJAR_KEYSIZE = "--zjar.keysize=";
    String ZJAR_IVSIZE = "--zjar.ivsize=";
    String ZJAR_PASSWORD = "--zjar.password=";
    String ZJAR_KEYFILE = "--zjar.keyfile=";

    String ZJAR_ALGORITHM_KEY = "ZJar-Algorithm";
    String ZJAR_KEYSIZE_KEY = "ZJar-Keysize";
    String ZJAR_IVSIZE_KEY = "ZJar-Ivsize";
    // password for maven
    String ZJAR_PASSWORD_KEY = "ZJar-Pid";

    String ZJAR_KEY_ALGORITHM = "algorithm";
    String ZJAR_KEY_KEYSIZE = "keysize";
    String ZJAR_KEY_IVSIZE = "ivsize";
    String ZJAR_KEY_PASSWORD = "password";
    String ZJAR_KEY_HOLD = "hold";

    String DEFAULT_ALGORITHM = "AES";
    int DEFAULT_KEYSIZE = 128;
    int DEFAULT_IVSIZE = 128;

    // 保留密钥在 META-INF/MANIFEST.MF 中，启动时无需输入密钥。
    int FLAG_DANGER = 1;
    // 危险模式：保留密钥
    int MODE_DANGER = FLAG_DANGER;
    // 普通模式
    int MODE_NORMAL = 0;

}
