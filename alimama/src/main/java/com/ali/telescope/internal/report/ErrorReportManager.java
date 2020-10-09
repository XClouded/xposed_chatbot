package com.ali.telescope.internal.report;

import alimama.com.unweventparse.constants.EventConstants;
import android.content.Context;
import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.interfaces.ErrReporter;
import com.ali.telescope.interfaces.TelescopeErrReporter;
import java.util.ArrayList;
import java.util.List;

public class ErrorReportManager {
    static Context sContext;
    private static List<TelescopeErrReporter> sErrorReporter;

    public static void addTelescopeErrorReporter(TelescopeErrReporter telescopeErrReporter) {
        if (sErrorReporter == null) {
            sErrorReporter = new ArrayList();
        }
        sErrorReporter.add(telescopeErrReporter);
    }

    public static void initContext(Context context) {
        sContext = context;
    }

    public static void adapter(IReportErrorBean iReportErrorBean) {
        if (sErrorReporter != null && sErrorReporter.size() > 0) {
            for (TelescopeErrReporter report : sErrorReporter) {
                try {
                    report(iReportErrorBean, report);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void report(IReportErrorBean iReportErrorBean, TelescopeErrReporter telescopeErrReporter) {
        ErrReporter errReporter = new ErrReporter();
        errReporter.errorType = iReportErrorBean.getErrorType();
        if (iReportErrorBean.getThrowable() != null) {
            errReporter.aggregationType = "STACK";
        } else {
            errReporter.aggregationType = "CONTENT";
        }
        errReporter.errorAggregationCode = iReportErrorBean.getKey();
        errReporter.errorId = iReportErrorBean.getErrorType() + "_" + iReportErrorBean.getTime();
        errReporter.errorDetail = iReportErrorBean.getBody();
        errReporter.throwable = iReportErrorBean.getThrowable();
        errReporter.thread = null;
        errReporter.version = "1.0.0.0";
        errReporter.arg1 = EventConstants.UT.ARG1;
        errReporter.arg2 = "arg2";
        errReporter.arg3 = "arg3";
        telescopeErrReporter.report(sContext, errReporter);
    }
}
