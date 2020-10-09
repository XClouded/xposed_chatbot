package com.uc.webview.export.internal.setup;

import android.util.Pair;
import com.ali.user.mobile.ui.WebConstant;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.uc.startup.a;
import com.uc.webview.export.internal.utility.k;
import java.util.HashMap;

/* compiled from: U4Source */
public final class i extends a {
    public i() {
        this.a = "InitCoreEngineJob";
        this.b = new Pair(261, Integer.valueOf(WebConstant.OPEN_WEV_H5_BIND_REQUEST));
    }

    /* access modifiers changed from: protected */
    public final void a() {
        HashMap hashMap = new HashMap();
        br brVar = af.c;
        if (brVar != null) {
            hashMap.put("ucm_corelib_path", brVar.soDirPath);
            hashMap.put("ucm_paks_resource_dir", brVar.resDirPath);
            if (brVar.coreImplModule != null) {
                hashMap.put("ucm_dex_path", brVar.coreImplModule.first);
                hashMap.put("ucm_odex_path", brVar.coreImplModule.second);
            }
            String str = (String) af.a(UCCore.OPTION_PRIVATE_DATA_DIRECTORY_SUFFIX);
            if (str != null) {
                hashMap.put("ucm_private_data_dir_suffix", str);
            }
            int i = (Integer) af.a(UCCore.OPTION_WEBVIEW_MULTI_PROCESS);
            if (i == null) {
                i = 0;
            }
            int i2 = (Integer) af.a(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_FALLBACK_TIMEOUT);
            if (i2 == null) {
                i2 = 0;
            }
            boolean z = (Boolean) af.a(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SERVICE_SPEEDUP);
            if (z == null) {
                z = false;
            }
            hashMap.put("ucm_multi_process", String.valueOf(i));
            hashMap.put("ucm_multi_process_fallback_timeout", String.valueOf(i2));
            hashMap.put("ucm_multi_process_enable_service_speedup", String.valueOf(z));
            hashMap.put(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SECCOMP, k.b(af.a(UCCore.OPTION_WEBVIEW_MULTI_PROCESS_ENABLE_SECCOMP)) ? "1" : "0");
            int i3 = (Integer) af.a(UCCore.OPTION_MULTI_PROCESS_STARTUP_TIMEOUT);
            if (i3 == null) {
                i3 = 0;
            }
            hashMap.put(UCCore.OPTION_MULTI_PROCESS_STARTUP_TIMEOUT, String.valueOf(i3));
            hashMap.put(UCCore.OPTION_MULTI_PROCESS_DISABLE_FALLBACK_TO_SINGLE_PROCESS, k.b(af.a(UCCore.OPTION_MULTI_PROCESS_DISABLE_FALLBACK_TO_SINGLE_PROCESS)) ? "1" : "0");
            hashMap.put(UCCore.OPTION_GPU_PROCESS_DISABLE_WATCHDOG_BEFORE_LOAD_URL, k.b(af.a(UCCore.OPTION_GPU_PROCESS_DISABLE_WATCHDOG_BEFORE_LOAD_URL)) ? "1" : "0");
            int i4 = (Integer) af.a(UCCore.OPTION_GPU_PROCESS_MODE);
            if (i4 == null) {
                i4 = 0;
            }
            hashMap.put("ucm_gpu_process_mode", String.valueOf(i4));
            Integer num = (Integer) af.a(UCCore.OPTION_GPU_WARM_UP_TIME);
            if (num != null) {
                hashMap.put(UCCore.OPTION_GPU_WARM_UP_TIME, String.valueOf(num));
            } else {
                hashMap.put(UCCore.OPTION_GPU_WARM_UP_TIME, "-1");
            }
            hashMap.put("ucm_skip_init_setting", String.valueOf(!af.b));
            hashMap.put("ucm_is_hardware_ac", String.valueOf(af.e));
            hashMap.put("ucm_sup", String.valueOf(af.a()));
        }
        try {
            a.a(9002, new Object[]{af.a, hashMap});
            af.a(3, new Object[0]);
        } catch (Throwable th) {
            throw new UCSetupException(3007, th);
        }
    }
}
