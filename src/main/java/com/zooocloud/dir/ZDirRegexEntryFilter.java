package com.zooocloud.dir;

import com.zooocloud.ZEntryFilter;
import com.zooocloud.filter.ZRegexEntryFilter;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件记录正则表达式过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:04
 */
public class ZDirRegexEntryFilter extends ZRegexEntryFilter<File> implements ZEntryFilter<File> {

    public ZDirRegexEntryFilter(String regex) {
        super(regex);
    }

    public ZDirRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(File entry) {
        return entry.getName();
    }
}
