package com.huawei.hianalytics.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public class e {

    private static class a extends Exception {
        a(String str) {
            super(str);
        }
    }

    private static Object a(Class cls, String str, Class[] clsArr, Object[] objArr) {
        String str2;
        String str3;
        if (cls != null) {
            if (clsArr == null) {
                if (objArr != null) {
                    throw new a("paramsType is null, but params is not null");
                }
            } else if (objArr == null) {
                throw new a("paramsType or params should be same");
            } else if (clsArr.length != objArr.length) {
                throw new a("paramsType len:" + clsArr.length + " should equal params.len:" + objArr.length);
            }
            try {
                try {
                    return cls.getMethod(str, clsArr).invoke((Object) null, objArr);
                } catch (IllegalAccessException unused) {
                    str2 = "globalStreamUtil";
                    str3 = "invokeStaticFun(): method invoke Exception!";
                    b.c(str2, str3);
                    return null;
                } catch (IllegalArgumentException unused2) {
                    str2 = "globalStreamUtil";
                    str3 = "invokeStaticFun(): Illegal Argument!";
                    b.c(str2, str3);
                    return null;
                } catch (InvocationTargetException unused3) {
                    str2 = "globalStreamUtil";
                    str3 = "invokeStaticFun(): Invocation Target Exception!";
                    b.c(str2, str3);
                    return null;
                }
            } catch (NoSuchMethodException unused4) {
                b.c("globalStreamUtil", "invokeStaticFun(): cls.getMethod(),No Such Method !");
            }
        } else {
            throw new a("class is null in invokeStaticFun");
        }
    }

    private static Object a(String str, String str2, Class[] clsArr, Object[] objArr) {
        String str3;
        String str4;
        try {
            return a((Class) Class.forName(str), str2, clsArr, objArr);
        } catch (ClassNotFoundException unused) {
            str4 = "globalStreamUtil";
            str3 = "invokeStaticFun() Not found class!";
            b.c(str4, str3);
            return null;
        } catch (a unused2) {
            str4 = "globalStreamUtil";
            str3 = "invokeStaticFun(): Static function call Exception ";
            b.c(str4, str3);
            return null;
        }
    }

    public static String a(InputStream inputStream, int i) {
        a aVar = new a(i);
        byte[] bArr = new byte[i];
        while (true) {
            int read = inputStream.read(bArr);
            if (((long) read) == -1) {
                break;
            }
            aVar.a(bArr, read);
        }
        return aVar.a() == 0 ? "" : new String(aVar.b(), "UTF-8");
    }

    public static String a(String str, String str2) {
        return b(str, str2);
    }

    private static String a(String str, String str2, String str3) {
        Object a2 = a(str, "get", new Class[]{String.class, String.class}, new Object[]{str2, str3});
        return a2 != null ? (String) a2 : str3;
    }

    public static void a(int i, Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
                b.c("globalStreamUtil", "closeQuietly(): Exception when closing the closeable!");
            }
        }
    }

    public static boolean a(Context context, String str) {
        if (context == null) {
            return true;
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (context.getPackageManager().checkPermission(str, context.getPackageName()) == 0) {
                return false;
            }
        } else if (context.checkSelfPermission(str) == 0) {
            return false;
        }
        b.c("HianalyticsSDK", "not have read phone permission!");
        return true;
    }

    public static String b(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        String a2 = a("android.os.SystemProperties", str, str2);
        return TextUtils.isEmpty(a2) ? a("com.huawei.android.os.SystemPropertiesEx", str, str2) : a2;
    }
}
