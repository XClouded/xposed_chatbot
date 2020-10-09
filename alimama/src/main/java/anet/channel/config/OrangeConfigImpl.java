package anet.channel.config;

import anet.channel.util.ALog;
import anetwork.channel.config.IRemoteConfig;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;

public class OrangeConfigImpl implements IRemoteConfig {
    private static final String NETWORK_ACCS_SESSION_BG_SWITCH = "network_accs_session_bg_switch";
    private static final String NETWORK_AMDC_PRESET_HOSTS = "network_amdc_preset_hosts";
    private static final String NETWORK_BIND_SERVICE_OPTIMISE = "network_bind_service_optimize";
    private static final String NETWORK_BIZ_WHITE_LIST_BG = "network_biz_white_list_bg";
    private static final String NETWORK_DELAY_RETRY_REQUEST_NO_NETWORK = "network_delay_retry_request_no_network";
    private static final String NETWORK_DETECT_ENABLE_SWITCH = "network_detect_enable_switch";
    private static final String NETWORK_EMPTY_SCHEME_HTTPS_SWITCH = "network_empty_scheme_https_switch";
    private static final String NETWORK_FORBID_NEXT_LUANCH_OPTIMIZE = "network_forbid_next_launch_optimize";
    private static final String NETWORK_GET_SESSION_ASYNC_SWITCH = "network_get_session_async_switch";
    private static final String NETWORK_HORSE_RACE_SWITCH = "network_horse_race_switch";
    private static final String NETWORK_HTTPS_SNI_ENABLE_SWITCH = "network_https_sni_enable_switch";
    private static final String NETWORK_HTTP_CACHE_FLAG = "network_http_cache_flag";
    private static final String NETWORK_HTTP_CACHE_SWITCH = "network_http_cache_switch";
    private static final String NETWORK_IDLE_SESSION_CLOSE_SWITCH = "network_idle_session_close_switch";
    private static final String NETWORK_IPV6_BLACKLIST_SWITCH = "network_ipv6_blacklist_switch";
    private static final String NETWORK_IPV6_BLACKLIST_TTL = "network_ipv6_blacklist_ttl";
    private static final String NETWORK_MONITOR_REQUESTS = "network_monitor_requests";
    private static final String NETWORK_NORMAL_THREAD_POOL_EXECUTOR_SIZE = "network_normal_thread_pool_executor_size";
    private static final String NETWORK_PING6_ENABLE_SWITCH = "network_ping6_enable_switch";
    private static final String NETWORK_QUIC_ENABLE_SWITCH = "network_quic_enable_switch";
    private static final String NETWORK_REQUEST_FORBIDDEN_BG = "network_request_forbidden_bg";
    private static final String NETWORK_REQUEST_STATISTIC_SAMPLE_RATE = "network_request_statistic_sample_rate";
    private static final String NETWORK_RESPONSE_BUFFER_SWITCH = "network_response_buffer_switch";
    private static final String NETWORK_SDK_GROUP = "networkSdk";
    private static final String NETWORK_SESSION_PRESET_HOSTS = "network_session_preset_hosts";
    private static final String NETWORK_SPDY_ENABLE_SWITCH = "network_spdy_enable_switch";
    private static final String NETWORK_URL_DEGRADE_LIST = "network_url_degrade_list";
    private static final String NETWORK_URL_WHITE_LIST_BG = "network_url_white_list_bg";
    private static final String NETWOTK_BG_FORBID_REQUEST_THRESHOLD = "network_bg_forbid_request_threshold";
    private static final String TAG = "awcn.OrangeConfigImpl";
    private static final String TNET_ENABLE_HEADER_CACHE = "tnet_enable_header_cache";
    private static boolean mOrangeValid = false;

    static {
        try {
            Class.forName("com.taobao.orange.OrangeConfig");
            mOrangeValid = true;
        } catch (Exception unused) {
            mOrangeValid = false;
        }
    }

