package com.taobao.weex.module;

import android.content.Context;
import android.text.TextUtils;
import androidx.appcompat.taobao.TBActionBar;
import com.alibaba.aliweex.adapter.IFestivalModuleAdapter;
import com.taobao.android.festival.FestivalMgr;
import com.taobao.weex.bridge.JSCallback;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WXFestivalModule implements IFestivalModuleAdapter {
    public Map<String, String> queryFestivalStyle() {
        return FestivalMgr.getInstance().getFestivalStyle();
    }

    public void setFestivalStyle(Context context, String str, JSCallback jSCallback, JSCallback jSCallback2) {
        if (!TextUtils.isEmpty(str)) {
            String trim = str.toLowerCase(Locale.getDefault()).trim();
            char c = 65535;
            boolean z = false;
            switch (trim.hashCode()) {
                case 48:
                    if (trim.equals("0")) {
                        c = 0;
                        break;
                    }
                    break;
                case 49:
                    if (trim.equals("1")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    z = true;
                    break;
            }
            FestivalMgr.getInstance().setBgUI4Actionbar(context, TBActionBar.ActionBarStyle.NORMAL, z);
            jSCallback.invoke((Object) null);
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("result", "WX_FAILED");
        hashMap.put("message", "The type of festival is not recognized!");
        jSCallback2.invoke(hashMap);
    }
}
