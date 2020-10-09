package com.ali.telescope.base.report;

public interface IReportErrorBean extends IReportBean {
    String getBody();

    String getErrorType();

    String getKey();

    Throwable getThrowable();
}
