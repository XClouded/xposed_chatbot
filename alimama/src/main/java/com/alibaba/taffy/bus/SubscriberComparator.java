package com.alibaba.taffy.bus;

import java.util.Comparator;

public class SubscriberComparator implements Comparator<Subscriber> {
    public static final int DEFAULT_PRIORITY = 0;

    public int compare(Subscriber subscriber, Subscriber subscriber2) {
        if (subscriber == subscriber2) {
            return 0;
        }
        if (subscriber == null) {
            return -1;
        }
        if (subscriber2 == null) {
            return 1;
        }
        int priority = subscriber.getPriority();
        if (priority < 0) {
            priority = 0;
        }
        int priority2 = subscriber2.getPriority();
        if (priority2 < 0) {
            priority2 = 0;
        }
        if (priority != priority2) {
            return priority - priority2;
        }
        if (subscriber.getId() > subscriber2.getId()) {
            return 1;
        }
        return subscriber.getId() < subscriber2.getId() ? -1 : 0;
    }
}
