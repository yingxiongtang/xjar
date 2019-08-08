package com.zooocloud.dir;

import com.zooocloud.ZEntryFilter;
import com.zooocloud.filter.ZAntEntryFilter;

import java.io.File;

/**
 * 文件记录Ant表达式过滤器
 *
 * @author CK 364656329@qq.com
 * 2018/12/7 12:05
 */
public class ZDirAntEntryFilter extends ZAntEntryFilter<File> implements ZEntryFilter<File> {

    public ZDirAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(File entry) {
        return entry.getName();
    }

}
