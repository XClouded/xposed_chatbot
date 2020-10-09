package org.android.agoo.common;

import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;

public class RomUtil {
    private static final String RUNTIME_HUAWEI = "ro.build.version.emui";
    private static final String RUNTIME_MEIZU = "ro.build.display.id";
    private static final String RUNTIME_MIUI_NAME = "ro.miui.ui.version.name";
    private static final String RUNTIME_OPPO = "ro.build.version.opporom";
    private static final String RUNTIME_VIVO = "ro.vivo.os.build.display.id";
    private static final String TAG = "RomUtil";

    public static String[] getRomInfo() {
        String[] strArr = new String[2];
        String romProperty = getRomProperty(RUNTIME_MIUI_NAME);
        if (!TextUtils.isEmpty(romProperty)) {
            strArr[0] = "MIUI";
            strArr[1] = romProperty;
        } else {
            String romProperty2 = getRomProperty(RUNTIME_HUAWEI);
            if (!TextUtils.isEmpty(romProperty2)) {
                strArr = romProperty2.split("_");
            } else {
                String romProperty3 = getRomProperty(RUNTIME_OPPO);
                if (!TextUtils.isEmpty(romProperty3)) {
                    strArr[0] = "ColorOS";
                    strArr[1] = romProperty3;
                } else {
                    String romProperty4 = getRomProperty(RUNTIME_VIVO);
                    if (!TextUtils.isEmpty(romProperty4)) {
                        strArr = romProperty4.trim().split("_");
                    } else {
                        String romProperty5 = getRomProperty(RUNTIME_MEIZU);
                        if (romProperty5.contains("Flyme")) {
                            strArr = romProperty5.split(Operators.SPACE_STR);
                        }
                    }
                }
            }
        }
        if (TextUtils.isEmpty(strArr[0]) || TextUtils.isEmpty(strArr[1])) {
            return null;
        }
        return strArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0060 A[SYNTHETIC, Splitter:B:29:0x0060] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0072 A[SYNTHETIC, Splitter:B:38:0x0072] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getRomProperty(java.lang.String r7) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            r3.<init>()     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            java.lang.String r4 = "getprop "
            r3.append(r4)     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            r3.append(r7)     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            java.lang.String r7 = r3.toString()     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            java.lang.Process r7 = r2.exec(r7)     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x004a, all -> 0x0048 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x004a, all -> 0x0048 }
            java.io.InputStream r4 = r7.getInputStream()     // Catch:{ IOException -> 0x004a, all -> 0x0048 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x004a, all -> 0x0048 }
            r4 = 1024(0x400, float:1.435E-42)
            r2.<init>(r3, r4)     // Catch:{ IOException -> 0x004a, all -> 0x0048 }
            java.lang.String r1 = r2.readLine()     // Catch:{ IOException -> 0x0042, all -> 0x003f }
            r2.close()     // Catch:{ IOException -> 0x0034 }
            goto L_0x0038
        L_0x0034:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0038:
            if (r7 == 0) goto L_0x003d
            r7.destroy()
        L_0x003d:
            r0 = r1
            goto L_0x006d
        L_0x003f:
            r0 = move-exception
            r1 = r2
            goto L_0x0070
        L_0x0042:
            r1 = move-exception
            r6 = r2
            r2 = r7
            r7 = r1
            r1 = r6
            goto L_0x0054
        L_0x0048:
            r0 = move-exception
            goto L_0x0070
        L_0x004a:
            r2 = move-exception
            r6 = r2
            r2 = r7
            r7 = r6
            goto L_0x0054
        L_0x004f:
            r0 = move-exception
            r7 = r1
            goto L_0x0070
        L_0x0052:
            r7 = move-exception
            r2 = r1
        L_0x0054:
            java.lang.String r3 = "RomUtil"
            java.lang.String r4 = "getRomProperty"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x006e }
            com.taobao.accs.utl.ALog.w(r3, r4, r7, r5)     // Catch:{ all -> 0x006e }
            if (r1 == 0) goto L_0x0068
            r1.close()     // Catch:{ IOException -> 0x0064 }
            goto L_0x0068
        L_0x0064:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0068:
            if (r2 == 0) goto L_0x006d
            r2.destroy()
        L_0x006d:
            return r0
        L_0x006e:
            r0 = move-exception
            r7 = r2
        L_0x0070:
            if (r1 == 0) goto L_0x007a
            r1.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x007a
        L_0x0076:
            r1 = move-exception
            r1.printStackTrace()
        L_0x007a:
            if (r7 == 0) goto L_0x007f
            r7.destroy()
        L_0x007f:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.common.RomUtil.getRomProperty(java.lang.String):java.lang.String");
    }
}
