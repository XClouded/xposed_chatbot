package com.huawei.hms.update.a;

import com.huawei.hms.c.e;
import com.huawei.hms.support.log.a;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/* compiled from: RandomFileOutputStream */
public class b extends OutputStream {
    private RandomAccessFile a;

    public b(File file, int i) {
        try {
            this.a = new RandomAccessFile(file, "rwd");
            this.a.setLength((long) i);
        } catch (FileNotFoundException unused) {
            a.d("RandomFileOutputStream", "create  file stream failed");
        } catch (IOException unused2) {
            e.a((Closeable) this.a);
            a.d("RandomFileOutputStream", "create  file stream failed");
        }
    }

    public void close() throws IOException {
        this.a.close();
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.a.write(bArr, i, i2);
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i}, 0, 1);
    }

    public void a(long j) throws IOException {
        this.a.seek(j);
    }
}
