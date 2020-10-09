package com.alibaba.aliweex.hc.module;

import androidx.fragment.app.FragmentActivity;
import com.alibaba.aliweex.hc.HC;
import com.alibaba.aliweex.hc.HCConfig;
import com.alibaba.aliweex.hc.HCConfigManager;
import com.alibaba.aliweex.hc.IHCModuleAdapter;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class WXHCModule extends WXModule {
    private int mIndex = -1;
    private HCConfigManager marketConfigManager;

    @JSMethod
    public void setTBHCConfig(JSONObject jSONObject) {
        if (this.mWXSDKInstance != null) {
            this.marketConfigManager = new HCConfigManager(jSONObject);
            updateActionBar((FragmentActivity) this.mWXSDKInstance.getContext(), this.marketConfigManager.getMarketConfig());
        }
    }

    @JSMethod
    public void setTabIndex(int i) {
        if (this.mWXSDKInstance != null) {
            HCConfig hCConfig = null;
            if (this.marketConfigManager != null) {
                hCConfig = this.marketConfigManager.getMarketConfig(i);
            }
            if (hCConfig != null) {
                this.mIndex = i;
                updateActionBar((FragmentActivity) this.mWXSDKInstance.getContext(), hCConfig);
            }
        }
    }

    public void updateActionBar(FragmentActivity fragmentActivity, HCConfig hCConfig) {
        IHCModuleAdapter hCModuleAdapter = HC.getInstance().getHCModuleAdapter();
        if (hCModuleAdapter != null) {
            hCModuleAdapter.updateActionBar(fragmentActivity, hCConfig);
        }
    }

    @JSMethod
    public void recoverHCConfig() {
        if (this.mWXSDKInstance != null && this.marketConfigManager != null) {
            HCConfig marketConfig = this.marketConfigManager.getMarketConfig();
            if (this.mIndex != -1) {
                marketConfig = this.marketConfigManager.getMarketConfig(this.mIndex);
            }
            updateActionBar((FragmentActivity) this.mWXSDKInstance.getContext(), marketConfig);
        }
    }
}
