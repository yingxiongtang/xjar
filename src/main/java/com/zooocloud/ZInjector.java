package com.zooocloud;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * ZJAR API
 *
 * @author CK 364656329@qq.com
 * 2018/11/25 10:34
 */
public class ZInjector {

    /**
     * 往JAR包中注入ZJar框架的classes
     *
     * @param zos jar包输出流
     * @throws IOException I/O 异常
     */
    public static void inject(JarArchiveOutputStream zos) throws IOException {
        Set<String> directories = new HashSet<>();
        Enumeration<Resource> resources = Loaders.ant().load("com/zooocloud/**");
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            System.out.println(name + " ------------------------------------------");
            if(!name.contains("/Z")) continue;
            String directory = name.substring(0, name.lastIndexOf('/') + 1);
            if (directories.add(directory)) {
                JarArchiveEntry xDirEntry = new JarArchiveEntry(directory);
                xDirEntry.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(xDirEntry);
                zos.closeArchiveEntry();
            }
            JarArchiveEntry zJarEntry = new JarArchiveEntry(name);
            zJarEntry.setTime(System.currentTimeMillis());
            zos.putArchiveEntry(zJarEntry);
            try (InputStream ris = resource.getInputStream()) {
                ZKit.transfer(ris, zos);
            }
            zos.closeArchiveEntry();
        }
    }

}
