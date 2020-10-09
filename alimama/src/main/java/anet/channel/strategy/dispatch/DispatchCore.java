package anet.channel.strategy.dispatch;

import android.util.Base64InputStream;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.flow.FlowStat;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.statist.AmdcStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.Inet64Util;
import com.taobao.accs.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

class DispatchCore {
    static final String CHECK_SIGN_FAIL = "-1003";
    static final String EMPTY_SIGN_ERROR = "-1001";
    static final int MAX_RETRY_TIMES = 3;
    static final int NO_RETRY = 2;
    static final String READ_ANSWER_ERROR = "-1002";
    static final String REQUEST_EXCEPTION = "-1000";
    static final String RESOLVE_ANSWER_FAIL = "-1004";
    static final int RETRY_NORMAL = 1;
    static final int SUCCESS = 0;
    static final String TAG = "awcn.DispatchCore";
    static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        public boolean verify(String str, SSLSession sSLSession) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(DispatchConstants.getAmdcServerDomain(), sSLSession);
        }
    };
    static Random random = new Random();
    static AtomicInteger seq = new AtomicInteger(0);

    DispatchCore() {
    }

    static List<IConnStrategy> prepareConnStrategy(String str) {
        List<IConnStrategy> list = Collections.EMPTY_LIST;
        if (!NetworkStatusHelper.isProxy()) {
            list = StrategyCenter.getInstance().getConnStrategyListByHost(DispatchConstants.getAmdcServerDomain());
            ListIterator<IConnStrategy> listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                if (!listIterator.next().getProtocol().protocol.equalsIgnoreCase(str)) {
                    listIterator.remove();
                }
            }
        }
        return list;
    }

    public static void sendRequest(Map map) {
        IConnStrategy iConnStrategy;
        String str;
        String str2;
        if (map != null) {
            String schemeByHost = StrategyCenter.getInstance().getSchemeByHost(DispatchConstants.getAmdcServerDomain(), "http");
            List<IConnStrategy> prepareConnStrategy = prepareConnStrategy(schemeByHost);
            int i = 0;
            while (i < 3) {
                HashMap hashMap = new HashMap(map);
                if (i != 2) {
                    iConnStrategy = !prepareConnStrategy.isEmpty() ? prepareConnStrategy.remove(0) : null;
                    if (iConnStrategy != null) {
                        str = buildRequestUrl(schemeByHost, iConnStrategy.getIp(), iConnStrategy.getPort(), hashMap, i);
                    } else {
                        str = buildRequestUrl(schemeByHost, (String) null, 0, hashMap, i);
                    }
                } else {
                    String[] amdcServerFixIp = DispatchConstants.getAmdcServerFixIp();
                    if (amdcServerFixIp == null || amdcServerFixIp.length <= 0) {
                        str2 = buildRequestUrl(schemeByHost, (String) null, 0, hashMap, i);
                    } else {
                        str2 = buildRequestUrl(schemeByHost, amdcServerFixIp[random.nextInt(amdcServerFixIp.length)], 0, hashMap, i);
                    }
                    String str3 = str2;
                    iConnStrategy = null;
                    str = str3;
                }
                int sendOneNetworkRequest = sendOneNetworkRequest(str, hashMap, i);
                if (iConnStrategy != null) {
                    ConnEvent connEvent = new ConnEvent();
                    connEvent.isSuccess = sendOneNetworkRequest == 0;
                    StrategyCenter.getInstance().notifyConnEvent(DispatchConstants.getAmdcServerDomain(), iConnStrategy, connEvent);
                }
                if (sendOneNetworkRequest != 0 && sendOneNetworkRequest != 2) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private static String buildRequestUrl(String str, String str2, int i, Map<String, String> map, int i2) {
        StringBuilder sb = new StringBuilder(64);
        if (i2 == 2 && "https".equalsIgnoreCase(str) && random.nextBoolean()) {
            str = "http";
        }
        sb.append(str);
        sb.append(HttpConstant.SCHEME_SPLIT);
        if (str2 != null) {
            if (Inet64Util.isIPv6OnlyNetwork() && Utils.isIPV4Address(str2)) {
                try {
                    str2 = Inet64Util.convertToIPv6ThrowsException(str2);
                } catch (Exception unused) {
                }
            }
            if (Utils.isIPV6Address(str2)) {
                sb.append(Operators.ARRAY_START);
                sb.append(str2);
                sb.append(Operators.ARRAY_END);
            } else {
                sb.append(str2);
            }
            if (i == 0) {
                i = "https".equalsIgnoreCase(str) ? Constants.PORT : 80;
            }
            sb.append(":");
            sb.append(i);
        } else {
            sb.append(DispatchConstants.getAmdcServerDomain());
        }
        sb.append(DispatchConstants.serverPath);
        TreeMap treeMap = new TreeMap();
        treeMap.put("appkey", map.remove("appkey"));
        treeMap.put("v", map.remove("v"));
        treeMap.put("deviceId", map.remove("deviceId"));
        treeMap.put("platform", map.remove("platform"));
        sb.append(Operators.CONDITION_IF);
        sb.append(Utils.encodeQueryParams(treeMap, "utf-8"));
        return sb.toString();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:104|105|(2:107|108)|111) */
    /* JADX WARNING: Code restructure failed: missing block: B:105:?, code lost:
        anet.channel.strategy.dispatch.HttpDispatcher.getInstance().fireEvent(new anet.channel.strategy.dispatch.DispatchEvent(0, (java.lang.Object) null));
        anet.channel.util.ALog.e(TAG, "resolve amdc anser failed", r3, new java.lang.Object[0]);
        commitStatistic(RESOLVE_ANSWER_FAIL, "resolve answer failed", r7, r2, 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x026b, code lost:
        if (r10 != null) goto L_0x026d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:?, code lost:
        r10.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0271, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0272, code lost:
        anet.channel.util.ALog.e(TAG, "http disconnect failed", (java.lang.String) null, r0, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x027d, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x0284, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0285, code lost:
        r5 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x0287, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0288, code lost:
        r1 = r0;
        r10 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0298, code lost:
        r1 = r0.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:?, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x02b0, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x02b1, code lost:
        anet.channel.util.ALog.e(TAG, "http disconnect failed", (java.lang.String) null, r0, new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:?, code lost:
        r10.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x02c6, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x02c7, code lost:
        anet.channel.util.ALog.e(TAG, "http disconnect failed", (java.lang.String) null, r0, new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:104:0x024e */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x0287 A[ExcHandler: all (r0v8 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x004e] */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0298 A[Catch:{ all -> 0x02bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x02ac A[SYNTHETIC, Splitter:B:128:0x02ac] */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x02c2 A[SYNTHETIC, Splitter:B:136:0x02c2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int sendOneNetworkRequest(java.lang.String r18, java.util.Map r19, int r20) {
        /*
            r0 = r18
            r1 = r19
            r2 = r20
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "AMDC"
            r3.append(r4)
            java.util.concurrent.atomic.AtomicInteger r4 = seq
            int r4 = r4.incrementAndGet()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "awcn.DispatchCore"
            java.lang.String r5 = "send amdc request"
            r6 = 4
            java.lang.Object[] r7 = new java.lang.Object[r6]
            java.lang.String r8 = "url"
            r9 = 0
            r7[r9] = r8
            r8 = 1
            r7[r8] = r0
            java.lang.String r10 = "\nhost"
            r11 = 2
            r7[r11] = r10
            java.lang.String r10 = "domain"
            java.lang.Object r10 = r1.get(r10)
            java.lang.String r10 = r10.toString()
            r12 = 3
            r7[r12] = r10
            anet.channel.util.ALog.i(r4, r5, r3, r7)
            java.lang.String r4 = "Env"
            java.lang.Object r4 = r1.remove(r4)
            anet.channel.entity.ENV r4 = (anet.channel.entity.ENV) r4
            r5 = 0
            java.net.URL r7 = new java.net.URL     // Catch:{ Throwable -> 0x028b, all -> 0x0287 }
            r7.<init>(r0)     // Catch:{ Throwable -> 0x028b, all -> 0x0287 }
            java.net.URLConnection r10 = r7.openConnection()     // Catch:{ Throwable -> 0x0284, all -> 0x0287 }
            java.net.HttpURLConnection r10 = (java.net.HttpURLConnection) r10     // Catch:{ Throwable -> 0x0284, all -> 0x0287 }
            r13 = 20000(0x4e20, float:2.8026E-41)
            r10.setConnectTimeout(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r10.setReadTimeout(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r13 = "POST"
            r10.setRequestMethod(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r10.setDoOutput(r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r10.setDoInput(r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r13 = "Connection"
            java.lang.String r14 = "close"
            r10.addRequestProperty(r13, r14)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r13 = "Accept-Encoding"
            java.lang.String r14 = "gzip"
            r10.addRequestProperty(r13, r14)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r10.setInstanceFollowRedirects(r9)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r13 = r7.getProtocol()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r14 = "https"
            boolean r13 = r13.equals(r14)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r13 == 0) goto L_0x0091
            r13 = r10
            javax.net.ssl.HttpsURLConnection r13 = (javax.net.ssl.HttpsURLConnection) r13     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            javax.net.ssl.HostnameVerifier r14 = hostnameVerifier     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r13.setHostnameVerifier(r14)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
        L_0x0091:
            java.io.OutputStream r13 = r10.getOutputStream()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r14 = "utf-8"
            java.lang.String r1 = anet.channel.strategy.utils.Utils.encodeQueryParams(r1, r14)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            byte[] r1 = r1.getBytes()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r13.write(r1)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            int r13 = r10.getResponseCode()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            boolean r14 = anet.channel.util.ALog.isPrintLog(r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r14 == 0) goto L_0x00ce
            java.lang.String r14 = "awcn.DispatchCore"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r15.<init>()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r12 = "amdc response. code: "
            r15.append(r12)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r15.append(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r12 = r15.toString()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.Object[] r15 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r16 = "\nheaders"
            r15[r9] = r16     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.util.Map r16 = r10.getHeaderFields()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r15[r8] = r16     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            anet.channel.util.ALog.d(r14, r12, r3, r15)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
        L_0x00ce:
            r12 = 200(0xc8, float:2.8E-43)
            if (r13 == r12) goto L_0x00f7
            r0 = 302(0x12e, float:4.23E-43)
            if (r13 == r0) goto L_0x00dc
            r0 = 307(0x133, float:4.3E-43)
            if (r13 != r0) goto L_0x00db
            goto L_0x00dc
        L_0x00db:
            r11 = 1
        L_0x00dc:
            java.lang.String r0 = java.lang.String.valueOf(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r1 = "response code not 200"
            commitStatistic(r0, r1, r7, r2, r11)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x00f6
            r10.disconnect()     // Catch:{ Exception -> 0x00eb }
            goto L_0x00f6
        L_0x00eb:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            anet.channel.util.ALog.e(r0, r2, r5, r1, r3)
        L_0x00f6:
            return r11
        L_0x00f7:
            java.lang.String r12 = "x-am-code"
            java.lang.String r12 = r10.getHeaderField(r12)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r13 = "1000"
            boolean r13 = r13.equals(r12)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r13 != 0) goto L_0x013e
            java.lang.String r0 = "1007"
            boolean r0 = r0.equals(r12)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r0 != 0) goto L_0x0118
            java.lang.String r0 = "1008"
            boolean r0 = r0.equals(r12)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r0 == 0) goto L_0x0117
            goto L_0x0118
        L_0x0117:
            r11 = 1
        L_0x0118:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r0.<init>()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r1 = "return code: "
            r0.append(r1)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r0.append(r12)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            commitStatistic(r12, r0, r7, r2, r11)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x013d
            r10.disconnect()     // Catch:{ Exception -> 0x0132 }
            goto L_0x013d
        L_0x0132:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            anet.channel.util.ALog.e(r0, r2, r5, r1, r3)
        L_0x013d:
            return r11
        L_0x013e:
            java.lang.String r13 = "x-am-sign"
            java.lang.String r13 = r10.getHeaderField(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            boolean r14 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r14 == 0) goto L_0x0164
            java.lang.String r0 = "-1001"
            java.lang.String r1 = "response sign is empty"
            commitStatistic(r0, r1, r7, r2, r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x0163
            r10.disconnect()     // Catch:{ Exception -> 0x0158 }
            goto L_0x0163
        L_0x0158:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            anet.channel.util.ALog.e(r0, r2, r5, r1, r3)
        L_0x0163:
            return r8
        L_0x0164:
            java.io.InputStream r14 = r10.getInputStream()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r15 = "gzip"
            java.lang.String r6 = r10.getContentEncoding()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            boolean r6 = r15.equalsIgnoreCase(r6)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r6 = readAnswer(r14, r6)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            boolean r14 = anet.channel.util.ALog.isPrintLog(r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r14 == 0) goto L_0x018b
            java.lang.String r14 = "awcn.DispatchCore"
            java.lang.String r15 = "amdc response body"
            java.lang.Object[] r5 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r16 = "\nbody"
            r5[r9] = r16     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r5[r8] = r6     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            anet.channel.util.ALog.d(r14, r15, r3, r5)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
        L_0x018b:
            int r1 = r1.length     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            long r14 = (long) r1     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            int r1 = r10.getContentLength()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r17 = r12
            long r11 = (long) r1     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            commitFlow(r0, r14, r11)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r0 == 0) goto L_0x01b7
            java.lang.String r0 = "-1002"
            java.lang.String r1 = "read answer error"
            commitStatistic(r0, r1, r7, r2, r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x01b6
            r10.disconnect()     // Catch:{ Exception -> 0x01aa }
            goto L_0x01b6
        L_0x01aa:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            r4 = 0
            anet.channel.util.ALog.e(r0, r2, r4, r1, r3)
        L_0x01b6:
            return r8
        L_0x01b7:
            anet.channel.strategy.dispatch.IAmdcSign r0 = anet.channel.strategy.dispatch.AmdcRuntimeInfo.getSign()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r0 == 0) goto L_0x01c2
            java.lang.String r5 = r0.sign(r6)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            goto L_0x01c3
        L_0x01c2:
            r5 = 0
        L_0x01c3:
            boolean r0 = r5.equalsIgnoreCase(r13)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r0 != 0) goto L_0x01fb
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r1 = "check ret sign failed"
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r6 = "retSign"
            r4[r9] = r6     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r4[r8] = r13     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r6 = "checkSign"
            r11 = 2
            r4[r11] = r6     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r6 = 3
            r4[r6] = r5     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            anet.channel.util.ALog.e(r0, r1, r3, r4)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r0 = "-1003"
            java.lang.String r1 = "check sign failed"
            commitStatistic(r0, r1, r7, r2, r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x01fa
            r10.disconnect()     // Catch:{ Exception -> 0x01ee }
            goto L_0x01fa
        L_0x01ee:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            r4 = 0
            anet.channel.util.ALog.e(r0, r2, r4, r1, r3)
        L_0x01fa:
            return r8
        L_0x01fb:
            org.json.JSONTokener r0 = new org.json.JSONTokener     // Catch:{ JSONException -> 0x024e }
            r0.<init>(r6)     // Catch:{ JSONException -> 0x024e }
            java.lang.Object r0 = r0.nextValue()     // Catch:{ JSONException -> 0x024e }
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ JSONException -> 0x024e }
            anet.channel.entity.ENV r1 = anet.channel.GlobalAppRuntimeInfo.getEnv()     // Catch:{ JSONException -> 0x024e }
            if (r1 == r4) goto L_0x0228
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r1 = "env change, do not notify result"
            java.lang.Object[] r4 = new java.lang.Object[r9]     // Catch:{ JSONException -> 0x024e }
            anet.channel.util.ALog.w(r0, r1, r3, r4)     // Catch:{ JSONException -> 0x024e }
            if (r10 == 0) goto L_0x0227
            r10.disconnect()     // Catch:{ Exception -> 0x021b }
            goto L_0x0227
        L_0x021b:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            r4 = 0
            anet.channel.util.ALog.e(r0, r2, r4, r1, r3)
        L_0x0227:
            return r9
        L_0x0228:
            anet.channel.strategy.dispatch.HttpDispatcher r1 = anet.channel.strategy.dispatch.HttpDispatcher.getInstance()     // Catch:{ JSONException -> 0x024e }
            anet.channel.strategy.dispatch.DispatchEvent r4 = new anet.channel.strategy.dispatch.DispatchEvent     // Catch:{ JSONException -> 0x024e }
            r4.<init>(r8, r0)     // Catch:{ JSONException -> 0x024e }
            r1.fireEvent(r4)     // Catch:{ JSONException -> 0x024e }
            java.lang.String r0 = "request success"
            r1 = r17
            commitStatistic(r1, r0, r7, r2, r9)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x024d
            r10.disconnect()     // Catch:{ Exception -> 0x0241 }
            goto L_0x024d
        L_0x0241:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            r4 = 0
            anet.channel.util.ALog.e(r0, r2, r4, r1, r3)
        L_0x024d:
            return r9
        L_0x024e:
            anet.channel.strategy.dispatch.HttpDispatcher r0 = anet.channel.strategy.dispatch.HttpDispatcher.getInstance()     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            anet.channel.strategy.dispatch.DispatchEvent r1 = new anet.channel.strategy.dispatch.DispatchEvent     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r4 = 0
            r1.<init>(r9, r4)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            r0.fireEvent(r1)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r1 = "resolve amdc anser failed"
            java.lang.Object[] r4 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            anet.channel.util.ALog.e(r0, r1, r3, r4)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            java.lang.String r0 = "-1004"
            java.lang.String r1 = "resolve answer failed"
            commitStatistic(r0, r1, r7, r2, r8)     // Catch:{ Throwable -> 0x0281, all -> 0x027e }
            if (r10 == 0) goto L_0x027d
            r10.disconnect()     // Catch:{ Exception -> 0x0271 }
            goto L_0x027d
        L_0x0271:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            r4 = 0
            anet.channel.util.ALog.e(r0, r2, r4, r1, r3)
        L_0x027d:
            return r8
        L_0x027e:
            r0 = move-exception
            r1 = r0
            goto L_0x02c0
        L_0x0281:
            r0 = move-exception
            r5 = r10
            goto L_0x028e
        L_0x0284:
            r0 = move-exception
            r5 = 0
            goto L_0x028e
        L_0x0287:
            r0 = move-exception
            r1 = r0
            r10 = 0
            goto L_0x02c0
        L_0x028b:
            r0 = move-exception
            r5 = 0
            r7 = 0
        L_0x028e:
            java.lang.String r1 = r0.getMessage()     // Catch:{ all -> 0x02bd }
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x02bd }
            if (r4 == 0) goto L_0x029c
            java.lang.String r1 = r0.toString()     // Catch:{ all -> 0x02bd }
        L_0x029c:
            java.lang.String r4 = "-1000"
            commitStatistic(r4, r1, r7, r2, r8)     // Catch:{ all -> 0x02bd }
            java.lang.String r1 = "awcn.DispatchCore"
            java.lang.String r2 = "amdc request fail"
            java.lang.Object[] r4 = new java.lang.Object[r9]     // Catch:{ all -> 0x02bd }
            anet.channel.util.ALog.e(r1, r2, r3, r0, r4)     // Catch:{ all -> 0x02bd }
            if (r5 == 0) goto L_0x02bc
            r5.disconnect()     // Catch:{ Exception -> 0x02b0 }
            goto L_0x02bc
        L_0x02b0:
            r0 = move-exception
            r1 = r0
            java.lang.String r0 = "awcn.DispatchCore"
            java.lang.String r2 = "http disconnect failed"
            java.lang.Object[] r3 = new java.lang.Object[r9]
            r4 = 0
            anet.channel.util.ALog.e(r0, r2, r4, r1, r3)
        L_0x02bc:
            return r8
        L_0x02bd:
            r0 = move-exception
            r1 = r0
            r10 = r5
        L_0x02c0:
            if (r10 == 0) goto L_0x02d2
            r10.disconnect()     // Catch:{ Exception -> 0x02c6 }
            goto L_0x02d2
        L_0x02c6:
            r0 = move-exception
            r2 = r0
            java.lang.Object[] r0 = new java.lang.Object[r9]
            java.lang.String r3 = "awcn.DispatchCore"
            java.lang.String r4 = "http disconnect failed"
            r5 = 0
            anet.channel.util.ALog.e(r3, r4, r5, r2, r0)
        L_0x02d2:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.dispatch.DispatchCore.sendOneNetworkRequest(java.lang.String, java.util.Map, int):int");
    }

    static String readAnswer(InputStream inputStream, boolean z) {
        FilterInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        if (z) {
            try {
                bufferedInputStream = new GZIPInputStream(bufferedInputStream);
            } catch (IOException e) {
                e = e;
                try {
                    ALog.e(TAG, "", (String) null, e, new Object[0]);
                    try {
                        bufferedInputStream.close();
                    } catch (IOException unused) {
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    try {
                        bufferedInputStream.close();
                    } catch (IOException unused2) {
                    }
                    throw th;
                }
            }
        }
        FilterInputStream base64InputStream = new Base64InputStream(bufferedInputStream, 0);
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = base64InputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            String str = new String(byteArrayOutputStream.toByteArray(), "utf-8");
            try {
                base64InputStream.close();
            } catch (IOException unused3) {
            }
            return str;
        } catch (IOException e2) {
            e = e2;
            bufferedInputStream = base64InputStream;
            ALog.e(TAG, "", (String) null, e, new Object[0]);
            bufferedInputStream.close();
            return null;
        } catch (Throwable th2) {
            th = th2;
            bufferedInputStream = base64InputStream;
            bufferedInputStream.close();
            throw th;
        }
    }

    static void commitStatistic(String str, String str2, URL url, int i, int i2) {
        if ((i2 != 1 || i == 2) && GlobalAppRuntimeInfo.isTargetProcess()) {
            try {
                AmdcStatistic amdcStatistic = new AmdcStatistic();
                amdcStatistic.errorCode = str;
                amdcStatistic.errorMsg = str2;
                if (url != null) {
                    amdcStatistic.host = url.getHost();
                    amdcStatistic.url = url.toString();
                }
                amdcStatistic.retryTimes = i;
                AppMonitor.getInstance().commitStat(amdcStatistic);
            } catch (Exception unused) {
            }
        }
    }

    static void commitFlow(String str, long j, long j2) {
        try {
            FlowStat flowStat = new FlowStat();
            flowStat.refer = "amdc";
            flowStat.protocoltype = "http";
            flowStat.req_identifier = str;
            flowStat.upstream = j;
            flowStat.downstream = j2;
            NetworkAnalysis.getInstance().commitFlow(flowStat);
        } catch (Exception e) {
            ALog.e(TAG, "commit flow info failed!", (String) null, e, new Object[0]);
        }
    }
}
