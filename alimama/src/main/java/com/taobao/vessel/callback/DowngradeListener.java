package com.taobao.vessel.callback;

import com.taobao.vessel.utils.VesselType;

public interface DowngradeListener extends OnLoadListener {
    void beforeDowngrade(VesselType vesselType, VesselType vesselType2);
}
