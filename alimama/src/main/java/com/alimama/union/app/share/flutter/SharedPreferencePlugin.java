package com.alimama.union.app.share.flutter;

import android.content.SharedPreferences;
import android.util.Log;
import com.alimama.moon.App;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class SharedPreferencePlugin implements MethodChannel.MethodCallHandler {
    private static final String TAG = "SharedPreferencePlugin";
    private final SharedPreferences sharedPreferences = App.sApplication.getSharedPreferences("flutter", 0);

    private SharedPreferencePlugin() {
    }

    public static void register(BinaryMessenger binaryMessenger) {
        new MethodChannel(binaryMessenger, "com.alimama.moon/shared_preference").setMethodCallHandler(new SharedPreferencePlugin());
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMethodCall(io.flutter.plugin.common.MethodCall r4, io.flutter.plugin.common.MethodChannel.Result r5) {
        /*
            r3 = this;
            java.lang.String r0 = r4.method
            int r1 = r0.hashCode()
            r2 = -75652256(0xfffffffffb7da360, float:-1.31696474E36)
            if (r1 == r2) goto L_0x001b
            r2 = 1984457324(0x76486a6c, float:1.0162284E33)
            if (r1 == r2) goto L_0x0011
            goto L_0x0025
        L_0x0011:
            java.lang.String r1 = "setBool"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0025
            r0 = 1
            goto L_0x0026
        L_0x001b:
            java.lang.String r1 = "getBool"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0025
            r0 = 0
            goto L_0x0026
        L_0x0025:
            r0 = -1
        L_0x0026:
            switch(r0) {
                case 0: goto L_0x003e;
                case 1: goto L_0x002a;
                default: goto L_0x0029;
            }
        L_0x0029:
            goto L_0x0051
        L_0x002a:
            java.lang.String r5 = "key"
            java.lang.Object r5 = r4.argument(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r0 = "val"
            java.lang.Object r4 = r4.argument(r0)
            java.lang.Boolean r4 = (java.lang.Boolean) r4
            r3.setBool(r5, r4)
            goto L_0x0051
        L_0x003e:
            java.lang.String r0 = "key"
            java.lang.Object r0 = r4.argument(r0)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r1 = "defaultVal"
            java.lang.Object r4 = r4.argument(r1)
            java.lang.Boolean r4 = (java.lang.Boolean) r4
            r3.getBool(r0, r4, r5)
        L_0x0051:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.share.flutter.SharedPreferencePlugin.onMethodCall(io.flutter.plugin.common.MethodCall, io.flutter.plugin.common.MethodChannel$Result):void");
    }

    private void getBool(String str, Boolean bool, MethodChannel.Result result) {
        result.success(Boolean.valueOf(this.sharedPreferences.getBoolean(str, bool != null ? bool.booleanValue() : false)));
    }

    private void setBool(String str, Boolean bool) {
        if (bool == null) {
            Log.e(TAG, "setBool missing val");
        } else {
            this.sharedPreferences.edit().putBoolean(str, bool.booleanValue()).apply();
        }
    }
}
