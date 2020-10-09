package org.slf4j.impl;

import android.util.Log;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

class AndroidLoggerAdapter extends MarkerIgnoringBase {
    private static final long serialVersionUID = -1227274521521287937L;

    AndroidLoggerAdapter(String str) {
        this.name = str;
    }

    public boolean isTraceEnabled() {
        return isLoggable(2);
    }

    public void trace(String str) {
        log(2, str, (Throwable) null);
    }

    public void trace(String str, Object obj) {
        formatAndLog(2, str, obj);
    }

    public void trace(String str, Object obj, Object obj2) {
        formatAndLog(2, str, obj, obj2);
    }

    public void trace(String str, Object... objArr) {
        formatAndLog(2, str, objArr);
    }

    public void trace(String str, Throwable th) {
        log(2, str, th);
    }

    public boolean isDebugEnabled() {
        return isLoggable(3);
    }

    public void debug(String str) {
        log(3, str, (Throwable) null);
    }

    public void debug(String str, Object obj) {
        formatAndLog(3, str, obj);
    }

    public void debug(String str, Object obj, Object obj2) {
        formatAndLog(3, str, obj, obj2);
    }

    public void debug(String str, Object... objArr) {
        formatAndLog(3, str, objArr);
    }

    public void debug(String str, Throwable th) {
        log(2, str, th);
    }

    public boolean isInfoEnabled() {
        return isLoggable(4);
    }

    public void info(String str) {
        log(4, str, (Throwable) null);
    }

    public void info(String str, Object obj) {
        formatAndLog(4, str, obj);
    }

    public void info(String str, Object obj, Object obj2) {
        formatAndLog(4, str, obj, obj2);
    }

    public void info(String str, Object... objArr) {
        formatAndLog(4, str, objArr);
    }

    public void info(String str, Throwable th) {
        log(4, str, th);
    }

    public boolean isWarnEnabled() {
        return isLoggable(5);
    }

    public void warn(String str) {
        log(5, str, (Throwable) null);
    }

    public void warn(String str, Object obj) {
        formatAndLog(5, str, obj);
    }

    public void warn(String str, Object obj, Object obj2) {
        formatAndLog(5, str, obj, obj2);
    }

    public void warn(String str, Object... objArr) {
        formatAndLog(5, str, objArr);
    }

    public void warn(String str, Throwable th) {
        log(5, str, th);
    }

    public boolean isErrorEnabled() {
        return isLoggable(6);
    }

    public void error(String str) {
        log(6, str, (Throwable) null);
    }

    public void error(String str, Object obj) {
        formatAndLog(6, str, obj);
    }

    public void error(String str, Object obj, Object obj2) {
        formatAndLog(6, str, obj, obj2);
    }

    public void error(String str, Object... objArr) {
        formatAndLog(6, str, objArr);
    }

    public void error(String str, Throwable th) {
        log(6, str, th);
    }

    private void formatAndLog(int i, String str, Object... objArr) {
        if (isLoggable(i)) {
            FormattingTuple arrayFormat = MessageFormatter.arrayFormat(str, objArr);
            logInternal(i, arrayFormat.getMessage(), arrayFormat.getThrowable());
        }
    }

    private void log(int i, String str, Throwable th) {
        if (isLoggable(i)) {
            logInternal(i, str, th);
        }
    }

    private boolean isLoggable(int i) {
        return Log.isLoggable(this.name, i);
    }

    private void logInternal(int i, String str, Throwable th) {
        if (th != null) {
            str = str + 10 + Log.getStackTraceString(th);
        }
        Log.println(i, this.name, str);
    }
}
