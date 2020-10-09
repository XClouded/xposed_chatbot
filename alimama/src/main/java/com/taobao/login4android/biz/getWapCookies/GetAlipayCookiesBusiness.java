package com.taobao.login4android.biz.getWapCookies;

import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;
import com.taobao.login4android.biz.getAlipayCookies.mtop.GetAlipayCookiesResponseData;
import com.taobao.login4android.session.SessionManager;

public class GetAlipayCookiesBusiness {
    public GetAlipayCookiesResponseData getAlipayCookies() {
        SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
        if (!instance.checkSessionValid()) {
            return null;
        }
        try {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.API_NAME = ApiConstants.ApiName.GET_ALIPAY_COOKIES;
            rpcRequest.VERSION = "1.0";
            rpcRequest.NEED_SESSION = true;
            rpcRequest.NEED_ECODE = true;
            rpcRequest.addParam(ApiConstants.ApiField.UMID_TOKEN, AppInfo.getInstance().getUmidToken());
            rpcRequest.requestSite = DataProviderFactory.getDataProvider().getSite();
            return (GetAlipayCookiesResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, GetAlipayCookiesResponseData.class, instance.getUserId());
        } catch (Exception unused) {
            return null;
        }
    }
}
