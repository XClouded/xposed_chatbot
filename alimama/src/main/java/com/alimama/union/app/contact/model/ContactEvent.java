package com.alimama.union.app.contact.model;

import com.alimama.moon.eventbus.IEvent;

public class ContactEvent {

    public static final class PermissionDenied implements IEvent {
    }

    public static final class PermissionGranted implements IEvent {
    }
}
