package com.alibaba.aliweex;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.alibaba.aliweex.bundle.WXNavBarAdapter;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.component.WXEmbed;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AliWXSDKInstance extends WXSDKInstance implements WXEmbed.EmbedManager {
    private Map<String, Object> extraParams;
    private Map<String, WXEmbed> mEmbedMap = new HashMap();
    protected String mFtag;
    private WXNavBarAdapter mNavBarAdapter;

    public AliWXSDKInstance(String str) {
    }

    public AliWXSDKInstance(Context context, String str) {
        super(context);
        this.mFtag = str;
    }

    public void setFragmentTag(String str) {
        this.mFtag = str;
    }

    public String getFragmentTag() {
        return this.mFtag;
    }

    public void setWXNavBarAdapter(WXNavBarAdapter wXNavBarAdapter) {
        this.mNavBarAdapter = wXNavBarAdapter;
    }

    public WXNavBarAdapter getWXNavBarAdapter() {
        return this.mNavBarAdapter;
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        this.mNavBarAdapter = null;
    }

    /* access modifiers changed from: protected */
    public WXSDKInstance newNestedInstance() {
        AliWXSDKInstance aliWXSDKInstance = new AliWXSDKInstance(getContext(), this.mFtag);
        aliWXSDKInstance.setWXNavBarAdapter(this.mNavBarAdapter);
        return aliWXSDKInstance;
    }

    public WXEmbed getEmbed(String str) {
        return this.mEmbedMap.get(str);
    }

    public void putEmbed(String str, WXEmbed wXEmbed) {
        this.mEmbedMap.put(str, wXEmbed);
    }

    @Keep
    public void putExtra(String str, Object obj) {
        if (!TextUtils.isEmpty(str) && obj != null) {
            if (this.extraParams == null) {
                this.extraParams = new ConcurrentHashMap();
            }
            this.extraParams.put(str, obj);
        }
    }

    @Keep
    public Object getExtra(String str, Object obj) {
        if (TextUtils.isEmpty(str) || obj == null || this.extraParams == null) {
            return null;
        }
        return this.extraParams.get(str);
    }
}
