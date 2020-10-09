package com.taobao.weex.analyzer;

import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.core.logcat.LogConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {
    public static final String TYPE_3D = "3d";
    public static final String TYPE_ALL_PERFORMANCE = "all_performance";
    public static final String TYPE_CPU = "cpu";
    public static final String TYPE_DEBUG = "debug";
    public static final String TYPE_EXTERNAL_CONFIG = "external_config";
    public static final String TYPE_FPS = "fps";
    public static final String TYPE_JS_EXCEPTION = "js_exception";
    public static final String TYPE_LIFECYCLE = "lifecycle";
    public static final String TYPE_LOG = "log";
    public static final String TYPE_MEMORY = "memory";
    public static final String TYPE_MTOP_INSPECTOR = "mtop_inspector";
    public static final String TYPE_NATIVE_MEMORY = "native_memory";
    public static final String TYPE_RENDER_ANALYSIS = "render_analysis";
    public static final String TYPE_STORAGE = "storage";
    public static final String TYPE_TOTAL_MEMORY = "total_memory";
    public static final String TYPE_TRAFFIC = "traffic";
    public static final String TYPE_VIEW_HIGHLIGHT = "highlight_view";
    public static final String TYPE_VIEW_INSPECTOR = "view_inspector";
    public static final String TYPE_WEEX_PERFORMANCE_STATISTICS = "weex_performance_statistics";
    public static final String TYPE_WEEX_PERFORMANCE_STATISTICS_V2 = "weex_performance_statistics_v2";
    public static final String TYPE_WINDMILL_PERFORMANCE_STATISTICS = "windmill_performance_statistics";
    private boolean enableShake;
    public boolean enableWeexMonitor = true;
    private List<String> ignoreOptions;
    private LogConfig logConfig;

    public void setEnableShake(boolean z) {
        this.enableShake = z;
    }

    public void setIgnoreOptions(List<String> list) {
        this.ignoreOptions = list;
    }

    public boolean isEnableShake() {
        return this.enableShake;
    }

    public LogConfig getLogConfig() {
        return this.logConfig;
    }

    public void setLogConfig(LogConfig logConfig2) {
        this.logConfig = logConfig2;
    }

    @NonNull
    public List<String> getIgnoreOptions() {
        if (this.ignoreOptions == null || this.ignoreOptions.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.ignoreOptions);
    }

    public static class Builder {
        private boolean enableShake;
        private List<String> ignoreOptions = new ArrayList();
        private LogConfig logConfig;

        public Builder enableShake(boolean z) {
            this.enableShake = z;
            return this;
        }

        public Builder ignoreOption(String str) {
            this.ignoreOptions.add(str);
            return this;
        }

        public Builder logConfig(LogConfig logConfig2) {
            this.logConfig = logConfig2;
            return this;
        }

        public Config build() {
            Config config = new Config();
            config.setEnableShake(this.enableShake);
            config.setIgnoreOptions(this.ignoreOptions);
            config.setLogConfig(this.logConfig);
            return config;
        }
    }
}
