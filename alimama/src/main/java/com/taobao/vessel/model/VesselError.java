package com.taobao.vessel.model;

import com.taobao.vessel.utils.VesselType;

public class VesselError {
    public String errorCode;
    public String errorMsg;
    public VesselType type;

    public VesselError() {
    }

    public VesselError(String str, String str2, VesselType vesselType) {
        this.errorCode = str;
        this.errorMsg = str2;
        this.type = vesselType;
    }
}
