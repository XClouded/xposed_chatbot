package com.taobao.uc.service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.uc.sandboxExport.SandboxedProcessService;

public class SandboxedProcessService0 extends SandboxedProcessService {
    private static final String TAG = "sandbox";

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "use sandboxed process");
    }

    public IBinder onBind(Intent intent) {
        Log.i(TAG, intent.toString());
        return super.onBind(intent);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
