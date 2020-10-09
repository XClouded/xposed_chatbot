package anet.channel;

import android.content.Context;
import android.content.Intent;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.Event;
import anet.channel.entity.EventCb;
import anet.channel.entity.EventType;
import anet.channel.entity.SessionType;
import anet.channel.session.HttpSession;
import anet.channel.session.TnetSpdySession;
import anet.channel.statist.AlarmObject;
import anet.channel.statist.SessionConnStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.utils.Utils;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.Inet64Util;
import anet.channel.util.SessionSeq;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

class SessionRequest {
    private static final String TAG = "awcn.SessionRequest";
    /* access modifiers changed from: private */
    public HashMap<SessionGetCallback, SessionGetWaitTimeoutTask> callbackTaskMap = new HashMap<>();
    SessionConnStat connStat = null;
    volatile Session connectingSession;
    volatile boolean isConnecting = false;
    volatile boolean isToClose = false;
    private Object locked = new Object();
    private String mHost;
    private String mRealHost;
    /* access modifiers changed from: private */
    public SessionCenter sessionCenter;
    /* access modifiers changed from: private */
    public SessionInfo sessionInfo;
    /* access modifiers changed from: private */
    public SessionPool sessionPool;
    private volatile Future timeoutTask;

    private interface IConnCb {
        void onDisConnect(Session session, long j, int i);

        void onFailed(Session session, long j, int i, int i2);

        void onSuccess(Session session, long j);
    }

    SessionRequest(String str, SessionCenter sessionCenter2) {
        this.mHost = str;
        this.mRealHost = this.mHost.substring(this.mHost.indexOf(HttpConstant.SCHEME_SPLIT) + 3);
        this.sessionCenter = sessionCenter2;
        this.sessionInfo = sessionCenter2.attributeManager.getSessionInfo(this.mRealHost);
        this.sessionPool = sessionCenter2.sessionPool;
    }

    /* access modifiers changed from: protected */
    public String getHost() {
        return this.mHost;
    }

    /* access modifiers changed from: package-private */
    public void setConnecting(boolean z) {
        this.isConnecting = z;
        if (!z) {
            if (this.timeoutTask != null) {
                this.timeoutTask.cancel(true);
                this.timeoutTask = null;
            }
            this.connectingSession = null;
        }
    }

    private class ConnectTimeoutTask implements Runnable {
        String seq = null;

        ConnectTimeoutTask(String str) {
            this.seq = str;
        }

        public void run() {
            if (SessionRequest.this.isConnecting) {
                ALog.e(SessionRequest.TAG, "Connecting timeout!!! reset status!", this.seq, new Object[0]);
                SessionRequest.this.connStat.ret = 2;
                SessionRequest.this.connStat.totalTime = System.currentTimeMillis() - SessionRequest.this.connStat.start;
                if (SessionRequest.this.connectingSession != null) {
                    SessionRequest.this.connectingSession.tryNextWhenFail = false;
                    SessionRequest.this.connectingSession.close();
                    SessionRequest.this.connStat.syncValueFromSession(SessionRequest.this.connectingSession);
                }
                AppMonitor.getInstance().commitStat(SessionRequest.this.connStat);
                SessionRequest.this.setConnecting(false);
            }
        }
    }

    protected class SessionGetWaitTimeoutTask implements Runnable {
        SessionGetCallback cb = null;
        AtomicBoolean isFinish = new AtomicBoolean(false);

        protected SessionGetWaitTimeoutTask(SessionGetCallback sessionGetCallback) {
            this.cb = sessionGetCallback;
        }

