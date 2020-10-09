package com.taobao.android.dinamicx.notification;

import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class DXSignalProduce {
    public static int PERIOD_TIME = 50;
    /* access modifiers changed from: private */
    public int MAX_TRACKER_COUNT;
    CopyOnWriteArrayList<WeakReference<DXControlEventCenter>> controlEventCenter;
    CopyOnWriteArrayList<WeakReference<DXNotificationCenter>> notificationCenter;
    int trackerCount;

    public interface SignalReceiver {
        void onReceiver();
    }

    public static DXSignalProduce getInstance() {
        return DXSignalProduceHolder.INSTANCE;
    }

    private static final class DXSignalProduceHolder {
        /* access modifiers changed from: private */
        public static final DXSignalProduce INSTANCE = new DXSignalProduce();

        private DXSignalProduceHolder() {
        }
    }

    private DXSignalProduce() {
        this.MAX_TRACKER_COUNT = 10;
        this.notificationCenter = new CopyOnWriteArrayList<>();
        this.controlEventCenter = new CopyOnWriteArrayList<>();
        startProduce();
    }

    /* access modifiers changed from: package-private */
    public void startProduce() {
        DXRunnableManager.scheduledExecutorService().scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    DXSignalProduce.this.scheduleNotification();
                    DXSignalProduce.this.scheduleControlEvent();
                } catch (Throwable th) {
                    if (DXSignalProduce.this.trackerCount < DXSignalProduce.this.MAX_TRACKER_COUNT) {
                        DXError dXError = new DXError("dinamicx");
                        DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_SIGNAL, DXMonitorConstant.DX_MONITOR_SIGNAL_EXCETION_, DXError.DX_ERROR_CODE_SIGNAL_EXCEPTION_CRASH);
                        dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
                        dXError.dxErrorInfoList.add(dXErrorInfo);
                        DXAppMonitor.trackerError(dXError);
                        DXSignalProduce.this.trackerCount++;
                    }
                }
            }
        }, 0, (long) PERIOD_TIME, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: private */
    public void scheduleControlEvent() {
        int i = 0;
        while (i < this.controlEventCenter.size()) {
            SignalReceiver signalReceiver = (SignalReceiver) this.controlEventCenter.get(i).get();
            if (signalReceiver != null) {
                signalReceiver.onReceiver();
                i++;
            } else {
                this.controlEventCenter.remove(i);
            }
        }
    }

    public void registerControlEventCenter(DXControlEventCenter dXControlEventCenter) {
        if (dXControlEventCenter != null) {
            this.controlEventCenter.add(new WeakReference(dXControlEventCenter));
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterControlEventCenter(DXControlEventCenter dXControlEventCenter) {
        if (dXControlEventCenter != null) {
            for (int i = 0; i < this.controlEventCenter.size(); i++) {
                if (this.controlEventCenter.get(i).get() == dXControlEventCenter) {
                    this.controlEventCenter.remove(i);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void scheduleNotification() {
        int i = 0;
        while (i < this.notificationCenter.size()) {
            SignalReceiver signalReceiver = (SignalReceiver) this.notificationCenter.get(i).get();
            if (signalReceiver != null) {
                signalReceiver.onReceiver();
                i++;
            } else {
                this.notificationCenter.remove(i);
            }
        }
    }

    public void registerNotificationCenter(DXNotificationCenter dXNotificationCenter) {
        if (dXNotificationCenter != null) {
            this.notificationCenter.add(new WeakReference(dXNotificationCenter));
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterNotificationCenter(DXNotificationCenter dXNotificationCenter) {
        if (dXNotificationCenter != null) {
            for (int i = 0; i < this.notificationCenter.size(); i++) {
                if (this.notificationCenter.get(i).get() == dXNotificationCenter) {
                    this.notificationCenter.remove(i);
                    return;
                }
            }
        }
    }
}
