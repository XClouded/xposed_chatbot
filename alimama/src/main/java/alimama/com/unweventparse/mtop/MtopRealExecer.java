package alimama.com.unweventparse.mtop;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IEvent;
import alimama.com.unwbase.net.ApiInfo;
import alimama.com.unwbase.net.RxMtopRequest;
import alimama.com.unwbase.net.RxMtopResponse;
import alimama.com.unwbase.tools.ConvertUtils;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unweventparse.constants.EventConstants;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;

public class MtopRealExecer extends RxMtopRequest<MtopResponse> implements RxMtopRequest.RxMtopResult<MtopResponse> {
    private static final String TAG = "_MtopExecer";
    public UNWEventTaskCompletionBlock callback;
    private String eventId;
    public EventInfo mEventInfo;
    private String parseKey = "";
    public Map<String, String> reqParams;

    public void result(RxMtopResponse<MtopResponse> rxMtopResponse) {
        IEvent iEvent;
        UNWLog.error(TAG, "handleCallBack");
        if (rxMtopResponse != null) {
            if (rxMtopResponse.result != null) {
                handleCallBack(((MtopResponse) rxMtopResponse.result).keyResult, rxMtopResponse.retMsg);
            } else {
                handleCallBack(new JSONObject(), rxMtopResponse.retMsg);
            }
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            logger.info(TAG, TAG, "msg:" + rxMtopResponse.retMsg);
        } else {
            UNWManager.getInstance().getLogger().info(TAG, TAG, "msg:response is null");
            handleCallBack(new JSONObject(), "response is null");
        }
        if (rxMtopResponse != null && rxMtopResponse.result != null && ((MtopResponse) rxMtopResponse.result).keyResult != null && ((MtopResponse) rxMtopResponse.result).isInteralParse) {
            if (((MtopResponse) rxMtopResponse.result).keyResult instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) ((MtopResponse) rxMtopResponse.result).keyResult;
                IEvent iEvent2 = (IEvent) UNWManager.getInstance().getService(IEvent.class);
                if (iEvent2 != null) {
                    iEvent2.parseJson(jSONObject);
                }
            } else if (((MtopResponse) rxMtopResponse.result).keyResult instanceof JSONArray) {
                Iterator<Object> it = ((JSONArray) ((MtopResponse) rxMtopResponse.result).keyResult).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if ((next instanceof JSONObject) && (iEvent = (IEvent) UNWManager.getInstance().getService(IEvent.class)) != null) {
                        iEvent.parseJson((JSONObject) next);
                    }
                }
            }
        }
    }

    public class EventInfo {
        public String api;
        public boolean needEcode;
        public boolean needSession;
        public String version = "1.0";

        public EventInfo() {
        }
    }

    public MtopRealExecer(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        this.callback = uNWEventTaskCompletionBlock;
        this.eventId = jSONObject.getString(EventConstants.EVENTID);
    }

    public MtopResponse decodeResult(JSONObject jSONObject) {
        UNWLog.error(TAG, "decodeResult=" + jSONObject);
        if (jSONObject == null) {
            return null;
        }
        JSONObject parseObject = JSONObject.parseObject(jSONObject.toString());
        if (TextUtils.isEmpty(this.parseKey)) {
            return new MtopResponse(parseObject, (String) null, false);
        }
        return new MtopResponse(parseObject.getJSONObject("data"), this.parseKey, true);
    }

    public void constructInfo(JSONObject jSONObject) {
        boolean z;
        JSONObject jSONObject2 = jSONObject.getJSONObject("params");
        if (jSONObject2 != null) {
            jSONObject = jSONObject2;
            z = false;
        } else if (jSONObject.containsKey("api")) {
            z = true;
        } else {
            return;
        }
        this.mEventInfo = new EventInfo();
        this.mEventInfo.api = jSONObject.getString("api");
        if (!TextUtils.isEmpty(jSONObject.getString("version"))) {
            this.mEventInfo.version = jSONObject.getString("version");
        } else if (!TextUtils.isEmpty(jSONObject.getString("v"))) {
            this.mEventInfo.version = jSONObject.getString("v");
        } else {
            this.mEventInfo.version = "1.0";
        }
        this.mEventInfo.needEcode = ConvertUtils.str2boolean(jSONObject.getString(EventConstants.Mtop.NEED_ECODE));
        this.mEventInfo.needSession = ConvertUtils.str2boolean(jSONObject.getString(EventConstants.Mtop.NEED_SESSION));
        String string = jSONObject.getString(EventConstants.Mtop.RETRYCOUNT);
        if (!TextUtils.isEmpty(string)) {
            setRetryTimes(ConvertUtils.getSafeIntValue(string));
        }
        if (z) {
            JSONObject jSONObject3 = jSONObject.getJSONObject("data");
            if (jSONObject3 != null) {
                try {
                    this.reqParams = ConvertUtils.json2map(new org.json.JSONObject(jSONObject3.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.parseKey = jSONObject.getString(EventConstants.Mtop.PARSEKEY);
            try {
                this.reqParams = ConvertUtils.json2map(new org.json.JSONObject(jSONObject.toString()));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void readyToSend() {
        setApiInfo(new ApiInfo(this.mEventInfo.api, this.mEventInfo.version, this.mEventInfo.needEcode, this.mEventInfo.needSession));
        this.reqParams.remove(EventConstants.Mtop.PARSEKEY);
        if (this.reqParams != null && !this.reqParams.isEmpty()) {
            appendParam(this.reqParams);
        }
        sendRequest(this);
        UNWLog.error(TAG, "send=" + getParams());
    }

    public void handleCallBack(Object obj, String str) {
        if (this.callback != null) {
            try {
                this.callback.onEventTaskCompletion(this.eventId, obj, str);
            } catch (Throwable th) {
                UNWLog.error(th.toString());
            }
        }
    }
}
