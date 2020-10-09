package com.ali.user.mobile.login.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.model.GetVerifyTokenResponseData;
import com.ali.user.mobile.login.model.GetVerifyTokenResult;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.login.ui.BaseLoginView;
import com.ali.user.mobile.login.ui.FaceLoginView;
import com.ali.user.mobile.model.FaceVerifyCallback;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.verify.impl.VerifyServiceImpl;
import com.ali.user.mobile.verify.model.GetVerifyUrlResponse;
import com.ali.user.mobile.verify.model.GetVerifyUrlReturnData;
import com.ali.user.mobile.verify.model.VerifyParam;
import com.uc.webview.export.internal.utility.Log;
import java.util.Properties;

public class FaceLoginPresenter extends BaseLoginPresenter {
    /* access modifiers changed from: private */
    public static final String TAG = ("login." + FaceLoginPresenter.class.getSimpleName());

    public FaceLoginPresenter(BaseLoginView baseLoginView, LoginParam loginParam) {
        super(baseLoginView, loginParam);
    }

    public void fetchScanToken(LoginParam loginParam) {
        this.mViewer.showLoading();
        UserLoginServiceImpl.getInstance().getScanToken(loginParam, new RpcRequestCallback() {
            public void onSuccess(RpcResponse rpcResponse) {
                if (FaceLoginPresenter.this.mViewer != null && FaceLoginPresenter.this.mViewer.isActive() && FaceLoginPresenter.this.mViewer.getBaseActivity() != null) {
                    FaceLoginPresenter.this.mViewer.dismissLoading();
                    GetVerifyTokenResponseData getVerifyTokenResponseData = (GetVerifyTokenResponseData) rpcResponse;
                    if (getVerifyTokenResponseData == null || getVerifyTokenResponseData.returnValue == null || ((GetVerifyTokenResult) getVerifyTokenResponseData.returnValue).extMap == null) {
                        FaceLoginPresenter.this.onFaceLoginError(rpcResponse, "Error");
                        return;
                    }
                    Properties properties = new Properties();
                    properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_T);
                    UserTrackAdapter.sendUT((String) null, ApiConstants.UTConstants.UT_FACE_GENERATE_RESULT, properties);
                    String str = ((GetVerifyTokenResult) getVerifyTokenResponseData.returnValue).extMap.get("scanFaceLoginRPToken");
                    final String str2 = ((GetVerifyTokenResult) getVerifyTokenResponseData.returnValue).token;
                    final String str3 = ((GetVerifyTokenResult) getVerifyTokenResponseData.returnValue).scene;
                    if (ServiceFactory.getService(FaceService.class) != null) {
                        ((FaceService) ServiceFactory.getService(FaceService.class)).nativeLogin(str, new FaceVerifyCallback() {
                            public void onSuccess() {
                                FaceLoginPresenter.this.buildTokenParam(str2, "FaceLogin", str3);
                                FaceLoginPresenter.this.login();
                            }

                            public void onFail(int i, String str) {
                                String access$000 = FaceLoginPresenter.TAG;
                                Log.e(access$000, "code = " + i);
                                if (i == 3) {
                                    ((FaceLoginView) FaceLoginPresenter.this.mViewer).toLastLoginFragment();
                                    return;
                                }
                                FaceLoginPresenter.this.mViewer.toast(FaceLoginPresenter.this.mViewer.getBaseActivity().getString(R.string.aliuser_scan_login_fail), 0);
                            }
                        });
                    }
                }
            }

            public void onSystemError(RpcResponse rpcResponse) {
                FaceLoginPresenter.this.onFaceLoginError(rpcResponse, "SystemError");
            }

            public void onError(RpcResponse rpcResponse) {
                FaceLoginPresenter.this.onFaceLoginError(rpcResponse, "Error");
            }
        });
    }

    /* access modifiers changed from: private */
    public void onFaceLoginError(RpcResponse rpcResponse, String str) {
        Properties properties = new Properties();
        properties.setProperty("is_success", ApiConstants.UTConstants.UT_SUCCESS_F);
        UserTrackAdapter.sendUT((String) null, ApiConstants.UTConstants.UT_FACE_GENERATE_RESULT, str, properties);
        if (this.mViewer != null && this.mViewer.isActive() && this.mViewer.getBaseActivity() != null) {
            this.mViewer.dismissLoading();
            this.mViewer.toast((rpcResponse == null || TextUtils.isEmpty(rpcResponse.message)) ? this.mViewer.getBaseActivity().getString(R.string.aliuser_network_error) : rpcResponse.message, 0);
        }
    }

    public void activeFaceLogin(final LoginParam loginParam) {
        if (loginParam != null) {
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, GetVerifyUrlResponse>() {
                private long userId;

                /* access modifiers changed from: protected */
                public GetVerifyUrlResponse doInBackground(Object[] objArr) {
                    VerifyParam verifyParam = new VerifyParam();
                    verifyParam.fromSite = FaceLoginPresenter.this.mViewer.getLoginSite();
                    verifyParam.actionType = "h5_non_login_open_verify";
                    if (loginParam != null) {
                        verifyParam.deviceTokenKey = loginParam.deviceTokenKey;
                        verifyParam.userId = loginParam.havanaId + "";
                        this.userId = loginParam.havanaId;
                    }
                    try {
                        return VerifyServiceImpl.getInstance().getNonLoginVerfiyUrl(verifyParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(GetVerifyUrlResponse getVerifyUrlResponse) {
                    if (getVerifyUrlResponse != null) {
                        if (getVerifyUrlResponse.code == 3000 && getVerifyUrlResponse.returnValue != null && !TextUtils.isEmpty(((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).url)) {
                            UrlParam urlParam = new UrlParam();
                            urlParam.url = ((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).url;
                            urlParam.ivScene = "h5_non_login_open_verify";
                            urlParam.userid = this.userId + "";
                            ((FaceService) ServiceFactory.getService(FaceService.class)).activeFaceLogin(((GetVerifyUrlReturnData) getVerifyUrlResponse.returnValue).url, (FaceVerifyCallback) null);
                        } else if (getVerifyUrlResponse.code == 13050) {
                            FaceLoginPresenter.this.fetchScanToken(loginParam);
                        }
                    }
                }
            }, new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public RpcResponse login(LoginParam loginParam) {
        if (loginParam.token != null) {
            return UserLoginServiceImpl.getInstance().loginByToken(loginParam);
        }
        return null;
    }
}
