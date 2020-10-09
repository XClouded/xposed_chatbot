package com.alimama.moon.utils;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.taffy.core.util.lang.CharSequenceUtil;
import com.taobao.android.dinamic.DinamicConstant;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringUtil {
    private static final String RMB_SYMBOL = "¥ ";

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean equalsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || charSequence.length() != charSequence2.length()) {
            return false;
        }
        return CharSequenceUtil.regionMatches(charSequence, 0, charSequence2, 0, charSequence.length(), true);
    }

    public static boolean isBlank(CharSequence charSequence) {
        int length;
        if (charSequence == null || (length = charSequence.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence);
    }

    public static SpannableStringBuilder getBoldStylePrice(@NonNull String str, int i, int i2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(RMB_SYMBOL);
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i, true), 0, RMB_SYMBOL.length(), 33);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i2, true), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new StyleSpan(1), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder getBoldStylePriceWithDecimal(@NonNull String str, int i, int i2, int i3) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(RMB_SYMBOL);
        spannableStringBuilder.append(str);
        try {
            if (str.contains(".")) {
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i, true), 0, RMB_SYMBOL.length(), 33);
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i2, true), RMB_SYMBOL.length(), RMB_SYMBOL.length() + str.indexOf(46), 33);
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i3, true), RMB_SYMBOL.length() + str.indexOf(46), spannableStringBuilder.length(), 33);
            } else {
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i, true), 0, RMB_SYMBOL.length(), 33);
                spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i2, true), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
            }
        } catch (Exception unused) {
        }
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder getStrikeThroughPrice(String str, int i, int i2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(RMB_SYMBOL);
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i, true), 0, RMB_SYMBOL.length(), 33);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(i2, true), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new StrikethroughSpan(), RMB_SYMBOL.length(), spannableStringBuilder.length(), 33);
        return spannableStringBuilder;
    }

    @NonNull
    public static String twoDecimalStr(Double d) {
        return (d == null || Double.isNaN(d.doubleValue())) ? "" : new DecimalFormat("#0.00").format(d);
    }

    public static String twoDecimalStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return new DecimalFormat("0.00").format(new BigDecimal(str));
    }

    public static String deleteDecimalZeroStr(String str) {
        return BigDecimal.valueOf(Double.parseDouble(str)).stripTrailingZeros().toPlainString();
    }

    public static String sellOutNumStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0件";
        }
        Double valueOf = Double.valueOf(Double.parseDouble(str));
        if (valueOf.doubleValue() >= 10000.0d) {
            Double valueOf2 = Double.valueOf(valueOf.doubleValue() / 10000.0d);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(valueOf2) + "万件";
        }
        return str + "件";
    }

    @NonNull
    public static String intValueStr(Double d) {
        return (d == null || Double.isNaN(d.doubleValue())) ? "" : String.valueOf(d.intValue());
    }

    @Nullable
    public static String maskPrivacyInfo(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int indexOf = str.indexOf(DinamicConstant.DINAMIC_PREFIX_AT);
        if (indexOf <= 0) {
            return maskPrivacyInfoInternal(str);
        }
        return maskPrivacyInfoInternal(str.substring(0, indexOf)) + str.substring(indexOf);
    }

    @Nullable
    private static String maskPrivacyInfoInternal(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int length = str.length();
        if (length <= 2) {
            return str;
        }
        int i = length < 5 ? 1 : 3;
        int i2 = length - 4;
        if (i2 <= i) {
            i2 = length - 1;
        }
        return String.format("%s****%s", new Object[]{str.substring(0, i), str.substring(i2, length)});
    }

    public static <T extends CharSequence> T optVal(T t, T t2) {
        return TextUtils.isEmpty(t) ? t2 : t;
    }

    public static int toInt(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static long toLong(String str, long j) {
        if (TextUtils.isEmpty(str)) {
            return j;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return j;
        }
    }

    public static float toFloat(String str, float f) {
        if (TextUtils.isEmpty(str)) {
            return f;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return f;
        }
    }
}
