package com.taobao.android.dinamicx.widget.event;

import android.text.TextUtils;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.notification.DXSignalProduce;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DXControlEventCenter implements DXSignalProduce.SignalReceiver {
    public static final int PERIOD_COUNT = 2;
    CopyOnWriteArrayList<DXControlEvent> delayEvents = new CopyOnWriteArrayList<>();
    Map<String, List<IDXControlEventListener>> listenerMap = new HashMap();
    int receiverCount;

    public DXControlEventCenter() {
        DXSignalProduce.getInstance().registerControlEventCenter(this);
    }

    public void registerListener(IDXControlEventListener iDXControlEventListener, String str) {
        if (!TextUtils.isEmpty(str) && iDXControlEventListener != null) {
            List list = this.listenerMap.get(str);
            if (list == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(iDXControlEventListener);
                this.listenerMap.put(str, arrayList);
                return;
            }
            list.add(iDXControlEventListener);
        }
    }

    public void destroyListeners() {
        this.listenerMap.clear();
    }

    public void unRegisterListener(IDXControlEventListener iDXControlEventListener, String str) {
        List list;
        if (!TextUtils.isEmpty(str) && iDXControlEventListener != null && (list = this.listenerMap.get(str)) != null) {
            list.remove(iDXControlEventListener);
        }
    }

    public void postEvent(DXControlEvent dXControlEvent) {
        List<IDXControlEventListener> list;
        if (dXControlEvent != null && !TextUtils.isEmpty(dXControlEvent.eventName) && (list = this.listenerMap.get(dXControlEvent.eventName)) != null) {
            for (IDXControlEventListener receivedControlEvent : list) {
                receivedControlEvent.receivedControlEvent(dXControlEvent);
            }
        }
    }

    public void postEventDelay(DXControlEvent dXControlEvent) {
        boolean z = false;
        int i = 0;
        while (true) {
            try {
                if (i >= this.delayEvents.size()) {
                    z = true;
                    break;
                } else if (this.delayEvents.get(i).equals(dXControlEvent)) {
                    break;
                } else {
                    i++;
                }
            } catch (Throwable th) {
                DXExceptionUtil.printStack(th);
                DXError dXError = new DXError("dinamicx");
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_CONTROL_EVENT_CENTER, DXMonitorConstant.DX_CONTROL_EVENT_CENTER_EXCETION_, DXError.DX_ERROR_CODE_CONTROL_EVENT_CENTER_EXCEPTION_CRASH);
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
                dXError.dxErrorInfoList.add(dXErrorInfo);
                DXAppMonitor.trackerError(dXError);
                return;
            }
        }
        if (z) {
            this.delayEvents.add(dXControlEvent);
        }
    }

    public void onReceiver() {
        if (this.receiverCount == 2) {
            for (int i = 0; i < this.delayEvents.size(); i++) {
                postEvent(this.delayEvents.get(i));
            }
            this.delayEvents.clear();
            this.receiverCount = 0;
            return;
        }
        this.receiverCount++;
    }
}
