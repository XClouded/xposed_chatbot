package com.alibaba.taffy.bus;

public final class TBusBuilder {
    private static TBus bus;

    private TBusBuilder() {
    }

    public static TBus instance() {
        if (bus == null) {
            synchronized (TBusBuilder.class) {
                if (bus == null) {
                    bus = new TBus();
                }
            }
        }
        return bus;
    }

    public static void destroy() {
        if (bus != null) {
            synchronized (TBusBuilder.class) {
                if (bus != null) {
                    bus.destroy();
                    bus = null;
                }
            }
        }
    }
}
