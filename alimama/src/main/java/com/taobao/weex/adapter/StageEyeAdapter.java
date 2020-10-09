package com.taobao.weex.adapter;

import com.alibaba.aliweex.adapter.IGodEyeStageAdapter;
import com.taobao.monitor.terminator.ApmGodEye;
import java.util.Map;

public class StageEyeAdapter implements IGodEyeStageAdapter {
    private static final String BIZ_WEEX = "WEEX";

    public void onStage(String str, Map<String, Object> map) {
        ApmGodEye.onStage(BIZ_WEEX, str, new Map[]{map});
    }

    public void onException(String str, String str2, Map<String, Object> map) {
        ApmGodEye.onException(BIZ_WEEX, str, str2, new Map[]{map});
    }

    public void onError(String str, String str2, Map<String, Object> map) {
        ApmGodEye.onError(BIZ_WEEX, str, str2, new Map[]{map});
    }
}
