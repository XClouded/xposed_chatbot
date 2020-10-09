package com.alibaba.android.update4mtl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.android.anynetwork.core.ANConfig;
import com.alibaba.android.anynetwork.core.ANRequest;
import com.alibaba.android.anynetwork.core.ANResponse;
import com.alibaba.android.anynetwork.core.AnyNetworkManager;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.IUpdateCallback;
import com.alibaba.android.update.IUpdateDelegate;
import com.alibaba.android.update.UpdateUtils;
import com.alibaba.android.update4mtl.proxy.AbstractService;
import com.taobao.accs.common.Constants;
import com.taobao.android.dinamic.DinamicConstant;
import java.util.HashMap;
import java.util.Map;

public class DefaultUpdateDelegate implements IUpdateDelegate {
    private static String ANDROID_VERSION = new Integer(Build.VERSION.SDK_INT).toString();
    private static final String API_GET_BASE_UPDATE_LIST = "mtop.atlas.getBaseUpdateList";
    private static final String API_VERSION = "1.0";
    private static final String TAG = "DefaultUpdateDelegate";
    private String mAppGroup = "";
    private ILogger mLogger;
    private UpdateRequestParams mParams;

    public void execute(Context context, IUpdateCallback iUpdateCallback) {
    }

    public DefaultUpdateDelegate(String str, UpdateRequestParams updateRequestParams) {
        if (this.mLogger == null) {
            this.mLogger = (ILogger) ServiceProxyFactory.getProxy().getService(ServiceProxy.COMMON_SERVICE_LOGGER);
        }
        this.mAppGroup = str;
        this.mParams = updateRequestParams;
    }

    public Object doInBackground(Context context, Object... objArr) {
        return doMybusiness(context);
    }

    private ANResponse doMybusiness(Context context) {
        HashMap hashMap = new HashMap();
        String str = getAppInfo(context).sourceDir;
        NetworkType currentNetworkType = NetworkType.getCurrentNetworkType(context);
        int value = currentNetworkType != null ? currentNetworkType.getValue() : 0;
        String str2 = "";
        ANConfig config = AnyNetworkManager.getConfig();
        if (config != null) {
            str2 = config.getNetworkMtopTtid();
            if (!TextUtils.isEmpty(str2) && str2.contains(DinamicConstant.DINAMIC_PREFIX_AT)) {
                str2 = str2.substring(0, str2.indexOf(DinamicConstant.DINAMIC_PREFIX_AT));
            }
        }
        hashMap.put("androidVersion", ANDROID_VERSION);
        hashMap.put("netStatus", new Integer(value).toString());
        hashMap.put("group", this.mAppGroup);
        hashMap.put("name", str2);
        AbstractService abstractService = null;
        ServiceProxy proxy = ServiceProxyFactory.getProxy(Constants.PROXY_UPDATE4MTL);
        if (proxy != null) {
            abstractService = (AbstractService) proxy.getService(Constants.UTIL_SERVICE);
        }
        if (abstractService == null) {
            hashMap.put("version", Utils.getClientVersion(context));
        } else {
            hashMap.put("version", abstractService.getClientVersion(context));
        }
        hashMap.put("md5", UpdateUtils.getMD5(str));
        addOptionalParams(hashMap);
        return AnyNetworkManager.getGlobalAnyNetwork().syncRequest(new ANRequest().setNetworkMtopUseHttps(true).setNetworkHttpMethod(2).setBaseType("mtop").setNetworkMtopApiName(API_GET_BASE_UPDATE_LIST).setNetworkMtopApiVersion("1.0").setNetworkMtopNeedEcode(false).setNetworkMtopDataJsonString(Utils.converMapToDataStr(hashMap)));
    }

    private static ApplicationInfo getAppInfo(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 8192);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addOptionalParams(Map<String, String> map) {
        if (this.mParams != null) {
            String str = this.mParams.get(UpdateRequestParams.PARAM_USER_ID);
            if (!TextUtils.isEmpty(str)) {
                map.put("userId", str);
            }
            String str2 = this.mParams.get(UpdateRequestParams.PARAM_BRAND);
            if (!TextUtils.isEmpty(str2)) {
                map.put("brand", str2);
            }
            String str3 = this.mParams.get(UpdateRequestParams.PARAM_CITY);
            if (!TextUtils.isEmpty(str3)) {
                map.put("city", str3);
            }
            String str4 = this.mParams.get(UpdateRequestParams.PARAM_LOCALE);
            if (!TextUtils.isEmpty(str4)) {
                map.put("local", str4);
            }
            String str5 = this.mParams.get(UpdateRequestParams.PARAM_MODEL);
            if (!TextUtils.isEmpty(str5)) {
                map.put(Constants.KEY_MODEL, str5);
            }
        }
    }
}
