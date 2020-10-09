package com.ali.telescope.internal.report;

import androidx.annotation.Keep;
import com.ali.telescope.base.report.IBeanReport;
import com.ali.telescope.base.report.IReportBean;

@Keep
public class BeanReportImpl implements IBeanReport {
    public static final String TAG = "BeanReport";

    public void send(IReportBean iReportBean) {
        ReportManager.getInstance().append(iReportBean);
    }
}
