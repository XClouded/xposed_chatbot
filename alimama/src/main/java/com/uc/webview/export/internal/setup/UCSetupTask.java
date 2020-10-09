package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.os.SystemClock;
import com.taobao.weex.BuildConfig;
import com.uc.webview.export.Build;
import com.uc.webview.export.annotations.Reflection;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.UCSetupTask;
import com.uc.webview.export.internal.setup.ah;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.k;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public abstract class UCSetupTask<RETURN_TYPE extends UCSetupTask<RETURN_TYPE, CALLBACK_TYPE>, CALLBACK_TYPE extends UCSetupTask<RETURN_TYPE, CALLBACK_TYPE>> extends BaseSetupTask<RETURN_TYPE, CALLBACK_TYPE> {
    private static UCMRunningInfo b;
    private static UCSetupTask d;
    private static UCAsyncTask e;
    private static int f;
    private static boolean g = true;
    private static boolean h = false;
    protected static final List<UCSetupTask> sTotalSetupTasks = new ArrayList(2);
    private UCMRunningInfo a;
    private UCMRepairInfo c;
    private String i;

    /* access modifiers changed from: protected */
    public ah getSetupCrashImprover(Context context, String str) {
        if (k.a(str)) {
            return null;
        }
        this.i = str;
        return ah.a.a(this, context, str);
    }

    /* access modifiers changed from: protected */
    public void resetCrashFlag() {
        if (this.i != null) {
            ah.a.a(this, getContext(), this.i).a();
        }
    }

    /* access modifiers changed from: protected */
    public final String getCrashCode() {
        return this.i != null ? ah.a.a(this, getContext(), this.i).a : "";
    }

    public static synchronized UCAsyncTask getRoot() {
        UCAsyncTask uCAsyncTask;
        synchronized (UCSetupTask.class) {
            if (e == null) {
                e = new bv(Integer.valueOf(f), Boolean.valueOf(g)).onEvent("start", new bu()).onEvent(UCCore.EVENT_DIE, new bt());
            }
            uCAsyncTask = e;
        }
        return uCAsyncTask;
    }

    /* access modifiers changed from: protected */
    public final void setTotalLoadedUCM(UCMRunningInfo uCMRunningInfo) {
        b = uCMRunningInfo;
    }

    public static UCMRunningInfo getTotalLoadedUCM() {
        return b;
    }

    @Reflection
    public static Class<?> classForName(String str) throws ClassNotFoundException {
        ClassLoader classLoader;
        UCMRunningInfo totalLoadedUCM = getTotalLoadedUCM();
        if (totalLoadedUCM == null) {
            classLoader = null;
        } else {
            classLoader = totalLoadedUCM.classLoader;
        }
        if (classLoader == null) {
            return Class.forName(str);
        }
        return Class.forName(str, true, classLoader);
    }

    public static boolean isSetupThread() {
        return getRoot().inThread();
    }

    public static void resumeAll() {
        synchronized (sTotalSetupTasks) {
            for (int i2 = 0; i2 < sTotalSetupTasks.size(); i2++) {
                sTotalSetupTasks.get(i2).resume();
            }
        }
    }

    public UCSetupTask() {
        synchronized (sTotalSetupTasks) {
            sTotalSetupTasks.add(this);
        }
    }

    public synchronized RETURN_TYPE start() {
        if (getParent() == null) {
            b.a(328);
            Integer num = (Integer) this.mOptions.get(UCCore.OPTION_SETUP_THREAD_PRIORITY);
            if (num != null) {
                setRootTaskPriority(num.intValue());
            }
            boolean z = (Boolean) this.mOptions.get(UCCore.OPTION_SETUP_CREATE_THREAD);
            if (!k.a((String) this.mOptions.get(UCCore.OPTION_UCM_UPD_URL))) {
                z = true;
            }
            if (z != null) {
                setEnableRootTaskCreateThread(z);
            }
            b.a(329);
            UCAsyncTask root = getRoot();
            setParent(root);
            b.a(330);
            RETURN_TYPE return_type = (UCSetupTask) super.start();
            root.start();
            return return_type;
        }
        return (UCSetupTask) super.start();
    }

    /* access modifiers changed from: protected */
    public void setupGlobalOnce() {
        int i2;
        String str;
        Log.d("UCSetupTask", "setupGlobalOnce");
        b.a(292);
        if (!h) {
            h = true;
            Context applicationContext = getContext().getApplicationContext();
            String str2 = (String) getOption(UCCore.OPTION_PRIVATE_DATA_DIRECTORY_SUFFIX);
            if (k.a(str2)) {
                str2 = "0";
            }
            UCCore.setGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION, str2);
            Boolean a2 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_SDK_INTERNATIONAL_ENV);
            if (a2 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_SDK_INTERNATIONAL_ENV, a2);
            }
            Integer num = (Integer) this.mOptions.get(UCCore.OPTION_WEBVIEW_POLICY);
            UCCore.setGlobalOption(UCCore.OPTION_WEBVIEW_POLICY, Integer.valueOf(num != null ? num.intValue() : 1));
            Boolean a3 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_USE_SDK_SETUP);
            if (a3 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_USE_SDK_SETUP, a3);
            }
            Boolean a4 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_MULTI_CORE_TYPE);
            if (a4 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_MULTI_CORE_TYPE, a4);
            }
            Boolean a5 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_HARDWARE_ACCELERATED);
            if (a5 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_HARDWARE_ACCELERATED, Integer.valueOf(a5.booleanValue() ? 1 : 0));
            }
            Boolean a6 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_VIDEO_HARDWARE_ACCELERATED);
            if (a6 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_VIDEO_HARDWARE_ACCELERATED, Integer.valueOf(a6.booleanValue() ? 1 : 0));
            }
            Boolean a7 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_GRANT_ALL_BUILDS);
            if (applicationContext.getPackageName().equals("com.ucsdk.cts") || (a7 != null && a7.booleanValue())) {
                UCCore.setGlobalOption(UCCore.OPTION_GRANT_ALL_BUILDS, true);
            }
            Integer num2 = (Integer) this.mOptions.get(UCCore.OPTION_CONNECTION_CONNECT_TIMEOUT);
            if (num2 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_CONNECTION_CONNECT_TIMEOUT, num2);
            }
            Integer num3 = (Integer) this.mOptions.get(UCCore.OPTION_CONNECTION_READ_TIMEOUT);
            if (num3 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_CONNECTION_READ_TIMEOUT, num3);
            }
            Boolean a8 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_EXACT_OLD_KERNEL_CHECK);
            if (a8 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_EXACT_OLD_KERNEL_CHECK, a8);
            }
            Boolean a9 = k.a((ConcurrentHashMap<String, Object>) this.mOptions, UCCore.OPTION_EXACT_LAST_MODIFIED_CHECK);
            if (a9 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_EXACT_LAST_MODIFIED_CHECK, a9);
            }
            Integer num4 = (Integer) this.mOptions.get(UCCore.OPTION_WEBVIEW_POLICY_WAIT_MILLIS);
            if (num4 != null) {
                UCCore.setGlobalOption(UCCore.OPTION_WEBVIEW_POLICY_WAIT_MILLIS, num4);
            }
            Object obj = this.mOptions.get(UCCore.OPTION_UC_PLAYER_ROOT);
            if (obj != null) {
                UCCore.setGlobalOption(UCCore.OPTION_UC_PLAYER_ROOT, obj.toString());
            }
            boolean z = this.mOptions.get(UCCore.OPTION_USE_UC_PLAYER);
            if (z == null) {
                z = true;
            }
            UCCore.setGlobalOption(UCCore.OPTION_USE_UC_PLAYER, z);
            Integer num5 = (Integer) this.mOptions.get(UCCore.OPTION_WEBVIEW_MULTI_PROCESS);
            UCCore.setGlobalOption(UCCore.OPTION_WEBVIEW_MULTI_PROCESS, Integer.valueOf(num5 != null ? num5.intValue() : 0));
            Integer num6 = (Integer) this.mOptions.get(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_FALLBACK_TIMEOUT);
            UCCore.setGlobalOption(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_FALLBACK_TIMEOUT, Integer.valueOf(num6 != null ? num6.intValue() : 0));
            boolean z2 = this.mOptions.get(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SERVICE_SPEEDUP);
            if (z2 == null) {
                z2 = false;
            }
            UCCore.setGlobalOption(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SERVICE_SPEEDUP, z2);
            UCCore.setGlobalOption(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SECCOMP, Boolean.valueOf(k.b(this.mOptions.get(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SECCOMP))));
            Integer num7 = (Integer) this.mOptions.get(UCCore.OPTION_GPU_PROCESS_MODE);
            UCCore.setGlobalOption(UCCore.OPTION_GPU_PROCESS_MODE, Integer.valueOf(num7 != null ? num7.intValue() : 0));
            if (((Integer) this.mOptions.get(UCCore.OPTION_STARTUP_POLICY)) == null) {
                this.mOptions.put(UCCore.OPTION_STARTUP_POLICY, 16);
            }
            Long l = (Long) getOption(UCCore.OPTION_APP_STARTUP_TIME);
            if (l != null) {
                UCCore.setGlobalOption(UCCore.STARTUP_ELAPSE_BEETWEEN_UC_INIT_AND_APP, Long.valueOf(SystemClock.elapsedRealtime() - l.longValue()));
            }
            Integer num8 = (Integer) getOption(UCCore.OPTION_APP_STARTUP_OPPORTUNITY);
            if (num8 == null) {
                i2 = 0;
            } else {
                i2 = num8.intValue();
            }
            UCCore.setGlobalOption(UCCore.OPTION_APP_STARTUP_OPPORTUNITY, Integer.valueOf(i2));
            if (!k.a(getInitType())) {
                UCCore.setGlobalOption(UCCore.OPTION_BUSINESS_INIT_TYPE, getInitType());
            }
            UCElapseTime uCElapseTime = new UCElapseTime();
            if (!Log.enableUCLogger() && getOption(UCCore.OPTION_LOG_CONFIG) != null) {
                Object[] objArr = (Object[]) getOption(UCCore.OPTION_LOG_CONFIG);
                Log.d("UCSetupTask", "setPrintLogBaseOnConfig " + objArr);
                if (objArr != null && objArr.length == 5) {
                    Log.setPrintLog(((Boolean) objArr[0]).booleanValue(), objArr);
                }
            }
            UCCyclone.enableDebugLog = Log.enableUCLogger();
            b.a(226, uCElapseTime.getMilis());
            UCLogger create = UCLogger.create("d", "UCSetupTask");
            if (create != null) {
                Object[] objArr2 = (Object[]) this.mOptions.get(UCCore.OPTION_LOG_CONFIG);
                StringBuilder sb = new StringBuilder("setupGlobalOnce: log_conf=");
                if (objArr2 == null) {
                    str = BuildConfig.buildJavascriptFrameworkVersion;
                } else {
                    str = Arrays.toString(objArr2);
                }
                sb.append(str);
                create.print2Cache(sb.toString(), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: PRIVATE_DATA_DIRECTORY_SUFFIX=" + d.a().a(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: sdk_international_env=" + d.a().a(UCCore.OPTION_SDK_INTERNATIONAL_ENV), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: WEBVIEW_POLICY=" + d.a().a(UCCore.OPTION_WEBVIEW_POLICY), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: sdk_setup=" + d.a().b(UCCore.OPTION_USE_SDK_SETUP), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: MULTI_CORE_TYPE=" + d.a().b(UCCore.OPTION_MULTI_CORE_TYPE), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: AC=" + d.a().a(UCCore.OPTION_HARDWARE_ACCELERATED), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: VIDEO_AC=" + d.a().a(UCCore.OPTION_VIDEO_HARDWARE_ACCELERATED), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: grant_all_builds=" + d.a().b(UCCore.OPTION_GRANT_ALL_BUILDS), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: conn_to=" + d.a().a(UCCore.OPTION_CONNECTION_CONNECT_TIMEOUT), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: read_to=" + d.a().a(UCCore.OPTION_CONNECTION_READ_TIMEOUT), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: exact_old=" + d.a().b(UCCore.OPTION_EXACT_OLD_KERNEL_CHECK), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: exact_mod=" + d.a().b(UCCore.OPTION_EXACT_LAST_MODIFIED_CHECK), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: wait_fallback_sys=" + d.a().a(UCCore.OPTION_WEBVIEW_POLICY_WAIT_MILLIS), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: ucPlayerRoot=" + d.a().a(UCCore.OPTION_UC_PLAYER_ROOT), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: ucPlayer=" + d.a().b(UCCore.OPTION_USE_UC_PLAYER), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: webview_multi_process=" + d.a().a(UCCore.OPTION_WEBVIEW_MULTI_PROCESS), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: webview_multi_process_fallback_timeout=" + d.a().a(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_FALLBACK_TIMEOUT), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: webview_multi_process_enable_service_speedup=" + d.a().b(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SERVICE_SPEEDUP), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: ucm_multi_process_enable_seccomp=" + d.a().b(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SECCOMP), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: gpu_process_mode=" + d.a().a(UCCore.OPTION_GPU_PROCESS_MODE), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: ucbs version:" + Build.Version.NAME + "_" + Build.Version.BUILD_SERIAL, new Throwable[0]);
                StringBuilder sb2 = new StringBuilder("setupGlobalOnce: preheat init type: ");
                sb2.append(getInitType());
                create.print2Cache(sb2.toString(), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: appStartupOpportunity: " + UCCore.getGlobalOption(UCCore.OPTION_APP_STARTUP_OPPORTUNITY), new Throwable[0]);
                create.print2Cache("setupGlobalOnce: appStartupTime: " + UCCore.getGlobalOption(UCCore.STARTUP_ELAPSE_BEETWEEN_UC_INIT_AND_APP), new Throwable[0]);
            }
            b.a(293);
        }
    }

    /* access modifiers changed from: protected */
    public final void setLoadedUCM(UCMRunningInfo uCMRunningInfo) {
        this.a = uCMRunningInfo;
    }

    @Reflection
    public final UCMRepairInfo getRepairInfo() {
        return this.c;
    }

    /* access modifiers changed from: protected */
    public final void setRepairInfo(UCMRepairInfo uCMRepairInfo) {
        this.c = uCMRepairInfo;
    }

    @Reflection
    public final UCMRunningInfo getLoadedUCM() {
        return this.a;
    }

    protected static void setRootTaskPriority(int i2) {
        f = i2;
    }

    protected static void setEnableRootTaskCreateThread(Boolean bool) {
        g = bool.booleanValue();
    }

    public static UCSetupTask getDefault() {
        return d;
    }

    /* access modifiers changed from: protected */
    public void setDefault(UCSetupTask uCSetupTask) {
        d = uCSetupTask;
    }
}
