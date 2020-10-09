package com.alibaba.android.enhance.nested.nested;

import android.content.Context;
import android.view.ViewGroup;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;

public class WXNestedChild extends WXListComponent {
    private static final String PROP_ACTIVE = "active";
    private boolean isActive;

    public WXNestedChild(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        if (!PROP_ACTIVE.equals(str)) {
            return super.setProperty(str, obj);
        }
        setActive(WXUtils.getBoolean(obj, false).booleanValue());
        return true;
    }

    @WXComponentProp(name = "active")
    private void setActive(boolean z) {
        WXNestedParent findParent;
        this.isActive = z;
        if (this.isActive && (findParent = findParent()) != null) {
            findParent.holdActiveChild(this);
        }
    }

    private WXNestedParent findParent() {
        Object hostView = getHostView();
        while (true) {
            ViewGroup viewGroup = (ViewGroup) hostView;
            if (viewGroup == null) {
                return null;
            }
            if (viewGroup instanceof WXCoordinatorLayout) {
                return ((WXCoordinatorLayout) viewGroup).getComponent();
            }
            hostView = viewGroup.getParent();
            if (!(hostView instanceof ViewGroup)) {
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public BounceRecyclerView generateListView(Context context, int i) {
        BounceRecyclerView generateListView = super.generateListView(context, i);
        if (generateListView.getSwipeLayout() != null) {
            try {
                generateListView.getSwipeLayout().setNestedScrollingEnabled(true);
            } catch (Throwable th) {
                WXLogUtils.e(th.getMessage());
                if (WXEnvironment.isApkDebugable()) {
                    throw new RuntimeException(th.getMessage());
                }
            }
        }
        return generateListView;
    }
}
