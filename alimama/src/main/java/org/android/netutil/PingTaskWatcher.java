package org.android.netutil;

public interface PingTaskWatcher {
    void OnEntry(int i, int i2, double d);

    void OnFailed(int i);

    void OnFinished();
}
