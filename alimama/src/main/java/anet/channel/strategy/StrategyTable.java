package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.HttpConstant;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

class StrategyTable implements Serializable {
    private static final int FRESH_TTL = 30000;
    private static final int MAX_HOST_COUNT_IN_ONCE_UPDATE = 40;
    private static final int MAX_HOST_SIZE = 256;
    private static final String TAG = "awcn.StrategyTable";
    protected static Comparator<StrategyCollection> comparator = new Comparator<StrategyCollection>() {
        public int compare(StrategyCollection strategyCollection, StrategyCollection strategyCollection2) {
            if (strategyCollection.ttl != strategyCollection2.ttl) {
                return (int) (strategyCollection.ttl - strategyCollection2.ttl);
            }
            return strategyCollection.host.compareTo(strategyCollection2.host);
        }
    };
    private static final long serialVersionUID = 6044722613437834958L;
    protected volatile String clientIp;
    private volatile transient int configVersion;
    boolean enableQuic = false;
    private HostLruCache hostStrategyMap;
    Map<String, Long> ipv6BlackList;
    protected transient boolean isChanged = false;
    protected String uniqueId;

    private static class HostLruCache extends SerialLruCache<String, StrategyCollection> {
        private static final long serialVersionUID = -4001655685948369525L;

        public HostLruCache(int i) {
            super(i);
        }

        /* access modifiers changed from: protected */
        public boolean entryRemoved(Map.Entry<String, StrategyCollection> entry) {
            if (!entry.getValue().isFixed) {
                return true;
            }
            Iterator it = entrySet().iterator();
            while (it.hasNext()) {
                if (!((StrategyCollection) ((Map.Entry) it.next()).getValue()).isFixed) {
                    it.remove();
                    return false;
                }
            }
            return false;
        }
    }

    protected StrategyTable(String str) {
        this.uniqueId = str;
        checkInit();
    }

