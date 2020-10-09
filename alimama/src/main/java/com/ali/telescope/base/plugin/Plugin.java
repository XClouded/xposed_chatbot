package com.ali.telescope.base.plugin;

import android.app.Application;
import com.ali.telescope.base.event.Event;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.json.JSONObject;

public class Plugin {
    public static final int BOUND_TYPE_CPU = 2;
    public static final int BOUND_TYPE_IO = 1;
    public static final int BOUND_TYPE_MEM = 4;
    public static final int BOUND_TYPE_NONE = 0;
    public int boundType = 0;
    public String pluginID;
    public int priority;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BoundType {
    }

    public boolean isPaused() {
        return false;
    }

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
    }

    public void onDestroy() {
    }

    public void onEvent(int i, Event event) {
    }

    public void onPause(int i, int i2) {
    }

    public void onResume(int i, int i2) {
    }

    public boolean isMatchBoundType(int i) {
        return (i & this.boundType) != 0;
    }
}
