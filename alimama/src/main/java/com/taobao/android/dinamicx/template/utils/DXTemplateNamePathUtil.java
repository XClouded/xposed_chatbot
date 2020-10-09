package com.taobao.android.dinamicx.template.utils;

import android.text.TextUtils;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;

public class DXTemplateNamePathUtil {
    public static final String ASSET_DIR = "template/";
    public static final String ASSET_PRESET_TEMPLATE_INFOLIST = "/presetTemplateInfos.json";
    public static final String ASSET_PRESET_TEMPLATE_OTHER = "other";
    public static final String ASSET_PRESET_TEMPLATE_VERSION = "version";
    public static final String DB_NAME = "dinamicx";
    public static final String DEFAULT_ROOT_DIR = "dinamicx/";
    public static final char DIR = '/';
    public static final String DX_MAIN_TEMPLATE_NAME = "main.dx";

    public static boolean isValid(String str, DXTemplateItem dXTemplateItem) {
        return isValid(dXTemplateItem) && !TextUtils.isEmpty(str);
    }

    public static boolean isValid(DXTemplateItem dXTemplateItem) {
        return dXTemplateItem != null && !TextUtils.isEmpty(dXTemplateItem.name) && dXTemplateItem.version > -1;
    }

    public static long findMaxVersion(String[] strArr) {
        long j = -1;
        if (strArr == null || strArr.length < 1) {
            return -1;
        }
        int length = strArr.length;
        if (length == 1) {
            try {
                return Long.valueOf(strArr[0]).longValue();
            } catch (NumberFormatException unused) {
                return -1;
            }
        } else {
            long j2 = -1;
            for (int i = 0; i < length; i++) {
                try {
                    j2 = Long.valueOf(strArr[i]).longValue();
                } catch (NumberFormatException unused2) {
                }
                if (j2 > j) {
                    j = j2;
                }
            }
            return j;
        }
    }
}
