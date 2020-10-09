package dalvik.system;

import androidx.annotation.Keep;

@Keep
public class VMStack {
    public static native StackTraceElement[] getThreadStackTrace(Thread thread);
}
