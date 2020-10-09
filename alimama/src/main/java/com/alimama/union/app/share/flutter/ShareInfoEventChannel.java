package com.alimama.union.app.share.flutter;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;

public class ShareInfoEventChannel implements EventChannel.StreamHandler {
    private static final String CHANNEL = "com.alimama.moon/getshareinfo";

    public void onCancel(Object obj) {
    }

    public static void register(BinaryMessenger binaryMessenger) {
        new EventChannel(binaryMessenger, CHANNEL).setStreamHandler(new ShareInfoEventChannel());
    }

    public void onListen(Object obj, EventChannel.EventSink eventSink) {
        eventSink.success("fetching");
    }
}
