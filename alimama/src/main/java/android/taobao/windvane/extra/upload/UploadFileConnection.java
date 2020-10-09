package android.taobao.windvane.extra.upload;

import android.taobao.windvane.connect.HttpConnectListener;

public class UploadFileConnection implements Runnable {
    private static final int DEFAULT_CONNECT_TIMEOUT = 60000;
    private static final int DEFAULT_READ_TIMEOUT = 60000;
    public static final int ERROE_CODE_FAIL = 1;
    public static final String ERROE_MSG_FAIL = "FAIL";
    public static final int ERR_CODE_TOKEN_INVALID = 2;
    public static final String ERR_MSG_TOKEN_INVALID = "TOKEN_IS_INVALID";
    private static final String TAG = "UploadFileConnection";
    private String accessToken;
    private String mFileExt;
    private String mFilePath;
    private HttpConnectListener<UploadFileData> mListener;
    private int tryNum;

    public UploadFileConnection(String str, String str2, HttpConnectListener<UploadFileData> httpConnectListener) {
        this.mListener = httpConnectListener;
        this.mFilePath = str;
        this.mFileExt = str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a2  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x010b A[Catch:{ JSONException -> 0x0171 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0179  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "wv-a-8.5.0-"
            r0.append(r1)
            android.app.Application r1 = android.taobao.windvane.config.GlobalConfig.context
            java.lang.String r1 = android.taobao.windvane.util.PhoneInfo.getImei(r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.taobao.windvane.connect.HttpConnector r1 = new android.taobao.windvane.connect.HttpConnector
            r1.<init>()
            java.lang.String r2 = android.taobao.windvane.extra.mtop.ApiUrlManager.getUploadTokenUrl(r0)
            android.taobao.windvane.connect.HttpResponse r1 = r1.syncConnect((java.lang.String) r2)
            boolean r2 = r1.isSuccess()
            r3 = 0
            if (r2 == 0) goto L_0x0080
            byte[] r2 = r1.getData()
            if (r2 == 0) goto L_0x0080
            java.lang.String r2 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x005b }
            byte[] r1 = r1.getData()     // Catch:{ UnsupportedEncodingException -> 0x005b }
            java.lang.String r4 = "utf-8"
            r2.<init>(r1, r4)     // Catch:{ UnsupportedEncodingException -> 0x005b }
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            if (r1 == 0) goto L_0x0060
            java.lang.String r1 = "UploadFileConnection"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            r4.<init>()     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            java.lang.String r5 = "get upload token success, content="
            r4.append(r5)     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            r4.append(r2)     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            java.lang.String r4 = r4.toString()     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            android.taobao.windvane.util.TaoLog.d(r1, r4)     // Catch:{ UnsupportedEncodingException -> 0x0059 }
            goto L_0x0060
        L_0x0059:
            r1 = move-exception
            goto L_0x005d
        L_0x005b:
            r1 = move-exception
            r2 = r3
        L_0x005d:
            r1.printStackTrace()
        L_0x0060:
            android.taobao.windvane.connect.api.ApiResponse r1 = new android.taobao.windvane.connect.api.ApiResponse
            r1.<init>()
            r1.parseResult(r2)
            boolean r2 = r1.success
            if (r2 == 0) goto L_0x0080
            org.json.JSONObject r1 = r1.data
            if (r1 == 0) goto L_0x0080
            java.lang.String r2 = "accessToken"
            java.lang.String r2 = r1.optString(r2)
            r6.accessToken = r2
            java.lang.String r2 = "tryNum"
            int r1 = r1.optInt(r2)
            r6.tryNum = r1
        L_0x0080:
            java.lang.String r1 = r6.accessToken
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x00a2
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x0095
            java.lang.String r0 = "UploadFileConnection"
            java.lang.String r1 = "get upload token fail, accessToken is empty"
            android.taobao.windvane.util.TaoLog.d(r0, r1)
        L_0x0095:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r0 = r6.mListener
            if (r0 == 0) goto L_0x00a1
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r0 = r6.mListener
            r1 = 2
            java.lang.String r2 = "TOKEN_IS_INVALID"
            r0.onError(r1, r2)
        L_0x00a1:
            return
        L_0x00a2:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r1 = r6.mListener
            if (r1 == 0) goto L_0x00ab
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r1 = r6.mListener
            r1.onStart()
        L_0x00ab:
            java.lang.String r1 = r6.accessToken
            java.lang.String r0 = android.taobao.windvane.extra.mtop.ApiUrlManager.getUploadUrl(r0, r1)
            java.lang.String r1 = r6.mFilePath
            android.taobao.windvane.connect.HttpResponse r0 = r6.uploadFile(r0, r1)
            boolean r1 = r0.isSuccess()
            if (r1 == 0) goto L_0x0175
            byte[] r1 = r0.getData()
            if (r1 == 0) goto L_0x0175
            java.lang.String r1 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x00ed }
            byte[] r0 = r0.getData()     // Catch:{ UnsupportedEncodingException -> 0x00ed }
            java.lang.String r2 = "utf-8"
            r1.<init>(r0, r2)     // Catch:{ UnsupportedEncodingException -> 0x00ed }
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            if (r0 == 0) goto L_0x00f2
            java.lang.String r0 = "UploadFileConnection"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            r2.<init>()     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            java.lang.String r4 = "upload file success, response: "
            r2.append(r4)     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            r2.append(r1)     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            java.lang.String r2 = r2.toString()     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            android.taobao.windvane.util.TaoLog.d(r0, r2)     // Catch:{ UnsupportedEncodingException -> 0x00eb }
            goto L_0x00f2
        L_0x00eb:
            r0 = move-exception
            goto L_0x00ef
        L_0x00ed:
            r0 = move-exception
            r1 = r3
        L_0x00ef:
            r0.printStackTrace()
        L_0x00f2:
            android.taobao.windvane.connect.api.ApiResponse r0 = new android.taobao.windvane.connect.api.ApiResponse
            r0.<init>()
            android.taobao.windvane.connect.api.ApiResponse r0 = r0.parseResult(r1)
            boolean r1 = r0.success
            if (r1 == 0) goto L_0x0175
            org.json.JSONObject r0 = r0.data
            if (r0 == 0) goto L_0x0175
            java.lang.String r1 = "status"
            boolean r1 = r0.getBoolean(r1)     // Catch:{ JSONException -> 0x0171 }
            if (r1 == 0) goto L_0x0175
            java.lang.String r1 = "uploadInfo"
            org.json.JSONObject r0 = r0.getJSONObject(r1)     // Catch:{ JSONException -> 0x0171 }
            android.taobao.windvane.extra.upload.UploadFileData r1 = new android.taobao.windvane.extra.upload.UploadFileData     // Catch:{ JSONException -> 0x0171 }
            r1.<init>()     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "accessToken"
            java.lang.String r2 = r0.getString(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.accessToken = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "blockNum"
            int r2 = r0.getInt(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.blockNum = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "fileExt"
            java.lang.String r2 = r0.getString(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.fileExt = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "fileName"
            java.lang.String r2 = r0.getString(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.fileName = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "fileSize"
            long r4 = r0.getLong(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.fileSize = r4     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "resourceUri"
            java.lang.String r2 = r0.getString(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.resourceUri = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "tfsKey"
            java.lang.String r2 = r0.getString(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.tfsKey = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "tryNum"
            int r2 = r0.getInt(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.tryNum = r2     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "validTime"
            long r4 = r0.getLong(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.validTime = r4     // Catch:{ JSONException -> 0x0171 }
            java.lang.String r2 = "finish"
            boolean r0 = r0.getBoolean(r2)     // Catch:{ JSONException -> 0x0171 }
            r1.finish = r0     // Catch:{ JSONException -> 0x0171 }
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r0 = r6.mListener     // Catch:{ JSONException -> 0x0171 }
            if (r0 == 0) goto L_0x0170
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r0 = r6.mListener     // Catch:{ JSONException -> 0x0171 }
            r2 = 0
            r0.onFinish(r1, r2)     // Catch:{ JSONException -> 0x0171 }
        L_0x0170:
            return
        L_0x0171:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0175:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r0 = r6.mListener
            if (r0 == 0) goto L_0x0181
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.extra.upload.UploadFileData> r0 = r6.mListener
            r1 = 1
            java.lang.String r2 = "FAIL"
            r0.onError(r1, r2)
        L_0x0181:
            r6.mListener = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.upload.UploadFileConnection.run():void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v19, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v20, resolved type: java.net.HttpURLConnection} */
    /* JADX WARNING: type inference failed for: r10v1, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r10v6, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r10v7 */
    /* JADX WARNING: type inference failed for: r10v8 */
    /* JADX WARNING: type inference failed for: r10v9 */
    /* JADX WARNING: type inference failed for: r10v10 */
    /* JADX WARNING: type inference failed for: r10v11 */
    /* JADX WARNING: type inference failed for: r10v14 */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0188, code lost:
        if (r10 != 0) goto L_0x01bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x01b9, code lost:
        if (r10 != 0) goto L_0x01bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01bb, code lost:
        r10.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x01c2, code lost:
        if (android.taobao.windvane.util.TaoLog.getLogStatus() == false) goto L_0x01cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x01c4, code lost:
        android.taobao.windvane.util.TaoLog.d(TAG, "upload file fail.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x01d0, code lost:
        return new android.taobao.windvane.connect.HttpResponse();
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0178 A[SYNTHETIC, Splitter:B:47:0x0178] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01a9 A[SYNTHETIC, Splitter:B:62:0x01a9] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01d4 A[SYNTHETIC, Splitter:B:79:0x01d4] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01e6  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.taobao.windvane.connect.HttpResponse uploadFile(java.lang.String r10, java.lang.String r11) {
        /*
            r9 = this;
            android.taobao.windvane.connect.HttpResponse r0 = new android.taobao.windvane.connect.HttpResponse
            r0.<init>()
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r1.<init>()
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x018b, Exception -> 0x015a, all -> 0x0156 }
            r3.<init>(r11)     // Catch:{ IOException -> 0x018b, Exception -> 0x015a, all -> 0x0156 }
            java.net.URL r11 = new java.net.URL     // Catch:{ IOException -> 0x018b, Exception -> 0x015a, all -> 0x0156 }
            r11.<init>(r10)     // Catch:{ IOException -> 0x018b, Exception -> 0x015a, all -> 0x0156 }
            java.net.URLConnection r10 = r11.openConnection()     // Catch:{ IOException -> 0x018b, Exception -> 0x015a, all -> 0x0156 }
            java.net.HttpURLConnection r10 = (java.net.HttpURLConnection) r10     // Catch:{ IOException -> 0x018b, Exception -> 0x015a, all -> 0x0156 }
            r4 = 60000(0xea60, float:8.4078E-41)
            r10.setReadTimeout(r4)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r10.setConnectTimeout(r4)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r4 = 1
            r10.setDoInput(r4)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r10.setDoOutput(r4)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r4 = 0
            r10.setUseCaches(r4)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r5 = "POST"
            r10.setRequestMethod(r5)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r5 = "Connection"
            java.lang.String r6 = "keep-alive"
            r10.setRequestProperty(r5, r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r5 = "Host"
            java.lang.String r11 = r11.getHost()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r10.setRequestProperty(r5, r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.util.UUID r11 = java.util.UUID.randomUUID()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r11 = r11.toString()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r5 = "Content-Type"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.<init>()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = "multipart/form-data;boundary="
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.append(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r10.setRequestProperty(r5, r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.io.DataOutputStream r5 = new java.io.DataOutputStream     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.io.OutputStream r6 = r10.getOutputStream()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r5.<init>(r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.<init>()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = "--"
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.append(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = "\r\n"
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r5.writeBytes(r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r6 = r9.mFileExt     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            if (r6 != 0) goto L_0x008c
            java.lang.String r6 = ""
            r9.mFileExt = r6     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
        L_0x008c:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.<init>()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = "Content-Disposition:form-data;name=\"file\";filename=\""
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = r3.getName()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = "."
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = r9.mFileExt     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r7 = "\"\r\n"
            r6.append(r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r5.writeBytes(r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r6 = "Content-Transfer-Encoding:binary\r\n\r\n"
            r5.writeBytes(r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r6.<init>(r3)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r3 = 4096(0x1000, float:5.74E-42)
            byte[] r3 = new byte[r3]     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
        L_0x00c1:
            int r7 = r6.read(r3)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r8 = -1
            if (r7 == r8) goto L_0x00cc
            r5.write(r3, r4, r7)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            goto L_0x00c1
        L_0x00cc:
            r6.close()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r3 = "\r\n"
            r5.writeBytes(r3)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r3.<init>()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r6 = "--"
            r3.append(r6)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r3.append(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r11 = "--\r\n"
            r3.append(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.lang.String r11 = r3.toString()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r5.writeBytes(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r5.flush()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r5.close()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            int r11 = r10.getResponseCode()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r0.setHttpCode(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r3 = 200(0xc8, float:2.8E-43)
            if (r11 != r3) goto L_0x013a
            java.lang.String r11 = r10.getContentEncoding()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            if (r11 == 0) goto L_0x011b
            java.lang.String r3 = "gzip"
            boolean r11 = r3.equals(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            if (r11 == 0) goto L_0x011b
            java.io.DataInputStream r11 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.util.zip.GZIPInputStream r3 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.io.InputStream r5 = r10.getInputStream()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r3.<init>(r5)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r11.<init>(r3)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            goto L_0x0124
        L_0x011b:
            java.io.DataInputStream r11 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            java.io.InputStream r3 = r10.getInputStream()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r11.<init>(r3)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
        L_0x0124:
            r2 = r11
            r11 = 2048(0x800, float:2.87E-42)
            byte[] r11 = new byte[r11]     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
        L_0x0129:
            int r3 = r2.read(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            if (r3 == r8) goto L_0x0133
            r1.write(r11, r4, r3)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            goto L_0x0129
        L_0x0133:
            byte[] r11 = r1.toByteArray()     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
            r0.setData(r11)     // Catch:{ IOException -> 0x0154, Exception -> 0x0152 }
        L_0x013a:
            if (r2 == 0) goto L_0x0144
            r2.close()     // Catch:{ IOException -> 0x0140 }
            goto L_0x0144
        L_0x0140:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0144:
            r1.close()     // Catch:{ Exception -> 0x0148 }
            goto L_0x014c
        L_0x0148:
            r11 = move-exception
            r11.printStackTrace()
        L_0x014c:
            if (r10 == 0) goto L_0x0151
            r10.disconnect()
        L_0x0151:
            return r0
        L_0x0152:
            r11 = move-exception
            goto L_0x015c
        L_0x0154:
            r11 = move-exception
            goto L_0x018d
        L_0x0156:
            r11 = move-exception
            r10 = r2
            goto L_0x01d2
        L_0x015a:
            r11 = move-exception
            r10 = r2
        L_0x015c:
            java.lang.String r0 = "UploadFileConnection"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d1 }
            r3.<init>()     // Catch:{ all -> 0x01d1 }
            java.lang.String r4 = "upload file error, "
            r3.append(r4)     // Catch:{ all -> 0x01d1 }
            java.lang.String r11 = r11.getMessage()     // Catch:{ all -> 0x01d1 }
            r3.append(r11)     // Catch:{ all -> 0x01d1 }
            java.lang.String r11 = r3.toString()     // Catch:{ all -> 0x01d1 }
            android.taobao.windvane.util.TaoLog.e(r0, r11)     // Catch:{ all -> 0x01d1 }
            if (r2 == 0) goto L_0x0180
            r2.close()     // Catch:{ IOException -> 0x017c }
            goto L_0x0180
        L_0x017c:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0180:
            r1.close()     // Catch:{ Exception -> 0x0184 }
            goto L_0x0188
        L_0x0184:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0188:
            if (r10 == 0) goto L_0x01be
            goto L_0x01bb
        L_0x018b:
            r11 = move-exception
            r10 = r2
        L_0x018d:
            java.lang.String r0 = "UploadFileConnection"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d1 }
            r3.<init>()     // Catch:{ all -> 0x01d1 }
            java.lang.String r4 = "upload file IO exception, "
            r3.append(r4)     // Catch:{ all -> 0x01d1 }
            java.lang.String r11 = r11.getMessage()     // Catch:{ all -> 0x01d1 }
            r3.append(r11)     // Catch:{ all -> 0x01d1 }
            java.lang.String r11 = r3.toString()     // Catch:{ all -> 0x01d1 }
            android.taobao.windvane.util.TaoLog.e(r0, r11)     // Catch:{ all -> 0x01d1 }
            if (r2 == 0) goto L_0x01b1
            r2.close()     // Catch:{ IOException -> 0x01ad }
            goto L_0x01b1
        L_0x01ad:
            r11 = move-exception
            r11.printStackTrace()
        L_0x01b1:
            r1.close()     // Catch:{ Exception -> 0x01b5 }
            goto L_0x01b9
        L_0x01b5:
            r11 = move-exception
            r11.printStackTrace()
        L_0x01b9:
            if (r10 == 0) goto L_0x01be
        L_0x01bb:
            r10.disconnect()
        L_0x01be:
            boolean r10 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r10 == 0) goto L_0x01cb
            java.lang.String r10 = "UploadFileConnection"
            java.lang.String r11 = "upload file fail."
            android.taobao.windvane.util.TaoLog.d(r10, r11)
        L_0x01cb:
            android.taobao.windvane.connect.HttpResponse r10 = new android.taobao.windvane.connect.HttpResponse
            r10.<init>()
            return r10
        L_0x01d1:
            r11 = move-exception
        L_0x01d2:
            if (r2 == 0) goto L_0x01dc
            r2.close()     // Catch:{ IOException -> 0x01d8 }
            goto L_0x01dc
        L_0x01d8:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01dc:
            r1.close()     // Catch:{ Exception -> 0x01e0 }
            goto L_0x01e4
        L_0x01e0:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01e4:
            if (r10 == 0) goto L_0x01e9
            r10.disconnect()
        L_0x01e9:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.upload.UploadFileConnection.uploadFile(java.lang.String, java.lang.String):android.taobao.windvane.connect.HttpResponse");
    }
}
