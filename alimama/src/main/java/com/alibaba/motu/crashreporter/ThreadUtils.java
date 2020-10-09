package com.alibaba.motu.crashreporter;

import com.uc.webview.export.extension.UCCore;
import java.util.HashMap;
import java.util.List;

public class ThreadUtils {
    public static String getThreadInfos() {
        try {
            List<ThreadCpuInfo> doCpuCheck = ThreadCpuInfoManager.instance().doCpuCheck();
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            if (doCpuCheck != null) {
                for (ThreadCpuInfo next : doCpuCheck) {
                    String threadName = next.getThreadName();
                    Integer num = (Integer) hashMap.get(threadName);
                    if (num == null) {
                        hashMap.put(threadName, 1);
                    } else {
                        hashMap.put(threadName, Integer.valueOf(num.intValue() + 1));
                    }
                    sb.append(next.toString());
                    sb.append("\n");
                }
            }
            return hashMap.toString() + "\n" + sb.toString();
        } catch (Throwable unused) {
            return UCCore.EVENT_EXCEPTION;
        }
    }
}
