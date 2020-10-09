package com.alimama.moon.web;

import android.net.Uri;
import com.alimama.union.app.configcenter.ConfigCenterDataUtils;

public final class WebPageIntentGenerator {
    public static Uri getPromotionUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.MINE_PROMOTION_URL));
    }

    public static Uri getUserGuideUri() {
        return getAlpBuilder().appendEncodedPath("guide").build();
    }

    public static Uri getFaqUri() {
        return getAlpBuilder().appendEncodedPath("help_list").build();
    }

    public static Uri getAboutUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.MINE_ABOUT_URL));
    }

    public static Uri getAgreementUrlUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.FIRST_USE_AGREEMENT_URL));
    }

    public static Uri getAccountHelpUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.FIRST_USE_HELP_URL));
    }

    public static String getItemLandingPage(String str) {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.TAOKE_DETAIL_URL)).buildUpon().appendQueryParameter("url", str).build().toString();
    }

    public static String getRebateItemLandingPage(String str) {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.FULI_DETAIL)).buildUpon().appendQueryParameter("url", str).build().toString();
    }

    public static String getSimilarItemPage(String str) {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.SEARCH_SIMILAR_URL)).buildUpon().appendQueryParameter("url", str).build().toString();
    }

    public static Uri getThresholdRegulationUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.THRESHOLD_REGULATION_URL));
    }

    public static Uri getThresholdUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.MINE_THRESHOLD_URL));
    }

    public static Uri getMyredbagsUri() {
        return Awp.getAlpHostBuilder().appendEncodedPath("union").appendEncodedPath("my_redbags").build();
    }

    public static Uri getOrderCheckUri() {
        return Awp.getAlpHostBuilder().appendEncodedPath("union").appendEncodedPath("query_cps").build();
    }

    public static Uri getButlerPrivilegeUri() {
        return Awp.getAlpHostBuilder().appendEncodedPath("union").appendEncodedPath("agent_steward").build();
    }

    public static Uri getWalletPrivilegeUri() {
        return Awp.getAlpHostBuilder().appendEncodedPath("union").appendEncodedPath("flbb").appendEncodedPath("index.html").build();
    }

    private static Uri.Builder getAlpBuilder() {
        return Awp.getAlpHostBuilder().appendEncodedPath("union").appendEncodedPath("uapp");
    }

    public static String getWordsExplainUrl() {
        return ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.REPORT_WORDS_EXPLAIN_URL);
    }

    public static String getReportRobotEntrance() {
        return ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.REPORT_ROBOT_ENTRANCE);
    }

    public static String getReportPageIconUrl() {
        return ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.REPORT_ROBOT_ENTRANCE_ICON_URL);
    }

    public static String getSearchUrl() {
        return ConfigCenterDataUtils.getFixedUrl("search");
    }

    public static String getTaoCodeTransferFaqUrl() {
        return ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.CHAIN_TOOL_URL);
    }

    public static String getSearchResultStoreUrl() {
        return ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.SEARCH_RESULT_STORE_URL);
    }
}
