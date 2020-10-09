package com.taobao.weex.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.taobao.android.festival.FestivalMgr;
import com.taobao.uikit.extend.feature.view.TIconFontTextView;
import com.taobao.weex.adapter.R;

public class WXTBUtil {
    private static final String TAG = "WXTBUtil";
    private static boolean isSupportSmartBar = isSupportSmartBar();

    @Deprecated
    public static int getActionBarHeight(Context context) {
        return 0;
    }

    @Deprecated
    public static int getDisplayHeight(AppCompatActivity appCompatActivity) {
        return 0;
    }

    @Deprecated
    public static int getDisplayWidth(AppCompatActivity appCompatActivity) {
        return 0;
    }

    @Deprecated
    private static int getStatusBarHeight(AppCompatActivity appCompatActivity) {
        return 0;
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

    public static final int getIconFontId(String str) {
        Class<R.string> cls = R.string.class;
        try {
            return cls.getDeclaredField("uik_icon_" + str).getInt((Object) null);
        } catch (SecurityException e) {
            e.printStackTrace();
            return 0;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return 0;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return 0;
        }
    }

    public static BitmapDrawable getIconFontDrawable(int i, Context context) {
        TIconFontTextView tIconFontTextView = new TIconFontTextView(context);
        tIconFontTextView.setText(i);
        tIconFontTextView.setTextSize(20.0f);
        tIconFontTextView.setTextColor(Color.parseColor("#F5A623"));
        tIconFontTextView.setTypeface(Typeface.createFromAsset(context.getAssets(), "uik_iconfont.ttf"));
        return new BitmapDrawable(context.getResources(), convertViewToBitmap(tIconFontTextView));
    }

    public static boolean isNav2H5(Context context, String str) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(str));
        intent.setPackage(context.getPackageName());
        intent.setAction("android.intent.action.VIEW");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveActivity == null) {
            TaoLog.i("isNav2H5", "isNav2H5 : false");
            return false;
        } else if ("com.taobao.browser.BrowserActivity".contains(resolveActivity.activityInfo.name)) {
            TaoLog.i("isNav2H5", "isNav2H5 : true");
            return true;
        } else {
            TaoLog.i("isNav2H5", "isNav2H5 : false");
            return false;
        }
    }

    public static boolean hasFestival() {
        try {
            String str = (String) FestivalMgr.getInstance().getFestivalStyle().get("isFestivalOn");
            if (TextUtils.isEmpty(str) || Integer.valueOf(str).intValue() != 1) {
                return false;
            }
            return true;
        } catch (RuntimeException e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            return false;
        }
    }

    public static int compareVersion(String str, String str2) {
        if (str.equals(str2)) {
            return 0;
        }
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int length = split.length;
        int length2 = split2.length;
        int i = length > length2 ? length2 : length;
        int i2 = 0;
        while (i2 < i) {
            if (split[i2].equals(split2[i2])) {
                i2++;
            } else if (Integer.parseInt(split[i2]) > Integer.parseInt(split2[i2])) {
                return 1;
            } else {
                return -1;
            }
        }
        if (length == length2) {
            return 0;
        }
        if (length > length2) {
            return 1;
        }
        return -1;
    }
}
