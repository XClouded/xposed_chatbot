package com.taobao.weex.ui.component.basic;

import android.view.View;
import androidx.annotation.NonNull;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;
import java.util.Set;

public abstract class WXBasicComponent<T extends View> {
    private BasicComponentData mBasicComponentData;
    private String mComponentType;
    private Object mExtra;
    private boolean mIsLayoutRTL;
    private GraphicPosition mLayoutPosition;
    private GraphicSize mLayoutSize;
    private String mRef;
    private int mViewPortWidth = FeatureFactory.PRIORITY_ABOVE_NORMAL;

    public WXBasicComponent(BasicComponentData basicComponentData) {
        this.mBasicComponentData = basicComponentData;
        this.mRef = basicComponentData.mRef;
        this.mComponentType = basicComponentData.mComponentType;
    }

    public BasicComponentData getBasicComponentData() {
        return this.mBasicComponentData;
    }

    /* access modifiers changed from: protected */
    public void bindComponent(WXComponent wXComponent) {
        this.mComponentType = wXComponent.getComponentType();
        this.mRef = wXComponent.getRef();
    }

    @NonNull
    public final WXStyle getStyles() {
        return this.mBasicComponentData.getStyles();
    }

    @NonNull
    public final WXAttr getAttrs() {
        return this.mBasicComponentData.getAttrs();
    }

    @NonNull
    public final WXEvent getEvents() {
        return this.mBasicComponentData.getEvents();
    }

    @NonNull
    public final CSSShorthand getMargin() {
        return this.mBasicComponentData.getMargin();
    }

    @NonNull
    public final CSSShorthand getPadding() {
        return this.mBasicComponentData.getPadding();
    }

    @NonNull
    public CSSShorthand getBorder() {
        return this.mBasicComponentData.getBorder();
    }

    public final void setMargins(@NonNull CSSShorthand cSSShorthand) {
        this.mBasicComponentData.setMargins(cSSShorthand);
    }

    public final void setPaddings(@NonNull CSSShorthand cSSShorthand) {
        this.mBasicComponentData.setPaddings(cSSShorthand);
    }

    public final void setBorders(@NonNull CSSShorthand cSSShorthand) {
        this.mBasicComponentData.setBorders(cSSShorthand);
    }

    public final void addAttr(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.addAttr(map);
        }
    }

    public final void addStyle(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.addStyle(map);
        }
    }

    public final void addStyle(Map<String, Object> map, boolean z) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.addStyle(map, z);
        }
    }

    public final void updateStyle(Map<String, Object> map, boolean z) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.getStyles().updateStyle(map, z);
        }
    }

    public final void addEvent(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            this.mBasicComponentData.addEvent(set);
        }
    }

    public final void addShorthand(Map<String, String> map) {
        if (!map.isEmpty() && this.mBasicComponentData != null) {
            this.mBasicComponentData.addShorthand(map);
        }
    }

    public int getViewPortWidth() {
        return this.mViewPortWidth;
    }

    public void setViewPortWidth(int i) {
        this.mViewPortWidth = i;
    }

    public Object getExtra() {
        return this.mExtra;
    }

    public void updateExtra(Object obj) {
        this.mExtra = obj;
    }

    public String getComponentType() {
        return this.mComponentType;
    }

    public String getRef() {
        return this.mRef;
    }

    public void setIsLayoutRTL(boolean z) {
        this.mIsLayoutRTL = z;
    }

    public boolean isLayoutRTL() {
        return this.mIsLayoutRTL;
    }

    public GraphicPosition getLayoutPosition() {
        if (this.mLayoutPosition == null) {
            this.mLayoutPosition = new GraphicPosition(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return this.mLayoutPosition;
    }

    /* access modifiers changed from: protected */
    public void setLayoutPosition(GraphicPosition graphicPosition) {
        this.mLayoutPosition = graphicPosition;
    }

    public GraphicSize getLayoutSize() {
        if (this.mLayoutSize == null) {
            this.mLayoutSize = new GraphicSize(0.0f, 0.0f);
        }
        return this.mLayoutSize;
    }

    /* access modifiers changed from: protected */
    public void setLayoutSize(GraphicSize graphicSize) {
        this.mLayoutSize = graphicSize;
    }

    public float getCSSLayoutTop() {
        if (this.mLayoutPosition == null) {
            return 0.0f;
        }
        return this.mLayoutPosition.getTop();
    }

    public float getCSSLayoutBottom() {
        if (this.mLayoutPosition == null) {
            return 0.0f;
        }
        return this.mLayoutPosition.getBottom();
    }

    public float getCSSLayoutLeft() {
        if (this.mLayoutPosition == null) {
            return 0.0f;
        }
        return this.mLayoutPosition.getLeft();
    }

    public float getCSSLayoutRight() {
        if (this.mLayoutPosition == null) {
            return 0.0f;
        }
        return this.mLayoutPosition.getRight();
    }

    public float getLayoutWidth() {
        if (this.mLayoutSize == null) {
            return 0.0f;
        }
        return this.mLayoutSize.getWidth();
    }

    public float getLayoutHeight() {
        if (this.mLayoutSize == null) {
            return 0.0f;
        }
        return this.mLayoutSize.getHeight();
    }
}
