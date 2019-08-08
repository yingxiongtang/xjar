package com.zooocloud;

import com.zooocloud.boot.ZBoot;
import com.zooocloud.jar.ZJarAntEntryFilter;
import com.zooocloud.key.ZKey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class Test {
    public static void main(String[] args) throws Exception {
        String password = "mypassword";
        ZEntryFilter or = ZKit.or()
                .mix(new ZJarAntEntryFilter("com/zooocloud/**"))
                .mix(new ZJarAntEntryFilter("com/matrixzoo/**"));
        byte[] bytes = getBytesByFile("E:\\public\\encryptionC\\run\\encryptJar\\license_window.lic");
        String pass = parseByte2HexStr(bytes);
        System.out.println(pass);
        ZKey zKey = ZKit.key(pass);
        ZBoot.encrypt("E:\\tmp\\run\\sbtest.jar", "E:\\tmp\\run\\sbtest-e-5.jar", zKey, ZConstants.MODE_DANGER, or);
    }

    static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
