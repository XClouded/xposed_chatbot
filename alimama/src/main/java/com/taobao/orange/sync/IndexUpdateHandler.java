package com.taobao.orange.sync;

import android.text.TextUtils;
import anet.channel.entity.ConnType;
import com.alibaba.fastjson.JSON;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

public class IndexUpdateHandler {
    private static final long CHECK_INDEX_UPD_INTERVAL = 20000;
    static final String TAG = "IndexUpdateHandler";
    private static boolean disableTaobaoClientIndexCheckUpd = true;
    private static volatile long lastIndexUpdTime;
    static final Set<IndexUpdateInfo> mCurIndexUpdInfo = new HashSet();

    public static void checkIndexUpdate(final String str, final String str2) {
        if (!AndroidUtil.isTaobaoPackage(GlobalOrange.context) || !disableTaobaoClientIndexCheckUpd) {
            synchronized (IndexUpdateHandler.class) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - lastIndexUpdTime <= 20000) {
                    OLog.w(TAG, "checkIndexUpdate too frequently, interval should more than 20s", new Object[0]);
                    return;
                }
                lastIndexUpdTime = currentTimeMillis;
                OLog.i(TAG, "checkIndexUpdate", "appIndexVersion", str, "versionIndexVersion", str2);
                OThreadFactory.execute(new Runnable() {
                    public void run() {
                        IndexUpdateHandler.updateIndex((String) new BaseAuthRequest<String>((String) null, false, OConstant.REQTYPE_INDEX_UPDATE) {
                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public String parseResContent(String str) {
                                return str;
                            }

                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                HashMap hashMap = new HashMap();
                                hashMap.put(OConstant.KEY_CLIENTAPPINDEXVERSION, str);
                                hashMap.put(OConstant.KEY_CLIENTVERSIONINDEXVERSION, str2);
                                return hashMap;
                            }
                        }.syncRequest(), true);
                    }
                });
                return;
            }
        }
        OLog.w(TAG, "checkIndexUpdate skip as in com.taobao.taobao package", new Object[0]);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002f, code lost:
        if (com.taobao.orange.util.OLog.isPrintLog(2) == false) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0031, code lost:
        com.taobao.orange.util.OLog.i(TAG, "updateIndex", r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0045, code lost:
        if ("https".equalsIgnoreCase(r3.protocol) == false) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0047, code lost:
        r4 = "https";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004a, code lost:
        r4 = "http";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        com.taobao.orange.GlobalOrange.schema = r4;
        com.taobao.orange.ConfigCenter.getInstance().updateIndex(r3);
        r4 = mCurIndexUpdInfo;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0057, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        mCurIndexUpdInfo.remove(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x005d, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateIndex(java.lang.String r3, boolean r4) {
        /*
            r0 = 0
            com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo r3 = parseIndexUpdInfo(r3, r4)     // Catch:{ Throwable -> 0x0065 }
            if (r3 == 0) goto L_0x006f
            boolean r4 = r3.checkValid()     // Catch:{ Throwable -> 0x0065 }
            if (r4 == 0) goto L_0x006f
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r4 = mCurIndexUpdInfo     // Catch:{ Throwable -> 0x0065 }
            monitor-enter(r4)     // Catch:{ Throwable -> 0x0065 }
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r1 = mCurIndexUpdInfo     // Catch:{ all -> 0x0062 }
            boolean r1 = r1.add(r3)     // Catch:{ all -> 0x0062 }
            if (r1 != 0) goto L_0x0029
            boolean r3 = com.taobao.orange.util.OLog.isPrintLog(r0)     // Catch:{ all -> 0x0062 }
            if (r3 == 0) goto L_0x0027
            java.lang.String r3 = "IndexUpdateHandler"
            java.lang.String r1 = "updateIndex is ongoing"
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ all -> 0x0062 }
            com.taobao.orange.util.OLog.v(r3, r1, r2)     // Catch:{ all -> 0x0062 }
        L_0x0027:
            monitor-exit(r4)     // Catch:{ all -> 0x0062 }
            return
        L_0x0029:
            monitor-exit(r4)     // Catch:{ all -> 0x0062 }
            r4 = 2
            boolean r4 = com.taobao.orange.util.OLog.isPrintLog(r4)     // Catch:{ Throwable -> 0x0065 }
            if (r4 == 0) goto L_0x003d
            java.lang.String r4 = "IndexUpdateHandler"
            java.lang.String r1 = "updateIndex"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0065 }
            r2[r0] = r3     // Catch:{ Throwable -> 0x0065 }
            com.taobao.orange.util.OLog.i(r4, r1, r2)     // Catch:{ Throwable -> 0x0065 }
        L_0x003d:
            java.lang.String r4 = "https"
            java.lang.String r1 = r3.protocol     // Catch:{ Throwable -> 0x0065 }
            boolean r4 = r4.equalsIgnoreCase(r1)     // Catch:{ Throwable -> 0x0065 }
            if (r4 == 0) goto L_0x004a
            java.lang.String r4 = "https"
            goto L_0x004c
        L_0x004a:
            java.lang.String r4 = "http"
        L_0x004c:
            com.taobao.orange.GlobalOrange.schema = r4     // Catch:{ Throwable -> 0x0065 }
            com.taobao.orange.ConfigCenter r4 = com.taobao.orange.ConfigCenter.getInstance()     // Catch:{ Throwable -> 0x0065 }
            r4.updateIndex(r3)     // Catch:{ Throwable -> 0x0065 }
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r4 = mCurIndexUpdInfo     // Catch:{ Throwable -> 0x0065 }
            monitor-enter(r4)     // Catch:{ Throwable -> 0x0065 }
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r1 = mCurIndexUpdInfo     // Catch:{ all -> 0x005f }
            r1.remove(r3)     // Catch:{ all -> 0x005f }
            monitor-exit(r4)     // Catch:{ all -> 0x005f }
            goto L_0x006f
        L_0x005f:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x005f }
            throw r3     // Catch:{ Throwable -> 0x0065 }
        L_0x0062:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0062 }
            throw r3     // Catch:{ Throwable -> 0x0065 }
        L_0x0065:
            r3 = move-exception
            java.lang.String r4 = "IndexUpdateHandler"
            java.lang.String r1 = "updateIndex"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.orange.util.OLog.e(r4, r1, r3, r0)
        L_0x006f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.sync.IndexUpdateHandler.updateIndex(java.lang.String, boolean):void");
    }

    private static IndexUpdateInfo parseIndexUpdInfo(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (z) {
            return (IndexUpdateInfo) JSON.parseObject(str, IndexUpdateInfo.class);
        }
        String[] split = str.split("&");
        if (split == null) {
            return null;
        }
        IndexUpdateInfo indexUpdateInfo = new IndexUpdateInfo();
        for (String str2 : split) {
            if (str2 != null) {
                String substring = str2.substring(str2.indexOf(SymbolExpUtil.SYMBOL_EQUAL) + 1);
                if (str2.startsWith(ConnType.PK_CDN)) {
                    indexUpdateInfo.cdn = substring.trim();
                } else if (str2.startsWith("md5")) {
                    indexUpdateInfo.md5 = substring.trim();
                } else if (str2.startsWith("resourceId")) {
                    indexUpdateInfo.resourceId = substring.trim();
                } else if (str2.startsWith("protocol")) {
                    indexUpdateInfo.protocol = substring;
                }
            }
        }
        return indexUpdateInfo;
    }

    public static class IndexUpdateInfo implements Serializable {
        static final String SYNC_KEY_CDN = "cdn";
        static final String SYNC_KEY_MD5 = "md5";
        static final String SYNC_KEY_PROTOCOL = "protocol";
        static final String SYNC_KEY_RESOURCEID = "resourceId";
        public String cdn;
        public String md5;
        public String protocol;
        public String resourceId;

        public String toString() {
            return "IndexUpdateInfo{" + "cdn='" + this.cdn + '\'' + ", resourceId='" + this.resourceId + '\'' + ", md5='" + this.md5 + '\'' + ", protocol='" + this.protocol + '\'' + '}';
        }

        public boolean checkValid() {
            if (!TextUtils.isEmpty(this.cdn) && !TextUtils.isEmpty(this.resourceId) && !TextUtils.isEmpty(this.md5)) {
                return true;
            }
            OLog.w(IndexUpdateHandler.TAG, "lack param", new Object[0]);
            return false;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IndexUpdateInfo indexUpdateInfo = (IndexUpdateInfo) obj;
            if (this.cdn.equals(indexUpdateInfo.cdn) && this.resourceId.equals(indexUpdateInfo.resourceId)) {
                return this.md5.equals(indexUpdateInfo.md5);
            }
            return false;
        }

        public int hashCode() {
            return (((this.cdn.hashCode() * 31) + this.resourceId.hashCode()) * 31) + this.md5.hashCode();
        }
    }
}
