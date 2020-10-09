package android.taobao.windvane.extra.uc;

import android.os.Handler;
import android.os.HandlerThread;

public class WVThread extends HandlerThread {
    private Handler mHandler;

    public WVThread(String str) {
        super(str);
        start();
        this.mHandler = new Handler(getLooper());
    }

    public WVThread(String str, int i) {
        super(str, i);
        start();
        this.mHandler = new Handler(getLooper());
    }

    public WVThread(String str, Handler.Callback callback) {
        super(str);
        start();
        this.mHandler = new Handler(getLooper(), callback);
    }

    public WVThread(String str, int i, Handler.Callback callback) {
        super(str, i);
        start();
        this.mHandler = new Handler(getLooper(), callback);
    }

    public boolean quit() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        return super.quit();
    }

    public Handler getHandler() {
        return this.mHandler;
    }
}
