package anet.channel.heartbeat;

import anet.channel.Constants;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.Session;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.util.concurrent.TimeUnit;

class DefaultHeartbeatImpl implements IHeartbeat, Runnable {
    private static final String TAG = "awcn.DefaultHeartbeatImpl";
    private volatile long executeTime = 0;
    private long interval = 0;
    private volatile boolean isCancelled = false;
    private Session session;

    DefaultHeartbeatImpl() {
    }

    public void start(Session session2) {
        if (session2 != null) {
            this.session = session2;
            this.interval = (long) session2.getConnStrategy().getHeartbeat();
            if (this.interval <= 0) {
                this.interval = Constants.MAX_SESSION_IDLE_TIME;
            }
            ALog.i(TAG, "heartbeat start", session2.mSeq, "session", session2, "interval", Long.valueOf(this.interval));
            submit(this.interval);
            return;
        }
        throw new NullPointerException("session is null");
    }

    public void stop() {
        if (this.session != null) {
            ALog.i(TAG, "heartbeat stop", this.session.mSeq, "session", this.session);
            this.isCancelled = true;
        }
    }

    public void reSchedule() {
        this.executeTime = System.currentTimeMillis() + this.interval;
    }

    public void run() {
        if (!this.isCancelled) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < this.executeTime - 1000) {
                submit(this.executeTime - currentTimeMillis);
            } else if (!GlobalAppRuntimeInfo.isAppBackground()) {
                if (ALog.isPrintLog(1)) {
                    ALog.d(TAG, "heartbeat", this.session.mSeq, "session", this.session);
                }
                this.session.ping(true);
                submit(this.interval);
            } else {
                ALog.e(TAG, "close session in background", this.session.mSeq, "session", this.session);
                this.session.close(false);
            }
        }
    }

    private void submit(long j) {
        try {
            this.executeTime = System.currentTimeMillis() + j;
            ThreadPoolExecutorFactory.submitScheduledTask(this, j + 50, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ALog.e(TAG, "Submit heartbeat task failed.", this.session.mSeq, e, new Object[0]);
        }
    }
}
