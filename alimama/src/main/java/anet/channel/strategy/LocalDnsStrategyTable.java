package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import com.taobao.accs.common.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class LocalDnsStrategyTable {
    private static final String TAG = "awcn.LocalDnsStrategyTable";
    final ConcurrentHashMap<String, List<IPConnStrategy>> localStrategyMap = new ConcurrentHashMap<>();
    final HashMap<String, Object> lockObjMap = new HashMap<>();

    LocalDnsStrategyTable() {
    }

    /* access modifiers changed from: package-private */
    public List queryByHost(String str) {
        Object obj;
        if (TextUtils.isEmpty(str) || !Utils.checkHostValidAndNotIp(str) || DispatchConstants.getAmdcServerDomain().equalsIgnoreCase(str)) {
            return Collections.EMPTY_LIST;
        }
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "try resolve ip with local dns", (String) null, "host", str);
        }
        List list = Collections.EMPTY_LIST;
        if (!this.localStrategyMap.containsKey(str)) {
            synchronized (this.lockObjMap) {
                if (!this.lockObjMap.containsKey(str)) {
                    obj = new Object();
                    this.lockObjMap.put(str, obj);
                    startLocalDnsLookup(str, obj);
                } else {
                    obj = this.lockObjMap.get(str);
                }
            }
            if (obj != null) {
                try {
                    synchronized (obj) {
                        obj.wait(500);
                    }
                } catch (InterruptedException unused) {
                }
            }
        }
        List list2 = this.localStrategyMap.get(str);
        if (!(list2 == null || list2 == Collections.EMPTY_LIST)) {
            list = new ArrayList(list2);
        }
        ALog.i(TAG, "get local strategy", (String) null, "strategyList", list2);
        return list;
    }

    /* access modifiers changed from: package-private */
    public void setProtocolForHost(String str, ConnProtocol connProtocol) {
        List<IPConnStrategy> list = this.localStrategyMap.get(str);
        if (list != null && !list.isEmpty()) {
            for (IPConnStrategy protocol : list) {
                if (protocol.getProtocol().equals(connProtocol)) {
                    return;
                }
            }
            list.add(IPConnStrategy.create(((IPConnStrategy) list.get(0)).getIp(), !(connProtocol.protocol.equalsIgnoreCase("https") || !TextUtils.isEmpty(connProtocol.publicKey)) ? 80 : Constants.PORT, connProtocol, 0, 0, 1, 45000));
            ALog.i(TAG, "setProtocolForHost", (String) null, "strategyList", list);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyConnEvent(String str, IConnStrategy iConnStrategy, ConnEvent connEvent) {
        List list;
        if (!connEvent.isSuccess && !TextUtils.isEmpty(str) && !connEvent.isAccs && (list = this.localStrategyMap.get(str)) != null && list != Collections.EMPTY_LIST) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (it.next() == iConnStrategy) {
                    it.remove();
                }
            }
            if (list.isEmpty()) {
                this.localStrategyMap.put(str, Collections.EMPTY_LIST);
            }
        }
    }

    private void startLocalDnsLookup(final String str, final Object obj) {
        AmdcThreadPoolExecutor.submitTask(new Runnable() {
            /* JADX WARNING: Code restructure failed: missing block: B:36:0x00bd, code lost:
                r0 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c3, code lost:
                if (anet.channel.util.ALog.isPrintLog(1) != false) goto L_0x00c5;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c5, code lost:
                anet.channel.util.ALog.d(anet.channel.strategy.LocalDnsStrategyTable.TAG, "resolve ip by local dns failed", (java.lang.String) null, "host", r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:41:0x00d6, code lost:
                r13.this$0.localStrategyMap.put(r2, java.util.Collections.EMPTY_LIST);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:43:0x00e5, code lost:
                monitor-enter(r13.this$0.lockObjMap);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
                r13.this$0.lockObjMap.remove(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f2, code lost:
                monitor-enter(r3);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
                r3.notifyAll();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:60:0x0104, code lost:
                monitor-enter(r13.this$0.lockObjMap);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
                r13.this$0.lockObjMap.remove(r2);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:65:0x0111, code lost:
                monitor-enter(r3);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
                r3.notifyAll();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:69:0x0118, code lost:
                throw r0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
                return;
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00bf */
            /* JADX WARNING: Removed duplicated region for block: B:12:0x0037 A[Catch:{ Exception -> 0x00bf }] */
            /* JADX WARNING: Removed duplicated region for block: B:13:0x003c A[Catch:{ Exception -> 0x00bf }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r13 = this;
                    r0 = 2
                    r1 = 0
                    r2 = 0
                    r3 = 1
                    java.lang.String r4 = r2     // Catch:{ Exception -> 0x00bf }
                    java.net.InetAddress r4 = java.net.InetAddress.getByName(r4)     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r4 = r4.getHostAddress()     // Catch:{ Exception -> 0x00bf }
                    java.util.LinkedList r12 = new java.util.LinkedList     // Catch:{ Exception -> 0x00bf }
                    r12.<init>()     // Catch:{ Exception -> 0x00bf }
                    anet.channel.strategy.StrategyTemplate r5 = anet.channel.strategy.StrategyTemplate.getInstance()     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r6 = r2     // Catch:{ Exception -> 0x00bf }
                    anet.channel.strategy.ConnProtocol r7 = r5.getConnProtocol(r6)     // Catch:{ Exception -> 0x00bf }
                    if (r7 == 0) goto L_0x004e
                    java.lang.String r5 = r7.protocol     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r6 = "https"
                    boolean r5 = r5.equalsIgnoreCase(r6)     // Catch:{ Exception -> 0x00bf }
                    if (r5 != 0) goto L_0x0034
                    java.lang.String r5 = r7.publicKey     // Catch:{ Exception -> 0x00bf }
                    boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x00bf }
                    if (r5 != 0) goto L_0x0032
                    goto L_0x0034
                L_0x0032:
                    r5 = 0
                    goto L_0x0035
                L_0x0034:
                    r5 = 1
                L_0x0035:
                    if (r5 != 0) goto L_0x003c
                    r5 = 80
                    r6 = 80
                    goto L_0x0040
                L_0x003c:
                    r5 = 443(0x1bb, float:6.21E-43)
                    r6 = 443(0x1bb, float:6.21E-43)
                L_0x0040:
                    r8 = 0
                    r9 = 0
                    r10 = 1
                    r11 = 45000(0xafc8, float:6.3058E-41)
                    r5 = r4
                    anet.channel.strategy.IPConnStrategy r5 = anet.channel.strategy.IPConnStrategy.create(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x00bf }
                    r12.add(r5)     // Catch:{ Exception -> 0x00bf }
                L_0x004e:
                    r6 = 80
                    anet.channel.strategy.ConnProtocol r7 = anet.channel.strategy.ConnProtocol.HTTP     // Catch:{ Exception -> 0x00bf }
                    r8 = 0
                    r9 = 0
                    r10 = 0
                    r11 = 0
                    r5 = r4
                    anet.channel.strategy.IPConnStrategy r5 = anet.channel.strategy.IPConnStrategy.create(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x00bf }
                    r12.add(r5)     // Catch:{ Exception -> 0x00bf }
                    r6 = 443(0x1bb, float:6.21E-43)
                    anet.channel.strategy.ConnProtocol r7 = anet.channel.strategy.ConnProtocol.HTTPS     // Catch:{ Exception -> 0x00bf }
                    r8 = 0
                    r9 = 0
                    r10 = 0
                    r11 = 0
                    r5 = r4
                    anet.channel.strategy.IPConnStrategy r5 = anet.channel.strategy.IPConnStrategy.create(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x00bf }
                    r12.add(r5)     // Catch:{ Exception -> 0x00bf }
                    anet.channel.strategy.LocalDnsStrategyTable r5 = anet.channel.strategy.LocalDnsStrategyTable.this     // Catch:{ Exception -> 0x00bf }
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<anet.channel.strategy.IPConnStrategy>> r5 = r5.localStrategyMap     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r6 = r2     // Catch:{ Exception -> 0x00bf }
                    r5.put(r6, r12)     // Catch:{ Exception -> 0x00bf }
                    boolean r5 = anet.channel.util.ALog.isPrintLog(r3)     // Catch:{ Exception -> 0x00bf }
                    if (r5 == 0) goto L_0x009e
                    java.lang.String r5 = "awcn.LocalDnsStrategyTable"
                    java.lang.String r6 = "resolve ip by local dns"
                    r7 = 6
                    java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r8 = "host"
                    r7[r2] = r8     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r8 = r2     // Catch:{ Exception -> 0x00bf }
                    r7[r3] = r8     // Catch:{ Exception -> 0x00bf }
                    java.lang.String r8 = "ip"
                    r7[r0] = r8     // Catch:{ Exception -> 0x00bf }
                    r8 = 3
                    r7[r8] = r4     // Catch:{ Exception -> 0x00bf }
                    r4 = 4
                    java.lang.String r8 = "list"
                    r7[r4] = r8     // Catch:{ Exception -> 0x00bf }
                    r4 = 5
                    r7[r4] = r12     // Catch:{ Exception -> 0x00bf }
                    anet.channel.util.ALog.d(r5, r6, r1, r7)     // Catch:{ Exception -> 0x00bf }
                L_0x009e:
                    anet.channel.strategy.LocalDnsStrategyTable r0 = anet.channel.strategy.LocalDnsStrategyTable.this
                    java.util.HashMap<java.lang.String, java.lang.Object> r0 = r0.lockObjMap
                    monitor-enter(r0)
                    anet.channel.strategy.LocalDnsStrategyTable r1 = anet.channel.strategy.LocalDnsStrategyTable.this     // Catch:{ all -> 0x00ba }
                    java.util.HashMap<java.lang.String, java.lang.Object> r1 = r1.lockObjMap     // Catch:{ all -> 0x00ba }
                    java.lang.String r2 = r2     // Catch:{ all -> 0x00ba }
                    r1.remove(r2)     // Catch:{ all -> 0x00ba }
                    monitor-exit(r0)     // Catch:{ all -> 0x00ba }
                    java.lang.Object r1 = r3
                    monitor-enter(r1)
                    java.lang.Object r0 = r3     // Catch:{ all -> 0x00b7 }
                    r0.notifyAll()     // Catch:{ all -> 0x00b7 }
                    monitor-exit(r1)     // Catch:{ all -> 0x00b7 }
                    goto L_0x00f9
                L_0x00b7:
                    r0 = move-exception
                    monitor-exit(r1)     // Catch:{ all -> 0x00b7 }
                    throw r0
                L_0x00ba:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x00ba }
                    throw r1
                L_0x00bd:
                    r0 = move-exception
                    goto L_0x0100
                L_0x00bf:
                    boolean r4 = anet.channel.util.ALog.isPrintLog(r3)     // Catch:{ all -> 0x00bd }
                    if (r4 == 0) goto L_0x00d6
                    java.lang.String r4 = "awcn.LocalDnsStrategyTable"
                    java.lang.String r5 = "resolve ip by local dns failed"
                    java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x00bd }
                    java.lang.String r6 = "host"
                    r0[r2] = r6     // Catch:{ all -> 0x00bd }
                    java.lang.String r2 = r2     // Catch:{ all -> 0x00bd }
                    r0[r3] = r2     // Catch:{ all -> 0x00bd }
                    anet.channel.util.ALog.d(r4, r5, r1, r0)     // Catch:{ all -> 0x00bd }
                L_0x00d6:
                    anet.channel.strategy.LocalDnsStrategyTable r0 = anet.channel.strategy.LocalDnsStrategyTable.this     // Catch:{ all -> 0x00bd }
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.List<anet.channel.strategy.IPConnStrategy>> r0 = r0.localStrategyMap     // Catch:{ all -> 0x00bd }
                    java.lang.String r1 = r2     // Catch:{ all -> 0x00bd }
                    java.util.List r2 = java.util.Collections.EMPTY_LIST     // Catch:{ all -> 0x00bd }
                    r0.put(r1, r2)     // Catch:{ all -> 0x00bd }
                    anet.channel.strategy.LocalDnsStrategyTable r0 = anet.channel.strategy.LocalDnsStrategyTable.this
                    java.util.HashMap<java.lang.String, java.lang.Object> r0 = r0.lockObjMap
                    monitor-enter(r0)
                    anet.channel.strategy.LocalDnsStrategyTable r1 = anet.channel.strategy.LocalDnsStrategyTable.this     // Catch:{ all -> 0x00fd }
                    java.util.HashMap<java.lang.String, java.lang.Object> r1 = r1.lockObjMap     // Catch:{ all -> 0x00fd }
                    java.lang.String r2 = r2     // Catch:{ all -> 0x00fd }
                    r1.remove(r2)     // Catch:{ all -> 0x00fd }
                    monitor-exit(r0)     // Catch:{ all -> 0x00fd }
                    java.lang.Object r1 = r3
                    monitor-enter(r1)
                    java.lang.Object r0 = r3     // Catch:{ all -> 0x00fa }
                    r0.notifyAll()     // Catch:{ all -> 0x00fa }
                    monitor-exit(r1)     // Catch:{ all -> 0x00fa }
                L_0x00f9:
                    return
                L_0x00fa:
                    r0 = move-exception
                    monitor-exit(r1)     // Catch:{ all -> 0x00fa }
                    throw r0
                L_0x00fd:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x00fd }
                    throw r1
                L_0x0100:
                    anet.channel.strategy.LocalDnsStrategyTable r1 = anet.channel.strategy.LocalDnsStrategyTable.this
                    java.util.HashMap<java.lang.String, java.lang.Object> r1 = r1.lockObjMap
                    monitor-enter(r1)
                    anet.channel.strategy.LocalDnsStrategyTable r2 = anet.channel.strategy.LocalDnsStrategyTable.this     // Catch:{ all -> 0x011c }
                    java.util.HashMap<java.lang.String, java.lang.Object> r2 = r2.lockObjMap     // Catch:{ all -> 0x011c }
                    java.lang.String r3 = r2     // Catch:{ all -> 0x011c }
                    r2.remove(r3)     // Catch:{ all -> 0x011c }
                    monitor-exit(r1)     // Catch:{ all -> 0x011c }
                    java.lang.Object r2 = r3
                    monitor-enter(r2)
                    java.lang.Object r1 = r3     // Catch:{ all -> 0x0119 }
                    r1.notifyAll()     // Catch:{ all -> 0x0119 }
                    monitor-exit(r2)     // Catch:{ all -> 0x0119 }
                    throw r0
                L_0x0119:
                    r0 = move-exception
                    monitor-exit(r2)     // Catch:{ all -> 0x0119 }
                    throw r0
                L_0x011c:
                    r0 = move-exception
                    monitor-exit(r1)     // Catch:{ all -> 0x011c }
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.LocalDnsStrategyTable.AnonymousClass1.run():void");
            }
        });
    }
}
