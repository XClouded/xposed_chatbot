package com.ta.audid.upload;

import android.text.TextUtils;
import com.ta.audid.Variables;
import com.ta.audid.utils.UtdidLogger;
import org.json.JSONException;
import org.json.JSONObject;

public class BizResponse {
    private static final String TAG_AUDID = "audid";
    private static final String TAG_CODE = "code";
    private static final String TAG_DATA = "data";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_UTDID = "utdid";
    public int code = -1;
    public String message = "";

    public static boolean isSuccess(int i) {
        return i >= 0 && i != 10012;
    }

    public static BizResponse parseResult(String str) {
        JSONObject jSONObject;
        BizResponse bizResponse = new BizResponse();
        try {
            JSONObject jSONObject2 = new JSONObject(str);
            if (jSONObject2.has("code")) {
                bizResponse.code = jSONObject2.getInt("code");
            }
            if (jSONObject2.has("message")) {
                bizResponse.message = jSONObject2.getString("message");
            }
            if (jSONObject2.has("data") && (jSONObject = jSONObject2.getJSONObject("data")) != null) {
                if (jSONObject.has("audid")) {
                    String string = jSONObject.getString("audid");
                    if (!TextUtils.isEmpty(string)) {
                        UtdidKeyFile.writeAudidFile(string);
                    }
                }
                if (jSONObject.has("utdid")) {
                    String string2 = jSONObject.getString("utdid");
                    if (!TextUtils.isEmpty(string2)) {
                        UtdidKeyFile.writeUtdidToSettings(Variables.getInstance().getContext(), string2);
                        UtdidKeyFile.writeSdcardUtdidFile(string2);
                        UtdidKeyFile.writeAppUtdidFile(string2);
                    }
                }
            }
        } catch (JSONException e) {
            UtdidLogger.d("", e.toString());
        }
        return bizResponse;
    }
}
