package com.taobao.accs.base;

import com.taobao.accs.base.TaoBaseService;

public abstract class AccsAbstractDataListener implements AccsDataListener {
    public void onAntiBrush(boolean z, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onConnected(TaoBaseService.ConnectInfo connectInfo) {
    }

    public void onDisconnected(TaoBaseService.ConnectInfo connectInfo) {
    }
}
