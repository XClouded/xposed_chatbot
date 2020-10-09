package com.taobao.android.dinamic;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.dinamic.AbsDinamicEventHandler;
import com.taobao.android.dinamic.dinamic.DinamicEventHandler;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.exception.DinamicException;
import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.log.DinamicLogThread;
import com.taobao.android.dinamic.log.IDinamicLog;
import com.taobao.android.dinamic.property.ScreenTool;
import com.taobao.android.dinamic.tempate.DTemplateManager;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.tempate.DinamicTemplateDownloaderCallback;
import com.taobao.android.dinamic.tempate.manager.TemplateCache;
import com.taobao.android.dinamic.view.ViewResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Dinamic {
    public static final String TAG = "Dinamic";
    private static Context dinamicContext = null;
    private static boolean init = false;
    private static boolean isDebugable = false;
    private static Map<String, ModuleContainer> moduleMap = new HashMap();

    @Deprecated
    public static void registeLogger(IDinamicLog iDinamicLog) {
    }

    @Deprecated
    public static void unregisteEventHandler(String str) throws DinamicException {
    }

    @Deprecated
    public static void unregisteView(String str) throws DinamicException {
    }

    @Deprecated
    public static void unregisterEventHandler(String str) throws DinamicException {
    }

    public static boolean unregisterParser(String str) {
        return false;
    }

    @Deprecated
    public static void unregisterView(String str) throws DinamicException {
    }

    public static void init(Context context, boolean z) {
        if (!init) {
            isDebugable = z;
            dinamicContext = context.getApplicationContext();
            moduleMap.put("default", ModuleContainer.build("default"));
            DinamicLogThread.init("monitor");
            init = true;
        }
    }

    public static ModuleContainer getModuleContainer(String str) {
        if (moduleMap.containsKey(str)) {
            return moduleMap.get(str);
        }
        ModuleContainer build = ModuleContainer.build(str);
        moduleMap.put(str, build);
        return build;
    }

    public static boolean isDebugable() {
        return isDebugable;
    }

    public static ViewResult createView(Context context, ViewGroup viewGroup, DinamicTemplate dinamicTemplate) {
        return DViewGenerator.defaultViewGenerator().createView(context, viewGroup, dinamicTemplate);
    }

    public static ViewResult bindData(View view, Object obj) {
        return DViewGenerator.defaultViewGenerator().bindData(view, obj);
    }

    public static ViewResult bindData(View view, JSONObject jSONObject) {
        return DViewGenerator.defaultViewGenerator().bindData(view, jSONObject);
    }

    @Deprecated
    public static ViewResult bindData(View view, Object obj, boolean z) {
        if (z) {
            return DViewGenerator.defaultViewGenerator().bindDataWithRoop(view, obj);
        }
        return DViewGenerator.defaultViewGenerator().bindData(view, obj);
    }

    public static ViewResult bindDataWithRoop(View view, Object obj) {
        return DViewGenerator.defaultViewGenerator().bindDataWithRoop(view, obj);
    }

    public static ViewResult bindData(View view, Object obj, Object obj2) {
        return DViewGenerator.defaultViewGenerator().bindData(view, obj, obj2);
    }

    @Deprecated
    public static void registeView(String str, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        DRegisterCenter.shareCenter().registerViewConstructor(str, dinamicViewAdvancedConstructor);
    }

    public static void registerView(String str, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        DRegisterCenter.shareCenter().registerViewConstructor(str, dinamicViewAdvancedConstructor);
    }

    @Deprecated
    public static void registeEventHandler(String str, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        DRegisterCenter.shareCenter().registerEventHandler(str, absDinamicEventHandler);
    }

    public static void registerEventHandler(String str, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        DRegisterCenter.shareCenter().registerEventHandler(str, absDinamicEventHandler);
    }

    public static DinamicViewAdvancedConstructor getViewConstructor(String str) {
        return DinamicViewHelper.getViewConstructor(str);
    }

    public static DinamicEventHandler getEventHandler(String str) {
        return DinamicViewHelper.getEventHandler(str);
    }

    public static void registerParser(String str, AbsDinamicDataParser absDinamicDataParser) throws DinamicException {
        DRegisterCenter.shareCenter().registerDataParser(str, absDinamicDataParser);
    }

    @Deprecated
    public static void registeHttpLoader(TemplateCache.HttpLoader httpLoader) {
        DRegisterCenter.shareCenter().registerHttpLoader(httpLoader);
    }

    public static void registerHttpLoader(TemplateCache.HttpLoader httpLoader) {
        DRegisterCenter.shareCenter().registerHttpLoader(httpLoader);
    }

    public static void registerLogger(IDinamicLog iDinamicLog) {
        DRegisterCenter.shareCenter().registerLogger(iDinamicLog);
    }

    public static void downloadTemplates(List<DinamicTemplate> list, DinamicTemplateDownloaderCallback dinamicTemplateDownloaderCallback) {
        DTemplateManager.defaultTemplateManager().downloadTemplates(list, dinamicTemplateDownloaderCallback);
    }

    public static Context getContext() {
        return dinamicContext;
    }

    public static void setDinamicContext(Context context) {
        dinamicContext = context;
    }

    public static void clearLruCache(String str) {
        moduleMap.remove(str);
    }

    public static void processWindowChanged(boolean z) {
        try {
            boolean checkScreenWidthChanged = ScreenTool.checkScreenWidthChanged();
            if (isDebugable()) {
                Log.d(TAG, "Dinamic processWindowChanged checkRet" + checkScreenWidthChanged);
            }
            if (checkScreenWidthChanged || z) {
                ScreenTool.forceResetScreenSize();
            }
        } catch (Exception e) {
            if (isDebugable()) {
                e.printStackTrace();
            }
        }
    }
}
