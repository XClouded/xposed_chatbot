package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.WVFileUtils;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.view.PopupWindowController;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.taobao.accs.common.Constants;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.phenix.compat.SimpleDiskCache;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.taobao.weex.ui.view.gesture.WXGestureType;

import org.android.agoo.common.AgooConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class WVCamera extends WVApiPlugin {
    public static final String LOCAL_IMAGE = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=";
    private static final String TAG = "WVCamera";
    public static int maxLength = 1024;
    private static String multiActivityClass;
    private static String uploadServiceClass;
    private long lastAccess = 0;
    /* access modifiers changed from: private */
    public WVCallBackContext mCallback = null;
    private String mLocalPath = null;
    private UploadParams mParams;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupController;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags = {"拍照", "从相册选择"};
    protected View.OnClickListener popupClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            WVCamera.this.mPopupController.hide();
            WVResult wVResult = new WVResult();
            if (WVCamera.this.mPopupMenuTags[0].equals(view.getTag())) {
                WVCamera.this.openCamara();
            } else if (WVCamera.this.mPopupMenuTags[1].equals(view.getTag())) {
                WVCamera.this.chosePhoto();
            } else {
                wVResult.addData("msg", "CANCELED_BY_USER");
                TaoLog.w("WVCamera", "take photo cancel, and callback.");
                WVCamera.this.mCallback.error(wVResult);
            }
        }
    };
    private WVUploadService uploadService;
    private boolean useCN = false;

    public boolean execute(String str, final String str2, final WVCallBackContext wVCallBackContext) {
        if ("takePhoto".equals(str)) {
            try {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        boolean unused = WVCamera.this.isAlive = true;
                        WVCamera.this.takePhoto(wVCallBackContext, str2);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("msg", "NO_PERMISSION");
                        wVCallBackContext.error(wVResult);
                    }
                }).execute();
            } catch (Exception unused) {
            }
        } else if (!"confirmUploadPhoto".equals(str)) {
            return false;
        } else {
            confirmUploadPhoto(wVCallBackContext, str2);
        }
        return true;
    }

    public void takePhotoPlus(WVCallBackContext wVCallBackContext, String str, String str2) {
        if (wVCallBackContext == null || str == null || str2 == null) {
            TaoLog.e("WVCamera", "takePhotoPlus fail, params error");
            return;
        }
        initTakePhoto(wVCallBackContext, str2);
        this.mLocalPath = str;
        zoomPicAndCallback(str, str, this.mParams);
    }

    public synchronized void takePhoto(WVCallBackContext wVCallBackContext, String str) {
        View peekDecorView;
        initTakePhoto(wVCallBackContext, str);
        if ((this.mContext instanceof Activity) && (peekDecorView = ((Activity) this.mContext).getWindow().peekDecorView()) != null) {
            ((InputMethodManager) this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(peekDecorView.getWindowToken(), 0);
        }
        if ("camera".equals(this.mParams.mode)) {
            openCamara();
        } else if ("photo".equals(this.mParams.mode)) {
            chosePhoto();
        } else {
            try {
                if (!EnvUtil.isCN() && !this.useCN) {
                    this.mPopupMenuTags[0] = "Take pictures";
                    this.mPopupMenuTags[1] = "Select from album";
                }
                if (this.mPopupController == null) {
                    this.mPopupController = new PopupWindowController(this.mContext, this.mWebView.getView(), this.mPopupMenuTags, this.popupClickListener);
                }
                this.mPopupController.show();
            } catch (Exception e) {
                TaoLog.w("WVCamera", e.getMessage());
            }
        }
        return;
    }

    /* access modifiers changed from: private */
    public void openCamara() {
        if (isHasCamaraPermission()) {
            TaoLog.d("WVCamera", "start to open system camera.");
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            UploadParams uploadParams = this.mParams;
            uploadParams.localUrl = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + System.currentTimeMillis();
            String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
            if (cacheDir != null) {
                File file = new File(cacheDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.mLocalPath = cacheDir + File.separator + DigestUtils.md5ToHex(this.mParams.localUrl);
                Uri fileUri = WVFileUtils.getFileUri(this.mContext, new File(this.mLocalPath));
                if (fileUri == null) {
                    WVResult wVResult = new WVResult();
                    wVResult.addData("msg", "image uri is null,check provider auth");
                    wVResult.setResult("CAMERA_OPEN_ERROR");
                    this.mCallback.error(wVResult);
                    return;
                }
                intent.putExtra("output", fileUri);
                intent.putExtra(WeexDevOptions.EXTRA_FROM, this.mWebView.hashCode());
                if (this.mContext instanceof Activity) {
                    ((Activity) this.mContext).startActivityForResult(intent, 4001);
                }
                Intent intent2 = new Intent("WVCameraFilter");
                intent2.putExtra("from-webview-id", this.mWebView.hashCode());
                LocalBroadcastManager.getInstance(GlobalConfig.context).sendBroadcast(intent2);
            } else if (this.mCallback != null) {
                WVResult wVResult2 = new WVResult();
                wVResult2.addData("msg", "NO_CACHEDIR");
                wVResult2.setResult("CAMERA_OPEN_ERROR");
                this.mCallback.error(wVResult2);
            }
        } else if (this.mCallback != null) {
            WVResult wVResult3 = new WVResult();
            wVResult3.addData("msg", "NO_PERMISSION");
            this.mCallback.error(wVResult3);
        }
    }

    private boolean isHasCamaraPermission() {
        try {
            Camera.open().release();
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void chosePhoto() {
        int i;
        Intent intent;
        TaoLog.d("WVCamera", "start to pick photo from system album.");
        if (!"1".equals(this.mParams.mutipleSelection)) {
            intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i = 4002;
        } else if (!this.mContext.getPackageName().equals(AgooConstants.TAOBAO_PACKAGE)) {
            WVResult wVResult = new WVResult();
            wVResult.addData("msg", "mutipleSelection only support in taobao!");
            this.mCallback.error(wVResult);
            return;
        } else {
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("taobao://go/ImgFileListActivity"));
            intent.putExtra("maxSelect", this.mParams.maxSelect);
            i = 4003;
        }
        if (this.mContext instanceof Activity) {
            try {
                ((Activity) this.mContext).startActivityForResult(intent, i);
                Intent intent2 = new Intent("WVCameraFilter");
                intent2.putExtra("from-webview-id", this.mWebView.hashCode());
                LocalBroadcastManager.getInstance(GlobalConfig.context).sendBroadcast(intent2);
            } catch (Throwable th) {
                th.printStackTrace();
                WVResult wVResult2 = new WVResult();
                wVResult2.setResult("ERROR_STARTACTIVITY");
                wVResult2.addData("msg", "ERROR_STARTACTIVITY");
                this.mCallback.error(wVResult2);
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVCamera", "takePhoto callback, requestCode: " + i + ";resultCode: " + i2);
        }
        WVResult wVResult = new WVResult();
        switch (i) {
            case 4001:
                if (i2 == -1) {
                    zoomPicAndCallback(this.mLocalPath, this.mLocalPath, this.mParams);
                    return;
                }
                TaoLog.w("WVCamera", "call takePhoto fail. resultCode: " + i2);
                wVResult.addData("msg", "CANCELED_BY_USER");
                this.mCallback.error(wVResult);
                return;
            case 4002:
                if (i2 != -1 || intent == null) {
                    TaoLog.w("WVCamera", "call pick photo fail. resultCode: " + i2);
                    wVResult.addData("msg", "CANCELED_BY_USER");
                    this.mCallback.error(wVResult);
                    return;
                }
                Uri data = intent.getData();
                String str = null;
                if (data != null) {
                    if ("file".equalsIgnoreCase(data.getScheme())) {
                        str = data.getPath();
                    } else {
                        String[] strArr = {"_data"};
                        Cursor query = this.mContext.getContentResolver().query(data, strArr, (String) null, (String[]) null, (String) null);
                        if (query == null || !query.moveToFirst()) {
                            TaoLog.w("WVCamera", "pick photo fail, Cursor is empty, imageUri: " + data.toString());
                        } else {
                            str = query.getString(query.getColumnIndex(strArr[0]));
                            query.close();
                        }
                    }
                }
                if (FileAccesser.exists(str)) {
                    UploadParams uploadParams = new UploadParams(this.mParams);
                    uploadParams.localUrl = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + System.currentTimeMillis();
                    zoomPicAndCallback(str, WVCacheManager.getInstance().getCacheDir(true) + File.separator + DigestUtils.md5ToHex(uploadParams.localUrl), uploadParams);
                    return;
                }
                TaoLog.w("WVCamera", "pick photo fail, picture not exist, picturePath: " + str);
                return;
            case 4003:
                if (intent == null || intent.getExtras() == null || intent.getExtras().get("fileList") == null) {
                    wVResult.addData("msg", "CANCELED_BY_USER");
                    this.mCallback.error(wVResult);
                    return;
                }
                ArrayList arrayList = (ArrayList) intent.getExtras().get("fileList");
                int size = arrayList.size();
                if (size == 0) {
                    wVResult.addData("msg", "CANCELED_BY_USER");
                    this.mCallback.error(wVResult);
                    return;
                }
                JSONArray jSONArray = new JSONArray();
                for (int i3 = 0; i3 < size; i3++) {
                    String str2 = (String) arrayList.get(i3);
                    if (FileAccesser.exists(str2)) {
                        UploadParams uploadParams2 = new UploadParams(this.mParams);
                        uploadParams2.localUrl = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + System.currentTimeMillis();
                        String str3 = WVCacheManager.getInstance().getCacheDir(true) + File.separator + DigestUtils.md5ToHex(uploadParams2.localUrl);
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("url", uploadParams2.localUrl);
                            jSONObject.put("localPath", str3);
                            jSONArray.put(jSONObject);
                            TaoLog.d("WVCamera", "url:" + uploadParams2.localUrl + " localPath:" + str3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (i3 == size - 1) {
                            uploadParams2.images = jSONArray;
                        } else {
                            uploadParams2.isLastPic = false;
                        }
                        zoomPicAndCallback(str2, str3, uploadParams2);
                        try {
                            Thread.sleep(100);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        TaoLog.w("WVCamera", "pick photo fail, picture not exist, picturePath: " + str2);
                    }
                }
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00ed */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A[RETURN, SYNTHETIC] */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zoomPicAndCallback(java.lang.String r8, java.lang.String r9, android.taobao.windvane.jsbridge.api.WVCamera.UploadParams r10) {
        /*
            r7 = this;
            r0 = 0
            android.taobao.windvane.jsbridge.api.WVCamera$UploadParams r1 = r7.mParams     // Catch:{ Exception -> 0x00ed }
            boolean r1 = r1.needZoom     // Catch:{ Exception -> 0x00ed }
            if (r1 == 0) goto L_0x003b
            int r1 = android.taobao.windvane.util.ImageTool.readRotationDegree(r8)     // Catch:{ Exception -> 0x00ed }
            int r2 = maxLength     // Catch:{ Exception -> 0x00ed }
            android.graphics.Bitmap r8 = android.taobao.windvane.util.ImageTool.readZoomImage(r8, r2)     // Catch:{ Exception -> 0x00ed }
            if (r8 != 0) goto L_0x001f
            if (r8 == 0) goto L_0x001e
            boolean r9 = r8.isRecycled()
            if (r9 != 0) goto L_0x001e
            r8.recycle()
        L_0x001e:
            return
        L_0x001f:
            int r2 = maxLength     // Catch:{ Exception -> 0x0038, all -> 0x0033 }
            android.graphics.Bitmap r2 = android.taobao.windvane.util.ImageTool.zoomBitmap(r8, r2)     // Catch:{ Exception -> 0x0038, all -> 0x0033 }
            android.graphics.Bitmap r8 = android.taobao.windvane.util.ImageTool.rotate(r2, r1)     // Catch:{ Exception -> 0x0030, all -> 0x002c }
            r3 = r8
            goto L_0x009e
        L_0x002c:
            r8 = move-exception
            r0 = r2
            goto L_0x0116
        L_0x0030:
            r0 = r2
            goto L_0x00ed
        L_0x0033:
            r9 = move-exception
            r0 = r8
            r8 = r9
            goto L_0x0116
        L_0x0038:
            r0 = r8
            goto L_0x00ed
        L_0x003b:
            boolean r1 = r9.equals(r8)     // Catch:{ Exception -> 0x00ed }
            if (r1 != 0) goto L_0x009d
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x00ed }
            r1.<init>(r9)     // Catch:{ Exception -> 0x00ed }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x00ed }
            if (r2 != 0) goto L_0x004f
            r1.createNewFile()     // Catch:{ Exception -> 0x00ed }
        L_0x004f:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x00ed }
            r2.<init>(r8)     // Catch:{ Exception -> 0x00ed }
            boolean r3 = r2.exists()     // Catch:{ Exception -> 0x00ed }
            if (r3 != 0) goto L_0x0080
            android.taobao.windvane.jsbridge.WVResult r9 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Exception -> 0x00ed }
            r9.<init>()     // Catch:{ Exception -> 0x00ed }
            java.lang.String r10 = "msg"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ed }
            r1.<init>()     // Catch:{ Exception -> 0x00ed }
            java.lang.String r2 = "Failed to read file : "
            r1.append(r2)     // Catch:{ Exception -> 0x00ed }
            r1.append(r8)     // Catch:{ Exception -> 0x00ed }
            java.lang.String r8 = r1.toString()     // Catch:{ Exception -> 0x00ed }
            r9.addData((java.lang.String) r10, (java.lang.String) r8)     // Catch:{ Exception -> 0x00ed }
            java.lang.String r8 = "READ_FILE_ERROR"
            r9.setResult(r8)     // Catch:{ Exception -> 0x00ed }
            android.taobao.windvane.jsbridge.WVCallBackContext r8 = r7.mCallback     // Catch:{ Exception -> 0x00ed }
            r8.error((android.taobao.windvane.jsbridge.WVResult) r9)     // Catch:{ Exception -> 0x00ed }
            return
        L_0x0080:
            boolean r8 = android.taobao.windvane.file.FileManager.copy((java.io.File) r2, (java.io.File) r1)     // Catch:{ Exception -> 0x00ed }
            if (r8 != 0) goto L_0x009d
            android.taobao.windvane.jsbridge.WVResult r8 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ Exception -> 0x00ed }
            r8.<init>()     // Catch:{ Exception -> 0x00ed }
            java.lang.String r9 = "msg"
            java.lang.String r10 = "Failed to copy file!"
            r8.addData((java.lang.String) r9, (java.lang.String) r10)     // Catch:{ Exception -> 0x00ed }
            java.lang.String r9 = "COPY_FILE_ERROR"
            r8.setResult(r9)     // Catch:{ Exception -> 0x00ed }
            android.taobao.windvane.jsbridge.WVCallBackContext r9 = r7.mCallback     // Catch:{ Exception -> 0x00ed }
            r9.error((android.taobao.windvane.jsbridge.WVResult) r8)     // Catch:{ Exception -> 0x00ed }
            return
        L_0x009d:
            r3 = r0
        L_0x009e:
            android.taobao.windvane.cache.WVFileInfo r4 = new android.taobao.windvane.cache.WVFileInfo     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            r4.<init>()     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            java.lang.String r8 = r10.localUrl     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            java.lang.String r8 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r8)     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            r4.fileName = r8     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            java.lang.String r8 = "image/jpeg"
            r4.mimeType = r8     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            r5 = 2592000000(0x9a7ec800, double:1.280618154E-314)
            long r1 = r1 + r5
            r4.expireTime = r1     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            boolean r8 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            if (r8 == 0) goto L_0x00d9
            java.lang.String r8 = "WVCamera"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            r1.<init>()     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            java.lang.String r2 = "write pic to file, name: "
            r1.append(r2)     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            java.lang.String r2 = r4.fileName     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            r1.append(r2)     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
            android.taobao.windvane.util.TaoLog.d(r8, r1)     // Catch:{ Exception -> 0x00e9, all -> 0x00e6 }
        L_0x00d9:
            android.taobao.windvane.jsbridge.api.WVCamera$4 r8 = new android.taobao.windvane.jsbridge.api.WVCamera$4     // Catch:{ Exception -> 0x00ed }
            r1 = r8
            r2 = r7
            r5 = r9
            r6 = r10
            r1.<init>(r3, r4, r5, r6)     // Catch:{ Exception -> 0x00ed }
            android.os.AsyncTask.execute(r8)     // Catch:{ Exception -> 0x00ed }
            goto L_0x0115
        L_0x00e6:
            r8 = move-exception
            r0 = r3
            goto L_0x0116
        L_0x00e9:
            r0 = r3
            goto L_0x00ed
        L_0x00eb:
            r8 = move-exception
            goto L_0x0116
        L_0x00ed:
            android.taobao.windvane.jsbridge.WVResult r8 = new android.taobao.windvane.jsbridge.WVResult     // Catch:{ all -> 0x00eb }
            r8.<init>()     // Catch:{ all -> 0x00eb }
            java.lang.String r9 = "reason"
            java.lang.String r10 = "write photo io error."
            r8.addData((java.lang.String) r9, (java.lang.String) r10)     // Catch:{ all -> 0x00eb }
            java.lang.String r9 = "WRITE_PHOTO_ERROR"
            r8.setResult(r9)     // Catch:{ all -> 0x00eb }
            android.taobao.windvane.jsbridge.WVCallBackContext r9 = r7.mCallback     // Catch:{ all -> 0x00eb }
            r9.error((android.taobao.windvane.jsbridge.WVResult) r8)     // Catch:{ all -> 0x00eb }
            java.lang.String r8 = "WVCamera"
            java.lang.String r9 = "write photo io error."
            android.taobao.windvane.util.TaoLog.e(r8, r9)     // Catch:{ all -> 0x00eb }
            if (r0 == 0) goto L_0x0115
            boolean r8 = r0.isRecycled()
            if (r8 != 0) goto L_0x0115
            r0.recycle()
        L_0x0115:
            return
        L_0x0116:
            if (r0 == 0) goto L_0x0121
            boolean r9 = r0.isRecycled()
            if (r9 != 0) goto L_0x0121
            r0.recycle()
        L_0x0121:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVCamera.zoomPicAndCallback(java.lang.String, java.lang.String, android.taobao.windvane.jsbridge.api.WVCamera$UploadParams):void");
    }

    public synchronized void confirmUploadPhoto(WVCallBackContext wVCallBackContext, String str) {
        this.mCallback = wVCallBackContext;
        UploadParams uploadParams = new UploadParams();
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("path");
            uploadParams.identifier = jSONObject.optString(WXGestureType.GestureInfo.POINTER_ID);
            uploadParams.v = jSONObject.optString("v");
            uploadParams.bizCode = jSONObject.optString("bizCode");
            uploadParams.extraInfo = jSONObject.optJSONObject("extraInfo");
            String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
            if (string == null || cacheDir == null || !string.startsWith(cacheDir)) {
                wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
            } else {
                uploadParams.filePath = string;
                upload(uploadParams);
            }
        } catch (Exception e) {
            TaoLog.e("WVCamera", "confirmUploadPhoto fail, params: " + str);
            WVResult wVResult = new WVResult();
            wVResult.setResult("HY_PARAM_ERR");
            wVResult.addData("msg", "PARAM_ERROR :" + e.getMessage());
            wVCallBackContext.error(wVResult);
        }
    }

    /* access modifiers changed from: private */
    public void takePhotoSuccess(String str, UploadParams uploadParams) {
        Bitmap readZoomImage;
        Bitmap readZoomImage2;
        if (uploadParams.type == 1) {
            String cacheDir = WVCacheManager.getInstance().getCacheDir(true);
            if (str == null || cacheDir == null || !str.startsWith(cacheDir)) {
                WVResult wVResult = new WVResult();
                wVResult.setResult("PIC_PATH_ERROR");
                wVResult.addData("msg", "PIC_PATH_ERROR");
                this.mCallback.error(wVResult);
                return;
            }
            uploadParams.filePath = str;
            upload(uploadParams);
            return;
        }
        WVResult wVResult2 = new WVResult();
        wVResult2.setSuccess();
        if (!"1".equals(uploadParams.mutipleSelection)) {
            wVResult2.addData("url", uploadParams.localUrl);
            wVResult2.addData("localPath", str);
            if (uploadParams.needBase64 && (readZoomImage = ImageTool.readZoomImage(str, 1024)) != null) {
                wVResult2.addData("base64Data", WVUtils.bitmapToBase64(readZoomImage));
            }
            TaoLog.d("WVCamera", "url:" + uploadParams.localUrl + " localPath:" + str);
            this.mCallback.success(wVResult2);
        } else if (uploadParams.isLastPic) {
            if (uploadParams.images == null) {
                wVResult2.addData("url", uploadParams.localUrl);
                wVResult2.addData("localPath", str);
                if (uploadParams.needBase64 && (readZoomImage2 = ImageTool.readZoomImage(str, 1024)) != null) {
                    wVResult2.addData("base64Data", WVUtils.bitmapToBase64(readZoomImage2));
                }
            } else {
                wVResult2.addData(SimpleDiskCache.DEFAULT_CACHE_IMAGE_DIR, uploadParams.images);
            }
            this.mCallback.success(wVResult2);
        } else {
            return;
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVCamera", "pic not upload and call success, retString: " + wVResult2.toJsonString());
        }
    }

    public class UploadParams {
        public String bizCode;
        public String extraData;
        public JSONObject extraInfo;
        public String filePath;
        public String identifier = "";
        public JSONArray images = null;
        public boolean isLastPic = true;
        public String localUrl;
        public int maxSelect = 9;
        public String mode = ProtocolConst.VAL_CORNER_TYPE_BOTH;
        public String mutipleSelection = "0";
        public boolean needBase64 = false;
        public boolean needLogin = false;
        public boolean needZoom = true;
        public int type;
        public String v;

        public UploadParams() {
        }

        public UploadParams(UploadParams uploadParams) {
            this.filePath = uploadParams.filePath;
            this.localUrl = uploadParams.localUrl;
            this.type = uploadParams.type;
            this.v = uploadParams.v;
            this.bizCode = uploadParams.bizCode;
            this.extraData = uploadParams.extraData;
            this.identifier = uploadParams.identifier;
            this.mode = uploadParams.mode;
            this.mutipleSelection = uploadParams.mutipleSelection;
            this.maxSelect = uploadParams.maxSelect;
            this.isLastPic = uploadParams.isLastPic;
            this.images = uploadParams.images;
            this.needZoom = uploadParams.needZoom;
            this.needLogin = uploadParams.needLogin;
            this.needBase64 = uploadParams.needBase64;
        }
    }

    /* access modifiers changed from: protected */
    public void upload(UploadParams uploadParams) {
        if (this.uploadService == null && uploadServiceClass != null) {
            try {
                Class<?> cls = Class.forName(uploadServiceClass);
                if (cls != null && WVUploadService.class.isAssignableFrom(cls)) {
                    this.uploadService = (WVUploadService) cls.newInstance();
                    this.uploadService.initialize(this.mContext, this.mWebView);
                }
            } catch (Exception e) {
                TaoLog.e("WVCamera", "create upload service error: " + uploadServiceClass + ". " + e.getMessage());
            }
        }
        if (this.uploadService != null) {
            this.uploadService.doUpload(uploadParams, this.mCallback);
        }
    }

    public static void registerUploadService(Class<? extends WVUploadService> cls) {
        if (cls != null) {
            uploadServiceClass = cls.getName();
        }
    }

    public static void registerMultiActivity(Class<? extends Activity> cls) {
        if (cls != null) {
            multiActivityClass = cls.getName();
        }
    }

    public static void registerMultiActivityName(String str) {
        if (str != null) {
            multiActivityClass = str;
        }
    }

    private void initTakePhoto(WVCallBackContext wVCallBackContext, String str) {
        if (this.isAlive) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastAccess;
            this.lastAccess = currentTimeMillis;
            if (j < 1000) {
                TaoLog.w("WVCamera", "takePhoto, call this method too frequent,  " + j);
                return;
            }
            this.mCallback = wVCallBackContext;
            this.mParams = new UploadParams();
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.mParams.type = jSONObject.optInt("type", 1);
                this.mParams.mode = jSONObject.optString(Constants.KEY_MODE);
                this.mParams.v = jSONObject.optString("v");
                this.mParams.bizCode = jSONObject.optString("bizCode");
                this.mParams.extraData = jSONObject.optString("extraData");
                this.mParams.extraInfo = jSONObject.optJSONObject("extraInfo");
                this.mParams.identifier = jSONObject.optString(WXGestureType.GestureInfo.POINTER_ID);
                this.mParams.maxSelect = jSONObject.optInt("maxSelect");
                this.mParams.mutipleSelection = jSONObject.optString("mutipleSelection");
                this.mParams.needZoom = jSONObject.optBoolean("needZoom", true);
                this.mParams.isLastPic = true;
                this.mParams.needLogin = jSONObject.optBoolean("needLogin", false);
                this.mParams.needBase64 = jSONObject.optBoolean("needBase64", false);
                maxLength = jSONObject.optInt("maxLength", 1024);
                this.useCN = jSONObject.optBoolean("lang", false);
                if (jSONObject.has("localUrl")) {
                    this.mParams.localUrl = jSONObject.optString("localUrl");
                }
            } catch (Exception unused) {
                TaoLog.e("WVCamera", "takePhoto fail, params: " + str);
                WVResult wVResult = new WVResult();
                wVResult.setResult("HY_PARAM_ERR");
                wVResult.addData("msg", "PHOTO_INIT_ERROR ,params:" + str);
                this.mCallback.error(wVResult);
            }
        }
    }
}
