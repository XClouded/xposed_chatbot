package android.app;

import android.os.Handler;

import androidx.annotation.Keep;

@Keep
public class ActivityThread {
    public static ActivityThread currentActivityThread() {
        return null;
    }

    @Keep
    public class H extends Handler {
        public H() {
        }
    }
}
