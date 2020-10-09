package anet.channel.detect;

import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.ConnType;
import anet.channel.request.Request;
import anet.channel.session.HttpConnector;
import anet.channel.statist.HorseRaceStat;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.TlsSniSocketFactory;
import com.taobao.android.tlog.protocol.model.joint.point.BackgroundJointPoint;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.android.netutil.PingResponse;
import org.android.netutil.PingTask;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

class HorseRaceDetector {
    private static final String TAG = "anet.HorseRaceDetector";
    /* access modifiers changed from: private */
    public Condition enterBackground = this.lock.newCondition();
    /* access modifiers changed from: private */
    public ReentrantLock lock = new ReentrantLock();
    /* access modifiers changed from: private */
    public Runnable runnable = new Runnable() {
        public void run() {
            Map.Entry entry;
            ALog.e(HorseRaceDetector.TAG, "network detect thread start", (String) null, new Object[0]);
            SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
            while (true) {
                HorseRaceDetector.this.lock.lock();
                try {
                    if (!GlobalAppRuntimeInfo.isAppBackground()) {
                        HorseRaceDetector.this.enterBackground.await();
                    }
                    if (HorseRaceDetector.this.tasks.isEmpty()) {
                        HorseRaceDetector.this.taskArrived.await();
                    }
                } catch (Exception unused) {
                } catch (Throwable th) {
                    HorseRaceDetector.this.lock.unlock();
                    throw th;
                }
                HorseRaceDetector.this.lock.unlock();
                while (GlobalAppRuntimeInfo.isAppBackground()) {
                    synchronized (HorseRaceDetector.this.tasks) {
                        if (!AwcnConfig.isHorseRaceEnable()) {
                            HorseRaceDetector.this.tasks.clear();
                            entry = null;
                        } else {
                            entry = HorseRaceDetector.this.tasks.pollFirstEntry();
                        }
                    }
                    if (entry == null) {
                        break;
                    }
                    try {
                        HorseRaceDetector.this.startTask((StrategyResultParser.HrTask) entry.getValue());
                    } catch (Exception e) {
                        ALog.e(HorseRaceDetector.TAG, "start hr task failed", (String) null, e, new Object[0]);
                    }
                }
            }
        }
    };
    private AtomicInteger seq = new AtomicInteger(1);
    /* access modifiers changed from: private */
    public Condition taskArrived = this.lock.newCondition();
    /* access modifiers changed from: private */
    public TreeMap<String, StrategyResultParser.HrTask> tasks = new TreeMap<>();
    /* access modifiers changed from: private */
    public volatile Thread thread = null;

    HorseRaceDetector() {
    }

