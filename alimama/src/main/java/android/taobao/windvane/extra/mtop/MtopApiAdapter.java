package android.taobao.windvane.extra.mtop;

import android.content.ContextWrapper;
import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.IApiAdapter;
import android.taobao.windvane.extra.security.SecurityManager;
import android.taobao.windvane.extra.security.TaoApiSign;
import android.taobao.windvane.util.TaoLog;

import java.util.Map;

import mtopsdk.common.util.SymbolExpUtil;

public class MtopApiAdapter implements IApiAdapter {
    private ApiRequest request;

    public String formatUrl(ApiRequest apiRequest) {
        if (apiRequest == null) {
            return "";
        }
        this.request = apiRequest;
        checkParams();
        return wrapUrl(GlobalConfig.getMtopUrl());
    }

    public String formatBody(ApiRequest apiRequest) {
        if (apiRequest == null) {
            return "";
        }
        this.request = apiRequest;
        checkParams();
        return wrapBody();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0098, code lost:
        if (r0 == 0) goto L_0x009a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkParams() {
        /*
            r6 = this;
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "ttid"
            android.taobao.windvane.config.GlobalConfig r2 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r2 = r2.getTtid()
            r0.addParam(r1, r2)
            android.taobao.windvane.config.GlobalConfig r0 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r0 = r0.getImei()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x001f
            java.lang.String r0 = "111111111111111"
        L_0x001f:
            android.taobao.windvane.config.GlobalConfig r1 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r1 = r1.getImsi()
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x002f
            java.lang.String r1 = "111111111111111"
        L_0x002f:
            android.taobao.windvane.connect.api.ApiRequest r2 = r6.request
            java.lang.String r3 = "imei"
            r2.addParam(r3, r0)
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r2 = "imsi"
            r0.addParam(r2, r1)
            android.taobao.windvane.config.GlobalConfig r0 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r0 = r0.getDeviceId()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x005a
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "deviceId"
            android.taobao.windvane.config.GlobalConfig r2 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r2 = r2.getDeviceId()
            r0.addParam(r1, r2)
        L_0x005a:
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.util.Map r0 = r0.getDataParams()
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x007c
            org.json.JSONObject r0 = new org.json.JSONObject
            android.taobao.windvane.connect.api.ApiRequest r1 = r6.request
            java.util.Map r1 = r1.getDataParams()
            r0.<init>(r1)
            java.lang.String r0 = r0.toString()
            android.taobao.windvane.connect.api.ApiRequest r1 = r6.request
            java.lang.String r2 = "data"
            r1.addParam(r2, r0)
        L_0x007c:
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.util.Map r0 = r0.getParams()
            java.lang.String r1 = "t"
            boolean r0 = r0.containsKey(r1)
            if (r0 != 0) goto L_0x00ac
            android.taobao.windvane.extra.WVIAdapter r0 = android.taobao.windvane.WindVaneSDKForTB.wvAdapter
            if (r0 == 0) goto L_0x009a
            android.taobao.windvane.extra.WVIAdapter r0 = android.taobao.windvane.WindVaneSDKForTB.wvAdapter
            long r0 = r0.getTimestamp()
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x009e
        L_0x009a:
            long r0 = java.lang.System.currentTimeMillis()
        L_0x009e:
            android.taobao.windvane.connect.api.ApiRequest r2 = r6.request
            java.lang.String r3 = "t"
            r4 = 1000(0x3e8, double:4.94E-321)
            long r0 = r0 / r4
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r2.addParam(r3, r0)
        L_0x00ac:
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "appKey"
            android.taobao.windvane.config.GlobalConfig r2 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r2 = r2.getAppKey()
            r0.addParam(r1, r2)
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            boolean r0 = r0.isSec()
            if (r0 == 0) goto L_0x00d0
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "wua"
            android.app.Application r2 = android.taobao.windvane.config.GlobalConfig.context
            java.lang.String r2 = r6.getSecBodyData(r2)
            r0.addParam(r1, r2)
        L_0x00d0:
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "appSecret"
            android.taobao.windvane.config.GlobalConfig r2 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r2 = r2.getAppSecret()
            r0.addParam(r1, r2)
            java.lang.String r0 = r6.getSign()
            android.taobao.windvane.connect.api.ApiRequest r1 = r6.request
            java.lang.String r2 = "sign"
            r1.addParam(r2, r0)
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.util.Map r0 = r0.getParams()
            java.lang.String r1 = "v"
            boolean r0 = r0.containsKey(r1)
            if (r0 != 0) goto L_0x0101
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "v"
            java.lang.String r2 = "*"
            r0.addParam(r1, r2)
        L_0x0101:
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.util.Map r0 = r0.getParams()
            java.lang.String r1 = "appSecret"
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L_0x0116
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "appSecret"
            r0.removeParam(r1)
        L_0x0116:
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.util.Map r0 = r0.getParams()
            java.lang.String r1 = "ecode"
            boolean r0 = r0.containsKey(r1)
            if (r0 == 0) goto L_0x012b
            android.taobao.windvane.connect.api.ApiRequest r0 = r6.request
            java.lang.String r1 = "ecode"
            r0.removeParam(r1)
        L_0x012b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.mtop.MtopApiAdapter.checkParams():void");
    }

    private String wrapUrl(String str) {
        if (str == null || str.length() <= 0) {
            return "";
        }
        Uri parse = Uri.parse(str);
        Uri.Builder buildUpon = parse.buildUpon();
        String path = parse.getPath();
        if (path == null || path.length() == 0) {
            buildUpon.appendPath("");
        }
        for (Map.Entry next : this.request.getParams().entrySet()) {
            buildUpon = buildUpon.appendQueryParameter((String) next.getKey(), (String) next.getValue());
        }
        return buildUpon.toString();
    }

    private String wrapBody() {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (Map.Entry next : this.request.getParams().entrySet()) {
            if (z) {
                sb.append("&");
            } else {
                z = true;
            }
            sb.append(Uri.encode((String) next.getKey()));
            sb.append(SymbolExpUtil.SYMBOL_EQUAL);
            sb.append(Uri.encode((String) next.getValue()));
        }
        return sb.toString();
    }

    private String getSign() {
        String sign = SecurityManager.getInstance().getSign(0, this.request.getParams(), this.request.getParam("appKey"));
        if (TaoLog.getLogStatus()) {
            TaoLog.d("MtopApiAdapter", "appkey: " + this.request.getParam("appKey") + " params: " + this.request.getParams());
        }
        if (sign != null) {
            return sign;
        }
        TaoLog.w("MtopApiAdapter", "SecurityManager.getSign failed, execute TaoApiSign.getSign");
        return TaoApiSign.getSign(this.request.getParams());
    }

    private String getSecBodyData(ContextWrapper contextWrapper) {
        return SecurityManager.getInstance().getSecBody(contextWrapper, this.request.getParam("t"), this.request.getParam("appKey"));
    }
}
