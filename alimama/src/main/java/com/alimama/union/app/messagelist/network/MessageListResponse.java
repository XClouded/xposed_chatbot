package com.alimama.union.app.messagelist.network;

import android.text.TextUtils;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.ArrayList;
import java.util.List;

public class MessageListResponse {
    private boolean mHasMore;
    public List<CommonItemInfo> messageListItems = new ArrayList();

    public MessageListResponse(SafeJSONObject safeJSONObject) {
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("data");
        this.mHasMore = TextUtils.equals("1", optJSONObject.optString(ProtocolConst.KEY_HAS_MORE));
        SafeJSONArray optJSONArray = optJSONObject.optJSONArray("items");
        for (int i = 0; i < optJSONArray.length(); i++) {
            SafeJSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
            CommonItemInfo createCommonItem = CommonItemInfo.createCommonItem(optJSONObject2.optString("type"), optJSONObject2.optJSONObject("data"));
            if (createCommonItem != null) {
                this.messageListItems.add(createCommonItem);
            }
        }
    }

    public boolean isHasMore() {
        return this.mHasMore;
    }
}
