package com.alibaba.android.update;

import android.content.Context;
import android.os.AsyncTask;

public class DefaultUpdateProcessor {
    /* access modifiers changed from: private */
    public Context mContex;

    public DefaultUpdateProcessor(Context context) {
        this.mContex = context;
    }

    public void process(final IUpdateDelegate iUpdateDelegate, final IUpdateCallback iUpdateCallback) {
        if (iUpdateDelegate != null) {
            new AsyncTask<Object, Object, Object>() {
                /* access modifiers changed from: protected */
                public void onPreExecute() {
                    if (iUpdateCallback != null) {
                        iUpdateCallback.onPreExecute(DefaultUpdateProcessor.this.mContex);
                    }
                }

                /* access modifiers changed from: protected */
                public Object doInBackground(Object... objArr) {
                    return iUpdateDelegate.doInBackground(DefaultUpdateProcessor.this.mContex, objArr);
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(Object obj) {
                    if (iUpdateCallback != null) {
                        iUpdateCallback.onPostExecute(DefaultUpdateProcessor.this.mContex, obj);
                    }
                }
            }.execute(new Object[0]);
        }
    }
}
