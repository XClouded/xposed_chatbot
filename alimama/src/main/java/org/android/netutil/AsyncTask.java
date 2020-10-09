package org.android.netutil;

import androidx.annotation.Keep;

@Keep
public abstract class AsyncTask {
    protected boolean done;

    /* access modifiers changed from: protected */
    public void onTaskFinish() {
        this.done = true;
    }
}
