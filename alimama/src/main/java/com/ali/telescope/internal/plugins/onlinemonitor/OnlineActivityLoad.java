package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import java.util.Map;

public class OnlineActivityLoad implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_ACTIVITY_LOAD;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("activityName")), OnlineHelper.getDimension(this.dimensionValues.get("CpuCore")), OnlineHelper.getDimension(this.dimensionValues.get("APILevel")), OnlineHelper.getDimension(this.dimensionValues.get("IsLowMemroy")), OnlineHelper.getDimension(this.dimensionValues.get("MemoryLevel")), OnlineHelper.getDimension(this.dimensionValues.get(UmbrellaConstants.LIFECYCLE_CREATE)), OnlineHelper.getDimension(this.dimensionValues.get("firstCreate")), OnlineHelper.getDimension(this.dimensionValues.get("isHotLauncher")), OnlineHelper.getDimension(this.dimensionValues.get("Info")), OnlineHelper.getMeasure(this.measureValues.get("StayTime")), OnlineHelper.getMeasure(this.measureValues.get("JankTime")), OnlineHelper.getMeasure(this.measureValues.get("IdleTime")), OnlineHelper.getMeasure(this.measureValues.get("FrameTime")), OnlineHelper.getMeasure(this.measureValues.get("JankCount")), OnlineHelper.getMeasure(this.measureValues.get("FrameCount")), OnlineHelper.getMeasure(this.measureValues.get("DeviceMem")), OnlineHelper.getMeasure(this.measureValues.get("BadCountOne")), OnlineHelper.getMeasure(this.measureValues.get("BadCountTwo")), OnlineHelper.getMeasure(this.measureValues.get("BadCountThree")), OnlineHelper.getMeasure(this.measureValues.get("BadCountFour")), OnlineHelper.getMeasure(this.measureValues.get("BadCountFive")), OnlineHelper.getMeasure(this.measureValues.get("BadCountSix")), OnlineHelper.getMeasure(this.measureValues.get("BadCountSeven")), OnlineHelper.getMeasure(this.measureValues.get("BadCountEight")), OnlineHelper.getMeasure(this.measureValues.get("BadCountNine")), OnlineHelper.getMeasure(this.measureValues.get("BadCountTen")), OnlineHelper.getMeasure(this.measureValues.get("BadCountEleven")), OnlineHelper.getMeasure(this.measureValues.get("BadCountTwelve")), OnlineHelper.getMeasure(this.measureValues.get("loadTime")), OnlineHelper.getMeasure(this.measureValues.get("EnterIdleTime")), OnlineHelper.getMeasure(this.measureValues.get("CpuMaxFreq")), OnlineHelper.getMeasure(this.measureValues.get("DeviceAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("TotalUsedMem")), OnlineHelper.getMeasure(this.measureValues.get("RemainMem")), OnlineHelper.getMeasure(this.measureValues.get("NativeHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("JavaHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("SysCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("PidCpuPercent")), OnlineHelper.getMeasure(this.measureValues.get("SysLoadAvg")), OnlineHelper.getMeasure(this.measureValues.get("RuntimeThread")), OnlineHelper.getMeasure(this.measureValues.get("RunningThread")), OnlineHelper.getMeasure(this.measureValues.get("ActivityScore")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("RunningProgress")), OnlineHelper.getMeasure(this.measureValues.get("RunningService")), OnlineHelper.getMeasure(this.measureValues.get("StartActivityTime")), OnlineHelper.getMeasure(this.measureValues.get("LoadSmUsedTime")), OnlineHelper.getMeasure(this.measureValues.get("LoadSmCount")), OnlineHelper.getMeasure(this.measureValues.get("LoadBadSmCount")), OnlineHelper.getMeasure(this.measureValues.get("LoadBadSmUsedTime")), OnlineHelper.getMeasure(this.measureValues.get("OpenFileCount")), OnlineHelper.getMeasure(this.measureValues.get("TotalTx")), OnlineHelper.getMeasure(this.measureValues.get("TotalRx")));
    }
}
