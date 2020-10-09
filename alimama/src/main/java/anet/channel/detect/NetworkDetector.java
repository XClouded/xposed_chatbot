package anet.channel.detect;

import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkDetector {
    private static final String TAG = "awcn.NetworkDetector";
    private static ExceptionDetector exceptionDetector = new ExceptionDetector();
    private static HorseRaceDetector horseRaceDetector = new HorseRaceDetector();
    private static AtomicBoolean isInit = new AtomicBoolean(false);
    private static MTUDetector mtuDetector = new MTUDetector();

    public static void registerListener() {
        if (isInit.compareAndSet(false, true)) {
            ALog.i(TAG, "registerListener", (String) null, new Object[0]);
            horseRaceDetector.register();
            exceptionDetector.register();
            mtuDetector.register();
        }
    }

    public static void commitDetect(RequestStatistic requestStatistic) {
        if (isInit.get()) {
            exceptionDetector.commitDetect(requestStatistic);
        }
    }
}
