package com.zooocloud.jar;

import com.zooocloud.ZDecryptor;
import com.zooocloud.ZEncryptor;
import com.zooocloud.ZKit;
import com.zooocloud.key.ZKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

/**
 * JAR包类加载器
 *
 * @author CK 364656329@qq.com
 * 2018/11/25 22:07
 */
public class ZJarClassLoader extends URLClassLoader {
    private final ZJarURLHandler zJarURLHandler;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public ZJarClassLoader(URL[] urls, ClassLoader parent, ZDecryptor zDecryptor, ZEncryptor zEncryptor, ZKey zKey) throws Exception {
        super(urls, parent);
        this.zJarURLHandler = new ZJarURLHandler(zDecryptor, zEncryptor, zKey, this);
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            return null;
        }
        try {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), zJarURLHandler);
        } catch (MalformedURLException e) {
            return url;
        }
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        Enumeration<URL> enumeration = super.findResources(name);
        if (enumeration == null) {
            return null;
        }
        return new ZJarEnumeration(enumeration);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            URL resource = findResource(name.replace('.', '/') + ".class");
            if (resource == null) {
                throw new ClassNotFoundException(name, e);
            }
            try (InputStream in = resource.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ZKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Throwable t) {
                throw new ClassNotFoundException(name, t);
            }
        }
    }

    private class ZJarEnumeration implements Enumeration<URL> {
        private final Enumeration<URL> enumeration;

        ZJarEnumeration(Enumeration<URL> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }

        @Override
        public URL nextElement() {
            URL url = enumeration.nextElement();
            if (url == null) {
                return null;
            }
            try {
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), zJarURLHandler);
            } catch (MalformedURLException e) {
                return url;
            }
        }
    }
}
