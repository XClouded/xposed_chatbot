package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import com.ali.telescope.util.ByteUtils;
import java.util.Map;

public class OnlineBitmapStatic implements IReportRawByteBean {
    public Map<String, String> dimensionValues;
    public Map<String, Double> measureValues;
    public long time;

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_ONLINE_RESOURCE_BITMAP;
    }

    public byte[] getBody() {
        if (this.dimensionValues == null || this.measureValues == null) {
            return null;
        }
        return ByteUtils.merge(OnlineHelper.getDimension(this.dimensionValues.get("APILevel")), OnlineHelper.getDimension(this.dimensionValues.get("activityName")), OnlineHelper.getDimension(this.dimensionValues.get("Info")), OnlineHelper.getMeasure(this.measureValues.get("DeviceMem")), OnlineHelper.getMeasure(this.measureValues.get("DeviceTotalAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("DeviceAvailMem")), OnlineHelper.getMeasure(this.measureValues.get("TotalUsedMem")), OnlineHelper.getMeasure(this.measureValues.get("RemainMem")), OnlineHelper.getMeasure(this.measureValues.get("NativeHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("JavaHeapSize")), OnlineHelper.getMeasure(this.measureValues.get("DeviceScore")), OnlineHelper.getMeasure(this.measureValues.get("SysScore")), OnlineHelper.getMeasure(this.measureValues.get("PidScore")), OnlineHelper.getMeasure(this.measureValues.get("BitmapCount")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap565Count")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap8888Count")), OnlineHelper.getMeasure(this.measureValues.get("BitmapByte")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap1M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap2M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap4M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap6M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap8M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap10M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap12M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap15M")), OnlineHelper.getMeasure(this.measureValues.get("Bitmap20M")), OnlineHelper.getMeasure(this.measureValues.get("SizeScreen")), OnlineHelper.getMeasure(this.measureValues.get("Size2Screen")), OnlineHelper.getMeasure(this.measureValues.get("SizeHashScreen")), OnlineHelper.getMeasure(this.measureValues.get("Size14Screen")));
    }
}
