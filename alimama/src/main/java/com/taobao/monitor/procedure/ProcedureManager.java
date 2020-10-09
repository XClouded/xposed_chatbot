package com.taobao.monitor.procedure;

import com.taobao.monitor.annotation.UnsafeMethod;

public class ProcedureManager implements IProcedureManager {
    private volatile IProcedure activityProcedure;
    private volatile IProcedure fragmentProcedure;
    private volatile IProcedure launcherProcedure = IProcedure.DEFAULT;
    private final IProcedure root = IProcedure.DEFAULT;

    @UnsafeMethod
    public IProcedure setCurrentActivityProcedure(IProcedure iProcedure) {
        this.activityProcedure = iProcedure;
        return iProcedure;
    }

    public IProcedure getCurrentActivityProcedure() {
        return this.activityProcedure;
    }

    @UnsafeMethod
    public IProcedure setCurrentFragmentProcedure(IProcedure iProcedure) {
        this.fragmentProcedure = iProcedure;
        return iProcedure;
    }

    public IProcedure getCurrentFragmentProcedure() {
        return this.fragmentProcedure;
    }

    public IProcedure getCurrentProcedure() {
        if (this.launcherProcedure != null && this.launcherProcedure.isAlive()) {
            return this.launcherProcedure;
        }
        if (this.activityProcedure != null) {
            return this.activityProcedure;
        }
        if (this.fragmentProcedure != null) {
            return this.fragmentProcedure;
        }
        return this.root;
    }

    public IProcedure getRootProcedure() {
        return this.root;
    }

    @UnsafeMethod
    public IProcedure getLauncherProcedure() {
        return this.launcherProcedure;
    }

    public IProcedure setLauncherProcedure(IProcedure iProcedure) {
        if (iProcedure == null) {
            this.launcherProcedure = IProcedure.DEFAULT;
        } else {
            this.launcherProcedure = iProcedure;
        }
        return this.launcherProcedure;
    }
}
