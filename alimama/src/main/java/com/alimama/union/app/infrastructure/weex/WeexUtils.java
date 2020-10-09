package com.alimama.union.app.infrastructure.weex;

import alimama.com.unwetaologger.base.UNWLogger;
import alimama.com.unwrouter.PageInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alimama.moon.network.login.TaoBaoUrlFilter;
import com.alimama.moon.utils.StringUtil;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.alimama.union.app.pagerouter.MoonComponentManager;

public class WeexUtils {
    public static boolean startTargetActivity(Context context, String str) {
        ComponentName componentName;
        if (TextUtils.isEmpty(str) || context == null) {
            return false;
        }
        Uri parse = Uri.parse(str);
        String scheme = parse.getScheme();
        if (!TextUtils.isEmpty(scheme) && scheme.equals("unionApp") && PageInfo.find(parse.getHost()) != null) {
            MoonComponentManager.getInstance().getPageRouter().gotoPage(str);
            return true;
        } else if (TaoBaoUrlFilter.isHitShare(str)) {
            ShareUtils.showShare(context, Uri.parse(str).getQueryParameter("targetUrl"));
            return true;
        } else {
            Uri.Builder buildUpon = parse.buildUpon();
            if (TextUtils.isEmpty(scheme)) {
                buildUpon.scheme("https");
            }
            String queryParameter = parse.getQueryParameter("container");
            if (TextUtils.isEmpty(queryParameter)) {
                queryParameter = UNWLogger.LOG_VALUE_SUBTYPE_H5;
            }
            Intent intent = new Intent();
            if (!StringUtil.equalsIgnoreCase(str, "weixin://")) {
                char c = 65535;
                int hashCode = queryParameter.hashCode();
                if (hashCode != 3277) {
                    if (hashCode == 3645441 && queryParameter.equals("weex")) {
                        c = 1;
                    }
                } else if (queryParameter.equals(UNWLogger.LOG_VALUE_SUBTYPE_H5)) {
                    c = 0;
                }
                switch (c) {
                    case 0:
                        componentName = new ComponentName(context.getPackageName(), "com.alimama.moon.web.WebActivity");
                        break;
                    case 1:
                        componentName = new ComponentName(context.getPackageName(), "com.alimama.moon.ui.WeexActivity");
                        break;
                    default:
                        componentName = new ComponentName(context.getPackageName(), "com.alimama.moon.web.WebActivity");
                        break;
                }
            } else if (!ShareUtils.isInstallApp(context, "com.tencent.mm", "com.tencent.mm.ui.LauncherUI")) {
                ShareUtils.showDialog(context, "您还未安装微信，请安装完成后再来试试");
                return false;
            } else {
                componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addFlags(268435456);
                intent.addFlags(134217728);
            }
            intent.setComponent(componentName);
            intent.setData(buildUpon.build());
            context.startActivity(intent);
            return true;
        }
    }
}
