package com.ali.user.mobile.bind;

import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.h5.MtopAccountCenterUrlResponseData;
import com.ali.user.mobile.service.RpcService;
import com.ali.user.mobile.service.ServiceFactory;

public class BindSerivceImpl implements BindService {
    private static BindSerivceImpl instance;

    public static BindSerivceImpl getInstance() {
        if (instance == null) {
            instance = new BindSerivceImpl();
        }
        return instance;
    }

    private BindSerivceImpl() {
    }

    public MtopAccountCenterUrlResponseData bind(BindParam bindParam) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.API_NAME = ApiConstants.ApiName.ACCOUNT_BIND;
        rpcRequest.VERSION = "1.0";
        rpcRequest.addParam("bindId", bindParam.bindId);
        rpcRequest.addParam("havanaId", bindParam.havanaId);
        rpcRequest.addParam("realm", Integer.valueOf(bindParam.realm));
        return (MtopAccountCenterUrlResponseData) ((RpcService) ServiceFactory.getService(RpcService.class)).post(rpcRequest, MtopAccountCenterUrlResponseData.class);
    }
}
