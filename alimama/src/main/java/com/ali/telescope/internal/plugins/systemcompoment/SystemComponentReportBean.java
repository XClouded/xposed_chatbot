package com.ali.telescope.internal.plugins.systemcompoment;

import com.ali.telescope.base.report.IReportStringBean;
import com.ali.telescope.internal.report.ProtocolConstants;
import org.json.JSONObject;

public class SystemComponentReportBean implements IReportStringBean {
    private String body;
    private JSONObject debugUseObject;
    private long time;

    public SystemComponentReportBean(long j, String str, int i, int i2, JSONObject jSONObject, int i3) {
        int i4;
        this.time = j;
        if (i == MessageConstants.LAUNCH_ACTIVITY) {
            i4 = 1;
        } else if (i == MessageConstants.RESUME_ACTIVITY) {
            i4 = 2;
        } else if (i == MessageConstants.PAUSE_ACTIVITY) {
            i4 = 3;
        } else if (i == MessageConstants.PAUSE_ACTIVITY_FINISHING) {
            i4 = 4;
        } else if (i == MessageConstants.STOP_ACTIVITY_SHOW) {
            i4 = 5;
        } else if (i == MessageConstants.STOP_ACTIVITY_HIDE) {
            i4 = 6;
        } else if (i == MessageConstants.DESTROY_ACTIVITY) {
            i4 = 7;
        } else if (i == MessageConstants.RECEIVER || i == MessageConstants.DYNAMICAL_RECEIVER) {
            i4 = 8;
        } else if (i == MessageConstants.CREATE_SERVICE) {
            i4 = 9;
        } else if (i == MessageConstants.SERVICE_ARGS) {
            i4 = 10;
        } else if (i == MessageConstants.BIND_SERVICE) {
            i4 = 11;
        } else if (i == MessageConstants.UNBIND_SERVICE) {
            i4 = 12;
        } else {
            i4 = i == MessageConstants.STOP_SERVICE ? 13 : i == MessageConstants.CALL_BACK_SERVICE_CONNECTION ? 14 : 0;
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("time", j);
            jSONObject2.put("class_name", str);
            jSONObject2.put("call_type", i4);
            jSONObject2.put("time_cost", i2);
            if (i3 != 0) {
                jSONObject2.put("sample_times", i3);
            }
            if (jSONObject != null) {
                jSONObject2.put("method_trace", jSONObject);
            }
        } catch (Exception unused) {
        }
        this.debugUseObject = jSONObject2;
        this.body = jSONObject2.toString();
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_COMPONENT_TRACE;
    }

    public String getBody() {
        return this.body;
    }

    public JSONObject getDebugUseObject() {
        return this.debugUseObject;
    }
}
