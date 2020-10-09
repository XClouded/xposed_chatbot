package com.taobao.uc.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.uc.sandboxExport.SandboxedProcessService;

public class GpuProcessService0 extends SandboxedProcessService {
    public void onCreate() {
        super.onCreate();
        Log.i("GPU_PROCESS", "start gpu process");
    }

    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
