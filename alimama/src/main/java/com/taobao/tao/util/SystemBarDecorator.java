package com.taobao.tao.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import com.rd.animation.ColorAnimation;
import java.lang.ref.WeakReference;

public class SystemBarDecorator {
    private static final String ENABLE_KEY = "immersive_status_enable";
    private static final String GROUP_NAME = "message_box_switch";
    private static final int MASK_COLOR = 805306368;
    private static final String TAG = "SystemBarDecorator";
    private WeakReference<Activity> mActivity;
    private SystemBarConfig mConfig;
    private boolean mNavBarAvailable = false;
    private boolean mStatusBarAvailable = true;
    private View mStatusBarTintView;
    private int mType;

    @TargetApi(19)
    public SystemBarDecorator(@NonNull Activity activity) {
        this.mActivity = new WeakReference<>(activity);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mType = 2;
        } else if (Build.VERSION.SDK_INT < 19) {
            this.mType = 0;
            this.mStatusBarAvailable = false;
        }
    }

    public SystemBarConfig getConfig() {
        if (this.mActivity == null || this.mActivity.get() == null) {
            return null;
        }
        if (this.mConfig == null) {
            this.mConfig = new SystemBarConfig((Activity) this.mActivity.get(), this.mStatusBarAvailable, this.mNavBarAvailable);
        }
        return this.mConfig;
    }

    public int getType() {
        return this.mType;
    }

    public boolean isStatusBarAvailable() {
        return this.mStatusBarAvailable;
    }

    @Deprecated
    public boolean enableImmersiveStatus(boolean z) {
        return enableImmersiveStatus((String) null, true, z);
    }

    @Deprecated
    public boolean enableImmersiveStatus(@Nullable String str, boolean z) {
        return enableImmersiveStatus(str, true, z);
    }

    @Deprecated
    public boolean enableImmersiveStatus(@Nullable String str, boolean z, boolean z2) {
        if (!this.mStatusBarAvailable || this.mActivity == null || this.mActivity.get() == null) {
            return false;
        }
        Activity activity = (Activity) this.mActivity.get();
        switch (this.mType) {
            case 1:
                break;
            case 2:
                activity.getWindow().clearFlags(201326592);
                activity.getWindow().addFlags(Integer.MIN_VALUE);
                if (z2) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(1024);
                    break;
                }
                break;
            default:
                this.mStatusBarAvailable = false;
                return false;
        }
        setStatusBarColor(str, z);
        return true;
    }

    public void setStatusBarColor(@Nullable String str) {
        setStatusBarColor(str, true);
    }

    public void setStatusBarColor(@Nullable String str, boolean z) {
        int parseColor = Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR);
        if (!TextUtils.isEmpty(str)) {
            parseColor = Color.parseColor(str);
        }
        setStatusBarColor(parseColor, z);
    }

    public void setStatusBarColor(@ColorInt int i, boolean z) {
        if (this.mStatusBarAvailable && this.mActivity != null && this.mActivity.get() != null) {
            Activity activity = (Activity) this.mActivity.get();
            if (z) {
                i = ColorUtils.compositeColors((int) MASK_COLOR, i);
            }
            switch (this.mType) {
                case 2:
                    activity.getWindow().setStatusBarColor(i);
                    return;
                default:
                    return;
            }
        }
    }

    public void enableFitsWindowsOnRoot(boolean z) {
        View childAt;
        if (this.mActivity != null && this.mActivity.get() != null && (childAt = ((ViewGroup) ((Activity) this.mActivity.get()).findViewById(16908290)).getChildAt(0)) != null && z) {
            childAt.setFitsSystemWindows(true);
        }
    }

    public static class SystemBarConfig {
        private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
        private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
        private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
        private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
        private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
        private final int mActionBarHeight;
        private final boolean mInPortrait;
        private final float mSmallestWidthDp;
        private final int mStatusBarHeight;
        private final boolean mTranslucentNavBar;
        private final boolean mTranslucentStatusBar;

        public SystemBarConfig(Activity activity, boolean z, boolean z2) {
            this.mInPortrait = isPortrait(activity);
            this.mSmallestWidthDp = getSmallestWidthDp(activity);
            this.mStatusBarHeight = getStatusBarHeight(activity);
            this.mActionBarHeight = getActionBarHeight(activity);
            this.mTranslucentStatusBar = z;
            this.mTranslucentNavBar = z2;
        }

        @TargetApi(14)
        public static int getActionBarHeight(Context context) {
            if (Build.VERSION.SDK_INT < 14) {
                return 0;
            }
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16843499, typedValue, true);
            return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }

        public static int getStatusBarHeight(Context context) {
            return getInternalDimensionSize(context.getResources(), STATUS_BAR_HEIGHT_RES_NAME);
        }

        private static int getInternalDimensionSize(Resources resources, String str) {
            int identifier = resources.getIdentifier(str, "dimen", "android");
            if (identifier > 0) {
                return resources.getDimensionPixelSize(identifier);
            }
            return 0;
        }

        private float getSmallestWidthDp(Activity activity) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= 17) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            } else {
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            }
            return Math.min(((float) displayMetrics.widthPixels) / displayMetrics.density, ((float) displayMetrics.heightPixels) / displayMetrics.density);
        }

        public static boolean isPortrait(Context context) {
            return context.getResources().getConfiguration().orientation == 1;
        }

        public boolean isNavigationAtBottom() {
            return this.mSmallestWidthDp >= 600.0f || this.mInPortrait;
        }

        public int getStatusBarHeight() {
            return this.mStatusBarHeight;
        }

        public int getActionBarHeight() {
            return this.mActionBarHeight;
        }

        public int getPixelInsetTop(boolean z) {
            int i = 0;
            int i2 = this.mTranslucentStatusBar ? this.mStatusBarHeight : 0;
            if (z) {
                i = this.mActionBarHeight;
            }
            return i2 + i;
        }
    }

    public boolean enableImmersiveStatusBar() {
        return enableImmersiveStatusBar(false);
    }

    public boolean enableImmersiveStatusBar(boolean z) {
        if (this.mActivity == null || this.mActivity.get() == null) {
            return false;
        }
        Window window = ((Activity) this.mActivity.get()).getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window2 = ((Activity) this.mActivity.get()).getWindow();
            if (z) {
                try {
                    window2.clearFlags(201326592);
                    window2.getDecorView().setSystemUiVisibility(9472);
                    window2.addFlags(Integer.MIN_VALUE);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else {
                window2.clearFlags(201334784);
                window2.getDecorView().setSystemUiVisibility(1280);
                window2.addFlags(Integer.MIN_VALUE);
            }
            window2.setStatusBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(67108864);
        }
        if (TBStatusBarUtils.isMIUIDevice()) {
            TBStatusBarUtils.miuiSetStatusBarLightMode((Activity) this.mActivity.get(), z);
            return true;
        } else if (!TBStatusBarUtils.isMeizuDevice()) {
            return true;
        } else {
            TBStatusBarUtils.setStatusBarDarkIcon((Activity) this.mActivity.get(), z);
            return true;
        }
    }

    public static int getStatusBarHeight(Context context) {
        return getSystemComponentDimen(context, "status_bar_height");
    }

    public static int getSystemComponentDimen(Context context, String str) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField(str).get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
