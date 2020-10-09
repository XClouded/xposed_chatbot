package com.taobao.monitor.adapter;

import android.app.Application;
import com.taobao.monitor.impl.processor.launcher.PageList;
import java.util.HashMap;

public class TMAPMInitiator extends AbsAPMInitiator {
    public /* bridge */ /* synthetic */ void init(Application application, HashMap hashMap) {
        super.init(application, hashMap);
    }

    /* access modifiers changed from: protected */
    public void initPage() {
        PageList.addBlackPage("com.tmall.wireless.splash.TMSplashActivity");
        PageList.addBlackPage("com.taobao.bootimage.activity.BootImageActivity");
        PageList.addBlackPage("com.taobao.linkmanager.AlibcEntranceActivity");
        PageList.addBlackPage("com.taobao.linkmanager.AlibcOpenActivity");
        PageList.addBlackPage("com.taobao.linkmanager.AlibcTransparentActivity");
        PageList.addBlackPage("com.taobao.linkmanager.AlibcWindvaneCompatActivity");
        PageList.addBlackPage("com.taobao.linkmanager.AlibcAuthActivity");
        PageList.addWhitePage("com.tmall.wireless.homepage.activity.TMHomePageActivity");
        PageList.addWhitePage("com.tmall.wireless.detail.ui.TMItemDetailsActivity");
        PageList.addWhitePage("com.tmall.wireless.maintab.module.TMMainTabActivity");
        PageList.addWhitePage("com.tmall.wireless.mytmall.ui.TMMtmallActivityA");
        PageList.addWhitePage("com.tmall.wireless.messagebox.activity.TMMsgboxCategoryActivity");
        PageList.addWhitePage("com.tmall.wireless.shop.TMShopActivity");
        PageList.addWhitePage("com.tmall.wireless.minidetail.activity.TMMiniDetailActivity");
        PageList.addWhitePage("com.taobao.message.accounts.activity.AccountActivity");
        PageList.addWhitePage("com.taobao.android.shop.activity.ShopHomePageActivity");
        PageList.addWhitePage("com.taobao.weex.WXActivity");
        PageList.addWhitePage("com.taobao.android.trade.cart.CartActivity");
        PageList.addWhitePage("com.tmall.wireless.login.TMLoginActivity");
    }
}
