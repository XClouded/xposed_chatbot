package com.taobao.tlog.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListener;
import com.taobao.tao.log.LogLevel;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogController;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.TLogUtils;
import java.io.File;
import java.util.Map;

public class TLogSwitchService {
    public static final String REMOTE_DEBUGER_ANDROID = "remote_debug";
    private static final String TAG = "TLOG_MONITOR";

    public static void init(final Context context) {
        String[] strArr = {REMOTE_DEBUGER_ANDROID};
        Log.e(TAG, "regist orange listener, groupName is remote_debug");
        OrangeConfig.getInstance().registerListener(strArr, (OrangeConfigListener) new OrangeConfigListener() {
            public void onConfigUpdate(String str) {
                Map<String, String> configs = OrangeConfig.getInstance().getConfigs(str);
                if (configs != null) {
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    String str2 = configs.get(TLogConstant.REMOTE_DEBUGER_LOG_DESTROY);
                    String str3 = configs.get(TLogConstant.REMOTE_DEBUGER_LOG_SWITCH);
                    String str4 = configs.get(TLogConstant.REMOTE_DEBUGER_LOG_LEVEL);
                    String str5 = configs.get(TLogConstant.REMOTE_DEBUGER_LOG_MODULE);
                    String str6 = configs.get("tlog_start_up_sampling");
                    if (TLogController.getInstance() != null) {
                        Log.e(TLogSwitchService.TAG, "tlogDestroy is: " + str2 + "tlogSwitch is : " + str3 + "  tlogLevel is : " + str4 + "  tlogModule is : " + str5);
                        if (TextUtils.isEmpty(str2) || !str2.equals("true")) {
                            edit.putBoolean(TLogConstant.REMOTE_DEBUGER_LOG_DESTROY, false).apply();
                            if (TextUtils.isEmpty(str3) || !str3.equals("false")) {
                                edit.putBoolean(TLogConstant.REMOTE_DEBUGER_LOG_SWITCH, true).apply();
                            } else {
                                Log.e(TLogSwitchService.TAG, "switch tlog function");
                                TLogController.getInstance().closeLog();
                                edit.putBoolean(TLogConstant.REMOTE_DEBUGER_LOG_SWITCH, false).apply();
                            }
                            if (!TextUtils.isEmpty(str4)) {
                                Log.e(TLogSwitchService.TAG, "change tlog level : " + str4);
                                LogLevel convertLogLevel = TLogUtils.convertLogLevel(str4);
                                edit.putString(TLogConstant.REMOTE_DEBUGER_LOG_LEVEL, str4).apply();
                                TLogController.getInstance().setLogLevel(convertLogLevel);
                            }
                            if (!TextUtils.isEmpty(str5)) {
                                Log.e(TLogSwitchService.TAG, "tlog model : " + str5);
                                if (TLogConstant.TLOG_MODULE_OFF.equals(str5)) {
                                    TLogController.getInstance().cleanModuleFilter();
                                    edit.remove(TLogConstant.REMOTE_DEBUGER_LOG_MODULE).apply();
                                } else {
                                    Map<String, LogLevel> makeModule = TLogUtils.makeModule(str5);
                                    if (makeModule != null && makeModule.size() > 0) {
                                        TLogController.getInstance().addModuleFilter(makeModule);
                                        edit.putString(TLogConstant.REMOTE_DEBUGER_LOG_MODULE, str5).apply();
                                    }
                                }
                            }
                            if (!TextUtils.isEmpty(str6)) {
                                Log.e(TLogSwitchService.TAG, "change tlog start up sampling : " + str6);
                                try {
                                    TLogInitializer.getInstance().startUpSampling(Integer.valueOf(Integer.parseInt(str6)));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                }
                            }
                        } else {
                            Log.e(TLogSwitchService.TAG, "destroy tlog");
                            TLogController.getInstance().closeLog();
                            TLogUtils.cleanDir(new File(TLogInitializer.getInstance().getFileDir()));
                            edit.putBoolean(TLogConstant.REMOTE_DEBUGER_LOG_DESTROY, true).apply();
                        }
                    }
                } else {
                    Log.i(TLogSwitchService.TAG, "TLogConfigSwitchReceiver --> the config is null!");
                }
            }
        });
    }
}
