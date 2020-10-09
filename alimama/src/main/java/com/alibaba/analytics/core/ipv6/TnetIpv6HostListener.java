package com.alibaba.analytics.core.ipv6;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.sync.TnetHostPortMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.SpSetting;
import com.taobao.accs.common.Constants;

public class TnetIpv6HostListener {
    private TnetIpv6HostPort mTnetIpv6HostPort = new TnetIpv6HostPort();
    private boolean mUseOuterTnetIpv6Host = false;

    public TnetIpv6HostListener() {
        try {
            Context context = Variables.getInstance().getContext();
            String string = AppInfoUtil.getString(context, Ipv6ConfigConstant.UTANALYTICS_TNET_HOST_PORT_IPV6);
            if (!TextUtils.isEmpty(string)) {
                this.mUseOuterTnetIpv6Host = true;
            }
            parseConfig(string);
            String str = SpSetting.get(context, Ipv6ConfigConstant.UTANALYTICS_TNET_HOST_PORT_IPV6);
            if (!TextUtils.isEmpty(str)) {
                this.mUseOuterTnetIpv6Host = true;
            }
            parseConfig(str);
        } catch (Throwable unused) {
        }
    }

    public TnetIpv6HostPort getHostPortEntity() {
        if (this.mUseOuterTnetIpv6Host) {
            return this.mTnetIpv6HostPort;
        }
        if (TnetHostPortMgr.getInstance().isUseOuterTnetHost()) {
            return null;
        }
        return this.mTnetIpv6HostPort;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0007, code lost:
        r4 = r4.trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void parseConfig(java.lang.String r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x003b }
            if (r0 != 0) goto L_0x0039
            java.lang.String r4 = r4.trim()     // Catch:{ all -> 0x003b }
            java.lang.String r0 = ":"
            int r0 = r4.indexOf(r0)     // Catch:{ all -> 0x003b }
            r1 = -1
            if (r0 == r1) goto L_0x0039
            r1 = 0
            java.lang.String r1 = r4.substring(r1, r0)     // Catch:{ all -> 0x003b }
            int r0 = r0 + 1
            int r2 = r4.length()     // Catch:{ all -> 0x003b }
            java.lang.String r4 = r4.substring(r0, r2)     // Catch:{ all -> 0x003b }
            int r4 = java.lang.Integer.parseInt(r4)     // Catch:{ all -> 0x003b }
            boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x003b }
            if (r0 != 0) goto L_0x0039
            if (r4 <= 0) goto L_0x0039
            com.alibaba.analytics.core.ipv6.TnetIpv6HostListener$TnetIpv6HostPort r0 = r3.mTnetIpv6HostPort     // Catch:{ all -> 0x003b }
            java.lang.String unused = r0.host = r1     // Catch:{ all -> 0x003b }
            com.alibaba.analytics.core.ipv6.TnetIpv6HostListener$TnetIpv6HostPort r0 = r3.mTnetIpv6HostPort     // Catch:{ all -> 0x003b }
            int unused = r0.port = r4     // Catch:{ all -> 0x003b }
        L_0x0039:
            monitor-exit(r3)
            return
        L_0x003b:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.ipv6.TnetIpv6HostListener.parseConfig(java.lang.String):void");
    }

    public static class TnetIpv6HostPort {
        /* access modifiers changed from: private */
        public String host = "v6-adashx.ut.taobao.com";
        /* access modifiers changed from: private */
        public int port = Constants.PORT;

        public String getHost() {
            return this.host;
        }

        public int getPort() {
            return this.port;
        }
    }
}