    public void register() {
        StrategyCenter.getInstance().registerListener(new IStrategyListener() {
            public void onStrategyUpdated(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
                ALog.i(HorseRaceDetector.TAG, "onStrategyUpdated", (String) null, new Object[0]);
                if (AwcnConfig.isHorseRaceEnable() && httpDnsResponse.hrTasks != null && httpDnsResponse.hrTasks.length != 0) {
                    if (HorseRaceDetector.this.thread == null) {
                        Thread unused = HorseRaceDetector.this.thread = new Thread(HorseRaceDetector.this.runnable);
                        HorseRaceDetector.this.thread.setName("AWCN HR");
                        HorseRaceDetector.this.thread.start();
                        ALog.i(HorseRaceDetector.TAG, "start horse race thread", (String) null, new Object[0]);
                    }
                    synchronized (HorseRaceDetector.this.tasks) {
                        for (StrategyResultParser.HrTask hrTask : httpDnsResponse.hrTasks) {
                            HorseRaceDetector.this.tasks.put(hrTask.host, hrTask);
                        }
                    }
                    HorseRaceDetector.this.lock.lock();
                    try {
                        HorseRaceDetector.this.taskArrived.signal();
                    } finally {
                        HorseRaceDetector.this.lock.unlock();
                    }
                }
            }
        });
        AppLifecycle.registerLifecycleListener(new AppLifecycle.AppLifecycleListener() {
            public void forground() {
            }

            public void background() {
                ALog.i(HorseRaceDetector.TAG, BackgroundJointPoint.TYPE, (String) null, new Object[0]);
                HorseRaceDetector.this.lock.lock();
                try {
                    HorseRaceDetector.this.enterBackground.signal();
                } finally {
                    HorseRaceDetector.this.lock.unlock();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void startTask(StrategyResultParser.HrTask hrTask) {
        if (hrTask.strategies != null && hrTask.strategies.length != 0) {
            String str = hrTask.host;
            for (StrategyResultParser.Strategy strategy : hrTask.strategies) {
                String str2 = strategy.aisles.protocol;
                if (str2.equalsIgnoreCase("http") || str2.equalsIgnoreCase("https")) {
                    startShortLinkTask(str, strategy);
                } else if (str2.equalsIgnoreCase(ConnType.HTTP2) || str2.equalsIgnoreCase(ConnType.SPDY) || str2.equalsIgnoreCase(ConnType.QUIC)) {
                    startLongLinkTask(str, strategy);
                } else if (str2.equalsIgnoreCase("tcp")) {
                    startTcpTask(str, strategy);
                }
            }
        }
    }

    private void startShortLinkTask(String str, StrategyResultParser.Strategy strategy) {
        HttpUrl parse = HttpUrl.parse(strategy.aisles.protocol + HttpConstant.SCHEME_SPLIT + str + strategy.path);
        if (parse != null) {
            int i = 1;
            ALog.i(TAG, "startShortLinkTask", (String) null, "url", parse);
            Request.Builder sslSocketFactory = new Request.Builder().setUrl(parse).addHeader("Connection", "close").setConnectTimeout(strategy.aisles.cto).setReadTimeout(strategy.aisles.rto).setRedirectEnable(false).setSslSocketFactory(new TlsSniSocketFactory(str));
            Request build = sslSocketFactory.setSeq("HR" + this.seq.getAndIncrement()).build();
            build.setDnsOptimize(strategy.ip, strategy.aisles.port);
            long currentTimeMillis = System.currentTimeMillis();
            HttpConnector.Response connect = HttpConnector.connect(build);
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            HorseRaceStat horseRaceStat = new HorseRaceStat(str, strategy);
            horseRaceStat.connTime = currentTimeMillis2;
            if (connect.httpCode <= 0) {
                horseRaceStat.connErrorCode = connect.httpCode;
            } else {
                horseRaceStat.connRet = 1;
                if (connect.httpCode != 200) {
                    i = 0;
                }
                horseRaceStat.reqRet = i;
                horseRaceStat.reqErrorCode = connect.httpCode;
                horseRaceStat.reqTime = horseRaceStat.connTime;
            }
            startPing6Task(strategy.ip, horseRaceStat);
            AppMonitor.getInstance().commitStat(horseRaceStat);
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x00dc */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startLongLinkTask(java.lang.String r17, anet.channel.strategy.StrategyResultParser.Strategy r18) {
        /*
            r16 = this;
            r9 = r16
            r0 = r17
            r10 = r18
            anet.channel.strategy.StrategyResultParser$Aisles r1 = r10.aisles
            anet.channel.strategy.ConnProtocol r1 = anet.channel.strategy.ConnProtocol.valueOf(r1)
            anet.channel.entity.ConnType r2 = anet.channel.entity.ConnType.valueOf(r1)
            if (r2 != 0) goto L_0x0013
            return
        L_0x0013:
            java.lang.String r3 = "anet.HorseRaceDetector"
            java.lang.String r4 = "startLongLinkTask"
            r5 = 0
            r6 = 8
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r7 = "host"
            r11 = 0
            r6[r11] = r7
            r7 = 1
            r6[r7] = r0
            r7 = 2
            java.lang.String r8 = "ip"
            r6[r7] = r8
            r7 = 3
            java.lang.String r8 = r10.ip
            r6[r7] = r8
            r7 = 4
            java.lang.String r8 = "port"
            r6[r7] = r8
            r7 = 5
            anet.channel.strategy.StrategyResultParser$Aisles r8 = r10.aisles
            int r8 = r8.port
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r6[r7] = r8
            r7 = 6
            java.lang.String r8 = "protocol"
            r6[r7] = r8
            r7 = 7
            r6[r7] = r1
            anet.channel.util.ALog.i(r3, r4, r5, r6)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "HR"
            r3.append(r4)
            java.util.concurrent.atomic.AtomicInteger r4 = r9.seq
            int r4 = r4.getAndIncrement()
            r3.append(r4)
            java.lang.String r6 = r3.toString()
            anet.channel.session.TnetSpdySession r12 = new anet.channel.session.TnetSpdySession
            android.content.Context r3 = anet.channel.GlobalAppRuntimeInfo.getContext()
            anet.channel.entity.ConnInfo r4 = new anet.channel.entity.ConnInfo
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            boolean r2 = r2.isSSL()
            if (r2 == 0) goto L_0x0076
            java.lang.String r2 = "https://"
            goto L_0x0078
        L_0x0076:
            java.lang.String r2 = "http://"
        L_0x0078:
            r5.append(r2)
            r5.append(r0)
            java.lang.String r2 = r5.toString()
            anet.channel.strategy.IConnStrategy r1 = makeConnStrategy(r1, r10)
            r4.<init>(r2, r6, r1)
            r12.<init>(r3, r4)
            anet.channel.statist.HorseRaceStat r13 = new anet.channel.statist.HorseRaceStat
            r13.<init>(r0, r10)
            long r14 = java.lang.System.currentTimeMillis()
            r0 = 257(0x101, float:3.6E-43)
            anet.channel.detect.HorseRaceDetector$4 r8 = new anet.channel.detect.HorseRaceDetector$4
            r1 = r8
            r2 = r16
            r3 = r13
            r4 = r14
            r7 = r18
            r11 = r8
            r8 = r12
            r1.<init>(r3, r4, r6, r7, r8)
            r12.registerEventcb(r0, r11)
            r12.connect()
            monitor-enter(r13)
            anet.channel.strategy.StrategyResultParser$Aisles r0 = r10.aisles     // Catch:{ InterruptedException -> 0x00dc }
            int r0 = r0.cto     // Catch:{ InterruptedException -> 0x00dc }
            if (r0 != 0) goto L_0x00b5
            r0 = 10000(0x2710, float:1.4013E-41)
            goto L_0x00b9
        L_0x00b5:
            anet.channel.strategy.StrategyResultParser$Aisles r0 = r10.aisles     // Catch:{ InterruptedException -> 0x00dc }
            int r0 = r0.cto     // Catch:{ InterruptedException -> 0x00dc }
        L_0x00b9:
            long r0 = (long) r0     // Catch:{ InterruptedException -> 0x00dc }
            r13.wait(r0)     // Catch:{ InterruptedException -> 0x00dc }
            long r0 = r13.connTime     // Catch:{ InterruptedException -> 0x00dc }
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x00cd
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedException -> 0x00dc }
            r2 = 0
            long r0 = r0 - r14
            r13.connTime = r0     // Catch:{ InterruptedException -> 0x00dc }
        L_0x00cd:
            java.lang.String r0 = r10.ip     // Catch:{ InterruptedException -> 0x00dc }
            r9.startPing6Task(r0, r13)     // Catch:{ InterruptedException -> 0x00dc }
            anet.channel.appmonitor.IAppMonitor r0 = anet.channel.appmonitor.AppMonitor.getInstance()     // Catch:{ InterruptedException -> 0x00dc }
            r0.commitStat(r13)     // Catch:{ InterruptedException -> 0x00dc }
            goto L_0x00dc
        L_0x00da:
            r0 = move-exception
            goto L_0x00e2
        L_0x00dc:
            monitor-exit(r13)     // Catch:{ all -> 0x00da }
            r0 = 0
            r12.close(r0)
            return
        L_0x00e2:
            monitor-exit(r13)     // Catch:{ all -> 0x00da }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.detect.HorseRaceDetector.startLongLinkTask(java.lang.String, anet.channel.strategy.StrategyResultParser$Strategy):void");
    }

    private static IConnStrategy makeConnStrategy(final ConnProtocol connProtocol, final StrategyResultParser.Strategy strategy) {
        return new IConnStrategy() {
            public int getHeartbeat() {
                return 0;
            }

            public int getIpSource() {
                return 2;
            }

            public int getIpType() {
                return 1;
            }

            public int getRetryTimes() {
                return 0;
            }

            public String getIp() {
                return strategy.ip;
            }

            public int getPort() {
                return strategy.aisles.port;
            }

            public ConnProtocol getProtocol() {
                return connProtocol;
            }

            public int getConnectionTimeout() {
                return strategy.aisles.cto;
            }

            public int getReadTimeout() {
                return strategy.aisles.rto;
            }
        };
    }

    private void startTcpTask(String str, StrategyResultParser.Strategy strategy) {
        String str2 = "HR" + this.seq.getAndIncrement();
        ALog.i(TAG, "startTcpTask", str2, "ip", strategy.ip, "port", Integer.valueOf(strategy.aisles.port));
        HorseRaceStat horseRaceStat = new HorseRaceStat(str, strategy);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            Socket socket = new Socket(strategy.ip, strategy.aisles.port);
            socket.setSoTimeout(strategy.aisles.cto == 0 ? 10000 : strategy.aisles.cto);
            ALog.i(TAG, "socket connect success", str2, new Object[0]);
            horseRaceStat.connRet = 1;
            horseRaceStat.connTime = System.currentTimeMillis() - currentTimeMillis;
            socket.close();
        } catch (IOException unused) {
            horseRaceStat.connTime = System.currentTimeMillis() - currentTimeMillis;
            horseRaceStat.connErrorCode = ErrorConstant.ERROR_IO_EXCEPTION;
        }
        AppMonitor.getInstance().commitStat(horseRaceStat);
    }

    private void startPing6Task(String str, HorseRaceStat horseRaceStat) {
        if (AwcnConfig.isPing6Enable() && Utils.isIPV6Address(str)) {
            try {
                PingResponse pingResponse = new PingTask(str, 1000, 3, 0, 0).launch().get();
                if (pingResponse != null) {
                    horseRaceStat.pingSuccessCount = pingResponse.getSuccessCnt();
                    horseRaceStat.pingTimeoutCount = 3 - horseRaceStat.pingSuccessCount;
                    horseRaceStat.localIP = pingResponse.getLocalIPStr();
                }
            } catch (Throwable th) {
                ALog.e(TAG, "ping6 task fail.", (String) null, th, new Object[0]);
            }
        }
    }
}
