package anet.channel.strategy;

import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.entity.ConnType;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.DispatchEvent;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import com.taobao.weex.common.Constants;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArraySet;
import org.json.JSONObject;

class StrategyInstance implements IStrategyInstance, HttpDispatcher.IDispatchEventListener {
    private static final String TAG = "awcn.StrategyCenter";
    private IStrategyFilter defaultStrategyFilter = new IStrategyFilter() {
        public boolean accept(IConnStrategy iConnStrategy) {
            boolean isQuicEnable = AwcnConfig.isQuicEnable();
            boolean z = StrategyInstance.this.holder.getCurrStrategyTable().enableQuic;
            String str = iConnStrategy.getProtocol().protocol;
            if ((isQuicEnable && z) || (!ConnType.QUIC.equals(str) && !ConnType.QUIC_PLAIN.equals(str))) {
                return true;
            }
            ALog.i(StrategyInstance.TAG, "quic strategy disabled", (String) null, Constants.Name.STRATEGY, iConnStrategy);
            return false;
        }
    };
    StrategyInfoHolder holder = null;
    boolean isInitialized = false;
    long lastPersistentTime = 0;
    CopyOnWriteArraySet<IStrategyListener> listeners = new CopyOnWriteArraySet<>();

    StrategyInstance() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0040, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initialize(android.content.Context r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.isInitialized     // Catch:{ all -> 0x0041 }
            if (r0 != 0) goto L_0x003f
            if (r6 != 0) goto L_0x0008
            goto L_0x003f
        L_0x0008:
            r0 = 0
            r1 = 0
            java.lang.String r2 = "awcn.StrategyCenter"
            java.lang.String r3 = "StrategyCenter initialize started."
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0033 }
            anet.channel.util.ALog.i(r2, r3, r1, r4)     // Catch:{ Exception -> 0x0033 }
            anet.channel.strategy.dispatch.AmdcRuntimeInfo.setContext(r6)     // Catch:{ Exception -> 0x0033 }
            anet.channel.strategy.StrategySerializeHelper.initialize(r6)     // Catch:{ Exception -> 0x0033 }
            anet.channel.strategy.dispatch.HttpDispatcher r6 = anet.channel.strategy.dispatch.HttpDispatcher.getInstance()     // Catch:{ Exception -> 0x0033 }
            r6.addListener(r5)     // Catch:{ Exception -> 0x0033 }
            anet.channel.strategy.StrategyInfoHolder r6 = anet.channel.strategy.StrategyInfoHolder.newInstance()     // Catch:{ Exception -> 0x0033 }
            r5.holder = r6     // Catch:{ Exception -> 0x0033 }
            r6 = 1
            r5.isInitialized = r6     // Catch:{ Exception -> 0x0033 }
            java.lang.String r6 = "awcn.StrategyCenter"
            java.lang.String r2 = "StrategyCenter initialize finished."
            java.lang.Object[] r3 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0033 }
            anet.channel.util.ALog.i(r6, r2, r1, r3)     // Catch:{ Exception -> 0x0033 }
            goto L_0x003d
        L_0x0033:
            r6 = move-exception
            java.lang.String r2 = "awcn.StrategyCenter"
            java.lang.String r3 = "StrategyCenter initialize failed."
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0041 }
            anet.channel.util.ALog.e(r2, r3, r1, r6, r0)     // Catch:{ all -> 0x0041 }
        L_0x003d:
            monitor-exit(r5)
            return
        L_0x003f:
            monitor-exit(r5)
            return
        L_0x0041:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.StrategyInstance.initialize(android.content.Context):void");
    }

    public synchronized void switchEnv() {
        StrategySerializeHelper.clearStrategyFolder();
        HttpDispatcher.getInstance().switchENV();
        if (this.holder != null) {
            this.holder.clear();
            this.holder = StrategyInfoHolder.newInstance();
        }
    }

    @Deprecated
    public String getSchemeByHost(String str) {
        return getSchemeByHost(str, (String) null);
    }

    public String getSchemeByHost(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (checkHolderIsNull()) {
            return str2;
        }
        String safeAislesByHost = this.holder.strategyConfig.getSafeAislesByHost(str);
        if (safeAislesByHost != null || TextUtils.isEmpty(str2)) {
            str2 = safeAislesByHost;
        }
        if (str2 == null && (str2 = SchemeGuesser.getInstance().guessScheme(str)) == null) {
            str2 = "http";
        }
        ALog.d(TAG, "getSchemeByHost", (String) null, "host", str, "scheme", str2);
        return str2;
    }

    public String getCNameByHost(String str) {
        if (checkHolderIsNull() || TextUtils.isEmpty(str)) {
            return null;
        }
        return this.holder.getCurrStrategyTable().getCnameByHost(str);
    }

    public String getFormalizeUrl(String str) {
        HttpUrl parse = HttpUrl.parse(str);
        if (parse == null) {
            ALog.e(TAG, "url is invalid.", (String) null, WVConstants.INTENT_EXTRA_URL, str);
            return null;
        }
        String urlString = parse.urlString();
        try {
            String schemeByHost = getSchemeByHost(parse.host(), parse.scheme());
            if (!schemeByHost.equalsIgnoreCase(parse.scheme())) {
                urlString = StringUtils.concatString(schemeByHost, ":", str.substring(str.indexOf("//")));
            }
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "", (String) null, "raw", StringUtils.simplifyString(str, 128), "ret", StringUtils.simplifyString(urlString, 128));
            }
        } catch (Exception e) {
            ALog.e(TAG, "getFormalizeUrl failed", (String) null, e, "raw", str);
        }
        return urlString;
    }

    public List<IConnStrategy> getConnStrategyListByHost(String str) {
        return getConnStrategyListByHost(str, this.defaultStrategyFilter);
    }

    public List<IConnStrategy> getConnStrategyListByHost(String str, IStrategyFilter iStrategyFilter) {
        if (TextUtils.isEmpty(str) || checkHolderIsNull()) {
            return Collections.EMPTY_LIST;
        }
        String cnameByHost = this.holder.getCurrStrategyTable().getCnameByHost(str);
        if (!TextUtils.isEmpty(cnameByHost)) {
            str = cnameByHost;
        }
        List<IConnStrategy> queryByHost = this.holder.getCurrStrategyTable().queryByHost(str);
        if (queryByHost.isEmpty()) {
            queryByHost = this.holder.localDnsStrategyTable.queryByHost(str);
        }
        if (queryByHost.isEmpty() || iStrategyFilter == null) {
            ALog.d("getConnStrategyListByHost", (String) null, "host", str, "result", queryByHost);
            return queryByHost;
        }
        boolean z = !AwcnConfig.isIpv6Enable() || (AwcnConfig.isIpv6BlackListEnable() && this.holder.getCurrStrategyTable().isHostInIpv6BlackList(str, AwcnConfig.getIpv6BlackListTtl()));
        ListIterator<IConnStrategy> listIterator = queryByHost.listIterator();
        while (listIterator.hasNext()) {
            IConnStrategy next = listIterator.next();
            if (!iStrategyFilter.accept(next)) {
                listIterator.remove();
            }
            if (z && Utils.isIPV6Address(next.getIp())) {
                listIterator.remove();
            }
        }
        if (ALog.isPrintLog(1)) {
            ALog.d("getConnStrategyListByHost", (String) null, "host", str, "result", queryByHost);
        }
        return queryByHost;
    }

    public void forceRefreshStrategy(String str) {
        if (!checkHolderIsNull() && !TextUtils.isEmpty(str)) {
            ALog.i(TAG, "force refresh strategy", (String) null, "host", str);
            this.holder.getCurrStrategyTable().sendAmdcRequest(str, true);
        }
    }

    public void registerListener(IStrategyListener iStrategyListener) {
        ALog.e(TAG, "registerListener", (String) null, "listener", this.listeners);
        if (iStrategyListener != null) {
            this.listeners.add(iStrategyListener);
        }
    }

    public void unregisterListener(IStrategyListener iStrategyListener) {
        ALog.e(TAG, "unregisterListener", (String) null, "listener", this.listeners);
        this.listeners.remove(iStrategyListener);
    }

    public String getUnitByHost(String str) {
        if (checkHolderIsNull()) {
            return null;
        }
        return this.holder.strategyConfig.getUnitByHost(str);
    }

    public String getClientIp() {
        if (checkHolderIsNull()) {
            return "";
        }
        return this.holder.getCurrStrategyTable().clientIp;
    }

    public void notifyConnEvent(String str, IConnStrategy iConnStrategy, ConnEvent connEvent) {
        if (!checkHolderIsNull() && iConnStrategy != null && (iConnStrategy instanceof IPConnStrategy)) {
            IPConnStrategy iPConnStrategy = (IPConnStrategy) iConnStrategy;
            if (iPConnStrategy.ipSource == 1) {
                this.holder.localDnsStrategyTable.notifyConnEvent(str, iConnStrategy, connEvent);
            } else if (iPConnStrategy.ipSource == 0) {
                this.holder.getCurrStrategyTable().notifyConnEvent(str, iConnStrategy, connEvent);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean checkHolderIsNull() {
        if (this.holder != null) {
            return false;
        }
        ALog.w("StrategyCenter not initialized", (String) null, "isInitialized", Boolean.valueOf(this.isInitialized));
        return true;
    }

    public void onEvent(DispatchEvent dispatchEvent) {
        if (dispatchEvent.eventType == 1 && this.holder != null) {
            ALog.d(TAG, "receive amdc event", (String) null, new Object[0]);
            StrategyResultParser.HttpDnsResponse parse = StrategyResultParser.parse((JSONObject) dispatchEvent.extraObject);
            if (parse != null) {
                this.holder.update(parse);
                saveData();
                Iterator<IStrategyListener> it = this.listeners.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().onStrategyUpdated(parse);
                    } catch (Exception e) {
                        ALog.e(TAG, "onStrategyUpdated failed", (String) null, e, new Object[0]);
                    }
                }
            }
        }
    }

    public synchronized void saveData() {
        ALog.i(TAG, "saveData", (String) null, new Object[0]);
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastPersistentTime > 30000) {
            this.lastPersistentTime = currentTimeMillis;
            AmdcThreadPoolExecutor.scheduleTask(new Runnable() {
                public void run() {
                    if (!StrategyInstance.this.checkHolderIsNull()) {
                        StrategyInstance.this.holder.saveData();
                    }
                }
            }, 500);
        }
    }
}
