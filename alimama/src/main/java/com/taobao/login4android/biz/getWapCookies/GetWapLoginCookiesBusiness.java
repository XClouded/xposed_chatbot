package com.taobao.login4android.biz.getWapCookies;

import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.biz.getWapCookies.mtop.GetWapLoginCookiesResponseData;
import com.taobao.login4android.session.SessionManager;

public class GetWapLoginCookiesBusiness {
    public String[] getWapCookies(String str, boolean z) {
        SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
        if (!instance.checkSessionValid()) {
            return null;
        }
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.GET_WAP_LOGIN_COOKIES;
        rpcRequest.VERSION = "1.0";
        rpcRequest.NEED_SESSION = true;
        rpcRequest.NEED_ECODE = true;
        rpcRequest.addParam("apiReferer", str);
        rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
        try {
            GetWapLoginCookiesResponseData getWapLoginCookiesResponseData = (GetWapLoginCookiesResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, GetWapLoginCookiesResponseData.class, instance.getUserId());
            if (getWapLoginCookiesResponseData == null || getWapLoginCookiesResponseData.result == null) {
                return null;
            }
            String[] strArr = getWapLoginCookiesResponseData.result;
            if (z && strArr == null) {
                ApiReferer.Refer refer = new ApiReferer.Refer();
                refer.eventName = "refreshCookiesFailed";
                SessionManager.getInstance(DataProviderFactory.getApplicationContext()).appendEventTrace(JSON.toJSONString(refer));
            }
            return strArr;
        } catch (Exception unused) {
            return null;
        }
    }
}
