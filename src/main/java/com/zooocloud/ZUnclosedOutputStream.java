package com.zooocloud;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 不关闭的输出流
 *
 * @author CK 364656329@qq.com
 * 2018/11/22 16:32
 */
public class ZUnclosedOutputStream extends OutputStream {
    private final OutputStream out;

    public ZUnclosedOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() {
    }
}
