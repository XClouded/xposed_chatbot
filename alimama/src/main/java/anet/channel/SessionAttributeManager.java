package anet.channel;

import android.text.TextUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SessionAttributeManager {
    Map<String, Integer> publicKeyMap = new HashMap();
    Map<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap();

    SessionAttributeManager() {
    }

    /* access modifiers changed from: package-private */
    public void registerSessionInfo(SessionInfo sessionInfo) {
        if (sessionInfo == null) {
            throw new NullPointerException("info is null");
        } else if (!TextUtils.isEmpty(sessionInfo.host)) {
            this.sessionInfoMap.put(sessionInfo.host, sessionInfo);
        } else {
            throw new IllegalArgumentException("host cannot be null or empty");
        }
    }

    /* access modifiers changed from: package-private */
    public SessionInfo unregisterSessionInfo(String str) {
        return this.sessionInfoMap.remove(str);
    }

    /* access modifiers changed from: package-private */
    public SessionInfo getSessionInfo(String str) {
        return this.sessionInfoMap.get(str);
    }

    /* access modifiers changed from: package-private */
    public Collection<SessionInfo> getSessionInfos() {
        return this.sessionInfoMap.values();
    }

    /* access modifiers changed from: package-private */
    public void registerPublicKey(String str, int i) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.publicKeyMap) {
                this.publicKeyMap.put(str, Integer.valueOf(i));
            }
            return;
        }
        throw new IllegalArgumentException("host cannot be null or empty");
    }

    public int getPublicKey(String str) {
        Integer num;
        synchronized (this.publicKeyMap) {
            num = this.publicKeyMap.get(str);
        }
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }
}
