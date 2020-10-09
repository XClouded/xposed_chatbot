package com.xiaomi.clientreport.processor;

import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.clientreport.data.PerfClientReport;
import com.xiaomi.clientreport.data.a;
import com.xiaomi.push.y;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.HashMap;

public class e {
    private static PerfClientReport a(PerfClientReport perfClientReport, String str) {
        long[] a;
        if (perfClientReport == null || (a = a(str)) == null) {
            return null;
        }
        perfClientReport.perfCounts = a[0];
        perfClientReport.perfLatencies = a[1];
        return perfClientReport;
    }

    private static PerfClientReport a(String str) {
        PerfClientReport perfClientReport = null;
        try {
            String[] a = a(str);
            if (a == null || a.length < 4 || TextUtils.isEmpty(a[0]) || TextUtils.isEmpty(a[1]) || TextUtils.isEmpty(a[2]) || TextUtils.isEmpty(a[3])) {
                return null;
            }
            PerfClientReport blankInstance = PerfClientReport.getBlankInstance();
            try {
                blankInstance.production = Integer.parseInt(a[0]);
                blankInstance.clientInterfaceId = a[1];
                blankInstance.reportType = Integer.parseInt(a[2]);
                blankInstance.code = Integer.parseInt(a[3]);
                return blankInstance;
            } catch (Exception unused) {
                perfClientReport = blankInstance;
                b.c("parse per key error");
                return perfClientReport;
            }
        } catch (Exception unused2) {
            b.c("parse per key error");
            return perfClientReport;
        }
    }

