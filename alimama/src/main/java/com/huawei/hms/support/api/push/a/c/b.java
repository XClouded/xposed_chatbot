package com.huawei.hms.support.api.push.a.c;

import android.R;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import com.huawei.hms.a.a;
import com.huawei.hms.support.log.a;

/* compiled from: NotificationIconUtil */
public class b {
    public static int a(Context context, String str, String str2, Object obj) {
        int i;
        int i2 = 0;
        try {
            String str3 = context.getPackageName() + ".R";
            a.a("PushSelfShowLog", "try to refrect " + str3 + " typeName is " + str2);
            Class[] classes = Class.forName(str3).getClasses();
            StringBuilder sb = new StringBuilder();
            sb.append("sonClassArr length ");
            sb.append(classes.length);
            a.a("PushSelfShowLog", sb.toString());
            Class cls = null;
            int i3 = 0;
            while (true) {
                if (i3 >= classes.length) {
                    break;
                }
                Class cls2 = classes[i3];
                String substring = cls2.getName().substring(str3.length() + 1);
                a.a("PushSelfShowLog", "sonTypeClass,query sonclass is " + substring + " sonClass.getName() is" + cls2.getName());
                if (str2.equals(substring)) {
                    cls = cls2;
                    break;
                }
                i3++;
            }
            if (cls != null) {
                i = cls.getField(str).getInt(obj);
                try {
                    a.a("PushSelfShowLog", "refect res id is " + i);
                } catch (ClassNotFoundException e) {
                    i2 = i;
                    e = e;
                } catch (NoSuchFieldException e2) {
                    i2 = i;
                    e = e2;
                    a.a("PushSelfShowLog", "NoSuchFieldException failed,", (Throwable) e);
                    return i2;
                } catch (IllegalAccessException e3) {
                    i2 = i;
                    e = e3;
                    a.a("PushSelfShowLog", "IllegalAccessException failed,", (Throwable) e);
                    return i2;
                } catch (IllegalArgumentException e4) {
                    i2 = i;
                    e = e4;
                    a.a("PushSelfShowLog", "IllegalArgumentException failed,", (Throwable) e);
                    return i2;
                } catch (IndexOutOfBoundsException e5) {
                    i2 = i;
                    e = e5;
                    a.a("PushSelfShowLog", "IndexOutOfBoundsException failed,", (Throwable) e);
                    return i2;
                }
            } else {
                a.a("PushSelfShowLog", "sonTypeClass is null");
                String str4 = context.getPackageName() + ".R$" + str2;
                a.a("PushSelfShowLog", "try to refrect 2 " + str4 + " typeName is " + str2);
                i = Class.forName(str4).getField(str).getInt(obj);
                a.a("PushSelfShowLog", " refect res id 2 is " + i);
            }
            return i;
        } catch (ClassNotFoundException e6) {
            e = e6;
            a.a("PushSelfShowLog", "ClassNotFound failed,", (Throwable) e);
            return i2;
        } catch (NoSuchFieldException e7) {
            e = e7;
            a.a("PushSelfShowLog", "NoSuchFieldException failed,", (Throwable) e);
            return i2;
        } catch (IllegalAccessException e8) {
            e = e8;
            a.a("PushSelfShowLog", "IllegalAccessException failed,", (Throwable) e);
            return i2;
        } catch (IllegalArgumentException e9) {
            e = e9;
            a.a("PushSelfShowLog", "IllegalArgumentException failed,", (Throwable) e);
            return i2;
        } catch (IndexOutOfBoundsException e10) {
            e = e10;
            a.a("PushSelfShowLog", "IndexOutOfBoundsException failed,", (Throwable) e);
            return i2;
        }
    }

    public static int a(Context context, com.huawei.hms.support.api.push.a.b.a aVar) {
        if (context == null || aVar == null) {
            a.b("PushSelfShowLog", "enter getSmallIconId, context or msg is null");
            return 0;
        }
        int i = context.getApplicationInfo().icon;
        if (i != 0) {
            return i;
        }
        int identifier = context.getResources().getIdentifier("btn_star_big_on", "drawable", "android");
        a.a("PushSelfShowLog", "icon is btn_star_big_on ");
        if (identifier != 0) {
            return identifier;
        }
        a.a("PushSelfShowLog", "icon is sym_def_app_icon ");
        return 17301651;
    }

    public static Bitmap b(Context context, com.huawei.hms.support.api.push.a.b.a aVar) {
        Bitmap bitmap;
        Bitmap bitmap2 = null;
        if (context == null || aVar == null) {
            return null;
        }
        try {
            if (aVar.m() != null && aVar.m().length() > 0) {
                int i = 0;
                String m = aVar.m();
                if (!m.equals("" + aVar.a())) {
                    i = a(context, aVar.m(), "drawable", new R.drawable());
                    if (i == 0) {
                        i = context.getResources().getIdentifier(aVar.m(), "drawable", "android");
                    }
                    a.a("PushSelfShowLog", "msg.notifyIcon is " + aVar.m() + ",and defaultIcon is " + i);
                }
                if (i != 0) {
                    bitmap2 = BitmapFactory.decodeResource(context.getResources(), i);
                }
            }
            if (a.C0009a.a >= 11) {
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "huawei phone, and emui5.0, need not show large icon.");
                return bitmap2;
            }
            if (bitmap2 != null || "com.huawei.android.pushagent".equals(aVar.i())) {
                bitmap = bitmap2;
            } else {
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "get left bitmap from " + aVar.i());
                bitmap = ((BitmapDrawable) context.getPackageManager().getApplicationIcon(aVar.i())).getBitmap();
            }
            return bitmap;
        } catch (PackageManager.NameNotFoundException e) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "" + e.toString(), (Throwable) e);
            return null;
        } catch (Exception e2) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "" + e2.toString(), (Throwable) e2);
            return null;
        }
    }

    @TargetApi(23)
    public static Icon c(Context context, com.huawei.hms.support.api.push.a.b.a aVar) {
        if (context == null || aVar == null) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "getSmallIcon, context is null");
            return null;
        } else if (Build.VERSION.SDK_INT < 23) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "getSmallIcon failed, Build.VERSION less than 23");
            return null;
        } else {
            try {
                return Icon.createWithResource(aVar.i(), context.getPackageManager().getApplicationInfo(aVar.i(), 0).icon);
            } catch (PackageManager.NameNotFoundException e) {
                com.huawei.hms.support.log.a.d("PushSelfShowLog", e.toString());
                return null;
            } catch (Exception e2) {
                com.huawei.hms.support.log.a.a("PushSelfShowLog", e2.toString(), (Throwable) e2);
                return null;
            }
        }
    }

    private static boolean d(Context context, com.huawei.hms.support.api.push.a.b.a aVar) {
        if ("com.huawei.android.pushagent".equals(aVar.i()) || Build.VERSION.SDK_INT < 23) {
            return false;
        }
        return com.huawei.hms.support.api.push.a.d.a.b(context) || com.huawei.hms.support.api.push.a.d.a.c(context);
    }

    @TargetApi(23)
    public static void a(Context context, Notification.Builder builder, com.huawei.hms.support.api.push.a.b.a aVar) {
        if (context == null || builder == null || aVar == null) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "msg is null");
        } else if (d(context, aVar)) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "get small icon from " + aVar.i());
            Icon c = c(context, aVar);
            if (c != null) {
                builder.setSmallIcon(c);
            } else {
                builder.setSmallIcon(a(context, aVar));
            }
        } else {
            builder.setSmallIcon(a(context, aVar));
        }
    }
}
