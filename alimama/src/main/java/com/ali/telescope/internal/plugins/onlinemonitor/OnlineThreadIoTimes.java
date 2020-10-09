package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineThreadIoTimes implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_THREAD_IO_TIMES;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("Thread")), OnlineHelper.getDimension(this.dimensionValues.get("Info")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("RWTimes")), OnlineHelper.getMeasure(this.measureValues.get("RTimes")), OnlineHelper.getMeasure(this.measureValues.get("WTimes")), OnlineHelper.getMeasure(this.measureValues.get("NetTimes")), OnlineHelper.getMeasure(this.measureValues.get("nice")), OnlineHelper.getMeasure(this.measureValues.get("ioWaitCount")), OnlineHelper.getMeasure(this.measureValues.get("ioWaitTime")));
    }
}
