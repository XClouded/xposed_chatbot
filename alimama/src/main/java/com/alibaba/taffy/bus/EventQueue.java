package com.alibaba.taffy.bus;

import com.alibaba.taffy.bus.event.Event;
import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
    private Queue<Event> eventQueue = new LinkedList();

    public void offer(Event event) {
        this.eventQueue.offer(event);
    }

    public Event poll() {
        return this.eventQueue.poll();
    }

    public boolean isEmpty() {
        return this.eventQueue.isEmpty();
    }
}
