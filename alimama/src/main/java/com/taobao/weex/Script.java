package com.taobao.weex;

import android.text.TextUtils;

public class Script {
    private byte[] mBinary;
    private String mContent;

    public Script(String str) {
        this.mContent = str;
    }

    public Script(byte[] bArr) {
        this.mBinary = bArr;
    }

    public String getContent() {
        return this.mContent;
    }

    public byte[] getBinary() {
        return this.mBinary;
    }

    public int length() {
        if (this.mContent != null) {
            return this.mContent.length();
        }
        if (this.mBinary != null) {
            return this.mBinary.length;
        }
        return 0;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.mContent) && (this.mBinary == null || this.mBinary.length == 0);
    }
}
