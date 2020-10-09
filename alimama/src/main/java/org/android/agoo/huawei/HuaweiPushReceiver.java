package org.android.agoo.huawei;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hms.support.api.push.PushReceiver;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.control.AgooFactory;
import org.android.agoo.control.NotifManager;
import org.android.agoo.message.MessageService;

public class HuaweiPushReceiver extends PushReceiver {
    private static final String HUAWEI_TOKEN = "HW_TOKEN";
    private static final String TAG = "HuaweiPushReceiver";
    private AgooFactory agooFactory;

    public void onEvent(Context context, PushReceiver.Event event, Bundle bundle) {
    }

    public void onPushState(Context context, boolean z) {
    }

    public void onToken(Context context, String str, Bundle bundle) {
        try {
            if (!TextUtils.isEmpty(str)) {
                ALog.i(TAG, "onToken", "token", str);
                NotifManager notifManager = new NotifManager();
                notifManager.init(context.getApplicationContext());
                notifManager.reportThirdPushToken(str, HUAWEI_TOKEN);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "onToken", th, new Object[0]);
        }
    }

    public boolean onPushMsg(Context context, byte[] bArr, Bundle bundle) {
        try {
            if (HuaWeiRegister.isChannelRegister) {
                Intent intent = new Intent();
                intent.setAction("org.agoo.android.intent.action.PING_V4");
                intent.setClassName(AgooConstants.TAOBAO_PACKAGE, AdapterUtilityImpl.channelService);
                intent.putExtra("source", "huawei-bundle");
                context.startService(intent);
            }
            ALog.i(TAG, "onPushMsg", "content", new String(bArr, "UTF-8"));
            if (this.agooFactory == null) {
                this.agooFactory = new AgooFactory();
                this.agooFactory.init(context, (NotifManager) null, (MessageService) null);
            }
            this.agooFactory.msgRecevie(bArr, AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI, (TaoBaseService.ExtraInfo) null);
        } catch (Throwable th) {
            ALog.e(TAG, "onPushMsg", th, new Object[0]);
        }
        return true;
    }
}
