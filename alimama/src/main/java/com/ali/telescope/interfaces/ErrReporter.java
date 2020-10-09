package com.ali.telescope.interfaces;

import java.util.Map;

public class ErrReporter {
    public String aggregationType;
    public String arg1;
    public String arg2;
    public String arg3;
    public Map<String, String> args;
    public String errorAggregationCode;
    public String errorDetail;
    public String errorId;
    public String errorType;
    public Thread thread;
    public Throwable throwable;
    public String version;
}
