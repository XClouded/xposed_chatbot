package android.taobao.windvane.extra.uc;

import android.taobao.windvane.embed.BaseEmbedView;
import android.taobao.windvane.embed.Empty;
import android.taobao.windvane.embed.WVEVManager;
import android.taobao.windvane.extra.config.WindVaneUrlCacheManager;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.EmbedViewConfig;
import com.uc.webview.export.extension.IEmbedView;
import com.uc.webview.export.extension.IEmbedViewContainer;
import com.uc.webview.export.extension.UCClient;

import java.util.Map;

public class WVUCClient extends UCClient {
    IWVWebView webView = null;

    public WVUCClient() {
    }

    public WVUCClient(IWVWebView iWVWebView) {
        this.webView = iWVWebView;
    }

    public void onWebViewEvent(final WebView webView2, int i, Object obj) {
        if (i == 9) {
            try {
                Integer num = (Integer) obj;
                String url = webView2.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    AppMonitorUtil.commitEmptyPage(url, "TYPEB_" + num.toString());
                } else if (webView2 instanceof WVUCWebView) {
                    String currentUrl = ((WVUCWebView) webView2).getCurrentUrl();
                    if (!TextUtils.isEmpty(currentUrl)) {
                        AppMonitorUtil.commitEmptyPage(currentUrl, "TYPEA_" + num.toString());
                    }
                }
            } catch (Throwable unused) {
            }
        } else if (i == 107) {
            TaoLog.i("sandbox", "onRenderProcessReady");
            if ((webView2 instanceof WVUCWebView) && webView2.getUCExtension() != null) {
                webView2.getUCExtension().getCoreStatus(1, new ValueCallback<Object>() {
                    public void onReceiveValue(Object obj) {
                        WVUCWebViewClient wVUCWebViewClient = ((WVUCWebView) webView2).webViewClient;
                        String str = "R_Success";
                        if (wVUCWebViewClient == null) {
                            str = "unknow";
                        } else if (wVUCWebViewClient.crashCount != 0) {
                            str = "Recover_Success";
                        }
                        if (obj instanceof Integer) {
                            int intValue = ((Integer) obj).intValue();
                            if (WVMonitorService.getWvMonitorInterface() != null) {
                                WVMonitorService.getWvMonitorInterface().commitRenderType(webView2.getUrl(), str, intValue);
                            }
                            TaoLog.i("sandbox", "process mode: " + intValue);
                        }
                    }
                });
            }
        } else if (i == 108) {
            TaoLog.i("sandbox", "WEBVIEW_EVENT_TYPE_DESTORY_NON_ISOLATE_STATIC_WEBVIEW");
            if (webView2 instanceof WVUCWebView) {
                WVUCWebView.destroyStaticWebViewIfNeeded();
            }
        } else if (i == 109) {
            TaoLog.i("sandbox", "WEBVIEW_EVENT_TYPE_CREATE_ISOLATE_STATIC_WEBVIEW");
            if ((webView2 instanceof WVUCWebView) && webView2.getContext() != null) {
                WVUCWebView.createStaticWebViewIfNeeded(webView2.getContext());
            }
        }
        super.onWebViewEvent(webView2, i, obj);
    }

    public String getCachedFilePath(String str) {
        return WindVaneUrlCacheManager.getCacheFilePath(str);
    }

    public IEmbedView getEmbedView(EmbedViewConfig embedViewConfig, IEmbedViewContainer iEmbedViewContainer) {
        Map map = embedViewConfig.mObjectParam;
        if (map.containsKey("viewType")) {
            BaseEmbedView createEV = WVEVManager.createEV(map.containsKey("bridgeId") ? (String) map.get("bridgeId") : "", (String) map.get("viewType"), this.webView, embedViewConfig);
            if (createEV == null) {
                TaoLog.e("EmbedView", "failed to create embedView");
            } else {
                iEmbedViewContainer.setOnParamChangedListener(createEV);
                iEmbedViewContainer.setOnStateChangedListener(createEV);
                iEmbedViewContainer.setOnVisibilityChangedListener(createEV);
                return createEV;
            }
        } else {
            TaoLog.e("EmbedView", "viewType should not be lost");
        }
        Empty empty = new Empty();
        empty.init("", "empty", this.webView, (EmbedViewConfig) null);
        return empty;
    }
}
