package com.xiaomi.push;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class jj extends jm {
    protected InputStream a = null;

    /* renamed from: a  reason: collision with other field name */
    protected OutputStream f790a = null;

    protected jj() {
    }

    public jj(OutputStream outputStream) {
        this.f790a = outputStream;
    }

    public int a(byte[] bArr, int i, int i2) {
        if (this.a != null) {
            try {
                int read = this.a.read(bArr, i, i2);
                if (read >= 0) {
                    return read;
                }
                throw new jn(4);
            } catch (IOException e) {
                throw new jn(0, (Throwable) e);
            }
        } else {
            throw new jn(1, "Cannot read from null inputStream");
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m527a(byte[] bArr, int i, int i2) {
        if (this.f790a != null) {
            try {
                this.f790a.write(bArr, i, i2);
            } catch (IOException e) {
                throw new jn(0, (Throwable) e);
            }
        } else {
            throw new jn(1, "Cannot write to null outputStream");
        }
    }
}
