package com.zooocloud.boot;

import com.zooocloud.ZLauncher;
import org.springframework.boot.loader.PropertiesLauncher;
import org.springframework.boot.loader.archive.Archive;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Spring-Boot Properties 启动器
 *
 * @author CK 364656329@qq.com
 * 2019/4/14 10:26
 */
public class ZExtLauncher extends PropertiesLauncher {
    private final ZLauncher zLauncher;

    public ZExtLauncher(String... args) throws Exception {
        this.zLauncher = new ZLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new ZExtLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(zLauncher.args);
    }

    @Override
    protected ClassLoader createClassLoader(List<Archive> archives) throws Exception {
        URLClassLoader classLoader = (URLClassLoader) super.createClassLoader(archives);
        URL[] urls = classLoader.getURLs();
        return new ZBootClassLoader(urls, this.getClass().getClassLoader(), zLauncher.zDecryptor, zLauncher.zEncryptor, zLauncher.zKey);
    }
}
