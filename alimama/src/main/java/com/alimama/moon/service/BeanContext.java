package com.alimama.moon.service;

import java.util.HashMap;
import java.util.Map;

public final class BeanContext {
    private static BeanContext instance;
    private Map<Class<?>, Object> beans = new HashMap();

    private BeanContext() {
    }

    public static void register(Class<?> cls, Object obj) {
        getInstance().put(cls, obj);
    }

    public static <T> T get(Class<T> cls) {
        return getInstance().retrieve(cls);
    }

    private static BeanContext getInstance() {
        if (instance == null) {
            synchronized (BeanContext.class) {
                if (instance == null) {
                    instance = new BeanContext();
                }
            }
        }
        return instance;
    }

    private <T> T retrieve(Class<T> cls) {
        return this.beans.get(cls);
    }

    private void put(Class<?> cls, Object obj) {
        this.beans.put(cls, obj);
    }
}
