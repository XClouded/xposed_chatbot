package com.ali.user.mobile.login.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.helper.LoginDataHelper;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.data.DataRepository;
import com.ali.user.mobile.data.LoginComponent;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.login.ui.BaseLoginView;
import com.ali.user.mobile.login.ui.UserMobileLoginView;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.MtopCountryCodeContextResult;
import com.ali.user.mobile.model.MtopRegisterInitcontextResponseData;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.model.BaseRegistRequest;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResponseData;
import com.ali.user.mobile.rpc.register.model.OceanRegisterResult;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.CountryCodeUtil;
import com.ali.user.mobile.utils.ResourceUtil;
import com.ali.user.mobile.utils.UTConstans;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class UserMobileLoginPresenter extends BaseLoginPresenter {
    private static final String TAG = ("login." + UserMobileLoginPresenter.class.getSimpleName());

    public UserMobileLoginPresenter(BaseLoginView baseLoginView, LoginParam loginParam) {
        super(baseLoginView, loginParam);
    }

    public void region() {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            getRegion(new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    MtopRegisterInitcontextResponseData mtopRegisterInitcontextResponseData;
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive() && (mtopRegisterInitcontextResponseData = (MtopRegisterInitcontextResponseData) rpcResponse) != null && mtopRegisterInitcontextResponseData.returnValue != null && ((MtopCountryCodeContextResult) mtopRegisterInitcontextResponseData.returnValue).countrycodes != null) {
                        ArrayList<RegionInfo> fillData = CountryCodeUtil.fillData(ResourceUtil.getStringById("aliuser_common_region"), ((MtopCountryCodeContextResult) mtopRegisterInitcontextResponseData.returnValue).countrycodes, new HashMap(), new ArrayList());
                        UserMobileLoginPresenter.this.mViewer.dismissLoading();
                        UserMobileLoginPresenter.this.mViewer.onGetRegion(fillData);
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                        UserMobileLoginPresenter.this.mViewer.dismissLoading();
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException((Integer) 6, ""));
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                        UserMobileLoginPresenter.this.mViewer.dismissLoading();
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException((Integer) 6, ""));
                    }
                }
            });
        }
    }

    private void getRegion(final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, MtopRegisterInitcontextResponseData>() {
            /* access modifiers changed from: protected */
            public MtopRegisterInitcontextResponseData doInBackground(Object[] objArr) {
                new BaseRegistRequest().ext = new HashMap();
                try {
                    return (MtopRegisterInitcontextResponseData) LoginComponent.getInstance().getCountryList();
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
                    } else if (mtopRegisterInitcontextResponseData.code == 3000) {
                        rpcRequestCallback.onSuccess(mtopRegisterInitcontextResponseData);
                    } else {
                        rpcRequestCallback.onError(mtopRegisterInitcontextResponseData);
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public RpcResponse login(LoginParam loginParam) {
        if (loginParam.token != null) {
            return UserLoginServiceImpl.getInstance().loginByToken(loginParam);
        }
        return LoginComponent.getInstance().smsLogin(loginParam);
    }

    /* access modifiers changed from: protected */
    public boolean onReceiveSuccess(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        if (ApiConstants.UTConstants.UT_TYPE_SMS_LOGIN_TO_REG.equals(this.mLoginParam.tokenType)) {
            String str = this.mLoginParam.isFromAccount ? ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN : ApiConstants.UTConstants.UT_PAGE_FIRST_LOGIN;
            Properties properties = new Properties();
            properties.put("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
            if (this.mLoginParam.isFamilyLoginToReg) {
                properties.put("type", ApiConstants.UTConstants.UT_LOGIN_TO_REG_FAMILY);
            } else {
                properties.put("type", ApiConstants.UTConstants.UT_LOGIN_TO_REG_NORMAL);
            }
            UserTrackAdapter.sendUT(str, ApiConstants.UTConstants.UT_Login_To_Reg_Result, (String) null, (String) null, properties);
        }
        return super.onReceiveSuccess(loginParam, rpcResponse);
    }

    /* access modifiers changed from: protected */
    public void onReceiveRegister(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        if (this.mViewer != null) {
            LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
            String str = rpcResponse.message;
            loginParam.token = null;
            if (rpcResponse.code == 14054) {
                ((UserMobileLoginView) this.mViewer).onNeedShowFamilyAccount(str, loginReturnData.token);
            } else {
                ((UserMobileLoginView) this.mViewer).onNeedReg(str, loginReturnData.token);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onReceiveAlert(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        if (this.mViewer != null) {
            String str = rpcResponse.message;
            if (TextUtils.isEmpty(str)) {
                str = ResourceUtil.getStringById("aliuser_network_error");
            }
            this.mViewer.alert("", str, this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_common_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                        UserMobileLoginPresenter.this.mViewer.dismissAlertDialog();
                        ((UserMobileLoginView) UserMobileLoginPresenter.this.mViewer).onCheckCodeError();
                    }
                }
            }, (String) null, (DialogInterface.OnClickListener) null);
        }
    }

    public void buildSMSLoginParam(String str, String str2, boolean z) {
        if (this.mLoginParam == null) {
            this.mLoginParam = new LoginParam();
        }
        this.mLoginParam.isFromAccount = this.mViewer.isHistoryMode();
        this.mLoginParam.loginSite = this.mViewer.getLoginSite();
        this.mLoginParam.loginAccount = str;
        this.mLoginParam.smsCode = str2;
        if (this.mLoginParam.externParams == null) {
            this.mLoginParam.externParams = new HashMap();
        }
        this.mLoginParam.externParams.put("apiReferer", ApiReferer.generateApiReferer());
        this.mLoginParam.tid = DataProviderFactory.getDataProvider().getTID();
        this.mLoginParam.loginType = this.mViewer.getLoginType().getType();
        this.mLoginParam.countryCode = ((UserMobileLoginView) this.mViewer).getCountryCode();
        this.mLoginParam.phoneCode = ((UserMobileLoginView) this.mViewer).getPhoneCode();
        this.mLoginParam.deviceTokenKey = "";
        this.mLoginParam.havanaId = 0;
        this.mLoginParam.enableVoiceSMS = z;
    }

    public void onLoginSuccess(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        LoginReturnData loginReturnData;
        if (rpcResponse != null && (loginReturnData = (LoginReturnData) rpcResponse.returnValue) != null) {
            LoginDataHelper.processLoginReturnData(false, loginReturnData, LoginStatus.browserRefUrl);
            Intent intent = new Intent(LoginResActions.LOGIN_SUCCESS_ACTION);
            if (loginParam != null && "mergeAccount".equals(loginParam.tokenType)) {
                intent.putExtra("message", "mergeAccount");
            }
            boolean sendLocalBroadCast = BroadCastHelper.sendLocalBroadCast(intent);
            String str = TAG;
            TLogAdapter.d(str, "send login success broadcast finish:sendResult = " + sendLocalBroadCast);
        }
    }

    public void sendSMS() {
        this.mViewer.showLoading();
        sendSMSAction(getLoginParam(), new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                    UserMobileLoginPresenter.this.mViewer.dismissLoading();
                    LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
                    if (loginReturnData != null) {
                        UserMobileLoginPresenter.this.mLoginParam.smsSid = loginReturnData.extMap.get("smsSid");
                        if (!TextUtils.equals(rpcResponse.actionType, "SUCCESS")) {
                            return;
                        }
                        if (rpcResponse.code == 14050) {
                            UserMobileLoginPresenter.this.mViewer.alert("", rpcResponse.message, UserMobileLoginPresenter.this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_common_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                                        UserMobileLoginPresenter.this.mViewer.dismissAlertDialog();
                                    }
                                }
                            }, (String) null, (DialogInterface.OnClickListener) null);
                            ((UserMobileLoginView) UserMobileLoginPresenter.this.mViewer).onSendSMSSuccess(60000, false);
                            return;
                        }
                        ((UserMobileLoginView) UserMobileLoginPresenter.this.mViewer).onSendSMSSuccess(60000, true);
                    }
                }
            }

            public void onSystemError(RpcResponse rpcResponse) {
                if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                    UserMobileLoginPresenter.this.mViewer.dismissLoading();
                    if (rpcResponse != null) {
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException(Integer.valueOf(rpcResponse.code), rpcResponse.message));
                    } else {
                        SDKExceptionHelper.getInstance().rpcExceptionHandler(new RpcException((Integer) 6, ""));
                    }
                }
            }

            public void onError(RpcResponse rpcResponse) {
                if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                    UserMobileLoginPresenter.this.mViewer.dismissLoading();
                    if (rpcResponse != null) {
                        LoginReturnData loginReturnData = (LoginReturnData) rpcResponse.returnValue;
                        if (loginReturnData != null) {
                            if (TextUtils.equals(rpcResponse.actionType, ApiConstants.ResultActionType.H5)) {
                                if (TextUtils.equals(loginReturnData.showNativeMachineVerify, "true")) {
                                    UserMobileLoginPresenter.this.mViewer.onNeedVerification("", 1001);
                                } else if (!TextUtils.isEmpty(loginReturnData.h5Url)) {
                                    String str = loginReturnData.h5Url;
                                    UserMobileLoginPresenter.this.mLoginParam.tokenType = TokenType.LOGIN;
                                    NavigatorManager.getInstance().navToWebViewPage(UserMobileLoginPresenter.this.mViewer.getBaseActivity(), str, UserMobileLoginPresenter.this.mLoginParam, loginReturnData);
                                } else if (rpcResponse != null) {
                                    String str2 = rpcResponse.message;
                                    if (TextUtils.isEmpty(str2)) {
                                        str2 = ResourceUtil.getStringById("aliuser_network_error");
                                    }
                                    UserMobileLoginPresenter.this.mViewer.toast(str2, 0);
                                }
                            } else if (TextUtils.equals(rpcResponse.actionType, ApiConstants.ResultActionType.TOAST) && UserMobileLoginPresenter.this.mViewer != null) {
                                String str3 = rpcResponse.message;
                                if (TextUtils.isEmpty(str3)) {
                                    str3 = ResourceUtil.getStringById("aliuser_network_error");
                                }
                                UserMobileLoginPresenter.this.mViewer.alert("", str3, UserMobileLoginPresenter.this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_common_ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                                            UserMobileLoginPresenter.this.mViewer.dismissAlertDialog();
                                        }
                                    }
                                }, (String) null, (DialogInterface.OnClickListener) null);
                            }
                        } else if (rpcResponse != null) {
                            String str4 = rpcResponse.message;
                            if (TextUtils.isEmpty(str4)) {
                                str4 = ResourceUtil.getStringById("aliuser_network_error");
                            }
                            UserMobileLoginPresenter.this.mViewer.toast(str4, 0);
                        }
                    }
                }
            }
        });
    }

    private void sendSMSAction(final LoginParam loginParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse<LoginReturnData>>() {
            /* access modifiers changed from: protected */
            public RpcResponse<LoginReturnData> doInBackground(Object... objArr) {
                if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isHistoryMode()) {
                    UserMobileLoginPresenter.this.matchHistoryAccount();
                }
                try {
                    return LoginComponent.getInstance().sendSmsByLogin(loginParam);
                } catch (RpcException e) {
                    e.printStackTrace();
                    return null;
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse<LoginReturnData> rpcResponse) {
                if (rpcRequestCallback != null) {
                    if (rpcResponse == null) {
                        rpcRequestCallback.onSystemError((RpcResponse) null);
                    } else if (TextUtils.equals(rpcResponse.actionType, "SUCCESS")) {
                        rpcRequestCallback.onSuccess(rpcResponse);
                    } else {
                        rpcRequestCallback.onError(rpcResponse);
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onActivityResultForSMSMachine(int i, Intent intent) {
        if (i == -1 && intent != null) {
            this.mLoginParam.slideCheckcodeSid = intent.getStringExtra("sid");
            this.mLoginParam.slideCheckcodeSig = intent.getStringExtra("sig");
            this.mLoginParam.slideCheckcodeToken = intent.getStringExtra("token");
            String str = this.mLoginParam.isFromAccount ? ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN : ApiConstants.UTConstants.UT_PAGE_FIRST_LOGIN;
            Properties properties = new Properties();
            properties.put("result", ApiConstants.UTConstants.UT_SLIDE_SUCCESS);
            UserTrackAdapter.sendUT(str, ApiConstants.UTConstants.UT_SLIDE_RESULT, properties);
            sendSMS();
        }
    }

    public void directRegister(String str, final boolean z) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            DataRepository.directRegister(str, new RpcRequestCallback() {
                public void onSuccess(RpcResponse rpcResponse) {
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                        UserMobileLoginPresenter.this.mViewer.dismissLoading();
                        OceanRegisterResponseData oceanRegisterResponseData = (OceanRegisterResponseData) rpcResponse;
                        if (oceanRegisterResponseData == null) {
                            return;
                        }
                        if ("SUCCESS".equals(rpcResponse.actionType)) {
                            Properties properties = new Properties();
                            properties.setProperty("result", "continueLoginToken");
                            UserTrackAdapter.sendUT("Page_Reg", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", properties);
                            AppMonitorAdapter.commitSuccess("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "");
                            if (oceanRegisterResponseData.returnValue != null) {
                                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_REGISTER_SUCCESS, new HashMap());
                                UserMobileLoginPresenter.this.mLoginParam.token = ((OceanRegisterResult) oceanRegisterResponseData.returnValue).continueLoginToken;
                                UserMobileLoginPresenter.this.mLoginParam.scene = "1012";
                                UserMobileLoginPresenter.this.mLoginParam.tokenType = ApiConstants.UTConstants.UT_TYPE_SMS_LOGIN_TO_REG;
                                UserMobileLoginPresenter.this.mLoginParam.isFamilyLoginToReg = z;
                                UserMobileLoginPresenter.this.mLoginParam.loginType = LoginType.TAOBAO_ACCOUNT.getType();
                                UserMobileLoginPresenter.this.login();
                            }
                        } else if (!ApiConstants.ResultActionType.H5.equals(rpcResponse.actionType)) {
                        }
                    }
                }

                public void onSystemError(RpcResponse rpcResponse) {
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                        UserMobileLoginPresenter.this.mViewer.dismissLoading();
                        if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                            UserMobileLoginPresenter.this.mViewer.toast(rpcResponse == null ? "" : rpcResponse.message, 0);
                            AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                        }
                    }
                }

                public void onError(RpcResponse rpcResponse) {
                    if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                        UserMobileLoginPresenter.this.mViewer.dismissLoading();
                        if (UserMobileLoginPresenter.this.mViewer != null && UserMobileLoginPresenter.this.mViewer.isActive()) {
                            UserMobileLoginPresenter.this.mViewer.toast(rpcResponse == null ? "" : rpcResponse.message, 0);
                            AppMonitorAdapter.commitFail("Page_Member_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT + "", "0", rpcResponse == null ? "" : String.valueOf(rpcResponse.code));
                        }
                    }
                }
            });
        }
    }
}
