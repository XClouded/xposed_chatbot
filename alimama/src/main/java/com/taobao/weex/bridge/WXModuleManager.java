package com.taobao.weex.bridge;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONArray;
import com.taobao.weex.WXEaglePluginManager;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.config.ConfigModuleFactory;
import com.taobao.weex.ui.module.WXDomModule;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.cache.RegisterCache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WXModuleManager {
    private static Map<String, WXDomModule> sDomModuleMap = new HashMap();
    /* access modifiers changed from: private */
    public static Map<String, WXModule> sGlobalModuleMap = new HashMap();
    private static Map<String, Map<String, WXModule>> sInstanceModuleMap = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public static volatile ConcurrentMap<String, ModuleFactoryImpl> sModuleFactoryMap = new ConcurrentHashMap();

    public static boolean registerModule(Map<String, RegisterCache.ModuleCache> map) {
        if (map.isEmpty()) {
            return true;
        }
        final Iterator<Map.Entry<String, RegisterCache.ModuleCache>> it = map.entrySet().iterator();
        WXBridgeManager.getInstance().postWithName(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                while (it.hasNext()) {
                    RegisterCache.ModuleCache moduleCache = (RegisterCache.ModuleCache) ((Map.Entry) it.next()).getValue();
                    String str = moduleCache.name;
                    if (TextUtils.equals(str, WXDomModule.WXDOM)) {
                        WXLogUtils.e("Cannot registered module with name 'dom'.");
                    } else {
                        if (WXModuleManager.sModuleFactoryMap != null && WXModuleManager.sModuleFactoryMap.containsKey(str)) {
                            WXLogUtils.w("WXComponentRegistry Duplicate the Module name: " + str);
                        }
                        ModuleFactory moduleFactory = moduleCache.factory;
                        try {
                            WXModuleManager.registerNativeModule(str, moduleFactory);
                        } catch (WXException e) {
                            WXLogUtils.e("registerNativeModule" + e);
                        }
                        if (moduleCache.global) {
                            try {
                                WXModule buildInstance = moduleFactory.buildInstance();
                                buildInstance.setModuleName(str);
                                WXModuleManager.sGlobalModuleMap.put(str, buildInstance);
                            } catch (Exception e2) {
                                WXLogUtils.e(str + " class must have a default constructor without params. ", (Throwable) e2);
                            }
                        }
                        try {
                            WXModuleManager.sModuleFactoryMap.put(str, new ModuleFactoryImpl(moduleFactory));
                        } catch (Throwable unused) {
                        }
                        hashMap.put(str, moduleFactory.getMethods());
                    }
                }
                WXSDKManager.getInstance().registerModules(hashMap);
            }
        }, (WXSDKInstance) null, "registerModule From Cache");
        return true;
    }

    public static boolean registerModule(final String str, final ModuleFactory moduleFactory, final boolean z) throws WXException {
        if (str == null || moduleFactory == null) {
            return false;
        }
        if (TextUtils.equals(str, WXDomModule.WXDOM)) {
            WXLogUtils.e("Cannot registered module with name 'dom'.");
            return false;
        }
        WXEaglePluginManager.getInstance().registerModule(str, moduleFactory, z);
        if (RegisterCache.getInstance().cacheModule(str, moduleFactory, z)) {
            return true;
        }
        WXBridgeManager.getInstance().postWithName(new Runnable() {
            public void run() {
                if (WXModuleManager.sModuleFactoryMap != null && WXModuleManager.sModuleFactoryMap.containsKey(str)) {
                    WXLogUtils.w("WXComponentRegistry Duplicate the Module name: " + str);
                }
                try {
                    WXModuleManager.registerNativeModule(str, moduleFactory);
                } catch (WXException e) {
                    WXLogUtils.e("registerNativeModule" + e);
                }
                if (z) {
                    try {
                        WXModule buildInstance = moduleFactory.buildInstance();
                        buildInstance.setModuleName(str);
                        WXModuleManager.sGlobalModuleMap.put(str, buildInstance);
                    } catch (Exception e2) {
                        WXLogUtils.e(str + " class must have a default constructor without params. ", (Throwable) e2);
                    }
                }
                WXModuleManager.registerJSModule(str, moduleFactory);
                try {
                    WXModuleManager.sModuleFactoryMap.put(str, new ModuleFactoryImpl(moduleFactory));
                } catch (Throwable unused) {
                }
            }
        }, (WXSDKInstance) null, "registerModule");
        return true;
    }

    static boolean registerNativeModule(String str, ModuleFactory moduleFactory) throws WXException {
        if (moduleFactory == null) {
            return false;
        }
        try {
            if (sModuleFactoryMap.containsKey(str)) {
                return true;
            }
            sModuleFactoryMap.put(str, new ModuleFactoryImpl(moduleFactory));
            return true;
        } catch (ArrayStoreException e) {
            e.printStackTrace();
            WXLogUtils.e("[WXModuleManager] registerNativeModule Error moduleName:" + str + " Error:" + e.toString());
            return true;
        }
    }

    static boolean registerJSModule(String str, ModuleFactory moduleFactory) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, moduleFactory.getMethods());
        WXSDKManager.getInstance().registerModules(hashMap);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00bc A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00bd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Object callModuleMethod(java.lang.String r12, java.lang.String r13, java.lang.String r14, com.alibaba.fastjson.JSONArray r15) {
        /*
            java.util.concurrent.ConcurrentMap<java.lang.String, com.taobao.weex.bridge.ModuleFactoryImpl> r0 = sModuleFactoryMap
            java.lang.Object r0 = r0.get(r13)
            com.taobao.weex.bridge.ModuleFactoryImpl r0 = (com.taobao.weex.bridge.ModuleFactoryImpl) r0
            r1 = 0
            if (r0 != 0) goto L_0x0011
            java.lang.String r12 = "[WXModuleManager] module factoryImpl not found."
            com.taobao.weex.utils.WXLogUtils.e(r12)
            return r1
        L_0x0011:
            com.taobao.weex.bridge.ModuleFactory r0 = r0.mFactory
            if (r0 != 0) goto L_0x001b
            java.lang.String r12 = "[WXModuleManager] module factory not found."
            com.taobao.weex.utils.WXLogUtils.e(r12)
            return r1
        L_0x001b:
            com.taobao.weex.common.WXModule r2 = findModule(r12, r13, r0)
            if (r2 != 0) goto L_0x0022
            return r1
        L_0x0022:
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()
            com.taobao.weex.WXSDKInstance r3 = r3.getSDKInstance(r12)
            r2.mWXSDKInstance = r3
            com.taobao.weex.bridge.Invoker r4 = r0.getMethodInvoker(r14)
            if (r4 != 0) goto L_0x00ba
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "CallNativeModule Failed For "
            r5.<init>(r6)
            r5.append(r13)
            java.lang.String r6 = ":"
            r5.append(r6)
            r5.append(r14)
            java.lang.String r6 = "'s invoker is null"
            r5.append(r6)
            boolean r6 = r0 instanceof com.taobao.weex.common.TypeModuleFactory     // Catch:{ Throwable -> 0x009b }
            if (r6 == 0) goto L_0x0098
            r6 = r0
            com.taobao.weex.common.TypeModuleFactory r6 = (com.taobao.weex.common.TypeModuleFactory) r6     // Catch:{ Throwable -> 0x009b }
            boolean r7 = r6.hasRebuild()     // Catch:{ Throwable -> 0x009b }
            if (r7 != 0) goto L_0x006d
            boolean r7 = r6.hasMethodInClass(r14)     // Catch:{ Throwable -> 0x009b }
            if (r7 == 0) goto L_0x006d
            java.lang.String r7 = " but has "
            r5.append(r7)     // Catch:{ Throwable -> 0x009b }
            r5.append(r14)     // Catch:{ Throwable -> 0x009b }
            java.lang.String r7 = " and rebuilding..."
            r5.append(r7)     // Catch:{ Throwable -> 0x009b }
            r6.reBuildMethodMap()     // Catch:{ Throwable -> 0x009b }
            goto L_0x0075
        L_0x006d:
            java.lang.String r7 = " do not has "
            r5.append(r7)     // Catch:{ Throwable -> 0x009b }
            r5.append(r14)     // Catch:{ Throwable -> 0x009b }
        L_0x0075:
            com.taobao.weex.bridge.Invoker r0 = r0.getMethodInvoker(r14)     // Catch:{ Throwable -> 0x009b }
            java.lang.String r4 = " And Class Name is "
            r5.append(r4)     // Catch:{ Throwable -> 0x0093 }
            java.lang.String r4 = r6.className()     // Catch:{ Throwable -> 0x0093 }
            r5.append(r4)     // Catch:{ Throwable -> 0x0093 }
            if (r0 != 0) goto L_0x008d
            java.lang.String r4 = " and rebuild Method Map Failed"
            r5.append(r4)     // Catch:{ Throwable -> 0x0093 }
            goto L_0x0099
        L_0x008d:
            java.lang.String r4 = " and rebuild Method Map Succeed, Continue Call Native Module"
            r5.append(r4)     // Catch:{ Throwable -> 0x0093 }
            goto L_0x0099
        L_0x0093:
            r4 = move-exception
            r11 = r4
            r4 = r0
            r0 = r11
            goto L_0x009c
        L_0x0098:
            r0 = r4
        L_0x0099:
            r4 = r0
            goto L_0x00a8
        L_0x009b:
            r0 = move-exception
        L_0x009c:
            java.lang.String r6 = " Throw Exception "
            r5.append(r6)
            java.lang.String r0 = r0.getMessage()
            r5.append(r0)
        L_0x00a8:
            java.lang.String r0 = r5.toString()
            com.taobao.weex.utils.WXLogUtils.e(r0)
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_RENDER_ERR_CALL_NATIVE_MODULE
            java.lang.String r6 = "callModuleMethod"
            java.lang.String r5 = r5.toString()
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r12, r0, r6, r5, r1)
        L_0x00ba:
            if (r4 != 0) goto L_0x00bd
            return r1
        L_0x00bd:
            if (r3 == 0) goto L_0x0115
            com.taobao.weex.WXSDKManager r12 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.adapter.IWXUserTrackAdapter r5 = r12.getIWXUserTrackAdapter()     // Catch:{ Exception -> 0x0113 }
            if (r5 == 0) goto L_0x0102
            java.util.HashMap r10 = new java.util.HashMap     // Catch:{ Exception -> 0x0113 }
            r10.<init>()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r12 = "errCode"
            java.lang.String r0 = "101"
            r10.put(r12, r0)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r12 = "arg"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0113 }
            r0.<init>()     // Catch:{ Exception -> 0x0113 }
            r0.append(r13)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r6 = "."
            r0.append(r6)     // Catch:{ Exception -> 0x0113 }
            r0.append(r14)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0113 }
            r10.put(r12, r0)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r12 = "errMsg"
            java.lang.String r0 = r3.getBundleUrl()     // Catch:{ Exception -> 0x0113 }
            r10.put(r12, r0)     // Catch:{ Exception -> 0x0113 }
            android.content.Context r6 = r3.getContext()     // Catch:{ Exception -> 0x0113 }
            r7 = 0
            java.lang.String r8 = "invokeModule"
            r9 = 0
            r5.commit(r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x0113 }
        L_0x0102:
            java.lang.Object r12 = dispatchCallModuleMethod(r3, r2, r15, r4)     // Catch:{ Exception -> 0x0113 }
            boolean r13 = r2 instanceof com.taobao.weex.ui.module.WXDomModule
            if (r13 != 0) goto L_0x010e
            boolean r13 = r2 instanceof com.taobao.weex.ui.module.WXTimerModule
            if (r13 == 0) goto L_0x0110
        L_0x010e:
            r2.mWXSDKInstance = r1
        L_0x0110:
            return r12
        L_0x0111:
            r12 = move-exception
            goto L_0x0151
        L_0x0113:
            r12 = move-exception
            goto L_0x0125
        L_0x0115:
            java.lang.String r12 = "callModuleMethod >>> instance is null"
            com.taobao.weex.utils.WXLogUtils.e(r12)     // Catch:{ Exception -> 0x0113 }
            boolean r12 = r2 instanceof com.taobao.weex.ui.module.WXDomModule
            if (r12 != 0) goto L_0x0122
            boolean r12 = r2 instanceof com.taobao.weex.ui.module.WXTimerModule
            if (r12 == 0) goto L_0x0124
        L_0x0122:
            r2.mWXSDKInstance = r1
        L_0x0124:
            return r1
        L_0x0125:
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x0111 }
            r15.<init>()     // Catch:{ all -> 0x0111 }
            java.lang.String r0 = "callModuleMethod >>> invoke module:"
            r15.append(r0)     // Catch:{ all -> 0x0111 }
            r15.append(r13)     // Catch:{ all -> 0x0111 }
            java.lang.String r13 = ", method:"
            r15.append(r13)     // Catch:{ all -> 0x0111 }
            r15.append(r14)     // Catch:{ all -> 0x0111 }
            java.lang.String r13 = " failed. "
            r15.append(r13)     // Catch:{ all -> 0x0111 }
            java.lang.String r13 = r15.toString()     // Catch:{ all -> 0x0111 }
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r13, (java.lang.Throwable) r12)     // Catch:{ all -> 0x0111 }
            boolean r12 = r2 instanceof com.taobao.weex.ui.module.WXDomModule
            if (r12 != 0) goto L_0x014e
            boolean r12 = r2 instanceof com.taobao.weex.ui.module.WXTimerModule
            if (r12 == 0) goto L_0x0150
        L_0x014e:
            r2.mWXSDKInstance = r1
        L_0x0150:
            return r1
        L_0x0151:
            boolean r13 = r2 instanceof com.taobao.weex.ui.module.WXDomModule
            if (r13 != 0) goto L_0x0159
            boolean r13 = r2 instanceof com.taobao.weex.ui.module.WXTimerModule
            if (r13 == 0) goto L_0x015b
        L_0x0159:
            r2.mWXSDKInstance = r1
        L_0x015b:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXModuleManager.callModuleMethod(java.lang.String, java.lang.String, java.lang.String, com.alibaba.fastjson.JSONArray):java.lang.Object");
    }

    private static Object dispatchCallModuleMethod(@NonNull WXSDKInstance wXSDKInstance, @NonNull WXModule wXModule, @NonNull JSONArray jSONArray, @NonNull Invoker invoker) throws Exception {
        if (!wXSDKInstance.isPreRenderMode()) {
            return wXSDKInstance.getNativeInvokeHelper().invoke(wXModule, invoker, jSONArray);
        }
        if (invoker.isRunOnUIThread()) {
            return null;
        }
        return wXSDKInstance.getNativeInvokeHelper().invoke(wXModule, invoker, jSONArray);
    }

    public static boolean hasModule(String str) {
        return sGlobalModuleMap.containsKey(str) || sModuleFactoryMap.containsKey(str);
    }

    private static WXModule findModule(String str, String str2, ModuleFactory moduleFactory) {
        WXModule wXModule;
        WXModule wXModule2 = sGlobalModuleMap.get(str2);
        if (wXModule2 != null) {
            return wXModule2;
        }
        Map map = sInstanceModuleMap.get(str);
        if (map == null) {
            map = new ConcurrentHashMap();
            sInstanceModuleMap.put(str, map);
        }
        WXModule wXModule3 = (WXModule) map.get(str2);
        if (wXModule3 != null) {
            return wXModule3;
        }
        try {
            if (moduleFactory instanceof ConfigModuleFactory) {
                wXModule = ((ConfigModuleFactory) moduleFactory).buildInstance(WXSDKManager.getInstance().getSDKInstance(str));
            } else {
                wXModule = moduleFactory.buildInstance();
            }
            wXModule.setModuleName(str2);
            map.put(str2, wXModule);
            return wXModule;
        } catch (Exception e) {
            WXLogUtils.e(str2 + " module build instace failed.", (Throwable) e);
            return null;
        }
    }

    public static void onActivityCreate(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityCreate();
                } else {
                    WXLogUtils.w("onActivityCreate can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityStart(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityStart();
                } else {
                    WXLogUtils.w("onActivityStart can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityPause(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityPause();
                } else {
                    WXLogUtils.w("onActivityPause can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityResume(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityResume();
                } else {
                    WXLogUtils.w("onActivityResume can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityStop(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityStop();
                } else {
                    WXLogUtils.w("onActivityStop can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityDestroy(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityDestroy();
                } else {
                    WXLogUtils.w("onActivityDestroy can not find the " + str2 + " module");
                }
            }
        }
    }

    public static boolean onActivityBack(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map == null) {
            return false;
        }
        for (String str2 : map.keySet()) {
            WXModule wXModule = (WXModule) map.get(str2);
            if (wXModule != null) {
                return wXModule.onActivityBack();
            }
            WXLogUtils.w("onActivityCreate can not find the " + str2 + " module");
        }
        return false;
    }

    public static void onActivityResult(String str, int i, int i2, Intent intent) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityResult(i, i2, intent);
                } else {
                    WXLogUtils.w("onActivityResult can not find the " + str2 + " module");
                }
            }
        }
    }

    public static boolean onCreateOptionsMenu(String str, Menu menu) {
        Map map = sInstanceModuleMap.get(str);
        if (map == null) {
            return false;
        }
        for (String str2 : map.keySet()) {
            WXModule wXModule = (WXModule) map.get(str2);
            if (wXModule != null) {
                wXModule.onCreateOptionsMenu(menu);
            } else {
                WXLogUtils.w("onActivityResult can not find the " + str2 + " module");
            }
        }
        return false;
    }

    public static void onRequestPermissionsResult(String str, int i, String[] strArr, int[] iArr) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onRequestPermissionsResult(i, strArr, iArr);
                } else {
                    WXLogUtils.w("onActivityResult can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void destroyInstanceModules(String str) {
        sDomModuleMap.remove(str);
        Map remove = sInstanceModuleMap.remove(str);
        if (remove != null && remove.size() >= 1) {
            for (Map.Entry value : remove.entrySet()) {
                WXModule wXModule = (WXModule) value.getValue();
                if (wXModule instanceof Destroyable) {
                    ((Destroyable) wXModule).destroy();
                }
            }
        }
    }

    public static void createDomModule(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance != null) {
            sDomModuleMap.put(wXSDKInstance.getInstanceId(), new WXDomModule(wXSDKInstance));
        }
    }

    public static void destoryDomModule(String str) {
        sDomModuleMap.remove(str);
    }

    public static WXDomModule getDomModule(String str) {
        return sDomModuleMap.get(str);
    }

    public static void reload() {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
                try {
                    registerJSModule((String) entry.getKey(), ((ModuleFactoryImpl) entry.getValue()).mFactory);
                } catch (Throwable unused) {
                }
            }
        }
    }

    public static void registerWhenCreateInstance() {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
                try {
                    if (!((ModuleFactoryImpl) entry.getValue()).hasRigster) {
                        registerJSModule((String) entry.getKey(), ((ModuleFactoryImpl) entry.getValue()).mFactory);
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }

    public static void resetAllModuleState() {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry value : sModuleFactoryMap.entrySet()) {
                ((ModuleFactoryImpl) value.getValue()).hasRigster = false;
            }
        }
    }

    public static void resetModuleState(String str, boolean z) {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
                try {
                    if (entry.getKey() != null && ((String) entry.getKey()).equals(str)) {
                        ((ModuleFactoryImpl) entry.getValue()).hasRigster = z;
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }
}
