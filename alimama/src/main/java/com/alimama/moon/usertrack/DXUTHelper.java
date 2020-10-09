package com.alimama.moon.usertrack;

import android.text.TextUtils;

public class DXUTHelper {
    public static final String DX_BULLETIN_BOARD_PAGE_NAME = "Page_dx_bulletin_board";

    public static void renderDXBulletinBoardSuccess(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            BaseUTHelper.sendControlHit(str, str2);
        }
    }
}
