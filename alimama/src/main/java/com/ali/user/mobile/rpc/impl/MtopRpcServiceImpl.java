package com.ali.user.mobile.rpc.impl;

import android.text.TextUtils;
import anet.channel.util.HttpSslUtil;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.callback.RpcRequestCallbackWithCode;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.service.RpcService;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import mtopsdk.mtop.deviceid.DeviceIDManager;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopParamType;
import mtopsdk.mtop.intf.MtopSetting;

public class MtopRpcServiceImpl implements RpcService {
    private String deviceId;

    public <T extends RpcResponse<?>> T post(RpcRequest rpcRequest, Class<T> cls) {
        return MTOPWrapper.getInstance().post(rpcRequest, cls);
    }

    public <T extends RpcResponse<?>> T post(RpcRequest rpcRequest, Class<T> cls, String str) {
        return MTOPWrapper.getInstance().post(rpcRequest, cls, str);
    }

    public void registerSessionInfo(String str, String str2, String str3) {
        Mtop.instance(DataProviderFactory.getApplicationContext()).registerSessionInfo(str, str2);
        MtopSetting.setParam(Mtop.Id.INNER, MtopParamType.HEADER, "x-disastergrd", str3);
    }

    public void setHeader(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            MtopSetting.setParam(Mtop.Id.INNER, MtopParamType.HEADER, str, str2);
        }
    }

    public String getDeviceId() {
        this.deviceId = SDKConfig.getInstance().getGlobalDeviceId();
        if (TextUtils.isEmpty(this.deviceId)) {
            try {
                DeviceIDManager.getInstance().getDeviceID(DataProviderFactory.getApplicationContext(), DataProviderFactory.getDataProvider().getAppkey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.deviceId;
    }

    public void logout() {
        Mtop.instance(DataProviderFactory.getApplicationContext()).logout();
    }

    public void sslDegrade() {
        HttpSslUtil.setHostnameVerifier(HttpSslUtil.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpSslUtil.setSslSocketFactory(HttpSslUtil.TRUST_ALL_SSL_SOCKET_FACTORY);
    }

    public <T extends RpcResponse<?>> void remoteBusiness(RpcRequest rpcRequest, Class<T> cls, RpcRequestCallback rpcRequestCallback) {
        remoteBusiness(rpcRequest, cls, rpcRequestCallback, true);
    }

    public <T extends RpcResponse<?>> void remoteBusiness(RpcRequest rpcRequest, Class<T> cls, RpcRequestCallback rpcRequestCallback, boolean z) {
        RemoteBusiness.init(DataProviderFactory.getApplicationContext(), DataProviderFactory.getDataProvider().getTTID());
        MTOPWrapper.getInstance().remoteBusiness(rpcRequest, cls, rpcRequestCallback, z);
    }

    public <T extends RpcResponse<?>> void remoteBusiness(RpcRequest rpcRequest, Class<T> cls, RpcRequestCallbackWithCode rpcRequestCallbackWithCode) {
        remoteBusiness(rpcRequest, cls, rpcRequestCallbackWithCode, true);
    }

    public <T extends RpcResponse<?>> void remoteBusiness(RpcRequest rpcRequest, Class<T> cls, RpcRequestCallbackWithCode rpcRequestCallbackWithCode, boolean z) {
        RemoteBusiness.init(DataProviderFactory.getApplicationContext(), DataProviderFactory.getDataProvider().getTTID());
        MTOPWrapper.getInstance().remoteBusiness(rpcRequest, cls, rpcRequestCallbackWithCode, z);
    }
}
