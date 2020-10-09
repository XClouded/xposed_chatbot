package com.alimama.union.app.toolCenter.data;

import com.alimama.union.app.toolCenter.data.ToolItemBean;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.List;

public class HomeToolFragDataResult {
    public List<ToolItemBean> toolItems;

    public HomeToolFragDataResult(SafeJSONObject safeJSONObject) {
        try {
            SafeJSONObject optJSONObject = safeJSONObject.optJSONObject("data");
            this.toolItems = new ArrayList();
            SafeJSONArray optJSONArray = optJSONObject.optJSONArray("items");
            for (int i = 0; i < optJSONArray.length(); i++) {
                SafeJSONObject optJSONObject2 = optJSONArray.optJSONObject(i).optJSONObject("data");
                ToolItemBean toolItemBean = new ToolItemBean();
                toolItemBean.title = optJSONObject2.optString("title");
                toolItemBean.groupName = optJSONObject2.optString("groupname");
                SafeJSONArray optJSONArray2 = optJSONObject2.optJSONArray("arrayItems");
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    SafeJSONObject optJSONObject3 = optJSONArray2.optJSONObject(i2);
                    ToolItemBean.ChildItem childItem = new ToolItemBean.ChildItem();
                    childItem.img = optJSONObject3.optString("img");
                    childItem.title = optJSONObject3.optString("title");
                    childItem.url = optJSONObject3.optString("url");
                    arrayList.add(childItem);
                }
                toolItemBean.items = arrayList;
                this.toolItems.add(toolItemBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
