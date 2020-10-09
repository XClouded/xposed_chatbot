package com.alibaba.ut.abtest.config;

import android.taobao.windvane.service.WVEventId;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Set;

public class Config implements Serializable {
    private static final long serialVersionUID = 2461930403646924652L;
    @JSONField(name = "autoTrackEnabled")
    public boolean autoTrackEnabled = true;
    @JSONField(name = "cacheEnabled")
    public boolean cacheEnabled = true;
    @JSONField(name = "cdnEnabled")
    public boolean cdnEnabled = false;
    @JSONField(name = "configRefreshDuration")
    public long configRefreshDuration;
    @JSONField(name = "dbReadEnabled")
    public boolean dbReadEnabled = true;
    @JSONField(name = "dbWriteEnabled")
    public boolean dbWriteEnabled = true;
    @JSONField(name = "enabled")
    public long enabled = 10000;
    @JSONField(name = "navEnabled")
    public boolean navEnabled = false;
    @JSONField(name = "navIgnores")
    public Set<String> navIgnores;
    @JSONField(name = "pullRange")
    public int[] pullRange = {0, 5000};
    @JSONField(name = "pushRange")
    public int[] pushRange = {WVEventId.ACCS_ONDATA, 10000};
    @JSONField(name = "syncCrowdDelayed")
    public int syncCrowdDelayed = 120000;
    @JSONField(name = "triggerEnabled")
    public boolean triggerEnabled = true;
}
