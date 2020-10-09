package com.ali.user.mobile.register.presenter;

import android.text.TextUtils;
import com.ali.user.mobile.base.BasePresenter;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.data.DataRepository;
import com.ali.user.mobile.data.RegisterComponent;
import com.ali.user.mobile.data.model.SmsApplyResponse;
import com.ali.user.mobile.data.model.SmsApplyResult;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.model.AliValidRequest;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.register.ui.RegisterFormView;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.register.model.FastRegResult;
import com.ali.user.mobile.rpc.register.model.NumAuthFastRegisterResponseData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResponseData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResult;
import com.ali.user.mobile.utils.UTConstans;
import com.taobao.login4android.broadcast.LoginAction;
import java.util.HashMap;
import java.util.Properties;

public class MobileRegisterPresenter implements BasePresenter {
    private static final String TAG = "MobileRegisterPresenter";
    /* access modifiers changed from: private */
    public String mSessionId;
    /* access modifiers changed from: private */
    public RegisterFormView mViewer;

    public void onStart() {
    }

    public RegisterFormView getViewer() {
        return this.mViewer;
    }

    public void setViewer(RegisterFormView registerFormView) {
        this.mViewer = registerFormView;
    }

    public MobileRegisterPresenter(RegisterFormView registerFormView) {
        this.mViewer = registerFormView;
    }

