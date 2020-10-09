package com.alimama.unionwl.utils;

import android.app.Application;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class UiUtils {
    private static Application sApplication;
    private static DisplayMetrics sDisplayMetrics;
    private static int sStatusbarHeight;

    public static void init(Application application) {
        sApplication = application;
    }

    public static DisplayMetrics getScreenMetrics() {
        if (sDisplayMetrics != null) {
            return sDisplayMetrics;
        }
        Display defaultDisplay = ((WindowManager) sApplication.getSystemService("window")).getDefaultDisplay();
        sDisplayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(sDisplayMetrics);
        return sDisplayMetrics;
    }

    public static float applyDimension(int i, float f) {
        return TypedValue.applyDimension(i, f, getScreenMetrics());
    }

    public static int getStatusbarHeight() {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            sStatusbarHeight = sApplication.getResources().getDimensionPixelSize(((Integer) cls.getField("status_bar_height").get(cls.newInstance())).intValue());
        } catch (Exception unused) {
        }
        return sStatusbarHeight;
    }

    public static void showToast(int i) {
        Toast makeText = Toast.makeText(sApplication, i, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public static void showToast(String str) {
        Toast makeText = Toast.makeText(sApplication, str, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public static <T> T getTag(View view, Class<T> cls) {
        if (view == null || view.getTag() == null || !cls.isInstance(view.getTag())) {
            return null;
        }
        return view.getTag();
    }

    public static int[] getGradientColor(int i, int i2, float f) {
        int red = Color.red(i);
        int red2 = Color.red(i2);
        int i3 = ((int) (((float) (red - red2)) * f)) + red2;
        int i4 = ((int) (((float) (red2 - red)) * f)) + red;
        int green = Color.green(i);
        int green2 = Color.green(i2);
        int i5 = ((int) (((float) (green - green2)) * f)) + green2;
        int i6 = ((int) (((float) (green2 - green)) * f)) + green;
        int blue = Color.blue(i);
        int blue2 = Color.blue(i2);
        return new int[]{Color.argb(255, i3, i5, ((int) (((float) (blue - blue2)) * f)) + blue2), Color.argb(255, i4, i6, ((int) (((float) (blue2 - blue)) * f)) + blue)};
    }

    public static int parseColor(String str) {
        if (!TextUtils.isEmpty(str)) {
            return parseColor(str, str.toUpperCase().startsWith("0X"));
        }
        return 0;
    }

    public static int parseColor(String str, boolean z) {
        if (z) {
            str = "#" + str.substring("0x".length());
        }
        try {
            return Color.parseColor(str);
        } catch (Exception unused) {
            return Color.parseColor("#FF000000");
        }
    }

    public static void display(TextView textView, CharSequence charSequence) {
        if (textView == null) {
            return;
        }
        if (TextUtils.isEmpty(charSequence)) {
            textView.setVisibility(8);
            return;
        }
        textView.setVisibility(0);
        textView.setText(charSequence);
    }

    public static CharSequence getSpanStringForDigtalSubString(String str, String str2, String str3) {
        int indexOf;
        if (TextUtils.isEmpty(str3) || (indexOf = str.indexOf(str2)) == -1) {
            return str;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str.replace("{{num}}", str3));
        spannableStringBuilder.clearSpans();
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF2AD")), indexOf, str3.length() + indexOf, 34);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(14, true), indexOf, str3.length() + indexOf, 34);
        spannableStringBuilder.setSpan(new StyleSpan(1), indexOf, str3.length() + indexOf, 34);
        return spannableStringBuilder;
    }
}
