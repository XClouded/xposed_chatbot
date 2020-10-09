package com.taobao.android.dinamic.constructor;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ColorTools {
    public static int parseColor(String str, int i) {
        if (str == null || str.length() <= 0) {
            return i;
        }
        String lowerCase = str.toLowerCase(Locale.SIMPLIFIED_CHINESE);
        StringBuilder sb = new StringBuilder("#");
        int i2 = 1;
        while (i2 < 9 && i2 < lowerCase.length()) {
            char charAt = lowerCase.charAt(i2);
            if ((charAt >= '0' && charAt <= '9') || (charAt >= 'a' && charAt <= 'f')) {
                sb.append(charAt);
            }
            i2++;
        }
        String sb2 = sb.toString();
        if (sb2.length() == 7 || sb2.length() == 9) {
            return Color.parseColor(sb2);
        }
        return i;
    }

    public static GradientDrawable getGradientDrawable(List<String> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        if (list.size() == 1) {
            list.add(list.get(0));
        }
        return getGradientDrawable((String[]) list.toArray(new String[list.size()]));
    }

    public static GradientDrawable getGradientDrawable(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        if (strArr.length == 1) {
            strArr = new String[]{strArr[0], strArr[0]};
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(Integer.valueOf(parseColor(str instanceof String ? str : null, 0)));
        }
        int size = arrayList.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = ((Integer) arrayList.get(i)).intValue();
        }
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, iArr);
    }
}
