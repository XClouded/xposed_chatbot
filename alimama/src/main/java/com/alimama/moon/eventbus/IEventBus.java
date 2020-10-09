package com.alimama.moon.eventbus;

public interface IEventBus {
    boolean isRegistered(ISubscriber iSubscriber);

    void post(IEvent iEvent);

    void register(ISubscriber iSubscriber);

    void unregister(ISubscriber iSubscriber);
}
