package com.taobao.tcommon.core;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.TypedValue;
import com.taobao.tcommon.log.FLog;
import com.taobao.weex.analyzer.Config;
import java.io.File;
import java.io.FileFilter;

public class RuntimeUtil {
    private static final FileFilter CPU_FILTER = new FileFilter() {
        public boolean accept(File file) {
            String name = file.getName();
            if (!name.startsWith(Config.TYPE_CPU)) {
                return false;
            }
            for (int i = 3; i < name.length(); i++) {
                if (name.charAt(i) < '0' || name.charAt(i) > '9') {
                    return false;
                }
            }
            return true;
        }
    };
    public static Integer sCpuCores;

    public static int getCpuCores() {
        if (sCpuCores == null) {
            if (Build.VERSION.SDK_INT <= 10) {
                sCpuCores = 1;
            } else {
                try {
                    sCpuCores = 1;
                    File[] listFiles = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER);
                    if (listFiles != null) {
                        sCpuCores = Integer.valueOf(listFiles.length);
                    }
                } catch (Exception unused) {
                    sCpuCores = 1;
                }
            }
        }
        return sCpuCores.intValue();
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().equals(Thread.currentThread());
    }

    public static String getClassShortName(Class cls) {
        String name = cls.getName();
        return name != null ? name.substring(name.lastIndexOf(46) + 1) : "";
    }

    public static boolean isPictureResource(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        try {
            context.getResources().getValue(i, typedValue, true);
        } catch (Exception e) {
            FLog.e("TCommon", "get resources type value error=%s", e);
        }
        if ((typedValue.type != 1 && typedValue.type != 3) || typedValue.string == null) {
            return false;
        }
        String charSequence = typedValue.string.toString();
        if (charSequence.endsWith(".png") || charSequence.endsWith(".jpg") || charSequence.endsWith(".webp")) {
            return true;
        }
        return false;
    }
}
