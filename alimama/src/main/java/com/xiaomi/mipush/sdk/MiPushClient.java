package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.text.TextUtils;
import com.coloros.mcssdk.mode.Message;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.data.Config;
import com.xiaomi.clientreport.manager.ClientReportClient;
import com.xiaomi.mipush.sdk.MiTinyDataClient;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ai;
import com.xiaomi.push.ay;
import com.xiaomi.push.dl;
import com.xiaomi.push.es;
import com.xiaomi.push.et;
import com.xiaomi.push.eu;
import com.xiaomi.push.fa;
import com.xiaomi.push.g;
import com.xiaomi.push.hg;
import com.xiaomi.push.hl;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.hu;
import com.xiaomi.push.i;
import com.xiaomi.push.ia;
import com.xiaomi.push.ig;
import com.xiaomi.push.ik;
import com.xiaomi.push.im;
import com.xiaomi.push.io;
import com.xiaomi.push.l;
import com.xiaomi.push.n;
import com.xiaomi.push.r;
import com.xiaomi.push.service.ag;
import com.xiaomi.push.service.aj;
import com.xiaomi.push.service.receivers.NetworkStatusReceiver;
import com.xiaomi.push.t;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public abstract class MiPushClient {
    public static final String COMMAND_REGISTER = "register";
    public static final String COMMAND_SET_ACCEPT_TIME = "accept-time";
    public static final String COMMAND_SET_ACCOUNT = "set-account";
    public static final String COMMAND_SET_ALIAS = "set-alias";
    public static final String COMMAND_SUBSCRIBE_TOPIC = "subscribe-topic";
    public static final String COMMAND_UNREGISTER = "unregister";
    public static final String COMMAND_UNSET_ACCOUNT = "unset-account";
    public static final String COMMAND_UNSET_ALIAS = "unset-alias";
    public static final String COMMAND_UNSUBSCRIBE_TOPIC = "unsubscibe-topic";
    public static final String PREF_EXTRA = "mipush_extra";
    private static boolean isCrashHandlerSuggested = false;
    /* access modifiers changed from: private */
    public static Context sContext;
    private static long sCurMsgId = System.currentTimeMillis();

    @Deprecated
    public static abstract class MiPushClientCallback {
        private String category;

        /* access modifiers changed from: protected */
        public String getCategory() {
            return this.category;
        }

        public void onCommandResult(String str, long j, String str2, List<String> list) {
        }

        public void onInitializeResult(long j, String str, String str2) {
        }

        public void onReceiveMessage(MiPushMessage miPushMessage) {
        }

        public void onReceiveMessage(String str, String str2, String str3, boolean z) {
        }

        public void onSubscribeResult(long j, String str, String str2) {
        }

        public void onUnsubscribeResult(long j, String str, String str2) {
        }

        /* access modifiers changed from: protected */
        public void setCategory(String str) {
            this.category = str;
        }
    }

    private static boolean acceptTimeSet(Context context, String str, String str2) {
        String acceptTime = getAcceptTime(context);
        return TextUtils.equals(acceptTime, str + "," + str2);
    }

    public static long accountSetTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        return sharedPreferences.getLong("account_" + str, -1);
    }

    static synchronized void addAcceptTime(Context context, String str, String str2) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putString(Constants.EXTRA_KEY_ACCEPT_TIME, str + "," + str2);
            r.a(edit);
        }
    }

    static synchronized void addAccount(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putLong("account_" + str, System.currentTimeMillis()).commit();
        }
    }

    static synchronized void addAlias(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putLong("alias_" + str, System.currentTimeMillis()).commit();
        }
    }

    private static void addPullNotificationTime(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.putLong("last_pull_notification", System.currentTimeMillis());
        r.a(edit);
    }

    private static void addRegRequestTime(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.putLong("last_reg_request", System.currentTimeMillis());
        r.a(edit);
    }

    static synchronized void addTopic(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putLong("topic_" + str, System.currentTimeMillis()).commit();
        }
    }

    public static long aliasSetTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        return sharedPreferences.getLong("alias_" + str, -1);
    }

    public static void awakeApps(Context context, String[] strArr) {
        ai.a(context).a((Runnable) new ah(strArr, context));
    }

    /* access modifiers changed from: private */
    public static void awakePushServiceByPackageInfo(Context context, PackageInfo packageInfo) {
        ServiceInfo[] serviceInfoArr = packageInfo.services;
        if (serviceInfoArr != null) {
            int length = serviceInfoArr.length;
            int i = 0;
            while (i < length) {
                ServiceInfo serviceInfo = serviceInfoArr[i];
                if (!serviceInfo.exported || !serviceInfo.enabled || !"com.xiaomi.mipush.sdk.PushMessageHandler".equals(serviceInfo.name) || context.getPackageName().equals(serviceInfo.packageName)) {
                    i++;
                } else {
                    try {
                        Thread.sleep(((long) ((Math.random() * 2.0d) + 1.0d)) * 1000);
                        Intent intent = new Intent();
                        intent.setClassName(serviceInfo.packageName, serviceInfo.name);
                        intent.setAction("com.xiaomi.mipush.sdk.WAKEUP");
                        intent.putExtra("waker_pkgname", context.getPackageName());
                        PushMessageHandler.a(context, intent);
                        return;
                    } catch (Throwable unused) {
                        return;
                    }
                }
            }
        }
    }

    private static void checkNotNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException("param " + str + " is not nullable");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003d, code lost:
        if (com.xiaomi.push.m.a(r5, "android.permission.WRITE_EXTERNAL_STORAGE") == false) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        if (android.text.TextUtils.isEmpty(r2) != false) goto L_0x0055;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean checkPermission(android.content.Context r5) {
        /*
            r0 = 1
            if (r5 == 0) goto L_0x0055
            boolean r1 = com.xiaomi.push.l.a()
            if (r1 != 0) goto L_0x0056
            java.lang.String r1 = "com.xiaomi.xmsf"
            java.lang.String r2 = r5.getPackageName()
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0016
            goto L_0x0056
        L_0x0016:
            java.lang.String r1 = com.xiaomi.push.i.b((android.content.Context) r5)
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x0021
            goto L_0x0056
        L_0x0021:
            android.content.pm.ApplicationInfo r1 = r5.getApplicationInfo()
            int r1 = r1.targetSdkVersion
            r2 = 23
            if (r1 < r2) goto L_0x0040
            int r1 = android.os.Build.VERSION.SDK_INT
            if (r1 < r2) goto L_0x0040
            java.lang.String r1 = "android.permission.READ_PHONE_STATE"
            boolean r1 = com.xiaomi.push.m.a(r5, r1)
            if (r1 != 0) goto L_0x0056
            java.lang.String r1 = "android.permission.WRITE_EXTERNAL_STORAGE"
            boolean r1 = com.xiaomi.push.m.a(r5, r1)
            if (r1 == 0) goto L_0x0055
            goto L_0x0056
        L_0x0040:
            java.lang.String r1 = com.xiaomi.push.i.f(r5)
            java.lang.String r2 = com.xiaomi.push.i.a()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0056
            boolean r1 = android.text.TextUtils.isEmpty(r2)
            if (r1 != 0) goto L_0x0055
            goto L_0x0056
        L_0x0055:
            r0 = 0
        L_0x0056:
            if (r0 != 0) goto L_0x00b1
            java.lang.String r1 = "Because of lack of necessary information, mi push can't be initialized"
            com.xiaomi.channel.commonutils.logger.b.d(r1)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = com.xiaomi.push.m.a(r5, r2)
            if (r2 != 0) goto L_0x006f
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            r1.add(r2)
        L_0x006f:
            java.lang.String r2 = "android.permission.WRITE_EXTERNAL_STORAGE"
            boolean r2 = com.xiaomi.push.m.a(r5, r2)
            if (r2 != 0) goto L_0x007c
            java.lang.String r2 = "android.permission.WRITE_EXTERNAL_STORAGE"
            r1.add(r2)
        L_0x007c:
            boolean r2 = r1.isEmpty()
            if (r2 != 0) goto L_0x00b1
            int r2 = r1.size()
            java.lang.String[] r2 = new java.lang.String[r2]
            r1.toArray(r2)
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            java.lang.String r3 = "com.xiaomi.mipush.ERROR"
            r1.setAction(r3)
            java.lang.String r3 = r5.getPackageName()
            r1.setPackage(r3)
            java.lang.String r3 = "message_type"
            r4 = 5
            r1.putExtra(r3, r4)
            java.lang.String r3 = "error_type"
            java.lang.String r4 = "error_lack_of_permission"
            r1.putExtra(r3, r4)
            java.lang.String r3 = "error_message"
            r1.putExtra(r3, r2)
            r5.sendBroadcast(r1)
        L_0x00b1:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.MiPushClient.checkPermission(android.content.Context):boolean");
    }

    protected static void clearExtras(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.clear();
        edit.commit();
    }

    public static void clearLocalNotificationType(Context context) {
        ay.a(context).e();
    }

    public static void clearNotification(Context context) {
        ay.a(context).a(-1);
    }

    public static void clearNotification(Context context, int i) {
        ay.a(context).a(i);
    }

    public static void clearNotification(Context context, String str, String str2) {
        ay.a(context).a(str, str2);
    }

    public static void disablePush(Context context) {
        ay.a(context).a(true);
    }

    public static void enablePush(Context context) {
        ay.a(context).a(false);
    }

    private static void forceHandleCrash() {
        boolean a = ag.a(sContext).a(hl.ForceHandleCrashSwitch.a(), false);
        if (!isCrashHandlerSuggested && a) {
            Thread.setDefaultUncaughtExceptionHandler(new x(sContext));
        }
    }

    protected static String getAcceptTime(Context context) {
        return context.getSharedPreferences("mipush_extra", 0).getString(Constants.EXTRA_KEY_ACCEPT_TIME, "00:00-23:59");
    }

    public static List<String> getAllAlias(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String next : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (next.startsWith("alias_")) {
                arrayList.add(next.substring("alias_".length()));
            }
        }
        return arrayList;
    }

    public static List<String> getAllTopic(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String next : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (next.startsWith("topic_") && !next.contains("**ALL**")) {
                arrayList.add(next.substring("topic_".length()));
            }
        }
        return arrayList;
    }

    public static List<String> getAllUserAccount(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String next : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (next.startsWith("account_")) {
                arrayList.add(next.substring("account_".length()));
            }
        }
        return arrayList;
    }

    public static String getAppRegion(Context context) {
        if (d.a(context).c()) {
            return d.a(context).f();
        }
        return null;
    }

    private static boolean getDefaultSwitch() {
        return l.b();
    }

    protected static boolean getOpenFCMPush(Context context) {
        checkNotNull(context, "context");
        return g.a(context).b(f.ASSEMBLE_PUSH_FCM);
    }

    protected static boolean getOpenHmsPush(Context context) {
        checkNotNull(context, "context");
        return g.a(context).b(f.ASSEMBLE_PUSH_HUAWEI);
    }

    protected static boolean getOpenOPPOPush(Context context) {
        checkNotNull(context, "context");
        return g.a(context).b(f.ASSEMBLE_PUSH_COS);
    }

    protected static boolean getOpenVIVOPush(Context context) {
        return g.a(context).b(f.ASSEMBLE_PUSH_FTOS);
    }

    public static String getRegId(Context context) {
        if (d.a(context).c()) {
            return d.a(context).c();
        }
        return null;
    }

    private static void initEventPerfLogic(Context context) {
        eu.a((eu.a) new ai());
        Config a = eu.a(context);
        ClientReportClient.init(context, a, new es(context), new et(context));
        a.a(context);
        t.a(context, a);
        ag.a(context).a((ag.a) new aj(100, "perf event job update", context));
    }

    @Deprecated
    public static void initialize(Context context, String str, String str2, MiPushClientCallback miPushClientCallback) {
        try {
            b.a("sdk_version = 3_6_19");
            if (miPushClientCallback != null) {
                PushMessageHandler.a(miPushClientCallback);
            }
            if (t.a(sContext)) {
                z.a(sContext);
            }
            if (d.a(sContext).a(str, str2) || checkPermission(sContext)) {
                boolean z = d.a(sContext).a() != Constants.a();
                if (z || shouldSendRegRequest(sContext)) {
                    if (z || !d.a(sContext).a(str, str2) || d.a(sContext).e()) {
                        String a = ay.a(6);
                        d.a(sContext).a();
                        d.a(sContext).a(Constants.a());
                        d.a(sContext).a(str, str2, a);
                        MiTinyDataClient.a.a().b(MiTinyDataClient.PENDING_REASON_APPID);
                        clearExtras(sContext);
                        ig igVar = new ig();
                        igVar.a(aj.a());
                        igVar.b(str);
                        igVar.e(str2);
                        igVar.d(sContext.getPackageName());
                        igVar.f(a);
                        igVar.c(g.a(sContext, sContext.getPackageName()));
                        igVar.b(g.a(sContext, sContext.getPackageName()));
                        igVar.g("3_6_19");
                        igVar.a(30619);
                        igVar.h(i.e(sContext));
                        igVar.a(hu.Init);
                        if (!l.d()) {
                            String g = i.g(sContext);
                            String i = i.i(sContext);
                            if (!TextUtils.isEmpty(g)) {
                                if (l.b()) {
                                    if (!TextUtils.isEmpty(i)) {
                                        g = g + "," + i;
                                    }
                                    igVar.i(g);
                                }
                                igVar.k(ay.a(g) + "," + i.j(sContext));
                            }
                        }
                        igVar.j(i.a());
                        int a2 = i.a();
                        if (a2 >= 0) {
                            igVar.c(a2);
                        }
                        ay.a(sContext).a(igVar, z);
                        b.a(sContext);
                        sContext.getSharedPreferences("mipush_extra", 4).getBoolean("mipush_registed", true);
                    } else {
                        if (1 == PushMessageHelper.getPushMode(sContext)) {
                            checkNotNull(miPushClientCallback, "callback");
                            miPushClientCallback.onInitializeResult(0, (String) null, d.a(sContext).c());
                        } else {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(d.a(sContext).c());
                            PushMessageHelper.sendCommandMessageBroadcast(sContext, PushMessageHelper.generateCommandMessage(fa.COMMAND_REGISTER.f328a, arrayList, 0, (String) null, (String) null));
                        }
                        ay.a(sContext).a();
                        if (d.a(sContext).a()) {
                            Cif ifVar = new Cif();
                            ifVar.b(d.a(sContext).a());
                            ifVar.c("client_info_update");
                            ifVar.a(aj.a());
                            ifVar.f625a = new HashMap();
                            ifVar.f625a.put("app_version", g.a(sContext, sContext.getPackageName()));
                            ifVar.f625a.put(Constants.EXTRA_KEY_APP_VERSION_CODE, Integer.toString(g.a(sContext, sContext.getPackageName())));
                            ifVar.f625a.put("push_sdk_vn", "3_6_19");
                            ifVar.f625a.put("push_sdk_vc", Integer.toString(30619));
                            String e = d.a(sContext).e();
                            if (!TextUtils.isEmpty(e)) {
                                ifVar.f625a.put("deviceid", e);
                            }
                            ay.a(sContext).a(ifVar, hg.Notification, false, (ht) null);
                            b.a(sContext);
                        }
                        if (!n.a(sContext, "update_devId", false)) {
                            updateIMEI();
                            n.a(sContext, "update_devId", true);
                        }
                        String d = i.d(sContext);
                        if (!TextUtils.isEmpty(d)) {
                            ia iaVar = new ia();
                            iaVar.a(aj.a());
                            iaVar.b(str);
                            iaVar.c(fa.COMMAND_CHK_VDEVID.f328a);
                            ArrayList arrayList2 = new ArrayList();
                            arrayList2.add(i.c(sContext));
                            arrayList2.add(d);
                            arrayList2.add(Build.MODEL != null ? Build.MODEL : "");
                            arrayList2.add(Build.BOARD != null ? Build.BOARD : "");
                            iaVar.a((List<String>) arrayList2);
                            ay.a(sContext).a(iaVar, hg.Command, false, (ht) null);
                        }
                        if (shouldUseMIUIPush(sContext) && shouldPullNotification(sContext)) {
                            Cif ifVar2 = new Cif();
                            ifVar2.b(d.a(sContext).a());
                            ifVar2.c(hq.PullOfflineMessage.f485a);
                            ifVar2.a(aj.a());
                            ifVar2.a(false);
                            ay.a(sContext).a(ifVar2, hg.Notification, false, (ht) null, false);
                            addPullNotificationTime(sContext);
                        }
                    }
                    addRegRequestTime(sContext);
                    scheduleOcVersionCheckJob();
                    scheduleDataCollectionJobs(sContext);
                    initEventPerfLogic(sContext);
                    be.a(sContext);
                    forceHandleCrash();
                    if (!sContext.getPackageName().equals("com.xiaomi.xmsf")) {
                        if (Logger.getUserLogger() != null) {
                            Logger.setLogger(sContext, Logger.getUserLogger());
                        }
                        b.a(2);
                    }
                    operateSyncAction(context);
                    return;
                }
                ay.a(sContext).a();
                b.a("Could not send  register message within 5s repeatly .");
            }
        } catch (Throwable th) {
            b.a(th);
        }
    }

    private static void operateSyncAction(Context context) {
        if ("syncing".equals(ao.a(sContext).a(bd.DISABLE_PUSH))) {
            disablePush(sContext);
        }
        if ("syncing".equals(ao.a(sContext).a(bd.ENABLE_PUSH))) {
            enablePush(sContext);
        }
        if ("syncing".equals(ao.a(sContext).a(bd.UPLOAD_HUAWEI_TOKEN))) {
            syncAssemblePushToken(sContext);
        }
        if ("syncing".equals(ao.a(sContext).a(bd.UPLOAD_FCM_TOKEN))) {
            syncAssembleFCMPushToken(sContext);
        }
        if ("syncing".equals(ao.a(sContext).a(bd.UPLOAD_COS_TOKEN))) {
            syncAssembleCOSPushToken(context);
        }
        if ("syncing".equals(ao.a(sContext).a(bd.UPLOAD_FTOS_TOKEN))) {
            syncAssembleFTOSPushToken(context);
        }
    }

    public static void pausePush(Context context, String str) {
        setAcceptTime(context, 0, 0, 0, 0, str);
    }

    static void reInitialize(Context context, hu huVar) {
        if (d.a(context).c()) {
            String a = ay.a(6);
            String a2 = d.a(context).a();
            String b = d.a(context).b();
            d.a(context).a();
            d.a(context).a(Constants.a());
            d.a(context).a(a2, b, a);
            ig igVar = new ig();
            igVar.a(aj.a());
            igVar.b(a2);
            igVar.e(b);
            igVar.f(a);
            igVar.d(context.getPackageName());
            igVar.c(g.a(context, context.getPackageName()));
            igVar.a(huVar);
            ay.a(context).a(igVar, false);
        }
    }

    public static void registerCrashHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        Thread.setDefaultUncaughtExceptionHandler(new x(sContext, uncaughtExceptionHandler));
        isCrashHandlerSuggested = true;
    }

    private static void registerNetworkReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addCategory("android.intent.category.DEFAULT");
            context.getApplicationContext().registerReceiver(new NetworkStatusReceiver((Object) null), intentFilter);
        } catch (Throwable th) {
            b.a(th);
        }
    }

    public static void registerPush(Context context, String str, String str2) {
        registerPush(context, str, str2, new PushConfiguration());
    }

    public static void registerPush(Context context, String str, String str2, PushConfiguration pushConfiguration) {
        checkNotNull(context, "context");
        checkNotNull(str, Message.APP_ID);
        checkNotNull(str2, "appToken");
        sContext = context.getApplicationContext();
        if (sContext == null) {
            sContext = context;
        }
        Context context2 = sContext;
        t.a(context2);
        if (!NetworkStatusReceiver.a()) {
            registerNetworkReceiver(sContext);
        }
        g.a(sContext).a(pushConfiguration);
        b.a();
        ai.a(context2).a((Runnable) new ae(str, str2));
    }

    static synchronized void removeAcceptTime(Context context) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove(Constants.EXTRA_KEY_ACCEPT_TIME);
            r.a(edit);
        }
    }

    static synchronized void removeAccount(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("account_" + str).commit();
        }
    }

    static synchronized void removeAlias(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("alias_" + str).commit();
        }
    }

    static synchronized void removeAllAccounts(Context context) {
        synchronized (MiPushClient.class) {
            for (String removeAccount : getAllUserAccount(context)) {
                removeAccount(context, removeAccount);
            }
        }
    }

    static synchronized void removeAllAliases(Context context) {
        synchronized (MiPushClient.class) {
            for (String removeAlias : getAllAlias(context)) {
                removeAlias(context, removeAlias);
            }
        }
    }

    static synchronized void removeAllTopics(Context context) {
        synchronized (MiPushClient.class) {
            for (String removeTopic : getAllTopic(context)) {
                removeTopic(context, removeTopic);
            }
        }
    }

    static synchronized void removeTopic(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("topic_" + str).commit();
        }
    }

    public static void reportAppRunInBackground(Context context, boolean z) {
        if (d.a(context).b()) {
            hq hqVar = z ? hq.APP_SLEEP : hq.APP_WAKEUP;
            Cif ifVar = new Cif();
            ifVar.b(d.a(context).a());
            ifVar.c(hqVar.f485a);
            ifVar.d(context.getPackageName());
            ifVar.a(aj.a());
            ifVar.a(false);
            ay.a(context).a(ifVar, hg.Notification, false, (ht) null, false);
        }
    }

    static void reportIgnoreRegMessageClicked(Context context, String str, ht htVar, String str2, String str3) {
        Cif ifVar = new Cif();
        if (TextUtils.isEmpty(str3)) {
            b.d("do not report clicked message");
            return;
        }
        ifVar.b(str3);
        ifVar.c("bar:click");
        ifVar.a(str);
        ifVar.a(false);
        ay.a(context).a(ifVar, hg.Notification, false, true, htVar, true, str2, str3);
    }

    public static void reportMessageClicked(Context context, MiPushMessage miPushMessage) {
        ht htVar = new ht();
        htVar.a(miPushMessage.getMessageId());
        htVar.b(miPushMessage.getTopic());
        htVar.d(miPushMessage.getDescription());
        htVar.c(miPushMessage.getTitle());
        htVar.c(miPushMessage.getNotifyId());
        htVar.a(miPushMessage.getNotifyType());
        htVar.b(miPushMessage.getPassThrough());
        htVar.a(miPushMessage.getExtra());
        reportMessageClicked(context, miPushMessage.getMessageId(), htVar, (String) null);
    }

    @Deprecated
    public static void reportMessageClicked(Context context, String str) {
        reportMessageClicked(context, str, (ht) null, (String) null);
    }

    static void reportMessageClicked(Context context, String str, ht htVar, String str2) {
        Cif ifVar = new Cif();
        if (TextUtils.isEmpty(str2)) {
            if (d.a(context).b()) {
                str2 = d.a(context).a();
            } else {
                b.d("do not report clicked message");
                return;
            }
        }
        ifVar.b(str2);
        ifVar.c("bar:click");
        ifVar.a(str);
        ifVar.a(false);
        ay.a(context).a(ifVar, hg.Notification, false, htVar);
    }

    public static void resumePush(Context context, String str) {
        setAcceptTime(context, 0, 0, 23, 59, str);
    }

    private static void scheduleDataCollectionJobs(Context context) {
        if (ag.a(sContext).a(hl.DataCollectionSwitch.a(), getDefaultSwitch())) {
            dl.a().a(new s(context));
            ai.a(sContext).a((Runnable) new af(), 10);
        }
    }

    private static void scheduleOcVersionCheckJob() {
        ai.a(sContext).a(new an(sContext), ag.a(sContext).a(hl.OcVersionCheckFrequency.a(), 86400), 5);
    }

    public static void setAcceptTime(Context context, int i, int i2, int i3, int i4, String str) {
        Context context2 = context;
        int i5 = i;
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        if (i5 < 0 || i5 >= 24 || i7 < 0 || i7 >= 24 || i6 < 0 || i6 >= 60 || i8 < 0 || i8 >= 60) {
            throw new IllegalArgumentException("the input parameter is not valid.");
        }
        long rawOffset = (long) (((TimeZone.getTimeZone("GMT+08").getRawOffset() - TimeZone.getDefault().getRawOffset()) / 1000) / 60);
        long j = ((((long) ((i5 * 60) + i6)) + rawOffset) + 1440) % 1440;
        long j2 = ((((long) ((i7 * 60) + i8)) + rawOffset) + 1440) % 1440;
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(j / 60), Long.valueOf(j % 60)}));
        arrayList.add(String.format("%1$02d:%2$02d", new Object[]{Long.valueOf(j2 / 60), Long.valueOf(j2 % 60)}));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(String.format("%1$02d:%2$02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
        arrayList2.add(String.format("%1$02d:%2$02d", new Object[]{Integer.valueOf(i3), Integer.valueOf(i4)}));
        if (!acceptTimeSet(context2, (String) arrayList.get(0), (String) arrayList.get(1))) {
            setCommand(context2, fa.COMMAND_SET_ACCEPT_TIME.f328a, (ArrayList<String>) arrayList, str);
        } else if (1 == PushMessageHelper.getPushMode(context)) {
            PushMessageHandler.a(context, str, fa.COMMAND_SET_ACCEPT_TIME.f328a, 0, (String) null, arrayList2);
        } else {
            PushMessageHelper.sendCommandMessageBroadcast(context2, PushMessageHelper.generateCommandMessage(fa.COMMAND_SET_ACCEPT_TIME.f328a, arrayList2, 0, (String) null, (String) null));
        }
    }

    public static void setAlias(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            setCommand(context, fa.COMMAND_SET_ALIAS.f328a, str, str2);
        }
    }

    protected static void setCommand(Context context, String str, String str2, String str3) {
        StringBuilder sb;
        String str4;
        fa faVar;
        Context context2 = context;
        String str5 = str;
        String str6 = str2;
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str6);
        }
        if (!fa.COMMAND_SET_ALIAS.f328a.equalsIgnoreCase(str5) || Math.abs(System.currentTimeMillis() - aliasSetTime(context, str6)) >= 3600000) {
            if (fa.COMMAND_UNSET_ALIAS.f328a.equalsIgnoreCase(str5) && aliasSetTime(context, str6) < 0) {
                sb = new StringBuilder();
                str4 = "Don't cancel alias for ";
            } else if (!fa.COMMAND_SET_ACCOUNT.f328a.equalsIgnoreCase(str5) || Math.abs(System.currentTimeMillis() - accountSetTime(context, str6)) >= 3600000) {
                if (!fa.COMMAND_UNSET_ACCOUNT.f328a.equalsIgnoreCase(str5) || accountSetTime(context, str6) >= 0) {
                    setCommand(context, str5, (ArrayList<String>) arrayList, str3);
                    return;
                } else {
                    sb = new StringBuilder();
                    str4 = "Don't cancel account for ";
                }
            } else if (1 != PushMessageHelper.getPushMode(context)) {
                faVar = fa.COMMAND_SET_ACCOUNT;
                PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper.generateCommandMessage(faVar.f328a, arrayList, 0, (String) null, str3));
                return;
            }
            sb.append(str4);
            sb.append(ay.a(arrayList.toString(), 3));
            sb.append(" is unseted");
            b.a(sb.toString());
            return;
        } else if (1 != PushMessageHelper.getPushMode(context)) {
            faVar = fa.COMMAND_SET_ALIAS;
            PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper.generateCommandMessage(faVar.f328a, arrayList, 0, (String) null, str3));
            return;
        }
        PushMessageHandler.a(context, str3, str, 0, (String) null, arrayList);
    }

    protected static void setCommand(Context context, String str, ArrayList<String> arrayList, String str2) {
        if (!TextUtils.isEmpty(d.a(context).a())) {
            ia iaVar = new ia();
            iaVar.a(aj.a());
            iaVar.b(d.a(context).a());
            iaVar.c(str);
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                iaVar.a(it.next());
            }
            iaVar.e(str2);
            iaVar.d(context.getPackageName());
            ay.a(context).a(iaVar, hg.Command, (ht) null);
        }
    }

    public static void setLocalNotificationType(Context context, int i) {
        ay.a(context).b(i & -1);
    }

    public static void setUserAccount(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            setCommand(context, fa.COMMAND_SET_ACCOUNT.f328a, str, str2);
        }
    }

    private static boolean shouldPullNotification(Context context) {
        return Math.abs(System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0).getLong("last_pull_notification", -1)) > 300000;
    }

    private static boolean shouldSendRegRequest(Context context) {
        return Math.abs(System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0).getLong("last_reg_request", -1)) > 5000;
    }

    public static boolean shouldUseMIUIPush(Context context) {
        return ay.a(context).a();
    }

    public static void subscribe(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(d.a(context).a()) && !TextUtils.isEmpty(str)) {
            if (Math.abs(System.currentTimeMillis() - topicSubscribedTime(context, str)) > 86400000) {
                ik ikVar = new ik();
                ikVar.a(aj.a());
                ikVar.b(d.a(context).a());
                ikVar.c(str);
                ikVar.d(context.getPackageName());
                ikVar.e(str2);
                ay.a(context).a(ikVar, hg.Subscription, (ht) null);
            } else if (1 == PushMessageHelper.getPushMode(context)) {
                PushMessageHandler.a(context, str2, 0, (String) null, str);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(str);
                PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper.generateCommandMessage(fa.COMMAND_SUBSCRIBE_TOPIC.f328a, arrayList, 0, (String) null, (String) null));
            }
        }
    }

    public static void syncAssembleCOSPushToken(Context context) {
        ay.a(context).a((String) null, bd.UPLOAD_COS_TOKEN, f.ASSEMBLE_PUSH_COS);
    }

    public static void syncAssembleFCMPushToken(Context context) {
        ay.a(context).a((String) null, bd.UPLOAD_FCM_TOKEN, f.ASSEMBLE_PUSH_FCM);
    }

    public static void syncAssembleFTOSPushToken(Context context) {
        ay.a(context).a((String) null, bd.UPLOAD_FTOS_TOKEN, f.ASSEMBLE_PUSH_FTOS);
    }

    public static void syncAssemblePushToken(Context context) {
        ay.a(context).a((String) null, bd.UPLOAD_HUAWEI_TOKEN, f.ASSEMBLE_PUSH_HUAWEI);
    }

    public static long topicSubscribedTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        return sharedPreferences.getLong("topic_" + str, -1);
    }

    public static void unregisterPush(Context context) {
        j.c(context);
        ag.a(context).a();
        if (d.a(context).b()) {
            im imVar = new im();
            imVar.a(aj.a());
            imVar.b(d.a(context).a());
            imVar.c(d.a(context).c());
            imVar.e(d.a(context).b());
            imVar.d(context.getPackageName());
            ay.a(context).a(imVar);
            PushMessageHandler.a();
            d.a(context).b();
            clearLocalNotificationType(context);
            clearNotification(context);
            clearExtras(context);
        }
    }

    public static void unsetAlias(Context context, String str, String str2) {
        setCommand(context, fa.COMMAND_UNSET_ALIAS.f328a, str, str2);
    }

    public static void unsetUserAccount(Context context, String str, String str2) {
        setCommand(context, fa.COMMAND_UNSET_ACCOUNT.f328a, str, str2);
    }

    public static void unsubscribe(Context context, String str, String str2) {
        if (d.a(context).b()) {
            if (topicSubscribedTime(context, str) < 0) {
                b.a("Don't cancel subscribe for " + str + " is unsubscribed");
                return;
            }
            io ioVar = new io();
            ioVar.a(aj.a());
            ioVar.b(d.a(context).a());
            ioVar.c(str);
            ioVar.d(context.getPackageName());
            ioVar.e(str2);
            ay.a(context).a(ioVar, hg.UnSubscription, (ht) null);
        }
    }

    private static void updateIMEI() {
        new Thread(new ag()).start();
    }
}