    public void register(final OceanRegisterParam oceanRegisterParam) {
        if (oceanRegisterParam != null) {
            oceanRegisterParam.sessionId = this.mSessionId;
        }
        this.mViewer.showLoading();
        DataRepository.register(oceanRegisterParam, new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                    MobileRegisterPresenter.this.mViewer.dismissLoading();
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        String str = "";
                        if (!TextUtils.isEmpty(oceanRegisterParam.thirdType)) {
                            str = "_" + oceanRegisterParam.thirdType;
                        }
                        OceanRegisterResponseData oceanRegisterResponseData = (OceanRegisterResponseData) rpcResponse;
                        if (oceanRegisterResponseData != null) {
                            if ("SUCCESS".equals(rpcResponse.actionType)) {
                                Properties properties = new Properties();
                                properties.setProperty("result", "continueLoginToken");
                                UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? "Page_Reg" : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_REGISTER_RESULT + str, properties);
                                AppMonitorAdapter.commitSuccess("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + str);
                                if (oceanRegisterResponseData.returnValue != null) {
                                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_REGISTER_SUCCESS, new HashMap());
                                    MobileRegisterPresenter.this.mViewer.onRegisterSuccess(((OceanRegisterResult) oceanRegisterResponseData.returnValue).continueLoginToken);
                                    return;
                                }
                            } else if (ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType)) {
                                MobileRegisterPresenter.this.mViewer.onH5(((OceanRegisterResult) oceanRegisterResponseData.returnValue).h5Url);
                                return;
                            }
                        }
                        AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + str, "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                        MobileRegisterPresenter.this.mViewer.onRegisterFail(rpcResponse == null ? 0 : rpcResponse.code, rpcResponse == null ? "" : rpcResponse.message);
                    }
                }
            }

            public void onSystemError(RpcResponse rpcResponse) {
                if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                    MobileRegisterPresenter.this.mViewer.dismissLoading();
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.onRegisterFail(rpcResponse == null ? 0 : rpcResponse.code, rpcResponse == null ? "" : rpcResponse.message);
                        AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                    }
                }
            }

            public void onError(RpcResponse rpcResponse) {
                if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                    MobileRegisterPresenter.this.mViewer.dismissLoading();
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.onRegisterFail(rpcResponse == null ? 0 : rpcResponse.code, rpcResponse == null ? "" : rpcResponse.message);
                        AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                    }
                }
            }
        });
    }

    public void sendSMS(OceanRegisterParam oceanRegisterParam) {
        this.mViewer.showLoading();
        if (oceanRegisterParam != null) {
            oceanRegisterParam.sessionId = this.mSessionId;
        }
        DataRepository.sendSMS(oceanRegisterParam, new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                SmsApplyResult smsApplyResult;
                if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                    MobileRegisterPresenter.this.mViewer.dismissLoading();
                    Properties properties = new Properties();
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive() && (smsApplyResult = (SmsApplyResult) ((SmsApplyResponse) rpcResponse).returnValue) != null) {
                        String unused = MobileRegisterPresenter.this.mSessionId = smsApplyResult.sdkSessionId;
                        if (TextUtils.equals("true", smsApplyResult.sendSmsResult)) {
                            properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
                            properties.setProperty("result", ApiConstants.UTConstants.UT_SUCCESS_F);
                            UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_SMS : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_SEND_MSG_RESULT, properties);
                            MobileRegisterPresenter.this.mViewer.onSendSMSSuccess(60000);
                        } else if (TextUtils.equals("true", smsApplyResult.needMachineVerify)) {
                            properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                            properties.setProperty("result", ApiConstants.UTConstants.UT_SEND_RESULT_SLIDE);
                            UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_SMS : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_SEND_MSG_RESULT, properties);
                            MobileRegisterPresenter.this.mViewer.onNeedVerification(MobileRegisterPresenter.this.mSessionId, 1001);
                        }
                    }
                }
            }

            public void onSystemError(RpcResponse rpcResponse) {
                if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                    MobileRegisterPresenter.this.mViewer.dismissLoading();
                    Properties properties = new Properties();
                    properties.setProperty("result", ApiConstants.UTConstants.UT_SUCCESS_F);
                    properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                    UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_SMS : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_SEND_MSG_RESULT, rpcResponse == null ? "" : rpcResponse.message, properties);
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.onSMSSendFail(rpcResponse);
                    }
                }
            }

            public void onError(RpcResponse rpcResponse) {
                if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                    MobileRegisterPresenter.this.mViewer.dismissLoading();
                    Properties properties = new Properties();
                    properties.setProperty("result", ApiConstants.UTConstants.UT_SUCCESS_F);
                    properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
                    UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_SMS : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_SEND_MSG_RESULT, rpcResponse == null ? "" : rpcResponse.message, properties);
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.onSMSSendFail(rpcResponse);
                    }
                }
            }
        });
    }

    public void checkCanAuthNumRegister(OceanRegisterParam oceanRegisterParam) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            RegisterComponent.getInstance().canAuthNumRegister(oceanRegisterParam, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    String str;
                    String str2;
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                            NumAuthFastRegisterResponseData numAuthFastRegisterResponseData = (NumAuthFastRegisterResponseData) rpcResponse;
                            if (numAuthFastRegisterResponseData != null && numAuthFastRegisterResponseData.returnValue != null && TextUtils.equals(numAuthFastRegisterResponseData.actionType, "SUCCESS")) {
                                String unused = MobileRegisterPresenter.this.mSessionId = ((FastRegResult) numAuthFastRegisterResponseData.returnValue).sdkSessionId;
                                MobileRegisterPresenter.this.mViewer.onCheckAuthNumSuccess();
                            } else if (numAuthFastRegisterResponseData == null || numAuthFastRegisterResponseData.returnValue == null || !TextUtils.equals(numAuthFastRegisterResponseData.actionType, ApiConstants.ResultActionType.TOAST)) {
                                if (!(numAuthFastRegisterResponseData == null || numAuthFastRegisterResponseData.returnValue == null || TextUtils.isEmpty(((FastRegResult) numAuthFastRegisterResponseData.returnValue).sdkSessionId))) {
                                    String unused2 = MobileRegisterPresenter.this.mSessionId = ((FastRegResult) numAuthFastRegisterResponseData.returnValue).sdkSessionId;
                                }
                                MobileRegisterPresenter.this.mViewer.onCheckAuthNumFail();
                            } else {
                                MobileRegisterPresenter.this.mViewer.toast(numAuthFastRegisterResponseData.message, 0);
                            }
                            Properties properties = new Properties();
                            properties.setProperty("sessionId", MobileRegisterPresenter.this.mSessionId + "");
                            if (numAuthFastRegisterResponseData == null) {
                                str = "";
                            } else {
                                str = numAuthFastRegisterResponseData.code + "";
                            }
                            properties.setProperty("code", str);
                            if (numAuthFastRegisterResponseData == null) {
                                str2 = "";
                            } else {
                                str2 = numAuthFastRegisterResponseData.actionType;
                            }
                            properties.setProperty("actionType", str2);
                            UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_ONEKEY_REG : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_REG_JUDGE_RESULT, properties);
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive() && MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        MobileRegisterPresenter.this.mViewer.onCheckAuthNumFail();
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive() && MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        MobileRegisterPresenter.this.mViewer.onCheckAuthNumFail();
                    }
                }
            });
        }
    }

    public void numAuthRegister(AliValidRequest aliValidRequest, final OceanRegisterParam oceanRegisterParam) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            if (oceanRegisterParam != null) {
                oceanRegisterParam.sessionId = this.mSessionId;
            }
            RegisterComponent.getInstance().numAuthRegister(aliValidRequest, oceanRegisterParam, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        String str = "";
                        if (!TextUtils.isEmpty(oceanRegisterParam.thirdType)) {
                            str = "_" + oceanRegisterParam.thirdType;
                        }
                        NumAuthFastRegisterResponseData numAuthFastRegisterResponseData = (NumAuthFastRegisterResponseData) rpcResponse;
                        if (numAuthFastRegisterResponseData != null) {
                            Properties properties = new Properties();
                            properties.setProperty("sessionId", MobileRegisterPresenter.this.mSessionId + "");
                            properties.setProperty("code", numAuthFastRegisterResponseData.code + "");
                            properties.setProperty("actionType", numAuthFastRegisterResponseData.actionType);
                            UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_ONEKEY_REG : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_REG_RESULT, properties);
                            if (numAuthFastRegisterResponseData.returnValue != null) {
                                String unused = MobileRegisterPresenter.this.mSessionId = ((FastRegResult) numAuthFastRegisterResponseData.returnValue).sdkSessionId;
                            }
                            if ("SUCCESS".equals(rpcResponse.actionType)) {
                                Properties properties2 = new Properties();
                                properties2.setProperty("result", "continueLoginToken");
                                UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_ONEKEY_REG : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_REGISTER_RESULT + str, properties2);
                                if (numAuthFastRegisterResponseData.returnValue != null) {
                                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_REGISTER_SUCCESS, new HashMap());
                                    MobileRegisterPresenter.this.mViewer.onRegisterSuccess(((FastRegResult) numAuthFastRegisterResponseData.returnValue).continueLoginToken);
                                    return;
                                }
                            } else if (ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType) && numAuthFastRegisterResponseData.returnValue != null && !TextUtils.isEmpty(((FastRegResult) numAuthFastRegisterResponseData.returnValue).h5Url)) {
                                UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_ONEKEY_REG : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_GET_AUTH_CONFIG_REGISTER_H5);
                                MobileRegisterPresenter.this.mViewer.onH5(((FastRegResult) numAuthFastRegisterResponseData.returnValue).h5Url);
                                return;
                            } else if (numAuthFastRegisterResponseData.returnValue != null && ((FastRegResult) numAuthFastRegisterResponseData.returnValue).needMachineVerify) {
                                UserTrackAdapter.sendUT(TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? UTConstans.PageName.UT_PAGE_ONEKEY_REG : MobileRegisterPresenter.this.mViewer.getPageName(), UTConstans.CustomEvent.UT_GET_AUTH_CONFIG_REGISTER_CAPTCHA);
                                MobileRegisterPresenter.this.mViewer.onNeedVerification(MobileRegisterPresenter.this.mSessionId, 1002);
                                return;
                            }
                            MobileRegisterPresenter.this.mViewer.onNumAuthRegisterFail(rpcResponse);
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        MobileRegisterPresenter.this.mViewer.onNumAuthRegisterFail(rpcResponse);
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        if (rpcResponse == null || (!ApiConstants.ResultActionType.ALERT.equals(rpcResponse.actionType) && !ApiConstants.ResultActionType.TOAST.equals(rpcResponse.actionType))) {
                            MobileRegisterPresenter.this.mViewer.onNumAuthRegisterFail(rpcResponse);
                        } else {
                            MobileRegisterPresenter.this.mViewer.toast(rpcResponse.message, 0);
                        }
                    }
                }
            });
        }
    }

    public void directRegister(String str, boolean z) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            DataRepository.directRegister(str, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        OceanRegisterResponseData oceanRegisterResponseData = (OceanRegisterResponseData) rpcResponse;
                        if (oceanRegisterResponseData == null) {
                            return;
                        }
                        if ("SUCCESS".equals(rpcResponse.actionType)) {
                            Properties properties = new Properties();
                            properties.setProperty("result", "continueLoginToken");
                            String pageName = TextUtils.isEmpty(MobileRegisterPresenter.this.mViewer.getPageName()) ? "Page_Reg" : MobileRegisterPresenter.this.mViewer.getPageName();
                            UserTrackAdapter.sendUT(pageName, UTConstans.CustomEvent.UT_REGISTER_RESULT + "", properties);
                            AppMonitorAdapter.commitSuccess("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "");
                            if (oceanRegisterResponseData.returnValue != null) {
                                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_REGISTER_SUCCESS, new HashMap());
                                MobileRegisterPresenter.this.mViewer.onRegisterSuccess(((OceanRegisterResult) oceanRegisterResponseData.returnValue).continueLoginToken);
                            }
                        } else if (ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType) && oceanRegisterResponseData.returnValue != null) {
                            MobileRegisterPresenter.this.mViewer.onH5(((OceanRegisterResult) oceanRegisterResponseData.returnValue).h5Url);
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                            MobileRegisterPresenter.this.mViewer.toast(rpcResponse == null ? "" : rpcResponse.message, 0);
                            AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                        }
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                        MobileRegisterPresenter.this.mViewer.dismissLoading();
                        if (MobileRegisterPresenter.this.mViewer != null && MobileRegisterPresenter.this.mViewer.isActive()) {
                            MobileRegisterPresenter.this.mViewer.toast(rpcResponse == null ? "" : rpcResponse.message, 0);
                            AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                        }
                    }
                }
            });
        }
    }

    public void setSessionId(String str) {
        this.mSessionId = str;
    }

    public String getSessionId() {
        return this.mSessionId;
    }

    public void onDestory() {
        this.mViewer = null;
    }
}
