package com.taobao.taolive.weexext.bubble;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import com.taobao.taolive.uikit.favor.FavorLayout;
import com.taobao.taolive.uikit.utils.ResourceManager;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.ArrayList;

public class TaoliveBubbleComponent extends WXComponent {
    private static final int MAX_BUBBLE_DURATION = 5000;
    private static final int MAX_BUBBLE_NUM = 10;
    private static final int MAX_BUBBLE_SCALE = 1;
    private static final int MIN_BUBBLE_DURAION = 1000;
    private static final int MIN_BUBBLE_NUM = 1;
    private static final int MIN_BUBBLE_SCALE = 0;
    /* access modifiers changed from: private */
    public FavorLayout mFavorLayout;

    public TaoliveBubbleComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    private void initBubbleView(Context context) {
        this.mFavorLayout = new FavorLayout(context);
        if (this.mFavorLayout != null) {
            this.mFavorLayout.setFavorDuration(2000);
            this.mFavorLayout.setScaleFactor(0.5d);
        }
    }

    @WXComponentProp(name = "scale")
    public void setBubbleScale(double d) {
        if (d < 0.0d) {
            d = 0.0d;
        } else if (d > 1.0d) {
            d = 1.0d;
        }
        if (this.mFavorLayout != null) {
            this.mFavorLayout.setScaleFactor(d);
        }
    }

    @WXComponentProp(name = "loop")
    public void setBubbleLoop(boolean z) {
        if (z) {
            if (this.mFavorLayout != null) {
                this.mFavorLayout.startFakeFavor();
            }
        } else if (this.mFavorLayout != null) {
            this.mFavorLayout.stopFakeFavor();
        }
    }

    @WXComponentProp(name = "speed")
    public void setBubbleSpeed(int i) {
        if (i < 1000) {
            i = 1000;
        } else if (i > 5000) {
            i = 5000;
        }
        if (this.mFavorLayout != null) {
            this.mFavorLayout.setFavorDuration(i);
        }
    }

    @WXComponentProp(name = "bubble-zip")
    public void setBubbleZip(String str) {
        if (this.mFavorLayout != null && !TextUtils.isEmpty(str)) {
            ResourceManager.getInstance().getDrawables(str, new ResourceManager.IGetDrawablesListener() {
                public void onGetFail() {
                }

                public void onGetSuccess(ArrayList<Drawable> arrayList) {
                    if (arrayList != null && arrayList.size() > 0) {
                        TaoliveBubbleComponent.this.mFavorLayout.setDrawables(arrayList);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        initBubbleView(context);
        return this.mFavorLayout;
    }

    @JSMethod
    public void start() {
        if (this.mFavorLayout != null) {
            this.mFavorLayout.startFakeFavor();
        }
    }

    @JSMethod
    public void stop() {
        if (this.mFavorLayout != null) {
            this.mFavorLayout.stopFakeFavor();
        }
    }

    @JSMethod
    public void bubble(int i) {
        if (i >= 1) {
            if (i > 10) {
                i = 10;
            }
            if (this.mFavorLayout != null) {
                this.mFavorLayout.addFavor(i);
            }
        }
    }
}
