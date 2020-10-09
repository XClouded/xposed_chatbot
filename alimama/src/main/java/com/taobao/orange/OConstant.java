package com.taobao.orange;

import anetwork.channel.util.RequestConstant;

public class OConstant {
    public static final String[] ACK_TAOBAO_HOSTS = {"orange-ack.alibaba.com", "orange-ack-pre.alibaba.com", "orange-ack-daily.alibaba.net"};
    public static final String[] ACK_YOUKU_HOSTS = {"orange-ack.youku.com", "orange-ack-pre.youku.com", "orange-ack-daily.heyi.test"};
    public static final String CANDIDATE_APPVER = "app_ver";
    public static final String CANDIDATE_BRAND = "m_brand";
    public static final String CANDIDATE_DIDHASH = "did_hash";
    public static final String CANDIDATE_MANUFACTURER = "m_fac";
    public static final String CANDIDATE_MODEL = "m_model";
    public static final String CANDIDATE_OSVER = "os_ver";
    public static final String CANDIDATE_SPNAME = "orange_candidate";
    public static final String CODE_POINT_EXP_BIND_SERVICE = "101";
    public static final String CODE_POINT_EXP_CREATE_TARGET_DIR = "104";
    public static final String CODE_POINT_EXP_GET_TARGET_DIR = "103";
    public static final String CODE_POINT_EXP_LOAD_CACHE = "102";
    public static final String[] DC_TAOBAO_HOSTS = {"orange-dc.alibaba.com", "orange-dc-pre.alibaba.com", "orange-dc-daily.alibaba.net"};
    public static final String[] DC_YOUKU_HOSTS = {"orange-dc.youku.com", "orange-dc-pre.youku.com", "orange-dc-daily.heyi.test"};
    public static final String DIMEN_CONFIG_NAME = "configName";
    public static final String DIMEN_CONFIG_VERSION = "configVersion";
    public static final int DOWN_GRADE_CLOSE = 0;
    public static final int DOWN_GRADE_ORANGE = 2;
    public static final int DOWN_GRADE_TB_SPEED = 1;
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final long IDLE_LAZY_LOAD_CONFIG = -1;
    public static final String KEY_APPKEY = "appKey";
    public static final String KEY_APPVERSION = "appVersion";
    public static final String KEY_CLIENTAPPINDEXVERSION = "clientAppIndexVersion";
    public static final String KEY_CLIENTVERSIONINDEXVERSION = "clientVersionIndexVersion";
    public static final String LAUNCH_APPVERSION = "appVersion";
    public static final String LAUNCH_ENVINDEX = "envIndex";
    public static final String LAUNCH_KEY_USERID = "userId";
    public static final String LAUNCH_ONLINEAPPKEY = "onlineAppKey";
    public static final String LAUNCH_PREAPPKEY = "preAppKey";
    public static final String LAUNCH_TESTAPPKEY = "dailyAppkey";
    @Deprecated
    public static final String LISTENERKEY_CONFIG_VERSION = "configVersion";
    @Deprecated
    public static final String LISTENERKEY_FROM_CACHE = "fromCache";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String MONITOR_MODULE = "OrangeConfig";
    public static final String MONITOR_PRIVATE_MODULE = "private_orange";
    public static final String ORANGE = "orange";
    public static final String POINT_ABTEST_DIDHASH = "did_hash";
    public static final String POINT_BOOT_PERF = "orange_boot_performance";
    public static final String POINT_CFG_RATE = "config_rate";
    public static final String POINT_CONFIG_ACK = "config_ack";
    public static final String POINT_CONFIG_NOTMATCH_COUNTS = "config_notmatch_counts";
    public static final String POINT_CONFIG_PENDING_UPDATE = "config_pending_update";
    public static final String POINT_CONFIG_REMOVE_COUNTS = "config_remove_counts";
    public static final String POINT_CONFIG_UPDATE = "config_update";
    public static final String POINT_CONFIG_USE = "config_use";
    public static final String POINT_DOWNGRADE = "getConfigDowngrade";
    public static final String POINT_EXCEPTION = "other_exception";
    public static final String POINT_FALLBACK_AVOID = "fallback_avoid";
    public static final String POINT_INDEX_ACK = "index_ack";
    public static final String POINT_INDEX_RATE = "index_rate";
    public static final String POINT_PERSIST_FAIL_COUNTS = "persist_fail_counts";
    public static final String POINT_RESTORE_FAIL_COUNTS = "restore_fail_counts";
    public static final String[][] PROBE_HOSTS = {new String[]{"acs.m.taobao.com"}, new String[]{"acs.wapa.taobao.com"}, new String[]{"acs.waptest.taobao.com"}};
    public static final String REFLECT_APPMONITOR = "com.alibaba.mtl.appmonitor.AppMonitor";
    public static final String REFLECT_NETWORKSDK = "anetwork.channel.degrade.DegradableNetwork";
    public static final String REFLECT_NETWORK_INTERCEPTOR = "anetwork.channel.interceptor.Interceptor";
    public static final String REFLECT_NETWORK_INTERCEPTORMANAGER = "anetwork.channel.interceptor.InterceptorManager";
    public static final String REFLECT_SECURITYGUARD = "com.alibaba.wireless.security.open.SecurityGuardManager";
    public static final String REFLECT_TLOG = "com.taobao.tlog.adapter.AdapterForTLog";
    public static final String REFLECT_UTDID = "com.ta.utdid2.device.UTDevice";
    public static final String REQTYPE_ACK_CONFIG_UPDATE = "/batchNamespaceUpdateAck";
    public static final String REQTYPE_ACK_INDEX_UPDATE = "/indexUpdateAck";
    public static final String REQTYPE_DOWNLOAD_RESOURCE = "/downloadResource";
    public static final String REQTYPE_INDEX_UPDATE = "/checkUpdate";
    public static final String SDK_VERSION = "1.5.4.40";
    public static final String SYSKEY_ACKVIPS = "ackVips";
    public static final String SYSKEY_DCVIPS = "dcVips";
    public static final String SYSKEY_DELAYACK_INTERVAL = "delayAckInterval";
    public static final String SYSKEY_DOWNGRADE = "downgrade";
    public static final String SYSKEY_FALLBACK_AVOID = "fallbackAvoid";
    public static final String SYSKEY_INDEXUPD_MODE = "indexUpdateMode";
    public static final String SYSKEY_PROBE_HOSTS = "hosts";
    public static final String SYSKEY_REPORT_UPDACK = "reportUpdateAck";
    public static final String SYSKEY_REQ_RETRY_NUM = "reqRetryNum";
    public static final String UTF_8 = "utf-8";

    public enum ENV {
        ONLINE(0, "online"),
        PREPARE(1, RequestConstant.ENV_PRE),
        TEST(2, "test");
        
        private String des;
        private int envMode;

        private ENV(int i, String str) {
            this.envMode = i;
            this.des = str;
        }

        public int getEnvMode() {
            return this.envMode;
        }

        public String getDes() {
            return this.des;
        }

        public static ENV valueOf(int i) {
            switch (i) {
                case 0:
                    return ONLINE;
                case 1:
                    return PREPARE;
                case 2:
                    return TEST;
                default:
                    return ONLINE;
            }
        }
    }

    public enum SERVER {
        TAOBAO,
        YOUKU;

        public static SERVER valueOf(int i) {
            switch (i) {
                case 0:
                    return TAOBAO;
                case 1:
                    return YOUKU;
                default:
                    return TAOBAO;
            }
        }
    }

    public enum UPDMODE {
        O_XMD,
        O_EVENT,
        O_ALL;

        public static UPDMODE valueOf(int i) {
            switch (i) {
                case 0:
                    return O_XMD;
                case 1:
                    return O_EVENT;
                case 2:
                    return O_ALL;
                default:
                    return O_XMD;
            }
        }
    }
}
