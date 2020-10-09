package com.taobao.phenix.compat.mtop;

import android.os.RemoteException;
import android.text.TextUtils;
import anetwork.channel.aidl.ParcelableInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MtopResponseInputStream extends InputStream {
    ParcelableInputStream mParcelableInputStream;

    public MtopResponseInputStream(ParcelableInputStream parcelableInputStream) {
        this.mParcelableInputStream = parcelableInputStream;
    }

    private boolean isReadTimeoutException(Exception exc) {
        return !TextUtils.isEmpty(exc.getMessage()) && exc.getMessage().contains("await timeout");
    }

    public int read() throws IOException {
        try {
            return this.mParcelableInputStream.readByte();
        } catch (RemoteException e) {
            throw new IOException(e);
        } catch (RuntimeException e2) {
            if (isReadTimeoutException(e2)) {
                throw new MtopReadTimeoutException();
            }
            throw e2;
        }
    }

    public int read(byte[] bArr) throws IOException {
        try {
            return this.mParcelableInputStream.read(bArr);
        } catch (RemoteException e) {
            throw new IOException(e);
        } catch (RuntimeException e2) {
            if (isReadTimeoutException(e2)) {
                throw new MtopReadTimeoutException();
            }
            throw e2;
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        try {
            return this.mParcelableInputStream.readBytes(bArr, i, i2);
        } catch (RemoteException e) {
            throw new IOException(e);
        } catch (RuntimeException e2) {
            if (isReadTimeoutException(e2)) {
                throw new MtopReadTimeoutException();
            }
            throw e2;
        }
    }

    public void close() throws IOException {
        try {
            this.mParcelableInputStream.close();
        } catch (RemoteException e) {
            throw new IOException(e);
        }
    }
}
