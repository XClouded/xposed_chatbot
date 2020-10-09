package com.taobao.android.dxcontainer;

import android.content.Context;
import com.taobao.android.dinamicx.AliDinamicX;
import com.taobao.android.dinamicx.DXGlobalInitConfig;
import com.taobao.android.dxcontainer.DXContainerGlobalInitConfig;

public class AliDXContainer {
    private static boolean init = false;

    public static void init(Context context, DXContainerGlobalInitConfig.Builder builder, boolean z) {
        if (!init) {
            builder.withIsDebug(z);
            builder.withIDXContainerAppMonitor(new DXContainerAppMonitorImpl());
            DXContainerEngine.initialize(context, builder.build());
            init = true;
        }
    }

    public static void initWithAliDinamicX(Context context, DXContainerGlobalInitConfig.Builder builder, DXGlobalInitConfig.Builder builder2, boolean z) {
        if (!init) {
            DXContainerEngine.initialize(context, builder.build());
            AliDinamicX.initV3(context, builder2, z);
            init = true;
        }
    }
}
