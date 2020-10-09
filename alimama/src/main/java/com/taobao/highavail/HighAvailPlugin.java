package com.taobao.highavail;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class HighAvailPlugin implements MethodChannel.MethodCallHandler {
    public static long interactionStartTime = -1;
    public static MethodChannel methodChannel;

    public static void registerWith(PluginRegistry.Registrar registrar) {
        HighAvailPlugin highAvailPlugin = new HighAvailPlugin();
        methodChannel = new MethodChannel(registrar.messenger(), "high_avail");
        methodChannel.setMethodCallHandler(highAvailPlugin);
    }

    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        if (methodCall.method.equals("getStartTime")) {
            result.success(String.valueOf(interactionStartTime));
            interactionStartTime = -1;
            return;
        }
        result.notImplemented();
    }

    public static void setPageStartTime(long j) {
        interactionStartTime = j;
    }
}
