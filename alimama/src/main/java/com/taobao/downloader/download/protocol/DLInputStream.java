package com.taobao.downloader.download.protocol;

public interface DLInputStream {
    void close() throws Exception;

    int read(byte[] bArr) throws Exception;
}
