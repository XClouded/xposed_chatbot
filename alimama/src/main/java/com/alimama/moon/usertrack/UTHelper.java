package com.alimama.moon.usertrack;

import android.text.TextUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTTracker;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UTHelper {
    public static final String CONTROL_NAME_CLICK_AD = "click_ad";
    public static final String CONTROL_NAME_CLICK_SAVE_PIC = "save_pic";
    public static final String CONTROL_NAME_HAVE_PERMISSION = "have_permission";
    public static final String CONTROL_NAME_MARKET_SHOW = "market_show";
    public static final String PAGE_NAME_HOME_MARKET = "union/union_homepagelayer";
    public static final String PAGE_NAME_ITEM_WEEX_SHARE = "union/union_itemweexshare";
    public static final String PAGE_NAME_MSGLIST_ACTIVITY = "Page_tblm_lmapp_MessageCenter";
    public static final String PAGE_NAME_SD_PERMISSION = "union/union_sdpermission";
    public static final String PAGE_NAME_SHARE_ACTIVITY = "union/union_share";
    public static final String SCM_SHARE_COPY_MSG = "20140618.1.01010001.s101c6";
    public static final String SCM_SHARE_COPY_PIC = "20140618.1.01010001.s101c6";
    public static final String SCM_SHARE_TO_QQ = "20140618.1.01010001.s101c2";
    public static final String SCM_SHARE_TO_WECHAT = "20140618.1.01010001.s101c4";
    public static final String SCM_SHARE_TO_WEIBO = "20140618.1.01010001.s101c1";
    public static final String SCM_URI_PARAMETER_KEY = "scm";
    public static final String SPM_CLICK_IMG_SHARE_TO_QQ = "imgShareToQQ";
    public static final String SPM_CLICK_IMG_SHARE_TO_WECHAT = "imgShareToWechat";
    public static final String SPM_CLICK_IMG_SHARE_TO_WEIBO = "imgShareToWeibo";
    public static final String SPM_CLICK_MSG = "msgClick";
    public static final String SPM_CLICK_SHARE_BACK = "backClick";
    public static final String SPM_CLICK_SHARE_CODE = "shareCodeClick";
    public static final String SPM_CLICK_SHARE_COPY_MSG = "copyMsgClick";
    public static final String SPM_CLICK_SHARE_IMG = "shareImgClick";
    public static final String SPM_CLICK_SHARE_RULE_TIP = "ruleTipClick";
    public static final String SPM_CLICK_SHARE_SAVE_PIC = "savePicClick";
    public static final String SPM_CLICK_TEXT_SHARE_TO_QQ = "textShareToQQ";
    public static final String SPM_CLICK_TEXT_SHARE_TO_WECHAT = "textShareToWechat";
    public static final String SPM_CLICK_TEXT_SHARE_TO_WEIBO = "textShareToWeibo";
    public static final String SPM_CNT_MSGLIST_ACTIVITY = "a21wq.9116219";
    public static final String SPM_CNT_SHARE_ACTIVITY = "a21wq.11162860";
    public static final String SPM_CUSTOM_SHARE_MSG_EDIT = "union/union_share_shareMsgEdit";
    public static final String SPM_SYSTEM_SHARE_CLICK = "sharePicClick";
    public static final String UT_TAO_CODE_DIALOG_PAGE_NAME = "Page_tblm_lmapp_TaoTokenTransfer";
    public static final String UT_WEBVIEW_PAGE_NAME = "Page_webview";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) UTHelper.class);

    public static class TaoCodeDialog {
        public static final String SPM_CNT = "a21wq.8595780";
        public static final String SPM_VAL_TO_DETAILS = "a21wq.8595780.1000.2";
        public static final String SPM_VAL_TO_SHARE = "a21wq.8595780.1000.3";
        public static final String SPM_VAL_TO_SIMILAR = "a21wq.8595780.1000.4";
    }

    public static class ShareFlutterPage {
        public static final String PAGE_NAME = "union/union_share";
        public static final String SPM_CNT = "a21wq.11162860";

        public static void clickWechatImgShare() {
            trackControlClick(UTHelper.SPM_CLICK_IMG_SHARE_TO_WECHAT);
        }

        public static void clickWechatCodeShare() {
            trackControlClick("codeShareToWechat");
        }

        public static void clickWechatSysShare() {
            trackControlClick("sysShareToWechat");
        }

        public static void clickWechatMoments() {
            trackControlClick("wechatMomentsShare");
        }

        public static void clickWeiboImgShare() {
            trackControlClick(UTHelper.SPM_CLICK_IMG_SHARE_TO_WEIBO);
        }

        public static void clickWeiboCodeShare() {
            trackControlClick("codeShareToWeibo");
        }

        public static void clickWeiboSysShare() {
            trackControlClick("sysShareToWeibo");
        }

        public static void clickQqImgShare() {
            trackControlClick(UTHelper.SPM_CLICK_IMG_SHARE_TO_QQ);
        }

        public static void clickQqCodeShare() {
            trackControlClick("codeShareToQQ");
        }

        public static void clickQqSysShare() {
            trackControlClick("sysShareToQq");
        }

        public static void clickTaoCodeErrorConfirm() {
            trackControlClick("taoCodeErrorConfirm");
        }

        private static void trackControlClick(String str) {
            if (!TextUtils.isEmpty(str)) {
                UTHelper.sendControlHit("union/union_share", str);
            }
        }
    }

    public static class SharePosterPage {
        public static final String PAGE_NAME = "union/union_shareposter";
        public static final String SPM_CNT = "a21wq.12986574";

        public static void clickWechatShare() {
            trackControlClick("posterShareToWechat");
        }

        public static void clickWechatSysShare() {
            trackControlClick("posterSysShareToWechat");
        }

        public static void clickWechatMoments() {
            trackControlClick("posterWechatMomentsShare");
        }

        public static void clickWeiboShare() {
            trackControlClick("posterShareToWeibo");
        }

        public static void clickWeiboSysShare() {
            trackControlClick("posterSysShareToWeibo");
        }

        public static void clickQqShare() {
            trackControlClick("posterShareToQQ");
        }

        public static void clickQqSysShare() {
            trackControlClick("posterSysShareToQq");
        }

        public static void clickPosterSysShare() {
            trackControlClick("posterSysShare");
        }

        public static void clickSavePoster() {
            trackControlClick("savePoster");
        }

        public static void selectCollageImage() {
            trackControlClick("selectCollageImage");
        }

        public static void deselectCollageImage() {
            trackControlClick("deselectCollageImage");
        }

        public static void turnOnQrCode() {
            trackControlClick("turnOnQrCode");
        }

        public static void turnOffQrCode() {
            trackControlClick("turnOffQrCode");
        }

        private static void trackControlClick(String str) {
            if (!TextUtils.isEmpty(str)) {
                UTHelper.sendControlHit(PAGE_NAME, str);
            }
        }
    }

    public static class SearchResultsPage {
        private static final String CONTROL_CONFIRM_FILTERS = "confirmFilters";
        private static final String CONTROL_EDIT_MAX_COMMISSION = "editMaxCommission";
        private static final String CONTROL_EDIT_MIN_COMMISSION = "editMinCommission";
        private static final String CONTROL_EDIT_MIN_PRICE = "editMinPrice";
        private static final String CONTROL_ITEM_INFO = "itemInfo";
        private static final String CONTROL_ITEM_SHARE = "itemShare";
        private static final String CONTROL_NAME_EDIT_MAX_PRICE = "editMaxPrice";
        private static final String CONTROL_NAME_OPEN_FILTERS = "openFilters";
        public static final String PAGE_NAME = "Page_Search_Results";
        public static final String SPM_CNT = "a21wq.12726013";
        public static final String SPM_VAL_ITEM_TO_SHARE = "a21wq.12726013.1000.2";
        public static final String SPM_VAL_TO_SHARE = "a21wq.12726013.1000.1";

        public static void trackControlClick(String str) {
            if (!TextUtils.isEmpty(str)) {
                UTHelper.sendControlHit(PAGE_NAME, str);
            }
        }

        public static void trackEditMinPrice() {
            trackControlClick(CONTROL_EDIT_MIN_PRICE);
        }

        public static void trackEditMaxPrice() {
            trackControlClick(CONTROL_NAME_EDIT_MAX_PRICE);
        }

        public static void trackEditMinCommission() {
            trackControlClick(CONTROL_EDIT_MIN_COMMISSION);
        }

        public static void trackEditMaxCommission() {
            trackControlClick(CONTROL_EDIT_MAX_COMMISSION);
        }

        public static void trackConfirmFilters() {
            trackControlClick(CONTROL_CONFIRM_FILTERS);
        }

        public static void trackOpenFilter() {
            trackControlClick(CONTROL_NAME_OPEN_FILTERS);
        }

        public static void trackItemShare(String str, String str2, String str3) {
            HashMap hashMap = new HashMap();
            hashMap.put("item", str);
            hashMap.put("lensId", str2);
            UTHelper.sendControlHit(PAGE_NAME, CONTROL_ITEM_SHARE, hashMap);
        }

        public static void trackItemInfo(String str, String str2, String str3) {
            HashMap hashMap = new HashMap();
            hashMap.put("item", str);
            hashMap.put("lensId", str2);
            UTHelper.sendControlHit(PAGE_NAME, CONTROL_ITEM_INFO, hashMap);
        }
    }

    public static class SearchInput {
        public static final String PAGE_NAME = "union/union_search_input";
        public static final String SPM_CNT = "a21wq.8448186";

        public static void clickRealtimeSug() {
            UTHelper.sendControlHit(PAGE_NAME, "realTimeSugClick");
        }

        public static void clickCancel() {
            UTHelper.sendControlHit(PAGE_NAME, "cancelClick");
        }

        public static void clickSwitchSearchType() {
            UTHelper.sendControlHit(PAGE_NAME, "typeSwitchClick");
        }

        public static void clickClearHistory() {
            UTHelper.sendControlHit(PAGE_NAME, "clearHistoryClick");
        }

        public static void clickRecoQuery() {
            UTHelper.sendControlHit(PAGE_NAME, "recommendClick");
        }

        public static void searchHistoryQuery() {
            UTHelper.sendControlHit(PAGE_NAME, "historyClick");
        }

        public static void searchItems() {
            UTHelper.sendControlHit(PAGE_NAME, "commodityClick");
        }

        public static void searchShops() {
            UTHelper.sendControlHit(PAGE_NAME, "shopClick");
        }
    }

    public static class PrivacyAndPermissionDialog {
        private static final String PAGE_NAME = "Page_privacy_permission_dialog";

        public static void clickPrivacyDialog(String str, String str2) {
            trackControlClick(str, str2, "privacy_dialog");
        }

        public static void clickPrivacyDialog(String str) {
            if (!TextUtils.isEmpty(str)) {
                UTHelper.sendControlHit(PAGE_NAME, str);
            }
        }

        private static void trackControlClick(String str, String str2, String str3) {
            if (!TextUtils.isEmpty(str3)) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, str2);
                UTHelper.sendControlHit(PAGE_NAME, str3, hashMap);
            }
        }
    }

    public static class DistributeCenterPage {
        public static final String INVOKE_CONTROL_NAME = "scheme_invoke";
        public static final String INVOKE_FAIL_CONTROL_NAME = "scheme_invoke_fail";
        private static final String PAGE_NAME = "Page_distribute_center";

        public static void trackControlClick(String str, String str2, String str3) {
            if (!TextUtils.isEmpty(str3)) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, str2);
                UTHelper.sendControlHit(PAGE_NAME, str3, hashMap);
            }
        }
    }

    public static class HomePage {
        private static final String CONTROL_PAGE_NAME = "union/union_homepage";
        private static final String KEY = "itemID";
        public static final String PAGE_NAME = "Page_tblm_lmapp_MaterielCenter";
        public static final String PAGE_NAME_BANNER = "/tblm_lmapp.tblm_lmapp_homepage.homepage";
        public static final String SPM_CLICK_FLASH_REBATE_EARN_MORE = "a21wq.9116673.rebate";
        public static final String SPM_CNT = "a21wq.9116673";
        public static final String SPM_CNT_BACKUP = "a21wq.9116673.0.0";

        public static void toSearchClicked() {
            trackControlClick("clicksearch01");
        }

        public static void toMessagesClicked() {
            trackControlClick("click_exposure_message");
        }

        public static void clickTabCate(String str) {
            trackControlClick("tab_name", str, "tabcategory_tab");
        }

        public static void clickTabCateItem(String str) {
            trackControlClick("item_id", str, "tabcategory_item");
        }

        public static void clickTabCateShare(String str) {
            trackControlClick("item_id", str, "tabcategory_share");
        }

        public static void clickCardItem(String str) {
            trackControlClick("item", str, "card.item");
        }

        public static void clickCardShare(String str) {
            trackControlClick("item", str, "card.share");
        }

        public static void clickCardMore() {
            trackControlClick("card.more");
        }

        public static void clickGuessItem(Map<String, String> map) {
            if (map != null && map.size() != 0) {
                UTHelper.sendControlHit(CONTROL_PAGE_NAME, "recommend.item", map);
            }
        }

        public static void clickGuessShare(Map<String, String> map) {
            if (map != null && map.size() != 0) {
                UTHelper.sendControlHit(CONTROL_PAGE_NAME, "recommend.share", map);
            }
        }

        public static void clickBannerItem(int i) {
            HashMap hashMap = new HashMap();
            hashMap.put("screenshot", String.valueOf(i));
            UTHelper.sendControlHit(PAGE_NAME_BANNER, "banner", hashMap);
        }

        public static void clickCircleItem(int i) {
            trackControlClick("circle_nav", String.valueOf(i), "circlenav");
        }

        public static void clickSaleBlock() {
            trackControlClick("salesblock");
        }

        public static void clickFlashSaleMore() {
            trackControlClick("bitui.more");
        }

        public static void clickFlashSaleItem(String str, String str2) {
            HashMap hashMap = new HashMap();
            hashMap.put("item", str);
            hashMap.put("lensId", str2);
            UTHelper.sendControlHit(CONTROL_PAGE_NAME, "bitui.item", hashMap);
        }

        public static void clickFlashSaleItemShare(String str, String str2) {
            HashMap hashMap = new HashMap();
            hashMap.put("item", str);
            hashMap.put("lensId", str2);
            UTHelper.sendControlHit(CONTROL_PAGE_NAME, "bitui.share", hashMap);
        }

        public static void clickResourcePlaceItem() {
            trackControlClick("toplist");
        }

        public static void showHomePage() {
            trackControlClick("showHome");
        }

        public static void hiddenHomePage() {
            trackControlClick("hiddenHome");
        }

        public static void renderWorshipSingleTab() {
            trackControlClick("switch_worship_single_tab");
        }

        public static void renderNormalMultipleTab() {
            trackControlClick("switch_normal_multiple_tab");
        }

        private static void trackControlClick(String str) {
            if (!TextUtils.isEmpty(str)) {
                UTHelper.sendControlHit(CONTROL_PAGE_NAME, str);
            }
        }

        private static void trackControlClick(String str, String str2, String str3) {
            if (!TextUtils.isEmpty(str3)) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, str2);
                UTHelper.sendControlHit(CONTROL_PAGE_NAME, str3, hashMap);
            }
        }
    }

    public static class MinePage {
        public static final String PAGE_NAME = "Page_tblm_lmapp_PersonalCenter";
        public static final String SPM_CNT = "a21wq.9114130";

        public static void toUserGrades() {
            UTHelper.sendControlHit(PAGE_NAME, "threshold");
        }

        public static void toItem(String str) {
            UTHelper.sendControlHit(PAGE_NAME, str);
        }

        public static void clickSignOut() {
            UTHelper.sendControlHit(PAGE_NAME, "click_sign_out");
        }
    }

    public static void sendControlHit(String str) {
        try {
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(str).build());
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendCustomUT(String str, String str2) {
        UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str2);
        uTCustomHitBuilder.setEventPage(str);
        UTTracker defaultTracker = UTAnalytics.getInstance().getDefaultTracker();
        if (defaultTracker != null) {
            defaultTracker.send(uTCustomHitBuilder.build());
        }
    }

    public static void sendControlHit(String str, String str2) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(str, str2).build());
    }

    public static void sendControlHit(String str, String str2, Map<String, String> map) {
        UTHitBuilders.UTControlHitBuilder uTControlHitBuilder = new UTHitBuilders.UTControlHitBuilder(str, str2);
        uTControlHitBuilder.setProperties(map);
        UTAnalytics.getInstance().getDefaultTracker().send(uTControlHitBuilder.build());
    }
}
