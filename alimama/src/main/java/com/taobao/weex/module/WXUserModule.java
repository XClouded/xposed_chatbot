package com.taobao.weex.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.aliweex.adapter.IUserModuleAdapter;
import com.alipay.sdk.util.e;
import com.taobao.login4android.api.Login;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.weex.bridge.JSCallback;
import java.util.HashMap;

public class WXUserModule implements IUserModuleAdapter {
    public void logout(Context context, JSCallback jSCallback) {
    }

    public void getUserInfo(Context context, JSCallback jSCallback) {
        String nick = Login.getNick();
        String userId = Login.getUserId();
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        if (!isLogin() || TextUtils.isEmpty(nick) || TextUtils.isEmpty(userId)) {
            hashMap.put("isLogin", "false");
        } else {
            hashMap.put("isLogin", "true");
            hashMap.put("nick", nick);
            hashMap.put("userId", userId);
            hashMap2.put("nick", nick);
            hashMap2.put("userId", userId);
            hashMap.put(ApiConstants.ApiField.INFO, hashMap2);
        }
        jSCallback.invoke(hashMap);
    }

    public void login(Context context, JSCallback jSCallback) {
        if (isLogin()) {
            HashMap hashMap = new HashMap();
            hashMap.put("status", "success");
            HashMap hashMap2 = new HashMap();
            String nick = Login.getNick();
            String userId = Login.getUserId();
            hashMap2.put("nick", nick);
            hashMap2.put("userId", userId);
            hashMap.put(ApiConstants.ApiField.INFO, hashMap2);
            jSCallback.invoke(hashMap);
            return;
        }
        LoginReceiver loginReceiver = new LoginReceiver(context);
        loginReceiver.setCallback(jSCallback);
        LoginBroadcastHelper.registerLoginReceiver(context, loginReceiver);
        Login.login(true);
    }

    private boolean isLogin() {
        return !TextUtils.isEmpty(Login.getSid()) && Login.checkSessionValid();
    }

    static class LoginReceiver extends BroadcastReceiver {
        private JSCallback mLoginCallback;
        private Context mWXSDKInstance;

        public void setCallback(JSCallback jSCallback) {
            this.mLoginCallback = jSCallback;
        }

        public JSCallback getCallback() {
            return this.mLoginCallback;
        }

        LoginReceiver(Context context) {
            this.mWXSDKInstance = context;
        }

        public void onReceive(Context context, Intent intent) {
            LoginAction valueOf;
            if (intent != null && (valueOf = LoginAction.valueOf(intent.getAction())) != null) {
                switch (valueOf) {
                    case NOTIFY_LOGIN_CANCEL:
                    case NOTIFY_LOGIN_FAILED:
                        LoginBroadcastHelper.unregisterLoginReceiver(this.mWXSDKInstance, this);
                        HashMap hashMap = new HashMap();
                        hashMap.put("status", e.a);
                        if (this.mLoginCallback != null) {
                            this.mLoginCallback.invoke(hashMap);
                            return;
                        }
                        return;
                    case NOTIFY_LOGOUT:
                        LoginBroadcastHelper.unregisterLoginReceiver(this.mWXSDKInstance, this);
                        HashMap hashMap2 = new HashMap();
                        hashMap2.put("status", "success");
                        if (this.mLoginCallback != null) {
                            this.mLoginCallback.invoke(hashMap2);
                            return;
                        }
                        return;
                    case NOTIFY_LOGIN_SUCCESS:
                        LoginBroadcastHelper.unregisterLoginReceiver(context, this);
                        HashMap hashMap3 = new HashMap();
                        hashMap3.put("status", "success");
                        HashMap hashMap4 = new HashMap();
                        String nick = Login.getNick();
                        String userId = Login.getUserId();
                        hashMap4.put("nick", nick);
                        hashMap4.put("userId", userId);
                        hashMap3.put(ApiConstants.ApiField.INFO, hashMap4);
                        if (this.mLoginCallback != null) {
                            this.mLoginCallback.invoke(hashMap3);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
