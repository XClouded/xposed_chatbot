package com.alimama.union.app.share.flutter;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class UTPlugin implements MethodChannel.MethodCallHandler {
    private static final String CHANNEL_NAME = "com.alimama.moon/UT";
    private static final String TAG = "UTPlugin";

    public static void register(BinaryMessenger binaryMessenger) {
        new MethodChannel(binaryMessenger, CHANNEL_NAME).setMethodCallHandler(new UTPlugin());
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMethodCall(io.flutter.plugin.common.MethodCall r8, io.flutter.plugin.common.MethodChannel.Result r9) {
        /*
            r7 = this;
            java.lang.String r9 = r8.method
            int r0 = r9.hashCode()
            r1 = 2
            r2 = 0
            r3 = 1
            switch(r0) {
                case -2144194874: goto L_0x0035;
                case -1636813445: goto L_0x002b;
                case 101609: goto L_0x0021;
                case 606174672: goto L_0x0017;
                case 1931371041: goto L_0x000d;
                default: goto L_0x000c;
            }
        L_0x000c:
            goto L_0x003f
        L_0x000d:
            java.lang.String r0 = "reportTime"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L_0x003f
            r9 = 3
            goto L_0x0040
        L_0x0017:
            java.lang.String r0 = "customUT"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L_0x003f
            r9 = 1
            goto L_0x0040
        L_0x0021:
            java.lang.String r0 = "fps"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L_0x003f
            r9 = 4
            goto L_0x0040
        L_0x002b:
            java.lang.String r0 = "reportException"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L_0x003f
            r9 = 2
            goto L_0x0040
        L_0x0035:
            java.lang.String r0 = "ctrClicked"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L_0x003f
            r9 = 0
            goto L_0x0040
        L_0x003f:
            r9 = -1
        L_0x0040:
            switch(r9) {
                case 0: goto L_0x00be;
                case 1: goto L_0x00aa;
                case 2: goto L_0x0076;
                case 3: goto L_0x005a;
                case 4: goto L_0x0045;
                default: goto L_0x0043;
            }
        L_0x0043:
            goto L_0x00d1
        L_0x0045:
            java.lang.String r9 = "fps"
            java.lang.Object r9 = r8.argument(r9)
            java.lang.Integer r9 = (java.lang.Integer) r9
            java.lang.String r0 = "pageId"
            java.lang.Object r8 = r8.argument(r0)
            java.lang.String r8 = (java.lang.String) r8
            com.alimama.moon.emas.FlutterApmReporter.fps(r9, r8)
            goto L_0x00d1
        L_0x005a:
            java.lang.String r9 = "firstFrameTime"
            java.lang.Object r9 = r8.argument(r9)
            java.lang.Integer r9 = (java.lang.Integer) r9
            java.lang.String r0 = "interactiveTime"
            java.lang.Object r0 = r8.argument(r0)
            java.lang.Integer r0 = (java.lang.Integer) r0
            java.lang.String r1 = "pageId"
            java.lang.Object r8 = r8.argument(r1)
            java.lang.String r8 = (java.lang.String) r8
            com.alimama.moon.emas.FlutterApmReporter.reportTime(r9, r0, r8)
            goto L_0x00d1
        L_0x0076:
            java.lang.String r9 = "errorType"
            java.lang.Object r9 = r8.argument(r9)
            java.lang.String r9 = (java.lang.String) r9
            java.lang.String r0 = "errorInfo"
            java.lang.Object r0 = r8.argument(r0)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r4 = "stackInfo"
            java.lang.Object r8 = r8.argument(r4)
            java.lang.String r8 = (java.lang.String) r8
            com.alimama.moon.App r4 = com.alimama.moon.App.sApplication
            java.lang.String r5 = "Flutter error: %s"
            java.lang.Object[] r6 = new java.lang.Object[r3]
            r6[r2] = r0
            java.lang.String r5 = java.lang.String.format(r5, r6)
            java.lang.String r6 = "Flutter error: %s \n stacktrace: %s"
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r1[r2] = r0
            r1[r3] = r8
            java.lang.String r8 = java.lang.String.format(r6, r1)
            com.alimama.moon.emas.crashreporter.FlutterExceptionReporter.sendError(r4, r9, r5, r8)
            goto L_0x00d1
        L_0x00aa:
            java.lang.String r9 = "pageName"
            java.lang.Object r9 = r8.argument(r9)
            java.lang.String r9 = (java.lang.String) r9
            java.lang.String r0 = "eventName"
            java.lang.Object r8 = r8.argument(r0)
            java.lang.String r8 = (java.lang.String) r8
            com.alimama.moon.usertrack.UTHelper.sendCustomUT(r9, r8)
            goto L_0x00d1
        L_0x00be:
            java.lang.String r9 = "ctrlName"
            java.lang.Object r9 = r8.argument(r9)
            java.lang.String r9 = (java.lang.String) r9
            java.lang.String r0 = "pageName"
            java.lang.Object r8 = r8.argument(r0)
            java.lang.String r8 = (java.lang.String) r8
            com.alimama.moon.usertrack.UTHelper.sendControlHit(r8, r9)
        L_0x00d1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.share.flutter.UTPlugin.onMethodCall(io.flutter.plugin.common.MethodCall, io.flutter.plugin.common.MethodChannel$Result):void");
    }
}
