package alimama.com.unwrouter;

import android.app.Activity;
import java.util.HashMap;
import java.util.Map;

public class PageInfo {
    public static final String PAGE_HOME = "ws-home";
    public static final String PAGE_WEBVIEW = "webview";
    private static Map<String, PageInfo> sList = new HashMap();
    private boolean isOverrided;
    private Class<? extends Activity> mClass;
    private int mEnterAni;
    private int mFlag;
    private boolean mNeedLogin;
    private String mPath;

    public PageInfo(String str, Class<? extends Activity> cls) {
        this(str, cls, -1, false);
    }

    public PageInfo(String str, Class<? extends Activity> cls, boolean z) {
        this(str, cls, -1, z);
    }

    public PageInfo(String str, Class<? extends Activity> cls, int i) {
        this(str, cls, i, false);
    }

    public PageInfo(String str, Class<? extends Activity> cls, int i, boolean z) {
        this.mFlag = -1;
        this.mNeedLogin = false;
        this.mEnterAni = -1;
        this.isOverrided = false;
        this.mPath = str;
        this.mClass = cls;
        this.mFlag = i;
        this.mNeedLogin = z;
        sList.put(str, this);
    }

    public static PageInfo find(String str) {
        if (sList.containsKey(str)) {
            return sList.get(str);
        }
        return null;
    }

    public static PageInfo find(Activity activity) {
        Class<?> cls = activity.getClass();
        for (Map.Entry next : sList.entrySet()) {
            if (((PageInfo) next.getValue()).getPageClass().equals(cls)) {
                return (PageInfo) next.getValue();
            }
        }
        return null;
    }

    public Class<?> getPageClass() {
        return this.mClass;
    }

    public String getPath() {
        return this.mPath;
    }

    public int getFlag() {
        return this.mFlag;
    }

    public void setNeedLogin(boolean z) {
        this.mNeedLogin = z;
    }

    public boolean isNeedLogin() {
        return this.mNeedLogin;
    }

    public int getEnterAni() {
        return this.mEnterAni;
    }

    public PageInfo setEnterAni(int i) {
        this.mEnterAni = i;
        return this;
    }

    public String toString() {
        return "PageInfo{mPath='" + this.mPath + '\'' + ", mClass=" + this.mClass + ", mFlag=" + this.mFlag + ", mNeedLogin=" + this.mNeedLogin + ", mEnterAni=" + this.mEnterAni + ", isOverrided=" + this.isOverrided + '}';
    }
}
