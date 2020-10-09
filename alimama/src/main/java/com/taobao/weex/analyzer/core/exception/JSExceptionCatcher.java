package com.taobao.weex.analyzer.core.exception;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.DevOptionsConfig;
import com.taobao.weex.analyzer.core.debug.DataRepository;
import com.taobao.weex.analyzer.view.alert.CompatibleAlertDialogBuilder;
import java.util.HashMap;
import java.util.Locale;

public class JSExceptionCatcher {
    private static final int ID = 4097;

    private JSExceptionCatcher() {
    }

    @Nullable
    public static AlertDialog catchException(@Nullable Context context, @Nullable DevOptionsConfig devOptionsConfig, @Nullable WXSDKInstance wXSDKInstance, @Nullable String str, @Nullable String str2) {
        if (context == null) {
            return null;
        }
        if (str == null && str2 == null) {
            return null;
        }
        if (devOptionsConfig != null && devOptionsConfig.isAllowExceptionNotification()) {
            sendNotification(context, wXSDKInstance, str, str2);
        }
        reportToCloud(context, str, str2);
        CompatibleAlertDialogBuilder compatibleAlertDialogBuilder = new CompatibleAlertDialogBuilder(context);
        compatibleAlertDialogBuilder.setTitle("WeexAnalyzer捕捉到异常");
        Locale locale = Locale.CHINA;
        Object[] objArr = new Object[2];
        if (TextUtils.isEmpty(str)) {
            str = "unknown";
        }
        objArr[0] = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = "unknown";
        }
        objArr[1] = str2;
        compatibleAlertDialogBuilder.setMessage(String.format(locale, "errorCode : %s\nerrorMsg : %s\n", objArr));
        compatibleAlertDialogBuilder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = compatibleAlertDialogBuilder.create();
        create.show();
        return create;
    }

    private static void reportToCloud(@NonNull Context context, String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("errorCode", str);
        hashMap.put(ILocatable.ERROR_MSG, str2);
        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
        intent.putExtra(Config.TYPE_JS_EXCEPTION, JSON.toJSONString(hashMap));
        intent.putExtra("type", Config.TYPE_JS_EXCEPTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private static void sendNotification(@NonNull Context context, @Nullable WXSDKInstance wXSDKInstance, @Nullable String str, @Nullable String str2) {
        String bundleUrl = wXSDKInstance != null ? wXSDKInstance.getBundleUrl() : null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.Builder autoCancel = builder.setSmallIcon(R.drawable.wxt_icon_bug_white).setContentTitle("WeexAnalyzer捕捉到异常").setAutoCancel(true);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        Locale locale = Locale.CHINA;
        Object[] objArr = new Object[3];
        if (TextUtils.isEmpty(bundleUrl)) {
            bundleUrl = "unknown";
        }
        objArr[0] = bundleUrl;
        objArr[1] = TextUtils.isEmpty(str) ? "unknown" : str;
        objArr[2] = TextUtils.isEmpty(str2) ? "unknown" : str2;
        NotificationCompat.Builder style = autoCancel.setStyle(bigTextStyle.bigText(String.format(locale, "page : %s\nerrorCode : %s\nerrorMsg : %s\n", objArr)));
        Locale locale2 = Locale.CHINA;
        Object[] objArr2 = new Object[2];
        if (TextUtils.isEmpty(str)) {
            str = "unknown";
        }
        objArr2[0] = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = "unknown";
        }
        objArr2[1] = str2;
        style.setContentText(String.format(locale2, "errorCode : %s,errorMsg : %s", objArr2));
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(1);
        }
        builder.setDefaults(3);
        ((NotificationManager) context.getSystemService("notification")).notify(4097, builder.build());
    }
}
