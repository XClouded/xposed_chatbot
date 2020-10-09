package com.alimama.union.app.configcenter;

import android.text.TextUtils;
import android.util.Log;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;

public class ConfigCenterDataUtils {
    public static final String CHAIN_TOOL_URL = "chain_tool";
    public static final String FIRST_USE_AGREEMENT_URL = "first_use_agreement";
    public static final String FIRST_USE_HELP_URL = "first_use_help";
    public static final String FULI_DETAIL = "fuli_detail";
    public static final String MINE_ABOUT_URL = "mine_about";
    public static final String MINE_PROMOTION_URL = "mine_promotion";
    public static final String MINE_THRESHOLD_URL = "mine_threshold";
    public static final String REPORT_ROBOT_ENTRANCE = "report_topurl";
    public static final String REPORT_ROBOT_ENTRANCE_ICON_URL = "report_topicon_url";
    public static final String REPORT_WEEX_URL = "report_weex";
    public static final String REPORT_WORDS_EXPLAIN_URL = "report_words_explain";
    public static final String SEARCH_EXTEND_URL = "search_extend";
    public static final String SEARCH_RESULT_STORE_URL = "search_result_store";
    public static final String SEARCH_SIMILAR_URL = "search_similar";
    public static final String SEARCH_URL = "search";
    public static final String SHARE_WEEX_URL = "share_weex";
    public static final String TAOKE_DETAIL_URL = "taoke_detail";
    public static final String THRESHOLD_REGULATION_URL = "threshold_regulation";

    public static String getFixedUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            SafeJSONArray optJSONArray = new SafeJSONObject(EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_FIXED_URL)).optJSONObject("data").optJSONArray("items");
            for (int i = 0; i < optJSONArray.length(); i++) {
                SafeJSONObject optJSONObject = optJSONArray.optJSONObject(i);
                String optString = optJSONObject.optString("key");
                Log.d("ConfigCenterDataUtils", optString + "   " + optJSONObject.optString("url"));
                if (str.equals(optString)) {
                    return optJSONObject.optString("url");
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
