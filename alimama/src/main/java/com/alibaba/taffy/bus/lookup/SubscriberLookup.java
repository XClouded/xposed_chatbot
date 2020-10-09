package com.alibaba.taffy.bus.lookup;

import com.alibaba.taffy.bus.Subscriber;
import java.util.Collection;
import java.util.Map;

public interface SubscriberLookup {
    Map<String, Collection<Subscriber>> findAll(Object obj, LookupListener lookupListener);
}
