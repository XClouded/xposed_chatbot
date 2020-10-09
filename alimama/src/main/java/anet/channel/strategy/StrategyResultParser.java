package anet.channel.strategy;

import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import com.taobao.accs.utl.BaseMonitor;
import org.json.JSONArray;
import org.json.JSONObject;

public class StrategyResultParser {
    public static HttpDnsResponse parse(JSONObject jSONObject) {
        try {
            return new HttpDnsResponse(jSONObject);
        } catch (Exception e) {
            ALog.e("StrategyResultParser", "Parse HttpDns response failed.", (String) null, e, "JSON Content", jSONObject.toString());
            return null;
        }
    }

    public static class Strategy {
        public final Aisles aisles;
        public final String ip;
        public final String path;

        public Strategy(JSONObject jSONObject) {
            this.ip = jSONObject.optString("ip");
            this.path = jSONObject.optString("path");
            this.aisles = new Aisles(jSONObject);
        }
    }

    public static class Aisles {
        public final int cto;
        public final int heartbeat;
        public final int port;
        public final String protocol;
        public final String publicKey;
        public final int retry;
        public final int rto;
        public final String rtt;

        public Aisles(JSONObject jSONObject) {
            this.port = jSONObject.optInt("port");
            this.protocol = jSONObject.optString("protocol");
            this.cto = jSONObject.optInt("cto");
            this.rto = jSONObject.optInt("rto");
            this.retry = jSONObject.optInt("retry");
            this.heartbeat = jSONObject.optInt("heartbeat");
            this.rtt = jSONObject.optString("rtt", "");
            this.publicKey = jSONObject.optString("publickey");
        }
    }

    public static class HrTask {
        public final String host;
        public final Strategy[] strategies;

        public HrTask(JSONObject jSONObject) {
            this.host = jSONObject.optString("host");
            JSONArray optJSONArray = jSONObject.optJSONArray("strategies");
            if (optJSONArray != null) {
                int length = optJSONArray.length();
                this.strategies = new Strategy[length];
                for (int i = 0; i < length; i++) {
                    this.strategies[i] = new Strategy(optJSONArray.optJSONObject(i));
                }
                return;
            }
            this.strategies = null;
        }
    }

    public static class DnsInfo {
        public final Aisles[] aisleses;
        public final boolean clear;
        public final String cname;
        public final boolean effectNow;
        public final String host;
        public final String[] ips;
        public final String safeAisles;
        public final String[] sips;
        public final Strategy[] strategies;
        public final int ttl;
        public final String unit;
        public final int version;

        public DnsInfo(JSONObject jSONObject) {
            this.host = jSONObject.optString("host");
            this.ttl = jSONObject.optInt("ttl");
            this.safeAisles = jSONObject.optString("safeAisles");
            this.cname = jSONObject.optString("cname", (String) null);
            this.unit = jSONObject.optString("unit", (String) null);
            this.clear = jSONObject.optInt("clear") != 1 ? false : true;
            this.effectNow = jSONObject.optBoolean("effectNow");
            this.version = jSONObject.optInt("version");
            JSONArray optJSONArray = jSONObject.optJSONArray("ips");
            if (optJSONArray != null) {
                int length = optJSONArray.length();
                this.ips = new String[length];
                for (int i = 0; i < length; i++) {
                    this.ips[i] = optJSONArray.optString(i);
                }
            } else {
                this.ips = null;
            }
            JSONArray optJSONArray2 = jSONObject.optJSONArray("sips");
            if (optJSONArray2 == null || optJSONArray2.length() <= 0) {
                this.sips = null;
            } else {
                int length2 = optJSONArray2.length();
                this.sips = new String[length2];
                for (int i2 = 0; i2 < length2; i2++) {
                    this.sips[i2] = optJSONArray2.optString(i2);
                }
            }
            JSONArray optJSONArray3 = jSONObject.optJSONArray("aisles");
            if (optJSONArray3 != null) {
                int length3 = optJSONArray3.length();
                this.aisleses = new Aisles[length3];
                for (int i3 = 0; i3 < length3; i3++) {
                    this.aisleses[i3] = new Aisles(optJSONArray3.optJSONObject(i3));
                }
            } else {
                this.aisleses = null;
            }
            JSONArray optJSONArray4 = jSONObject.optJSONArray("strategies");
            if (optJSONArray4 == null || optJSONArray4.length() <= 0) {
                this.strategies = null;
                return;
            }
            int length4 = optJSONArray4.length();
            this.strategies = new Strategy[length4];
            for (int i4 = 0; i4 < length4; i4++) {
                this.strategies[i4] = new Strategy(optJSONArray4.optJSONObject(i4));
            }
        }
    }

    public static class HttpDnsResponse {
        public final String clientIp;
        public final int configVersion;
        public final DnsInfo[] dnsInfo;
        public final int fcLevel;
        public final int fcTime;
        public final HrTask[] hrTasks;
        public final String userId;
        public final String utdid;

        public HttpDnsResponse(JSONObject jSONObject) {
            this.clientIp = jSONObject.optString("ip");
            this.userId = jSONObject.optString("uid", (String) null);
            this.utdid = jSONObject.optString("utdid", (String) null);
            this.configVersion = jSONObject.optInt(DispatchConstants.CONFIG_VERSION);
            this.fcLevel = jSONObject.optInt("fcl");
            this.fcTime = jSONObject.optInt("fct");
            JSONArray optJSONArray = jSONObject.optJSONArray(BaseMonitor.COUNT_POINT_DNS);
            if (optJSONArray != null) {
                int length = optJSONArray.length();
                this.dnsInfo = new DnsInfo[length];
                for (int i = 0; i < length; i++) {
                    this.dnsInfo[i] = new DnsInfo(optJSONArray.optJSONObject(i));
                }
            } else {
                this.dnsInfo = null;
            }
            JSONArray optJSONArray2 = jSONObject.optJSONArray("hrTask");
            if (optJSONArray2 != null) {
                int length2 = optJSONArray2.length();
                this.hrTasks = new HrTask[length2];
                for (int i2 = 0; i2 < length2; i2++) {
                    this.hrTasks[i2] = new HrTask(optJSONArray2.optJSONObject(i2));
                }
                return;
            }
            this.hrTasks = null;
        }
    }
}
