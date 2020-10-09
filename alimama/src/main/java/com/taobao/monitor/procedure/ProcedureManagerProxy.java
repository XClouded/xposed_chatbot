package com.taobao.monitor.procedure;

public class ProcedureManagerProxy implements IProcedureManager {
    public static ProcedureManagerProxy PROXY = new ProcedureManagerProxy();
    private IProcedureManager real = new DefaultProcedureManager();

    private ProcedureManagerProxy() {
    }

    public ProcedureManagerProxy setReal(IProcedureManager iProcedureManager) {
        this.real = iProcedureManager;
        return this;
    }

    public IProcedure getCurrentActivityProcedure() {
        return this.real.getCurrentActivityProcedure();
    }

    public IProcedure getCurrentFragmentProcedure() {
        return this.real.getCurrentFragmentProcedure();
    }

    public IProcedure getCurrentProcedure() {
        return this.real.getCurrentProcedure();
    }

    public IProcedure getRootProcedure() {
        return this.real.getRootProcedure();
    }

    public IProcedure getLauncherProcedure() {
        return this.real.getLauncherProcedure();
    }
}
