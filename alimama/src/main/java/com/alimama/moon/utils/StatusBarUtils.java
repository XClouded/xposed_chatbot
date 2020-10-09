package com.alimama.moon.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import com.alimama.moon.R;

public class StatusBarUtils {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 0;
    private static final int FAKE_STATUS_BAR_VIEW_ID = 2131689575;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = 2131689576;

    public static void setColor(Activity activity, @ColorInt int i, @IntRange(from = 0, to = 255) int i2, boolean z) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().setStatusBarColor(calculateStatusColor(i, i2));
        }
    }

    private static int calculateStatusColor(@ColorInt int i, int i2) {
        if (i2 == 0) {
            return i;
        }
        float f = 1.0f - (((float) i2) / 255.0f);
        double d = (double) (((float) ((i >> 16) & 255)) * f);
        Double.isNaN(d);
        int i3 = (int) (d + 0.5d);
        double d2 = (double) (((float) ((i >> 8) & 255)) * f);
        Double.isNaN(d2);
        double d3 = (double) (((float) (i & 255)) * f);
        Double.isNaN(d3);
        return ((int) (d3 + 0.5d)) | (i3 << 16) | -16777216 | (((int) (d2 + 0.5d)) << 8);
    }

    @TargetApi(23)
    public static void setStatusBarTextColor(Window window, boolean z) {
        if (window != null) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            decorView.setSystemUiVisibility(z ? systemUiVisibility | 8192 : systemUiVisibility & -8193);
        }
    }

    public static int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().getDecorView().setSystemUiVisibility(1280);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(67108864, 67108864);
        }
    }

    public static void setGradientColor(Activity activity, @DrawableRes int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(67108864);
            ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            View findViewById = viewGroup.findViewById(R.id.statusbarutil_fake_status_bar_view);
            if (findViewById != null) {
                if (findViewById.getVisibility() == 8) {
                    findViewById.setVisibility(0);
                }
                findViewById.setBackgroundResource(i);
            } else {
                viewGroup.addView(createStatusBarView(activity, i));
            }
            setRootView(activity);
        }
    }

    public static void setGradientColorDr(Activity activity, GradientDrawable gradientDrawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(67108864);
            ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            View findViewById = viewGroup.findViewById(R.id.statusbarutil_fake_status_bar_view);
            if (findViewById != null) {
                if (findViewById.getVisibility() == 8) {
                    findViewById.setVisibility(0);
                }
                findViewById.setBackgroundDrawable(gradientDrawable);
            } else {
                viewGroup.addView(createStatusBarViewDr(activity, gradientDrawable));
            }
            setRootView(activity);
        }
    }

    public static void setGradientDrawable(Activity activity, GradientDrawable gradientDrawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(67108864);
            ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            View findViewById = viewGroup.findViewById(R.id.statusbarutil_fake_status_bar_view);
            if (findViewById != null) {
                if (findViewById.getVisibility() == 8) {
                    findViewById.setVisibility(0);
                }
                findViewById.setBackgroundDrawable(gradientDrawable);
            } else {
                viewGroup.addView(createStatusBarViewWithDrawable(activity, gradientDrawable));
            }
            setRootView(activity);
        }
    }

    private static View createStatusBarView(Activity activity, @DrawableRes int i) {
        return createStatusBarView(activity, i, 0);
    }

    private static View createStatusBarViewDr(Activity activity, GradientDrawable gradientDrawable) {
        return createStatusBarViewDr(activity, gradientDrawable, 0);
    }

    private static View createStatusBarView(Activity activity, @DrawableRes int i, int i2) {
        View view = new View(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity)));
        view.setBackgroundResource(i);
        view.setId(R.id.statusbarutil_fake_status_bar_view);
        return view;
    }

    private static View createStatusBarViewDr(Activity activity, GradientDrawable gradientDrawable, int i) {
        View view = new View(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity)));
        view.setBackgroundDrawable(gradientDrawable);
        view.setId(R.id.statusbarutil_fake_status_bar_view);
        return view;
    }

    private static View createStatusBarViewWithDrawable(Activity activity, GradientDrawable gradientDrawable) {
        View view = new View(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity)));
        view.setBackgroundDrawable(gradientDrawable);
        view.setId(R.id.statusbarutil_fake_status_bar_view);
        return view;
    }

    private static void setRootView(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(16908290);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                childAt.setFitsSystemWindows(true);
                ((ViewGroup) childAt).setClipToPadding(true);
            }
        }
    }
}
