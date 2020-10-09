package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

class StrategyConfig implements Serializable {
    public static final String NO_RESULT = "No_Result";
    private static final long serialVersionUID = -7798500032935529499L;
    private transient StrategyInfoHolder holder = null;
    private SerialLruCache<String, String> schemeMap = null;
    private Map<String, String> unitMap = null;

    StrategyConfig() {
    }

    /* access modifiers changed from: package-private */
    public void setHolder(StrategyInfoHolder strategyInfoHolder) {
        this.holder = strategyInfoHolder;
    }

    /* access modifiers changed from: package-private */
    public void checkInit() {
        if (this.schemeMap == null) {
            this.schemeMap = new SerialLruCache<>(256);
        }
        if (this.unitMap == null) {
            this.unitMap = new ConcurrentHashMap();
        }
    }

    /* access modifiers changed from: package-private */
    public void update(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
        if (httpDnsResponse.dnsInfo != null) {
            synchronized (this) {
                TreeMap treeMap = null;
                for (StrategyResultParser.DnsInfo dnsInfo : httpDnsResponse.dnsInfo) {
                    if (dnsInfo.clear) {
                        this.schemeMap.remove(dnsInfo.host);
                    } else if (dnsInfo.cname != null) {
                        if (treeMap == null) {
                            treeMap = new TreeMap();
                        }
                        treeMap.put(dnsInfo.host, dnsInfo.cname);
                    } else {
                        if ("http".equalsIgnoreCase(dnsInfo.safeAisles) || "https".equalsIgnoreCase(dnsInfo.safeAisles)) {
                            this.schemeMap.put(dnsInfo.host, dnsInfo.safeAisles);
                        } else {
                            this.schemeMap.put(dnsInfo.host, NO_RESULT);
                        }
                        if (!TextUtils.isEmpty(dnsInfo.unit)) {
                            this.unitMap.put(dnsInfo.host, dnsInfo.unit);
                        } else {
                            this.unitMap.remove(dnsInfo.host);
                        }
                    }
                }
                if (treeMap != null) {
                    for (Map.Entry entry : treeMap.entrySet()) {
                        String str = (String) entry.getValue();
                        if (this.schemeMap.containsKey(str)) {
                            this.schemeMap.put(entry.getKey(), this.schemeMap.get(str));
                        } else {
                            this.schemeMap.put(entry.getKey(), NO_RESULT);
                        }
                    }
                }
            }
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.StrategyConfig", "", (String) null, "SchemeMap", this.schemeMap.toString());
                ALog.d("awcn.StrategyConfig", "", (String) null, "UnitMap", this.unitMap.toString());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String getSafeAislesByHost(String str) {
        String str2;
        if (TextUtils.isEmpty(str) || !Utils.checkHostValidAndNotIp(str)) {
            return null;
        }
        synchronized (this.schemeMap) {
            str2 = (String) this.schemeMap.get(str);
            if (str2 == null) {
                this.schemeMap.put(str, NO_RESULT);
            }
        }
        if (str2 == null) {
            this.holder.getCurrStrategyTable().sendAmdcRequest(str, false);
        } else if (NO_RESULT.equals(str2)) {
            return null;
        }
        return str2;
    }

    /* access modifiers changed from: package-private */
    public String getUnitByHost(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return this.unitMap.get(str);
    }
}
