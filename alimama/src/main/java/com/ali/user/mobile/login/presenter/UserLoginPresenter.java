package com.ali.user.mobile.login.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.log.ApiReferer;
import com.ali.user.mobile.log.LongLifeCycleUserTrack;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.login.ui.BaseLoginView;
import com.ali.user.mobile.login.ui.UserLoginView;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.h5.MtopAccountCenterUrlResponseData;
import com.ali.user.mobile.rpc.h5.MtopFoundPasswordResponseData;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.url.model.AccountCenterParam;
import com.ali.user.mobile.url.service.impl.UrlFetchServiceImpl;
import com.ali.user.mobile.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;

public class UserLoginPresenter extends BaseLoginPresenter {
    public UserLoginPresenter(BaseLoginView baseLoginView, LoginParam loginParam) {
        super(baseLoginView, loginParam);
    }

    /* access modifiers changed from: protected */
    public RpcResponse login(LoginParam loginParam) {
        if (loginParam.token != null) {
            return UserLoginServiceImpl.getInstance().loginByToken(loginParam);
        }
        return UserLoginServiceImpl.getInstance().unifyLoginWithTaobaoGW(loginParam);
    }

    public void setSnsToken(String str) {
        if (this.mLoginParam == null) {
            this.mLoginParam = new LoginParam();
        }
        this.mLoginParam.snsToken = str;
        this.mLoginParam.loginSite = DataProviderFactory.getDataProvider().getSite();
        if (this.mLoginParam.externParams == null) {
            this.mLoginParam.externParams = new HashMap();
        }
        this.mLoginParam.externParams.put("apiReferer", ApiReferer.generateApiReferer());
        this.mLoginParam.tid = DataProviderFactory.getDataProvider().getTID();
    }

    /* access modifiers changed from: protected */
    public void onReceiveToast(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        if (!TextUtils.isEmpty(LongLifeCycleUserTrack.getResultScene())) {
            LongLifeCycleUserTrack.sendUT(LongLifeCycleUserTrack.getResultScene() + "_FAILURE");
        }
        if (this.mViewer != null && (this.mViewer instanceof UserLoginView)) {
            ((UserLoginView) this.mViewer).showFindPasswordAlert(loginParam, rpcResponse);
        }
    }

