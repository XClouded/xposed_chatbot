package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.RemoteViews;
import com.xiaomi.push.at;
import com.xiaomi.push.g;
import com.xiaomi.push.hg;
import com.xiaomi.push.ht;
import com.xiaomi.push.ic;
import com.xiaomi.push.l;
import com.xiaomi.push.service.af;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

public class z {
    public static long a;

    /* renamed from: a  reason: collision with other field name */
    private static final LinkedList<Pair<Integer, ic>> f933a = new LinkedList<>();

    /* renamed from: a  reason: collision with other field name */
    private static ExecutorService f934a = Executors.newCachedThreadPool();

    private static class a implements Callable<Bitmap> {
        private Context a;

        /* renamed from: a  reason: collision with other field name */
        private String f935a;

        /* renamed from: a  reason: collision with other field name */
        private boolean f936a;

        public a(String str, Context context, boolean z) {
            this.a = context;
            this.f935a = str;
            this.f936a = z;
        }

        /* renamed from: a */
        public Bitmap call() {
            String str;
            Bitmap bitmap = null;
            if (!TextUtils.isEmpty(this.f935a)) {
                if (this.f935a.startsWith("http")) {
                    af.b a2 = af.a(this.a, this.f935a, this.f936a);
                    if (a2 != null) {
                        return a2.f841a;
                    }
                } else {
                    bitmap = af.a(this.a, this.f935a);
                    if (bitmap != null) {
                        return bitmap;
                    }
                }
                str = "Failed get online picture/icon resource";
            } else {
                str = "Failed get online picture/icon resource cause picUrl is empty";
            }
            com.xiaomi.channel.commonutils.logger.b.a(str);
            return bitmap;
        }
    }

    public static class b {
        long a = 0;

        /* renamed from: a  reason: collision with other field name */
        Notification f937a;
    }

    public static class c {
        public long a = 0;

        /* renamed from: a  reason: collision with other field name */
        public String f938a;
    }

    public static class d {
        public HashMap<String, Integer> a = new HashMap<>();

        /* renamed from: a  reason: collision with other field name */
        public boolean f939a = false;
        public HashMap<String, HashSet<Integer>> b = new HashMap<>();
    }

    static int a(Context context, String str) {
        return context.getSharedPreferences("pref_notify_type", 0).getInt(str, Integer.MAX_VALUE);
    }

    private static int a(Context context, String str, String str2) {
        if (str.equals(context.getPackageName())) {
            return context.getResources().getIdentifier(str2, "drawable", str);
        }
        return 0;
    }

    private static int a(String str, String str2) {
        return (((str + str2).hashCode() / 10) * 10) + 32768;
    }

