package com.alibaba.aliweex.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.aliweex.AliWeex;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;
import org.android.agoo.common.AgooConstants;

public class WXUtil {
    private static String APP_packageName = null;
    public static final String ERROR_BUNDLE_URL = "http://taobao.com?_wx_tpl=http://h5.m.taobao.com/weex/render/error.js";
    public static final String ERROR_RENDER_URL = "http://h5.m.taobao.com/weex/render/error.js";
    private static final String TAG = "WXTBUtil";
    private static int atlas_status = 0;
    private static boolean isAirGrey = false;
    private static boolean isSupportSmartBar = isSupportSmartBar();

    public static String getZCacheFromUrl(String str, String str2) {
        try {
            return ZipAppUtils.getStreamByUrl(str, str2);
        } catch (Throwable th) {
            WXLogUtils.e("TBWXSDKEngine", "getZCacheFromUrl:" + th.getMessage());
            return null;
        }
    }

    public static int getDisplayWidth(Activity activity) {
        if (activity == null || activity.getWindowManager() == null || activity.getWindowManager().getDefaultDisplay() == null) {
            return 0;
        }
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int getDisplayHeight(Activity activity) {
        int i;
        if (activity == null || activity.getWindowManager() == null || activity.getWindowManager().getDefaultDisplay() == null) {
            i = 0;
        } else {
            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(point);
            i = point.y;
        }
        if (activity.getActionBar() != null) {
            int height = activity.getActionBar().getHeight();
            if (height == 0) {
                height = (int) activity.obtainStyledAttributes(new int[]{16843499}).getDimension(0, 0.0f);
            }
            WXLogUtils.d(TAG, "actionbar:" + height);
            i -= height;
        }
        int statusBarHeight = getStatusBarHeight(activity);
        WXLogUtils.d(TAG, "status:" + statusBarHeight);
        int i2 = i - statusBarHeight;
        WXLogUtils.d(TAG, "height-24:" + i2);
        return i2;
    }

    private static int getStatusBarHeight(Activity activity) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return activity.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getSmartBarHeight(Activity activity) {
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) {
            return 0;
        }
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return activity.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("mz_action_button_min_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            actionBar.getHeight();
            return 0;
        }
    }

    private static boolean isSupportSmartBar() {
        try {
            return Build.class.getMethod("hasSmartBar", new Class[0]) != null;
        } catch (Exception unused) {
            return false;
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = ((float) i) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static final int getIconFontId(Context context, String str) {
        if (context == null || context.getResources() == null) {
            return 0;
        }
        Resources resources = context.getResources();
        return resources.getIdentifier("uik_icon_" + str, "string", context.getPackageName());
    }

    public static int getActionBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(new int[]{16843499});
        int dimension = (int) obtainStyledAttributes.getDimension(0, 0.0f);
        obtainStyledAttributes.recycle();
        return dimension;
    }

    public static boolean classLoadable(String str) {
        Class<?> cls;
        try {
            cls = Class.forName(str);
        } catch (ClassNotFoundException unused) {
            cls = null;
        }
        return cls != null;
    }

    @Deprecated
    public static boolean hasAtlas() {
        if (atlas_status == 0) {
            try {
                String str = WXEnvironment.getCustomOptions().get("hasAtlas");
                if (str != null) {
                    atlas_status = Boolean.valueOf(str).booleanValue() ? 1 : -1;
                } else {
                    Class.forName("android.taobao.atlas.framework.Atlas");
                    atlas_status = 1;
                }
            } catch (Exception e) {
                WXLogUtils.e("AtlasCheck", (Throwable) e);
                atlas_status = -1;
            }
        }
        if (atlas_status > 0) {
            return true;
        }
        return false;
    }

    public static boolean isTaobao() {
        if (TextUtils.isEmpty(APP_packageName)) {
            Application application = AliWeex.getInstance().getApplication();
            if (application == null) {
                return false;
            }
            APP_packageName = application.getPackageName();
        }
        return AgooConstants.TAOBAO_PACKAGE.equals(APP_packageName);
    }

    public static void setAirGrey(boolean z) {
        if (WXEnvironment.isApkDebugable()) {
            isAirGrey = true;
        }
    }

    public static boolean isAirGrey() {
        return isAirGrey;
    }
}
