package anet.channel.heartbeat;

import anet.channel.Constants;
import anet.channel.Session;
import anet.channel.thread.ThreadPoolExecutorFactory;
import java.util.concurrent.TimeUnit;

public class SelfKillHeartbeatImpl implements IHeartbeat, Runnable {
    private volatile long expectSelfKillTime = System.currentTimeMillis();
    private volatile boolean isCancelled = false;
    private Session session = null;

    public void start(Session session2) {
        if (session2 != null) {
            this.session = session2;
            this.expectSelfKillTime = System.currentTimeMillis() + Constants.MAX_SESSION_IDLE_TIME;
            ThreadPoolExecutorFactory.submitScheduledTask(this, Constants.MAX_SESSION_IDLE_TIME, TimeUnit.MILLISECONDS);
            return;
        }
        throw new NullPointerException("session is null");
    }

    public void stop() {
        this.isCancelled = true;
    }

    public void reSchedule() {
        this.expectSelfKillTime = System.currentTimeMillis() + Constants.MAX_SESSION_IDLE_TIME;
    }

    public void run() {
        if (!this.isCancelled) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < this.expectSelfKillTime - 1000) {
                ThreadPoolExecutorFactory.submitScheduledTask(this, this.expectSelfKillTime - currentTimeMillis, TimeUnit.MILLISECONDS);
            } else {
                this.session.close(false);
            }
        }
    }
}
