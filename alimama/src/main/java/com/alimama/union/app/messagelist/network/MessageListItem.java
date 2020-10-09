package com.alimama.union.app.messagelist.network;

import com.alimamaunion.common.listpage.CommonBaseItem;
import org.json.JSONObject;

public class MessageListItem extends CommonBaseItem {
    public final String gmtCreate;
    public final String msgContent;
    public final String msgIcon;
    public final String msgImg;
    public final String msgSubContent;
    public final String msgTitle;
    public final String msgUrl;
    public String readStatus;
    public final String timestamp;
    public final String topCategoryId;
    public final String topCategoryName;
    public final String userId;

    public MessageListItem(String str, int i, JSONObject jSONObject) {
        super(str, i, jSONObject);
        this.userId = jSONObject.optString("userId");
        this.timestamp = jSONObject.optString("timestamp");
        this.topCategoryName = jSONObject.optString("topCategoryName");
        this.msgTitle = jSONObject.optString("msgTitle");
        this.msgContent = jSONObject.optString("msgContent");
        this.msgImg = jSONObject.optString("msgImg");
        this.msgUrl = jSONObject.optString("msgUrl");
        this.gmtCreate = jSONObject.optString("gmtCreate");
        this.readStatus = jSONObject.optString("readStatus");
        this.msgIcon = jSONObject.optString("msgIcon");
        this.msgSubContent = jSONObject.optString("msgSubContent");
        this.topCategoryId = jSONObject.optString("topCategoryId");
    }
}
