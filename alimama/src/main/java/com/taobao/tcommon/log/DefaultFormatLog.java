package com.taobao.tcommon.log;

import android.util.Log;

public class DefaultFormatLog extends FastFormatLog {
    private int mMinLevel = 5;

    public boolean isLoggable(int i) {
        return i >= this.mMinLevel;
    }

    public void setMinLevel(int i) {
        this.mMinLevel = i;
    }

    public void v(String str, String str2, Object... objArr) {
        Log.v(str, fastFormat(str2, objArr));
    }

    public void d(String str, String str2, Object... objArr) {
        Log.d(str, fastFormat(str2, objArr));
    }

    public void i(String str, String str2, Object... objArr) {
        Log.i(str, fastFormat(str2, objArr));
    }

    public void w(String str, String str2, Object... objArr) {
        Log.w(str, fastFormat(str2, objArr));
    }

    public void e(String str, String str2, Object... objArr) {
        Log.e(str, fastFormat(str2, objArr));
    }

    public void e(int i, String str, String str2) {
        Log.e(str, str2);
    }
}
