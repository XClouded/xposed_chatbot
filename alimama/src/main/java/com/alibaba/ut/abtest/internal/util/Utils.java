package com.alibaba.ut.abtest.internal.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.ABConstants;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Random;
import java.util.StringTokenizer;

public final class Utils {
    private static final String TAG = "Utils";
    private static final Random random = new Random();
    private static Application sApplication;

    private Utils() {
    }

    public static synchronized Application getApplication() {
        Application application;
        synchronized (Utils.class) {
            if (sApplication == null) {
                sApplication = getSystemApp();
            }
            application = sApplication;
        }
        return application;
    }

    private static Application getSystemApp() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field declaredField = cls.getDeclaredField("mInitialApplication");
            declaredField.setAccessible(true);
            return (Application) declaredField.get(declaredMethod.invoke((Object) null, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(String str, int i) {
        if (str == null || str.length() == 0) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static long toLong(String str) {
        return toLong(str, 0);
    }

    public static long toLong(String str, long j) {
        if (str == null) {
            return j;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    public static String getExperimentComponentKey(String str, String str2) {
        return str + "##" + str2;
    }

    public static void closeIO(Closeable... closeableArr) {
        if (closeableArr != null) {
            for (Closeable closeable : closeableArr) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        LogUtils.logE(TAG, e.getMessage(), e);
                    }
                }
            }
        }
    }

    public static String join(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objArr.length; i++) {
            if (i > 0 && str != null) {
                sb.append(str);
            }
            if (objArr[i] != null) {
                sb.append(objArr[i].toString());
            }
        }
        return sb.toString();
    }

    public static String join(Collection collection, String str) {
        if (collection == null) {
            return null;
        }
        return join(collection.toArray(), str);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null || checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE") == -1) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
        return false;
    }

    public static int checkSelfPermission(Context context, String str) {
        if (str != null) {
            try {
                return context.checkPermission(str, Process.myPid(), Process.myUid());
            } catch (RuntimeException unused) {
                return -1;
            }
        } else {
            throw new IllegalArgumentException("permission is null");
        }
    }

    public static String getUriKey(Uri uri) {
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme) || scheme.startsWith("http")) {
            scheme = "http";
        }
        return scheme + ":" + uri.getAuthority() + ":" + uri.getPath();
    }

    public static Uri getRedirectUrlOperatorAny(Uri uri, Uri uri2, Uri uri3) {
        String str;
        int lastIndexOf;
        try {
            String path = uri.getPath();
            if (TextUtils.equals(path, "/UTABTEST-ANY")) {
                str = uri2.toString().replace("/UTABTEST-ANY", uri3.getPath());
            } else {
                String path2 = uri3.getPath();
                int indexOf = path.indexOf(ABConstants.Operator.URI_ANY);
                String substring = indexOf > 1 ? path.substring(0, indexOf - 1) : null;
                String substring2 = ABConstants.Operator.URI_ANY.length() + indexOf < path.length() ? path.substring(indexOf + ABConstants.Operator.URI_ANY.length()) : null;
                if (!TextUtils.isEmpty(substring)) {
                    path2 = path2.replace(substring, "");
                }
                if (!TextUtils.isEmpty(substring2) && (lastIndexOf = path2.lastIndexOf(substring2)) != -1) {
                    path2 = path2.substring(0, lastIndexOf);
                }
                str = uri2.toString().replace("/UTABTEST-ANY", path2);
            }
            return Uri.parse(str);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
            return null;
        }
    }

    public static int nextRandomInt(int i) {
        try {
            return Math.abs(random.nextInt(i));
        } catch (Exception unused) {
            return i;
        }
    }

    public static long[] splitLongs(String str) throws IllegalArgumentException {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int countTokens = stringTokenizer.countTokens();
        long[] jArr = new long[countTokens];
        for (int i = 0; i < countTokens; i++) {
            jArr[i] = Long.parseLong(stringTokenizer.nextToken());
        }
        return jArr;
    }

    public static String[] split(String str, String str2, boolean z) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2);
        int countTokens = stringTokenizer.countTokens();
        String[] strArr = new String[countTokens];
        for (int i = 0; i < countTokens; i++) {
            if (z) {
                strArr[i] = stringTokenizer.nextToken().trim();
            } else {
                strArr[i] = stringTokenizer.nextToken();
            }
        }
        return strArr;
    }
}
