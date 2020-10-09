package com.ali.telescope.internal.pluginengine;

import android.content.Context;
import android.text.TextUtils;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.internal.pluginengine.model.PluginData;
import com.ali.telescope.util.FileUtils;
import com.ali.telescope.util.IOUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PluginDataManager {
    private static final String ASSETS_CONFIG_FILE = "telescope/pluginConfig.json";
    private static final String CONFIG_FILE_PATTERN = "plugin_config_%s.json";
    private static final String DIR_APM = "apm";
    private static final String DIR_CONFIG = "config";
    private static String appVersion;
    private static String configString = "";
    private static ArrayList<String> forceDisableList = new ArrayList<>();
    private static Map<String, PluginData> pluginConfigMap = new HashMap();
    public static String version;

    public static boolean loadCacheConfig() {
        return false;
    }

    public static void loadLocalConfig(Context context, String str) {
        appVersion = str;
        String readConfigData = readConfigData(context);
        if (TextUtils.isEmpty(readConfigData)) {
            initDefaultData();
        } else {
            initDataByConfig(readConfigData);
        }
    }

    private static void initDefaultData() {
        cleanPluginData();
        forceDisableList.add(PluginIDContant.KEY_CRASH_REPORT_PLUGIN);
        pluginConfigMap.put(PluginIDContant.KEY_CRASH_REPORT_PLUGIN, new PluginData(PluginIDContant.KEY_CRASH_REPORT_PLUGIN, false));
        pluginConfigMap.put(PluginIDContant.KEY_CPUPLUGIN, new PluginData(PluginIDContant.KEY_CPUPLUGIN, true));
        pluginConfigMap.put("StartPrefPlugin", new PluginData("StartPrefPlugin", true));
        pluginConfigMap.put(PluginIDContant.KEY_PAGE_LOAD_PLUGIN, new PluginData(PluginIDContant.KEY_PAGE_LOAD_PLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_SMOOTHPREF, new PluginData(PluginIDContant.KEY_SMOOTHPREF, true));
        pluginConfigMap.put(PluginIDContant.KEY_MEMORYPLUGIN, new PluginData(PluginIDContant.KEY_MEMORYPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_MEMLEAKPLUGIN, new PluginData(PluginIDContant.KEY_MEMLEAKPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_APP_EVENT_DETECT_PLUGIN, new PluginData(PluginIDContant.KEY_APP_EVENT_DETECT_PLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_MEMBITMAPPLUGIN, new PluginData(PluginIDContant.KEY_MEMBITMAPPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_FDOVERFLOWPLUGIN, new PluginData(PluginIDContant.KEY_FDOVERFLOWPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_MAINTHREADBLOCKPLUGIN, new PluginData(PluginIDContant.KEY_MAINTHREADBLOCKPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_MAINTHREADIOPLUGIN, new PluginData(PluginIDContant.KEY_MAINTHREADIOPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_RESOURCELEAKPLUGIN, new PluginData(PluginIDContant.KEY_RESOURCELEAKPLUGIN, true));
        pluginConfigMap.put(PluginIDContant.KEY_UPLOADPLUGIN, new PluginData(PluginIDContant.KEY_UPLOADPLUGIN, true));
        version = "10";
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initDataByConfig(java.lang.String r12) {
        /*
            java.lang.String r0 = "0"
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r3 = 1
            r4 = 0
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ JSONException -> 0x008b, Exception -> 0x0080 }
            r5.<init>(r12)     // Catch:{ JSONException -> 0x008b, Exception -> 0x0080 }
            java.lang.String r6 = "version"
            java.lang.String r6 = r5.getString(r6)     // Catch:{ JSONException -> 0x008b, Exception -> 0x0080 }
            java.lang.String r0 = "forceDisable"
            boolean r0 = r5.has(r0)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            if (r0 == 0) goto L_0x0038
            java.lang.String r0 = "forceDisable"
            org.json.JSONArray r0 = r5.getJSONArray(r0)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            r7 = 0
        L_0x0028:
            int r8 = r0.length()     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            if (r7 >= r8) goto L_0x0043
            java.lang.String r8 = r0.getString(r7)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            r2.add(r8)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            int r7 = r7 + 1
            goto L_0x0028
        L_0x0038:
            java.lang.String r0 = "PluginDataManager"
            java.lang.String[] r7 = new java.lang.String[r3]     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            java.lang.String r8 = "localConfig file no 'disable' phase!"
            r7[r4] = r8     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            com.ali.telescope.util.TelescopeLog.i(r0, r7)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
        L_0x0043:
            java.lang.String r0 = "plugins"
            org.json.JSONArray r0 = r5.getJSONArray(r0)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            r5 = 0
        L_0x004a:
            int r7 = r0.length()     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            if (r5 >= r7) goto L_0x007a
            org.json.JSONObject r7 = r0.getJSONObject(r5)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            java.lang.String r8 = "name"
            java.lang.String r8 = r7.getString(r8)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            java.lang.String r9 = "enable"
            boolean r9 = r7.getBoolean(r9)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            r10 = 0
            java.lang.String r11 = "params"
            boolean r11 = r7.has(r11)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            if (r11 == 0) goto L_0x006f
            java.lang.String r10 = "params"
            org.json.JSONObject r10 = r7.getJSONObject(r10)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
        L_0x006f:
            com.ali.telescope.internal.pluginengine.model.PluginData r7 = new com.ali.telescope.internal.pluginengine.model.PluginData     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            r7.<init>(r8, r9, r10)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            r1.put(r8, r7)     // Catch:{ JSONException -> 0x007e, Exception -> 0x007c }
            int r5 = r5 + 1
            goto L_0x004a
        L_0x007a:
            r4 = 1
            goto L_0x0095
        L_0x007c:
            r0 = move-exception
            goto L_0x0083
        L_0x007e:
            r0 = move-exception
            goto L_0x008e
        L_0x0080:
            r3 = move-exception
            r6 = r0
            r0 = r3
        L_0x0083:
            java.lang.String r3 = "PluginDataManager"
            java.lang.String r5 = "localConfig file error"
            com.ali.telescope.util.StrictRuntime.onHandle(r3, r5, r0)
            goto L_0x0095
        L_0x008b:
            r3 = move-exception
            r6 = r0
            r0 = r3
        L_0x008e:
            java.lang.String r3 = "PluginDataManager"
            java.lang.String r5 = "localConfig file json error"
            com.ali.telescope.util.StrictRuntime.onHandle(r3, r5, r0)
        L_0x0095:
            if (r4 == 0) goto L_0x00a2
            cleanPluginData()
            pluginConfigMap = r1
            forceDisableList = r2
            version = r6
            configString = r12
        L_0x00a2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.pluginengine.PluginDataManager.initDataByConfig(java.lang.String):void");
    }

    public static void cleanPluginData() {
        pluginConfigMap.clear();
        forceDisableList.clear();
    }

    public static Map<String, PluginData> getAllPluginData() {
        return pluginConfigMap;
    }

    public static boolean isPluginForceDisable(String str) {
        return forceDisableList.contains(str);
    }

    public static void onConfigChanged(String str, Context context) {
        if (!TextUtils.isEmpty(str) && !str.equals(configString)) {
            File cachedConfigFile = getCachedConfigFile(context);
            if (!cachedConfigFile.getParentFile().exists()) {
                cachedConfigFile.getParentFile().mkdirs();
            }
            synchronized (configString) {
                try {
                    FileUtils.write(cachedConfigFile, str, Charset.forName("utf-8"));
                    configString = str;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readConfigData(Context context) {
        File cachedConfigFile = getCachedConfigFile(context);
        try {
            if (!cachedConfigFile.exists() || cachedConfigFile.length() <= 0) {
                return IOUtils.toString(context.getAssets().open(ASSETS_CONFIG_FILE), Charset.forName("utf-8"));
            }
            return FileUtils.readFileToString(cachedConfigFile, Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File getCachedConfigFile(Context context) {
        File file = new File(FileUtils.getTelescopeFilePath(context, "config"));
        if (!file.exists()) {
            file.mkdir();
        }
        return new File(file, String.format(CONFIG_FILE_PATTERN, new Object[]{appVersion}));
    }
}
