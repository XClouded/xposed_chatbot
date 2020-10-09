package com.taobao.orange.model;

import android.text.TextUtils;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.Map;

public class ConfigDO implements Serializable, CheckDO {
    protected static final String TAG = "ConfigDO";
    private static final long serialVersionUID = 6057693726984967889L;
    public String appKey;
    public String appVersion;
    public CandidateDO candidate;
    public Map<String, String> content;
    public String createTime;
    public String id;
    public String loadLevel;
    public volatile transient boolean monitored = false;
    public String name;
    public transient boolean persisted = true;
    public String resourceId;
    public String type;
    public String version;

    public boolean checkValid() {
        if (TextUtils.isEmpty(this.appKey) || TextUtils.isEmpty(this.appVersion) || TextUtils.isEmpty(this.id) || TextUtils.isEmpty(this.name) || TextUtils.isEmpty(this.resourceId) || TextUtils.isEmpty(this.type) || TextUtils.isEmpty(this.loadLevel) || TextUtils.isEmpty(this.version) || this.content == null || this.content.isEmpty()) {
            OLog.w(TAG, "lack param", new Object[0]);
            return false;
        }
        boolean z = ("*".equals(this.appVersion) || GlobalOrange.appVersion.equals(this.appVersion)) && GlobalOrange.appKey.equals(this.appKey);
        if (!z) {
            OLog.w(TAG, "invaild", new Object[0]);
        }
        return z;
    }

    public String getCurVersion() {
        return this.candidate == null ? this.version : this.candidate.version;
    }

    public String toString() {
        return String.format("ConfigDO{name:'%s', appVersion:'%s', verison:'%s', type:'%s'}", new Object[]{this.name, this.appVersion, this.version, this.type});
    }
}
