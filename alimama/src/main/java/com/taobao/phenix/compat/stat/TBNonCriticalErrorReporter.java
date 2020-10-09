package com.taobao.phenix.compat.stat;

import android.content.Context;
import com.alibaba.motu.crashreportadapter.AdapterExceptionModule;
import com.alibaba.motu.crashreportadapter.MotuReportAdapteHandler;
import com.alibaba.motu.crashreportadapter.module.AggregationType;
import com.alibaba.motu.crashreportadapter.module.BusinessType;
import java.util.Map;

public class TBNonCriticalErrorReporter implements NonCriticalErrorReporter {
    private final Context mContext;
    private final MotuReportAdapteHandler mReportAdaptHandler = new MotuReportAdapteHandler();

    public TBNonCriticalErrorReporter(Context context) {
        this.mContext = context;
    }

    public void onNonCriticalErrorHappen(String str, Throwable th, Map<String, Object> map) {
        if (th != null) {
            AdapterExceptionModule adapterExceptionModule = new AdapterExceptionModule();
            adapterExceptionModule.aggregationType = AggregationType.CONTENT;
            adapterExceptionModule.businessType = BusinessType.IMAGE_ERROR;
            adapterExceptionModule.exceptionCode = str;
            adapterExceptionModule.exceptionArgs = map;
            adapterExceptionModule.throwable = th;
            this.mReportAdaptHandler.adapter(this.mContext, adapterExceptionModule);
        }
    }
}
