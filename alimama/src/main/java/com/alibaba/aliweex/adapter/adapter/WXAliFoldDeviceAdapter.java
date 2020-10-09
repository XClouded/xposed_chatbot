package com.alibaba.aliweex.adapter.adapter;

import com.alibaba.aliweex.AliWeex;
import com.taobao.android.autosize.TBDeviceUtils;
import com.taobao.weex.adapter.IWXFoldDeviceAdapter;

public class WXAliFoldDeviceAdapter implements IWXFoldDeviceAdapter {
    private static WXAliFoldDeviceAdapter instance;

    private WXAliFoldDeviceAdapter() {
    }

    public static WXAliFoldDeviceAdapter getFoldDeviceAdapter() {
        try {
            Class.forName(TBDeviceUtils.class.getName());
            if (instance == null) {
                synchronized (WXAliFoldDeviceAdapter.class) {
                    if (instance == null) {
                        instance = new WXAliFoldDeviceAdapter();
                    }
                }
            }
            return instance;
        } catch (Throwable unused) {
            return null;
        }
    }

    public boolean isFoldDevice() {
        return TBDeviceUtils.isFoldDevice(AliWeex.getInstance().getContext());
    }

    public boolean isMateX() {
        return TBDeviceUtils.isMateX(AliWeex.getInstance().getContext());
    }

    public boolean isGalaxyFold() {
        return TBDeviceUtils.isGalaxyFold(AliWeex.getInstance().getContext());
    }
}
