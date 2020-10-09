package com.ali.user.mobile.utils;

import android.graphics.Typeface;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;

public class FontUtil {
    private static final String FONT_TTF_MEMBER = "alimember_iconfont.ttf";
    private static Typeface typeface;

    public static Typeface getDefaultFont() {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(DataProviderFactory.getApplicationContext().getAssets(), FONT_TTF_MEMBER);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return typeface;
    }
}
