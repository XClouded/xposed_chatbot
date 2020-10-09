package alimama.com.unweventparse.mtop;

import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unweventparse.model.BaseEventModel;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;

public class MtopEventModel extends BaseEventModel {
    public EventInfo mEventInfo;
    public String mEventKey;
    public String mNextEventId;
    public HashMap<String, String> otherParams;

    public class EventInfo {
        public String api;
        public boolean needEcode;
        public boolean needSession;
        public JSONObject params;
        public String version;

        public EventInfo() {
        }
    }

    public MtopEventModel(JSONObject jSONObject) {
        this.mEventId = jSONObject.getString(EventConstants.EVENT_ID);
        JSONObject jSONObject2 = jSONObject.getJSONObject("event");
        if (jSONObject2 != null) {
            this.mEventSchema = jSONObject2.getString(EventConstants.EVENT_SCHEME);
            this.mEventType = jSONObject2.getString("event_type");
            JSONObject jSONObject3 = jSONObject2.getJSONObject(EventConstants.EVENT_INFO);
            if (jSONObject3 != null) {
                JSONObject jSONObject4 = jSONObject3.getJSONObject(EventConstants.Mtop.HEADERS);
                EventInfo eventInfo = new EventInfo();
                if (jSONObject4 != null) {
                    eventInfo.api = jSONObject4.getString("api");
                    if (TextUtils.isEmpty(jSONObject4.getString("version"))) {
                        eventInfo.version = "1.0";
                    } else {
                        eventInfo.version = jSONObject4.getString("version");
                    }
                    if (jSONObject4.getBoolean(EventConstants.Mtop.NEED_ECODE) == null) {
                        eventInfo.needEcode = false;
                    } else {
                        eventInfo.needEcode = jSONObject4.getBoolean(EventConstants.Mtop.NEED_ECODE).booleanValue();
                    }
                    if (jSONObject4.getBoolean(EventConstants.Mtop.NEED_SESSION) == null) {
                        eventInfo.needSession = true;
                    } else {
                        eventInfo.needSession = jSONObject4.getBoolean(EventConstants.Mtop.NEED_SESSION).booleanValue();
                    }
                }
                eventInfo.params = jSONObject3.getJSONObject("params");
                this.mEventInfo = eventInfo;
            }
            JSONObject jSONObject5 = jSONObject.getJSONObject(EventConstants.NEXT_EVENTS);
            if (jSONObject5 != null) {
                this.mEventKey = jSONObject5.getJSONObject(EventConstants.Mtop.SUCCESS_HANDLER).getString(EventConstants.Mtop.EVENT_KEY);
                this.mNextEventId = jSONObject5.getJSONObject(EventConstants.Mtop.ERROR_HANDLER).getString(EventConstants.EVENT_ID);
            }
        }
    }
}
