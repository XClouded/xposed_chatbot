package com.ali.telescope.base.plugin;

import androidx.annotation.NonNull;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.report.IBeanReport;

public interface ITelescopeContext {
    void broadcastEvent(@NonNull Event event);

    IBeanReport getBeanReport();

    INameConverter getNameConverter();

    boolean isMatchedPluginOnPauseState(int i, String str);

    void registerBroadcast(int i, @NonNull String str);

    boolean requestPause(int i, @NonNull String str, int i2);

    boolean requestResume(int i, @NonNull String str, int i2);

    void unregisterBroadcast(int i, @NonNull String str);
}
