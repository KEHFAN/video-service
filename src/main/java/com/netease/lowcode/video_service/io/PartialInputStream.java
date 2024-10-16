package com.netease.lowcode.video_service.io;

import java.io.IOException;
import java.io.InputStream;

public class PartialInputStream extends InputStream {

    private final InputStream inputStream;
    private final long start;
    private final long end;
    private long position;

    public PartialInputStream(InputStream inputStream, long start, long end) {
        this.inputStream = inputStream;
        this.start = start;
        this.end = end;
    }

    @Override
    public int read() throws IOException {
        if (position > end) {
            return -1;
        }
        position++;
        return inputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (position > end) {
            return -1;
        }

        int readLen = (int) Math.min(len, end - position + 1);

        int bytes = inputStream.read(b, off, readLen);
        position += bytes;

        return bytes;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
