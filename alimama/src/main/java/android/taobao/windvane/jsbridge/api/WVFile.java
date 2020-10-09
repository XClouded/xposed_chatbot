package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;

import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.taobao.accs.common.Constants;
import com.taobao.alivfsadapter.MonitorCacheEvent;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WVFile extends WVApiPlugin {
    public static final long FILE_MAX_SIZE = 5242880;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (MonitorCacheEvent.OPERATION_READ.equals(str)) {
            read(str2, wVCallBackContext);
            return true;
        } else if (!MonitorCacheEvent.OPERATION_WRITE.equals(str)) {
            return false;
        } else {
            write(str2, wVCallBackContext);
            return true;
        }
    }

    public final void read(String str, WVCallBackContext wVCallBackContext) {
        String str2;
        String str3;
        String str4 = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                str2 = jSONObject.optString("fileName");
                str4 = jSONObject.optString("share", "false");
                if (str2 == null || str2.contains(File.separator)) {
                    throw new Exception();
                }
            } catch (Exception unused) {
                wVCallBackContext.error(new WVResult("HY_PARAM_ERR"));
                return;
            }
        } else {
            str2 = "";
        }
        String cacheDir = WVCacheManager.getInstance().getCacheDir(false);
        if (cacheDir == null) {
            WVResult wVResult = new WVResult();
            wVResult.addData("error", "GET_DIR_FAILED");
            wVCallBackContext.error(wVResult);
            return;
        }
        if (!"true".equalsIgnoreCase(str4)) {
            String str5 = cacheDir + File.separator;
            str3 = str5 + WVUtils.getHost(this.mWebView.getUrl());
        } else {
            str3 = (cacheDir + File.separator) + "wvShareFiles";
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str3 + File.separator + str2));
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            String str6 = new String(bArr, "UTF-8");
            fileInputStream.close();
            WVResult wVResult2 = new WVResult();
            wVResult2.addData("data", str6);
            wVCallBackContext.success(wVResult2);
        } catch (FileNotFoundException unused2) {
            WVResult wVResult3 = new WVResult();
            wVResult3.addData("error", "FILE_NOT_FOUND");
            wVCallBackContext.error(wVResult3);
        } catch (Exception e) {
            WVResult wVResult4 = new WVResult();
            wVResult4.addData("error", "READ_FILE_FAILED");
            wVCallBackContext.error(wVResult4);
            e.printStackTrace();
        }
    }

    public final void write(String str, WVCallBackContext wVCallBackContext) {
        String str2;
        String str3;
        String str4 = "";
        String str5 = "";
        String str6 = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                str2 = jSONObject.optString(Constants.KEY_MODE, MonitorCacheEvent.OPERATION_WRITE);
                str4 = jSONObject.optString("data");
                str5 = jSONObject.optString("fileName");
                str6 = jSONObject.optString("share", "false");
                if (str2 == null || str5 == null || str5.contains(File.separator)) {
                    throw new Exception();
                }
            } catch (Exception unused) {
                WVResult wVResult = new WVResult();
                wVResult.addData("error", ILocatable.ErrorMsg.PARAMS_ERROR);
                wVCallBackContext.error(wVResult);
                return;
            }
        } else {
            str2 = "";
        }
        String cacheDir = WVCacheManager.getInstance().getCacheDir(false);
        if (cacheDir == null) {
            WVResult wVResult2 = new WVResult();
            wVResult2.addData("error", "GET_DIR_FAILED");
            wVCallBackContext.error(wVResult2);
            return;
        }
        if (!"true".equalsIgnoreCase(str6)) {
            String str7 = cacheDir + File.separator;
            str3 = str7 + WVUtils.getHost(this.mWebView.getUrl());
        } else {
            str3 = (cacheDir + File.separator) + "wvShareFiles";
        }
        File file = new File(str3);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(str3 + File.separator + str5);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException unused2) {
                WVResult wVResult3 = new WVResult();
                wVResult3.addData("error", "MAKE_FILE_FAILED");
                wVCallBackContext.error(wVResult3);
                return;
            }
        } else if (MonitorCacheEvent.OPERATION_WRITE.equalsIgnoreCase(str2)) {
            WVResult wVResult4 = new WVResult();
            wVResult4.addData("error", "FILE_EXIST");
            wVCallBackContext.error(wVResult4);
            return;
        }
        try {
            boolean equalsIgnoreCase = "append".equalsIgnoreCase(str2);
            if (!canWriteFile(file2.length(), str4, equalsIgnoreCase)) {
                WVResult wVResult5 = new WVResult();
                wVResult5.addData("error", "FILE_TOO_LARGE");
                wVCallBackContext.error(wVResult5);
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2, equalsIgnoreCase);
            fileOutputStream.write(str4.getBytes());
            fileOutputStream.close();
            wVCallBackContext.success();
        } catch (Exception e) {
            WVResult wVResult6 = new WVResult();
            wVResult6.addData("error", "WRITE_FILE_FAILED");
            wVCallBackContext.error(wVResult6);
            e.printStackTrace();
        }
    }

    private boolean canWriteFile(long j, String str, boolean z) {
        long j2;
        if (z) {
            j2 = j + ((long) str.length());
        } else {
            j2 = (long) str.length();
        }
        return j2 <= FILE_MAX_SIZE;
    }
}
