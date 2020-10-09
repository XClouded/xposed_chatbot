package com.alimama.moon.network.login;

import android.net.Uri;
import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaoBaoUrlFilter {
    private static final String ACTIVITY_URL = "http://i.83jie.com/";
    private static final String CLOSE_WINDOW_URL = "moon://close";
    private static final String[] DETAIL_URLS = {"https://detail.tmall.com/item.htm", "http://detail.tmall.com/item.htm", "https://detail.m.tmall.com/item.htm", "http://detail.m.tmall.com/item.htm", "http://detail.wapa.tmall.com/item.htm", "https://detail.wapa.tmall.com/item.htm", "http://h5.wapa.taobao.com/awp/core/detail.htm", "https://h5.wapa.taobao.com/awp/core/detail.htm", "https://h5.m.taobao.com/awp/core/detail.htm", "http://h5.m.taobao.com/awp/core/detail.htm", "https://item.taobao.com/item.htm", "http://item.taobao.com/item.htm", "https://a.m.taobao.com", "http://a.m.taobao.com", "http://a.wapa.taobao.com/", "http://h5.waptest.taobao.com/awp/core/detail.htm", "https://h5.waptest.taobao.com/awp/core/detail.htm", "http://detail.waptest.tmall.com/item.htm", "https://detail.waptest.tmall.com/item.htm"};
    private static final String FEEDBACK_URL = "moon://feedback";
    private static final String[] LOGIN_URLS = {"taobao.com/login/login.htm", "taobao.com/login.htm", "login.taobao.com/member/login.html", "h5.m.taobao.com/awp/base/buy.htm"};
    private static final String MOON_APP_SCHEMA = "moon";
    private static final String MOON_SHARE_HOST = "share";
    private static final String[] SHOP_URLS = {"http://shop.m.taobao.com", "http://shop.taobao.com"};
    private static final String SHOW_SOFTINPUT_URL = "moon://showSoftInput";
    private static final Pattern TQG_DETAIL_PATTERN = Pattern.compile("^(?:http|https)://a\\.m\\.taobao\\.com/i\\d+\\.htm");
    private static final Pattern TQG_NOT_STARTED_DETAIL_PATTERN = Pattern.compile("^(?:http|https)://tqg\\.taobao\\.com/m/jusp/alone/qgdetail/mtp\\.htm");
    private static final Pattern TTTJ_DETAIL_PATTERN = Pattern.compile("^(?:http|https)://item\\.taobao\\.com/item\\.htm");
    private static final String USER_TRACK_URL = "moon://ut?";
    private static final String[] WHITE_URLS = {"http://wapp.waptest.taobao.com/taokeapp/", "http://wapp.wapa.taobao.com/taokeapp/", "http://wapp.m.taobao.com/taokeapp/", "http://h5.m.taobao.com/taokeapp/"};
    private static final String[] loginRedirects = {"https://login.taobao.com/jump?", "http://login.taobao.com/jump?", "https://login.tmall.com/jump?", "http://login.tmall.com/jump?"};
    private static final Pattern loginUrlPattern = Pattern.compile("^(http|https)://login.(m.|wapa.|waptest.){0,1}(taobao|tmall).com/");
    private static final Pattern shopUrlPattern = Pattern.compile("^http://shop(\\d{5,})(.m){0,1}.taobao.com");
    private static final Pattern shopUrlPatternForTmall = Pattern.compile("^http://(\\S{2,})(.m){0,1}.tmall.com");

    public static boolean isHitTqgDetail(String str) {
        if (!TextUtils.isEmpty(str) && TQG_DETAIL_PATTERN.matcher(str).find()) {
            return true;
        }
        return false;
    }

    public static boolean isHitTqgNotStartedDetail(String str) {
        if (!TextUtils.isEmpty(str) && TQG_NOT_STARTED_DETAIL_PATTERN.matcher(str).find()) {
            return true;
        }
        return false;
    }

    public static String extractTqgDetailUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Matcher matcher = TQG_DETAIL_PATTERN.matcher(str);
        return matcher.find() ? matcher.group() : "";
    }

    public static String extractTqgNotStartedDetailUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Matcher matcher = TQG_NOT_STARTED_DETAIL_PATTERN.matcher(str);
        return matcher.find() ? matcher.group() : "";
    }

    public static boolean isHitTttjDetail(String str) {
        if (!TextUtils.isEmpty(str) && TTTJ_DETAIL_PATTERN.matcher(str).find()) {
            return true;
        }
        return false;
    }

    public static String extractTttjDetailUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Matcher matcher = TTTJ_DETAIL_PATTERN.matcher(str);
        return matcher.find() ? matcher.group() : "";
    }

    public static boolean isHitOnLogin(String str) {
        if (str == null || str.trim().equals("") || !loginUrlPattern.matcher(str).find()) {
            return false;
        }
        for (String startsWith : loginRedirects) {
            if (str.startsWith(startsWith)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHitShopUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (shopUrlPattern.matcher(str).find()) {
            return true;
        }
        if (shopUrlPatternForTmall.matcher(str).find() && !str.startsWith("http://shop.m.taobao.com") && !str.startsWith("http://shop.m.tmall.com") && !str.startsWith("http://detail.m.tmall.com") && !str.startsWith("http://detail.tmall.com") && !str.startsWith("http://www.tmall.com")) {
            return true;
        }
        for (String startsWith : SHOP_URLS) {
            if (str.startsWith(startsWith)) {
                return true;
            }
        }
        return shopUrlPattern.matcher(str).matches();
    }

    public static boolean isHitActivityUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith(ACTIVITY_URL);
    }

    public static String getAuctionId(String str) {
        Matcher matcher = Pattern.compile("id=(\\d{5,})").matcher(str);
        if (!matcher.find()) {
            return "";
        }
        String group = matcher.group();
        System.out.println(group);
        return group;
    }

    public static boolean isHitOnDetail(String str) {
        if (str == null || !str.startsWith("http://")) {
            return false;
        }
        for (String startsWith : WHITE_URLS) {
            if (str.toLowerCase().startsWith(startsWith)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHitNewWebviewPage(String str) {
        if (!isHitUlr(str, DETAIL_URLS) && !isHitShopUrl(str)) {
            return isHitActivityUrl(str);
        }
        return true;
    }

    public static boolean isHitAuctionUrl(String str) {
        return isHitUlr(str, DETAIL_URLS);
    }

    public static boolean isHitShare(String str) {
        if (str == null) {
            return false;
        }
        Uri parse = Uri.parse(str);
        if (!"moon".equalsIgnoreCase(parse.getScheme()) || !"share".equalsIgnoreCase(parse.getHost())) {
            return false;
        }
        return true;
    }

    public static boolean isHitShowSoftInput(String str) {
        if (str == null) {
            return false;
        }
        return str.trim().startsWith(SHOW_SOFTINPUT_URL);
    }

    public static boolean isHitUlr(String str, String[] strArr) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String startsWith : strArr) {
            if (str.toLowerCase().startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHitUserTrack(String str) {
        if (str == null) {
            return false;
        }
        return str.toLowerCase().startsWith(USER_TRACK_URL);
    }

    public static boolean isHitCloseWindow(String str) {
        if (str == null) {
            return false;
        }
        return str.toLowerCase().startsWith(CLOSE_WINDOW_URL);
    }

    public static boolean isHitFeedback(String str) {
        if (str == null) {
            return false;
        }
        return str.toLowerCase().startsWith(FEEDBACK_URL);
    }
}
