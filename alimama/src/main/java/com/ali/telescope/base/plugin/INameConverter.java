package com.ali.telescope.base.plugin;

import android.app.Activity;

public interface INameConverter {
    public static final INameConverter DEFAULT_CONVERTR = new INameConverter() {
        public String convert(Activity activity) {
            return activity == null ? "" : activity.getClass().getSimpleName();
        }
    };

    String convert(Activity activity);
}
