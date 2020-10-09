package com.ali.user.mobile.login.presenter;

import android.os.AsyncTask;
import com.ali.user.mobile.base.BasePresenter;
import com.ali.user.mobile.base.BaseView;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.data.LoginComponent;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.model.MtopCountryCodeContextResult;
import com.ali.user.mobile.model.MtopRegisterInitcontextResponseData;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.register.model.BaseRegistRequest;
import com.ali.user.mobile.register.service.impl.UserRegisterServiceImpl;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.utils.CountryCodeUtil;
import com.ali.user.mobile.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;

public class RegionPresenter implements BasePresenter {
    public static final int LOGIN_REGION = 0;
    public static final int REGISTER_REGION = 1;
    BaseView mViewer;

    public void onStart() {
    }

    public RegionPresenter(BaseView baseView) {
        this.mViewer = baseView;
    }

    public void region(int i) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            getRegion(i, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    MtopRegisterInitcontextResponseData mtopRegisterInitcontextResponseData = (MtopRegisterInitcontextResponseData) rpcResponse;
                    if (mtopRegisterInitcontextResponseData != null && mtopRegisterInitcontextResponseData.returnValue != null && ((MtopCountryCodeContextResult) mtopRegisterInitcontextResponseData.returnValue).countrycodes != null) {
                        ArrayList<RegionInfo> fillData = CountryCodeUtil.fillData(ResourceUtil.getStringById("aliuser_common_region"), ((MtopCountryCodeContextResult) mtopRegisterInitcontextResponseData.returnValue).countrycodes, new HashMap(), new ArrayList());
                        if (RegionPresenter.this.mViewer != null) {
                            RegionPresenter.this.mViewer.dismissLoading();
                            RegionPresenter.this.mViewer.onGetRegion(fillData);
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (RegionPresenter.this.mViewer != null && RegionPresenter.this.mViewer.isActive()) {
                        RegionPresenter.this.mViewer.dismissLoading();
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException((Integer) 6, ""));
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (RegionPresenter.this.mViewer != null && RegionPresenter.this.mViewer.isActive()) {
                        RegionPresenter.this.mViewer.dismissLoading();
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException((Integer) 6, ""));
                    }
                }
            });
        }
    }

    private void getRegion(final int i, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, MtopRegisterInitcontextResponseData>() {
            /* access modifiers changed from: protected */
            public MtopRegisterInitcontextResponseData doInBackground(Object[] objArr) {
                HashMap hashMap = new HashMap();
                BaseRegistRequest baseRegistRequest = new BaseRegistRequest();
                baseRegistRequest.ext = hashMap;
                try {
                    if (i == 0) {
                        return (MtopRegisterInitcontextResponseData) LoginComponent.getInstance().getCountryList();
                    }
                    return UserRegisterServiceImpl.getInstance().countryCodeRes(baseRegistRequest);
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(MtopRegisterInitcontextResponseData mtopRegisterInitcontextResponseData) {
                if (rpcRequestCallback != null) {
                    if (mtopRegisterInitcontextResponseData == null) {
                        rpcRequestCallback.onSystemError((RpcResponse) null);
                    } else if (mtopRegisterInitcontextResponseData.returnValue != null) {
                        rpcRequestCallback.onSuccess(mtopRegisterInitcontextResponseData);
                    } else {
                        rpcRequestCallback.onError(mtopRegisterInitcontextResponseData);
                    }
                }
            }
        }, new Object[0]);
    }

    public void onDestory() {
        this.mViewer = null;
    }
}
