package com.alibaba.aliweex.hc.bundle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.alibaba.aliweex.hc.R;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.weex.utils.WXLogUtils;

public class WXActionBarMenuItem {
    public Intent data = null;
    public String href = "";
    public Bitmap iconBitmap = null;
    public int iconFontId = -1;
    public int iconResId = -1;
    public INavigationBarModuleAdapter.OnItemClickListener itemClickListener;
    public boolean stretch = false;
    public String title = "";

    public boolean setIconBitmap(String str, float f) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String replace = str.replace(' ', '+');
        try {
            byte[] decode = Base64.decode(replace, 0);
            this.iconBitmap = WXUtil.resizeBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length), (int) (f / 2.0f));
            if (this.iconBitmap == null) {
                WXLogUtils.e("TBWXActionBarMenuItem", replace + "is not a base64 bitmap data");
            }
        } catch (IllegalArgumentException unused) {
            WXLogUtils.e("TBWXActionBarMenuItem", "base64 to byteArr decode fail");
        }
        if (this.iconBitmap != null) {
            return true;
        }
        return false;
    }

    public int setIconResId(String str) {
        int i = "tb_icon_navibar_default_right".equals(str) ? R.drawable.tb_icon_navibar_default_right : -1;
        this.iconResId = i;
        return i;
    }

    public int setIconFontId(Context context, String str) {
        this.iconFontId = WXUtil.getIconFontId(context, str);
        return this.iconFontId;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
