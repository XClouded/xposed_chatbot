package com.alibaba.analytics.core.sync;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.SpSetting;
import com.taobao.accs.common.Constants;

public class TnetHostPortMgr implements SystemConfigMgr.IKVChangeListener {
    public static final String TAG_TNET_HOST_PORT = "utanalytics_tnet_host_port";
    public static TnetHostPortMgr instance;
    public TnetHostPort entity;
    private boolean mUseOuterTnetHost = false;

    public static synchronized TnetHostPortMgr getInstance() {
        TnetHostPortMgr tnetHostPortMgr;
        synchronized (TnetHostPortMgr.class) {
            if (instance == null) {
                instance = new TnetHostPortMgr();
            }
            tnetHostPortMgr = instance;
        }
        return tnetHostPortMgr;
    }

    TnetHostPortMgr() {
        try {
            this.entity = new TnetHostPort();
            String string = AppInfoUtil.getString(Variables.getInstance().getContext(), TAG_TNET_HOST_PORT);
            if (!TextUtils.isEmpty(string)) {
                this.mUseOuterTnetHost = true;
            }
            parseConifg(string);
            String str = SpSetting.get(Variables.getInstance().getContext(), TAG_TNET_HOST_PORT);
            if (!TextUtils.isEmpty(str)) {
                this.mUseOuterTnetHost = true;
            }
            parseConifg(str);
            parseConifg(SystemConfigMgr.getInstance().get(TAG_TNET_HOST_PORT));
            SystemConfigMgr.getInstance().register(TAG_TNET_HOST_PORT, this);
        } catch (Throwable unused) {
        }
    }

    public boolean isUseOuterTnetHost() {
        return this.mUseOuterTnetHost;
    }

    public TnetHostPort getEntity() {
        return this.entity;
    }

    public void onChange(String str, String str2) {
        parseConifg(str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r4 = r4.trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseConifg(java.lang.String r4) {
        /*
            r3 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 != 0) goto L_0x0036
            java.lang.String r4 = r4.trim()
            java.lang.String r0 = ":"
            int r0 = r4.indexOf(r0)
            r1 = -1
            if (r0 == r1) goto L_0x0036
            r1 = 0
            java.lang.String r1 = r4.substring(r1, r0)
            int r0 = r0 + 1
            int r2 = r4.length()
            java.lang.String r4 = r4.substring(r0, r2)
            int r4 = java.lang.Integer.parseInt(r4)
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 != 0) goto L_0x0036
            if (r4 <= 0) goto L_0x0036
            com.alibaba.analytics.core.sync.TnetHostPortMgr$TnetHostPort r0 = r3.entity
            r0.host = r1
            com.alibaba.analytics.core.sync.TnetHostPortMgr$TnetHostPort r0 = r3.entity
            r0.port = r4
        L_0x0036:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.TnetHostPortMgr.parseConifg(java.lang.String):void");
    }

    public static class TnetHostPort {
        public String host = "adashx.m.taobao.com";
        public int port = Constants.PORT;

        public String getHost() {
            return this.host;
        }

        public int getPort() {
            return this.port;
        }
    }
}
