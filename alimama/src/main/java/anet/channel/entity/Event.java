package anet.channel.entity;

public class Event {
    public int errorCode;
    public String errorDetail;
    public final int eventType;

    public Event(int i) {
        this.eventType = i;
    }

    public Event(int i, int i2, String str) {
        this.eventType = i;
        this.errorCode = i2;
        this.errorDetail = str;
    }
}
