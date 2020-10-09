package com.ali.telescope.internal.plugins.bitmap;

import alimama.com.unweventparse.constants.EventConstants;
import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;
import org.json.JSONObject;

public class MemoryBitmapBean implements IReportErrorBean {
    public String body;
    public Throwable mThrowable;
    public long time;

    public String getErrorType() {
        return ErrorConstants.HA_BIG_BITMAP;
    }

    public String getKey() {
        return null;
    }

    public MemoryBitmapBean(long j, String str, int i, Throwable th, boolean z) {
        this.time = j;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("time", j);
            jSONObject.put("size", i);
            jSONObject.put(EventConstants.UT.PAGE_NAME, str);
            jSONObject.put("decode_on_main", z);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.body = jSONObject.toString();
        this.mThrowable = th;
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_BITMAP_MEMORY;
    }

    public String getBody() {
        return this.body;
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }
}
