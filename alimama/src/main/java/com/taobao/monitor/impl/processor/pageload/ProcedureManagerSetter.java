package com.taobao.monitor.impl.processor.pageload;

import com.taobao.monitor.procedure.IProcedure;

public class ProcedureManagerSetter implements IProcedureManager {
    private IProcedureManager proxy;

    private ProcedureManagerSetter() {
        this.proxy = new Default();
    }

    public static ProcedureManagerSetter instance() {
        return Holder.INSTANCE;
    }

    public ProcedureManagerSetter setProxy(IProcedureManager iProcedureManager) {
        this.proxy = iProcedureManager;
        return this;
    }

    public void setCurrentActivityProcedure(IProcedure iProcedure) {
        this.proxy.setCurrentActivityProcedure(iProcedure);
    }

    public void setCurrentFragmentProcedure(IProcedure iProcedure) {
        this.proxy.setCurrentFragmentProcedure(iProcedure);
    }

    public void setCurrentLauncherProcedure(IProcedure iProcedure) {
        this.proxy.setCurrentLauncherProcedure(iProcedure);
    }

    private static class Holder {
        static final ProcedureManagerSetter INSTANCE = new ProcedureManagerSetter();

        private Holder() {
        }
    }

    private static class Default implements IProcedureManager {
        public void setCurrentActivityProcedure(IProcedure iProcedure) {
        }

        public void setCurrentFragmentProcedure(IProcedure iProcedure) {
        }

        public void setCurrentLauncherProcedure(IProcedure iProcedure) {
        }

        private Default() {
        }
    }
}
