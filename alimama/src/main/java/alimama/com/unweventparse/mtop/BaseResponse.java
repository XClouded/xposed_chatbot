package alimama.com.unweventparse.mtop;

import com.alibaba.fastjson.JSONObject;

public class BaseResponse {
    public JSONObject key;

    public BaseResponse(JSONObject jSONObject, String str) {
        this.key = jSONObject.getJSONObject(str);
    }
}
