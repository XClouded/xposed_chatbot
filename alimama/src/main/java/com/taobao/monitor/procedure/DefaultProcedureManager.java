package com.taobao.monitor.procedure;

/* compiled from: ProcedureManagerProxy */
class DefaultProcedureManager implements IProcedureManager {
    DefaultProcedureManager() {
    }

    public IProcedure getCurrentActivityProcedure() {
        return IProcedure.DEFAULT;
    }

    public IProcedure getCurrentFragmentProcedure() {
        return IProcedure.DEFAULT;
    }

    public IProcedure getCurrentProcedure() {
        return IProcedure.DEFAULT;
    }

    public IProcedure getRootProcedure() {
        return IProcedure.DEFAULT;
    }

    public IProcedure getLauncherProcedure() {
        return IProcedure.DEFAULT;
    }
}
