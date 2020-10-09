package alimama.com.unweventparse.model;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEvent;
import alimama.com.unwbase.interfaces.IUTAction;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unweventparse.interfaces.IResourceParse;
import alimama.com.unweventparse.model.BaseResourceData;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.android.agoo.common.AgooConstants;
import org.jetbrains.annotations.NotNull;

public abstract class BaseResourceData<T extends BaseResourceData> implements IResourceParse<T> {
    private static final String TAG = "_BaseResourceData";
    public final String HTTPS = "https";
    public final String MTOP = "mtop";
    public final String UT_TYPE_CLICK = "ut_click";
    public final String UT_TYPE_EXPOSE = "ut_expose";
    public ArrayList<JSONObject> httpsJsons = new ArrayList<>();
    public ArrayList<JSONObject> mtopJsons = new ArrayList<>();
    public String resType;
    public String spm;
    public String url;
    public ArrayList<BaseResourceData<T>.UTInfo> utInfos = new ArrayList<>();

    public class UTInfo {
        public String arg1;
        public String arg2;
        public String arg3;
        public String eventname;
        public HashMap<String, String> map;
        public String pagename;
        public String spm;

        public UTInfo() {
        }
    }

    public void processTrace(JSONObject jSONObject) {
        processTrace(jSONObject.getJSONArray(AgooConstants.MESSAGE_TRACE));
    }

    public void processTrace(JSONArray jSONArray) {
        this.utInfos.clear();
        this.mtopJsons.clear();
        this.httpsJsons.clear();
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String string = jSONObject.getString("type");
            if (TextUtils.equals(string, "ut")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("params");
                UTInfo uTInfo = new UTInfo();
                uTInfo.pagename = "Page_" + jSONObject2.getString(EventConstants.UT.PAGE_NAME);
                uTInfo.arg1 = jSONObject2.getString(EventConstants.UT.ARG1);
                uTInfo.eventname = uTInfo.pagename + "_Button-" + uTInfo.arg1;
                uTInfo.arg2 = jSONObject2.getString("arg2");
                uTInfo.arg3 = jSONObject2.getString("arg3");
                uTInfo.spm = jSONObject2.getString("spm");
                uTInfo.map = new HashMap<>();
                if (!TextUtils.isEmpty(uTInfo.spm)) {
                    uTInfo.map.put("spm", uTInfo.spm);
                } else {
                    uTInfo.map.put("spm", this.spm);
                }
                JSONObject jSONObject3 = jSONObject2.getJSONObject("param");
                if (jSONObject3 != null) {
                    for (String next : jSONObject3.keySet()) {
                        uTInfo.map.put(next, jSONObject3.getString(next));
                    }
                }
                this.utInfos.add(uTInfo);
            } else if (TextUtils.equals(string, "mtop")) {
                this.mtopJsons.add(jSONObject);
            } else if (TextUtils.equals(string, "https")) {
                this.httpsJsons.add(jSONObject);
            }
        }
    }

    public void exposeUt() {
        IUTAction iUTAction = (IUTAction) UNWManager.getInstance().getService(IUTAction.class);
        if (iUTAction == null) {
            log("ut == null");
            return;
        }
        Iterator<BaseResourceData<T>.UTInfo> it = this.utInfos.iterator();
        while (it.hasNext()) {
            UTInfo next = it.next();
            iUTAction.expoTrack(next.pagename, next.eventname, next.arg2, next.arg3, next.map);
        }
        IEvent iEvent = (IEvent) UNWManager.getInstance().getService(IEvent.class);
        if (iEvent == null) {
            log("iEvent == null");
            return;
        }
        Iterator<JSONObject> it2 = this.mtopJsons.iterator();
        while (it2.hasNext()) {
            JSONObject replaceMtopJson = replaceMtopJson(it2.next(), "2201");
            if (replaceMtopJson != null) {
                replaceMtopJson.put("event_type", (Object) "mtop");
                iEvent.parseJson(replaceMtopJson);
            }
        }
        Iterator<JSONObject> it3 = this.httpsJsons.iterator();
        while (it3.hasNext()) {
            JSONObject next2 = it3.next();
            next2.put("event_type", (Object) "mtop");
            iEvent.parseJson(next2);
        }
    }

    public void click() {
        UTInfo uTInfo = this.utInfos.size() > 0 ? this.utInfos.get(0) : null;
        if (uTInfo != null) {
            click(uTInfo.arg1);
        }
        IEvent iEvent = (IEvent) UNWManager.getInstance().getService(IEvent.class);
        if (iEvent == null) {
            log("iEvent == null");
            return;
        }
        Iterator<JSONObject> it = this.mtopJsons.iterator();
        while (it.hasNext()) {
            JSONObject replaceMtopJson = replaceMtopJson(it.next(), "2101");
            if (replaceMtopJson != null) {
                replaceMtopJson.put("event_type", (Object) "mtop");
                iEvent.parseJson(replaceMtopJson);
            }
        }
    }

    public void click(@NotNull String str) {
        if (!TextUtils.isEmpty(str)) {
            IUTAction iUTAction = (IUTAction) UNWManager.getInstance().getService(IUTAction.class);
            if (iUTAction == null) {
                log("ut == null");
                return;
            }
            UTInfo uTInfo = null;
            if (this.utInfos.size() > 0) {
                uTInfo = this.utInfos.get(0);
            }
            if (uTInfo == null) {
                log("utInfo == null");
                return;
            }
            String str2 = uTInfo.pagename;
            iUTAction.ctrlClicked(str2, "Button-" + str, uTInfo.map);
        }
    }

    public void log(String str) {
        UNWLog.error(TAG, str);
    }

    private JSONObject replaceMtopJson(JSONObject jSONObject, String str) {
        JSONObject jSONObject2;
        if (jSONObject == null || (jSONObject2 = jSONObject.getJSONObject("params")) == null) {
            return null;
        }
        jSONObject2.put("event", (Object) str);
        jSONObject.remove("params");
        jSONObject.put("params", (Object) jSONObject2);
        return jSONObject;
    }
}
