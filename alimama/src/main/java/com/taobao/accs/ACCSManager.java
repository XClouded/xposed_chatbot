package com.taobao.accs;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.accs.internal.ACCSManagerImpl;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.Utils;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;

@Deprecated
public final class ACCSManager {
    private static final String TAG = "ACCSManager";
    public static Map<String, IACCSManager> mAccsInstances = new ConcurrentHashMap(2);
    public static Context mContext = null;
    public static String mDefaultAppkey = null;
    public static String mDefaultConfigTag = "default";
    public static int mEnv;

    @Deprecated
    public static String getUserUnit(Context context) {
        return null;
    }

    @Deprecated
    public static void setServiceListener(Context context, String str, IServiceReceiver iServiceReceiver) {
    }

    @Deprecated
    public static void unbindApp(Context context) {
    }

    private ACCSManager() {
    }

    @Deprecated
    public static void setAppkey(Context context, String str, @AccsClientConfig.ENV int i) {
        Utils.setAgooAppkey(context, str);
        mContext = context.getApplicationContext();
        mDefaultAppkey = str;
        Utils.setSpValue(mContext, Constants.SP_KEY_DEFAULT_APPKEY, mDefaultAppkey);
        mEnv = i;
        AccsClientConfig.mEnv = i;
    }

    @Deprecated
    public static String getDefaultAppkey(Context context) {
        if (TextUtils.isEmpty(mDefaultAppkey)) {
            ALog.e(TAG, "old interface!!, please AccsManager.setAppkey() first!", new Object[0]);
            mDefaultAppkey = Utils.getSpValue(context, Constants.SP_KEY_DEFAULT_APPKEY, (String) null);
            if (TextUtils.isEmpty(mDefaultAppkey)) {
                try {
                    mDefaultAppkey = SecurityGuardManager.getInstance(context).getStaticDataStoreComp().getAppKeyByIndex(0, (String) null);
                } catch (Throwable th) {
                    ALog.e(TAG, "getDefaultAppkey", th, new Object[0]);
                }
            }
            if (TextUtils.isEmpty(mDefaultAppkey)) {
                mDefaultAppkey = "0";
            }
        }
        return mDefaultAppkey;
    }

