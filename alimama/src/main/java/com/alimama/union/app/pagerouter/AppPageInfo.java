package com.alimama.union.app.pagerouter;

import alimama.com.unwrouter.PageInfo;
import com.alimama.moon.ResultActivity;
import com.alimama.moon.features.feedback.FeedbackActivity;
import com.alimama.moon.features.reports.withdraw.WithdrawActivity;
import com.alimama.moon.features.search.SearchInputActivity;
import com.alimama.moon.features.search.SearchResultsActivity;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.moon.ui.UserGuideActivity;
import com.alimama.moon.ui.WeexActivity;
import com.alimama.moon.ui.WizardActivity;
import com.alimama.moon.ui.splashad.SplashAdActivity;
import com.alimama.moon.update.UpdateActivity;
import com.alimama.moon.web.WebActivity;
import com.alimama.union.app.aalogin.view.LoginChooserActivity;
import com.alimama.union.app.messagelist.MsgListActivity;
import com.alimama.union.app.share.ShareActivity;
import com.alimama.union.app.share.ShareFlutterActivity;
import com.alimama.union.app.share.SharePosterActivity;

public class AppPageInfo {
    public static final String APP_SCHEMA = "unionApp";
    public static final String FEED_BACK = "feedBack";
    public static final String LOGIN_CHOOSE = "loginChoose";
    public static final PageInfo PAGE_FEED_BACK = new PageInfo(FEED_BACK, FeedbackActivity.class);
    public static final PageInfo PAGE_HOME = new PageInfo(PageInfo.PAGE_HOME, BottomNavActivity.class);
    public static final PageInfo PAGE_LOGIN_CHOOSE = new PageInfo(LOGIN_CHOOSE, LoginChooserActivity.class);
    public static final PageInfo PAGE_MESSAGE_LIST = new PageInfo(PAGE_NAME_MESSAGE_LIST, MsgListActivity.class);
    public static final String PAGE_NAME_MESSAGE_LIST = "messageList";
    public static final String PAGE_NAME_SEARCH_INPUT = "searchInput";
    public static final String PAGE_NAME_SEARCH_RESULTS = "searchResults";
    public static final String PAGE_NAME_SHARE = "share";
    public static final String PAGE_NAME_SHARE_FLUTTER = "share_flutter";
    public static final String PAGE_NAME_SHARE_POSTER = "share_poster";
    public static final PageInfo PAGE_RESULT = new PageInfo("result", ResultActivity.class);
    public static final PageInfo PAGE_SEARCH_INPUT = new PageInfo(PAGE_NAME_SEARCH_INPUT, SearchInputActivity.class);
    public static final PageInfo PAGE_SEARCH_RESULTS = new PageInfo(PAGE_NAME_SEARCH_RESULTS, SearchResultsActivity.class);
    public static final PageInfo PAGE_SHARE = new PageInfo("share", ShareActivity.class);
    public static final PageInfo PAGE_SHARE_FLUTTER = new PageInfo(PAGE_NAME_SHARE_FLUTTER, ShareFlutterActivity.class);
    public static final PageInfo PAGE_SHARE_POSTER = new PageInfo(PAGE_NAME_SHARE_POSTER, SharePosterActivity.class);
    public static final PageInfo PAGE_SPLASH_AD = new PageInfo(SPLASH_AD, SplashAdActivity.class);
    public static final PageInfo PAGE_UPDATE = new PageInfo(UPDATE, UpdateActivity.class);
    public static final PageInfo PAGE_USER_GUIDE = new PageInfo(USER_GUIDE, UserGuideActivity.class);
    public static final PageInfo PAGE_WEBVIEW = new PageInfo(PageInfo.PAGE_WEBVIEW, WebActivity.class);
    public static final PageInfo PAGE_WEEX = new PageInfo("weex", WeexActivity.class);
    public static final PageInfo PAGE_WITH_DRAW = new PageInfo(WITH_DRAW, WithdrawActivity.class);
    public static final PageInfo PAGE_WIZARD = new PageInfo(WIZARD, WizardActivity.class);
    public static final String RESULT = "result";
    public static final String SELLER_MSG = "sellerMsg";
    public static final String SPLASH_AD = "splashAd";
    public static final String UPDATE = "update";
    public static final String USER_GUIDE = "userGuide";
    public static final String WEEX = "weex";
    public static final String WITH_DRAW = "withDraw";
    public static final String WIZARD = "wizard";

    public static void init() {
    }
}
