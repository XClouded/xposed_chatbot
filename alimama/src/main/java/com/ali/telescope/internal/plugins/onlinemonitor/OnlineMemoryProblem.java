package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineMemoryProblem implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_MEMORY_PROBLEM;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("APILevel")), OnlineHelper.getDimension(this.dimensionValues.get("ActivityName")), OnlineHelper.getDimension(this.dimensionValues.get("Info")), OnlineHelper.getDimension(this.dimensionValues.get("MemoryLevel")), OnlineHelper.getDimension(this.dimensionValues.get("Activitys")), OnlineHelper.getDimension(this.dimensionValues.get("Threads")), OnlineHelper.getDimension(this.dimensionValues.get("MemoryType")), OnlineHelper.getMeasure(this.measureValues.get("DeviceMem")), OnlineHelper.getMeasure(this.measureValues.get("TotalUsedMem")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("RuntimeThread")), OnlineHelper.getMeasure(this.measureValues.get("RunningThread")), OnlineHelper.getMeasure(this.measureValues.get("OtherSo")), OnlineHelper.getMeasure(this.measureValues.get("OtherJar")), OnlineHelper.getMeasure(this.measureValues.get("OtherApk")), OnlineHelper.getMeasure(this.measureValues.get("OtherTtf")), OnlineHelper.getMeasure(this.measureValues.get("OtherDex")), OnlineHelper.getMeasure(this.measureValues.get("OtherOat")), OnlineHelper.getMeasure(this.measureValues.get("OtherArt")), OnlineHelper.getMeasure(this.measureValues.get("OtherMap")), OnlineHelper.getMeasure(this.measureValues.get("OtherAshmem")));
    }
}