    @Deprecated
    public static void setDefaultConfig(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            ALog.i(TAG, "setDefaultConfig", Constants.KEY_CONFIG_TAG, str);
            mDefaultConfigTag = str;
        }
    }

    public static String getDefaultConfig(Context context) {
        return mDefaultConfigTag;
    }

    @Deprecated
    public static void bindApp(Context context, String str, String str2, String str3, IAppReceiver iAppReceiver) {
        if (!TextUtils.isEmpty(mDefaultAppkey)) {
            Utils.initConfig();
            getManagerImpl(context).bindApp(context, mDefaultAppkey, str2, str3, iAppReceiver);
            return;
        }
        throw new RuntimeException("old interface!!, please AccsManager.setAppkey() first!");
    }

    @Deprecated
    public static void bindApp(Context context, String str, String str2, IAppReceiver iAppReceiver) {
        bindApp(context, mDefaultAppkey, "", str2, iAppReceiver);
    }

    @Deprecated
    public static void bindUser(Context context, String str) {
        bindUser(context, str, false);
    }

    @Deprecated
    public static void bindUser(Context context, String str, boolean z) {
        if (!TextUtils.isEmpty(mDefaultAppkey)) {
            getManagerImpl(context).bindUser(context, str, z);
            return;
        }
        throw new RuntimeException("old interface!!, please AccsManager.setAppkey() first!");
    }

    @Deprecated
    public static void unbindUser(Context context) {
        getManagerImpl(context).unbindUser(context);
    }

    @Deprecated
    public static void bindService(Context context, String str) {
        getManagerImpl(context).bindService(context, str);
    }

    @Deprecated
    public static void unbindService(Context context, String str) {
        getManagerImpl(context).unbindService(context, str);
    }

    @Deprecated
    public static String sendData(Context context, String str, String str2, byte[] bArr, String str3) {
        return getManagerImpl(context).sendData(context, str, str2, bArr, str3);
    }

    @Deprecated
    public static String sendData(Context context, String str, String str2, byte[] bArr, String str3, String str4, URL url) {
        return getManagerImpl(context).sendData(context, str, str2, bArr, str3, str4, url);
    }

    @Deprecated
    public static String sendData(Context context, String str, String str2, byte[] bArr, String str3, String str4) {
        return getManagerImpl(context).sendData(context, str, str2, bArr, str3, str4);
    }

    @Deprecated
    public static String sendData(Context context, AccsRequest accsRequest) {
        return getManagerImpl(context).sendData(context, accsRequest);
    }

    @Deprecated
    public static String sendRequest(Context context, String str, String str2, byte[] bArr, String str3, String str4, URL url) {
        return getManagerImpl(context).sendRequest(context, str, str2, bArr, str3, str4, url);
    }

    @Deprecated
    public static String sendRequest(Context context, String str, String str2, byte[] bArr, String str3) {
        return sendRequest(context, str, str2, bArr, str3, (String) null);
    }

    @Deprecated
    public static String sendRequest(Context context, String str, String str2, byte[] bArr, String str3, String str4) {
        return getManagerImpl(context).sendRequest(context, str, str2, bArr, str3, str4);
    }

    @Deprecated
    public static String sendRequest(Context context, AccsRequest accsRequest) {
        return getManagerImpl(context).sendRequest(context, accsRequest);
    }

    @Deprecated
    public static String sendPushResponse(Context context, AccsRequest accsRequest, TaoBaseService.ExtraInfo extraInfo) {
        return getManagerImpl(context).sendPushResponse(context, accsRequest, extraInfo);
    }

    @Deprecated
    public static boolean isNetworkReachable(Context context) {
        return getManagerImpl(context).isNetworkReachable(context);
    }

    @Deprecated
    public static void setMode(Context context, int i) {
        mEnv = i;
        getManagerImpl(context).setMode(context, i);
    }

    @Deprecated
    public static void setProxy(Context context, String str, int i) {
        getManagerImpl(context).setProxy(context, str, i);
    }

    @Deprecated
    public static void startInAppConnection(Context context, String str, String str2, String str3, IAppReceiver iAppReceiver) {
        Utils.initConfig();
        getManagerImpl(context).startInAppConnection(context, mDefaultAppkey, str2, str3, iAppReceiver);
    }

    @Deprecated
    public static void startInAppConnection(Context context, String str, String str2, IAppReceiver iAppReceiver) {
        startInAppConnection(context, mDefaultAppkey, "", str2, iAppReceiver);
    }

    @Deprecated
    public static void setLoginInfoImpl(Context context, ILoginInfo iLoginInfo) {
        getManagerImpl(context).setLoginInfo(context, iLoginInfo);
    }

    @Deprecated
    public static void clearLoginInfoImpl(Context context) {
        getManagerImpl(context).clearLoginInfo(context);
    }

    @Deprecated
    public static Map<String, Boolean> getChannelState(Context context) throws Exception {
        return getManagerImpl(context).getChannelState();
    }

    @Deprecated
    public static Map<String, Boolean> forceReConnectChannel(Context context) throws Exception {
        return getManagerImpl(context).forceReConnectChannel();
    }

    @Deprecated
    public static boolean isChannelError(Context context, int i) {
        return getManagerImpl(context).isChannelError(i);
    }

    @Deprecated
    public static void registerSerivce(Context context, String str, String str2) {
        getManagerImpl(context).registerSerivce(context, str, str2);
    }

    @Deprecated
    public static void unregisterService(Context context, String str) {
        getManagerImpl(context).unRegisterSerivce(context, str);
    }

    @Deprecated
    public static void registerDataListener(Context context, String str, AccsAbstractDataListener accsAbstractDataListener) {
        if (getManagerImpl(context) == null) {
            ALog.e(TAG, "getManagerImpl null, return", new Object[0]);
        } else {
            getManagerImpl(context).registerDataListener(context, str, accsAbstractDataListener);
        }
    }

    @Deprecated
    public static void unRegisterDataListener(Context context, String str) {
        getManagerImpl(context).unRegisterDataListener(context, str);
    }

    private static synchronized IACCSManager getManagerImpl(Context context) {
        IACCSManager accsInstance;
        synchronized (ACCSManager.class) {
            accsInstance = getAccsInstance(context, (String) null, getDefaultConfig(context));
        }
        return accsInstance;
    }

    public static IACCSManager getAccsInstance(Context context, String str, String str2) {
        IACCSManager iACCSManager;
        if (context == null || TextUtils.isEmpty(str2)) {
            ALog.e(TAG, "getAccsInstance param null", Constants.KEY_CONFIG_TAG, str2);
            return null;
        }
        String str3 = str2 + "|" + AccsClientConfig.mEnv;
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(TAG, "getAccsInstance", "key", str3);
        }
        IACCSManager iACCSManager2 = mAccsInstances.get(str3);
        if (iACCSManager2 == null) {
            synchronized (ACCSManager.class) {
                if (iACCSManager2 == null) {
                    try {
                        iACCSManager = createAccsInstance(context, str2);
                    } catch (Exception e) {
                        ALog.e(TAG, "createAccsInstance error", e.getMessage());
                        iACCSManager = iACCSManager2;
                    }
                    if (iACCSManager != null) {
                        mAccsInstances.put(str3, iACCSManager);
                    }
                    iACCSManager2 = iACCSManager;
                }
            }
        }
        return iACCSManager2;
    }

    protected static IACCSManager createAccsInstance(Context context, String str) {
        return new ACCSManagerImpl(context, str);
    }

    public static void forceEnableService(Context context) {
        getManagerImpl(context).forceEnableService(context);
    }

    public static void forceDisableService(Context context) {
        getManagerImpl(context).forceDisableService(context);
    }

    public static String[] getAppkey(Context context) {
        try {
            String string = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getString("appkey", (String) null);
            ALog.i(TAG, "getAppkey:" + string, new Object[0]);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return string.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static class AccsRequest implements Serializable {
        public String businessId;
        public byte[] data;
        public String dataId;
        public URL host;
        public boolean isUnitBusiness = false;
        public String serviceId;
        public String tag;
        public String target;
        public String targetServiceName;
        public int timeout;
        public String userId;

        public AccsRequest(String str, String str2, byte[] bArr, String str3, String str4, URL url, String str5) {
            this.userId = str;
            this.serviceId = str2;
            this.data = bArr;
            this.dataId = str3;
            this.target = str4;
            this.host = url;
            this.businessId = str5;
        }

        public AccsRequest(String str, String str2, byte[] bArr, String str3) {
            this.userId = str;
            this.serviceId = str2;
            this.data = bArr;
            this.dataId = str3;
        }

        public void setTag(String str) {
            this.tag = str;
        }

        public void setTimeOut(int i) {
            this.timeout = i;
        }

        public void setIsUnitBusiness(boolean z) {
            this.isUnitBusiness = z;
        }

        public void setTarget(String str) {
            this.target = str;
        }

        public void setTargetServiceName(String str) {
            this.targetServiceName = str;
        }

        public void setHost(URL url) {
            this.host = url;
        }

        public void setBusinessId(String str) {
            this.businessId = str;
        }
    }
}
