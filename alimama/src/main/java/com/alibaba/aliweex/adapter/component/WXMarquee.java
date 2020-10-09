package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.view.Marquee;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXMarquee extends WXVContainer<Marquee> {
    private List<View> mViews = new ArrayList();

    public WXMarquee(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public Marquee initComponentHostView(@NonNull Context context) {
        return new Marquee(context);
    }

    public void createViewImpl() {
        super.createViewImpl();
        ((Marquee) getHostView()).setClipChildren(true);
    }

    public ViewGroup getRealView() {
        return (ViewGroup) ((Marquee) getHostView()).getRealView();
    }

    @WXComponentProp(name = "delay")
    public void setDelay(String str) {
        if (str != null && TextUtils.isDigitsOnly(str.trim())) {
            long parseLong = Long.parseLong(str.trim());
            if (parseLong > 0) {
                ((Marquee) getHostView()).setDelayTime(parseLong);
            }
        }
    }

    @WXComponentProp(name = "interval")
    public void setInterval(String str) {
        if (str != null && TextUtils.isDigitsOnly(str.trim())) {
            long parseLong = Long.parseLong(str.trim());
            if (parseLong > 0) {
                ((Marquee) getHostView()).setIntervalTime(parseLong);
            }
        }
    }

    @WXComponentProp(name = "transitionDuration")
    public void setTransitionDuration(String str) {
        if (str != null && TextUtils.isDigitsOnly(str.trim())) {
            long parseLong = Long.parseLong(str.trim());
            if (parseLong > 0) {
                ((Marquee) getHostView()).setDurationTime(parseLong);
            }
        }
    }

    public void updateProperties(Map map) {
        super.updateProperties(map);
        ((Marquee) getHostView()).setViewList(this.mViews, (FrameLayout.LayoutParams) getView().getLayoutParams());
        ((Marquee) getHostView()).startScrollA();
    }

    public void addSubView(View view, int i) {
        this.mViews.add(view);
    }

    public void remove(WXComponent wXComponent, boolean z) {
        this.mViews.clear();
        super.remove(wXComponent, z);
    }

    public void onActivityPause() {
        super.onActivityPause();
        if (getHostView() != null) {
            ((Marquee) getHostView()).stopScroll();
        }
    }

    public void onActivityResume() {
        super.onActivityResume();
        if (getHostView() != null) {
            ((Marquee) getHostView()).startScroll();
        }
    }

    public void destroy() {
        super.destroy();
        this.mViews.clear();
        ((Marquee) getHostView()).destroy();
    }
}
