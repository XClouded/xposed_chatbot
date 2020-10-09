package com.alibaba.taffy.bus.lookup;

import com.alibaba.taffy.bus.Subscriber;

public interface LookupListener {
    Subscriber onLookup(Subscriber subscriber);
}
