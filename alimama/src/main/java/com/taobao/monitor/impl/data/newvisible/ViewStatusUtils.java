package com.taobao.monitor.impl.data.newvisible;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import com.taobao.monitor.impl.data.ViewUtils;

public class ViewStatusUtils {
    public static String createViewType(View view) {
        if (view != null) {
            return view.getClass().getSimpleName();
        }
        throw new IllegalArgumentException();
    }

    public static String createViewKey(View view) {
        if (view != null) {
            return "" + view.hashCode();
        }
        throw new IllegalArgumentException();
    }

    public static String createViewStatus(View view) {
        if (view != null) {
            return status(view);
        }
        throw new IllegalArgumentException();
    }

    public static String createViewLocation(View view, View view2) {
        if (view2 == null || view == null) {
            throw new IllegalArgumentException();
        }
        int[] absLocationInWindow = ViewUtils.getAbsLocationInWindow(view2, view);
        int max = Math.max(0, absLocationInWindow[0]);
        int min = Math.min(ViewUtils.screenWidth, absLocationInWindow[0] + view2.getWidth());
        int max2 = Math.max(0, absLocationInWindow[1]);
        int min2 = Math.min(ViewUtils.screenHeight, absLocationInWindow[1] + view2.getHeight());
        return "_" + max2 + "_" + min + "_" + min2 + "_" + max;
    }

    public static String createViewPath(View view, View view2) {
        if (view2 == null || view == null) {
            throw new IllegalArgumentException();
        }
        StringBuilder sb = new StringBuilder();
        while (view2 != null && view2 != view) {
            sb.append(view2.getClass().getSimpleName());
            if (!(view2.getParent() instanceof View)) {
                break;
            }
            view2 = (View) view2.getParent();
        }
        return sb.toString();
    }

    private static String status(View view) {
        StringBuilder sb = new StringBuilder();
        Drawable background = view.getBackground();
        if (Build.VERSION.SDK_INT >= 23 && (background instanceof DrawableWrapper)) {
            background = ((DrawableWrapper) background).getDrawable();
        }
        if (isValidDrawable(background)) {
            sb.append(background.getClass().getSimpleName());
        }
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (Build.VERSION.SDK_INT >= 23 && (drawable instanceof DrawableWrapper)) {
                drawable = ((DrawableWrapper) drawable).getDrawable();
            }
            if (isValidDrawable(drawable)) {
                sb.append(drawable.getClass().getSimpleName());
            }
        }
        return sb.toString();
    }

    private static boolean isValidDrawable(Drawable drawable) {
        return drawable != null && !drawable.getClass().getSimpleName().equals("BorderDrawable") && !(drawable instanceof ColorDrawable);
    }
}
