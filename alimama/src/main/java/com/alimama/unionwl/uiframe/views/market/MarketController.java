package com.alimama.unionwl.uiframe.views.market;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import com.ali.user.mobile.login.model.LoginConstant;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class MarketController {
    private static String FORMAT = "yyyyMMdd";
    public static final String HOME_MARKET_DIALOG = "home_market_dialog";
    private static final String LAST_SHOWN_CONTENT = "home-market-dialog-content";
    private static final String LAST_SHOWN_DATE = "home-market-dialog-shown";
    private static MarketController sMarketController;
    private Date dateToday;
    private Date dateTodayLong;
    private String dateTodayStr;
    private int height;
    private String img;
    private String url;
    private int width;

    private MarketController() {
    }

    public static MarketController getInstance() {
        if (sMarketController == null) {
            sMarketController = new MarketController();
        }
        return sMarketController;
    }

    private boolean checkDate(String str) {
        try {
            SafeJSONObject safeJSONObject = new SafeJSONObject(str);
            String optString = safeJSONObject.optString(LoginConstant.START_TIME);
            String optString2 = safeJSONObject.optString("endTime");
            this.img = safeJSONObject.optString("img");
            this.width = safeJSONObject.optInt("width");
            this.height = safeJSONObject.optInt("height");
            this.url = safeJSONObject.optString("url");
            this.url = appendQueryParameter(Uri.parse(this.url), "spm", safeJSONObject.optString("spm")).toString();
            if (TextUtils.isEmpty(this.img)) {
                return false;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT, Locale.US);
            if (!this.dateTodayLong.after(simpleDateFormat.parse(optString)) || !this.dateTodayLong.before(simpleDateFormat.parse(optString2))) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static Uri appendQueryParameter(Uri uri, String str, String str2) {
        String str3;
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        Uri.Builder clearQuery = uri.buildUpon().clearQuery();
        boolean z = false;
        for (String next : queryParameterNames) {
            if (next.equals(str)) {
                z = true;
                str3 = str2;
            } else {
                str3 = uri.getQueryParameter(next);
            }
            clearQuery.appendQueryParameter(next, str3);
        }
        if (!z) {
            clearQuery.appendQueryParameter(str, str2);
        }
        return clearQuery.build();
    }

    private boolean needShowMarDia(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HOME_MARKET_DIALOG, 0);
        String string = sharedPreferences.getString(LAST_SHOWN_DATE, "");
        String string2 = sharedPreferences.getString(LAST_SHOWN_CONTENT, "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT, Locale.US);
        try {
            if (TextUtils.isEmpty(string) || !TextUtils.equals(string2, str) || this.dateToday.after(simpleDateFormat.parse(string))) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    private void afterShowMarDia(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(HOME_MARKET_DIALOG, 0).edit();
        edit.putString(LAST_SHOWN_DATE, str2);
        edit.putString(LAST_SHOWN_CONTENT, str);
        edit.apply();
    }

    public void marketing(String str, Context context, long j, MarketDialogInterceptor marketDialogInterceptor) {
        this.dateTodayLong = TimeUtil.tsToDateLong(j);
        this.dateToday = TimeUtil.tsToDate(j, FORMAT);
        this.dateTodayStr = TimeUtil.tsToDateStr(j, FORMAT);
        boolean needShowMarDia = needShowMarDia(context, str);
        boolean checkDate = checkDate(str);
        if (needShowMarDia && checkDate) {
            MarketDialog.doShow(context, this.img, this.url, this.width, this.height, marketDialogInterceptor);
            afterShowMarDia(context, str, this.dateTodayStr);
        }
    }
}
