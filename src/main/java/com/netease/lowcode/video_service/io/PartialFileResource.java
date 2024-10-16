package com.netease.lowcode.video_service.io;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.InputStream;

public class PartialFileResource extends FileSystemResource {
    private long start;
    private long end;

    public PartialFileResource(String path, long start, long end) {
        super(path);
        this.start = start;
        this.end = end;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = super.getInputStream();
        inputStream.skip(start);
        return new PartialInputStream(inputStream, start, end);
    }

    @Override
    public long contentLength() throws IOException {
        return end - start + 1;
    }
}
