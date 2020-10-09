package com.taobao.android.dinamicx.bindingx;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class DXBindingXScrollHandler implements PlatformManager.IScrollFactory {
    Map<String, PlatformManager.ScrollListener> listenerMap = new HashMap();

    public PlatformManager.ScrollListener getListener(String str) {
        if (this.listenerMap == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return this.listenerMap.get(str);
    }

    public void setListener(String str, PlatformManager.ScrollListener scrollListener) {
        if (this.listenerMap == null) {
            this.listenerMap = new HashMap();
        }
        this.listenerMap.put(str, scrollListener);
    }

    public void removeListener(String str) {
        if (this.listenerMap != null && !TextUtils.isEmpty(str)) {
            this.listenerMap.remove(str);
        }
    }

    public void addScrollListenerWith(@NonNull String str, @NonNull PlatformManager.ScrollListener scrollListener) {
        setListener(str, scrollListener);
    }

    public void removeScrollListenerWith(@NonNull String str, @NonNull PlatformManager.ScrollListener scrollListener) {
        removeListener(str);
    }

    public void postScrollingMessage(String str, int i, int i2, JSONObject jSONObject) {
        PlatformManager.ScrollListener listener = getListener(str);
        if (listener != null) {
            listener.onScrolled((float) i, (float) i2);
        }
    }

    public void postScrollStartMessage(String str, int i, int i2, JSONObject jSONObject) {
        PlatformManager.ScrollListener listener = getListener(str);
        if (listener != null) {
            listener.onScrollStart();
        }
    }

    public void postScrollEndMessage(String str, int i, int i2, JSONObject jSONObject) {
        PlatformManager.ScrollListener listener = getListener(str);
        if (listener != null) {
            listener.onScrollEnd((float) i, (float) i2);
        }
    }
}
