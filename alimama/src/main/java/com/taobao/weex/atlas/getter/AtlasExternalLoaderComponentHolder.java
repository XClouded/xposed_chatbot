package com.taobao.weex.atlas.getter;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.ExternalLoaderComponentHolder;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

public class AtlasExternalLoaderComponentHolder extends ExternalLoaderComponentHolder {
    private IExternalComponentGetter clzGetter;
    private WeakReference<ClassLoader> lastInstance = new WeakReference<>((Object) null);
    private String mType;

    public AtlasExternalLoaderComponentHolder(String str, IExternalComponentGetter iExternalComponentGetter) {
        super(str, iExternalComponentGetter);
        this.clzGetter = iExternalComponentGetter;
        this.mType = str;
    }

    public synchronized WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!(wXSDKInstance.getContext() == null || this.lastInstance.get() == wXSDKInstance.getContext().getClassLoader())) {
            this.lastInstance = new WeakReference<>(wXSDKInstance.getContext().getClassLoader());
            this.clzGetter.getExternalComponentClass(this.mType, wXSDKInstance);
        }
        return super.createInstance(wXSDKInstance, wXVContainer, basicComponentData);
    }
}
