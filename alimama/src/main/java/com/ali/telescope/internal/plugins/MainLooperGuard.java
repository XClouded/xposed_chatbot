package com.ali.telescope.internal.plugins;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Printer;
import com.ali.telescope.util.Reflector;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainLooperGuard {
    private static final int CHECK_INTERVAL = 10000;
    private static MainLooperGuard mInstance;
    /* access modifiers changed from: private */
    public final CopyOnWriteArrayList<LoopCallback> mCallbacks = new CopyOnWriteArrayList<>();
    /* access modifiers changed from: private */
    public final InnerHandler mCheckHandler = new InnerHandler(Looper.getMainLooper());
    private volatile boolean mIsInstalled;
    private Field mLoggingField;
    private final LooperPrinterWrapper mPrinter = new LooperPrinterWrapper();

    public interface LoopCallback {
        void onAfterLoop(String str);

        void onBeforeLoop(String str);
    }

    public static MainLooperGuard getInstance() {
        if (mInstance == null) {
            mInstance = new MainLooperGuard();
        }
        return mInstance;
    }

    public boolean tryInstallGuard() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            if (!this.mIsInstalled) {
                this.mIsInstalled = install();
            }
            return this.mIsInstalled;
        }
        throw new RuntimeException("Must be called on main thread");
    }

    public void addCallback(LoopCallback loopCallback) {
        this.mCallbacks.add(loopCallback);
    }

    public void removeCallback(LoopCallback loopCallback) {
        this.mCallbacks.remove(loopCallback);
    }

    /* access modifiers changed from: private */
    public boolean install() {
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            Object obj = null;
            if (this.mLoggingField == null) {
                this.mLoggingField = Reflector.field(myLooper.getClass(), "mLogging");
            }
            if (this.mLoggingField == null) {
                return false;
            }
            try {
                obj = this.mLoggingField.get(myLooper);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (obj == null) {
                myLooper.setMessageLogging(this.mPrinter);
                return true;
            } else if (obj == this.mPrinter) {
                return true;
            } else {
                if (obj instanceof Printer) {
                    myLooper.setMessageLogging(this.mPrinter.wrap((Printer) obj));
                    return true;
                }
            }
        }
        return false;
    }

    private class InnerHandler extends Handler {
        public static final int MSG_CHECK_INSTALL_LOGGIN = 492900;

        public InnerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 492900) {
                boolean unused = MainLooperGuard.this.install();
            }
        }
    }

    private final class LooperPrinterWrapper implements Printer {
        private boolean isExcuted;
        private boolean mStartPrint;
        private Printer mWrapped;

        private LooperPrinterWrapper() {
            this.mStartPrint = false;
            this.isExcuted = false;
        }

        public Printer wrap(Printer printer) {
            this.mWrapped = printer;
            return this;
        }

        public void println(String str) {
            if (!this.isExcuted && str != null) {
                if (this.mWrapped != null) {
                    this.isExcuted = true;
                    this.mWrapped.println(str);
                    this.isExcuted = false;
                }
                if (!this.mStartPrint && !MainLooperGuard.this.mCheckHandler.hasMessages(InnerHandler.MSG_CHECK_INSTALL_LOGGIN)) {
                    MainLooperGuard.this.mCheckHandler.sendEmptyMessageDelayed(InnerHandler.MSG_CHECK_INSTALL_LOGGIN, 10000);
                }
                if (this.mStartPrint) {
                    this.mStartPrint = false;
                    Iterator it = MainLooperGuard.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        ((LoopCallback) it.next()).onAfterLoop(str);
                    }
                    return;
                }
                this.mStartPrint = true;
                Iterator it2 = MainLooperGuard.this.mCallbacks.iterator();
                while (it2.hasNext()) {
                    ((LoopCallback) it2.next()).onBeforeLoop(str);
                }
            }
        }
    }
}