    private void initStrategy() {
        if (HttpDispatcher.getInstance().isInitHostsChanged(this.uniqueId)) {
            for (String next : HttpDispatcher.getInstance().getInitHosts()) {
                this.hostStrategyMap.put(next, new StrategyCollection(next));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void checkInit() {
        if (this.hostStrategyMap == null) {
            this.hostStrategyMap = new HostLruCache(256);
            initStrategy();
        }
        for (StrategyCollection checkInit : this.hostStrategyMap.values()) {
            checkInit.checkInit();
        }
        int i = 0;
        ALog.i(TAG, "strategy map", (String) null, "size", Integer.valueOf(this.hostStrategyMap.size()));
        if (!GlobalAppRuntimeInfo.isTargetProcess()) {
            i = -1;
        }
        this.configVersion = i;
        if (this.ipv6BlackList == null) {
            this.ipv6BlackList = new ConcurrentHashMap();
        }
    }

    public List<IConnStrategy> queryByHost(String str) {
        StrategyCollection strategyCollection;
        if (TextUtils.isEmpty(str) || !Utils.checkHostValidAndNotIp(str)) {
            return Collections.EMPTY_LIST;
        }
        checkInitHost();
        synchronized (this.hostStrategyMap) {
            strategyCollection = (StrategyCollection) this.hostStrategyMap.get(str);
            if (strategyCollection == null) {
                strategyCollection = new StrategyCollection(str);
                this.hostStrategyMap.put(str, strategyCollection);
            }
        }
        if (strategyCollection.ttl == 0 || (strategyCollection.isExpired() && AmdcRuntimeInfo.getAmdcLimitLevel() == 0)) {
            sendAmdcRequest(str);
        }
        return strategyCollection.queryStrategyList();
    }

    public String getCnameByHost(String str) {
        StrategyCollection strategyCollection;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this.hostStrategyMap) {
            strategyCollection = (StrategyCollection) this.hostStrategyMap.get(str);
        }
        if (strategyCollection != null && strategyCollection.isExpired() && AmdcRuntimeInfo.getAmdcLimitLevel() == 0) {
            sendAmdcRequest(str);
        }
        if (strategyCollection != null) {
            return strategyCollection.cname;
        }
        return null;
    }

    public void update(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
        ALog.i(TAG, "update strategyTable with httpDns response", this.uniqueId, new Object[0]);
        try {
            this.clientIp = httpDnsResponse.clientIp;
            this.configVersion = httpDnsResponse.configVersion;
            StrategyResultParser.DnsInfo[] dnsInfoArr = httpDnsResponse.dnsInfo;
            if (dnsInfoArr != null) {
                synchronized (this.hostStrategyMap) {
                    for (StrategyResultParser.DnsInfo dnsInfo : dnsInfoArr) {
                        if (dnsInfo != null) {
                            if (dnsInfo.host != null) {
                                if (dnsInfo.clear) {
                                    this.hostStrategyMap.remove(dnsInfo.host);
                                } else {
                                    StrategyCollection strategyCollection = (StrategyCollection) this.hostStrategyMap.get(dnsInfo.host);
                                    if (strategyCollection == null) {
                                        strategyCollection = new StrategyCollection(dnsInfo.host);
                                        this.hostStrategyMap.put(dnsInfo.host, strategyCollection);
                                    }
                                    strategyCollection.update(dnsInfo);
                                }
                            }
                        }
                    }
                }
                this.isChanged = true;
                if (ALog.isPrintLog(1)) {
                    StringBuilder sb = new StringBuilder("uniqueId : ");
                    sb.append(this.uniqueId);
                    sb.append("\n-------------------------domains:------------------------------------");
                    ALog.d(TAG, sb.toString(), (String) null, new Object[0]);
                    synchronized (this.hostStrategyMap) {
                        for (Map.Entry entry : this.hostStrategyMap.entrySet()) {
                            sb.setLength(0);
                            sb.append((String) entry.getKey());
                            sb.append(" = ");
                            sb.append(((StrategyCollection) entry.getValue()).toString());
                            ALog.d(TAG, sb.toString(), (String) null, new Object[0]);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            ALog.e(TAG, "fail to update strategyTable", this.uniqueId, th, new Object[0]);
        }
    }

    private void sendAmdcRequest(String str) {
        TreeSet treeSet = new TreeSet();
        treeSet.add(str);
        sendAmdcRequest((Set<String>) treeSet);
    }

    /* access modifiers changed from: protected */
    public void sendAmdcRequest(String str, boolean z) {
        StrategyCollection strategyCollection;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.hostStrategyMap) {
                strategyCollection = (StrategyCollection) this.hostStrategyMap.get(str);
                if (strategyCollection == null) {
                    strategyCollection = new StrategyCollection(str);
                    this.hostStrategyMap.put(str, strategyCollection);
                }
            }
            if (z || strategyCollection.ttl == 0 || (strategyCollection.isExpired() && AmdcRuntimeInfo.getAmdcLimitLevel() == 0)) {
                sendAmdcRequest(str);
            }
        }
    }

    private void sendAmdcRequest(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            if ((!GlobalAppRuntimeInfo.isAppBackground() || AppLifecycle.lastEnterBackgroundTime <= 0) && NetworkStatusHelper.isConnected()) {
                int amdcLimitLevel = AmdcRuntimeInfo.getAmdcLimitLevel();
                if (amdcLimitLevel != 3) {
                    long currentTimeMillis = System.currentTimeMillis();
                    synchronized (this.hostStrategyMap) {
                        for (String str : set) {
                            StrategyCollection strategyCollection = (StrategyCollection) this.hostStrategyMap.get(str);
                            if (strategyCollection != null) {
                                strategyCollection.ttl = 30000 + currentTimeMillis;
                            }
                        }
                    }
                    if (amdcLimitLevel == 0) {
                        fillUpdateHosts(set);
                    }
                    HttpDispatcher.getInstance().sendAmdcRequest(set, this.configVersion);
                    return;
                }
                return;
            }
            ALog.i(TAG, "app in background or no network", this.uniqueId, new Object[0]);
        }
    }

    private void fillUpdateHosts(Set<String> set) {
        TreeSet treeSet = new TreeSet(comparator);
        synchronized (this.hostStrategyMap) {
            treeSet.addAll(this.hostStrategyMap.values());
        }
        long currentTimeMillis = System.currentTimeMillis();
        Iterator it = treeSet.iterator();
        while (it.hasNext()) {
            StrategyCollection strategyCollection = (StrategyCollection) it.next();
            if (strategyCollection.isExpired() && set.size() < 40) {
                strategyCollection.ttl = 30000 + currentTimeMillis;
                set.add(strategyCollection.host);
            } else {
                return;
            }
        }
    }

    private void checkInitHost() {
        try {
            if (HttpDispatcher.getInstance().isInitHostsChanged(this.uniqueId)) {
                TreeSet treeSet = null;
                synchronized (this.hostStrategyMap) {
                    for (String next : HttpDispatcher.getInstance().getInitHosts()) {
                        if (!this.hostStrategyMap.containsKey(next)) {
                            this.hostStrategyMap.put(next, new StrategyCollection(next));
                            if (treeSet == null) {
                                treeSet = new TreeSet();
                            }
                            treeSet.add(next);
                        }
                    }
                }
                if (treeSet != null) {
                    sendAmdcRequest((Set<String>) treeSet);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "checkInitHost failed", this.uniqueId, e, new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyConnEvent(String str, IConnStrategy iConnStrategy, ConnEvent connEvent) {
        StrategyCollection strategyCollection;
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "[notifyConnEvent]", (String) null, HttpConstant.HOST, str, "IConnStrategy", iConnStrategy, "ConnEvent", connEvent);
        }
        String str2 = iConnStrategy.getProtocol().protocol;
        if (ConnType.QUIC.equals(str2) || ConnType.QUIC_PLAIN.equals(str2)) {
            this.enableQuic = connEvent.isSuccess;
            ALog.e(TAG, "enbale quic", (String) null, "uniqueId", this.uniqueId, "enable", Boolean.valueOf(connEvent.isSuccess));
        }
        if (!connEvent.isSuccess && Utils.isIPV6Address(iConnStrategy.getIp())) {
            this.ipv6BlackList.put(str, Long.valueOf(System.currentTimeMillis()));
            ALog.e(TAG, "disable ipv6", (String) null, "uniqueId", this.uniqueId, "host", str);
        }
        synchronized (this.hostStrategyMap) {
            strategyCollection = (StrategyCollection) this.hostStrategyMap.get(str);
        }
        if (strategyCollection != null) {
            strategyCollection.notifyConnEvent(iConnStrategy, connEvent);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isHostInIpv6BlackList(String str, long j) {
        Long l = this.ipv6BlackList.get(str);
        if (l == null) {
            return false;
        }
        if (l.longValue() + j >= System.currentTimeMillis()) {
            return true;
        }
        this.ipv6BlackList.remove(str);
        return false;
    }
}
