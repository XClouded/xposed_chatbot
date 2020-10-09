package com.alimama.union.app.sceneCenter.item;

import android.text.TextUtils;
import com.alimamaunion.common.listpage.CommonBaseItem;
import com.coloros.mcssdk.mode.Message;
import org.json.JSONObject;

public class SceneTabItem extends CommonBaseItem {
    public String mDescription;
    public String mImg;
    public String mSubDescription;
    public String mTagImg;
    public boolean mTipShow = (!TextUtils.isEmpty(this.mTagImg));
    public String mTitle;
    public String mUrl;

    public SceneTabItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        this.mTagImg = optJSONObject.optString("tagImg");
        this.mImg = optJSONObject.optString("img");
        this.mTitle = optJSONObject.optString("title");
        this.mDescription = optJSONObject.optString(Message.DESCRIPTION);
        this.mSubDescription = optJSONObject.optString("subDescription");
        this.mUrl = optJSONObject.optString("url");
    }
}
