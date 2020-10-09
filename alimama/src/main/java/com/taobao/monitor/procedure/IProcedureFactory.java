package com.taobao.monitor.procedure;

public interface IProcedureFactory {
    IProcedure createProcedure(String str);

    IProcedure createProcedure(String str, ProcedureConfig procedureConfig);
}
