package com.alibaba.android.prefetchx.core.jsmodule;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PFUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSServiceSizeManager {
    private static volatile JSServiceSizeManager instance;
    private Map<String, String> allJSModules = new ConcurrentHashMap(PFJSModule.JS_MODULE_SIZE);
    private boolean hasReportTooManyJSModules = false;
    Map<String, Integer> sizeByHostPath = new ConcurrentHashMap();
    Map<String, Integer> sizeByJSModule = new ConcurrentHashMap(PFJSModule.JS_MODULE_SIZE);
    Map<String, Map<String, Integer>> sizeEachByHostPath = new ConcurrentHashMap();

    private JSServiceSizeManager() {
    }

    public static JSServiceSizeManager getInstance() {
        if (instance == null) {
            synchronized (JSServiceSizeManager.class) {
                if (instance == null) {
                    instance = new JSServiceSizeManager();
                }
            }
        }
        return instance;
    }

    public Pair<Boolean, String> load(@NonNull JSModulePojo jSModulePojo) {
        String key = jSModulePojo.getKey();
        String str = jSModulePojo.url;
        String str2 = jSModulePojo.name;
        String str3 = jSModulePojo.version;
        String str4 = jSModulePojo.jsModule;
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str)) {
            return new Pair<>(false, "in load. name or version or url is empty");
        }
        if (TextUtils.isEmpty(str4)) {
            return new Pair<>(false, "in load. jsModule is empty");
        }
        Integer valueOf = Integer.valueOf(str4.length());
        String urlHostPath = getUrlHostPath(str);
        if (isOverLimitByJSModule(str2, str3, str4)) {
            return new Pair<>(false, PFUtil.s("too much sizeByHostPath of one jsModule. ", key, ", length is" + valueOf));
        }
        Integer num = this.sizeByJSModule.get(key);
        if (valueOf.equals(num)) {
            return new Pair<>(false, PFUtil.s("key has been loaded. ignore this time. ", key));
        } else if (num == null || valueOf.equals(num)) {
            this.sizeByJSModule.put(key, valueOf);
            Integer num2 = this.sizeByHostPath.get(urlHostPath);
            if (num2 == null) {
                this.sizeByHostPath.put(urlHostPath, valueOf);
            } else {
                Integer valueOf2 = Integer.valueOf(num2.intValue() + valueOf.intValue());
                if (isOverLimitByHostPath(urlHostPath, valueOf2)) {
                    return new Pair<>(false, PFUtil.s("too much sizeByHostPath of hostPath. ", key, ", last sizeByHostPath is ", num2, ", now adding ", valueOf));
                }
                this.sizeByHostPath.put(urlHostPath, valueOf2);
            }
            Map map = this.sizeEachByHostPath.get(urlHostPath);
            if (map == null) {
                HashMap hashMap = new HashMap(32);
                hashMap.put(key, valueOf);
                this.sizeEachByHostPath.put(urlHostPath, hashMap);
            } else {
                map.put(key, valueOf);
            }
            this.allJSModules.put(key, str3);
            if (this.allJSModules.size() > 300 && !this.hasReportTooManyJSModules) {
                PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_TOO_MANY_JSMODULE, "too many js modules, over 300.", new Object[0]);
                this.hasReportTooManyJSModules = true;
            }
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, PFUtil.s("key has been loaded. ignore this time. but size is different! ", key, " lengthFromSize is ", num, " length is ", valueOf));
        }
    }

    public Pair<Boolean, String> unload(@NonNull JSModulePojo jSModulePojo) {
        String key = jSModulePojo.getKey();
        String str = jSModulePojo.name;
        String str2 = jSModulePojo.version;
        String str3 = jSModulePojo.url;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return new Pair<>(false, "in unload. name or version or url is empty");
        }
        String urlHostPath = getUrlHostPath(str3);
        Map map = this.sizeEachByHostPath.get(urlHostPath);
        if (map == null) {
            return new Pair<>(true, "");
        }
        Integer num = (Integer) map.remove(key);
        Integer num2 = this.sizeByHostPath.get(urlHostPath);
        if (num2 == null) {
            PFLog.JSModule.w(key + " is not loaded at sizeByHostPath Map.", new Throwable[0]);
        } else if (num != null) {
            this.sizeByHostPath.put(urlHostPath, Integer.valueOf(num2.intValue() - num.intValue()));
        }
        this.sizeByJSModule.remove(key);
        String str4 = this.allJSModules.get(str);
        if (str4 != null && !str4.equals(str2)) {
            return new Pair<>(false, "not the same version");
        }
        if (str4 != null && !str4.equals(str2)) {
            this.allJSModules.remove(str);
        }
        return new Pair<>(true, "");
    }

    private String getUrlHostPath(String str) {
        Uri parse = Uri.parse(str);
        if (!parse.isHierarchical()) {
            return str;
        }
        String str2 = parse.getHost() + parse.getPath();
        return str2.endsWith("//") ? str2.substring(0, str2.length() - 1) : str2;
    }

    /* access modifiers changed from: protected */
    public boolean isOverLimitByHostPath(String str, Integer num) {
        return num.intValue() > 20971520;
    }

    /* access modifiers changed from: protected */
    public boolean isOverLimitByJSModule(String str, String str2, String str3) {
        return str3.length() > 5242880;
    }

    public Map<String, String> getAllJSModules() {
        HashMap hashMap = new HashMap();
        synchronized (this) {
            hashMap.putAll(this.allJSModules);
        }
        return hashMap;
    }

    public Map<String, Integer> getAllSize() {
        HashMap hashMap = new HashMap();
        int i = 0;
        int i2 = 0;
        for (String str : this.sizeEachByHostPath.keySet()) {
            i2 += this.sizeByHostPath.get(str).intValue();
        }
        hashMap.put("sizeByHostPath", Integer.valueOf(i2));
        int i3 = 0;
        for (String str2 : this.sizeByJSModule.keySet()) {
            i3 += this.sizeByJSModule.get(str2).intValue();
        }
        hashMap.put("sizeByJSModule", Integer.valueOf(i3));
        for (String next : this.sizeEachByHostPath.keySet()) {
            for (String str3 : this.sizeEachByHostPath.get(next).keySet()) {
                i += ((Integer) this.sizeEachByHostPath.get(next).get(str3)).intValue();
            }
        }
        hashMap.put("sizeEachByHostPath", Integer.valueOf(i));
        return hashMap;
    }
}
