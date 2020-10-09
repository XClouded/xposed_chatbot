package com.ali.telescope.base.event;

import android.app.Activity;

public class ActivityEvent extends Event {
    public static final int ACTIVITY_CREATED = 1;
    public static final int ACTIVITY_DESTROYED = 6;
    public static final int ACTIVITY_PAUSED = 4;
    public static final int ACTIVITY_RESUMED = 3;
    public static final int ACTIVITY_STARTED = 2;
    public static final int ACTIVITY_STOPPED = 5;
    public int subEvent;
    public Activity target;

    private ActivityEvent(int i, Activity activity) {
        this.subEvent = i;
        this.target = activity;
        this.eventType = 1;
    }

    public static ActivityEvent obtain(int i, Activity activity) {
        return new ActivityEvent(i, activity);
    }
}
