package com.alibaba.wireless.security.open.avmpTest;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent;

public class AVMPUTTest {
    public static final String TAG = "AVMPUTTest_T";
    private static IAVMPGenericComponent a;
    private static IAVMPGenericComponent.IAVMPGenericInstance b;

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0057 A[Catch:{ SecException -> 0x00da, Exception -> 0x0092, all -> 0x00d6, all -> 0x008f }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0058 A[Catch:{ SecException -> 0x00da, Exception -> 0x0092, all -> 0x00d6, all -> 0x008f }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0131 A[Catch:{ SecException -> 0x00da, Exception -> 0x0092, all -> 0x00d6, all -> 0x008f }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0132 A[Catch:{ SecException -> 0x00da, Exception -> 0x0092, all -> 0x00d6, all -> 0x008f }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:45:0x0123=Splitter:B:45:0x0123, B:15:0x0049=Splitter:B:15:0x0049} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void a(android.content.Context r22) {
        /*
            java.lang.Class<com.alibaba.wireless.security.open.avmpTest.AVMPUTTest> r1 = com.alibaba.wireless.security.open.avmpTest.AVMPUTTest.class
            monitor-enter(r1)
            r3 = 0
            java.lang.String r10 = ""
            int r2 = isForeground(r22)     // Catch:{ all -> 0x0167 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0167 }
            r6 = 1
            r7 = 0
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r0 = b     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            if (r0 == 0) goto L_0x001a
            java.lang.String r0 = "AVMP instance has been initialized"
            a((java.lang.String) r0)     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            goto L_0x002c
        L_0x001a:
            com.alibaba.wireless.security.open.SecurityGuardManager r0 = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(r22)     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            java.lang.Class<com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent> r8 = com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent.class
            java.lang.Object r0 = r0.getInterface(r8)     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent r0 = (com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent) r0     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            a = r0     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent r0 = a     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            if (r0 != 0) goto L_0x002e
        L_0x002c:
            r0 = 0
            goto L_0x0049
        L_0x002e:
            com.alibaba.wireless.security.open.SecurityGuardManager r0 = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(r22)     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            java.lang.Class<com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent> r8 = com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent.class
            java.lang.Object r0 = r0.getInterface(r8)     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent r0 = (com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent) r0     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            a = r0     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent r0 = a     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            java.lang.String r8 = "mwua"
            java.lang.String r9 = "sgcipher"
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r0 = r0.createAVMPInstance(r8, r9)     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            b = r0     // Catch:{ SecException -> 0x00da, Exception -> 0x0092 }
            r0 = 1
        L_0x0049:
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0167 }
            r11 = 0
            long r8 = r8 - r4
            int r4 = isForeground(r22)     // Catch:{ all -> 0x0167 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r5 = b     // Catch:{ all -> 0x0167 }
            if (r5 == 0) goto L_0x0058
            goto L_0x0059
        L_0x0058:
            r6 = 0
        L_0x0059:
            java.lang.String r5 = "100086"
            r7 = 1
            java.lang.String r11 = ""
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x0167 }
            java.lang.String r12 = java.lang.String.valueOf(r6)     // Catch:{ all -> 0x0167 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0167 }
            r6.<init>()     // Catch:{ all -> 0x0167 }
            java.lang.String r13 = ""
            r6.append(r13)     // Catch:{ all -> 0x0167 }
            r6.append(r2)     // Catch:{ all -> 0x0167 }
            java.lang.String r2 = ""
            r6.append(r2)     // Catch:{ all -> 0x0167 }
            r6.append(r4)     // Catch:{ all -> 0x0167 }
            java.lang.String r13 = r6.toString()     // Catch:{ all -> 0x0167 }
            java.lang.String r14 = ""
            r2 = r5
            r4 = r7
            r5 = r11
            r6 = r8
            r8 = r10
            r9 = r0
            r10 = r12
            r11 = r13
            r12 = r14
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r2, r3, r4, r5, r6, r8, r9, r10, r11, r12)     // Catch:{ all -> 0x0167 }
            goto L_0x0121
        L_0x008f:
            r0 = move-exception
            goto L_0x0123
        L_0x0092:
            r0 = move-exception
            r12 = 1999(0x7cf, float:2.801E-42)
            java.lang.String r17 = r0.getMessage()     // Catch:{ all -> 0x00d6 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0167 }
            r0 = 0
            long r15 = r8 - r4
            int r0 = isForeground(r22)     // Catch:{ all -> 0x0167 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r3 = b     // Catch:{ all -> 0x0167 }
            if (r3 == 0) goto L_0x00a9
            goto L_0x00aa
        L_0x00a9:
            r6 = 0
        L_0x00aa:
            java.lang.String r11 = "100086"
            r13 = 1
            java.lang.String r14 = ""
            java.lang.String r18 = java.lang.String.valueOf(r7)     // Catch:{ all -> 0x0167 }
            java.lang.String r19 = java.lang.String.valueOf(r6)     // Catch:{ all -> 0x0167 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0167 }
            r3.<init>()     // Catch:{ all -> 0x0167 }
            java.lang.String r4 = ""
            r3.append(r4)     // Catch:{ all -> 0x0167 }
            r3.append(r2)     // Catch:{ all -> 0x0167 }
            java.lang.String r2 = ""
            r3.append(r2)     // Catch:{ all -> 0x0167 }
            r3.append(r0)     // Catch:{ all -> 0x0167 }
            java.lang.String r20 = r3.toString()     // Catch:{ all -> 0x0167 }
            java.lang.String r21 = ""
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r11, r12, r13, r14, r15, r17, r18, r19, r20, r21)     // Catch:{ all -> 0x0167 }
            goto L_0x0121
        L_0x00d6:
            r0 = move-exception
            r3 = 1999(0x7cf, float:2.801E-42)
            goto L_0x0123
        L_0x00da:
            r0 = move-exception
            int r0 = r0.getErrorCode()     // Catch:{ all -> 0x008f }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0167 }
            r3 = 0
            long r8 = r8 - r4
            int r3 = isForeground(r22)     // Catch:{ all -> 0x0167 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r4 = b     // Catch:{ all -> 0x0167 }
            if (r4 == 0) goto L_0x00ee
            goto L_0x00ef
        L_0x00ee:
            r6 = 0
        L_0x00ef:
            java.lang.String r4 = "100086"
            r11 = 1
            java.lang.String r12 = ""
            java.lang.String r13 = java.lang.String.valueOf(r7)     // Catch:{ all -> 0x0167 }
            java.lang.String r14 = java.lang.String.valueOf(r6)     // Catch:{ all -> 0x0167 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0167 }
            r5.<init>()     // Catch:{ all -> 0x0167 }
            java.lang.String r6 = ""
            r5.append(r6)     // Catch:{ all -> 0x0167 }
            r5.append(r2)     // Catch:{ all -> 0x0167 }
            java.lang.String r2 = ""
            r5.append(r2)     // Catch:{ all -> 0x0167 }
            r5.append(r3)     // Catch:{ all -> 0x0167 }
            java.lang.String r2 = r5.toString()     // Catch:{ all -> 0x0167 }
            java.lang.String r3 = ""
            r5 = r0
            r6 = r11
            r7 = r12
            r11 = r13
            r12 = r14
            r13 = r2
            r14 = r3
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r4, r5, r6, r7, r8, r10, r11, r12, r13, r14)     // Catch:{ all -> 0x0167 }
        L_0x0121:
            monitor-exit(r1)
            return
        L_0x0123:
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0167 }
            r11 = 0
            long r8 = r8 - r4
            int r4 = isForeground(r22)     // Catch:{ all -> 0x0167 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r5 = b     // Catch:{ all -> 0x0167 }
            if (r5 == 0) goto L_0x0132
            goto L_0x0133
        L_0x0132:
            r6 = 0
        L_0x0133:
            java.lang.String r5 = "100086"
            r11 = 1
            java.lang.String r12 = ""
            java.lang.String r13 = java.lang.String.valueOf(r7)     // Catch:{ all -> 0x0167 }
            java.lang.String r14 = java.lang.String.valueOf(r6)     // Catch:{ all -> 0x0167 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0167 }
            r6.<init>()     // Catch:{ all -> 0x0167 }
            java.lang.String r7 = ""
            r6.append(r7)     // Catch:{ all -> 0x0167 }
            r6.append(r2)     // Catch:{ all -> 0x0167 }
            java.lang.String r2 = ""
            r6.append(r2)     // Catch:{ all -> 0x0167 }
            r6.append(r4)     // Catch:{ all -> 0x0167 }
            java.lang.String r2 = r6.toString()     // Catch:{ all -> 0x0167 }
            java.lang.String r15 = ""
            r4 = r5
            r5 = r3
            r6 = r11
            r7 = r12
            r11 = r13
            r12 = r14
            r13 = r2
            r14 = r15
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r4, r5, r6, r7, r8, r10, r11, r12, r13, r14)     // Catch:{ all -> 0x0167 }
            throw r0     // Catch:{ all -> 0x0167 }
        L_0x0167:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.open.avmpTest.AVMPUTTest.a(android.content.Context):void");
    }

    private static void a(String str) {
        Log.d("AVMPUTTest", str);
    }

    public static byte[] avmpSign(IAVMPGenericComponent.IAVMPGenericInstance iAVMPGenericInstance, byte[] bArr, String str, int i) throws Exception {
        if (iAVMPGenericInstance == null) {
            return null;
        }
        return (byte[]) iAVMPGenericInstance.invokeAVMP("sign", new byte[0].getClass(), Integer.valueOf(i), str.getBytes(), Integer.valueOf(str.getBytes().length), null, bArr, 0);
    }

    public static byte[] avmpSign2(IAVMPGenericComponent.IAVMPGenericInstance iAVMPGenericInstance, String str, String str2) throws Exception {
        if (iAVMPGenericInstance == null) {
            return null;
        }
        return (byte[]) iAVMPGenericInstance.invokeAVMP2("sign_v2", new byte[0].getClass(), str2, str, 0);
    }

    public static void avmpTest(Context context, String str) {
        a("enter avmpTest");
        Context applicationContext = context.getApplicationContext();
        runAVMPSignSchedule(applicationContext, str + "|startLVMTrack");
    }

    public static int isForeground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (Build.VERSION.SDK_INT < 21) {
                return activityManager.getRunningTasks(1).get(0).topActivity.getPackageName().equals(context.getPackageName()) ? 1 : 0;
            }
            int i = 0;
            for (ActivityManager.RunningAppProcessInfo next : activityManager.getRunningAppProcesses()) {
                try {
                    if (next.importance == 100) {
                        String[] strArr = next.pkgList;
                        int length = strArr.length;
                        int i2 = i;
                        int i3 = 0;
                        while (i3 < length) {
                            try {
                                if (strArr[i3].equals(context.getPackageName())) {
                                    i2 = 1;
                                }
                                i3++;
                            } catch (Exception unused) {
                                return i2;
                            }
                        }
                        i = i2;
                    }
                } catch (Exception unused2) {
                }
            }
            return i;
        } catch (Exception unused3) {
            return 0;
        }
    }

    public static void mySleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Exception unused) {
        }
    }

    public static void runAVMPSignSchedule(Context context, String str) {
        for (int i = 0; i < 15; i++) {
            a("enter runAVMPSignSchedule");
            runAVMPSignSchedule1(context, str, "ib00000010b4732dc6645e87a20900b653a94ef27d72696f99", "ib0001002026f1091f2042df0cae1af323ac6e80a661d4a169");
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:106:0x0531=Splitter:B:106:0x0531, B:95:0x0484=Splitter:B:95:0x0484, B:45:0x01b5=Splitter:B:45:0x01b5, B:57:0x0279=Splitter:B:57:0x0279, B:84:0x03d9=Splitter:B:84:0x03d9, B:14:0x003d=Splitter:B:14:0x003d, B:30:0x00fe=Splitter:B:30:0x00fe, B:72:0x032e=Splitter:B:72:0x032e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void runAVMPSignSchedule1(android.content.Context r31, java.lang.String r32, java.lang.String r33, java.lang.String r34) {
        /*
            java.lang.Class<com.alibaba.wireless.security.open.avmpTest.AVMPUTTest> r1 = com.alibaba.wireless.security.open.avmpTest.AVMPUTTest.class
            monitor-enter(r1)
            a((android.content.Context) r31)     // Catch:{ all -> 0x05c4 }
            r2 = 3000(0xbb8, double:1.482E-320)
            mySleep(r2)     // Catch:{ all -> 0x05c4 }
            r5 = 0
            java.lang.String r4 = ""
            int r6 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r0 = 4
            r15 = 0
            r16 = 1
            r14 = 0
            byte[] r0 = new byte[r0]     // Catch:{ SecException -> 0x0197, Exception -> 0x00df, all -> 0x00d9 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r9 = b     // Catch:{ SecException -> 0x0197, Exception -> 0x00df, all -> 0x00d9 }
            java.lang.String r10 = "ib00000010b4732dc6645e87a20900b653a94ef27d72696f99"
            byte[] r0 = avmpSign(r9, r0, r10, r14)     // Catch:{ SecException -> 0x0197, Exception -> 0x00df, all -> 0x00d9 }
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r11 = 0
            long r9 = r9 - r7
            int r7 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x003b }
            r8.<init>(r0)     // Catch:{ Exception -> 0x003b }
            int r0 = r8.length()     // Catch:{ Exception -> 0x003b }
            r8 = r0
            r0 = 1
            goto L_0x003d
        L_0x003b:
            r0 = 0
            r8 = 0
        L_0x003d:
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r11.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r12 = "enter test1: isForeground1="
            r11.append(r12)     // Catch:{ all -> 0x05c4 }
            r11.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r12 = " isForeground11="
            r11.append(r12)     // Catch:{ all -> 0x05c4 }
            r11.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r12 = " msg"
            r11.append(r12)     // Catch:{ all -> 0x05c4 }
            r11.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r12 = " callRes1"
            r11.append(r12)     // Catch:{ all -> 0x05c4 }
            r11.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r12 = " signLength="
            r11.append(r12)     // Catch:{ all -> 0x05c4 }
            r11.append(r8)     // Catch:{ all -> 0x05c4 }
            java.lang.String r12 = " duration="
            r11.append(r12)     // Catch:{ all -> 0x05c4 }
            r11.append(r9)     // Catch:{ all -> 0x05c4 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r11)     // Catch:{ all -> 0x05c4 }
            java.lang.String r11 = "100087"
            r12 = 1
            java.lang.String r13 = ""
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r14.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = ""
            r14.append(r2)     // Catch:{ all -> 0x05c4 }
            r14.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = ""
            r14.append(r2)     // Catch:{ all -> 0x05c4 }
            r14.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = "&"
            r14.append(r2)     // Catch:{ all -> 0x05c4 }
            r14.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = r14.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = "1"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r4.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r6 = ""
            r4.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x05c4 }
            r4.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r4.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r6 = ""
            r4.append(r6)     // Catch:{ all -> 0x05c4 }
            r4.append(r8)     // Catch:{ all -> 0x05c4 }
            java.lang.String r14 = r4.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r20 = ""
            r4 = r11
            r6 = r12
            r7 = r13
            r8 = r9
            r10 = r2
            r11 = r3
            r12 = r0
            r13 = r14
            r2 = 0
            r14 = r20
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r4, r5, r6, r7, r8, r10, r11, r12, r13, r14)     // Catch:{ all -> 0x05c4 }
            goto L_0x0247
        L_0x00d9:
            r0 = move-exception
            r2 = 0
        L_0x00db:
            r18 = 0
            goto L_0x051a
        L_0x00df:
            r0 = move-exception
            r2 = 0
            r21 = 1999(0x7cf, float:2.801E-42)
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x0192 }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r5 = 0
            long r3 = r3 - r7
            int r5 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x00fc }
            r7.<init>(r15)     // Catch:{ Exception -> 0x00fc }
            int r14 = r7.length()     // Catch:{ Exception -> 0x00fc }
            r7 = 1
            goto L_0x00fe
        L_0x00fc:
            r7 = 0
            r14 = 0
        L_0x00fe:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r8.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = "enter test1: isForeground1="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " isForeground11="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " msg"
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " callRes1"
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " signLength="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " duration="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r8)     // Catch:{ all -> 0x05c4 }
            java.lang.String r20 = "100087"
            r22 = 1
            java.lang.String r23 = ""
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r8.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = ""
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r6 = ""
            r8.append(r6)     // Catch:{ all -> 0x05c4 }
            r8.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = "&"
            r8.append(r5)     // Catch:{ all -> 0x05c4 }
            r8.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = r8.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = "1"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = ""
            r0.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = java.lang.String.valueOf(r7)     // Catch:{ all -> 0x05c4 }
            r0.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r28 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = ""
            r0.append(r5)     // Catch:{ all -> 0x05c4 }
            r0.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r29 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r30 = ""
            r24 = r3
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r20, r21, r22, r23, r24, r26, r27, r28, r29, r30)     // Catch:{ all -> 0x05c4 }
            goto L_0x0247
        L_0x0192:
            r0 = move-exception
            r18 = 1999(0x7cf, float:2.801E-42)
            goto L_0x051a
        L_0x0197:
            r0 = move-exception
            r2 = 0
            int r21 = r0.getErrorCode()     // Catch:{ all -> 0x0517 }
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r0 = 0
            long r7 = r9 - r7
            int r0 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x01b3 }
            r3.<init>(r15)     // Catch:{ Exception -> 0x01b3 }
            int r14 = r3.length()     // Catch:{ Exception -> 0x01b3 }
            r3 = 1
            goto L_0x01b5
        L_0x01b3:
            r3 = 0
            r14 = 0
        L_0x01b5:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r5.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = "enter test1: isForeground1="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " isForeground11="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " msg"
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " callRes1"
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " signLength="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " duration="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r20 = "100087"
            r22 = 1
            java.lang.String r23 = ""
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r5.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = ""
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r6 = ""
            r5.append(r6)     // Catch:{ all -> 0x05c4 }
            r5.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r0 = "&"
            r5.append(r0)     // Catch:{ all -> 0x05c4 }
            r5.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = r5.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = "1"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r0.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x05c4 }
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r28 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = ""
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            r0.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r29 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r30 = ""
            r24 = r7
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r20, r21, r22, r23, r24, r26, r27, r28, r29, r30)     // Catch:{ all -> 0x05c4 }
        L_0x0247:
            r3 = 3000(0xbb8, double:1.482E-320)
            mySleep(r3)     // Catch:{ all -> 0x05c4 }
            r18 = 0
            java.lang.String r3 = ""
            int r4 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r0 = b     // Catch:{ SecException -> 0x03bd, Exception -> 0x0310 }
            java.lang.String r7 = "ib00000010b4732dc6645e87a20900b653a94ef27d72696f99"
            java.lang.String r8 = "vs_config_0"
            byte[] r0 = avmpSign2(r0, r7, r8)     // Catch:{ SecException -> 0x03bd, Exception -> 0x0310 }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r9 = 0
            long r5 = r7 - r5
            int r7 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x0278 }
            r8.<init>(r0)     // Catch:{ Exception -> 0x0278 }
            int r14 = r8.length()     // Catch:{ Exception -> 0x0278 }
            r2 = 1
            goto L_0x0279
        L_0x0278:
            r14 = 0
        L_0x0279:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = "enter test2: isForeground1="
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " isForeground11="
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " msg"
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " callRes1"
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " signLength="
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " dration="
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r17 = "100087"
            r19 = 1
            java.lang.String r20 = ""
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = ""
            r0.append(r8)     // Catch:{ all -> 0x05c4 }
            r0.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r0.append(r4)     // Catch:{ all -> 0x05c4 }
            r0.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = "&"
            r0.append(r4)     // Catch:{ all -> 0x05c4 }
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r23 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r24 = "3"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = ""
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x05c4 }
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r25 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = ""
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            r0.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = ""
        L_0x0306:
            r21 = r5
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r17, r18, r19, r20, r21, r23, r24, r25, r26, r27)     // Catch:{ all -> 0x05c4 }
            goto L_0x046b
        L_0x030d:
            r0 = move-exception
            goto L_0x046d
        L_0x0310:
            r0 = move-exception
            r18 = 1999(0x7cf, float:2.801E-42)
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x030d }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r3 = 0
            long r5 = r7 - r5
            int r3 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x032d }
            r7.<init>(r15)     // Catch:{ Exception -> 0x032d }
            int r14 = r7.length()     // Catch:{ Exception -> 0x032d }
            r2 = 1
            goto L_0x032e
        L_0x032d:
            r14 = 0
        L_0x032e:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r7.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = "enter test2: isForeground1="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " isForeground11="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " msg"
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " callRes1"
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " signLength="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " dration="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r17 = "100087"
            r19 = 1
            java.lang.String r20 = ""
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r7.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = ""
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r7.append(r4)     // Catch:{ all -> 0x05c4 }
            r7.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = "&"
            r7.append(r3)     // Catch:{ all -> 0x05c4 }
            r7.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r23 = r7.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r24 = "3"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = ""
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x05c4 }
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r25 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = ""
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            r0.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = ""
            goto L_0x0306
        L_0x03bd:
            r0 = move-exception
            int r20 = r0.getErrorCode()     // Catch:{ all -> 0x030d }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r0 = 0
            long r5 = r7 - r5
            int r0 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x03d8 }
            r7.<init>(r15)     // Catch:{ Exception -> 0x03d8 }
            int r14 = r7.length()     // Catch:{ Exception -> 0x03d8 }
            r2 = 1
            goto L_0x03d9
        L_0x03d8:
            r14 = 0
        L_0x03d9:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r7.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = "enter test2: isForeground1="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " isForeground11="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " msg"
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " callRes1"
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " signLength="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = " dration="
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r19 = "100087"
            r21 = 1
            java.lang.String r22 = ""
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r7.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = ""
            r7.append(r8)     // Catch:{ all -> 0x05c4 }
            r7.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r7.append(r4)     // Catch:{ all -> 0x05c4 }
            r7.append(r0)     // Catch:{ all -> 0x05c4 }
            java.lang.String r0 = "&"
            r7.append(r0)     // Catch:{ all -> 0x05c4 }
            r7.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r25 = r7.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = "3"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = ""
            r0.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x05c4 }
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r0.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = ""
            r0.append(r2)     // Catch:{ all -> 0x05c4 }
            r0.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r28 = r0.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r29 = ""
            r23 = r5
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r19, r20, r21, r22, r23, r25, r26, r27, r28, r29)     // Catch:{ all -> 0x05c4 }
        L_0x046b:
            monitor-exit(r1)
            return
        L_0x046d:
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r9 = 0
            long r5 = r7 - r5
            int r7 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = new java.lang.String     // Catch:{ Exception -> 0x0483 }
            r8.<init>(r15)     // Catch:{ Exception -> 0x0483 }
            int r14 = r8.length()     // Catch:{ Exception -> 0x0483 }
            r2 = 1
            goto L_0x0484
        L_0x0483:
            r14 = 0
        L_0x0484:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r8.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = "enter test2: isForeground1="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " isForeground11="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " msg"
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " callRes1"
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " signLength="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " dration="
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r8)     // Catch:{ all -> 0x05c4 }
            java.lang.String r17 = "100087"
            r19 = 1
            java.lang.String r20 = ""
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r8.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = ""
            r8.append(r9)     // Catch:{ all -> 0x05c4 }
            r8.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r8.append(r4)     // Catch:{ all -> 0x05c4 }
            r8.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = "&"
            r8.append(r4)     // Catch:{ all -> 0x05c4 }
            r8.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r23 = r8.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r24 = "3"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r3.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r3.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x05c4 }
            r3.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r25 = r3.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r2.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = ""
            r2.append(r3)     // Catch:{ all -> 0x05c4 }
            r2.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = r2.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = ""
            r21 = r5
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r17, r18, r19, r20, r21, r23, r24, r25, r26, r27)     // Catch:{ all -> 0x05c4 }
            throw r0     // Catch:{ all -> 0x05c4 }
        L_0x0517:
            r0 = move-exception
            goto L_0x00db
        L_0x051a:
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x05c4 }
            r3 = 0
            long r7 = r9 - r7
            int r3 = isForeground(r31)     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = new java.lang.String     // Catch:{ Exception -> 0x0530 }
            r5.<init>(r15)     // Catch:{ Exception -> 0x0530 }
            int r14 = r5.length()     // Catch:{ Exception -> 0x0530 }
            r2 = 1
            goto L_0x0531
        L_0x0530:
            r14 = 0
        L_0x0531:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r5.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = "enter test1: isForeground1="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " isForeground11="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " msg"
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " callRes1"
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " signLength="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = " duration="
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r7)     // Catch:{ all -> 0x05c4 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x05c4 }
            a((java.lang.String) r5)     // Catch:{ all -> 0x05c4 }
            java.lang.String r17 = "100087"
            r19 = 1
            java.lang.String r20 = ""
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r5.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r9 = ""
            r5.append(r9)     // Catch:{ all -> 0x05c4 }
            r5.append(r6)     // Catch:{ all -> 0x05c4 }
            java.lang.String r6 = ""
            r5.append(r6)     // Catch:{ all -> 0x05c4 }
            r5.append(r3)     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = "&"
            r5.append(r3)     // Catch:{ all -> 0x05c4 }
            r5.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r23 = r5.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r24 = "1"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r3.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r4 = ""
            r3.append(r4)     // Catch:{ all -> 0x05c4 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x05c4 }
            r3.append(r2)     // Catch:{ all -> 0x05c4 }
            java.lang.String r25 = r3.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c4 }
            r2.<init>()     // Catch:{ all -> 0x05c4 }
            java.lang.String r3 = ""
            r2.append(r3)     // Catch:{ all -> 0x05c4 }
            r2.append(r14)     // Catch:{ all -> 0x05c4 }
            java.lang.String r26 = r2.toString()     // Catch:{ all -> 0x05c4 }
            java.lang.String r27 = ""
            r21 = r7
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r17, r18, r19, r20, r21, r23, r24, r25, r26, r27)     // Catch:{ all -> 0x05c4 }
            throw r0     // Catch:{ all -> 0x05c4 }
        L_0x05c4:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.open.avmpTest.AVMPUTTest.runAVMPSignSchedule1(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void");
    }
}
