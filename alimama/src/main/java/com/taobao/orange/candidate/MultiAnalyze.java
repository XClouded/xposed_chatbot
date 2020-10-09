package com.taobao.orange.candidate;

import android.os.Build;
import android.os.RemoteException;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.ICandidateCompare;
import com.taobao.orange.OCandidate;
import com.taobao.orange.OConstant;
import com.taobao.orange.util.OLog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MultiAnalyze {
    private static final String JOINER_CHAR = "&";
    private static final String TAG = "MultiAnalyze";
    public static Map<String, OCandidate> candidateMap = new ConcurrentHashMap();
    public List<UnitAnalyze> unitAnalyzes = new ArrayList();

    public static void initBuildInCandidates() {
        OCandidate[] oCandidateArr = {new OCandidate((String) OConstant.CANDIDATE_APPVER, GlobalOrange.appVersion, (Class<? extends ICandidateCompare>) VersionCompare.class), new OCandidate((String) OConstant.CANDIDATE_OSVER, String.valueOf(Build.VERSION.SDK_INT), (Class<? extends ICandidateCompare>) IntCompare.class), new OCandidate((String) OConstant.CANDIDATE_MANUFACTURER, String.valueOf(Build.MANUFACTURER), (Class<? extends ICandidateCompare>) StringCompare.class), new OCandidate((String) OConstant.CANDIDATE_BRAND, String.valueOf(Build.BRAND), (Class<? extends ICandidateCompare>) StringCompare.class), new OCandidate((String) OConstant.CANDIDATE_MODEL, String.valueOf(Build.MODEL), (Class<? extends ICandidateCompare>) StringCompare.class), new OCandidate("did_hash", GlobalOrange.deviceId, (Class<? extends ICandidateCompare>) HashCompare.class)};
        OLog.d(TAG, "initBuildInCandidates", new Object[0]);
        addCandidate(oCandidateArr);
    }

    public static void addCandidate(OCandidate... oCandidateArr) {
        HashSet hashSet = new HashSet();
        int length = oCandidateArr.length;
        int i = 0;
        while (i < length) {
            OCandidate oCandidate = oCandidateArr[i];
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "addCandidate", "candidate", oCandidate);
            }
            String key = oCandidate.getKey();
            OCandidate oCandidate2 = candidateMap.get(key);
            if (oCandidate2 == null || !oCandidate2.compare(oCandidate)) {
                if (oCandidate2 != null) {
                    OLog.w(TAG, "addCandidate", "update baseCandidate", oCandidate2);
                }
                candidateMap.put(key, oCandidate);
                hashSet.add(key);
                i++;
            } else {
                OLog.w(TAG, "addCandidate exist same", new Object[0]);
                return;
            }
        }
        ConfigCenter.getInstance().rematchNamespace(hashSet);
    }

    public static MultiAnalyze complie(String str, boolean z) {
        return new MultiAnalyze(str, z);
    }

    private MultiAnalyze(String str, boolean z) {
        for (String complie : str.split("&")) {
            this.unitAnalyzes.add(UnitAnalyze.complie(complie));
        }
        if (z && OLog.isPrintLog(0)) {
            OLog.v(TAG, "parse start", "unitAnalyzes", this.unitAnalyzes);
        }
    }

    public boolean match() throws RemoteException {
        for (UnitAnalyze next : this.unitAnalyzes) {
            OCandidate oCandidate = candidateMap.get(next.key);
            if (oCandidate == null) {
                if (OLog.isPrintLog(3)) {
                    OLog.w(TAG, "match fail", "key", next.key, "reason", "no found local Candidate");
                }
                return false;
            } else if (!next.match(oCandidate.getClientVal(), oCandidate.getCompare())) {
                return false;
            }
        }
        return true;
    }

    public Set<String> getKeySet() {
        HashSet hashSet = new HashSet();
        for (UnitAnalyze unitAnalyze : this.unitAnalyzes) {
            hashSet.add(unitAnalyze.key);
        }
        return hashSet;
    }
}
