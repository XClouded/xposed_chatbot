package com.taobao.accs.client;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.taobao.accs.IAgooAppReceiver;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.IGlobalClientInfoService;
import com.taobao.accs.ILoginInfo;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.base.AccsDataListener;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.aipc.AIPC;
import com.taobao.aipc.annotation.method.MethodName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.android.agoo.common.AgooConstants;

public class GlobalClientInfo {
    public static final String AGOO_SERVICE_ID = "agooSend";
    private static Map<String, String> SERVICES = new ConcurrentHashMap();
    private static Map<String, Map<String, String>> TAG_SERVICES = new ConcurrentHashMap();
    public static IAgooAppReceiver mAgooAppReceiver = null;
    public static Context mContext = null;
    public static String mCookieSec = null;
    private static volatile GlobalClientInfo mInstance = null;
    public static boolean mSupprotElection = false;
    private Map<String, AccsDataListener> LISTENERS = new ConcurrentHashMap();
    private ActivityManager mActivityManager;
    private ConcurrentHashMap<String, IAppReceiver> mAppReceiver;
    private ConnectivityManager mConnectivityManager;
    private ConcurrentHashMap<String, ILoginInfo> mILoginInfoImpl;
    private PackageInfo mPackageInfo;

    static {
        SERVICES.put(AGOO_SERVICE_ID, "org.android.agoo.accs.AgooService");
        SERVICES.put(AgooConstants.AGOO_SERVICE_AGOOACK, "org.android.agoo.accs.AgooService");
        SERVICES.put("agooTokenReport", "org.android.agoo.accs.AgooService");
    }

