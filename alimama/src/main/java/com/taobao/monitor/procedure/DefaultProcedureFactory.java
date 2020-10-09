package com.taobao.monitor.procedure;

/* compiled from: ProcedureFactoryProxy */
class DefaultProcedureFactory implements IProcedureFactory {
    DefaultProcedureFactory() {
    }

    public IProcedure createProcedure(String str) {
        return IProcedure.DEFAULT;
    }

    public IProcedure createProcedure(String str, ProcedureConfig procedureConfig) {
        return IProcedure.DEFAULT;
    }
}
