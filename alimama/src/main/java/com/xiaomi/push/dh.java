package com.xiaomi.push;

import android.content.Context;
import android.util.Pair;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.push.al;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class dh implements LoggerInterface {
    private static al a = new al(true);

    /* renamed from: a  reason: collision with other field name */
    public static String f231a = "/MiPushLog";

    /* renamed from: a  reason: collision with other field name */
    private static final SimpleDateFormat f232a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aaa");

    /* renamed from: a  reason: collision with other field name */
    private static List<Pair<String, Throwable>> f233a = Collections.synchronizedList(new ArrayList());

    /* renamed from: a  reason: collision with other field name */
    private Context f234a;
    private String b;
    private String c = "";

    public dh(Context context) {
        this.f234a = context;
        if (context.getApplicationContext() != null) {
            this.f234a = context.getApplicationContext();
        }
        this.b = this.f234a.getPackageName();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: java.nio.channels.FileLock} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: java.nio.channels.FileLock} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: java.io.RandomAccessFile} */
    /* JADX WARNING: type inference failed for: r0v0, types: [java.io.BufferedWriter, java.lang.String] */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r0v16 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x01b5 A[SYNTHETIC, Splitter:B:102:0x01b5] */
    /* JADX WARNING: Removed duplicated region for block: B:111:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0161 A[SYNTHETIC, Splitter:B:72:0x0161] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0183 A[SYNTHETIC, Splitter:B:84:0x0183] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0193 A[SYNTHETIC, Splitter:B:90:0x0193] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a() {
        /*
            r10 = this;
            r0 = 0
            java.lang.String r1 = r10.c     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r1 == 0) goto L_0x0028
            android.content.Context r1 = r10.f234a     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.io.File r1 = r1.getExternalFilesDir(r0)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r1 == 0) goto L_0x0028
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r2.<init>()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r2.append(r1)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r1 = ""
            r2.append(r1)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r10.c = r1     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
        L_0x0028:
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r2.<init>()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r3 = r10.c     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r2.append(r3)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r3 = f231a     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r2.append(r3)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r2 == 0) goto L_0x004c
            boolean r2 = r1.isDirectory()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r2 != 0) goto L_0x005a
        L_0x004c:
            boolean r2 = r1.mkdirs()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r2 != 0) goto L_0x005a
            java.lang.String r1 = r10.b     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r2 = "Create mipushlog directory fail."
            android.util.Log.w(r1, r2)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            return
        L_0x005a:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r3 = "log.lock"
            r2.<init>(r1, r3)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            boolean r3 = r2.exists()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r3 == 0) goto L_0x006d
            boolean r3 = r2.isDirectory()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            if (r3 == 0) goto L_0x0070
        L_0x006d:
            r2.createNewFile()     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
        L_0x0070:
            java.io.RandomAccessFile r3 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.lang.String r4 = "rw"
            r3.<init>(r2, r4)     // Catch:{ Exception -> 0x0155, all -> 0x0151 }
            java.nio.channels.FileChannel r2 = r3.getChannel()     // Catch:{ Exception -> 0x014e, all -> 0x014b }
            java.nio.channels.FileLock r2 = r2.lock()     // Catch:{ Exception -> 0x014e, all -> 0x014b }
            java.io.BufferedWriter r4 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x0149 }
            java.io.OutputStreamWriter r5 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x0149 }
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0149 }
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x0149 }
            java.lang.String r8 = "log1.txt"
            r7.<init>(r1, r8)     // Catch:{ Exception -> 0x0149 }
            r8 = 1
            r6.<init>(r7, r8)     // Catch:{ Exception -> 0x0149 }
            r5.<init>(r6)     // Catch:{ Exception -> 0x0149 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0149 }
        L_0x0096:
            java.util.List<android.util.Pair<java.lang.String, java.lang.Throwable>> r5 = f233a     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            boolean r5 = r5.isEmpty()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            if (r5 != 0) goto L_0x00ec
            java.util.List<android.util.Pair<java.lang.String, java.lang.Throwable>> r5 = f233a     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r6 = 0
            java.lang.Object r5 = r5.remove(r6)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            android.util.Pair r5 = (android.util.Pair) r5     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.Object r6 = r5.first     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.Object r7 = r5.second     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            if (r7 == 0) goto L_0x00d7
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r7.<init>()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r7.append(r6)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r6 = "\n"
            r7.append(r6)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r6 = r7.toString()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r7.<init>()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r7.append(r6)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.Object r5 = r5.second     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.Throwable r5 = (java.lang.Throwable) r5     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r5 = android.util.Log.getStackTraceString(r5)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r7.append(r5)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r6 = r7.toString()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
        L_0x00d7:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r5.<init>()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r5.append(r6)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r6 = "\n"
            r5.append(r6)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r4.write(r5)     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            goto L_0x0096
        L_0x00ec:
            r4.flush()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            r4.close()     // Catch:{ Exception -> 0x0146, all -> 0x0143 }
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x0149 }
            java.lang.String r5 = "log1.txt"
            r4.<init>(r1, r5)     // Catch:{ Exception -> 0x0149 }
            long r5 = r4.length()     // Catch:{ Exception -> 0x0149 }
            r7 = 1048576(0x100000, double:5.180654E-318)
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 < 0) goto L_0x011d
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0149 }
            java.lang.String r6 = "log0.txt"
            r5.<init>(r1, r6)     // Catch:{ Exception -> 0x0149 }
            boolean r1 = r5.exists()     // Catch:{ Exception -> 0x0149 }
            if (r1 == 0) goto L_0x011a
            boolean r1 = r5.isFile()     // Catch:{ Exception -> 0x0149 }
            if (r1 == 0) goto L_0x011a
            r5.delete()     // Catch:{ Exception -> 0x0149 }
        L_0x011a:
            r4.renameTo(r5)     // Catch:{ Exception -> 0x0149 }
        L_0x011d:
            if (r0 == 0) goto L_0x012b
            r0.close()     // Catch:{ IOException -> 0x0123 }
            goto L_0x012b
        L_0x0123:
            r0 = move-exception
            java.lang.String r1 = r10.b
            java.lang.String r4 = ""
            android.util.Log.e(r1, r4, r0)
        L_0x012b:
            if (r2 == 0) goto L_0x013f
            boolean r0 = r2.isValid()
            if (r0 == 0) goto L_0x013f
            r2.release()     // Catch:{ IOException -> 0x0137 }
            goto L_0x013f
        L_0x0137:
            r0 = move-exception
            java.lang.String r1 = r10.b
            java.lang.String r2 = ""
            android.util.Log.e(r1, r2, r0)
        L_0x013f:
            r3.close()     // Catch:{ IOException -> 0x0187 }
            goto L_0x018f
        L_0x0143:
            r1 = move-exception
            r0 = r4
            goto L_0x0191
        L_0x0146:
            r1 = move-exception
            r0 = r4
            goto L_0x0158
        L_0x0149:
            r1 = move-exception
            goto L_0x0158
        L_0x014b:
            r1 = move-exception
            r2 = r0
            goto L_0x0191
        L_0x014e:
            r1 = move-exception
            r2 = r0
            goto L_0x0158
        L_0x0151:
            r1 = move-exception
            r2 = r0
            r3 = r2
            goto L_0x0191
        L_0x0155:
            r1 = move-exception
            r2 = r0
            r3 = r2
        L_0x0158:
            java.lang.String r4 = r10.b     // Catch:{ all -> 0x0190 }
            java.lang.String r5 = ""
            android.util.Log.e(r4, r5, r1)     // Catch:{ all -> 0x0190 }
            if (r0 == 0) goto L_0x016d
            r0.close()     // Catch:{ IOException -> 0x0165 }
            goto L_0x016d
        L_0x0165:
            r0 = move-exception
            java.lang.String r1 = r10.b
            java.lang.String r4 = ""
            android.util.Log.e(r1, r4, r0)
        L_0x016d:
            if (r2 == 0) goto L_0x0181
            boolean r0 = r2.isValid()
            if (r0 == 0) goto L_0x0181
            r2.release()     // Catch:{ IOException -> 0x0179 }
            goto L_0x0181
        L_0x0179:
            r0 = move-exception
            java.lang.String r1 = r10.b
            java.lang.String r2 = ""
            android.util.Log.e(r1, r2, r0)
        L_0x0181:
            if (r3 == 0) goto L_0x018f
            r3.close()     // Catch:{ IOException -> 0x0187 }
            goto L_0x018f
        L_0x0187:
            r0 = move-exception
            java.lang.String r1 = r10.b
            java.lang.String r2 = ""
            android.util.Log.e(r1, r2, r0)
        L_0x018f:
            return
        L_0x0190:
            r1 = move-exception
        L_0x0191:
            if (r0 == 0) goto L_0x019f
            r0.close()     // Catch:{ IOException -> 0x0197 }
            goto L_0x019f
        L_0x0197:
            r0 = move-exception
            java.lang.String r4 = r10.b
            java.lang.String r5 = ""
            android.util.Log.e(r4, r5, r0)
        L_0x019f:
            if (r2 == 0) goto L_0x01b3
            boolean r0 = r2.isValid()
            if (r0 == 0) goto L_0x01b3
            r2.release()     // Catch:{ IOException -> 0x01ab }
            goto L_0x01b3
        L_0x01ab:
            r0 = move-exception
            java.lang.String r2 = r10.b
            java.lang.String r4 = ""
            android.util.Log.e(r2, r4, r0)
        L_0x01b3:
            if (r3 == 0) goto L_0x01c1
            r3.close()     // Catch:{ IOException -> 0x01b9 }
            goto L_0x01c1
        L_0x01b9:
            r0 = move-exception
            java.lang.String r2 = r10.b
            java.lang.String r3 = ""
            android.util.Log.e(r2, r3, r0)
        L_0x01c1:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.dh.a():void");
    }

    public final void log(String str) {
        log(str, (Throwable) null);
    }

    public final void log(String str, Throwable th) {
        f233a.add(new Pair(String.format("%1$s %2$s %3$s ", new Object[]{f232a.format(new Date()), this.b, str}), th));
        a.a((al.b) new di(this));
    }

    public final void setTag(String str) {
        this.b = str;
    }
}
