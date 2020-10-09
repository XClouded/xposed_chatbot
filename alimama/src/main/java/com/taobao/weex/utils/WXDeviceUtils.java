package com.taobao.weex.utils;

import android.content.Context;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXFoldDeviceAdapter;

public class WXDeviceUtils {
    public static boolean isAutoResize(Context context) {
        IWXFoldDeviceAdapter wXFoldDeviceAdapter = WXSDKManager.getInstance().getWXFoldDeviceAdapter();
        if (wXFoldDeviceAdapter == null) {
            return false;
        }
        if (wXFoldDeviceAdapter.isFoldDevice() || wXFoldDeviceAdapter.isMateX() || wXFoldDeviceAdapter.isGalaxyFold()) {
            return true;
        }
        return false;
    }
}
