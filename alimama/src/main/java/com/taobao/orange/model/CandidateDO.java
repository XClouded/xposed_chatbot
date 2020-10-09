package com.taobao.orange.model;

import android.text.TextUtils;
import com.taobao.orange.OConstant;
import com.taobao.orange.candidate.MultiAnalyze;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeMonitor;
import java.io.Serializable;

public class CandidateDO implements Serializable {
    private static final String TAG = "CandidateDO";
    public String match;
    public String md5;
    public String resourceId;
    public String version;

    /* access modifiers changed from: package-private */
    public boolean checkValid() {
        if (!TextUtils.isEmpty(this.resourceId) && !TextUtils.isEmpty(this.match) && !TextUtils.isEmpty(this.version)) {
            return true;
        }
        OLog.w(TAG, "lack param", new Object[0]);
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean checkMatch(String str) {
        try {
            MultiAnalyze complie = MultiAnalyze.complie(this.match, true);
            boolean match2 = complie.match();
            if (complie.unitAnalyzes.size() == 1 && "did_hash".equals(complie.unitAnalyzes.get(0).key)) {
                String format = String.format("%s:%s:%s", new Object[]{str, this.version, this.match});
                if (match2) {
                    OrangeMonitor.commitSuccess(OConstant.MONITOR_MODULE, "did_hash", format);
                } else {
                    OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, "did_hash", format, (String) null, (String) null);
                }
            }
            return match2;
        } catch (Exception e) {
            OLog.e(TAG, "checkMatch", e, new Object[0]);
            return false;
        }
    }

    public String toString() {
        return String.format("CandidateDO{match:'%s', verison:'%s'}", new Object[]{this.match, this.version});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CandidateDO candidateDO = (CandidateDO) obj;
        if (this.resourceId == null ? candidateDO.resourceId != null : !this.resourceId.equals(candidateDO.resourceId)) {
            return false;
        }
        if (this.match == null ? candidateDO.match != null : !this.match.equals(candidateDO.match)) {
            return false;
        }
        if (this.version != null) {
            return this.version.equals(candidateDO.version);
        }
        if (candidateDO.version == null) {
            return true;
        }
        return false;
    }
}
