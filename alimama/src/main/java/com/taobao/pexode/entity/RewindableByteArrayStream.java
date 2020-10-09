package com.taobao.pexode.entity;

import java.io.FileDescriptor;
import java.io.IOException;
import kotlin.UByte;

public class RewindableByteArrayStream extends RewindableStream {
    private byte[] buf;
    private int count;
    private final int mInitialOffset;
    private int mark;
    private int pos;

    public void close() throws IOException {
    }

    public FileDescriptor getFD() {
        return null;
    }

    public RewindableByteArrayStream(byte[] bArr, int i, int i2) {
        super(1);
        this.buf = bArr;
        this.pos = i;
        this.mark = i;
        int i3 = i2 + i;
        this.count = i3 > this.buf.length ? this.buf.length : i3;
        this.mInitialOffset = i;
    }

    public void rewind() {
        this.pos = this.mInitialOffset;
    }

    public void rewindAndSetBufferSize(int i) {
        rewind();
    }

    public byte[] getBuffer() {
        return this.buf;
    }

    public int getBufferOffset() {
        return this.mInitialOffset;
    }

    public int getBufferLength() {
        return this.count;
    }

    public synchronized int available() {
        return this.count - this.pos;
    }

    public synchronized void mark(int i) {
        this.mark = this.pos;
    }

    public synchronized int read() {
        byte b;
        if (this.pos < this.count) {
            byte[] bArr = this.buf;
            int i = this.pos;
            this.pos = i + 1;
            b = bArr[i] & UByte.MAX_VALUE;
        } else {
            b = -1;
        }
        return b;
    }

    public synchronized int read(byte[] bArr, int i, int i2) {
        if (this.pos >= this.count) {
            return -1;
        }
        if (i2 == 0) {
            return 0;
        }
        if (this.count - this.pos < i2) {
            i2 = this.count - this.pos;
        }
        System.arraycopy(this.buf, this.pos, bArr, i, i2);
        this.pos += i2;
        return i2;
    }

    public synchronized void reset() {
        this.pos = this.mark;
    }

    public synchronized long skip(long j) {
        if (j <= 0) {
            return 0;
        }
        int i = this.pos;
        this.pos = ((long) (this.count - this.pos)) < j ? this.count : (int) (((long) this.pos) + j);
        return (long) (this.pos - i);
    }
}
