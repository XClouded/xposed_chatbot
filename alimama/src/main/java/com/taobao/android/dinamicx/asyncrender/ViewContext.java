package com.taobao.android.dinamicx.asyncrender;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.log.DXLog;
import java.lang.ref.WeakReference;

public class ViewContext extends ContextWrapper {
    static final String TAG = "ViewContext";
    private WeakReference<Context> currContextRef;

    public ViewContext(Context context) {
        super(context.getApplicationContext());
        this.currContextRef = new WeakReference<>(context);
    }

    public void setCurrentContext(Context context) {
        this.currContextRef = new WeakReference<>(context);
    }

    public Context getCurrentContext() {
        if (this.currContextRef != null) {
            return (Context) this.currContextRef.get();
        }
        return null;
    }

    public void startActivity(Intent intent) {
        Context context = this.currContextRef != null ? (Context) this.currContextRef.get() : null;
        if (context != null) {
            context.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }

    @TargetApi(16)
    public void startActivity(Intent intent, Bundle bundle) {
        Context context = this.currContextRef != null ? (Context) this.currContextRef.get() : null;
        if (context != null) {
            context.startActivity(intent, bundle);
        } else {
            super.startActivity(intent, bundle);
        }
    }

    public void startActivities(Intent[] intentArr) {
        Context context = this.currContextRef != null ? (Context) this.currContextRef.get() : null;
        if (context != null) {
            context.startActivities(intentArr);
        } else {
            super.startActivities(intentArr);
        }
    }

    @TargetApi(16)
    public void startActivities(Intent[] intentArr, Bundle bundle) {
        Context context = this.currContextRef != null ? (Context) this.currContextRef.get() : null;
        if (context != null) {
            context.startActivities(intentArr, bundle);
        } else {
            super.startActivities(intentArr, bundle);
        }
    }

    public Resources getResources() {
        Context context = this.currContextRef != null ? (Context) this.currContextRef.get() : null;
        if (context != null) {
            return context.getResources();
        }
        return super.getResources();
    }

    public Resources.Theme getTheme() {
        Context context = this.currContextRef != null ? (Context) this.currContextRef.get() : null;
        if (context != null) {
            return context.getTheme();
        }
        return super.getTheme();
    }

    public static Context getRealContext(Context context) {
        if (context instanceof ViewContext) {
            Context currentContext = ((ViewContext) context).getCurrentContext();
            if (currentContext != null) {
                return currentContext;
            }
            DXLog.d(TAG, "getRealContext but currContext is null");
        }
        return context;
    }

    @Nullable
    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (!(context instanceof ViewContext)) {
            return null;
        }
        Context currentContext = ((ViewContext) context).getCurrentContext();
        if (currentContext instanceof Activity) {
            return (Activity) currentContext;
        }
        return null;
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        try {
            Context currentContext = getCurrentContext();
            if (currentContext != null) {
                return currentContext.registerReceiver(broadcastReceiver, intentFilter);
            }
            return super.registerReceiver(broadcastReceiver, intentFilter);
        } catch (Throwable unused) {
            return null;
        }
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler) {
        try {
            Context currentContext = getCurrentContext();
            if (currentContext != null) {
                return currentContext.registerReceiver(broadcastReceiver, intentFilter, str, handler);
            }
            return super.registerReceiver(broadcastReceiver, intentFilter, str, handler);
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|(2:3|4)|5|6|8) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unregisterReceiver(android.content.BroadcastReceiver r2) {
        /*
            r1 = this;
            android.content.Context r0 = r1.getCurrentContext()     // Catch:{ Throwable -> 0x000a }
            if (r0 == 0) goto L_0x000a
            r0.unregisterReceiver(r2)     // Catch:{ Throwable -> 0x000a }
            return
        L_0x000a:
            super.unregisterReceiver(r2)     // Catch:{ Throwable -> 0x000d }
        L_0x000d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.asyncrender.ViewContext.unregisterReceiver(android.content.BroadcastReceiver):void");
    }
}
