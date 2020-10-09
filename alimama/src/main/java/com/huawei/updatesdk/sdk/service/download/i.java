package com.huawei.updatesdk.sdk.service.download;

import com.alipay.literpc.android.phone.mrpc.core.Headers;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.ssl.HttpsURLConnection;

public class i {
    private static Object a(Object obj, String str) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Class cls = obj.getClass();
        while (cls != null) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (NoSuchFieldException unused) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    public static String a(HttpURLConnection httpURLConnection) {
        String str;
        StringBuilder sb;
        String str2;
        if (httpURLConnection == null) {
            return "";
        }
        try {
            if (httpURLConnection instanceof HttpsURLConnection) {
                return a((HttpURLConnection) a(httpURLConnection, "delegate"));
            }
            Object a = a(httpURLConnection, "httpEngine");
            if (a == null) {
                a.d("GetServerIpHelper", "HttpURLConnection not connected");
                return "";
            }
            Object a2 = a(a, Headers.CONN_DIRECTIVE);
            return a2 == null ? ((InetSocketAddress) a(a(a, "route"), "inetSocketAddress")).getAddress().getHostAddress() : ((Socket) a(a2, "socket")).getInetAddress().getHostAddress();
        } catch (NoSuchFieldException e) {
            str2 = "GetServerIpHelper";
            sb = new StringBuilder();
            sb.append("NoSuchFieldException:");
            str = e.getMessage();
            sb.append(str);
            a.d(str2, sb.toString());
            return "";
        } catch (IllegalArgumentException e2) {
            str2 = "GetServerIpHelper";
            sb = new StringBuilder();
            sb.append("IllegalArgumentException:");
            str = e2.getMessage();
            sb.append(str);
            a.d(str2, sb.toString());
            return "";
        } catch (Throwable th) {
            str2 = "GetServerIpHelper";
            sb = new StringBuilder();
            sb.append("Throwable:");
            str = th.getMessage();
            sb.append(str);
            a.d(str2, sb.toString());
            return "";
        }
    }
}
