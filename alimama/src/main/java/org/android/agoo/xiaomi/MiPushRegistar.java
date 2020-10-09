package org.android.agoo.xiaomi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.BaseNotifyClickActivity;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

public class MiPushRegistar {
    private static final String PACKAGE_XIAOMI = "com.xiaomi.xmsf";
    private static final String TAG = "MiPushRegistar";
    private static final String XIAOMI = "Xiaomi".toLowerCase();
    private static String phoneBrand = Build.BRAND;

    public static boolean checkDevice(Context context) {
        boolean z;
        PackageInfo packageInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (TextUtils.equals(XIAOMI, phoneBrand.toLowerCase()) && (packageInfo = packageManager.getPackageInfo(PACKAGE_XIAOMI, 4)) != null && packageInfo.versionCode >= 105) {
                z = true;
                ALog.d(TAG, "checkDevice", "result", Boolean.valueOf(z));
                return z;
            }
        } catch (Throwable th) {
            ALog.e(TAG, "checkDevice", th, new Object[0]);
        }
        z = false;
        ALog.d(TAG, "checkDevice", "result", Boolean.valueOf(z));
        return z;
    }

    public static void register(Context context, String str, String str2) {
        try {
            if (!UtilityImpl.isMainProcess(context)) {
                ALog.e(TAG, "register not in main process, return", new Object[0]);
            } else if (checkDevice(context)) {
                ALog.i(TAG, "register begin", new Object[0]);
                BaseNotifyClickActivity.addNotifyListener(new XiaoMiNotifyListener());
                MiPushClient.registerPush(context, str, str2);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "register", th, new Object[0]);
        }
    }

    public static void unregister(Context context) {
        try {
            MiPushClient.unregisterPush(context);
        } catch (Throwable th) {
            ALog.e(TAG, MiPushClient.COMMAND_UNREGISTER, th, new Object[0]);
        }
    }

    private static class XiaoMiNotifyListener implements BaseNotifyClickActivity.INotifyListener {
        public String getMsgSource() {
            return "xiaomi";
        }

        private XiaoMiNotifyListener() {
        }

        public String parseMsgFromIntent(Intent intent) {
            String str;
            try {
                str = ((MiPushMessage) intent.getSerializableExtra(PushMessageHelper.KEY_MESSAGE)).getContent();
            } catch (Exception unused) {
                str = null;
            }
            ALog.i(MiPushRegistar.TAG, "parseMsgFromIntent", "msg", str);
            return str;
        }

        public String toString() {
            return "INotifyListener: " + getMsgSource();
        }
    }
}
