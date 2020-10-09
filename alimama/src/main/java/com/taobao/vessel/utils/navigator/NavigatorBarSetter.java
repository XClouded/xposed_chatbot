package com.taobao.vessel.utils.navigator;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.android.nav.Nav;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import org.json.JSONObject;

public class NavigatorBarSetter implements IActivityNavBarSetter {
    private Context mContext;

    public boolean clearNavBarLeftItem(String str) {
        return false;
    }

    public boolean clearNavBarMoreItem(String str) {
        return false;
    }

    public boolean clearNavBarRightItem(String str) {
        return false;
    }

    public boolean pop(String str) {
        return false;
    }

    public boolean setNavBarLeftItem(String str) {
        return false;
    }

    public boolean setNavBarMoreItem(String str) {
        return false;
    }

    public boolean setNavBarRightItem(String str) {
        return false;
    }

    public boolean setNavBarTitle(String str) {
        return false;
    }

    public NavigatorBarSetter(Context context) {
        this.mContext = context;
    }

    public boolean push(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            String optString = new JSONObject(str).optString("url", "");
            if (TextUtils.isEmpty(optString)) {
                return false;
            }
            Uri parse = Uri.parse(optString);
            String scheme = parse.getScheme();
            Uri.Builder buildUpon = parse.buildUpon();
            if (!TextUtils.equals(scheme, "http") && !TextUtils.equals(scheme, "https")) {
                buildUpon.scheme("http");
            }
            if (Nav.from(this.mContext).toUri(buildUpon.toString())) {
                return true;
            }
            return false;
        } catch (Exception unused) {
        }
    }
}
