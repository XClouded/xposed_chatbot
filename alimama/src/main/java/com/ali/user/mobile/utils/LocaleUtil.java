package com.ali.user.mobile.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtil {
    public static Locale convertLocale(Locale locale) {
        if (locale == null) {
            return locale;
        }
        List<String> asList = Arrays.asList(new String[]{"en_US", "pt_PT", "ru_RU", "es_ES", "tr_TR", "it_IT", "fr_FR", "de_DE", "nl_NL", "th_TH", "ja_JP", "vi_VN", "ko_KR", "ar_SA", "in_ID", "iw_IL", "zh_CN", "zh_TW"});
        String locale2 = locale.toString();
        if (asList.contains(locale2)) {
            return locale;
        }
        for (String str : asList) {
            if (str.startsWith(locale2)) {
                String[] split = str.split("_");
                return new Locale(split[0], split[1]);
            }
        }
        return null;
    }
}
