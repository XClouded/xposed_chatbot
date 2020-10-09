package com.alibaba.android.umbrella.performance;

public class UmbrellaProcess {
    public static final UmbrellaProcess DATAPARSE = new UmbrellaProcess(4, "dataParse");
    public static final UmbrellaProcess DRAWVIEW = new UmbrellaProcess(5, "drawView");
    public static final UmbrellaProcess INIT = new UmbrellaProcess(1, "init");
    public static final UmbrellaProcess LIFECYCLE = new UmbrellaProcess(2, "lifeCycle");
    public static final UmbrellaProcess NETWORK = new UmbrellaProcess(3, "netWork");
    public static final UmbrellaProcess PAGELOAD = new UmbrellaProcess(6, "pageLoad");
    public static final UmbrellaProcess SUB_BIND_VIEW = new UmbrellaProcess(8, "bindData");
    public static final UmbrellaProcess SUB_CREATE_VIEW = new UmbrellaProcess(7, "createView");
    private String name;
    private int type;

    UmbrellaProcess(int i, String str) {
        this.type = i;
        this.name = str;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
