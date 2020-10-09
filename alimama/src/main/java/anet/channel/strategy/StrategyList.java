package anet.channel.strategy;

import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import com.taobao.weex.common.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

class StrategyList implements Serializable {
    private static final String TAG = "awcn.StrategyList";
    private static final long serialVersionUID = -258058881561327174L;
    private boolean containsStaticIp = false;
    private transient Comparator<IPConnStrategy> defaultComparator = null;
    /* access modifiers changed from: private */
    public Map<Integer, ConnHistoryItem> historyItemMap = new SerialLruCache(40);
    private List<IPConnStrategy> ipStrategyList = new ArrayList();

    private interface Predicate<T> {
        boolean apply(T t);
    }

    public StrategyList() {
    }

    StrategyList(List<IPConnStrategy> list) {
        this.ipStrategyList = list;
    }

    public void checkInit() {
        if (this.ipStrategyList == null) {
            this.ipStrategyList = new ArrayList();
        }
        if (this.historyItemMap == null) {
            this.historyItemMap = new SerialLruCache(40);
        }
        Iterator<Map.Entry<Integer, ConnHistoryItem>> it = this.historyItemMap.entrySet().iterator();
        while (it.hasNext()) {
            if (((ConnHistoryItem) it.next().getValue()).isExpire()) {
                it.remove();
            }
        }
        for (IPConnStrategy next : this.ipStrategyList) {
            if (!this.historyItemMap.containsKey(Integer.valueOf(next.getUniqueId()))) {
                this.historyItemMap.put(Integer.valueOf(next.getUniqueId()), new ConnHistoryItem());
            }
        }
        Collections.sort(this.ipStrategyList, getDefaultComparator());
    }

    public String toString() {
        return this.ipStrategyList.toString();
    }

    public List<IConnStrategy> getStrategyList() {
        if (this.ipStrategyList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        LinkedList linkedList = null;
        for (IPConnStrategy next : this.ipStrategyList) {
            ConnHistoryItem connHistoryItem = this.historyItemMap.get(Integer.valueOf(next.getUniqueId()));
            if (connHistoryItem == null || !connHistoryItem.shouldBan()) {
                if (linkedList == null) {
                    linkedList = new LinkedList();
                }
                linkedList.add(next);
            } else {
                ALog.i(TAG, "strategy ban!", (String) null, Constants.Name.STRATEGY, next);
            }
        }
        return linkedList == null ? Collections.EMPTY_LIST : linkedList;
    }

    public void update(StrategyResultParser.DnsInfo dnsInfo) {
        for (IPConnStrategy iPConnStrategy : this.ipStrategyList) {
            iPConnStrategy.isToRemove = true;
        }
        for (int i = 0; i < dnsInfo.aisleses.length; i++) {
            for (String handleUpdate : dnsInfo.ips) {
                handleUpdate(handleUpdate, 1, dnsInfo.aisleses[i]);
            }
            if (dnsInfo.sips != null) {
                this.containsStaticIp = true;
                for (String handleUpdate2 : dnsInfo.sips) {
                    handleUpdate(handleUpdate2, 0, dnsInfo.aisleses[i]);
                }
            } else {
                this.containsStaticIp = false;
            }
        }
        if (dnsInfo.strategies != null) {
            for (StrategyResultParser.Strategy strategy : dnsInfo.strategies) {
                handleUpdate(strategy.ip, Utils.checkHostValidAndNotIp(strategy.ip) ? -1 : 1, strategy.aisles);
            }
        }
        ListIterator<IPConnStrategy> listIterator = this.ipStrategyList.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.next().isToRemove) {
                listIterator.remove();
            }
        }
        Collections.sort(this.ipStrategyList, getDefaultComparator());
    }

    private void handleUpdate(final String str, int i, final StrategyResultParser.Aisles aisles) {
        final ConnProtocol valueOf = ConnProtocol.valueOf(aisles);
        int find = find(this.ipStrategyList, new Predicate<IPConnStrategy>() {
            public boolean apply(IPConnStrategy iPConnStrategy) {
                return iPConnStrategy.getPort() == aisles.port && iPConnStrategy.getIp().equals(str) && iPConnStrategy.protocol.equals(valueOf);
            }
        });
        if (find != -1) {
            IPConnStrategy iPConnStrategy = this.ipStrategyList.get(find);
            iPConnStrategy.cto = aisles.cto;
            iPConnStrategy.rto = aisles.rto;
            iPConnStrategy.heartbeat = aisles.heartbeat;
            iPConnStrategy.ipType = i;
            iPConnStrategy.ipSource = 0;
            iPConnStrategy.isToRemove = false;
            return;
        }
        IPConnStrategy create = IPConnStrategy.create(str, aisles);
        if (create != null) {
            create.ipType = i;
            create.ipSource = 0;
            if (!this.historyItemMap.containsKey(Integer.valueOf(create.getUniqueId()))) {
                this.historyItemMap.put(Integer.valueOf(create.getUniqueId()), new ConnHistoryItem());
            }
            this.ipStrategyList.add(create);
        }
    }

    public boolean shouldRefresh() {
        boolean z = true;
        boolean z2 = true;
        for (IPConnStrategy next : this.ipStrategyList) {
            if (!this.historyItemMap.get(Integer.valueOf(next.getUniqueId())).latestFail()) {
                if (next.ipType == 0) {
                    z = false;
                }
                z2 = false;
            }
        }
        if ((!this.containsStaticIp || !z) && !z2) {
            return false;
        }
        return true;
    }

    public void notifyConnEvent(IConnStrategy iConnStrategy, ConnEvent connEvent) {
        if ((iConnStrategy instanceof IPConnStrategy) && this.ipStrategyList.indexOf(iConnStrategy) != -1) {
            this.historyItemMap.get(Integer.valueOf(((IPConnStrategy) iConnStrategy).getUniqueId())).update(connEvent.isSuccess);
            Collections.sort(this.ipStrategyList, this.defaultComparator);
        }
    }

    private Comparator getDefaultComparator() {
        if (this.defaultComparator == null) {
            this.defaultComparator = new Comparator<IPConnStrategy>() {
                public int compare(IPConnStrategy iPConnStrategy, IPConnStrategy iPConnStrategy2) {
                    int countFail = ((ConnHistoryItem) StrategyList.this.historyItemMap.get(Integer.valueOf(iPConnStrategy.getUniqueId()))).countFail();
                    int countFail2 = ((ConnHistoryItem) StrategyList.this.historyItemMap.get(Integer.valueOf(iPConnStrategy2.getUniqueId()))).countFail();
                    if (countFail != countFail2) {
                        return countFail - countFail2;
                    }
                    if (iPConnStrategy.ipType != iPConnStrategy2.ipType) {
                        return iPConnStrategy.ipType - iPConnStrategy2.ipType;
                    }
                    return iPConnStrategy.protocol.isHttp - iPConnStrategy2.protocol.isHttp;
                }
            };
        }
        return this.defaultComparator;
    }

    private static <T> int find(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null) {
            return -1;
        }
        int i = 0;
        Iterator<T> it = collection.iterator();
        while (it.hasNext() && !predicate.apply(it.next())) {
            i++;
        }
        if (i == collection.size()) {
            return -1;
        }
        return i;
    }
}
