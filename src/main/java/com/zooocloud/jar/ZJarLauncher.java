package com.zooocloud.jar;

import com.zooocloud.ZConstants;
import com.zooocloud.ZLauncher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * JAR包启动器
 *
 * @author CK 364656329@qq.com
 * 2018/11/25 18:41
 */
public class ZJarLauncher implements ZConstants {
    private final ZLauncher zLauncher;

    public ZJarLauncher(String... args) throws Exception {
        this.zLauncher = new ZLauncher(args);
    }

    public static void main(String... args) throws Exception {
        new ZJarLauncher(args).launch();
    }

    public void launch() throws Exception {
        ZJarClassLoader zJarClassLoader;

        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            zJarClassLoader = new ZJarClassLoader(urlClassLoader.getURLs(), classLoader.getParent(), zLauncher.zDecryptor, zLauncher.zEncryptor, zLauncher.zKey);
        } else {
            ProtectionDomain domain = this.getClass().getProtectionDomain();
            CodeSource source = domain.getCodeSource();
            URI location = (source == null ? null : source.getLocation().toURI());
            String path = (location == null ? null : location.getSchemeSpecificPart());
            if (path == null) {
                throw new IllegalStateException("Unable to determine code source archive");
            }
            File jar = new File(path);
            URL url = jar.toURI().toURL();
            zJarClassLoader = new ZJarClassLoader(new URL[]{url}, classLoader.getParent(), zLauncher.zDecryptor, zLauncher.zEncryptor, zLauncher.zKey);
        }

        Thread.currentThread().setContextClassLoader(zJarClassLoader);
        ProtectionDomain domain = this.getClass().getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URI location = source.getLocation().toURI();
        String filepath = location.getSchemeSpecificPart();
        File file = new File(filepath);
        JarFile jar = new JarFile(file, false);
        Manifest manifest = jar.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        String jarMainClass = attributes.getValue("Jar-Main-Class");
        Class<?> mainClass = zJarClassLoader.loadClass(jarMainClass);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        mainMethod.invoke(null, new Object[]{zLauncher.args});
    }

}
