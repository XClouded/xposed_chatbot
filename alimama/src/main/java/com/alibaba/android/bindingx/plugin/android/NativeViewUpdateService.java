package com.alibaba.android.bindingx.plugin.android;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.WeakRunnable;
import com.alibaba.android.bindingx.core.internal.Utils;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NativeViewUpdateService {
    private static final NOpUpdater EMPTY_INVOKER = new NOpUpdater();
    private static final List<String> LAYOUT_PROPERTIES = Arrays.asList(new String[]{"width", "height", "margin-left", "margin-right", "margin-top", "margin-bottom", "padding-left", "padding-right", "padding-top", "padding-bottom"});
    private static final String LAYOUT_PROPERTY_HEIGHT = "height";
    private static final String LAYOUT_PROPERTY_MARGIN_BOTTOM = "margin-bottom";
    private static final String LAYOUT_PROPERTY_MARGIN_LEFT = "margin-left";
    private static final String LAYOUT_PROPERTY_MARGIN_RIGHT = "margin-right";
    private static final String LAYOUT_PROPERTY_MARGIN_TOP = "margin-top";
    private static final String LAYOUT_PROPERTY_PADDING_BOTTOM = "padding-bottom";
    private static final String LAYOUT_PROPERTY_PADDING_LEFT = "padding-left";
    private static final String LAYOUT_PROPERTY_PADDING_RIGHT = "padding-right";
    private static final String LAYOUT_PROPERTY_PADDING_TOP = "padding-top";
    private static final String LAYOUT_PROPERTY_WIDTH = "width";
    private static final String PERSPECTIVE = "perspective";
    private static final String TRANSFORM_ORIGIN = "transformOrigin";
    private static final LayoutUpdater sLayoutUpdater = new LayoutUpdater();
    private static final Map<String, INativeViewUpdater> sTransformPropertyUpdaterMap = new HashMap();
    private static final Handler sUIHandler = new Handler(Looper.getMainLooper());

    static {
        sTransformPropertyUpdaterMap.put("opacity", new OpacityUpdater());
        sTransformPropertyUpdaterMap.put("transform.translate", new TranslateUpdater());
        sTransformPropertyUpdaterMap.put("transform.translateX", new TranslateXUpdater());
        sTransformPropertyUpdaterMap.put("transform.translateY", new TranslateYUpdater());
        sTransformPropertyUpdaterMap.put("transform.scale", new ScaleUpdater());
        sTransformPropertyUpdaterMap.put("transform.scaleX", new ScaleXUpdater());
        sTransformPropertyUpdaterMap.put("transform.scaleY", new ScaleYUpdater());
        sTransformPropertyUpdaterMap.put("transform.rotate", new RotateUpdater());
        sTransformPropertyUpdaterMap.put("transform.rotateZ", new RotateUpdater());
        sTransformPropertyUpdaterMap.put("transform.rotateX", new RotateXUpdater());
        sTransformPropertyUpdaterMap.put("transform.rotateY", new RotateYUpdater());
        sTransformPropertyUpdaterMap.put("background-color", new BackgroundUpdater());
        sTransformPropertyUpdaterMap.put("color", new ColorUpdater());
        sTransformPropertyUpdaterMap.put("scroll.contentOffsetX", new ContentOffsetXUpdater());
        sTransformPropertyUpdaterMap.put("scroll.contentOffsetY", new ContentOffsetYUpdater());
    }

    @NonNull
    public static INativeViewUpdater findUpdater(@NonNull String str) {
        INativeViewUpdater iNativeViewUpdater = sTransformPropertyUpdaterMap.get(str);
        if (iNativeViewUpdater != null) {
            return iNativeViewUpdater;
        }
        if (LAYOUT_PROPERTIES.contains(str)) {
            sLayoutUpdater.setPropertyName(str);
            return sLayoutUpdater;
        }
        LogProxy.e("unknown property [" + str + Operators.ARRAY_END_STR);
        return EMPTY_INVOKER;
    }

    private static final class NOpUpdater implements INativeViewUpdater {
        public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
        }

        private NOpUpdater() {
        }
    }

    /* access modifiers changed from: private */
    public static void runOnUIThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            sUIHandler.post(new WeakRunnable(runnable));
        }
    }

    public static void clearCallbacks() {
        sUIHandler.removeCallbacksAndMessages((Object) null);
    }

    private static final class OpacityUpdater implements INativeViewUpdater {
        private OpacityUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Double) {
                final float doubleValue = (float) ((Double) obj).doubleValue();
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view.setAlpha(doubleValue);
                    }
                });
            }
        }
    }

    private static final class TranslateUpdater implements INativeViewUpdater {
        private TranslateUpdater() {
        }

        public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof ArrayList) {
                ArrayList arrayList = (ArrayList) obj;
                if (arrayList.size() >= 2 && (arrayList.get(0) instanceof Double) && (arrayList.get(1) instanceof Double)) {
                    final double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                    final double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                    final View view2 = view;
                    final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                    NativeViewUpdateService.runOnUIThread(new Runnable() {
                        public void run() {
                            view2.setTranslationX((float) NativeViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                            view2.setTranslationY((float) NativeViewUpdateService.getRealSize(doubleValue2, iDeviceResolutionTranslator2));
                        }
                    });
                }
            }
        }
    }

    private static final class TranslateXUpdater implements INativeViewUpdater {
        private TranslateXUpdater() {
        }

        public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view2.setTranslationX((float) NativeViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    private static final class TranslateYUpdater implements INativeViewUpdater {
        private TranslateYUpdater() {
        }

        public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view2.setTranslationY((float) NativeViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    private static final class ScaleUpdater implements INativeViewUpdater {
        private ScaleUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull final Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull final Map<String, Object> map) {
            NativeViewUpdateService.runOnUIThread(new Runnable() {
                public void run() {
                    Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(Utils.getStringValue(map, "transformOrigin"), view);
                    if (parseTransformOrigin != null) {
                        view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                        view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                    }
                    if (obj instanceof Double) {
                        float doubleValue = (float) ((Double) obj).doubleValue();
                        view.setScaleX(doubleValue);
                        view.setScaleY(doubleValue);
                    } else if (obj instanceof ArrayList) {
                        ArrayList arrayList = (ArrayList) obj;
                        if (arrayList.size() >= 2 && (arrayList.get(0) instanceof Double) && (arrayList.get(1) instanceof Double)) {
                            double doubleValue2 = ((Double) arrayList.get(0)).doubleValue();
                            double doubleValue3 = ((Double) arrayList.get(1)).doubleValue();
                            view.setScaleX((float) doubleValue2);
                            view.setScaleY((float) doubleValue3);
                        }
                    }
                }
            });
        }
    }

    private static final class ScaleXUpdater implements INativeViewUpdater {
        private ScaleXUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull final Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull final Map<String, Object> map) {
            if (obj instanceof Double) {
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(Utils.getStringValue(map, "transformOrigin"), view);
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setScaleX((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class ScaleYUpdater implements INativeViewUpdater {
        private ScaleYUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull final Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull final Map<String, Object> map) {
            if (obj instanceof Double) {
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(Utils.getStringValue(map, "transformOrigin"), view);
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setScaleY((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class RotateUpdater implements INativeViewUpdater {
        private RotateUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull final Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull final Map<String, Object> map) {
            if (obj instanceof Double) {
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), NativeViewUpdateService.getIntValue(map, "perspective"));
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(Utils.getStringValue(map, "transformOrigin"), view);
                        if (normalizedPerspectiveValue != 0) {
                            view.setCameraDistance((float) normalizedPerspectiveValue);
                        }
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setRotation((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class RotateXUpdater implements INativeViewUpdater {
        private RotateXUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull final Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull final Map<String, Object> map) {
            if (obj instanceof Double) {
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), NativeViewUpdateService.getIntValue(map, "perspective"));
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(Utils.getStringValue(map, "transformOrigin"), view);
                        if (normalizedPerspectiveValue != 0) {
                            view.setCameraDistance((float) normalizedPerspectiveValue);
                        }
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setRotationX((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class RotateYUpdater implements INativeViewUpdater {
        private RotateYUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull final Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull final Map<String, Object> map) {
            if (obj instanceof Double) {
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), NativeViewUpdateService.getIntValue(map, "perspective"));
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(Utils.getStringValue(map, "transformOrigin"), view);
                        if (normalizedPerspectiveValue != 0) {
                            view.setCameraDistance((float) normalizedPerspectiveValue);
                        }
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setRotationY((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class BackgroundUpdater implements INativeViewUpdater {
        private BackgroundUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Integer) {
                final int intValue = ((Integer) obj).intValue();
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view.getBackground();
                        if (background == null) {
                            view.setBackgroundColor(intValue);
                        } else if (background instanceof ColorDrawable) {
                            ((ColorDrawable) background).setColor(intValue);
                        }
                    }
                });
            }
        }
    }

    private static final class ColorUpdater implements INativeViewUpdater {
        private ColorUpdater() {
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Integer) {
                final int intValue = ((Integer) obj).intValue();
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        if (view instanceof TextView) {
                            ((TextView) view).setTextColor(intValue);
                        }
                    }
                });
            }
        }
    }

    private static final class ContentOffsetXUpdater implements INativeViewUpdater {
        private ContentOffsetXUpdater() {
        }

        public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view2.setScrollX((int) NativeViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    private static final class ContentOffsetYUpdater implements INativeViewUpdater {
        private ContentOffsetYUpdater() {
        }

        public void update(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                NativeViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view2.setScrollY((int) NativeViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public static double getRealSize(double d, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        return iDeviceResolutionTranslator.webToNative(d, new Object[0]);
    }

    /* access modifiers changed from: private */
    public static int getIntValue(Map<String, Object> map, String str) {
        Object obj;
        if (map == null || TextUtils.isEmpty(str) || (obj = map.get(str)) == null) {
            return 0;
        }
        try {
            if (obj instanceof String) {
                return Integer.parseInt((String) obj);
            }
            if (obj instanceof Integer) {
                return ((Integer) obj).intValue();
            }
            return 0;
        } catch (Throwable unused) {
            return 0;
        }
    }

    private static final class LayoutUpdater implements INativeViewUpdater {
        private String propertyName;

        private LayoutUpdater() {
        }

        /* access modifiers changed from: package-private */
        public void setPropertyName(String str) {
            this.propertyName = str;
        }

        public void update(@NonNull final View view, @NonNull String str, @NonNull Object obj, @NonNull PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map) {
            if ((obj instanceof Double) && !TextUtils.isEmpty(this.propertyName)) {
                final int access$1700 = (int) NativeViewUpdateService.getRealSize(((Double) obj).doubleValue(), iDeviceResolutionTranslator);
                String str2 = this.propertyName;
                char c = 65535;
                switch (str2.hashCode()) {
                    case -1502084711:
                        if (str2.equals("padding-top")) {
                            c = 8;
                            break;
                        }
                        break;
                    case -1221029593:
                        if (str2.equals("height")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -887955139:
                        if (str2.equals("margin-right")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -396426912:
                        if (str2.equals("padding-right")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 113126854:
                        if (str2.equals("width")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 143541095:
                        if (str2.equals("padding-bottom")) {
                            c = 9;
                            break;
                        }
                        break;
                    case 679766083:
                        if (str2.equals("padding-left")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 941004998:
                        if (str2.equals("margin-left")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1970025654:
                        if (str2.equals("margin-top")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 2086035242:
                        if (str2.equals("margin-bottom")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.width = access$1700;
                                view.setLayoutParams(layoutParams);
                            }
                        });
                        break;
                    case 1:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                layoutParams.height = access$1700;
                                view.setLayoutParams(layoutParams);
                            }
                        });
                        break;
                    case 2:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                                    ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = access$1700;
                                    view.setLayoutParams(layoutParams);
                                    return;
                                }
                                LogProxy.e("set margin left failed");
                            }
                        });
                        break;
                    case 3:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                                    ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = access$1700;
                                    view.setLayoutParams(layoutParams);
                                    return;
                                }
                                LogProxy.e("set margin right failed");
                            }
                        });
                        break;
                    case 4:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                                    ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = access$1700;
                                    view.setLayoutParams(layoutParams);
                                    return;
                                }
                                LogProxy.e("set margin top failed");
                            }
                        });
                        break;
                    case 5:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = access$1700;
                                    view.setLayoutParams(layoutParams);
                                    return;
                                }
                                LogProxy.e("set margin bottom failed");
                            }
                        });
                        break;
                    case 6:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                view.setPadding(access$1700, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                            }
                        });
                        break;
                    case 7:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), access$1700, view.getPaddingBottom());
                            }
                        });
                        break;
                    case 8:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                view.setPadding(view.getPaddingLeft(), access$1700, view.getPaddingRight(), view.getPaddingBottom());
                            }
                        });
                        break;
                    case 9:
                        NativeViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), access$1700);
                            }
                        });
                        break;
                }
                this.propertyName = null;
            }
        }
    }
}
