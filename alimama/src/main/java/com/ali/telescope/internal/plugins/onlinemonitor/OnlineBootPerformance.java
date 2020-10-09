package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineBootPerformance implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_BOOT_PERFORMANCE;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("MemoryLevel")), OnlineHelper.getDimension(this.dimensionValues.get("BootType")), OnlineHelper.getDimension(this.dimensionValues.get("InBootStep")), OnlineHelper.getDimension(this.dimensionValues.get("TaskName")), OnlineHelper.getMeasure(this.measureValues.get("DeviceMem")), OnlineHelper.getMeasure(this.measureValues.get("DeviceAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("TotalUsedMem")), OnlineHelper.getMeasure(this.measureValues.get("RemainMem")), OnlineHelper.getMeasure(this.measureValues.get("NativeHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("JavaHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("SysCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("PidCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("SysLoadAvg")), OnlineHelper.getMeasure(this.measureValues.get("ThreadCount")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("RunningProgress")), OnlineHelper.getMeasure(this.measureValues.get("RunningService")), OnlineHelper.getMeasure(this.measureValues.get("PercentInBoot")), OnlineHelper.getMeasure(this.measureValues.get("PercentInPid")), OnlineHelper.getMeasure(this.measureValues.get("PercentInSystem")), OnlineHelper.getMeasure(this.measureValues.get("PercentInDevice")), OnlineHelper.getMeasure(this.measureValues.get("TaskUsedTime")), OnlineHelper.getMeasure(this.measureValues.get("TaskCpuTime")));
    }
}
