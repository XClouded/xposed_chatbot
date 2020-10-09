package com.taobao.accs.net;

import anet.channel.entity.ConnType;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.DispatchEvent;
import anet.channel.strategy.dispatch.HttpDispatcher;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpDnsProvider {
    private static final String TAG = "HttpDnsProvider";
    private int mCurrStrategyPos = 0;
    private List<IConnStrategy> mStrategys = new ArrayList();

    public HttpDnsProvider(String str) {
        HttpDispatcher.getInstance().addListener(new HttpDispatcher.IDispatchEventListener() {
            public void onEvent(DispatchEvent dispatchEvent) {
                ThreadPoolExecutorFactory.schedule(new Runnable() {
                    public void run() {
                        StrategyCenter.getInstance().saveData();
                    }
                }, 2000, TimeUnit.MILLISECONDS);
            }
        });
        getAvailableStrategy(str);
    }

    public List<IConnStrategy> getAvailableStrategy(String str) {
        List<IConnStrategy> connStrategyListByHost;
        if ((this.mCurrStrategyPos == 0 || this.mStrategys.isEmpty()) && (connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(str)) != null && !connStrategyListByHost.isEmpty()) {
            this.mStrategys.clear();
            for (IConnStrategy next : connStrategyListByHost) {
                ConnType valueOf = ConnType.valueOf(next.getProtocol());
                if (valueOf.getTypeLevel() == ConnType.TypeLevel.SPDY && valueOf.isSSL()) {
                    this.mStrategys.add(next);
                }
            }
        }
        return this.mStrategys;
    }

    public IConnStrategy getStrategy() {
        return getStrategy(this.mStrategys);
    }

    public IConnStrategy getStrategy(List<IConnStrategy> list) {
        if (list == null || list.isEmpty()) {
            ALog.d(TAG, "strategys null or 0", new Object[0]);
            return null;
        }
        if (this.mCurrStrategyPos < 0 || this.mCurrStrategyPos >= list.size()) {
            this.mCurrStrategyPos = 0;
        }
        return list.get(this.mCurrStrategyPos);
    }

    public void updateStrategyPos() {
        this.mCurrStrategyPos++;
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(TAG, "updateStrategyPos StrategyPos:" + this.mCurrStrategyPos, new Object[0]);
        }
    }

    public int getStrategyPos() {
        return this.mCurrStrategyPos;
    }

    public void forceUpdateStrategy(String str) {
        StrategyCenter.getInstance().forceRefreshStrategy(str);
    }
}
