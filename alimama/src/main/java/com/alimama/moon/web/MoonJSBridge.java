package com.alimama.moon.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.App;
import com.alimama.moon.alipay.AlipayAdapter;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.moon.ui.dialog.AvatarDialog;
import com.alimama.moon.windvane.jsbridge.JsBridgeUtil;
import com.alimama.moon.windvane.jsbridge.model.ShowAvatarDialogModel;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.messageCenter.model.AlertMessageRepository;
import com.alimama.union.app.messageCenter.model.JSMessage;
import com.alimama.union.app.messageCenter.view.Utils;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonJSBridge extends WVApiPlugin {
    private static final String ACTION_JUMP_TO_PAGE = "jumpToPage";
    private static final String ACTION_LOAD_TOOLBAR_MENU = "loadToolbarMenu";
    private static final String ACTION_OPEN_WEB_VIEW = "openWebView";
    private static final String ACTION_PAY = "payUrlOrder";
    private static final String ACTION_SEND_NEW_USER_GRADE = "sendNewUserGradeAlertMessageAsync";
    private static final String ACTION_SET_NAV_TAB = "setNavTab";
    private static final String ACTION_SET_PAGE_TITLE = "setCustomPageTitle";
    private static final String ACTION_SHOW_ALERT = "showAlert";
    private static final String ACTION_SHOW_AVATAR_DIALOG = "showAvatarDailog";
    private static final String ACTION_WEB_VIEW_REFRESH = "webViewPullToRefresh";
    public static final String JUMP_TO_HOME_PAGE_IS_REFRESH_KEY = "jump_to_home_page_is_refresh_key";
    public static final String JUMP_TO_HOME_PAGE_NUM_KEY = "jump_to_home_page_num_key";
    private static final String JUMP_TO_PAGE_SCHEME = "moon://home";
    private static final String TAG = "MoonJSBridge";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MoonJSBridge.class);
    @Inject
    AlertMessageRepository alertMessageRepository;
    @Inject
    ILogin login;

    public void initialize(Context context, IWVWebView iWVWebView, Object obj) {
        super.initialize(context, iWVWebView, obj);
        App.getAppComponent().inject(this);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean execute(java.lang.String r4, java.lang.String r5, android.taobao.windvane.jsbridge.WVCallBackContext r6) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -1931275169: goto L_0x0067;
                case -1856058393: goto L_0x005d;
                case -555567752: goto L_0x0053;
                case -189112362: goto L_0x0049;
                case 140957364: goto L_0x003f;
                case 428818036: goto L_0x0035;
                case 1026644591: goto L_0x002b;
                case 1157006301: goto L_0x0021;
                case 1552025230: goto L_0x0016;
                case 1635109762: goto L_0x000b;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x0071
        L_0x000b:
            java.lang.String r0 = "webViewPullToRefresh"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 9
            goto L_0x0072
        L_0x0016:
            java.lang.String r0 = "showAvatarDailog"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 8
            goto L_0x0072
        L_0x0021:
            java.lang.String r0 = "sendNewUserGradeAlertMessageAsync"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 2
            goto L_0x0072
        L_0x002b:
            java.lang.String r0 = "openWebView"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 0
            goto L_0x0072
        L_0x0035:
            java.lang.String r0 = "setNavTab"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 6
            goto L_0x0072
        L_0x003f:
            java.lang.String r0 = "loadToolbarMenu"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 4
            goto L_0x0072
        L_0x0049:
            java.lang.String r0 = "setCustomPageTitle"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 5
            goto L_0x0072
        L_0x0053:
            java.lang.String r0 = "jumpToPage"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 7
            goto L_0x0072
        L_0x005d:
            java.lang.String r0 = "payUrlOrder"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 3
            goto L_0x0072
        L_0x0067:
            java.lang.String r0 = "showAlert"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0071
            r4 = 1
            goto L_0x0072
        L_0x0071:
            r4 = -1
        L_0x0072:
            switch(r4) {
                case 0: goto L_0x009a;
                case 1: goto L_0x0096;
                case 2: goto L_0x0092;
                case 3: goto L_0x008e;
                case 4: goto L_0x008a;
                case 5: goto L_0x0086;
                case 6: goto L_0x0082;
                case 7: goto L_0x007e;
                case 8: goto L_0x007a;
                case 9: goto L_0x0076;
                default: goto L_0x0075;
            }
        L_0x0075:
            return r1
        L_0x0076:
            r3.updateWebViewPullToRefresh(r5, r6)
            return r2
        L_0x007a:
            r3.showAvatarDialog(r5, r6)
            return r2
        L_0x007e:
            r3.jumpToPage(r5, r6)
            return r2
        L_0x0082:
            r3.setNavTab(r5, r6)
            return r2
        L_0x0086:
            r3.setCustomPageTitle(r5, r6)
            return r2
        L_0x008a:
            r3.loadToolbarMenu(r5, r6)
            return r2
        L_0x008e:
            r3.payUrlOrder(r5, r6)
            return r2
        L_0x0092:
            r3.sendNewUserGradeAlertMessageAsync(r5, r6)
            return r2
        L_0x0096:
            r3.showAlert(r5, r6)
            return r2
        L_0x009a:
            r3.openWebView(r5, r6)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.web.MoonJSBridge.execute(java.lang.String, java.lang.String, android.taobao.windvane.jsbridge.WVCallBackContext):boolean");
    }

    private void updateWebViewPullToRefresh(String str, WVCallBackContext wVCallBackContext) {
        boolean equals = TextUtils.equals(JsBridgeUtil.parseParams(str, "canPullToRefresh", wVCallBackContext), "true");
        if (this.mContext instanceof WebActivity) {
            try {
                ((WebActivity) this.mContext).updatePullToRefreshSwitch(equals);
                wVCallBackContext.success();
            } catch (Exception unused) {
                wVCallBackContext.error(WVResult.RET_PARAM_ERR);
            }
        }
    }

    private void showAvatarDialog(String str, WVCallBackContext wVCallBackContext) {
        AvatarDialog.showAvatarDialog(this.mContext, (ShowAvatarDialogModel) JSON.parseObject(str, ShowAvatarDialogModel.class), wVCallBackContext);
    }

    private void jumpToPage(String str, WVCallBackContext wVCallBackContext) {
        String parseParams = JsBridgeUtil.parseParams(str, "scheme", wVCallBackContext);
        if (parseParams.startsWith(JUMP_TO_PAGE_SCHEME)) {
            try {
                String queryParameter = Uri.parse(parseParams).getQueryParameter("page");
                Boolean valueOf = Boolean.valueOf(TextUtils.equals(Uri.parse(parseParams).getQueryParameter("refresh"), "true"));
                Intent intent = new Intent(this.mContext, BottomNavActivity.class);
                intent.putExtra(JUMP_TO_HOME_PAGE_NUM_KEY, queryParameter);
                intent.putExtra(JUMP_TO_HOME_PAGE_IS_REFRESH_KEY, valueOf);
                this.mContext.startActivity(intent);
            } catch (Exception unused) {
            }
        }
    }

    private void setNavTab(String str, WVCallBackContext wVCallBackContext) {
        if (this.mContext instanceof WebActivity) {
            try {
                ((WebActivity) this.mContext).loadNavTabMenu((NavTabParam) JSON.parseObject(str, NavTabParam.class));
                wVCallBackContext.success();
            } catch (Exception unused) {
                wVCallBackContext.error(WVResult.RET_PARAM_ERR);
            }
        }
    }

    private void sendNewUserGradeAlertMessageAsync(String str, WVCallBackContext wVCallBackContext) {
        this.alertMessageRepository.sendNewUserGradeAlertMessageAsync((JSMessage) JSON.parseObject(str, JSMessage.class));
        this.login.saveAccount();
        wVCallBackContext.success(WVResult.RET_SUCCESS);
    }

    private void showAlert(String str, WVCallBackContext wVCallBackContext) {
        try {
            Utils.showAlert((BaseActivity) this.mContext, (JSMessage) JSON.parseObject(str, JSMessage.class));
            wVCallBackContext.success(WVResult.RET_SUCCESS);
        } catch (Exception e) {
            logger.warn("show alert exception: {}", (Object) e.getMessage());
            wVCallBackContext.error(WVResult.RET_FAIL);
        }
    }

    private void openWebView(String str, WVCallBackContext wVCallBackContext) {
        try {
            ShareParam shareParam = (ShareParam) JSON.parseObject(str, ShareParam.class);
            if (shareParam == null || shareParam.getUrl() == null || TextUtils.isEmpty(shareParam.getUrl())) {
                wVCallBackContext.error(WVResult.RET_PARAM_ERR);
                return;
            }
            try {
                Uri parse = Uri.parse(shareParam.getUrl());
                if (TextUtils.isEmpty(parse.getScheme())) {
                    parse = parse.buildUpon().scheme("https").build();
                }
                wVCallBackContext.success(WVResult.RET_SUCCESS);
                Context context = this.mWebView.getContext();
                Intent intent = new Intent(context, WebActivity.class);
                intent.setData(parse);
                context.startActivity(intent);
            } catch (Exception unused) {
                wVCallBackContext.error(WVResult.RET_PARAM_ERR);
            }
        } catch (Exception unused2) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        }
    }

    private void payUrlOrder(String str, final WVCallBackContext wVCallBackContext) {
        try {
            AlipayAdapter.payV2((BaseActivity) this.mContext, ((PayOrderParam) JSON.parseObject(str, PayOrderParam.class)).getOrderInfo(), new AlipayAdapter.AlipayCallback() {
                /* JADX WARNING: Removed duplicated region for block: B:17:0x003d  */
                /* JADX WARNING: Removed duplicated region for block: B:18:0x0048  */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void payComplete(java.lang.String r3, com.alimama.moon.alipay.PayResult r4) {
                    /*
                        r2 = this;
                        android.taobao.windvane.jsbridge.WVResult r4 = android.taobao.windvane.jsbridge.WVResult.RET_SUCCESS
                        java.lang.String r0 = "resultCode"
                        r4.addData((java.lang.String) r0, (java.lang.String) r3)
                        int r0 = r3.hashCode()
                        r1 = 1656382(0x19463e, float:2.321086E-39)
                        if (r0 == r1) goto L_0x002f
                        r1 = 1715960(0x1a2ef8, float:2.404572E-39)
                        if (r0 == r1) goto L_0x0025
                        r1 = 1745751(0x1aa357, float:2.446318E-39)
                        if (r0 == r1) goto L_0x001b
                        goto L_0x0039
                    L_0x001b:
                        java.lang.String r0 = "9000"
                        boolean r0 = r3.equals(r0)
                        if (r0 == 0) goto L_0x0039
                        r0 = 2
                        goto L_0x003a
                    L_0x0025:
                        java.lang.String r0 = "8000"
                        boolean r0 = r3.equals(r0)
                        if (r0 == 0) goto L_0x0039
                        r0 = 1
                        goto L_0x003a
                    L_0x002f:
                        java.lang.String r0 = "6004"
                        boolean r0 = r3.equals(r0)
                        if (r0 == 0) goto L_0x0039
                        r0 = 0
                        goto L_0x003a
                    L_0x0039:
                        r0 = -1
                    L_0x003a:
                        switch(r0) {
                            case 0: goto L_0x0048;
                            case 1: goto L_0x0048;
                            case 2: goto L_0x0048;
                            default: goto L_0x003d;
                        }
                    L_0x003d:
                        android.taobao.windvane.jsbridge.WVCallBackContext r0 = r4
                        r0.error((android.taobao.windvane.jsbridge.WVResult) r4)
                        java.lang.String r4 = "MoonJSBridge"
                        com.alimama.union.app.logger.BusinessMonitorLogger.Alipay.payResult(r4, r3)
                        goto L_0x004d
                    L_0x0048:
                        android.taobao.windvane.jsbridge.WVCallBackContext r3 = r4
                        r3.success((android.taobao.windvane.jsbridge.WVResult) r4)
                    L_0x004d:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.web.MoonJSBridge.AnonymousClass1.payComplete(java.lang.String, com.alimama.moon.alipay.PayResult):void");
                }
            });
        } catch (Exception e) {
            logger.warn("payUrlOrder exception: {}", (Object) e.getMessage());
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        }
    }

    private void loadToolbarMenu(@NonNull String str, @NonNull WVCallBackContext wVCallBackContext) {
        if (!(this.mContext instanceof WebActivity)) {
            logger.error("js bridge isn't loaded in web activity");
            return;
        }
        try {
            ((WebActivity) this.mContext).loadToolbarMenu((MenuItemParam) JSON.parseObject(str, MenuItemParam.class));
            wVCallBackContext.success();
        } catch (Exception e) {
            logger.warn("parsing error when trying to load menu items", (Throwable) e);
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        }
    }

    private void setCustomPageTitle(@NonNull String str, @NonNull WVCallBackContext wVCallBackContext) {
        if (!(this.mContext instanceof WebActivity)) {
            logger.error("js bridge isn't loaded in web activity");
            return;
        }
        try {
            ((WebActivity) this.mContext).changeTitle(((PageInfoParam) JSON.parseObject(str, PageInfoParam.class)).getTitle(), "setCustomPageTitle");
            wVCallBackContext.success();
        } catch (Exception e) {
            logger.warn("parsing error when trying to load menu items", (Throwable) e);
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        }
    }
}
