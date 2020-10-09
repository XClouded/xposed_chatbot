package com.taobao.weex.ui.animation;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.action.GraphicActionAnimation;
import com.taobao.weex.ui.component.WXComponent;

public class WXAnimationModule extends WXModule {
    @JSMethod
    public void transition(@Nullable String str, @Nullable String str2, @Nullable String str3) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && this.mWXSDKInstance != null) {
            GraphicActionAnimation graphicActionAnimation = new GraphicActionAnimation(this.mWXSDKInstance, str, str2, str3);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAnimation.getPageId(), graphicActionAnimation);
        }
    }

    public static class AnimationHolder {
        private String callback;
        private WXAnimationBean wxAnimationBean;

        public void execute(WXSDKInstance wXSDKInstance, WXComponent wXComponent) {
            if (wXSDKInstance != null && wXComponent != null) {
                GraphicActionAnimation graphicActionAnimation = new GraphicActionAnimation(wXSDKInstance, wXComponent.getRef(), this.wxAnimationBean, this.callback);
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAnimation.getPageId(), graphicActionAnimation);
            }
        }

        public AnimationHolder(WXAnimationBean wXAnimationBean, String str) {
            this.wxAnimationBean = wXAnimationBean;
            this.callback = str;
        }
    }
}
