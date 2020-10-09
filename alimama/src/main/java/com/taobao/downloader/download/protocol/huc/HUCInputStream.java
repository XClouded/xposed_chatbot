package com.taobao.downloader.download.protocol.huc;

import com.taobao.downloader.download.protocol.DLConfig;
import com.taobao.downloader.download.protocol.DLInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class HUCInputStream implements DLInputStream {
    private InputStream inputStream;

    public HUCInputStream(InputStream inputStream2) {
        this.inputStream = new BufferedInputStream(inputStream2, DLConfig.LARGE_BUFFER_SIZE);
    }

    public int read(byte[] bArr) throws Exception {
        return this.inputStream.read(bArr, 0, bArr.length);
    }

    public void close() throws Exception {
        this.inputStream.close();
    }
}
