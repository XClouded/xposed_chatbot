package com.taobao.monitor.adapter;

import android.app.Application;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.taobao.monitor.impl.processor.fragmentload.FragmentInterceptorProxy;
import com.taobao.monitor.impl.processor.fragmentload.IFragmentInterceptor;
import com.taobao.monitor.impl.processor.launcher.PageList;
import java.util.HashMap;

public class TBAPMInitiator extends AbsAPMInitiator {
    public /* bridge */ /* synthetic */ void init(Application application, HashMap hashMap) {
        super.init(application, hashMap);
    }

    /* access modifiers changed from: protected */
    public void initPage() {
        PageList.addBlackPage("com.taobao.tao.welcome.Welcome");
        PageList.addBlackPage("com.taobao.bootimage.activity.BootImageActivity");
        PageList.addBlackPage("com.taobao.linkmanager.afc.TbFlowInActivity");
        PageList.addBlackPage("com.taobao.tao.detail.activity.DetailActivity");
        PageList.addWhitePage("com.taobao.tao.homepage.MainActivity3");
        PageList.addWhitePage("com.taobao.tao.TBMainActivity");
        PageList.addWhitePage("com.taobao.search.sf.MainSearchResultActivity");
        PageList.addWhitePage("com.taobao.browser.BrowserActivity");
        PageList.addWhitePage("com.taobao.android.detail.wrapper.activity.DetailActivity");
        PageList.addWhitePage("com.taobao.order.detail.ui.OrderDetailActivity");
        PageList.addWhitePage("com.taobao.message.accounts.activity.AccountActivity");
        PageList.addWhitePage("com.taobao.android.shop.activity.ShopHomePageActivity");
        PageList.addWhitePage("com.taobao.weex.WXActivity");
        PageList.addWhitePage("com.taobao.android.trade.cart.CartActivity");
        PageList.addComplexPage("com.taobao.tao.homepage.MainActivity3");
        PageList.addComplexPage("com.taobao.android.detail.wrapper.activity.DetailActivity");
        PageList.addComplexPage("com.taobao.android.shop.activity.ShopHomePageActivity");
        PageList.addComplexPage("com.taobao.weex.WXActivity");
        PageList.addComplexPage("com.taobao.tao.TBMainActivity");
    }

    /* access modifiers changed from: protected */
    public void initExpendLauncher(Application application) {
        FragmentInterceptorProxy.INSTANCE.setInterceptor(new IFragmentInterceptor() {
            public boolean needPopFragment(Fragment fragment) {
                FragmentActivity activity = fragment.getActivity();
                if (activity != null) {
                    return "com.taobao.tao.TBMainActivity".equals(activity.getClass().getName());
                }
                return false;
            }
        });
    }
}
