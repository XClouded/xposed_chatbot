package com.alibaba.aliweex.hc.component;

import com.alibaba.aliweex.adapter.component.TabLayout;
import com.alibaba.aliweex.adapter.component.WXTabbar;
import com.alibaba.aliweex.adapter.component.WXWVWeb;
import com.alibaba.aliweex.hc.HCWXSDKInstance;
import com.alibaba.fastjson.JSONArray;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;

public class HCWXTabbar extends WXTabbar {
    public HCWXTabbar(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void onTabSelected(TabLayout.Tab tab) {
        super.onTabSelected(tab);
        JSONArray jSONArray = new JSONArray();
        jSONArray.add(Integer.valueOf(tab.getPosition()));
        WXBridgeManager.getInstance().callModuleMethod(getInstance().getInstanceId(), "hc", "setTabIndex", jSONArray);
    }

    /* access modifiers changed from: protected */
    public void updateTabState(int i, boolean z) {
        HCWXSDKInstance hCWXSDKInstance;
        WXWVWeb wXWVWeb;
        super.updateTabState(i, z);
        WXTabbar.TabItem tabItem = (WXTabbar.TabItem) this.mItems.get(i);
        if ((getInstance() instanceof HCWXSDKInstance) && (hCWXSDKInstance = (HCWXSDKInstance) getInstance()) != null && (wXWVWeb = hCWXSDKInstance.getWXWVWeb(tabItem.getItemId())) != null) {
            wXWVWeb.setVisibility(z ? "visible" : "hidden");
        }
    }
}
