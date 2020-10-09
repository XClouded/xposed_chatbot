package com.ali.user.mobile.login.presenter;

import android.text.TextUtils;
import com.ali.user.mobile.base.BasePresenter;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.data.RegisterComponent;
import com.ali.user.mobile.data.model.RegisterUserInfo;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.ui.FastRegView;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResponseData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResult;
import com.ali.user.mobile.utils.UTConstans;
import com.taobao.login4android.broadcast.LoginAction;
import java.util.HashMap;
import java.util.Properties;

public class FastRegPresenter implements BasePresenter {
    protected FastRegView mViewer;

    public void onDestory() {
    }

    public void onStart() {
    }

    public FastRegPresenter(FastRegView fastRegView) {
        this.mViewer = fastRegView;
    }

    public void fastReg(final OceanRegisterParam oceanRegisterParam, String str, boolean z) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            RegisterUserInfo registerUserInfo = new RegisterUserInfo();
            registerUserInfo.email = oceanRegisterParam.email;
            RegisterComponent.getInstance().fastRegister(str, registerUserInfo, z, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    if (FastRegPresenter.this.mViewer != null && FastRegPresenter.this.mViewer.isActive()) {
                        FastRegPresenter.this.mViewer.dismissLoading();
                        if (FastRegPresenter.this.mViewer != null && FastRegPresenter.this.mViewer.isActive()) {
                            String str = "";
                            if (!TextUtils.isEmpty(oceanRegisterParam.thirdType)) {
                                str = "_" + oceanRegisterParam.thirdType;
                            }
                            OceanRegisterResponseData oceanRegisterResponseData = (OceanRegisterResponseData) rpcResponse;
                            if (oceanRegisterResponseData == null || oceanRegisterResponseData.returnValue == null || !"SUCCESS".equals(rpcResponse.actionType)) {
                                AppMonitorAdapter.commitFail("Page_SNS_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + str, "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                                FastRegPresenter.this.mViewer.onRegisterFail(rpcResponse == null ? 0 : rpcResponse.code, rpcResponse == null ? "" : rpcResponse.message);
                                return;
                            }
                            Properties properties = new Properties();
                            properties.setProperty("result", "continueLoginToken");
                            UserTrackAdapter.sendUT("Page_Reg", UTConstans.CustomEvent.UT_REGISTER_RESULT + str, properties);
                            AppMonitorAdapter.commitSuccess("Page_SNS_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + str);
                            BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_REGISTER_SUCCESS, new HashMap());
                            FastRegPresenter.this.mViewer.onRegisterSuccess(((OceanRegisterResult) oceanRegisterResponseData.returnValue).continueLoginToken);
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (FastRegPresenter.this.mViewer != null && FastRegPresenter.this.mViewer.isActive()) {
                        FastRegPresenter.this.mViewer.dismissLoading();
                        if (FastRegPresenter.this.mViewer != null && FastRegPresenter.this.mViewer.isActive()) {
                            FastRegPresenter.this.mViewer.onRegisterFail(rpcResponse == null ? 0 : rpcResponse.code, rpcResponse == null ? "" : rpcResponse.message);
                        }
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (FastRegPresenter.this.mViewer != null && FastRegPresenter.this.mViewer.isActive()) {
                        FastRegPresenter.this.mViewer.dismissLoading();
                        if (FastRegPresenter.this.mViewer != null && FastRegPresenter.this.mViewer.isActive()) {
                            FastRegPresenter.this.mViewer.onRegisterFail(rpcResponse == null ? 0 : rpcResponse.code, rpcResponse == null ? "" : rpcResponse.message);
                        }
                    }
                }
            });
        }
    }
}
