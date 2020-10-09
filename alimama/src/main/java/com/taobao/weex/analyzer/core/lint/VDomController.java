package com.taobao.weex.analyzer.core.lint;

import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;

@Deprecated
public class VDomController implements IVDomMonitor {
    public static boolean isPollingMode = false;
    public static boolean isStandardMode = false;
    private PollingVDomMonitor mPollingVDomMonitor;
    private StandardVDomMonitor mStandardVDomMonitor;

    public VDomController(@NonNull PollingVDomMonitor pollingVDomMonitor, @NonNull StandardVDomMonitor standardVDomMonitor) {
        this.mPollingVDomMonitor = pollingVDomMonitor;
        this.mStandardVDomMonitor = standardVDomMonitor;
    }

    public void monitor(@NonNull WXSDKInstance wXSDKInstance) {
        if (isPollingMode) {
            this.mPollingVDomMonitor.monitor(wXSDKInstance);
        } else if (isStandardMode) {
            this.mStandardVDomMonitor.monitor(wXSDKInstance);
        }
    }

    public void destroy() {
        this.mPollingVDomMonitor.destroy();
        this.mStandardVDomMonitor.destroy();
    }
}
