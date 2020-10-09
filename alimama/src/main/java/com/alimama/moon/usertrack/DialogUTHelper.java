package com.alimama.moon.usertrack;

import android.text.TextUtils;

public class DialogUTHelper {
    public static final String AVATAR_DIALOG_PAGE_NAME = "Page_worship_dialog";
    public static final String CLICK_DIALOG_CLOSE_CONTROL_NAME = "click_dialog_close";
    public static final String CLOSE_PASTE_WECHAT_COMMENTS_AGAIN_DIALOG = "close_goods_dialog_paste_again";
    public static final String CLOSE_PASTE_WECHAT_COMMENTS_DIALOG = "close_goods_dialog_paste";
    public static final String CLOSE_SHARE_WECHAT_COMMENTS_DIALOG = "close_goods_dialog_share";
    public static final String GO_TO_INVITE_CONTROL_DNAME = "go_to_invite";
    public static final String PAGE_NAME = "Page_goods_dialog";
    public static final String PASTE_WECHAT_COMMENTS_AGAIN_CONTROL_NAME = "paste_wechat_comments_again";
    public static final String PASTE_WECHAT_COMMENTS_CONTROL_NAME = "paste_wechat_comments";
    public static final String SHARE_WECHAT_COMMENTS_CONTROL_NAME = "share_wechat_moments";

    public static void clickDialog(String str) {
        if (!TextUtils.isEmpty(str)) {
            BaseUTHelper.sendControlHit(PAGE_NAME, str);
        }
    }

    public static void clickDialog(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            BaseUTHelper.sendControlHit(str, str2);
        }
    }
}
