package com.alimama.moon.eventbus;

import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

@Singleton
public final class DefaultEventBusImpl implements IEventBus {
    public void post(IEvent iEvent) {
        EventBus.getDefault().post(iEvent);
    }

    public boolean isRegistered(ISubscriber iSubscriber) {
        return EventBus.getDefault().isRegistered(iSubscriber);
    }

    public void register(ISubscriber iSubscriber) {
        EventBus.getDefault().register(iSubscriber);
    }

    public void unregister(ISubscriber iSubscriber) {
        EventBus.getDefault().unregister(iSubscriber);
    }
}
