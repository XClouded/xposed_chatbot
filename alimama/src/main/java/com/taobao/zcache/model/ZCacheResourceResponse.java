package com.taobao.zcache.model;

import android.text.TextUtils;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ZCacheResourceResponse {
    public static final String ZCACHE_INFO = "X-ZCache-Info";
    public static final String ZCACHE_NO_HEADER = "NO_HEADER";
    public static final String ZCACHE_NO_RESPONSE = "NO_RESPONSE";
    public String encoding;
    public Map<String, String> headers;
    public InputStream inputStream;
    public boolean isSuccess = false;
    public String mimeType;
    public int status;
    public String zcacheInfo;

    public void insertZCacheInfo(String str, long j, String str2) {
        if (!TextUtils.isEmpty(str)) {
            if (TextUtils.isEmpty(str2)) {
                str2 = "0";
            }
            if (this.headers == null) {
                this.headers = new HashMap();
            }
            this.headers.put("X-ZCache-Info", str + "_" + j + "_" + str2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0100, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0101, code lost:
        r1 = r0;
        r17 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0106, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0107, code lost:
        r20 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x010c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x010d, code lost:
        r20 = r12;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0100 A[ExcHandler: all (r0v70 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:24:0x00df] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x011e A[SYNTHETIC, Splitter:B:54:0x011e] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x012d A[SYNTHETIC, Splitter:B:63:0x012d] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x018b A[SYNTHETIC, Splitter:B:74:0x018b] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01c1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.zcache.model.ZCacheResourceResponse buildFrom(com.taobao.zcachecorewrapper.model.ResourceInfo r24, @androidx.annotation.Nullable java.util.Map<java.lang.String, java.lang.String> r25) {
        /*
            r1 = r24
            com.taobao.zcache.model.ZCacheResourceResponse r2 = new com.taobao.zcache.model.ZCacheResourceResponse
            r2.<init>()
            r3 = 0
            if (r1 != 0) goto L_0x0013
            r2.isSuccess = r3
            r2.status = r3
            java.lang.String r0 = "NO_RESPONSE"
            r2.zcacheInfo = r0
            return r2
        L_0x0013:
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r0 = r1.resourceItemInfos
            r4 = 2
            if (r0 == 0) goto L_0x0027
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r0 = r1.resourceItemInfos
            int r0 = r0.size()
            if (r0 == 0) goto L_0x0027
            int r0 = r1.errCode
            if (r0 == 0) goto L_0x0025
            goto L_0x0027
        L_0x0025:
            r0 = 0
            goto L_0x0036
        L_0x0027:
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r0 = r1.resourceItemInfos
            if (r0 == 0) goto L_0x03cf
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r0 = r1.resourceItemInfos
            int r0 = r0.size()
            if (r0 >= r4) goto L_0x0035
            goto L_0x03cf
        L_0x0035:
            r0 = 1
        L_0x0036:
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r6]
            java.io.ByteArrayOutputStream r7 = new java.io.ByteArrayOutputStream
            r7.<init>()
            r8 = 0
            long r9 = java.lang.System.currentTimeMillis()
            double r9 = (double) r9
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r12 = r1.resourceItemInfos
            java.util.Iterator r12 = r12.iterator()
            r17 = r8
            r15 = 0
            r8 = r0
        L_0x0055:
            boolean r0 = r12.hasNext()
            r4 = 6
            r5 = 5
            if (r0 == 0) goto L_0x0219
            java.lang.Object r0 = r12.next()
            r15 = r0
            com.taobao.zcachecorewrapper.model.ResourceItemInfo r15 = (com.taobao.zcachecorewrapper.model.ResourceItemInfo) r15
            java.util.HashMap r13 = new java.util.HashMap
            r13.<init>(r5)
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>(r4)
            java.lang.String r0 = "appName"
            java.lang.String r4 = r15.appName
            r13.put(r0, r4)
            java.lang.String r0 = "packSeq"
            long r3 = r15.seq
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r13.put(r0, r3)
            java.lang.String r0 = "errorCode"
            int r3 = r15.errCode
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r13.put(r0, r3)
            java.lang.String r0 = "errorMsg"
            java.lang.String r3 = r15.errMsg
            r13.put(r0, r3)
            java.lang.String r0 = "comboCount"
            java.lang.String r3 = "1"
            r13.put(r0, r3)
            java.lang.String r0 = "sessionID"
            com.taobao.zcache.ZCacheManager r3 = com.taobao.zcache.ZCacheManager.instance()
            java.lang.String r3 = r3.getSeesionID()
            r13.put(r0, r3)
            java.lang.String r0 = "trigger"
            java.lang.String r3 = r15.trigger
            r13.put(r0, r3)
            double r3 = r15.matchTime
            r18 = r9
            double r9 = r15.readAppResTime
            double r3 = r3 + r9
            java.lang.String r0 = "matchTime"
            double r9 = r15.matchTime
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            r5.put(r0, r9)
            java.lang.String r0 = "readAppResTime"
            double r9 = r15.readAppResTime
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            r5.put(r0, r9)
            java.lang.String r0 = r15.url
            r11.add(r0)
            int r0 = r15.errCode
            if (r0 != 0) goto L_0x0195
            long r9 = java.lang.System.currentTimeMillis()
            double r9 = (double) r9
            java.io.FileInputStream r14 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0125, IOException -> 0x0116 }
            java.lang.String r0 = r15.filePath     // Catch:{ FileNotFoundException -> 0x0125, IOException -> 0x0116 }
            r14.<init>(r0)     // Catch:{ FileNotFoundException -> 0x0125, IOException -> 0x0116 }
        L_0x00df:
            int r0 = r14.read(r6)     // Catch:{ FileNotFoundException -> 0x010c, IOException -> 0x0106, all -> 0x0100 }
            r20 = r12
            r12 = -1
            if (r0 == r12) goto L_0x00f3
            r12 = 0
            r7.write(r6, r12, r0)     // Catch:{ FileNotFoundException -> 0x00f1, IOException -> 0x00ef, all -> 0x0100 }
            r12 = r20
            goto L_0x00df
        L_0x00ef:
            r0 = move-exception
            goto L_0x0109
        L_0x00f1:
            r0 = move-exception
            goto L_0x010f
        L_0x00f3:
            r14.close()     // Catch:{ IOException -> 0x00f7 }
            goto L_0x00fc
        L_0x00f7:
            r0 = move-exception
            r12 = r0
            r12.printStackTrace()
        L_0x00fc:
            r17 = r14
            r0 = 1
            goto L_0x0137
        L_0x0100:
            r0 = move-exception
            r1 = r0
            r17 = r14
            goto L_0x0189
        L_0x0106:
            r0 = move-exception
            r20 = r12
        L_0x0109:
            r17 = r14
            goto L_0x0119
        L_0x010c:
            r0 = move-exception
            r20 = r12
        L_0x010f:
            r17 = r14
            goto L_0x0128
        L_0x0112:
            r0 = move-exception
            r1 = r0
            goto L_0x0189
        L_0x0116:
            r0 = move-exception
            r20 = r12
        L_0x0119:
            r0.printStackTrace()     // Catch:{ all -> 0x0112 }
            if (r17 == 0) goto L_0x0136
            r17.close()     // Catch:{ IOException -> 0x0122 }
            goto L_0x0136
        L_0x0122:
            r0 = move-exception
            r12 = r0
            goto L_0x0133
        L_0x0125:
            r0 = move-exception
            r20 = r12
        L_0x0128:
            r0.printStackTrace()     // Catch:{ all -> 0x0112 }
            if (r17 == 0) goto L_0x0136
            r17.close()     // Catch:{ IOException -> 0x0131 }
            goto L_0x0136
        L_0x0131:
            r0 = move-exception
            r12 = r0
        L_0x0133:
            r12.printStackTrace()
        L_0x0136:
            r0 = 0
        L_0x0137:
            if (r0 != 0) goto L_0x0160
            java.lang.String r12 = "errorCode"
            java.lang.String r14 = "3109"
            r13.put(r12, r14)
            java.lang.String r12 = "errorMsg"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r21 = r0
            java.lang.String r0 = r15.appName
            r14.append(r0)
            java.lang.String r0 = " file not found: "
            r14.append(r0)
            java.lang.String r0 = r15.filePath
            r14.append(r0)
            java.lang.String r0 = r14.toString()
            r13.put(r12, r0)
            goto L_0x0162
        L_0x0160:
            r21 = r0
        L_0x0162:
            r22 = r6
            r23 = r7
            long r6 = java.lang.System.currentTimeMillis()
            double r6 = (double) r6
            java.lang.Double.isNaN(r6)
            java.lang.Double.isNaN(r9)
            double r6 = r6 - r9
            double r3 = r3 + r6
            java.lang.String r0 = "readFileTime"
            long r6 = java.lang.System.currentTimeMillis()
            double r6 = (double) r6
            java.lang.Double.isNaN(r6)
            java.lang.Double.isNaN(r9)
            double r6 = r6 - r9
            java.lang.Double r6 = java.lang.Double.valueOf(r6)
            r5.put(r0, r6)
            goto L_0x01b2
        L_0x0189:
            if (r17 == 0) goto L_0x0194
            r17.close()     // Catch:{ IOException -> 0x018f }
            goto L_0x0194
        L_0x018f:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x0194:
            throw r1
        L_0x0195:
            r22 = r6
            r23 = r7
            r20 = r12
            java.lang.String r0 = "readFileTime"
            r6 = 0
            java.lang.Double r8 = java.lang.Double.valueOf(r6)
            r5.put(r0, r8)
            java.lang.String r0 = "verifyTime"
            java.lang.Double r8 = java.lang.Double.valueOf(r6)
            r5.put(r0, r8)
            r8 = 1
            r21 = 0
        L_0x01b2:
            java.lang.String r0 = "time"
            java.lang.Double r6 = java.lang.Double.valueOf(r3)
            r5.put(r0, r6)
            java.lang.String r0 = "N"
            boolean r6 = r15.isFirstVisit
            if (r6 == 0) goto L_0x01e8
            java.lang.String r0 = "NF"
            java.lang.String r6 = "isHit"
            if (r21 == 0) goto L_0x01ca
            java.lang.String r7 = "true"
            goto L_0x01cc
        L_0x01ca:
            java.lang.String r7 = "false"
        L_0x01cc:
            r13.put(r6, r7)
            com.taobao.zcache.intelligent.ZIntelligentManger r6 = com.taobao.zcache.intelligent.ZIntelligentManger.getInstance()
            com.taobao.zcache.intelligent.IIntelligent r6 = r6.getIntelligentImpl()
            if (r6 == 0) goto L_0x01e8
            com.taobao.zcache.intelligent.ZIntelligentManger r6 = com.taobao.zcache.intelligent.ZIntelligentManger.getInstance()
            com.taobao.zcache.intelligent.IIntelligent r6 = r6.getIntelligentImpl()
            java.lang.String r7 = r15.appName
            boolean r9 = r15.isAppInstalled
            r6.commitFirstVisit(r13, r5, r7, r9)
        L_0x01e8:
            java.lang.String r6 = "visitType"
            r13.put(r6, r0)
            int r0 = r15.errCode
            r6 = 3001(0xbb9, float:4.205E-42)
            if (r0 == r6) goto L_0x020c
            com.taobao.zcache.monitor.ZMonitorManager r0 = com.taobao.zcache.monitor.ZMonitorManager.getInstance()
            com.taobao.zcache.monitor.IZCacheMonitor r0 = r0.getMonitorProxy()
            if (r0 == 0) goto L_0x020c
            com.taobao.zcache.monitor.ZMonitorManager r0 = com.taobao.zcache.monitor.ZMonitorManager.getInstance()
            com.taobao.zcache.monitor.IZCacheMonitor r0 = r0.getMonitorProxy()
            java.lang.String r6 = "ZCache"
            java.lang.String r7 = "AppVisit"
            r0.commitStat(r6, r7, r13, r5)
        L_0x020c:
            r15 = r3
            r9 = r18
            r12 = r20
            r6 = r22
            r7 = r23
            r3 = 0
            r4 = 2
            goto L_0x0055
        L_0x0219:
            r23 = r7
            r18 = r9
            if (r8 == 0) goto L_0x0270
            r3 = 0
            r2.isSuccess = r3
            r3 = 1
            r2.status = r3
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = r1.appName
            r0.append(r3)
            java.lang.String r3 = "_"
            r0.append(r3)
            long r6 = r1.seq
            r0.append(r6)
            java.lang.String r3 = "_"
            r0.append(r3)
            int r3 = r1.errCode
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            r2.zcacheInfo = r0
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r0 = r1.resourceItemInfos
            int r0 = r0.size()
            r3 = 1
            if (r0 <= r3) goto L_0x026f
            r3 = 0
            java.lang.String[] r0 = new java.lang.String[r3]
            com.taobao.zcache.custom.ZCustomCacheManager r3 = com.taobao.zcache.custom.ZCustomCacheManager.getInstance()
            java.lang.String r6 = r1.url
            java.lang.Object[] r0 = r11.toArray(r0)
            java.lang.String[] r0 = (java.lang.String[]) r0
            java.util.HashMap<java.lang.String, java.lang.String> r7 = r1.headers
            r9 = r25
            java.io.InputStream r0 = r3.getCacheResource(r6, r0, r7, r9)
            if (r0 != 0) goto L_0x026c
            return r2
        L_0x026c:
            r2.inputStream = r0
            goto L_0x027b
        L_0x026f:
            return r2
        L_0x0270:
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream
            byte[] r3 = r23.toByteArray()
            r0.<init>(r3)
            r2.inputStream = r0
        L_0x027b:
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r0 = r1.resourceItemInfos
            int r0 = r0.size()
            r3 = 1
            if (r0 <= r3) goto L_0x033d
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>(r5)
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>(r4)
            java.lang.String r4 = "appName"
            java.lang.String r5 = "COMBO"
            r0.put(r4, r5)
            java.lang.String r4 = "packSeq"
            java.lang.String r5 = "1"
            r0.put(r4, r5)
            java.lang.String r4 = "errorCode"
            int r5 = r1.errCode
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r0.put(r4, r5)
            java.lang.String r4 = "errorMsg"
            java.lang.String r5 = r1.errMsg
            r0.put(r4, r5)
            java.lang.String r4 = "comboCount"
            java.util.ArrayList<com.taobao.zcachecorewrapper.model.ResourceItemInfo> r5 = r1.resourceItemInfos
            int r5 = r5.size()
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r0.put(r4, r5)
            java.lang.String r4 = "sessionID"
            com.taobao.zcache.ZCacheManager r5 = com.taobao.zcache.ZCacheManager.instance()
            java.lang.String r5 = r5.getSeesionID()
            r0.put(r4, r5)
            java.lang.String r4 = "visitType"
            java.lang.String r5 = "N"
            r0.put(r4, r5)
            java.lang.String r4 = "matchTime"
            double r5 = r1.matchTime
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            r3.put(r4, r5)
            java.lang.String r4 = "readAppResTime"
            double r5 = r1.readAppResTime
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            r3.put(r4, r5)
            java.lang.String r4 = "readFileTime"
            long r5 = java.lang.System.currentTimeMillis()
            double r5 = (double) r5
            java.lang.Double.isNaN(r5)
            java.lang.Double.isNaN(r18)
            double r5 = r5 - r18
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            r3.put(r4, r5)
            java.lang.String r4 = "verifyTime"
            r5 = 0
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            r3.put(r4, r5)
            double r4 = r1.matchTime
            double r6 = r1.readAppResTime
            double r4 = r4 + r6
            long r6 = java.lang.System.currentTimeMillis()
            double r6 = (double) r6
            java.lang.Double.isNaN(r6)
            double r4 = r4 + r6
            java.lang.Double.isNaN(r18)
            double r15 = r4 - r18
            java.lang.String r4 = "time"
            java.lang.Double r5 = java.lang.Double.valueOf(r15)
            r3.put(r4, r5)
            com.taobao.zcache.monitor.ZMonitorManager r4 = com.taobao.zcache.monitor.ZMonitorManager.getInstance()
            com.taobao.zcache.monitor.IZCacheMonitor r4 = r4.getMonitorProxy()
            if (r4 == 0) goto L_0x033d
            com.taobao.zcache.monitor.ZMonitorManager r4 = com.taobao.zcache.monitor.ZMonitorManager.getInstance()
            com.taobao.zcache.monitor.IZCacheMonitor r4 = r4.getMonitorProxy()
            java.lang.String r5 = "ZCache"
            java.lang.String r6 = "AppVisit"
            r4.commitStat(r5, r6, r0, r3)
        L_0x033d:
            r3 = r15
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "Read res: {\"url\":\""
            r0.append(r5)
            java.lang.String r5 = r1.url
            r0.append(r5)
            java.lang.String r5 = "\", "
            r0.append(r5)
            java.lang.String r5 = "\"name\":\""
            r0.append(r5)
            java.lang.String r5 = r1.appName
            r0.append(r5)
            java.lang.String r5 = "\", "
            r0.append(r5)
            java.lang.String r5 = "\"seq\":"
            r0.append(r5)
            long r5 = r1.seq
            r0.append(r5)
            java.lang.String r5 = ", "
            r0.append(r5)
            java.lang.String r5 = "\"time\":"
            r0.append(r5)
            r0.append(r3)
            java.lang.String r3 = ","
            r0.append(r3)
            java.lang.String r3 = "from: "
            r0.append(r3)
            if (r8 == 0) goto L_0x0387
            java.lang.String r3 = "Custom"
            goto L_0x0389
        L_0x0387:
            java.lang.String r3 = "ZCache"
        L_0x0389:
            r0.append(r3)
            java.lang.String r3 = "}"
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.taobao.zcache.log.ZLog.i(r0)
            r3 = 1
            r2.isSuccess = r3
            if (r8 == 0) goto L_0x03a9
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.comboHeaders
            r2.headers = r0
            r1 = 0
            r2.status = r1
            java.lang.String r0 = ""
            r2.zcacheInfo = r0
            goto L_0x03ce
        L_0x03a9:
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            r2.headers = r0
            r3 = 2
            r2.status = r3
            java.lang.String r0 = "NO_HEADER"
            r2.zcacheInfo = r0
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            if (r0 == 0) goto L_0x03ce
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            java.lang.String r3 = "X-ZCache-Info"
            boolean r0 = r0.containsKey(r3)
            if (r0 == 0) goto L_0x03ce
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            java.lang.String r1 = "X-ZCache-Info"
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r2.zcacheInfo = r0
        L_0x03ce:
            return r2
        L_0x03cf:
            r2.isSuccess = r3
            r3 = 1
            r2.status = r3
            java.lang.String r0 = "NO_HEADER"
            r2.zcacheInfo = r0
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            if (r0 == 0) goto L_0x03f2
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            java.lang.String r3 = "X-ZCache-Info"
            boolean r0 = r0.containsKey(r3)
            if (r0 == 0) goto L_0x03f2
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            java.lang.String r3 = "X-ZCache-Info"
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            r2.zcacheInfo = r0
        L_0x03f2:
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.headers
            r2.headers = r0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.zcache.model.ZCacheResourceResponse.buildFrom(com.taobao.zcachecorewrapper.model.ResourceInfo, java.util.Map):com.taobao.zcache.model.ZCacheResourceResponse");
    }
}
