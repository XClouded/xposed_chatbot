package android.taobao.windvane.extra.core;

import android.app.Application;
import android.content.Context;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.extra.uc.WVCoreSettings;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.CoreEventCallback;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;

import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.UCCore;

import java.io.File;

public class WVCore {
    public static final String TAG = "WVCore";
    private static WVCore instance;
    private CoreDownLoadBack coreDownLoadBack;
    private boolean isUCSDKSupport = false;
    private boolean open4GDownload = false;

    public interface CoreDownLoadBack {
        void initError();

        void progress(int i);
    }

    public static WVCore getInstance() {
        if (instance == null) {
            synchronized (WVCore.class) {
                if (instance == null) {
                    instance = new WVCore();
                }
            }
        }
        return instance;
    }

    public boolean isUCSupport() {
        return this.isUCSDKSupport && WebView.getCoreType() == 3;
    }

    public void setUCSupport(boolean z) {
        this.isUCSDKSupport = z;
    }

    public String getV8SoPath() {
        if (WVUCWebView.INNER) {
            StringBuilder sb = new StringBuilder();
            Application application = GlobalConfig.context;
            sb.append(UCCore.getExtractDirPath((Context) application, GlobalConfig.context.getApplicationInfo().nativeLibraryDir + "/" + "libkernelu4_7z_uc.so"));
            sb.append("/lib/libwebviewuc.so");
            String sb2 = sb.toString();
            String str = TAG;
            TaoLog.i(str, "get v8 path by inner so, path=[" + sb2 + Operators.ARRAY_END_STR);
            return sb2;
        }
        String ucSoPath = getUcSoPath(UCCore.getExtractDirPathByUrl(GlobalConfig.context, WVUCWebView.UC_CORE_URL));
        String str2 = TAG;
        TaoLog.i(str2, "get v8 path by download so, path=[" + ucSoPath + Operators.ARRAY_END_STR);
        return ucSoPath;
    }

    public void initUCCore(Context context, String[] strArr, String str, CoreEventCallback coreEventCallback) {
        if (context instanceof Application) {
            GlobalConfig.context = (Application) context;
            if (!TextUtils.isEmpty(str) && new File(str).exists()) {
                GlobalConfig.getInstance().setUc7ZPath(str);
            }
            WVCoreSettings.getInstance().setCoreEventCallback(coreEventCallback);
            GlobalConfig.getInstance().setUcsdkappkeySec(strArr);
            WVUCWebView.initUCCore(context);
            return;
        }
        new AndroidRuntimeException("cannot init uccore for context is not application").printStackTrace();
    }

    private String getUcSoPath(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles == null) {
            return "";
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                String ucSoPath = getUcSoPath(file.getPath());
                if (ucSoPath.endsWith("libwebviewuc.so")) {
                    return ucSoPath;
                }
            } else if (file.getName().endsWith("libwebviewuc.so")) {
                return file.getPath();
            }
        }
        return "";
    }

    public void setOpen4GDownload(boolean z) {
        this.open4GDownload = z;
    }

    public boolean isOpen4GDownload() {
        return this.open4GDownload || GlobalConfig.getInstance().isOpen4GDownload();
    }

    public void setCoreDownLoadBack(CoreDownLoadBack coreDownLoadBack2) {
        this.coreDownLoadBack = coreDownLoadBack2;
    }

    public CoreDownLoadBack getCoreDownLoadBack() {
        return this.coreDownLoadBack;
    }

    public void startDownload() {
        UCCore.startDownload();
    }
}
