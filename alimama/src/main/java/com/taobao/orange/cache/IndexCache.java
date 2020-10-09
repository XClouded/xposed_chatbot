package com.taobao.orange.cache;

import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.candidate.MultiAnalyze;
import com.taobao.orange.model.CandidateDO;
import com.taobao.orange.model.IndexDO;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.util.FileUtil;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import com.taobao.orange.util.OrangeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

public class IndexCache {
    public static final String INDEX_STORE_NAME = "orange.index";
    private static final String TAG = "IndexCache";
    public Map<String, Set<String>> candidateNamespace = new HashMap();
    /* access modifiers changed from: private */
    public volatile IndexDO mIndex = new IndexDO();

    public void load() {
        IndexDO indexDO = (IndexDO) FileUtil.restoreObject(INDEX_STORE_NAME);
        if (indexDO != null) {
            if (OLog.isPrintLog(2)) {
                OLog.i(TAG, "load", "indexDO", OrangeUtils.formatIndexDO(indexDO));
            }
            this.candidateNamespace = getCandidateNamespace(indexDO);
            this.mIndex = indexDO;
        } else {
            OLog.w(TAG, "load fail", new Object[0]);
            try {
                FileUtil.clearCacheFile();
            } catch (Throwable th) {
                OLog.e(TAG, "load clean cache exception", th, new Object[0]);
            }
        }
        updateOrangeHeader();
    }

    public List<String> cache(IndexDO indexDO) {
        Map<String, NameSpaceDO> formatMergedNamespaceMap = formatMergedNamespaceMap(this.mIndex.mergedNamespaces);
        Map<String, NameSpaceDO> formatMergedNamespaceMap2 = formatMergedNamespaceMap(indexDO.mergedNamespaces);
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(formatMergedNamespaceMap.keySet());
        arrayList.removeAll(formatMergedNamespaceMap2.keySet());
        for (Map.Entry next : formatMergedNamespaceMap2.entrySet()) {
            NameSpaceDO nameSpaceDO = formatMergedNamespaceMap.get((String) next.getKey());
            NameSpaceDO nameSpaceDO2 = (NameSpaceDO) next.getValue();
            if (nameSpaceDO == null) {
                nameSpaceDO2.hasChanged = true;
            } else {
                boolean z = !nameSpaceDO2.equals(nameSpaceDO);
                if (z && OLog.isPrintLog(2)) {
                    OLog.i(TAG, "cache", "compare change NameSpaceDO", OrangeUtils.formatNamespaceDO(nameSpaceDO2));
                }
                nameSpaceDO2.hasChanged = z;
            }
            if (nameSpaceDO2.hasChanged) {
                OrangeMonitor.commitConfigMonitor(OConstant.POINT_CONFIG_PENDING_UPDATE, nameSpaceDO2.name, nameSpaceDO2.version);
            }
        }
        this.candidateNamespace = getCandidateNamespace(indexDO);
        this.mIndex = indexDO;
        updateOrangeHeader();
        OThreadFactory.executeDisk(new Runnable() {
            public void run() {
                FileUtil.persistObject(IndexCache.this.mIndex, IndexCache.INDEX_STORE_NAME);
            }
        });
        return arrayList;
    }

    private Map<String, Set<String>> getCandidateNamespace(IndexDO indexDO) {
        if (indexDO == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (NameSpaceDO next : indexDO.mergedNamespaces) {
            if (next.candidates != null && !next.candidates.isEmpty()) {
                for (CandidateDO candidateDO : next.candidates) {
                    for (String next2 : MultiAnalyze.complie(candidateDO.match, false).getKeySet()) {
                        Set set = (Set) hashMap.get(next2);
                        if (set == null) {
                            set = new HashSet();
                            hashMap.put(next2, set);
                        }
                        set.add(next.name);
                    }
                }
            }
        }
        if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "getCandidateNamespace", "result", hashMap);
        }
        return hashMap;
    }

    private Map<String, NameSpaceDO> formatMergedNamespaceMap(List<NameSpaceDO> list) {
        HashMap hashMap = new HashMap();
        if (list == null || list.isEmpty()) {
            return hashMap;
        }
        for (NameSpaceDO next : list) {
            hashMap.put(next.name, next);
        }
        return hashMap;
    }

    private void updateOrangeHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("appKey");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(GlobalOrange.appKey);
        sb.append("&");
        sb.append("appVersion");
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(GlobalOrange.appVersion);
        sb.append("&");
        sb.append(OConstant.KEY_CLIENTAPPINDEXVERSION);
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(getAppIndexVersion());
        sb.append("&");
        sb.append(OConstant.KEY_CLIENTVERSIONINDEXVERSION);
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(getVersionIndexVersion());
        OLog.i(TAG, "updateOrangeHeader", "reqOrangeHeader", sb.toString());
        GlobalOrange.reqOrangeHeader = sb.toString();
    }

    public IndexDO getIndex() {
        return this.mIndex;
    }

    public Set<NameSpaceDO> getAllNameSpaces() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.mIndex.mergedNamespaces);
        return hashSet;
    }

    public Set<NameSpaceDO> getUpdateNameSpaces(Set<String> set) {
        HashSet hashSet = new HashSet();
        for (NameSpaceDO next : this.mIndex.mergedNamespaces) {
            if (next.hasChanged) {
                if (NameSpaceDO.LEVEL_HIGH.equals(next.loadLevel)) {
                    hashSet.add(next);
                } else if (set != null && set.contains(next.name)) {
                    hashSet.add(next);
                }
            }
        }
        return hashSet;
    }

    public NameSpaceDO getNameSpace(String str) {
        long nanoTime = System.nanoTime();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (NameSpaceDO next : this.mIndex.mergedNamespaces) {
            if (str.equals(next.name)) {
                OLog.d(TAG, "time (getNameSpace)", "time: " + String.valueOf((System.nanoTime() - nanoTime) / 1000));
                return next;
            }
        }
        OLog.d(TAG, "time (getNameSpace)", "time: " + String.valueOf((System.nanoTime() - nanoTime) / 1000));
        return null;
    }

    public String getCdnUrl() {
        if (TextUtils.isEmpty(this.mIndex.cdn)) {
            return null;
        }
        return GlobalOrange.schema + HttpConstant.SCHEME_SPLIT + this.mIndex.cdn;
    }

    public String getAppIndexVersion() {
        return this.mIndex.appIndexVersion == null ? "0" : this.mIndex.appIndexVersion;
    }

    public String getVersionIndexVersion() {
        return this.mIndex.versionIndexVersion == null ? "0" : this.mIndex.versionIndexVersion;
    }
}