    public void fetchUrlAndToWebView(Context context, final String str) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, MtopAccountCenterUrlResponseData>() {
                /* access modifiers changed from: protected */
                public MtopAccountCenterUrlResponseData doInBackground(Object... objArr) {
                    try {
                        AccountCenterParam accountCenterParam = new AccountCenterParam();
                        accountCenterParam.userInputName = str;
                        accountCenterParam.fromSite = UserLoginPresenter.this.mViewer.getLoginSite();
                        accountCenterParam.scene = "foundpassword";
                        return UrlFetchServiceImpl.getInstance().foundH5urls(accountCenterParam);
                    } catch (RpcException | Exception unused) {
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(MtopAccountCenterUrlResponseData mtopAccountCenterUrlResponseData) {
                    if (UserLoginPresenter.this.mViewer != null && UserLoginPresenter.this.mViewer.isActive()) {
                        if (mtopAccountCenterUrlResponseData == null) {
                            try {
                                UserLoginPresenter.this.mViewer.toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error), 0);
                                if (UserLoginPresenter.this.mViewer != null) {
                                    UserLoginPresenter.this.mViewer.dismissLoading();
                                }
                            } catch (RpcException e) {
                                SDKExceptionHelper.getInstance().rpcExceptionHandler(e);
                                if (UserLoginPresenter.this.mViewer == null) {
                                }
                            } catch (Exception unused) {
                                if (UserLoginPresenter.this.mViewer == null) {
                                }
                            } catch (Throwable th) {
                                if (UserLoginPresenter.this.mViewer != null) {
                                    UserLoginPresenter.this.mViewer.dismissLoading();
                                }
                                throw th;
                            }
                        } else {
                            if (mtopAccountCenterUrlResponseData.h5Url != null) {
                                LoginParam loginParam = new LoginParam();
                                loginParam.tokenType = TokenType.FIND_PWD;
                                LoginReturnData loginReturnData = new LoginReturnData();
                                loginReturnData.site = UserLoginPresenter.this.mViewer.getLoginSite();
                                NavigatorManager.getInstance().navToWebViewPage(UserLoginPresenter.this.mViewer.getBaseActivity(), mtopAccountCenterUrlResponseData.h5Url, loginParam, loginReturnData);
                            } else {
                                UserLoginPresenter.this.mViewer.toast(mtopAccountCenterUrlResponseData.errorMessage, 0);
                            }
                            if (UserLoginPresenter.this.mViewer == null) {
                                return;
                            }
                            UserLoginPresenter.this.mViewer.dismissLoading();
                        }
                    }
                }
            }, new Object[0]);
        }
    }

    public void fetchUrlAndToWebViewWithUserId(Context context, final String str, final long j) {
        if (this.mViewer != null && this.mViewer.isActive()) {
            this.mViewer.showLoading();
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, MtopFoundPasswordResponseData>() {
                /* access modifiers changed from: protected */
                public MtopFoundPasswordResponseData doInBackground(Object... objArr) {
                    try {
                        AccountCenterParam accountCenterParam = new AccountCenterParam();
                        accountCenterParam.havanaId = String.valueOf(j);
                        HistoryAccount matchHistoryAccount = SecurityGuardManagerWraper.matchHistoryAccount(str);
                        if (matchHistoryAccount != null) {
                            accountCenterParam.deviceTokenKey = matchHistoryAccount.tokenKey;
                        }
                        accountCenterParam.fromSite = UserLoginPresenter.this.mViewer.getLoginSite();
                        return UrlFetchServiceImpl.getInstance().foundPassword(accountCenterParam);
                    } catch (RpcException | Exception unused) {
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(MtopFoundPasswordResponseData mtopFoundPasswordResponseData) {
                    if (UserLoginPresenter.this.mViewer != null && UserLoginPresenter.this.mViewer.isActive()) {
                        try {
                            UserLoginPresenter.this.handlePhoneLoginFindPwd(mtopFoundPasswordResponseData);
                            if (UserLoginPresenter.this.mViewer == null) {
                                return;
                            }
                        } catch (RpcException e) {
                            SDKExceptionHelper.getInstance().rpcExceptionHandler(e);
                            if (UserLoginPresenter.this.mViewer == null) {
                                return;
                            }
                        } catch (Exception unused) {
                            if (UserLoginPresenter.this.mViewer == null) {
                                return;
                            }
                        } catch (Throwable th) {
                            if (UserLoginPresenter.this.mViewer != null) {
                                UserLoginPresenter.this.mViewer.dismissLoading();
                            }
                            throw th;
                        }
                        UserLoginPresenter.this.mViewer.dismissLoading();
                    }
                }
            }, new Object[0]);
        }
    }

    /* access modifiers changed from: private */
    public void handlePhoneLoginFindPwd(MtopFoundPasswordResponseData mtopFoundPasswordResponseData) {
        if (mtopFoundPasswordResponseData == null) {
            this.mViewer.toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error), 0);
            return;
        }
        final HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(mtopFoundPasswordResponseData.mobileLoginUrl)) {
            String string = this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_find_pwd_phone_hint, new Object[]{StringUtil.hideAccount(mtopFoundPasswordResponseData.secMobile)});
            hashMap.put(string, mtopFoundPasswordResponseData.mobileLoginUrl);
            arrayList.add(string);
        }
        if (!TextUtils.isEmpty(mtopFoundPasswordResponseData.passwordFindUrl)) {
            String string2 = this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_alert_findpwd);
            hashMap.put(string2, mtopFoundPasswordResponseData.passwordFindUrl);
            arrayList.add(string2);
        }
        if (arrayList.size() > 1) {
            arrayList.add(this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_cancel));
            final String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
            this.mViewer.alertList(strArr, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (UserLoginPresenter.this.mViewer != null && UserLoginPresenter.this.mViewer.isActive()) {
                        if (TextUtils.equals(UserLoginPresenter.this.mViewer.getBaseActivity().getResources().getString(R.string.aliuser_cancel), strArr[i])) {
                            UserTrackAdapter.sendControlUT(ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN, "Button-ForgetPwd-Cancel");
                            UserLoginPresenter.this.mViewer.dismissAlertDialog();
                            return;
                        }
                        LoginParam loginParam = new LoginParam();
                        loginParam.tokenType = TokenType.FIND_PWD;
                        loginParam.isFromAccount = true;
                        LoginReturnData loginReturnData = new LoginReturnData();
                        loginReturnData.site = UserLoginPresenter.this.mViewer.getLoginSite();
                        if (i == 0) {
                            UserTrackAdapter.sendControlUT(ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN, "Button-LoginByPhone");
                            loginReturnData.scene = "1016";
                        } else {
                            UserTrackAdapter.sendControlUT(ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN, "Button-ResetPwd");
                            loginReturnData.scene = "1014";
                        }
                        HistoryAccount historyAccount = UserLoginPresenter.this.mViewer.getHistoryAccount();
                        if (historyAccount != null) {
                            loginReturnData.showLoginId = historyAccount.userInputName;
                        }
                        NavigatorManager.getInstance().navToWebViewPage(UserLoginPresenter.this.mViewer.getBaseActivity(), (String) hashMap.get(strArr[i]), loginParam, loginReturnData);
                    }
                }
            }, true);
        } else if (arrayList.size() == 1) {
            LoginParam loginParam = new LoginParam();
            loginParam.tokenType = TokenType.FIND_PWD;
            loginParam.isFromAccount = true;
            LoginReturnData loginReturnData = new LoginReturnData();
            loginReturnData.site = this.mViewer.getLoginSite();
            loginReturnData.scene = "1014";
            NavigatorManager.getInstance().navToWebViewPage(this.mViewer.getBaseActivity(), mtopFoundPasswordResponseData.passwordFindUrl, loginParam, loginReturnData);
        } else {
            this.mViewer.toast(DataProviderFactory.getApplicationContext().getString(R.string.aliuser_network_error), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void cleanDataHodler() {
        super.cleanDataHodler();
        if (this.mViewer instanceof UserLoginView) {
            ((UserLoginView) this.mViewer).clearPasswordInput();
        }
    }
}
