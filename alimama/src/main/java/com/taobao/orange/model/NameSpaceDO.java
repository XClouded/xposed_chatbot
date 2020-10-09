package com.taobao.orange.model;

import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import java.io.Serializable;
import java.util.List;

public class NameSpaceDO implements Serializable {
    public static final int HIGH_INIT = 0;
    public static final int HIGH_LAZY = 1;
    public static final String LEVEL_DEFAULT = "DEFAULT";
    public static final String LEVEL_HIGH = "HIGH";
    private static final String TAG = "NameSpaceDO";
    public static final String TYPE_CUSTOM = "CUSTOM";
    public static final String TYPE_STANDARD = "STANDARD";
    private static final long serialVersionUID = -4740785816043854483L;
    public List<CandidateDO> candidates;
    public transient CandidateDO curCandidateDO;
    public transient boolean hasChanged;
    public Integer highLazy = 0;
    public String loadLevel;
    public String md5;
    public String name;
    public String resourceId;
    public String type;
    public String version;

    public boolean checkValid(ConfigDO configDO) {
        ConfigDO configDO2 = configDO;
        long j = 0;
        long parseLong = configDO2 == null ? 0 : OrangeUtils.parseLong(configDO.getCurVersion());
        boolean z = (configDO2 == null || configDO2.candidate == null) ? false : true;
        if (configDO2 != null && !z) {
            j = OrangeUtils.parseLong(configDO2.version);
        }
        long parseLong2 = OrangeUtils.parseLong(this.version);
        if (this.candidates != null && !this.candidates.isEmpty()) {
            if (OLog.isPrintLog(0)) {
                OLog.v(TAG, "checkCandidates start", BindingXConstants.KEY_CONFIG, this.name, "candidates.size", Integer.valueOf(this.candidates.size()));
            }
            int i = 0;
            while (i < this.candidates.size()) {
                CandidateDO candidateDO = this.candidates.get(i);
                if (OLog.isPrintLog(0)) {
                    OLog.v(TAG, "checkCandidate start", "index", Integer.valueOf(i), candidateDO);
                }
                if (!candidateDO.checkValid() || !candidateDO.checkMatch(this.name)) {
                    i++;
                } else if (!z || OrangeUtils.parseLong(candidateDO.version) != parseLong) {
                    if (OLog.isPrintLog(1)) {
                        OLog.d(TAG, "checkCandidate match", "localV", Long.valueOf(parseLong), "remoteV", candidateDO.version);
                    }
                    this.curCandidateDO = candidateDO;
                    return true;
                } else {
                    if (OLog.isPrintLog(1)) {
                        OLog.d(TAG, "checkCandidate match but no version update", new Object[0]);
                    }
                    return false;
                }
            }
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "checkCandidates finish", "not any match");
            }
        }
        boolean z2 = parseLong2 > j;
        if (!z2 && OLog.isPrintLog(1)) {
            OLog.d(TAG, "checkValid", "no version update");
        }
        return z2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NameSpaceDO{");
        sb.append("loadLevel='");
        sb.append(this.loadLevel);
        sb.append('\'');
        sb.append(", name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", version='");
        sb.append(this.version);
        sb.append('\'');
        sb.append('}');
        return String.format("NameSpaceDO{level:'%s', name:'%s', verison:'%s'}", new Object[]{this.loadLevel, this.name, this.version});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NameSpaceDO nameSpaceDO = (NameSpaceDO) obj;
        if (this.loadLevel == null ? nameSpaceDO.loadLevel != null : !this.loadLevel.equals(nameSpaceDO.loadLevel)) {
            return false;
        }
        if (this.md5 == null ? nameSpaceDO.md5 != null : !this.md5.equals(nameSpaceDO.md5)) {
            return false;
        }
        if (this.name == null ? nameSpaceDO.name != null : !this.name.equals(nameSpaceDO.name)) {
            return false;
        }
        if (this.resourceId == null ? nameSpaceDO.resourceId != null : !this.resourceId.equals(nameSpaceDO.resourceId)) {
            return false;
        }
        if (this.version == null ? nameSpaceDO.version != null : !this.version.equals(nameSpaceDO.version)) {
            return false;
        }
        if (this.candidates != null) {
            return this.candidates.equals(nameSpaceDO.candidates);
        }
        if (nameSpaceDO.candidates == null) {
            return true;
        }
        return false;
    }
}
