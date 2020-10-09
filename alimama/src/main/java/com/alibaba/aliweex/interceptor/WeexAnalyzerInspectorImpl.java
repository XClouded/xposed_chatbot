package com.alibaba.aliweex.interceptor;

import android.content.Context;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.interceptor.IWeexAnalyzerInspector;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Collections;
import java.util.Map;

public class WeexAnalyzerInspectorImpl implements IWeexAnalyzerInspector {
    private static final String TAG = "NetworkInspectorImpl";
    private static WeexAnalyzerInspectorImpl sImpl;
    @Nullable
    private Object mSenderObj;

    private WeexAnalyzerInspectorImpl() {
        try {
            this.mSenderObj = Class.forName("com.taobao.weex.analyzer.core.NetworkEventSender").getDeclaredConstructor(new Class[]{Context.class}).newInstance(new Object[]{WXEnvironment.getApplication()});
        } catch (Exception e) {
            WXLogUtils.d(TAG, e.getMessage());
        }
    }

    public static WeexAnalyzerInspectorImpl createDefault() {
        if (sImpl == null) {
            synchronized (WeexAnalyzerInspectorImpl.class) {
                if (sImpl == null) {
                    sImpl = new WeexAnalyzerInspectorImpl();
                }
            }
        }
        return sImpl;
    }

    public void onRequest(String str, IWeexAnalyzerInspector.InspectorRequest inspectorRequest) {
        sendMessage("request", inspectorRequest.api, inspectorRequest.method, inspectorRequest.headers == null ? null : inspectorRequest.headers.toString(), Collections.singletonMap("bizType", str));
    }

    public void onResponse(String str, IWeexAnalyzerInspector.InspectorResponse inspectorResponse) {
        String str2;
        String str3 = inspectorResponse.api;
        StringBuilder sb = new StringBuilder();
        sb.append(inspectorResponse.statusCode);
        if (inspectorResponse.headers != null) {
            str2 = "|" + inspectorResponse.headers.toString();
        } else {
            str2 = "";
        }
        sb.append(str2);
        sendMessage(NetworkEventSender.TYPE_RESPONSE, str3, sb.toString(), inspectorResponse.data, Collections.singletonMap("bizType", str));
    }

    public boolean isEnabled() {
        return WXEnvironment.isApkDebugable() && this.mSenderObj != null;
    }

    private void sendMessage(String str, String str2, String str3, String str4, Map<String, String> map) {
        if (this.mSenderObj != null) {
            try {
                this.mSenderObj.getClass().getDeclaredMethod("sendMessage", new Class[]{String.class, String.class, String.class, String.class, Map.class}).invoke(this.mSenderObj, new Object[]{str, str2, str3, str4, map});
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }
}