    public void register() {
        if (!mOrangeValid) {
            ALog.w(TAG, "no orange sdk", (String) null, new Object[0]);
            return;
        }
        try {
            OrangeConfig.getInstance().registerListener(new String[]{NETWORK_SDK_GROUP}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
                public void onConfigUpdate(String str, boolean z) {
                    OrangeConfigImpl.this.onConfigUpdate(str);
                }
            });
            getConfig(NETWORK_SDK_GROUP, NETWORK_EMPTY_SCHEME_HTTPS_SWITCH, "true");
        } catch (Exception e) {
            ALog.e(TAG, "register fail", (String) null, e, new Object[0]);
        }
    }

    public void unRegister() {
        if (!mOrangeValid) {
            ALog.w(TAG, "no orange sdk", (String) null, new Object[0]);
            return;
        }
        OrangeConfig.getInstance().unregisterListener(new String[]{NETWORK_SDK_GROUP});
    }

    public String getConfig(String... strArr) {
        if (!mOrangeValid) {
            ALog.w(TAG, "no orange sdk", (String) null, new Object[0]);
            return null;
        }
        try {
            return OrangeConfig.getInstance().getConfig(strArr[0], strArr[1], strArr[2]);
        } catch (Exception e) {
            ALog.e(TAG, "get config failed!", (String) null, e, new Object[0]);
            return null;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(76:2|3|4|5|6|7|8|9|10|(1:12)|13|14|15|16|17|18|19|20|(1:22)|23|24|25|26|(1:28)|29|30|(1:32)|33|34|35|36|37|38|(1:40)|41|42|43|44|(1:46)|47|48|(3:50|(1:52)|53)|54|55|(1:57)|58|59|(1:61)|62|63|(1:65)|66|67|(1:69)|70|71|(1:73)|74|75|(1:77)|78|79|(1:81)|82|83|(1:85)|86|87|(1:89)|90|91|(1:93)|94|95|(1:97)|(3:98|99|(2:101|103)(1:106))) */
    /* JADX WARNING: Can't wrap try/catch for region: R(78:2|3|4|5|6|7|8|9|10|(1:12)|13|14|15|16|17|18|19|20|(1:22)|23|24|25|26|(1:28)|29|30|(1:32)|33|34|35|36|37|38|(1:40)|41|42|43|44|(1:46)|47|48|(3:50|(1:52)|53)|54|55|(1:57)|58|59|(1:61)|62|63|(1:65)|66|67|(1:69)|70|71|(1:73)|74|75|(1:77)|78|79|(1:81)|82|83|(1:85)|86|87|(1:89)|90|91|(1:93)|94|95|(1:97)|98|99|(2:101|103)(1:106)) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x008c */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x00a7 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x00c2 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x00dd */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x00fc */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x010d */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0124 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x013b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0156 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0171 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x0190 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x01ab */
    /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x01ca */
    /* JADX WARNING: Missing exception handler attribute for start block: B:54:0x01ec */
    /* JADX WARNING: Missing exception handler attribute for start block: B:58:0x020b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x003b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:62:0x022a */
    /* JADX WARNING: Missing exception handler attribute for start block: B:66:0x0241 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:70:0x0258 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:74:0x0277 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:78:0x0296 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0056 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:82:0x02ad */
    /* JADX WARNING: Missing exception handler attribute for start block: B:86:0x02cc */
    /* JADX WARNING: Missing exception handler attribute for start block: B:90:0x02fc */
    /* JADX WARNING: Missing exception handler attribute for start block: B:94:0x032c */
    /* JADX WARNING: Missing exception handler attribute for start block: B:98:0x034b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0071 */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x035f A[Catch:{ Exception -> 0x036a }] */
    /* JADX WARNING: Removed duplicated region for block: B:106:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0081 A[Catch:{ Exception -> 0x008c }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00f1 A[Catch:{ Exception -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0121 A[Catch:{ Exception -> 0x0124 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0138 A[Catch:{ Exception -> 0x013b }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0185 A[Catch:{ Exception -> 0x0190 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x01bf A[Catch:{ Exception -> 0x01ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01de A[Catch:{ Exception -> 0x01ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0200 A[Catch:{ Exception -> 0x020b }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x021f A[Catch:{ Exception -> 0x022a }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x023e A[Catch:{ Exception -> 0x0241 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0255 A[Catch:{ Exception -> 0x0258 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x026c A[Catch:{ Exception -> 0x0277 }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x028b A[Catch:{ Exception -> 0x0296 }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x02aa A[Catch:{ Exception -> 0x02ad }] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x02c1 A[Catch:{ Exception -> 0x02cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x02e0 A[Catch:{ Exception -> 0x02fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0310 A[Catch:{ Exception -> 0x032c }] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0340 A[Catch:{ Exception -> 0x034b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfigUpdate(java.lang.String r10) {
        /*
            r9 = this;
            java.lang.String r0 = "networkSdk"
            boolean r0 = r0.equals(r10)
            if (r0 == 0) goto L_0x036a
            java.lang.String r0 = "awcn.OrangeConfigImpl"
            java.lang.String r1 = "onConfigUpdate"
            r2 = 2
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.String r4 = "namespace"
            r5 = 0
            r3[r5] = r4
            r4 = 1
            r3[r4] = r10
            r6 = 0
            anet.channel.util.ALog.i(r0, r1, r6, r3)
            r0 = 3
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x003b }
            r1[r5] = r10     // Catch:{ Exception -> 0x003b }
            java.lang.String r3 = "network_empty_scheme_https_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x003b }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x003b }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x003b }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x003b }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x003b }
            anet.channel.strategy.SchemeGuesser r3 = anet.channel.strategy.SchemeGuesser.getInstance()     // Catch:{ Exception -> 0x003b }
            r3.setEnabled(r1)     // Catch:{ Exception -> 0x003b }
        L_0x003b:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0056 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = "network_spdy_enable_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x0056 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0056 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0056 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x0056 }
            anetwork.channel.config.NetworkConfigCenter.setSpdyEnabled(r1)     // Catch:{ Exception -> 0x0056 }
        L_0x0056:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0071 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0071 }
            java.lang.String r3 = "network_http_cache_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x0071 }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x0071 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0071 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0071 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x0071 }
            anetwork.channel.config.NetworkConfigCenter.setHttpCacheEnable(r1)     // Catch:{ Exception -> 0x0071 }
        L_0x0071:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x008c }
            r1[r5] = r10     // Catch:{ Exception -> 0x008c }
            java.lang.String r3 = "network_http_cache_flag"
            r1[r4] = r3     // Catch:{ Exception -> 0x008c }
            r1[r2] = r6     // Catch:{ Exception -> 0x008c }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x008c }
            if (r1 == 0) goto L_0x008c
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ Exception -> 0x008c }
            long r7 = r1.longValue()     // Catch:{ Exception -> 0x008c }
            anetwork.channel.config.NetworkConfigCenter.setCacheFlag(r7)     // Catch:{ Exception -> 0x008c }
        L_0x008c:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x00a7 }
            r1[r5] = r10     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r3 = "network_https_sni_enable_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x00a7 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x00a7 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x00a7 }
            anet.channel.AwcnConfig.setHttpsSniEnable(r1)     // Catch:{ Exception -> 0x00a7 }
        L_0x00a7:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x00c2 }
            r1[r5] = r10     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r3 = "network_accs_session_bg_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x00c2 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x00c2 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x00c2 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x00c2 }
            anet.channel.AwcnConfig.setAccsSessionCreateForbiddenInBg(r1)     // Catch:{ Exception -> 0x00c2 }
        L_0x00c2:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x00dd }
            r1[r5] = r10     // Catch:{ Exception -> 0x00dd }
            java.lang.String r3 = "network_request_statistic_sample_rate"
            r1[r4] = r3     // Catch:{ Exception -> 0x00dd }
            java.lang.String r3 = "10000"
            r1[r2] = r3     // Catch:{ Exception -> 0x00dd }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x00dd }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x00dd }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x00dd }
            anetwork.channel.config.NetworkConfigCenter.setRequestStatisticSampleRate(r1)     // Catch:{ Exception -> 0x00dd }
        L_0x00dd:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x00fc }
            r1[r5] = r10     // Catch:{ Exception -> 0x00fc }
            java.lang.String r3 = "network_request_forbidden_bg"
            r1[r4] = r3     // Catch:{ Exception -> 0x00fc }
            r1[r2] = r6     // Catch:{ Exception -> 0x00fc }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x00fc }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x00fc }
            if (r3 != 0) goto L_0x00fc
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x00fc }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x00fc }
            anetwork.channel.config.NetworkConfigCenter.setBgRequestForbidden(r1)     // Catch:{ Exception -> 0x00fc }
        L_0x00fc:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x010d }
            r1[r5] = r10     // Catch:{ Exception -> 0x010d }
            java.lang.String r3 = "network_url_white_list_bg"
            r1[r4] = r3     // Catch:{ Exception -> 0x010d }
            r1[r2] = r6     // Catch:{ Exception -> 0x010d }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x010d }
            anetwork.channel.config.NetworkConfigCenter.updateWhiteListMap(r1)     // Catch:{ Exception -> 0x010d }
        L_0x010d:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0124 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0124 }
            java.lang.String r3 = "network_biz_white_list_bg"
            r1[r4] = r3     // Catch:{ Exception -> 0x0124 }
            r1[r2] = r6     // Catch:{ Exception -> 0x0124 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0124 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0124 }
            if (r3 != 0) goto L_0x0124
            anetwork.channel.config.NetworkConfigCenter.updateBizWhiteList(r1)     // Catch:{ Exception -> 0x0124 }
        L_0x0124:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x013b }
            r1[r5] = r10     // Catch:{ Exception -> 0x013b }
            java.lang.String r3 = "network_amdc_preset_hosts"
            r1[r4] = r3     // Catch:{ Exception -> 0x013b }
            r1[r2] = r6     // Catch:{ Exception -> 0x013b }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x013b }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x013b }
            if (r3 != 0) goto L_0x013b
            anetwork.channel.config.NetworkConfigCenter.setAmdcPresetHosts(r1)     // Catch:{ Exception -> 0x013b }
        L_0x013b:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0156 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0156 }
            java.lang.String r3 = "network_horse_race_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x0156 }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x0156 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0156 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0156 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x0156 }
            anet.channel.AwcnConfig.setHorseRaceEnable(r1)     // Catch:{ Exception -> 0x0156 }
        L_0x0156:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0171 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0171 }
            java.lang.String r3 = "tnet_enable_header_cache"
            r1[r4] = r3     // Catch:{ Exception -> 0x0171 }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x0171 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0171 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0171 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x0171 }
            anet.channel.AwcnConfig.setTnetHeaderCacheEnable(r1)     // Catch:{ Exception -> 0x0171 }
        L_0x0171:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0190 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0190 }
            java.lang.String r3 = "network_quic_enable_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x0190 }
            r1[r2] = r6     // Catch:{ Exception -> 0x0190 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0190 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0190 }
            if (r3 != 0) goto L_0x0190
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0190 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x0190 }
            anet.channel.AwcnConfig.setQuicEnable(r1)     // Catch:{ Exception -> 0x0190 }
        L_0x0190:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x01ab }
            r1[r5] = r10     // Catch:{ Exception -> 0x01ab }
            java.lang.String r3 = "network_response_buffer_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x01ab }
            java.lang.String r3 = "true"
            r1[r2] = r3     // Catch:{ Exception -> 0x01ab }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x01ab }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x01ab }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x01ab }
            anetwork.channel.config.NetworkConfigCenter.setResponseBufferEnable(r1)     // Catch:{ Exception -> 0x01ab }
        L_0x01ab:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x01ca }
            r1[r5] = r10     // Catch:{ Exception -> 0x01ca }
            java.lang.String r3 = "network_get_session_async_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x01ca }
            r1[r2] = r6     // Catch:{ Exception -> 0x01ca }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x01ca }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x01ca }
            if (r3 != 0) goto L_0x01ca
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x01ca }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x01ca }
            anetwork.channel.config.NetworkConfigCenter.setGetSessionAsyncEnable(r1)     // Catch:{ Exception -> 0x01ca }
        L_0x01ca:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x01ec }
            r1[r5] = r10     // Catch:{ Exception -> 0x01ec }
            java.lang.String r3 = "network_bg_forbid_request_threshold"
            r1[r4] = r3     // Catch:{ Exception -> 0x01ec }
            r1[r2] = r6     // Catch:{ Exception -> 0x01ec }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x01ec }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x01ec }
            if (r3 != 0) goto L_0x01ec
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x01ec }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x01ec }
            if (r1 >= 0) goto L_0x01e9
            r1 = 0
        L_0x01e9:
            anetwork.channel.config.NetworkConfigCenter.setBgForbidRequestThreshold(r1)     // Catch:{ Exception -> 0x01ec }
        L_0x01ec:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x020b }
            r1[r5] = r10     // Catch:{ Exception -> 0x020b }
            java.lang.String r3 = "network_normal_thread_pool_executor_size"
            r1[r4] = r3     // Catch:{ Exception -> 0x020b }
            r1[r2] = r6     // Catch:{ Exception -> 0x020b }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x020b }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x020b }
            if (r3 != 0) goto L_0x020b
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x020b }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x020b }
            anet.channel.thread.ThreadPoolExecutorFactory.setNormalExecutorPoolSize(r1)     // Catch:{ Exception -> 0x020b }
        L_0x020b:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x022a }
            r1[r5] = r10     // Catch:{ Exception -> 0x022a }
            java.lang.String r3 = "network_idle_session_close_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x022a }
            r1[r2] = r6     // Catch:{ Exception -> 0x022a }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x022a }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x022a }
            if (r3 != 0) goto L_0x022a
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x022a }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x022a }
            anet.channel.AwcnConfig.setIdleSessionCloseEnable(r1)     // Catch:{ Exception -> 0x022a }
        L_0x022a:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0241 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0241 }
            java.lang.String r3 = "network_monitor_requests"
            r1[r4] = r3     // Catch:{ Exception -> 0x0241 }
            r1[r2] = r6     // Catch:{ Exception -> 0x0241 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0241 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0241 }
            if (r3 != 0) goto L_0x0241
            anetwork.channel.config.NetworkConfigCenter.setMonitorRequestList(r1)     // Catch:{ Exception -> 0x0241 }
        L_0x0241:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0258 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0258 }
            java.lang.String r3 = "network_session_preset_hosts"
            r1[r4] = r3     // Catch:{ Exception -> 0x0258 }
            r1[r2] = r6     // Catch:{ Exception -> 0x0258 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0258 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0258 }
            if (r3 != 0) goto L_0x0258
            anet.channel.AwcnConfig.registerPresetSessions(r1)     // Catch:{ Exception -> 0x0258 }
        L_0x0258:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0277 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0277 }
            java.lang.String r3 = "network_ipv6_blacklist_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x0277 }
            r1[r2] = r6     // Catch:{ Exception -> 0x0277 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0277 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0277 }
            if (r3 != 0) goto L_0x0277
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0277 }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x0277 }
            anet.channel.AwcnConfig.setIpv6BlackListEnable(r1)     // Catch:{ Exception -> 0x0277 }
        L_0x0277:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0296 }
            r1[r5] = r10     // Catch:{ Exception -> 0x0296 }
            java.lang.String r3 = "network_ipv6_blacklist_ttl"
            r1[r4] = r3     // Catch:{ Exception -> 0x0296 }
            r1[r2] = r6     // Catch:{ Exception -> 0x0296 }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x0296 }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0296 }
            if (r3 != 0) goto L_0x0296
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ Exception -> 0x0296 }
            long r7 = r1.longValue()     // Catch:{ Exception -> 0x0296 }
            anet.channel.AwcnConfig.setIpv6BlackListTtl(r7)     // Catch:{ Exception -> 0x0296 }
        L_0x0296:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x02ad }
            r1[r5] = r10     // Catch:{ Exception -> 0x02ad }
            java.lang.String r3 = "network_url_degrade_list"
            r1[r4] = r3     // Catch:{ Exception -> 0x02ad }
            r1[r2] = r6     // Catch:{ Exception -> 0x02ad }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x02ad }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x02ad }
            if (r3 != 0) goto L_0x02ad
            anetwork.channel.config.NetworkConfigCenter.setDegradeRequestList(r1)     // Catch:{ Exception -> 0x02ad }
        L_0x02ad:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x02cc }
            r1[r5] = r10     // Catch:{ Exception -> 0x02cc }
            java.lang.String r3 = "network_delay_retry_request_no_network"
            r1[r4] = r3     // Catch:{ Exception -> 0x02cc }
            r1[r2] = r6     // Catch:{ Exception -> 0x02cc }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x02cc }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x02cc }
            if (r3 != 0) goto L_0x02cc
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x02cc }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x02cc }
            anetwork.channel.config.NetworkConfigCenter.setRequestDelayRetryForNoNetwork(r1)     // Catch:{ Exception -> 0x02cc }
        L_0x02cc:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x02fc }
            r1[r5] = r10     // Catch:{ Exception -> 0x02fc }
            java.lang.String r3 = "network_bind_service_optimize"
            r1[r4] = r3     // Catch:{ Exception -> 0x02fc }
            r1[r2] = r6     // Catch:{ Exception -> 0x02fc }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x02fc }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x02fc }
            if (r3 != 0) goto L_0x02fc
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x02fc }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x02fc }
            android.content.Context r3 = anetwork.channel.http.NetworkSdkSetting.getContext()     // Catch:{ Exception -> 0x02fc }
            android.content.SharedPreferences r3 = android.preference.PreferenceManager.getDefaultSharedPreferences(r3)     // Catch:{ Exception -> 0x02fc }
            android.content.SharedPreferences$Editor r3 = r3.edit()     // Catch:{ Exception -> 0x02fc }
            java.lang.String r7 = "SERVICE_OPTIMIZE"
            r3.putBoolean(r7, r1)     // Catch:{ Exception -> 0x02fc }
            r3.apply()     // Catch:{ Exception -> 0x02fc }
        L_0x02fc:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x032c }
            r1[r5] = r10     // Catch:{ Exception -> 0x032c }
            java.lang.String r3 = "network_forbid_next_launch_optimize"
            r1[r4] = r3     // Catch:{ Exception -> 0x032c }
            r1[r2] = r6     // Catch:{ Exception -> 0x032c }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x032c }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x032c }
            if (r3 != 0) goto L_0x032c
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x032c }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x032c }
            android.content.Context r3 = anetwork.channel.http.NetworkSdkSetting.getContext()     // Catch:{ Exception -> 0x032c }
            android.content.SharedPreferences r3 = android.preference.PreferenceManager.getDefaultSharedPreferences(r3)     // Catch:{ Exception -> 0x032c }
            android.content.SharedPreferences$Editor r3 = r3.edit()     // Catch:{ Exception -> 0x032c }
            java.lang.String r7 = "NEXT_LAUNCH_FORBID"
            r3.putBoolean(r7, r1)     // Catch:{ Exception -> 0x032c }
            r3.apply()     // Catch:{ Exception -> 0x032c }
        L_0x032c:
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch:{ Exception -> 0x034b }
            r1[r5] = r10     // Catch:{ Exception -> 0x034b }
            java.lang.String r3 = "network_detect_enable_switch"
            r1[r4] = r3     // Catch:{ Exception -> 0x034b }
            r1[r2] = r6     // Catch:{ Exception -> 0x034b }
            java.lang.String r1 = r9.getConfig(r1)     // Catch:{ Exception -> 0x034b }
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x034b }
            if (r3 != 0) goto L_0x034b
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x034b }
            boolean r1 = r1.booleanValue()     // Catch:{ Exception -> 0x034b }
            anet.channel.AwcnConfig.setNetworkDetectEnable(r1)     // Catch:{ Exception -> 0x034b }
        L_0x034b:
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ Exception -> 0x036a }
            r0[r5] = r10     // Catch:{ Exception -> 0x036a }
            java.lang.String r10 = "network_ping6_enable_switch"
            r0[r4] = r10     // Catch:{ Exception -> 0x036a }
            r0[r2] = r6     // Catch:{ Exception -> 0x036a }
            java.lang.String r10 = r9.getConfig(r0)     // Catch:{ Exception -> 0x036a }
            boolean r0 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Exception -> 0x036a }
            if (r0 != 0) goto L_0x036a
            java.lang.Boolean r10 = java.lang.Boolean.valueOf(r10)     // Catch:{ Exception -> 0x036a }
            boolean r10 = r10.booleanValue()     // Catch:{ Exception -> 0x036a }
            anet.channel.AwcnConfig.setPing6Enable(r10)     // Catch:{ Exception -> 0x036a }
        L_0x036a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.config.OrangeConfigImpl.onConfigUpdate(java.lang.String):void");
    }
}
