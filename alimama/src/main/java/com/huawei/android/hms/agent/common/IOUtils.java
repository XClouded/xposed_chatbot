package com.huawei.android.hms.agent.common;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
                HMSAgentLog.d("close fail");
            }
        }
    }
}
