package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.statist.StrategyStatObject;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class StrategyInfoHolder implements NetworkStatusHelper.INetworkStatusChangeListener {
    static final String CONFIG_FILE = "StrategyConfig";
    static final int MAX_TABLE_NUM_IN_MEM = 3;
    private static final String TAG = "awcn.StrategyInfoHolder";
    private final Set<String> loadingFiles = new HashSet();
    final LocalDnsStrategyTable localDnsStrategyTable = new LocalDnsStrategyTable();
    volatile StrategyConfig strategyConfig = null;
    Map<String, StrategyTable> strategyTableMap = new LruStrategyMap();
    private volatile String uniqueId = "";
    private final StrategyTable unknownStrategyTable = new StrategyTable("Unknown");

    public static StrategyInfoHolder newInstance() {
        return new StrategyInfoHolder();
    }

    private StrategyInfoHolder() {
        try {
            init();
            restore();
        } catch (Throwable th) {
            checkInit();
            throw th;
        }
        checkInit();
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        NetworkStatusHelper.removeStatusChangeListener(this);
    }

    private void init() {
        NetworkStatusHelper.addStatusChangeListener(this);
        this.uniqueId = getUniqueId(NetworkStatusHelper.getStatus());
    }

    private void checkInit() {
        for (Map.Entry<String, StrategyTable> value : this.strategyTableMap.entrySet()) {
            ((StrategyTable) value.getValue()).checkInit();
        }
        synchronized (this) {
            if (this.strategyConfig == null) {
                StrategyConfig strategyConfig2 = new StrategyConfig();
                strategyConfig2.checkInit();
                strategyConfig2.setHolder(this);
                this.strategyConfig = strategyConfig2;
            }
        }
    }

    private void restore() {
        ALog.i(TAG, "restore", (String) null, new Object[0]);
        final String str = this.uniqueId;
        if (!AwcnConfig.isAsyncLoadStrategyEnable()) {
            if (!TextUtils.isEmpty(str)) {
                loadFile(str, true);
            }
            this.strategyConfig = (StrategyConfig) StrategySerializeHelper.restore(CONFIG_FILE, (StrategyStatObject) null);
            if (this.strategyConfig != null) {
                this.strategyConfig.checkInit();
                this.strategyConfig.setHolder(this);
            }
        }
        AmdcThreadPoolExecutor.submitTask(new Runnable() {
            public void run() {
                try {
                    ALog.i(StrategyInfoHolder.TAG, "start loading strategy files", (String) null, new Object[0]);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (AwcnConfig.isAsyncLoadStrategyEnable()) {
                        ALog.i(StrategyInfoHolder.TAG, "load strategy async", (String) null, new Object[0]);
                        if (!TextUtils.isEmpty(str)) {
                            StrategyInfoHolder.this.loadFile(str, true);
                        }
                        StrategyConfig strategyConfig = (StrategyConfig) StrategySerializeHelper.restore(StrategyInfoHolder.CONFIG_FILE, (StrategyStatObject) null);
                        if (strategyConfig != null) {
                            strategyConfig.checkInit();
                            strategyConfig.setHolder(StrategyInfoHolder.this);
                            synchronized (StrategyInfoHolder.this) {
                                StrategyInfoHolder.this.strategyConfig = strategyConfig;
                            }
                        }
                    }
                    File[] sortedFiles = StrategySerializeHelper.getSortedFiles();
                    if (sortedFiles != null) {
                        int i = 0;
                        for (int i2 = 0; i2 < sortedFiles.length && i < 2; i2++) {
                            File file = sortedFiles[i2];
                            if (!file.isDirectory()) {
                                String name = file.getName();
                                if (!name.equals(str) && !name.startsWith(StrategyInfoHolder.CONFIG_FILE)) {
                                    StrategyInfoHolder.this.loadFile(name, false);
                                    i++;
                                }
                            }
                        }
                        ALog.i(StrategyInfoHolder.TAG, "end loading strategy files", (String) null, "total cost", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    }
                } catch (Exception unused) {
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        r2 = (anet.channel.strategy.StrategyTable) anet.channel.strategy.StrategySerializeHelper.restore(r7, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
        if (r2 == null) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
        r2.checkInit();
        r3 = r6.strategyTableMap;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0029, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r6.strategyTableMap.put(r2.uniqueId, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0031, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0036, code lost:
        r3 = r6.loadingFiles;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0038, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r6.loadingFiles.remove(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003e, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x003f, code lost:
        if (r8 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0041, code lost:
        if (r2 == null) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0043, code lost:
        r1 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0044, code lost:
        r0.isSucceed = r1;
        anet.channel.appmonitor.AppMonitor.getInstance().commitStat(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0011, code lost:
        r0 = null;
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        if (r8 == false) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        r0 = new anet.channel.statist.StrategyStatObject(0);
        r0.readStrategyFileId = r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadFile(java.lang.String r7, boolean r8) {
        /*
            r6 = this;
            java.util.Set<java.lang.String> r0 = r6.loadingFiles
            monitor-enter(r0)
            java.util.Set<java.lang.String> r1 = r6.loadingFiles     // Catch:{ all -> 0x0053 }
            boolean r1 = r1.contains(r7)     // Catch:{ all -> 0x0053 }
            if (r1 != 0) goto L_0x0051
            java.util.Set<java.lang.String> r1 = r6.loadingFiles     // Catch:{ all -> 0x0053 }
            r1.add(r7)     // Catch:{ all -> 0x0053 }
            monitor-exit(r0)     // Catch:{ all -> 0x0053 }
            r0 = 0
            r1 = 0
            if (r8 == 0) goto L_0x001c
            anet.channel.statist.StrategyStatObject r0 = new anet.channel.statist.StrategyStatObject
            r0.<init>(r1)
            r0.readStrategyFileId = r7
        L_0x001c:
            java.lang.Object r2 = anet.channel.strategy.StrategySerializeHelper.restore(r7, r0)
            anet.channel.strategy.StrategyTable r2 = (anet.channel.strategy.StrategyTable) r2
            if (r2 == 0) goto L_0x0036
            r2.checkInit()
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyTable> r3 = r6.strategyTableMap
            monitor-enter(r3)
            java.util.Map<java.lang.String, anet.channel.strategy.StrategyTable> r4 = r6.strategyTableMap     // Catch:{ all -> 0x0033 }
            java.lang.String r5 = r2.uniqueId     // Catch:{ all -> 0x0033 }
            r4.put(r5, r2)     // Catch:{ all -> 0x0033 }
            monitor-exit(r3)     // Catch:{ all -> 0x0033 }
            goto L_0x0036
        L_0x0033:
            r7 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0033 }
            throw r7
        L_0x0036:
            java.util.Set<java.lang.String> r3 = r6.loadingFiles
            monitor-enter(r3)
            java.util.Set<java.lang.String> r4 = r6.loadingFiles     // Catch:{ all -> 0x004e }
            r4.remove(r7)     // Catch:{ all -> 0x004e }
            monitor-exit(r3)     // Catch:{ all -> 0x004e }
            if (r8 == 0) goto L_0x004d
            if (r2 == 0) goto L_0x0044
            r1 = 1
        L_0x0044:
            r0.isSucceed = r1
            anet.channel.appmonitor.IAppMonitor r7 = anet.channel.appmonitor.AppMonitor.getInstance()
            r7.commitStat(r0)
        L_0x004d:
            return
        L_0x004e:
            r7 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x004e }
            throw r7
        L_0x0051:
            monitor-exit(r0)     // Catch:{ all -> 0x0053 }
            return
        L_0x0053:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0053 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategyInfoHolder.loadFile(java.lang.String, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public void saveData() {
        synchronized (this) {
            for (StrategyTable next : this.strategyTableMap.values()) {
                if (next.isChanged) {
                    StrategyStatObject strategyStatObject = new StrategyStatObject(1);
                    strategyStatObject.writeStrategyFileId = next.uniqueId;
                    StrategySerializeHelper.persist(next, next.uniqueId, strategyStatObject);
                    next.isChanged = false;
                }
            }
            StrategySerializeHelper.persist(this.strategyConfig, CONFIG_FILE, (StrategyStatObject) null);
        }
    }

    /* access modifiers changed from: package-private */
    public StrategyTable getCurrStrategyTable() {
        StrategyTable strategyTable = this.unknownStrategyTable;
        String str = this.uniqueId;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.strategyTableMap) {
                strategyTable = this.strategyTableMap.get(str);
                if (strategyTable == null) {
                    strategyTable = new StrategyTable(str);
                    this.strategyTableMap.put(str, strategyTable);
                }
            }
        }
        return strategyTable;
    }

    private String getUniqueId(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (networkStatus.isWifi()) {
            String md5ToHex = StringUtils.md5ToHex(NetworkStatusHelper.getWifiBSSID());
            if (TextUtils.isEmpty(md5ToHex)) {
                md5ToHex = "";
            }
            return "WIFI$" + md5ToHex;
        } else if (!networkStatus.isMobile()) {
            return "";
        } else {
            return networkStatus.getType() + "$" + NetworkStatusHelper.getApn();
        }
    }

    /* access modifiers changed from: package-private */
    public void update(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
        if (httpDnsResponse.fcLevel != 0) {
            AmdcRuntimeInfo.updateAmdcLimit(httpDnsResponse.fcLevel, httpDnsResponse.fcTime);
        }
        getCurrStrategyTable().update(httpDnsResponse);
        this.strategyConfig.update(httpDnsResponse);
    }

    public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
        this.uniqueId = getUniqueId(networkStatus);
        final String str = this.uniqueId;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.strategyTableMap) {
                if (!this.strategyTableMap.containsKey(str)) {
                    AmdcThreadPoolExecutor.submitTask(new Runnable() {
                        public void run() {
                            StrategyInfoHolder.this.loadFile(str, true);
                        }
                    });
                }
            }
        }
    }

    private static class LruStrategyMap extends SerialLruCache<String, StrategyTable> {
        private static final long serialVersionUID = 1866478394612290927L;

        public LruStrategyMap() {
            super(3);
        }

        /* access modifiers changed from: protected */
        public boolean entryRemoved(final Map.Entry<String, StrategyTable> entry) {
            AmdcThreadPoolExecutor.submitTask(new Runnable() {
                public void run() {
                    StrategyTable strategyTable = (StrategyTable) entry.getValue();
                    if (strategyTable.isChanged) {
                        StrategyStatObject strategyStatObject = new StrategyStatObject(1);
                        strategyStatObject.writeStrategyFileId = strategyTable.uniqueId;
                        StrategySerializeHelper.persist((Serializable) entry.getValue(), strategyTable.uniqueId, strategyStatObject);
                        strategyTable.isChanged = false;
                    }
                }
            });
            return true;
        }
    }
}
