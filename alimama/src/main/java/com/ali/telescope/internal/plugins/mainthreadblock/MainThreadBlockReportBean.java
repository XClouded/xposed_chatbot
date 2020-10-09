package com.ali.telescope.internal.plugins.mainthreadblock;

import android.text.TextUtils;
import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;
import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

public class MainThreadBlockReportBean implements IReportErrorBean {
    private String body;
    private String key = "";
    private long time;

    public String getErrorType() {
        return ErrorConstants.HA_MAIN_THREAD_BLOCK;
    }

    public Throwable getThrowable() {
        return null;
    }

    public MainThreadBlockReportBean(long j, String str, int i, JSONObject jSONObject, int i2) {
        this.time = j;
        if (str != null) {
            this.key = str;
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            if (TextUtils.isEmpty(str)) {
                jSONObject2.put("page", str);
            }
            jSONObject2.put("blockTime", i);
            if (jSONObject != null) {
                jSONObject2.put(AgooConstants.MESSAGE_TRACE, jSONObject);
                jSONObject2.put("sample_times", i2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.body = jSONObject2.toString();
    }

    public short getType() {
        return ProtocolConstants.EVENT_MAINTHREAD_BLOCK;
    }

    public long getTime() {
        return this.time;
    }

    public String getBody() {
        return this.body;
    }

    public String getKey() {
        return this.key;
    }
}
