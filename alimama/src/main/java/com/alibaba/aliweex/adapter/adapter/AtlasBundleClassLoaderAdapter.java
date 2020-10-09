package com.alibaba.aliweex.adapter.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.ClassLoaderAdapter;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.IExternalModuleGetter;
import com.taobao.weex.ui.component.WXComponent;
import java.util.List;

public class AtlasBundleClassLoaderAdapter extends ClassLoaderAdapter {
    public Class<? extends WXModule> getModuleClass(String str, String str2, Context context) {
        Class<? extends WXModule> externalModuleClass = getExternalModuleClass(str, context);
        if (externalModuleClass != null) {
            return externalModuleClass;
        }
        Class<? extends WXModule> externalModuleClass2 = getExternalModuleClass(str2, context);
        if (externalModuleClass2 != null) {
            return externalModuleClass2;
        }
        return super.getModuleClass(str, str2, context);
    }

    public Class<? extends WXComponent> getComponentClass(String str, String str2, WXSDKInstance wXSDKInstance) {
        Class<? extends WXComponent> externalComponentClass = getExternalComponentClass(str, wXSDKInstance);
        if (externalComponentClass != null) {
            return externalComponentClass;
        }
        Class<? extends WXComponent> externalComponentClass2 = getExternalComponentClass(str2, wXSDKInstance);
        if (externalComponentClass2 != null) {
            return externalComponentClass2;
        }
        return super.getComponentClass(str, str2, wXSDKInstance);
    }

    private Class<? extends WXComponent> getExternalComponentClass(String str, WXSDKInstance wXSDKInstance) {
        Context context;
        ServiceInfo serviceInfo;
        if (!(wXSDKInstance == null || (context = wXSDKInstance.getContext()) == null || TextUtils.isEmpty(str))) {
            Intent intent = new Intent(str);
            intent.setPackage(context.getPackageName());
            List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 4);
            if (!(queryIntentServices == null || queryIntentServices.isEmpty() || (serviceInfo = queryIntentServices.get(0).serviceInfo) == null || serviceInfo.name == null)) {
                try {
                    return ((IExternalComponentGetter) getClass().getClassLoader().loadClass(serviceInfo.name).newInstance()).getExternalComponentClass(str, wXSDKInstance);
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    private Class<? extends WXModule> getExternalModuleClass(String str, Context context) {
        ServiceInfo serviceInfo;
        if (context != null && !TextUtils.isEmpty(str)) {
            Intent intent = new Intent(str);
            intent.setPackage(context.getPackageName());
            List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 4);
            if (!(queryIntentServices == null || queryIntentServices.isEmpty() || (serviceInfo = queryIntentServices.get(0).serviceInfo) == null || serviceInfo.name == null)) {
                try {
                    return ((IExternalModuleGetter) getClass().getClassLoader().loadClass(serviceInfo.name).newInstance()).getExternalModuleClass(str, context);
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
