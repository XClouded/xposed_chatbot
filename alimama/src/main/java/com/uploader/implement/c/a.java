package com.uploader.implement.c;

import com.taobao.weex.el.parse.Operators;
import com.uploader.export.TaskError;

/* compiled from: RetryableTaskError */
public class a extends TaskError {
    public boolean a;

    public a(String str, String str2, String str3, boolean z) {
        this.code = str;
        this.subcode = str2;
        this.info = str3;
        this.a = z;
    }

    public String toString() {
        return "[retryable:" + this.a + " code:" + this.code + " subcode:" + this.subcode + " info:" + this.info + Operators.ARRAY_END_STR;
    }
}
