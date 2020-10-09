package com.taobao.monitor.impl.processor.pageload;

import com.taobao.monitor.procedure.IProcedure;

public interface IProcedureManager {
    void setCurrentActivityProcedure(IProcedure iProcedure);

    void setCurrentFragmentProcedure(IProcedure iProcedure);

    void setCurrentLauncherProcedure(IProcedure iProcedure);
}
