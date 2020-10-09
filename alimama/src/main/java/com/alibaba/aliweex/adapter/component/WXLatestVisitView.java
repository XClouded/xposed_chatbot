package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.view.NearlyAround;
import com.alibaba.aliweex.adapter.view.NearlyAroundItem;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;

public class WXLatestVisitView extends WXComponent {
    private NearlyAround mNearlyAround;

    public WXLatestVisitView(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(@NonNull Context context) {
        this.mNearlyAround = new NearlyAround(context);
        this.mNearlyAround.updataList();
        this.mNearlyAround.setOnNearlyItemClickListener(new NearlyAround.OnNearlyItemClickListener() {
            public void OnNearlyItemClick(NearlyAroundItem nearlyAroundItem) {
                if (nearlyAroundItem != null && nearlyAroundItem.getUrl() != null && WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("openUrl:" + nearlyAroundItem.getUrl());
                }
            }
        });
        return this.mNearlyAround.getRootView();
    }

    public void onActivityResume() {
        super.onActivityResume();
        if (this.mNearlyAround != null) {
            this.mNearlyAround.updataList();
        }
    }
}
