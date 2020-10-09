package com.taobao.zcache.zipdownload;

import java.util.Map;

public class DownLoadManager {
    private static final String TAG = "DownLoadManager";
    private String destFilePath = null;
    private Map<String, String> headers = null;
    public boolean isTBDownloaderEnabled = true;
    private DownLoadListener listener;
    private int timeout = 5000;
    private String zipUrl;

    public DownLoadManager(String str, DownLoadListener downLoadListener) {
        this.listener = downLoadListener;
        this.zipUrl = str;
    }

    public void setZipUrl(String str) {
        this.zipUrl = str;
    }

    public void setDestFile(String str) {
        this.destFilePath = str;
    }

    public boolean doTask() {
        return doDefaultTask();
    }

    public void setHeaders(Map<String, String> map) {
        this.headers = map;
    }

    public void setTimeout(int i) {
        this.timeout = i;
    }

    /* JADX WARNING: type inference failed for: r0v16, types: [java.net.URLConnection] */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x02be, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x02bf, code lost:
        r3 = r13;
        r4 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x02c3, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x02c4, code lost:
        r3 = r13;
        r4 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x02c7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x02c8, code lost:
        r4 = r15;
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x02cd, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x02ce, code lost:
        r4 = r15;
        r3 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x02b0 A[Catch:{ Exception -> 0x02a9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x02b9  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x02c3 A[ExcHandler: Exception (e java.lang.Exception), Splitter:B:65:0x01e2] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x02cd A[ExcHandler: Exception (e java.lang.Exception), Splitter:B:51:0x01b5] */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0314 A[SYNTHETIC, Splitter:B:125:0x0314] */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x031c A[Catch:{ Exception -> 0x0318 }] */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0321 A[Catch:{ Exception -> 0x0318 }] */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x032a  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x0336 A[SYNTHETIC, Splitter:B:141:0x0336] */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x033e A[Catch:{ Exception -> 0x033a }] */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x0343 A[Catch:{ Exception -> 0x033a }] */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x034c  */
    /* JADX WARNING: Removed duplicated region for block: B:155:0x0203 A[EDGE_INSN: B:155:0x0203->B:74:0x0203 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01f4  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x020f A[SYNTHETIC, Splitter:B:78:0x020f] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0258 A[Catch:{ Exception -> 0x02c3, all -> 0x02be }] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x02a1  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x02a5 A[SYNTHETIC, Splitter:B:95:0x02a5] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean doDefaultTask() {
        /*
            r19 = this;
            r1 = r19
            long r2 = java.lang.System.currentTimeMillis()
            java.lang.String r0 = "DownLoadManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "url="
            r4.append(r5)
            java.lang.String r5 = r1.zipUrl
            r4.append(r5)
            java.lang.String r5 = "线程ID="
            r4.append(r5)
            java.lang.Thread r5 = java.lang.Thread.currentThread()
            long r5 = r5.getId()
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.taobao.zcache.log.ZLog.d(r0, r4)
            r4 = 1
            com.taobao.zcache.global.ZCacheGlobal r0 = com.taobao.zcache.global.ZCacheGlobal.instance()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            android.content.Context r0 = r0.context()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            java.io.File r0 = r0.getCacheDir()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            r6.<init>(r0)     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            boolean r0 = r6.exists()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            if (r0 != 0) goto L_0x0054
            r6.mkdir()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            java.lang.String r0 = "DownLoadManager"
            java.lang.String r7 = "TMP 目录不存在，新建一个tmp目录"
            com.taobao.zcache.log.ZLog.d(r0, r7)     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
        L_0x0054:
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            java.lang.String r7 = r1.zipUrl     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            r0.<init>(r7)     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            r7 = r0
            java.net.HttpURLConnection r7 = (java.net.HttpURLConnection) r7     // Catch:{ Exception -> 0x02e3, all -> 0x02dc }
            int r0 = r1.timeout     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r7.setConnectTimeout(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r0 = "GET"
            r7.setRequestMethod(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.headers     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            if (r0 == 0) goto L_0x0096
            java.util.Map<java.lang.String, java.lang.String> r0 = r1.headers     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
        L_0x007a:
            boolean r8 = r0.hasNext()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            if (r8 == 0) goto L_0x0096
            java.lang.Object r8 = r0.next()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.util.Map$Entry r8 = (java.util.Map.Entry) r8     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.Object r9 = r8.getKey()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.Object r8 = r8.getValue()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r7.setRequestProperty(r9, r8)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            goto L_0x007a
        L_0x0096:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r0.<init>()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r0.append(r6)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r6 = java.io.File.separator     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r0.append(r6)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r6 = r1.zipUrl     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r6 = com.taobao.zcache.util.DigestUtils.md5ToHex(r6)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r0.append(r6)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r6.<init>(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            boolean r8 = r6.exists()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            if (r8 != 0) goto L_0x00be
            r6.mkdir()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
        L_0x00be:
            java.lang.String r6 = "zcache"
            java.lang.String r8 = r1.zipUrl     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r9 = "/"
            int r8 = r8.lastIndexOf(r9)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r9 = -1
            if (r8 == r9) goto L_0x00d2
            java.lang.String r6 = r1.zipUrl     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            int r8 = r8 + r4
            java.lang.String r6 = r6.substring(r8)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
        L_0x00d2:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r8.<init>()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r8.append(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r0 = java.io.File.separator     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r8.append(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r8.append(r6)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r0 = ".zip"
            boolean r0 = r6.endsWith(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            if (r0 == 0) goto L_0x00ed
            java.lang.String r0 = ""
            goto L_0x00ef
        L_0x00ed:
            java.lang.String r0 = ".zip"
        L_0x00ef:
            r8.append(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r12 = r8.toString()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r0.<init>(r12)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            boolean r6 = r0.exists()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            if (r6 == 0) goto L_0x0104
            r0.delete()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
        L_0x0104:
            r0.createNewFile()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            int r6 = r7.getContentLength()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            long r10 = (long) r6     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r13 = 500000(0x7a120, double:2.47033E-318)
            r6 = 0
            int r8 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r8 >= 0) goto L_0x0133
            java.lang.String r8 = "DownLoadManager"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r10.<init>()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r11 = "isBPDownLoad = false  zipUrl=【"
            r10.append(r11)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r11 = r1.zipUrl     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r10.append(r11)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r11 = "】"
            r10.append(r11)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            com.taobao.zcache.log.ZLog.d(r8, r10)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r8 = 0
            goto L_0x0134
        L_0x0133:
            r8 = 1
        L_0x0134:
            if (r8 == 0) goto L_0x01a2
            java.io.RandomAccessFile r10 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            java.lang.String r11 = "rwd"
            r10.<init>(r0, r11)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            long r13 = r10.length()     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r15 = 0
            int r11 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r11 <= 0) goto L_0x014b
            r17 = 1
            long r13 = r13 - r17
        L_0x014b:
            r10.seek(r13)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            int r11 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r11 <= 0) goto L_0x0175
            java.lang.String r11 = "Range"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r15.<init>()     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r5 = "bytes="
            r15.append(r5)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r15.append(r13)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r5 = "-"
            r15.append(r5)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r5 = ""
            r15.append(r5)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r5 = r15.toString()     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r7.setRequestProperty(r11, r5)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r7.getContentLength()     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
        L_0x0175:
            java.lang.String r5 = "DownLoadManager"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r11.<init>()     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r13 = "isBPDownLoad = true  zipUrl=【"
            r11.append(r13)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r13 = r1.zipUrl     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r11.append(r13)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r13 = "】"
            r11.append(r13)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            com.taobao.zcache.log.ZLog.d(r5, r11)     // Catch:{ Exception -> 0x019c, all -> 0x0195 }
            r5 = r10
            r15 = 0
            goto L_0x01b5
        L_0x0195:
            r0 = move-exception
            r2 = r0
            r5 = r10
            r3 = 0
            r4 = 0
            goto L_0x0334
        L_0x019c:
            r0 = move-exception
            r5 = r10
            r3 = 0
            r4 = 0
            goto L_0x02e8
        L_0x01a2:
            boolean r5 = r0.isDirectory()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            if (r5 == 0) goto L_0x01ae
            r0.delete()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r0.createNewFile()     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
        L_0x01ae:
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r5.<init>(r0)     // Catch:{ Exception -> 0x02d7, all -> 0x02d1 }
            r15 = r5
            r5 = 0
        L_0x01b5:
            java.io.InputStream r10 = r7.getInputStream()     // Catch:{ Exception -> 0x02cd, all -> 0x02c7 }
            java.lang.String r11 = r7.getContentEncoding()     // Catch:{ Exception -> 0x02cd, all -> 0x02c7 }
            int r14 = r7.getResponseCode()     // Catch:{ Exception -> 0x02cd, all -> 0x02c7 }
            if (r11 == 0) goto L_0x01dc
            java.lang.String r13 = "gzip"
            boolean r11 = r13.equals(r11)     // Catch:{ Exception -> 0x02cd, all -> 0x01d7 }
            if (r11 == 0) goto L_0x01dc
            java.util.zip.GZIPInputStream r11 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x02cd, all -> 0x01d7 }
            r11.<init>(r10)     // Catch:{ Exception -> 0x02cd, all -> 0x01d7 }
            java.io.DataInputStream r10 = new java.io.DataInputStream     // Catch:{ Exception -> 0x02cd, all -> 0x01d7 }
            r10.<init>(r11)     // Catch:{ Exception -> 0x02cd, all -> 0x01d7 }
            r13 = r10
            goto L_0x01e2
        L_0x01d7:
            r0 = move-exception
            r2 = r0
            r4 = r15
            goto L_0x02ca
        L_0x01dc:
            java.io.DataInputStream r11 = new java.io.DataInputStream     // Catch:{ Exception -> 0x02cd, all -> 0x02c7 }
            r11.<init>(r10)     // Catch:{ Exception -> 0x02cd, all -> 0x02c7 }
            r13 = r11
        L_0x01e2:
            com.taobao.zcache.thread.ZCacheFixedThreadPool r10 = com.taobao.zcache.thread.ZCacheFixedThreadPool.getInstance()     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            com.taobao.zcache.thread.ZCacheFixedThreadPool$BufferWrapper r10 = r10.getTempBuffer()     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
        L_0x01ea:
            byte[] r11 = r10.tempBuffer     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            int r4 = com.taobao.zcache.thread.ZCacheFixedThreadPool.bufferSize     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            int r4 = r13.read(r11, r6, r4)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            if (r4 == r9) goto L_0x0203
            if (r8 == 0) goto L_0x01fd
            byte[] r11 = r10.tempBuffer     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r5.write(r11, r6, r4)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
        L_0x01fb:
            r4 = 1
            goto L_0x01ea
        L_0x01fd:
            byte[] r11 = r10.tempBuffer     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r15.write(r11, r6, r4)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            goto L_0x01fb
        L_0x0203:
            r4 = 1
            r10.setIsFree(r4)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r4 = r1.destFilePath     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            boolean r4 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            if (r4 != 0) goto L_0x0254
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r6 = r1.destFilePath     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r4.<init>(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            boolean r4 = r4.exists()     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            if (r4 != 0) goto L_0x021f
            r0.createNewFile()     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
        L_0x021f:
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r6 = r1.destFilePath     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r4.<init>(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            boolean r0 = r0.renameTo(r4)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r4.<init>()     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r6 = "destFile=["
            r4.append(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r6 = r1.destFilePath     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r4.append(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r6 = "] exits, rename = ["
            r4.append(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            r4.append(r0)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r0 = "]"
            r4.append(r0)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            java.lang.String r0 = r4.toString()     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            com.taobao.zcache.log.ZLog.i(r0)     // Catch:{ Exception -> 0x02c3, all -> 0x024e }
            goto L_0x0254
        L_0x024e:
            r0 = move-exception
            r2 = r0
            r3 = r13
            r4 = r15
            goto L_0x0334
        L_0x0254:
            com.taobao.zcache.zipdownload.DownLoadListener r0 = r1.listener     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            if (r0 == 0) goto L_0x02a1
            java.lang.String r0 = "DownLoadManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            r4.<init>()     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r6 = "zipUrl =【"
            r4.append(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r6 = r1.zipUrl     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            r4.append(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r6 = "】  下载耗时=【"
            r4.append(r6)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            r6 = 0
            long r9 = r9 - r2
            r4.append(r9)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r2 = "】isBPDownLoad  =【"
            r4.append(r2)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            r4.append(r8)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r2 = "】"
            r4.append(r2)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r2 = r4.toString()     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            com.taobao.zcache.log.ZLog.d(r0, r2)     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            com.taobao.zcache.zipdownload.DownLoadListener r10 = r1.listener     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.lang.String r11 = r1.zipUrl     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            r0.<init>()     // Catch:{ Exception -> 0x02c3, all -> 0x02be }
            r2 = 0
            java.lang.String r16 = "success"
            r3 = r13
            r13 = r0
            r4 = r15
            r15 = r2
            r10.callback(r11, r12, r13, r14, r15, r16)     // Catch:{ Exception -> 0x029f }
            goto L_0x02a3
        L_0x029f:
            r0 = move-exception
            goto L_0x02e8
        L_0x02a1:
            r3 = r13
            r4 = r15
        L_0x02a3:
            if (r5 == 0) goto L_0x02ab
            r5.close()     // Catch:{ Exception -> 0x02a9 }
            goto L_0x02ab
        L_0x02a9:
            r0 = move-exception
            goto L_0x02b4
        L_0x02ab:
            r3.close()     // Catch:{ Exception -> 0x02a9 }
            if (r4 == 0) goto L_0x02b7
            r4.close()     // Catch:{ Exception -> 0x02a9 }
            goto L_0x02b7
        L_0x02b4:
            r0.printStackTrace()
        L_0x02b7:
            if (r7 == 0) goto L_0x032d
            r7.disconnect()
            goto L_0x032d
        L_0x02be:
            r0 = move-exception
            r3 = r13
            r4 = r15
            goto L_0x0333
        L_0x02c3:
            r0 = move-exception
            r3 = r13
            r4 = r15
            goto L_0x02e8
        L_0x02c7:
            r0 = move-exception
            r4 = r15
            r2 = r0
        L_0x02ca:
            r3 = 0
            goto L_0x0334
        L_0x02cd:
            r0 = move-exception
            r4 = r15
            r3 = 0
            goto L_0x02e8
        L_0x02d1:
            r0 = move-exception
            r2 = r0
            r3 = 0
            r4 = 0
            r5 = 0
            goto L_0x0334
        L_0x02d7:
            r0 = move-exception
            r3 = 0
            r4 = 0
            r5 = 0
            goto L_0x02e8
        L_0x02dc:
            r0 = move-exception
            r2 = r0
            r3 = 0
            r4 = 0
            r5 = 0
            r7 = 0
            goto L_0x0334
        L_0x02e3:
            r0 = move-exception
            r3 = 0
            r4 = 0
            r5 = 0
            r7 = 0
        L_0x02e8:
            com.taobao.zcache.zipdownload.DownLoadListener r8 = r1.listener     // Catch:{ all -> 0x0332 }
            java.lang.String r9 = r1.zipUrl     // Catch:{ all -> 0x0332 }
            r10 = 0
            r11 = 0
            r12 = 101(0x65, float:1.42E-43)
            r13 = -1
            java.lang.String r14 = r0.getMessage()     // Catch:{ all -> 0x0332 }
            r8.callback(r9, r10, r11, r12, r13, r14)     // Catch:{ all -> 0x0332 }
            java.lang.String r2 = "DownLoadManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0332 }
            r6.<init>()     // Catch:{ all -> 0x0332 }
            java.lang.String r8 = "WVZipBPDownloader  Exception : "
            r6.append(r8)     // Catch:{ all -> 0x0332 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x0332 }
            r6.append(r0)     // Catch:{ all -> 0x0332 }
            java.lang.String r0 = r6.toString()     // Catch:{ all -> 0x0332 }
            com.taobao.zcache.log.ZLog.e(r2, r0)     // Catch:{ all -> 0x0332 }
            if (r5 == 0) goto L_0x031a
            r5.close()     // Catch:{ Exception -> 0x0318 }
            goto L_0x031a
        L_0x0318:
            r0 = move-exception
            goto L_0x0325
        L_0x031a:
            if (r3 == 0) goto L_0x031f
            r3.close()     // Catch:{ Exception -> 0x0318 }
        L_0x031f:
            if (r4 == 0) goto L_0x0328
            r4.close()     // Catch:{ Exception -> 0x0318 }
            goto L_0x0328
        L_0x0325:
            r0.printStackTrace()
        L_0x0328:
            if (r7 == 0) goto L_0x032d
            r7.disconnect()
        L_0x032d:
            r2 = 0
            r1.listener = r2
            r2 = 1
            return r2
        L_0x0332:
            r0 = move-exception
        L_0x0333:
            r2 = r0
        L_0x0334:
            if (r5 == 0) goto L_0x033c
            r5.close()     // Catch:{ Exception -> 0x033a }
            goto L_0x033c
        L_0x033a:
            r0 = move-exception
            goto L_0x0347
        L_0x033c:
            if (r3 == 0) goto L_0x0341
            r3.close()     // Catch:{ Exception -> 0x033a }
        L_0x0341:
            if (r4 == 0) goto L_0x034a
            r4.close()     // Catch:{ Exception -> 0x033a }
            goto L_0x034a
        L_0x0347:
            r0.printStackTrace()
        L_0x034a:
            if (r7 == 0) goto L_0x034f
            r7.disconnect()
        L_0x034f:
            r3 = 0
            r1.listener = r3
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.zcache.zipdownload.DownLoadManager.doDefaultTask():boolean");
    }
}
