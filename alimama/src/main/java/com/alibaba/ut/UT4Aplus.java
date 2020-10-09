package com.alibaba.ut;

import android.app.Application;
import android.util.Log;
import android.webkit.WebView;
import com.alibaba.ut.biz.ContainerLifeCBNotify;
import com.alibaba.ut.comm.ActivityLifecycleCB;
import com.alibaba.ut.comm.AutoAddJsInterface;
import com.alibaba.ut.comm.JsBridge;
import com.alibaba.ut.page.PageDestroyHandler;
import com.alibaba.ut.utils.Logger;
import java.util.Map;

public class UT4Aplus {
    public static final String SDK_VERSION = "0.2.29";
    private static final String TAG = "UT4Aplus";
    public static final String USER_AGENT = "UT4Aplus/0.2.29";
    private static volatile boolean isInit = false;

    public static void init(Application application) {
        init(application, (Map) null);
    }

    public static void init(Application application, String str) {
        init(application, str, (Map) null);
    }

    public static synchronized void init(Application application, String str, Map map) {
        synchronized (UT4Aplus.class) {
            if (!isInit && str.contains("dd")) {
                isInit = true;
                ActivityLifecycleCB.getInstance().init(application);
                new PageDestroyHandler().init();
                Log.i(TAG, "ut4aplus for DingTalk init success. sdk_version:0.2.29");
            }
        }
    }

    public static synchronized void init(Application application, Map map) {
        synchronized (UT4Aplus.class) {
            if (!isInit) {
                isInit = true;
                ActivityLifecycleCB.getInstance().init(application);
                new ContainerLifeCBNotify().init();
                new PageDestroyHandler().init();
                AutoAddJsInterface.getInstance().init();
                Log.i(TAG, "ut4aplus init success. sdk_version:0.2.29");
            }
        }
    }

    public static void addJavascriptInterface(Object obj) {
        Logger.i((String) null, "[addJavascriptInterface]");
        if (obj != null) {
            if (obj instanceof WebView) {
                Logger.i((String) null, "[addJavascriptInterface] webview");
                ((WebView) obj).addJavascriptInterface(new JsBridge(obj), TAG);
            } else if (obj instanceof com.uc.webview.export.WebView) {
                Logger.i((String) null, "[addJavascriptInterface] ucwebview");
                ContainerLifeCBNotify.addExcludeWebView(obj.hashCode());
                ((com.uc.webview.export.WebView) obj).addJavascriptInterface(new JsBridge(obj), TAG);
            } else if (obj instanceof IWebView) {
                Logger.i((String) null, "[addJavascriptInterface] IWebView");
                ((IWebView) obj).addJavascriptInterface(new JsBridge(obj), TAG);
            } else {
                Log.w(TAG, "Unsupported WebView");
            }
        }
    }
}
