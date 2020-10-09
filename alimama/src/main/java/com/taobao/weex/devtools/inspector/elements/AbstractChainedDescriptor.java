package com.taobao.weex.devtools.inspector.elements;

import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.Util;
import javax.annotation.Nullable;

public abstract class AbstractChainedDescriptor<E> extends Descriptor implements ChainedDescriptor {
    private Descriptor mSuper;

    /* access modifiers changed from: protected */
    public void onGetAttributes(E e, AttributeAccumulator attributeAccumulator) {
    }

    /* access modifiers changed from: protected */
    public void onGetChildren(E e, Accumulator<Object> accumulator) {
    }

    /* access modifiers changed from: protected */
    public void onGetStyles(E e, StyleAccumulator styleAccumulator) {
    }

    /* access modifiers changed from: protected */
    public void onHook(E e) {
    }

    /* access modifiers changed from: protected */
    public void onUnhook(E e) {
    }

    public void setSuper(Descriptor descriptor) {
        Util.throwIfNull(descriptor);
        if (descriptor.equals(this.mSuper)) {
            return;
        }
        if (this.mSuper == null) {
            this.mSuper = descriptor;
            return;
        }
        throw new IllegalStateException();
    }

    public final Descriptor getSuper() {
        return this.mSuper;
    }

    public final void hook(Object obj) {
        verifyThreadAccess();
        this.mSuper.hook(obj);
        onHook(obj);
    }

    public final void unhook(Object obj) {
        verifyThreadAccess();
        onUnhook(obj);
        this.mSuper.unhook(obj);
    }

    public final NodeType getNodeType(Object obj) {
        return onGetNodeType(obj);
    }

    /* access modifiers changed from: protected */
    public NodeType onGetNodeType(E e) {
        return this.mSuper.getNodeType(e);
    }

    public final String getNodeName(Object obj) {
        return onGetNodeName(obj);
    }

    /* access modifiers changed from: protected */
    public String onGetNodeName(E e) {
        return this.mSuper.getNodeName(e);
    }

    public final String getLocalName(Object obj) {
        return onGetLocalName(obj);
    }

    /* access modifiers changed from: protected */
    public String onGetLocalName(E e) {
        return this.mSuper.getLocalName(e);
    }

    public final String getNodeValue(Object obj) {
        return onGetNodeValue(obj);
    }

    @Nullable
    public String onGetNodeValue(E e) {
        return this.mSuper.getNodeValue(e);
    }

    public final void getChildren(Object obj, Accumulator<Object> accumulator) {
        this.mSuper.getChildren(obj, accumulator);
        onGetChildren(obj, accumulator);
    }

    public final void getAttributes(Object obj, AttributeAccumulator attributeAccumulator) {
        this.mSuper.getAttributes(obj, attributeAccumulator);
        onGetAttributes(obj, attributeAccumulator);
    }

    public final void setAttributesAsText(Object obj, String str) {
        onSetAttributesAsText(obj, str);
    }

    /* access modifiers changed from: protected */
    public void onSetAttributesAsText(E e, String str) {
        this.mSuper.setAttributesAsText(e, str);
    }

    public final void getStyles(Object obj, StyleAccumulator styleAccumulator) {
        this.mSuper.getStyles(obj, styleAccumulator);
        onGetStyles(obj, styleAccumulator);
    }
}