    public static String a(PerfClientReport perfClientReport) {
        return perfClientReport.production + "#" + perfClientReport.clientInterfaceId + "#" + perfClientReport.reportType + "#" + perfClientReport.code;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static HashMap<String, String> m24a(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (TextUtils.isEmpty(str) || !new File(str).exists()) {
            return hashMap;
        }
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(str));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String[] split = readLine.split("%%%");
                    if (split.length >= 2 && !TextUtils.isEmpty(split[0]) && !TextUtils.isEmpty(split[1])) {
                        hashMap.put(split[0], split[1]);
                    }
                } catch (Exception e) {
                    e = e;
                    bufferedReader = bufferedReader2;
                    try {
                        b.a((Throwable) e);
                        y.a((Closeable) bufferedReader);
                        return hashMap;
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader2 = bufferedReader;
                        y.a((Closeable) bufferedReader2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    y.a((Closeable) bufferedReader2);
                    throw th;
                }
            }
            y.a((Closeable) bufferedReader2);
        } catch (Exception e2) {
            e = e2;
            b.a((Throwable) e);
            y.a((Closeable) bufferedReader);
            return hashMap;
        }
        return hashMap;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.nio.channels.FileLock} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v11, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v12, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v13, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v16, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v18, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v20, resolved type: java.io.BufferedReader} */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r2v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v4, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: type inference failed for: r2v17 */
    /* JADX WARNING: type inference failed for: r2v19 */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00d3, code lost:
        if (r1 == null) goto L_0x00d8;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x00f4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<java.lang.String> a(android.content.Context r7, java.lang.String r8) {
        /*
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            if (r0 != 0) goto L_0x00f8
            java.io.File r0 = new java.io.File
            r0.<init>(r8)
            boolean r0 = r0.exists()
            if (r0 != 0) goto L_0x0018
            goto L_0x00f8
        L_0x0018:
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            r2.<init>()     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            r2.append(r8)     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            java.lang.String r3 = ".lock"
            r2.append(r3)     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x00b6, all -> 0x00b1 }
            com.xiaomi.push.y.a((java.io.File) r1)     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            java.lang.String r3 = "rw"
            r2.<init>(r1, r3)     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            java.nio.channels.FileChannel r3 = r2.getChannel()     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            java.nio.channels.FileLock r3 = r3.lock()     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
            java.io.FileReader r5 = new java.io.FileReader     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
            r5.<init>(r8)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
            r4.<init>(r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009f }
        L_0x004b:
            java.lang.String r8 = r4.readLine()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            if (r8 == 0) goto L_0x0084
            java.lang.String r0 = "%%%"
            java.lang.String[] r8 = r8.split(r0)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r0 = r8.length     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r5 = 2
            if (r0 < r5) goto L_0x004b
            r0 = 0
            r5 = r8[r0]     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            if (r5 != 0) goto L_0x004b
            r5 = 1
            r6 = r8[r5]     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            if (r6 != 0) goto L_0x004b
            r0 = r8[r0]     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            com.xiaomi.clientreport.data.PerfClientReport r0 = a((java.lang.String) r0)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r8 = r8[r5]     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            com.xiaomi.clientreport.data.PerfClientReport r8 = a((com.xiaomi.clientreport.data.PerfClientReport) r0, (java.lang.String) r8)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            if (r8 != 0) goto L_0x007c
            goto L_0x004b
        L_0x007c:
            java.lang.String r8 = r8.toJsonString()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r7.add(r8)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            goto L_0x004b
        L_0x0084:
            if (r3 == 0) goto L_0x0094
            boolean r8 = r3.isValid()
            if (r8 == 0) goto L_0x0094
            r3.release()     // Catch:{ IOException -> 0x0090 }
            goto L_0x0094
        L_0x0090:
            r8 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r8)
        L_0x0094:
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            goto L_0x00d5
        L_0x009b:
            r7 = move-exception
            goto L_0x00db
        L_0x009d:
            r8 = move-exception
            goto L_0x00a3
        L_0x009f:
            r7 = move-exception
            goto L_0x00dc
        L_0x00a1:
            r8 = move-exception
            r4 = r0
        L_0x00a3:
            r0 = r3
            goto L_0x00ba
        L_0x00a5:
            r7 = move-exception
            r3 = r0
            goto L_0x00dc
        L_0x00a8:
            r8 = move-exception
            r4 = r0
            goto L_0x00ba
        L_0x00ab:
            r7 = move-exception
            r2 = r0
            goto L_0x00b4
        L_0x00ae:
            r8 = move-exception
            r2 = r0
            goto L_0x00b9
        L_0x00b1:
            r7 = move-exception
            r1 = r0
            r2 = r1
        L_0x00b4:
            r3 = r2
            goto L_0x00dc
        L_0x00b6:
            r8 = move-exception
            r1 = r0
            r2 = r1
        L_0x00b9:
            r4 = r2
        L_0x00ba:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r8)     // Catch:{ all -> 0x00d9 }
            if (r0 == 0) goto L_0x00cd
            boolean r8 = r0.isValid()
            if (r8 == 0) goto L_0x00cd
            r0.release()     // Catch:{ IOException -> 0x00c9 }
            goto L_0x00cd
        L_0x00c9:
            r8 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r8)
        L_0x00cd:
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            com.xiaomi.push.y.a((java.io.Closeable) r4)
            if (r1 == 0) goto L_0x00d8
        L_0x00d5:
            r1.delete()
        L_0x00d8:
            return r7
        L_0x00d9:
            r7 = move-exception
            r3 = r0
        L_0x00db:
            r0 = r4
        L_0x00dc:
            if (r3 == 0) goto L_0x00ec
            boolean r8 = r3.isValid()
            if (r8 == 0) goto L_0x00ec
            r3.release()     // Catch:{ IOException -> 0x00e8 }
            goto L_0x00ec
        L_0x00e8:
            r8 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r8)
        L_0x00ec:
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            if (r1 == 0) goto L_0x00f7
            r1.delete()
        L_0x00f7:
            throw r7
        L_0x00f8:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.clientreport.processor.e.a(android.content.Context, java.lang.String):java.util.List");
    }

    private static void a(String str, HashMap<String, String> hashMap) {
        BufferedWriter bufferedWriter;
        Exception e;
        if (!TextUtils.isEmpty(str) && hashMap != null && hashMap.size() != 0) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(file));
                try {
                    for (String next : hashMap.keySet()) {
                        bufferedWriter.write(next + "%%%" + hashMap.get(next));
                        bufferedWriter.newLine();
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        b.a((Throwable) e);
                        y.a((Closeable) bufferedWriter);
                    } catch (Throwable th) {
                        th = th;
                        y.a((Closeable) bufferedWriter);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                bufferedWriter = null;
                e = e3;
                b.a((Throwable) e);
                y.a((Closeable) bufferedWriter);
            } catch (Throwable th2) {
                bufferedWriter = null;
                th = th2;
                y.a((Closeable) bufferedWriter);
                throw th;
            }
            y.a((Closeable) bufferedWriter);
        }
    }

    public static void a(String str, a[] aVarArr) {
        RandomAccessFile randomAccessFile;
        FileLock lock;
        if (aVarArr != null && aVarArr.length > 0 && !TextUtils.isEmpty(str)) {
            FileLock fileLock = null;
            try {
                File file = new File(str + ".lock");
                y.a(file);
                randomAccessFile = new RandomAccessFile(file, "rw");
                try {
                    lock = randomAccessFile.getChannel().lock();
                } catch (Throwable unused) {
                    try {
                        b.c("failed to write perf to file ");
                        try {
                            fileLock.release();
                        } catch (IOException e) {
                            e = e;
                        }
                        y.a((Closeable) randomAccessFile);
                    } catch (Throwable th) {
                        th = th;
                        try {
                            fileLock.release();
                        } catch (IOException e2) {
                            b.a((Throwable) e2);
                        }
                        y.a((Closeable) randomAccessFile);
                        throw th;
                    }
                }
                try {
                    HashMap a = a(str);
                    for (PerfClientReport perfClientReport : aVarArr) {
                        if (perfClientReport != null) {
                            String a2 = a(perfClientReport);
                            long j = perfClientReport.perfCounts;
                            long j2 = perfClientReport.perfLatencies;
                            if (!TextUtils.isEmpty(a2) && j > 0) {
                                if (j2 >= 0) {
                                    a(a, a2, j, j2);
                                }
                            }
                        }
                    }
                    a(str, (HashMap<String, String>) a);
                    if (lock != null && lock.isValid()) {
                        try {
                            lock.release();
                        } catch (IOException e3) {
                            e = e3;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileLock = lock;
                    fileLock.release();
                    y.a((Closeable) randomAccessFile);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                randomAccessFile = null;
                if (fileLock != null && fileLock.isValid()) {
                    fileLock.release();
                }
                y.a((Closeable) randomAccessFile);
                throw th;
            }
            y.a((Closeable) randomAccessFile);
        }
        return;
        b.a((Throwable) e);
        y.a((Closeable) randomAccessFile);
    }

    private static void a(HashMap<String, String> hashMap, String str, long j, long j2) {
        StringBuilder sb;
        String str2 = hashMap.get(str);
        if (TextUtils.isEmpty(str2)) {
            sb = new StringBuilder();
        } else {
            long[] a = a(str2);
            if (a == null || a[0] <= 0 || a[1] < 0) {
                sb = new StringBuilder();
            } else {
                j += a[0];
                j2 += a[1];
                sb = new StringBuilder();
            }
        }
        sb.append(j);
        sb.append("#");
        sb.append(j2);
        hashMap.put(str, sb.toString());
    }

    /* renamed from: a  reason: collision with other method in class */
    protected static long[] m25a(String str) {
        long[] jArr = new long[2];
        try {
            String[] split = str.split("#");
            if (split.length >= 2) {
                jArr[0] = Long.parseLong(split[0].trim());
                jArr[1] = Long.parseLong(split[1].trim());
            }
            return jArr;
        } catch (Exception e) {
            b.a((Throwable) e);
            return null;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private static String[] m26a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split("#");
    }
}
