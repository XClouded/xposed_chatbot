package com.taobao.weex.module;

import com.alibaba.aliweex.adapter.component.WXIExternalComponentGetter;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.component.WXComponent;

public class TBLiveComponentGetter extends WXIExternalComponentGetter {
    private static final String VIDEO_CLASSNAME = "com.taobao.tbliveweexvideo.TBLiveWeexService";
    private static final String VIDEO_TAG = "video";

    public Class<? extends WXComponent> getExternalComponentClass(String str, WXSDKInstance wXSDKInstance) {
        if ("video".equals(str)) {
            try {
                return ((IExternalComponentGetter) getClass().getClassLoader().loadClass(VIDEO_CLASSNAME).newInstance()).getExternalComponentClass(str, wXSDKInstance);
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        } else if (!"tbliveMiniRoom".equals(str)) {
            return null;
        } else {
            try {
                return ((IExternalComponentGetter) getClass().getClassLoader().loadClass(VIDEO_CLASSNAME).newInstance()).getExternalComponentClass(str, wXSDKInstance);
            } catch (Throwable th2) {
                th2.printStackTrace();
                return null;
            }
        }
    }
}
