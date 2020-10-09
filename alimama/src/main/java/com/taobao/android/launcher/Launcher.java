package com.taobao.android.launcher;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Launcher<T> {
    public static final String TAG = "LauncherError";
    /* access modifiers changed from: private */
    public static Builder mDefaultBuilder;
    /* access modifiers changed from: private */
    public static HashMap<String, Builder> sBuilderMap = new HashMap<>();
    private Builder mBuilder;

    public static void defaultBuilder(String str) {
        mDefaultBuilder = sBuilderMap.get(str);
    }

    public static void addBuilder(String str, Builder builder) {
        sBuilderMap.put(str, builder);
    }

    public Launcher(Builder builder) {
        this.mBuilder = builder;
    }

    public void start(T t) {
        if (this.mBuilder == null) {
            Log.e(TAG, "builder can not be null");
        } else if (this.mBuilder.mExecutors.size() == 0) {
            Log.e(TAG, "mExecutors can not be null");
        } else {
            int i = 0;
            if (this.mBuilder.mExecutors.size() == 1) {
                ((IExecutable) this.mBuilder.mExecutors.get(0)).execute(t);
                return;
            }
            int size = this.mBuilder.mExecutors.size();
            while (i < size && !((IExecutable) this.mBuilder.mExecutors.get(i)).execute(t)) {
                i++;
            }
        }
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public List<IExecutable> mExecutors = new ArrayList();

        public Builder add(IExecutable iExecutable) {
            this.mExecutors.add(iExecutable);
            return this;
        }

        public static <T> Launcher<T> create(String str) {
            Builder builder = (Builder) Launcher.sBuilderMap.get(str);
            if (builder == null) {
                builder = Launcher.mDefaultBuilder;
            }
            if (builder != null) {
                return new Launcher<>(builder);
            }
            throw new RuntimeException("can not find builder by name=" + str);
        }
    }

    public static void free() {
        sBuilderMap.clear();
    }
}
