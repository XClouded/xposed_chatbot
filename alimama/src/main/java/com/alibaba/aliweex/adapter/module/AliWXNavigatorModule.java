package com.alibaba.aliweex.adapter.module;

import android.view.Menu;
import com.alibaba.aliweex.AliWXSDKInstance;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.taobao.weex.appfram.navigator.WXNavigatorModule;

public class AliWXNavigatorModule extends WXNavigatorModule {
    public boolean onCreateOptionsMenu(Menu menu) {
        INavigationBarModuleAdapter navigationBarModuleAdapter = AliWeex.getInstance().getNavigationBarModuleAdapter();
        if (navigationBarModuleAdapter == null && (this.mWXSDKInstance instanceof AliWXSDKInstance)) {
            navigationBarModuleAdapter = ((AliWXSDKInstance) this.mWXSDKInstance).getWXNavBarAdapter();
        }
        if (navigationBarModuleAdapter != null) {
            navigationBarModuleAdapter.onCreateOptionsMenu(this.mWXSDKInstance, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
