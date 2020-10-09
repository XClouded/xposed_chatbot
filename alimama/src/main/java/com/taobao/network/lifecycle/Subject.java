package com.taobao.network.lifecycle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subject {
    private List<Observer> observers = new CopyOnWriteArrayList();

    public void register(Observer observer) {
        this.observers.add(observer);
    }

    public void unregister(Observer observer) {
        this.observers.remove(observer);
    }

    public void notify(Object obj) {
        for (Observer call : this.observers) {
            call.call(obj);
        }
    }

    public static Subject instance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static final Subject INSTANCE = new Subject();

        private Holder() {
        }
    }
}
