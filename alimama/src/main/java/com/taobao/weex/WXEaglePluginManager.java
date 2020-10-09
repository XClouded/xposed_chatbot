package com.taobao.weex;

import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.bridge.WXEaglePlugin;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXSoInstallMgrSdk;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WXEaglePluginManager {
    private static final String LOG_TAG = "WXEaglePluginManager";
    private Map<String, Pair<IFComponentHolder, Map<String, Object>>> mCompCache;
    private Map<String, Pair<ModuleFactory, Boolean>> mModuleCache;
    private Map<String, WXEaglePlugin> mPluginMap;

    private static class InstHolder {
        /* access modifiers changed from: private */
        public static final WXEaglePluginManager INST = new WXEaglePluginManager();

        private InstHolder() {
        }
    }

    public static WXEaglePluginManager getInstance() {
        return InstHolder.INST;
    }

    private WXEaglePluginManager() {
        this.mPluginMap = new ConcurrentHashMap();
        this.mCompCache = new ConcurrentHashMap();
        this.mModuleCache = new ConcurrentHashMap();
    }

    public void register(WXEaglePlugin wXEaglePlugin) {
        if (wXEaglePlugin != null && !TextUtils.isEmpty(wXEaglePlugin.getPluginName())) {
            WXEaglePlugin put = this.mPluginMap.put(wXEaglePlugin.getPluginName(), wXEaglePlugin);
            for (Map.Entry next : this.mCompCache.entrySet()) {
                wXEaglePlugin.registerComponent((String) next.getKey(), (IFComponentHolder) ((Pair) next.getValue()).first, (Map) ((Pair) next.getValue()).second);
            }
            for (Map.Entry next2 : this.mModuleCache.entrySet()) {
                wXEaglePlugin.registerModules((String) next2.getKey(), (ModuleFactory) ((Pair) next2.getValue()).first, ((Boolean) ((Pair) next2.getValue()).second).booleanValue());
            }
            if (put != null) {
                WXLogUtils.w(LOG_TAG, "Register plugin already exist: " + wXEaglePlugin.getPluginName());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void initSo(int i, IWXUserTrackAdapter iWXUserTrackAdapter) {
        for (WXEaglePlugin soLibName : this.mPluginMap.values()) {
            WXSoInstallMgrSdk.initSo(soLibName.getSoLibName(), i, iWXUserTrackAdapter);
        }
    }

    public WXEaglePlugin getPlugin(String str) {
        if (str == null) {
            return null;
        }
        return this.mPluginMap.get(str);
    }

    public Pair<String, WXEaglePlugin> filterUrl(String str) {
        for (Map.Entry next : this.mPluginMap.entrySet()) {
            String isSupportedUrl = ((WXEaglePlugin) next.getValue()).isSupportedUrl(str);
            if (isSupportedUrl != null) {
                return Pair.create(isSupportedUrl, next.getValue());
            }
        }
        return null;
    }

    public void registerComponent(String str, IFComponentHolder iFComponentHolder, Map<String, Object> map) {
        this.mCompCache.put(str, Pair.create(iFComponentHolder, map));
        for (WXEaglePlugin registerComponent : this.mPluginMap.values()) {
            registerComponent.registerComponent(str, iFComponentHolder, map);
        }
    }

    public void registerModule(String str, ModuleFactory moduleFactory, boolean z) {
        this.mModuleCache.put(str, Pair.create(moduleFactory, Boolean.valueOf(z)));
        for (WXEaglePlugin registerModules : this.mPluginMap.values()) {
            registerModules.registerModules(str, moduleFactory, z);
        }
    }

    public boolean callEagleTaskFromWeex(String str, String str2, JSONObject jSONObject) {
        if (TextUtils.isEmpty(str)) {
            Iterator<WXEaglePlugin> it = this.mPluginMap.values().iterator();
            while (true) {
                boolean z = false;
                while (true) {
                    if (!it.hasNext()) {
                        return z;
                    }
                    WXEaglePlugin next = it.next();
                    if (z || next.callEagleTaskFromWeex(str2, jSONObject)) {
                        z = true;
                    }
                }
            }
        } else {
            WXEaglePlugin plugin = getPlugin(str);
            if (plugin != null) {
                return plugin.callEagleTaskFromWeex(str2, jSONObject);
            }
            return false;
        }
    }

    static String getPluginName(WXRenderStrategy wXRenderStrategy) {
        if (wXRenderStrategy == WXRenderStrategy.DATA_RENDER) {
            return "EagleVue";
        }
        if (wXRenderStrategy == WXRenderStrategy.DATA_RENDER_BINARY) {
            return "EagleRax";
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.taobao.weex.common.WXRenderStrategy getRenderStrategyByPlugin(java.lang.String r2) {
        /*
            int r0 = r2.hashCode()
            r1 = -1059993691(0xffffffffc0d1c7a5, float:-6.5556207)
            if (r0 == r1) goto L_0x0019
            r1 = -1059989246(0xffffffffc0d1d902, float:-6.55774)
            if (r0 == r1) goto L_0x000f
            goto L_0x0023
        L_0x000f:
            java.lang.String r0 = "EagleVue"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0023
            r2 = 0
            goto L_0x0024
        L_0x0019:
            java.lang.String r0 = "EagleRax"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0023
            r2 = 1
            goto L_0x0024
        L_0x0023:
            r2 = -1
        L_0x0024:
            switch(r2) {
                case 0: goto L_0x002c;
                case 1: goto L_0x0029;
                default: goto L_0x0027;
            }
        L_0x0027:
            r2 = 0
            return r2
        L_0x0029:
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY
            return r2
        L_0x002c:
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXEaglePluginManager.getRenderStrategyByPlugin(java.lang.String):com.taobao.weex.common.WXRenderStrategy");
    }
}
