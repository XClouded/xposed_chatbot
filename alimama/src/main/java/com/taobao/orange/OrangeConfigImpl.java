package com.taobao.orange;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.orange.aidl.IOrangeApiService;
import com.taobao.orange.aidl.OrangeApiServiceStub;
import com.taobao.orange.aidl.OrangeConfigListenerStub;
import com.taobao.orange.service.OrangeApiService;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.AgooConstants;

public class OrangeConfigImpl extends OrangeConfig {
    static final String TAG = "OrangeConfigImpl";
    static OrangeConfigImpl mInstance = new OrangeConfigImpl();
    volatile CountDownLatch mBindServiceLock;
    final Set<String> mBlackNamespaces = new HashSet<String>() {
        {
            add("android_download_task");
            add("networkSdk");
            add("flow_customs_config");
            add("custom_out_config");
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName componentName) {
            OLog.w(OrangeConfigImpl.TAG, "onServiceDisconnected", new Object[0]);
            OrangeConfigImpl.this.mRemoteService = null;
            OrangeConfigImpl.this.mIsBindingService.set(false);
            if (OrangeConfigImpl.this.mBindServiceLock != null) {
                OrangeConfigImpl.this.mBindServiceLock.countDown();
            }
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            OLog.i(OrangeConfigImpl.TAG, "onServiceConnected", new Object[0]);
            OrangeConfigImpl.this.mRemoteService = IOrangeApiService.Stub.asInterface(iBinder);
            OrangeConfigImpl.this.mIsBindingService.set(false);
            if (OrangeConfigImpl.this.mBindServiceLock != null) {
                OrangeConfigImpl.this.mBindServiceLock.countDown();
            }
        }
    };
    volatile Context mContext;
    final List<OCandidate> mFailCandidates = Collections.synchronizedList(new ArrayList());
    final Map<String, Set<OrangeConfigListenerStub>> mFailListeners = new ConcurrentHashMap();
    final Set<String> mFailNamespaces = Collections.synchronizedSet(new HashSet());
    volatile String mFailUserId = null;
    AtomicBoolean mIsBindingService = new AtomicBoolean(false);
    volatile IOrangeApiService mRemoteService;

    private OrangeConfigImpl() {
    }

