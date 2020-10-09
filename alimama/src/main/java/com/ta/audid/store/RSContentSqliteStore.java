package com.ta.audid.store;

import com.ta.audid.Variables;
import com.ta.audid.db.Entity;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;

public class RSContentSqliteStore {
    private static RSContentSqliteStore mInstance;

    private RSContentSqliteStore() {
    }

    public static synchronized RSContentSqliteStore getInstance() {
        RSContentSqliteStore rSContentSqliteStore;
        synchronized (RSContentSqliteStore.class) {
            if (mInstance == null) {
                mInstance = new RSContentSqliteStore();
            }
            rSContentSqliteStore = mInstance;
        }
        return rSContentSqliteStore;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0047, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void insertStringList(java.util.List<java.lang.String> r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r4 == 0) goto L_0x0046
            int r0 = r4.size()     // Catch:{ all -> 0x0043 }
            r1 = 1
            if (r0 >= r1) goto L_0x000b
            goto L_0x0046
        L_0x000b:
            int r0 = r3.count()     // Catch:{ all -> 0x0043 }
            r1 = 200(0xc8, float:2.8E-43)
            if (r0 <= r1) goto L_0x0018
            r0 = 100
            r3.clearOldLogByCount(r0)     // Catch:{ all -> 0x0043 }
        L_0x0018:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x0043 }
            r0.<init>()     // Catch:{ all -> 0x0043 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x0043 }
        L_0x0021:
            boolean r1 = r4.hasNext()     // Catch:{ all -> 0x0043 }
            if (r1 == 0) goto L_0x0036
            java.lang.Object r1 = r4.next()     // Catch:{ all -> 0x0043 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0043 }
            com.ta.audid.store.RSContent r2 = new com.ta.audid.store.RSContent     // Catch:{ all -> 0x0043 }
            r2.<init>(r1)     // Catch:{ all -> 0x0043 }
            r0.add(r2)     // Catch:{ all -> 0x0043 }
            goto L_0x0021
        L_0x0036:
            com.ta.audid.Variables r4 = com.ta.audid.Variables.getInstance()     // Catch:{ all -> 0x0043 }
            com.ta.audid.db.DBMgr r4 = r4.getDbMgr()     // Catch:{ all -> 0x0043 }
            r4.insert((java.util.List<? extends com.ta.audid.db.Entity>) r0)     // Catch:{ all -> 0x0043 }
            monitor-exit(r3)
            return
        L_0x0043:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        L_0x0046:
            monitor-exit(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.store.RSContentSqliteStore.insertStringList(java.util.List):void");
    }

    private int count() {
        return Variables.getInstance().getDbMgr().count(RSContent.class);
    }

    private int clearOldLogByCount(int i) {
        String tablename = Variables.getInstance().getDbMgr().getTablename(RSContent.class);
        return Variables.getInstance().getDbMgr().delete(RSContent.class, " _id in ( select _id from " + tablename + " ORDER BY _id ASC LIMIT " + i + " )", (String[]) null);
    }

    public synchronized int find(String str) {
        return Variables.getInstance().getDbMgr().count(RSContent.class, "content = '" + str + DXBindingXConstant.SINGLE_QUOTE);
    }

    public synchronized void clear() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) RSContent.class);
    }
}
