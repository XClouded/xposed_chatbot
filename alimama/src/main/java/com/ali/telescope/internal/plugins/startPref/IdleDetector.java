package com.ali.telescope.internal.plugins.startPref;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import com.ali.telescope.util.TelescopeLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class IdleDetector {
    public static final int ADD_IDLE_HANDLER_MESSAGE = 1;
    public static final int IDLE_DETECTED = 2;
    private static final int IDLE_DURATION_MILLIS = 800;
    public static final String TAG = "bootFinishedIdle";
    public static HashMap<String, Boolean> actStartUpFinished = new HashMap<>();
    public static boolean isFinished = false;
    public static boolean isFinishedExternControl = false;
    /* access modifiers changed from: private */
    public static long mFirstIdleTime = -1;
    /* access modifiers changed from: private */
    public static MessageQueue.IdleHandler mIdleHandler = new MessageQueue.IdleHandler() {
        public boolean queueIdle() {
            long currentTimeMillis = System.currentTimeMillis();
            TelescopeLog.i(IdleDetector.TAG, "mFirstIdleTime:" + (IdleDetector.mFirstIdleTime % 100000) + ", curTime:" + (currentTimeMillis % 100000));
            if (IdleDetector.mFirstIdleTime < 0) {
                long unused = IdleDetector.mFirstIdleTime = currentTimeMillis;
                long unused2 = IdleDetector.mLastIdleTime = currentTimeMillis;
            }
            if (currentTimeMillis - IdleDetector.mLastIdleTime >= 100) {
                long unused3 = IdleDetector.mFirstIdleTime = currentTimeMillis;
            }
            long unused4 = IdleDetector.mLastIdleTime = currentTimeMillis;
            if (currentTimeMillis - IdleDetector.mFirstIdleTime >= 800) {
                TelescopeLog.v(IdleDetector.TAG, "send IDLE_DETECTED");
                IdleDetector.getInstance().sendMessage(2);
            } else {
                TelescopeLog.v(IdleDetector.TAG, "send ADD_IDLE_HANDLER_MESSAGE");
                IdleDetector.getInstance().sendMessageDelayed(1, 50);
            }
            return false;
        }
    };
    private static IdleDetector mInstance = null;
    /* access modifiers changed from: private */
    public static long mLastIdleTime = -1;
    /* access modifiers changed from: private */
    public static MessageQueue mMessageQueue;
    public static ArrayList<String> startUpPageNames = new ArrayList<>();
    private ArrayList<onBootFinishedIdlelistener> arrCallbacks = new ArrayList<>();
    private boolean isTrigger = false;
    private Application mApplication;
    private MyHandler mHandler;

    public interface onBootFinishedIdlelistener {
        void callback();
    }

    private void unregisterLifecycle() {
    }

    public static IdleDetector getInstance() {
        if (mInstance == null) {
            synchronized (IdleDetector.class) {
                if (mInstance == null) {
                    mInstance = new IdleDetector();
                }
            }
        }
        return mInstance;
    }

    public IdleDetector setStartUpPageName(String[] strArr) {
        for (String add : strArr) {
            startUpPageNames.add(add);
        }
        return this;
    }

    public void start(Application application) {
        this.mApplication = application;
        startDetectDurationIdle();
        this.isTrigger = false;
    }

    public void stop() {
        unregisterLifecycle();
        this.arrCallbacks.clear();
        startUpPageNames.clear();
        this.mApplication = null;
        mMessageQueue = null;
        this.isTrigger = true;
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler = null;
        }
    }

    private static class MyHandler extends Handler {
        private MyHandler() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    if (IdleDetector.mMessageQueue != null) {
                        IdleDetector.mMessageQueue.addIdleHandler(IdleDetector.mIdleHandler);
                        return;
                    }
                    return;
                case 2:
                    IdleDetector.getInstance().dispatch();
                    return;
                default:
                    return;
            }
        }
    }

    public boolean sendMessage(int i) {
        if (this.mHandler == null) {
            return false;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(i));
        return true;
    }

    public boolean sendMessageDelayed(int i, long j) {
        if (this.mHandler == null) {
            return false;
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(i), j);
        return true;
    }

    /* access modifiers changed from: private */
    public void dispatch() {
        if (!this.isTrigger) {
            this.isTrigger = true;
            Iterator<onBootFinishedIdlelistener> it = this.arrCallbacks.iterator();
            while (it.hasNext()) {
                it.next().callback();
            }
            stop();
        }
    }

    public void onActivityResumed(String str) {
        TelescopeLog.v(TAG, "Activity Name :" + str);
        if (this.mApplication != null) {
            judgeBootFinished(str);
        }
    }

    public void onPieceResumed(String str) {
        TelescopeLog.v(TAG, "Piece Name :" + str);
        if (this.mApplication != null) {
            judgeBootFinished(str);
        }
    }

    private void judgeBootFinished(String str) {
        mFirstIdleTime = -1;
        if (!this.isTrigger) {
            if (!isFinishedExternControl) {
                boolean z = false;
                Iterator<String> it = startUpPageNames.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().equals(str)) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (z) {
                    actStartUpFinished.put(str, true);
                }
                if (actStartUpFinished.size() == startUpPageNames.size() && !startUpPageNames.isEmpty()) {
                    isFinished = true;
                }
            }
            if (isFinished) {
                this.isTrigger = true;
                startDetectDurationIdle();
            }
        }
    }

    private void startDetectDurationIdle() {
        TelescopeLog.i(TAG, "startDetectDurationIdle : " + System.currentTimeMillis());
        mMessageQueue = Looper.myQueue();
        mMessageQueue.addIdleHandler(mIdleHandler);
        this.mHandler = new MyHandler();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                IdleDetector.getInstance().dispatch();
            }
        }, 2800);
        unregisterLifecycle();
    }

    public IdleDetector addOnFinishedCallback(onBootFinishedIdlelistener onbootfinishedidlelistener) {
        this.arrCallbacks.add(onbootfinishedidlelistener);
        return this;
    }
}
