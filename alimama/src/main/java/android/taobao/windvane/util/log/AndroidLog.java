package android.taobao.windvane.util.log;

import android.util.Log;

public class AndroidLog implements ILog {
    public boolean isLogLevelEnabled(int i) {
        return true;
    }

    public void d(String str, String str2) {
        if (str != null && str2 != null) {
            Log.d(str, str2);
        }
    }

    public void d(String str, String str2, Throwable th) {
        if (str != null && str2 != null && th != null) {
            Log.d(str, str2, th);
        }
    }

    public void e(String str, String str2) {
        if (str != null && str2 != null) {
            Log.e(str, str2);
        }
    }

    public void e(String str, String str2, Throwable th) {
        if (str != null && str2 != null && th != null) {
            Log.e(str, str2, th);
        }
    }

    public void i(String str, String str2) {
        if (str != null && str2 != null) {
            Log.i(str, str2);
        }
    }

    public void i(String str, String str2, Throwable th) {
        if (str != null && str2 != null && th != null) {
            Log.i(str, str2, th);
        }
    }

    public void v(String str, String str2) {
        if (str != null && str2 != null) {
            Log.v(str, str2);
        }
    }

    public void v(String str, String str2, Throwable th) {
        if (str != null && str2 != null && th != null) {
            Log.v(str, str2, th);
        }
    }

    public void w(String str, String str2) {
        if (str != null && str2 != null) {
            Log.w(str, str2);
        }
    }

    public void w(String str, String str2, Throwable th) {
        if (str != null && str2 != null && th != null) {
            Log.w(str, str2, th);
        }
    }
}
