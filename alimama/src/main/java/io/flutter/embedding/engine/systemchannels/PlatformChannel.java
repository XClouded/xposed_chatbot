package io.flutter.embedding.engine.systemchannels;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlatformChannel {
    private static final String TAG = "PlatformChannel";
    @NonNull
    public final MethodChannel channel;
    @VisibleForTesting
    @NonNull
    protected final MethodChannel.MethodCallHandler parsingMethodCallHandler = new MethodChannel.MethodCallHandler() {
        /* JADX WARNING: Removed duplicated region for block: B:58:0x00fa A[Catch:{ JSONException -> 0x022f }] */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x0109 A[Catch:{ JSONException -> 0x022f }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onMethodCall(@androidx.annotation.NonNull io.flutter.plugin.common.MethodCall r5, @androidx.annotation.NonNull io.flutter.plugin.common.MethodChannel.Result r6) {
            /*
                r4 = this;
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler
                if (r0 != 0) goto L_0x0009
                return
            L_0x0009:
                java.lang.String r0 = r5.method
                java.lang.Object r5 = r5.arguments
                java.lang.String r1 = "PlatformChannel"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Received '"
                r2.append(r3)
                r2.append(r0)
                java.lang.String r3 = "' message."
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                io.flutter.Log.v(r1, r2)
                r1 = -1
                r2 = 0
                int r3 = r0.hashCode()     // Catch:{ JSONException -> 0x022f }
                switch(r3) {
                    case -766342101: goto L_0x00a7;
                    case -720677196: goto L_0x009c;
                    case -548468504: goto L_0x0092;
                    case -247230243: goto L_0x0088;
                    case -215273374: goto L_0x007e;
                    case 96412730: goto L_0x0073;
                    case 232206254: goto L_0x0068;
                    case 241845679: goto L_0x005e;
                    case 1390477857: goto L_0x0054;
                    case 1514180520: goto L_0x0049;
                    case 1674312266: goto L_0x003e;
                    case 2119655719: goto L_0x0033;
                    default: goto L_0x0031;
                }     // Catch:{ JSONException -> 0x022f }
            L_0x0031:
                goto L_0x00b0
            L_0x0033:
                java.lang.String r3 = "SystemChrome.setPreferredOrientations"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 2
                goto L_0x00b0
            L_0x003e:
                java.lang.String r3 = "SystemChrome.setEnabledSystemUIOverlays"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 4
                goto L_0x00b0
            L_0x0049:
                java.lang.String r3 = "Clipboard.getData"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 10
                goto L_0x00b0
            L_0x0054:
                java.lang.String r3 = "SystemChrome.setSystemUIOverlayStyle"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 6
                goto L_0x00b0
            L_0x005e:
                java.lang.String r3 = "SystemChrome.restoreSystemUIOverlays"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 5
                goto L_0x00b0
            L_0x0068:
                java.lang.String r3 = "SystemGestures.setSystemGestureExclusionRects"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 9
                goto L_0x00b0
            L_0x0073:
                java.lang.String r3 = "SystemGestures.getSystemGestureExclusionRects"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 8
                goto L_0x00b0
            L_0x007e:
                java.lang.String r3 = "SystemSound.play"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 0
                goto L_0x00b0
            L_0x0088:
                java.lang.String r3 = "HapticFeedback.vibrate"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 1
                goto L_0x00b0
            L_0x0092:
                java.lang.String r3 = "SystemChrome.setApplicationSwitcherDescription"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 3
                goto L_0x00b0
            L_0x009c:
                java.lang.String r3 = "Clipboard.setData"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 11
                goto L_0x00b0
            L_0x00a7:
                java.lang.String r3 = "SystemNavigator.pop"
                boolean r0 = r0.equals(r3)     // Catch:{ JSONException -> 0x022f }
                if (r0 == 0) goto L_0x00b0
                r1 = 7
            L_0x00b0:
                switch(r1) {
                    case 0: goto L_0x0211;
                    case 1: goto L_0x01f3;
                    case 2: goto L_0x01d3;
                    case 3: goto L_0x01b1;
                    case 4: goto L_0x018f;
                    case 5: goto L_0x0181;
                    case 6: goto L_0x015f;
                    case 7: goto L_0x0151;
                    case 8: goto L_0x0131;
                    case 9: goto L_0x010e;
                    case 10: goto L_0x00ce;
                    case 11: goto L_0x00b8;
                    default: goto L_0x00b3;
                }     // Catch:{ JSONException -> 0x022f }
            L_0x00b3:
                r6.notImplemented()     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x00b8:
                org.json.JSONObject r5 = (org.json.JSONObject) r5     // Catch:{ JSONException -> 0x022f }
                java.lang.String r0 = "text"
                java.lang.String r5 = r5.getString(r0)     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ JSONException -> 0x022f }
                r0.setClipboardData(r5)     // Catch:{ JSONException -> 0x022f }
                r6.success(r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x00ce:
                java.lang.String r5 = (java.lang.String) r5     // Catch:{ JSONException -> 0x022f }
                if (r5 == 0) goto L_0x00ed
                io.flutter.embedding.engine.systemchannels.PlatformChannel$ClipboardContentFormat r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.ClipboardContentFormat.fromValue(r5)     // Catch:{ NoSuchFieldException -> 0x00d7 }
                goto L_0x00ee
            L_0x00d7:
                java.lang.String r0 = "error"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x022f }
                r1.<init>()     // Catch:{ JSONException -> 0x022f }
                java.lang.String r3 = "No such clipboard content format: "
                r1.append(r3)     // Catch:{ JSONException -> 0x022f }
                r1.append(r5)     // Catch:{ JSONException -> 0x022f }
                java.lang.String r5 = r1.toString()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
            L_0x00ed:
                r0 = r2
            L_0x00ee:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r5 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r5 = r5.platformMessageHandler     // Catch:{ JSONException -> 0x022f }
                java.lang.CharSequence r5 = r5.getClipboardData(r0)     // Catch:{ JSONException -> 0x022f }
                if (r5 == 0) goto L_0x0109
                org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x022f }
                r0.<init>()     // Catch:{ JSONException -> 0x022f }
                java.lang.String r1 = "text"
                r0.put(r1, r5)     // Catch:{ JSONException -> 0x022f }
                r6.success(r0)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x0109:
                r6.success(r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x010e:
                boolean r0 = r5 instanceof org.json.JSONArray     // Catch:{ JSONException -> 0x022f }
                if (r0 != 0) goto L_0x011b
                java.lang.String r5 = "Input type is incorrect. Ensure that a List<Map<String, int>> is passed as the input for SystemGestureExclusionRects.setSystemGestureExclusionRects."
                java.lang.String r0 = "inputTypeError"
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x011b:
                org.json.JSONArray r5 = (org.json.JSONArray) r5     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                java.util.ArrayList r5 = r0.decodeExclusionRects(r5)     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ JSONException -> 0x022f }
                r0.setSystemGestureExclusionRects(r5)     // Catch:{ JSONException -> 0x022f }
                r6.success(r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x0131:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r5 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r5 = r5.platformMessageHandler     // Catch:{ JSONException -> 0x022f }
                java.util.List r5 = r5.getSystemGestureExclusionRects()     // Catch:{ JSONException -> 0x022f }
                if (r5 != 0) goto L_0x0146
                java.lang.String r5 = "Exclusion rects only exist for Android API 29+."
                java.lang.String r0 = "error"
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x0146:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                java.util.ArrayList r5 = r0.encodeExclusionRects(r5)     // Catch:{ JSONException -> 0x022f }
                r6.success(r5)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x0151:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r5 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r5 = r5.platformMessageHandler     // Catch:{ JSONException -> 0x022f }
                r5.popSystemNavigator()     // Catch:{ JSONException -> 0x022f }
                r6.success(r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x015f:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                org.json.JSONObject r5 = (org.json.JSONObject) r5     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$SystemChromeStyle r5 = r0.decodeSystemChromeStyle(r5)     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                r0.setSystemUiOverlayStyle(r5)     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                r6.success(r2)     // Catch:{ NoSuchFieldException | JSONException -> 0x0175 }
                goto L_0x024a
            L_0x0175:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.String r5 = r5.getMessage()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x0181:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r5 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x022f }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r5 = r5.platformMessageHandler     // Catch:{ JSONException -> 0x022f }
                r5.restoreSystemUiOverlays()     // Catch:{ JSONException -> 0x022f }
                r6.success(r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x018f:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                org.json.JSONArray r5 = (org.json.JSONArray) r5     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                java.util.List r5 = r0.decodeSystemUiOverlays(r5)     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                r0.showSystemOverlays(r5)     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                r6.success(r2)     // Catch:{ NoSuchFieldException | JSONException -> 0x01a5 }
                goto L_0x024a
            L_0x01a5:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.String r5 = r5.getMessage()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x01b1:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x01c7 }
                org.json.JSONObject r5 = (org.json.JSONObject) r5     // Catch:{ JSONException -> 0x01c7 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$AppSwitcherDescription r5 = r0.decodeAppSwitcherDescription(r5)     // Catch:{ JSONException -> 0x01c7 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ JSONException -> 0x01c7 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ JSONException -> 0x01c7 }
                r0.setApplicationSwitcherDescription(r5)     // Catch:{ JSONException -> 0x01c7 }
                r6.success(r2)     // Catch:{ JSONException -> 0x01c7 }
                goto L_0x024a
            L_0x01c7:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.String r5 = r5.getMessage()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x01d3:
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                org.json.JSONArray r5 = (org.json.JSONArray) r5     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                int r5 = r0.decodeOrientations(r5)     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                r0.setPreferredOrientations(r5)     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                r6.success(r2)     // Catch:{ NoSuchFieldException | JSONException -> 0x01e8 }
                goto L_0x024a
            L_0x01e8:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.String r5 = r5.getMessage()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x01f3:
                java.lang.String r5 = (java.lang.String) r5     // Catch:{ NoSuchFieldException -> 0x0206 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$HapticFeedbackType r5 = io.flutter.embedding.engine.systemchannels.PlatformChannel.HapticFeedbackType.fromValue(r5)     // Catch:{ NoSuchFieldException -> 0x0206 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException -> 0x0206 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ NoSuchFieldException -> 0x0206 }
                r0.vibrateHapticFeedback(r5)     // Catch:{ NoSuchFieldException -> 0x0206 }
                r6.success(r2)     // Catch:{ NoSuchFieldException -> 0x0206 }
                goto L_0x024a
            L_0x0206:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.String r5 = r5.getMessage()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x0211:
                java.lang.String r5 = (java.lang.String) r5     // Catch:{ NoSuchFieldException -> 0x0224 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$SoundType r5 = io.flutter.embedding.engine.systemchannels.PlatformChannel.SoundType.fromValue(r5)     // Catch:{ NoSuchFieldException -> 0x0224 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel r0 = io.flutter.embedding.engine.systemchannels.PlatformChannel.this     // Catch:{ NoSuchFieldException -> 0x0224 }
                io.flutter.embedding.engine.systemchannels.PlatformChannel$PlatformMessageHandler r0 = r0.platformMessageHandler     // Catch:{ NoSuchFieldException -> 0x0224 }
                r0.playSystemSound(r5)     // Catch:{ NoSuchFieldException -> 0x0224 }
                r6.success(r2)     // Catch:{ NoSuchFieldException -> 0x0224 }
                goto L_0x024a
            L_0x0224:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.String r5 = r5.getMessage()     // Catch:{ JSONException -> 0x022f }
                r6.error(r0, r5, r2)     // Catch:{ JSONException -> 0x022f }
                goto L_0x024a
            L_0x022f:
                r5 = move-exception
                java.lang.String r0 = "error"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r3 = "JSON error: "
                r1.append(r3)
                java.lang.String r5 = r5.getMessage()
                r1.append(r5)
                java.lang.String r5 = r1.toString()
                r6.error(r0, r5, r2)
            L_0x024a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.flutter.embedding.engine.systemchannels.PlatformChannel.AnonymousClass1.onMethodCall(io.flutter.plugin.common.MethodCall, io.flutter.plugin.common.MethodChannel$Result):void");
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public PlatformMessageHandler platformMessageHandler;

    public interface PlatformMessageHandler {
        @Nullable
        CharSequence getClipboardData(@Nullable ClipboardContentFormat clipboardContentFormat);

        List<Rect> getSystemGestureExclusionRects();

        void playSystemSound(@NonNull SoundType soundType);

        void popSystemNavigator();

        void restoreSystemUiOverlays();

        void setApplicationSwitcherDescription(@NonNull AppSwitcherDescription appSwitcherDescription);

        void setClipboardData(@NonNull String str);

        void setPreferredOrientations(int i);

        void setSystemGestureExclusionRects(@NonNull ArrayList<Rect> arrayList);

        void setSystemUiOverlayStyle(@NonNull SystemChromeStyle systemChromeStyle);

        void showSystemOverlays(@NonNull List<SystemUiOverlay> list);

        void vibrateHapticFeedback(@NonNull HapticFeedbackType hapticFeedbackType);
    }

    public PlatformChannel(@NonNull DartExecutor dartExecutor) {
        this.channel = new MethodChannel(dartExecutor, "flutter/platform", JSONMethodCodec.INSTANCE);
        this.channel.setMethodCallHandler(this.parsingMethodCallHandler);
    }

    public void setPlatformMessageHandler(@Nullable PlatformMessageHandler platformMessageHandler2) {
        this.platformMessageHandler = platformMessageHandler2;
    }

    /* access modifiers changed from: private */
    public int decodeOrientations(@NonNull JSONArray jSONArray) throws JSONException, NoSuchFieldException {
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < jSONArray.length(); i++) {
            switch (DeviceOrientation.fromValue(jSONArray.getString(i))) {
                case PORTRAIT_UP:
                    z |= true;
                    break;
                case PORTRAIT_DOWN:
                    z |= true;
                    break;
                case LANDSCAPE_LEFT:
                    z |= true;
                    break;
                case LANDSCAPE_RIGHT:
                    z |= true;
                    break;
            }
            if (!z2) {
                z2 = z;
            }
        }
        switch (z) {
            case false:
                return -1;
            case true:
                return 1;
            case true:
                return 0;
            case true:
            case true:
            case true:
            case true:
            case true:
            case true:
            case true:
                if (z2) {
                    return 9;
                }
                if (z2) {
                    return 8;
                }
                switch (z2) {
                    case true:
                        return 1;
                    case true:
                        return 0;
                }
            case true:
                return 9;
            case true:
                return 12;
            case true:
                return 8;
            case true:
                return 11;
            case true:
                return 2;
            case true:
                return 13;
        }
        return 1;
    }

    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<Rect> decodeExclusionRects(@NonNull JSONArray jSONArray) throws JSONException {
        ArrayList<Rect> arrayList = new ArrayList<>();
        int i = 0;
        while (i < jSONArray.length()) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            try {
                arrayList.add(new Rect(jSONObject.getInt("left"), jSONObject.getInt("top"), jSONObject.getInt("right"), jSONObject.getInt("bottom")));
                i++;
            } catch (JSONException unused) {
                throw new JSONException("Incorrect JSON data shape. To set system gesture exclusion rects, \na JSONObject with top, right, bottom and left values need to be set to int values.");
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public ArrayList<HashMap<String, Integer>> encodeExclusionRects(List<Rect> list) {
        ArrayList<HashMap<String, Integer>> arrayList = new ArrayList<>();
        for (Rect next : list) {
            HashMap hashMap = new HashMap();
            hashMap.put("top", Integer.valueOf(next.top));
            hashMap.put("right", Integer.valueOf(next.right));
            hashMap.put("bottom", Integer.valueOf(next.bottom));
            hashMap.put("left", Integer.valueOf(next.left));
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    @NonNull
    public AppSwitcherDescription decodeAppSwitcherDescription(@NonNull JSONObject jSONObject) throws JSONException {
        int i = jSONObject.getInt("primaryColor");
        if (i != 0) {
            i |= -16777216;
        }
        return new AppSwitcherDescription(i, jSONObject.getString("label"));
    }

    /* access modifiers changed from: private */
    @NonNull
    public List<SystemUiOverlay> decodeSystemUiOverlays(@NonNull JSONArray jSONArray) throws JSONException, NoSuchFieldException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            switch (SystemUiOverlay.fromValue(jSONArray.getString(i))) {
                case TOP_OVERLAYS:
                    arrayList.add(SystemUiOverlay.TOP_OVERLAYS);
                    break;
                case BOTTOM_OVERLAYS:
                    arrayList.add(SystemUiOverlay.BOTTOM_OVERLAYS);
                    break;
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    @NonNull
    public SystemChromeStyle decodeSystemChromeStyle(@NonNull JSONObject jSONObject) throws JSONException, NoSuchFieldException {
        Integer num = null;
        Brightness fromValue = !jSONObject.isNull("systemNavigationBarIconBrightness") ? Brightness.fromValue(jSONObject.getString("systemNavigationBarIconBrightness")) : null;
        Integer valueOf = !jSONObject.isNull("systemNavigationBarColor") ? Integer.valueOf(jSONObject.getInt("systemNavigationBarColor")) : null;
        Brightness fromValue2 = !jSONObject.isNull("statusBarIconBrightness") ? Brightness.fromValue(jSONObject.getString("statusBarIconBrightness")) : null;
        Integer valueOf2 = !jSONObject.isNull("statusBarColor") ? Integer.valueOf(jSONObject.getInt("statusBarColor")) : null;
        if (!jSONObject.isNull("systemNavigationBarDividerColor")) {
            num = Integer.valueOf(jSONObject.getInt("systemNavigationBarDividerColor"));
        }
        return new SystemChromeStyle(valueOf2, fromValue2, valueOf, fromValue, num);
    }

    public enum SoundType {
        CLICK("SystemSoundType.click");
        
        @NonNull
        private final String encodedName;

        @NonNull
        static SoundType fromValue(@NonNull String str) throws NoSuchFieldException {
            for (SoundType soundType : values()) {
                if (soundType.encodedName.equals(str)) {
                    return soundType;
                }
            }
            throw new NoSuchFieldException("No such SoundType: " + str);
        }

        private SoundType(@NonNull String str) {
            this.encodedName = str;
        }
    }

    public enum HapticFeedbackType {
        STANDARD((String) null),
        LIGHT_IMPACT("HapticFeedbackType.lightImpact"),
        MEDIUM_IMPACT("HapticFeedbackType.mediumImpact"),
        HEAVY_IMPACT("HapticFeedbackType.heavyImpact"),
        SELECTION_CLICK("HapticFeedbackType.selectionClick");
        
        @Nullable
        private final String encodedName;

        @NonNull
        static HapticFeedbackType fromValue(@Nullable String str) throws NoSuchFieldException {
            for (HapticFeedbackType hapticFeedbackType : values()) {
                if ((hapticFeedbackType.encodedName == null && str == null) || (hapticFeedbackType.encodedName != null && hapticFeedbackType.encodedName.equals(str))) {
                    return hapticFeedbackType;
                }
            }
            throw new NoSuchFieldException("No such HapticFeedbackType: " + str);
        }

        private HapticFeedbackType(@Nullable String str) {
            this.encodedName = str;
        }
    }

    public enum DeviceOrientation {
        PORTRAIT_UP("DeviceOrientation.portraitUp"),
        PORTRAIT_DOWN("DeviceOrientation.portraitDown"),
        LANDSCAPE_LEFT("DeviceOrientation.landscapeLeft"),
        LANDSCAPE_RIGHT("DeviceOrientation.landscapeRight");
        
        @NonNull
        private String encodedName;

        @NonNull
        static DeviceOrientation fromValue(@NonNull String str) throws NoSuchFieldException {
            for (DeviceOrientation deviceOrientation : values()) {
                if (deviceOrientation.encodedName.equals(str)) {
                    return deviceOrientation;
                }
            }
            throw new NoSuchFieldException("No such DeviceOrientation: " + str);
        }

        private DeviceOrientation(@NonNull String str) {
            this.encodedName = str;
        }
    }

    public enum SystemUiOverlay {
        TOP_OVERLAYS("SystemUiOverlay.top"),
        BOTTOM_OVERLAYS("SystemUiOverlay.bottom");
        
        @NonNull
        private String encodedName;

        @NonNull
        static SystemUiOverlay fromValue(@NonNull String str) throws NoSuchFieldException {
            for (SystemUiOverlay systemUiOverlay : values()) {
                if (systemUiOverlay.encodedName.equals(str)) {
                    return systemUiOverlay;
                }
            }
            throw new NoSuchFieldException("No such SystemUiOverlay: " + str);
        }

        private SystemUiOverlay(@NonNull String str) {
            this.encodedName = str;
        }
    }

    public static class AppSwitcherDescription {
        public final int color;
        @NonNull
        public final String label;

        public AppSwitcherDescription(int i, @NonNull String str) {
            this.color = i;
            this.label = str;
        }
    }

    public static class SystemChromeStyle {
        @Nullable
        public final Integer statusBarColor;
        @Nullable
        public final Brightness statusBarIconBrightness;
        @Nullable
        public final Integer systemNavigationBarColor;
        @Nullable
        public final Integer systemNavigationBarDividerColor;
        @Nullable
        public final Brightness systemNavigationBarIconBrightness;

        public SystemChromeStyle(@Nullable Integer num, @Nullable Brightness brightness, @Nullable Integer num2, @Nullable Brightness brightness2, @Nullable Integer num3) {
            this.statusBarColor = num;
            this.statusBarIconBrightness = brightness;
            this.systemNavigationBarColor = num2;
            this.systemNavigationBarIconBrightness = brightness2;
            this.systemNavigationBarDividerColor = num3;
        }
    }

    public enum Brightness {
        LIGHT("Brightness.light"),
        DARK("Brightness.dark");
        
        @NonNull
        private String encodedName;

        @NonNull
        static Brightness fromValue(@NonNull String str) throws NoSuchFieldException {
            for (Brightness brightness : values()) {
                if (brightness.encodedName.equals(str)) {
                    return brightness;
                }
            }
            throw new NoSuchFieldException("No such Brightness: " + str);
        }

        private Brightness(@NonNull String str) {
            this.encodedName = str;
        }
    }

    public enum ClipboardContentFormat {
        PLAIN_TEXT("text/plain");
        
        @NonNull
        private String encodedName;

        @NonNull
        static ClipboardContentFormat fromValue(@NonNull String str) throws NoSuchFieldException {
            for (ClipboardContentFormat clipboardContentFormat : values()) {
                if (clipboardContentFormat.encodedName.equals(str)) {
                    return clipboardContentFormat;
                }
            }
            throw new NoSuchFieldException("No such ClipboardContentFormat: " + str);
        }

        private ClipboardContentFormat(@NonNull String str) {
            this.encodedName = str;
        }
    }
}
