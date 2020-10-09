package com.alimama.moon.di.component;

import com.alimama.moon.App;
import com.alimama.moon.di.module.AppModule;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.moon.ui.MonkeyActivity;
import com.alimama.moon.ui.PageRouterActivity;
import com.alimama.moon.ui.WizardActivity;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.ui.splashad.SplashAdActivity;
import com.alimama.moon.update.UpdateActivity;
import com.alimama.moon.update.UpdateRemindDialog;
import com.alimama.moon.web.MoonJSBridge;
import com.alimama.moon.web.WebActivity;
import com.alimama.union.app.aalogin.view.LoginChooserActivity;
import com.alimama.union.app.contact.model.WVContactPlugin;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin;
import com.alimama.union.app.infrastructure.image.download.ImageDownloaderModule;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.infrastructure.permission.WVPermissionPlugin;
import com.alimama.union.app.infrastructure.socialShare.ShareModuleAdapter;
import com.alimama.union.app.infrastructure.socialShare.WVSharePlugin;
import com.alimama.union.app.infrastructure.weex.ShareImageModule;
import com.alimama.union.app.messageCenter.viewmodel.AlertMessageViewModel;
import com.alimama.union.app.personalCenter.view.NewMineFragment;
import com.alimama.union.app.personalCenter.viewmodel.MineViewModel;
import com.alimama.union.app.rxnetwork.RxRequestManager;
import com.alimama.union.app.share.ShareActivity;
import com.alimama.union.app.share.SharePosterActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);

    void inject(BaseActivity baseActivity);

    void inject(BottomNavActivity bottomNavActivity);

    void inject(MonkeyActivity monkeyActivity);

    void inject(PageRouterActivity pageRouterActivity);

    void inject(WizardActivity wizardActivity);

    void inject(BaseFragment baseFragment);

    void inject(SplashAdActivity splashAdActivity);

    void inject(UpdateActivity updateActivity);

    void inject(UpdateRemindDialog updateRemindDialog);

    void inject(MoonJSBridge moonJSBridge);

    void inject(WebActivity webActivity);

    void inject(LoginChooserActivity loginChooserActivity);

    void inject(WVContactPlugin wVContactPlugin);

    void inject(WVImagePlugin wVImagePlugin);

    void inject(ImageDownloaderModule imageDownloaderModule);

    void inject(StoragePermissionValidator storagePermissionValidator);

    void inject(WVPermissionPlugin wVPermissionPlugin);

    void inject(ShareModuleAdapter shareModuleAdapter);

    void inject(WVSharePlugin wVSharePlugin);

    void inject(ShareImageModule shareImageModule);

    void inject(AlertMessageViewModel alertMessageViewModel);

    void inject(NewMineFragment newMineFragment);

    void inject(MineViewModel mineViewModel);

    void inject(RxRequestManager rxRequestManager);

    void inject(ShareActivity shareActivity);

    void inject(SharePosterActivity sharePosterActivity);
}
