package com.taobao.taolive.weexext.cardsuite;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.taobao.taolive.uikit.cardsuite.view.TBLiveMiddleCard;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class TBLiveMiddleCardWeexComponent extends WXComponent {
    Context mContext;
    TBLiveMiddleCard mMiddleCard;

    @JSMethod
    public void hideGoodBubble() {
    }

    @JSMethod
    public void showGoodBubble() {
    }

    @JSMethod
    public void startPlayVideo() {
    }

    @JSMethod
    public void stopPlayVideo() {
    }

    public TBLiveMiddleCardWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    private void initMiddlecardView(Context context) {
        this.mMiddleCard = new TBLiveMiddleCard(context);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        this.mContext = context;
        initMiddlecardView(context);
        return this.mMiddleCard;
    }

    @JSMethod
    public void stopFavor() {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.stopFavor();
        }
    }

    @JSMethod
    public void startFavor() {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.startFavor();
        }
    }

    @JSMethod
    public void clickEvent() {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.clickEvent();
        }
    }

    @WXComponentProp(name = "videoDO")
    public void setCardParams(String str) {
        if (!TextUtils.isEmpty(str) && this.mMiddleCard != null) {
            try {
                this.mMiddleCard.setParams(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @WXComponentProp(name = "hideFavor")
    public void hideFavor(boolean z) {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.setHideFavor(z);
        }
    }

    @WXComponentProp(name = "hideAccountInfo")
    public void hideAccountInfo(boolean z) {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.setHideAccountInfo(z);
        }
    }

    @WXComponentProp(name = "hideLiveStateInfo")
    public void hideLiveStatusInfo(boolean z) {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.setHideLiveStateInfo(z);
        }
    }

    @WXComponentProp(name = "hideTitle")
    public void hideTitle(boolean z) {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.setHideTitle(z);
        }
    }

    public void destroy() {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.stopFavor();
        }
        super.destroy();
    }

    @WXComponentProp(name = "subType")
    public void setSubType(String str) {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.setSubType(str);
        }
    }

    @WXComponentProp(name = "sceneType")
    public void setSceneType(String str) {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.setSceneType(str);
        }
    }

    @JSMethod
    public void exposureEvent() {
        if (this.mMiddleCard != null) {
            this.mMiddleCard.exposureEvent();
        }
    }
}
