package com.uc.webview.export.internal;

import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.setup.UCSetupException;

/* compiled from: U4Source */
final class c implements Runnable {
    c() {
    }

    public final void run() {
        while (true) {
            try {
                Runnable runnable = (Runnable) SDKFactory.b.a.poll();
                if (runnable != null) {
                    runnable.run();
                } else {
                    return;
                }
            } catch (Exception e) {
                SDKFactory.b.a.clear();
                UCSetupException unused = SDKFactory.b.b = new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_UPDATE_MODEL_NOT_EXIST, (Throwable) e);
                return;
            }
        }
    }
}