        public void run() {
            if (this.isFinish.compareAndSet(false, true)) {
                ALog.e(SessionRequest.TAG, "get session timeout", (String) null, new Object[0]);
                synchronized (SessionRequest.this.callbackTaskMap) {
                    SessionRequest.this.callbackTaskMap.remove(this.cb);
                }
                this.cb.onSessionGetFail();
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(Unknown Source)
        	at java.util.ArrayList.get(Unknown Source)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    protected synchronized void start(android.content.Context r10, int r11, java.lang.String r12, anet.channel.SessionGetCallback r13, long r14) {
        /*
            r9 = this;
            monitor-enter(r9)
            anet.channel.SessionPool r0 = r9.sessionPool     // Catch:{ all -> 0x0132 }
            anet.channel.Session r0 = r0.getSession(r9, r11)     // Catch:{ all -> 0x0132 }
            r1 = 0
            if (r0 == 0) goto L_0x001a
            java.lang.String r10 = "awcn.SessionRequest"
            java.lang.String r11 = "Available Session exist!!!"
            java.lang.Object[] r14 = new java.lang.Object[r1]     // Catch:{ all -> 0x0132 }
            anet.channel.util.ALog.d(r10, r11, r12, r14)     // Catch:{ all -> 0x0132 }
            if (r13 == 0) goto L_0x0018
            r13.onSessionGetSuccess(r0)     // Catch:{ all -> 0x0132 }
        L_0x0018:
            monitor-exit(r9)
            return
        L_0x001a:
            boolean r0 = android.text.TextUtils.isEmpty(r12)     // Catch:{ all -> 0x0132 }
            if (r0 == 0) goto L_0x0025
            r12 = 0
            java.lang.String r12 = anet.channel.util.SessionSeq.createSequenceNo(r12)     // Catch:{ all -> 0x0132 }
        L_0x0025:
            java.lang.String r0 = "awcn.SessionRequest"
            java.lang.String r2 = "SessionRequest start"
            r3 = 4
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0132 }
            java.lang.String r5 = "host"
            r4[r1] = r5     // Catch:{ all -> 0x0132 }
            java.lang.String r5 = r9.mHost     // Catch:{ all -> 0x0132 }
            r6 = 1
            r4[r6] = r5     // Catch:{ all -> 0x0132 }
            java.lang.String r5 = "type"
            r7 = 2
            r4[r7] = r5     // Catch:{ all -> 0x0132 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r11)     // Catch:{ all -> 0x0132 }
            r8 = 3
            r4[r8] = r5     // Catch:{ all -> 0x0132 }
            anet.channel.util.ALog.d(r0, r2, r12, r4)     // Catch:{ all -> 0x0132 }
            boolean r0 = r9.isConnecting     // Catch:{ all -> 0x0132 }
            if (r0 == 0) goto L_0x007f
            java.lang.String r10 = "awcn.SessionRequest"
            java.lang.String r0 = "session connecting"
            java.lang.Object[] r2 = new java.lang.Object[r7]     // Catch:{ all -> 0x0132 }
            java.lang.String r3 = "host"
            r2[r1] = r3     // Catch:{ all -> 0x0132 }
            java.lang.String r1 = r9.getHost()     // Catch:{ all -> 0x0132 }
            r2[r6] = r1     // Catch:{ all -> 0x0132 }
            anet.channel.util.ALog.d(r10, r0, r12, r2)     // Catch:{ all -> 0x0132 }
            if (r13 == 0) goto L_0x007d
            int r10 = r9.getConnectingType()     // Catch:{ all -> 0x0132 }
            if (r10 != r11) goto L_0x007a
            anet.channel.SessionRequest$SessionGetWaitTimeoutTask r10 = new anet.channel.SessionRequest$SessionGetWaitTimeoutTask     // Catch:{ all -> 0x0132 }
            r10.<init>(r13)     // Catch:{ all -> 0x0132 }
            java.util.HashMap<anet.channel.SessionGetCallback, anet.channel.SessionRequest$SessionGetWaitTimeoutTask> r11 = r9.callbackTaskMap     // Catch:{ all -> 0x0132 }
            monitor-enter(r11)     // Catch:{ all -> 0x0132 }
            java.util.HashMap<anet.channel.SessionGetCallback, anet.channel.SessionRequest$SessionGetWaitTimeoutTask> r12 = r9.callbackTaskMap     // Catch:{ all -> 0x0077 }
            r12.put(r13, r10)     // Catch:{ all -> 0x0077 }
            monitor-exit(r11)     // Catch:{ all -> 0x0077 }
            java.util.concurrent.TimeUnit r11 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ all -> 0x0132 }
            anet.channel.thread.ThreadPoolExecutorFactory.submitScheduledTask(r10, r14, r11)     // Catch:{ all -> 0x0132 }
            goto L_0x007d
        L_0x0077:
            r10 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0077 }
            throw r10     // Catch:{ all -> 0x0132 }
        L_0x007a:
            r13.onSessionGetFail()     // Catch:{ all -> 0x0132 }
        L_0x007d:
            monitor-exit(r9)
            return
        L_0x007f:
            r9.setConnecting(r6)     // Catch:{ all -> 0x0132 }
            anet.channel.SessionRequest$ConnectTimeoutTask r0 = new anet.channel.SessionRequest$ConnectTimeoutTask     // Catch:{ all -> 0x0132 }
            r0.<init>(r12)     // Catch:{ all -> 0x0132 }
            r4 = 45
            java.util.concurrent.TimeUnit r2 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ all -> 0x0132 }
            java.util.concurrent.Future r0 = anet.channel.thread.ThreadPoolExecutorFactory.submitScheduledTask(r0, r4, r2)     // Catch:{ all -> 0x0132 }
            r9.timeoutTask = r0     // Catch:{ all -> 0x0132 }
            anet.channel.statist.SessionConnStat r0 = new anet.channel.statist.SessionConnStat     // Catch:{ all -> 0x0132 }
            r0.<init>()     // Catch:{ all -> 0x0132 }
            r9.connStat = r0     // Catch:{ all -> 0x0132 }
            anet.channel.statist.SessionConnStat r0 = r9.connStat     // Catch:{ all -> 0x0132 }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0132 }
            r0.start = r4     // Catch:{ all -> 0x0132 }
            boolean r0 = anet.channel.status.NetworkStatusHelper.isConnected()     // Catch:{ all -> 0x0132 }
            if (r0 != 0) goto L_0x00ce
            boolean r10 = anet.channel.util.ALog.isPrintLog(r6)     // Catch:{ all -> 0x0132 }
            if (r10 == 0) goto L_0x00c3
            java.lang.String r10 = "awcn.SessionRequest"
            java.lang.String r11 = "network is not available, can't create session"
            java.lang.Object[] r13 = new java.lang.Object[r7]     // Catch:{ all -> 0x0132 }
            java.lang.String r14 = "isConnected"
            r13[r1] = r14     // Catch:{ all -> 0x0132 }
            boolean r14 = anet.channel.status.NetworkStatusHelper.isConnected()     // Catch:{ all -> 0x0132 }
            java.lang.Boolean r14 = java.lang.Boolean.valueOf(r14)     // Catch:{ all -> 0x0132 }
            r13[r6] = r14     // Catch:{ all -> 0x0132 }
            anet.channel.util.ALog.d(r10, r11, r12, r13)     // Catch:{ all -> 0x0132 }
        L_0x00c3:
            r9.finish()     // Catch:{ all -> 0x0132 }
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ all -> 0x0132 }
            java.lang.String r11 = "no network"
            r10.<init>(r11)     // Catch:{ all -> 0x0132 }
            throw r10     // Catch:{ all -> 0x0132 }
        L_0x00ce:
            java.util.List r0 = r9.getAvailStrategy(r11, r12)     // Catch:{ all -> 0x0132 }
            boolean r2 = r0.isEmpty()     // Catch:{ all -> 0x0132 }
            if (r2 != 0) goto L_0x010c
            java.util.List r11 = r9.getConnInfoList(r0, r12)     // Catch:{ all -> 0x0132 }
            java.lang.Object r12 = r11.remove(r1)     // Catch:{ Throwable -> 0x0107 }
            anet.channel.entity.ConnInfo r12 = (anet.channel.entity.ConnInfo) r12     // Catch:{ Throwable -> 0x0107 }
            anet.channel.SessionRequest$ConnCb r0 = new anet.channel.SessionRequest$ConnCb     // Catch:{ Throwable -> 0x0107 }
            r0.<init>(r10, r11, r12)     // Catch:{ Throwable -> 0x0107 }
            java.lang.String r11 = r12.getSeq()     // Catch:{ Throwable -> 0x0107 }
            r9.createSession(r10, r12, r0, r11)     // Catch:{ Throwable -> 0x0107 }
            if (r13 == 0) goto L_0x010a
            anet.channel.SessionRequest$SessionGetWaitTimeoutTask r10 = new anet.channel.SessionRequest$SessionGetWaitTimeoutTask     // Catch:{ Throwable -> 0x0107 }
            r10.<init>(r13)     // Catch:{ Throwable -> 0x0107 }
            java.util.HashMap<anet.channel.SessionGetCallback, anet.channel.SessionRequest$SessionGetWaitTimeoutTask> r11 = r9.callbackTaskMap     // Catch:{ Throwable -> 0x0107 }
            monitor-enter(r11)     // Catch:{ Throwable -> 0x0107 }
            java.util.HashMap<anet.channel.SessionGetCallback, anet.channel.SessionRequest$SessionGetWaitTimeoutTask> r12 = r9.callbackTaskMap     // Catch:{ all -> 0x0104 }
            r12.put(r13, r10)     // Catch:{ all -> 0x0104 }
            monitor-exit(r11)     // Catch:{ all -> 0x0104 }
            java.util.concurrent.TimeUnit r11 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ Throwable -> 0x0107 }
            anet.channel.thread.ThreadPoolExecutorFactory.submitScheduledTask(r10, r14, r11)     // Catch:{ Throwable -> 0x0107 }
            goto L_0x010a
        L_0x0104:
            r10 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0104 }
            throw r10     // Catch:{ Throwable -> 0x0107 }
        L_0x0107:
            r9.finish()     // Catch:{ all -> 0x0132 }
        L_0x010a:
            monitor-exit(r9)
            return
        L_0x010c:
            java.lang.String r10 = "awcn.SessionRequest"
            java.lang.String r13 = "no avalible strategy, can't create session"
            java.lang.Object[] r14 = new java.lang.Object[r3]     // Catch:{ all -> 0x0132 }
            java.lang.String r15 = "host"
            r14[r1] = r15     // Catch:{ all -> 0x0132 }
            java.lang.String r15 = r9.mHost     // Catch:{ all -> 0x0132 }
            r14[r6] = r15     // Catch:{ all -> 0x0132 }
            java.lang.String r15 = "type"
            r14[r7] = r15     // Catch:{ all -> 0x0132 }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ all -> 0x0132 }
            r14[r8] = r11     // Catch:{ all -> 0x0132 }
            anet.channel.util.ALog.i(r10, r13, r12, r14)     // Catch:{ all -> 0x0132 }
            r9.finish()     // Catch:{ all -> 0x0132 }
            anet.channel.NoAvailStrategyException r10 = new anet.channel.NoAvailStrategyException     // Catch:{ all -> 0x0132 }
            java.lang.String r11 = "no avalible strategy"
            r10.<init>(r11)     // Catch:{ all -> 0x0132 }
            throw r10     // Catch:{ all -> 0x0132 }
        L_0x0132:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.SessionRequest.start(android.content.Context, int, java.lang.String, anet.channel.SessionGetCallback, long):void");
    }

    class ConnCb implements IConnCb {
        private ConnInfo connInfo;
        /* access modifiers changed from: private */
        public Context context;
        boolean isHandleFinish = false;
        private List<ConnInfo> strategys;

        ConnCb(Context context2, List<ConnInfo> list, ConnInfo connInfo2) {
            this.context = context2;
            this.strategys = list;
            this.connInfo = connInfo2;
        }

        public void onFailed(Session session, long j, int i, int i2) {
            if (ALog.isPrintLog(1)) {
                ALog.d(SessionRequest.TAG, "Connect failed", this.connInfo.getSeq(), "session", session, "host", SessionRequest.this.getHost(), "isHandleFinish", Boolean.valueOf(this.isHandleFinish));
            }
            if (SessionRequest.this.isToClose) {
                SessionRequest.this.isToClose = false;
            } else if (!this.isHandleFinish) {
                this.isHandleFinish = true;
                SessionRequest.this.sessionPool.remove(SessionRequest.this, session);
                if (!session.tryNextWhenFail || !NetworkStatusHelper.isConnected() || this.strategys.isEmpty()) {
                    SessionRequest.this.finish();
                    SessionRequest.this.commitFail(session, i, i2);
                    synchronized (SessionRequest.this.callbackTaskMap) {
                        for (Map.Entry entry : SessionRequest.this.callbackTaskMap.entrySet()) {
                            SessionGetWaitTimeoutTask sessionGetWaitTimeoutTask = (SessionGetWaitTimeoutTask) entry.getValue();
                            if (sessionGetWaitTimeoutTask.isFinish.compareAndSet(false, true)) {
                                ThreadPoolExecutorFactory.removeScheduleTask(sessionGetWaitTimeoutTask);
                                ((SessionGetCallback) entry.getKey()).onSessionGetFail();
                            }
                        }
                        SessionRequest.this.callbackTaskMap.clear();
                    }
                    return;
                }
                if (ALog.isPrintLog(1)) {
                    ALog.d(SessionRequest.TAG, "use next connInfo to create session", this.connInfo.getSeq(), "host", SessionRequest.this.getHost());
                }
                if (this.connInfo.retryTime == this.connInfo.maxRetryTime && (i2 == -2003 || i2 == -2410)) {
                    ListIterator<ConnInfo> listIterator = this.strategys.listIterator();
                    while (listIterator.hasNext()) {
                        if (session.getIp().equals(listIterator.next().strategy.getIp())) {
                            listIterator.remove();
                        }
                    }
                }
                if (Utils.isIPV6Address(session.getIp())) {
                    ListIterator<ConnInfo> listIterator2 = this.strategys.listIterator();
                    while (listIterator2.hasNext()) {
                        if (Utils.isIPV6Address(listIterator2.next().strategy.getIp())) {
                            listIterator2.remove();
                        }
                    }
                }
                if (this.strategys.isEmpty()) {
                    SessionRequest.this.finish();
                    SessionRequest.this.commitFail(session, i, i2);
                    return;
                }
                ConnInfo remove = this.strategys.remove(0);
                SessionRequest.this.createSession(this.context, remove, new ConnCb(this.context, this.strategys, remove), remove.getSeq());
            }
        }

        public void onSuccess(Session session, long j) {
            ALog.d(SessionRequest.TAG, "Connect Success", this.connInfo.getSeq(), "session", session, "host", SessionRequest.this.getHost());
            try {
                if (SessionRequest.this.isToClose) {
                    SessionRequest.this.isToClose = false;
                    session.close(false);
                    SessionRequest.this.finish();
                    return;
                }
                SessionRequest.this.sessionPool.add(SessionRequest.this, session);
                SessionRequest.this.commitSuccess(session);
                synchronized (SessionRequest.this.callbackTaskMap) {
                    for (Map.Entry entry : SessionRequest.this.callbackTaskMap.entrySet()) {
                        SessionGetWaitTimeoutTask sessionGetWaitTimeoutTask = (SessionGetWaitTimeoutTask) entry.getValue();
                        if (sessionGetWaitTimeoutTask.isFinish.compareAndSet(false, true)) {
                            ThreadPoolExecutorFactory.removeScheduleTask(sessionGetWaitTimeoutTask);
                            ((SessionGetCallback) entry.getKey()).onSessionGetSuccess(session);
                        }
                    }
                    SessionRequest.this.callbackTaskMap.clear();
                }
                SessionRequest.this.finish();
            } catch (Exception e) {
                try {
                    ALog.e(SessionRequest.TAG, "[onSuccess]:", this.connInfo.getSeq(), e, new Object[0]);
                } catch (Throwable th) {
                    SessionRequest.this.finish();
                    throw th;
                }
            }
        }

        public void onDisConnect(final Session session, long j, int i) {
            boolean isAppBackground = GlobalAppRuntimeInfo.isAppBackground();
            ALog.d(SessionRequest.TAG, "Connect Disconnect", this.connInfo.getSeq(), "session", session, "host", SessionRequest.this.getHost(), "appIsBg", Boolean.valueOf(isAppBackground), "isHandleFinish", Boolean.valueOf(this.isHandleFinish));
            SessionRequest.this.sessionPool.remove(SessionRequest.this, session);
            if (!this.isHandleFinish) {
                this.isHandleFinish = true;
                if (session.autoReCreate) {
                    if (isAppBackground && (SessionRequest.this.sessionInfo == null || !SessionRequest.this.sessionInfo.isAccs || AwcnConfig.isAccsSessionCreateForbiddenInBg())) {
                        ALog.e(SessionRequest.TAG, "[onDisConnect]app background, don't Recreate", this.connInfo.getSeq(), "session", session);
                    } else if (!NetworkStatusHelper.isConnected()) {
                        ALog.e(SessionRequest.TAG, "[onDisConnect]no network, don't Recreate", this.connInfo.getSeq(), "session", session);
                    } else {
                        try {
                            ALog.d(SessionRequest.TAG, "session disconnected, try to recreate session", this.connInfo.getSeq(), new Object[0]);
                            int i2 = 10000;
                            if (SessionRequest.this.sessionInfo != null && SessionRequest.this.sessionInfo.isAccs) {
                                i2 = AwcnConfig.getAccsReconnectionDelayPeriod();
                            }
                            AnonymousClass1 r10 = new Runnable() {
                                public void run() {
                                    try {
                                        SessionRequest.this.start(ConnCb.this.context, session.getConnType().getType(), SessionSeq.createSequenceNo(SessionRequest.this.sessionCenter.seqNum), (SessionGetCallback) null, 0);
                                    } catch (Exception unused) {
                                    }
                                }
                            };
                            double random = Math.random();
                            double d = (double) i2;
                            Double.isNaN(d);
                            ThreadPoolExecutorFactory.submitScheduledTask(r10, (long) (random * d), TimeUnit.MILLISECONDS);
                        } catch (Exception unused) {
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void commitSuccess(Session session) {
        AlarmObject alarmObject = new AlarmObject();
        alarmObject.module = "networkPrefer";
        alarmObject.modulePoint = "policy";
        alarmObject.arg = this.mHost;
        alarmObject.isSuccess = true;
        AppMonitor.getInstance().commitAlarm(alarmObject);
        this.connStat.syncValueFromSession(session);
        this.connStat.ret = 1;
        this.connStat.totalTime = System.currentTimeMillis() - this.connStat.start;
        AppMonitor.getInstance().commitStat(this.connStat);
    }

    /* access modifiers changed from: private */
    public void commitFail(Session session, int i, int i2) {
        if (256 == i && i2 != -2613 && i2 != -2601) {
            AlarmObject alarmObject = new AlarmObject();
            alarmObject.module = "networkPrefer";
            alarmObject.modulePoint = "policy";
            alarmObject.arg = this.mHost;
            alarmObject.errorCode = String.valueOf(i2);
            alarmObject.isSuccess = false;
            AppMonitor.getInstance().commitAlarm(alarmObject);
            this.connStat.ret = 0;
            this.connStat.appendErrorTrace(i2);
            this.connStat.errorCode = String.valueOf(i2);
            this.connStat.totalTime = System.currentTimeMillis() - this.connStat.start;
            this.connStat.syncValueFromSession(session);
            AppMonitor.getInstance().commitStat(this.connStat);
        }
    }

    private List<IConnStrategy> getAvailStrategy(int i, String str) {
        List<IConnStrategy> list;
        try {
            HttpUrl parse = HttpUrl.parse(getHost());
            if (parse == null) {
                return Collections.EMPTY_LIST;
            }
            list = StrategyCenter.getInstance().getConnStrategyListByHost(parse.host());
            try {
                if (!list.isEmpty()) {
                    boolean equalsIgnoreCase = "https".equalsIgnoreCase(parse.scheme());
                    boolean isIPv4OnlyNetwork = Inet64Util.isIPv4OnlyNetwork();
                    ListIterator<IConnStrategy> listIterator = list.listIterator();
                    while (listIterator.hasNext()) {
                        IConnStrategy next = listIterator.next();
                        ConnType valueOf = ConnType.valueOf(next.getProtocol());
                        if (valueOf != null) {
                            if (!(valueOf.isSSL() == equalsIgnoreCase && (i == SessionType.ALL || valueOf.getType() == i))) {
                                listIterator.remove();
                            }
                            if (isIPv4OnlyNetwork && Utils.isIPV6Address(next.getIp())) {
                                listIterator.remove();
                            }
                        }
                    }
                }
                if (ALog.isPrintLog(1)) {
                    ALog.d(TAG, "[getAvailStrategy]", str, "strategies", list);
                }
            } catch (Throwable th) {
                th = th;
                ALog.e(TAG, "", str, th, new Object[0]);
                return list;
            }
            return list;
        } catch (Throwable th2) {
            th = th2;
            list = Collections.EMPTY_LIST;
            ALog.e(TAG, "", str, th, new Object[0]);
            return list;
        }
    }

    private List<ConnInfo> getConnInfoList(List<IConnStrategy> list, String str) {
        if (list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (i < list.size()) {
            IConnStrategy iConnStrategy = list.get(i);
            int retryTimes = iConnStrategy.getRetryTimes();
            int i3 = i2;
            for (int i4 = 0; i4 <= retryTimes; i4++) {
                i3++;
                String host = getHost();
                ConnInfo connInfo = new ConnInfo(host, str + "_" + i3, iConnStrategy);
                connInfo.retryTime = i4;
                connInfo.maxRetryTime = retryTimes;
                arrayList.add(connInfo);
            }
            i++;
            i2 = i3;
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void createSession(Context context, ConnInfo connInfo, IConnCb iConnCb, String str) {
        ConnType connType = connInfo.getConnType();
        if (context == null || connType.isHttpType()) {
            this.connectingSession = new HttpSession(context, connInfo);
        } else {
            TnetSpdySession tnetSpdySession = new TnetSpdySession(context, connInfo);
            tnetSpdySession.initConfig(this.sessionCenter.config);
            tnetSpdySession.initSessionInfo(this.sessionInfo);
            tnetSpdySession.setTnetPublicKey(this.sessionCenter.attributeManager.getPublicKey(this.mRealHost));
            this.connectingSession = tnetSpdySession;
        }
        ALog.i(TAG, "create connection...", str, HttpConstant.HOST, getHost(), "Type", connInfo.getConnType(), "IP", connInfo.getIp(), "Port", Integer.valueOf(connInfo.getPort()), "heartbeat", Integer.valueOf(connInfo.getHeartbeat()), "session", this.connectingSession);
        registerEvent(this.connectingSession, iConnCb, System.currentTimeMillis(), str);
        this.connectingSession.connect();
        this.connStat.retryTimes++;
        this.connStat.startConnect = System.currentTimeMillis();
        if (this.connStat.retryTimes == 0) {
            this.connStat.putExtra("firstIp", connInfo.getIp());
        }
    }

    private void registerEvent(final Session session, final IConnCb iConnCb, final long j, String str) {
        if (iConnCb != null) {
            session.registerEventcb(EventType.ALL, new EventCb() {
                public void onEvent(Session session, int i, Event event) {
                    String str;
                    Session session2 = session;
                    int i2 = i;
                    Event event2 = event;
                    if (session2 != null) {
                        int i3 = event2 == null ? 0 : event2.errorCode;
                        if (event2 == null) {
                            str = "";
                        } else {
                            str = event2.errorDetail;
                        }
                        if (i2 == 2) {
                            ALog.d(SessionRequest.TAG, (String) null, session2 != null ? session2.mSeq : null, "Session", session2, "EventType", Integer.valueOf(i), DXMonitorConstant.DX_MONITOR_EVENT, event2);
                            SessionRequest.this.sendConnectInfoBroadCastToAccs(session2, i3, str);
                            if (SessionRequest.this.sessionPool.containsValue(SessionRequest.this, session2)) {
                                iConnCb.onDisConnect(session2, j, i2);
                            } else {
                                iConnCb.onFailed(session, j, i, i3);
                            }
                        } else if (i2 == 256) {
                            ALog.d(SessionRequest.TAG, (String) null, session2 != null ? session2.mSeq : null, "Session", session2, "EventType", Integer.valueOf(i), DXMonitorConstant.DX_MONITOR_EVENT, event2);
                            SessionRequest.this.sendConnectInfoBroadCastToAccs(session2, i3, str);
                            iConnCb.onFailed(session, j, i, i3);
                        } else if (i2 == 512) {
                            ALog.d(SessionRequest.TAG, (String) null, session2 != null ? session2.mSeq : null, "Session", session2, "EventType", Integer.valueOf(i), DXMonitorConstant.DX_MONITOR_EVENT, event2);
                            SessionRequest.this.sendConnectInfoBroadCastToAccs(session2, 0, (String) null);
                            iConnCb.onSuccess(session2, j);
                        }
                    }
                }
            });
            session.registerEventcb(1792, new EventCb() {
                public void onEvent(Session session, int i, Event event) {
                    ALog.d(SessionRequest.TAG, "Receive session event", (String) null, BindingXConstants.KEY_EVENT_TYPE, Integer.valueOf(i));
                    ConnEvent connEvent = new ConnEvent();
                    if (i == 512) {
                        connEvent.isSuccess = true;
                    }
                    if (SessionRequest.this.sessionInfo != null) {
                        connEvent.isAccs = SessionRequest.this.sessionInfo.isAccs;
                    }
                    StrategyCenter.getInstance().notifyConnEvent(session.getRealHost(), session.getConnStrategy(), connEvent);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void closeSessions(boolean z) {
        ALog.d(TAG, "closeSessions", this.sessionCenter.seqNum, "host", this.mHost, "autoCreate", Boolean.valueOf(z));
        if (!z && this.connectingSession != null) {
            this.connectingSession.tryNextWhenFail = false;
            this.connectingSession.close(false);
        }
        List<Session> sessions = this.sessionPool.getSessions(this);
        if (sessions != null) {
            for (Session next : sessions) {
                if (next != null) {
                    next.close(z);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void reCreateSession(String str) {
        ALog.d(TAG, "reCreateSession", str, "host", this.mHost);
        closeSessions(true);
    }

    /* access modifiers changed from: protected */
    public void await(long j) throws InterruptedException, TimeoutException {
        ALog.d(TAG, "[await]", (String) null, "timeoutMs", Long.valueOf(j));
        if (j > 0) {
            synchronized (this.locked) {
                long currentTimeMillis = System.currentTimeMillis() + j;
                while (true) {
                    if (!this.isConnecting) {
                        break;
                    }
                    long currentTimeMillis2 = System.currentTimeMillis();
                    if (currentTimeMillis2 >= currentTimeMillis) {
                        break;
                    }
                    this.locked.wait(currentTimeMillis - currentTimeMillis2);
                }
                if (this.isConnecting) {
                    throw new TimeoutException();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getConnectingType() {
        Session session = this.connectingSession;
        if (session != null) {
            return session.mConnType.getType();
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public void finish() {
        setConnecting(false);
        synchronized (this.locked) {
            this.locked.notifyAll();
        }
    }

    /* access modifiers changed from: private */
    public void sendConnectInfoBroadCastToAccs(Session session, int i, String str) {
        Context context = GlobalAppRuntimeInfo.getContext();
        if (context != null && this.sessionInfo != null && this.sessionInfo.isAccs) {
            try {
                Intent intent = new Intent(Constants.ACTION_RECEIVE);
                intent.setPackage(context.getPackageName());
                intent.setClassName(context, AdapterUtilityImpl.msgService);
                intent.putExtra("command", 103);
                intent.putExtra("host", session.getHost());
                intent.putExtra(Constants.KEY_CENTER_HOST, true);
                boolean isAvailable = session.isAvailable();
                if (!isAvailable) {
                    intent.putExtra("errorCode", i);
                    intent.putExtra(Constants.KEY_ERROR_DETAIL, str);
                }
                intent.putExtra(Constants.KEY_CONNECT_AVAILABLE, isAvailable);
                intent.putExtra(Constants.KEY_TYPE_INAPP, true);
                context.startService(intent);
            } catch (Throwable th) {
                ALog.e(TAG, "sendConnectInfoBroadCastToAccs", (String) null, th, new Object[0]);
            }
        }
    }
}
