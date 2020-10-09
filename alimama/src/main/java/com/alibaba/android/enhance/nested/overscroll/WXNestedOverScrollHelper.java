package com.alibaba.android.enhance.nested.overscroll;

import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import java.util.Map;

public final class WXNestedOverScrollHelper {
    private static final String KEY_NESTED_OVERSCROLL_ENABLED = "nested-overscroll_enabled";
    private static final int OVERSCROLL_PROPERTY_STATE_DISABLED = 2;
    private static final int OVERSCROLL_PROPERTY_STATE_ENABLED = 1;
    private static final int OVERSCROLL_PROPERTY_STATE_NOT_USED = 0;
    private WXSDKInstance instance;
    @OverScrollPropertyState
    private int overScrollPropertyState = 0;

    public WXNestedOverScrollHelper(WXSDKInstance wXSDKInstance) {
        this.instance = wXSDKInstance;
    }

    static void setOverScroll(WXSDKInstance wXSDKInstance, boolean z) {
        wXSDKInstance.getContainerInfo().put(KEY_NESTED_OVERSCROLL_ENABLED, Boolean.toString(z));
    }

    private static boolean isOverScroll(WXSDKInstance wXSDKInstance) {
        Map<String, String> containerInfo = wXSDKInstance.getContainerInfo();
        if (containerInfo == null) {
            return false;
        }
        String str = containerInfo.get(KEY_NESTED_OVERSCROLL_ENABLED);
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /* access modifiers changed from: package-private */
    public final boolean isOverScrollDisabled() {
        if (this.overScrollPropertyState == 0) {
            return !isOverScroll(this.instance);
        }
        if (this.overScrollPropertyState == 2) {
            return true;
        }
        return false;
    }

    public void setOverScrollProperty(boolean z) {
        this.overScrollPropertyState = z ? 1 : 2;
    }
}
