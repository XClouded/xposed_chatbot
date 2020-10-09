package com.xiaomi.push.service;

import android.os.Process;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.cs;
import com.xiaomi.push.ed;
import com.xiaomi.push.gy;
import com.xiaomi.push.y;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ac {
    private static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static ThreadPoolExecutor f837a = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, new LinkedBlockingQueue());

    /* renamed from: a  reason: collision with other field name */
    private static final Pattern f838a = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");

    private static String a(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(str)));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append("\n");
                        sb.append(readLine);
                    } else {
                        String sb2 = sb.toString();
                        y.a((Closeable) bufferedReader);
                        return sb2;
                    }
                }
            } catch (Exception unused) {
                y.a((Closeable) bufferedReader);
                return null;
            } catch (Throwable th2) {
                th = th2;
                y.a((Closeable) bufferedReader);
                throw th;
            }
        } catch (Exception unused2) {
            bufferedReader = null;
            y.a((Closeable) bufferedReader);
            return null;
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
            y.a((Closeable) bufferedReader);
            throw th;
        }
    }

    public static void a() {
        ed.a a2;
        long currentTimeMillis = System.currentTimeMillis();
        if ((f837a.getActiveCount() <= 0 || currentTimeMillis - a >= ConfigStorage.DEFAULT_SMALL_MAX_AGE) && gy.a().a() && (a2 = ba.a().a()) != null && a2.e() > 0) {
            a = currentTimeMillis;
            a(a2.a(), true);
        }
    }

    public static void a(List<String> list, boolean z) {
        f837a.execute(new ad(list, z));
    }

    public static void b() {
        String a2 = a("/proc/self/net/tcp");
        if (!TextUtils.isEmpty(a2)) {
            b.a("dump tcp for uid = " + Process.myUid());
            b.a(a2);
        }
        String a3 = a("/proc/self/net/tcp6");
        if (!TextUtils.isEmpty(a3)) {
            b.a("dump tcp6 for uid = " + Process.myUid());
            b.a(a3);
        }
    }

    private static boolean b(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            b.a("ConnectivityTest: begin to connect to " + str);
            Socket socket = new Socket();
            socket.connect(cs.a(str, 5222), 5000);
            socket.setTcpNoDelay(true);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            b.a("ConnectivityTest: connect to " + str + " in " + currentTimeMillis2);
            socket.close();
            return true;
        } catch (Throwable th) {
            b.d("ConnectivityTest: could not connect to:" + str + " exception: " + th.getClass().getSimpleName() + " description: " + th.getMessage());
            return false;
        }
    }
}
