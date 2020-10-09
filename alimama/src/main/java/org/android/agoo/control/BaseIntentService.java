package org.android.agoo.control;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.Utils;
import com.taobao.weex.el.parse.Operators;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.Config;
import org.android.agoo.intent.IntentUtil;
import org.android.agoo.message.MessageService;

public abstract class BaseIntentService extends Service {
    private static final String TAG = "BaseIntentService";
    private static final String msgStatus = "4";
    /* access modifiers changed from: private */
    public AgooFactory agooFactory;
    private Context mContext = null;
    /* access modifiers changed from: private */
    public MessageService messageService;
    private Messenger messenger = new Messenger(new Handler() {
        public void handleMessage(Message message) {
            if (message != null) {
                ALog.i(BaseIntentService.TAG, "handleMessage on receive msg", "msg", message.toString());
                final Intent intent = (Intent) message.getData().getParcelable("intent");
                if (intent != null) {
                    ALog.i(BaseIntentService.TAG, "handleMessage get intent success", "intent", intent.toString());
                    ThreadPoolExecutorFactory.execute(new Runnable() {
                        public void run() {
                            BaseIntentService.this.onHandleIntent(intent);
                        }
                    });
                }
            }
        }
    });
    /* access modifiers changed from: private */
    public NotifManager notifyManager;

    /* access modifiers changed from: protected */
    public abstract void onError(Context context, String str);

    /* access modifiers changed from: protected */
    public abstract void onMessage(Context context, Intent intent);

    /* access modifiers changed from: protected */
    public abstract void onRegistered(Context context, String str);

    /* access modifiers changed from: protected */
    public void onUserCommand(Context context, Intent intent) {
    }

