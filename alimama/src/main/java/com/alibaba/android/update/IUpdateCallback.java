package com.alibaba.android.update;

import android.content.Context;

public interface IUpdateCallback {
    @Deprecated
    void execute(Context context, Object obj);

    void onPostExecute(Context context, Object obj);

    void onPreExecute(Context context);
}
