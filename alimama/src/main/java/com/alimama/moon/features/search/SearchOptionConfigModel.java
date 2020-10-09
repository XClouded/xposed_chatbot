package com.alimama.moon.features.search;

import androidx.annotation.NonNull;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class SearchOptionConfigModel {
    private static final String CONFIG_KEY_FIRST_CLASS_OPTION = "search_option_items";
    private static final String CONFIG_KEY_SIDE_PANEL_FILTER = "search_filter_items";
    private static final String CONFIG_KEY_SORT_BY = "search_sort_items";
    private final Map<String, List<SearchOptionModel>> mSearchConfigMap = new HashMap();

    public static SearchOptionConfigModel fromJsonJson(String str) {
        SearchOptionConfigModel searchOptionConfigModel = new SearchOptionConfigModel();
        try {
            SafeJSONArray optJSONArray = new SafeJSONObject(str).optJSONObject("data").optJSONArray("items");
            for (int i = 0; i < optJSONArray.length(); i++) {
                SafeJSONObject optJSONObject = optJSONArray.optJSONObject(i);
                String optString = optJSONObject.optString("key");
                ArrayList arrayList = new ArrayList();
                SafeJSONArray optJSONArray2 = optJSONObject.optJSONArray("items");
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    SafeJSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                    arrayList.add(new SearchOptionModel(optJSONObject2.optString("title"), optJSONObject2.optString("condition"), optJSONObject2.optString("controlName")));
                }
                searchOptionConfigModel.mSearchConfigMap.put(optString, arrayList);
            }
            return searchOptionConfigModel;
        } catch (JSONException unused) {
            return searchOptionConfigModel;
        }
    }

    @NonNull
    public List<SearchOptionModel> getSortByOptionList() {
        List<SearchOptionModel> list = this.mSearchConfigMap.get(CONFIG_KEY_SORT_BY);
        return list == null ? Collections.emptyList() : list;
    }

    @NonNull
    public List<SearchOptionModel> getTopOptionList() {
        List<SearchOptionModel> list = this.mSearchConfigMap.get(CONFIG_KEY_FIRST_CLASS_OPTION);
        return list == null ? Collections.emptyList() : list;
    }

    @NonNull
    public List<SearchOptionModel> getSidePanelFilters() {
        List<SearchOptionModel> list = this.mSearchConfigMap.get(CONFIG_KEY_SIDE_PANEL_FILTER);
        return list == null ? Collections.emptyList() : list;
    }
}
