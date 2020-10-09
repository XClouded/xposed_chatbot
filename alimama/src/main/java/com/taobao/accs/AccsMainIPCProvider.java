package com.taobao.accs;

import android.app.Application;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ForeBackManager;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.aipc.AIPC;
import com.taobao.aipc.core.channel.DuplexIPCProvider;

public class AccsMainIPCProvider extends DuplexIPCProvider {
    public boolean onCreate() {
        GlobalClientInfo.mContext = getContext();
        ForeBackManager.getManager().initialize((Application) getContext().getApplicationContext());
        if (OrangeAdapter.isChannelModeEnable()) {
            Constants.SDK_VERSION_CODE = 400;
            AIPC.init(getContext());
        }
        return super.onCreate();
    }
}
