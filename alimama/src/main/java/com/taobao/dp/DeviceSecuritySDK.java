package com.taobao.dp;

import android.content.Context;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.umid.IUMIDComponent;
import com.alibaba.wireless.security.open.umid.IUMIDInitListenerEx;
import com.taobao.dp.client.IInitResultListener;
import com.taobao.dp.http.IUrlRequestService;

@Deprecated
public final class DeviceSecuritySDK {
    public static final int ENVIRONMENT_DAILY = 2;
    public static final int ENVIRONMENT_ONLINE = 0;
    public static final int ENVIRONMENT_PRE = 1;
    private static DeviceSecuritySDK instance;
    private IUMIDComponent mUmidComponent = null;
    private String mVersion;

    @Deprecated
    public void sendLoginResult(String str) {
    }

    private DeviceSecuritySDK(Context context) {
        try {
            SecurityGuardManager instance2 = SecurityGuardManager.getInstance(context);
            this.mUmidComponent = (IUMIDComponent) instance2.getInterface(IUMIDComponent.class);
            this.mVersion = instance2.getSDKVerison();
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public static DeviceSecuritySDK getInstance(Context context) {
        if (instance == null) {
            synchronized (DeviceSecuritySDK.class) {
                if (instance == null) {
                    instance = new DeviceSecuritySDK(context);
                }
            }
        }
        return instance;
    }

    public int initSync(String str, int i, IUrlRequestService iUrlRequestService) {
        return initSync(str, "", i, iUrlRequestService);
    }

    public int initSync(String str, String str2, int i, IUrlRequestService iUrlRequestService) {
        try {
            if (this.mUmidComponent != null) {
                return this.mUmidComponent.initUMIDSync(i);
            }
            return -1;
        } catch (SecException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void initAsync(String str, int i, IUrlRequestService iUrlRequestService, IInitResultListener iInitResultListener) {
        initAsync(str, "", i, iUrlRequestService, iInitResultListener);
    }

    public void initAsync(String str, String str2, int i, IUrlRequestService iUrlRequestService, final IInitResultListener iInitResultListener) {
        AnonymousClass1 r2 = iInitResultListener != null ? new IUMIDInitListenerEx() {
            public void onUMIDInitFinishedEx(String str, int i) {
                iInitResultListener.onInitFinished(str, i);
            }
        } : null;
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.initUMID(i, r2);
            }
        } catch (SecException e) {
            e.printStackTrace();
            if (iInitResultListener != null) {
                iInitResultListener.onInitFinished((String) null, -1);
            }
        }
    }

    @Deprecated
    public void init() {
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.initUMID();
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void init(IUrlRequestService iUrlRequestService) {
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.initUMID();
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public String getSecurityToken() {
        try {
            if (this.mUmidComponent != null) {
                return this.mUmidComponent.getSecurityToken();
            }
            return "000000000000000000000000";
        } catch (SecException e) {
            e.printStackTrace();
            return "000000000000000000000000";
        }
    }

    public String getSecurityToken(int i) {
        try {
            if (this.mUmidComponent != null) {
                return this.mUmidComponent.getSecurityToken(i);
            }
            return "000000000000000000000000";
        } catch (SecException e) {
            e.printStackTrace();
            return "000000000000000000000000";
        }
    }

    public void setEnvironment(int i) {
        try {
            if (this.mUmidComponent != null) {
                this.mUmidComponent.setEnvironment(i);
            }
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setOnlineHost(OnlineHost onlineHost) throws IllegalArgumentException {
        if (onlineHost != null) {
            try {
                if (this.mUmidComponent != null) {
                    this.mUmidComponent.setOnlineHost(onlineHost.getHost());
                }
            } catch (SecException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("host is null");
        }
        return;
    }

    public String getVersion() {
        return this.mVersion;
    }
}
