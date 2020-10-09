package com.taobao.tao.log;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.TLogSecret;
import com.taobao.tao.log.godeye.GodeyeConfig;
import com.taobao.tao.log.godeye.GodeyeInitializer;
import com.taobao.tao.log.message.MessageInfo;
import com.taobao.tao.log.message.MessageSender;
import com.taobao.tao.log.monitor.DefaultTLogMonitorImpl;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.task.CommandManager;
import com.taobao.tao.log.task.HeapDumpReplyTask;
import com.taobao.tao.log.task.MethodTraceReplyTask;
import com.taobao.tao.log.task.PullTask;
import com.taobao.tao.log.task.StartUpRequestTask;
import com.taobao.tao.log.upload.LogUploader;
import com.taobao.tao.log.uploader.service.TLogFileUploader;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TLogInitializer {
    public static final String DEFAULT_DIR = "logs";
    public static final int INIT_END = 2;
    public static final int INIT_ING = 1;
    public static final int INIT_NO = 0;
    private static final String NAMEPREFIX = "TAOBAO";
    private static final String TAG = "TLOG.TLogInitializer";
    public String accsServiceId;
    public String accsTag;
    private String appId;
    private String appVersion;
    private String appkey;
    private Application application;
    private String authCode;
    private Context context;
    private boolean debugByUsr;
    public Map<String, TLogFileUploader> fileUploaderMap;
    private boolean isDebug;
    private boolean isVersionUpdate;
    public File logDir;
    private LogLevel logLevel;
    private LogUploader logUploader;
    private volatile int mInitState;
    private boolean mInitSync;
    private String mRandomSecret;
    public String messageHostName;
    private MessageSender messageSender;
    private String namePrefix;
    public String ossBucketName;
    private boolean storeLogOnInternal;
    private TLogMonitor tLogMonitor;
    private String ttid;
    private String userNick;
    private String utdid;

    private TLogInitializer() {
        this.isDebug = false;
        this.appVersion = "";
        this.utdid = "bbbbbbbbbbbbbbbbb";
        this.ttid = "-";
        this.userNick = "";
        this.logLevel = LogLevel.E;
        this.debugByUsr = false;
        this.mInitSync = true;
        this.mInitState = 0;
        this.ossBucketName = "ha-remote-log";
        this.messageHostName = "adash.emas-ha.cn";
        this.accsServiceId = "emas-ha";
        this.accsTag = null;
        this.fileUploaderMap = new ConcurrentHashMap();
        this.logUploader = null;
        this.messageSender = null;
        this.tLogMonitor = null;
        this.isVersionUpdate = false;
        this.authCode = "";
        this.storeLogOnInternal = false;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final TLogInitializer INSTANCE = new TLogInitializer();

        private SingletonHolder() {
        }
    }

    public static TLogInitializer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public TLogInitializer init() {
        if (this.mInitState != 0) {
            return this;
        }
        this.mInitState = 1;
        try {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
            if (defaultSharedPreferences.contains(TLogConstant.REMOTE_DEBUGER_LOG_VERSION)) {
                String string = defaultSharedPreferences.getString(TLogConstant.REMOTE_DEBUGER_LOG_VERSION, (String) null);
                if (string == null || !string.equals(this.appVersion)) {
                    this.isVersionUpdate = true;
                } else {
                    this.isVersionUpdate = false;
                }
            } else {
                this.isVersionUpdate = true;
            }
            if (defaultSharedPreferences.contains(TLogConstant.REMOTE_DEBUGER_LOG_LEVEL) && !this.isVersionUpdate) {
                this.logLevel = TLogUtils.convertLogLevel(defaultSharedPreferences.getString(TLogConstant.REMOTE_DEBUGER_LOG_LEVEL, "ERROR"));
                TLogController.getInstance().updateLogLevel(this.logLevel);
            }
            if (defaultSharedPreferences.contains(TLogConstant.REMOTE_DEBUGER_LOG_MODULE) && !this.isVersionUpdate) {
                TLogController.getInstance().addModuleFilter(TLogUtils.makeModule(defaultSharedPreferences.getString(TLogConstant.REMOTE_DEBUGER_LOG_MODULE, (String) null)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TLogNative.appenderOpen(this.logLevel.getIndex(), this.context.getFilesDir().getAbsolutePath() + File.separator + DEFAULT_DIR, this.logDir.getAbsolutePath(), this.namePrefix, this.appkey);
        if (TLogNative.isSoOpen()) {
            TLogNative.setConsoleLogOpen(false);
        }
        TLogController.getInstance().updateLogLevel(this.logLevel);
        CommandManager.getInstance().init();
        this.mInitState = 2;
        TLog.loge("tlog", "init", "tlog init end !" + this.logLevel);
        if (!this.mInitSync) {
            TLogController.getInstance().updateAsyncConfig();
        }
        godeyeInit();
        return this;
    }

    private void godeyeInit() {
        GodeyeInitializer.getInstance().registGodEyeReponse(OpCode.METHOD_TRACE_DUMP, new MethodTraceReplyTask());
        GodeyeInitializer.getInstance().registGodEyeReponse(OpCode.HEAP_DUMP, new HeapDumpReplyTask());
        GodeyeConfig godeyeConfig = new GodeyeConfig();
        godeyeConfig.appVersion = getInstance().getAppVersion();
        godeyeConfig.packageTag = null;
        godeyeConfig.utdid = getUTDID();
        godeyeConfig.appId = getInstance().getAppId();
        if (getInstance().getApplication() != null) {
            GodeyeInitializer.getInstance().init(getInstance().getApplication(), godeyeConfig);
        }
    }

    @TargetApi(8)
    public TLogInitializer builder(Context context2, LogLevel logLevel2, String str, String str2, String str3, String str4) {
        if (this.mInitState != 0) {
            return this;
        }
        this.isDebug = isAPKDebug(context2);
        this.logLevel = logLevel2;
        this.context = context2;
        this.appkey = str3;
        this.appVersion = str4;
        if (TextUtils.isEmpty(str2)) {
            str2 = NAMEPREFIX;
        }
        this.namePrefix = str2;
        if (TextUtils.isEmpty(str)) {
            str = DEFAULT_DIR;
        }
        if (this.storeLogOnInternal) {
            this.logDir = context2.getDir(str, 0);
        } else {
            File file = null;
            try {
                file = context2.getExternalFilesDir(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file == null) {
                file = context2.getDir(str, 0);
            }
            this.logDir = file;
        }
        return this;
    }

    public TLogInitializer setAppId(String str) {
        this.appId = str;
        return this;
    }

    public TLogInitializer setApplication(Application application2) {
        this.application = application2;
        return this;
    }

    public TLogInitializer setAppVersion(String str) {
        this.appVersion = str;
        return this;
    }

    public TLogInitializer setUtdid(String str) {
        this.utdid = str;
        return this;
    }

    public TLogInitializer setSecurityKey(String str) {
        this.mRandomSecret = str;
        return this;
    }

    public TLogInitializer changeRsaPublishKey(String str) {
        if (str != null) {
            TLogSecret.getInstance().setRsapublickey(str);
        }
        return this;
    }

    public TLogInitializer setMessageSender(MessageSender messageSender2) {
        this.messageSender = messageSender2;
        if (this.messageSender != null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.context = this.context;
            messageInfo.appKey = getInstance().getAppkey();
            messageInfo.accsServiceId = this.accsServiceId;
            this.messageSender.init(messageInfo);
            StartUpRequestTask.execute();
            PullTask.getInstance().start();
        }
        return this;
    }

    public TLogInitializer setTLogUserDefineUploader(String str, TLogFileUploader tLogFileUploader) {
        try {
            this.fileUploaderMap.put(str, tLogFileUploader);
        } catch (Exception e) {
            Log.e(TAG, "regist tlog user define uploader error", e);
        }
        return this;
    }

    public TLogInitializer setLogUploader(LogUploader logUploader2) {
        this.logUploader = logUploader2;
        return this;
    }

    public TLogInitializer setTTid(String str) {
        this.ttid = str;
        return this;
    }

    public TLogInitializer setAuthCode(String str) {
        this.authCode = str;
        return this;
    }

    public TLogInitializer useDataStoreLog(boolean z) {
        this.storeLogOnInternal = z;
        return this;
    }

    public TLogInitializer setDebugMode(boolean z) {
        this.debugByUsr = true;
        this.isDebug = z;
        return this;
    }

    public TLogInitializer setInitSync(boolean z) {
        this.mInitSync = z;
        return this;
    }

    public TLogInitializer settLogMonitor(TLogMonitor tLogMonitor2) {
        this.tLogMonitor = tLogMonitor2;
        return this;
    }

    public void updateLogLevel(String str) {
        try {
            this.logLevel = TLogUtils.convertLogLevel(str);
            TLogController.getInstance().setLogLevel(this.logLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInitSync() {
        return this.mInitSync;
    }

    public int getInitState() {
        return this.mInitState;
    }

    public void startUpSampling(Integer num) {
        StartUpRequestTask.updateSample(num);
    }

    public static ITLogController getTLogControler() {
        return TLogController.getInstance();
    }

    public TLogInitializer setUserNick(String str) {
        this.userNick = str;
        return this;
    }

    public String getNameprefix() {
        return this.namePrefix;
    }

    public LogUploader getLogUploader() {
        return this.logUploader;
    }

    public MessageSender getMessageSender() {
        return this.messageSender;
    }

    public TLogMonitor gettLogMonitor() {
        if (this.tLogMonitor == null) {
            this.tLogMonitor = new DefaultTLogMonitorImpl();
        }
        return this.tLogMonitor;
    }

    public String getTtid() {
        return this.ttid;
    }

    public Context getContext() {
        return this.context;
    }

    public Application getApplication() {
        return this.application;
    }

    public String getSecurityKey() {
        return this.mRandomSecret;
    }

    public String getAppId() {
        if (this.appId == null) {
            this.appId = this.appkey + "@android";
        }
        return this.appId;
    }

    public String getAppkey() {
        return this.appkey;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public static String getUTDID() {
        return getInstance().utdid;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public String getFileDir() {
        return this.logDir.getAbsolutePath();
    }

    public String getAuthCode() {
        return this.authCode;
    }

    public boolean isDebugable() {
        return this.isDebug;
    }

    private boolean isAPKDebug(Context context2) {
        if (this.debugByUsr) {
            return this.isDebug;
        }
        try {
            if ((context2.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
