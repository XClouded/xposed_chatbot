package android.taobao.windvane.jsbridge.api;

import android.app.Activity;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.util.DigestUtils;
import android.text.TextUtils;

import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.weex.el.parse.Operators;

import org.json.JSONObject;

import java.io.File;

import alimama.com.unwetaologger.base.LogContent;

public class WVScreen extends WVApiPlugin {
    private static final String TAG = "WVScreen";

    public boolean execute(String str, final String str2, final WVCallBackContext wVCallBackContext) {
        if ("capture".equals(str)) {
            if (this.mContext != null) {
                PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        WVScreen.this.capture(wVCallBackContext, str2);
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        WVResult wVResult = new WVResult();
                        wVResult.addData("msg", "no permission");
                        wVCallBackContext.error(wVResult);
                    }
                }).execute();
            }
        } else if ("getOrientation".equals(str)) {
            getOrientation(wVCallBackContext, str2);
        } else if (!"setOrientation".equals(str)) {
            return false;
        } else {
            setOrientation(wVCallBackContext, str2);
        }
        return true;
    }

    public void capture(WVCallBackContext wVCallBackContext, String str) {
        boolean z;
        long j;
        long j2;
        long j3;
        WVCallBackContext wVCallBackContext2 = wVCallBackContext;
        WVResult wVResult = new WVResult();
        String str2 = "";
        String str3 = LogContent.LOG_VALUE_SOURCE_DEFAULT;
        int i = 50;
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString("inAlbum", "false");
                String optString2 = jSONObject.optString("type", "view");
                long optLong = jSONObject.optLong("maxShortSide", 10240);
                long optLong2 = jSONObject.optLong("maxLongSide", 10240);
                int optInt = jSONObject.optInt("quality", 50);
                if (optInt <= 100) {
                    if (optInt >= 0) {
                        i = optInt;
                    }
                }
                z = jSONObject.optBoolean(ProtocolConst.KEY_COMPRESS, true);
                str2 = optString;
                str3 = optString2;
                j2 = optLong2;
                j = optLong;
            } catch (Exception e) {
                wVResult.addData("msg", "param error: [" + e.getMessage() + Operators.ARRAY_END_STR);
                wVCallBackContext2.error(wVResult);
                return;
            }
        } else {
            j2 = 10240;
            j = 10240;
            z = true;
        }
        boolean z2 = !"false".equals(str2);
        try {
            if (str3.equals(LogContent.LOG_VALUE_SOURCE_DEFAULT)) {
                j3 = ScreenCaptureUtil.captureByActivty((Activity) this.mContext, z2, (long) i, j2, j, z);
            } else {
                j3 = ScreenCaptureUtil.capture(this.mWebView.getView(), z2, (long) i, j2, j, z);
            }
            String virtualPath = WVUtils.getVirtualPath(Long.valueOf(j3));
            wVResult.addData("url", virtualPath);
            wVResult.addData("localPath", WVCacheManager.getInstance().getCacheDir(true) + File.separator + DigestUtils.md5ToHex(virtualPath));
            wVCallBackContext2.success(wVResult);
        } catch (Exception unused) {
            wVCallBackContext.error();
        }
    }

    public void getOrientation(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        if (!(this.mContext instanceof Activity)) {
            wVResult.addData("error", "Context must be Activty!");
            wVCallBackContext.error(wVResult);
            return;
        }
        int requestedOrientation = ((Activity) this.mContext).getRequestedOrientation();
        wVResult.addData("orientation", requestedOrientation == 0 ? "landscape" : requestedOrientation == 1 ? "portrait" : "unknown");
        wVCallBackContext.success(wVResult);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setOrientation(android.taobao.windvane.jsbridge.WVCallBackContext r4, java.lang.String r5) {
        /*
            r3 = this;
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult
            r0.<init>()
            java.lang.String r0 = ""
            boolean r1 = android.text.TextUtils.isEmpty(r5)
            if (r1 != 0) goto L_0x0025
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x001b }
            r1.<init>(r5)     // Catch:{ Exception -> 0x001b }
            java.lang.String r5 = "orientation"
            java.lang.String r2 = ""
            java.lang.String r5 = r1.optString(r5, r2)     // Catch:{ Exception -> 0x001b }
            goto L_0x0026
        L_0x001b:
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult
            java.lang.String r1 = "HY_PARAM_ERR"
            r5.<init>(r1)
            r4.error((android.taobao.windvane.jsbridge.WVResult) r5)
        L_0x0025:
            r5 = r0
        L_0x0026:
            android.content.Context r0 = r3.mContext
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 != 0) goto L_0x003c
            android.taobao.windvane.jsbridge.WVResult r5 = new android.taobao.windvane.jsbridge.WVResult
            r5.<init>()
            java.lang.String r0 = "error"
            java.lang.String r1 = "Context must be Activty!"
            r5.addData((java.lang.String) r0, (java.lang.String) r1)
            r4.error((android.taobao.windvane.jsbridge.WVResult) r5)
            return
        L_0x003c:
            android.content.Context r0 = r3.mContext
            android.app.Activity r0 = (android.app.Activity) r0
            java.lang.String r1 = "landscape"
            boolean r1 = r5.equals(r1)
            if (r1 != 0) goto L_0x0094
            java.lang.String r1 = "landscapeRight"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L_0x0051
            goto L_0x0094
        L_0x0051:
            java.lang.String r1 = "landscapeLeft"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L_0x005f
            r5 = 8
            r0.setRequestedOrientation(r5)
            goto L_0x0098
        L_0x005f:
            java.lang.String r1 = "portrait"
            boolean r1 = r5.equals(r1)
            if (r1 != 0) goto L_0x008f
            java.lang.String r1 = "default"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L_0x0070
            goto L_0x008f
        L_0x0070:
            java.lang.String r1 = "portraitUpsideDown"
            boolean r1 = r5.equals(r1)
            if (r1 == 0) goto L_0x007e
            r5 = 9
            r0.setRequestedOrientation(r5)
            goto L_0x0098
        L_0x007e:
            java.lang.String r1 = "auto"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x008b
            r5 = 4
            r0.setRequestedOrientation(r5)
            goto L_0x0098
        L_0x008b:
            r4.error()
            return
        L_0x008f:
            r5 = 1
            r0.setRequestedOrientation(r5)
            goto L_0x0098
        L_0x0094:
            r5 = 0
            r0.setRequestedOrientation(r5)
        L_0x0098:
            r4.success()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVScreen.setOrientation(android.taobao.windvane.jsbridge.WVCallBackContext, java.lang.String):void");
    }
}
