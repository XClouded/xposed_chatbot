package com.ali.telescope.internal.pluginengine;

import androidx.annotation.NonNull;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.INameConverter;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.report.IBeanReport;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.interfaces.OnAccurateBootListener;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.internal.report.BeanReportImpl;
import com.ali.telescope.util.TelescopeLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TelescopeContextImpl implements ITelescopeContext {
    private static final String TAG = "TelescopeContextImpl";
    private HashMap<Integer, Set<String>> eventToPluginList = new HashMap<>();
    private BeanReportImpl mBeanReport = new BeanReportImpl();
    private INameConverter mNameConverter;
    public ArrayList<OnAccurateBootListener> mOnAccurateBootListenerList = new ArrayList<>();
    private boolean pauseAlready;

    public void broadcastEvent(@NonNull final Event event) {
        if (isOnTelescopeMainThread()) {
            broadcastEventInner(event);
            broadcastEventOuter(event);
            return;
        }
        Loopers.getTelescopeHandler().post(new Runnable() {
            public void run() {
                TelescopeContextImpl.this.broadcastEventInner(event);
                TelescopeContextImpl.this.broadcastEventOuter(event);
            }
        });
    }

    public void registerBroadcast(final int i, @NonNull final String str) {
        if (isOnTelescopeMainThread()) {
            registerBroadcastInner(i, str);
        } else {
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    TelescopeContextImpl.this.registerBroadcastInner(i, str);
                }
            });
        }
    }

    public void unregisterBroadcast(final int i, @NonNull final String str) {
        if (isOnTelescopeMainThread()) {
            unregisterBroadcastInner(i, str);
        } else {
            Loopers.getTelescopeHandler().post(new Runnable() {
                public void run() {
                    TelescopeContextImpl.this.unregisterBroadcastInner(i, str);
                }
            });
        }
    }

    public boolean requestPause(int i, @NonNull String str, int i2) {
        boolean z;
        if (isOnTelescopeMainThread()) {
            return requestPauseInner(i, str, i2);
        }
        boolean[] zArr = new boolean[2];
        final int i3 = i;
        final String str2 = str;
        final int i4 = i2;
        final boolean[] zArr2 = zArr;
        Loopers.getTelescopeHandler().post(new Runnable() {
            public void run() {
                boolean access$400 = TelescopeContextImpl.this.requestPauseInner(i3, str2, i4);
                synchronized (TelescopeContextImpl.this) {
                    zArr2[0] = access$400;
                    zArr2[1] = true;
                    TelescopeContextImpl.this.notifyAll();
                }
            }
        });
        synchronized (this) {
            while (!zArr[1]) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            z = zArr[0];
        }
        return z;
    }

    public boolean requestResume(int i, @NonNull String str, int i2) {
        boolean z;
        if (isOnTelescopeMainThread()) {
            return requestResumeInner(i, str, i2);
        }
        boolean[] zArr = new boolean[2];
        final int i3 = i;
        final String str2 = str;
        final int i4 = i2;
        final boolean[] zArr2 = zArr;
        Loopers.getTelescopeHandler().post(new Runnable() {
            public void run() {
                boolean access$500 = TelescopeContextImpl.this.requestResumeInner(i3, str2, i4);
                synchronized (TelescopeContextImpl.this) {
                    zArr2[0] = access$500;
                    zArr2[1] = true;
                    TelescopeContextImpl.this.notifyAll();
                }
            }
        });
        synchronized (this) {
            while (!zArr[1]) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            z = zArr[0];
        }
        return z;
    }

    public boolean isMatchedPluginOnPauseState(final int i, final String str) {
        boolean z;
        if (isOnTelescopeMainThread()) {
            return isMatchedPluginOnPauseStateInnner(i, str);
        }
        final boolean[] zArr = new boolean[2];
        Loopers.getTelescopeHandler().post(new Runnable() {
            public void run() {
                boolean access$600 = TelescopeContextImpl.this.isMatchedPluginOnPauseStateInnner(i, str);
                synchronized (TelescopeContextImpl.this) {
                    zArr[0] = access$600;
                    zArr[1] = true;
                    TelescopeContextImpl.this.notifyAll();
                }
            }
        });
        synchronized (this) {
            while (!zArr[1]) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            z = zArr[0];
        }
        return z;
    }

    public void setNameConverter(INameConverter iNameConverter) {
        this.mNameConverter = iNameConverter;
    }

    public INameConverter getNameConverter() {
        return this.mNameConverter;
    }

    public IBeanReport getBeanReport() {
        return this.mBeanReport;
    }

    /* access modifiers changed from: private */
    public boolean isMatchedPluginOnPauseStateInnner(int i, @NonNull String str) {
        if (!this.pauseAlready) {
            return false;
        }
        for (Plugin next : PluginManager.getAllPlugin()) {
            if (next != null && !next.pluginID.equals(str) && next.isMatchBoundType(i) && !next.isPaused()) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean requestPauseInner(int i, @NonNull String str, int i2) {
        if (!this.pauseAlready) {
            this.pauseAlready = true;
            for (Plugin next : PluginManager.getAllPlugin()) {
                if (next != null && !next.pluginID.equals(str) && next.isMatchBoundType(i)) {
                    next.onPause(i, i2);
                }
            }
            return true;
        }
        TelescopeLog.w(TAG, "one pause request is already exist, may forget call resume....");
        return false;
    }

    /* access modifiers changed from: private */
    public boolean requestResumeInner(int i, @NonNull String str, int i2) {
        if (this.pauseAlready) {
            this.pauseAlready = false;
            for (Plugin next : PluginManager.getAllPlugin()) {
                if (next != null && !next.pluginID.equals(str) && next.isMatchBoundType(i)) {
                    next.onResume(i, i2);
                }
            }
            return true;
        }
        TelescopeLog.w(TAG, "none pause is exist...");
        return false;
    }

    /* access modifiers changed from: private */
    public void broadcastEventInner(@NonNull Event event) {
        Set<String> set = this.eventToPluginList.get(Integer.valueOf(event.eventType));
        if (set != null) {
            for (String pluginByPluginId : set) {
                Plugin pluginByPluginId2 = PluginManager.getPluginByPluginId(pluginByPluginId);
                if (pluginByPluginId2 != null) {
                    pluginByPluginId2.onEvent(event.eventType, event);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void broadcastEventOuter(Event event) {
        HashMap hashMap = new HashMap();
        hashMap.put("appKey", AppConfig.appKey);
        hashMap.put("versionName", AppConfig.versionName);
        hashMap.put("packageName", AppConfig.packageName);
        hashMap.put("utdid", AppConfig.utdid);
        hashMap.put("isRooted", String.valueOf(DeviceInfoManager.instance().getIsRooted()));
        hashMap.put("isEmulator", String.valueOf(DeviceInfoManager.instance().isEmulator()));
        hashMap.put("mobileBrand", String.valueOf(DeviceInfoManager.instance().getMobileBrand()));
        hashMap.put("mobileModel", String.valueOf(DeviceInfoManager.instance().getMobileModel()));
        hashMap.put("apiLevel", String.valueOf(DeviceInfoManager.instance().getApiLevel()));
        hashMap.put("storeTotalSize", String.valueOf(DeviceInfoManager.instance().getStoreTotalSize()));
        hashMap.put("deviceTotalMemory", String.valueOf(DeviceInfoManager.instance().getDeviceTotalMemory()));
        hashMap.put("memoryThreshold", String.valueOf(DeviceInfoManager.instance().getMemoryThreshold()));
        hashMap.put("cpuModel", String.valueOf(DeviceInfoManager.instance().getCpuModel()));
        hashMap.put("cpuBrand", String.valueOf(DeviceInfoManager.instance().getCpuBrand()));
        hashMap.put("cpuArch", String.valueOf(DeviceInfoManager.instance().getCpuArch()));
        hashMap.put("cpuProcessCount", String.valueOf(DeviceInfoManager.instance().getCpuProcessCount()));
        hashMap.put("cpuFreqArray", Arrays.toString(DeviceInfoManager.instance().getCpuFreqArray()));
        hashMap.put("cpuMaxFreq", String.valueOf(DeviceInfoManager.instance().getCpuMaxFreq()));
        hashMap.put("cpuMinFreq", String.valueOf(DeviceInfoManager.instance().getCpuMinFreq()));
        hashMap.put("gpuMaxFreq", String.valueOf(DeviceInfoManager.instance().getGpuMaxFreq()));
        hashMap.put("screenWidth", String.valueOf(DeviceInfoManager.instance().getScreenWidth()));
        hashMap.put("screenHeight", String.valueOf(DeviceInfoManager.instance().getScreenHeight()));
        hashMap.put("screenDensity", String.valueOf(DeviceInfoManager.instance().getScreenDensity()));
        if (event.eventType == 3 && this.mOnAccurateBootListenerList.size() != 0) {
            Iterator<OnAccurateBootListener> it = this.mOnAccurateBootListenerList.iterator();
            while (it.hasNext()) {
                it.next().OnAccurateBootFinished(hashMap);
            }
        }
    }

    /* access modifiers changed from: private */
    public void unregisterBroadcastInner(int i, @NonNull String str) {
        Set set = this.eventToPluginList.get(Integer.valueOf(i));
        if (set != null) {
            set.remove(str);
        }
    }

    /* access modifiers changed from: private */
    public void registerBroadcastInner(int i, @NonNull String str) {
        Set set = this.eventToPluginList.get(Integer.valueOf(i));
        if (set == null) {
            set = new HashSet();
            this.eventToPluginList.put(Integer.valueOf(i), set);
        }
        set.add(str);
    }

    private boolean isOnTelescopeMainThread() {
        return Thread.currentThread() == Loopers.getTelescopeLooper().getThread();
    }

    public void addOnAccurateBootListener(OnAccurateBootListener onAccurateBootListener) {
        this.mOnAccurateBootListenerList.add(onAccurateBootListener);
    }
}
