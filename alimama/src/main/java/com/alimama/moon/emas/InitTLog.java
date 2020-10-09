package com.alimama.moon.emas;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.ali.user.mobile.log.TLogAdapter;
import com.alimama.moon.BuildConfig;
import com.alimama.moon.init.InitConstants;
import com.alimama.union.app.configproperties.EnvHelper;
import com.taobao.android.tlog.message.TLogTaobaoMessage;
import com.taobao.android.tlog.uploader.TLogUploader;
import com.taobao.login4android.Login;
import com.taobao.tao.log.LogLevel;
import com.taobao.tao.log.TLogInitializer;
import com.uploader.export.UploaderGlobal;
import com.uploader.portal.UploaderDependencyImpl;
import com.uploader.portal.UploaderEnvironmentImpl2;
import com.ut.device.UTDevice;

public class InitTLog {
    public static void init(Context context, boolean z, String str) {
        String str2 = z ? "moon" : "channel";
        try {
            TLogInitializer.getInstance().accsServiceId = "ha-remote-debug";
            TLogInitializer.getInstance().ossBucketName = "motu-debug-log";
            TLogInitializer.getInstance().changeRsaPublishKey(InitConstants.Tlog.rsaPublishKey);
            TLogInitializer.getInstance().accsTag = "default";
            TLogInitializer.getInstance().builder(context, LogLevel.D, TLogInitializer.DEFAULT_DIR, str2, str, BuildConfig.VERSION_NAME).setApplication((Application) context).setSecurityKey("8951ae070be6560f4fc1401e90a83a4e").setUserNick(Login.getNick()).setUtdid(UTDevice.getUtdid(context)).init();
        } catch (Exception e) {
            Log.d("ACCSInitAction", "e: " + e.getMessage());
        }
        TLogInitializer.getInstance().setLogUploader(new TLogUploader());
        TLogInitializer.getInstance().setMessageSender(new TLogTaobaoMessage());
        UploaderGlobal.setContext(context);
        String currentApiEnv = EnvHelper.getInstance().getCurrentApiEnv();
        char c = 65535;
        int hashCode = currentApiEnv.hashCode();
        int i = 2;
        if (hashCode != -1012222381) {
            if (hashCode != -318370553) {
                if (hashCode == 95458899 && currentApiEnv.equals("debug")) {
                    c = 2;
                }
            } else if (currentApiEnv.equals(EnvHelper.API_ENV_PREPARE)) {
                c = 0;
            }
        } else if (currentApiEnv.equals("online")) {
            c = 1;
        }
        switch (c) {
            case 0:
                i = 1;
                break;
            case 2:
                break;
            default:
                i = 0;
                break;
        }
        UploaderGlobal.putElement(0, str);
        UploaderGlobal.putElement(1, str);
        UploaderEnvironmentImpl2 uploaderEnvironmentImpl2 = new UploaderEnvironmentImpl2(context);
        uploaderEnvironmentImpl2.setEnvironment(i);
        UploaderGlobal.putDependency(new UploaderDependencyImpl(context, uploaderEnvironmentImpl2));
        TLogAdapter.d("moon.application.onCreate", " [moon] application.onCeate");
    }
}
