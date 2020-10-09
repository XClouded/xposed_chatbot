package com.alimama.union.app.aalogin;

import com.taobao.login4android.login.DefaultTaobaoAppProvider;

public class NTaobaoAppProvider extends DefaultTaobaoAppProvider {
    public NTaobaoAppProvider() {
        this.needWindVaneInit = false;
        this.isTaobaoApp = false;
        this.needTaobaoSsoGuide = false;
        this.needPwdGuide = true;
        this.alipaySsoDesKey = "authlogin_tbsdk_android_aes128";
    }
}
