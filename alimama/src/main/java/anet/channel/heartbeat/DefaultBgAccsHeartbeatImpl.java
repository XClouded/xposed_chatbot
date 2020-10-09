package anet.channel.heartbeat;

import anet.channel.Constants;
import anet.channel.Session;
import anet.channel.thread.ThreadPoolExecutorFactory;
import java.util.concurrent.TimeUnit;

public class DefaultBgAccsHeartbeatImpl implements IHeartbeat, Runnable {
    volatile boolean isCancelled = false;
    Session session = null;

    public void reSchedule() {
    }

    public void start(Session session2) {
        if (session2 != null) {
            this.session = session2;
            run();
            return;
        }
        throw new NullPointerException("session is null");
    }

    public void stop() {
        this.isCancelled = true;
    }

    public void run() {
        if (!this.isCancelled) {
            this.session.ping(true);
            ThreadPoolExecutorFactory.submitScheduledTask(this, Constants.MAX_SESSION_IDLE_TIME, TimeUnit.MILLISECONDS);
        }
    }
}
