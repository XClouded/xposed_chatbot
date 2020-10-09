package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.ITlog;
import com.ali.user.mobile.log.TLogAdapter;
import com.alibaba.ha.adapter.AliHaConfig;
import com.ta.utdid2.device.UTDevice;
import com.taobao.android.tlog.message.TLogTaobaoMessage;
import com.taobao.android.tlog.uploader.TLogUploader;
import com.taobao.login4android.Login;
import com.taobao.tao.log.LogLevel;
import com.taobao.tao.log.TLog;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.upload.FileUploadListener;
import com.taobao.tao.log.upload.LogFileUploadManager;
import com.uploader.export.UploaderGlobal;
import com.uploader.portal.UploaderDependencyImpl;
import com.uploader.portal.UploaderEnvironmentImpl2;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class UNWTlog implements ITlog {
    private String namePrefix;
    private String rsaPublishKey;

    public void loge(String str, String str2, String str3) {
        TLog.loge(str, str2, str3);
    }

    public void loge(String str, String str2, Throwable th) {
        TLog.loge(str, str2, th);
    }

    public void logw(String str, String str2, String str3) {
        TLog.logw(str, str2, str3);
    }

    public void logw(String str, String str2, Throwable th) {
        TLog.logw(str, str2, th);
    }

    public void logv(String str, String str2, String str3) {
        TLog.logv(str, str2, str3);
    }

    public void upload() {
        new LogFileUploadManager(UNWManager.getInstance().application).uploadWithFilePrefix("FEEDBACK", "etao_feedback", (Map<String, String>) new HashMap(), (FileUploadListener) new FileUploadListener() {
            public void onError(String str, String str2, String str3) {
                TLogAdapter.d("Tlog", "uploadWithFilePrefix failure! " + str2 + " msg:" + str3);
            }

            public void onSucessed(String str, String str2) {
                TLogAdapter.d("Tlog", "uploadWithFilePrefix success!");
            }
        });
    }

    public UNWTlog(@NotNull String str, @NotNull String str2) {
    }

    public void init() {
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        try {
            TLogInitializer.getInstance().accsServiceId = "ha-remote-debug";
            TLogInitializer.getInstance().ossBucketName = "motu-debug-log";
            TLogInitializer.getInstance().changeRsaPublishKey(this.rsaPublishKey);
            TLogInitializer.getInstance().accsTag = "default";
            TLogInitializer.getInstance().builder(UNWManager.getInstance().application, LogLevel.D, TLogInitializer.DEFAULT_DIR, this.namePrefix, iSecurity.getAppkey(), UNWManager.getInstance().getAppVersion()).setApplication(UNWManager.getInstance().application).setSecurityKey("8951ae070be6560f4fc1401e90a83a4e").setUserNick(Login.getNick()).setUtdid(UTDevice.getUtdid(UNWManager.getInstance().application)).init();
        } catch (Exception unused) {
        }
        TLogInitializer.getInstance().setLogUploader(new TLogUploader());
        TLogInitializer.getInstance().setMessageSender(new TLogTaobaoMessage());
        uploadInit();
    }

    private void uploadInit() {
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        UploaderGlobal.setContext(UNWManager.getInstance().application);
        UploaderGlobal.putElement(0, iSecurity.getAppkey());
        UploaderGlobal.putElement(1, iSecurity.getAppkey());
        UploaderEnvironmentImpl2 uploaderEnvironmentImpl2 = new UploaderEnvironmentImpl2(UNWManager.getInstance().application);
        uploaderEnvironmentImpl2.setEnvironment(getTlogEnv());
        UploaderGlobal.putDependency(new UploaderDependencyImpl(UNWManager.getInstance().application, uploaderEnvironmentImpl2));
        TLogAdapter.d("etao.application.onCreate", " [etao] application.onCeate");
    }

    private int getTlogEnv() {
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment != null && !iAppEnvironment.isProd()) {
            return iAppEnvironment.isPre() ? 1 : 2;
        }
        return 0;
    }

    private AliHaConfig buildAliHaConfig() {
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        if (iSecurity == null) {
            return new AliHaConfig();
        }
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment == null) {
            return new AliHaConfig();
        }
        AliHaConfig aliHaConfig = new AliHaConfig();
        aliHaConfig.isAliyunos = false;
        aliHaConfig.appKey = iSecurity.getAppkey();
        aliHaConfig.userNick = Login.getNick();
        aliHaConfig.channel = iAppEnvironment.getChannelId();
        aliHaConfig.appVersion = UNWManager.getInstance().getAppVersion();
        aliHaConfig.application = UNWManager.getInstance().application;
        aliHaConfig.context = UNWManager.getInstance().application;
        return aliHaConfig;
    }
}
