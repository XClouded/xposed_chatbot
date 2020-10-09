package alimama.com.unwetaologger.base;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class UNWLoggerManager {
    private static final String DEFAULT_MODULE = "default";
    private static EmptyLogger sLogger = new EmptyLogger("empty");
    private boolean isEnable;
    private boolean isLocalEnable;
    private String mBusiness;
    private int mLevel;
    private Map<String, WeakReference<UNWLogger>> mLoggerMap;

    private static class Holder {
        /* access modifiers changed from: private */
        public static UNWLoggerManager sInstance = new UNWLoggerManager();

        private Holder() {
        }
    }

    private UNWLoggerManager() {
        this.isLocalEnable = false;
        this.isEnable = false;
        this.mLevel = 1;
        this.mBusiness = "default";
        this.mLoggerMap = new HashMap();
    }

    public void init(String str) {
        this.mBusiness = str;
    }

    public String getBusiness() {
        return this.mBusiness;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public void setEnable(boolean z) {
        this.isEnable = z;
    }

    public void setLocalEnable(boolean z) {
        this.isLocalEnable = z;
    }

    public boolean getLocalEnable() {
        return this.isLocalEnable;
    }

    public void setLevel(int i) {
        this.mLevel = i;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public static UNWLoggerManager getInstance() {
        return Holder.sInstance;
    }

    @NonNull
    public synchronized UNWLogger getLoggerByModule(@NonNull String str) {
        UNWLogger uNWLogger;
        if (!this.isEnable) {
            if (sLogger == null) {
                sLogger = new EmptyLogger("empty");
            }
            return sLogger;
        }
        if (TextUtils.isEmpty(str)) {
            str = "default";
        }
        WeakReference weakReference = this.mLoggerMap.get(str);
        if (weakReference != null && (uNWLogger = (UNWLogger) weakReference.get()) != null) {
            return uNWLogger;
        }
        UNWLogger uNWLogger2 = new UNWLogger(str);
        this.mLoggerMap.put(str, new WeakReference(uNWLogger2));
        for (Map.Entry next : this.mLoggerMap.entrySet()) {
            if (next.getValue() == null) {
                this.mLoggerMap.remove(next.getKey());
            }
        }
        return uNWLogger2;
    }
}
