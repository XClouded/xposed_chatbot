package android.taobao.windvane.util;

import android.webkit.ValueCallback;

import java.util.concurrent.ConcurrentHashMap;

public class WVNativeCallbackUtil {
    public static final String SEPERATER = "/";
    private static ConcurrentHashMap<String, ValueCallback<String>> mNativeCallbackIdMap = new ConcurrentHashMap<>();

    public static void putNativeCallbak(String str, ValueCallback<String> valueCallback) {
        mNativeCallbackIdMap.put(str, valueCallback);
    }

    public static ValueCallback<String> getNativeCallback(String str) {
        return mNativeCallbackIdMap.get(str);
    }

    public static void clearNativeCallback(String str) {
        mNativeCallbackIdMap.remove(str);
    }

    public static void clearAllNativeCallback() {
        mNativeCallbackIdMap.clear();
    }
}
