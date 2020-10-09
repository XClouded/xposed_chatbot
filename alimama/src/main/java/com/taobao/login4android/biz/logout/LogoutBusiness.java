package com.taobao.login4android.biz.logout;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.login.model.LogoutRequest;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.SessionManager;
import com.taobao.login4android.session.constants.SessionConstants;
import java.util.HashMap;

public class LogoutBusiness {
    public void logout() {
        SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
        logout(instance.getLoginSite(), instance.getSid(), instance.getLoginToken(), instance.getUserId());
    }

    public void logout(int i, String str, String str2, String str3) {
        logout(i, str, (String) null, str2, str3);
    }

    public void logout(int i, String str, String str2, String str3, String str4) {
        try {
            RpcRequest rpcRequest = new RpcRequest();
            if (i == 17) {
                rpcRequest.API_NAME = ApiConstants.ApiName.GUC_LOGOUT;
                rpcRequest.VERSION = "1.0";
                LogoutRequest logoutRequest = new LogoutRequest();
                logoutRequest.apdid = AlipayInfo.getInstance().getApdid();
                logoutRequest.appKey = DataProviderFactory.getDataProvider().getAppkey();
                logoutRequest.autoLoginToken = str3;
                logoutRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
                logoutRequest.umidToken = AppInfo.getInstance().getUmidToken();
                logoutRequest.sid = str;
                logoutRequest.userId = str4;
                logoutRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
                rpcRequest.addParam("request", JSON.toJSONString(logoutRequest));
                rpcRequest.addParam("userId", str4);
            } else if (i == 4) {
                rpcRequest.API_NAME = ApiConstants.ApiName.OCEAN_LOGOUT;
                rpcRequest.VERSION = "1.0";
                LogoutRequest logoutRequest2 = new LogoutRequest();
                logoutRequest2.apdid = AlipayInfo.getInstance().getApdid();
                logoutRequest2.appKey = DataProviderFactory.getDataProvider().getAppkey();
                logoutRequest2.autoLoginToken = str3;
                logoutRequest2.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
                logoutRequest2.umidToken = AppInfo.getInstance().getUmidToken();
                logoutRequest2.sid = str;
                logoutRequest2.userId = str4;
                logoutRequest2.ttid = DataProviderFactory.getDataProvider().getTTID();
                rpcRequest.addParam("request", JSON.toJSONString(logoutRequest2));
                rpcRequest.addParam("userId", str4);
            } else if (i == 100) {
                rpcRequest.API_NAME = ApiConstants.ApiName.LOGOUT_COMMON;
                rpcRequest.VERSION = "1.0";
                LogoutRequest logoutRequest3 = new LogoutRequest();
                logoutRequest3.apdid = AlipayInfo.getInstance().getApdid();
                logoutRequest3.appKey = DataProviderFactory.getDataProvider().getAppkey();
                logoutRequest3.autoLoginToken = str3;
                logoutRequest3.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
                logoutRequest3.umidToken = AppInfo.getInstance().getUmidToken();
                logoutRequest3.sid = str;
                logoutRequest3.userId = str4;
                logoutRequest3.ttid = DataProviderFactory.getDataProvider().getTTID();
                logoutRequest3.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
                rpcRequest.addParam("request", JSON.toJSONString(logoutRequest3));
            } else {
                rpcRequest.API_NAME = ApiConstants.ApiName.LOGOUT;
                rpcRequest.VERSION = "1.0";
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.apdid = AlipayInfo.getInstance().getApdid();
                loginRequest.appKey = DataProviderFactory.getDataProvider().getAppkey();
                loginRequest.deviceId = DataProviderFactory.getDataProvider().getDeviceId();
                loginRequest.umidToken = AppInfo.getInstance().getUmidToken();
                loginRequest.sid = str;
                try {
                    loginRequest.userId = Long.parseLong(str4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loginRequest.ttid = DataProviderFactory.getDataProvider().getTTID();
                HashMap hashMap = new HashMap();
                if (!TextUtils.isEmpty(str2)) {
                    hashMap.put(SessionConstants.SUBSID, str2);
                }
                loginRequest.attributes = hashMap;
                rpcRequest.addParam("request", JSON.toJSONString(loginRequest));
                rpcRequest.addParam("userId", str4);
            }
            rpcRequest.NEED_SESSION = true;
            rpcRequest.NEED_ECODE = false;
            rpcRequest.requestSite = i;
            ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, (Class) null, str4);
            ((RpcService) ServiceFactory.getService(RpcService.class)).logout();
            ((RpcService) ServiceFactory.getService(RpcService.class)).setHeader("x-disastergrd", "");
        } catch (Exception e2) {
            LoginTLogAdapter.w("login.LogoutBusiness", "logout from server failed.", e2);
            e2.printStackTrace();
        }
    }
}
