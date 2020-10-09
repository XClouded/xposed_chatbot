package com.taobao.monitor.impl.util;

import android.os.Process;
import java.io.File;

public class ProcessUtils {
    private ProcessUtils() {
    }

    public static long getProcessStartSystemTime() {
        File file = new File("/proc/" + Process.myPid() + "/comm");
        if (file.exists()) {
            return file.lastModified();
        }
        return -1;
    }
}
