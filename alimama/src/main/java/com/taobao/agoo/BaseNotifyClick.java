package com.taobao.agoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.agoo.BaseNotifyClickActivity;
import java.util.Iterator;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.MsgDO;
import org.android.agoo.control.AgooFactory;
import org.android.agoo.control.NotifManager;
import org.android.agoo.message.MessageService;

public abstract class BaseNotifyClick {
    private static final String TAG = "accs.BaseNotifyClick";
    /* access modifiers changed from: private */
    public AgooFactory agooFactory;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public String msgSource;
    /* access modifiers changed from: private */
    public NotifManager notifyManager;

    public abstract void onMessage(Intent intent);

    public void onCreate(Context context2, Intent intent) {
        ALog.i(TAG, UmbrellaConstants.LIFECYCLE_CREATE, new Object[0]);
        this.context = context2;
        buildMessage(intent);
    }

    public void onNewIntent(Intent intent) {
        ALog.i(TAG, "onNewIntent", new Object[0]);
        buildMessage(intent);
    }

    private void buildMessage(final Intent intent) {
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                Intent intent = null;
                try {
                    if (intent != null) {
                        String access$000 = BaseNotifyClick.this.parseMsgByThirdPush(intent);
                        if (TextUtils.isEmpty(access$000) || TextUtils.isEmpty(BaseNotifyClick.this.msgSource)) {
                            ALog.e(BaseNotifyClick.TAG, "parseMsgFromNotifyListener null!!", "source", BaseNotifyClick.this.msgSource);
                        } else {
                            if (BaseNotifyClick.this.notifyManager == null) {
                                NotifManager unused = BaseNotifyClick.this.notifyManager = new NotifManager();
                            }
                            if (BaseNotifyClick.this.agooFactory == null) {
                                AgooFactory unused2 = BaseNotifyClick.this.agooFactory = new AgooFactory();
                                BaseNotifyClick.this.agooFactory.init(BaseNotifyClick.this.context, BaseNotifyClick.this.notifyManager, (MessageService) null);
                            }
                            Bundle msgReceiverPreHandler = BaseNotifyClick.this.agooFactory.msgReceiverPreHandler(access$000.getBytes("UTF-8"), BaseNotifyClick.this.msgSource, (TaoBaseService.ExtraInfo) null, false);
                            String string = msgReceiverPreHandler.getString("body");
                            ALog.i(BaseNotifyClick.TAG, "begin parse EncryptedMsg", new Object[0]);
                            String parseEncryptedMsg = AgooFactory.parseEncryptedMsg(string);
                            if (!TextUtils.isEmpty(parseEncryptedMsg)) {
                                msgReceiverPreHandler.putString("body", parseEncryptedMsg);
                            } else {
                                ALog.e(BaseNotifyClick.TAG, "parse EncryptedMsg fail, empty", new Object[0]);
                            }
                            Intent intent2 = new Intent();
                            try {
                                intent2.putExtras(msgReceiverPreHandler);
                                BaseNotifyClick.this.agooFactory.saveMsg(access$000.getBytes("UTF-8"), "2");
                                BaseNotifyClick.this.reportClickNotifyMsg(intent2);
                                intent = intent2;
                            } catch (Throwable th) {
                                th = th;
                                intent = intent2;
                                BaseNotifyClick.this.onMessage(intent);
                                throw th;
                            }
                        }
                    }
                    try {
                        BaseNotifyClick.this.onMessage(intent);
                    } catch (Throwable th2) {
                        ALog.e(BaseNotifyClick.TAG, "onMessage", th2, new Object[0]);
                    }
                } catch (Throwable th3) {
                    th = th3;
                    ALog.e(BaseNotifyClick.TAG, "buildMessage", th, new Object[0]);
                    BaseNotifyClick.this.onMessage(intent);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public String parseMsgByThirdPush(Intent intent) {
        String str;
        if (BaseNotifyClickActivity.notifyListeners != null && BaseNotifyClickActivity.notifyListeners.size() > 0) {
            Iterator<BaseNotifyClickActivity.INotifyListener> it = BaseNotifyClickActivity.notifyListeners.iterator();
            str = null;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BaseNotifyClickActivity.INotifyListener next = it.next();
                String parseMsgFromIntent = next.parseMsgFromIntent(intent);
                if (!TextUtils.isEmpty(parseMsgFromIntent)) {
                    this.msgSource = next.getMsgSource();
                    str = parseMsgFromIntent;
                    break;
                }
                str = parseMsgFromIntent;
            }
        } else {
            ALog.e(TAG, "no impl, try use default impl to parse intent!", new Object[0]);
            BaseNotifyClickActivity.INotifyListener defaultHuaweiMsgParseImpl = new DefaultHuaweiMsgParseImpl();
            String parseMsgFromIntent2 = defaultHuaweiMsgParseImpl.parseMsgFromIntent(intent);
            if (TextUtils.isEmpty(parseMsgFromIntent2)) {
                defaultHuaweiMsgParseImpl = new DefaultXiaomiMsgParseImpl();
                parseMsgFromIntent2 = defaultHuaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(parseMsgFromIntent2)) {
                defaultHuaweiMsgParseImpl = new DefaultOppoMsgParseImpl();
                parseMsgFromIntent2 = defaultHuaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(parseMsgFromIntent2)) {
                defaultHuaweiMsgParseImpl = new DefaultVivoMsgParseImpl();
                parseMsgFromIntent2 = defaultHuaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(parseMsgFromIntent2)) {
                defaultHuaweiMsgParseImpl = new DefaultMeizuMsgParseImpl();
                parseMsgFromIntent2 = defaultHuaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(str)) {
                AppMonitorAdapter.commitCount("accs", "error", "parse 3push error", 0.0d);
            } else {
                this.msgSource = defaultHuaweiMsgParseImpl.getMsgSource();
                AppMonitorAdapter.commitCount("accs", "error", "parse 3push default " + this.msgSource, 0.0d);
            }
        }
        ALog.i(TAG, "parseMsgByThirdPush", "result", str, "msgSource", this.msgSource);
        return str;
    }

    /* access modifiers changed from: private */
    public void reportClickNotifyMsg(Intent intent) {
        try {
            String stringExtra = intent.getStringExtra("id");
            String stringExtra2 = intent.getStringExtra(AgooConstants.MESSAGE_SOURCE);
            String stringExtra3 = intent.getStringExtra(AgooConstants.MESSAGE_REPORT);
            String stringExtra4 = intent.getStringExtra(AgooConstants.MESSAGE_EXT);
            MsgDO msgDO = new MsgDO();
            msgDO.msgIds = stringExtra;
            msgDO.extData = stringExtra4;
            msgDO.messageSource = stringExtra2;
            msgDO.reportStr = stringExtra3;
            msgDO.msgStatus = "8";
            ALog.i(TAG, "reportClickNotifyMsg messageId:" + stringExtra + " source:" + stringExtra2 + " reportStr:" + stringExtra3 + " status:" + msgDO.msgStatus, new Object[0]);
            this.notifyManager.report(msgDO, (TaoBaseService.ExtraInfo) null);
        } catch (Exception e) {
            ALog.e(TAG, "reportClickNotifyMsg exception: " + e, new Object[0]);
        }
    }
}
