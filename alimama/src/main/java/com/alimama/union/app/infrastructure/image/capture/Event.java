package com.alimama.union.app.infrastructure.image.capture;

import com.alimama.moon.eventbus.IEvent;

public class Event {

    public static final class WriteExternalStoragePermissionDenied implements IEvent {
    }

    public static final class WriteExternalStoragePermissionGranted implements IEvent {
    }
}
