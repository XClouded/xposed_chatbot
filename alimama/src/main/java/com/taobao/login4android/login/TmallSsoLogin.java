package com.taobao.login4android.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.taobao.login4android.Login;
import com.taobao.orange.util.MD5Util;

public class TmallSsoLogin {
    private static final String TAG = "login.TmallSsoLogin";
    private static TmallSsoLogin sTmallSsoLogin;

    private TmallSsoLogin() {
    }

    public static synchronized TmallSsoLogin getInstance() {
        TmallSsoLogin tmallSsoLogin;
        synchronized (TmallSsoLogin.class) {
            if (sTmallSsoLogin == null) {
                sTmallSsoLogin = new TmallSsoLogin();
            }
            tmallSsoLogin = sTmallSsoLogin;
        }
        return tmallSsoLogin;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:1|2|3|(7:5|6|7|8|9|10|11)|13|14|(3:16|17|18)(7:19|20|21|(1:23)(1:24)|25|26|27)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0021 */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void jumpToTmallWithLoginToken(final android.content.Context r7, java.lang.String r8) {
        /*
            r6 = this;
            monitor-enter(r6)
            java.lang.String r0 = "false"
            r1 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r8)     // Catch:{ all -> 0x006e }
            if (r2 != 0) goto L_0x0021
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x0021 }
            r2.<init>(r8)     // Catch:{ Exception -> 0x0021 }
            java.lang.String r8 = "onlyLaunch"
            java.lang.Object r8 = r2.get(r8)     // Catch:{ Exception -> 0x0021 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x0021 }
            java.lang.String r0 = "targetPage"
            java.lang.Object r0 = r2.get(r0)     // Catch:{ Exception -> 0x0020 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0020 }
            r1 = r0
        L_0x0020:
            r0 = r8
        L_0x0021:
            com.taobao.login4android.login.TmallSsoLogin r8 = getInstance()     // Catch:{ all -> 0x006e }
            boolean r8 = r8.isSupportTmall(r7)     // Catch:{ all -> 0x006e }
            if (r8 != 0) goto L_0x0030
            com.taobao.login4android.constants.LoginStatus.resetLoginFlag()     // Catch:{ all -> 0x006e }
            monitor-exit(r6)
            return
        L_0x0030:
            java.lang.String r8 = "true"
            boolean r8 = android.text.TextUtils.equals(r8, r0)     // Catch:{ all -> 0x006e }
            if (r8 == 0) goto L_0x0043
            com.taobao.login4android.login.TmallSsoLogin r8 = getInstance()     // Catch:{ all -> 0x006e }
            java.lang.String r0 = ""
            r2 = 1
            r8.launchTMall(r7, r0, r1, r2)     // Catch:{ all -> 0x006e }
            goto L_0x0069
        L_0x0043:
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x006e }
            r3.<init>()     // Catch:{ all -> 0x006e }
            java.lang.String r8 = "source"
            java.lang.String r0 = "tb"
            r3.put(r8, r0)     // Catch:{ all -> 0x006e }
            com.taobao.login4android.login.LoginController r0 = com.taobao.login4android.login.LoginController.getInstance()     // Catch:{ all -> 0x006e }
            com.ali.user.mobile.app.dataprovider.IDataProvider r8 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ all -> 0x006e }
            int r8 = r8.getSite()     // Catch:{ all -> 0x006e }
            java.lang.String r2 = com.taobao.login4android.Login.getUserId()     // Catch:{ all -> 0x006e }
            r4 = 0
            com.taobao.login4android.login.TmallSsoLogin$1 r5 = new com.taobao.login4android.login.TmallSsoLogin$1     // Catch:{ all -> 0x006e }
            r5.<init>(r7, r1)     // Catch:{ all -> 0x006e }
            r1 = r8
            r0.applyTokenWithRemoteBiz(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x006e }
        L_0x0069:
            com.taobao.login4android.constants.LoginStatus.resetLoginFlag()     // Catch:{ all -> 0x006e }
            monitor-exit(r6)
            return
        L_0x006e:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.login4android.login.TmallSsoLogin.jumpToTmallWithLoginToken(android.content.Context, java.lang.String):void");
    }

    public void launchTMall(Context context, String str, String str2, boolean z) {
        try {
            Intent intent = new Intent();
            intent.addFlags(268435456);
            StringBuilder sb = new StringBuilder("tmall://account.taobao.com/crossAppLogin?");
            sb.append("loginToken=");
            sb.append(str);
            sb.append("&onlyLaunch=");
            sb.append(z);
            sb.append("&targetPage=");
            sb.append(str2);
            sb.append("&version=1.0");
            sb.append("&source=tb");
            sb.append("&encrpytedHid=" + MD5Util.md5(Login.getUserId()));
            intent.setData(Uri.parse(sb.toString()));
            intent.addFlags(67108864);
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                try {
                    context.startActivity(intent);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else {
                intent.setData(Uri.parse("tmall://tab.switch/home"));
                if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    try {
                        context.startActivity(intent);
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSupportTmall(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo("com.tmall.wireless", 1);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("tmall://account.taobao.com/crossAppLogin"));
            if (packageManager.queryIntentActivities(intent, 0).size() > 0) {
                return true;
            }
            Intent intent2 = new Intent();
            intent2.setAction("android.intent.action.VIEW");
            intent2.setData(Uri.parse("tmall://tab.switch/home"));
            return packageManager.queryIntentActivities(intent2, 0).size() > 0;
        } catch (PackageManager.NameNotFoundException | Exception unused) {
        }
    }
}
