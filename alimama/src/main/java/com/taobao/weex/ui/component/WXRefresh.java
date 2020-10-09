package com.taobao.weex.ui.component;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.WXRefreshLayout;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;

@Component(lazyload = false)
public class WXRefresh extends WXBaseRefresh implements WXSwipeLayout.WXOnRefreshListener {
    public static final String HIDE = "hide";

    public boolean canRecycled() {
        return false;
    }

    @Deprecated
    public WXRefresh(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXRefresh(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public WXFrameLayout initComponentHostView(@NonNull Context context) {
        return new WXRefreshLayout(context);
    }

    public void onRefresh() {
        if (!isDestoryed() && getEvents().contains("refresh")) {
            fireEvent("refresh");
        }
    }

    public int getLayoutTopOffsetForSibling() {
        if (getParent() instanceof Scrollable) {
            return -Math.round(getLayoutHeight());
        }
        return 0;
    }

    public void onPullingDown(float f, int i, float f2) {
        if (getEvents() != null && getEvents().contains(Constants.Event.ONPULLING_DOWN)) {
            HashMap hashMap = new HashMap();
            hashMap.put("dy", Float.valueOf(f));
            hashMap.put("pullingDistance", Integer.valueOf(i));
            hashMap.put("viewHeight", Float.valueOf(f2));
            fireEvent(Constants.Event.ONPULLING_DOWN, hashMap);
        }
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        if (((str.hashCode() == 1671764162 && str.equals("display")) ? (char) 0 : 65535) != 0) {
            return super.setProperty(str, obj);
        }
        String string = WXUtils.getString(obj, (String) null);
        if (string == null) {
            return true;
        }
        setDisplay(string);
        return true;
    }

    @WXComponentProp(name = "display")
    public void setDisplay(String str) {
        if (!TextUtils.isEmpty(str) && str.equals("hide")) {
            if (((getParent() instanceof WXListComponent) || (getParent() instanceof WXScroller)) && ((BaseBounceView) getParent().getHostView()).getSwipeLayout().isRefreshing()) {
                ((BaseBounceView) getParent().getHostView()).finishPullRefresh();
                ((BaseBounceView) getParent().getHostView()).onRefreshingComplete();
            }
        }
    }
}
