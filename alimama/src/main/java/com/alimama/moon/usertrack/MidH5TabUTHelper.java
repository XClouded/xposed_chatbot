package com.alimama.moon.usertrack;

import android.text.TextUtils;

public class MidH5TabUTHelper {
    public static final String PAGE_NAME = "Page_mid_h5_tab";
    public static final String REFRESH_MID_H5_PAGE_CONTROL_NAME = "refresh_mid_h5_page_";
    public static final String RENDER_MID_TAB_CONTROL_NAME = "render_mid_tab_";

    public static void midH5PageOperate(String str) {
        if (!TextUtils.isEmpty(str)) {
            BaseUTHelper.sendControlHit(PAGE_NAME, str);
        }
    }
}
