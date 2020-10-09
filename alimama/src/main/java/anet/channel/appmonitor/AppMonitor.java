package anet.channel.appmonitor;

import anet.channel.statist.AlarmObject;
import anet.channel.statist.CountObject;
import anet.channel.statist.StatObject;

public class AppMonitor {
    /* access modifiers changed from: private */
    public static volatile IAppMonitor apmMonitor = null;
    private static volatile IAppMonitor appMonitor = new Proxy((IAppMonitor) null);

    public static IAppMonitor getInstance() {
        return appMonitor;
    }

    public static void setInstance(IAppMonitor iAppMonitor) {
        appMonitor = new Proxy(iAppMonitor);
    }

    public static void setApmMonitor(IAppMonitor iAppMonitor) {
        apmMonitor = iAppMonitor;
    }

    static class Proxy implements IAppMonitor {
        IAppMonitor appMonitor = null;

        @Deprecated
        public void register() {
        }

        @Deprecated
        public void register(Class<?> cls) {
        }

        Proxy(IAppMonitor iAppMonitor) {
            this.appMonitor = iAppMonitor;
        }

        public void commitStat(StatObject statObject) {
            if (AppMonitor.apmMonitor != null) {
                AppMonitor.apmMonitor.commitStat(statObject);
            }
            if (this.appMonitor != null) {
                this.appMonitor.commitStat(statObject);
            }
        }

        public void commitAlarm(AlarmObject alarmObject) {
            if (this.appMonitor != null) {
                this.appMonitor.commitAlarm(alarmObject);
            }
        }

        public void commitCount(CountObject countObject) {
            if (this.appMonitor != null) {
                this.appMonitor.commitCount(countObject);
            }
        }
    }
}
