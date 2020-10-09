package com.alimama.union.app.infrastructure.weex;

import android.content.Context;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.aliweex.adapter.IUserModuleAdapter;
import com.alimama.union.app.aalogin.DefaultLoginListener;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.model.User;
import com.taobao.weex.bridge.JSCallback;
import java.util.HashMap;

public class UserModuleAdapter implements IUserModuleAdapter {
    private final ILogin login;

    public UserModuleAdapter(ILogin iLogin) {
        this.login = iLogin;
    }

    public void getUserInfo(Context context, JSCallback jSCallback) {
        if (this.login.checkSessionValid()) {
            User user = this.login.getUser();
            HashMap hashMap = new HashMap();
            hashMap.put("isLogin", true);
            HashMap hashMap2 = new HashMap();
            hashMap2.put("userId", user.getUserId());
            hashMap2.put("nick", user.getUserNick());
            hashMap.put(ApiConstants.ApiField.INFO, hashMap2);
            jSCallback.invoke(hashMap);
            return;
        }
        HashMap hashMap3 = new HashMap();
        hashMap3.put("isLogin", false);
        jSCallback.invoke(hashMap3);
    }

    public void login(Context context, JSCallback jSCallback) {
        this.login.showLoginChooserUI();
    }

    public void logout(Context context, final JSCallback jSCallback) {
        this.login.logout(new DefaultLoginListener() {
            public void onLogoutSuccess() {
                HashMap hashMap = new HashMap();
                hashMap.put("status", "success");
                jSCallback.invoke(hashMap);
            }
        });
    }
}
