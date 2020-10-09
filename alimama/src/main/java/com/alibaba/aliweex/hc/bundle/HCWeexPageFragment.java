package com.alibaba.aliweex.hc.bundle;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alibaba.aliweex.hc.cache.CachePerf;
import com.alibaba.aliweex.hc.cache.PackageCache;
import com.alibaba.aliweex.hc.cache.WeexCacheMsgPanel;
import com.alibaba.fastjson.JSONArray;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;

public class HCWeexPageFragment extends WeexPageFragment {
    public static final String WH_WX = "wh_weex";
    public static final String WX_TPL = "_wx_tpl";

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z && getNavBarAdapter() != null && (getNavBarAdapter() instanceof WXHCNavBarAdapter) && !((WXHCNavBarAdapter) getNavBarAdapter()).shouldSetNavigator()) {
            WXBridgeManager.getInstance().callModuleMethod(getWXSDKInstance().getInstanceId(), "hc", "recoverHCConfig", (JSONArray) null);
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        String renderUrl = getRenderUrl();
        if (!TextUtils.isEmpty(renderUrl) && renderUrl.contains("wh_msgPanel=1")) {
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            if (supportFragmentManager.findFragmentByTag("weexMsgPanel") == null) {
                Fragment instantiate = Fragment.instantiate(getActivity(), WeexCacheMsgPanel.class.getName(), (Bundle) null);
                FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
                beginTransaction.add(16908290, instantiate, "weexMsgPanel");
                beginTransaction.commitAllowingStateLoss();
            }
        }
        super.onActivityCreated(bundle);
    }

    /* access modifiers changed from: protected */
    public void onWXViewCreated(WXSDKInstance wXSDKInstance, View view) {
        super.onWXViewCreated(wXSDKInstance, view);
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            String config = configAdapter.getConfig("weexcache_cfg", "preload", "true");
            if (!TextUtils.isEmpty(config) && "true".equals(config)) {
                PackageCache.getInstance().tryToPutZCachePackageIntoMemroyCache(false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public WeexPageContract.IRenderPresenter createRenderPresenter(IWXRenderListener iWXRenderListener, WeexPageContract.IUTPresenter iUTPresenter, WeexPageContract.IDynamicUrlPresenter iDynamicUrlPresenter, WeexPageContract.IProgressBar iProgressBar, WeexPageContract.IUrlValidate iUrlValidate) {
        return new HCRenderPresenter(getActivity(), this.mFtag, iWXRenderListener, iUTPresenter, iDynamicUrlPresenter, iProgressBar, getNavBarAdapter(), iUrlValidate);
    }

    public void onDestroy() {
        super.onDestroy();
        CachePerf.getInstance().destroy();
    }
}
