package com.ali.telescope.internal.plugins.threadio;

import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.internal.report.ErrorConstants;
import com.ali.telescope.internal.report.ProtocolConstants;
import org.json.JSONObject;

public class IOReportBean implements IReportErrorBean {
    public static final int SUB_TYPE_READ = 1;
    public static final int SUB_TYPE_SQL = 3;
    public static final int SUB_TYPE_WRITE = 2;
    private String body;
    private Throwable mThrowable;
    private long time;

    public String getErrorType() {
        return ErrorConstants.HA_MAIN_THREAD_IO;
    }

    public String getKey() {
        return null;
    }

    public IOReportBean(long j, int i, int i2, Throwable th) {
        this.time = j;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("io_time", i);
            String str = "";
            if (i2 == 1) {
                str = "io_read";
            } else if (i2 == 2) {
                str = "io_write";
            } else if (i2 == 3) {
                str = "io_sql";
            }
            jSONObject.put("io_type", str);
            this.body = jSONObject.toString();
            this.mThrowable = th;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getTime() {
        return this.time;
    }

    public short getType() {
        return ProtocolConstants.EVENT_MAINTHREAD_IO;
    }

    public String getBody() {
        return this.body;
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }
}
