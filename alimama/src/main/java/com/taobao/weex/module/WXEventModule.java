package com.taobao.weex.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.aliweex.adapter.IEventModuleAdapter;
import com.taobao.android.nav.Nav;
import com.taobao.tao.Globals;

public class WXEventModule implements IEventModuleAdapter {
    public void openURL(Context context, String str) {
        if (!TextUtils.isEmpty(str) && !Nav.from(Globals.getApplication()).toUri(str.trim())) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            context.startActivity(intent);
        }
    }
}
