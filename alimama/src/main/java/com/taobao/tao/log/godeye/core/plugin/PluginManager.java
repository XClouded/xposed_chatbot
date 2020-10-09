package com.taobao.tao.log.godeye.core.plugin;

import android.app.Application;
import com.taobao.tao.log.godeye.core.control.Godeye;
import com.taobao.tao.log.godeye.core.plugin.runtime.BuildInPlugin;
import com.taobao.tao.log.godeye.core.plugin.runtime.Plugin;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class PluginManager {
    private static final String[] DEFAULT_PLUGIN_INITIALIZER = {"com.taobao.tao.log.godeye.methodtrace.MethodTraceInitializer", "com.taobao.tao.log.godeye.memorydump.MemoryDumpInitializer"};
    private static final String PLUGINS_CONFIG_FILE_NAME = "godeye.plugin.cfg";
    private static boolean defaultPluginsInitialized = false;
    private static final HashSet<String> initedPlugins = new HashSet<>();

    public static void loadPlugin(Application application) throws Exception {
        File filesDir = application.getFilesDir();
        File file = new File(filesDir, PLUGINS_CONFIG_FILE_NAME + Godeye.sharedInstance().getAppVersion());
        if (file.exists()) {
            Iterator<Plugin.PluginData> it = parsePluginData(file).iterator();
            while (it.hasNext()) {
                Plugin.PluginData next = it.next();
                new BuildInPlugin(next).execute();
                initedPlugins.add(next.getMainClass());
            }
        }
    }

    public static void removeAllPlugins(Application application) {
        File filesDir = application.getFilesDir();
        File file = new File(filesDir, PLUGINS_CONFIG_FILE_NAME + Godeye.sharedInstance().getAppVersion());
        if (file.exists()) {
            file.delete();
        }
        defaultPluginsInitialized = false;
    }

    private static ArrayList<Plugin.PluginData> parsePluginData(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        ArrayList<Plugin.PluginData> arrayList = new ArrayList<>();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                bufferedReader.close();
                return arrayList;
            } else if (!"".equals(readLine)) {
                Plugin.PluginData pluginData = new Plugin.PluginData();
                pluginData.setMainClass(readLine);
                arrayList.add(pluginData);
            }
        }
    }

    public static void loadDefaultPlugins() throws Exception {
        if (!defaultPluginsInitialized) {
            boolean z = false;
            for (String str : DEFAULT_PLUGIN_INITIALIZER) {
                if (!initedPlugins.contains(str)) {
                    Plugin.PluginData pluginData = new Plugin.PluginData();
                    pluginData.setMainClass(str);
                    new BuildInPlugin(pluginData).execute();
                    initedPlugins.add(str);
                    z = true;
                }
            }
            if (z) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(Godeye.sharedInstance().getApplication().getFilesDir(), PLUGINS_CONFIG_FILE_NAME + Godeye.sharedInstance().getAppVersion())));
                bufferedWriter.write("");
                Iterator<String> it = initedPlugins.iterator();
                while (it.hasNext()) {
                    bufferedWriter.append(it.next()).append("\n");
                }
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            defaultPluginsInitialized = true;
        }
    }
}
