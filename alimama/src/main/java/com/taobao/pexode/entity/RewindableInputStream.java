package com.taobao.pexode.entity;

import com.taobao.pexode.DecodeHelper;
import com.taobao.pexode.Pexode;
import com.taobao.tcommon.log.FLog;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public class RewindableInputStream extends RewindableStream {
    private byte[] mBuffer;
    private int mBufferSize;
    private int mBufferedSoFar;
    protected boolean mClosed;
    protected final InputStream mHostStream;
    private final boolean mMarkSupported;
    private int mOffset;
    private boolean mReachedEOF;

    public int getBufferOffset() {
        return 0;
    }

    public FileDescriptor getFD() {
        return null;
    }

    public RewindableInputStream(InputStream inputStream, int i) {
        this(3, inputStream, i);
    }

    public RewindableInputStream(int i, InputStream inputStream, int i2) {
        super(i);
        this.mHostStream = inputStream;
        this.mMarkSupported = this.mHostStream.markSupported();
        setBufferSize(i2);
    }

    /* access modifiers changed from: protected */
    public void setBufferSize(int i) {
        this.mBufferSize = i;
        if (this.mMarkSupported) {
            this.mHostStream.mark(this.mBufferSize);
        }
    }

    public byte[] getBuffer() {
        return this.mBuffer;
    }

    public int getBufferLength() {
        if (this.mBufferedSoFar > 0) {
            return this.mBufferedSoFar;
        }
        return this.mBufferSize;
    }

    public void rewind() throws IOException {
        if (this.mMarkSupported) {
            this.mHostStream.reset();
        } else if (this.mOffset > this.mBufferedSoFar) {
            throw new IOException("cannot rewind cause input stream offset too far");
        }
        this.mOffset = 0;
        this.mReachedEOF = false;
    }

    public void rewindAndSetBufferSize(int i) throws IOException {
        rewind();
        setBufferSize(i);
    }

    private int readFromBuffer(byte[] bArr, int i, int i2) {
        int min = Math.min(i2, this.mBufferedSoFar - this.mOffset);
        System.arraycopy(this.mBuffer, this.mOffset, bArr, i, min);
        this.mOffset += min;
        return min;
    }

    private int bufferAndWriteTo(byte[] bArr, int i, int i2) throws IOException {
        int min = Math.min(i2, this.mBufferSize - this.mBufferedSoFar);
        int i3 = this.mBufferedSoFar + min;
        if (this.mBuffer == null || i3 > this.mBuffer.length) {
            byte[] offerBytes = DecodeHelper.instance().offerBytes(Math.min(i3 + min, this.mBufferSize));
            if (this.mBuffer != null) {
                System.arraycopy(this.mBuffer, 0, offerBytes, 0, this.mBufferedSoFar);
                DecodeHelper.instance().releaseBytes(this.mBuffer);
            }
            this.mBuffer = offerBytes;
        }
        int i4 = 0;
        int i5 = -1;
        while (true) {
            int i6 = this.mOffset;
            int read = this.mHostStream.read(this.mBuffer, this.mOffset, min - i4);
            if (read < 0) {
                this.mReachedEOF = true;
                FLog.d(Pexode.TAG, "bufferAndWriteTo() read stream end -1 now", new Object[0]);
                break;
            }
            if (read > 0) {
                this.mBufferedSoFar += read;
                this.mOffset = this.mBufferedSoFar;
                System.arraycopy(this.mBuffer, i6, bArr, i + i4, read);
            }
            i5 = i4 + read;
            if (i5 == min) {
                break;
            }
            i4 = i5;
        }
        return i5;
    }

    private int readDirectlyFromStream(byte[] bArr, int i, int i2) throws IOException {
        int read = this.mHostStream.read(bArr, i, i2);
        if (read < 0) {
            this.mReachedEOF = true;
            return -1;
        }
        if (read > 0) {
            this.mOffset += read;
            DecodeHelper.instance().releaseBytes(this.mBuffer);
            this.mBuffer = null;
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        int readDirectlyFromStream;
        int bufferAndWriteTo;
        if (bArr == null || i < 0 || i2 <= 0) {
            throw new IOException("read parameters illegal");
        }
        int i4 = -1;
        if (this.mReachedEOF) {
            return -1;
        }
        if (!this.mMarkSupported) {
            if (this.mOffset < this.mBufferedSoFar) {
                i4 = readFromBuffer(bArr, i, i2);
                i3 = i2 - i4;
            } else {
                i3 = i2;
            }
            if (i3 > 0 && this.mBufferedSoFar < this.mBufferSize && (bufferAndWriteTo = bufferAndWriteTo(bArr, (i + i2) - i3, i3)) >= 0) {
                i3 -= bufferAndWriteTo;
                i4 = i4 < 0 ? bufferAndWriteTo : i4 + bufferAndWriteTo;
            }
        } else {
            i3 = i2;
        }
        if (this.mReachedEOF || i3 <= 0 || (readDirectlyFromStream = readDirectlyFromStream(bArr, (i + i2) - i3, i3)) < 0) {
            return i4;
        }
        return i4 < 0 ? readDirectlyFromStream : i4 + readDirectlyFromStream;
    }

    public int read() throws IOException {
        byte[] bArr = new byte[1];
        if (read(bArr, 0, 1) > 0) {
            return bArr[0];
        }
        return -1;
    }

    public void close() throws IOException {
        this.mHostStream.close();
        this.mClosed = true;
    }
}
