package alimama.com.unwweex;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.IAliPayModuleAdapter;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.alibaba.aliweex.adapter.IPageInfoModuleAdapter;
import com.alibaba.aliweex.adapter.IShareModuleAdapter;
import com.alibaba.aliweex.adapter.IUserModuleAdapter;
import com.alibaba.aliweex.hc.adapter.HCWXHttpAdapter;
import com.ta.utdid2.device.UTDevice;
import com.taobao.android.eagle.EagleLauncher;
import com.taobao.taolive.weexext.bubble.TaoliveBubbleComponent;
import com.taobao.taolive.weexext.cardsuite.TBLiveBigCardWeexComponent;
import com.taobao.taolive.weexext.cardsuite.TBLiveMiddleCardWeexComponent;
import com.taobao.taolive.weexext.cardsuite.TBLiveSmallCardWeexComponent;
import com.taobao.taolive.weexext.drawboard.TBLiveDrawBoardWeexComponent;
import com.taobao.taolive.weexext.drawboard.TBLiveImageWeexComponent;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.TBConfigAdapter;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.module.WXEventModule;
import com.taobao.weex.module.WXPageInfoModule;
import com.taobao.weex.ui.animation.WXAnimationModule;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;

public class UNWWeex implements IWeex {
    private IAliPayModuleAdapter aliPayModuleAdapter;
    private String appName;
    private IEventModuleAdapter eventModuleAdapter;
    private IWXHttpAdapter httpAdapter;
    private IConfigAdapter iConfigAdapter;
    private boolean isDebug;
    private INavigationBarModuleAdapter navigationBarModuleAdapter;
    private IPageInfoModuleAdapter pageInfoModuleAdapter;
    private IShareModuleAdapter shareModuleAdapter;
    private IUserModuleAdapter userModuleAdapter;

    public UNWWeex(String str, boolean z) {
        this.appName = str;
        this.isDebug = z;
    }

    public void setUserModuleAdapter(IUserModuleAdapter iUserModuleAdapter) {
        this.userModuleAdapter = iUserModuleAdapter;
    }

    public void setShareModuleAdapter(IShareModuleAdapter iShareModuleAdapter) {
        this.shareModuleAdapter = iShareModuleAdapter;
    }

    public void setEventModuleAdapter(IEventModuleAdapter iEventModuleAdapter) {
        this.eventModuleAdapter = iEventModuleAdapter;
    }

    public void setPageInfoModuleAdapter(IPageInfoModuleAdapter iPageInfoModuleAdapter) {
        this.pageInfoModuleAdapter = iPageInfoModuleAdapter;
    }

    public void setAliPayModuleAdapter(IAliPayModuleAdapter iAliPayModuleAdapter) {
        this.aliPayModuleAdapter = iAliPayModuleAdapter;
    }

    public void setNavigationBarModuleAdapter(INavigationBarModuleAdapter iNavigationBarModuleAdapter) {
        this.navigationBarModuleAdapter = iNavigationBarModuleAdapter;
    }

    public void setiConfigAdapter(IConfigAdapter iConfigAdapter2) {
        this.iConfigAdapter = iConfigAdapter2;
    }

    public void setHttpAdapter(IWXHttpAdapter iWXHttpAdapter) {
        this.httpAdapter = iWXHttpAdapter;
    }

    public void init() {
        WXEnvironment.addCustomOptions("appName", this.appName);
        WXEnvironment.addCustomOptions("hasAtlas", String.valueOf(false));
        WXEnvironment.addCustomOptions("utdid", UTDevice.getUtdid(UNWManager.getInstance().application));
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment != null) {
            WXEnvironment.addCustomOptions("ttid", iAppEnvironment.getTTid());
        }
        WXEnvironment.addCustomOptions(WXConfig.debugMode, String.valueOf(this.isDebug));
        try {
            AliWeex.Config.Builder configAdapter = new AliWeex.Config.Builder().setConfigAdapter(new IConfigAdapter() {
                public boolean checkMode(String str) {
                    return false;
                }

                public String getConfig(String str, String str2, String str3) {
                    return str3;
                }

                public Map<String, String> getConfigs(String str) {
                    return null;
                }
            });
            if (this.eventModuleAdapter != null) {
                configAdapter.setEventModuleAdapter(this.eventModuleAdapter);
            } else {
                configAdapter.setEventModuleAdapter(new WXEventModule());
            }
            if (this.eventModuleAdapter != null) {
                configAdapter.setPageInfoModuleAdapter(this.pageInfoModuleAdapter);
            } else {
                configAdapter.setPageInfoModuleAdapter(new WXPageInfoModule());
            }
            if (this.eventModuleAdapter != null) {
                configAdapter.setUserModuleAdapter(this.userModuleAdapter);
            }
            if (this.eventModuleAdapter != null) {
                configAdapter.setShareModuleAdapter(this.shareModuleAdapter);
            }
            if (this.eventModuleAdapter != null) {
                configAdapter.setConfigAdapter(this.iConfigAdapter);
            } else {
                configAdapter.setConfigAdapter(new TBConfigAdapter());
            }
            if (this.eventModuleAdapter != null) {
                configAdapter.setHttpAdapter(this.httpAdapter);
            } else {
                configAdapter.setHttpAdapter(new HCWXHttpAdapter());
            }
            if (this.aliPayModuleAdapter != null) {
                configAdapter.setAliPayModuleAdapter(this.aliPayModuleAdapter);
            }
            if (this.navigationBarModuleAdapter != null) {
                configAdapter.setNavigationBarModuleAdapter(this.navigationBarModuleAdapter);
            }
            configAdapter.addNativeLibrary(EagleLauncher.soName);
            AliWeex.getInstance().initWithConfig(UNWManager.getInstance().application, configAdapter.build());
            WXSDKEngine.registerModule("anmation", WXAnimationModule.class);
            AliWXSDKEngine.initSDKEngine();
            registerModulesAndComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerModulesAndComponents() {
        try {
            WXSDKEngine.registerComponent("liveshowbubble", (Class<? extends WXComponent>) TaoliveBubbleComponent.class);
            WXSDKEngine.registerComponent("alilivebigcard", (Class<? extends WXComponent>) TBLiveBigCardWeexComponent.class);
            WXSDKEngine.registerComponent("alilivemiddlecard", (Class<? extends WXComponent>) TBLiveMiddleCardWeexComponent.class);
            WXSDKEngine.registerComponent("alilivesmallcard", (Class<? extends WXComponent>) TBLiveSmallCardWeexComponent.class);
            WXSDKEngine.registerComponent(TBLiveDrawBoardWeexComponent.NAME, (Class<? extends WXComponent>) TBLiveDrawBoardWeexComponent.class);
            WXSDKEngine.registerComponent(TBLiveImageWeexComponent.NAME, (Class<? extends WXComponent>) TBLiveImageWeexComponent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
