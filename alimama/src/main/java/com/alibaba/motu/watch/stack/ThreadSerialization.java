package com.alibaba.motu.watch.stack;

import android.util.Log;
import com.alibaba.motu.watch.WatchConfig;
import java.lang.Thread;
import java.util.Map;

public class ThreadSerialization {
    public String serialization(Map<Thread, StackTraceElement[]> map, boolean z) {
        Thread thread;
        if (map == null) {
            return "";
        }
        try {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            for (Map.Entry next : map.entrySet()) {
                if (!(next == null || (thread = (Thread) next.getKey()) == null)) {
                    String name = thread.getName();
                    if (name == null || !name.equals("MainLooper_Monitor")) {
                        int priority = thread.getPriority();
                        long id = thread.getId();
                        String str = "";
                        Thread.State state = thread.getState();
                        if (state != null) {
                            if (z || !state.name().equals("RUNNABLE")) {
                                str = state.name();
                            }
                        }
                        String str2 = "";
                        ThreadGroup threadGroup = thread.getThreadGroup();
                        if (threadGroup != null) {
                            str2 = threadGroup.getName();
                            if (str2.equals("system")) {
                            }
                        }
                        String name2 = thread.getClass().getName();
                        String str3 = "";
                        ClassLoader contextClassLoader = thread.getContextClassLoader();
                        if (contextClassLoader != null) {
                            str3 = contextClassLoader.toString();
                        }
                        stringBuffer.append(String.format("name:%s prio:%d tid:%d \n|state:%s \n|group:%s \n|class:%s \n|classLoader:%s\n", new Object[]{name, Integer.valueOf(priority), Long.valueOf(id), str, str2, name2, str3}));
                        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) next.getValue();
                        if (stackTraceElementArr != null) {
                            stringBuffer.append("|stackTrace:\n ");
                            int length = stackTraceElementArr.length;
                            int i2 = 0;
                            int i3 = 0;
                            while (true) {
                                if (i2 >= length) {
                                    break;
                                }
                                StackTraceElement stackTraceElement = stackTraceElementArr[i2];
                                if (stackTraceElement != null) {
                                    stringBuffer.append(String.format("at %s\n", new Object[]{stackTraceElement.toString()}));
                                }
                                i3++;
                                if (i3 >= 64) {
                                    break;
                                }
                                i2++;
                            }
                        }
                        stringBuffer.append("\n");
                    }
                }
                i++;
                if (i >= 200) {
                    break;
                }
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            Log.e(WatchConfig.TAG, "serialization failed.", e);
            return "";
        }
    }
}
