package com.zooocloud.jar;

import com.zooocloud.ZEntryFilter;
import com.zooocloud.filter.ZAntEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Jar记录Ant表达式过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:05
 */
public class ZJarAntEntryFilter extends ZAntEntryFilter<JarArchiveEntry> implements ZEntryFilter<JarArchiveEntry> {

    public ZJarAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(JarArchiveEntry entry) {
        return entry.getName();
    }

}
