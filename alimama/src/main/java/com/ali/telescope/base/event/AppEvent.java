package com.ali.telescope.base.event;

public class AppEvent extends Event {
    public static final int APP_BACKGROUND = 1;
    public static final int APP_FORGROUND = 2;
    private static final AppEvent BACKGROUND_EVENT = new AppEvent(1);
    private static final AppEvent FORGROUND_EVENT = new AppEvent(2);
    public int subEvent;

    private AppEvent(int i) {
        this.subEvent = i;
        this.eventType = 2;
    }

    public static AppEvent obtain(int i) {
        if (i == 1) {
            return BACKGROUND_EVENT;
        }
        if (i == 2) {
            return FORGROUND_EVENT;
        }
        throw new IllegalArgumentException("subEvent : " + i + " is not valid");
    }
}
