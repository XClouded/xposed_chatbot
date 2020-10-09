package android.taobao.windvane.packageapp.zipapp;

import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.vessel.utils.Utils;

import org.json.JSONObject;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

public class ZipPrefixesManager {
    public static final String DATA_KEY = "WVZipPrefixesData";
    public static final String SPNAME = "WVZipPrefixes";
    private static ZipPrefixesManager zipPrefixesManager;
    private String TAG = "PackageApp-ZipPrefixesManager";
    private Hashtable<String, Hashtable<String, String>> localPrefixes = null;
    public String prefix = "{}";
    private HashSet<String> zipAppsName = null;

    public static ZipPrefixesManager getInstance() {
        if (zipPrefixesManager == null) {
            synchronized (ZipPrefixesManager.class) {
                if (zipPrefixesManager == null) {
                    zipPrefixesManager = new ZipPrefixesManager();
                    zipPrefixesManager.init();
                }
            }
        }
        return zipPrefixesManager;
    }

    private void init() {
        String stringVal = ConfigStorage.getStringVal(SPNAME, DATA_KEY, "");
        if (!TextUtils.isEmpty(stringVal)) {
            this.localPrefixes = ZipAppUtils.parsePrefixes(stringVal);
            if (this.localPrefixes == null || this.localPrefixes.size() <= 0) {
                TaoLog.w("ZipPrefixesManager", "zipPrefixes parse failed");
                WVPackageAppPrefixesConfig.getInstance().resetConfig();
                ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_PREFIXES, "0");
                return;
            }
            parseZipAppsName();
            TaoLog.i("ZipPrefixesManager", "zipPrefixes parse success");
            return;
        }
        TaoLog.w("ZipPrefixesManager", "zipPrefixes readFile is empty data");
    }

    public String getZipAppName(String str) {
        if (this.localPrefixes == null || this.localPrefixes.size() == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        String replaceFirst = str.replaceFirst(CommonUtils.HTTP_PRE, "").replaceFirst(Utils.HTTPS_SCHEMA, "");
        Enumeration<String> keys = this.localPrefixes.keys();
        if (keys != null) {
            while (keys.hasMoreElements()) {
                String nextElement = keys.nextElement();
                if (replaceFirst.startsWith(nextElement)) {
                    String replaceFirst2 = replaceFirst.replaceFirst(nextElement, "");
                    Hashtable hashtable = this.localPrefixes.get(nextElement);
                    if (hashtable == null) {
                        continue;
                    } else if (hashtable.containsKey("*")) {
                        return (String) hashtable.get("*");
                    } else {
                        Enumeration keys2 = hashtable.keys();
                        boolean z = false;
                        while (keys2.hasMoreElements()) {
                            String str2 = (String) keys2.nextElement();
                            if ("".equals(str2)) {
                                z = true;
                            } else if (!str2.endsWith("/")) {
                                if (replaceFirst2.equals(str2)) {
                                    return (String) hashtable.get(str2);
                                }
                            } else if (replaceFirst2.startsWith(str2)) {
                                return (String) hashtable.get(str2);
                            }
                        }
                        if (z && !replaceFirst2.contains("/")) {
                            return (String) hashtable.get("");
                        }
                    }
                }
            }
        }
        return null;
    }

    public synchronized void saveLocalPrefixesData() {
        if (this.localPrefixes != null) {
            String jSONObject = new JSONObject(this.localPrefixes).toString();
            ConfigStorage.putStringVal(SPNAME, DATA_KEY, jSONObject);
            parseZipAppsName();
            String str = this.TAG;
            TaoLog.i(str, "saveLocalPrefixesData : " + jSONObject);
        }
    }

    private synchronized void parseZipAppsName() {
        if (this.localPrefixes != null && this.localPrefixes.size() > 0) {
            if (this.zipAppsName == null) {
                this.zipAppsName = new HashSet<>();
            }
            Enumeration<String> keys = this.localPrefixes.keys();
            while (keys.hasMoreElements()) {
                Hashtable hashtable = this.localPrefixes.get(keys.nextElement());
                if (hashtable != null) {
                    this.zipAppsName.addAll(hashtable.values());
                }
            }
            TaoLog.i(this.TAG, this.zipAppsName.toString());
        } else if (this.zipAppsName != null) {
            this.zipAppsName.clear();
        }
    }

    public synchronized boolean isAvailableApp(String str) {
        if (this.zipAppsName != null) {
            if (this.zipAppsName.size() != 0) {
                return this.zipAppsName.contains(str);
            }
        }
        return true;
    }

    public synchronized void clear() {
        if (this.localPrefixes != null) {
            this.localPrefixes.clear();
        }
    }

    public synchronized boolean mergePrefixes(Hashtable<String, Hashtable<String, String>> hashtable) {
        if (this.localPrefixes == null) {
            this.localPrefixes = new Hashtable<>();
        }
        Enumeration<String> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String nextElement = keys.nextElement();
            Hashtable hashtable2 = hashtable.get(nextElement);
            if (!nextElement.startsWith("//")) {
                nextElement = "//" + nextElement;
            }
            if (hashtable2.containsKey("*")) {
                String str = (String) hashtable2.get("*");
                if (!TextUtils.isEmpty(str)) {
                    if (str.equals("-1")) {
                        this.localPrefixes.remove(nextElement);
                        TaoLog.i(this.TAG, "mergPrefixes : removeAll :" + nextElement);
                    } else {
                        Hashtable hashtable3 = new Hashtable();
                        hashtable3.put("*", str);
                        this.localPrefixes.put(nextElement, hashtable3);
                    }
                }
            } else {
                Hashtable hashtable4 = new Hashtable();
                Hashtable hashtable5 = this.localPrefixes.get(nextElement);
                if (hashtable5 != null) {
                    Enumeration keys2 = hashtable5.keys();
                    while (keys2.hasMoreElements()) {
                        String str2 = (String) keys2.nextElement();
                        String str3 = (String) hashtable5.get(str2);
                        if (!"*".equals(str2)) {
                            if (str2.startsWith("/")) {
                                str2.replaceFirst("/", "");
                            }
                            hashtable4.put(str2, str3);
                            TaoLog.i(this.TAG, "mergPrefixes : retain :" + nextElement + ";  appPrefix : " + str2);
                        }
                    }
                }
                Enumeration keys3 = hashtable2.keys();
                while (keys3.hasMoreElements()) {
                    String str4 = (String) keys3.nextElement();
                    String str5 = (String) hashtable2.get(str4);
                    if (!"-1".equals(str5)) {
                        if (str4.startsWith("/")) {
                            str4.replaceFirst("/", "");
                        }
                        hashtable4.put(str4, str5);
                        TaoLog.i(this.TAG, "mergPrefixes : add :" + nextElement + ";  appPrefix : " + str4);
                    } else if (hashtable4.containsKey(str4)) {
                        hashtable4.remove(str4);
                        TaoLog.i(this.TAG, "mergPrefixes : remove :" + nextElement + ";  appPrefix : " + str4);
                    }
                }
                this.localPrefixes.put(nextElement, hashtable4);
            }
        }
        saveLocalPrefixesData();
        return true;
    }
}
