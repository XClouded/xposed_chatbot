package org.android.agoo.oppo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushAdapter;
import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.BaseNotifyClickActivity;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.List;
import org.android.agoo.control.NotifManager;

public class OppoRegister {
    private static final String OPPO_TOKEN = "OPPO_TOKEN";
    public static final String TAG = "OppoPush";
    /* access modifiers changed from: private */
    public static Context mContext;
    private static PushCallback mPushCallback = new PushAdapter() {
        public void onGetAliases(int i, List<SubscribeResult> list) {
        }

        public void onGetNotificationStatus(int i, int i2) {
        }

        public void onGetPushStatus(int i, int i2) {
        }

        public void onGetTags(int i, List<SubscribeResult> list) {
        }

        public void onGetUserAccounts(int i, List<SubscribeResult> list) {
        }

        public void onSetAliases(int i, List<SubscribeResult> list) {
        }

        public void onSetPushTime(int i, String str) {
        }

        public void onSetTags(int i, List<SubscribeResult> list) {
        }

        public void onSetUserAccounts(int i, List<SubscribeResult> list) {
        }

        public void onUnsetAliases(int i, List<SubscribeResult> list) {
        }

        public void onUnsetTags(int i, List<SubscribeResult> list) {
        }

        public void onUnsetUserAccounts(int i, List<SubscribeResult> list) {
        }

        public void onRegister(int i, String str) {
            if (i == 0) {
                ALog.i(OppoRegister.TAG, "onRegister regid=" + str, new Object[0]);
                OppoRegister.reportToken(OppoRegister.mContext, str);
                return;
            }
            ALog.e(OppoRegister.TAG, "onRegister code=" + i + ",regid=" + str, new Object[0]);
        }

        public void onUnRegister(int i) {
            Log.i(OppoRegister.TAG, "onUnRegister code=" + i);
        }
    };

    public static void register(Context context, String str, String str2) {
        try {
            mContext = context.getApplicationContext();
            if (!UtilityImpl.isMainProcess(mContext)) {
                ALog.i(TAG, "not in main process, return", new Object[0]);
            } else if (PushManager.isSupportPush(mContext)) {
                BaseNotifyClickActivity.addNotifyListener(new OppoMsgParseImpl());
                PushCallback pushCallback = mPushCallback;
                ALog.i(TAG, "register oppo begin ", new Object[0]);
                PushManager.getInstance().register(mContext, str, str2, pushCallback);
            } else {
                ALog.i(TAG, "not support oppo push", new Object[0]);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "register error", th, new Object[0]);
        }
    }

    public static void unregister() {
        ALog.i(TAG, MiPushClient.COMMAND_UNREGISTER, new Object[0]);
        PushManager.getInstance().unRegister();
    }

    public static void setPushCallback(PushCallback pushCallback) {
        ALog.i(TAG, "setPushCallback", new Object[0]);
        PushManager.getInstance().setPushCallback(pushCallback);
    }

    public static void pausePush() {
        ALog.w(TAG, "pausePush", new Object[0]);
        PushManager.getInstance().pausePush();
    }

    public static void resumePush() {
        ALog.w(TAG, "resumePush", new Object[0]);
        PushManager.getInstance().resumePush();
    }

    /* access modifiers changed from: private */
    public static void reportToken(Context context, String str) {
        if (!TextUtils.isEmpty(str) && context != null) {
            NotifManager notifManager = new NotifManager();
            notifManager.init(context.getApplicationContext());
            notifManager.reportThirdPushToken(str, OPPO_TOKEN, false);
        }
    }
}
