package com.alibaba.ha.adapter.service.tlog;

import android.content.Context;
import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.service.bizError.BizErrorInfo;
import com.alibaba.ha.bizerrorreporter.module.AggregationType;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;

public class TLogMonitorImpl implements TLogMonitor {
    private static String BIZ_ERROR_TYPE = "TLOG_MONITOR";
    private static String MONITOR_POINTER = "TLOG_MONITOR_STAGE";
    private static String PAGE = "TLOG_MONITOR";
    public static String TAG = "AliHaAdapter.TLogMonitorImpl";
    private static String TLOG_MODEL = "TLOG_MONITOR";
    private static String dim_stage = "stage";
    private static String mea_errorCount = "errorStageCount";
    private static String mea_totalCount = "totalStageCount";
    private Context mContext = null;

    public void init() {
        this.mContext = AliHaAdapter.getInstance().context;
        registMonitorStage();
    }

    private void registMonitorStage() {
        try {
            DimensionSet create = DimensionSet.create();
            create.addDimension(dim_stage);
            MeasureSet create2 = MeasureSet.create();
            create2.addMeasure(mea_totalCount);
            create2.addMeasure(mea_errorCount);
            AppMonitor.register(PAGE, MONITOR_POINTER, create2, create, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stageInfo(String str, String str2, String str3) {
        try {
            AliHaAdapter.getInstance().tLogService.logi(TLOG_MODEL, str, buildInfo(str2, str3));
            Log.w(TAG, str + ":" + str2 + ":" + str3);
            if (!str.equals(TLogStage.MSG_SEND)) {
                if (!str.equals(TLogStage.MSG_LOG_UPLOAD)) {
                    if (!str.equals(TLogStage.MSG_REVEIVE)) {
                        str = null;
                    }
                }
            }
            if (str != null) {
                DimensionValueSet create = DimensionValueSet.create();
                create.setValue(dim_stage, str);
                MeasureValueSet create2 = MeasureValueSet.create();
                create2.setValue(mea_totalCount, 1.0d);
                create2.setValue(mea_errorCount, 0.0d);
                AppMonitor.Stat.commit(PAGE, MONITOR_POINTER, create, create2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stageError(String str, String str2, String str3) {
        try {
            AliHaAdapter.getInstance().tLogService.logw(TLOG_MODEL, str, buildInfo(str2, str3));
            String str4 = TAG;
            Log.e(str4, str + ":" + str2 + ":" + str3);
            BizErrorInfo bizErrorInfo = new BizErrorInfo();
            bizErrorInfo.aggregationType = AggregationType.CONTENT;
            bizErrorInfo.businessType = BIZ_ERROR_TYPE;
            bizErrorInfo.exceptionCode = str;
            bizErrorInfo.exceptionId = str2;
            bizErrorInfo.exceptionDetail = str3;
            bizErrorInfo.exceptionVersion = "1.0.0.0";
            AliHaAdapter.getInstance().bizErrorService.sendBizError(this.mContext, bizErrorInfo);
            monitorStageError(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stageError(String str, String str2, Throwable th) {
        try {
            AliHaAdapter.getInstance().tLogService.loge(TLOG_MODEL, str, th);
            Context context = AliHaAdapter.getInstance().context;
            String str3 = TAG;
            Log.e(str3, str + ":" + str2, th);
            BizErrorInfo bizErrorInfo = new BizErrorInfo();
            bizErrorInfo.aggregationType = AggregationType.STACK;
            bizErrorInfo.businessType = BIZ_ERROR_TYPE;
            bizErrorInfo.exceptionCode = str;
            bizErrorInfo.exceptionId = str2;
            bizErrorInfo.exceptionVersion = "1.0.0.0";
            bizErrorInfo.throwable = th;
            AliHaAdapter.getInstance().bizErrorService.sendBizError(context, bizErrorInfo);
            monitorStageError(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void monitorStageError(String str) {
        DimensionValueSet create = DimensionValueSet.create();
        create.setValue(dim_stage, str);
        MeasureValueSet create2 = MeasureValueSet.create();
        create2.setValue(mea_totalCount, 0.0d);
        create2.setValue(mea_errorCount, 1.0d);
        AppMonitor.Stat.commit(PAGE, MONITOR_POINTER, create, create2);
    }

    private String buildInfo(String str, String str2) {
        return str + " : " + str2;
    }
}
