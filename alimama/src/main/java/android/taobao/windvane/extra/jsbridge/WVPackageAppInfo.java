package android.taobao.windvane.extra.jsbridge;

import android.content.Context;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.cleanup.InfoSnippet;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import com.taobao.ju.track.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WVPackageAppInfo extends WVApiPlugin {
    private static final String TAG = "WVPackageAppInfo";

    public void initialize(Context context, IWVWebView iWVWebView) {
        WVEventService.getInstance().addEventListener(new WVPackageEventListener(iWVWebView));
        super.initialize(context, iWVWebView);
    }

    public static class WVPackageEventListener implements WVEventListener {
        private WeakReference<IWVWebView> webview;

        public WVPackageEventListener(IWVWebView iWVWebView) {
            this.webview = new WeakReference<>(iWVWebView);
        }

        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (this.webview.get() == null) {
                return null;
            }
            if (i != 6001) {
                switch (i) {
                    case 6004:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"安装进度 : +" + objArr[0] + "%\"}");
                        break;
                    case 6005:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"解压状态 : " + objArr[0] + "\"}");
                        break;
                    case 6006:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"校验结果 : " + objArr[0] + "\"}");
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"安装流程完成已安装seq : +" + objArr[1] + "\"}");
                        break;
                    case 6007:
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"安装失败 : " + objArr[0] + "\"}");
                        ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"失败信息 : " + objArr[1] + "\"}");
                        break;
                }
            } else {
                ((IWVWebView) this.webview.get()).fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"全部app安装完成\"}");
                TaoLog.d(WVPackageAppInfo.TAG, "PACKAGE_UPLOAD_COMPLETE");
            }
            return null;
        }
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("localPathForURL".equals(str)) {
            localPathForURL(wVCallBackContext, str2);
            return true;
        } else if ("registerApp".equals(str)) {
            WVPackageAppRuntime.registerApp(wVCallBackContext, str2);
            return true;
        } else if ("previewApp".equals(str)) {
            previewApp(wVCallBackContext, str2);
            return true;
        } else if (!"readMemoryStatisitcs".equals(str)) {
            return false;
        } else {
            readMemoryStatisitcs(wVCallBackContext, str2);
            return true;
        }
    }

    private void localPathForURL(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            String locPathByUrl = ZipAppUtils.getLocPathByUrl(new JSONObject(str).optString("url", "").replaceAll("^((?i)https:)?//", "http://"));
            if (TextUtils.isEmpty(locPathByUrl)) {
                wVResult.setResult("HY_FAILED");
                wVCallBackContext.error(wVResult);
                return;
            }
            wVResult.addData("localPath", locPathByUrl);
            wVCallBackContext.success(wVResult);
        } catch (Exception unused) {
            TaoLog.e(TAG, "param parse to JSON error, param=" + str);
            wVResult.setResult("HY_PARAM_ERR");
            wVCallBackContext.error(wVResult);
        }
    }

    private void readMemoryStatisitcs(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        HashMap<String, InfoSnippet> infoMap = WVPackageAppCleanup.getInstance().getInfoMap();
        if (infoMap != null) {
            for (Map.Entry next : infoMap.entrySet()) {
                String str2 = (String) next.getKey();
                InfoSnippet infoSnippet = (InfoSnippet) next.getValue();
                JSONObject jSONObject = new JSONObject();
                try {
                    if (infoSnippet.count != 0.0d || infoSnippet.failCount != 0) {
                        jSONObject.put("accessCount", infoSnippet.count);
                        jSONObject.put("errorCount", infoSnippet.failCount);
                        jSONObject.put("needReinstall", infoSnippet.needReinstall);
                        wVResult.addData(str2, jSONObject);
                    }
                } catch (Exception unused) {
                }
            }
        }
        wVCallBackContext.success(wVResult);
    }

    private void previewApp(final WVCallBackContext wVCallBackContext, final String str) {
        WVThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                TaoLog.w(WVPackageAppInfo.TAG, "exec preview task");
                WVResult wVResult = new WVResult();
                try {
                    String optString = new JSONObject(str).optString("appName");
                    final ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
                    String str = "http://wapp." + GlobalConfig.env.getValue() + ".taobao.com/app/";
                    WVPackageAppPrefixesConfig.getInstance().preViewMode = true;
                    ConnectManager.getInstance().connectSync(str + optString + "/app-prefix.wvc", new HttpConnectListener<HttpResponse>() {
                        public void onFinish(HttpResponse httpResponse, int i) {
                            byte[] data = httpResponse.getData();
                            if (httpResponse != null && data != null) {
                                try {
                                    if (WVPackageAppPrefixesConfig.getInstance().parseConfig(new String(data, "utf-8"))) {
                                        WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app-prefix 解析成功\"}");
                                        return;
                                    }
                                } catch (Exception unused) {
                                }
                                WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app-prefix 解析失败\"}");
                            }
                        }

                        public void onError(int i, String str) {
                            WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app-prefix 下载失败\"}");
                            super.onError(i, str);
                        }
                    });
                    ConnectManager.getInstance().connectSync(str + optString + "/config/app.json", new HttpConnectListener<HttpResponse>() {
                        public void onFinish(HttpResponse httpResponse, int i) {
                            byte[] data = httpResponse.getData();
                            if (httpResponse != null && data != null) {
                                try {
                                    JSONObject jSONObject = new JSONObject(new String(data, "utf-8"));
                                    String next = jSONObject.keys().next();
                                    JSONObject jSONObject2 = jSONObject.getJSONObject(next);
                                    if (jSONObject2 != null) {
                                        String optString = jSONObject2.optString("v", "");
                                        if (!TextUtils.isEmpty(optString)) {
                                            ZipAppInfo appInfo = locGlobalConfig.getAppInfo(next);
                                            if (appInfo == null) {
                                                appInfo = new ZipAppInfo();
                                                locGlobalConfig.putAppInfo2Table(next, appInfo);
                                            }
                                            appInfo.isPreViewApp = true;
                                            appInfo.v = optString;
                                            appInfo.name = next;
                                            appInfo.status = ZipAppConstants.ZIP_NEWEST;
                                            appInfo.s = jSONObject2.optLong("s", 0);
                                            appInfo.f = jSONObject2.optLong("f", 5);
                                            appInfo.t = jSONObject2.optLong("t", 0);
                                            appInfo.z = jSONObject2.optString("z", "");
                                            appInfo.installedSeq = 0;
                                            appInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
                                            WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app.json 解析成功\"}");
                                            WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"准备下载(如长时间未开始下载，请刷新本页面)\"}");
                                            ZipAppDownloaderQueue.getInstance().startUpdateAppsTask();
                                        }
                                    }
                                } catch (Exception unused) {
                                    WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app.json 解析失败\"}");
                                }
                            }
                        }

                        public void onError(int i, String str) {
                            WVPackageAppInfo.this.mWebView.fireEvent("WV.Event.Package.PreviewProgress", "{\"msg\":\"app.json 下载失败\"}");
                            super.onError(i, str);
                        }
                    });
                    WVPackageAppPrefixesConfig.getInstance().preViewMode = false;
                    wVCallBackContext.success();
                } catch (JSONException unused) {
                    TaoLog.e(WVPackageAppInfo.TAG, "param parse to JSON error, param=" + str);
                    wVResult.setResult("HY_PARAM_ERR");
                    wVCallBackContext.error(wVResult);
                }
            }
        });
    }
}
