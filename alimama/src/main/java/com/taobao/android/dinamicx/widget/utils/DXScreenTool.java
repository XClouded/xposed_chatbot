package com.taobao.android.dinamicx.widget.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.widget.DXTextViewWidgetNode;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.HashMap;
import java.util.Map;

public class DXScreenTool {
    public static final int DEFAULT_HEIGHT_SPEC = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(8388607, 0);
    public static int DEFAULT_WIDTH_SPEC = 0;
    private static float DENSITY = -1.0f;
    private static final String SUFFIX_AP = "ap";
    private static final String SUFFIX_NP = "np";
    private static final int WIDTH_REFER = 375;
    private static int WIDTH_SCREEN = -1;
    private static Map<String, Integer> cacheMap = new HashMap();
    private static int globalOrientation;
    private static boolean hasSetGlobalOrientation;

    public static int getPx(Context context, String str, int i) {
        int i2;
        if (TextUtils.isEmpty(str)) {
            if (DinamicXEngine.isDebug()) {
                DXLog.d("DinamicX", "size属性为空字符串");
            }
            return i;
        } else if (cacheMap.containsKey(str)) {
            return cacheMap.get(str).intValue();
        } else {
            try {
                if (str.contains("np")) {
                    i2 = dip2px(context, Float.valueOf(Float.parseFloat(str.replace("np", ""))).floatValue());
                } else if (str.contains("ap")) {
                    i2 = ap2px(context, Float.valueOf(Float.parseFloat(str.replace("ap", ""))).floatValue());
                } else {
                    i2 = ap2px(context, Float.parseFloat(str));
                }
                int i3 = i2;
                cacheMap.put(str, Integer.valueOf(i3));
                return i3;
            } catch (NumberFormatException unused) {
                if (!DinamicXEngine.isDebug()) {
                    return i;
                }
                DXLog.w("DinamicX", str, "写法错误，解析出错");
                return i;
            }
        }
    }

    public static int dip2px(Context context, float f) {
        return Math.round(f * getDensity(context));
    }

    public static int ap2px(Context context, float f) {
        return Math.round(((float) getScreenWidth(context)) * (f / 375.0f));
    }

    public static int px2ap(Context context, float f) {
        return Math.round((f * 375.0f) / ((float) getScreenWidth(context)));
    }

    public static float getDensity(Context context) {
        return getDensity(context, false);
    }

    static float getDensity(Context context, boolean z) {
        if (DENSITY < 0.0f || z) {
            DENSITY = context.getResources().getDisplayMetrics().density;
        }
        return DENSITY;
    }

    public static int getScreenWidth(Context context) {
        return getScreenWidth(context, false);
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics;
        if (context == null || (displayMetrics = context.getResources().getDisplayMetrics()) == null) {
            return 0;
        }
        return displayMetrics.heightPixels;
    }

    static int getScreenWidth(Context context, boolean z) {
        if (WIDTH_SCREEN < 0 || z) {
            if (context == null || context.getResources() == null) {
                return 0;
            }
            Configuration configuration = context.getResources().getConfiguration();
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics == null) {
                return 0;
            }
            if (!hasSetGlobalOrientation || globalOrientation == 3) {
                if (configuration != null && configuration.orientation == 1) {
                    WIDTH_SCREEN = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
                } else if (configuration == null || configuration.orientation != 2) {
                    WIDTH_SCREEN = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
                } else {
                    WIDTH_SCREEN = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
                }
            } else if (globalOrientation == 1) {
                WIDTH_SCREEN = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else if (globalOrientation == 2) {
                WIDTH_SCREEN = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
            }
        }
        return WIDTH_SCREEN;
    }

    public static int getDefaultWidthSpec() {
        return getDefaultWidthSpec(false);
    }

    static int getDefaultWidthSpec(boolean z) {
        if ((DEFAULT_WIDTH_SPEC == 0 || z) && DinamicXEngine.getApplicationContext() != null) {
            DEFAULT_WIDTH_SPEC = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(getScreenWidth(DinamicXEngine.getApplicationContext()), 1073741824);
        }
        return DEFAULT_WIDTH_SPEC;
    }

    public static int getDefaultHeightSpec() {
        return DEFAULT_HEIGHT_SPEC;
    }

    public static void forceResetScreenSize(boolean z) {
        int i = WIDTH_SCREEN;
        if (DinamicXEngine.getApplicationContext() == null) {
            return;
        }
        if (i != getScreenWidth(DinamicXEngine.getApplicationContext(), true) || z) {
            getDefaultWidthSpec(true);
            getDensity(DinamicXEngine.getApplicationContext(), true);
            cacheMap.clear();
            DXTextViewWidgetNode.clearStaticField();
        }
    }

    public static void _setGlobalOrientation(int i) {
        if (!hasSetGlobalOrientation) {
            globalOrientation = i;
            hasSetGlobalOrientation = true;
        }
    }
}
