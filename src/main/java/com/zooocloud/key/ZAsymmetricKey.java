package com.zooocloud.key;

/**
 * 非对称密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public interface ZAsymmetricKey extends ZKey {

    /**
     * @return 公钥
     */
    byte[] getPublicKey();

    /**
     * @return 私钥
     */
    byte[] getPrivateKey();

}
