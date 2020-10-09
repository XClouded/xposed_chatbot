package com.alimama.unwdinamicxcontainer.event;

import android.util.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;

public class DXEventManager {
    private static DXEventManager sInstance;
    private final String TAG = "DXEventManagerTAG";
    private HashMap<String, IDXEvent> mHashMap = new HashMap<>();

    private DXEventManager() {
    }

    public static DXEventManager getInstance() {
        if (sInstance == null) {
            sInstance = new DXEventManager();
        }
        return sInstance;
    }

    public void registerDXEvent(String str, IDXEvent iDXEvent) {
        if (this.mHashMap == null) {
            this.mHashMap = new HashMap<>();
        }
        this.mHashMap.put(str, iDXEvent);
    }

    public HashMap<String, IDXEvent> fetchRegisterEvent() {
        return this.mHashMap;
    }

    public void dispatchEvent(Object[] objArr) {
        try {
            JSONArray parseArray = JSONObject.parseArray(objArr[0].toString());
            for (int i = 0; i < parseArray.size(); i++) {
                JSONObject jSONObject = (JSONObject) parseArray.get(i);
                String string = jSONObject.getString("type");
                if (this.mHashMap.get(string) != null) {
                    this.mHashMap.get(string).executeEvent(jSONObject.toJSONString());
                }
            }
        } catch (Exception e) {
            Log.e("DXEventManagerTAG", e.toString());
        }
    }
}
