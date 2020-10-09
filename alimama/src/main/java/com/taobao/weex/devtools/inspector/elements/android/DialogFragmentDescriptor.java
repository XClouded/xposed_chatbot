package com.taobao.weex.devtools.inspector.elements.android;

import android.view.View;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.common.android.DialogFragmentAccessor;
import com.taobao.weex.devtools.common.android.FragmentCompat;
import com.taobao.weex.devtools.inspector.elements.AttributeAccumulator;
import com.taobao.weex.devtools.inspector.elements.ChainedDescriptor;
import com.taobao.weex.devtools.inspector.elements.Descriptor;
import com.taobao.weex.devtools.inspector.elements.DescriptorMap;
import com.taobao.weex.devtools.inspector.elements.NodeType;
import com.taobao.weex.devtools.inspector.elements.StyleAccumulator;
import javax.annotation.Nullable;

final class DialogFragmentDescriptor extends Descriptor implements ChainedDescriptor, HighlightableDescriptor {
    private final DialogFragmentAccessor mAccessor;
    private Descriptor mSuper;

    public void getStyles(Object obj, StyleAccumulator styleAccumulator) {
    }

    public static DescriptorMap register(DescriptorMap descriptorMap) {
        maybeRegister(descriptorMap, FragmentCompat.getSupportLibInstance());
        maybeRegister(descriptorMap, FragmentCompat.getFrameworkInstance());
        return descriptorMap;
    }

    private static void maybeRegister(DescriptorMap descriptorMap, @Nullable FragmentCompat fragmentCompat) {
        if (fragmentCompat != null) {
            Class dialogFragmentClass = fragmentCompat.getDialogFragmentClass();
            LogUtil.d("Adding support for %s", dialogFragmentClass);
            descriptorMap.register(dialogFragmentClass, new DialogFragmentDescriptor(fragmentCompat));
        }
    }

    private DialogFragmentDescriptor(FragmentCompat fragmentCompat) {
        this.mAccessor = fragmentCompat.forDialogFragment();
    }

    public void setSuper(Descriptor descriptor) {
        Util.throwIfNull(descriptor);
        if (descriptor == this.mSuper) {
            return;
        }
        if (this.mSuper == null) {
            this.mSuper = descriptor;
            return;
        }
        throw new IllegalStateException();
    }

    public void hook(Object obj) {
        this.mSuper.hook(obj);
    }

    public void unhook(Object obj) {
        this.mSuper.unhook(obj);
    }

    public NodeType getNodeType(Object obj) {
        return this.mSuper.getNodeType(obj);
    }

    public String getNodeName(Object obj) {
        return this.mSuper.getNodeName(obj);
    }

    public String getLocalName(Object obj) {
        return this.mSuper.getLocalName(obj);
    }

    @Nullable
    public String getNodeValue(Object obj) {
        return this.mSuper.getNodeValue(obj);
    }

    public void getChildren(Object obj, Accumulator<Object> accumulator) {
        accumulator.store(this.mAccessor.getDialog(obj));
    }

    public void getAttributes(Object obj, AttributeAccumulator attributeAccumulator) {
        this.mSuper.getAttributes(obj, attributeAccumulator);
    }

    public void setAttributesAsText(Object obj, String str) {
        this.mSuper.setAttributesAsText(obj, str);
    }

    @Nullable
    public View getViewForHighlighting(Object obj) {
        Descriptor.Host host = getHost();
        if (!(host instanceof AndroidDescriptorHost)) {
            return null;
        }
        return ((AndroidDescriptorHost) host).getHighlightingView(this.mAccessor.getDialog(obj));
    }
}
