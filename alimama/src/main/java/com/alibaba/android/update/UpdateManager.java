package com.alibaba.android.update;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyBase;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.proxy.Constants;
import com.alibaba.android.update.proxy.UpdateServiceProxy;
import com.taobao.bspatch.BSPatch;

public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private static UpdateManager instance;
    private ILogger logger = ((ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER));
    private IUpdateCallback mDefaultCallback;
    private IUpdateDelegate mDefaultDelegate;
    private ServiceProxyBase mServiceProxy;
    private IUpdateCallback mSilentCallback;
    private IUpdateDelegate mSilentDelegate;

    private UpdateManager() {
    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            instance = new UpdateManager();
            try {
                Class.forName("android.os.AsyncTask");
            } catch (Throwable unused) {
            }
        }
        return instance;
    }

    public boolean setDownloadDirectory(Context context, String str) {
        ILogger iLogger = this.logger;
        iLogger.logd(TAG, "download directory: " + str);
        return UpdateUtils.setDownloadDirectory(context, str);
    }

    public UpdateManager setSilent(IUpdateDelegate iUpdateDelegate, IUpdateCallback iUpdateCallback) {
        this.mSilentDelegate = iUpdateDelegate;
        this.mSilentCallback = iUpdateCallback;
        return this;
    }

    @Deprecated
    public void execute(Context context, IUpdateDelegate iUpdateDelegate, IUpdateCallback iUpdateCallback) {
        if (context != null && iUpdateDelegate != null) {
            if (this.mServiceProxy == null) {
                setProxy(context, (ServiceProxyBase) null);
            }
            iUpdateDelegate.execute(context, iUpdateCallback);
        }
    }

    public UpdateManager setDelegate(IUpdateDelegate iUpdateDelegate) {
        this.mDefaultDelegate = iUpdateDelegate;
        return this;
    }

    public UpdateManager setCallback(IUpdateCallback iUpdateCallback) {
        this.mDefaultCallback = iUpdateCallback;
        return this;
    }

    public void execute(Context context) {
        if (context != null) {
            if (this.mServiceProxy == null) {
                setProxy(context, (ServiceProxyBase) null);
            }
            new DefaultUpdateProcessor(context).process(this.mDefaultDelegate, this.mDefaultCallback);
        }
    }

    public void executeInSilent(Context context) {
        this.logger.logd(TAG, "update->UpdateManager: executeInSilent");
        if (this.mServiceProxy == null) {
            setProxy(context, (ServiceProxyBase) null);
        }
        new DefaultUpdateProcessor(context).process(this.mSilentDelegate, this.mSilentCallback);
    }

    @Deprecated
    public void startService(Context context, UpdateActionType updateActionType, String str, String str2, String str3) {
        if (context != null && updateActionType != null && !TextUtils.isEmpty(str)) {
            Intent intent = new Intent(context, UpdateService.class);
            intent.setAction(updateActionType.toString());
            intent.putExtra(UpdateService.EXTRA_DOWNLOAD_URL, str);
            intent.putExtra(UpdateService.EXTRA_DOWNLOAD_FILE_NAME, str2);
            intent.putExtra(UpdateService.EXTRA_DOWNLOAD_TITLE_NAME, str3);
            this.logger.logd(TAG, "update->startService");
            intent.setPackage(context.getPackageName());
            context.startService(intent);
        }
    }

    public boolean testPatch(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || BSPatch.bspatch(str, str2, str3) != 1) {
            return false;
        }
        return true;
    }

    public static String getFileNameByString(String str) {
        return UpdateUtils.getFileNameByString(str);
    }

    public UpdateManager switchSilentOn(Context context, boolean z) {
        SharedPreferences.Editor edit = UpdatePreference.getInstance(context).getSharedPreferences().edit();
        edit.putBoolean(UpdatePreference.KEY_SWITCH_SILIENT_ON, z);
        edit.commit();
        return this;
    }

    public UpdateManager setProxy(Context context, ServiceProxyBase serviceProxyBase) {
        if (serviceProxyBase == null) {
            this.mServiceProxy = new UpdateServiceProxy(context);
        } else {
            this.mServiceProxy = serviceProxyBase;
        }
        ServiceProxyFactory.registerProxy(Constants.PROXY_UPDATE_CORE, this.mServiceProxy);
        return this;
    }

    public void onDestroy() {
        setCallback((IUpdateCallback) null);
    }
}
