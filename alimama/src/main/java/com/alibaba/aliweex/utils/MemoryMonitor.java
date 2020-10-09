package com.alibaba.aliweex.utils;

import android.text.TextUtils;
import android.util.Log;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.ali.watchmem.core.IJavaLowMemoryListener;
import com.ali.watchmem.core.INativeLowMemoryListener;
import com.ali.watchmem.core.WatchmemJavaMemoryManager;
import com.ali.watchmem.core.WatchmemLevel;
import com.ali.watchmem.core.WatchmemNativeMemoryManager;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMonitor {
    private static String TAG = "MemoryMonitor";
    private static boolean deviceClassFound = true;
    /* access modifiers changed from: private */
    public static MemoryStatus javaMemory = MemoryStatus.NORMAL;
    /* access modifiers changed from: private */
    public static String mCurrentMemoryStatus = MemoryStatus.NORMAL.status;
    private static Map<String, MemoryListener> mListeners = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public static MemoryStatus nativeMemory = MemoryStatus.NORMAL;

    public interface MemoryListener {
        void onChange(String str);
    }

    public static void addMemoryListener(String str, MemoryListener memoryListener) {
        if (!TextUtils.isEmpty(str) && memoryListener != null) {
            mListeners.put(str, memoryListener);
        }
    }

    public static void removeListeners(String str) {
        if (!TextUtils.isEmpty(str)) {
            mListeners.remove(str);
        }
    }

    enum MemoryStatus {
        NORMAL("good"),
        HIGH("normal"),
        DANGEROUS("dangerous"),
        CRITICAL("fatal");
        
        String status;

        private MemoryStatus(String str) {
            this.status = str;
        }

        /* access modifiers changed from: package-private */
        public boolean fatal() {
            return equals(CRITICAL);
        }

        /* access modifiers changed from: package-private */
        public boolean dangerous() {
            return equals(DANGEROUS);
        }

        /* access modifiers changed from: package-private */
        public boolean normal() {
            return equals(HIGH);
        }

        /* access modifiers changed from: package-private */
        public boolean good() {
            return equals(NORMAL);
        }
    }

    public static String getMemoryStatus() {
        if (javaMemory.good() && nativeMemory.good()) {
            return MemoryStatus.NORMAL.status;
        }
        if (javaMemory.fatal() || nativeMemory.fatal()) {
            return MemoryStatus.CRITICAL.status;
        }
        if (javaMemory.dangerous() || nativeMemory.dangerous()) {
            return MemoryStatus.DANGEROUS.status;
        }
        if (javaMemory.normal() || nativeMemory.normal()) {
            return MemoryStatus.HIGH.status;
        }
        return MemoryStatus.NORMAL.status;
    }

    /* access modifiers changed from: private */
    public static void memoryChanged() {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            String config = configAdapter.getConfig("android_weex_config_memory", "forbid_memory_change_notify", "false");
            if (!"1".equals(config) && !"true".equals(config)) {
                mCurrentMemoryStatus = getMemoryStatus();
                for (Map.Entry<String, MemoryListener> value : mListeners.entrySet()) {
                    MemoryListener memoryListener = (MemoryListener) value.getValue();
                    if (memoryListener != null) {
                        memoryListener.onChange(mCurrentMemoryStatus);
                    }
                }
            }
        }
    }

    public static void listenMemory() {
        try {
            WatchmemNativeMemoryManager.instance().addNativeLowMemoryListener(new INativeLowMemoryListener() {
                public void onNativeLowMemory(WatchmemLevel watchmemLevel) {
                    if (watchmemLevel == WatchmemLevel.HIGH) {
                        MemoryStatus unused = MemoryMonitor.nativeMemory = MemoryStatus.HIGH;
                    } else if (watchmemLevel == WatchmemLevel.DANGEROUS) {
                        MemoryStatus unused2 = MemoryMonitor.nativeMemory = MemoryStatus.DANGEROUS;
                    } else if (watchmemLevel == WatchmemLevel.CRITICAL) {
                        MemoryStatus unused3 = MemoryMonitor.nativeMemory = MemoryStatus.CRITICAL;
                    } else {
                        MemoryStatus unused4 = MemoryMonitor.nativeMemory = MemoryStatus.NORMAL;
                    }
                    if (!MemoryMonitor.getMemoryStatus().equals(MemoryMonitor.mCurrentMemoryStatus)) {
                        MemoryMonitor.memoryChanged();
                    }
                }
            });
            WatchmemJavaMemoryManager.instance().addJavaLowMemoryListener(new IJavaLowMemoryListener() {
                public void onJavaLowMemory(WatchmemLevel watchmemLevel) {
                    if (watchmemLevel == WatchmemLevel.HIGH) {
                        MemoryStatus unused = MemoryMonitor.javaMemory = MemoryStatus.HIGH;
                    } else if (watchmemLevel == WatchmemLevel.DANGEROUS) {
                        MemoryStatus unused2 = MemoryMonitor.javaMemory = MemoryStatus.DANGEROUS;
                    } else if (watchmemLevel == WatchmemLevel.CRITICAL) {
                        MemoryStatus unused3 = MemoryMonitor.javaMemory = MemoryStatus.CRITICAL;
                    } else {
                        MemoryStatus unused4 = MemoryMonitor.javaMemory = MemoryStatus.NORMAL;
                    }
                    if (!MemoryMonitor.getMemoryStatus().equals(MemoryMonitor.mCurrentMemoryStatus)) {
                        MemoryMonitor.memoryChanged();
                    }
                }
            });
        } catch (Throwable th) {
            Log.e(TAG, th.getMessage());
        }
    }

    public static String getDeviceInfo() {
        AliHAHardware.OutlineInfo outlineInfo;
        String str;
        if (!deviceClassFound) {
            return "unknown";
        }
        try {
            AliHAHardware instance = AliHAHardware.getInstance();
            if (instance == null || (outlineInfo = instance.getOutlineInfo()) == null) {
                return "unknown";
            }
            int i = outlineInfo.deviceLevel;
            if (i != 2) {
                switch (i) {
                    case -1:
                        str = "unknown";
                        break;
                    case 0:
                        str = "high_end";
                        break;
                    default:
                        str = "medium";
                        break;
                }
            } else {
                str = "low_end";
            }
            return str;
        } catch (Throwable unused) {
            deviceClassFound = false;
            return "unknown";
        }
    }
}
