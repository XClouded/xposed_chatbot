package com.taobao.phenix.entity;

import android.util.TypedValue;
import com.taobao.rxm.common.Releasable;
import java.io.IOException;
import java.io.InputStream;

public class ResponseData implements Releasable {
    public static final int TYPE_BYTE_ARRAY = 1;
    public static final int TYPE_INPUT_STREAM = 3;
    public final byte[] bytes;
    public final InputStream inputStream;
    public final int length;
    public final int offset;
    public TypedValue resourceValue;
    public final int type;

    public ResponseData(byte[] bArr, int i, int i2) {
        this(1, bArr, i, (InputStream) null, i2, (TypedValue) null);
    }

    public ResponseData(InputStream inputStream2, int i) {
        this(3, (byte[]) null, 0, inputStream2, i, (TypedValue) null);
    }

    public ResponseData(InputStream inputStream2, int i, TypedValue typedValue) {
        this(3, (byte[]) null, 0, inputStream2, i, typedValue);
    }

    protected ResponseData(int i, byte[] bArr, int i2, InputStream inputStream2, int i3, TypedValue typedValue) {
        this.type = i;
        this.bytes = bArr;
        this.offset = i2;
        this.inputStream = inputStream2;
        this.length = i3;
        this.resourceValue = typedValue;
    }

    public void release() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException unused) {
            }
        }
    }
}
