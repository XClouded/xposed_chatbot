package com.alimama.union.app.infrastructure.weex;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;

public class WeexEventModuleAdapter implements IEventModuleAdapter {
    public void openURL(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            WeexUtils.startTargetActivity(context, str);
        }
    }
}
