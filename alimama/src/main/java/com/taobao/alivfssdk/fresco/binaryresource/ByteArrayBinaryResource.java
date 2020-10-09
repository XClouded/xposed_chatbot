package com.taobao.alivfssdk.fresco.binaryresource;

import com.taobao.alivfssdk.fresco.common.internal.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayBinaryResource implements BinaryResource {
    private byte[] mBytes;

    public ByteArrayBinaryResource() {
    }

    public ByteArrayBinaryResource(byte[] bArr) {
        this.mBytes = (byte[]) Preconditions.checkNotNull(bArr);
    }

    public void setBytes(byte[] bArr) {
        this.mBytes = bArr;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public long size() {
        return (long) this.mBytes.length;
    }

    public InputStream openStream() throws IOException {
        return new ByteArrayInputStream(this.mBytes);
    }

    public byte[] read() {
        return this.mBytes;
    }
}
