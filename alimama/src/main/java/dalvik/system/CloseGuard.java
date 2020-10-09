package dalvik.system;

import androidx.annotation.Keep;

@Keep
public class CloseGuard {

    @Keep
    public interface Reporter {
        void report(String str, Throwable th);
    }
}
