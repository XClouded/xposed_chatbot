package com.taobao.monitor.procedure;

public interface IProcedureGroup extends IProcedure {
    void addSubProcedure(IProcedure iProcedure);

    void removeSubProcedure(IProcedure iProcedure);
}
