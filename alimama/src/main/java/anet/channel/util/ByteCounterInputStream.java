package anet.channel.util;

import java.io.IOException;
import java.io.InputStream;

public class ByteCounterInputStream extends InputStream {
    private long cnt = 0;
    private InputStream is = null;

    public ByteCounterInputStream(InputStream inputStream) {
        if (inputStream != null) {
            this.is = inputStream;
            return;
        }
        throw new NullPointerException("input stream cannot be null");
    }

    public long getReadByteCount() {
        return this.cnt;
    }

    public int read() throws IOException {
        this.cnt++;
        return this.is.read();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.is.read(bArr, i, i2);
        if (read != -1) {
            this.cnt += (long) read;
        }
        return read;
    }
}
