package com.taobao.weex.ui.action;

import androidx.collection.ArrayMap;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentFactory;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.Map;
import java.util.Set;

public abstract class GraphicActionAbstractAddElement extends BasicGraphicAction {
    protected Map<String, String> mAttributes;
    protected float[] mBorders;
    protected String mComponentType;
    protected Set<String> mEvents;
    protected int mIndex = -1;
    protected float[] mMargins;
    protected float[] mPaddings;
    protected String mParentRef;
    protected Map<String, String> mStyle;
    private long startTime = System.currentTimeMillis();

    public GraphicActionAbstractAddElement(WXSDKInstance wXSDKInstance, String str) {
        super(wXSDKInstance, str);
    }

    /* access modifiers changed from: protected */
    public WXComponent createComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        long currentTimeMillis = System.currentTimeMillis();
        if (basicComponentData != null) {
            basicComponentData.addStyle(this.mStyle);
            basicComponentData.addAttr(this.mAttributes);
            basicComponentData.addEvent(this.mEvents);
            basicComponentData.addShorthand(this.mMargins, CSSShorthand.TYPE.MARGIN);
            basicComponentData.addShorthand(this.mPaddings, CSSShorthand.TYPE.PADDING);
            basicComponentData.addShorthand(this.mBorders, CSSShorthand.TYPE.BORDER);
        }
        WXComponent newInstance = WXComponentFactory.newInstance(wXSDKInstance, wXVContainer, basicComponentData);
        WXSDKManager.getInstance().getWXRenderManager().registerComponent(getPageId(), getRef(), newInstance);
        if (this.mStyle != null && this.mStyle.containsKey("transform") && newInstance.getTransition() == null) {
            ArrayMap arrayMap = new ArrayMap(2);
            arrayMap.put("transform", this.mStyle.get("transform"));
            arrayMap.put(Constants.Name.TRANSFORM_ORIGIN, this.mStyle.get(Constants.Name.TRANSFORM_ORIGIN));
            newInstance.addAnimationForElement(arrayMap);
        }
        wXSDKInstance.onComponentCreate(newInstance, System.currentTimeMillis() - currentTimeMillis);
        return newInstance;
    }

    public void executeAction() {
        getWXSDKIntance().callActionAddElementTime(System.currentTimeMillis() - this.startTime);
    }

    public String getComponentType() {
        return this.mComponentType;
    }

    public String getParentRef() {
        return this.mParentRef;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public Map<String, String> getStyle() {
        return this.mStyle;
    }

    public Map<String, String> getAttributes() {
        return this.mAttributes;
    }

    public Set<String> getEvents() {
        return this.mEvents;
    }
}
