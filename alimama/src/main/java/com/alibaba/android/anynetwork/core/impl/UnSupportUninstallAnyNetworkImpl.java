package com.alibaba.android.anynetwork.core.impl;

import android.text.TextUtils;
import com.alibaba.android.anynetwork.core.ANConfig;
import com.alibaba.android.anynetwork.core.ANRequest;
import com.alibaba.android.anynetwork.core.ANRequestId;
import com.alibaba.android.anynetwork.core.ANResponse;
import com.alibaba.android.anynetwork.core.AnyNetworkManager;
import com.alibaba.android.anynetwork.core.IANAsyncCallback;
import com.alibaba.android.anynetwork.core.IANService;
import com.alibaba.android.anynetwork.core.IAnyNetwork;
import com.alibaba.android.anynetwork.core.utils.ANLog;
import java.util.ArrayList;

public class UnSupportUninstallAnyNetworkImpl implements IAnyNetwork {
    private static final String TAG = "UnSupportUninstallAnyNetworkImpl";
    ArrayList<String> mServiceKeys = new ArrayList<>();
    ArrayList<IANService> mServices = new ArrayList<>();

    public void updateAllConfig(ANConfig aNConfig) {
        if (aNConfig != null) {
            int size = this.mServices.size();
            for (int i = 0; i < size; i++) {
                this.mServices.get(i).updateConfig(aNConfig);
            }
        }
    }

    public void updateConfig(String str, ANConfig aNConfig) {
        if (aNConfig != null) {
            int selectKeyIndex = getSelectKeyIndex(str);
            if (selectKeyIndex >= 0) {
                this.mServices.get(selectKeyIndex).updateConfig(aNConfig);
                return;
            }
            throw new IllegalArgumentException("Cannot find the service");
        }
    }

    public void installService(String str, IANService iANService) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("ServiceKey should not be empty");
        } else if (iANService != null) {
            ANLog.d(TAG, "install service:" + str);
            int selectKeyIndex = getSelectKeyIndex(str);
            if (selectKeyIndex > 0) {
                this.mServices.set(selectKeyIndex, iANService);
            } else {
                this.mServiceKeys.add(str);
                this.mServices.add(iANService);
            }
            iANService.init(AnyNetworkManager.getConfig());
        } else {
            throw new IllegalArgumentException("Service should not be null");
        }
    }

    public void uninstallService(String str) {
        throw new UnsupportedOperationException("Unsupport uninstallService method");
    }

    public ANResponse syncRequest(ANRequest aNRequest) {
        int i;
        if (aNRequest == null) {
            ANLog.e(TAG, "sync request is null");
            return ANResponse.generateFailResponse("", -1004, "request is null");
        }
        if (TextUtils.isEmpty(aNRequest.getServiceKey())) {
            i = getSupportServiceIndex(aNRequest);
        } else {
            i = getSelectKeyIndex(aNRequest.getServiceKey());
        }
        if (i < 0) {
            ANLog.e(TAG, "no support service for sync serviceKey=" + aNRequest.getServiceKey() + ", requestType=" + aNRequest.getBaseType());
            return ANResponse.generateFailResponse(aNRequest.getBaseType(), -1002, "no support service");
        }
        ANLog.d(TAG, "AN->syncRequest:" + aNRequest.toString());
        return this.mServices.get(i).syncRequest(aNRequest);
    }

    public ANRequestId asyncRequest(ANRequest aNRequest) {
        int i;
        if (aNRequest == null) {
            ANLog.e(TAG, "async request is null");
            return new ANRequestId("", (Object) null);
        }
        if (TextUtils.isEmpty(aNRequest.getServiceKey())) {
            i = getSupportServiceIndex(aNRequest);
        } else {
            i = getSelectKeyIndex(aNRequest.getServiceKey());
        }
        if (i < 0) {
            ANLog.e(TAG, "no support service for async serviceKey=" + aNRequest.getServiceKey() + ", requestType=" + aNRequest.getBaseType());
            onAsyncNoSupportService(aNRequest);
            return new ANRequestId("", (Object) null);
        }
        ANLog.d(TAG, "AN->asyncRequest: " + aNRequest.toString());
        ANRequestId asyncRequest = this.mServices.get(i).asyncRequest(aNRequest);
        asyncRequest.serviceKey = this.mServiceKeys.get(i);
        return asyncRequest;
    }

    public boolean cancelRequest(ANRequestId aNRequestId) {
        if (aNRequestId == null || TextUtils.isEmpty(aNRequestId.serviceKey) || aNRequestId.idObj == null) {
            ANLog.e(TAG, "try to cancel illegal requestId ");
            return false;
        }
        int selectKeyIndex = getSelectKeyIndex(aNRequestId.serviceKey);
        if (selectKeyIndex < 0) {
            ANLog.e(TAG, "no support service for cancel serviceKey=" + aNRequestId.serviceKey);
            return false;
        }
        ANLog.d(TAG, "cancel request:" + aNRequestId.toString());
        return this.mServices.get(selectKeyIndex).cancelRequest(aNRequestId);
    }

    public Object getDataFromService(String str, String str2) {
        int selectKeyIndex = getSelectKeyIndex(str);
        if (selectKeyIndex < 0) {
            return null;
        }
        return this.mServices.get(selectKeyIndex).getDataByKey(str2);
    }

    /* access modifiers changed from: package-private */
    public void onAsyncNoSupportService(ANRequest aNRequest) {
        IANAsyncCallback networkAsyncCallback = aNRequest.getNetworkAsyncCallback();
        if (networkAsyncCallback != null) {
            networkAsyncCallback.onCallback(ANResponse.generateFailResponse(aNRequest.getBaseType(), -1002, "async no support service"));
        }
    }

    /* access modifiers changed from: package-private */
    public int getSelectKeyIndex(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        int size = this.mServiceKeys.size();
        for (int i = 0; i < size; i++) {
            if (str.equals(this.mServiceKeys.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int getSupportServiceIndex(ANRequest aNRequest) {
        int size = this.mServices.size();
        for (int i = 0; i < size; i++) {
            if (this.mServices.get(i).isSupportRequest(aNRequest)) {
                return i;
            }
        }
        return -1;
    }
}
