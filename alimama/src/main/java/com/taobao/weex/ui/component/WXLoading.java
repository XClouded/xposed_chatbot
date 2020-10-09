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
import com.taobao.weex.ui.view.WXLoadingLayout;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;

@Component(lazyload = false)
public class WXLoading extends WXBaseRefresh implements WXSwipeLayout.WXOnLoadingListener {
    public static final String HIDE = "hide";

    public boolean canRecycled() {
        return false;
    }

    public WXLoading(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public WXFrameLayout initComponentHostView(@NonNull Context context) {
        return new WXLoadingLayout(context);
    }

    public void onLoading() {
        if (getEvents().contains("loading")) {
            fireEvent("loading");
        }
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(WXFrameLayout wXFrameLayout, int i, int i2, int i3, int i4, int i5, int i6) {
        super.setHostLayoutParams(wXFrameLayout, i, i2, 0, 0, 0, 0);
    }

    public void onPullingUp(float f, int i, float f2) {
        if (getEvents().contains(Constants.Event.ONPULLING_UP)) {
            HashMap hashMap = new HashMap();
            hashMap.put("dy", Float.valueOf(f));
            hashMap.put("pullingDistance", Integer.valueOf(i));
            hashMap.put("viewHeight", Float.valueOf(f2));
            fireEvent(Constants.Event.ONPULLING_UP, hashMap);
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
                ((BaseBounceView) getParent().getHostView()).finishPullLoad();
                ((BaseBounceView) getParent().getHostView()).onLoadmoreComplete();
            }
        }
    }
}
