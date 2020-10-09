package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineBlockOrCloseGuard implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_BLOCK_OR_CLOSE_GUARD;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("activityName")), OnlineHelper.getDimension(this.dimensionValues.get("threadName")), OnlineHelper.getDimension(this.dimensionValues.get("typeString")), OnlineHelper.getDimension(this.dimensionValues.get("stacks")), OnlineHelper.getMeasure(this.measureValues.get("type")), OnlineHelper.getMeasure(this.measureValues.get("size")));
    }
}
