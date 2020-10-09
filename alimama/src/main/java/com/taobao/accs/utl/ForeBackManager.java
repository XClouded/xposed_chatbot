package com.taobao.accs.utl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.init.Launcher_InitAccs;
import com.taobao.agoo.TaobaoRegister;
import java.util.ArrayList;
import java.util.Iterator;
import org.android.agoo.common.AgooConstants;

public class ForeBackManager implements Application.ActivityLifecycleCallbacks {
    public static final String ACTION_STATE_BACK = "com.taobao.accs.ACTION_STATE_BACK";
    public static final String ACTION_STATE_DEEPBACK = "com.taobao.accs.ACTION_STATE_DEEPBACK";
    public static final String ACTION_STATE_FORE = "com.taobao.accs.ACTION_STATE_FORE";
    private static final int DEEP_DELAY = 10000;
    private static final int FIRST = 1;
    private static final int LAUNCH = 2;
    public static final int STATE_BACK = 0;
    public static final int STATE_DEEPBACK = 2;
    public static final int STATE_FORE = 1;
    private static final String TAG = "ForeBackManager";
    /* access modifiers changed from: private */
    public static ArrayList<ClickMessageRunnable> clickMessageRunnableArrayList;
    /* access modifiers changed from: private */
    public static Context context;
    /* access modifiers changed from: private */
    public static long currentLaunchTime;
    private static volatile ForeBackManager foreBackManager;
    /* access modifiers changed from: private */
    public static long lastLaunchTime;
    private int count = 0;
    private Runnable deepRunnable = new Runnable() {
        public void run() {
            int unused = ForeBackManager.this.state = 2;
            LocalBroadcastManager.getInstance(ForeBackManager.context).sendBroadcast(new Intent(ForeBackManager.ACTION_STATE_DEEPBACK));
        }
    };
    private int flag = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isComeFromBg;
    private boolean isPullUp;
    /* access modifiers changed from: private */
    public int state = 1;

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public static ForeBackManager getManager() {
        if (foreBackManager == null) {
            synchronized (ForeBackManager.class) {
                if (foreBackManager == null) {
                    foreBackManager = new ForeBackManager();
                }
            }
        }
        return foreBackManager;
    }

    private ForeBackManager() {
        clickMessageRunnableArrayList = new ArrayList<>();
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (!this.isPullUp) {
            return;
        }
        if ((this.flag & 1) != 1) {
            this.flag |= 1;
            this.flag |= 2;
            currentLaunchTime = System.currentTimeMillis();
            lastLaunchTime = OrangeAdapter.getLastLaunchTime(context);
            OrangeAdapter.saveLastLaunchTime(context, currentLaunchTime);
        } else if ((this.flag & 2) == 2) {
            this.flag &= -3;
        }
    }

    public void onActivityStarted(Activity activity) {
        int i = this.count;
        this.count = i + 1;
        int i2 = 2;
        if (i == 0) {
            ALog.i(TAG, "onActivityStarted back to force", new Object[0]);
            this.isComeFromBg = true;
            boolean z = this.state == 2;
            this.state = 1;
            Intent intent = new Intent(new Intent(ACTION_STATE_FORE));
            intent.putExtra("state", z);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
        if (this.isPullUp) {
            int i3 = this.isComeFromBg ? 4 : 0;
            if ((this.flag & 2) != 2) {
                i2 = 1;
            }
            int i4 = i3 | i2;
            if (activity != null && activity.getIntent() != null) {
                try {
                    Intent intent2 = activity.getIntent();
                    String stringExtra = intent2.getStringExtra(AgooConstants.FLAG_FROM_AGOO_MESSAGE_ID);
                    if (!TextUtils.isEmpty(stringExtra)) {
                        ALog.i(TAG, "onActivityStarted isFromAgoo", new Object[0]);
                        intent2.removeExtra(AgooConstants.FLAG_FROM_AGOO_MESSAGE_ID);
                        final ClickMessageRunnable clickMessageRunnable = new ClickMessageRunnable(stringExtra, i4);
                        if (!TaobaoRegister.isRegisterSuccess()) {
                            if (!Launcher_InitAccs.mIsInited) {
                                ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
                                    public void run() {
                                        ForeBackManager.clickMessageRunnableArrayList.add(clickMessageRunnable);
                                    }
                                });
                                return;
                            }
                        }
                        ThreadPoolExecutorFactory.execute(clickMessageRunnable);
                    }
                } catch (Exception e) {
                    ALog.e(TAG, "onActivityStarted Error:", e, new Object[0]);
                }
            }
        }
    }

    public void onActivityResumed(Activity activity) {
        this.isComeFromBg = false;
    }

    public void onActivityStopped(Activity activity) {
        int i = this.count - 1;
        this.count = i;
        if (i == 0) {
            this.state = 0;
            this.handler.postDelayed(this.deepRunnable, 10000);
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_STATE_BACK));
        }
    }

    public void initialize(Application application) {
        if (context == null) {
            context = application;
            this.isPullUp = OrangeAdapter.isPullUp();
            application.registerActivityLifecycleCallbacks(this);
        }
    }

    public void uninitialize(Application application) {
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }
    }

    public void reportSaveClickMessage() {
        if (clickMessageRunnableArrayList != null) {
            Iterator<ClickMessageRunnable> it = clickMessageRunnableArrayList.iterator();
            while (it.hasNext()) {
                ThreadPoolExecutorFactory.getScheduledExecutor().execute(it.next());
            }
            clickMessageRunnableArrayList.clear();
        }
    }

    public int getState() {
        return this.state;
    }

    public static class ClickMessageRunnable implements Runnable {
        private String msgId;
        private int startFlag;

        ClickMessageRunnable(String str, int i) {
            this.msgId = str;
            this.startFlag = i;
        }

        public void run() {
            if (ForeBackManager.lastLaunchTime == 0 || UtilityImpl.isSameDay(ForeBackManager.lastLaunchTime, ForeBackManager.currentLaunchTime)) {
                this.startFlag |= 8;
            }
            TaobaoRegister.setLastCurrentLaunchTime(ForeBackManager.lastLaunchTime);
            TaobaoRegister.clickMessage(ForeBackManager.context, this.msgId, (String) null, this.startFlag);
        }
    }
}
