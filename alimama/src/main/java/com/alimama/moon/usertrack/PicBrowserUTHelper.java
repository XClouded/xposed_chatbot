package com.alimama.moon.usertrack;

import android.text.TextUtils;

public class PicBrowserUTHelper {
    public static final String PAGE_NAME = "Page_pic_browser";
    public static final String PRE_CONTROL_NAME = "pic_browser_index";

    public static void viewImageOnPicBrowser(String str) {
        if (!TextUtils.isEmpty(str)) {
            BaseUTHelper.sendControlHit(PAGE_NAME, PRE_CONTROL_NAME + str);
        }
    }
}
