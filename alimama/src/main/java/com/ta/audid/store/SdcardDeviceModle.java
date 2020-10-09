package com.ta.audid.store;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.Variables;
import com.ta.audid.collect.DeviceInfo2;
import com.ta.audid.upload.UtdidKeyFile;
import com.ta.audid.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

@Deprecated
public class SdcardDeviceModle {
    private static final String MODULE_GSID = "gsid";
    private static final String MODULE_IMEI = "imei";
    private static final String MODULE_IMSI = "imsi";
    private static Map<String, String> mSdcardDeviceModle;

    public static String getModuleImei() {
        try {
            return getModule("imei");
        } catch (Exception unused) {
            return "";
        }
    }

    public static String getModuleImsi() {
        try {
            return getModule("imsi");
        } catch (Exception unused) {
            return "";
        }
    }

    public static synchronized void writeSdcardDeviceModle(String str, String str2) {
        synchronized (SdcardDeviceModle.class) {
            Context context = Variables.getInstance().getContext();
            if (context != null) {
                try {
                    if (checkSdcardDeviceModle()) {
                        if (!TextUtils.isEmpty(str)) {
                            mSdcardDeviceModle.put("imei", str);
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            mSdcardDeviceModle.put("imsi", str2);
                        }
                    } else {
                        mSdcardDeviceModle = new HashMap();
                        if (!TextUtils.isEmpty(str)) {
                            mSdcardDeviceModle.put("imei", str);
                        }
                        if (!TextUtils.isEmpty(str2)) {
                            mSdcardDeviceModle.put("imsi", str2);
                        }
                        String androidID = DeviceInfo2.getAndroidID(context);
                        if (!TextUtils.isEmpty(androidID)) {
                            mSdcardDeviceModle.put(MODULE_GSID, androidID);
                        }
                        UtdidKeyFile.writeSdcardDeviceModleFile(UtdidContentUtil.getEncodedContent(new JSONObject(mSdcardDeviceModle).toString()));
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    private static synchronized String getModule(String str) {
        synchronized (SdcardDeviceModle.class) {
            if (Variables.getInstance().getContext() == null) {
                return "";
            }
            if (mSdcardDeviceModle != null) {
                String str2 = mSdcardDeviceModle.get(str);
                return str2;
            } else if (checkSdcardDeviceModle()) {
                String str3 = mSdcardDeviceModle.get(str);
                return str3;
            } else {
                clearSdcardDeviceModle();
                return "";
            }
        }
    }

    private static void clearSdcardDeviceModle() {
        try {
            mSdcardDeviceModle.clear();
            mSdcardDeviceModle = null;
            UtdidKeyFile.writeSdcardDeviceModleFile("");
        } catch (Exception unused) {
        }
    }

    private static boolean checkSdcardDeviceModle() {
        if (Variables.getInstance().getContext() == null) {
            return false;
        }
        try {
            mSdcardDeviceModle = JsonUtils.jsonToMap(UtdidContentUtil.getDecodedContent(UtdidKeyFile.readSdcardDeviceModleFile()));
            if (DeviceInfo2.getAndroidID(Variables.getInstance().getContext()).equals(mSdcardDeviceModle.get(MODULE_GSID))) {
                return true;
            }
            clearSdcardDeviceModle();
            return false;
        } catch (Exception unused) {
            clearSdcardDeviceModle();
            return false;
        }
    }
}
