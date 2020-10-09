package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.g;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.internal.utility.s;
import java.io.File;
import java.util.Locale;

/* compiled from: U4Source */
public final class h {
    private static final int a = -1;

    private static String a(int i) {
        switch (i) {
            case 2:
                return MessageDigestAlgorithms.MD5;
            case 3:
                return "SHA1";
            case 4:
                return "SHA256";
            default:
                return "SHA1(default)";
        }
    }

    public static UCElapseTime a(Context context, Integer num, String str) {
        UCElapseTime uCElapseTime = new UCElapseTime();
        if ((num.intValue() & 1073741824) == 0 || !s.a(str, context)) {
            if (g.a(str, context, context, "com.UCMobile", new g.b("cd_cvsv"), (e.a) null)) {
                Log.d("FileVerifier", "组件校验 Dex Success [" + str + Operators.ARRAY_END_STR);
                s.a(str, context, true);
            } else {
                Log.d("FileVerifier", "组件校验 Dex Failed [" + str + Operators.ARRAY_END_STR);
                s.a(str, context, false);
                throw new UCSetupException(3005, String.format("[%s] verify failed", new Object[]{str}));
            }
        }
        return uCElapseTime;
    }

    public static void a(Context context, String str, String[][] strArr, Integer num) {
        if (context != null && !k.a(str) && strArr != null && strArr.length > 0 && num != null) {
            b.a(37);
            boolean z = true;
            boolean z2 = (num.intValue() & 1073741824) != 0;
            boolean z3 = (num.intValue() & 268435456) != 0;
            boolean z4 = (z2 || z3) && SDKFactory.a(context).exists();
            if ((!z2 && !z3) || !z4) {
                z = false;
            }
            int i = a;
            UCLogger.print(i, "newWebViewFlag : " + z4, new Throwable[0]);
            a(context, str, strArr, num, z);
            SDKFactory.b(context);
            b.a(221);
        }
    }

    public static void b(Context context, String str, String[][] strArr, Integer num) throws UCSetupException {
        if (context != null && !k.a(str) && strArr != null && strArr.length > 0 && num != null) {
            boolean z = false;
            boolean z2 = (num.intValue() & 1073741824) != 0;
            boolean z3 = (num.intValue() & UCCore.VERIFY_POLICY_PAK_QUICK) != 0;
            if (z2 || z3) {
                z = true;
            }
            a(context, str, strArr, num, z);
        }
    }

    private static void a(Context context, String str, String[][] strArr, Integer num, boolean z) throws UCSetupException {
        String str2;
        Context context2 = context;
        String[][] strArr2 = strArr;
        int i = 2;
        int i2 = strArr2[0].length > 3 ? 3 : 2;
        if ((num.intValue() & 1048576) != 0) {
            i2 = 2;
        } else if ((num.intValue() & 4194304) != 0 && strArr2[0].length > 4) {
            i2 = 4;
        }
        int length = strArr2.length;
        int i3 = 0;
        while (i3 < length) {
            String[] strArr3 = strArr2[i3];
            String str3 = strArr3[0];
            String str4 = strArr3[i2];
            File file = new File(str, str3);
            if (!z || !s.a(file.getAbsolutePath(), context2)) {
                long currentTimeMillis = System.currentTimeMillis();
                if (i2 == i) {
                    str2 = UCCyclone.hashFileContents(file, UCCyclone.MessageDigestType.MD5);
                } else if (i2 == 4) {
                    str2 = UCCyclone.hashFileContents(file, UCCyclone.MessageDigestType.SHA256);
                } else {
                    str2 = UCCyclone.hashFileContents(file, UCCyclone.MessageDigestType.SHA1);
                }
                try {
                    if (k.a(str4) || str4.equals(str2)) {
                        if (z) {
                            s.a(file.getAbsolutePath(), context2, true);
                        }
                        Log.d("FileVerifier", "组件校验(" + a(i2) + ") Pass:true [" + file.getAbsolutePath() + "] time[" + (System.currentTimeMillis() - currentTimeMillis) + "ms]");
                    } else {
                        Object[] objArr = new Object[4];
                        objArr[0] = file;
                        objArr[1] = i2 == i ? "md5" : "sha";
                        objArr[i] = str2;
                        objArr[3] = str4;
                        throw new UCSetupException(1011, String.format("file [%s] with [%s] [%s] mismatch to predefined [%s].", objArr));
                    }
                } catch (Throwable th) {
                    if (z) {
                        s.a(file.getAbsolutePath(), context2, false);
                    }
                    Log.d("FileVerifier", "组件校验(" + a(i2) + ") Pass:false [" + file.getAbsolutePath() + "] time[" + (System.currentTimeMillis() - currentTimeMillis) + "ms]");
                    throw th;
                }
            }
            UCLogger.print(a, String.format(Locale.CHINA, "Check file hash ok [%s].", new Object[]{file}), new Throwable[0]);
            i3++;
            i = 2;
        }
    }
}
