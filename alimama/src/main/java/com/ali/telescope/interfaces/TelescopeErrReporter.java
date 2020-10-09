package com.ali.telescope.interfaces;

import android.content.Context;

public interface TelescopeErrReporter {
    void report(Context context, ErrReporter errReporter);
}
