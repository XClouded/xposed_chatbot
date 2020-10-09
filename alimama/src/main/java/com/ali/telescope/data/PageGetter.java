package com.ali.telescope.data;

import android.app.Activity;
import com.ali.telescope.base.plugin.INameConverter;
import com.ali.telescope.util.StringUtils;

public class PageGetter {
    public static String getPageName(Activity activity, INameConverter iNameConverter) {
        if (activity == null || iNameConverter == null) {
            return "";
        }
        String convert = iNameConverter.convert(activity);
        return StringUtils.isEmpty(convert) ? activity.getClass().getName() : convert;
    }

    public static String getPageHashCode(Activity activity) {
        return Integer.toHexString(activity.hashCode());
    }
}
