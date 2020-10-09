package com.taobao.vessel.utils;

public enum VesselType {
    Weex(1),
    Web(2),
    Native(3);
    
    private int type;

    private VesselType(int i) {
        this.type = i;
    }
}
