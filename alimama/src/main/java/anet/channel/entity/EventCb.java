package anet.channel.entity;

import anet.channel.Session;

public interface EventCb {
    void onEvent(Session session, int i, Event event);
}
