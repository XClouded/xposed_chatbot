package anet.channel.strategy;

import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.PolicyVersionStat;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

class StrategyCollection implements Serializable {
    private static final long MAX_AVAILABLE_PERIOD = 172800000;
    private static final long serialVersionUID = 1454976454894208229L;
    volatile String cname = null;
    String host;
    private transient boolean isFirstUsed = true;
    boolean isFixed = false;
    private transient long lastAmdcRequestSend = 0;
    StrategyList strategyList = null;
    volatile long ttl = 0;
    int version = 0;

    public StrategyCollection() {
    }

    protected StrategyCollection(String str) {
        this.host = str;
        this.isFixed = DispatchConstants.isAmdcServerDomain(str);
    }

    public void checkInit() {
        if (System.currentTimeMillis() - this.ttl > MAX_AVAILABLE_PERIOD) {
            this.strategyList = null;
        } else if (this.strategyList != null) {
            this.strategyList.checkInit();
        }
    }

    public synchronized List<IConnStrategy> queryStrategyList() {
        if (this.strategyList == null) {
            return Collections.EMPTY_LIST;
        }
        if (this.isFirstUsed) {
            this.isFirstUsed = false;
            PolicyVersionStat policyVersionStat = new PolicyVersionStat(this.host, this.version);
            policyVersionStat.reportType = 0;
            AppMonitor.getInstance().commitStat(policyVersionStat);
        }
        return this.strategyList.getStrategyList();
    }

    public synchronized void notifyConnEvent(IConnStrategy iConnStrategy, ConnEvent connEvent) {
        if (this.strategyList != null) {
            this.strategyList.notifyConnEvent(iConnStrategy, connEvent);
            if (!connEvent.isSuccess && this.strategyList.shouldRefresh()) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.lastAmdcRequestSend > 60000) {
                    StrategyCenter.getInstance().forceRefreshStrategy(this.host);
                    this.lastAmdcRequestSend = currentTimeMillis;
                }
            }
        }
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.ttl;
    }

    public synchronized void update(StrategyResultParser.DnsInfo dnsInfo) {
        this.ttl = System.currentTimeMillis() + (((long) dnsInfo.ttl) * 1000);
        if (!dnsInfo.host.equalsIgnoreCase(this.host)) {
            ALog.e("StrategyCollection", "update error!", (String) null, "host", this.host, "dnsInfo.host", dnsInfo.host);
            return;
        }
        if (this.version != dnsInfo.version) {
            this.version = dnsInfo.version;
            PolicyVersionStat policyVersionStat = new PolicyVersionStat(this.host, this.version);
            policyVersionStat.reportType = 1;
            AppMonitor.getInstance().commitStat(policyVersionStat);
        }
        this.cname = dnsInfo.cname;
        if (dnsInfo.ips == null || dnsInfo.ips.length == 0 || dnsInfo.aisleses == null || dnsInfo.aisleses.length == 0) {
            if (dnsInfo.strategies != null) {
                if (dnsInfo.strategies.length == 0) {
                }
            }
            this.strategyList = null;
            return;
        }
        if (this.strategyList == null) {
            this.strategyList = new StrategyList();
        }
        this.strategyList.update(dnsInfo);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("\nStrategyList = ");
        sb.append(this.ttl);
        if (this.strategyList != null) {
            sb.append(this.strategyList.toString());
        } else if (this.cname != null) {
            sb.append(Operators.ARRAY_START);
            sb.append(this.host);
            sb.append("=>");
            sb.append(this.cname);
            sb.append(Operators.ARRAY_END);
        } else {
            sb.append("[]");
        }
        return sb.toString();
    }
}
