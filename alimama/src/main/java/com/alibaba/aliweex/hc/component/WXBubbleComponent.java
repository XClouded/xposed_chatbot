package com.alibaba.aliweex.hc.component;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.bubble.BubbleContainer;
import com.alibaba.aliweex.bubble.BubbleEventCenter;
import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.taobao.uikit.feature.features.BounceScrollFeature;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Component(lazyload = false)
@WeexComponent(names = {"bubble"})
public class WXBubbleComponent extends WXVContainer<BubbleContainer> {
    private static final String TAG = "WXBubbleComponent";
    private BubbleContainer mBubbleContainer;

    public WXBubbleComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, str, z, basicComponentData);
    }

    public WXBubbleComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXBubbleComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXBubbleComponent(wXSDKInstance, wXVContainer, z, basicComponentData);
        }

        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXBubbleComponent(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    /* access modifiers changed from: protected */
    public BubbleContainer initComponentHostView(@NonNull Context context) {
        this.mBubbleContainer = new BubbleContainer(context);
        return this.mBubbleContainer;
    }

    @WXComponentProp(name = "positions")
    public void setPositions(float[][] fArr) {
        if (this.mBubbleContainer != null && fArr != null) {
            this.mBubbleContainer.setPositions(fArr);
        }
    }

    @WXComponentProp(name = "rows")
    public void setRows(int i) {
        if (this.mBubbleContainer != null && i > 0) {
            this.mBubbleContainer.setRows(i);
        }
    }

    @WXComponentProp(name = "total")
    public void setTotal(int i) {
        if (this.mBubbleContainer != null && i > 0) {
            this.mBubbleContainer.setTotal(i);
        }
    }

    public void addSubView(View view, int i) {
        if (view != null && getRealView() != null && getRealView() == this.mBubbleContainer) {
            if (i >= getRealView().getChildCount()) {
                i = -1;
            }
            this.mBubbleContainer.addView(view, i);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r14, java.lang.Object r15) {
        /*
            r13 = this;
            int r0 = r14.hashCode()
            r1 = 2
            r2 = 0
            r3 = 1
            switch(r0) {
                case 3506649: goto L_0x0033;
                case 104581405: goto L_0x0029;
                case 110549828: goto L_0x001f;
                case 1707117674: goto L_0x0015;
                case 2030987079: goto L_0x000b;
                default: goto L_0x000a;
            }
        L_0x000a:
            goto L_0x003d
        L_0x000b:
            java.lang.String r0 = "animationMode"
            boolean r0 = r14.equals(r0)
            if (r0 == 0) goto L_0x003d
            r0 = 4
            goto L_0x003e
        L_0x0015:
            java.lang.String r0 = "positions"
            boolean r0 = r14.equals(r0)
            if (r0 == 0) goto L_0x003d
            r0 = 0
            goto L_0x003e
        L_0x001f:
            java.lang.String r0 = "total"
            boolean r0 = r14.equals(r0)
            if (r0 == 0) goto L_0x003d
            r0 = 3
            goto L_0x003e
        L_0x0029:
            java.lang.String r0 = "nails"
            boolean r0 = r14.equals(r0)
            if (r0 == 0) goto L_0x003d
            r0 = 2
            goto L_0x003e
        L_0x0033:
            java.lang.String r0 = "rows"
            boolean r0 = r14.equals(r0)
            if (r0 == 0) goto L_0x003d
            r0 = 1
            goto L_0x003e
        L_0x003d:
            r0 = -1
        L_0x003e:
            r4 = 750(0x2ee, float:1.051E-42)
            r5 = 0
            switch(r0) {
                case 0: goto L_0x00e4;
                case 1: goto L_0x00d6;
                case 2: goto L_0x0072;
                case 3: goto L_0x0064;
                case 4: goto L_0x0049;
                default: goto L_0x0044;
            }
        L_0x0044:
            boolean r14 = super.setProperty(r14, r15)
            return r14
        L_0x0049:
            java.lang.String r14 = com.taobao.weex.utils.WXUtils.getString(r15, r5)
            com.alibaba.aliweex.bubble.BubbleContainer r15 = r13.mBubbleContainer
            if (r15 == 0) goto L_0x0063
            java.lang.String r15 = "scroll"
            boolean r14 = r15.equals(r14)
            com.alibaba.aliweex.bubble.BubbleContainer r15 = r13.mBubbleContainer
            if (r14 == 0) goto L_0x005e
            com.alibaba.aliweex.bubble.BubbleContainer$BubbleMode r14 = com.alibaba.aliweex.bubble.BubbleContainer.BubbleMode.Scroll
            goto L_0x0060
        L_0x005e:
            com.alibaba.aliweex.bubble.BubbleContainer$BubbleMode r14 = com.alibaba.aliweex.bubble.BubbleContainer.BubbleMode.Swipe
        L_0x0060:
            r15.setBubbleMode(r14)
        L_0x0063:
            return r3
        L_0x0064:
            int r14 = com.taobao.weex.utils.WXUtils.getInt(r15)
            com.alibaba.aliweex.bubble.BubbleContainer r15 = r13.mBubbleContainer
            if (r15 == 0) goto L_0x0071
            com.alibaba.aliweex.bubble.BubbleContainer r15 = r13.mBubbleContainer
            r15.setTotal(r14)
        L_0x0071:
            return r3
        L_0x0072:
            java.lang.String r14 = com.taobao.weex.utils.WXUtils.getString(r15, r5)
            if (r14 == 0) goto L_0x00d5
            java.lang.Object r14 = com.alibaba.fastjson.JSONArray.parse(r14)     // Catch:{ Throwable -> 0x00d5 }
            com.alibaba.fastjson.JSONArray r14 = (com.alibaba.fastjson.JSONArray) r14     // Catch:{ Throwable -> 0x00d5 }
            int r15 = r14.size()     // Catch:{ Throwable -> 0x00d5 }
            if (r15 == r1) goto L_0x008c
            java.lang.String r14 = TAG     // Catch:{ Throwable -> 0x00d5 }
            java.lang.String r15 = "nail array length must be 2, 0 is head, 1 is tail. example:[ [[head_nail1],[head_nail2]], [[tail_nail1],[tail_nail2]] ]"
            android.util.Log.e(r14, r15)     // Catch:{ Throwable -> 0x00d5 }
            return r2
        L_0x008c:
            r0 = 0
        L_0x008d:
            if (r0 >= r15) goto L_0x00d5
            com.alibaba.fastjson.JSONArray r1 = r14.getJSONArray(r0)     // Catch:{ Throwable -> 0x00d5 }
            int r5 = r1.size()     // Catch:{ Throwable -> 0x00d5 }
            float[][] r6 = new float[r5][]     // Catch:{ Throwable -> 0x00d5 }
            r7 = 0
        L_0x009a:
            if (r7 >= r5) goto L_0x00c1
            com.alibaba.fastjson.JSONArray r8 = r1.getJSONArray(r7)     // Catch:{ Throwable -> 0x00d5 }
            int r9 = r8.size()     // Catch:{ Throwable -> 0x00d5 }
            float[] r10 = new float[r9]     // Catch:{ Throwable -> 0x00d5 }
            r6[r7] = r10     // Catch:{ Throwable -> 0x00d5 }
            r10 = 0
        L_0x00a9:
            if (r10 >= r9) goto L_0x00be
            r11 = r6[r7]     // Catch:{ Throwable -> 0x00d5 }
            java.lang.Float r12 = r8.getFloat(r10)     // Catch:{ Throwable -> 0x00d5 }
            float r12 = r12.floatValue()     // Catch:{ Throwable -> 0x00d5 }
            float r12 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r12, r4)     // Catch:{ Throwable -> 0x00d5 }
            r11[r10] = r12     // Catch:{ Throwable -> 0x00d5 }
            int r10 = r10 + 1
            goto L_0x00a9
        L_0x00be:
            int r7 = r7 + 1
            goto L_0x009a
        L_0x00c1:
            com.alibaba.aliweex.bubble.BubbleContainer r1 = r13.mBubbleContainer     // Catch:{ Throwable -> 0x00d5 }
            if (r1 == 0) goto L_0x00d2
            if (r0 != 0) goto L_0x00cd
            com.alibaba.aliweex.bubble.BubbleContainer r1 = r13.mBubbleContainer     // Catch:{ Throwable -> 0x00d5 }
            r1.setHeadNails(r6)     // Catch:{ Throwable -> 0x00d5 }
            goto L_0x00d2
        L_0x00cd:
            com.alibaba.aliweex.bubble.BubbleContainer r1 = r13.mBubbleContainer     // Catch:{ Throwable -> 0x00d5 }
            r1.setTailNails(r6)     // Catch:{ Throwable -> 0x00d5 }
        L_0x00d2:
            int r0 = r0 + 1
            goto L_0x008d
        L_0x00d5:
            return r3
        L_0x00d6:
            int r14 = com.taobao.weex.utils.WXUtils.getInt(r15)
            com.alibaba.aliweex.bubble.BubbleContainer r15 = r13.mBubbleContainer
            if (r15 == 0) goto L_0x00e3
            com.alibaba.aliweex.bubble.BubbleContainer r15 = r13.mBubbleContainer
            r15.setRows(r14)
        L_0x00e3:
            return r3
        L_0x00e4:
            java.lang.String r14 = com.taobao.weex.utils.WXUtils.getString(r15, r5)
            if (r14 == 0) goto L_0x0127
            java.lang.Object r14 = com.alibaba.fastjson.JSONArray.parse(r14)     // Catch:{ Throwable -> 0x0127 }
            com.alibaba.fastjson.JSONArray r14 = (com.alibaba.fastjson.JSONArray) r14     // Catch:{ Throwable -> 0x0127 }
            int r15 = r14.size()     // Catch:{ Throwable -> 0x0127 }
            float[][] r0 = new float[r15][]     // Catch:{ Throwable -> 0x0127 }
            r1 = 0
        L_0x00f7:
            if (r1 >= r15) goto L_0x011e
            com.alibaba.fastjson.JSONArray r5 = r14.getJSONArray(r1)     // Catch:{ Throwable -> 0x0127 }
            int r6 = r5.size()     // Catch:{ Throwable -> 0x0127 }
            float[] r7 = new float[r6]     // Catch:{ Throwable -> 0x0127 }
            r0[r1] = r7     // Catch:{ Throwable -> 0x0127 }
            r7 = 0
        L_0x0106:
            if (r7 >= r6) goto L_0x011b
            r8 = r0[r1]     // Catch:{ Throwable -> 0x0127 }
            java.lang.Float r9 = r5.getFloat(r7)     // Catch:{ Throwable -> 0x0127 }
            float r9 = r9.floatValue()     // Catch:{ Throwable -> 0x0127 }
            float r9 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r9, r4)     // Catch:{ Throwable -> 0x0127 }
            r8[r7] = r9     // Catch:{ Throwable -> 0x0127 }
            int r7 = r7 + 1
            goto L_0x0106
        L_0x011b:
            int r1 = r1 + 1
            goto L_0x00f7
        L_0x011e:
            com.alibaba.aliweex.bubble.BubbleContainer r14 = r13.mBubbleContainer     // Catch:{ Throwable -> 0x0127 }
            if (r14 == 0) goto L_0x0127
            com.alibaba.aliweex.bubble.BubbleContainer r14 = r13.mBubbleContainer     // Catch:{ Throwable -> 0x0127 }
            r14.setPositions(r0)     // Catch:{ Throwable -> 0x0127 }
        L_0x0127:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.component.WXBubbleComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @JSMethod
    public void registerCallback(final JSCallback jSCallback, final JSCallback jSCallback2, final JSCallback jSCallback3) {
        if (this.mBubbleContainer != null) {
            this.mBubbleContainer.addAnimationCallback(new BubbleContainer.IAnimationListener() {
                public void onAnimationStart(BubbleEventCenter.AnimationType animationType) {
                    HashMap hashMap = new HashMap();
                    if (animationType == BubbleEventCenter.AnimationType.MoveLeft) {
                        hashMap.put("direction", "left");
                        hashMap.put("type", "swipe");
                    } else if (animationType == BubbleEventCenter.AnimationType.MoveRight) {
                        hashMap.put("direction", "right");
                        hashMap.put("type", "swipe");
                    } else if (animationType == BubbleEventCenter.AnimationType.EdgeBounceLeft) {
                        hashMap.put("direction", "left");
                        hashMap.put("type", BounceScrollFeature.BOUNCE_TAG);
                    } else if (animationType == BubbleEventCenter.AnimationType.EdgeBounceRight) {
                        hashMap.put("direction", "right");
                        hashMap.put("type", BounceScrollFeature.BOUNCE_TAG);
                    }
                    jSCallback.invokeAndKeepAlive(hashMap);
                }

                public void onAnimationEnd(BubbleEventCenter.AnimationType animationType) {
                    HashMap hashMap = new HashMap();
                    if (animationType == BubbleEventCenter.AnimationType.MoveLeft) {
                        hashMap.put("direction", "left");
                        hashMap.put("type", "swipe");
                    } else if (animationType == BubbleEventCenter.AnimationType.MoveRight) {
                        hashMap.put("direction", "right");
                        hashMap.put("type", "swipe");
                    } else if (animationType == BubbleEventCenter.AnimationType.EdgeBounceLeft) {
                        hashMap.put("direction", "left");
                        hashMap.put("type", BounceScrollFeature.BOUNCE_TAG);
                    } else if (animationType == BubbleEventCenter.AnimationType.EdgeBounceRight) {
                        hashMap.put("direction", "right");
                        hashMap.put("type", BounceScrollFeature.BOUNCE_TAG);
                    } else if (animationType == BubbleEventCenter.AnimationType.ScrollRight) {
                        hashMap.put("direction", "left");
                        hashMap.put("type", "scroll");
                    } else if (animationType == BubbleEventCenter.AnimationType.ScrollLeft) {
                        hashMap.put("direction", "left");
                        hashMap.put("type", "scroll");
                    }
                    jSCallback2.invokeAndKeepAlive(hashMap);
                }
            });
            this.mBubbleContainer.addBubbleClickCallback(new BubbleContainer.IBubbleClickListener() {
                public void onClick(int i) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("bubbleId", Integer.valueOf(i));
                    jSCallback3.invokeAndKeepAlive(hashMap);
                }
            });
        }
    }

    @JSMethod(uiThread = true)
    public void scroll(boolean z) {
        if (this.mBubbleContainer != null) {
            this.mBubbleContainer.swipeByFronted(z ? 256 : 512);
        }
    }

    @JSMethod
    public void replaceBubble(int i, int i2) {
        if (this.mBubbleContainer != null) {
            this.mBubbleContainer.replaceBubble(i, i2);
        }
    }

    @JSMethod
    public void resetBubbles() {
        if (this.mBubbleContainer != null) {
            this.mBubbleContainer.resetBubbles();
        }
    }

    @JSMethod
    public void inViewBubbleList(JSCallback jSCallback) {
        if (jSCallback != null && this.mBubbleContainer != null) {
            jSCallback.invoke(this.mBubbleContainer.inViewBubbleList());
        }
    }

    @JSMethod
    public void outViewBubbleList(JSCallback jSCallback) {
        if (jSCallback != null && this.mBubbleContainer != null) {
            jSCallback.invoke(this.mBubbleContainer.outViewBubbleList());
        }
    }
}
