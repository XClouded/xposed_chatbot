package com.ali.telescope.internal.plugins.fdoverflow;

import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;
import org.json.JSONArray;
import org.json.JSONObject;

public class FdOverflowReportBean implements IReportErrorBean {
    public String body;
    public int count;
    public long time;

    public String getErrorType() {
        return ErrorConstants.HA_FD_OVERFLOW;
    }

    public Throwable getThrowable() {
        return null;
    }

    public FdOverflowReportBean(long j, String[] strArr) {
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        int i = 0;
        for (String str : strArr) {
            if (str != null) {
                jSONArray.put(str);
                i++;
            }
        }
        try {
            jSONObject.put("file_list", jSONArray);
            jSONObject.put("file_count", i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.time = j;
        this.body = jSONObject.toString();
        int length = strArr.length;
    }

    public String getBody() {
        return this.body;
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_FD_OVERFLOW;
    }

    public String getKey() {
        return this.count + "";
    }
}
