package com.alimama.union.app.sceneCenter.data;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.alimamaunion.common.listpage.CommonItemInfo;
import java.util.ArrayList;
import java.util.List;

public class SceneTabDataResult {
    public List<CommonItemInfo> toolItems = new ArrayList();

    public SceneTabDataResult(SafeJSONObject safeJSONObject) {
        SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("data");
        this.toolItems = new ArrayList();
        SafeJSONArray optJSONArray = optJSONObject.optJSONArray("items");
        for (int i = 0; i < optJSONArray.length(); i++) {
            SafeJSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
            CommonItemInfo createCommonItem = CommonItemInfo.createCommonItem(optJSONObject2.optString("type"), optJSONObject2);
            if (createCommonItem != null) {
                this.toolItems.add(createCommonItem);
            }
        }
    }
}
