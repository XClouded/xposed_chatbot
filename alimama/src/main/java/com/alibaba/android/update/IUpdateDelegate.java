package com.alibaba.android.update;

import android.content.Context;

public interface IUpdateDelegate {
    Object doInBackground(Context context, Object... objArr);

    @Deprecated
    void execute(Context context, IUpdateCallback iUpdateCallback);
}
