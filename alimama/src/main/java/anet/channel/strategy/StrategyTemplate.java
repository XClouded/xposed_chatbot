package anet.channel.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StrategyTemplate {
    Map<String, ConnProtocol> templateMap = new ConcurrentHashMap();

    public static StrategyTemplate getInstance() {
        return holder.instance;
    }

    static class holder {
        static StrategyTemplate instance = new StrategyTemplate();

        holder() {
        }
    }

    public void registerConnProtocol(String str, ConnProtocol connProtocol) {
        if (connProtocol != null) {
            this.templateMap.put(str, connProtocol);
            try {
                IStrategyInstance instance = StrategyCenter.getInstance();
                if (instance instanceof StrategyInstance) {
                    ((StrategyInstance) instance).holder.localDnsStrategyTable.setProtocolForHost(str, connProtocol);
                }
            } catch (Exception unused) {
            }
        }
    }

    public ConnProtocol getConnProtocol(String str) {
        return this.templateMap.get(str);
    }
}
