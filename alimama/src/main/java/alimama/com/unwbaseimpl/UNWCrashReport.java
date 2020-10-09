package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.ICrashReport;
import alimama.com.unwbase.interfaces.ISecurity;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.alibaba.motu.crashreporter.ReporterConfigure;
import com.taobao.login4android.Login;
import java.util.HashMap;
import java.util.Map;

public class UNWCrashReport implements ICrashReport {
    public void init() {
        ISecurity iSecurity;
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment != null && (iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class)) != null) {
            ReporterConfigure reporterConfigure = new ReporterConfigure();
            reporterConfigure.setEnableDebug(UNWManager.getInstance().getDebuggable());
            reporterConfigure.setEnableDumpSysLog(true);
            reporterConfigure.setEnableDumpRadioLog(true);
            reporterConfigure.setEnableDumpEventsLog(true);
            reporterConfigure.setEnableCatchANRException(true);
            reporterConfigure.setEnableANRMainThreadOnly(true);
            reporterConfigure.setEnableDumpAllThread(true);
            reporterConfigure.enableDeduplication = false;
            MotuCrashReporter.getInstance().enable(UNWManager.getInstance().application, iSecurity.getAppId(), iSecurity.getAppkey(), UNWManager.getInstance().mVersionName, iAppEnvironment.getTTid(), Login.getNick(), reporterConfigure);
            MotuCrashReporter.getInstance().registerLifeCallbacks(UNWManager.getInstance().application);
            MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new IUTCrashCaughtListener() {
                public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
                    HashMap hashMap = new HashMap();
                    if (!(th == null || thread == null)) {
                        try {
                            HashMap hashMap2 = new HashMap();
                            hashMap2.put("thread", thread.toString());
                            UNWManager.getInstance().getLogger().info("crash", "crash", th.toString(), hashMap2);
                        } catch (Exception unused) {
                        }
                    }
                    return hashMap;
                }
            });
            MotuCrashReporter.getInstance().setUserNick(Login.getNick());
        }
    }

    public void setCrashCaughtListener(IUTCrashCaughtListener iUTCrashCaughtListener) {
        MotuCrashReporter.getInstance().setCrashCaughtListener(iUTCrashCaughtListener);
    }
}
