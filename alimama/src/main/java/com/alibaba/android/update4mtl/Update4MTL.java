package com.alibaba.android.update4mtl;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.anynetwork.core.ANConfig;
import com.alibaba.android.anynetwork.core.AnyNetworkManager;
import com.alibaba.android.anynetwork.core.IANService;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyBase;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.IUpdateCallback;
import com.alibaba.android.update.UpdateManager;
import com.alibaba.android.update4mtl.proxy.Update4MTLProxy;

public class Update4MTL {
    private static final String TAG = "Update4MTL";
    private static Update4MTL instance;
    private ILogger logger;
    private String mAppGroup = "";
    private ServiceProxyBase update4mtlProxy;

    private Update4MTL() {
        if (this.logger == null) {
            this.logger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        }
        this.logger.logd(TAG, TAG);
    }

    public static Update4MTL getInstance() {
        if (instance == null) {
            instance = new Update4MTL();
        }
        return instance;
    }

    public Update4MTL init(Context context, String str, String str2, int i, String str3, IANService iANService) {
        if (context == null) {
            return this;
        }
        if (this.update4mtlProxy == null) {
            this.update4mtlProxy = new Update4MTLProxy(context);
            ServiceProxyFactory.registerProxy(Constants.PROXY_UPDATE4MTL, this.update4mtlProxy);
        }
        this.mAppGroup = str;
        AnyNetworkManager.setConfig(new ANConfig().setNetworkMtopEnvironment(i).setNetworkMtopTtid(str2));
        AnyNetworkManager.getGlobalAnyNetwork().installService(str3, iANService);
        return this;
    }

    public void execute(Context context, UpdateRequestParams updateRequestParams, IUpdateCallback iUpdateCallback) {
        if (context == null || iUpdateCallback == null) {
            ILogger iLogger = this.logger;
            iLogger.logd(TAG, "execute: input params is invalid, callback: " + iUpdateCallback);
            return;
        }
        UpdateManager.getInstance().setDelegate(new DefaultUpdateDelegate(this.mAppGroup, updateRequestParams)).setCallback(iUpdateCallback).execute(context);
    }

    public Update4MTL switchSilentOn(Context context, boolean z) {
        UpdateManager.getInstance().switchSilentOn(context, z);
        return this;
    }

    public Update4MTL setDownloadDirectory(Context context, String str) {
        if (!TextUtils.isEmpty(str) && !UpdateManager.getInstance().setDownloadDirectory(context, str)) {
            ILogger iLogger = this.logger;
            iLogger.logw(TAG, "set directory failed, set path: " + str);
        }
        return this;
    }

    public void setSilentCallback(UpdateRequestParams updateRequestParams, IUpdateCallback iUpdateCallback) {
        UpdateManager.getInstance().setSilent(new DefaultUpdateDelegate(this.mAppGroup, updateRequestParams), iUpdateCallback);
    }

    public Update4MTL setProxy(Context context, ServiceProxyBase serviceProxyBase) {
        UpdateManager.getInstance().setProxy(context, serviceProxyBase);
        return this;
    }

    public Update4MTL set4MTLProxy(ServiceProxyBase serviceProxyBase) {
        if (serviceProxyBase != null) {
            this.update4mtlProxy = serviceProxyBase;
            ServiceProxyFactory.registerProxy(Constants.PROXY_UPDATE4MTL, this.update4mtlProxy);
        }
        return this;
    }

    public void onDestroy() {
        setProxy((Context) null, (ServiceProxyBase) null);
        UpdateManager.getInstance().onDestroy();
    }
}
