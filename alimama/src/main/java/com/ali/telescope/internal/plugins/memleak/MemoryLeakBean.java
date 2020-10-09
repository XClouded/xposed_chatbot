package com.ali.telescope.internal.plugins.memleak;

import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;
import org.json.JSONObject;

public class MemoryLeakBean implements IReportErrorBean {
    public String body;
    public String key = "";
    public long time;

    public String getErrorType() {
        return ErrorConstants.HA_MEM_LEAK;
    }

    public Throwable getThrowable() {
        return null;
    }

    public MemoryLeakBean(long j, String str, String str2) {
        this.time = j;
        this.key = str;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("leak_class", str);
            jSONObject.put("leak_trace", str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.body = jSONObject.toString();
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_MEMORY_LEAK;
    }

    public String getBody() {
        return this.body;
    }

    public String getKey() {
        return this.key;
    }
}
