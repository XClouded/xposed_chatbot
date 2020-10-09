package com.taobao.taolive.weexext.weex;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.taobao.taolive.weexext.bubble.TaoliveBubbleComponent;
import com.taobao.taolive.weexext.cardsuite.TBLiveBigCardWeexComponent;
import com.taobao.taolive.weexext.cardsuite.TBLiveMiddleCardWeexComponent;
import com.taobao.taolive.weexext.cardsuite.TBLiveSmallCardWeexComponent;
import com.taobao.taolive.weexext.drawboard.TBLiveDrawBoardWeexComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.component.WXComponent;

public class TaoliveUIKitWeexService extends Service implements IExternalComponentGetter {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Class<? extends WXComponent> getExternalComponentClass(String str, WXSDKInstance wXSDKInstance) {
        if ("livebigcard".equals(str)) {
            return TBLiveBigcardViewComponent.class;
        }
        if ("liveshowbubble".equals(str)) {
            return TaoliveBubbleComponent.class;
        }
        if ("alilivebigcard".equals(str)) {
            return TBLiveBigCardWeexComponent.class;
        }
        if ("alilivesmallcard".equals(str)) {
            return TBLiveSmallCardWeexComponent.class;
        }
        if ("alilivemiddlecard".equals(str)) {
            return TBLiveMiddleCardWeexComponent.class;
        }
        if (TBLiveDrawBoardWeexComponent.NAME.equals(str)) {
            return TBLiveDrawBoardWeexComponent.class;
        }
        return null;
    }
}
