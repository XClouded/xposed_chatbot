package com.alimama.union.app.infrastructure.weex;

import android.net.Uri;
import com.alimama.moon.config.MoonConfigCenter;
import com.alimama.union.app.configcenter.ConfigCenterDataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WeexPageGenerator {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) WeexPageGenerator.class);

    public static Uri getShareCreatorUri() {
        return Uri.parse(ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.SHARE_WEEX_URL));
    }

    public static Uri getShareTextCreatorUri() {
        return getCdnHost().buildUpon().appendEncodedPath("mm").appendEncodedPath("unionapp-weex").appendEncodedPath(MoonConfigCenter.getVueBundleJsVersion()).appendEncodedPath("share_text.js").build();
    }

    public static Uri getIndexUri() {
        return getCdnHost().buildUpon().appendEncodedPath("mm").appendEncodedPath("unionapp").appendEncodedPath(MoonConfigCenter.getBundleJsVersion()).appendEncodedPath("index.js").build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.net.Uri getCdnHost() {
        /*
            android.net.Uri$Builder r0 = new android.net.Uri$Builder
            r0.<init>()
            java.lang.String r1 = "https"
            r0.scheme(r1)
            com.alimama.union.app.configproperties.EnvHelper r1 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r1 = r1.getCurrentApiEnv()
            int r2 = r1.hashCode()
            r3 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            if (r2 == r3) goto L_0x003a
            r3 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r2 == r3) goto L_0x0030
            r3 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r2 == r3) goto L_0x0026
            goto L_0x0044
        L_0x0026:
            java.lang.String r2 = "debug"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0044
            r1 = 0
            goto L_0x0045
        L_0x0030:
            java.lang.String r2 = "prepare"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0044
            r1 = 1
            goto L_0x0045
        L_0x003a:
            java.lang.String r2 = "online"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0044
            r1 = 2
            goto L_0x0045
        L_0x0044:
            r1 = -1
        L_0x0045:
            switch(r1) {
                case 0: goto L_0x0054;
                case 1: goto L_0x0054;
                case 2: goto L_0x004e;
                default: goto L_0x0048;
            }
        L_0x0048:
            java.lang.String r1 = "g.alicdn.com"
            r0.authority(r1)
            goto L_0x0059
        L_0x004e:
            java.lang.String r1 = "g.alicdn.com"
            r0.authority(r1)
            goto L_0x0059
        L_0x0054:
            java.lang.String r1 = "g-assets.daily.taobao.net"
            r0.authority(r1)
        L_0x0059:
            android.net.Uri r0 = r0.build()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.infrastructure.weex.WeexPageGenerator.getCdnHost():android.net.Uri");
    }

    public static String getReportUrl() {
        return ConfigCenterDataUtils.getFixedUrl(ConfigCenterDataUtils.REPORT_WEEX_URL);
    }
}
