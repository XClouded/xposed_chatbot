package com.alibaba.android.umbrella.link.util;

import androidx.annotation.NonNull;
import com.alibaba.android.umbrella.link.UMStringUtils;
import com.alimamaunion.common.listpage.CommonItemInfo;
import java.util.Random;

public final class UMLaunchId {
    private static final Random RANDOM = new Random();
    private static final String SEPARATOR = "-";
    private static String launchId;

    public static String getLaunchId() {
        if (UMStringUtils.isEmpty(launchId)) {
            synchronized (UMLaunchId.class) {
                if (UMStringUtils.isEmpty(launchId)) {
                    launchId = UMLinkLogUtils.getUtdid() + "-" + UMLinkLogUtils.getLLTimestamp() + RANDOM.nextInt(CommonItemInfo.FOOT_TYPE);
                }
            }
        }
        return launchId;
    }

    public static String createLinkId(@NonNull String str) {
        String str2;
        if (UMStringUtils.isNotEmpty(str)) {
            str2 = "-" + str;
        } else {
            str2 = "";
        }
        return UMLinkLogUtils.getUtdid() + "-" + UMLinkLogUtils.getLLTimestamp() + RANDOM.nextInt(CommonItemInfo.FOOT_TYPE) + str2;
    }
}
