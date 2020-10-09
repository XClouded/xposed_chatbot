package com.alibaba.aliweex.adapter.module;

import android.taobao.windvane.jsbridge.IJsApiFailedCallBack;
import android.taobao.windvane.jsbridge.IJsApiSucceedCallBack;
import com.alibaba.aliweex.interceptor.mtop.MtopTracker;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.utils.WXLogUtils;

public class WXWindVaneCallBack implements IJsApiSucceedCallBack, IJsApiFailedCallBack {
    private String InstanceId;
    private String callback;
    private boolean isMtop;
    private boolean transObject = true;

    WXWindVaneCallBack(String str, String str2, boolean z, boolean z2) {
        this.InstanceId = str;
        this.callback = str2;
        this.transObject = z;
        this.isMtop = z2;
    }

    public void succeed(String str) {
        MtopTracker popMtopTracker;
        if (this.transObject) {
            try {
                if (this.isMtop) {
                    WXStateRecord instance = WXStateRecord.getInstance();
                    String str2 = this.InstanceId;
                    instance.recordAction(str2, "windvane mtop succeed,calllBack:" + this.callback);
                }
                WXBridgeManager.getInstance().callback(this.InstanceId, this.callback, JSONObject.parse(str), false);
            } catch (Exception unused) {
            }
        } else {
            WXBridgeManager.getInstance().callback(this.InstanceId, this.callback, str);
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("WXWindVaneModule", "call succeed s:" + str);
        }
        if (WXEnvironment.isApkDebugable() && (popMtopTracker = WXWindVaneModule.popMtopTracker(this.callback)) != null) {
            popMtopTracker.onResponse(str);
        }
    }

    public void fail(String str) {
        MtopTracker popMtopTracker;
        if (this.transObject) {
            try {
                if (this.isMtop) {
                    WXStateRecord instance = WXStateRecord.getInstance();
                    String str2 = this.InstanceId;
                    instance.recordAction(str2, "windvane mtop failed,callBack" + this.callback + ",result" + str);
                }
                WXBridgeManager.getInstance().callback(this.InstanceId, this.callback, JSONObject.parse(str), false);
            } catch (Exception unused) {
            }
        } else {
            WXBridgeManager.getInstance().callback(this.InstanceId, this.callback, str);
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("WXWindVaneModule", "call fail s:" + str);
        }
        if (WXEnvironment.isApkDebugable() && (popMtopTracker = WXWindVaneModule.popMtopTracker(this.callback)) != null) {
            popMtopTracker.onFailed((String) null, str);
        }
    }
}
