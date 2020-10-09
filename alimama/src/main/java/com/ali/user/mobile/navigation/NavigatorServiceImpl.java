package com.ali.user.mobile.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.SDKExceptionHelper;
import com.ali.user.mobile.bind.NewAccountBindActivity;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegType;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.model.TokenType;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.register.model.BaseRegistRequest;
import com.ali.user.mobile.register.service.impl.UserRegisterServiceImpl;
import com.ali.user.mobile.rpc.LoginHistory;
import com.ali.user.mobile.rpc.register.model.MtopRegisterH5ResponseData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.webview.AliUserRegisterWebviewActivity;
import com.ali.user.mobile.webview.WebViewActivity;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.constants.LoginSceneConstants;
import com.taobao.orange.OrangeConfig;
import java.util.HashMap;

public class NavigatorServiceImpl implements NavigatorService {
    private static final String TAG = "Login.NavigatorServiceImpl";

    public void openBindPage(Context context, Boolean bool, String str, String str2) {
    }

    public void openAccountBindPage(Context context, String str) {
        Intent intent = new Intent(DataProviderFactory.getApplicationContext(), NewAccountBindActivity.class);
        intent.putExtra(WebConstant.WEBURL, str);
        if (context == null) {
            context = DataProviderFactory.getApplicationContext();
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
    }

    public void openLoginPage(final Context context, final String str, final Bundle bundle) {
        try {
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, LoginHistory>() {
                /* access modifiers changed from: protected */
                public LoginHistory doInBackground(Object... objArr) {
                    try {
                        return SecurityGuardManagerWraper.getLoginHistory();
                    } catch (Exception unused) {
                        return null;
                    } catch (Throwable unused2) {
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(LoginHistory loginHistory) {
                    boolean z;
                    boolean z2 = true;
                    if (DataProviderFactory.getDataProvider().isTaobaoApp()) {
                        try {
                            Class<?> cls = Class.forName("com.taobao.taobaocompat.lifecycle.AppForgroundObserver");
                            z = cls.getField("isForeground").getBoolean(cls);
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                        if (!z) {
                            boolean access$000 = bundle != null ? NavigatorServiceImpl.this.isProcessSupportLogin(context, bundle.getString("process_name")) : false;
                            if (DataProviderFactory.getDataProvider().isForbidLoginFromBackground() && !access$000) {
                                Intent intent = new Intent();
                                intent.setAction("NOTIFY_LOGIN_FAILED");
                                intent.setPackage(DataProviderFactory.getApplicationContext().getPackageName());
                                DataProviderFactory.getApplicationContext().sendBroadcast(intent);
                                return;
                            }
                        }
                    }
                    boolean z3 = bundle != null && TextUtils.equals(bundle.getString(LoginConstant.LOGIN_TYPE_SMS), "true");
                    if (bundle != null && bundle.getBoolean(LoginConstant.LAUCNH_MOBILE_LOGIN_FRAGMENT_LABEL, false)) {
                        z3 = true;
                    }
                    if (bundle == null || !bundle.getBoolean(LoginConstant.LAUNCH_PASS_GUIDE_FRAGMENT, false)) {
                        z2 = false;
                    }
                    LoginParam loginParam = new LoginParam();
                    loginParam.source = bundle != null ? bundle.getString("source") : "";
                    if (!TextUtils.isEmpty(str)) {
                        loginParam.externParams = new HashMap();
                        loginParam.externParams.put("apiReferer", str);
                    }
                    NavigatorManager.getInstance().navToLoginPage(context, JSON.toJSONString(loginParam), z3, z2);
                }
            }, new Object[0]);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setAction("NOTIFY_LOGIN_FAILED");
                intent.setPackage(DataProviderFactory.getApplicationContext().getPackageName());
                DataProviderFactory.getApplicationContext().sendBroadcast(intent);
            } catch (Exception unused) {
            }
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public boolean isProcessSupportLogin(Context context, String str) {
        String[] split;
        try {
            String config = OrangeConfig.getInstance().getConfig("login4android", "process_whitelist", "com.taobao.taobao;com.taobao.taobao:wml");
            if (!TextUtils.isEmpty(config) && (split = config.split(";")) != null && split.length > 0) {
                for (String equals : split) {
                    if (TextUtils.equals(equals, str)) {
                        return true;
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return false;
    }

    public void openRegisterPage(final Context context, final RegistParam registParam) {
        if (registParam.registSite == 6) {
            fetchRegisterUrl(context, registParam);
        } else if (registParam.registSite == 3 && DataProviderFactory.getDataProvider().needEnterPriseRegister()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, 16973939));
            View inflate = LayoutInflater.from(context).inflate(R.layout.aliuser_cbu_register_dialog, (ViewGroup) null);
            final AlertDialog create = builder.setView(inflate).create();
            Window window = create.getWindow();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
            window.setAttributes(layoutParams);
            window.setGravity(80);
            create.show();
            inflate.findViewById(R.id.aliuser_register_enterprise).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    registParam.regFrom = "CBU_ENTERPRISE";
                    NavigatorServiceImpl.this.fetchRegisterUrl(context, registParam);
                    create.dismiss();
                }
            });
            inflate.findViewById(R.id.aliuser_register_person).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    NavigatorManager.getInstance().navToRegisterPage(context, registParam);
                    create.dismiss();
                }
            });
            inflate.findViewById(R.id.aliuser_register_cancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    create.dismiss();
                }
            });
        } else if (RegType.H5_REG.equals(DataProviderFactory.getDataProvider().getRegType())) {
            fetchRegisterUrl(context, registParam);
        } else {
            NavigatorManager.getInstance().navToRegisterPage(context, registParam);
        }
    }

    public void fetchRegisterUrl(final Context context, final RegistParam registParam) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, MtopRegisterH5ResponseData>() {
            /* access modifiers changed from: protected */
            public MtopRegisterH5ResponseData doInBackground(Object... objArr) {
                try {
                    BaseRegistRequest baseRegistRequest = new BaseRegistRequest();
                    baseRegistRequest.regFrom = registParam.regFrom;
                    baseRegistRequest.registSite = registParam.registSite;
                    return UserRegisterServiceImpl.getInstance().getRegisterH5Url(baseRegistRequest);
                } catch (RpcException e) {
                    SDKExceptionHelper.getInstance().rpcExceptionHandler(e);
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(MtopRegisterH5ResponseData mtopRegisterH5ResponseData) {
                if (mtopRegisterH5ResponseData != null) {
                    try {
                        if (!TextUtils.isEmpty((CharSequence) mtopRegisterH5ResponseData.returnValue)) {
                            Context context = context;
                            if (context == null) {
                                context = DataProviderFactory.getApplicationContext();
                            }
                            Intent intent = new Intent(context, AliUserRegisterWebviewActivity.class);
                            if (!(context instanceof Activity)) {
                                intent.addFlags(268435456);
                            }
                            intent.putExtra(WebConstant.SITE, registParam.registSite);
                            intent.putExtra(WebConstant.WEBURL, (String) mtopRegisterH5ResponseData.returnValue);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Object[0]);
    }

    public void openWebViewPage(Context context, UrlParam urlParam) {
        Intent intent = new Intent(context, WebViewActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        addData(urlParam, intent);
        context.startActivity(intent);
    }

    private void addData(UrlParam urlParam, Intent intent) {
        intent.putExtra(WebConstant.WEBURL, urlParam.url);
        if (LoginSceneConstants.SCENE_CHANGEPASSWORD.equals(urlParam.scene) || LoginSceneConstants.SCENE_FOUNDPASSWORD.equals(urlParam.scene)) {
            intent.putExtra(WebConstant.WEB_LOGIN_TOKEN_TYPE, TokenType.FIND_PWD);
        } else {
            intent.putExtra(WebConstant.WEB_LOGIN_TOKEN_TYPE, urlParam.tokenType);
        }
        if (!TextUtils.isEmpty(urlParam.ivScene)) {
            intent.putExtra(WebConstant.WEB_IV_SCENE, urlParam.ivScene);
        }
        if (!TextUtils.isEmpty(urlParam.scene)) {
            intent.putExtra("scene", urlParam.scene);
        }
        if (!TextUtils.isEmpty(urlParam.token)) {
            intent.putExtra("token", urlParam.token);
        }
        if (!TextUtils.isEmpty(urlParam.userid)) {
            intent.putExtra("USERID", urlParam.userid);
        }
        if (urlParam.loginParam != null) {
            intent.putExtra("loginParam", urlParam.loginParam);
        }
    }

    public void startWebViewForResult(Activity activity, UrlParam urlParam) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        addData(urlParam, intent);
        activity.startActivityForResult(intent, urlParam.requestCode);
    }

    public void navToLoginPage(Context context, String str, boolean z, boolean z2) {
        NavigatorManager.getInstance().navToLoginPage(context, str, z, z2);
    }
}
