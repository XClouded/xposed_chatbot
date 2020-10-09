package com.taobao.monitor.procedure;

public interface IProcedureManager {
    IProcedure getCurrentActivityProcedure();

    IProcedure getCurrentFragmentProcedure();

    IProcedure getCurrentProcedure();

    IProcedure getLauncherProcedure();

    IProcedure getRootProcedure();
}
