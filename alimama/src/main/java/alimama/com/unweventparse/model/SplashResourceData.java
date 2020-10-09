package alimama.com.unweventparse.model;

import android.net.Uri;
import com.ali.user.mobile.login.model.LoginConstant;
import com.alibaba.fastjson.JSONObject;
import org.android.agoo.common.AgooConstants;

public class SplashResourceData extends BaseResourceData<SplashResourceData> {
    public String assetType;
    public String clickZone;
    public String closeTxt;
    public String creativeId;
    public String endTime;
    public int height;
    public String img;
    public String md5;
    public String openType;
    public boolean skip;
    public String startTime;
    public String templateId;
    public String title;
    public String type;
    public String url;
    public int width;

    public SplashResourceData make(Uri uri) {
        return null;
    }

    public SplashResourceData make(JSONObject jSONObject) {
        this.creativeId = jSONObject.getString("creativeId");
        this.templateId = jSONObject.getString("templateId");
        this.assetType = jSONObject.getString("assetType");
        this.width = jSONObject.getInteger("width").intValue();
        this.height = jSONObject.getInteger("height").intValue();
        this.url = jSONObject.getString("url");
        this.openType = jSONObject.getString("assetType");
        this.img = jSONObject.getString("img");
        this.closeTxt = jSONObject.getString("closeTxt");
        this.md5 = jSONObject.getString("md5");
        this.type = jSONObject.getString("type");
        this.clickZone = jSONObject.getString("clickZone");
        this.title = jSONObject.getString("title");
        this.skip = jSONObject.getBoolean("skip").booleanValue();
        this.startTime = jSONObject.getString(LoginConstant.START_TIME);
        this.endTime = jSONObject.getString("endTime");
        processTrace(jSONObject.getJSONArray(AgooConstants.MESSAGE_TRACE));
        return this;
    }
}