    private static int a(Map<String, String> map) {
        String str = map == null ? null : map.get("timeout");
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return 0;
        }
    }

    @TargetApi(16)
    private static Notification.Builder a(Notification.Builder builder, Context context, String str, Map<String, String> map) {
        PendingIntent a2 = a(context, str, 1, map);
        if (a2 != null && !TextUtils.isEmpty(map.get("notification_style_button_left_name"))) {
            builder.addAction(0, map.get("notification_style_button_left_name"), a2);
        }
        PendingIntent a3 = a(context, str, 2, map);
        if (a3 != null && !TextUtils.isEmpty(map.get("notification_style_button_mid_name"))) {
            builder.addAction(0, map.get("notification_style_button_mid_name"), a3);
        }
        PendingIntent a4 = a(context, str, 3, map);
        if (a4 != null && !TextUtils.isEmpty(map.get("notification_style_button_right_name"))) {
            builder.addAction(0, map.get("notification_style_button_right_name"), a4);
        }
        return builder;
    }

    @TargetApi(16)
    private static Notification.Builder a(Context context, Map<String, String> map, Notification.Builder builder, String str) {
        if ("2".equals(map.get("notification_style_type"))) {
            Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
            Bitmap a2 = map == null ? null : a(context, map.get("notification_bigPic_uri"), false);
            if (a2 == null) {
                return builder;
            }
            bigPictureStyle.bigPicture(a2);
            bigPictureStyle.setSummaryText(str);
            bigPictureStyle.bigLargeIcon((Bitmap) null);
            builder.setStyle(bigPictureStyle);
        } else if ("1".equals(map.get("notification_style_type"))) {
            builder.setStyle(new Notification.BigTextStyle().bigText(str));
        }
        return builder;
    }

    private static Notification a(Notification notification) {
        Object a2 = at.a((Object) notification, "extraNotification");
        if (a2 != null) {
            at.a(a2, "setCustomizedIcon", true);
        }
        return notification;
    }

    private static Notification a(Notification notification, String str) {
        try {
            Field declaredField = Notification.class.getDeclaredField("extraNotification");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(notification);
            Method declaredMethod = obj.getClass().getDeclaredMethod("setTargetPkg", new Class[]{CharSequence.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj, new Object[]{str});
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
        }
        return notification;
    }

    private static PendingIntent a(Context context, ic icVar, ht htVar, byte[] bArr) {
        Intent intent;
        ComponentName componentName;
        int i = c(icVar) ? 1000 : a(icVar) ? 3000 : -1;
        String str = "";
        if (htVar != null) {
            str = htVar.a();
        }
        if (htVar == null || TextUtils.isEmpty(htVar.f529e)) {
            if (a(icVar)) {
                intent = new Intent();
                componentName = new ComponentName("com.xiaomi.xmsf", "com.xiaomi.mipush.sdk.PushMessageHandler");
            } else {
                intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
                componentName = new ComponentName(icVar.f613b, "com.xiaomi.mipush.sdk.PushMessageHandler");
            }
            intent.setComponent(componentName);
            intent.putExtra("mipush_payload", bArr);
            intent.putExtra("mipush_notified", true);
            intent.addCategory(String.valueOf(htVar.c()));
            intent.putExtra("messageId", str);
            intent.putExtra("eventMessageType", i);
            if (!f.b(context, icVar.f613b, "com.xiaomi.mipush.MESSAGE_CLICKED")) {
                return PendingIntent.getService(context, 0, intent, 134217728);
            }
            Intent intent2 = new Intent();
            intent2.setAction("com.xiaomi.mipush.MESSAGE_CLICKED");
            intent2.setClassName(icVar.f613b, "com.xiaomi.mipush.sdk.BridgeActivity");
            intent2.addFlags(276824064);
            intent2.putExtra("mipush_serviceIntent", intent);
            intent2.addCategory(String.valueOf(htVar.c()));
            return PendingIntent.getActivity(context, 0, intent2, 134217728);
        }
        Intent intent3 = new Intent("android.intent.action.VIEW");
        intent3.setData(Uri.parse(htVar.f529e));
        intent3.addFlags(268435456);
        intent3.putExtra("messageId", str);
        intent3.putExtra("eventMessageType", i);
        return PendingIntent.getActivity(context, 0, intent3, 134217728);
    }

    private static PendingIntent a(Context context, String str, int i, Map<String, String> map) {
        Intent a2;
        if (map == null || (a2 = a(context, str, i, map)) == null) {
            return null;
        }
        return PendingIntent.getActivity(context, 0, a2, 0);
    }

    private static Intent a(Context context, Intent intent) {
        try {
            if (context.getPackageManager().getPackageInfo("com.android.browser", 4) != null) {
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            }
            return intent;
        } catch (PackageManager.NameNotFoundException e) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
            return intent;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:74:0x0145  */
    /* renamed from: a  reason: collision with other method in class */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.content.Intent m618a(android.content.Context r5, java.lang.String r6, int r7, java.util.Map<java.lang.String, java.lang.String> r8) {
        /*
            r0 = 3
            r1 = 2
            if (r7 >= r1) goto L_0x0007
            java.lang.String r2 = "notification_style_button_left_notify_effect"
            goto L_0x000e
        L_0x0007:
            if (r7 >= r0) goto L_0x000c
            java.lang.String r2 = "notification_style_button_mid_notify_effect"
            goto L_0x000e
        L_0x000c:
            java.lang.String r2 = "notification_style_button_right_notify_effect"
        L_0x000e:
            java.lang.Object r2 = r8.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            r4 = 0
            if (r3 == 0) goto L_0x001c
            return r4
        L_0x001c:
            java.lang.String r3 = com.xiaomi.push.service.ap.a
            boolean r3 = r3.equals(r2)
            if (r3 == 0) goto L_0x0049
            android.content.pm.PackageManager r7 = r5.getPackageManager()     // Catch:{ Exception -> 0x002e }
            android.content.Intent r6 = r7.getLaunchIntentForPackage(r6)     // Catch:{ Exception -> 0x002e }
            goto L_0x0143
        L_0x002e:
            r6 = move-exception
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Cause: "
            r7.append(r8)
            java.lang.String r6 = r6.getMessage()
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r6)
            goto L_0x0142
        L_0x0049:
            java.lang.String r3 = com.xiaomi.push.service.ap.b
            boolean r3 = r3.equals(r2)
            if (r3 == 0) goto L_0x00b3
            if (r7 >= r1) goto L_0x0056
            java.lang.String r2 = "notification_style_button_left_intent_uri"
            goto L_0x005d
        L_0x0056:
            if (r7 >= r0) goto L_0x005b
            java.lang.String r2 = "notification_style_button_mid_intent_uri"
            goto L_0x005d
        L_0x005b:
            java.lang.String r2 = "notification_style_button_right_intent_uri"
        L_0x005d:
            if (r7 >= r1) goto L_0x0062
            java.lang.String r7 = "notification_style_button_left_intent_class"
            goto L_0x0069
        L_0x0062:
            if (r7 >= r0) goto L_0x0067
            java.lang.String r7 = "notification_style_button_mid_intent_class"
            goto L_0x0069
        L_0x0067:
            java.lang.String r7 = "notification_style_button_right_intent_class"
        L_0x0069:
            boolean r0 = r8.containsKey(r2)
            if (r0 == 0) goto L_0x0094
            java.lang.Object r7 = r8.get(r2)
            java.lang.String r7 = (java.lang.String) r7
            if (r7 == 0) goto L_0x00af
            r8 = 1
            android.content.Intent r7 = android.content.Intent.parseUri(r7, r8)     // Catch:{ URISyntaxException -> 0x0082 }
            r7.setPackage(r6)     // Catch:{ URISyntaxException -> 0x0080 }
            goto L_0x00b0
        L_0x0080:
            r6 = move-exception
            goto L_0x0084
        L_0x0082:
            r6 = move-exception
            r7 = r4
        L_0x0084:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r0 = "Cause: "
            r8.append(r0)
            java.lang.String r6 = r6.getMessage()
            goto L_0x0136
        L_0x0094:
            boolean r0 = r8.containsKey(r7)
            if (r0 == 0) goto L_0x00af
            java.lang.Object r7 = r8.get(r7)
            java.lang.String r7 = (java.lang.String) r7
            android.content.Intent r8 = new android.content.Intent
            r8.<init>()
            android.content.ComponentName r0 = new android.content.ComponentName
            r0.<init>(r6, r7)
            r8.setComponent(r0)
            r7 = r8
            goto L_0x00b0
        L_0x00af:
            r7 = r4
        L_0x00b0:
            r6 = r7
            goto L_0x0143
        L_0x00b3:
            java.lang.String r6 = com.xiaomi.push.service.ap.c
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0142
            if (r7 >= r1) goto L_0x00c0
            java.lang.String r6 = "notification_style_button_left_web_uri"
            goto L_0x00c7
        L_0x00c0:
            if (r7 >= r0) goto L_0x00c5
            java.lang.String r6 = "notification_style_button_mid_web_uri"
            goto L_0x00c7
        L_0x00c5:
            java.lang.String r6 = "notification_style_button_right_web_uri"
        L_0x00c7:
            java.lang.Object r6 = r8.get(r6)
            java.lang.String r6 = (java.lang.String) r6
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 != 0) goto L_0x0142
            java.lang.String r6 = r6.trim()
            java.lang.String r7 = "http://"
            boolean r7 = r6.startsWith(r7)
            if (r7 != 0) goto L_0x00f8
            java.lang.String r7 = "https://"
            boolean r7 = r6.startsWith(r7)
            if (r7 != 0) goto L_0x00f8
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "http://"
            r7.append(r8)
            r7.append(r6)
            java.lang.String r6 = r7.toString()
        L_0x00f8:
            java.net.URL r7 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0126 }
            r7.<init>(r6)     // Catch:{ MalformedURLException -> 0x0126 }
            java.lang.String r7 = r7.getProtocol()     // Catch:{ MalformedURLException -> 0x0126 }
            java.lang.String r8 = "http"
            boolean r8 = r8.equals(r7)     // Catch:{ MalformedURLException -> 0x0126 }
            if (r8 != 0) goto L_0x0111
            java.lang.String r8 = "https"
            boolean r7 = r8.equals(r7)     // Catch:{ MalformedURLException -> 0x0126 }
            if (r7 == 0) goto L_0x0142
        L_0x0111:
            android.content.Intent r7 = new android.content.Intent     // Catch:{ MalformedURLException -> 0x0126 }
            java.lang.String r8 = "android.intent.action.VIEW"
            r7.<init>(r8)     // Catch:{ MalformedURLException -> 0x0126 }
            android.net.Uri r6 = android.net.Uri.parse(r6)     // Catch:{ MalformedURLException -> 0x0124 }
            r7.setData(r6)     // Catch:{ MalformedURLException -> 0x0124 }
            android.content.Intent r6 = a((android.content.Context) r5, (android.content.Intent) r7)     // Catch:{ MalformedURLException -> 0x0124 }
            goto L_0x0143
        L_0x0124:
            r6 = move-exception
            goto L_0x0128
        L_0x0126:
            r6 = move-exception
            r7 = r4
        L_0x0128:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r0 = "Cause: "
            r8.append(r0)
            java.lang.String r6 = r6.getMessage()
        L_0x0136:
            r8.append(r6)
            java.lang.String r6 = r8.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r6)
            goto L_0x00b0
        L_0x0142:
            r6 = r4
        L_0x0143:
            if (r6 == 0) goto L_0x0170
            r7 = 268435456(0x10000000, float:2.5243549E-29)
            r6.addFlags(r7)
            android.content.pm.PackageManager r5 = r5.getPackageManager()     // Catch:{ Exception -> 0x0157 }
            r7 = 65536(0x10000, float:9.18355E-41)
            android.content.pm.ResolveInfo r5 = r5.resolveActivity(r6, r7)     // Catch:{ Exception -> 0x0157 }
            if (r5 == 0) goto L_0x0170
            return r6
        L_0x0157:
            r5 = move-exception
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Cause: "
            r6.append(r7)
            java.lang.String r5 = r5.getMessage()
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r5)
        L_0x0170:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.z.m618a(android.content.Context, java.lang.String, int, java.util.Map):android.content.Intent");
    }

    private static Bitmap a(Context context, int i) {
        return a(context.getResources().getDrawable(i));
    }

    /* JADX INFO: finally extract failed */
    private static Bitmap a(Context context, String str, boolean z) {
        Future submit = f934a.submit(new a(str, context, z));
        try {
            Bitmap bitmap = (Bitmap) submit.get(180, TimeUnit.SECONDS);
            if (bitmap != null) {
                return bitmap;
            }
            submit.cancel(true);
            return bitmap;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            submit.cancel(true);
            return null;
        } catch (Throwable th) {
            submit.cancel(true);
            throw th;
        }
    }

    public static Bitmap a(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i = 1;
        if (intrinsicWidth <= 0) {
            intrinsicWidth = 1;
        }
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicHeight > 0) {
            i = intrinsicHeight;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    private static RemoteViews a(Context context, ic icVar, byte[] bArr) {
        ht a2 = icVar.a();
        String a3 = a(icVar);
        Map a4 = a2.a();
        if (a4 == null) {
            return null;
        }
        String str = (String) a4.get("layout_name");
        String str2 = (String) a4.get("layout_value");
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(a3);
                int identifier = resourcesForApplication.getIdentifier(str, "layout", a3);
                if (identifier == 0) {
                    return null;
                }
                RemoteViews remoteViews = new RemoteViews(a3, identifier);
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    if (jSONObject.has("text")) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("text");
                        Iterator<String> keys = jSONObject2.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            String string = jSONObject2.getString(next);
                            int identifier2 = resourcesForApplication.getIdentifier(next, "id", a3);
                            if (identifier2 > 0) {
                                remoteViews.setTextViewText(identifier2, string);
                            }
                        }
                    }
                    if (jSONObject.has("image")) {
                        JSONObject jSONObject3 = jSONObject.getJSONObject("image");
                        Iterator<String> keys2 = jSONObject3.keys();
                        while (keys2.hasNext()) {
                            String next2 = keys2.next();
                            String string2 = jSONObject3.getString(next2);
                            int identifier3 = resourcesForApplication.getIdentifier(next2, "id", a3);
                            int identifier4 = resourcesForApplication.getIdentifier(string2, "drawable", a3);
                            if (identifier3 > 0) {
                                remoteViews.setImageViewResource(identifier3, identifier4);
                            }
                        }
                    }
                    if (jSONObject.has("time")) {
                        JSONObject jSONObject4 = jSONObject.getJSONObject("time");
                        Iterator<String> keys3 = jSONObject4.keys();
                        while (keys3.hasNext()) {
                            String next3 = keys3.next();
                            String string3 = jSONObject4.getString(next3);
                            if (string3.length() == 0) {
                                string3 = "yy-MM-dd hh:mm";
                            }
                            int identifier5 = resourcesForApplication.getIdentifier(next3, "id", a3);
                            if (identifier5 > 0) {
                                remoteViews.setTextViewText(identifier5, new SimpleDateFormat(string3).format(new Date(System.currentTimeMillis())));
                            }
                        }
                    }
                    return remoteViews;
                } catch (JSONException e) {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                    return null;
                }
            } catch (PackageManager.NameNotFoundException e2) {
                com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:105:0x0240  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0251  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x0293  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0295  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x02d5 A[Catch:{ Exception -> 0x03e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x038e A[Catch:{ Exception -> 0x03e5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x03f7 A[SYNTHETIC, Splitter:B:174:0x03f7] */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x0463  */
    /* JADX WARNING: Removed duplicated region for block: B:196:0x048b  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x010a  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x011a A[SYNTHETIC, Splitter:B:45:0x011a] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0184  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0186  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0194  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01b7  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01f7  */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.xiaomi.push.service.z.b a(android.content.Context r18, com.xiaomi.push.ic r19, byte[] r20, android.widget.RemoteViews r21, android.app.PendingIntent r22) {
        /*
            r1 = r18
            r0 = r21
            com.xiaomi.push.service.z$b r2 = new com.xiaomi.push.service.z$b
            r2.<init>()
            com.xiaomi.push.ht r3 = r19.a()
            java.lang.String r4 = a((com.xiaomi.push.ic) r19)
            java.util.Map r5 = r3.a()
            android.app.Notification$Builder r6 = new android.app.Notification$Builder
            r6.<init>(r1)
            java.lang.String[] r7 = a((android.content.Context) r1, (com.xiaomi.push.ht) r3)
            r8 = 0
            r9 = r7[r8]
            r6.setContentTitle(r9)
            r9 = 1
            r10 = r7[r9]
            r6.setContentText(r10)
            if (r0 == 0) goto L_0x0030
            r6.setContent(r0)
            goto L_0x0046
        L_0x0030:
            int r0 = android.os.Build.VERSION.SDK_INT
            r10 = 16
            if (r0 < r10) goto L_0x0046
            if (r5 == 0) goto L_0x0046
            java.lang.String r0 = "notification_style_type"
            boolean r0 = r5.containsKey(r0)
            if (r0 == 0) goto L_0x0046
            r0 = r7[r9]
            android.app.Notification$Builder r6 = a((android.content.Context) r1, (java.util.Map<java.lang.String, java.lang.String>) r5, (android.app.Notification.Builder) r6, (java.lang.String) r0)
        L_0x0046:
            java.lang.String r0 = r19.b()
            android.app.Notification$Builder r6 = a((android.app.Notification.Builder) r6, (android.content.Context) r1, (java.lang.String) r0, (java.util.Map<java.lang.String, java.lang.String>) r5)
            long r10 = java.lang.System.currentTimeMillis()
            r6.setWhen(r10)
            if (r5 != 0) goto L_0x0059
            r0 = 0
            goto L_0x0061
        L_0x0059:
            java.lang.String r0 = "notification_show_when"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
        L_0x0061:
            boolean r10 = android.text.TextUtils.isEmpty(r0)
            r11 = 24
            if (r10 == 0) goto L_0x0073
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 < r11) goto L_0x0070
            r6.setShowWhen(r9)
        L_0x0070:
            r0 = r22
            goto L_0x007b
        L_0x0073:
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
            r6.setShowWhen(r0)
            goto L_0x0070
        L_0x007b:
            r6.setContentIntent(r0)
            java.lang.String r0 = "mipush_notification"
            int r0 = a((android.content.Context) r1, (java.lang.String) r4, (java.lang.String) r0)
            java.lang.String r10 = "mipush_small_notification"
            int r10 = a((android.content.Context) r1, (java.lang.String) r4, (java.lang.String) r10)
            if (r0 <= 0) goto L_0x0099
            if (r10 <= 0) goto L_0x0099
            android.graphics.Bitmap r0 = a((android.content.Context) r1, (int) r0)
            r6.setLargeIcon(r0)
            r6.setSmallIcon(r10)
            goto L_0x00a0
        L_0x0099:
            int r0 = b((android.content.Context) r1, (java.lang.String) r4)
            r6.setSmallIcon(r0)
        L_0x00a0:
            int r0 = android.os.Build.VERSION.SDK_INT
            r10 = 23
            if (r0 < r10) goto L_0x0130
            if (r5 != 0) goto L_0x00aa
            r0 = 0
            goto L_0x00b6
        L_0x00aa:
            java.lang.String r0 = "notification_small_icon_uri"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            android.graphics.Bitmap r0 = a((android.content.Context) r1, (java.lang.String) r0, (boolean) r9)
        L_0x00b6:
            if (r0 == 0) goto L_0x00fa
            java.lang.String r10 = "android.graphics.drawable.Icon"
            java.lang.String r12 = "createWithBitmap"
            java.lang.Object[] r13 = new java.lang.Object[r9]
            r13[r8] = r0
            java.lang.Object r0 = com.xiaomi.push.at.a((java.lang.String) r10, (java.lang.String) r12, (java.lang.Object[]) r13)
            if (r0 == 0) goto L_0x00dd
            java.lang.String r10 = "setSmallIcon"
            java.lang.Object[] r12 = new java.lang.Object[r9]
            r12[r8] = r0
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r10, (java.lang.Object[]) r12)
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            java.lang.String r10 = "miui.isGrayscaleIcon"
            r0.putBoolean(r10, r9)
            r6.addExtras(r0)
            goto L_0x0108
        L_0x00dd:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r10 = "failed te get small icon with url:"
            r0.append(r10)
        L_0x00e7:
            java.lang.String r10 = "notification_small_icon_uri"
            java.lang.Object r10 = r5.get(r10)
            java.lang.String r10 = (java.lang.String) r10
        L_0x00ef:
            r0.append(r10)
            java.lang.String r0 = r0.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            goto L_0x0108
        L_0x00fa:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r10 = "failed to get small icon url:"
            r0.append(r10)
            if (r5 != 0) goto L_0x00e7
            r10 = 0
            goto L_0x00ef
        L_0x0108:
            if (r5 != 0) goto L_0x010c
            r0 = 0
            goto L_0x0114
        L_0x010c:
            java.lang.String r0 = "notification_small_icon_color"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
        L_0x0114:
            boolean r10 = android.text.TextUtils.isEmpty(r0)
            if (r10 != 0) goto L_0x0130
            int r0 = android.graphics.Color.parseColor(r0)     // Catch:{ Exception -> 0x012c }
            java.lang.String r10 = "setColor"
            java.lang.Object[] r12 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x012c }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x012c }
            r12[r8] = r0     // Catch:{ Exception -> 0x012c }
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r10, (java.lang.Object[]) r12)     // Catch:{ Exception -> 0x012c }
            goto L_0x0130
        L_0x012c:
            r0 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x0130:
            if (r5 != 0) goto L_0x0134
            r0 = 0
            goto L_0x013c
        L_0x0134:
            java.lang.String r0 = "__dynamic_icon_uri"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
        L_0x013c:
            if (r5 == 0) goto L_0x014e
            java.lang.String r10 = "__adiom"
            java.lang.Object r10 = r5.get(r10)
            java.lang.String r10 = (java.lang.String) r10
            boolean r10 = java.lang.Boolean.parseBoolean(r10)
            if (r10 == 0) goto L_0x014e
            r10 = 1
            goto L_0x014f
        L_0x014e:
            r10 = 0
        L_0x014f:
            if (r10 != 0) goto L_0x015a
            boolean r10 = com.xiaomi.push.l.a()
            if (r10 != 0) goto L_0x0158
            goto L_0x015a
        L_0x0158:
            r10 = 0
            goto L_0x015b
        L_0x015a:
            r10 = 1
        L_0x015b:
            if (r0 == 0) goto L_0x0181
            if (r10 == 0) goto L_0x0181
            java.lang.String r10 = "http"
            boolean r10 = r0.startsWith(r10)
            if (r10 == 0) goto L_0x0176
            com.xiaomi.push.service.af$b r0 = com.xiaomi.push.service.af.a((android.content.Context) r1, (java.lang.String) r0, (boolean) r9)
            if (r0 == 0) goto L_0x0174
            android.graphics.Bitmap r10 = r0.f841a
            long r12 = r0.a
            r2.a = r12
            goto L_0x017a
        L_0x0174:
            r10 = 0
            goto L_0x017a
        L_0x0176:
            android.graphics.Bitmap r10 = com.xiaomi.push.service.af.a((android.content.Context) r1, (java.lang.String) r0)
        L_0x017a:
            if (r10 == 0) goto L_0x0181
            r6.setLargeIcon(r10)
            r10 = 1
            goto L_0x0182
        L_0x0181:
            r10 = 0
        L_0x0182:
            if (r5 != 0) goto L_0x0186
            r0 = 0
            goto L_0x0192
        L_0x0186:
            java.lang.String r0 = "notification_large_icon_uri"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            android.graphics.Bitmap r0 = a((android.content.Context) r1, (java.lang.String) r0, (boolean) r9)
        L_0x0192:
            if (r0 == 0) goto L_0x0197
            r6.setLargeIcon(r0)
        L_0x0197:
            if (r5 == 0) goto L_0x01d1
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 < r11) goto L_0x01d1
            java.lang.String r0 = "notification_group"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r11 = "notification_is_summary"
            java.lang.Object r11 = r5.get(r11)
            java.lang.String r11 = (java.lang.String) r11
            boolean r11 = java.lang.Boolean.parseBoolean(r11)
            boolean r12 = android.text.TextUtils.isEmpty(r0)
            if (r12 == 0) goto L_0x01bb
            java.lang.String r0 = a((com.xiaomi.push.ic) r19)
        L_0x01bb:
            java.lang.String r12 = "setGroup"
            java.lang.Object[] r13 = new java.lang.Object[r9]
            r13[r8] = r0
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r12, (java.lang.Object[]) r13)
            java.lang.String r0 = "setGroupSummary"
            java.lang.Object[] r12 = new java.lang.Object[r9]
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)
            r12[r8] = r11
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r0, (java.lang.Object[]) r12)
        L_0x01d1:
            r6.setAutoCancel(r9)
            long r11 = java.lang.System.currentTimeMillis()
            if (r5 == 0) goto L_0x01ed
            java.lang.String r0 = "ticker"
            boolean r0 = r5.containsKey(r0)
            if (r0 == 0) goto L_0x01ed
            java.lang.String r0 = "ticker"
            java.lang.Object r0 = r5.get(r0)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            r6.setTicker(r0)
        L_0x01ed:
            long r13 = a
            long r13 = r11 - r13
            r15 = 10000(0x2710, double:4.9407E-320)
            int r17 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r17 <= 0) goto L_0x0240
            a = r11
            int r3 = r3.f516a
            boolean r11 = b((android.content.Context) r1, (java.lang.String) r4)
            if (r11 == 0) goto L_0x0205
            int r3 = a((android.content.Context) r1, (java.lang.String) r4)
        L_0x0205:
            r6.setDefaults(r3)
            if (r5 == 0) goto L_0x0242
            r11 = r3 & 1
            if (r11 == 0) goto L_0x0242
            java.lang.String r11 = "sound_uri"
            java.lang.Object r11 = r5.get(r11)
            java.lang.String r11 = (java.lang.String) r11
            boolean r12 = android.text.TextUtils.isEmpty(r11)
            if (r12 != 0) goto L_0x0242
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "android.resource://"
            r12.append(r13)
            r12.append(r4)
            java.lang.String r12 = r12.toString()
            boolean r12 = r11.startsWith(r12)
            if (r12 == 0) goto L_0x0242
            r12 = r3 ^ 1
            r6.setDefaults(r12)
            android.net.Uri r11 = android.net.Uri.parse(r11)
            r6.setSound(r11)
            goto L_0x0243
        L_0x0240:
            r3 = -100
        L_0x0242:
            r11 = 0
        L_0x0243:
            if (r5 == 0) goto L_0x0414
            int r14 = android.os.Build.VERSION.SDK_INT
            r15 = 26
            if (r14 < r15) goto L_0x0414
            int r14 = a((java.util.Map<java.lang.String, java.lang.String>) r5)
            if (r14 <= 0) goto L_0x0261
            java.lang.String r7 = "setTimeoutAfter"
            java.lang.Object[] r0 = new java.lang.Object[r9]
            int r14 = r14 * 1000
            long r13 = (long) r14
            java.lang.Long r13 = java.lang.Long.valueOf(r13)
            r0[r8] = r13
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r7, (java.lang.Object[]) r0)
        L_0x0261:
            java.lang.String r0 = "channel_id"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            boolean r7 = android.text.TextUtils.isEmpty(r0)
            if (r7 == 0) goto L_0x0277
            android.content.pm.ApplicationInfo r7 = r18.getApplicationInfo()
            int r7 = r7.targetSdkVersion
            if (r7 < r15) goto L_0x03e9
        L_0x0277:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r13 = "mipush_"
            r7.append(r13)
            r7.append(r4)
            java.lang.String r13 = "_default"
            r7.append(r13)
            java.lang.String r7 = r7.toString()
            boolean r13 = android.text.TextUtils.isEmpty(r0)
            if (r13 == 0) goto L_0x0295
            r0 = r7
            goto L_0x02b4
        L_0x0295:
            boolean r13 = com.xiaomi.push.l.a()
            if (r13 == 0) goto L_0x02b4
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "mipush_"
            r13.append(r14)
            r13.append(r4)
            java.lang.String r14 = "_"
            r13.append(r14)
            r13.append(r0)
            java.lang.String r0 = r13.toString()
        L_0x02b4:
            java.lang.String r13 = "setChannelId"
            java.lang.Object[] r14 = new java.lang.Object[r9]
            r14[r8] = r0
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r13, (java.lang.Object[]) r14)
            java.lang.String r13 = a((android.content.Context) r1, (java.lang.String) r4, (java.util.Map<java.lang.String, java.lang.String>) r5)
            java.lang.String r14 = "notification"
            java.lang.Object r14 = r1.getSystemService(r14)
            android.app.NotificationManager r14 = (android.app.NotificationManager) r14
            java.lang.String r15 = "getNotificationChannel"
            java.lang.Object[] r12 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x03e5 }
            r12[r8] = r0     // Catch:{ Exception -> 0x03e5 }
            java.lang.Object r12 = com.xiaomi.push.at.a((java.lang.Object) r14, (java.lang.String) r15, (java.lang.Object[]) r12)     // Catch:{ Exception -> 0x03e5 }
            if (r12 != 0) goto L_0x036f
            java.lang.String r12 = "android.app.NotificationChannel"
            java.lang.Class r12 = java.lang.Class.forName(r12)     // Catch:{ Exception -> 0x03e5 }
            r15 = 3
            java.lang.Class[] r9 = new java.lang.Class[r15]     // Catch:{ Exception -> 0x03e5 }
            java.lang.Class<java.lang.String> r15 = java.lang.String.class
            r9[r8] = r15     // Catch:{ Exception -> 0x03e5 }
            java.lang.Class<java.lang.CharSequence> r15 = java.lang.CharSequence.class
            r16 = 1
            r9[r16] = r15     // Catch:{ Exception -> 0x03e5 }
            java.lang.Class r15 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x03e5 }
            r16 = 2
            r9[r16] = r15     // Catch:{ Exception -> 0x03e5 }
            java.lang.reflect.Constructor r9 = r12.getConstructor(r9)     // Catch:{ Exception -> 0x03e5 }
            r12 = 3
            java.lang.Object[] r15 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x03e5 }
            r15[r8] = r0     // Catch:{ Exception -> 0x03e5 }
            r16 = 1
            r15[r16] = r13     // Catch:{ Exception -> 0x03e5 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x03e5 }
            r12 = 2
            r15[r12] = r0     // Catch:{ Exception -> 0x03e5 }
            java.lang.Object r0 = r9.newInstance(r15)     // Catch:{ Exception -> 0x03e5 }
            r9 = -100
            if (r3 <= r9) goto L_0x0362
            r9 = 0
            r6.setSound(r9, r9)     // Catch:{ Exception -> 0x03e5 }
            r6.setDefaults(r8)     // Catch:{ Exception -> 0x03e5 }
            r9 = r3 & 1
            r12 = r3 & 2
            r13 = 4
            r3 = r3 & r13
            r15 = 1
            if (r9 == r15) goto L_0x0329
            java.lang.String r9 = "setSound"
            r13 = 2
            java.lang.Object[] r15 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x03e5 }
            r13 = 0
            r15[r8] = r13     // Catch:{ Exception -> 0x03e5 }
            r16 = 1
            r15[r16] = r13     // Catch:{ Exception -> 0x03e5 }
            com.xiaomi.push.at.a((java.lang.Object) r0, (java.lang.String) r9, (java.lang.Object[]) r15)     // Catch:{ Exception -> 0x03e5 }
        L_0x0329:
            if (r11 == 0) goto L_0x033a
            java.lang.String r9 = "setSound"
            r13 = 2
            java.lang.Object[] r15 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x03e5 }
            r15[r8] = r11     // Catch:{ Exception -> 0x03e5 }
            android.media.AudioAttributes r11 = android.app.Notification.AUDIO_ATTRIBUTES_DEFAULT     // Catch:{ Exception -> 0x03e5 }
            r13 = 1
            r15[r13] = r11     // Catch:{ Exception -> 0x03e5 }
            com.xiaomi.push.at.a((java.lang.Object) r0, (java.lang.String) r9, (java.lang.Object[]) r15)     // Catch:{ Exception -> 0x03e5 }
        L_0x033a:
            java.lang.String r9 = "enableVibration"
            r11 = 1
            java.lang.Object[] r13 = new java.lang.Object[r11]     // Catch:{ Exception -> 0x03e5 }
            r11 = 2
            if (r12 != r11) goto L_0x0344
            r11 = 1
            goto L_0x0345
        L_0x0344:
            r11 = 0
        L_0x0345:
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)     // Catch:{ Exception -> 0x03e5 }
            r13[r8] = r11     // Catch:{ Exception -> 0x03e5 }
            com.xiaomi.push.at.a((java.lang.Object) r0, (java.lang.String) r9, (java.lang.Object[]) r13)     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r9 = "enableLights"
            r11 = 1
            java.lang.Object[] r12 = new java.lang.Object[r11]     // Catch:{ Exception -> 0x03e5 }
            r11 = 4
            if (r3 != r11) goto L_0x0358
            r3 = 1
            goto L_0x0359
        L_0x0358:
            r3 = 0
        L_0x0359:
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x03e5 }
            r12[r8] = r3     // Catch:{ Exception -> 0x03e5 }
            com.xiaomi.push.at.a((java.lang.Object) r0, (java.lang.String) r9, (java.lang.Object[]) r12)     // Catch:{ Exception -> 0x03e5 }
        L_0x0362:
            a((java.lang.Object) r0, (java.util.Map<java.lang.String, java.lang.String>) r5)     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r3 = "createNotificationChannel"
            r9 = 1
            java.lang.Object[] r11 = new java.lang.Object[r9]     // Catch:{ Exception -> 0x03e5 }
            r11[r8] = r0     // Catch:{ Exception -> 0x03e5 }
            com.xiaomi.push.at.a((java.lang.Object) r14, (java.lang.String) r3, (java.lang.Object[]) r11)     // Catch:{ Exception -> 0x03e5 }
        L_0x036f:
            java.lang.String r0 = "getNotificationChannels"
            java.lang.Object[] r3 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x03e5 }
            java.lang.Object r0 = com.xiaomi.push.at.a((java.lang.Object) r14, (java.lang.String) r0, (java.lang.Object[]) r3)     // Catch:{ Exception -> 0x03e5 }
            if (r0 == 0) goto L_0x03e9
            boolean r3 = r0 instanceof java.util.List     // Catch:{ Exception -> 0x03e5 }
            if (r3 == 0) goto L_0x03e9
            java.util.List r0 = (java.util.List) r0     // Catch:{ Exception -> 0x03e5 }
            if (r0 == 0) goto L_0x03e9
            int r3 = r0.size()     // Catch:{ Exception -> 0x03e5 }
            if (r3 <= 0) goto L_0x03e9
            r3 = 0
        L_0x0388:
            int r9 = r0.size()     // Catch:{ Exception -> 0x03e5 }
            if (r3 >= r9) goto L_0x03e9
            java.lang.Object r9 = r0.get(r3)     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r11 = "getName"
            java.lang.Object[] r12 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x03e5 }
            java.lang.Object r11 = com.xiaomi.push.at.a((java.lang.Object) r9, (java.lang.String) r11, (java.lang.Object[]) r12)     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r12 = "getId"
            java.lang.Object[] r13 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x03e5 }
            java.lang.Object r9 = com.xiaomi.push.at.a((java.lang.Object) r9, (java.lang.String) r12, (java.lang.Object[]) r13)     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r12 = com.xiaomi.push.g.b((android.content.Context) r1, (java.lang.String) r4)     // Catch:{ Exception -> 0x03e5 }
            if (r11 == 0) goto L_0x03e2
            if (r9 == 0) goto L_0x03e2
            boolean r13 = r11 instanceof java.lang.CharSequence     // Catch:{ Exception -> 0x03e5 }
            if (r13 == 0) goto L_0x03e2
            boolean r13 = r9 instanceof java.lang.String     // Catch:{ Exception -> 0x03e5 }
            if (r13 == 0) goto L_0x03e2
            boolean r11 = r11.equals(r12)     // Catch:{ Exception -> 0x03e5 }
            if (r11 == 0) goto L_0x03e2
            r11 = r9
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Exception -> 0x03e5 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x03e5 }
            r12.<init>()     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r13 = "mipush_"
            r12.append(r13)     // Catch:{ Exception -> 0x03e5 }
            r12.append(r4)     // Catch:{ Exception -> 0x03e5 }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x03e5 }
            boolean r11 = r11.startsWith(r12)     // Catch:{ Exception -> 0x03e5 }
            if (r11 == 0) goto L_0x03e2
            boolean r11 = r9.equals(r7)     // Catch:{ Exception -> 0x03e5 }
            if (r11 != 0) goto L_0x03e2
            java.lang.String r11 = "deleteNotificationChannel"
            r12 = 1
            java.lang.Object[] r13 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x03e5 }
            r13[r8] = r9     // Catch:{ Exception -> 0x03e5 }
            com.xiaomi.push.at.a((java.lang.Object) r14, (java.lang.String) r11, (java.lang.Object[]) r13)     // Catch:{ Exception -> 0x03e5 }
        L_0x03e2:
            int r3 = r3 + 1
            goto L_0x0388
        L_0x03e5:
            r0 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x03e9:
            java.lang.String r0 = "background_color"
            java.lang.Object r0 = r5.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto L_0x0414
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            r6.setOngoing(r3)     // Catch:{ Exception -> 0x0410 }
            r6.setColor(r0)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r0 = "setColorized"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0410 }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x0410 }
            r4[r8] = r7     // Catch:{ Exception -> 0x0410 }
            com.xiaomi.push.at.a((java.lang.Object) r6, (java.lang.String) r0, (java.lang.Object[]) r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0414
        L_0x0410:
            r0 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x0414:
            boolean r0 = com.xiaomi.push.l.c()
            if (r0 == 0) goto L_0x043c
            java.lang.String r0 = "com.xiaomi.xmsf"
            java.lang.String r3 = r18.getPackageName()
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x043c
            java.lang.String r0 = "miui.util.NotificationHelper"
            java.lang.String r3 = "setTargetPkg"
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r4[r8] = r1
            r1 = 1
            r4[r1] = r6
            java.lang.String r1 = a((com.xiaomi.push.ic) r19)
            r7 = 2
            r4[r7] = r1
            com.xiaomi.push.at.a((java.lang.String) r0, (java.lang.String) r3, (java.lang.Object[]) r4)
        L_0x043c:
            android.app.Notification r0 = r6.getNotification()
            if (r10 == 0) goto L_0x044b
            boolean r1 = com.xiaomi.push.l.a()
            if (r1 == 0) goto L_0x044b
            a((android.app.Notification) r0)
        L_0x044b:
            if (r5 == 0) goto L_0x04a5
            java.lang.String r1 = "extraNotification"
            java.lang.Object r1 = com.xiaomi.push.at.a((java.lang.Object) r0, (java.lang.String) r1)
            if (r1 == 0) goto L_0x04a5
            java.lang.String r3 = "enable_keyguard"
            java.lang.Object r3 = r5.get(r3)
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x047d
            java.lang.String r3 = "setEnableKeyguard"
            r4 = 1
            java.lang.Object[] r6 = new java.lang.Object[r4]
            java.lang.String r4 = "enable_keyguard"
            java.lang.Object r4 = r5.get(r4)
            java.lang.String r4 = (java.lang.String) r4
            boolean r4 = java.lang.Boolean.parseBoolean(r4)
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)
            r6[r8] = r4
            com.xiaomi.push.at.a((java.lang.Object) r1, (java.lang.String) r3, (java.lang.Object[]) r6)
        L_0x047d:
            java.lang.String r3 = "enable_float"
            java.lang.Object r3 = r5.get(r3)
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x04a5
            java.lang.String r3 = "setEnableFloat"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.String r6 = "enable_float"
            java.lang.Object r5 = r5.get(r6)
            java.lang.String r5 = (java.lang.String) r5
            boolean r5 = java.lang.Boolean.parseBoolean(r5)
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)
            r4[r8] = r5
            com.xiaomi.push.at.a((java.lang.Object) r1, (java.lang.String) r3, (java.lang.Object[]) r4)
        L_0x04a5:
            r2.f937a = r0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.z.a(android.content.Context, com.xiaomi.push.ic, byte[], android.widget.RemoteViews, android.app.PendingIntent):com.xiaomi.push.service.z$b");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v27, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v5, resolved type: java.lang.String} */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0106, code lost:
        if (r5 == null) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x010a, code lost:
        if (r5 == null) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x010e, code lost:
        if (r5 == null) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0112, code lost:
        if (r5 == null) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0114, code lost:
        com.xiaomi.push.ev.a(r19.getApplicationContext()).a(r20.b(), b(r20), r5.a(), r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x012b, code lost:
        com.xiaomi.channel.commonutils.logger.b.a(r0);
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* renamed from: a  reason: collision with other method in class */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.xiaomi.push.service.z.c m619a(android.content.Context r19, com.xiaomi.push.ic r20, byte[] r21) {
        /*
            r1 = r19
            r2 = r20
            r0 = r21
            com.xiaomi.push.service.z$c r3 = new com.xiaomi.push.service.z$c
            r3.<init>()
            java.lang.String r4 = a((com.xiaomi.push.ic) r20)
            com.xiaomi.push.g$a r4 = com.xiaomi.push.g.a((android.content.Context) r1, (java.lang.String) r4)
            com.xiaomi.push.g$a r5 = com.xiaomi.push.g.a.NOT_ALLOWED
            if (r4 != r5) goto L_0x006c
            com.xiaomi.push.ht r0 = r20.a()
            if (r0 == 0) goto L_0x004e
            android.content.Context r1 = r19.getApplicationContext()
            com.xiaomi.push.ev r1 = com.xiaomi.push.ev.a((android.content.Context) r1)
            java.lang.String r4 = r20.b()
            java.lang.String r5 = b((com.xiaomi.push.ic) r20)
            java.lang.String r0 = r0.a()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Do not notify because user block "
            r6.append(r7)
            java.lang.String r7 = a((com.xiaomi.push.ic) r20)
            r6.append(r7)
            java.lang.String r7 = "‘s notification"
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            r1.a((java.lang.String) r4, (java.lang.String) r5, (java.lang.String) r0, (java.lang.String) r6)
        L_0x004e:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Do not notify because user block "
            r0.append(r1)
            java.lang.String r1 = a((com.xiaomi.push.ic) r20)
            r0.append(r1)
            java.lang.String r1 = "‘s notification"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L_0x0068:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            return r3
        L_0x006c:
            java.lang.String r4 = "notification"
            java.lang.Object r4 = r1.getSystemService(r4)
            android.app.NotificationManager r4 = (android.app.NotificationManager) r4
            com.xiaomi.push.ht r5 = r20.a()
            android.widget.RemoteViews r6 = a((android.content.Context) r19, (com.xiaomi.push.ic) r20, (byte[]) r21)
            android.app.PendingIntent r7 = a((android.content.Context) r1, (com.xiaomi.push.ic) r2, (com.xiaomi.push.ht) r5, (byte[]) r0)
            if (r7 != 0) goto L_0x00a0
            if (r5 == 0) goto L_0x009d
            android.content.Context r0 = r19.getApplicationContext()
            com.xiaomi.push.ev r0 = com.xiaomi.push.ev.a((android.content.Context) r0)
            java.lang.String r1 = r20.b()
            java.lang.String r2 = b((com.xiaomi.push.ic) r20)
            java.lang.String r4 = r5.a()
            java.lang.String r5 = "The click PendingIntent is null. "
            r0.a((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r4, (java.lang.String) r5)
        L_0x009d:
            java.lang.String r0 = "The click PendingIntent is null. "
            goto L_0x0068
        L_0x00a0:
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 11
            r10 = 0
            if (r8 < r9) goto L_0x00ba
            com.xiaomi.push.service.z$b r0 = a(r1, r2, r0, r6, r7)
            long r6 = r0.a
            r3.a = r6
            java.lang.String r6 = a((com.xiaomi.push.ic) r20)
            r3.f938a = r6
            android.app.Notification r0 = r0.f937a
            r8 = r0
            goto L_0x01ae
        L_0x00ba:
            android.app.Notification r8 = new android.app.Notification
            java.lang.String r0 = a((com.xiaomi.push.ic) r20)
            int r0 = b((android.content.Context) r1, (java.lang.String) r0)
            long r11 = java.lang.System.currentTimeMillis()
            r8.<init>(r0, r10, r11)
            java.lang.String[] r0 = a((android.content.Context) r1, (com.xiaomi.push.ht) r5)
            r9 = 1
            java.lang.Class r11 = r8.getClass()     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.String r12 = "setLatestEventInfo"
            r13 = 4
            java.lang.Class[] r14 = new java.lang.Class[r13]     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.Class<android.content.Context> r15 = android.content.Context.class
            r16 = 0
            r14[r16] = r15     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.Class<java.lang.CharSequence> r15 = java.lang.CharSequence.class
            r14[r9] = r15     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.Class<java.lang.CharSequence> r15 = java.lang.CharSequence.class
            r17 = 2
            r14[r17] = r15     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.Class<android.app.PendingIntent> r15 = android.app.PendingIntent.class
            r18 = 3
            r14[r18] = r15     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.reflect.Method r11 = r11.getMethod(r12, r14)     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            java.lang.Object[] r12 = new java.lang.Object[r13]     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r12[r16] = r1     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r13 = r0[r16]     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r12[r9] = r13     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r0 = r0[r9]     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r12[r17] = r0     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r12[r18] = r7     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            r11.invoke(r8, r12)     // Catch:{ NoSuchMethodException -> 0x0111, IllegalAccessException -> 0x010d, IllegalArgumentException -> 0x0109, InvocationTargetException -> 0x0105 }
            goto L_0x012e
        L_0x0105:
            r0 = move-exception
            if (r5 == 0) goto L_0x012b
            goto L_0x0114
        L_0x0109:
            r0 = move-exception
            if (r5 == 0) goto L_0x012b
            goto L_0x0114
        L_0x010d:
            r0 = move-exception
            if (r5 == 0) goto L_0x012b
            goto L_0x0114
        L_0x0111:
            r0 = move-exception
            if (r5 == 0) goto L_0x012b
        L_0x0114:
            android.content.Context r7 = r19.getApplicationContext()
            com.xiaomi.push.ev r7 = com.xiaomi.push.ev.a((android.content.Context) r7)
            java.lang.String r11 = r20.b()
            java.lang.String r12 = b((com.xiaomi.push.ic) r20)
            java.lang.String r13 = r5.a()
            r7.a((java.lang.String) r11, (java.lang.String) r12, (java.lang.String) r13, (java.lang.Throwable) r0)
        L_0x012b:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x012e:
            java.util.Map r0 = r5.a()
            if (r0 == 0) goto L_0x0146
            java.lang.String r7 = "ticker"
            boolean r7 = r0.containsKey(r7)
            if (r7 == 0) goto L_0x0146
            java.lang.String r7 = "ticker"
            java.lang.Object r7 = r0.get(r7)
            java.lang.CharSequence r7 = (java.lang.CharSequence) r7
            r8.tickerText = r7
        L_0x0146:
            long r11 = java.lang.System.currentTimeMillis()
            long r13 = a
            long r13 = r11 - r13
            r15 = 10000(0x2710, double:4.9407E-320)
            int r7 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r7 <= 0) goto L_0x01a4
            a = r11
            int r7 = r5.f516a
            java.lang.String r11 = a((com.xiaomi.push.ic) r20)
            boolean r11 = b((android.content.Context) r1, (java.lang.String) r11)
            if (r11 == 0) goto L_0x016a
            java.lang.String r7 = a((com.xiaomi.push.ic) r20)
            int r7 = a((android.content.Context) r1, (java.lang.String) r7)
        L_0x016a:
            r8.defaults = r7
            if (r0 == 0) goto L_0x01a4
            r11 = r7 & 1
            if (r11 == 0) goto L_0x01a4
            java.lang.String r11 = "sound_uri"
            java.lang.Object r0 = r0.get(r11)
            java.lang.String r0 = (java.lang.String) r0
            boolean r11 = android.text.TextUtils.isEmpty(r0)
            if (r11 != 0) goto L_0x01a4
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "android.resource://"
            r11.append(r12)
            java.lang.String r12 = a((com.xiaomi.push.ic) r20)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            boolean r11 = r0.startsWith(r11)
            if (r11 == 0) goto L_0x01a4
            r7 = r7 ^ r9
            r8.defaults = r7
            android.net.Uri r0 = android.net.Uri.parse(r0)
            r8.sound = r0
        L_0x01a4:
            int r0 = r8.flags
            r0 = r0 | 16
            r8.flags = r0
            if (r6 == 0) goto L_0x01ae
            r8.contentView = r6
        L_0x01ae:
            boolean r0 = com.xiaomi.push.l.a()
            if (r0 == 0) goto L_0x0218
            int r0 = android.os.Build.VERSION.SDK_INT
            r6 = 19
            if (r0 < r6) goto L_0x0218
            java.lang.String r0 = r5.a()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x01cf
            android.os.Bundle r0 = r8.extras
            java.lang.String r6 = "message_id"
            java.lang.String r7 = r5.a()
            r0.putString(r6, r7)
        L_0x01cf:
            java.util.Map r0 = r5.b()
            if (r0 != 0) goto L_0x01d7
            r0 = r10
            goto L_0x01e3
        L_0x01d7:
            java.util.Map r0 = r5.b()
            java.lang.String r6 = "score_info"
            java.lang.Object r0 = r0.get(r6)
            java.lang.String r0 = (java.lang.String) r0
        L_0x01e3:
            boolean r6 = android.text.TextUtils.isEmpty(r0)
            if (r6 != 0) goto L_0x01f0
            android.os.Bundle r6 = r8.extras
            java.lang.String r7 = "score_info"
            r6.putString(r7, r0)
        L_0x01f0:
            r0 = -1
            boolean r6 = c(r20)
            if (r6 == 0) goto L_0x01fa
            r0 = 1000(0x3e8, float:1.401E-42)
            goto L_0x0202
        L_0x01fa:
            boolean r6 = a((com.xiaomi.push.ic) r20)
            if (r6 == 0) goto L_0x0202
            r0 = 3000(0xbb8, float:4.204E-42)
        L_0x0202:
            android.os.Bundle r6 = r8.extras
            java.lang.String r7 = "eventMessageType"
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r6.putString(r7, r0)
            android.os.Bundle r0 = r8.extras
            java.lang.String r6 = "target_package"
            java.lang.String r7 = a((com.xiaomi.push.ic) r20)
            r0.putString(r6, r7)
        L_0x0218:
            java.util.Map r0 = r5.a()
            if (r0 != 0) goto L_0x021f
            goto L_0x022c
        L_0x021f:
            java.util.Map r0 = r5.a()
            java.lang.String r6 = "message_count"
            java.lang.Object r0 = r0.get(r6)
            r10 = r0
            java.lang.String r10 = (java.lang.String) r10
        L_0x022c:
            boolean r0 = com.xiaomi.push.l.a()
            if (r0 == 0) goto L_0x0259
            if (r10 == 0) goto L_0x0259
            int r0 = java.lang.Integer.parseInt(r10)     // Catch:{ NumberFormatException -> 0x023c }
            a((android.app.Notification) r8, (int) r0)     // Catch:{ NumberFormatException -> 0x023c }
            goto L_0x0259
        L_0x023c:
            r0 = move-exception
            if (r5 == 0) goto L_0x0256
            android.content.Context r6 = r19.getApplicationContext()
            com.xiaomi.push.ev r6 = com.xiaomi.push.ev.a((android.content.Context) r6)
            java.lang.String r7 = r20.b()
            java.lang.String r9 = b((com.xiaomi.push.ic) r20)
            java.lang.String r10 = r5.a()
            r6.a((java.lang.String) r7, (java.lang.String) r9, (java.lang.String) r10, (java.lang.Throwable) r0)
        L_0x0256:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x0259:
            boolean r0 = com.xiaomi.push.l.c()
            if (r0 != 0) goto L_0x0272
            java.lang.String r0 = "com.xiaomi.xmsf"
            java.lang.String r6 = r19.getPackageName()
            boolean r0 = r0.equals(r6)
            if (r0 == 0) goto L_0x0272
            java.lang.String r0 = a((com.xiaomi.push.ic) r20)
            a((android.app.Notification) r8, (java.lang.String) r0)
        L_0x0272:
            int r0 = r5.c()
            java.lang.String r6 = a((com.xiaomi.push.ic) r20)
            int r6 = r6.hashCode()
            int r6 = r6 / 10
            int r6 = r6 * 10
            int r6 = r6 + r0
            r4.notify(r6, r8)
            boolean r0 = com.xiaomi.push.l.a()
            if (r0 == 0) goto L_0x029f
            java.lang.String r0 = "com.xiaomi.xmsf"
            java.lang.String r7 = r19.getPackageName()
            boolean r0 = r0.equals(r7)
            if (r0 == 0) goto L_0x029f
            java.lang.String r0 = a((com.xiaomi.push.ic) r20)
            a((android.content.Context) r1, (android.app.Notification) r8, (int) r6, (java.lang.String) r0)
        L_0x029f:
            boolean r0 = a((com.xiaomi.push.ic) r20)
            if (r0 == 0) goto L_0x02c0
            android.content.Context r0 = r19.getApplicationContext()
            com.xiaomi.push.ev r7 = com.xiaomi.push.ev.a((android.content.Context) r0)
            java.lang.String r8 = r20.b()
            java.lang.String r9 = b((com.xiaomi.push.ic) r20)
            java.lang.String r10 = r5.a()
            r11 = 3002(0xbba, float:4.207E-42)
            java.lang.String r12 = "try show business message"
            r7.a(r8, r9, r10, r11, r12)
        L_0x02c0:
            boolean r0 = c(r20)
            if (r0 == 0) goto L_0x02e1
            android.content.Context r0 = r19.getApplicationContext()
            com.xiaomi.push.ev r7 = com.xiaomi.push.ev.a((android.content.Context) r0)
            java.lang.String r8 = r20.b()
            java.lang.String r9 = b((com.xiaomi.push.ic) r20)
            java.lang.String r10 = r5.a()
            r11 = 1002(0x3ea, float:1.404E-42)
            java.lang.String r12 = "try show notification message"
            r7.a(r8, r9, r10, r11, r12)
        L_0x02e1:
            int r0 = android.os.Build.VERSION.SDK_INT
            r7 = 26
            if (r0 >= r7) goto L_0x0300
            com.xiaomi.push.ai r0 = com.xiaomi.push.ai.a((android.content.Context) r19)
            r0.a((int) r6)
            java.util.Map r1 = r5.a()
            int r1 = a((java.util.Map<java.lang.String, java.lang.String>) r1)
            if (r1 <= 0) goto L_0x0300
            com.xiaomi.push.service.aa r5 = new com.xiaomi.push.service.aa
            r5.<init>(r6, r4)
            r0.b(r5, r1)
        L_0x0300:
            android.util.Pair r0 = new android.util.Pair
            java.lang.Integer r1 = java.lang.Integer.valueOf(r6)
            r0.<init>(r1, r2)
            java.util.LinkedList<android.util.Pair<java.lang.Integer, com.xiaomi.push.ic>> r1 = f933a
            monitor-enter(r1)
            java.util.LinkedList<android.util.Pair<java.lang.Integer, com.xiaomi.push.ic>> r2 = f933a     // Catch:{ all -> 0x0322 }
            r2.add(r0)     // Catch:{ all -> 0x0322 }
            java.util.LinkedList<android.util.Pair<java.lang.Integer, com.xiaomi.push.ic>> r0 = f933a     // Catch:{ all -> 0x0322 }
            int r0 = r0.size()     // Catch:{ all -> 0x0322 }
            r2 = 100
            if (r0 <= r2) goto L_0x0320
            java.util.LinkedList<android.util.Pair<java.lang.Integer, com.xiaomi.push.ic>> r0 = f933a     // Catch:{ all -> 0x0322 }
            r0.remove()     // Catch:{ all -> 0x0322 }
        L_0x0320:
            monitor-exit(r1)     // Catch:{ all -> 0x0322 }
            return r3
        L_0x0322:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0322 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.z.m619a(android.content.Context, com.xiaomi.push.ic, byte[]):com.xiaomi.push.service.z$c");
    }

    private static d a(NotificationManager notificationManager, String str, String str2) {
        StatusBarNotification[] activeNotifications;
        Object a2;
        d dVar = new d();
        if (notificationManager != null && Build.VERSION.SDK_INT >= 23 && !TextUtils.isEmpty(str) && (activeNotifications = notificationManager.getActiveNotifications()) != null && activeNotifications.length > 0) {
            for (StatusBarNotification statusBarNotification : activeNotifications) {
                if (statusBarNotification != null) {
                    String packageName = statusBarNotification.getPackageName();
                    String str3 = "";
                    if (statusBarNotification.getNotification() != null) {
                        str3 = statusBarNotification.getNotification().getGroup();
                    }
                    if (!TextUtils.isEmpty(str3)) {
                        String str4 = packageName + str3;
                        HashSet hashSet = null;
                        if (!str2.equals(str4) || (a2 = at.a((Object) statusBarNotification.getNotification(), "isGroupSummary", (Object[]) null)) == null || !(a2 instanceof Boolean) || !Boolean.class.cast(a2).booleanValue()) {
                            dVar.a.put(str4, Integer.valueOf((dVar.a.containsKey(str4) ? dVar.a.get(str4).intValue() : 0) + 1));
                            if (dVar.b.containsKey(str4)) {
                                hashSet = dVar.b.get(str4);
                            }
                            if (hashSet == null) {
                                hashSet = new HashSet();
                                dVar.b.put(str4, hashSet);
                            }
                            hashSet.add(Integer.valueOf(statusBarNotification.getId()));
                        } else {
                            dVar.f939a = true;
                        }
                    }
                }
            }
        }
        return dVar;
    }

    private static String a(Context context, String str, Map<String, String> map) {
        return (map == null || TextUtils.isEmpty(map.get("channel_name"))) ? g.b(context, str) : map.get("channel_name");
    }

    static String a(ic icVar) {
        ht a2;
        if (!(!"com.xiaomi.xmsf".equals(icVar.f613b) || (a2 = icVar.a()) == null || a2.a() == null)) {
            String str = (String) a2.a().get("miui_package_name");
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return icVar.f613b;
    }

    private static void a(Notification notification, int i) {
        Object a2 = at.a((Object) notification, "extraNotification");
        if (a2 != null) {
            at.a(a2, "setMessageCount", Integer.valueOf(i));
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private static void m620a(NotificationManager notificationManager, String str, String str2) {
        int a2 = a(str, str2);
        com.xiaomi.channel.commonutils.logger.b.b("GROUPSUMMARY  : cancelGroupSummarytargetPackageName = " + str + " id = " + a2);
        notificationManager.cancel(a2);
    }

    @TargetApi(23)
    private static void a(Context context, Notification notification, int i, String str) {
        HashSet hashSet;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        String str2 = context.getPackageName() + notification.getGroup();
        d a2 = a(notificationManager, notification.getGroup(), str2);
        boolean z = false;
        int intValue = a2.a.containsKey(str2) ? a2.a.get(str2).intValue() : 0;
        if (a2.b.containsKey(str2) && (hashSet = a2.b.get(str2)) != null && hashSet.contains(Integer.valueOf(i))) {
            z = true;
        }
        if (intValue > 1 || (intValue == 1 && !z)) {
            if (!a2.f939a) {
                a(context, str, notification, notificationManager);
            }
        } else if (intValue < 1 && a2.f939a) {
            a(notificationManager, str, notification.getGroup());
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m621a(Context context, String str) {
        a(context, str, -1);
    }

    public static void a(Context context, String str, int i) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        int hashCode = ((str.hashCode() / 10) * 10) + i;
        LinkedList linkedList = new LinkedList();
        if (i >= 0) {
            notificationManager.cancel(hashCode);
        }
        synchronized (f933a) {
            Iterator it = f933a.iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                ic icVar = (ic) pair.second;
                if (icVar != null) {
                    String a2 = a(icVar);
                    if (i >= 0) {
                        if (hashCode == ((Integer) pair.first).intValue()) {
                            if (!TextUtils.equals(a2, str)) {
                            }
                        }
                    } else if (i == -1 && TextUtils.equals(a2, str)) {
                        notificationManager.cancel(((Integer) pair.first).intValue());
                    }
                    linkedList.add(pair);
                }
            }
            if (f933a != null) {
                f933a.removeAll(linkedList);
                a(context, (LinkedList<? extends Object>) linkedList);
            }
        }
    }

    @TargetApi(23)
    private static void a(Context context, String str, Notification notification, NotificationManager notificationManager) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                String group = notification.getGroup();
                Notification notification2 = null;
                if (Build.VERSION.SDK_INT >= 26) {
                    if (at.a((Object) notificationManager, "getNotificationChannel", "groupSummary") == null) {
                        at.a((Object) notificationManager, "createNotificationChannel", Class.forName("android.app.NotificationChannel").getConstructor(new Class[]{String.class, CharSequence.class, Integer.TYPE}).newInstance(new Object[]{"groupSummary", "group_summary", 3}));
                    }
                    if (!TextUtils.isEmpty(group)) {
                        Notification.Builder builder = new Notification.Builder(context);
                        builder.setContentTitle("GroupSummary").setContentText("GroupSummary").setLargeIcon(notification.getLargeIcon()).setSmallIcon(notification.getSmallIcon()).setAutoCancel(true).setGroup(group).setGroupSummary(true);
                        at.a((Object) builder, "setChannelId", "groupSummary");
                        notification2 = builder.build();
                    }
                } else if (!TextUtils.isEmpty(group)) {
                    Notification.Builder builder2 = new Notification.Builder(context);
                    builder2.setContentTitle("GroupSummary").setContentText("GroupSummary").setLargeIcon(notification.getLargeIcon()).setSmallIcon(notification.getSmallIcon()).setPriority(0).setDefaults(-1).setAutoCancel(true).setGroup(group).setGroupSummary(true);
                    notification2 = builder2.build();
                }
                if (notification2 != null && !l.c() && "com.xiaomi.xmsf".equals(context.getPackageName())) {
                    a(notification2, str);
                    if (notification2.extras != null) {
                        notification2.extras.putString("target_package", str);
                    }
                }
                int a2 = a(str, group);
                if (notification2 != null) {
                    notificationManager.notify(a2, notification2);
                    com.xiaomi.channel.commonutils.logger.b.b("GROUPSUMMARY  : showGroupSummary targetPackageName = " + str + " id = " + a2);
                }
            }
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
        }
    }

    public static void a(Context context, String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            LinkedList linkedList = new LinkedList();
            synchronized (f933a) {
                Iterator it = f933a.iterator();
                while (it.hasNext()) {
                    Pair pair = (Pair) it.next();
                    ic icVar = (ic) pair.second;
                    if (icVar != null) {
                        String a2 = a(icVar);
                        ht a3 = icVar.a();
                        if (a3 != null) {
                            if (TextUtils.equals(a2, str)) {
                                String c2 = a3.c();
                                String d2 = a3.d();
                                if (!TextUtils.isEmpty(c2) && !TextUtils.isEmpty(d2) && a(str2, c2) && a(str3, d2)) {
                                    notificationManager.cancel(((Integer) pair.first).intValue());
                                    linkedList.add(pair);
                                }
                            }
                        }
                    }
                }
                if (f933a != null) {
                    f933a.removeAll(linkedList);
                    a(context, (LinkedList<? extends Object>) linkedList);
                }
            }
        }
    }

    public static void a(Context context, LinkedList<? extends Object> linkedList) {
        if (linkedList != null && linkedList.size() > 0) {
            be.a(context, "category_clear_notification", "clear_notification", (long) linkedList.size(), "");
        }
    }

    private static void a(Object obj, Map<String, String> map) {
        if (map != null && !TextUtils.isEmpty(map.get("channel_description"))) {
            at.a(obj, "setDescription", map.get("channel_description"));
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m622a(Context context, String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.importance == 100 && Arrays.asList(next.pkgList).contains(str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean a(ht htVar) {
        if (htVar == null) {
            return false;
        }
        String a2 = htVar.a();
        return !TextUtils.isEmpty(a2) && a2.length() == 22 && "satuigm".indexOf(a2.charAt(0)) >= 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m623a(ic icVar) {
        ht a2 = icVar.a();
        return a(a2) && a2.l();
    }

    /* renamed from: a  reason: collision with other method in class */
    private static boolean m624a(String str, String str2) {
        return TextUtils.isEmpty(str) || str2.contains(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m625a(Map<String, String> map) {
        if (map == null || !map.containsKey("notify_foreground")) {
            return true;
        }
        return "1".equals(map.get("notify_foreground"));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0070, code lost:
        if (android.text.TextUtils.isEmpty(r3) == false) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x004e, code lost:
        if (android.text.TextUtils.isEmpty(r3) == false) goto L_0x0072;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String[] a(android.content.Context r3, com.xiaomi.push.ht r4) {
        /*
            java.lang.String r0 = r4.c()
            java.lang.String r1 = r4.d()
            java.util.Map r4 = r4.a()
            if (r4 == 0) goto L_0x0073
            android.content.res.Resources r2 = r3.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r2 = r2.widthPixels
            android.content.res.Resources r3 = r3.getResources()
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()
            float r3 = r3.density
            float r2 = (float) r2
            float r2 = r2 / r3
            r3 = 1056964608(0x3f000000, float:0.5)
            float r2 = r2 + r3
            java.lang.Float r3 = java.lang.Float.valueOf(r2)
            int r3 = r3.intValue()
            r2 = 320(0x140, float:4.48E-43)
            if (r3 > r2) goto L_0x0051
            java.lang.String r3 = "title_short"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L_0x0042
            r0 = r3
        L_0x0042:
            java.lang.String r3 = "description_short"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 != 0) goto L_0x0073
            goto L_0x0072
        L_0x0051:
            r2 = 360(0x168, float:5.04E-43)
            if (r3 <= r2) goto L_0x0073
            java.lang.String r3 = "title_long"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L_0x0064
            r0 = r3
        L_0x0064:
            java.lang.String r3 = "description_long"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 != 0) goto L_0x0073
        L_0x0072:
            r1 = r3
        L_0x0073:
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]
            r4 = 0
            r3[r4] = r0
            r4 = 1
            r3[r4] = r1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.z.a(android.content.Context, com.xiaomi.push.ht):java.lang.String[]");
    }

    private static int b(Context context, String str) {
        int a2 = a(context, str, "mipush_notification");
        int a3 = a(context, str, "mipush_small_notification");
        if (a2 <= 0) {
            a2 = a3 > 0 ? a3 : context.getApplicationInfo().icon;
        }
        return (a2 != 0 || Build.VERSION.SDK_INT < 9) ? a2 : context.getApplicationInfo().logo;
    }

    public static String b(ic icVar) {
        return a(icVar) ? "E100002" : c(icVar) ? "E100000" : b(icVar) ? "E100001" : d(icVar) ? "E100003" : "";
    }

    /* renamed from: b  reason: collision with other method in class */
    static void m626b(Context context, String str) {
        context.getSharedPreferences("pref_notify_type", 0).edit().remove(str).commit();
    }

    static void b(Context context, String str, int i) {
        context.getSharedPreferences("pref_notify_type", 0).edit().putInt(str, i).commit();
    }

    /* renamed from: b  reason: collision with other method in class */
    static boolean m627b(Context context, String str) {
        return context.getSharedPreferences("pref_notify_type", 0).contains(str);
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m628b(ic icVar) {
        ht a2 = icVar.a();
        return a(a2) && a2.f522b == 1 && !a(icVar);
    }

    public static boolean c(ic icVar) {
        ht a2 = icVar.a();
        return a(a2) && a2.f522b == 0 && !a(icVar);
    }

    public static boolean d(ic icVar) {
        return icVar.a() == hg.Registration;
    }

    public static boolean e(ic icVar) {
        return a(icVar) || c(icVar) || b(icVar);
    }
}
