package com.taobao.android.dinamicx;

import android.content.Context;
import android.util.Log;
import com.taobao.android.dinamic.DRegisterCenter;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamicx.DXGlobalInitConfig;

public class AliDinamicX {
    private static boolean init = false;

    public static void init(Context context, DXGlobalInitConfig.Builder builder, boolean z) {
        if (!init) {
            if (builder != null) {
                builder.withWebImageInterface(new AliDXImageViewImpl());
                builder.withAppMonitor(new DXAppMonitorImpl());
                builder.withRemoteDebugLog(new DXRemoteLogImpl());
                builder.withDxDownloader(new DXHttpLoader());
                builder.withUmbrellaImpl(new DXUmbrellaImpl());
            }
            init(context, builder, true, z);
            init = true;
        }
    }

    public static void initV3(Context context, DXGlobalInitConfig.Builder builder, boolean z) {
        if (!init) {
            init(context, builder, false, z);
            init = true;
        }
    }

    private static void init(Context context, DXGlobalInitConfig.Builder builder, boolean z, boolean z2) {
        if (z) {
            try {
                initV2(context, z2);
            } catch (Exception e) {
                Log.e("DinamicException", "AliDinamicX v2 init failed", e);
            }
        }
        if (builder != null) {
            try {
                builder.withDebug(z2);
                DXGlobalInitConfig build = builder.build();
                boolean z3 = false;
                if (build.dxWebImageInterface == null) {
                    builder.withWebImageInterface(new AliDXImageViewImpl());
                    z3 = true;
                }
                if (build.appMonitor == null) {
                    builder.withAppMonitor(new DXAppMonitorImpl());
                    z3 = true;
                }
                if (build.remoteDebugLog == null) {
                    builder.withRemoteDebugLog(new DXRemoteLogImpl());
                    z3 = true;
                }
                if (build.dxDownloader == null) {
                    builder.withDxDownloader(new DXHttpLoader());
                    z3 = true;
                }
                if (build.umbrellaImpl == null) {
                    builder.withUmbrellaImpl(new DXUmbrellaImpl());
                    z3 = true;
                }
                if (z3) {
                    build = builder.build();
                }
                DinamicXEngine.initialize(context, build);
            } catch (Exception e2) {
                Log.e("DinamicException", "AliDinamicX registerView failed", e2);
            }
        } else {
            DinamicXEngine.initialize(context, (DXGlobalInitConfig) null);
        }
    }

    private static void initV2(Context context, boolean z) {
        Dinamic.init(context.getApplicationContext(), z);
        DRegisterCenter.shareCenter().registerHttpLoader(new HttpLoader());
        DRegisterCenter.shareCenter().registerImageInterface(new AliImageViewImpl());
        DRegisterCenter.shareCenter().registerAppMonitor(new AppMonitorImpl());
        DRegisterCenter.shareCenter().registerRemoteDebugLog(new RemoteLogImpl());
    }
}
