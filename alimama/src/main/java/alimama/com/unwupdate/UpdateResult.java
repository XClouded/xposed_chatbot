package alimama.com.unwupdate;

import android.text.TextUtils;
import android.util.Log;
import com.ali.user.mobile.rpc.ApiConstants;
import org.json.JSONObject;

public class UpdateResult {
    public String channelNum;
    public String etag;
    public boolean forceUpdate;
    public boolean hasUpdate;
    public String info;
    public String jsonData;
    public String md5;
    public String pkgSize;
    public String pkgUrl;
    public String pkgVersion;
    public String remindCount;
    public String remindStrategy;

    public static UpdateResult parse(JSONObject jSONObject) {
        UpdateResult updateResult = new UpdateResult();
        if (jSONObject == null || TextUtils.isEmpty(jSONObject.optString("data"))) {
            return updateResult;
        }
        updateResult.jsonData = jSONObject.toString();
        updateResult.hasUpdate = jSONObject.optJSONObject("data").optBoolean("hasUpdate", false);
        if (!updateResult.hasUpdate) {
            return updateResult;
        }
        try {
            updateResult.hasUpdate = jSONObject.optJSONObject("data").optBoolean("hasUpdate", false);
            updateResult.channelNum = jSONObject.optJSONObject("data").optJSONObject("main").optString("channelNum");
            updateResult.etag = jSONObject.optJSONObject("data").optJSONObject("main").optString("etag");
            updateResult.info = jSONObject.optJSONObject("data").optJSONObject("main").optString(ApiConstants.ApiField.INFO);
            updateResult.md5 = jSONObject.optJSONObject("data").optJSONObject("main").optString("md5");
            updateResult.pkgUrl = jSONObject.optJSONObject("data").optJSONObject("main").optString("packageUrl");
            if (!TextUtils.isEmpty(updateResult.pkgUrl) && updateResult.pkgUrl.startsWith("http://")) {
                updateResult.pkgUrl = updateResult.pkgUrl.replace("http://", "https://");
            }
            updateResult.remindCount = jSONObject.optJSONObject("data").optJSONObject("main").optString("remindCount");
            updateResult.remindStrategy = jSONObject.optJSONObject("data").optJSONObject("main").optString("remindStrategy");
            updateResult.pkgSize = jSONObject.optJSONObject("data").optJSONObject("main").optString("size");
            updateResult.pkgVersion = jSONObject.optJSONObject("data").optJSONObject("main").optString("version");
            updateResult.forceUpdate = TextUtils.equals(updateResult.remindStrategy, "2");
        } catch (Exception e) {
            Log.d("UpdateResult", e.getMessage());
        }
        return updateResult;
    }
}
