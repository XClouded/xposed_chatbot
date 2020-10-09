package com.taobao.taolive.weexext.cardsuite;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.taobao.taolive.uikit.cardsuite.view.TBLiveSmallCard;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class TBLiveSmallCardWeexComponent extends WXComponent {
    Context mContext;
    TBLiveSmallCard mSmallCard;

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

    public TBLiveSmallCardWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    private void initSmallcardView(Context context) {
        this.mSmallCard = new TBLiveSmallCard(context);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        this.mContext = context;
        initSmallcardView(context);
        return this.mSmallCard;
    }

    @JSMethod
    public void stopFavor() {
        if (this.mSmallCard != null) {
            this.mSmallCard.stopFavor();
        }
    }

    @JSMethod
    public void startFavor() {
        if (this.mSmallCard != null) {
            this.mSmallCard.startFavor();
        }
    }

    @JSMethod
    public void clickEvent() {
        if (this.mSmallCard != null) {
            this.mSmallCard.clickEvent();
        }
    }

    @JSMethod
    public void exposureEvent() {
        if (this.mSmallCard != null) {
            this.mSmallCard.exposureEvent();
        }
    }

    @WXComponentProp(name = "videoDO")
    public void setCardParams(String str) {
        if (!TextUtils.isEmpty(str) && this.mSmallCard != null) {
            try {
                this.mSmallCard.setParams(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @WXComponentProp(name = "hideFavor")
    public void hideFavor(boolean z) {
        if (this.mSmallCard != null) {
            this.mSmallCard.setHideFavor(z);
        }
    }

    @WXComponentProp(name = "hideAccountInfo")
    public void hideAccountInfo(boolean z) {
        if (this.mSmallCard != null) {
            this.mSmallCard.setHideAccountInfo(z);
        }
    }

    @WXComponentProp(name = "hideLiveStateInfo")
    public void hideLiveStatusInfo(boolean z) {
        if (this.mSmallCard != null) {
            this.mSmallCard.setHideLiveStateInfo(z);
        }
    }

    @WXComponentProp(name = "hideTitle")
    public void hideTitle(boolean z) {
        if (this.mSmallCard != null) {
            this.mSmallCard.setHideTitle(z);
        }
    }

    public void destroy() {
        if (this.mSmallCard != null) {
            this.mSmallCard.stopFavor();
        }
        super.destroy();
    }

    @WXComponentProp(name = "subType")
    public void setSubType(String str) {
        if (this.mSmallCard != null) {
            this.mSmallCard.setSubType(str);
        }
    }

    @WXComponentProp(name = "sceneType")
    public void setSceneType(String str) {
        if (this.mSmallCard != null) {
            this.mSmallCard.setSceneType(str);
        }
    }
}