    @Keep
    public static GlobalClientInfo getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GlobalClientInfo.class) {
                if (mInstance == null) {
                    mInstance = new GlobalClientInfo(context);
                }
            }
        }
        return mInstance;
    }

    public static Context getContext() {
        if (mContext == null) {
            try {
                ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
                if (currentActivityThread != null) {
                    mContext = currentActivityThread.getApplication();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mContext;
    }

    private GlobalClientInfo(Context context) {
        mContext = getContext();
        if (mContext == null && context != null) {
            mContext = context.getApplicationContext();
        }
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                GlobalClientInfo.mCookieSec = UtilityImpl.restoreCookie(GlobalClientInfo.mContext);
            }
        });
    }

    public ActivityManager getActivityManager() {
        if (this.mActivityManager == null) {
            this.mActivityManager = (ActivityManager) mContext.getSystemService("activity");
        }
        return this.mActivityManager;
    }

    public ConnectivityManager getConnectivityManager() {
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager) mContext.getSystemService("connectivity");
        }
        return this.mConnectivityManager;
    }

    public void setLoginInfoImpl(String str, ILoginInfo iLoginInfo) {
        if (this.mILoginInfoImpl == null) {
            this.mILoginInfoImpl = new ConcurrentHashMap<>(1);
        }
        if (iLoginInfo != null) {
            this.mILoginInfoImpl.put(str, iLoginInfo);
        }
    }

    public void clearLoginInfoImpl() {
        this.mILoginInfoImpl = null;
    }

    public String getSid(String str) {
        ILoginInfo iLoginInfo;
        if (this.mILoginInfoImpl == null || (iLoginInfo = this.mILoginInfoImpl.get(str)) == null) {
            return null;
        }
        return iLoginInfo.getSid();
    }

    public String getUserId(String str) {
        ILoginInfo iLoginInfo;
        if (this.mILoginInfoImpl == null || (iLoginInfo = this.mILoginInfoImpl.get(str)) == null) {
            return null;
        }
        return iLoginInfo.getUserId();
    }

    public String getNick(String str) {
        ILoginInfo iLoginInfo;
        if (this.mILoginInfoImpl == null || (iLoginInfo = this.mILoginInfoImpl.get(str)) == null) {
            return null;
        }
        return iLoginInfo.getNick();
    }

    public void setAppReceiver(String str, IAppReceiver iAppReceiver) {
        if ((!OrangeAdapter.isChannelModeEnable() || UtilityImpl.isMainProcess(mContext)) && iAppReceiver != null) {
            boolean z = iAppReceiver instanceof IAgooAppReceiver;
            if (z) {
                mAgooAppReceiver = (IAgooAppReceiver) iAppReceiver;
            } else {
                if (this.mAppReceiver == null) {
                    this.mAppReceiver = new ConcurrentHashMap<>(2);
                }
                this.mAppReceiver.put(str, iAppReceiver);
                registerAllService(str, iAppReceiver.getAllServices());
            }
            if (OrangeAdapter.isChannelModeEnable()) {
                IGlobalClientInfoService iGlobalClientInfoService = (IGlobalClientInfoService) AIPC.getInstance(IGlobalClientInfoService.class, mContext);
                if (z) {
                    iGlobalClientInfoService.setRemoteAgooAppReceiver((IAgooAppReceiver) iAppReceiver);
                } else {
                    iGlobalClientInfoService.setRemoteAppReceiver(str, iAppReceiver);
                }
            }
        }
    }

    public Map<String, IAppReceiver> getAppReceiver() {
        return this.mAppReceiver;
    }

    public Map<String, AccsDataListener> getListener() {
        return this.LISTENERS;
    }

    public void registerService(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            SERVICES.put(str, str2);
            if (OrangeAdapter.isChannelModeEnable() && UtilityImpl.isMainProcess(mContext)) {
                IGlobalClientInfoService iGlobalClientInfoService = (IGlobalClientInfoService) AIPC.getInstance(IGlobalClientInfoService.class, mContext);
                if (iGlobalClientInfoService != null) {
                    iGlobalClientInfoService.registerRemoteService(str, str2);
                }
            }
        }
    }

    public void unRegisterService(String str) {
        if (!TextUtils.isEmpty(str)) {
            SERVICES.remove(str);
            if (OrangeAdapter.isChannelModeEnable() && UtilityImpl.isMainProcess(mContext)) {
                IGlobalClientInfoService iGlobalClientInfoService = (IGlobalClientInfoService) AIPC.getInstance(IGlobalClientInfoService.class, mContext);
                if (iGlobalClientInfoService != null) {
                    iGlobalClientInfoService.unregisterRemoteService(str);
                }
            }
        }
    }

    public String getService(String str) {
        return SERVICES.get(str);
    }

    public String getService(String str, String str2) {
        if (TAG_SERVICES.get(str) != null) {
            return (String) TAG_SERVICES.get(str).get(str2);
        }
        return null;
    }

    public void registerListener(String str, AccsAbstractDataListener accsAbstractDataListener) {
        registerListener(str, (AccsDataListener) accsAbstractDataListener);
    }

    public void registerListener(String str, AccsDataListener accsDataListener) {
        if ((!OrangeAdapter.isChannelModeEnable() || UtilityImpl.isMainProcess(mContext)) && !TextUtils.isEmpty(str) && accsDataListener != null) {
            this.LISTENERS.put(str, accsDataListener);
            if (OrangeAdapter.isChannelModeEnable()) {
                ((IGlobalClientInfoService) AIPC.getInstance(IGlobalClientInfoService.class, mContext)).registerRemoteListener(str, accsDataListener);
            }
        }
    }

    public void unregisterListener(String str) {
        if (!OrangeAdapter.isChannelModeEnable() || UtilityImpl.isMainProcess(mContext)) {
            this.LISTENERS.remove(str);
            if (OrangeAdapter.isChannelModeEnable()) {
                ((IGlobalClientInfoService) AIPC.getInstance(IGlobalClientInfoService.class, mContext)).unregisterRemoteListener(str);
            }
        }
    }

    public AccsDataListener getListener(String str) {
        return this.LISTENERS.get(str);
    }

    public PackageInfo getPackageInfo() {
        try {
            if (this.mPackageInfo == null) {
                this.mPackageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            }
        } catch (Throwable th) {
            ALog.e("GlobalClientInfo", "getPackageInfo", th, new Object[0]);
        }
        return this.mPackageInfo;
    }

    public Map<String, String> getAllService(String str) {
        if (TAG_SERVICES.get(str) == null || TAG_SERVICES.get(str).isEmpty()) {
            return null;
        }
        return TAG_SERVICES.get(str);
    }

    private void registerAllService(String str, Map<String, String> map) {
        if (map != null) {
            if (TAG_SERVICES.get(str) == null) {
                TAG_SERVICES.put(str, new ConcurrentHashMap());
            }
            TAG_SERVICES.get(str).putAll(map);
        }
    }

    @MethodName("setRemoteAgooAppReceiver")
    @Keep
    private void setRemoteAgooAppReceiver(IAgooAppReceiver iAgooAppReceiver) {
        mAgooAppReceiver = iAgooAppReceiver;
    }

    @MethodName("setRemoteAppReceiver")
    @Keep
    private void setRemoteAppReceiver(String str, IAppReceiver iAppReceiver) {
        if (this.mAppReceiver == null) {
            this.mAppReceiver = new ConcurrentHashMap<>(2);
        }
        this.mAppReceiver.put(str, iAppReceiver);
        registerAllService(str, iAppReceiver.getAllServices());
    }

    @MethodName("registerRemoteListener")
    @Keep
    private void registerRemoteListener(String str, AccsDataListener accsDataListener) {
        this.LISTENERS.put(str, accsDataListener);
    }

    @MethodName("unregisterRemoteListener")
    @Keep
    private void unregisterRemoteListener(String str) {
        this.LISTENERS.remove(str);
    }

    @MethodName("registerRemoteService")
    @Keep
    private void registerRemoteService(String str, String str2) {
        SERVICES.put(str, str2);
    }

    @MethodName("unregisterRemoteService")
    @Keep
    private void unregisterRemoteService(String str) {
        SERVICES.remove(str);
    }
}
