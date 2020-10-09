package com.alimama.moon.features.search.network;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchRealTimeSugResponse {
    public List<String> mList = new ArrayList();

    public SearchRealTimeSugResponse(SafeJSONObject safeJSONObject) {
        SafeJSONArray optJSONArray = safeJSONObject.optJSONArray("result");
        for (int i = 0; i < optJSONArray.length(); i++) {
            this.mList.add(optJSONArray.optJSONArray(i).optString(0));
        }
    }
}
