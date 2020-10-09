package com.alibaba.analytics.core.ipv6;

import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.ipv6.TnetIpv6HostListener;

public class TnetIpv6Manager {
    public static TnetIpv6Manager instance;
    private boolean bError = false;
    private boolean bIpv6Connection = false;
    private CloseDetectIpv6Listener mCloseDetectIpv6Listener = new CloseDetectIpv6Listener();
    private SampleIpv6Listener mSampleIpv6Listener = new SampleIpv6Listener();
    private TnetIpv6HostListener mTnetIpv6HostListener = new TnetIpv6HostListener();

    public static synchronized TnetIpv6Manager getInstance() {
        TnetIpv6Manager tnetIpv6Manager;
        synchronized (TnetIpv6Manager.class) {
            if (instance == null) {
                instance = new TnetIpv6Manager();
            }
            tnetIpv6Manager = instance;
        }
        return tnetIpv6Manager;
    }

    private TnetIpv6Manager() {
    }

    public void registerConfigListener() {
        SystemConfigMgr.getInstance().register(Ipv6ConfigConstant.CLOSE_DETECT_IPV6, this.mCloseDetectIpv6Listener);
        SystemConfigMgr.getInstance().register(Ipv6ConfigConstant.SAMPLE_IPV6, this.mSampleIpv6Listener);
    }

    public void response(boolean z, int i, long j) {
        Ipv6Monitor.sendIpv6Init(z, i, j);
        if (!z && this.bIpv6Connection) {
            this.bError = true;
            setIpv6Connection(false);
            Ipv6Monitor.sendIpv6Error(i, j);
        }
    }

    public boolean isEnable() {
        if (this.bError || this.mCloseDetectIpv6Listener.isCloseDetect() || getHostPortEntity() == null) {
            return false;
        }
        int detectIpStack = Inet64Util.detectIpStack();
        if (detectIpStack == 2) {
            return true;
        }
        if (detectIpStack == 3) {
            return this.mSampleIpv6Listener.isEnableBySample();
        }
        return false;
    }

    public boolean isIpv6Connection() {
        return this.bIpv6Connection;
    }

    public void setIpv6Connection(boolean z) {
        this.bIpv6Connection = z;
    }

    public TnetIpv6HostListener.TnetIpv6HostPort getHostPortEntity() {
        return this.mTnetIpv6HostListener.getHostPortEntity();
    }
}
