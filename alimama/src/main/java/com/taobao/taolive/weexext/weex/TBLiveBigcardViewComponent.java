package com.taobao.taolive.weexext.weex;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.taobao.taolive.uikit.mtop.LiveInfoItem;
import com.taobao.taolive.uikit.video.HomepageVideoFrame;
import com.taobao.taolive.uikit.view.TaoliveHomeBigCardView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class TBLiveBigcardViewComponent extends WXComponent {
    LiveInfoItem liveInfoItem;
    TaoliveHomeBigCardView mBigcardView;
    Context mContext;

    @JSMethod
    public void closeBubbleTips() {
    }

    @JSMethod
    public void showBubbleTips() {
    }

    @JSMethod
    public void stopPlayVideo() {
    }

    public TBLiveBigcardViewComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    private void initBigcardView(Context context) {
        this.mBigcardView = new TaoliveHomeBigCardView(context);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        this.mContext = context;
        initBigcardView(context);
        if (this.mBigcardView != null) {
            this.mBigcardView.setClickNavEnable(false);
            this.mBigcardView.setPointBuryEnable(false);
        }
        return this.mBigcardView;
    }

    @JSMethod
    public void startPlayVideo() {
        if (this.mBigcardView != null) {
            this.mBigcardView.startPlayVideo();
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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v14, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: com.taobao.taolive.uikit.mtop.GoodItem} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clickEvent() {
        /*
            r12 = this;
            com.taobao.taolive.uikit.view.TaoliveHomeBigCardView r0 = r12.mBigcardView
            if (r0 == 0) goto L_0x00f5
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            if (r0 != 0) goto L_0x000a
            goto L_0x00f5
        L_0x000a:
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            int r0 = r0.roomStatus
            r1 = 1
            r2 = 0
            r3 = 0
            if (r1 != r0) goto L_0x00c6
            com.alibaba.fastjson.JSONObject r0 = new com.alibaba.fastjson.JSONObject
            r0.<init>()
            com.alibaba.fastjson.JSONArray r1 = new com.alibaba.fastjson.JSONArray
            r1.<init>()
            java.lang.String r4 = "liveUrlList"
            r0.put((java.lang.String) r4, (java.lang.Object) r1)
            java.lang.String r4 = "h265"
            com.taobao.taolive.uikit.mtop.LiveInfoItem r5 = r12.liveInfoItem
            boolean r5 = r5.h265
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)
            r0.put((java.lang.String) r4, (java.lang.Object) r5)
            java.lang.String r4 = "mediaConfig"
            com.taobao.taolive.uikit.mtop.LiveInfoItem r5 = r12.liveInfoItem
            java.lang.String r5 = r5.mediaConfig
            r0.put((java.lang.String) r4, (java.lang.Object) r5)
            java.lang.String r4 = "rateAdapte"
            com.taobao.taolive.uikit.mtop.LiveInfoItem r5 = r12.liveInfoItem
            boolean r5 = r5.rateAdapte
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)
            r0.put((java.lang.String) r4, (java.lang.Object) r5)
            java.lang.String r4 = "useArtp"
            com.taobao.taolive.uikit.mtop.LiveInfoItem r5 = r12.liveInfoItem
            boolean r5 = r5.useArtp
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)
            r0.put((java.lang.String) r4, (java.lang.Object) r5)
            java.lang.String r4 = "liveUrl"
            com.taobao.taolive.uikit.mtop.LiveInfoItem r5 = r12.liveInfoItem
            java.lang.String r5 = r5.liveUrl
            r0.put((java.lang.String) r4, (java.lang.Object) r5)
            java.lang.String r4 = "liveUrlHls"
            com.taobao.taolive.uikit.mtop.LiveInfoItem r5 = r12.liveInfoItem
            java.lang.String r5 = r5.liveUrlHls
            r0.put((java.lang.String) r4, (java.lang.Object) r5)
            com.taobao.taolive.uikit.mtop.LiveInfoItem r4 = r12.liveInfoItem
            java.util.List r4 = r4.liveUrlList
            if (r4 == 0) goto L_0x0073
            com.taobao.taolive.uikit.mtop.LiveInfoItem r4 = r12.liveInfoItem
            java.util.List r4 = r4.liveUrlList
            int r4 = r4.size()
            goto L_0x0074
        L_0x0073:
            r4 = 0
        L_0x0074:
            r5 = 0
        L_0x0075:
            if (r5 >= r4) goto L_0x00c4
            com.taobao.taolive.uikit.mtop.LiveInfoItem r6 = r12.liveInfoItem
            java.util.List r6 = r6.liveUrlList
            java.lang.Object r6 = r6.get(r5)
            com.taobao.taolive.uikit.mtop.QualitySelectItem r6 = (com.taobao.taolive.uikit.mtop.QualitySelectItem) r6
            com.alibaba.fastjson.JSONObject r7 = new com.alibaba.fastjson.JSONObject
            r7.<init>()
            java.lang.String r8 = "name"
            java.lang.String r9 = r6.name
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "flvUrl"
            java.lang.String r9 = r6.flvUrl
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "h265Url"
            java.lang.String r9 = r6.h265Url
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "artpUrl"
            java.lang.String r9 = r6.artpUrl
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "hlsUrl"
            java.lang.String r9 = r6.hlsUrl
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "auth_key"
            java.lang.String r9 = r6.auth_key
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "wholeH265FlvUrl"
            java.lang.String r9 = r6.wholeH265FlvUrl
            r7.put((java.lang.String) r8, (java.lang.Object) r9)
            java.lang.String r8 = "definition"
            java.lang.String r6 = r6.definition
            r7.put((java.lang.String) r8, (java.lang.Object) r6)
            r1.add(r7)
            int r5 = r5 + 1
            goto L_0x0075
        L_0x00c4:
            r9 = r0
            goto L_0x00c7
        L_0x00c6:
            r9 = r2
        L_0x00c7:
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            java.util.ArrayList r0 = r0.goodsList
            if (r0 == 0) goto L_0x00e2
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            java.util.ArrayList r0 = r0.goodsList
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x00e2
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            java.util.ArrayList r0 = r0.goodsList
            java.lang.Object r0 = r0.get(r3)
            r2 = r0
            com.taobao.taolive.uikit.mtop.GoodItem r2 = (com.taobao.taolive.uikit.mtop.GoodItem) r2
        L_0x00e2:
            r11 = r2
            android.content.Context r6 = r12.mContext
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            java.lang.String r7 = r0.nativeFeedDetailUrl
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            java.lang.String r8 = r0.coverImg
            com.taobao.taolive.uikit.mtop.LiveInfoItem r0 = r12.liveInfoItem
            boolean r10 = r0.landScape
            com.taobao.taolive.uikit.utils.ActionUtils.navWithExtras(r6, r7, r8, r9, r10, r11)
            return
        L_0x00f5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.taolive.weexext.weex.TBLiveBigcardViewComponent.clickEvent():void");
    }

    @WXComponentProp(name = "videoDO")
    public void setCardParams(String str) {
        if (!TextUtils.isEmpty(str) && this.mBigcardView != null) {
            try {
                this.liveInfoItem = (LiveInfoItem) JSON.parseObject(str, LiveInfoItem.class);
                this.mBigcardView.setLiveParams(this.liveInfoItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        if (this.mBigcardView != null) {
            this.mBigcardView.stopFavor();
        }
        HomepageVideoFrame.destroy();
        super.destroy();
    }
}
