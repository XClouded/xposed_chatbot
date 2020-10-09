package com.ali.user.mobile.rpc.impl;

import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.callback.RpcRequestCallbackWithCode;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.rpc.RpcRequest;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.utils.ResourceUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import org.json.JSONObject;

public class MTOPWrapper {
    private static MTOPWrapper INSTANCE = null;
    private static final int MTOP_BIZ_CODE = 94;
    private static final String TAG = "login.MTOPWrapperImpl";

    public static synchronized MTOPWrapper getInstance() {
        MTOPWrapper mTOPWrapper;
        synchronized (MTOPWrapper.class) {
            if (INSTANCE == null) {
                synchronized (MTOPWrapper.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new MTOPWrapper();
                    }
                }
            }
            mTOPWrapper = INSTANCE;
        }
        return mTOPWrapper;
    }

    private MTOPWrapper() {
    }

    public <T extends RpcResponse<?>> T post(RpcRequest rpcRequest, Class<T> cls) {
        return post(rpcRequest, cls, (String) null);
    }

    public <T extends RpcResponse<?>> T post(RpcRequest rpcRequest, Class<T> cls, String str) {
        MtopResponse mtopResponse;
        MtopResponse mtopResponse2;
        try {
            MtopBuilder retryTime = Mtop.instance(DataProviderFactory.getApplicationContext()).build(buildMtopRequest(rpcRequest), DataProviderFactory.getDataProvider().getTTID()).reqMethod(MethodEnum.POST).setBizId(94).setConnectionTimeoutMilliSecond(5000).setSocketTimeoutMilliSecond(5000).retryTime(0);
            if (!TextUtils.isEmpty(str)) {
                retryTime.setReqUserId(str);
            }
            if (DataProviderFactory.getDataProvider().getAdditionalHeaders() != null) {
                retryTime.headers(DataProviderFactory.getDataProvider().getAdditionalHeaders());
            }
            long currentTimeMillis = System.currentTimeMillis();
            mtopResponse = retryTime.syncRequest();
            try {
                long currentTimeMillis2 = System.currentTimeMillis();
                TLogAdapter.d(TAG, "receive MtopResponse" + mtopResponse + ",time=" + (currentTimeMillis2 - currentTimeMillis));
            } catch (Exception e) {
                Exception exc = e;
                mtopResponse2 = mtopResponse;
                e = exc;
            }
        } catch (Exception e2) {
            e = e2;
            mtopResponse2 = null;
            TLogAdapter.e(TAG, "MtopResponse error", e);
            e.printStackTrace();
            mtopResponse = mtopResponse2;
            if (mtopResponse == null) {
            }
            TLogAdapter.e(TAG, "MtopResponse response=null");
            return null;
        }
        if (mtopResponse == null && cls != null) {
            return processMtopResponse(mtopResponse, cls);
        }
        TLogAdapter.e(TAG, "MtopResponse response=null");
        return null;
    }

    public <T extends RpcResponse<?>> T processMtopResponse(MtopResponse mtopResponse, Class<T> cls) {
        if (mtopResponse != null && mtopResponse.isApiSuccess()) {
            return getBizData(mtopResponse, cls);
        }
        if (mtopResponse == null) {
            return null;
        }
        if (mtopResponse.isNetworkError()) {
            throw new RpcException(7, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (mtopResponse.isApiLockedResult()) {
            throw new RpcException(400, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (mtopResponse.is41XResult()) {
            throw new RpcException(401, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (mtopResponse.isExpiredRequest()) {
            throw new RpcException(402, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (mtopResponse.isIllegelSign()) {
            throw new RpcException(403, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (mtopResponse.isSystemError()) {
            throw new RpcException(406, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (mtopResponse.isMtopSdkError()) {
            throw new RpcException(406, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        } else if (!mtopResponse.isSessionInvalid()) {
            return getBizData(mtopResponse, cls);
        } else {
            throw new RpcException(407, ResourceUtil.getStringById("aliusersdk_network_error"), mtopResponse.getRetCode());
        }
    }

    private <T extends RpcResponse<?>> T getBizData(MtopResponse mtopResponse, Class<T> cls) {
        try {
            JSONObject dataJsonObject = mtopResponse.getDataJsonObject();
            if (dataJsonObject != null) {
                return (RpcResponse) JSON.parseObject(dataJsonObject.toString(), cls);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends RpcResponse<?>> void remoteBusiness(RpcRequest rpcRequest, final Class<T> cls, final RpcRequestCallback rpcRequestCallback, boolean z) {
        if (rpcRequest != null && rpcRequestCallback != null) {
            try {
                MtopBusiness build = MtopBusiness.build(buildMtopRequest(rpcRequest), DataProviderFactory.getDataProvider().getTTID());
                if (DataProviderFactory.getDataProvider().getAdditionalHeaders() != null) {
                    build.headers(DataProviderFactory.getDataProvider().getAdditionalHeaders());
                }
                build.setConnectionTimeoutMilliSecond(7000);
                build.setSocketTimeoutMilliSecond(7000);
                build.showLoginUI(z);
                build.addListener((MtopListener) new IRemoteBaseListener() {
                    public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
                        rpcRequestCallback.onSuccess(MTOPWrapper.this.processMtopResponse(mtopResponse, cls));
                    }

                    public void onError(int i, MtopResponse mtopResponse, Object obj) {
                        try {
                            rpcRequestCallback.onError(MTOPWrapper.this.processMtopResponse(mtopResponse, cls));
                        } catch (RpcException e) {
                            RpcResponse rpcResponse = new RpcResponse();
                            rpcResponse.code = e.getCode();
                            rpcResponse.message = "亲，您的手机网络不太顺畅哦~";
                            rpcRequestCallback.onError(rpcResponse);
                        }
                    }

                    public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
                        try {
                            rpcRequestCallback.onSystemError(MTOPWrapper.this.processMtopResponse(mtopResponse, cls));
                        } catch (RpcException e) {
                            RpcResponse rpcResponse = new RpcResponse();
                            rpcResponse.code = e.getCode();
                            rpcResponse.message = "亲，您的手机网络不太顺畅哦~";
                            rpcRequestCallback.onError(rpcResponse);
                        }
                    }
                });
                build.startRequest();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public <T extends RpcResponse<?>> void remoteBusiness(RpcRequest rpcRequest, final Class<T> cls, final RpcRequestCallbackWithCode rpcRequestCallbackWithCode, boolean z) {
        if (rpcRequest != null && rpcRequestCallbackWithCode != null) {
            try {
                MtopBusiness build = MtopBusiness.build(buildMtopRequest(rpcRequest), DataProviderFactory.getDataProvider().getTTID());
                if (DataProviderFactory.getDataProvider().getAdditionalHeaders() != null) {
                    build.headers(DataProviderFactory.getDataProvider().getAdditionalHeaders());
                }
                build.addListener((MtopListener) new IRemoteBaseListener() {
                    public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
                        rpcRequestCallbackWithCode.onSuccess(MTOPWrapper.this.processMtopResponse(mtopResponse, cls));
                    }

                    public void onError(int i, MtopResponse mtopResponse, Object obj) {
                        String str = "-1";
                        if (mtopResponse != null) {
                            str = mtopResponse.getRetCode();
                        }
                        try {
                            rpcRequestCallbackWithCode.onError(str, MTOPWrapper.this.processMtopResponse(mtopResponse, cls));
                        } catch (RpcException unused) {
                            rpcRequestCallbackWithCode.onSystemError(str, (RpcResponse) null);
                        }
                    }

                    public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
                        String str = "-1";
                        if (mtopResponse != null) {
                            str = mtopResponse.getRetCode();
                        }
                        try {
                            rpcRequestCallbackWithCode.onSystemError(str, MTOPWrapper.this.processMtopResponse(mtopResponse, cls));
                        } catch (RpcException unused) {
                            rpcRequestCallbackWithCode.onSystemError(str, (RpcResponse) null);
                        }
                    }
                });
                build.startRequest();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private MtopRequest buildMtopRequest(RpcRequest rpcRequest) {
        if (rpcRequest == null) {
            return null;
        }
        try {
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName(rpcRequest.API_NAME);
            mtopRequest.setVersion(rpcRequest.VERSION);
            mtopRequest.setNeedEcode(rpcRequest.NEED_ECODE);
            mtopRequest.setNeedSession(rpcRequest.NEED_SESSION);
            JSONObject jSONObject = new JSONObject();
            for (int i = 0; i < rpcRequest.paramNames.size(); i++) {
                if (!(rpcRequest.paramNames.get(i) == null || rpcRequest.paramValues.get(i) == null)) {
                    jSONObject.put(rpcRequest.paramNames.get(i), rpcRequest.paramValues.get(i).toString());
                }
            }
            mtopRequest.setData(jSONObject.toString());
            return mtopRequest;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }
}
