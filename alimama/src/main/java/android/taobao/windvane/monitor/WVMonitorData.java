package android.taobao.windvane.monitor;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mtopsdk.common.util.SymbolExpUtil;

public class WVMonitorData {
    public extra args = new extra();
    public long init = 0;
    public boolean isInit = false;
    public String performanceInfo = "";
    public String protocolType = "";
    public long startTime = 0;
    public stat stat = new stat();
    public String url;
    public int wvAppMonitor = 1;

    public HashMap<String, Object> toJsonStringMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("url", this.url);
        hashMap.put("loadTime", Long.valueOf(this.stat.onLoad));
        hashMap.put("isFinish", Integer.valueOf(this.stat.finish));
        hashMap.put("firstByte", Long.valueOf(this.stat.firstByteTime));
        hashMap.put("domLoad", Long.valueOf(this.stat.onDomLoad));
        hashMap.put("fromType", Integer.valueOf(this.stat.fromType));
        hashMap.put("matchCost", Long.valueOf(this.stat.matchCost));
        hashMap.put("statusCode", Integer.valueOf(this.args.statusCode));
        hashMap.put("packageappversion", this.stat.packageAppVersion);
        hashMap.put("packageAppName", this.stat.packageAppName);
        hashMap.put("verifyCacheSize", Integer.valueOf(this.stat.verifyCacheSize));
        hashMap.put("via", this.args.via);
        hashMap.put("verifyError", Integer.valueOf(this.stat.verifyError));
        hashMap.put("verifyResTime", Long.valueOf(this.stat.verifyResTime));
        hashMap.put("verifyTime", Long.valueOf(this.stat.verifyTime));
        hashMap.put("allVerifyTime", Long.valueOf(this.stat.allVerifyTime));
        if (this.args.netStat != null) {
            hashMap.put("netStat", toUtArray(this.args.netStat));
        }
        if (!this.args.resStat.isEmpty() && ((int) Math.ceil((Math.random() * 100.0d) + 0.5d)) <= WVMonitorConfigManager.getInstance().config.stat.resSample) {
            ArrayList arrayList = new ArrayList();
            for (Map.Entry next : this.args.resStat.entrySet()) {
                if (((resStat) next.getValue()).end - ((resStat) next.getValue()).start > WVMonitorConfigManager.getInstance().config.stat.resTime) {
                    Map<String, String> utMap = ((resStat) next.getValue()).toUtMap();
                    String str = (String) next.getKey();
                    Uri parse = Uri.parse(str);
                    if (parse != null && parse.isHierarchical()) {
                        utMap.put("url", str);
                        arrayList.add(utMap);
                    }
                }
            }
            hashMap.put("resStat", arrayList);
        }
        return hashMap;
    }

    public String[] toJsonStringDict() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("fromType=" + this.stat.fromType);
        if (!TextUtils.isEmpty(this.stat.appSeq)) {
            arrayList.add("PackageApp-Seq=" + this.stat.appSeq);
            arrayList.add("PackageApp-Version=" + this.stat.packageAppVersion);
            arrayList.add("PackageApp-Name=" + this.stat.packageAppName);
        }
        if (this.stat.onDomLoad > 0) {
            arrayList.add("domLoad=" + this.stat.onDomLoad);
        }
        if (((int) Math.ceil((Math.random() * 100.0d) + 0.5d)) <= WVMonitorConfigManager.getInstance().config.stat.resSample && !this.args.resStat.isEmpty()) {
            JSONArray jSONArray = new JSONArray();
            for (Map.Entry next : this.args.resStat.entrySet()) {
                if (((resStat) next.getValue()).end - ((resStat) next.getValue()).start >= WVMonitorConfigManager.getInstance().config.stat.resTime) {
                    Map<String, String> utMap = ((resStat) next.getValue()).toUtMap();
                    utMap.put("url", next.getKey());
                    jSONArray.put(new JSONObject(utMap));
                }
            }
            arrayList.add("resStat=" + jSONArray.toString());
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public static resStat createNewResStatInstance() {
        return new resStat();
    }

    public class stat {
        public long allVerifyTime = 0;
        public String appSeq = "";
        public int finish = 0;
        public long firstByteTime = 0;
        public int fromType = 1;
        public long matchCost = -1;
        public long onDomLoad = 0;
        public long onLoad = 0;
        public String packageAppName = "";
        public String packageAppVersion = "";
        public int verifyCacheSize = 0;
        public int verifyError = 0;
        public long verifyResTime = 0;
        public long verifyTime = 0;

        public stat() {
        }
    }

    public static class resStat {
        public long end;
        public int fromType;
        public WVPerformanceMonitorInterface.NetStat netStat;
        public String protocolType = "";
        public long start;
        public int statusCode;
        public long tcpTime = 0;
        public int verifyError = 0;
        public long verifyResTime = 0;
        public long verifyTime = 0;
        public String via;

        public Map<String, String> toUtMap() {
            Map<String, String> map;
            if (this.netStat == null) {
                map = new HashMap<>();
            } else {
                map = WVMonitorData.toUtMap(this.netStat);
            }
            if (this.statusCode > 0) {
                map.put("statusCode", String.valueOf(this.statusCode));
            }
            if (this.via != null) {
                map.put("via", this.via);
            }
            if (this.start > 0) {
                map.put("start", String.valueOf(this.start));
            }
            if (this.end > 0) {
                map.put("end", String.valueOf(this.end));
            }
            map.put("fromType", String.valueOf(this.fromType));
            map.put("protocolType", this.protocolType);
            map.put("tcpTime", String.valueOf(this.tcpTime));
            map.put("verifyError", String.valueOf(this.verifyError));
            map.put("verifyResTime", String.valueOf(this.verifyResTime));
            map.put("verifyTime", String.valueOf(this.verifyTime));
            return map;
        }
    }

    public class extra {
        public WVPerformanceMonitorInterface.NetStat netStat = null;
        public Map<String, resStat> resStat = new ConcurrentHashMap();
        public Map<String, Long> selfDefine = new ConcurrentHashMap();
        public int statusCode;
        public String via;

        public extra() {
        }
    }

    public static Map<String, String> toUtMap(WVPerformanceMonitorInterface.NetStat netStat) {
        HashMap hashMap = new HashMap();
        hashMap.put("net_dnsTime", String.valueOf(netStat.dnsTime));
        hashMap.put("net_isDNSTimeout", String.valueOf(netStat.isDNSTimeout));
        hashMap.put("net_oneWayTime", String.valueOf(netStat.oneWayTime));
        hashMap.put("net_tcpLinkDate", String.valueOf(netStat.tcpLinkDate));
        hashMap.put("net_waitTime", String.valueOf(netStat.waitTime));
        hashMap.put("net_postBodyTime", String.valueOf(netStat.postBodyTime));
        hashMap.put("net_firstDataTime", String.valueOf(netStat.firstDataTime));
        hashMap.put("net_serverRT", String.valueOf(netStat.serverRT));
        hashMap.put("net_totalSize", String.valueOf(netStat.totalSize));
        hashMap.put("net_recDataTime", String.valueOf(netStat.recDataTime));
        hashMap.put("net_isSSL", String.valueOf(netStat.isSSL));
        hashMap.put("net_dataSpeed", String.valueOf(netStat.dataSpeed));
        hashMap.put("net_spdy", String.valueOf(netStat.spdy));
        return hashMap;
    }

    public static ArrayList<String> toUtArray(WVPerformanceMonitorInterface.NetStat netStat) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Map.Entry next : toUtMap(netStat).entrySet()) {
            arrayList.add(((String) next.getKey()) + SymbolExpUtil.SYMBOL_EQUAL + ((String) next.getValue()));
        }
        return arrayList;
    }
}
