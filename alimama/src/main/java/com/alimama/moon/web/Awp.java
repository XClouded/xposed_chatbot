package com.alimama.moon.web;

public final class Awp {
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.net.Uri.Builder getAwpHostBuilder() {
        /*
            android.net.Uri$Builder r0 = new android.net.Uri$Builder
            r0.<init>()
            java.lang.String r1 = "https"
            r0.scheme(r1)
            java.lang.String r1 = "h5.m.taobao.com"
            r0.authority(r1)
            com.alimama.union.app.configproperties.EnvHelper r1 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r1 = r1.getCurrentApiEnv()
            int r2 = r1.hashCode()
            r3 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r2 == r3) goto L_0x0030
            r3 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r2 == r3) goto L_0x0026
            goto L_0x003a
        L_0x0026:
            java.lang.String r2 = "debug"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x003a
            r1 = 0
            goto L_0x003b
        L_0x0030:
            java.lang.String r2 = "prepare"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x003a
            r1 = 1
            goto L_0x003b
        L_0x003a:
            r1 = -1
        L_0x003b:
            switch(r1) {
                case 0: goto L_0x004c;
                case 1: goto L_0x003f;
                default: goto L_0x003e;
            }
        L_0x003e:
            goto L_0x0058
        L_0x003f:
            java.lang.String r1 = "wapp.m.taobao.com"
            r0.authority(r1)
            java.lang.String r1 = "systype"
            java.lang.String r2 = "wapa"
            r0.appendQueryParameter(r1, r2)
            goto L_0x0058
        L_0x004c:
            java.lang.String r1 = "wapp.m.taobao.com"
            r0.authority(r1)
            java.lang.String r1 = "systype"
            java.lang.String r2 = "waptest"
            r0.appendQueryParameter(r1, r2)
        L_0x0058:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.web.Awp.getAwpHostBuilder():android.net.Uri$Builder");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0057  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.net.Uri.Builder getAlpHostBuilder() {
        /*
            android.net.Uri$Builder r0 = new android.net.Uri$Builder
            r0.<init>()
            java.lang.String r1 = "https"
            r0.scheme(r1)
            java.lang.String r1 = "mo.m.taobao.com"
            r0.authority(r1)
            com.alimama.union.app.configproperties.EnvHelper r1 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r1 = r1.getCurrentApiEnv()
            int r2 = r1.hashCode()
            r3 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r2 == r3) goto L_0x0030
            r3 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r2 == r3) goto L_0x0026
            goto L_0x003a
        L_0x0026:
            java.lang.String r2 = "debug"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x003a
            r1 = 0
            goto L_0x003b
        L_0x0030:
            java.lang.String r2 = "prepare"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x003a
            r1 = 1
            goto L_0x003b
        L_0x003a:
            r1 = -1
        L_0x003b:
            switch(r1) {
                case 0: goto L_0x0057;
                case 1: goto L_0x003f;
                default: goto L_0x003e;
            }
        L_0x003e:
            goto L_0x006e
        L_0x003f:
            java.lang.String r1 = "preview"
            java.lang.String r2 = "1"
            android.net.Uri$Builder r1 = r0.appendQueryParameter(r1, r2)
            java.lang.String r2 = "blockPreview"
            java.lang.String r3 = "1"
            android.net.Uri$Builder r1 = r1.appendQueryParameter(r2, r3)
            java.lang.String r2 = "systype"
            java.lang.String r3 = "wapa"
            r1.appendQueryParameter(r2, r3)
            goto L_0x006e
        L_0x0057:
            java.lang.String r1 = "preview"
            java.lang.String r2 = "1"
            android.net.Uri$Builder r1 = r0.appendQueryParameter(r1, r2)
            java.lang.String r2 = "blockPreview"
            java.lang.String r3 = "1"
            android.net.Uri$Builder r1 = r1.appendQueryParameter(r2, r3)
            java.lang.String r2 = "systype"
            java.lang.String r3 = "waptest"
            r1.appendQueryParameter(r2, r3)
        L_0x006e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.web.Awp.getAlpHostBuilder():android.net.Uri$Builder");
    }
}
