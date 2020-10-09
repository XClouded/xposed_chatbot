package com.taobao.android.dinamicx.template;

import android.text.TextUtils;
import android.util.LruCache;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.template.download.DXIOUtils;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.download.DXTemplatePackageInfo;
import com.taobao.android.dinamicx.template.loader.DXFileManager;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DXTemplateInfoManager {
    private static final int EXIST_IN_ASSETS = 2;
    private static final int EXIST_IN_FILES = 1;
    private static final int EXIST_IN_NO = -1;
    private static final int EXIST_IN_UNKNOW = 0;
    private final LruCache<String, Integer> existCache;
    private ConcurrentHashMap<String, JSONObject> presetTemplateInfos;
    private final DXLongSparseArray<DXDowngradeTableInfo> templateInfoCache;
    private final Map<String, Set<Long>> templateNameEngineIdMap;
    private final Map<String, Map<String, LinkedList<DXTemplateItem>>> templatesInfoFromDB;

    private DXTemplateInfoManager() {
        this.presetTemplateInfos = new ConcurrentHashMap<>();
        this.templatesInfoFromDB = new HashMap();
        this.templateInfoCache = new DXLongSparseArray<>();
        this.templateNameEngineIdMap = new HashMap();
        this.existCache = new LruCache<>(100);
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DXTemplateInfoManager INSTANCE = new DXTemplateInfoManager();

        private SingletonHolder() {
        }
    }

    public static DXTemplateInfoManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void putPresetTemplateInfo(String str, JSONObject jSONObject) {
        if (!TextUtils.isEmpty(str) && jSONObject != null) {
            this.presetTemplateInfos.put(str, jSONObject);
        }
    }

    public boolean needLoadPresetTemplateInfo(String str) {
        if (TextUtils.isEmpty(str) || this.presetTemplateInfos.get(str) != null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void clearPresetTemplateInfo(String str) {
        this.presetTemplateInfos.remove(str);
    }

    private class DXDowngradeTableInfo {
        Map<String, LinkedList<DXTemplateItem>> downgradeTableInfo;

        private DXDowngradeTableInfo() {
            this.downgradeTableInfo = new HashMap();
        }
    }

    private void syncTable(String str, long j, DXTemplateItem dXTemplateItem) {
        LinkedList linkedList;
        synchronized (this.templateInfoCache) {
            DXDowngradeTableInfo dXDowngradeTableInfo = this.templateInfoCache.get(j);
            if (dXDowngradeTableInfo == null) {
                dXDowngradeTableInfo = new DXDowngradeTableInfo();
                this.templateInfoCache.put(j, dXDowngradeTableInfo);
            }
            linkedList = dXDowngradeTableInfo.downgradeTableInfo.get(dXTemplateItem.name);
            if (linkedList == null) {
                Map map = this.templatesInfoFromDB.get(str);
                if (map == null || map.get(dXTemplateItem.name) == null) {
                    syncMainTable(str, dXTemplateItem);
                }
                Map map2 = this.templatesInfoFromDB.get(str);
                if (map2 != null) {
                    LinkedList linkedList2 = (LinkedList) map2.get(dXTemplateItem.name);
                    if (linkedList2 == null) {
                        dXDowngradeTableInfo.downgradeTableInfo.put(dXTemplateItem.name, new LinkedList());
                    } else {
                        dXDowngradeTableInfo.downgradeTableInfo.put(dXTemplateItem.name, new LinkedList(linkedList2));
                    }
                }
            }
        }
        if (linkedList == null) {
            synchronized (this.templateNameEngineIdMap) {
                String str2 = str + dXTemplateItem.name;
                Set set = this.templateNameEngineIdMap.get(str2);
                if (set == null) {
                    HashSet hashSet = new HashSet();
                    hashSet.add(Long.valueOf(j));
                    this.templateNameEngineIdMap.put(str2, hashSet);
                } else {
                    set.add(Long.valueOf(j));
                }
            }
        }
    }

    private void syncMainTable(String str, DXTemplateItem dXTemplateItem) {
        synchronized (this.templatesInfoFromDB) {
            Map map = this.templatesInfoFromDB.get(str);
            if (map == null) {
                map = new HashMap();
                this.templatesInfoFromDB.put(str, map);
            }
            if (((LinkedList) map.get(dXTemplateItem.name)) == null) {
                LinkedList<DXTemplateItem> queryTemplates = DXTemplateDBManager.getInstance().queryTemplates(str, dXTemplateItem);
                DXTemplateItem findPresetTemplate = findPresetTemplate(str, dXTemplateItem);
                if (findPresetTemplate != null) {
                    insertTemplate(queryTemplates, findPresetTemplate);
                }
                map.put(dXTemplateItem.name, queryTemplates);
            }
        }
    }

    private int toInt(Integer num) {
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    private boolean isParamsValid(String str, long j, DXTemplateItem dXTemplateItem) {
        return j != 0 && DXTemplateNamePathUtil.isValid(str, dXTemplateItem);
    }

    private DXTemplateItem findPresetTemplate(String str, DXTemplateItem dXTemplateItem) {
        JSONObject jSONObject = this.presetTemplateInfos.get(str);
        if (jSONObject == null || jSONObject.isEmpty()) {
            return findPresetTemplateByAssetsList(str, dXTemplateItem);
        }
        return findPresetTemplateByInfoFile(str, dXTemplateItem, jSONObject);
    }

    private DXTemplateItem findPresetTemplateByInfoFile(String str, DXTemplateItem dXTemplateItem, JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject(dXTemplateItem.name);
        if (jSONObject2 == null || jSONObject2.isEmpty()) {
            if (DinamicXEngine.isDebug()) {
                DXLog.i("DXTemplateInfoManager", str + '|' + dXTemplateItem.name + "无内置");
            }
            return null;
        }
        long longValue = jSONObject2.getLongValue("version");
        if (longValue <= 0) {
            if (DinamicXEngine.isDebug()) {
                DXLog.w("DXTemplateInfoManager", str + '|' + dXTemplateItem.name + "内置索引文件版本号非数字或版本号小于1");
            }
            return null;
        }
        String string = jSONObject2.getString(DXTemplateNamePathUtil.DX_MAIN_TEMPLATE_NAME);
        if (TextUtils.isEmpty(string)) {
            if (DinamicXEngine.isDebug()) {
                DXLog.w("DXTemplateInfoManager", str + '|' + dXTemplateItem.name + "内置索引文件缺少主模板路径");
            }
            return null;
        }
        DXTemplateItem dXTemplateItem2 = new DXTemplateItem();
        dXTemplateItem2.name = dXTemplateItem.name;
        dXTemplateItem2.version = longValue;
        dXTemplateItem2.isPreset = true;
        dXTemplateItem2.packageInfo = new DXTemplatePackageInfo();
        dXTemplateItem2.packageInfo.mainFilePath = string;
        JSONObject jSONObject3 = jSONObject2.getJSONObject("other");
        if (jSONObject3 != null && !jSONObject3.isEmpty()) {
            dXTemplateItem2.packageInfo.subFilePathDict = new HashMap();
            for (String next : jSONObject3.keySet()) {
                dXTemplateItem2.packageInfo.subFilePathDict.put(next, jSONObject3.getString(next));
            }
        }
        return dXTemplateItem2;
    }

    private DXTemplateItem findPresetTemplateByAssetsList(String str, DXTemplateItem dXTemplateItem) {
        StringBuilder sb = new StringBuilder(DXFileManager.getInstance().getAssetsPath());
        sb.append(str);
        sb.append(DXTemplateNamePathUtil.DIR);
        sb.append(dXTemplateItem.name);
        long findMaxVersion = DXTemplateNamePathUtil.findMaxVersion(DXIOUtils.getAssetsFileNameList(sb.toString()));
        if (findMaxVersion < 0) {
            return null;
        }
        DXTemplateItem dXTemplateItem2 = new DXTemplateItem();
        dXTemplateItem2.name = dXTemplateItem.name;
        dXTemplateItem2.version = findMaxVersion;
        sb.append(DXTemplateNamePathUtil.DIR);
        sb.append(findMaxVersion);
        String sb2 = sb.toString();
        String[] assetsFileNameList = DXIOUtils.getAssetsFileNameList(sb2);
        if (assetsFileNameList != null) {
            HashMap hashMap = new HashMap();
            for (String str2 : assetsFileNameList) {
                hashMap.put(str2, sb2 + DXTemplateNamePathUtil.DIR + str2);
            }
            String str3 = (String) hashMap.get(DXTemplateNamePathUtil.DX_MAIN_TEMPLATE_NAME);
            if (TextUtils.isEmpty(str3)) {
                return null;
            }
            hashMap.remove(DXTemplateNamePathUtil.DX_MAIN_TEMPLATE_NAME);
            dXTemplateItem2.isPreset = true;
            dXTemplateItem2.packageInfo = new DXTemplatePackageInfo();
            DXTemplatePackageInfo dXTemplatePackageInfo = dXTemplateItem2.packageInfo;
            if (hashMap.isEmpty()) {
                hashMap = null;
            }
            dXTemplatePackageInfo.subFilePathDict = hashMap;
            dXTemplateItem2.packageInfo.mainFilePath = str3;
        }
        return dXTemplateItem2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x009e, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isTemplateExist(java.lang.String r12, com.taobao.android.dinamicx.template.download.DXTemplateItem r13) {
        /*
            r11 = this;
            boolean r0 = com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil.isValid(r12, r13)
            r1 = 0
            if (r0 == 0) goto L_0x00c5
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r12)
            java.lang.String r2 = r13.getIdentifier()
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.LruCache<java.lang.String, java.lang.Integer> r2 = r11.existCache
            monitor-enter(r2)
            android.util.LruCache<java.lang.String, java.lang.Integer> r3 = r11.existCache     // Catch:{ all -> 0x00c2 }
            java.lang.Object r3 = r3.get(r0)     // Catch:{ all -> 0x00c2 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x00c2 }
            int r3 = r11.toInt(r3)     // Catch:{ all -> 0x00c2 }
            r4 = 1
            switch(r3) {
                case -1: goto L_0x00c0;
                case 0: goto L_0x0038;
                case 1: goto L_0x0034;
                case 2: goto L_0x0030;
                default: goto L_0x002d;
            }     // Catch:{ all -> 0x00c2 }
        L_0x002d:
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            goto L_0x00c5
        L_0x0030:
            r13.isPreset = r4     // Catch:{ all -> 0x00c2 }
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r4
        L_0x0034:
            r13.isPreset = r1     // Catch:{ all -> 0x00c2 }
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r4
        L_0x0038:
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>>> r3 = r11.templatesInfoFromDB     // Catch:{ all -> 0x00c2 }
            java.lang.Object r3 = r3.get(r12)     // Catch:{ all -> 0x00c2 }
            java.util.Map r3 = (java.util.Map) r3     // Catch:{ all -> 0x00c2 }
            if (r3 == 0) goto L_0x004a
            java.lang.String r5 = r13.name     // Catch:{ all -> 0x00c2 }
            java.lang.Object r3 = r3.get(r5)     // Catch:{ all -> 0x00c2 }
            if (r3 != 0) goto L_0x004d
        L_0x004a:
            r11.syncMainTable(r12, r13)     // Catch:{ all -> 0x00c2 }
        L_0x004d:
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>>> r3 = r11.templatesInfoFromDB     // Catch:{ all -> 0x00c2 }
            java.lang.Object r12 = r3.get(r12)     // Catch:{ all -> 0x00c2 }
            java.util.Map r12 = (java.util.Map) r12     // Catch:{ all -> 0x00c2 }
            r3 = -1
            if (r12 == 0) goto L_0x00b5
            java.lang.String r5 = r13.name     // Catch:{ all -> 0x00c2 }
            java.lang.Object r12 = r12.get(r5)     // Catch:{ all -> 0x00c2 }
            java.util.LinkedList r12 = (java.util.LinkedList) r12     // Catch:{ all -> 0x00c2 }
            if (r12 == 0) goto L_0x00aa
            boolean r5 = r12.isEmpty()     // Catch:{ all -> 0x00c2 }
            if (r5 == 0) goto L_0x0069
            goto L_0x00aa
        L_0x0069:
            java.util.Iterator r12 = r12.iterator()     // Catch:{ all -> 0x00c2 }
        L_0x006d:
            boolean r5 = r12.hasNext()     // Catch:{ all -> 0x00c2 }
            if (r5 == 0) goto L_0x009f
            java.lang.Object r5 = r12.next()     // Catch:{ all -> 0x00c2 }
            com.taobao.android.dinamicx.template.download.DXTemplateItem r5 = (com.taobao.android.dinamicx.template.download.DXTemplateItem) r5     // Catch:{ all -> 0x00c2 }
            long r6 = r5.version     // Catch:{ all -> 0x00c2 }
            long r8 = r13.version     // Catch:{ all -> 0x00c2 }
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 != 0) goto L_0x006d
            boolean r12 = r5.isPreset     // Catch:{ all -> 0x00c2 }
            if (r12 == 0) goto L_0x0092
            r13.isPreset = r4     // Catch:{ all -> 0x00c2 }
            android.util.LruCache<java.lang.String, java.lang.Integer> r12 = r11.existCache     // Catch:{ all -> 0x00c2 }
            r13 = 2
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch:{ all -> 0x00c2 }
            r12.put(r0, r13)     // Catch:{ all -> 0x00c2 }
            goto L_0x009d
        L_0x0092:
            r13.isPreset = r1     // Catch:{ all -> 0x00c2 }
            android.util.LruCache<java.lang.String, java.lang.Integer> r12 = r11.existCache     // Catch:{ all -> 0x00c2 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x00c2 }
            r12.put(r0, r13)     // Catch:{ all -> 0x00c2 }
        L_0x009d:
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r4
        L_0x009f:
            android.util.LruCache<java.lang.String, java.lang.Integer> r12 = r11.existCache     // Catch:{ all -> 0x00c2 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x00c2 }
            r12.put(r0, r13)     // Catch:{ all -> 0x00c2 }
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r1
        L_0x00aa:
            android.util.LruCache<java.lang.String, java.lang.Integer> r12 = r11.existCache     // Catch:{ all -> 0x00c2 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x00c2 }
            r12.put(r0, r13)     // Catch:{ all -> 0x00c2 }
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r1
        L_0x00b5:
            android.util.LruCache<java.lang.String, java.lang.Integer> r12 = r11.existCache     // Catch:{ all -> 0x00c2 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x00c2 }
            r12.put(r0, r13)     // Catch:{ all -> 0x00c2 }
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r1
        L_0x00c0:
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            return r1
        L_0x00c2:
            r12 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00c2 }
            throw r12
        L_0x00c5:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.DXTemplateInfoManager.isTemplateExist(java.lang.String, com.taobao.android.dinamicx.template.download.DXTemplateItem):boolean");
    }

    /* access modifiers changed from: package-private */
    public DXTemplateItem getAvailableTemplate(String str, long j, DXTemplateItem dXTemplateItem) {
        if (isParamsValid(str, j, dXTemplateItem)) {
            syncTable(str, j, dXTemplateItem);
            synchronized (this.templateInfoCache) {
                LinkedList linkedList = this.templateInfoCache.get(j).downgradeTableInfo.get(dXTemplateItem.name);
                if (linkedList != null) {
                    if (linkedList.size() == 0) {
                        return null;
                    }
                    long j2 = -1;
                    Iterator descendingIterator = linkedList.descendingIterator();
                    while (descendingIterator.hasNext()) {
                        DXTemplateItem dXTemplateItem2 = (DXTemplateItem) descendingIterator.next();
                        if (dXTemplateItem2.version == dXTemplateItem.version) {
                            return dXTemplateItem2;
                        }
                        if (dXTemplateItem2.isPreset) {
                            j2 = dXTemplateItem2.version;
                        }
                        if (dXTemplateItem2.version < dXTemplateItem.version) {
                            if (dXTemplateItem2.version < j2) {
                                return null;
                            }
                            return dXTemplateItem2;
                        }
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public DXTemplateItem getPresetTemplate(String str, long j, DXTemplateItem dXTemplateItem) {
        if (!isParamsValid(str, j, dXTemplateItem)) {
            return null;
        }
        syncTable(str, j, dXTemplateItem);
        synchronized (this.templateInfoCache) {
            Iterator descendingIterator = this.templateInfoCache.get(j).downgradeTableInfo.get(dXTemplateItem.name).descendingIterator();
            while (descendingIterator.hasNext()) {
                DXTemplateItem dXTemplateItem2 = (DXTemplateItem) descendingIterator.next();
                if (dXTemplateItem2.isPreset && dXTemplateItem2.version <= dXTemplateItem.version) {
                    return dXTemplateItem2;
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public DXTemplateItem getSelfOrPresetTemplate(String str, long j, DXTemplateItem dXTemplateItem) {
        if (!isParamsValid(str, j, dXTemplateItem)) {
            return null;
        }
        syncTable(str, j, dXTemplateItem);
        synchronized (this.templateInfoCache) {
            Iterator descendingIterator = this.templateInfoCache.get(j).downgradeTableInfo.get(dXTemplateItem.name).descendingIterator();
            while (descendingIterator.hasNext()) {
                DXTemplateItem dXTemplateItem2 = (DXTemplateItem) descendingIterator.next();
                if (dXTemplateItem2.version == dXTemplateItem.version) {
                    return dXTemplateItem2;
                }
                if (dXTemplateItem2.isPreset && dXTemplateItem2.version < dXTemplateItem.version) {
                    return dXTemplateItem2;
                }
            }
            return null;
        }
    }

    public void removeTemplate(String str, DXTemplateItem dXTemplateItem) {
        LinkedList linkedList;
        if (DXTemplateNamePathUtil.isValid(str, dXTemplateItem)) {
            synchronized (this.templatesInfoFromDB) {
                Map map = this.templatesInfoFromDB.get(str);
                if (map == null || map.get(dXTemplateItem.name) == null) {
                    syncMainTable(str, dXTemplateItem);
                }
                Map map2 = this.templatesInfoFromDB.get(str);
                if (map2 != null) {
                    LinkedList linkedList2 = (LinkedList) map2.get(dXTemplateItem.name);
                    if (linkedList2 == null) {
                        map2.put(dXTemplateItem.name, new LinkedList());
                    } else {
                        linkedList2.remove(dXTemplateItem);
                    }
                }
            }
            synchronized (this.existCache) {
                this.existCache.put(str + dXTemplateItem.getIdentifier(), -1);
            }
            HashSet<Long> hashSet = null;
            synchronized (this.templateNameEngineIdMap) {
                Set set = this.templateNameEngineIdMap.get(str + dXTemplateItem.name);
                if (set != null && !set.isEmpty()) {
                    hashSet = new HashSet<>(set);
                }
            }
            if (hashSet != null && !hashSet.isEmpty()) {
                synchronized (this.templateInfoCache) {
                    for (Long longValue : hashSet) {
                        DXDowngradeTableInfo dXDowngradeTableInfo = this.templateInfoCache.get(longValue.longValue());
                        if (!(dXDowngradeTableInfo == null || (linkedList = dXDowngradeTableInfo.downgradeTableInfo.get(dXTemplateItem.name)) == null)) {
                            linkedList.remove(dXTemplateItem);
                        }
                    }
                }
            }
            DXTemplateDBManager.getInstance().deleteTemplateItem(str, dXTemplateItem);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0045, code lost:
        r6 = r3.existCache;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0047, code lost:
        monitor-enter(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r3.existCache.put(r4 + r7.getIdentifier(), 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0065, code lost:
        monitor-exit(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0066, code lost:
        r5 = null;
        r0 = r3.templateNameEngineIdMap;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0069, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r4 = r3.templateNameEngineIdMap.get(r4 + r7.name);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0083, code lost:
        if (r4 == null) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0089, code lost:
        if (r4.isEmpty() != false) goto L_0x0090;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008b, code lost:
        r5 = new java.util.HashSet(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0090, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0091, code lost:
        if (r5 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0097, code lost:
        if (r5.isEmpty() != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0099, code lost:
        r4 = r3.templateInfoCache;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x009b, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r5 = r5.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00a4, code lost:
        if (r5.hasNext() == false) goto L_0x00c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a6, code lost:
        r6 = r3.templateInfoCache.get(((java.lang.Long) r5.next()).longValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b8, code lost:
        if (r6 == null) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00ba, code lost:
        insertTemplate(r6.downgradeTableInfo.get(r7.name), r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c8, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateTemplate(java.lang.String r4, long r5, com.taobao.android.dinamicx.template.download.DXTemplateItem r7) {
        /*
            r3 = this;
            boolean r5 = r3.isParamsValid(r4, r5, r7)
            if (r5 == 0) goto L_0x00d6
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>>> r5 = r3.templatesInfoFromDB
            monitor-enter(r5)
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>>> r6 = r3.templatesInfoFromDB     // Catch:{ all -> 0x00d3 }
            java.lang.Object r6 = r6.get(r4)     // Catch:{ all -> 0x00d3 }
            java.util.Map r6 = (java.util.Map) r6     // Catch:{ all -> 0x00d3 }
            if (r6 == 0) goto L_0x001b
            java.lang.String r0 = r7.name     // Catch:{ all -> 0x00d3 }
            java.lang.Object r6 = r6.get(r0)     // Catch:{ all -> 0x00d3 }
            if (r6 != 0) goto L_0x001e
        L_0x001b:
            r3.syncMainTable(r4, r7)     // Catch:{ all -> 0x00d3 }
        L_0x001e:
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>>> r6 = r3.templatesInfoFromDB     // Catch:{ all -> 0x00d3 }
            java.lang.Object r6 = r6.get(r4)     // Catch:{ all -> 0x00d3 }
            java.util.Map r6 = (java.util.Map) r6     // Catch:{ all -> 0x00d3 }
            if (r6 == 0) goto L_0x0044
            java.lang.String r0 = r7.name     // Catch:{ all -> 0x00d3 }
            java.lang.Object r0 = r6.get(r0)     // Catch:{ all -> 0x00d3 }
            java.util.LinkedList r0 = (java.util.LinkedList) r0     // Catch:{ all -> 0x00d3 }
            if (r0 != 0) goto L_0x003c
            java.util.LinkedList r0 = new java.util.LinkedList     // Catch:{ all -> 0x00d3 }
            r0.<init>()     // Catch:{ all -> 0x00d3 }
            java.lang.String r1 = r7.name     // Catch:{ all -> 0x00d3 }
            r6.put(r1, r0)     // Catch:{ all -> 0x00d3 }
        L_0x003c:
            boolean r6 = r3.insertTemplate(r0, r7)     // Catch:{ all -> 0x00d3 }
            if (r6 != 0) goto L_0x0044
            monitor-exit(r5)     // Catch:{ all -> 0x00d3 }
            return
        L_0x0044:
            monitor-exit(r5)     // Catch:{ all -> 0x00d3 }
            android.util.LruCache<java.lang.String, java.lang.Integer> r6 = r3.existCache
            monitor-enter(r6)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d0 }
            r5.<init>()     // Catch:{ all -> 0x00d0 }
            r5.append(r4)     // Catch:{ all -> 0x00d0 }
            java.lang.String r0 = r7.getIdentifier()     // Catch:{ all -> 0x00d0 }
            r5.append(r0)     // Catch:{ all -> 0x00d0 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00d0 }
            android.util.LruCache<java.lang.String, java.lang.Integer> r0 = r3.existCache     // Catch:{ all -> 0x00d0 }
            r1 = 1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x00d0 }
            r0.put(r5, r1)     // Catch:{ all -> 0x00d0 }
            monitor-exit(r6)     // Catch:{ all -> 0x00d0 }
            r5 = 0
            java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> r0 = r3.templateNameEngineIdMap
            monitor-enter(r0)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cd }
            r6.<init>()     // Catch:{ all -> 0x00cd }
            r6.append(r4)     // Catch:{ all -> 0x00cd }
            java.lang.String r4 = r7.name     // Catch:{ all -> 0x00cd }
            r6.append(r4)     // Catch:{ all -> 0x00cd }
            java.lang.String r4 = r6.toString()     // Catch:{ all -> 0x00cd }
            java.util.Map<java.lang.String, java.util.Set<java.lang.Long>> r6 = r3.templateNameEngineIdMap     // Catch:{ all -> 0x00cd }
            java.lang.Object r4 = r6.get(r4)     // Catch:{ all -> 0x00cd }
            java.util.Set r4 = (java.util.Set) r4     // Catch:{ all -> 0x00cd }
            if (r4 == 0) goto L_0x0090
            boolean r6 = r4.isEmpty()     // Catch:{ all -> 0x00cd }
            if (r6 != 0) goto L_0x0090
            java.util.HashSet r5 = new java.util.HashSet     // Catch:{ all -> 0x00cd }
            r5.<init>(r4)     // Catch:{ all -> 0x00cd }
        L_0x0090:
            monitor-exit(r0)     // Catch:{ all -> 0x00cd }
            if (r5 == 0) goto L_0x00d6
            boolean r4 = r5.isEmpty()
            if (r4 != 0) goto L_0x00d6
            com.taobao.android.dinamicx.model.DXLongSparseArray<com.taobao.android.dinamicx.template.DXTemplateInfoManager$DXDowngradeTableInfo> r4 = r3.templateInfoCache
            monitor-enter(r4)
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x00ca }
        L_0x00a0:
            boolean r6 = r5.hasNext()     // Catch:{ all -> 0x00ca }
            if (r6 == 0) goto L_0x00c8
            java.lang.Object r6 = r5.next()     // Catch:{ all -> 0x00ca }
            java.lang.Long r6 = (java.lang.Long) r6     // Catch:{ all -> 0x00ca }
            com.taobao.android.dinamicx.model.DXLongSparseArray<com.taobao.android.dinamicx.template.DXTemplateInfoManager$DXDowngradeTableInfo> r0 = r3.templateInfoCache     // Catch:{ all -> 0x00ca }
            long r1 = r6.longValue()     // Catch:{ all -> 0x00ca }
            java.lang.Object r6 = r0.get(r1)     // Catch:{ all -> 0x00ca }
            com.taobao.android.dinamicx.template.DXTemplateInfoManager$DXDowngradeTableInfo r6 = (com.taobao.android.dinamicx.template.DXTemplateInfoManager.DXDowngradeTableInfo) r6     // Catch:{ all -> 0x00ca }
            if (r6 == 0) goto L_0x00a0
            java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>> r6 = r6.downgradeTableInfo     // Catch:{ all -> 0x00ca }
            java.lang.String r0 = r7.name     // Catch:{ all -> 0x00ca }
            java.lang.Object r6 = r6.get(r0)     // Catch:{ all -> 0x00ca }
            java.util.LinkedList r6 = (java.util.LinkedList) r6     // Catch:{ all -> 0x00ca }
            r3.insertTemplate(r6, r7)     // Catch:{ all -> 0x00ca }
            goto L_0x00a0
        L_0x00c8:
            monitor-exit(r4)     // Catch:{ all -> 0x00ca }
            goto L_0x00d6
        L_0x00ca:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00ca }
            throw r5
        L_0x00cd:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00cd }
            throw r4
        L_0x00d0:
            r4 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x00d0 }
            throw r4
        L_0x00d3:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00d3 }
            throw r4
        L_0x00d6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.DXTemplateInfoManager.updateTemplate(java.lang.String, long, com.taobao.android.dinamicx.template.download.DXTemplateItem):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0098, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int downgradeTemplate(java.lang.String r8, long r9, com.taobao.android.dinamicx.template.download.DXTemplateItem r11) {
        /*
            r7 = this;
            monitor-enter(r7)
            boolean r0 = r7.isParamsValid(r8, r9, r11)     // Catch:{ all -> 0x0099 }
            r1 = 0
            if (r0 == 0) goto L_0x0097
            r7.syncTable(r8, r9, r11)     // Catch:{ all -> 0x0099 }
            com.taobao.android.dinamicx.model.DXLongSparseArray<com.taobao.android.dinamicx.template.DXTemplateInfoManager$DXDowngradeTableInfo> r0 = r7.templateInfoCache     // Catch:{ all -> 0x0099 }
            java.lang.Object r9 = r0.get(r9)     // Catch:{ all -> 0x0099 }
            com.taobao.android.dinamicx.template.DXTemplateInfoManager$DXDowngradeTableInfo r9 = (com.taobao.android.dinamicx.template.DXTemplateInfoManager.DXDowngradeTableInfo) r9     // Catch:{ all -> 0x0099 }
            java.util.Map<java.lang.String, java.util.LinkedList<com.taobao.android.dinamicx.template.download.DXTemplateItem>> r9 = r9.downgradeTableInfo     // Catch:{ all -> 0x0099 }
            java.lang.String r10 = r11.name     // Catch:{ all -> 0x0099 }
            java.lang.Object r9 = r9.get(r10)     // Catch:{ all -> 0x0099 }
            java.util.LinkedList r9 = (java.util.LinkedList) r9     // Catch:{ all -> 0x0099 }
            int r10 = r9.size()     // Catch:{ all -> 0x0099 }
            java.util.Iterator r9 = r9.descendingIterator()     // Catch:{ all -> 0x0099 }
        L_0x0025:
            boolean r0 = r9.hasNext()     // Catch:{ all -> 0x0099 }
            if (r0 == 0) goto L_0x0097
            java.lang.Object r0 = r9.next()     // Catch:{ all -> 0x0099 }
            com.taobao.android.dinamicx.template.download.DXTemplateItem r0 = (com.taobao.android.dinamicx.template.download.DXTemplateItem) r0     // Catch:{ all -> 0x0099 }
            long r2 = r11.version     // Catch:{ all -> 0x0099 }
            long r4 = r0.version     // Catch:{ all -> 0x0099 }
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 != 0) goto L_0x0025
            boolean r0 = r0.isPreset     // Catch:{ all -> 0x0099 }
            r2 = 124(0x7c, float:1.74E-43)
            r3 = 1
            if (r0 == 0) goto L_0x006b
            boolean r10 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()     // Catch:{ all -> 0x0099 }
            if (r10 == 0) goto L_0x0065
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0099 }
            r10.<init>(r8)     // Catch:{ all -> 0x0099 }
            r10.append(r2)     // Catch:{ all -> 0x0099 }
            java.lang.String r8 = r11.name     // Catch:{ all -> 0x0099 }
            r10.append(r8)     // Catch:{ all -> 0x0099 }
            java.lang.String r8 = "内置被降级，无法再降级"
            r10.append(r8)     // Catch:{ all -> 0x0099 }
            java.lang.String r8 = "DXTemplateInfoManager"
            java.lang.String[] r11 = new java.lang.String[r3]     // Catch:{ all -> 0x0099 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0099 }
            r11[r1] = r10     // Catch:{ all -> 0x0099 }
            com.taobao.android.dinamicx.log.DXLog.i(r8, r11)     // Catch:{ all -> 0x0099 }
        L_0x0065:
            r9.remove()     // Catch:{ all -> 0x0099 }
            r8 = 2
            monitor-exit(r7)
            return r8
        L_0x006b:
            if (r10 != r3) goto L_0x0092
            boolean r10 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()     // Catch:{ all -> 0x0099 }
            if (r10 == 0) goto L_0x0092
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x0099 }
            r10.<init>(r8)     // Catch:{ all -> 0x0099 }
            r10.append(r2)     // Catch:{ all -> 0x0099 }
            java.lang.String r8 = r11.name     // Catch:{ all -> 0x0099 }
            r10.append(r8)     // Catch:{ all -> 0x0099 }
            java.lang.String r8 = "无内置情况，无法再降级"
            r10.append(r8)     // Catch:{ all -> 0x0099 }
            java.lang.String r8 = "DXTemplateInfoManager"
            java.lang.String[] r11 = new java.lang.String[r3]     // Catch:{ all -> 0x0099 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0099 }
            r11[r1] = r10     // Catch:{ all -> 0x0099 }
            com.taobao.android.dinamicx.log.DXLog.i(r8, r11)     // Catch:{ all -> 0x0099 }
        L_0x0092:
            r9.remove()     // Catch:{ all -> 0x0099 }
            monitor-exit(r7)
            return r3
        L_0x0097:
            monitor-exit(r7)
            return r1
        L_0x0099:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.template.DXTemplateInfoManager.downgradeTemplate(java.lang.String, long, com.taobao.android.dinamicx.template.download.DXTemplateItem):int");
    }

    public DXTemplatePackageInfo getPackageInfo(String str, DXTemplateItem dXTemplateItem) {
        LinkedList linkedList;
        if (DXTemplateNamePathUtil.isValid(str, dXTemplateItem)) {
            synchronized (this.templatesInfoFromDB) {
                Map map = this.templatesInfoFromDB.get(str);
                if (map == null || map.get(dXTemplateItem.name) == null) {
                    syncMainTable(str, dXTemplateItem);
                }
                Map map2 = this.templatesInfoFromDB.get(str);
                if (!(map2 == null || (linkedList = (LinkedList) map2.get(dXTemplateItem.name)) == null)) {
                    if (linkedList.size() == 0) {
                        return null;
                    }
                    Iterator descendingIterator = linkedList.descendingIterator();
                    while (descendingIterator.hasNext()) {
                        DXTemplateItem dXTemplateItem2 = (DXTemplateItem) descendingIterator.next();
                        if (dXTemplateItem2.version == dXTemplateItem.version) {
                            DXTemplatePackageInfo dXTemplatePackageInfo = dXTemplateItem2.packageInfo;
                            return dXTemplatePackageInfo;
                        }
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public synchronized void clearTemplateInfoCache(long j) {
        if (j != 0) {
            this.templateInfoCache.remove(j);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean insertTemplate(LinkedList<DXTemplateItem> linkedList, DXTemplateItem dXTemplateItem) {
        if (linkedList == null || dXTemplateItem == null) {
            return false;
        }
        long j = dXTemplateItem.version;
        int size = linkedList.size();
        if (size == 0) {
            linkedList.add(dXTemplateItem);
            return true;
        } else if (j > linkedList.getLast().version) {
            linkedList.add(dXTemplateItem);
            return true;
        } else {
            Iterator<DXTemplateItem> descendingIterator = linkedList.descendingIterator();
            descendingIterator.next();
            int i = size - 2;
            while (descendingIterator.hasNext()) {
                if (descendingIterator.next().version < j) {
                    linkedList.add(i + 1, dXTemplateItem);
                    return true;
                }
                i--;
            }
            linkedList.addFirst(dXTemplateItem);
            return true;
        }
    }
}
