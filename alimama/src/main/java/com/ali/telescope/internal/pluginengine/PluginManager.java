package com.ali.telescope.internal.pluginengine;

import android.app.Application;
import androidx.annotation.NonNull;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.plugin.PluginIDContant;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.pluginengine.model.PluginData;
import com.ali.telescope.internal.plugins.anr.ANRPlugin;
import com.ali.telescope.internal.plugins.appevent.AppEventDetectPlugin;
import com.ali.telescope.internal.plugins.bitmap.MemoryBitmapPlugin;
import com.ali.telescope.internal.plugins.cpu.CpuPlugin;
import com.ali.telescope.internal.plugins.fdoverflow.FdOverflowMonitorPlugin;
import com.ali.telescope.internal.plugins.mainthreadblock.MainThreadBlockPlugin;
import com.ali.telescope.internal.plugins.memleak.MemoryLeakPlugin;
import com.ali.telescope.internal.plugins.memory.MemoryPlugin;
import com.ali.telescope.internal.plugins.pageload.PageLoadPlugin;
import com.ali.telescope.internal.plugins.resourceleak.ResourceLeakPlugin;
import com.ali.telescope.internal.plugins.smooth.SmoothPlugin;
import com.ali.telescope.internal.plugins.startPref.StartPrefPlugin;
import com.ali.telescope.internal.plugins.systemcompoment.SystemComponentPlugin;
import com.ali.telescope.internal.plugins.threadio.IOMonitorPlugin;
import com.ali.telescope.internal.plugins.upload.UploadPlugin;
import com.ali.telescope.util.StrictRuntime;
import com.ali.telescope.util.TelescopeLog;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

public class PluginManager {
    public static final String PREFIX = "com.ali.telescope.";
    private static final String TAG = "PLUGIN_MANAGER";
    private static Map<String, Class> pluginClasses = new HashMap();
    /* access modifiers changed from: private */
    public static volatile Application sApplication;
    private static boolean sInited;
    /* access modifiers changed from: private */
    public static Map<String, Plugin> sPlugins = new ConcurrentHashMap();
    private static Map<String, String> sSubscribers = new HashMap();
    /* access modifiers changed from: private */
    public static volatile ITelescopeContext sTContext;

    public static synchronized void init(@NonNull Application application, @NonNull ITelescopeContext iTelescopeContext) {
        synchronized (PluginManager.class) {
            if (!sInited) {
                sApplication = application;
                sTContext = iTelescopeContext;
                addPluginClass(PluginIDContant.KEY_CPUPLUGIN, CpuPlugin.class);
                addPluginClass(PluginIDContant.KEY_MEMORYPLUGIN, MemoryPlugin.class);
                addPluginClass(PluginIDContant.KEY_SMOOTHPREF, SmoothPlugin.class);
                addPluginClass(PluginIDContant.KEY_APP_EVENT_DETECT_PLUGIN, AppEventDetectPlugin.class);
                addPluginClass(PluginIDContant.KEY_MEMLEAKPLUGIN, MemoryLeakPlugin.class);
                addPluginClass(PluginIDContant.KEY_MEMBITMAPPLUGIN, MemoryBitmapPlugin.class);
                addPluginClass(PluginIDContant.KEY_SYSTEMCOMPONENTPLUGIN, SystemComponentPlugin.class);
                addPluginClass(PluginIDContant.KEY_PAGE_LOAD_PLUGIN, PageLoadPlugin.class);
                addPluginClass(PluginIDContant.KEY_FDOVERFLOWPLUGIN, FdOverflowMonitorPlugin.class);
                addPluginClass(PluginIDContant.KEY_MAINTHREADBLOCKPLUGIN, MainThreadBlockPlugin.class);
                addPluginClass("StartPrefPlugin", StartPrefPlugin.class);
                addPluginClass(PluginIDContant.KEY_MAINTHREADIOPLUGIN, IOMonitorPlugin.class);
                addPluginClass(PluginIDContant.KEY_RESOURCELEAKPLUGIN, ResourceLeakPlugin.class);
                addPluginClass(PluginIDContant.KEY_UPLOADPLUGIN, UploadPlugin.class);
                sInited = true;
                new ANRPlugin().onCreate(sApplication, sTContext, (JSONObject) null);
            }
        }
    }

    public static void addPluginClass(String str, Class cls) {
        pluginClasses.put(str, cls);
    }

    public static void loadPlugin(@NonNull Map<String, PluginData> map) {
        for (String str : map.keySet()) {
            PluginData pluginData = map.get(str);
            if (!PluginDataManager.isPluginForceDisable(pluginData.name) && pluginData.enable) {
                if (PluginIDContant.isOuterPlugin(pluginData.name)) {
                    createPlugin(pluginData.name, pluginData.params);
                } else if (pluginClasses.containsKey(pluginData.name)) {
                    createPlugin(pluginData.name, pluginClasses.get(pluginData.name), pluginData.params);
                } else {
                    TelescopeLog.e(TAG, "The plugin [" + pluginData.name + "] is not supported!");
                }
            }
        }
    }

    public static void createPlugin(@NonNull String str, @NonNull JSONObject jSONObject) {
        try {
            createPlugin(str, Class.forName(PREFIX + str), jSONObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createPlugin(@NonNull final String str, @NonNull final Class cls, final JSONObject jSONObject) {
        checkInit();
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                try {
                    if (PluginManager.sPlugins.get(str) != null) {
                        StrictRuntime.onHandle(PluginManager.TAG, "plugin (" + str + ") already exist!", new RuntimeException("test"));
                        return;
                    }
                    Plugin plugin = (Plugin) cls.newInstance();
                    PluginManager.sPlugins.put(str, plugin);
                    plugin.pluginID = str;
                    plugin.onCreate(PluginManager.sApplication, PluginManager.sTContext, jSONObject);
                    TelescopeLog.d(PluginManager.TAG, str + "is create");
                } catch (Throwable th) {
                    StrictRuntime.onHandle(new RuntimeException("createPlugin error!", th));
                }
            }
        };
        if (str.equals("StartPrefPlugin")) {
            r0.run();
        } else {
            Loopers.getTelescopeHandler().post(r0);
        }
    }

    public static Collection<Plugin> getAllPlugin() {
        return sPlugins.values();
    }

    public static Plugin getPluginByPluginId(String str) {
        return sPlugins.get(str);
    }

    private static synchronized void checkInit() {
        synchronized (PluginManager.class) {
            if (!sInited) {
                throw new IllegalStateException("please call init first");
            }
        }
    }

    public static void destroyPlugin(@NonNull final String str) {
        checkInit();
        Loopers.getTelescopeHandler().post(new Runnable() {
            public void run() {
                Plugin plugin = (Plugin) PluginManager.sPlugins.get(str);
                if (plugin != null) {
                    plugin.onDestroy();
                    PluginManager.sPlugins.remove(str);
                    return;
                }
                StrictRuntime.onHandle(PluginManager.TAG, "destroy a un-exsited plugin:", new RuntimeException());
            }
        });
    }

    public static void destoryAll() {
        if (sInited) {
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    for (String str : PluginManager.sPlugins.keySet()) {
                        Plugin plugin = (Plugin) PluginManager.sPlugins.get(str);
                        if (plugin != null) {
                            plugin.onDestroy();
                        } else {
                            StrictRuntime.onHandle(PluginManager.TAG, "destroyAll a un-exsited plugin:", new RuntimeException());
                        }
                    }
                    PluginManager.sPlugins.clear();
                }
            });
        }
    }
}
