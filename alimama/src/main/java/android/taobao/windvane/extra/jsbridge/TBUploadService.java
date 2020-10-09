package android.taobao.windvane.extra.jsbridge;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.WindVaneSDKForTB;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.extra.upload.UploadFileConnection;
import android.taobao.windvane.extra.upload.UploadFileData;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.WVCamera;
import android.taobao.windvane.jsbridge.api.WVUploadService;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.MimeTypeEnum;
import android.taobao.windvane.util.TaoLog;

import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.taobao.phenix.compat.SimpleDiskCache;
import com.uploader.export.ITaskListener;
import com.uploader.export.ITaskResult;
import com.uploader.export.IUploaderTask;
import com.uploader.export.TaskError;
import com.uploader.export.UploaderCreator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TBUploadService extends WVUploadService implements Handler.Callback {
    private static final String LAST_PIC = "\"isLastPic\":\"true\"";
    private static final String MUTI_SELECTION = "\"mutipleSelection\":\"1\"";
    private static final int NOTIFY_ERROR = 2003;
    private static final int NOTIFY_FINISH = 2002;
    private static final int NOTIFY_START = 2001;
    private static final String TAG = "TBUploadService";
    private WVCallBackContext mCallback;
    /* access modifiers changed from: private */
    public Handler mHandler;

    public TBUploadService() {
        this.mHandler = null;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void doUpload(final WVCamera.UploadParams uploadParams, WVCallBackContext wVCallBackContext) {
        if (uploadParams == null) {
            TaoLog.d(TAG, "UploadParams is null.");
            wVCallBackContext.error(new WVResult());
            return;
        }
        this.mCallback = wVCallBackContext;
        try {
            String url = wVCallBackContext.getWebview().getUrl();
            AppMonitorUtil.commitOffMonitor(url, "TBUploadService bizCode:" + uploadParams.bizCode, uploadParams.v);
        } catch (Throwable unused) {
        }
        if ("2.0".equals(uploadParams.v)) {
            if (WindVaneSDKForTB.wvAdapter != null) {
                WindVaneSDKForTB.wvAdapter.getLoginInfo((Handler) null);
            }
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    TBUploadService.this.doMtopUpload(uploadParams);
                }
            });
            return;
        }
        doNormalUpload(uploadParams);
    }

    private void doNormalUpload(final WVCamera.UploadParams uploadParams) {
        WVThreadPool.getInstance().execute(new UploadFileConnection(uploadParams.filePath, MimeTypeEnum.JPG.getSuffix(), new HttpConnectListener<UploadFileData>() {
            public void onStart() {
                TBUploadService.this.mHandler.sendEmptyMessage(2001);
            }

            public void onFinish(UploadFileData uploadFileData, int i) {
                Bitmap readZoomImage;
                if (uploadFileData != null) {
                    Message obtain = Message.obtain();
                    obtain.what = 2002;
                    WVResult wVResult = new WVResult();
                    wVResult.setSuccess();
                    if (uploadParams.needBase64 && (readZoomImage = ImageTool.readZoomImage(uploadParams.filePath, 1024)) != null) {
                        wVResult.addData("base64Data", WVUtils.bitmapToBase64(readZoomImage));
                    }
                    wVResult.addData("url", uploadParams.localUrl);
                    wVResult.addData("localPath", uploadParams.filePath);
                    wVResult.addData("resourceURL", uploadFileData.resourceUri);
                    wVResult.addData("isLastPic", String.valueOf(uploadParams.isLastPic));
                    wVResult.addData("mutipleSelection", uploadParams.mutipleSelection);
                    wVResult.addData("tfsKey", uploadFileData.tfsKey);
                    if (uploadParams.isLastPic) {
                        wVResult.addData(SimpleDiskCache.DEFAULT_CACHE_IMAGE_DIR, uploadParams.images);
                    }
                    obtain.obj = wVResult;
                    TBUploadService.this.mHandler.sendMessage(obtain);
                }
            }

            public void onError(int i, String str) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TBUploadService.TAG, "upload file error. code: " + i + ";msg: " + str);
                }
                WVResult wVResult = new WVResult();
                wVResult.addData("errorCode", (Object) Integer.valueOf(i));
                wVResult.addData(ILocatable.ERROR_MSG, str);
                wVResult.addData("localPath", uploadParams.filePath);
                wVResult.addData("isLastPic", String.valueOf(uploadParams.isLastPic));
                wVResult.addData("mutipleSelection", uploadParams.mutipleSelection);
                Message obtain = Message.obtain();
                obtain.what = 2003;
                obtain.obj = wVResult;
                TBUploadService.this.mHandler.sendMessage(obtain);
            }
        }));
    }

    /* access modifiers changed from: private */
    public void doMtopUpload(final WVCamera.UploadParams uploadParams) {
        try {
            final File createTempFile = File.createTempFile("windvane", "." + MimeTypeEnum.JPG.getSuffix(), WVCacheManager.getInstance().getTempDir(true));
            if (!FileManager.copy(new File(uploadParams.filePath), createTempFile)) {
                WVResult wVResult = new WVResult();
                wVResult.addData("errorInfo", "Failed to copy file!");
                this.mCallback.error(wVResult);
                return;
            }
            final WVResult wVResult2 = new WVResult();
            try {
                UploaderCreator.get().uploadAsync(new IUploaderTask() {
                    public String getFileType() {
                        return ".jpg";
                    }

                    public String getBizType() {
                        return uploadParams.bizCode;
                    }

                    public String getFilePath() {
                        return createTempFile.getAbsolutePath();
                    }

                    public Map<String, String> getMetaInfo() {
                        if (uploadParams.extraInfo == null) {
                            return null;
                        }
                        HashMap hashMap = new HashMap();
                        Iterator<String> keys = uploadParams.extraInfo.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            hashMap.put(next, uploadParams.extraInfo.optString(next));
                        }
                        return hashMap;
                    }
                }, new ITaskListener() {
                    public void onCancel(IUploaderTask iUploaderTask) {
                    }

                    public void onPause(IUploaderTask iUploaderTask) {
                    }

                    public void onResume(IUploaderTask iUploaderTask) {
                    }

                    public void onStart(IUploaderTask iUploaderTask) {
                    }

                    public void onWait(IUploaderTask iUploaderTask) {
                    }

                    public void onSuccess(IUploaderTask iUploaderTask, ITaskResult iTaskResult) {
                        Bitmap readZoomImage;
                        wVResult2.setSuccess();
                        wVResult2.addData("url", uploadParams.localUrl);
                        wVResult2.addData("localPath", uploadParams.filePath);
                        String fileUrl = iTaskResult.getFileUrl();
                        wVResult2.addData("resourceURL", fileUrl);
                        wVResult2.addData("isLastPic", String.valueOf(uploadParams.isLastPic));
                        wVResult2.addData("mutipleSelection", uploadParams.mutipleSelection);
                        if (uploadParams.needBase64 && (readZoomImage = ImageTool.readZoomImage(uploadParams.filePath, 1024)) != null) {
                            wVResult2.addData("base64Data", WVUtils.bitmapToBase64(readZoomImage));
                        }
                        int lastIndexOf = fileUrl.lastIndexOf("/") + 1;
                        if (lastIndexOf != 0) {
                            wVResult2.addData("tfsKey", fileUrl.substring(lastIndexOf));
                        }
                        if (uploadParams.isLastPic) {
                            wVResult2.addData(SimpleDiskCache.DEFAULT_CACHE_IMAGE_DIR, uploadParams.images);
                        }
                        Message.obtain(TBUploadService.this.mHandler, 2002, wVResult2).sendToTarget();
                    }

                    public void onFailure(IUploaderTask iUploaderTask, TaskError taskError) {
                        wVResult2.addData("subCode", taskError.subcode);
                        wVResult2.addData("errorCode", taskError.code);
                        wVResult2.addData(ILocatable.ERROR_MSG, taskError.info);
                        wVResult2.addData("localPath", uploadParams.filePath);
                        Message.obtain(TBUploadService.this.mHandler, 2003, wVResult2).sendToTarget();
                    }

                    public void onProgress(IUploaderTask iUploaderTask, int i) {
                        String valueOf = String.valueOf(i);
                        TaoLog.e(TBUploadService.TAG, "uploadFile onProgress " + valueOf);
                    }
                }, this.mHandler);
                TaoLog.i(TAG, "do aus upload " + uploadParams.filePath);
            } catch (Throwable th) {
                TaoLog.e(TAG, "mtop sdk not exist." + th.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:58:0x00d3 A[Catch:{ JSONException -> 0x00d9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e5  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00ef  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleMessage(android.os.Message r10) {
        /*
            r9 = this;
            int r0 = r10.what
            r1 = 1
            switch(r0) {
                case 2001: goto L_0x0129;
                case 2002: goto L_0x003d;
                case 2003: goto L_0x0008;
                default: goto L_0x0006;
            }
        L_0x0006:
            r10 = 0
            return r10
        L_0x0008:
            java.lang.Object r0 = r10.obj
            if (r0 == 0) goto L_0x0037
            java.lang.Object r10 = r10.obj
            android.taobao.windvane.jsbridge.WVResult r10 = (android.taobao.windvane.jsbridge.WVResult) r10
            java.lang.String r0 = r10.toJsonString()
            java.lang.String r2 = "\"mutipleSelection\":\"1\""
            boolean r2 = r0.contains(r2)
            if (r2 == 0) goto L_0x0031
            android.taobao.windvane.jsbridge.WVCallBackContext r2 = r9.mCallback
            java.lang.String r3 = "WVPhoto.Event.uploadPhotoFailed"
            r2.fireEvent(r3, r0)
            java.lang.String r2 = "\"isLastPic\":\"true\""
            boolean r0 = r0.contains(r2)
            if (r0 == 0) goto L_0x003c
            android.taobao.windvane.jsbridge.WVCallBackContext r0 = r9.mCallback
            r0.error((android.taobao.windvane.jsbridge.WVResult) r10)
            goto L_0x003c
        L_0x0031:
            android.taobao.windvane.jsbridge.WVCallBackContext r0 = r9.mCallback
            r0.error((android.taobao.windvane.jsbridge.WVResult) r10)
            goto L_0x003c
        L_0x0037:
            android.taobao.windvane.jsbridge.WVCallBackContext r10 = r9.mCallback
            r10.error()
        L_0x003c:
            return r1
        L_0x003d:
            java.lang.Object r0 = r10.obj
            if (r0 == 0) goto L_0x0128
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x0065
            java.lang.String r0 = "TBUploadService"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "upload file success, retString: "
            r2.append(r3)
            java.lang.Object r3 = r10.obj
            android.taobao.windvane.jsbridge.WVResult r3 = (android.taobao.windvane.jsbridge.WVResult) r3
            java.lang.String r3 = r3.toJsonString()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r2)
        L_0x0065:
            java.lang.Object r10 = r10.obj
            android.taobao.windvane.jsbridge.WVResult r10 = (android.taobao.windvane.jsbridge.WVResult) r10
            java.lang.String r10 = r10.toJsonString()
            r0 = 0
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00af }
            r2.<init>(r10)     // Catch:{ JSONException -> 0x00af }
            java.lang.String r3 = "images"
            org.json.JSONArray r3 = r2.optJSONArray(r3)     // Catch:{ JSONException -> 0x00af }
            java.lang.String r4 = "url"
            java.lang.String r4 = r2.optString(r4)     // Catch:{ JSONException -> 0x00ac }
            java.lang.String r5 = "resourceURL"
            java.lang.String r5 = r2.optString(r5)     // Catch:{ JSONException -> 0x00a9 }
            java.lang.String r6 = "localPath"
            java.lang.String r6 = r2.optString(r6)     // Catch:{ JSONException -> 0x00a6 }
            java.lang.String r7 = "tfsKey"
            java.lang.String r7 = r2.optString(r7)     // Catch:{ JSONException -> 0x00a3 }
            java.lang.String r8 = "base64Data"
            boolean r8 = r2.has(r8)     // Catch:{ JSONException -> 0x00a1 }
            if (r8 == 0) goto L_0x00b8
            java.lang.String r8 = "base64Data"
            java.lang.String r2 = r2.optString(r8)     // Catch:{ JSONException -> 0x00a1 }
            r0 = r2
            goto L_0x00b8
        L_0x00a1:
            r2 = move-exception
            goto L_0x00b5
        L_0x00a3:
            r2 = move-exception
            r7 = r0
            goto L_0x00b5
        L_0x00a6:
            r2 = move-exception
            r6 = r0
            goto L_0x00b4
        L_0x00a9:
            r2 = move-exception
            r5 = r0
            goto L_0x00b3
        L_0x00ac:
            r2 = move-exception
            r4 = r0
            goto L_0x00b2
        L_0x00af:
            r2 = move-exception
            r3 = r0
            r4 = r3
        L_0x00b2:
            r5 = r4
        L_0x00b3:
            r6 = r5
        L_0x00b4:
            r7 = r6
        L_0x00b5:
            r2.printStackTrace()
        L_0x00b8:
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>()
            java.lang.String r8 = "url"
            r2.put(r8, r4)     // Catch:{ JSONException -> 0x00d9 }
            java.lang.String r4 = "resourceURL"
            r2.put(r4, r5)     // Catch:{ JSONException -> 0x00d9 }
            java.lang.String r4 = "localPath"
            r2.put(r4, r6)     // Catch:{ JSONException -> 0x00d9 }
            java.lang.String r4 = "tfsKey"
            r2.put(r4, r7)     // Catch:{ JSONException -> 0x00d9 }
            if (r0 == 0) goto L_0x00dd
            java.lang.String r4 = "base64Data"
            r2.put(r4, r0)     // Catch:{ JSONException -> 0x00d9 }
            goto L_0x00dd
        L_0x00d9:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00dd:
            java.lang.String r0 = "\"mutipleSelection\":\"1\""
            boolean r0 = r10.contains(r0)
            if (r0 != 0) goto L_0x00ef
            android.taobao.windvane.jsbridge.WVCallBackContext r10 = r9.mCallback
            java.lang.String r0 = r2.toString()
            r10.success((java.lang.String) r0)
            goto L_0x011d
        L_0x00ef:
            java.lang.String r0 = "\"isLastPic\":\"true\""
            boolean r10 = r10.contains(r0)
            if (r10 == 0) goto L_0x0112
            if (r3 != 0) goto L_0x0103
            android.taobao.windvane.jsbridge.WVCallBackContext r10 = r9.mCallback
            java.lang.String r0 = r2.toString()
            r10.success((java.lang.String) r0)
            goto L_0x0112
        L_0x0103:
            android.taobao.windvane.jsbridge.WVResult r10 = new android.taobao.windvane.jsbridge.WVResult
            r10.<init>()
            java.lang.String r0 = "images"
            r10.addData((java.lang.String) r0, (org.json.JSONArray) r3)
            android.taobao.windvane.jsbridge.WVCallBackContext r0 = r9.mCallback
            r0.success((android.taobao.windvane.jsbridge.WVResult) r10)
        L_0x0112:
            android.taobao.windvane.jsbridge.WVCallBackContext r10 = r9.mCallback
            java.lang.String r0 = "WVPhoto.Event.uploadPhotoSuccess"
            java.lang.String r2 = r2.toString()
            r10.fireEvent(r0, r2)
        L_0x011d:
            android.taobao.windvane.cache.WVCacheManager r10 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            java.io.File r10 = r10.getTempDir(r1)
            android.taobao.windvane.file.FileAccesser.deleteFile((java.io.File) r10)
        L_0x0128:
            return r1
        L_0x0129:
            java.lang.String r10 = "TBUploadService"
            java.lang.String r0 = "start upload file ..."
            android.taobao.windvane.util.TaoLog.d(r10, r0)
            android.taobao.windvane.jsbridge.WVCallBackContext r10 = r9.mCallback
            java.lang.String r0 = "WVPhoto.Event.prepareUploadPhotoSuccess"
            java.lang.String r2 = "{}"
            r10.fireEvent(r0, r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.jsbridge.TBUploadService.handleMessage(android.os.Message):boolean");
    }
}
