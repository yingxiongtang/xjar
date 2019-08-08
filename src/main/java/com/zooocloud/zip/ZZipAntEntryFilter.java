package com.zooocloud.zip;

import com.zooocloud.ZEntryFilter;
import com.zooocloud.filter.ZAntEntryFilter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

/**
 * Zip记录Ant表达式过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:05
 */
public class ZZipAntEntryFilter extends ZAntEntryFilter<ZipArchiveEntry> implements ZEntryFilter<ZipArchiveEntry> {

    public ZZipAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(ZipArchiveEntry entry) {
        return entry.getName();
    }

}
