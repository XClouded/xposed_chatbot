package com.taobao.weex.module.monitor;

import com.taobao.android.monitor.adaptor.FeedbackManager;
import com.taobao.android.utils.Debuggable;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXModuleAnno;
import java.util.Map;

public class AliAPMAdaptorModule extends WXModule {
    @WXModuleAnno
    public void reportFeedbackFullstrace(Map<String, Object> map) {
        if (Debuggable.isDebug()) {
            TBToast.makeText(this.mWXSDKInstance.getContext(), "AliAPMAdaptorModule.reportFeedbackFullstrace", 0).show();
        }
        FeedbackManager.reportFullstrace(this.mWXSDKInstance.getContext(), map);
    }
}
