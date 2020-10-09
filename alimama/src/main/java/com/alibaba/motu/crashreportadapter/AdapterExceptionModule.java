package com.alibaba.motu.crashreportadapter;

import com.alibaba.motu.crashreportadapter.module.AdapterBaseModule;
import java.util.Map;

public class AdapterExceptionModule extends AdapterBaseModule {
    public String exceptionArg1;
    public String exceptionArg2;
    public String exceptionArg3;
    public Map<String, Object> exceptionArgs;
    public String exceptionCode;
    public String exceptionDetail;
    public String exceptionId;
    public String exceptionVersion;
    public Thread thread;
    public Throwable throwable;
}
