package com.zooocloud.boot;

import com.zooocloud.ZLauncher;
import org.springframework.boot.loader.JarLauncher;

import java.net.URL;

/**
 * Spring-Boot Jar 启动器
 *
 * @author CK 364656329@qq.com
 * 2018/11/23 23:06
 */
public class ZJarLauncher extends JarLauncher {
    private final ZLauncher zLauncher;

    public ZJarLauncher(String... args) throws Exception {
        this.zLauncher = new ZLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new ZJarLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(zLauncher.args);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new ZBootClassLoader(urls, this.getClass().getClassLoader(), zLauncher.zDecryptor, zLauncher.zEncryptor, zLauncher.zKey);
    }

}
