package mtopsdk.mtop.upload.service;

import anetwork.channel.Network;
import anetwork.channel.Response;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.mtop.upload.domain.FileBaseInfo;
import mtopsdk.mtop.upload.domain.UploadConstants;
import mtopsdk.mtop.upload.domain.UploadFileInfo;
import mtopsdk.mtop.upload.domain.UploadResult;
import mtopsdk.mtop.upload.domain.UploadToken;
import mtopsdk.mtop.upload.util.FileUploadSetting;
import mtopsdk.mtop.upload.util.FileUtil;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.mtop.util.MtopProxyUtils;
import mtopsdk.mtop.util.Result;
import mtopsdk.security.ISign;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class UploadFileServiceImpl implements UploadFileService {
    private static final int NETWORK_RETRY_TIMES = 1;
    private static final String SCHEMA_HTTP = "http://";
    private static final String SCHEMA_HTTPS = "https://";
    private static final int SOCKET_TIMEOUT_MILISECONDS = 40000;
    private static final String TAG = "mtopsdk.UploadFileServiceImpl";
    private static final int UPLOAD_BIZID = 4096;
    private static final String UPLOAD_PATH = "/uploadv2.do";
    private static volatile long timestampOffset;
    private Network networkImpl;
    private ISign signGenerator;

    public UploadFileServiceImpl() {
        this.networkImpl = null;
        this.signGenerator = null;
        this.networkImpl = new DegradableNetwork(SDKConfig.getInstance().getGlobalContext());
        this.signGenerator = SDKConfig.getInstance().getGlobalSign();
    }

    public Result<UploadToken> getUploadToken(UploadFileInfo uploadFileInfo) {
        FileBaseInfo computeFileBaseInfo = computeFileBaseInfo(uploadFileInfo);
        if (computeFileBaseInfo == null || computeFileBaseInfo.fileSize <= 0) {
            UploadToken uploadToken = new UploadToken();
            uploadToken.useHttps = uploadFileInfo.isUseHttps();
            uploadToken.fileBaseInfo = computeFileBaseInfo;
            uploadToken.bizCode = uploadFileInfo.getBizCode();
            Result<UploadToken> result = new Result<>(false, UploadConstants.ERRTYPE_ILLEGAL_FILE_ERROR, UploadConstants.ERRCODE_FILE_INVALID, UploadConstants.ERRMSG_FILE_INVALID);
            result.setModel(uploadToken);
            return result;
        }
        UploadToken computeUploadToken = computeUploadToken(uploadFileInfo, computeFileBaseInfo);
        if (computeUploadToken != null) {
            return new Result<>(computeUploadToken);
        }
        UploadToken uploadToken2 = new UploadToken();
        uploadToken2.useHttps = uploadFileInfo.isUseHttps();
        uploadToken2.fileBaseInfo = computeFileBaseInfo;
        Result<UploadToken> result2 = new Result<>(false, UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, UploadConstants.ERRCODE_INVALID_UPLOAD_TOKEN, UploadConstants.ERRMSG_INVALID_UPLOAD_TOKEN);
        result2.setModel(uploadToken2);
        return result2;
    }

    public Result<UploadResult> fileUpload(UploadToken uploadToken, long j, int i) {
        UploadToken uploadToken2 = uploadToken;
        if (uploadToken2 == null || !uploadToken.isValid()) {
            return new Result<>(false, UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, UploadConstants.ERRCODE_INVALID_UPLOAD_TOKEN, UploadConstants.ERRMSG_INVALID_UPLOAD_TOKEN);
        }
        try {
            RequestImpl requestImpl = new RequestImpl(genUploadUrl(uploadToken2, (String) null));
            requestImpl.setBizId(4096);
            requestImpl.setCookieEnabled(false);
            requestImpl.setReadTimeout(40000);
            requestImpl.setRetryTime(1);
            requestImpl.setMethod(MethodEnum.POST.getMethod());
            FileBaseInfo fileBaseInfo = uploadToken2.fileBaseInfo;
            long j2 = fileBaseInfo.fileSize - j;
            if (j2 < 0) {
                return new Result<>(false, UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, UploadConstants.ERRCODE_FILE_UPLOAD_FAIL, UploadConstants.ERRMSG_FILE_UPLOAD_FAIL);
            }
            long j3 = uploadToken2.segmentSize;
            long j4 = j2 > j3 ? j3 : j2;
            HashMap hashMap = new HashMap();
            hashMap.put("Content-Type", UploadConstants.FILE_CONTENT_TYPE);
            hashMap.put("Content-Length", String.valueOf(j4));
            String value = XState.getValue("ua");
            if (value != null) {
                hashMap.put("user-agent", value);
            }
            requestImpl.setHeaders(MtopProxyUtils.createHttpHeaders(hashMap));
            HashMap hashMap2 = new HashMap();
            if (i > 0) {
                hashMap2.put(UploadConstants.RETRY_TIMES, String.valueOf(i));
            }
            hashMap2.put("token", uploadToken2.token);
            hashMap2.put("offset", String.valueOf(j));
            hashMap2.putAll(uploadToken2.tokenParams);
            requestImpl.setParams(MtopProxyUtils.createHttpParams(hashMap2));
            if (fileBaseInfo.file != null) {
                requestImpl.setBodyHandler(new FileUploadBodyHandlerImpl(fileBaseInfo.file, j, j4));
            } else {
                requestImpl.setBodyHandler(new FileStreamUploadBodyHandlerImpl(fileBaseInfo.fileInputStream, fileBaseInfo.fileSize, j, j4));
            }
            return parseUploadResponse(this.networkImpl.syncSend(requestImpl, (Object) null));
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[fileUpload]gen fileUpload address url error", (Throwable) e);
            new Result(false, UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, UploadConstants.ERRCODE_INVALID_UPLOAD_ADDRESS, UploadConstants.ERRMSG_INVALID_UPLOAD_ADDRESS);
            return null;
        }
    }

    private Result<UploadResult> parseUploadResponse(Response response) {
        Result<UploadResult> result;
        Result<UploadResult> result2;
        int statusCode = response.getStatusCode();
        Map<String, List<String>> connHeadFields = response.getConnHeadFields();
        if (statusCode < 0) {
            if (-200 == statusCode) {
                result2 = new Result<>(false, UploadConstants.ERRTYPE_NETWORK_ERROR, ErrorConstant.ERRCODE_NO_NETWORK, ErrorConstant.ERRMSG_NO_NETWORK);
            } else {
                result2 = new Result<>(false, UploadConstants.ERRTYPE_NETWORK_ERROR, "ANDROID_SYS_NETWORK_ERROR", anet.channel.util.ErrorConstant.getErrMsg(statusCode));
            }
            result2.setStatusCode(statusCode);
            return result2;
        }
        if (200 == statusCode) {
            String parseErrCode = FileUtil.parseErrCode(connHeadFields);
            if ("SUCCESS".equalsIgnoreCase(parseErrCode)) {
                String parseUrlLocation = FileUtil.parseUrlLocation(connHeadFields);
                if (StringUtils.isNotBlank(parseUrlLocation)) {
                    UploadResult uploadResult = new UploadResult(true, parseUrlLocation);
                    uploadResult.serverRT = HeaderHandlerUtil.getSingleHeaderFieldByKey(connHeadFields, UploadConstants.X_SERVER_RT);
                    result = new Result<>(uploadResult);
                } else {
                    result = new Result<>(new UploadResult(false, (String) null));
                }
                result.setErrType("SUCCESS");
                result.setErrCode("SUCCESS");
            } else {
                if (UploadConstants.ERRCODE_TOKEN_EXPIRED.equalsIgnoreCase(parseErrCode)) {
                    computeTimeStampOffset(HeaderHandlerUtil.getSingleHeaderFieldByKey(connHeadFields, UploadConstants.X_SERVER_TIMESTAMP));
                }
                result = new Result<>(false, UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, parseErrCode, FileUtil.parseErrMsg(connHeadFields));
            }
        } else {
            result = new Result<>(false, UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, "ANDROID_SYS_NETWORK_ERROR", "ANDROID_SYS_NETWORK_ERROR");
        }
        result.setStatusCode(statusCode);
        return result;
    }

    private String genUploadUrl(UploadToken uploadToken, String str) {
        if (StringUtils.isBlank(uploadToken.domain)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(32);
        if (FileUploadSetting.useHttps(uploadToken.bizCode) || uploadToken.useHttps) {
            sb.append(SCHEMA_HTTPS);
        } else {
            sb.append(SCHEMA_HTTP);
        }
        sb.append(uploadToken.domain);
        sb.append(UPLOAD_PATH);
        return sb.toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v14, resolved type: java.io.File} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v10, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v11, resolved type: mtopsdk.mtop.upload.domain.FileBaseInfo} */
    /* JADX WARNING: type inference failed for: r11v8, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r11v12 */
    /* JADX WARNING: type inference failed for: r11v17 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00bd  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private mtopsdk.mtop.upload.domain.FileBaseInfo computeFileBaseInfo(mtopsdk.mtop.upload.domain.UploadFileInfo r11) {
        /*
            r10 = this;
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            java.lang.String r2 = r11.getFilePath()
            boolean r3 = mtopsdk.common.util.StringUtils.isNotBlank(r2)
            r4 = 0
            r6 = 0
            if (r3 == 0) goto L_0x0054
            java.io.File r11 = new java.io.File     // Catch:{ Exception -> 0x002a }
            r11.<init>(r2)     // Catch:{ Exception -> 0x002a }
            java.lang.String r3 = r11.getName()     // Catch:{ Exception -> 0x0026 }
            long r6 = r11.length()     // Catch:{ Exception -> 0x0021 }
            r0 = r3
            r4 = r6
            goto L_0x004e
        L_0x0021:
            r0 = move-exception
            r6 = r11
            r11 = r0
            r0 = r3
            goto L_0x002b
        L_0x0026:
            r3 = move-exception
            r6 = r11
            r11 = r3
            goto L_0x002b
        L_0x002a:
            r11 = move-exception
        L_0x002b:
            java.lang.String r3 = "mtopsdk.UploadFileServiceImpl"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "[computeFileBaseInfo]get FileBaseInfo error.check filePath="
            r7.append(r8)
            r7.append(r2)
            java.lang.String r2 = "; ---"
            r7.append(r2)
            java.lang.String r11 = r11.toString()
            r7.append(r11)
            java.lang.String r11 = r7.toString()
            mtopsdk.common.util.TBSdkLog.e(r3, r11)
            r11 = r6
        L_0x004e:
            mtopsdk.mtop.upload.domain.FileBaseInfo r6 = new mtopsdk.mtop.upload.domain.FileBaseInfo
            r6.<init>((java.io.File) r11)
            goto L_0x00a9
        L_0x0054:
            mtopsdk.mtop.upload.domain.FileStreamInfo r11 = r11.getFileStreamInfo()
            if (r11 == 0) goto L_0x00a9
            java.lang.String r2 = r11.getFileName()     // Catch:{ Exception -> 0x0075 }
            long r6 = r11.fileLength     // Catch:{ Exception -> 0x0073 }
            int r0 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0067
            long r6 = r11.fileLength     // Catch:{ Exception -> 0x0073 }
            goto L_0x0070
        L_0x0067:
            java.io.InputStream r0 = r11.getFileStream()     // Catch:{ Exception -> 0x0073 }
            int r0 = r0.available()     // Catch:{ Exception -> 0x0073 }
            long r6 = (long) r0
        L_0x0070:
            r0 = r2
            r4 = r6
            goto L_0x00a0
        L_0x0073:
            r0 = move-exception
            goto L_0x0079
        L_0x0075:
            r2 = move-exception
            r9 = r2
            r2 = r0
            r0 = r9
        L_0x0079:
            java.lang.String r3 = "mtopsdk.UploadFileServiceImpl"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "[[computeFileBaseInfo]]get FileBaseInfo error.check fileStreamInfo="
            r6.append(r7)
            java.lang.String r7 = r11.toString()
            r6.append(r7)
            java.lang.String r7 = ";---"
            r6.append(r7)
            java.lang.String r0 = r0.toString()
            r6.append(r0)
            java.lang.String r0 = r6.toString()
            mtopsdk.common.util.TBSdkLog.e(r3, r0)
            r0 = r2
        L_0x00a0:
            mtopsdk.mtop.upload.domain.FileBaseInfo r6 = new mtopsdk.mtop.upload.domain.FileBaseInfo
            java.io.InputStream r11 = r11.getFileStream()
            r6.<init>((java.io.InputStream) r11)
        L_0x00a9:
            boolean r11 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r11 == 0) goto L_0x00bb
            java.lang.String r11 = "."
            int r11 = r0.lastIndexOf(r11)
            if (r11 < 0) goto L_0x00bb
            java.lang.String r1 = r0.substring(r11)
        L_0x00bb:
            if (r6 == 0) goto L_0x00cd
            r6.fileName = r0
            r6.fileType = r1
            java.util.UUID r11 = java.util.UUID.randomUUID()
            java.lang.String r11 = r11.toString()
            r6.fileId = r11
            r6.fileSize = r4
        L_0x00cd:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.upload.service.UploadFileServiceImpl.computeFileBaseInfo(mtopsdk.mtop.upload.domain.UploadFileInfo):mtopsdk.mtop.upload.domain.FileBaseInfo");
    }

    private UploadToken computeUploadToken(UploadFileInfo uploadFileInfo, FileBaseInfo fileBaseInfo) {
        if (this.signGenerator == null) {
            TBSdkLog.e(TAG, "[computeToken]ISign for SDKConfig.getInstance().getGlobalSign is null");
            return null;
        }
        UploadToken uploadToken = new UploadToken();
        uploadToken.useHttps = uploadFileInfo.isUseHttps();
        uploadToken.bizCode = uploadFileInfo.getBizCode();
        uploadToken.retryCount = (long) FileUploadSetting.getSegmentRetryTimes();
        uploadToken.segmentSize = (long) FileUploadSetting.getSegmentSize(XState.getValue(XStateConstants.KEY_NQ), uploadFileInfo.getBizCode());
        HashMap hashMap = new HashMap();
        hashMap.put("version", "1");
        hashMap.put(UploadConstants.BIZ_CODE, uploadFileInfo.getBizCode());
        hashMap.put("appkey", SDKConfig.getInstance().getGlobalAppKey());
        hashMap.put("t", String.valueOf(System.currentTimeMillis() + timestampOffset));
        hashMap.put("utdid", SDKConfig.getInstance().getGlobalUtdid());
        hashMap.put(UploadConstants.USERID, XState.getValue("uid"));
        hashMap.put(UploadConstants.FILE_ID, fileBaseInfo.fileId);
        hashMap.put("filename", fileBaseInfo.fileName);
        hashMap.put(UploadConstants.FILE_SIZE, String.valueOf(fileBaseInfo.fileSize));
        hashMap.put(UploadConstants.SEGMENT_SIZE, String.valueOf(uploadToken.segmentSize));
        uploadToken.tokenParams = hashMap;
        StringBuilder sb = new StringBuilder();
        for (TokenParamsEnum tokenParamsEnum : TokenParamsEnum.values()) {
            String str = (String) hashMap.get(tokenParamsEnum.getKey());
            if (StringUtils.isBlank(str)) {
                str = "";
                hashMap.remove(tokenParamsEnum.getKey());
            }
            sb.append(str);
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        uploadToken.token = this.signGenerator.getCommonHmacSha1Sign(sb.toString(), (String) hashMap.get("appkey"));
        uploadToken.domain = FileUploadSetting.uploadDomainMap.get(SDKConfig.getInstance().getGlobalEnvMode().getEnvMode());
        uploadToken.fileBaseInfo = fileBaseInfo;
        return uploadToken;
    }

    private void computeTimeStampOffset(String str) {
        if (!StringUtils.isBlank(str)) {
            try {
                long parseLong = Long.parseLong(str);
                if (parseLong > 0) {
                    timestampOffset = parseLong - System.currentTimeMillis();
                }
            } catch (Exception unused) {
                TBSdkLog.e(TAG, "[computeTimeStampOffset] compute TimeStampOffset error,serverTimeStamp=" + str);
            }
        }
    }

    enum TokenParamsEnum {
        VERSION("version"),
        BIZ_CODE(UploadConstants.BIZ_CODE),
        APPKEY("appkey"),
        TIMESTAMP("t"),
        UTDID("utdid"),
        USERID(UploadConstants.USERID),
        FILE_ID(UploadConstants.FILE_ID),
        FILE_NAME("filename"),
        FILE_SIZE(UploadConstants.FILE_SIZE),
        SEGMENT_SIZE(UploadConstants.SEGMENT_SIZE);
        
        private String key;

        private TokenParamsEnum(String str) {
            this.key = str;
        }

        public String getKey() {
            return this.key;
        }
    }
}
