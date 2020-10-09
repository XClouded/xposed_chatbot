package com.taobao.monitor.impl.data;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.taobao.monitor.impl.util.DeviceUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CanvasCalculator implements ICalculator {
    private static final String TAG = "DrawCalculator2";
    private static final float TARGET_PERCENT = 0.8f;
    private final View container;
    private HashSet<Drawable> drawableCache = new HashSet<>();
    private boolean editFocus = false;
    private final View rootView;

    public CanvasCalculator(View view, View view2) {
        this.container = view;
        this.rootView = view2;
    }

    private float drawCalculate(View view, List<ViewInfo> list, View view2) {
        View view3;
        if (!ViewUtils.isInVisibleArea(view, view2)) {
            return 0.0f;
        }
        if (view.getHeight() < ViewUtils.screenHeight / 20) {
            return 1.0f;
        }
        if (view.getVisibility() != 0 || (view instanceof ViewStub)) {
            return 0.0f;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup instanceof WebView) {
                if (DefaultWebView.INSTANCE.webViewProgress((WebView) viewGroup) == 100) {
                    return 1.0f;
                }
                return 0.0f;
            } else if (!WebViewProxy.INSTANCE.isWebView(viewGroup)) {
                View[] children = ViewUtils.getChildren(viewGroup);
                if (children == null) {
                    return 0.0f;
                }
                int length = children.length;
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                while (i < length && (view3 = children[i]) != null) {
                    i2++;
                    ArrayList arrayList = new ArrayList();
                    if (drawCalculate(view3, arrayList, view2) > TARGET_PERCENT) {
                        i3++;
                        list.add(ViewInfo.obtain(view3, view2));
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            ((ViewInfo) it.next()).recycle();
                        }
                    } else {
                        list.addAll(arrayList);
                    }
                    i++;
                }
                if (view.getHeight() < ViewUtils.screenHeight / 8 && (((viewGroup instanceof LinearLayout) || (viewGroup instanceof RelativeLayout)) && i2 == i3 && i2 != 0)) {
                    return 1.0f;
                }
                float calculate = new LineTreeCalculator(DeviceUtils.dip2px(30)).calculate(viewGroup, list, view2);
                if (calculate > TARGET_PERCENT) {
                    return 1.0f;
                }
                return calculate;
            } else if (WebViewProxy.INSTANCE.webViewProgress(viewGroup) == 100) {
                return 1.0f;
            } else {
                return 0.0f;
            }
        } else if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (Build.VERSION.SDK_INT >= 23 && (drawable instanceof DrawableWrapper)) {
                drawable = ((DrawableWrapper) drawable).getDrawable();
            }
            if (!isValidDrawable(drawable) || this.drawableCache.contains(drawable)) {
                Drawable background = view.getBackground();
                if (Build.VERSION.SDK_INT >= 23 && (background instanceof DrawableWrapper)) {
                    background = ((DrawableWrapper) drawable).getDrawable();
                }
                if (!isValidDrawable(background) || this.drawableCache.contains(background)) {
                    return 0.0f;
                }
                this.drawableCache.add(background);
                return 1.0f;
            }
            this.drawableCache.add(drawable);
            return 1.0f;
        } else if (!(view instanceof TextView)) {
            return 1.0f;
        } else {
            if (view instanceof EditText) {
                this.editFocus = view.isFocusable();
                return 1.0f;
            } else if (TextUtils.isEmpty(((TextView) view).getText().toString())) {
                return 0.0f;
            } else {
                return 1.0f;
            }
        }
    }

    private boolean isValidDrawable(Drawable drawable) {
        return (drawable instanceof BitmapDrawable) || (drawable instanceof NinePatchDrawable) || (drawable instanceof AnimationDrawable) || (drawable instanceof ShapeDrawable) || (drawable instanceof PictureDrawable);
    }

    public float calculate() {
        ArrayList arrayList = new ArrayList();
        float drawCalculate = drawCalculate(this.container, arrayList, this.rootView);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((ViewInfo) it.next()).recycle();
        }
        this.drawableCache.clear();
        if (this.editFocus) {
            return 1.0f;
        }
        return drawCalculate;
    }
}
