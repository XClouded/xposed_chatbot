package com.xiaomi.clientreport.data;

import alimama.com.unweventparse.constants.EventConstants;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.xiaomi.channel.commonutils.logger.b;
import org.json.JSONException;
import org.json.JSONObject;

public class EventClientReport extends a {
    public String eventContent;
    public String eventId;
    public long eventTime;
    public int eventType;

    public static EventClientReport getBlankInstance() {
        return new EventClientReport();
    }

    public JSONObject toJson() {
        try {
            JSONObject json = super.toJson();
            if (json == null) {
                return null;
            }
            json.put(EventConstants.EVENTID, this.eventId);
            json.put(BindingXConstants.KEY_EVENT_TYPE, this.eventType);
            json.put("eventTime", this.eventTime);
            json.put("eventContent", this.eventContent);
            return json;
        } catch (JSONException e) {
            b.a((Throwable) e);
            return null;
        }
    }

    public String toJsonString() {
        return super.toJsonString();
    }
}
