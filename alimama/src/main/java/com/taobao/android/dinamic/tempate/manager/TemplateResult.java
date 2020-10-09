package com.taobao.android.dinamic.tempate.manager;

import com.alibaba.fastjson.JSONObject;

public class TemplateResult {
    public int arrivedPhase = 0;
    public String content;
    public long fileCostTimeMillis = 0;
    public long jsonCostTimeMillis = 0;
    public JSONObject jsonObject;
    public boolean loadDefault;
    public long memCostTimeMillis = 0;
    public long networkCostTimeMillis = 0;

    public void fillPerfInfo(TemplatePerfInfo templatePerfInfo) {
        this.arrivedPhase = templatePerfInfo.phase;
        this.memCostTimeMillis = templatePerfInfo.memCostTimeMillis;
        this.fileCostTimeMillis = templatePerfInfo.fileCostTimeMillis;
        this.networkCostTimeMillis = templatePerfInfo.networkCostTimeMillis;
    }
}
