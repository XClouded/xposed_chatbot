package com.taobao.taolive.weexext.cardsuite;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.taobao.taolive.uikit.cardsuite.view.TBLiveBigCard;
import com.taobao.taolive.uikit.cardsuite.view.TBLiveSingleInstanceVideoFrame;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class TBLiveBigCardWeexComponent extends WXComponent {
    TBLiveBigCard mBigcardView;
    Context mContext;

    public TBLiveBigCardWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    private void initBigcardView(Context context) {
        this.mBigcardView = new TBLiveBigCard(context);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        this.mContext = context;
        initBigcardView(context);
        return this.mBigcardView;
    }

    @JSMethod
    public void startPlayVideo() {
        if (this.mBigcardView != null) {
            this.mBigcardView.startPlayVideo();
        }
    }

    @JSMethod
    public void stopPlayVideo() {
        if (this.mBigcardView != null) {
            this.mBigcardView.stopVideo();
        }
    }

    @JSMethod
    public void stopFavor() {
        if (this.mBigcardView != null) {
            this.mBigcardView.stopFavor();
        }
    }

    @JSMethod
    public void startFavor() {
        if (this.mBigcardView != null) {
            this.mBigcardView.startFavor();
        }
    }

    @JSMethod
    public void showGoodBubble() {
        if (this.mBigcardView != null) {
            this.mBigcardView.popGoodBubble();
        }
    }

    @JSMethod
    public void hideGoodBubble() {
        if (this.mBigcardView != null) {
            this.mBigcardView.retractGoodBubble();
        }
    }

    @JSMethod
    public void clickEvent() {
        if (this.mBigcardView != null) {
            this.mBigcardView.clickEvent();
        }
    }

    @WXComponentProp(name = "videoDO")
    public void setCardParams(String str) {
        if (!TextUtils.isEmpty(str) && this.mBigcardView != null) {
            try {
                this.mBigcardView.setParams(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @WXComponentProp(name = "hideFavor")
    public void hideFavor(boolean z) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setHideFavor(z);
        }
    }

    @WXComponentProp(name = "hideAccountInfo")
    public void hideAccountInfo(boolean z) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setHideAccountInfo(z);
        }
    }

    @WXComponentProp(name = "hideLiveStateInfo")
    public void hideLiveStatusInfo(boolean z) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setHideLiveStateInfo(z);
        }
    }

    @WXComponentProp(name = "hideTitle")
    public void hideTitle(boolean z) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setHideTitle(z);
        }
    }

    public void destroy() {
        if (this.mBigcardView != null) {
            this.mBigcardView.stopFavor();
        }
        TBLiveSingleInstanceVideoFrame.destroyInstance();
        super.destroy();
    }

    @WXComponentProp(name = "subType")
    public void setSubType(String str) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setSubType(str);
        }
    }

    @WXComponentProp(name = "sceneType")
    public void setSceneType(String str) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setSceneType(str);
        }
    }

    @WXComponentProp(name = "width")
    public void setWidth(int i) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setWidth(i);
        }
    }

    @WXComponentProp(name = "height")
    public void setHeight(int i) {
        if (this.mBigcardView != null) {
            this.mBigcardView.setHeight(i);
        }
    }

    @JSMethod
    public void exposureEvent() {
        if (this.mBigcardView != null) {
            this.mBigcardView.exposureEvent();
        }
    }
}
