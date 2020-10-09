package com.taobao.weex.adapter;

import android.os.Build;
import android.text.TextUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;

public class DefaultFoldDeviceAdapter implements IWXFoldDeviceAdapter {
    private static final String HUAWEI_BRAND = "HUAWEI";
    private static final List<String> HUAWEI_FOLD_DEVICES = new ArrayList<String>() {
        {
            add("unknownRLI");
            add("HWTAH");
            add("MRX-AL09");
            add("HWMRX");
            add("TAH-AN00m");
            add("HWTAH-C");
            add("RHA-AN00m");
            add("unknownRHA");
        }
    };
    private static final String SAMSUNG_BRAND = "samsung";
    private static final String TAG = "TBDeviceUtils";

    public boolean isFoldDevice() {
        if (SAMSUNG_BRAND.equalsIgnoreCase(Build.BRAND) && TextUtils.equals("SM-F9000", Build.MODEL)) {
            return true;
        }
        if (!HUAWEI_BRAND.equalsIgnoreCase(Build.BRAND) || !HUAWEI_FOLD_DEVICES.contains(Build.DEVICE)) {
            return false;
        }
        WXLogUtils.e(TAG, "is HUAWEI Fold Device");
        return true;
    }

    public boolean isMateX() {
        if (!HUAWEI_BRAND.equalsIgnoreCase(Build.BRAND) || !HUAWEI_FOLD_DEVICES.contains(Build.DEVICE)) {
            return false;
        }
        WXLogUtils.e(TAG, "is HUAWEI Fold Device");
        return true;
    }

    public boolean isGalaxyFold() {
        return SAMSUNG_BRAND.equalsIgnoreCase(Build.BRAND) && "SM-F9000".equalsIgnoreCase(Build.MODEL);
    }
}
