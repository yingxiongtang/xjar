package com.zooocloud.key;

/**
 * 对称密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public final class ZSymmetricSecureKey extends ZSecureKey implements ZSymmetricKey {
    private static final long serialVersionUID = -2932869368903909669L;

    private final byte[] secretKey;
    private final byte[] iv;

    public ZSymmetricSecureKey(String algorithm, int keysize, int ivsize, String password, byte[] key, byte[] iv) {
        super(algorithm, keysize, ivsize, password);
        this.secretKey = key;
        this.iv = iv;
    }

    public byte[] getEncryptKey() {
        return secretKey;
    }

    public byte[] getDecryptKey() {
        return secretKey;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public byte[] getIvParameter() {
        return iv;
    }

}
