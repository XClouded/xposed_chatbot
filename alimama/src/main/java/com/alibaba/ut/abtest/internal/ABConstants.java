package com.alibaba.ut.abtest.internal;

import java.io.File;
import java.nio.charset.Charset;

public interface ABConstants {

    public interface Analytics {
    }

    public interface BasicConstants {
        public static final boolean CONFIG_DATA_TRIGGER_ENABLED_DEFAULT = true;
        public static final long CONFIG_DOWNLOAD_EXPERIMENT_DATA_DELAY_TIME_DEFAULT = 60000;
        public static final boolean CONFIG_ENABLED_DEFAULT = true;
        public static final boolean CONFIG_NAV_ENABLED_DEFAULT = true;
        public static final long CONFIG_REQUEST_EXPERIMENT_DATA_INTERVAL_TIME_DEFAULT = 60000;
        public static final boolean CONFIG_TRACK_AUTO_ENABLED_DEFAULT = true;
        public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
        public static final String DEFAULT_VARIATION_NAME = "bucket";
        public static final String MULTIPROCESS_CLIENT_CLASSNAME = "com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientImpl";
        public static final String PUSHCLIENT_CLASSNAME = "com.alibaba.ut.abtest.push.UTABPushClientImpl";
        public static final String TRACK_PREFIX = "aliabtest";
        public static final String URI_PARAMNAME_ABTEST = "utabtest";
    }

    public interface Database {
        public static final String DB_NAME = "ut-abtest-v2.db";
        public static final int DB_VERSION = 1;
    }

    public interface MultiProcess {
        public static final int MSG_DEBUG_MODE = 1000;
    }

    public interface Operator {
        public static final String NAV_LOOPBACK = "UTABTEST-LOOPBACK";
        public static final String NAV_LOOPBACK_VALUE_ALLOW = "allow";
        public static final String NAV_LOOPBACK_VALUE_DISALLOW = "disallow";
        public static final String NAV_LOOPBACK_VALUE_IGNORE = "ignore";
        public static final String PARAMETER_DELETE = "UTABTEST-DELETE";
        public static final String URI_ANY = "UTABTEST-ANY";
    }

    public interface Path {
        public static final String FILE_PATH = (ROOT_PATH + File.separator + "File");
        public static final String ROOT_PATH = (File.separator + "UT_AB");
    }

    public interface Pipeline {
        public static final int CONNECTION_READ_TIMEOUT_MS = 10000;
        public static final int CONNECTION_TIMEOUT_MS = 15000;
        public static final String HOST_DAILY = "http://abtest-daily.tmall.net";
        public static final String HOST_PREPARE = "http://preabtest.alibaba-inc.com";
        public static final String HOST_PRODUCT = "https://abtest.alibaba.com";
        public static final int SOCKET_TIMEOUT_MS = 10000;
    }

    public interface Preference {
        public static final String EXPERIMENT_DATA_SIGNATURE = "experimentDataSignature";
        public static final String EXPERIMENT_DATA_VERSION = "experimentDataVersion";
        public static final String NAME = "ut-ab";
        public static final String USER_ID = "uid";
        public static final String USER_LONG_ID = "luid";
        public static final String USER_LONG_NICK = "lun";
        public static final String USER_NICK = "un";
    }

    public interface TaskType {
        public static final int DOWNLOAD_EXPERIMENT_FILE = 1002;
        public static final int SYNC_CROWD = 1001;
    }
}
