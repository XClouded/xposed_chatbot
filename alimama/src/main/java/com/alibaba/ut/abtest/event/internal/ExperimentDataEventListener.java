package com.alibaba.ut.abtest.event.internal;

import android.text.TextUtils;
import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.event.Event;
import com.alibaba.ut.abtest.event.EventListener;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.bucketing.model.ExperimentResponseData;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;

public class ExperimentDataEventListener implements EventListener<ExperimentData> {
    private static final String TAG = "ExperimentDataEventHandler";

    public void onEvent(Event<ExperimentData> event) throws Exception {
        if (ABContext.getInstance().getCurrentApiMethod() != UTABMethod.Push) {
            LogUtils.logWAndReport(TAG, "接收到实验数据，由于不是推模式，停止处理。currentApiMethod=" + ABContext.getInstance().getCurrentApiMethod());
            return;
        }
        final ExperimentData eventValue = event.getEventValue();
        if (eventValue == null || TextUtils.isEmpty(eventValue.getExperimentData())) {
            LogUtils.logWAndReport(TAG, "接收到实验数据，内容为空！");
            return;
        }
        if (ABContext.getInstance().isDebugMode()) {
            LogUtils.logDAndReport(TAG, "接收到实验数据\n" + eventValue.getExperimentData());
            if (event.getEventValue().getBetaExperimentGroups() == null || event.getEventValue().getBetaExperimentGroups().isEmpty()) {
                LogUtils.logDAndReport(TAG, "实验数据中未包含Beta实验数据。");
            } else {
                LogUtils.logDAndReport(TAG, "实验数据中包含Beta实验数据\n" + JsonUtil.toJson(event.getEventValue().getBetaExperimentGroups()));
            }
        }
        final ExperimentResponseData experimentResponseData = (ExperimentResponseData) JsonUtil.fromJson(eventValue.getExperimentData(), ExperimentResponseData.class);
        if (experimentResponseData == null) {
            LogUtils.logDAndReport(TAG, "实验数据解析错误。\n" + event.getEventValue());
        } else if (eventValue.getBetaExperimentGroups() != null || !TextUtils.equals(experimentResponseData.sign, ABContext.getInstance().getDecisionService().getExperimentDataSignature())) {
            TaskExecutor.executeBackground(new Runnable() {
                public void run() {
                    ABContext.getInstance().getDecisionService().saveExperiments(experimentResponseData.groups, experimentResponseData.version, experimentResponseData.sign);
                    ABContext.getInstance().getDecisionService().saveBetaExperiments(eventValue.getBetaExperimentGroups());
                }
            });
        } else {
            LogUtils.logDAndReport(TAG, "接收到实验数据，数据未发生变化。数据签名=" + experimentResponseData.sign + ", 数据版本=" + experimentResponseData.version);
        }
    }
}
