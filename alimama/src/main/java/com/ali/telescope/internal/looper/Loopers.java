package com.ali.telescope.internal.looper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class Loopers {

    private static class TelescopeMainSingleton {
        public static Handler sHandler = new Handler(sLooper);
        public static Looper sLooper;

        private TelescopeMainSingleton() {
        }

        static {
            HandlerThread handlerThread = new HandlerThread("Telescope_Main_Thread");
            handlerThread.start();
            sLooper = handlerThread.getLooper();
        }
    }

    private static class ReportSingleton {
        public static Handler sHandler = new Handler(sLooper);
        public static Looper sLooper;

        private ReportSingleton() {
        }

        static {
            HandlerThread handlerThread = new HandlerThread("Telescope_Report_Thread");
            handlerThread.start();
            sLooper = handlerThread.getLooper();
        }
    }

    public static Looper getTelescopeLooper() {
        return TelescopeMainSingleton.sLooper;
    }

    public static Handler getTelescopeHandler() {
        return TelescopeMainSingleton.sHandler;
    }

    public static Looper getReportLooper() {
        return ReportSingleton.sLooper;
    }

    public static Handler getReportHandler() {
        return ReportSingleton.sHandler;
    }

    private static class UISingleton {
        public static Handler sUiHandler = new Handler(Looper.getMainLooper());

        private UISingleton() {
        }
    }

    public static Handler getUiHandler() {
        return UISingleton.sUiHandler;
    }
}
