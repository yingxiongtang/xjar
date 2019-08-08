package com.zooocloud.jar;

import com.zooocloud.ZConstants;
import com.zooocloud.ZDecryptor;
import com.zooocloud.ZEncryptor;
import com.zooocloud.key.ZKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 加密的URL处理器
 *
 * @author CK 364656329@qq.com
 * 2018/11/24 13:19
 */
public class ZJarURLHandler extends URLStreamHandler implements ZConstants {
    private final ZDecryptor zDecryptor;
    private final ZEncryptor zEncryptor;
    private final ZKey zKey;
    private final Set<String> indexes;

    public ZJarURLHandler(ZDecryptor zDecryptor, ZEncryptor zEncryptor, ZKey zKey, ClassLoader classLoader) throws Exception {
        this.zDecryptor = zDecryptor;
        this.zEncryptor = zEncryptor;
        this.zKey = zKey;
        this.indexes = new LinkedHashSet<>();
        Enumeration<URL> resources = classLoader.getResources(ZJAR_INF_DIR + ZJAR_INF_IDX);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String url = resource.toString();
            String classpath = url.substring(0, url.lastIndexOf("!/") + 2);
            InputStream in = resource.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            LineNumberReader lnr = new LineNumberReader(isr);
            String name;
            while ((name = lnr.readLine()) != null) indexes.add(classpath + name);
        }
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection urlConnection = new URL(url.toString()).openConnection();
        return indexes.contains(url.toString())
                && urlConnection instanceof JarURLConnection
                ? new ZJarURLConnection((JarURLConnection) urlConnection, zDecryptor, zEncryptor, zKey)
                : urlConnection;
    }

}
