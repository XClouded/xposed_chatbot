package mtopsdk.network.domain;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RequestBody {
    public long contentLength() {
        return -1;
    }

    public abstract String contentType();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    public static RequestBody create(final String str, final byte[] bArr) throws Exception {
        if (bArr != null) {
            return new RequestBody() {
                public String contentType() {
                    return str;
                }

                public long contentLength() {
                    return (long) bArr.length;
                }

                public void writeTo(OutputStream outputStream) throws IOException {
                    outputStream.write(bArr);
                }
            };
        }
        throw new NullPointerException("content == null");
    }
}
