package android.taobao.windvane.embed;

import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.extension.EmbedViewConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WVEVManager {
    private static final String TAG = "WVEVManager";
    private static final Map<String, WVEmbedViewInfo> embedViews = new ConcurrentHashMap();

    public static void registerEmbedView(String str, Class<? extends BaseEmbedView> cls, boolean z) {
        if (!TextUtils.isEmpty(str) && cls != null) {
            ClassLoader classLoader = null;
            if (z) {
                classLoader = cls.getClassLoader();
            }
            WVEmbedViewInfo wVEmbedViewInfo = new WVEmbedViewInfo(cls.getName(), classLoader);
            if (embedViews.containsKey(str)) {
                TaoLog.e(TAG, "new view:[" + cls.getSimpleName() + "] will overlap the old view [" + embedViews.get(str).getClassName() + Operators.ARRAY_END_STR);
            }
            embedViews.put(str, wVEmbedViewInfo);
        }
    }

    public static WVEmbedViewInfo getEv(String str) {
        return embedViews.get(str);
    }

    public static BaseEmbedView createEV(String str, String str2, IWVWebView iWVWebView, EmbedViewConfig embedViewConfig) {
        Class<?> cls;
        WVEmbedViewInfo ev = getEv(str2);
        if (ev == null) {
            TaoLog.e(TAG, "no register view with type:[" + str2 + Operators.ARRAY_END_STR);
            return null;
        }
        try {
            ClassLoader classLoader = ev.getClassLoader();
            if (classLoader == null) {
                cls = Class.forName(ev.getClassName());
            } else {
                cls = classLoader.loadClass(ev.getClassName());
            }
            if (cls == null || !BaseEmbedView.class.isAssignableFrom(cls)) {
                TaoLog.e(TAG, "no class found");
                return null;
            }
            BaseEmbedView baseEmbedView = (BaseEmbedView) cls.newInstance();
            if (baseEmbedView.init(str, str2, iWVWebView, embedViewConfig)) {
                return baseEmbedView;
            }
            TaoLog.e(TAG, "type check error, required type:[" + baseEmbedView.getViewType() + "], real type:[" + str2 + Operators.ARRAY_END_STR);
            return null;
        } catch (Exception e) {
            TaoLog.e(TAG, "create embed view error, type:" + str2 + " | msg:" + e.getMessage());
        }
    }

    public static class WVEmbedViewInfo {
        private ClassLoader classLoader;
        private String className;

        WVEmbedViewInfo(String str) {
            this.className = str;
        }

        WVEmbedViewInfo(String str, ClassLoader classLoader2) {
            this.className = str;
            this.classLoader = classLoader2;
        }

        public void setClassLoader(ClassLoader classLoader2) {
            this.classLoader = classLoader2;
        }

        public ClassLoader getClassLoader() {
            return this.classLoader;
        }

        public void setClassName(String str) {
            this.className = str;
        }

        public String getClassName() {
            return this.className;
        }
    }
}
