package anet.channel.strategy;

public class StrategyCenter {
    private static volatile IStrategyInstance instance;

    public static IStrategyInstance getInstance() {
        if (instance == null) {
            synchronized (StrategyCenter.class) {
                if (instance == null) {
                    instance = new StrategyInstance();
                }
            }
        }
        return instance;
    }

    public static void setInstance(IStrategyInstance iStrategyInstance) {
        instance = iStrategyInstance;
    }

    private StrategyCenter() {
    }
}
