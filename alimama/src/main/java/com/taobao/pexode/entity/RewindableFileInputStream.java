package com.taobao.pexode.entity;

import androidx.annotation.NonNull;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class RewindableFileInputStream extends RewindableInputStream {
    private boolean mCanReposition;
    private FileChannel mChannel;
    private FileDescriptor mFD;
    private long mInitialPosition;

    public RewindableFileInputStream(@NonNull FileInputStream fileInputStream, int i) {
        super(fileInputStream, 0);
        this.mChannel = fileInputStream.getChannel();
        try {
            this.mFD = fileInputStream.getFD();
        } catch (IOException unused) {
        }
        this.mInitialPosition = -1;
        if (this.mChannel != null) {
            try {
                this.mInitialPosition = this.mChannel.position();
            } catch (IOException unused2) {
            }
        }
        this.mCanReposition = reposition((IOException[]) null);
        if (!this.mCanReposition) {
            setBufferSize(i);
        } else if (this.mFD != null) {
            resetInputType(2);
        }
    }

    private boolean reposition(IOException[] iOExceptionArr) {
        if (this.mInitialPosition < 0) {
            return false;
        }
        try {
            this.mChannel.position(this.mInitialPosition);
            return true;
        } catch (IOException e) {
            if (iOExceptionArr == null || iOExceptionArr.length <= 0) {
                return false;
            }
            iOExceptionArr[0] = e;
            return false;
        }
    }

    public FileDescriptor getFD() {
        return this.mFD;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.mCanReposition) {
            return this.mHostStream.read(bArr, i, i2);
        }
        return super.read(bArr, i, i2);
    }

    public int read() throws IOException {
        if (this.mCanReposition) {
            return this.mHostStream.read();
        }
        return super.read();
    }

    public void rewind() throws IOException {
        if (!this.mCanReposition) {
            super.rewind();
        } else if (!this.mClosed) {
            IOException[] iOExceptionArr = new IOException[1];
            if (!reposition(iOExceptionArr)) {
                IOException iOException = iOExceptionArr[0];
                StringBuilder sb = new StringBuilder();
                sb.append("cannot rewind cause file stream reposition(");
                sb.append(this.mInitialPosition);
                sb.append(":");
                sb.append(this.mFD);
                sb.append(") failed, detail=");
                sb.append(iOException != null ? iOException.getMessage() : BuildConfig.buildJavascriptFrameworkVersion);
                sb.append(Operators.AND_NOT);
                throw new IOException(sb.toString());
            }
        } else {
            throw new IOException("cannot rewind cause file stream has been closed!");
        }
    }

    public void rewindAndSetBufferSize(int i) throws IOException {
        rewind();
        setBufferSize(i);
    }

    public int getBufferLength() {
        try {
            if (this.mChannel.size() > 0) {
                return (int) this.mChannel.size();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getBufferLength();
    }
}
