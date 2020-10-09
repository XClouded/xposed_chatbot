package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineThreadPoolProblem implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_THREAD_POOL_PROBLEM;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("activityName")), OnlineHelper.getDimension(this.dimensionValues.get("CpuCore")), OnlineHelper.getDimension(this.dimensionValues.get("APILevel")), OnlineHelper.getDimension(this.dimensionValues.get("IsLowMemroy")), OnlineHelper.getDimension(this.dimensionValues.get("MemoryLevel")), OnlineHelper.getDimension(this.dimensionValues.get("Info")), OnlineHelper.getDimension(this.dimensionValues.get("QueueThread")), OnlineHelper.getDimension(this.dimensionValues.get("PoolThread")), OnlineHelper.getDimension(this.dimensionValues.get("PoolThreadDetail")), OnlineHelper.getMeasure(this.measureValues.get("QueueSize")), OnlineHelper.getMeasure(this.measureValues.get("CoreSize")), OnlineHelper.getMeasure(this.measureValues.get("MaxSize")), OnlineHelper.getMeasure(this.measureValues.get("ActiveCount")), OnlineHelper.getMeasure(this.measureValues.get("CompletedCount")), OnlineHelper.getMeasure(this.measureValues.get("ThreadSize")), OnlineHelper.getMeasure(this.measureValues.get("DeviceMem")), OnlineHelper.getMeasure(this.measureValues.get("CpuMaxFreq")), OnlineHelper.getMeasure(this.measureValues.get("DeviceAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("DeviceTotalAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("TotalUsedMem")), OnlineHelper.getMeasure(this.measureValues.get("RemainMem")), OnlineHelper.getMeasure(this.measureValues.get("NativeHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("JavaHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("SysCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("PidCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("SysLoadAvg")), OnlineHelper.getMeasure(this.measureValues.get("RuntimeThread")), OnlineHelper.getMeasure(this.measureValues.get("RunningThread")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("RunningProgress")), OnlineHelper.getMeasure(this.measureValues.get("RunningService")));
    }
}
