package com.ali.user.mobile.coordinator;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;

public class CoordinatorWrapper<Params> {
    @TargetApi(11)
    public void execute(Runnable runnable) {
        if (runnable != null) {
            try {
                if (DataProviderFactory.getDataProvider().useSeparateThreadPool()) {
                    Coordinator.execute(runnable);
                } else if (!executeWithSki(runnable)) {
                    Coordinator.execute(runnable);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private boolean executeWithSki(Runnable runnable) {
        try {
            Class.forName("com.taobao.android.task.Coordinator").getMethod("execute", new Class[]{Runnable.class}).invoke((Object) null, new Object[]{runnable});
            return true;
        } catch (ClassNotFoundException | Exception unused) {
            return false;
        }
    }

    public void execute(AsyncTask asyncTask, Params... paramsArr) {
        if (asyncTask == null) {
            return;
        }
        if (DataProviderFactory.getDataProvider().useSeparateThreadPool()) {
            try {
                asyncTask.executeOnExecutor(Coordinator.sThreadPoolExecutor, paramsArr);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } else {
            try {
                asyncTask.execute(paramsArr);
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
    }
}
