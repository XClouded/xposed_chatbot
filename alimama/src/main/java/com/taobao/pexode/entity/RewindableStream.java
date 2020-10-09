package com.taobao.pexode.entity;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public abstract class RewindableStream extends InputStream {
    private int mInputType;

    public abstract byte[] getBuffer();

    public abstract int getBufferLength();

    public abstract int getBufferOffset();

    public abstract FileDescriptor getFD();

    public abstract void rewind() throws IOException;

    public abstract void rewindAndSetBufferSize(int i) throws IOException;

    public RewindableStream(int i) {
        this.mInputType = i;
    }

    public final int getInputType() {
        return this.mInputType;
    }

    /* access modifiers changed from: protected */
    public void resetInputType(int i) {
        this.mInputType = i;
    }

    public void back2StreamType() {
        this.mInputType = 3;
    }
}
