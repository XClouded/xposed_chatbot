package com.ali.telescope.internal.report;

import android.content.Context;
import com.ali.telescope.interfaces.ErrReporter;
import com.ali.telescope.interfaces.TelescopeErrReporter;
import com.alibaba.ha.bizerrorreporter.BizErrorReporter;
import com.alibaba.ha.bizerrorreporter.module.AggregationType;
import com.alibaba.ha.bizerrorreporter.module.BizErrorModule;
import java.util.HashMap;
import java.util.Map;

public class ErrReporterListener implements TelescopeErrReporter {
    public void report(Context context, ErrReporter errReporter) {
        try {
            BizErrorModule bizErrorModule = new BizErrorModule();
            bizErrorModule.businessType = errReporter.errorType;
            bizErrorModule.aggregationType = AggregationType.valueOf(errReporter.aggregationType);
            bizErrorModule.exceptionCode = errReporter.errorAggregationCode;
            bizErrorModule.exceptionId = errReporter.errorId;
            bizErrorModule.exceptionDetail = errReporter.errorDetail;
            bizErrorModule.throwable = errReporter.throwable;
            bizErrorModule.thread = errReporter.thread;
            bizErrorModule.exceptionVersion = errReporter.version;
            bizErrorModule.exceptionArg1 = errReporter.arg1;
            bizErrorModule.exceptionArg2 = errReporter.arg2;
            bizErrorModule.exceptionArg3 = errReporter.arg3;
            if (errReporter.args != null) {
                HashMap hashMap = new HashMap();
                for (Map.Entry next : errReporter.args.entrySet()) {
                    bizErrorModule.exceptionArgs.put(next.getKey(), next.getValue());
                }
                bizErrorModule.exceptionArgs = hashMap;
            }
            BizErrorReporter.getInstance().send(context, bizErrorModule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
