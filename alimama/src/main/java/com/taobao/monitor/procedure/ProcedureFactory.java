package com.taobao.monitor.procedure;

import com.taobao.monitor.ProcedureGlobal;
import com.taobao.monitor.network.ProcedureLifecycleImpl;
import com.taobao.monitor.procedure.ProcedureConfig;

public class ProcedureFactory implements IProcedureFactory {
    public IProcedure createProcedure(String str) {
        return createProcedure(str, new ProcedureConfig.Builder().setUpload(false).setIndependent(true).setParentNeedStats(true).setParent(ProcedureGlobal.PROCEDURE_MANAGER.getCurrentProcedure()).build());
    }

    public IProcedure createProcedure(String str, ProcedureConfig procedureConfig) {
        if (procedureConfig == null) {
            procedureConfig = new ProcedureConfig.Builder().setUpload(false).setIndependent(true).setParentNeedStats(true).setParent(ProcedureGlobal.PROCEDURE_MANAGER.getCurrentProcedure()).build();
        }
        return new ProcedureProxy(createProcedureImpl(str, procedureConfig));
    }

    private ProcedureImpl createProcedureImpl(String str, ProcedureConfig procedureConfig) {
        IProcedure parent = procedureConfig.getParent();
        if (parent == IProcedure.DEFAULT) {
            parent = ProcedureGlobal.PROCEDURE_MANAGER.getCurrentProcedure();
        }
        ProcedureImpl procedureImpl = new ProcedureImpl(str, parent, procedureConfig.isIndependent(), procedureConfig.isParentNeedStats());
        if (procedureConfig.isUpload()) {
            procedureImpl.setLifeCycle(new ProcedureLifecycleImpl());
        }
        return procedureImpl;
    }
}
