package com.taobao.agoo;

import android.content.Intent;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import org.android.agoo.common.AgooConstants;

public class DefaultOppoMsgParseImpl implements BaseNotifyClickActivity.INotifyListener {
    private static final String TAG = "DefaultOppoMsgParseImpl";

    public String getMsgSource() {
        return AgooConstants.MESSAGE_SYSTEM_SOURCE_OPPO;
    }

    public String parseMsgFromIntent(Intent intent) {
        String str;
        Throwable th;
        if (intent == null) {
            ALog.e(TAG, "parseMsgFromIntent null", new Object[0]);
            return null;
        }
        try {
            str = intent.getStringExtra(AgooConstants.MESSAGE_OPPO_PAYLOAD);
            try {
                ALog.i(TAG, "parseMsgFromIntent", "msg", str);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            str = null;
            ALog.e(TAG, "parseMsgFromIntent", th, new Object[0]);
            return str;
        }
        return str;
    }
}
