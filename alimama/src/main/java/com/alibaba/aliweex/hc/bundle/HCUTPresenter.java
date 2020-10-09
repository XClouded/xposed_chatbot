package com.alibaba.aliweex.hc.bundle;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.aliweex.bundle.UTPresenter;

public class HCUTPresenter extends UTPresenter {
    public HCUTPresenter(Activity activity) {
        super(activity);
    }

    public void pageAppear(String str) {
        super.pageAppear(getPageName(str));
    }

    public void updatePageName(String str) {
        super.updatePageName(getPageName(str));
    }

    private String getPageName(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("wh_pid")) {
            return str;
        }
        return Uri.parse(str).buildUpon().appendPath(Uri.parse(str).getQueryParameter("wh_pid")).toString();
    }
}
