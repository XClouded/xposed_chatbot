package android.taobao.windvane.extra.performance2;

public interface WVPerformance {
    void end();

    void onResourceFinished(String str, int i);

    void onResourceReceivedStatusCode(String str, int i);

    void onResourceStarted(String str);

    void recordProperties(String str, Object obj);

    void recordStage(String str, long j);

    void recordStatistics(String str, long j);

    void start();
}
