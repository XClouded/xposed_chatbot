package com.huawei.updatesdk.sdk.service.download;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class h extends RandomAccessFile {

    public static class a extends IOException {
        public a(Exception exc) {
            super(exc);
        }
    }

    public h(String str, String str2) throws FileNotFoundException {
        super(str, str2);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        try {
            super.write(bArr, i, i2);
        } catch (Exception e) {
            throw new a(e);
        }
    }
}
