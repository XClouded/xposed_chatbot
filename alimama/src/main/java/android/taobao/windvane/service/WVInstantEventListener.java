package android.taobao.windvane.service;

public interface WVInstantEventListener {
    WVEventResult onInstantEvent(int i, WVEventContext wVEventContext, Object... objArr);
}
