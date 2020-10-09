package android.taobao.windvane.service;

import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;

import java.util.ArrayList;
import java.util.List;

public class WVEventService {
    private static volatile WVEventService EventManager = null;
    public static int WV_BACKWARD_EVENT = -1;
    public static int WV_EVENT = 0;
    public static int WV_FORWARD_EVENT = 1;
    private List<WVEventListener> mBackwardList = new ArrayList();
    private List<WVEventListener> mForwardList = new ArrayList();
    private WVInstantEventListener mInstantEvent;
    private List<WVEventListener> mList = new ArrayList();

    public static WVEventService getInstance() {
        if (EventManager == null) {
            synchronized (WVEventService.class) {
                if (EventManager == null) {
                    EventManager = new WVEventService();
                }
            }
        }
        return EventManager;
    }

    public synchronized void addEventListener(WVEventListener wVEventListener, int i) {
        if (wVEventListener != null) {
            if (i == WV_FORWARD_EVENT) {
                this.mForwardList.add(wVEventListener);
            } else if (i == WV_EVENT) {
                this.mList.add(wVEventListener);
            } else if (i == WV_BACKWARD_EVENT) {
                this.mBackwardList.add(wVEventListener);
            }
        }
    }

    public synchronized void addEventListener(WVEventListener wVEventListener) {
        addEventListener(wVEventListener, WV_EVENT);
    }

    public synchronized void setInstantEvent(WVInstantEventListener wVInstantEventListener) {
        if (wVInstantEventListener == null) {
            TaoLog.e("WVEventService", "event can not be null");
        } else if (this.mInstantEvent != null) {
            TaoLog.e("WVEventService", "an instance has already been set, please wait it end");
        } else {
            this.mInstantEvent = wVInstantEventListener;
        }
    }

    public synchronized void removeInstantEvent(WVInstantEventListener wVInstantEventListener) {
        if (wVInstantEventListener == null) {
            TaoLog.e("WVEventService", "event can not be null");
        } else if (this.mInstantEvent == null) {
            TaoLog.e("WVEventService", "event already be null");
        } else if (this.mInstantEvent != wVInstantEventListener) {
            TaoLog.e("WVEventService", "remove failed");
        } else {
            this.mInstantEvent = null;
        }
    }

    public synchronized void removeEventListener(WVEventListener wVEventListener) {
        if (wVEventListener != null) {
            int indexOf = this.mList.indexOf(wVEventListener);
            if (-1 != indexOf) {
                this.mList.remove(indexOf);
            }
            int indexOf2 = this.mForwardList.indexOf(wVEventListener);
            if (-1 != indexOf2) {
                this.mForwardList.remove(indexOf2);
            }
            int indexOf3 = this.mBackwardList.indexOf(wVEventListener);
            if (-1 != this.mBackwardList.indexOf(wVEventListener)) {
                this.mBackwardList.remove(indexOf3);
            }
        }
    }

    public synchronized WVEventResult onEvent(int i, IWVWebView iWVWebView, String str, Object... objArr) {
        WVEventResult onEvent;
        WVEventResult onEvent2;
        WVEventResult onEvent3;
        WVEventContext wVEventContext = new WVEventContext(iWVWebView, str);
        int i2 = 0;
        while (this.mForwardList != null && i2 < this.mForwardList.size()) {
            if (this.mForwardList.get(i2) != null && (onEvent3 = this.mForwardList.get(i2).onEvent(i, wVEventContext, objArr)) != null && onEvent3.isSuccess) {
                return onEvent3;
            }
            i2++;
        }
        int i3 = 0;
        while (this.mList != null && i3 < this.mList.size()) {
            if (this.mList.get(i3) != null && (onEvent2 = this.mList.get(i3).onEvent(i, wVEventContext, objArr)) != null && onEvent2.isSuccess) {
                return onEvent2;
            }
            i3++;
        }
        int i4 = 0;
        while (this.mBackwardList != null && i4 < this.mBackwardList.size()) {
            if (this.mBackwardList.get(i4) != null && (onEvent = this.mBackwardList.get(i4).onEvent(i, wVEventContext, objArr)) != null && onEvent.isSuccess) {
                return onEvent;
            }
            i4++;
        }
        return new WVEventResult(false);
    }

    public WVEventResult onEvent(int i) {
        return onEvent(i, (IWVWebView) null, (String) null, new Object[0]);
    }

    public WVEventResult onEvent(int i, Object... objArr) {
        return onEvent(i, (IWVWebView) null, (String) null, objArr);
    }

    public WVEventResult onInstantEvent(int i, Object... objArr) {
        WVEventContext wVEventContext = new WVEventContext((IWVWebView) null, (String) null);
        if (this.mInstantEvent != null) {
            return this.mInstantEvent.onInstantEvent(i, wVEventContext, objArr);
        }
        return null;
    }
}
