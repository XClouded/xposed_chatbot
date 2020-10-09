package com.alimama.moon.eventbus;

import dagger.internal.Factory;

public final class DefaultEventBusImpl_Factory implements Factory<DefaultEventBusImpl> {
    private static final DefaultEventBusImpl_Factory INSTANCE = new DefaultEventBusImpl_Factory();

    public DefaultEventBusImpl get() {
        return new DefaultEventBusImpl();
    }

    public static Factory<DefaultEventBusImpl> create() {
        return INSTANCE;
    }
}
