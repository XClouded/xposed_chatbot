package mtopsdk.mtop.stat;

import androidx.annotation.NonNull;
import java.util.HashMap;

public class MtopMonitor {
    private static volatile IMtopMonitor headerMonitor;
    private static volatile IMtopMonitor monitor;

    public static void addMtopMonitor(@NonNull IMtopMonitor iMtopMonitor) {
        monitor = new Proxy(iMtopMonitor);
    }

    public static void addHeaderMonitor(@NonNull IMtopMonitor iMtopMonitor) {
        headerMonitor = new Proxy(iMtopMonitor);
    }

    public static IMtopMonitor getInstance() {
        return monitor;
    }

    public static IMtopMonitor getHeaderMonitor() {
        return headerMonitor;
    }

    static class Proxy implements IMtopMonitor {
        IMtopMonitor mtopMonitor = null;

        public Proxy(IMtopMonitor iMtopMonitor) {
            this.mtopMonitor = iMtopMonitor;
        }

        public void onCommit(String str, HashMap<String, String> hashMap) {
            if (this.mtopMonitor != null) {
                this.mtopMonitor.onCommit(str, hashMap);
            }
        }
    }
}
