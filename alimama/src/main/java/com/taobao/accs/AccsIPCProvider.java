package com.taobao.accs;

import anet.channel.GlobalAppRuntimeInfo;
import com.taobao.accs.base.AccsDataListener;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.internal.ConnectionServiceImpl;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.aipc.AIPC;
import com.taobao.aipc.core.channel.DuplexIPCProvider;

public class AccsIPCProvider extends DuplexIPCProvider {
    static {
        AIPC.register(ConnectionServiceImpl.class);
        AIPC.register(GlobalClientInfo.class);
        AIPC.register(IAppReceiver.class);
        AIPC.register(IAgooAppReceiver.class);
        AIPC.register(AccsDataListener.class);
    }

    public boolean onCreate() {
        GlobalClientInfo.mContext = getContext();
        if (OrangeAdapter.isChannelModeEnable()) {
            Constants.SDK_VERSION_CODE = 400;
            GlobalAppRuntimeInfo.setBackground(false);
            AIPC.init(getContext());
        }
        return super.onCreate();
    }
}
