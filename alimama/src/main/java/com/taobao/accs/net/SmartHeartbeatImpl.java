package com.taobao.accs.net;

import anet.channel.Constants;
import anet.channel.Session;
import anet.channel.heartbeat.IHeartbeat;
import anet.channel.thread.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SmartHeartbeatImpl implements IHeartbeat, Runnable {
    private static final int BACKGROUND_INTERVAL = 270000;
    private static final int FOREGROUND_INTERVAL = 45000;
    private static final String TAG = "SmartHeartbeatImpl";
    private Future future;
    private long interval = Constants.MAX_SESSION_IDLE_TIME;
    private volatile boolean isCancelled = false;
    private Session session;
    private int state = 1;

    /* access modifiers changed from: package-private */
    public void setState(int i) {
        if (this.state == i || this.state + i <= 1) {
            this.state = i;
            return;
        }
        String str = TAG;
        ALog.i(str, "reset state, last state: " + this.state + " current state: " + i, new Object[0]);
        this.state = i;
        this.interval = this.state < 2 ? Constants.MAX_SESSION_IDLE_TIME : 270000;
        reSchedule();
    }

    public void start(Session session2) {
        if (session2 != null) {
            this.session = session2;
            this.interval = this.state < 2 ? Constants.MAX_SESSION_IDLE_TIME : 270000;
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
        submit(this.interval);
    }

    public void run() {
        if (!this.isCancelled) {
            ALog.e(TAG, "ping ", new Object[0]);
            this.session.ping(true);
        }
    }

    private synchronized void submit(long j) {
        try {
            String str = TAG;
            ALog.i(str, "submit ping current delay: " + j, new Object[0]);
            if (this.future != null) {
                this.future.cancel(false);
                this.future = null;
            }
            this.future = ThreadPoolExecutorFactory.submitScheduledTask(this, j + 50, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ALog.e(TAG, "Submit heartbeat task failed.", this.session.mSeq, e);
        }
        return;
    }
}
