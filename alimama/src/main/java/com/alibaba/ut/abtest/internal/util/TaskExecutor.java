package com.alibaba.ut.abtest.internal.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class TaskExecutor {
    private static final String TAG = "TaskExecutor";
    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private static volatile Handler myHandler = null;

    private static synchronized void initMyHandler() {
        synchronized (TaskExecutor.class) {
            if (myHandler == null) {
                HandlerThread handlerThread = new HandlerThread(TAG);
                handlerThread.start();
                myHandler = new Handler(handlerThread.getLooper()) {
                    public void handleMessage(Message message) {
                        super.handleMessage(message);
                        try {
                            if (message.obj != null && (message.obj instanceof Runnable)) {
                                TaskExecutor.executeBackground((Runnable) message.obj);
                            }
                        } catch (Throwable unused) {
                        }
                    }
                };
            }
        }
    }

    public static void executeBackground(Runnable runnable) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(runnable);
    }

    public static void executeBackgroundDelayed(Runnable runnable, long j) {
        executeBackgroundDelayed(0, runnable, j);
    }

    public static void executeBackgroundDelayed(int i, Runnable runnable, long j) {
        try {
            if (myHandler == null) {
                initMyHandler();
            }
            Message obtain = Message.obtain(myHandler, i);
            obtain.obj = runnable;
            myHandler.sendMessageDelayed(obtain, j);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }

    public static void removeBackgroundCallbacks(int i) {
        try {
            if (myHandler == null) {
                initMyHandler();
            }
            myHandler.removeMessages(i);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }

    public static boolean hasBackgroundCallbacks(int i) {
        try {
            if (myHandler == null) {
                initMyHandler();
            }
            return myHandler.hasMessages(i);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
            return false;
        }
    }

    public static void executeMain(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public static void executeMainDelayed(Runnable runnable, long j) {
        mainHandler.postDelayed(runnable, j);
    }
}
