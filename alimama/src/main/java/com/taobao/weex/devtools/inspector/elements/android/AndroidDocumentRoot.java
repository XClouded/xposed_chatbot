package com.taobao.weex.devtools.inspector.elements.android;

import android.app.Application;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.inspector.elements.AbstractChainedDescriptor;
import com.taobao.weex.devtools.inspector.elements.NodeType;
import com.taobao.weex.devtools.inspector.protocol.module.DOM;
import com.taobao.weex.ui.WXRenderManager;
import java.util.List;

public final class AndroidDocumentRoot extends AbstractChainedDescriptor<AndroidDocumentRoot> {
    private Application mApplication;

    /* access modifiers changed from: protected */
    public String onGetNodeName(AndroidDocumentRoot androidDocumentRoot) {
        return ProtocolConst.KEY_ROOT;
    }

    public AndroidDocumentRoot(Application application) {
        this.mApplication = (Application) Util.throwIfNull(application);
    }

    /* access modifiers changed from: protected */
    public NodeType onGetNodeType(AndroidDocumentRoot androidDocumentRoot) {
        return NodeType.DOCUMENT_NODE;
    }

    /* access modifiers changed from: protected */
    public void onGetChildren(AndroidDocumentRoot androidDocumentRoot, Accumulator<Object> accumulator) {
        List<WXSDKInstance> allInstances;
        if (DOM.isNativeMode()) {
            accumulator.store(this.mApplication);
            return;
        }
        WXRenderManager wXRenderManager = WXSDKManager.getInstance().getWXRenderManager();
        if (wXRenderManager != null && (allInstances = wXRenderManager.getAllInstances()) != null && !allInstances.isEmpty()) {
            for (WXSDKInstance store : allInstances) {
                accumulator.store(store);
            }
        }
    }
}
