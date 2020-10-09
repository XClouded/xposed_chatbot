package com.alibaba.aliweex.plugin;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.utils.WXPrefetchConstant;
import com.alibaba.aliweex.utils.WXPrefetchUtil;
import com.alibaba.android.prefetchx.core.file.WXFilePrefetchModule;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import com.taobao.weaver.prefetch.PrefetchHandler;
import com.taobao.weaver.prefetch.PrefetchType;
import com.taobao.weaver.prefetch.WMLPrefetchDecision;
import com.taobao.weex.WXSDKInstance;
import java.util.List;
import java.util.Map;

@Deprecated
public class WMMtopPrefetch implements PrefetchHandler {
    public WMLPrefetchDecision isSupported(String str, Map<String, Object> map) {
        if (str == null || (!str.contains("wh_prefetch") && !str.contains("mtop_prefetch_enable") && !str.contains("data_prefetch") && !str.contains(WXPrefetchConstant.KEY_MTOP_PREFETCH) && !str.contains("wh_prefetch_id") && !str.contains(WXPrefetchConstant.KEY_MTOP_PREFETCH_ID) && !isUrlInWhiteList(str))) {
            return new WMLPrefetchDecision();
        }
        WMLPrefetchDecision wMLPrefetchDecision = new WMLPrefetchDecision();
        wMLPrefetchDecision.status = PrefetchType.SUPPORTED;
        Map<String, String> generatePrefetchString = MtopPreloader.generatePrefetchString(str);
        if (generatePrefetchString != null) {
            wMLPrefetchDecision.externalKey = generatePrefetchString.get(WXFilePrefetchModule.PREFETCH_MODULE_NAME);
        }
        return wMLPrefetchDecision;
    }

    private boolean isUrlInWhiteList(String str) {
        List<String> allowWhiteUrlList = WXPrefetchUtil.getAllowWhiteUrlList();
        if (allowWhiteUrlList != null && allowWhiteUrlList.size() > 0) {
            for (String contains : allowWhiteUrlList) {
                if (str.contains(contains)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String prefetchData(String str, Map<String, Object> map, PrefetchDataCallback prefetchDataCallback) {
        return MtopPreloader.preload(str, new WXSDKInstance(AliWeex.getInstance().getApplication()), prefetchDataCallback);
    }
}
