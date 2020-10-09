package com.alibaba.android.prefetchx;

import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.tao.image.ImageStrategyConfig;

public class PFMonitor {
    private static final String TEMPLATE_CAUSED_BY = "Caused By %s: %s";
    private static final String TEMPLATE_EXCEPTION_CAUSE = "Exception in thread \"%s\" %s: %s";
    private static final String TEMPLATE_EXCEPTION_STACKTRACE = "at %s.%s(%s:%s)";

    public static class Data {
        public static void success() {
            AppMonitor.Alarm.commitSuccess("PrefetchX", "PrefetchX_Data");
        }

        public static void fail(String str, String str2, Object... objArr) {
            AppMonitor.Alarm.commitFail("PrefetchX", "PrefetchX_Data", PFUtil.s(objArr), str, str2);
        }
    }

    public static class File {
        public static void success() {
            AppMonitor.Alarm.commitSuccess("PrefetchX", "PrefetchX_File");
        }

        public static void fail(String str, String str2, Object... objArr) {
            AppMonitor.Alarm.commitFail("PrefetchX", "PrefetchX_File", PFUtil.s(objArr), str, str2);
        }
    }

    public static class JSModule {
        public static void success(Object... objArr) {
            if (objArr == null) {
                AppMonitor.Alarm.commitSuccess("PrefetchX", "PrefetchX_JSModule");
            } else {
                AppMonitor.Alarm.commitSuccess("PrefetchX", "PrefetchX_JSModule", PFUtil.s(objArr));
            }
        }

        public static void fail(String str, String str2, Object... objArr) {
            AppMonitor.Alarm.commitFail("PrefetchX", "PrefetchX_JSModule", PFUtil.s(objArr), str, str2);
        }
    }

    public static class Image {
        public static void success(Object... objArr) {
            if (objArr == null) {
                AppMonitor.Alarm.commitSuccess("PrefetchX", "PrefetchX_Image");
            } else {
                AppMonitor.Alarm.commitSuccess("PrefetchX", "PrefetchX", PFUtil.s(objArr));
            }
        }

        public static void fail(String str, String str2, Throwable th, Object... objArr) {
            AppMonitor.Alarm.commitFail("PrefetchX", "PrefetchX", PFUtil.s(objArr, PFMonitor.formatExceptionStackTrace(th)), str, str2);
        }

        public static void fail(String str, String str2, Object... objArr) {
            AppMonitor.Alarm.commitFail("PrefetchX", "PrefetchX", PFUtil.s(objArr), str, str2);
        }
    }

    public static String formatExceptionStackTrace(Throwable th) {
        if (th == null) {
            return "exception is null";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(TEMPLATE_EXCEPTION_CAUSE, new Object[]{Thread.currentThread().getName(), th.getClass().getName(), th.getMessage()}));
            sb.append("\n");
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                sb.append(String.format(TEMPLATE_EXCEPTION_STACKTRACE, new Object[]{stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber())}));
                sb.append("\n");
            }
            for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                sb.append(causedByStackTracePart(cause));
            }
            return sb.toString();
        } catch (Throwable unused) {
            String str = "simple exception msg is " + String.format(TEMPLATE_EXCEPTION_CAUSE, new Object[]{Thread.currentThread().getName(), th.getClass().getName(), th.getMessage()});
            AppMonitor.Alarm.commitFail("PrefetchX", "PrefetchX", str, "-52901", "error in formatExceptionStackTrace");
            return str;
        }
    }

    private static String causedByStackTracePart(Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(TEMPLATE_CAUSED_BY, new Object[]{th.getClass().getName(), th.getMessage()}));
        sb.append("\n");
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            String className = stackTraceElement.getClassName();
            if (className != null && (className.contains("taobao") || className.contains("alibaba") || className.contains(ImageStrategyConfig.SHOP) || className.contains("tm"))) {
                sb.append(String.format(TEMPLATE_EXCEPTION_STACKTRACE, new Object[]{className, stackTraceElement.getMethodName(), stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber())}));
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
