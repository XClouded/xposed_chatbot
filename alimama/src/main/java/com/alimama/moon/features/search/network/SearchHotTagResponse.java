package com.alimama.moon.features.search.network;

import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.List;

public class SearchHotTagResponse {
    public List<SearchHotTagItem> hotTagItems = new ArrayList();
    private String url;
    private String word;

    public SearchHotTagResponse(SafeJSONObject safeJSONObject) {
        SafeJSONArray optJSONArray = safeJSONObject.optJSONArray("data");
        for (int i = 0; i < optJSONArray.length(); i++) {
            this.word = optJSONArray.optJSONObject(i).optString("word");
            this.url = optJSONArray.optJSONObject(i).optString("url");
            this.hotTagItems.add(new SearchHotTagItem(this.word, this.url));
        }
    }
}
