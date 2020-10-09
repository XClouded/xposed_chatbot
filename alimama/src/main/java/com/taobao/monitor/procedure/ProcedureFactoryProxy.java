package com.taobao.monitor.procedure;

public class ProcedureFactoryProxy implements IProcedureFactory {
    public static ProcedureFactoryProxy PROXY = new ProcedureFactoryProxy();
    private IProcedureFactory real = new DefaultProcedureFactory();

    private ProcedureFactoryProxy() {
    }

    public ProcedureFactoryProxy setReal(IProcedureFactory iProcedureFactory) {
        this.real = iProcedureFactory;
        return this;
    }

    public IProcedure createProcedure(String str) {
        return this.real.createProcedure(str);
    }

    public IProcedure createProcedure(String str, ProcedureConfig procedureConfig) {
        return this.real.createProcedure(str, procedureConfig);
    }
}
