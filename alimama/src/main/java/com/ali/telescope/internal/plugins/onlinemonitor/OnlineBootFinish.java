package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineBootFinish implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_BOOT_FINISH;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        OnlineHelper.getDimension(this.dimensionValues.get(""));
        OnlineHelper.getMeasure(this.measureValues.get(""));
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("isFirstInstall")), OnlineHelper.getDimension(this.dimensionValues.get("CpuCore")), OnlineHelper.getDimension(this.dimensionValues.get("APILevel")), OnlineHelper.getDimension(this.dimensionValues.get("IsLowMemory")), OnlineHelper.getDimension(this.dimensionValues.get("MemoryLevel")), OnlineHelper.getDimension(this.dimensionValues.get("BootType")), OnlineHelper.getDimension(this.dimensionValues.get("Info")), OnlineHelper.getMeasure(this.measureValues.get("BootTotalTime")), OnlineHelper.getMeasure(this.measureValues.get("loadTime")), OnlineHelper.getMeasure(this.measureValues.get("BlockingGCCount")), OnlineHelper.getMeasure(this.measureValues.get("CpuMaxFreq")), OnlineHelper.getMeasure(this.measureValues.get("DeviceMem")), OnlineHelper.getMeasure(this.measureValues.get("DeviceAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("TotalUsedMem")), OnlineHelper.getMeasure(this.measureValues.get("RemainMem")), OnlineHelper.getMeasure(this.measureValues.get("NativeHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("JavaHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("SysCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("PidCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("IOWaitTime")), OnlineHelper.getMeasure(this.measureValues.get("SysLoadAvg")), OnlineHelper.getMeasure(this.measureValues.get("RuntimeThread")), OnlineHelper.getMeasure(this.measureValues.get("RunningThread")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("RunningProgress")), OnlineHelper.getMeasure(this.measureValues.get("RunningService")), OnlineHelper.getMeasure(this.measureValues.get("PidPrepareTime")), OnlineHelper.getMeasure(this.measureValues.get("AdvTime")));
    }
}
