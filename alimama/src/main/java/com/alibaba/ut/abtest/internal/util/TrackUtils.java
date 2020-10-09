package com.alibaba.ut.abtest.internal.util;

import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.ABConstants;

public final class TrackUtils {
    private static final String TAG = "TrackUtils";

    private TrackUtils() {
    }

    public static String generateAbTrackId(long j, long j2) {
        return ABConstants.BasicConstants.TRACK_PREFIX + j + "_" + j2;
    }

    public static String[] splitAbTrackId(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split("\\.");
    }

    public static String generateUTPageObjectKey(Object obj) {
        String str;
        if (obj instanceof String) {
            str = (String) obj;
        } else {
            str = obj.getClass().getSimpleName();
        }
        int hashCode = obj.hashCode();
        return str + hashCode;
    }
}
