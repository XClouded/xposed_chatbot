package com.alibaba.android.anynetwork.plugin.allinone;

import android.content.Context;
import com.alibaba.android.anynetwork.core.ANConfig;
import com.alibaba.android.anynetwork.core.ANRequest;
import com.alibaba.android.anynetwork.core.ANRequestId;
import com.alibaba.android.anynetwork.core.ANResponse;
import com.alibaba.android.anynetwork.core.IANService;
import com.alibaba.android.anynetwork.core.utils.ANLog;
import com.alibaba.android.anynetwork.plugin.allinone.impl.AllInOneConverterDefaultImpl;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.intf.Mtop;

public class AllInOneANService implements IANService {
    private static final String TAG = "AllInOneANService";
    private Context mContext;
    private IAllInOneConverter mConverter = new AllInOneConverterDefaultImpl();
    private String mTtid;

    public Object getDataByKey(String str) {
        return null;
    }

    public void updateConfig(ANConfig aNConfig) {
    }

    public AllInOneANService(Context context) {
        this.mContext = context;
    }

    public IAllInOneConverter getConverter() {
        return this.mConverter;
    }

    public void init(ANConfig aNConfig) {
        if (aNConfig != null) {
            this.mTtid = aNConfig.getNetworkMtopTtid();
            Mtop.instance(this.mContext, this.mTtid).switchEnvMode(convertMtopEnv(aNConfig.getNetworkMtopEnvironment()));
        }
    }

    private EnvModeEnum convertMtopEnv(int i) {
        switch (i) {
            case 0:
                return EnvModeEnum.ONLINE;
            case 1:
                return EnvModeEnum.PREPARE;
            case 2:
                return EnvModeEnum.TEST;
            case 3:
                return EnvModeEnum.TEST_SANDBOX;
            default:
                return EnvModeEnum.ONLINE;
        }
    }

    public ANResponse syncRequest(ANRequest aNRequest) {
        String baseType = aNRequest.getBaseType();
        ANLog.d(TAG, "syncRequest type:" + baseType);
        if (!"mtop".equals(baseType)) {
            ANLog.e(TAG, "syncRequest nonsupport type");
            return ANResponse.generateFailResponse(baseType, -1003, "all in one sync nonsupport type:" + baseType);
        }
        MethodEnum methodEnum = MethodEnum.GET;
        int networkHttpMethod = aNRequest.getNetworkHttpMethod();
        if (networkHttpMethod == 1) {
            methodEnum = MethodEnum.GET;
        } else if (networkHttpMethod == 2) {
            methodEnum = MethodEnum.POST;
        }
        return this.mConverter.convertMtopResponse2ANResponse(Mtop.instance(this.mContext).build(this.mConverter.convertANRequest2MtopRequest(aNRequest), this.mTtid).reqMethod(methodEnum).syncRequest());
    }

    public ANRequestId asyncRequest(ANRequest aNRequest) {
        String str = (String) aNRequest.getProperty("base_type");
        ANLog.d(TAG, "asyncRequest type:" + str);
        MethodEnum methodEnum = MethodEnum.GET;
        int networkHttpMethod = aNRequest.getNetworkHttpMethod();
        if (networkHttpMethod == 1) {
            methodEnum = MethodEnum.GET;
        } else if (networkHttpMethod == 2) {
            methodEnum = MethodEnum.POST;
        }
        if ("mtop".equals(str)) {
            ApiID asyncRequest = Mtop.instance(this.mContext).build(this.mConverter.convertANRequest2MtopRequest(aNRequest), this.mTtid).reqMethod(methodEnum).addListener(new AllInOneMtopAsyncCallbackProxy(aNRequest.getNetworkAsyncCallback(), this.mConverter)).asyncRequest();
            ANRequestId aNRequestId = new ANRequestId(new AllInOneRequestIdWrapper(str, 0));
            aNRequestId.idObj = asyncRequest;
            return aNRequestId;
        }
        ANLog.e(TAG, "asyncRequest nonsupport:" + str);
        return null;
    }

    public boolean isSupportRequest(ANRequest aNRequest) {
        String str = (String) aNRequest.getProperty("base_type");
        return "mtop".equals(str) || "http".equals(str) || "download".equals(str);
    }

    public boolean cancelRequest(ANRequestId aNRequestId) {
        if (aNRequestId == null || aNRequestId.idObj == null) {
            return false;
        }
        if (!(aNRequestId.idObj instanceof AllInOneRequestIdWrapper)) {
            ANLog.e(TAG, "cancel nonsupport ANRequestId.idObj");
            return false;
        }
        String str = ((AllInOneRequestIdWrapper) aNRequestId.idObj).type;
        if ("mtop".equals(str)) {
            ApiID apiID = (ApiID) aNRequestId.idObj;
            if (apiID == null) {
                return true;
            }
            apiID.cancelApiCall();
            return true;
        }
        ANLog.e(TAG, "cancel nonsupport:" + str);
        return false;
    }
}
