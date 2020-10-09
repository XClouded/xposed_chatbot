package com.ali.telescope.internal.plugins.anr.sharedpreferences;

import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.ali.telescope.util.ThreadUtils;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SharedPreferenceWrapper implements SharedPreferences {
    private final SharedPreferences sharedPreferences;

    public SharedPreferenceWrapper(SharedPreferences sharedPreferences2) {
        this.sharedPreferences = sharedPreferences2;
    }

    public Map<String, ?> getAll() {
        return this.sharedPreferences.getAll();
    }

    @Nullable
    public String getString(String str, @Nullable String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    @Nullable
    public Set<String> getStringSet(String str, @Nullable Set<String> set) {
        return this.sharedPreferences.getStringSet(str, set);
    }

    public int getInt(String str, int i) {
        return this.sharedPreferences.getInt(str, i);
    }

    public long getLong(String str, long j) {
        return this.sharedPreferences.getLong(str, j);
    }

    public float getFloat(String str, float f) {
        return this.sharedPreferences.getFloat(str, f);
    }

    public boolean getBoolean(String str, boolean z) {
        return this.sharedPreferences.getBoolean(str, z);
    }

    public boolean contains(String str) {
        return this.sharedPreferences.contains(str);
    }

    public SharedPreferences.Editor edit() {
        return new EditImplWrapper(this.sharedPreferences.edit());
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private static class EditImplWrapper implements SharedPreferences.Editor {
        static final Executor executor = new ThreadPoolExecutor(0, 1, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        final SharedPreferences.Editor editor;

        public EditImplWrapper(SharedPreferences.Editor editor2) {
            this.editor = editor2;
        }

        public SharedPreferences.Editor putString(String str, @Nullable String str2) {
            return this.editor.putString(str, str2);
        }

        public SharedPreferences.Editor putStringSet(String str, @Nullable Set<String> set) {
            return this.editor.putStringSet(str, set);
        }

        public SharedPreferences.Editor putInt(String str, int i) {
            return this.editor.putInt(str, i);
        }

        public SharedPreferences.Editor putLong(String str, long j) {
            return this.editor.putLong(str, j);
        }

        public SharedPreferences.Editor putFloat(String str, float f) {
            return this.editor.putFloat(str, f);
        }

        public SharedPreferences.Editor putBoolean(String str, boolean z) {
            return this.editor.putBoolean(str, z);
        }

        public SharedPreferences.Editor remove(String str) {
            return this.editor.remove(str);
        }

        public SharedPreferences.Editor clear() {
            return this.editor.clear();
        }

        public boolean commit() {
            return this.editor.commit();
        }

        public void apply() {
            if (ThreadUtils.isUiThread()) {
                executor.execute(new Runnable() {
                    public void run() {
                        EditImplWrapper.this.editor.commit();
                    }
                });
            } else {
                this.editor.commit();
            }
        }
    }
}
