package com.alibaba.android.prefetchx.core.data;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.android.prefetchx.PrefetchX;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import com.taobao.weaver.prefetch.PrefetchHandler;
import com.taobao.weaver.prefetch.PrefetchType;
import com.taobao.weaver.prefetch.WMLPrefetchDecision;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class SupportWindmill implements PrefetchHandler {
    public WMLPrefetchDecision isSupported(String str, Map<String, Object> map) {
        if (str == null || (!str.contains("wh_prefetch") && !str.contains("mtop_prefetch_enable") && !str.contains("data_prefetch") && !str.contains("wh_prefetch_id") && !PFMtop.getInstance().isUrlInMappingJSONFile(str))) {
            return new WMLPrefetchDecision();
        }
        WMLPrefetchDecision wMLPrefetchDecision = new WMLPrefetchDecision();
        wMLPrefetchDecision.status = PrefetchType.SUPPORTED;
        wMLPrefetchDecision.externalKey = PFMtop.getInstance().generatePrefetchString(PrefetchX.sContext, Uri.parse(str), (Map<String, Object>) null);
        if (!TextUtils.isEmpty(wMLPrefetchDecision.externalKey)) {
            try {
                wMLPrefetchDecision.externalKey = URLEncoder.encode(wMLPrefetchDecision.externalKey, "UTF-8");
            } catch (UnsupportedEncodingException unused) {
            }
        }
        return wMLPrefetchDecision;
    }

    public String prefetchData(String str, Map<String, Object> map, PrefetchDataCallback prefetchDataCallback) {
        return PFMtop.getInstance().prefetch(str, prefetchDataCallback);
    }
}
