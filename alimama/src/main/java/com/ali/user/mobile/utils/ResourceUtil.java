package com.ali.user.mobile.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;

public class ResourceUtil {
    public static String getStringById(String str) {
        try {
            int stringId = getStringId(str);
            return stringId > 0 ? DataProviderFactory.getApplicationContext().getResources().getString(stringId) : "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static View findViewById(View view, String str) {
        if (view == null) {
            return null;
        }
        return view.findViewById(getViewId(str));
    }

    public static Drawable findDrawableById(String str) {
        return DataProviderFactory.getApplicationContext().getResources().getDrawable(getDrawableId(str));
    }

    public static int getViewId(String str) {
        return getIdentifierByName(str, "id");
    }

    public static int getStringId(String str) {
        return getIdentifierByName(str, "string");
    }

    public static int getDrawableId(String str) {
        return getIdentifierByName(str, "drawable");
    }

    public static int getLayoutId(String str) {
        return getIdentifierByName(str, "layout");
    }

    private static int getIdentifierByName(String str, String str2) {
        int identifier = DataProviderFactory.getApplicationContext().getResources().getIdentifier(str, str2, DataProviderFactory.getApplicationContext().getPackageName());
        if (identifier != 0) {
            return identifier;
        }
        return DataProviderFactory.getApplicationContext().getResources().getIdentifier(str, str2, DataProviderFactory.getDataProvider().getAppName());
    }
}
