package anet.channel;

import android.text.TextUtils;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.StringUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

class AccsSessionManager {
    private static final String TAG = "awcn.AccsSessionManager";
    SessionCenter instance = null;
    Set<String> lastKeys = Collections.EMPTY_SET;

    AccsSessionManager(SessionCenter sessionCenter) {
        this.instance = sessionCenter;
    }

    public synchronized void checkAndStartSession() {
        Collection<SessionInfo> sessionInfos = this.instance.attributeManager.getSessionInfos();
        Set<String> set = Collections.EMPTY_SET;
        if (!sessionInfos.isEmpty()) {
            set = new TreeSet<>();
        }
        for (SessionInfo next : sessionInfos) {
            if (next.isKeepAlive) {
                set.add(StringUtils.concatString(StrategyCenter.getInstance().getSchemeByHost(next.host, next.isAccs ? "https" : "http"), HttpConstant.SCHEME_SPLIT, next.host));
            }
        }
        for (String next2 : this.lastKeys) {
            if (!set.contains(next2)) {
                closeSessions(next2);
            }
        }
        if (isNeedCheckSession()) {
            for (String next3 : set) {
                try {
                    this.instance.get(next3, ConnType.TypeLevel.SPDY, 0);
                } catch (Exception unused) {
                    ALog.e("start session failed", (String) null, "host", next3);
                }
            }
            this.lastKeys = set;
        }
    }

    public synchronized void forceCloseSession(boolean z) {
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "forceCloseSession", this.instance.seqNum, "reCreate", Boolean.valueOf(z));
        }
        for (String closeSessions : this.lastKeys) {
            closeSessions(closeSessions);
        }
        if (z) {
            checkAndStartSession();
        }
    }

    private boolean isNeedCheckSession() {
        if ((!GlobalAppRuntimeInfo.isAppBackground() || !AwcnConfig.isAccsSessionCreateForbiddenInBg()) && NetworkStatusHelper.isConnected()) {
            return true;
        }
        return false;
    }

    private void closeSessions(String str) {
        if (!TextUtils.isEmpty(str)) {
            ALog.d(TAG, "closeSessions", this.instance.seqNum, "host", str);
            this.instance.getSessionRequest(str).closeSessions(false);
        }
    }
}
