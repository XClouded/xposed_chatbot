package mtopsdk.mtop.xcommand;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import mtopsdk.common.util.StringUtils;

public class XcmdEventMgr {
    private static final String TAG = "mtopsdk.XcmdEventMgr";
    static Set<NewXcmdListener> oxcmdListeners = new CopyOnWriteArraySet();
    private static XcmdEventMgr xm;

    public static XcmdEventMgr getInstance() {
        if (xm == null) {
            synchronized (XcmdEventMgr.class) {
                if (xm == null) {
                    xm = new XcmdEventMgr();
                }
            }
        }
        return xm;
    }

    public void addOrangeXcmdListener(NewXcmdListener newXcmdListener) {
        oxcmdListeners.add(newXcmdListener);
    }

    public void removeOrangeXcmdListener(NewXcmdListener newXcmdListener) {
        oxcmdListeners.remove(newXcmdListener);
    }

    public void onOrangeEvent(String str) {
        if (!StringUtils.isBlank(str)) {
            NewXcmdEvent newXcmdEvent = new NewXcmdEvent(str);
            for (NewXcmdListener onEvent : oxcmdListeners) {
                try {
                    onEvent.onEvent(newXcmdEvent);
                } catch (Throwable unused) {
                }
            }
        }
    }
}
