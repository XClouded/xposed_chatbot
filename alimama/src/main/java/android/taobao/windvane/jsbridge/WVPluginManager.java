package android.taobao.windvane.jsbridge;

import android.taobao.windvane.monitor.WVJSBrdigeMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

import com.taobao.weex.el.parse.Operators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WVPluginManager {
    public static final String KEY_METHOD = "method";
    public static final String KEY_NAME = "name";
    private static final String SEPARATOR = "::";
    private static final String TAG = "WVPluginManager";
    private static final Map<String, String> aliasPlugins = new ConcurrentHashMap();
    private static IJsBridgeService jsBridgeService = null;
    private static final Map<IWVWebView, Map<String, WVPluginInfo>> localPlugins = new ConcurrentHashMap();
    private static final Map<String, WVApiPlugin> objPlugins = new ConcurrentHashMap();
    private static final Map<String, WVPluginInfo> plugins = new ConcurrentHashMap();

    public static void registerPlugin(String str, Class<? extends WVApiPlugin> cls) {
        registerPlugin(str, cls, true);
    }

    private static void registerPlugin(String str, Class<? extends WVApiPlugin> cls, boolean z, Map<String, WVPluginInfo> map) {
        if (!TextUtils.isEmpty(str) && cls != null) {
            ClassLoader classLoader = null;
            if (z) {
                classLoader = cls.getClassLoader();
            }
            map.put(str, new WVPluginInfo(cls.getName(), classLoader));
            if (WVMonitorService.getJsBridgeMonitor() != null) {
                WVJSBrdigeMonitorInterface jsBridgeMonitor = WVMonitorService.getJsBridgeMonitor();
                jsBridgeMonitor.onJsBridgeReturn("HY_REGISTERPLUGIN", TAG, "HY_REGISTERPLUGIN", str + SEPARATOR + cls.getName(), "");
            }
        }
    }

    public static void registerPlugin(String str, Class<? extends WVApiPlugin> cls, boolean z) {
        registerPlugin(str, cls, z, plugins);
    }

    public static void registerPluginwithParam(String str, Class<? extends WVApiPlugin> cls, Object... objArr) {
        if (!TextUtils.isEmpty(str) && cls != null) {
            WVPluginInfo wVPluginInfo = new WVPluginInfo(cls.getName(), cls.getClassLoader());
            if (objArr != null) {
                wVPluginInfo.setParamObj(objArr);
            }
            plugins.put(str, wVPluginInfo);
            if (WVMonitorService.getJsBridgeMonitor() != null) {
                WVJSBrdigeMonitorInterface jsBridgeMonitor = WVMonitorService.getJsBridgeMonitor();
                jsBridgeMonitor.onJsBridgeReturn("HY_REGISTERPLUGIN", "", "HY_REGISTERPLUGIN", str + SEPARATOR + cls.getName(), "");
            }
        }
    }

    @Deprecated
    public static void registerPlugin(String str, String str2) {
        registerPlugin(str, str2, (ClassLoader) null);
    }

    public static WVPluginInfo getPluginInfo(String str) {
        if (plugins == null) {
            return null;
        }
        return plugins.get(str);
    }

    public static String getPluginInfo() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : plugins.entrySet()) {
            sb.append((String) next.getKey());
            sb.append("-");
            sb.append(((WVPluginInfo) next.getValue()).className);
            sb.append(",");
        }
        return sb.toString();
    }

    public static void registerPlugin(String str, Object obj) {
        try {
            if (obj instanceof WVApiPlugin) {
                objPlugins.put(str, (WVApiPlugin) obj);
            }
        } catch (Throwable th) {
            if (TaoLog.getLogStatus()) {
                TaoLog.e(TAG, "registerPlugin by Object error : " + th.getMessage());
            }
        }
    }

    @Deprecated
    public static void registerPlugin(String str, String str2, ClassLoader classLoader) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            plugins.put(str, new WVPluginInfo(str2, classLoader));
            if (WVMonitorService.getJsBridgeMonitor() != null) {
                WVJSBrdigeMonitorInterface jsBridgeMonitor = WVMonitorService.getJsBridgeMonitor();
                jsBridgeMonitor.onJsBridgeReturn("HY_REGISTERPLUGIN", "", "HY_REGISTERPLUGIN", str + SEPARATOR + str2, "");
            }
        }
    }

    public static void registerPlugin(String str, String str2, ClassLoader classLoader, Object... objArr) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            WVPluginInfo wVPluginInfo = new WVPluginInfo(str2, classLoader);
            wVPluginInfo.setParamObj(objArr);
            plugins.put(str, wVPluginInfo);
            if (WVMonitorService.getJsBridgeMonitor() != null) {
                WVJSBrdigeMonitorInterface jsBridgeMonitor = WVMonitorService.getJsBridgeMonitor();
                jsBridgeMonitor.onJsBridgeReturn("HY_REGISTERPLUGIN", "", "HY_REGISTERPLUGIN", str + SEPARATOR + str2, "");
            }
        }
    }

    public static void registerLocalPlugin(IWVWebView iWVWebView, String str, Class<? extends WVApiPlugin> cls) {
        if (iWVWebView != null) {
            Map map = localPlugins.get(iWVWebView);
            if (map == null) {
                map = new ConcurrentHashMap();
                localPlugins.put(iWVWebView, map);
            }
            registerPlugin(str, cls, true, (Map<String, WVPluginInfo>) map);
            TaoLog.i(TAG, "注册到局部API，使用范围=[" + iWVWebView.getClass().getSimpleName() + "],API=[" + str + SEPARATOR + cls.getSimpleName() + Operators.ARRAY_END_STR);
            return;
        }
        registerPlugin(str, cls);
    }

    public static void registerWVJsBridgeService(IJsBridgeService iJsBridgeService) {
        jsBridgeService = iJsBridgeService;
    }

    public static void registerAlias(String str, String str2, String str3, String str4) {
        if (!plugins.containsKey(str3) || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            TaoLog.w(TAG, "registerAlias quit, this is no original plugin or alias is invalid.");
        } else if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4)) {
            aliasPlugins.put(str + SEPARATOR + str2, str3 + SEPARATOR + str4);
        }
    }

    public static Map<String, String> getOriginalPlugin(String str, String str2) {
        int indexOf;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            TaoLog.w(TAG, "getOriginalPlugin failed, alias is empty.");
            return null;
        }
        Map<String, String> map = aliasPlugins;
        String str3 = map.get(str + SEPARATOR + str2);
        if (TextUtils.isEmpty(str3) || (indexOf = str3.indexOf(SEPARATOR)) <= 0) {
            return null;
        }
        String substring = str3.substring(0, indexOf);
        String substring2 = str3.substring(indexOf + SEPARATOR.length());
        HashMap hashMap = new HashMap();
        hashMap.put("name", substring);
        hashMap.put("method", substring2);
        return hashMap;
    }

    public static void unregisterLocalPlugins(IWVWebView iWVWebView) {
        if (localPlugins.get(iWVWebView) != null) {
            localPlugins.remove(iWVWebView);
        }
    }

    public static void unregisterPlugin(String str) {
        if (plugins.containsKey(str)) {
            plugins.remove(str);
        } else if (objPlugins.containsKey(str)) {
            objPlugins.remove(str);
        }
    }

    public static void unregisterAlias(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            TaoLog.w(TAG, "unregisterAlias quit, alias is invalid.");
            return;
        }
        Map<String, String> map = aliasPlugins;
        map.remove(str + SEPARATOR + str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        if (android.text.TextUtils.isEmpty(r2) != false) goto L_0x0074;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.taobao.windvane.jsbridge.WVApiPlugin createPlugin(java.lang.String r4, android.content.Context r5, android.taobao.windvane.webview.IWVWebView r6) {
        /*
            java.util.concurrent.ConcurrentHashMap r0 = new java.util.concurrent.ConcurrentHashMap
            r0.<init>()
            if (r6 == 0) goto L_0x000f
            java.util.Map<android.taobao.windvane.webview.IWVWebView, java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo>> r0 = localPlugins
            java.lang.Object r0 = r0.get(r6)
            java.util.Map r0 = (java.util.Map) r0
        L_0x000f:
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVApiPlugin> r1 = objPlugins
            boolean r1 = r1.containsKey(r4)
            if (r1 == 0) goto L_0x0020
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVApiPlugin> r5 = objPlugins
            java.lang.Object r4 = r5.get(r4)
            android.taobao.windvane.jsbridge.WVApiPlugin r4 = (android.taobao.windvane.jsbridge.WVApiPlugin) r4
            return r4
        L_0x0020:
            r1 = 0
            if (r0 == 0) goto L_0x0041
            boolean r2 = r0.containsKey(r4)
            if (r2 == 0) goto L_0x0041
            java.lang.Object r0 = r0.get(r4)
            android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo r0 = (android.taobao.windvane.jsbridge.WVPluginManager.WVPluginInfo) r0
            if (r0 != 0) goto L_0x0039
            java.lang.String r2 = "WVPluginManager"
            java.lang.String r3 = "无局部API，尝试从全局API获取"
            android.taobao.windvane.util.TaoLog.i(r2, r3)
            goto L_0x0068
        L_0x0039:
            java.lang.String r2 = "WVPluginManager"
            java.lang.String r3 = "使用局部API"
            android.taobao.windvane.util.TaoLog.i(r2, r3)
            goto L_0x0068
        L_0x0041:
            android.taobao.windvane.jsbridge.IJsBridgeService r0 = jsBridgeService
            if (r0 == 0) goto L_0x0067
            android.taobao.windvane.jsbridge.IJsBridgeService r0 = jsBridgeService
            java.lang.Class r0 = r0.getBridgeClass(r4)
            if (r0 == 0) goto L_0x0067
            r2 = 1
            registerPlugin((java.lang.String) r4, (java.lang.Class<? extends android.taobao.windvane.jsbridge.WVApiPlugin>) r0, (boolean) r2)
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo> r0 = plugins
            java.lang.Object r0 = r0.get(r4)
            android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo r0 = (android.taobao.windvane.jsbridge.WVPluginManager.WVPluginInfo) r0
            if (r0 == 0) goto L_0x0066
            java.lang.String r2 = r0.getClassName()
            if (r2 != 0) goto L_0x0062
            goto L_0x0066
        L_0x0062:
            r0.getClassName()
            goto L_0x0068
        L_0x0066:
            return r1
        L_0x0067:
            r0 = r1
        L_0x0068:
            if (r0 == 0) goto L_0x0074
            java.lang.String r2 = r0.getClassName()
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x008a
        L_0x0074:
            java.util.Map<java.lang.String, android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo> r0 = plugins
            java.lang.Object r0 = r0.get(r4)
            android.taobao.windvane.jsbridge.WVPluginManager$WVPluginInfo r0 = (android.taobao.windvane.jsbridge.WVPluginManager.WVPluginInfo) r0
            if (r0 == 0) goto L_0x0105
            java.lang.String r2 = r0.getClassName()
            if (r2 != 0) goto L_0x0086
            goto L_0x0105
        L_0x0086:
            java.lang.String r2 = r0.getClassName()
        L_0x008a:
            java.lang.ClassLoader r3 = r0.getClassLoader()     // Catch:{ Exception -> 0x00c5 }
            if (r3 != 0) goto L_0x0095
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ Exception -> 0x00c5 }
            goto L_0x0099
        L_0x0095:
            java.lang.Class r2 = r3.loadClass(r2)     // Catch:{ Exception -> 0x00c5 }
        L_0x0099:
            if (r2 == 0) goto L_0x00e8
            java.lang.Class<android.taobao.windvane.jsbridge.WVApiPlugin> r3 = android.taobao.windvane.jsbridge.WVApiPlugin.class
            boolean r3 = r3.isAssignableFrom(r2)     // Catch:{ Exception -> 0x00c5 }
            if (r3 == 0) goto L_0x00e8
            java.lang.Object r2 = r2.newInstance()     // Catch:{ Exception -> 0x00c5 }
            android.taobao.windvane.jsbridge.WVApiPlugin r2 = (android.taobao.windvane.jsbridge.WVApiPlugin) r2     // Catch:{ Exception -> 0x00c5 }
            java.lang.Object r3 = r0.paramObj     // Catch:{ Exception -> 0x00c5 }
            if (r3 == 0) goto L_0x00b7
            java.lang.Object r0 = r0.paramObj     // Catch:{ Exception -> 0x00c5 }
            r2.initialize(r5, r6, r0, r4)     // Catch:{ Exception -> 0x00c5 }
            goto L_0x00c4
        L_0x00b7:
            boolean r0 = r6 instanceof android.taobao.windvane.webview.WVWebView     // Catch:{ Exception -> 0x00c5 }
            if (r0 == 0) goto L_0x00c1
            android.taobao.windvane.webview.WVWebView r6 = (android.taobao.windvane.webview.WVWebView) r6     // Catch:{ Exception -> 0x00c5 }
            r2.initialize(r5, r6, r1, r4)     // Catch:{ Exception -> 0x00c5 }
            goto L_0x00c4
        L_0x00c1:
            r2.initialize(r5, r6, r1, r4)     // Catch:{ Exception -> 0x00c5 }
        L_0x00c4:
            return r2
        L_0x00c5:
            r5 = move-exception
            java.lang.String r6 = "WVPluginManager"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "create plugin error: "
            r0.append(r2)
            r0.append(r4)
            java.lang.String r2 = ". "
            r0.append(r2)
            java.lang.String r5 = r5.getMessage()
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            android.taobao.windvane.util.TaoLog.e(r6, r5)
        L_0x00e8:
            boolean r5 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r5 == 0) goto L_0x0104
            java.lang.String r5 = "WVPluginManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "create plugin failed: "
            r6.append(r0)
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            android.taobao.windvane.util.TaoLog.w(r5, r4)
        L_0x0104:
            return r1
        L_0x0105:
            boolean r5 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r5 == 0) goto L_0x0121
            java.lang.String r5 = "WVPluginManager"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "create plugin failed, plugin not register or empty, "
            r6.append(r0)
            r6.append(r4)
            java.lang.String r4 = r6.toString()
            android.taobao.windvane.util.TaoLog.w(r5, r4)
        L_0x0121:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.WVPluginManager.createPlugin(java.lang.String, android.content.Context, android.taobao.windvane.webview.IWVWebView):android.taobao.windvane.jsbridge.WVApiPlugin");
    }

    public static class WVPluginInfo {
        private ClassLoader classLoader;
        /* access modifiers changed from: private */
        public String className;
        /* access modifiers changed from: private */
        public Object paramObj;

        WVPluginInfo(String str) {
            this.className = str;
        }

        WVPluginInfo(String str, ClassLoader classLoader2) {
            this.className = str;
            this.classLoader = classLoader2;
        }

        public String getClassName() {
            return this.className;
        }

        public void setClassName(String str) {
            this.className = str;
        }

        public ClassLoader getClassLoader() {
            return this.classLoader;
        }

        public void setClassLoader(ClassLoader classLoader2) {
            this.classLoader = classLoader2;
        }

        public Object getParamObj() {
            return this.paramObj;
        }

        public void setParamObj(Object obj) {
            this.paramObj = obj;
        }
    }
}
