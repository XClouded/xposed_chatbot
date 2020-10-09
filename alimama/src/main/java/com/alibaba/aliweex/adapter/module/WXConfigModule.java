package com.alibaba.aliweex.adapter.module;

import android.text.TextUtils;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IConfigGeneratorAdapter;
import com.alibaba.aliweex.adapter.IConfigModuleAdapter;
import com.alibaba.aliweex.adapter.IConfigModuleListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WXConfigModule extends WXModule implements IConfigModuleListener, Destroyable {
    public static final String NAME = "orange";
    private IConfigModuleAdapter adapter;
    private Map<String, Map<String, String>> eventMap = new HashMap();

    private IConfigModuleAdapter getConfigAdapter() {
        IConfigGeneratorAdapter configGeneratorAdapter;
        if (this.adapter == null && (configGeneratorAdapter = AliWeex.getInstance().getConfigGeneratorAdapter()) != null) {
            this.adapter = configGeneratorAdapter.generateConfigInstance("");
        }
        if (this.adapter == null) {
            this.adapter = new IConfigModuleAdapter() {
                public boolean checkMode(String str) {
                    return false;
                }

                public void destroy() {
                }

                public String getConfig(String str, String str2, String str3) {
                    return null;
                }

                public Map<String, String> getConfigs(String str) {
                    return null;
                }

                public void registerListener(String[] strArr, IConfigModuleListener iConfigModuleListener) {
                }

                public void unregisterListener(String[] strArr, IConfigModuleListener iConfigModuleListener) {
                }
            };
        }
        return this.adapter;
    }

    @JSMethod(uiThread = false)
    public Object getConfig(String str) {
        JSONObject parseObject = JSON.parseObject(str);
        String string = parseObject.getString("namespace");
        String string2 = parseObject.getString("key");
        String string3 = parseObject.getString("defaultValue");
        if (TextUtils.isEmpty(string2)) {
            return getConfigAdapter().getConfigs(string);
        }
        return getConfigAdapter().getConfig(string, string2, string3);
    }

    @JSMethod
    public void addEventListener(String str, String str2, Map<String, Object> map) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            String string = WXUtils.getString(map.get("action"), "");
            if (!TextUtils.isEmpty(string)) {
                super.addEventListener(str, str2, map);
                Map map2 = this.eventMap.get(str);
                if (map2 == null) {
                    map2 = new HashMap();
                    this.eventMap.put(str, map2);
                }
                map2.put(string, str2);
                getConfigAdapter().registerListener(getNameSpace(str), this);
            }
        }
    }

    @JSMethod
    public void removeActionListener(String str, Map<String, Object> map) {
        Map map2;
        if (!TextUtils.isEmpty(str)) {
            String string = WXUtils.getString(map.get("action"), "");
            if (!TextUtils.isEmpty(string) && (map2 = this.eventMap.get(str)) != null && !map2.isEmpty()) {
                getEventCallbacks(str).remove((String) map2.remove(string));
                if (map2.isEmpty()) {
                    this.eventMap.remove(str);
                    getConfigAdapter().unregisterListener(getNameSpace(str), this);
                }
            }
        }
    }

    @JSMethod
    public void removeEventListener(String str) {
        removeAllEventListeners(str);
    }

    @JSMethod
    public void removeAllEventListener() {
        Iterator<Map.Entry<String, Map<String, String>>> it = this.eventMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry next = it.next();
            getConfigAdapter().unregisterListener(getNameSpace((String) next.getKey()), this);
            super.removeAllEventListeners((String) next.getKey());
            it.remove();
        }
    }

    public void removeAllEventListeners(String str) {
        super.removeAllEventListeners(str);
        getConfigAdapter().unregisterListener(getNameSpace(str), this);
        this.eventMap.remove(str);
    }

    public void onConfigUpdate(String str, Map<String, String> map) {
        List<String> eventCallbacks = getEventCallbacks(str);
        if (eventCallbacks != null && !eventCallbacks.isEmpty()) {
            HashMap hashMap = new HashMap();
            hashMap.put("namespace", str);
            hashMap.put("configVersion", map.get("configVersion"));
            hashMap.put("args", getConfigAdapter().getConfigs(str));
            for (String callback : eventCallbacks) {
                WXSDKManager.getInstance().callback(this.mWXSDKInstance.getInstanceId(), callback, hashMap, true);
            }
        }
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        getConfigAdapter().destroy();
        removeAllEventListener();
    }

    private static String[] getNameSpace(String str) {
        return new String[]{str};
    }

    public void destroy() {
        getConfigAdapter().destroy();
        removeAllEventListener();
    }
}
