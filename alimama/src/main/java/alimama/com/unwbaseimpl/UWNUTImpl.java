package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IABTest;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.IUTAction;
import alimama.com.unwbase.interfaces.IUnionLens;
import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alimama.moon.utils.UnionLensUtil;
import com.taobao.ju.track.constants.Constants;
import com.ut.mini.IUTApplication;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageStatus;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class UWNUTImpl implements IUTAction {
    private static final String TAG = "UWNUTImpl";
    private boolean isInitialized = false;

    public void init() {
        if (!this.isInitialized) {
            final IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
            if (iAppEnvironment == null) {
                this.isInitialized = false;
                return;
            }
            final ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
            if (iSecurity == null) {
                this.isInitialized = false;
                return;
            }
            UTAnalytics.getInstance().setAppApplicationInstance(UNWManager.getInstance().application, new IUTApplication() {
                public boolean isAliyunOsSystem() {
                    return false;
                }

                public boolean isUTCrashHandlerDisable() {
                    return true;
                }

                public String getUTAppVersion() {
                    return UNWManager.getInstance().getAppVersion();
                }

                public String getUTChannel() {
                    return iAppEnvironment.getTTid();
                }

                public IUTRequestAuthentication getUTRequestAuthInstance() {
                    return new UTSecuritySDKRequestAuthentication(iSecurity.getAppkey());
                }

                public boolean isUTLogEnable() {
                    return UNWManager.getInstance().getDebuggable();
                }

                public IUTCrashCaughtListner getUTCrashCraughtListener() {
                    return new IUTCrashCaughtListner() {
                        public Map<String, String> onCrashCaught(Thread thread, Throwable th) {
                            return null;
                        }
                    };
                }
            });
            this.isInitialized = true;
            UTAnalytics.getInstance().turnOffAutoPageTrack();
        }
    }

    public void ctrlClicked(String str, String str2) {
        Map<String, String> changeArgs;
        if (UTAnalytics.getInstance().isInit() && !TextUtils.isEmpty(str)) {
            UTHitBuilders.UTControlHitBuilder uTControlHitBuilder = new UTHitBuilders.UTControlHitBuilder(str, str2);
            IABTest iABTest = (IABTest) UNWManager.getInstance().getService(IABTest.class);
            if (!(iABTest == null || (changeArgs = iABTest.changeArgs(str2, (Map<String, String>) null)) == null)) {
                uTControlHitBuilder.setProperties(changeArgs);
            }
            UTAnalytics.getInstance().getDefaultTracker().send(uTControlHitBuilder.build());
        }
    }

    public void ctrlClicked(String str, String str2, Map<String, String> map) {
        Map<String, String> changeArgs;
        if (UTAnalytics.getInstance().isInit() && !TextUtils.isEmpty(str)) {
            UTHitBuilders.UTControlHitBuilder uTControlHitBuilder = new UTHitBuilders.UTControlHitBuilder(str, str2);
            IABTest iABTest = (IABTest) UNWManager.getInstance().getService(IABTest.class);
            if (!(iABTest == null || (changeArgs = iABTest.changeArgs(str2, map)) == null)) {
                uTControlHitBuilder.setProperties(changeArgs);
            }
            UTAnalytics.getInstance().getDefaultTracker().send(uTControlHitBuilder.build());
        }
    }

    public void customEvent(String str, String str2, Map<String, String> map) {
        if (UTAnalytics.getInstance().isInit()) {
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str2);
            uTCustomHitBuilder.setEventPage(str);
            if (map != null) {
                uTCustomHitBuilder.setProperties(map);
            }
            UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        }
    }

    public void addSpmUrl(String str, Map<String, String> map) {
        if (UTAnalytics.getInstance().isInit() && !TextUtils.isEmpty(str)) {
            if (map == null) {
                map = new HashMap<>();
            }
            map.put("spm-url", str);
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(map);
        }
    }

    public void pageAppear(Activity activity, String str) {
        if (UTAnalytics.getInstance().isInit()) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppear(activity, str);
        }
    }

    public void pageDisappear(Activity activity, String str) {
        if (UTAnalytics.getInstance().isInit()) {
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(str)) {
                hashMap.put(Constants.PARAM_OUTER_SPM_CNT, str);
            }
            IUnionLens iUnionLens = (IUnionLens) UNWManager.getInstance().getService(IUnionLens.class);
            if (iUnionLens != null && iUnionLens.isUnionLensReport()) {
                try {
                    Map<String, String> pageProperties = UTAnalytics.getInstance().getDefaultTracker().getPageProperties(activity);
                    String appendNaUnionLens = iUnionLens.appendNaUnionLens(pageProperties);
                    if (appendNaUnionLens != null && !appendNaUnionLens.isEmpty()) {
                        hashMap.put(UnionLensUtil.UNION_LENS_LOG, iUnionLens.appendNaUnionLens(pageProperties));
                    }
                } catch (Throwable th) {
                    Log.d(TAG, "add unionLens error: " + th.getMessage());
                }
            }
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, hashMap);
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
        }
    }

    public void skipPage(Activity activity) {
        if (isInit() && activity != null) {
            UTAnalytics.getInstance().getDefaultTracker().skipPage(activity);
        }
    }

    public void pageAppearDonotSkip(Activity activity, String str) {
        if (isInit() && activity != null) {
            UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(activity, str);
        }
    }

    public void updatePageStatus(Object obj, UTPageStatus uTPageStatus) {
        if (isInit() && obj != null) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageStatus(obj, uTPageStatus);
        }
    }

    public void updatePageUrl(Object obj, Uri uri) {
        if (isInit() && obj != null) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageUrl(obj, uri);
        }
    }

    public void updateAccount(String str, String str2, String str3) {
        if (isInit()) {
            UTAnalytics.getInstance().updateUserAccount(str, str2, str3);
        }
    }

    public void expoTrack(@NotNull String str, String str2, String str3, String str4, Map<String, String> map) {
        if (isInit()) {
            UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder(str, SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM, str2, str3, str4, map).build());
        }
    }

    public boolean isInit() {
        return UTAnalytics.getInstance().isInit();
    }
}
