package com.zooocloud.zip;

import com.zooocloud.ZEntryFilter;
import com.zooocloud.filter.ZRegexEntryFilter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.util.regex.Pattern;

/**
 * Zip记录正则表达式过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:04
 */
public class ZZipRegexEntryFilter extends ZRegexEntryFilter<ZipArchiveEntry> implements ZEntryFilter<ZipArchiveEntry> {

    public ZZipRegexEntryFilter(String regex) {
        super(regex);
    }

    public ZZipRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(ZipArchiveEntry entry) {
        return entry.getName();
    }
}
