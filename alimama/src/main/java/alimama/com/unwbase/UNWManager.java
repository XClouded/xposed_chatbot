package alimama.com.unwbase;

import alimama.com.unwbase.emptyimpl.EtaoIsharedEmptyImpl;
import alimama.com.unwbase.emptyimpl.EtaoLoggerEmptyImpl;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IInitAction;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.ISharedPreference;
import alimama.com.unwbase.tools.EnvModel;
import alimama.com.unwbase.tools.ThreadUtils;
import alimama.com.unwbase.tools.UNWLog;
import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.mtop.deviceid.DeviceIDManager;

public class UNWManager {
    private static UNWManager sInstance = new UNWManager();
    private String appName = "";
    private String appVersion = "";
    public Application application;
    private EnvModel debuggable = EnvModel.Release;
    public int mVersionCode;
    public String mVersionName;
    private String sDeviceId;
    private Map<Class<?>, Object> services = new ConcurrentHashMap();

    private UNWManager() {
    }

    public static UNWManager getInstance() {
        return sInstance;
    }

    public void init(Application application2) {
        this.application = application2;
        initCode(application2);
    }

    public <T> T getService(Class<T> cls) {
        T t = this.services.get(cls);
        if (t != null) {
            return t;
        }
        return null;
    }

    public <T> void registerService(Class<T> cls, T t) {
        if (this.application != null) {
            this.services.put(cls, t);
            if (t instanceof IInitAction) {
                ((IInitAction) t).init();
                return;
            }
            return;
        }
        throw new NullPointerException("UNWManager请先进行初始化");
    }

    public <T> void registerServiceAsync(Class<T> cls, T t) {
        if (this.application != null) {
            this.services.put(cls, t);
            if (t instanceof IInitAction) {
                final IInitAction iInitAction = (IInitAction) t;
                ThreadUtils.runInBackByFixThread(new Runnable() {
                    public void run() {
                        iInitAction.init();
                    }
                });
                return;
            }
            return;
        }
        throw new NullPointerException("UNWManager请先进行初始化");
    }

    public <T> void registerService(Class<T> cls, T t, long j) {
        if (this.application != null) {
            this.services.put(cls, t);
            if (t instanceof IInitAction) {
                final IInitAction iInitAction = (IInitAction) t;
                ThreadUtils.runInBackByScheduleThread(new Runnable() {
                    public void run() {
                        iInitAction.init();
                    }
                }, j);
                return;
            }
            return;
        }
        throw new NullPointerException("UNWManager请先进行初始化");
    }

    @NonNull
    public IEtaoLogger getLogger() {
        if (this.services.get(IEtaoLogger.class) != null) {
            return (IEtaoLogger) this.services.get(IEtaoLogger.class);
        }
        return new EtaoLoggerEmptyImpl();
    }

    @NonNull
    public ISharedPreference getSharedPreference() {
        if (this.services.get(ISharedPreference.class) == null) {
            registerService(ISharedPreference.class, new EtaoIsharedEmptyImpl());
        }
        if (this.services.get(ISharedPreference.class) != null) {
            return (ISharedPreference) this.services.get(ISharedPreference.class);
        }
        return new EtaoIsharedEmptyImpl();
    }

    public void setDebuggable(EnvModel envModel) {
        this.debuggable = envModel;
        UNWLog.logable = EnvModel.Debug == envModel;
    }

    public boolean getDebuggable() {
        return EnvModel.Debug == this.debuggable;
    }

    public boolean isMainProcess() {
        ActivityManager activityManager = (ActivityManager) getInstance().application.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager != null ? activityManager.getRunningAppProcesses() : null;
        String packageName = this.application.getPackageName();
        int myPid = Process.myPid();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.pid == myPid && packageName.equals(next.processName)) {
                return true;
            }
        }
        return false;
    }

    public String getAppVersion() {
        if (TextUtils.isEmpty(this.appVersion)) {
            try {
                this.appVersion = this.application.getPackageManager().getPackageInfo(this.application.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException unused) {
            }
            if (TextUtils.isEmpty(this.appVersion)) {
                this.appVersion = "1.0.0";
            }
        }
        return this.appVersion;
    }

    public boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private void initCode(Application application2) {
        PackageInfo packageInfo;
        try {
            packageInfo = application2.getPackageManager().getPackageInfo(application2.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        if (packageInfo != null) {
            this.mVersionCode = packageInfo.versionCode;
            this.mVersionName = packageInfo.versionName;
            int stringOnNum = getStringOnNum(this.mVersionName, ".", 4);
            if (stringOnNum > 0) {
                this.mVersionName = this.mVersionName.substring(0, stringOnNum);
            }
        }
    }

    private static int getStringOnNum(String str, String str2, int i) {
        int[] iArr = new int[i];
        String str3 = str;
        for (int i2 = 0; i2 < i; i2++) {
            int indexOf = str3.indexOf(str2);
            if (indexOf <= 0) {
                return -1;
            }
            iArr[i2] = indexOf;
            str3 = str3.substring(indexOf + 1);
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            i3 += iArr[i4];
        }
        return i3 + (str2.length() * (i - 1));
    }

    public String getAppName() {
        if (TextUtils.isEmpty(this.appName)) {
            try {
                this.appName = this.application.getResources().getString(this.application.getPackageManager().getPackageInfo(this.application.getPackageName(), 0).applicationInfo.labelRes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.appName;
    }

    public String getDeviceId() {
        ISecurity iSecurity = (ISecurity) getService(ISecurity.class);
        if (iSecurity == null) {
            return "";
        }
        if (TextUtils.isEmpty(this.sDeviceId)) {
            this.sDeviceId = DeviceIDManager.getInstance().getLocalDeviceID(this.application, iSecurity.getAppkey());
        }
        return this.sDeviceId;
    }
}
