package com.zooocloud.jar;

import com.zooocloud.ZEntryFilter;
import com.zooocloud.filter.ZRegexEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.util.regex.Pattern;

/**
 * Jar记录正则表达式过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:04
 */
public class ZJarRegexEntryFilter extends ZRegexEntryFilter<JarArchiveEntry> implements ZEntryFilter<JarArchiveEntry> {

    public ZJarRegexEntryFilter(String regex) {
        super(regex);
    }

    public ZJarRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(JarArchiveEntry entry) {
        return entry.getName();
    }
}
