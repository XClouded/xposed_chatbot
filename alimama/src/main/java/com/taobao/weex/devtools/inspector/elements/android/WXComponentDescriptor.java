package com.taobao.weex.devtools.inspector.elements.android;

import android.text.TextUtils;
import android.view.View;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.StringUtil;
import com.taobao.weex.devtools.inspector.elements.AbstractChainedDescriptor;
import com.taobao.weex.devtools.inspector.elements.AttributeAccumulator;
import com.taobao.weex.devtools.inspector.elements.StyleAccumulator;
import com.taobao.weex.devtools.inspector.elements.W3CStyleConstants;
import com.taobao.weex.ui.component.WXA;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.component.WXHeader;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXInput;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.WXSlider;
import com.taobao.weex.ui.component.WXSwitch;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.WXVideo;
import com.taobao.weex.ui.component.list.HorizontalListComponent;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.WXEditText;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class WXComponentDescriptor extends AbstractChainedDescriptor<WXComponent> implements HighlightableDescriptor {
    private static HashMap<Class, String> sClassName = new HashMap<>();

    /* access modifiers changed from: protected */
    public void onGetChildren(WXComponent wXComponent, Accumulator<Object> accumulator) {
    }

    static {
        sClassName.put(WXComponent.class, WXBridgeManager.COMPONENT);
        sClassName.put(WXText.class, "text");
        sClassName.put(WXVContainer.class, "container");
        sClassName.put(WXDiv.class, WXBasicComponentType.DIV);
        sClassName.put(WXEditText.class, WXBasicComponentType.TEXTAREA);
        sClassName.put(WXA.class, "a");
        sClassName.put(WXInput.class, "input");
        sClassName.put(WXLoading.class, "loading");
        sClassName.put(WXScroller.class, WXBasicComponentType.SCROLLER);
        sClassName.put(WXSwitch.class, "switch");
        sClassName.put(WXSlider.class, WXBasicComponentType.SLIDER);
        sClassName.put(WXVideo.class, "video");
        sClassName.put(WXImage.class, "image");
        sClassName.put(WXHeader.class, "header");
        sClassName.put(WXEmbed.class, WXBasicComponentType.EMBED);
        sClassName.put(WXListComponent.class, WXBasicComponentType.LIST);
        sClassName.put(HorizontalListComponent.class, WXBasicComponentType.HLIST);
        sClassName.put(WXCell.class, WXBasicComponentType.CELL);
    }

    /* access modifiers changed from: protected */
    public String onGetNodeName(WXComponent wXComponent) {
        Class<?> cls = wXComponent.getClass();
        String str = sClassName.get(cls);
        return TextUtils.isEmpty(str) ? StringUtil.removePrefix(cls.getSimpleName(), "WX") : str;
    }

    @Nullable
    public View getViewForHighlighting(Object obj) {
        return ((WXComponent) obj).getRealView();
    }

    public HashMap<String, String> getStyles(WXComponent wXComponent) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (Map.Entry next : wXComponent.getStyles().entrySet()) {
            if (next.getValue() != null) {
                hashMap.put(next.getKey(), next.getValue().toString());
            }
        }
        return hashMap;
    }

    public HashMap<String, String> getAttribute(WXComponent wXComponent) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (Map.Entry next : wXComponent.getAttrs().entrySet()) {
            if (next.getValue() != null) {
                hashMap.put(next.getKey(), next.getValue().toString());
            }
        }
        return hashMap;
    }

    private static boolean filter(String str) {
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        if (lowerCase.contains("padding") || lowerCase.contains("margin") || lowerCase.contains("width") || lowerCase.contains("height") || lowerCase.contains("left") || lowerCase.contains("right") || lowerCase.contains("top") || lowerCase.contains("bottom")) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onGetStyles(WXComponent wXComponent, StyleAccumulator styleAccumulator) {
        HashMap<String, String> styles = getStyles(wXComponent);
        if (styles != null && styles.size() > 0) {
            for (Map.Entry next : styles.entrySet()) {
                styleAccumulator.store(W3CStyleConstants.V_PREFIX + ((String) next.getKey()), (String) next.getValue(), false);
                if (filter((String) next.getKey())) {
                    styleAccumulator.store((String) next.getKey(), (String) next.getValue(), false);
                }
            }
        }
        View realView = wXComponent.getRealView();
        if (realView != null) {
            styleAccumulator.store("left", String.valueOf(realView.getLeft()), false);
            styleAccumulator.store("top", String.valueOf(realView.getTop()), false);
            styleAccumulator.store("right", String.valueOf(realView.getRight()), false);
            styleAccumulator.store("bottom", String.valueOf(realView.getBottom()), false);
            styleAccumulator.store("width", String.valueOf(realView.getWidth()), false);
            styleAccumulator.store("height", String.valueOf(realView.getHeight()), false);
            if (!(realView.getPaddingTop() == 0 && realView.getPaddingBottom() == 0 && realView.getPaddingLeft() == 0 && realView.getPaddingRight() == 0)) {
                styleAccumulator.store(W3CStyleConstants.PADDING_LEFT, String.valueOf(realView.getPaddingLeft()), false);
                styleAccumulator.store(W3CStyleConstants.PADDING_TOP, String.valueOf(realView.getPaddingTop()), false);
                styleAccumulator.store(W3CStyleConstants.PADDING_RIGHT, String.valueOf(realView.getPaddingRight()), false);
                styleAccumulator.store(W3CStyleConstants.PADDING_BOTTOM, String.valueOf(realView.getPaddingBottom()), false);
            }
            styleAccumulator.store("visibility", String.valueOf(realView.isShown()), false);
        }
    }

    /* access modifiers changed from: protected */
    public void onGetAttributes(WXComponent wXComponent, AttributeAccumulator attributeAccumulator) {
        HashMap<String, String> attribute = getAttribute(wXComponent);
        if (attribute != null && attribute.size() > 0) {
            for (Map.Entry next : attribute.entrySet()) {
                attributeAccumulator.store((String) next.getKey(), (String) next.getValue());
            }
        }
        View realView = wXComponent.getRealView();
        if (realView != null && !realView.isShown()) {
            attributeAccumulator.store("visibility", String.valueOf(realView.isShown()));
        }
    }
}
