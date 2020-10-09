package com.alibaba.motu.watch.stack;

import android.os.Looper;
import com.alibaba.motu.watch.stack.ThreadMsg$$;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ThreadMsg extends Error {
    private static final long serialVersionUID = 1;
    private final Map<Thread, StackTraceElement[]> _stackTraces;

    private ThreadMsg(ThreadMsg$$._Thread _thread, Map<Thread, StackTraceElement[]> map) {
        super("Application Not Responding", _thread);
        this._stackTraces = map;
    }

    public Map<Thread, StackTraceElement[]> getStackTraces() {
        return this._stackTraces;
    }

    public Throwable fillInStackTrace() {
        setStackTrace(new StackTraceElement[0]);
        return this;
    }

    public static ThreadMsg New(String str, boolean z) {
        final Thread thread = Looper.getMainLooper().getThread();
        TreeMap treeMap = new TreeMap(new Comparator<Thread>() {
            public int compare(Thread thread, Thread thread2) {
                if (thread == thread2) {
                    return 0;
                }
                if (thread == thread) {
                    return 1;
                }
                if (thread2 == thread) {
                    return -1;
                }
                return thread2.getName().compareTo(thread.getName());
            }
        });
        for (Map.Entry next : Thread.getAllStackTraces().entrySet()) {
            if (next.getKey() == thread || (((Thread) next.getKey()).getName().startsWith(str) && (z || ((StackTraceElement[]) next.getValue()).length > 0))) {
                treeMap.put(next.getKey(), next.getValue());
            }
        }
        ThreadMsg$$._Thread _thread = null;
        for (Map.Entry entry : treeMap.entrySet()) {
            ThreadMsg$$ threadMsg$$ = new ThreadMsg$$(((Thread) entry.getKey()).getName(), (StackTraceElement[]) entry.getValue());
            threadMsg$$.getClass();
            _thread = new ThreadMsg$$._Thread(_thread);
        }
        return new ThreadMsg(_thread, treeMap);
    }

    public static ThreadMsg NewMainOnly() {
        Thread thread = Looper.getMainLooper().getThread();
        StackTraceElement[] stackTrace = thread.getStackTrace();
        HashMap hashMap = new HashMap(1);
        hashMap.put(thread, stackTrace);
        ThreadMsg$$ threadMsg$$ = new ThreadMsg$$(thread.getName(), stackTrace);
        threadMsg$$.getClass();
        return new ThreadMsg(new ThreadMsg$$._Thread((ThreadMsg$$._Thread) null), hashMap);
    }

    public static Map<Thread, StackTraceElement[]> getCurrentThread() {
        Thread currentThread = Thread.currentThread();
        if (currentThread == null) {
            return null;
        }
        HashMap hashMap = new HashMap(1);
        StackTraceElement[] stackTrace = currentThread.getStackTrace();
        if (stackTrace == null) {
            return null;
        }
        hashMap.put(currentThread, stackTrace);
        return hashMap;
    }

    public static Map<Thread, StackTraceElement[]> getMainThread() {
        Thread thread = Looper.getMainLooper().getThread();
        if (thread == null) {
            return null;
        }
        HashMap hashMap = new HashMap(1);
        StackTraceElement[] stackTrace = thread.getStackTrace();
        if (stackTrace == null) {
            return null;
        }
        hashMap.put(thread, stackTrace);
        return hashMap;
    }
}