    public IBinder onBind(Intent intent) {
        if (OrangeAdapter.isBindService() && Utils.isTarget26(this)) {
            getApplicationContext().bindService(new Intent(this, getClass()), new ServiceConnection() {
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                }

                public void onServiceDisconnected(ComponentName componentName) {
                }
            }, 1);
        }
        return this.messenger.getBinder();
    }

    public void onCreate() {
        super.onCreate();
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
                NotifManager unused = BaseIntentService.this.notifyManager = new NotifManager();
                BaseIntentService.this.notifyManager.init(BaseIntentService.this.getApplicationContext());
                MessageService unused2 = BaseIntentService.this.messageService = new MessageService();
                BaseIntentService.this.messageService.init(BaseIntentService.this.getApplicationContext());
                AgooFactory unused3 = BaseIntentService.this.agooFactory = new AgooFactory();
                BaseIntentService.this.agooFactory.init(BaseIntentService.this.getApplicationContext(), BaseIntentService.this.notifyManager, BaseIntentService.this.messageService);
            }
        });
    }

    public int onStartCommand(final Intent intent, int i, int i2) {
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                BaseIntentService.this.onHandleIntent(intent);
            }
        });
        return 2;
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        this.mContext = getApplicationContext();
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                String agooCommand = IntentUtil.getAgooCommand(this.mContext);
                String thirdPushCommand = IntentUtil.getThirdPushCommand(this.mContext);
                ALog.i(TAG, "onHandleIntent,action=" + action + ",agooCommand=" + agooCommand + ",mipushCommand=" + thirdPushCommand, new Object[0]);
                try {
                    if (TextUtils.equals(action, agooCommand)) {
                        String stringExtra = intent.getStringExtra("command");
                        ALog.d(TAG, "actionCommand --->[" + stringExtra + Operators.ARRAY_END_STR, new Object[0]);
                        if (TextUtils.equals(stringExtra, AgooConstants.AGOO_COMMAND_MESSAGE_READED) || TextUtils.equals(stringExtra, AgooConstants.AGOO_COMMAND_MESSAGE_DELETED)) {
                            onUserCommand(this.mContext, intent);
                        }
                    } else if (TextUtils.equals(action, thirdPushCommand)) {
                        String stringExtra2 = intent.getStringExtra("command");
                        String stringExtra3 = intent.getStringExtra(AgooConstants.THIRD_PUSH_ID);
                        if (TextUtils.equals(stringExtra2, AgooConstants.AGOO_COMMAND_MIPUSHID_REPORT)) {
                            this.notifyManager.reportThirdPushToken(stringExtra3, "MI_TOKEN", false);
                        } else if (TextUtils.equals(stringExtra2, AgooConstants.AGOO_COMMAND_HUAWEIPUSHID_REPORT)) {
                            ALog.d(TAG, "HW_TOKEN report begin..regid=" + stringExtra3, new Object[0]);
                            this.notifyManager.reportThirdPushToken(stringExtra3, "HW_TOKEN", false);
                        } else if (TextUtils.equals(stringExtra2, AgooConstants.AGOO_COMMAND_GCMIPUSHID_REPORT)) {
                            ALog.i(TAG, "GCM_TOKEN report begin..regid=" + stringExtra3, new Object[0]);
                            this.notifyManager.reportThirdPushToken(stringExtra3, "gcm", false);
                        }
                    } else if (action.equals(AgooConstants.INTENT_FROM_AGOO_MESSAGE)) {
                        handleRemoteMessage(this.mContext, intent);
                    } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                        handleRemovePackage(this.mContext, intent);
                    } else if (TextUtils.equals(action, AgooConstants.INTENT_FROM_AGOO_REPORT) || TextUtils.equals(action, "android.net.conn.CONNECTIVITY_CHANGE") || TextUtils.equals(action, "android.intent.action.BOOT_COMPLETED") || TextUtils.equals(action, "android.intent.action.PACKAGE_ADDED") || TextUtils.equals(action, "android.intent.action.PACKAGE_REPLACED") || TextUtils.equals(action, "android.intent.action.USER_PRESENT") || TextUtils.equals(action, "android.intent.action.ACTION_POWER_CONNECTED") || TextUtils.equals(action, "android.intent.action.ACTION_POWER_DISCONNECTED")) {
                        ALog.i(TAG, "is report cache msg,Config.isReportCacheMsg(mContext)=" + Config.isReportCacheMsg(this.mContext), new Object[0]);
                        if (Config.isReportCacheMsg(this.mContext) && AdapterUtilityImpl.isNetworkConnected(this.mContext)) {
                            Config.clearReportTimes(this.mContext);
                            this.agooFactory.reportCacheMsg();
                            this.messageService.deleteCacheMessage();
                        }
                        long currentTimeMillis = System.currentTimeMillis();
                        if (ALog.isPrintLog(ALog.Level.I)) {
                            ALog.i(TAG, "is clear all msg=" + Config.isClearTime(this.mContext, currentTimeMillis), new Object[0]);
                        }
                        if (Config.isClearTime(this.mContext, currentTimeMillis)) {
                            Config.setClearTimes(this.mContext, currentTimeMillis);
                            this.messageService.deleteCacheMessage();
                        }
                    }
                } catch (Throwable th) {
                    try {
                        if (ALog.isPrintLog(ALog.Level.E)) {
                            ALog.e(TAG, "onHandleIntent deal error", th, new Object[0]);
                        }
                    } catch (Throwable th2) {
                        AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
                        throw th2;
                    }
                }
                AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
            }
        }
    }

    private final void handleRemovePackage(Context context, Intent intent) {
        if (intent != null && context != null) {
            String str = null;
            Uri data = intent.getData();
            if (data != null) {
                str = data.getSchemeSpecificPart();
            }
            if (!TextUtils.isEmpty(str)) {
                boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(TAG, "handleRemovePackage---->[replacing:" + booleanExtra + "],uninstallPack=" + str, new Object[0]);
                }
                if (!booleanExtra) {
                    this.notifyManager.doUninstall(str, booleanExtra);
                }
            }
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:105:0x02d5 */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x033e A[Catch:{ Exception -> 0x0349 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a5 A[Catch:{ Throwable -> 0x01ba, Throwable -> 0x0358 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0117 A[Catch:{ Throwable -> 0x01ba, Throwable -> 0x0358 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x016e A[Catch:{ Throwable -> 0x01ba, Throwable -> 0x0358 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void handleRemoteMessage(android.content.Context r29, android.content.Intent r30) {
        /*
            r28 = this;
            r1 = r28
            r2 = r30
            java.lang.String r0 = "id"
            java.lang.String r5 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "body"
            java.lang.String r6 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "type"
            java.lang.String r7 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "message_source"
            java.lang.String r8 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "report"
            java.lang.String r9 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "encrypted"
            java.lang.String r10 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "extData"
            java.lang.String r11 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "oriData"
            java.lang.String r12 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "trace"
            r3 = -1
            long r3 = r2.getLongExtra(r0, r3)     // Catch:{ Throwable -> 0x0078 }
            java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ Throwable -> 0x0078 }
            long r3 = r0.longValue()     // Catch:{ Throwable -> 0x0078 }
            r15 = r29
            r1.getTrace(r15, r3)     // Catch:{ Throwable -> 0x0076 }
            java.lang.String r0 = "msg_agoo_bundle"
            android.os.Bundle r0 = r2.getBundleExtra(r0)     // Catch:{ Throwable -> 0x0076 }
            if (r0 == 0) goto L_0x005b
            java.lang.String r3 = "accs_extra"
            java.io.Serializable r0 = r0.getSerializable(r3)     // Catch:{ Throwable -> 0x0076 }
            com.taobao.accs.base.TaoBaseService$ExtraInfo r0 = (com.taobao.accs.base.TaoBaseService.ExtraInfo) r0     // Catch:{ Throwable -> 0x0076 }
            r3 = r0
            goto L_0x005c
        L_0x005b:
            r3 = 0
        L_0x005c:
            java.lang.String r0 = "source"
            java.lang.String r4 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0074 }
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x0072 }
            if (r0 == 0) goto L_0x006b
            java.lang.String r0 = "oldsdk"
            r4 = r0
        L_0x006b:
            java.lang.String r0 = "fromAppkey"
            java.lang.String r0 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0072 }
            goto L_0x009b
        L_0x0072:
            r0 = move-exception
            goto L_0x007d
        L_0x0074:
            r0 = move-exception
            goto L_0x007c
        L_0x0076:
            r0 = move-exception
            goto L_0x007b
        L_0x0078:
            r0 = move-exception
            r15 = r29
        L_0x007b:
            r3 = 0
        L_0x007c:
            r4 = 0
        L_0x007d:
            java.lang.String r13 = "BaseIntentService"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r14.<init>()     // Catch:{ Throwable -> 0x0358 }
            r18 = r3
            java.lang.String r3 = "_trace,t="
            r14.append(r3)     // Catch:{ Throwable -> 0x0358 }
            r14.append(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = r14.toString()     // Catch:{ Throwable -> 0x0358 }
            r3 = 0
            java.lang.Object[] r14 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.e(r13, r0, r14)     // Catch:{ Throwable -> 0x0358 }
            r3 = r18
            r0 = 0
        L_0x009b:
            com.taobao.accs.utl.ALog$Level r13 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x0358 }
            boolean r13 = com.taobao.accs.utl.ALog.isPrintLog(r13)     // Catch:{ Throwable -> 0x0358 }
            r16 = 1
            if (r13 == 0) goto L_0x00ee
            java.lang.String r13 = "BaseIntentService"
            java.lang.String r14 = "handleRemoteMessage"
            r15 = 12
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r18 = "message"
            r17 = 0
            r15[r17] = r18     // Catch:{ Throwable -> 0x0358 }
            r15[r16] = r6     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r18 = "source"
            r20 = 2
            r15[r20] = r18     // Catch:{ Throwable -> 0x0358 }
            r18 = 3
            r15[r18] = r8     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r18 = "msgId"
            r19 = 4
            r15[r19] = r18     // Catch:{ Throwable -> 0x0358 }
            r18 = 5
            r15[r18] = r5     // Catch:{ Throwable -> 0x0358 }
            r18 = 6
            java.lang.String r21 = "utdid"
            r15[r18] = r21     // Catch:{ Throwable -> 0x0358 }
            r18 = 7
            java.lang.String r21 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r29)     // Catch:{ Throwable -> 0x0358 }
            r15[r18] = r21     // Catch:{ Throwable -> 0x0358 }
            r18 = 8
            java.lang.String r21 = "fromPkg"
            r15[r18] = r21     // Catch:{ Throwable -> 0x0358 }
            r18 = 9
            r15[r18] = r4     // Catch:{ Throwable -> 0x0358 }
            r18 = 10
            java.lang.String r21 = "fromAppkey"
            r15[r18] = r21     // Catch:{ Throwable -> 0x0358 }
            r18 = 11
            r15[r18] = r0     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.i(r13, r14, r15)     // Catch:{ Throwable -> 0x0358 }
        L_0x00ee:
            org.android.agoo.common.MsgDO r13 = new org.android.agoo.common.MsgDO     // Catch:{ Throwable -> 0x0358 }
            r13.<init>()     // Catch:{ Throwable -> 0x0358 }
            r13.msgIds = r5     // Catch:{ Throwable -> 0x0358 }
            r13.extData = r11     // Catch:{ Throwable -> 0x0358 }
            r13.messageSource = r8     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r11 = "4"
            r13.msgStatus = r11     // Catch:{ Throwable -> 0x0358 }
            r13.reportStr = r9     // Catch:{ Throwable -> 0x0358 }
            r13.fromPkg = r4     // Catch:{ Throwable -> 0x0358 }
            r13.fromAppkey = r0     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = com.taobao.accs.client.AdapterGlobalClientInfo.isFirstStartProc()     // Catch:{ Throwable -> 0x0358 }
            r13.isStartProc = r0     // Catch:{ Throwable -> 0x0358 }
            android.content.Context r0 = r1.mContext     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = com.taobao.accs.utl.AdapterUtilityImpl.isNotificationEnabled(r0)     // Catch:{ Throwable -> 0x0358 }
            r13.notifyEnable = r0     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0358 }
            if (r0 != 0) goto L_0x0154
            r0 = 4
            java.lang.String r0 = java.lang.Integer.toString(r0)     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = r0.equals(r10)     // Catch:{ Throwable -> 0x0358 }
            if (r0 == 0) goto L_0x0140
            java.lang.String r0 = "BaseIntentService"
            java.lang.String r4 = "message is encrypted, attemp to decrypt msg"
            r9 = 0
            java.lang.Object[] r10 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.i(r0, r4, r10)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r6 = org.android.agoo.control.AgooFactory.parseEncryptedMsg(r6)     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0358 }
            if (r0 == 0) goto L_0x0154
            java.lang.String r0 = "22"
            r13.errorCode = r0     // Catch:{ Throwable -> 0x0358 }
            org.android.agoo.control.NotifManager r0 = r1.notifyManager     // Catch:{ Throwable -> 0x0358 }
            r0.handlerACKMessage(r13, r3)     // Catch:{ Throwable -> 0x0358 }
            return
        L_0x0140:
            java.lang.String r0 = "BaseIntentService"
            java.lang.String r2 = "msg encrypted flag not exist~~"
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.e(r0, r2, r4)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "24"
            r13.errorCode = r0     // Catch:{ Throwable -> 0x0153 }
            org.android.agoo.control.NotifManager r0 = r1.notifyManager     // Catch:{ Throwable -> 0x0153 }
            r0.report(r13, r3)     // Catch:{ Throwable -> 0x0153 }
        L_0x0153:
            return
        L_0x0154:
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0358 }
            if (r0 == 0) goto L_0x016e
            java.lang.String r0 = "21"
            r13.errorCode = r0     // Catch:{ Throwable -> 0x0163 }
            org.android.agoo.control.NotifManager r0 = r1.notifyManager     // Catch:{ Throwable -> 0x0163 }
            r0.report(r13, r3)     // Catch:{ Throwable -> 0x0163 }
        L_0x0163:
            java.lang.String r0 = "BaseIntentService"
            java.lang.String r2 = "handleMessage--->[null]"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.e(r0, r2, r3)     // Catch:{ Throwable -> 0x0358 }
            return
        L_0x016e:
            java.lang.String r0 = "body"
            r2.putExtra(r0, r6)     // Catch:{ Throwable -> 0x0358 }
            org.android.agoo.control.NotifManager r0 = r1.notifyManager     // Catch:{ Throwable -> 0x01ba }
            r0.report(r13, r3)     // Catch:{ Throwable -> 0x01ba }
            org.android.agoo.message.MessageService r0 = r1.messageService     // Catch:{ Throwable -> 0x01ba }
            java.lang.String r3 = "0"
            r0.addAccsMessage(r5, r12, r3)     // Catch:{ Throwable -> 0x01ba }
            com.taobao.accs.utl.UTMini r21 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x01ba }
            r22 = 19999(0x4e1f, float:2.8025E-41)
            java.lang.String r23 = "Page_Push"
            java.lang.String r24 = "agoo_arrive_id"
            r25 = 0
            r26 = 0
            r3 = 2
            java.lang.String[] r0 = new java.lang.String[r3]     // Catch:{ Throwable -> 0x01ba }
            r3 = 0
            r4 = 0
            r0[r3] = r4     // Catch:{ Throwable -> 0x01ba }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01ba }
            r3.<init>()     // Catch:{ Throwable -> 0x01ba }
            java.lang.String r4 = "messageId="
            r3.append(r4)     // Catch:{ Throwable -> 0x01ba }
            java.lang.String r4 = r13.msgIds     // Catch:{ Throwable -> 0x01ba }
            r3.append(r4)     // Catch:{ Throwable -> 0x01ba }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x01ba }
            r0[r16] = r3     // Catch:{ Throwable -> 0x01ba }
            r27 = r0
            r21.commitEvent((int) r22, (java.lang.String) r23, (java.lang.Object) r24, (java.lang.Object) r25, (java.lang.Object) r26, (java.lang.String[]) r27)     // Catch:{ Throwable -> 0x01ba }
            java.lang.String r0 = "accs"
            java.lang.String r3 = "agoo_arrive"
            java.lang.String r4 = "arrive"
            r9 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r3, r4, r9)     // Catch:{ Throwable -> 0x01ba }
            goto L_0x01d8
        L_0x01ba:
            r0 = move-exception
            java.lang.String r9 = "BaseIntentService"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r10.<init>()     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r11 = "report message Throwable--->t="
            r10.append(r11)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0358 }
            r10.append(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = r10.toString()     // Catch:{ Throwable -> 0x0358 }
            r10 = 0
            java.lang.Object[] r11 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.e(r9, r0, r11)     // Catch:{ Throwable -> 0x0358 }
        L_0x01d8:
            org.android.agoo.message.MessageService r0 = r1.messageService     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = r0.hasMessageDuplicate(r5)     // Catch:{ Throwable -> 0x0358 }
            if (r0 == 0) goto L_0x0219
            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)     // Catch:{ Throwable -> 0x0358 }
            if (r0 == 0) goto L_0x020d
            java.lang.String r0 = "BaseIntentService"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r2.<init>()     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r6 = "handleRemoteMessage hasMessageDuplicate,messageId="
            r2.append(r6)     // Catch:{ Throwable -> 0x0358 }
            r2.append(r5)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r5 = ",utdid="
            r2.append(r5)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r5 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r29)     // Catch:{ Throwable -> 0x0358 }
            r2.append(r5)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0358 }
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.i(r0, r2, r5)     // Catch:{ Throwable -> 0x0358 }
        L_0x020d:
            java.lang.String r0 = "accs"
            java.lang.String r2 = "agoo_arrive"
            java.lang.String r5 = "arrive_dup"
            r3 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r2, r5, r3)     // Catch:{ Throwable -> 0x0358 }
            return
        L_0x0219:
            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x0358 }
            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)     // Catch:{ Throwable -> 0x0358 }
            if (r0 == 0) goto L_0x0247
            java.lang.String r0 = "BaseIntentService"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r9.<init>()     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r10 = "handleMessage--->["
            r9.append(r10)     // Catch:{ Throwable -> 0x0358 }
            r9.append(r6)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r10 = "],["
            r9.append(r10)     // Catch:{ Throwable -> 0x0358 }
            r9.append(r8)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r8 = "]"
            r9.append(r8)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r8 = r9.toString()     // Catch:{ Throwable -> 0x0358 }
            r9 = 0
            java.lang.Object[] r10 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.i(r0, r8, r10)     // Catch:{ Throwable -> 0x0358 }
        L_0x0247:
            java.lang.String r0 = "duplicate"
            java.lang.String r0 = r2.getStringExtra(r0)     // Catch:{ Throwable -> 0x0273 }
            boolean r8 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0273 }
            if (r8 != 0) goto L_0x0299
            java.lang.String r8 = "1"
            boolean r0 = android.text.TextUtils.equals(r0, r8)     // Catch:{ Throwable -> 0x0273 }
            if (r0 == 0) goto L_0x0299
            int r0 = r6.hashCode()     // Catch:{ Throwable -> 0x0273 }
            org.android.agoo.message.MessageService r8 = r1.messageService     // Catch:{ Throwable -> 0x0273 }
            boolean r0 = r8.hasMessageDuplicate(r5, r0)     // Catch:{ Throwable -> 0x0273 }
            if (r0 == 0) goto L_0x0299
            java.lang.String r0 = "accs"
            java.lang.String r8 = "agoo_arrive"
            java.lang.String r9 = "arrive_dupbody"
            r3 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r8, r9, r3)     // Catch:{ Throwable -> 0x0273 }
            return
        L_0x0273:
            r0 = move-exception
            com.taobao.accs.utl.ALog$Level r8 = com.taobao.accs.utl.ALog.Level.E     // Catch:{ Throwable -> 0x0358 }
            boolean r8 = com.taobao.accs.utl.ALog.isPrintLog(r8)     // Catch:{ Throwable -> 0x0358 }
            if (r8 == 0) goto L_0x0299
            java.lang.String r8 = "BaseIntentService"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r9.<init>()     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r10 = "hasMessageDuplicate message,e="
            r9.append(r10)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0358 }
            r9.append(r0)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = r9.toString()     // Catch:{ Throwable -> 0x0358 }
            r9 = 0
            java.lang.Object[] r10 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.e(r8, r0, r10)     // Catch:{ Throwable -> 0x0358 }
        L_0x0299:
            r0 = -1
            java.lang.String r8 = "notify"
            java.lang.String r8 = r2.getStringExtra(r8)     // Catch:{ Throwable -> 0x02a5 }
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ Throwable -> 0x02a5 }
            r0 = r8
        L_0x02a5:
            java.lang.String r8 = ""
            java.lang.String r9 = "has_test"
            java.lang.String r9 = r2.getStringExtra(r9)     // Catch:{ Throwable -> 0x02d5 }
            boolean r10 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x02d5 }
            if (r10 != 0) goto L_0x02cc
            java.lang.String r10 = "1"
            boolean r9 = android.text.TextUtils.equals(r9, r10)     // Catch:{ Throwable -> 0x02d5 }
            if (r9 == 0) goto L_0x02cc
            org.android.agoo.message.MessageService r9 = r1.messageService     // Catch:{ Throwable -> 0x02d5 }
            r9.addMessage(r5, r6, r7, r0)     // Catch:{ Throwable -> 0x02d5 }
            java.lang.String r9 = "accs"
            java.lang.String r10 = "agoo_arrive"
            java.lang.String r11 = "arrive_test"
            r3 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r9, r10, r11, r3)     // Catch:{ Throwable -> 0x02d5 }
            return
        L_0x02cc:
            java.lang.Class r9 = r28.getClass()     // Catch:{ Throwable -> 0x02d5 }
            java.lang.String r9 = r9.getName()     // Catch:{ Throwable -> 0x02d5 }
            r8 = r9
        L_0x02d5:
            org.android.agoo.message.MessageService r9 = r1.messageService     // Catch:{ Throwable -> 0x0358 }
            r9.addMessage(r5, r6, r7, r0)     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.UTMini r21 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ Throwable -> 0x0358 }
            r22 = 19999(0x4e1f, float:2.8025E-41)
            java.lang.String r23 = "Page_Push"
            java.lang.String r24 = "agoo_arrive_real_id"
            r25 = 0
            r26 = 0
            r5 = 2
            java.lang.String[] r0 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x0358 }
            r5 = 0
            r6 = 0
            r0[r5] = r6     // Catch:{ Throwable -> 0x0358 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r5.<init>()     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r6 = "messageId="
            r5.append(r6)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r6 = r13.msgIds     // Catch:{ Throwable -> 0x0358 }
            r5.append(r6)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0358 }
            r0[r16] = r5     // Catch:{ Throwable -> 0x0358 }
            r27 = r0
            r21.commitEvent((int) r22, (java.lang.String) r23, (java.lang.Object) r24, (java.lang.Object) r25, (java.lang.Object) r26, (java.lang.String[]) r27)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r0 = "accs"
            java.lang.String r5 = "agoo_arrive"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0358 }
            r6.<init>()     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r7 = "arrive_real_"
            r6.append(r7)     // Catch:{ Throwable -> 0x0358 }
            r6.append(r8)     // Catch:{ Throwable -> 0x0358 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0358 }
            r3 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r0, r5, r6, r3)     // Catch:{ Throwable -> 0x0358 }
            android.os.Bundle r0 = r30.getExtras()     // Catch:{ Exception -> 0x0349 }
            java.lang.Class<com.taobao.accs.ut.monitor.NetPerformanceMonitor> r5 = com.taobao.accs.ut.monitor.NetPerformanceMonitor.class
            java.lang.ClassLoader r5 = r5.getClassLoader()     // Catch:{ Exception -> 0x0349 }
            r0.setClassLoader(r5)     // Catch:{ Exception -> 0x0349 }
            android.os.Bundle r0 = r30.getExtras()     // Catch:{ Exception -> 0x0349 }
            java.lang.String r5 = "monitor"
            java.io.Serializable r0 = r0.getSerializable(r5)     // Catch:{ Exception -> 0x0349 }
            com.taobao.accs.ut.monitor.NetPerformanceMonitor r0 = (com.taobao.accs.ut.monitor.NetPerformanceMonitor) r0     // Catch:{ Exception -> 0x0349 }
            if (r0 == 0) goto L_0x0354
            r0.onToAgooTime()     // Catch:{ Exception -> 0x0349 }
            anet.channel.appmonitor.IAppMonitor r5 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ Exception -> 0x0349 }
            r5.commitStat(r0)     // Catch:{ Exception -> 0x0349 }
            goto L_0x0354
        L_0x0349:
            r0 = move-exception
            java.lang.String r5 = "BaseIntentService"
            java.lang.String r6 = "get NetPerformanceMonitor Error:"
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x0358 }
            com.taobao.accs.utl.ALog.e(r5, r6, r0, r7)     // Catch:{ Throwable -> 0x0358 }
        L_0x0354:
            r28.onMessage(r29, r30)     // Catch:{ Throwable -> 0x0358 }
            goto L_0x0377
        L_0x0358:
            r0 = move-exception
            java.lang.String r2 = "accs"
            java.lang.String r5 = "agoo_arrive"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "arrive_exception"
            r6.append(r7)
            java.lang.String r0 = r0.toString()
            r6.append(r0)
            java.lang.String r0 = r6.toString()
            r3 = 0
            com.taobao.accs.utl.AppMonitorAdapter.commitCount(r2, r5, r0, r3)
        L_0x0377:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.control.BaseIntentService.handleRemoteMessage(android.content.Context, android.content.Intent):void");
    }

    private final String getTrace(Context context, long j) {
        String str = null;
        String str2 = TextUtils.isEmpty((CharSequence) null) ? "unknow" : null;
        if (TextUtils.isEmpty((CharSequence) null)) {
            str = "unknow";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("appkey");
        stringBuffer.append("|");
        stringBuffer.append(j);
        stringBuffer.append("|");
        stringBuffer.append(System.currentTimeMillis());
        stringBuffer.append("|");
        stringBuffer.append(str2);
        stringBuffer.append("|");
        stringBuffer.append(str);
        return stringBuffer.toString();
    }

    public static final void runIntentInService(Context context, Intent intent, String str) {
        try {
            intent.setClassName(context, str);
            context.startService(intent);
        } catch (Throwable th) {
            ALog.w(TAG, "runIntentInService", th, new Object[0]);
        }
    }
}
