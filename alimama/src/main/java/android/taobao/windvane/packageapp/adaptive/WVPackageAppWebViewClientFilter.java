package android.taobao.windvane.packageapp.adaptive;

import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ComboInfo;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.webkit.WebResourceResponse;

import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheManager;
import com.taobao.zcache.model.ZCacheResourceResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WVPackageAppWebViewClientFilter implements WVEventListener {
    private String TAG = WVPackageAppWebViewClientFilter.class.getSimpleName();

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        Map map;
        WVWrapWebResourceResponse wVWrapWebResourceResponse;
        String str;
        String str2;
        WVWrapWebResourceResponse wVWrapWebResourceResponse2;
        int i2 = i;
        WVEventContext wVEventContext2 = wVEventContext;
        if (wVEventContext2 == null) {
            return new WVEventResult(false);
        }
        ZCacheConfigManager.getInstance().triggerZCacheConfig();
        if (!"3".equals(GlobalConfig.zType)) {
            if (i2 == 6002) {
                TaoLog.i("WVConfigManager", "speed=[" + GlobalConfig.getInstance().needSpeed() + "],launch=[" + WVConfigManager.launch + Operators.ARRAY_END_STR);
                if (WVConfigManager.launch) {
                    TaoLog.i("WVConfigManager", "skip first download");
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            if (!ZipAppDownloaderQueue.getInstance().hasPrefetch) {
                                ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
                            }
                        }
                    }, 30000);
                    WVConfigManager.launch = false;
                } else if (!ZipAppDownloaderQueue.getInstance().hasPrefetch) {
                    ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
                }
            } else if ((i2 == 1004 || i2 == 1008) && WVCommonConfig.commonConfig.packageAppStatus != 0) {
                String str3 = wVEventContext2.url;
                if (wVEventContext2.url != null && wVEventContext2.url.contains("https")) {
                    wVEventContext2.url = wVEventContext2.url.replace("https", "http");
                }
                wVEventContext2.url = WVUrlUtil.removeQueryParam(wVEventContext2.url);
                System.currentTimeMillis();
                ZipAppInfo appInfoByUrl = WVPackageAppRuntime.getAppInfoByUrl(wVEventContext2.url);
                WVWrapWebResourceResponse wrapResourceResponse = appInfoByUrl != null ? WVPackageAppRuntime.getWrapResourceResponse(wVEventContext2.url, appInfoByUrl) : null;
                System.currentTimeMillis();
                if (wrapResourceResponse != null) {
                    try {
                        str2 = String.valueOf(appInfoByUrl.s);
                    } catch (Exception unused) {
                        str2 = "0";
                    }
                    if (WVMonitorService.getPerformanceMonitor() == null) {
                        wVWrapWebResourceResponse2 = wrapResourceResponse;
                    } else if (!WVUrlUtil.isHtml(wVEventContext2.url) || appInfoByUrl == null) {
                        wVWrapWebResourceResponse2 = wrapResourceResponse;
                        WVMonitorService.getPerformanceMonitor().didGetResourceStatusCode(str3, 200, 3, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                    } else {
                        WVPerformanceMonitorInterface performanceMonitor = WVMonitorService.getPerformanceMonitor();
                        String str4 = appInfoByUrl.v;
                        String str5 = appInfoByUrl.name;
                        wVWrapWebResourceResponse2 = wrapResourceResponse;
                        performanceMonitor.didGetPageStatusCode(str3, 200, 3, str4, str5, str2, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                    }
                    return new WVEventResult(true, wVWrapWebResourceResponse2);
                }
                WVWrapWebResourceResponse wVWrapWebResourceResponse3 = wrapResourceResponse;
                System.currentTimeMillis();
                ZipGlobalConfig.CacheFileData isZcacheUrl = ConfigManager.getLocGlobalConfig().isZcacheUrl(wVEventContext2.url);
                if (isZcacheUrl != null) {
                    wVWrapWebResourceResponse3 = WVPackageAppRuntime.getWrapResourceResponse(wVEventContext2.url, isZcacheUrl);
                }
                System.currentTimeMillis();
                if (wVWrapWebResourceResponse3 != null) {
                    try {
                        str = String.valueOf(isZcacheUrl.seq);
                    } catch (Exception unused2) {
                        str = "0";
                    }
                    if (WVMonitorService.getPerformanceMonitor() != null) {
                        if (WVUrlUtil.isHtml(wVEventContext2.url)) {
                            WVMonitorService.getPerformanceMonitor().didGetPageStatusCode(str3, 200, 4, isZcacheUrl.v, isZcacheUrl.appName, str, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                        } else {
                            WVMonitorService.getPerformanceMonitor().didGetResourceStatusCode(str3, 200, 4, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
                        }
                    }
                    return new WVEventResult(true, wVWrapWebResourceResponse3);
                }
                if (i2 == 1008) {
                    try {
                        map = objArr[0];
                    } catch (Exception unused3) {
                    }
                } else {
                    map = null;
                }
                if (map == null) {
                    map = new HashMap();
                }
                WebResourceResponse makeComboRes = WVPackageAppRuntime.makeComboRes(wVEventContext2.url, (ComboInfo) null, map);
                if (makeComboRes != null) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        wVWrapWebResourceResponse = new WVWrapWebResourceResponse(makeComboRes.getMimeType(), makeComboRes.getEncoding(), makeComboRes.getData(), makeComboRes.getResponseHeaders());
                    } else {
                        wVWrapWebResourceResponse = new WVWrapWebResourceResponse(makeComboRes.getMimeType(), makeComboRes.getEncoding(), makeComboRes.getData(), (Map<String, String>) null);
                    }
                    return new WVEventResult(true, wVWrapWebResourceResponse);
                }
                TaoLog.i(this.TAG, str3 + " request online");
            } else if (i2 == 6011) {
                return new WVEventResult(true, ZipAppManager.getUrlsByAppName(objArr[0]));
            }
            return new WVEventResult(false);
        } else if ((i2 != 1004 && i2 != 1008) || WVCommonConfig.commonConfig.packageAppStatus == 0) {
            return new WVEventResult(false);
        } else {
            if (wVEventContext2.url != null && wVEventContext2.url.contains("https")) {
                wVEventContext2.url = wVEventContext2.url.replace("https", "http");
            }
            wVEventContext2.url = WVUrlUtil.removeQueryParam(wVEventContext2.url);
            Map hashMap = new HashMap();
            if (i2 == 1008) {
                try {
                    hashMap = objArr[0];
                } catch (Throwable unused4) {
                }
            }
            ZCacheResourceResponse zCacheResource = ZCacheManager.instance().getZCacheResource(wVEventContext2.url, hashMap);
            if (zCacheResource == null) {
                TaoLog.i("ZCache", "H5 use ZCache 3.0, url=[" + wVEventContext2.url + "], with response:[false]");
                return new WVEventResult(false);
            }
            TaoLog.i("ZCache", "H5 use ZCache 3.0, url=[" + wVEventContext2.url + "] with response:[" + zCacheResource.isSuccess + Operators.ARRAY_END_STR);
            return new WVEventResult(zCacheResource.isSuccess, new WVWrapWebResourceResponse(zCacheResource.mimeType, zCacheResource.encoding, zCacheResource.inputStream, zCacheResource.headers));
        }
    }
}
