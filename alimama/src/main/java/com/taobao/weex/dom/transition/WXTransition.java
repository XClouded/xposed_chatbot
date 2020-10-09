package com.taobao.weex.dom.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.collection.ArrayMap;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.animation.BackgroundColorProperty;
import com.taobao.weex.ui.animation.TransformParser;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.SingleFunctionParser;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class WXTransition {
    private static final Set<String> LAYOUT_PROPERTIES = new HashSet();
    public static final Pattern PROPERTY_SPLIT_PATTERN = Pattern.compile("\\||,");
    private static final Set<String> TRANSFORM_PROPERTIES = new HashSet();
    public static final String TRANSITION_DELAY = "transitionDelay";
    public static final String TRANSITION_DURATION = "transitionDuration";
    public static final String TRANSITION_PROPERTY = "transitionProperty";
    public static final String TRANSITION_TIMING_FUNCTION = "transitionTimingFunction";
    /* access modifiers changed from: private */
    public Runnable animationRunnable;
    private long delay;
    /* access modifiers changed from: private */
    public long duration;
    private Handler handler = new Handler();
    private Interpolator interpolator;
    private Map<String, Object> layoutPendingUpdates = new ArrayMap();
    private ValueAnimator layoutValueAnimator;
    /* access modifiers changed from: private */
    public volatile AtomicInteger lockToken = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public WXComponent mWXComponent;
    private List<String> properties = new ArrayList(4);
    private Map<String, Object> targetStyles = new ArrayMap();
    private Runnable transformAnimationRunnable;
    private ObjectAnimator transformAnimator;
    private Map<String, Object> transformPendingUpdates = new ArrayMap();
    /* access modifiers changed from: private */
    public Runnable transitionEndEvent;

    static {
        LAYOUT_PROPERTIES.add("width");
        LAYOUT_PROPERTIES.add("height");
        LAYOUT_PROPERTIES.add("marginTop");
        LAYOUT_PROPERTIES.add("marginBottom");
        LAYOUT_PROPERTIES.add("marginLeft");
        LAYOUT_PROPERTIES.add("marginRight");
        LAYOUT_PROPERTIES.add("left");
        LAYOUT_PROPERTIES.add("right");
        LAYOUT_PROPERTIES.add("top");
        LAYOUT_PROPERTIES.add("bottom");
        LAYOUT_PROPERTIES.add("paddingLeft");
        LAYOUT_PROPERTIES.add("paddingRight");
        LAYOUT_PROPERTIES.add("paddingTop");
        LAYOUT_PROPERTIES.add("paddingBottom");
        TRANSFORM_PROPERTIES.add("opacity");
        TRANSFORM_PROPERTIES.add("backgroundColor");
        TRANSFORM_PROPERTIES.add("transform");
    }

    public static WXTransition fromMap(Map<String, Object> map, WXComponent wXComponent) {
        String string;
        if (map.get(TRANSITION_PROPERTY) == null || (string = WXUtils.getString(map.get(TRANSITION_PROPERTY), (String) null)) == null) {
            return null;
        }
        WXTransition wXTransition = new WXTransition();
        updateTransitionProperties(wXTransition, string);
        if (wXTransition.properties.isEmpty()) {
            return null;
        }
        wXTransition.duration = parseTimeMillis(map, TRANSITION_DURATION, 0);
        wXTransition.delay = parseTimeMillis(map, TRANSITION_DELAY, 0);
        wXTransition.interpolator = createTimeInterpolator(WXUtils.getString(map.get(TRANSITION_TIMING_FUNCTION), (String) null));
        wXTransition.mWXComponent = wXComponent;
        return wXTransition;
    }

    public boolean hasTransitionProperty(Map<String, Object> map) {
        for (String containsKey : this.properties) {
            if (map.containsKey(containsKey)) {
                return true;
            }
        }
        return false;
    }

    public void updateTranstionParams(Map<String, Object> map) {
        if (map.containsKey(TRANSITION_DELAY)) {
            this.mWXComponent.getStyles().put(TRANSITION_DELAY, map.remove(TRANSITION_DELAY));
            this.delay = parseTimeMillis(this.mWXComponent.getStyles(), TRANSITION_DELAY, 0);
        }
        if (map.containsKey(TRANSITION_TIMING_FUNCTION) && map.get(TRANSITION_TIMING_FUNCTION) != null) {
            this.mWXComponent.getStyles().put(TRANSITION_TIMING_FUNCTION, map.remove(TRANSITION_TIMING_FUNCTION));
            this.interpolator = createTimeInterpolator(this.mWXComponent.getStyles().get(TRANSITION_TIMING_FUNCTION).toString());
        }
        if (map.containsKey(TRANSITION_DURATION)) {
            this.mWXComponent.getStyles().put(TRANSITION_DURATION, map.remove(TRANSITION_DURATION));
            this.duration = parseTimeMillis(this.mWXComponent.getStyles(), TRANSITION_DURATION, 0);
        }
        if (map.containsKey(TRANSITION_PROPERTY)) {
            this.mWXComponent.getStyles().put(TRANSITION_PROPERTY, map.remove(TRANSITION_PROPERTY));
            updateTransitionProperties(this, WXUtils.getString(this.mWXComponent.getStyles().get(TRANSITION_PROPERTY), (String) null));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0088, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startTransition(java.util.Map<java.lang.String, java.lang.Object> r8) {
        /*
            r7 = this;
            java.util.concurrent.atomic.AtomicInteger r0 = r7.lockToken
            monitor-enter(r0)
            android.view.View r1 = r7.getTargetView()     // Catch:{ all -> 0x0089 }
            if (r1 != 0) goto L_0x000b
            monitor-exit(r0)     // Catch:{ all -> 0x0089 }
            return
        L_0x000b:
            java.util.concurrent.atomic.AtomicInteger r1 = r7.lockToken     // Catch:{ all -> 0x0089 }
            int r1 = r1.incrementAndGet()     // Catch:{ all -> 0x0089 }
            java.util.List<java.lang.String> r2 = r7.properties     // Catch:{ all -> 0x0089 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0089 }
        L_0x0017:
            boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0089 }
            if (r3 == 0) goto L_0x0049
            java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x0089 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0089 }
            boolean r4 = r8.containsKey(r3)     // Catch:{ all -> 0x0089 }
            if (r4 == 0) goto L_0x0017
            java.lang.Object r4 = r8.remove(r3)     // Catch:{ all -> 0x0089 }
            java.util.Set<java.lang.String> r5 = LAYOUT_PROPERTIES     // Catch:{ all -> 0x0089 }
            boolean r5 = r5.contains(r3)     // Catch:{ all -> 0x0089 }
            if (r5 == 0) goto L_0x003b
            java.util.Map<java.lang.String, java.lang.Object> r5 = r7.layoutPendingUpdates     // Catch:{ all -> 0x0089 }
            r5.put(r3, r4)     // Catch:{ all -> 0x0089 }
            goto L_0x0017
        L_0x003b:
            java.util.Set<java.lang.String> r5 = TRANSFORM_PROPERTIES     // Catch:{ all -> 0x0089 }
            boolean r5 = r5.contains(r3)     // Catch:{ all -> 0x0089 }
            if (r5 == 0) goto L_0x0017
            java.util.Map<java.lang.String, java.lang.Object> r5 = r7.transformPendingUpdates     // Catch:{ all -> 0x0089 }
            r5.put(r3, r4)     // Catch:{ all -> 0x0089 }
            goto L_0x0017
        L_0x0049:
            com.taobao.weex.ui.component.WXComponent r8 = r7.mWXComponent     // Catch:{ all -> 0x0089 }
            com.taobao.weex.dom.WXAttr r8 = r8.getAttrs()     // Catch:{ all -> 0x0089 }
            java.lang.String r2 = "actionDelay"
            java.lang.Object r8 = r8.get(r2)     // Catch:{ all -> 0x0089 }
            r2 = 16
            int r8 = com.taobao.weex.utils.WXUtils.getNumberInt(r8, r2)     // Catch:{ all -> 0x0089 }
            long r2 = (long) r8     // Catch:{ all -> 0x0089 }
            long r4 = r7.duration     // Catch:{ all -> 0x0089 }
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0065
            long r2 = r7.duration     // Catch:{ all -> 0x0089 }
            int r8 = (int) r2     // Catch:{ all -> 0x0089 }
        L_0x0065:
            java.lang.Runnable r2 = r7.animationRunnable     // Catch:{ all -> 0x0089 }
            if (r2 == 0) goto L_0x0070
            android.os.Handler r2 = r7.handler     // Catch:{ all -> 0x0089 }
            java.lang.Runnable r3 = r7.animationRunnable     // Catch:{ all -> 0x0089 }
            r2.removeCallbacks(r3)     // Catch:{ all -> 0x0089 }
        L_0x0070:
            com.taobao.weex.dom.transition.WXTransition$1 r2 = new com.taobao.weex.dom.transition.WXTransition$1     // Catch:{ all -> 0x0089 }
            r2.<init>(r1)     // Catch:{ all -> 0x0089 }
            r7.animationRunnable = r2     // Catch:{ all -> 0x0089 }
            if (r8 <= 0) goto L_0x0082
            android.os.Handler r1 = r7.handler     // Catch:{ all -> 0x0089 }
            java.lang.Runnable r2 = r7.animationRunnable     // Catch:{ all -> 0x0089 }
            long r3 = (long) r8     // Catch:{ all -> 0x0089 }
            r1.postDelayed(r2, r3)     // Catch:{ all -> 0x0089 }
            goto L_0x0087
        L_0x0082:
            java.lang.Runnable r8 = r7.animationRunnable     // Catch:{ all -> 0x0089 }
            r8.run()     // Catch:{ all -> 0x0089 }
        L_0x0087:
            monitor-exit(r0)     // Catch:{ all -> 0x0089 }
            return
        L_0x0089:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0089 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.transition.WXTransition.startTransition(java.util.Map):void");
    }

    /* access modifiers changed from: private */
    public void doTransitionAnimation(final int i) {
        View targetView = getTargetView();
        if (targetView != null) {
            if (this.targetStyles.size() > 0) {
                for (String next : this.properties) {
                    if ((LAYOUT_PROPERTIES.contains(next) || TRANSFORM_PROPERTIES.contains(next)) && !this.layoutPendingUpdates.containsKey(next) && !this.transformPendingUpdates.containsKey(next)) {
                        synchronized (this.targetStyles) {
                            if (this.targetStyles.containsKey(next)) {
                                this.mWXComponent.getStyles().put(next, this.targetStyles.remove(next));
                            }
                        }
                    }
                }
            }
            if (this.transitionEndEvent != null) {
                targetView.removeCallbacks(this.transitionEndEvent);
            }
            if (this.transitionEndEvent == null && ((float) this.duration) > Float.MIN_NORMAL) {
                this.transitionEndEvent = new Runnable() {
                    public void run() {
                        Runnable unused = WXTransition.this.transitionEndEvent = null;
                        if (((float) WXTransition.this.duration) >= Float.MIN_NORMAL && WXTransition.this.mWXComponent != null && WXTransition.this.mWXComponent.getEvents().contains(Constants.Event.ON_TRANSITION_END)) {
                            WXTransition.this.mWXComponent.fireEvent(Constants.Event.ON_TRANSITION_END);
                        }
                    }
                };
            }
            if (this.transformAnimationRunnable != null) {
                targetView.removeCallbacks(this.transformAnimationRunnable);
            }
            this.transformAnimationRunnable = new Runnable() {
                public void run() {
                    synchronized (WXTransition.this.lockToken) {
                        if (i == WXTransition.this.lockToken.get()) {
                            WXTransition.this.doPendingTransformAnimation(i);
                        }
                    }
                }
            };
            targetView.post(this.transformAnimationRunnable);
            doPendingLayoutAnimation();
        }
    }

    /* access modifiers changed from: private */
    public void doPendingTransformAnimation(int i) {
        View targetView;
        if (this.transformAnimator != null) {
            this.transformAnimator.cancel();
            this.transformAnimator = null;
        }
        if (this.transformPendingUpdates.size() != 0 && (targetView = getTargetView()) != null) {
            ArrayList arrayList = new ArrayList(8);
            String string = WXUtils.getString(this.transformPendingUpdates.remove("transform"), (String) null);
            if (!TextUtils.isEmpty(string)) {
                for (PropertyValuesHolder add : TransformParser.toHolders(TransformParser.parseTransForm(this.mWXComponent.getInstanceId(), string, (int) this.mWXComponent.getLayoutWidth(), (int) this.mWXComponent.getLayoutHeight(), this.mWXComponent.getViewPortWidth()))) {
                    arrayList.add(add);
                }
                synchronized (this.targetStyles) {
                    this.targetStyles.put("transform", string);
                }
            }
            for (String next : this.properties) {
                if (TRANSFORM_PROPERTIES.contains(next) && this.transformPendingUpdates.containsKey(next)) {
                    Object remove = this.transformPendingUpdates.remove(next);
                    synchronized (this.targetStyles) {
                        this.targetStyles.put(next, remove);
                    }
                    char c = 65535;
                    int hashCode = next.hashCode();
                    if (hashCode != -1267206133) {
                        if (hashCode == 1287124693 && next.equals("backgroundColor")) {
                            c = 1;
                        }
                    } else if (next.equals("opacity")) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                            arrayList.add(PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{targetView.getAlpha(), WXUtils.getFloat(remove, Float.valueOf(1.0f)).floatValue()}));
                            targetView.setLayerType(1, (Paint) null);
                            break;
                        case 1:
                            int color = WXResourceUtils.getColor(WXUtils.getString(this.mWXComponent.getStyles().getBackgroundColor(), (String) null), 0);
                            int color2 = WXResourceUtils.getColor(WXUtils.getString(remove, (String) null), 0);
                            if (WXViewUtils.getBorderDrawable(targetView) != null) {
                                color = WXViewUtils.getBorderDrawable(targetView).getColor();
                            } else if (targetView.getBackground() instanceof ColorDrawable) {
                                color = ((ColorDrawable) targetView.getBackground()).getColor();
                            }
                            arrayList.add(PropertyValuesHolder.ofObject(new BackgroundColorProperty(), new ArgbEvaluator(), new Integer[]{Integer.valueOf(color), Integer.valueOf(color2)}));
                            break;
                    }
                }
            }
            if (i == this.lockToken.get()) {
                this.transformPendingUpdates.clear();
            }
            this.transformAnimator = ObjectAnimator.ofPropertyValuesHolder(targetView, (PropertyValuesHolder[]) arrayList.toArray(new PropertyValuesHolder[arrayList.size()]));
            this.transformAnimator.setDuration(this.duration);
            if (this.delay > 0) {
                this.transformAnimator.setStartDelay(this.delay);
            }
            if (this.interpolator != null) {
                this.transformAnimator.setInterpolator(this.interpolator);
            }
            this.transformAnimator.addListener(new AnimatorListenerAdapter() {
                boolean hasCancel = false;

                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.hasCancel = true;
                }

                public void onAnimationEnd(Animator animator) {
                    if (!this.hasCancel) {
                        super.onAnimationEnd(animator);
                        WXTransition.this.onTransitionAnimationEnd();
                    }
                }
            });
            this.transformAnimator.start();
        }
    }

    public void doPendingLayoutAnimation() {
        if (this.layoutValueAnimator != null) {
            this.layoutValueAnimator.cancel();
            this.layoutValueAnimator = null;
        }
        if (this.layoutPendingUpdates.size() != 0) {
            PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[this.layoutPendingUpdates.size()];
            int i = 0;
            for (String next : this.properties) {
                if (LAYOUT_PROPERTIES.contains(next) && this.layoutPendingUpdates.containsKey(next)) {
                    Object remove = this.layoutPendingUpdates.remove(next);
                    synchronized (this.targetStyles) {
                        this.targetStyles.put(next, remove);
                    }
                    propertyValuesHolderArr[i] = createLayoutPropertyValueHolder(next, remove);
                    i++;
                }
            }
            this.layoutPendingUpdates.clear();
            doLayoutPropertyValuesHolderAnimation(propertyValuesHolderArr);
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.animation.PropertyValuesHolder createLayoutPropertyValueHolder(java.lang.String r8, java.lang.Object r9) {
        /*
            r7 = this;
            int r0 = r8.hashCode()
            r1 = 1
            r2 = 0
            r3 = 2
            switch(r0) {
                case -1501175880: goto L_0x0097;
                case -1383228885: goto L_0x008c;
                case -1221029593: goto L_0x0082;
                case -1044792121: goto L_0x0078;
                case -289173127: goto L_0x006e;
                case 115029: goto L_0x0063;
                case 3317767: goto L_0x0059;
                case 90130308: goto L_0x004e;
                case 108511772: goto L_0x0044;
                case 113126854: goto L_0x003a;
                case 202355100: goto L_0x002e;
                case 713848971: goto L_0x0022;
                case 975087886: goto L_0x0017;
                case 1970934485: goto L_0x000c;
                default: goto L_0x000a;
            }
        L_0x000a:
            goto L_0x00a2
        L_0x000c:
            java.lang.String r0 = "marginLeft"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 3
            goto L_0x00a3
        L_0x0017:
            java.lang.String r0 = "marginRight"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 4
            goto L_0x00a3
        L_0x0022:
            java.lang.String r0 = "paddingRight"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 13
            goto L_0x00a3
        L_0x002e:
            java.lang.String r0 = "paddingBottom"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 11
            goto L_0x00a3
        L_0x003a:
            java.lang.String r0 = "width"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 0
            goto L_0x00a3
        L_0x0044:
            java.lang.String r0 = "right"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 7
            goto L_0x00a3
        L_0x004e:
            java.lang.String r0 = "paddingTop"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 10
            goto L_0x00a3
        L_0x0059:
            java.lang.String r0 = "left"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 6
            goto L_0x00a3
        L_0x0063:
            java.lang.String r0 = "top"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 9
            goto L_0x00a3
        L_0x006e:
            java.lang.String r0 = "marginBottom"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 5
            goto L_0x00a3
        L_0x0078:
            java.lang.String r0 = "marginTop"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 2
            goto L_0x00a3
        L_0x0082:
            java.lang.String r0 = "height"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 1
            goto L_0x00a3
        L_0x008c:
            java.lang.String r0 = "bottom"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 8
            goto L_0x00a3
        L_0x0097:
            java.lang.String r0 = "paddingLeft"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x00a2
            r0 = 12
            goto L_0x00a3
        L_0x00a2:
            r0 = -1
        L_0x00a3:
            r4 = 0
            switch(r0) {
                case 0: goto L_0x02f2;
                case 1: goto L_0x02c9;
                case 2: goto L_0x029c;
                case 3: goto L_0x026e;
                case 4: goto L_0x0240;
                case 5: goto L_0x0212;
                case 6: goto L_0x01e6;
                case 7: goto L_0x01ba;
                case 8: goto L_0x018e;
                case 9: goto L_0x0162;
                case 10: goto L_0x0134;
                case 11: goto L_0x0106;
                case 12: goto L_0x00d8;
                case 13: goto L_0x00aa;
                default: goto L_0x00a7;
            }
        L_0x00a7:
            r9 = 0
            goto L_0x031a
        L_0x00aa:
            java.lang.String r0 = "paddingRight"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x00d8:
            java.lang.String r0 = "paddingLeft"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x0106:
            java.lang.String r0 = "paddingBottom"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x0134:
            java.lang.String r0 = "paddingTop"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x0162:
            java.lang.String r0 = "top"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r5 = r5.getLayoutPosition()
            float r5 = r5.getTop()
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x018e:
            java.lang.String r0 = "bottom"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r5 = r5.getLayoutPosition()
            float r5 = r5.getBottom()
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x01ba:
            java.lang.String r0 = "right"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r5 = r5.getLayoutPosition()
            float r5 = r5.getRight()
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x01e6:
            java.lang.String r0 = "left"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r5 = r5.getLayoutPosition()
            float r5 = r5.getLeft()
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x0212:
            java.lang.String r0 = "marginBottom"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x0240:
            java.lang.String r0 = "marginRight"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x026e:
            java.lang.String r0 = "marginLeft"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x029c:
            java.lang.String r0 = "marginTop"
            float[] r4 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r5 = r7.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
            float r5 = r5.get((com.taobao.weex.dom.CSSShorthand.EDGE) r6)
            r4[r2] = r5
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXUtils.getFloatByViewport(r9, r2)
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r4[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r4)
            goto L_0x031a
        L_0x02c9:
            java.lang.String r0 = "height"
            float[] r5 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r6 = r7.mWXComponent
            float r6 = r6.getLayoutHeight()
            r5[r2] = r6
            java.lang.Float r2 = java.lang.Float.valueOf(r4)
            java.lang.Float r9 = com.taobao.weex.utils.WXUtils.getFloat(r9, r2)
            float r9 = r9.floatValue()
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r5[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r5)
            goto L_0x031a
        L_0x02f2:
            java.lang.String r0 = "width"
            float[] r5 = new float[r3]
            com.taobao.weex.ui.component.WXComponent r6 = r7.mWXComponent
            float r6 = r6.getLayoutWidth()
            r5[r2] = r6
            java.lang.Float r2 = java.lang.Float.valueOf(r4)
            java.lang.Float r9 = com.taobao.weex.utils.WXUtils.getFloat(r9, r2)
            float r9 = r9.floatValue()
            com.taobao.weex.ui.component.WXComponent r2 = r7.mWXComponent
            int r2 = r2.getViewPortWidth()
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r2)
            r5[r1] = r9
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r0, r5)
        L_0x031a:
            if (r9 != 0) goto L_0x0325
            float[] r9 = new float[r3]
            r9 = {1065353216, 1065353216} // fill-array
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofFloat(r8, r9)
        L_0x0325:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.transition.WXTransition.createLayoutPropertyValueHolder(java.lang.String, java.lang.Object):android.animation.PropertyValuesHolder");
    }

    private void doLayoutPropertyValuesHolderAnimation(PropertyValuesHolder[] propertyValuesHolderArr) {
        this.layoutValueAnimator = ValueAnimator.ofPropertyValuesHolder(propertyValuesHolderArr);
        this.layoutValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                for (PropertyValuesHolder propertyName : valueAnimator.getValues()) {
                    String propertyName2 = propertyName.getPropertyName();
                    WXTransition.asynchronouslyUpdateLayout(WXTransition.this.mWXComponent, propertyName2, ((Float) valueAnimator.getAnimatedValue(propertyName2)).floatValue());
                }
            }
        });
        this.layoutValueAnimator.addListener(new AnimatorListenerAdapter() {
            boolean hasCancel = false;

            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.hasCancel = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.hasCancel) {
                    super.onAnimationEnd(animator);
                    WXTransition.this.onTransitionAnimationEnd();
                }
            }
        });
        if (this.interpolator != null) {
            this.layoutValueAnimator.setInterpolator(this.interpolator);
        }
        this.layoutValueAnimator.setStartDelay(this.delay);
        this.layoutValueAnimator.setDuration(this.duration);
        this.layoutValueAnimator.start();
    }

    public static void asynchronouslyUpdateLayout(WXComponent wXComponent, final String str, final float f) {
        if (wXComponent != null) {
            final String ref = wXComponent.getRef();
            final String instanceId = wXComponent.getInstanceId();
            if (!TextUtils.isEmpty(ref) && !TextUtils.isEmpty(instanceId)) {
                WXSDKManager.getInstance().getWXBridgeManager().post(new Runnable() {
                    /* JADX WARNING: Can't fix incorrect switch cases order */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r5 = this;
                            java.lang.String r0 = r4
                            int r1 = r0.hashCode()
                            switch(r1) {
                                case -1501175880: goto L_0x0096;
                                case -1383228885: goto L_0x008b;
                                case -1221029593: goto L_0x0081;
                                case -1044792121: goto L_0x0077;
                                case -289173127: goto L_0x006d;
                                case 115029: goto L_0x0062;
                                case 3317767: goto L_0x0058;
                                case 90130308: goto L_0x004d;
                                case 108511772: goto L_0x0043;
                                case 113126854: goto L_0x0039;
                                case 202355100: goto L_0x002d;
                                case 713848971: goto L_0x0021;
                                case 975087886: goto L_0x0016;
                                case 1970934485: goto L_0x000b;
                                default: goto L_0x0009;
                            }
                        L_0x0009:
                            goto L_0x00a1
                        L_0x000b:
                            java.lang.String r1 = "marginLeft"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 3
                            goto L_0x00a2
                        L_0x0016:
                            java.lang.String r1 = "marginRight"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 4
                            goto L_0x00a2
                        L_0x0021:
                            java.lang.String r1 = "paddingRight"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 13
                            goto L_0x00a2
                        L_0x002d:
                            java.lang.String r1 = "paddingBottom"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 11
                            goto L_0x00a2
                        L_0x0039:
                            java.lang.String r1 = "width"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 0
                            goto L_0x00a2
                        L_0x0043:
                            java.lang.String r1 = "right"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 7
                            goto L_0x00a2
                        L_0x004d:
                            java.lang.String r1 = "paddingTop"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 10
                            goto L_0x00a2
                        L_0x0058:
                            java.lang.String r1 = "left"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 6
                            goto L_0x00a2
                        L_0x0062:
                            java.lang.String r1 = "top"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 9
                            goto L_0x00a2
                        L_0x006d:
                            java.lang.String r1 = "marginBottom"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 5
                            goto L_0x00a2
                        L_0x0077:
                            java.lang.String r1 = "marginTop"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 2
                            goto L_0x00a2
                        L_0x0081:
                            java.lang.String r1 = "height"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 1
                            goto L_0x00a2
                        L_0x008b:
                            java.lang.String r1 = "bottom"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 8
                            goto L_0x00a2
                        L_0x0096:
                            java.lang.String r1 = "paddingLeft"
                            boolean r0 = r0.equals(r1)
                            if (r0 == 0) goto L_0x00a1
                            r0 = 12
                            goto L_0x00a2
                        L_0x00a1:
                            r0 = -1
                        L_0x00a2:
                            switch(r0) {
                                case 0: goto L_0x017b;
                                case 1: goto L_0x016d;
                                case 2: goto L_0x015d;
                                case 3: goto L_0x014d;
                                case 4: goto L_0x013d;
                                case 5: goto L_0x012d;
                                case 6: goto L_0x011d;
                                case 7: goto L_0x010d;
                                case 8: goto L_0x00fc;
                                case 9: goto L_0x00eb;
                                case 10: goto L_0x00da;
                                case 11: goto L_0x00c9;
                                case 12: goto L_0x00b8;
                                case 13: goto L_0x00a7;
                                default: goto L_0x00a5;
                            }
                        L_0x00a5:
                            goto L_0x0188
                        L_0x00a7:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
                            float r4 = r5
                            r0.setPadding(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x00b8:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
                            float r4 = r5
                            r0.setPadding(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x00c9:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
                            float r4 = r5
                            r0.setPadding(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x00da:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
                            float r4 = r5
                            r0.setPadding(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x00eb:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
                            float r4 = r5
                            r0.setPosition(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x00fc:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
                            float r4 = r5
                            r0.setPosition(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x010d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
                            float r4 = r5
                            r0.setPosition(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x011d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
                            float r4 = r5
                            r0.setPosition(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x012d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
                            float r4 = r5
                            r0.setMargin(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x013d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
                            float r4 = r5
                            r0.setMargin(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x014d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
                            float r4 = r5
                            r0.setMargin(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x015d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            com.taobao.weex.dom.CSSShorthand$EDGE r3 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
                            float r4 = r5
                            r0.setMargin(r1, r2, r3, r4)
                            goto L_0x0188
                        L_0x016d:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            float r3 = r5
                            r0.setStyleHeight(r1, r2, r3)
                            goto L_0x0188
                        L_0x017b:
                            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()
                            java.lang.String r1 = r3
                            java.lang.String r2 = r0
                            float r3 = r5
                            r0.setStyleWidth(r1, r2, r3)
                        L_0x0188:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.transition.WXTransition.AnonymousClass7.run():void");
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void onTransitionAnimationEnd() {
        if (this.duration > 0 && this.transitionEndEvent != null) {
            View targetView = getTargetView();
            if (!(targetView == null || this.transitionEndEvent == null)) {
                targetView.post(this.transitionEndEvent);
            }
            this.transitionEndEvent = null;
        }
        synchronized (this.targetStyles) {
            if (this.targetStyles.size() > 0) {
                for (String next : this.properties) {
                    if (this.targetStyles.containsKey(next)) {
                        this.mWXComponent.getStyles().put(next, this.targetStyles.remove(next));
                    }
                }
                this.targetStyles.clear();
            }
        }
    }

    private View getTargetView() {
        if (this.mWXComponent != null) {
            return this.mWXComponent.getHostView();
        }
        return null;
    }

    private static long parseTimeMillis(Map<String, Object> map, String str, long j) {
        String string = WXUtils.getString(map.get(str), (String) null);
        if (string != null) {
            string = string.replaceAll("ms", "");
        }
        if (string != null) {
            if (WXEnvironment.isApkDebugable() && string.contains("px")) {
                WXLogUtils.w("Transition Duration Unit Only Support ms, " + string + " is not ms Unit");
            }
            string = string.replaceAll("px", "");
        }
        if (TextUtils.isEmpty(string)) {
            return j;
        }
        try {
            return (long) Float.parseFloat(string);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    private static Interpolator createTimeInterpolator(String str) {
        if (!TextUtils.isEmpty(str)) {
            char c = 65535;
            switch (str.hashCode()) {
                case -1965120668:
                    if (str.equals("ease-in")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1102672091:
                    if (str.equals("linear")) {
                        c = 4;
                        break;
                    }
                    break;
                case -789192465:
                    if (str.equals("ease-out")) {
                        c = 1;
                        break;
                    }
                    break;
                case -361990811:
                    if (str.equals("ease-in-out")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3105774:
                    if (str.equals(Constants.TimeFunction.EASE)) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return PathInterpolatorCompat.create(0.42f, 0.0f, 1.0f, 1.0f);
                case 1:
                    return PathInterpolatorCompat.create(0.0f, 0.0f, 0.58f, 1.0f);
                case 2:
                    return PathInterpolatorCompat.create(0.42f, 0.0f, 0.58f, 1.0f);
                case 3:
                    return PathInterpolatorCompat.create(0.25f, 0.1f, 0.25f, 1.0f);
                case 4:
                    return PathInterpolatorCompat.create(0.0f, 0.0f, 1.0f, 1.0f);
                default:
                    try {
                        List parse = new SingleFunctionParser(str, new SingleFunctionParser.FlatMapper<Float>() {
                            public Float map(String str) {
                                return Float.valueOf(Float.parseFloat(str));
                            }
                        }).parse("cubic-bezier");
                        if (parse != null && parse.size() == 4) {
                            return PathInterpolatorCompat.create(((Float) parse.get(0)).floatValue(), ((Float) parse.get(1)).floatValue(), ((Float) parse.get(2)).floatValue(), ((Float) parse.get(3)).floatValue());
                        }
                    } catch (RuntimeException e) {
                        if (WXEnvironment.isApkDebugable()) {
                            WXLogUtils.e("WXTransition", (Throwable) e);
                            break;
                        }
                    }
                    break;
            }
        }
        return PathInterpolatorCompat.create(0.25f, 0.1f, 0.25f, 1.0f);
    }

    private static void updateTransitionProperties(WXTransition wXTransition, String str) {
        if (str != null) {
            wXTransition.properties.clear();
            for (String trim : PROPERTY_SPLIT_PATTERN.split(str)) {
                String trim2 = trim.trim();
                if (!TextUtils.isEmpty(trim2)) {
                    if (LAYOUT_PROPERTIES.contains(trim2) || TRANSFORM_PROPERTIES.contains(trim2)) {
                        wXTransition.properties.add(trim2);
                    } else if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.e("WXTransition Property Not Supported" + trim2 + " in " + str);
                    }
                }
            }
        }
    }

    public List<String> getProperties() {
        return this.properties;
    }
}
