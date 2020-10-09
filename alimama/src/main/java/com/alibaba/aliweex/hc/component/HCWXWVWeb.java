package com.alibaba.aliweex.hc.component;

import com.alibaba.aliweex.adapter.component.WXWVWeb;
import com.alibaba.aliweex.hc.HCWXSDKInstance;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.component.WXVContainer;

public class HCWXWVWeb extends WXWVWeb {
    public HCWXWVWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        Object obj;
        if ((wXSDKInstance instanceof HCWXSDKInstance) && (obj = getAttrs().get(WXEmbed.ITEM_ID)) != null) {
            ((HCWXSDKInstance) wXSDKInstance).putWXWVWeb(obj.toString(), this);
        }
    }

    public void setVisibility(String str) {
        super.setVisibility(str);
    }
}
