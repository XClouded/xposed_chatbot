package android.taobao.windvane.jsbridge.api;

import android.os.Build;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.debug.WVPageFinishJSRender;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.monitor.WVLocPerformanceMonitor;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.WVPackageAppTool;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.log.AndroidLog;
import android.taobao.windvane.webview.WVWebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.orange.OConstant;
import com.taobao.weex.ui.component.WXBasicComponentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class WVDevelopTool extends WVApiPlugin {
    private static final String TAG = "WVDevelopTool";
    private static int mLastMode;
    private boolean mIsDebugOpen = false;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("isDebugEnabled".equals(str)) {
            WVResult wVResult = new WVResult();
            wVResult.addData(ProtocolConst.KEY_GLOBAL, String.valueOf(EnvUtil.isDebug()));
            wVCallBackContext.success(wVResult);
            return true;
        } else if ("clearWindVaneCache".equals(str)) {
            clearWindVaneCache(str2, wVCallBackContext);
            return true;
        } else if ("setWebViewDebugEnabled".equals(str)) {
            setWebViewDebugEnabled(str2, wVCallBackContext);
            return true;
        } else if ("setWebViewFinishJs".equals(str)) {
            setWebViewFinishJs(str2, wVCallBackContext);
            return true;
        } else if ("clearWebViewFinishJs".equals(str)) {
            clearWebViewFinishJs(str2, wVCallBackContext);
            return true;
        } else if ("setPackageAppEnabled".equals(str)) {
            setPackageAppEnabled(str2, wVCallBackContext);
            return true;
        } else if ("isPackageAppEnabled".equals(str)) {
            isPackageAppEnabled(str2, wVCallBackContext);
            return true;
        } else if ("setUCEnabled".equals(str)) {
            setUCEnabled(str2, wVCallBackContext);
            return true;
        } else if ("isUCEnabled".equals(str)) {
            isUCEnabled(str2, wVCallBackContext);
            return true;
        } else if ("readPackageAppMemoryInfo".equals(str)) {
            readPackageAppMemoryInfo(str2, wVCallBackContext);
            return true;
        } else if ("readMemoryZCacheMap".equals(str)) {
            readMemoryZCacheMap(str2, wVCallBackContext);
            return true;
        } else if ("readMemoryPrefixes".equals(str)) {
            readMemoryPrefixes(str2, wVCallBackContext);
            return true;
        } else if ("readPackageAppDiskConfig".equals(str)) {
            readPackageAppDiskConfig(str2, wVCallBackContext);
            return true;
        } else if ("readPackageAppDiskFileList".equals(str)) {
            readPackageAppDiskFileList(str2, wVCallBackContext);
            return true;
        } else if ("clearPackageApp".equals(str)) {
            clearPackageApp(str2, wVCallBackContext);
            return true;
        } else if ("updatePackageApp".equals(str)) {
            updatePackageApp(str2, wVCallBackContext);
            return true;
        } else if ("getLocPerformanceData".equals(str)) {
            getLocPerformanceData(str2, wVCallBackContext);
            return true;
        } else if ("openSpdyforDebug".equals(str)) {
            openSpdyforDebug(str2, wVCallBackContext);
            return true;
        } else if ("closeSpdyforDebug".equals(str)) {
            closeSpdyforDebug(str2, wVCallBackContext);
            return true;
        } else if ("openLocPerformanceMonitor".equals(str)) {
            openLocPerformanceMonitor(str2, wVCallBackContext);
            return true;
        } else if ("closeLocPerformanceMonitor".equals(str)) {
            closeLocPerformanceMonitor(str2, wVCallBackContext);
            return true;
        } else if ("resetConfig".equals(str)) {
            resetConfig(wVCallBackContext, str2);
            return true;
        } else if ("updateConfig".equals(str)) {
            updateConfig(wVCallBackContext, str2);
            return true;
        } else if ("getConfigVersions".equals(str)) {
            getConfigVersions(wVCallBackContext, str2);
            return true;
        } else if ("setDebugEnabled".equals(str)) {
            setDebugEnabled(wVCallBackContext, str2);
            return true;
        } else if ("cleanUp".equals(str)) {
            cleanUp(wVCallBackContext, str2);
            return true;
        } else if ("readMemoryStatisitcs".equals(str)) {
            readMemoryStatisitcs(wVCallBackContext, str2);
            return true;
        } else if ("getURLContentType".equals(str)) {
            getURLContentType(wVCallBackContext, str2);
            return true;
        } else if (!"openRemoteLog".equals(str)) {
            return false;
        } else {
            openRemoteLog(wVCallBackContext, str2);
            return true;
        }
    }

    public void openLocPerformanceMonitor(String str, WVCallBackContext wVCallBackContext) {
        WVLocPerformanceMonitor.setOpenLocPerformanceMonitor(true);
    }

    public void closeLocPerformanceMonitor(String str, WVCallBackContext wVCallBackContext) {
        WVLocPerformanceMonitor.setOpenLocPerformanceMonitor(false);
    }

    public void openSpdyforDebug(String str, WVCallBackContext wVCallBackContext) {
        EnvUtil.setOpenSpdyforDebug(true);
    }

    public void closeSpdyforDebug(String str, WVCallBackContext wVCallBackContext) {
        EnvUtil.setOpenSpdyforDebug(false);
    }

    public void getLocPerformanceData(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        try {
            wVResult.setData(new JSONObject(WVLocPerformanceMonitor.getInstance().getMonitorData().toString()));
            wVCallBackContext.success(wVResult);
        } catch (Exception e) {
            wVCallBackContext.error(e.getMessage());
        }
    }

    public final void clearWindVaneCache(String str, WVCallBackContext wVCallBackContext) {
        this.mWebView.clearCache();
        wVCallBackContext.success();
    }

    public final void setPackageAppEnabled(String str, WVCallBackContext wVCallBackContext) {
        try {
            if (!new JSONObject(str).optBoolean("enable", false)) {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.packageAppStatus = 0;
            } else {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.packageAppStatus = 2;
            }
            wVCallBackContext.success();
        } catch (Exception unused) {
            wVCallBackContext.error();
        }
    }

    public final void isPackageAppEnabled(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        WVCommonConfig.getInstance();
        if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
            wVResult.addData("enabled", "false");
        } else {
            wVResult.addData("enabled", "true");
        }
        wVCallBackContext.success(wVResult);
    }

    public final void setUCEnabled(String str, WVCallBackContext wVCallBackContext) {
        try {
            if (!new JSONObject(str).optBoolean("enable", false)) {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.useSystemWebView = true;
                Toast.makeText(this.mContext, "关闭UC, 重启后生效", 1).show();
            } else {
                WVCommonConfig.getInstance();
                WVCommonConfig.commonConfig.useSystemWebView = false;
                Toast.makeText(this.mContext, "启用UC, 重启后生效", 1).show();
            }
            wVCallBackContext.success();
        } catch (Exception unused) {
            wVCallBackContext.error();
        }
    }

    public final void isUCEnabled(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        WVCommonConfig.getInstance();
        if (WVCommonConfig.commonConfig.useSystemWebView) {
            wVResult.addData("enabled", "false");
        } else {
            wVResult.addData("enabled", "true");
        }
        wVCallBackContext.success(wVResult);
    }

    public final void setWebViewDebugEnabled(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        try {
            boolean optBoolean = new JSONObject(str).optBoolean("enabled", false);
            if (Build.VERSION.SDK_INT < 19) {
                wVResult.addData("error", "api level < 19");
                wVCallBackContext.error(wVResult);
                return;
            }
            if (this.mWebView instanceof WVWebView) {
                WVWebView wVWebView = (WVWebView) this.mWebView;
                WVWebView.setWebContentsDebuggingEnabled(optBoolean);
            }
            this.mIsDebugOpen = optBoolean;
            wVCallBackContext.success();
        } catch (Throwable unused) {
            wVResult.addData("error", "failed to enable debugging");
            wVCallBackContext.error(wVResult);
        }
    }

    public final void setWebViewFinishJs(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        try {
            WVPageFinishJSRender.setJsContent(new JSONObject(str).optString("js"));
            wVCallBackContext.success();
        } catch (JSONException unused) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        } catch (Throwable unused2) {
            wVResult.addData("error", "failed to enable setWebViewFinishJs");
            wVCallBackContext.error(wVResult);
        }
    }

    public final void clearWebViewFinishJs(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        try {
            WVPageFinishJSRender.clearJsRender();
            wVCallBackContext.success();
        } catch (Throwable unused) {
            wVResult.addData("error", "failed to enable clearWebViewFinishJs");
            wVCallBackContext.error(wVResult);
        }
    }

    public final void readMemoryZCacheMap(String str, WVCallBackContext wVCallBackContext) {
        ZipGlobalConfig globalConfig = WVPackageAppService.getWvPackageAppConfig() != null ? WVPackageAppService.getWvPackageAppConfig().getGlobalConfig() : null;
        if (globalConfig == null) {
            wVCallBackContext.error();
        } else {
            wVCallBackContext.success(JSON.toJSONString(globalConfig.getZcacheResConfig()));
        }
    }

    public final void readMemoryPrefixes(String str, WVCallBackContext wVCallBackContext) {
        String stringVal = ConfigStorage.getStringVal(ZipPrefixesManager.SPNAME, ZipPrefixesManager.DATA_KEY, "");
        if (stringVal == null) {
            wVCallBackContext.error();
        } else {
            wVCallBackContext.success(stringVal);
        }
    }

    public final void readPackageAppMemoryInfo(String str, WVCallBackContext wVCallBackContext) {
        ZipGlobalConfig globalConfig = WVPackageAppService.getWvPackageAppConfig() != null ? WVPackageAppService.getWvPackageAppConfig().getGlobalConfig() : null;
        if (globalConfig == null) {
            wVCallBackContext.error();
        } else {
            wVCallBackContext.success(JSON.toJSONString(globalConfig));
        }
    }

    public final void readPackageAppDiskConfig(String str, WVCallBackContext wVCallBackContext) {
        String readGlobalConfig = ZipAppFileManager.getInstance().readGlobalConfig(false);
        WVResult wVResult = new WVResult();
        wVResult.addData("text", readGlobalConfig);
        wVCallBackContext.success(wVResult);
    }

    public final void readPackageAppDiskFileList(String str, WVCallBackContext wVCallBackContext) {
        List<String> appsFileList = WVPackageAppTool.getAppsFileList();
        WVResult wVResult = new WVResult();
        wVResult.addData(WXBasicComponentType.LIST, new JSONArray(appsFileList));
        wVCallBackContext.success(wVResult);
    }

    public final void clearPackageApp(String str, WVCallBackContext wVCallBackContext) {
        WVPackageAppTool.uninstallAll();
        wVCallBackContext.success();
    }

    public final void updatePackageApp(String str, WVCallBackContext wVCallBackContext) {
        WVConfigManager.getInstance().resetConfig();
        WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0);
        wVCallBackContext.success();
    }

    private void resetConfig(WVCallBackContext wVCallBackContext, String str) {
        WVConfigManager.getInstance().resetConfig();
        WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0);
        wVCallBackContext.success();
    }

    private void updateConfig(WVCallBackContext wVCallBackContext, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            WVConfigManager.getInstance().updateConfig(jSONObject.optString(OConstant.DIMEN_CONFIG_NAME, ""), String.valueOf(Long.MAX_VALUE), jSONObject.optString("configUrl", ""), WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0);
            wVCallBackContext.success();
        } catch (JSONException unused) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        }
    }

    private void getConfigVersions(WVCallBackContext wVCallBackContext, String str) {
        HashMap configVersions = WVConfigManager.getInstance().getConfigVersions();
        WVResult wVResult = new WVResult();
        wVResult.setSuccess();
        if (configVersions != null) {
            for (String str2 : configVersions.keySet()) {
                wVResult.addData(str2, (String) configVersions.get(str2));
            }
        }
        wVCallBackContext.success(wVResult);
    }

    private void cleanUp(WVCallBackContext wVCallBackContext, String str) {
        List<String> cleanUp = WVPackageAppCleanup.getInstance().cleanUp(1);
        WVResult wVResult = new WVResult();
        if (cleanUp != null) {
            wVResult.addData("validApps", new JSONArray(cleanUp));
        }
        wVCallBackContext.success(wVResult);
    }

    private void readMemoryStatisitcs(WVCallBackContext wVCallBackContext, String str) {
        wVCallBackContext.success();
    }

    private void setDebugEnabled(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            if (new JSONObject(str).optBoolean("logLevel", true)) {
                TaoLog.setImpl(new AndroidLog());
                TaoLog.setLogSwitcher(true);
            } else {
                TaoLog.setLogSwitcher(false);
            }
            wVCallBackContext.success();
        } catch (JSONException unused) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        } catch (Throwable unused2) {
            wVResult.addData("error", "failed to setDebugEnabled");
            wVCallBackContext.error(wVResult);
        }
    }

    private void getURLContentType(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            String optString = new JSONObject(str).optString("url", "");
            if (WVServerConfig.isBlackUrl(optString)) {
                wVResult.addData("type", (Object) -1);
            } else if (WVServerConfig.isTrustedUrl(optString)) {
                wVResult.addData("type", (Object) 8);
            } else if (WVServerConfig.isThirdPartyUrl(optString)) {
                wVResult.addData("type", (Object) 2);
            } else {
                wVResult.addData("type", (Object) 1);
            }
            wVCallBackContext.success(wVResult);
        } catch (JSONException unused) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        } catch (Throwable unused2) {
            wVResult.addData("error", "failed to getURLContentType");
            wVCallBackContext.error(wVResult);
        }
    }

    private void openRemoteLog(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.optString("id", "123");
            jSONObject.optString("server", "http://h5.alibaba-inc.com");
            new String();
            wVCallBackContext.success(wVResult);
        } catch (JSONException unused) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
        } catch (Throwable unused2) {
            wVResult.addData("error", "failed to openRemoteLog");
            wVCallBackContext.error(wVResult);
        }
    }
}