    public void init(@NonNull final Context context, @NonNull final OConfig oConfig) {
        if (context == null) {
            OLog.e(TAG, "init error as ctx is null", new Object[0]);
            return;
        }
        String packageName = context.getPackageName();
        GlobalOrange.isTaobaoPackage = !TextUtils.isEmpty(packageName) && packageName.equals(AgooConstants.TAOBAO_PACKAGE);
        GlobalOrange.isMainProcess = AndroidUtil.isMainProcess(context);
        boolean z = (context.getApplicationInfo().flags & 2) != 0;
        if (z) {
            OLog.isUseTlog = false;
        } else {
            OLog.isUseTlog = true;
        }
        OLog.i(TAG, "init", "isDebug", Boolean.valueOf(z), "isMainProcess", Boolean.valueOf(GlobalOrange.isMainProcess));
        if (TextUtils.isEmpty(oConfig.appKey) || TextUtils.isEmpty(oConfig.appVersion)) {
            OLog.e(TAG, "init error as appKey or appVersion is empty", new Object[0]);
            return;
        }
        if (this.mContext == null) {
            this.mContext = context.getApplicationContext();
        }
        OThreadFactory.execute(new Runnable() {
            public void run() {
                OrangeConfigImpl.this.asyncGetRemoteService(context, true);
                if (OrangeConfigImpl.this.mRemoteService != null) {
                    try {
                        OrangeConfigImpl.this.sendFailItems();
                        OrangeConfigImpl.this.mRemoteService.init(oConfig);
                    } catch (Throwable th) {
                        OLog.e(OrangeConfigImpl.TAG, "asyncInit", th, new Object[0]);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void sendFailItems() {
        if (this.mRemoteService != null) {
            try {
                OLog.i(TAG, "sendFailItems start", new Object[0]);
                long currentTimeMillis = System.currentTimeMillis();
                if (this.mFailUserId != null) {
                    this.mRemoteService.setUserId(this.mFailUserId);
                    this.mFailUserId = null;
                }
                if (this.mFailNamespaces.size() > 0) {
                    this.mRemoteService.addFails((String[]) this.mFailNamespaces.toArray(new String[this.mFailNamespaces.size()]));
                }
                this.mFailNamespaces.clear();
                for (Map.Entry next : this.mFailListeners.entrySet()) {
                    for (OrangeConfigListenerStub orangeConfigListenerStub : (Set) next.getValue()) {
                        this.mRemoteService.registerListener((String) next.getKey(), orangeConfigListenerStub, orangeConfigListenerStub.isAppend());
                    }
                }
                this.mFailListeners.clear();
                if (GlobalOrange.isMainProcess) {
                    for (OCandidate next2 : this.mFailCandidates) {
                        this.mRemoteService.addCandidate(next2.getKey(), next2.getClientVal(), next2.getCompare());
                    }
                }
                this.mFailCandidates.clear();
                OLog.i(TAG, "sendFailItems end", "cost", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            } catch (Throwable th) {
                OLog.e(TAG, "sendFailItems", th, new Object[0]);
            }
        }
    }

    public Map<String, String> getSyncConfigs(@NonNull String str, long j) {
        Map<String, String> configs = getConfigs(str);
        if (configs != null) {
            return configs;
        }
        final HashMap hashMap = new HashMap();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            OLog.e(TAG, "getSyncConfigs in main thread", "namespace", str, "timeout", Long.valueOf(j));
        } else if (OLog.isPrintLog(0)) {
            OLog.v(TAG, "getSyncConfigs", "namespace", str, "timeout", Long.valueOf(j));
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        registerListener(new String[]{str}, new OConfigListener() {
            public void onConfigUpdate(String str, Map<String, String> map) {
                countDownLatch.countDown();
                hashMap.putAll(OrangeConfigImpl.this.getConfigs(str));
            }
        }, false);
        if (j > 0) {
            try {
                countDownLatch.await(j, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                OLog.w(TAG, "getSyncConfigs", e, new Object[0]);
            }
        } else {
            countDownLatch.await();
        }
        return hashMap;
    }

    public String getConfig(@NonNull String str, @NonNull String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            OLog.e(TAG, "getConfig error as param is empty", new Object[0]);
            return str3;
        }
        asyncGetRemoteService(this.mContext, false);
        if (this.mRemoteService == null) {
            if (this.mFailNamespaces.add(str)) {
                OLog.w(TAG, "getConfig wait", "namespace", str);
            }
        } else if (GlobalOrange.isTaobaoPackage && !GlobalOrange.isMainProcess && this.mBlackNamespaces.contains(str)) {
            return str3;
        } else {
            try {
                return this.mRemoteService.getConfig(str, str2, str3);
            } catch (Throwable th) {
                OLog.e(TAG, "getConfig", th, new Object[0]);
            }
        }
        return str3;
    }

    public Map<String, String> getConfigs(@NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            OLog.e(TAG, "getConfig error as param is empty", new Object[0]);
            return null;
        }
        asyncGetRemoteService(this.mContext, false);
        if (this.mRemoteService == null) {
            if (this.mFailNamespaces.add(str)) {
                OLog.w(TAG, "getConfigs wait", "namespace", str);
            }
        } else if (GlobalOrange.isTaobaoPackage && !GlobalOrange.isMainProcess && this.mBlackNamespaces.contains(str)) {
            return null;
        } else {
            try {
                return this.mRemoteService.getConfigs(str);
            } catch (Throwable th) {
                OLog.e(TAG, "getConfigs", th, new Object[0]);
            }
        }
        return null;
    }

    public String getSyncCustomConfig(@NonNull String str, final String str2, long j) {
        final StringBuilder sb = new StringBuilder(str2);
        getCustomConfig(str, str2);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        registerListener(new String[]{str}, new OConfigListener() {
            public void onConfigUpdate(String str, Map<String, String> map) {
                countDownLatch.countDown();
                sb.setLength(0);
                sb.append(OrangeConfigImpl.this.getCustomConfig(str, str2));
            }
        }, false);
        if (j > 0) {
            try {
                countDownLatch.await(j, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                OLog.w(TAG, "getSyncCustomConfig", e, new Object[0]);
            }
        } else {
            countDownLatch.await();
        }
        return sb.toString();
    }

    public String getCustomConfig(@NonNull String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            OLog.e(TAG, "getCustomConfig error as param is empty", new Object[0]);
            return null;
        }
        asyncGetRemoteService(this.mContext, false);
        if (this.mRemoteService == null) {
            if (this.mFailNamespaces.add(str)) {
                OLog.w(TAG, "getCustomConfig wait", "namespace", str);
            }
        } else if (GlobalOrange.isTaobaoPackage && !GlobalOrange.isMainProcess && this.mBlackNamespaces.contains(str)) {
            return null;
        } else {
            try {
                return this.mRemoteService.getCustomConfig(str, str2);
            } catch (Throwable th) {
                OLog.e(TAG, "getCustomConfig", th, new Object[0]);
            }
        }
        return str2;
    }

    public void registerListener(@NonNull String[] strArr, @NonNull OrangeConfigListener orangeConfigListener) {
        regCommonListener(strArr, orangeConfigListener, true);
    }

    public void registerListener(@NonNull String[] strArr, @NonNull OrangeConfigListenerV1 orangeConfigListenerV1) {
        regCommonListener(strArr, orangeConfigListenerV1, true);
    }

    public void registerListener(@NonNull String[] strArr, @NonNull OConfigListener oConfigListener, boolean z) {
        regCommonListener(strArr, oConfigListener, z);
    }

    private <T extends OBaseListener> void regCommonListener(final String[] strArr, T t, boolean z) {
        if (strArr == null || strArr.length == 0 || t == null) {
            OLog.e(TAG, "registerListener error as param null", new Object[0]);
            return;
        }
        final OrangeConfigListenerStub orangeConfigListenerStub = new OrangeConfigListenerStub(t, z);
        if (this.mRemoteService == null) {
            OLog.w(TAG, "registerListener wait", "namespaces", Arrays.asList(strArr));
            for (String failListenerStubByKey : strArr) {
                getFailListenerStubByKey(failListenerStubByKey).add(orangeConfigListenerStub);
            }
            return;
        }
        OThreadFactory.execute(new Runnable() {
            public void run() {
                OrangeConfigImpl.this.registerListener(strArr, orangeConfigListenerStub);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void registerListener(String[] strArr, OrangeConfigListenerStub orangeConfigListenerStub) {
        if (this.mRemoteService != null && strArr != null && strArr.length != 0 && orangeConfigListenerStub != null) {
            for (String registerListener : strArr) {
                try {
                    this.mRemoteService.registerListener(registerListener, orangeConfigListenerStub, orangeConfigListenerStub.isAppend());
                } catch (Throwable th) {
                    OLog.w(TAG, "registerListener", th, new Object[0]);
                }
            }
        }
    }

    private Set<OrangeConfigListenerStub> getFailListenerStubByKey(String str) {
        Set<OrangeConfigListenerStub> set = this.mFailListeners.get(str);
        if (set != null) {
            return set;
        }
        HashSet hashSet = new HashSet();
        this.mFailListeners.put(str, hashSet);
        return hashSet;
    }

    public void unregisterListener(@NonNull String[] strArr, OrangeConfigListenerV1 orangeConfigListenerV1) {
        if (strArr == null || strArr.length == 0 || orangeConfigListenerV1 == null) {
            OLog.e(TAG, "unregisterListenerV1 error as param null", new Object[0]);
        } else if (this.mRemoteService != null) {
            try {
                for (String unregisterListener : strArr) {
                    this.mRemoteService.unregisterListener(unregisterListener, new OrangeConfigListenerStub(orangeConfigListenerV1));
                }
            } catch (Throwable th) {
                OLog.e(TAG, "unregisterListenerV1", th, new Object[0]);
            }
        } else {
            OLog.w(TAG, "unregisterListenerV1 fail", new Object[0]);
        }
    }

    public void unregisterListener(@NonNull String[] strArr, OConfigListener oConfigListener) {
        if (strArr == null || strArr.length == 0 || oConfigListener == null) {
            OLog.e(TAG, "unregisterListener error as param null", new Object[0]);
        } else if (this.mRemoteService != null) {
            try {
                for (String unregisterListener : strArr) {
                    this.mRemoteService.unregisterListener(unregisterListener, new OrangeConfigListenerStub(oConfigListener));
                }
            } catch (Throwable th) {
                OLog.e(TAG, "unregisterListener", th, new Object[0]);
            }
        } else {
            OLog.w(TAG, "unregisterListener fail", new Object[0]);
        }
    }

    public void unregisterListener(@NonNull String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            OLog.e(TAG, "unregisterListeners error as namespaces is null", new Object[0]);
        } else if (this.mRemoteService != null) {
            try {
                for (String unregisterListeners : strArr) {
                    this.mRemoteService.unregisterListeners(unregisterListeners);
                }
            } catch (Throwable th) {
                OLog.e(TAG, "unregisterListeners", th, new Object[0]);
            }
        } else {
            OLog.w(TAG, "unregisterListeners fail", new Object[0]);
        }
    }

    public void forceCheckUpdate() {
        if (this.mRemoteService != null) {
            try {
                this.mRemoteService.forceCheckUpdate();
            } catch (Throwable th) {
                OLog.e(TAG, "forceCheckUpdate", th, new Object[0]);
            }
        } else {
            OLog.w(TAG, "forceCheckUpdate fail", new Object[0]);
        }
    }

    public void enterForeground() {
        forceCheckUpdate();
    }

    public void setUserId(String str) {
        if (str == null) {
            str = "";
        }
        if (this.mRemoteService == null) {
            this.mFailUserId = str;
            return;
        }
        try {
            this.mRemoteService.setUserId(str);
        } catch (Throwable th) {
            OLog.e(TAG, "setUserId", th, new Object[0]);
        }
    }

    public void addCandidate(@NonNull OCandidate oCandidate) {
        if (oCandidate == null) {
            OLog.e(TAG, "addCandidate error as candidate is null", new Object[0]);
            return;
        }
        String key = oCandidate.getKey();
        if (OConstant.CANDIDATE_APPVER.equals(key) || OConstant.CANDIDATE_OSVER.equals(key) || OConstant.CANDIDATE_MANUFACTURER.equals(key) || OConstant.CANDIDATE_BRAND.equals(key) || OConstant.CANDIDATE_MODEL.equals(key) || "did_hash".equals(key)) {
            OLog.e(TAG, "addCandidate fail as not allow override build-in candidate", "key", key);
        } else if (this.mRemoteService != null) {
            try {
                if (GlobalOrange.isMainProcess) {
                    this.mRemoteService.addCandidate(oCandidate.getKey(), oCandidate.getClientVal(), oCandidate.getCompare());
                }
            } catch (Throwable th) {
                OLog.e(TAG, "addCandidate", th, new Object[0]);
            }
        } else if (this.mFailCandidates.add(oCandidate)) {
            OLog.w(TAG, "addCandidate wait", "candidate", oCandidate);
        }
    }

    public void enterBackground() {
        OLog.e(TAG, "enterBackground api is @Deprecated", new Object[0]);
    }

    public void setAppSecret(String str) {
        OLog.e(TAG, "setAppSecret api is @Deprecated, please set appSecret in init(OConfig config) api", new Object[0]);
    }

    public void setIndexUpdateMode(int i) {
        OLog.e(TAG, "setIndexUpdateMode api is @Deprecated, please set indexUpdateMode in init(OConfig config) api", new Object[0]);
    }

    public void setHosts(List<String> list) {
        OLog.e(TAG, "setHosts api is @Deprecated, please set probeHosts in init(OConfig config) api", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void asyncGetRemoteService(Context context, boolean z) {
        if (this.mRemoteService == null) {
            bindRemoteService(context);
            if (z) {
                if (this.mBindServiceLock == null) {
                    this.mBindServiceLock = new CountDownLatch(1);
                }
                if (this.mRemoteService == null) {
                    try {
                        this.mBindServiceLock.await(20, TimeUnit.SECONDS);
                    } catch (Throwable th) {
                        OLog.e(TAG, "syncGetBindService", th, new Object[0]);
                    }
                    if (this.mRemoteService == null && context != null && GlobalOrange.isMainProcess) {
                        OLog.w(TAG, "syncGetBindService", "bind service timeout local stub in main process");
                        this.mRemoteService = new OrangeApiServiceStub(context);
                        OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_EXCEPTION, String.valueOf(System.currentTimeMillis() - 0), OConstant.CODE_POINT_EXP_BIND_SERVICE, "bind fail and start local stub");
                    }
                }
            }
        }
    }

    private void bindRemoteService(Context context) {
        if (context != null && this.mRemoteService == null && this.mIsBindingService.compareAndSet(false, true)) {
            OLog.i(TAG, "bindRemoteService start", new Object[0]);
            try {
                Intent intent = new Intent(context, OrangeApiService.class);
                intent.setAction(OrangeApiService.class.getName());
                intent.addCategory("android.intent.category.DEFAULT");
                if (!context.bindService(intent, this.mConnection, 1)) {
                    OLog.w(TAG, "bindRemoteService fail", new Object[0]);
                }
            } catch (Throwable th) {
                OLog.e(TAG, "bindRemoteService", th, new Object[0]);
            }
        }
    }
}
