package alimama.com.unwetaologger;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwetaologger.base.ErrorContent;
import alimama.com.unwetaologger.base.LogContent;
import alimama.com.unwetaologger.base.SuccessContent;
import alimama.com.unwetaologger.base.UNWLogger;
import alimama.com.unwetaologger.base.UNWLoggerManager;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.util.Map;

public class EtaoLoggerImpl implements IEtaoLogger {
    public void init() {
        UNWLoggerManager.getInstance().setLocalEnable(UNWManager.getInstance().getDebuggable());
        UNWLoggerManager.getInstance().setEnable(true);
        UNWLoggerManager.getInstance().setLevel(1);
    }

    public void info(@NonNull String str, @NonNull String str2, @NonNull String str3) {
        info(str, str2, str3, (Map<String, String>) null);
    }

    public void info(@NonNull String str, @NonNull String str2, @NonNull String str3, Map<String, String> map) {
        LogContent logContent = new LogContent();
        logContent.setPoint(str2);
        logContent.setMsg(str3);
        if (map != null) {
            logContent.setInfoMap(map);
        }
        UNWLoggerManager.getInstance().getLoggerByModule(str).info(logContent);
    }

    public void error(@NonNull String str, @NonNull String str2, @NonNull String str3) {
        error(str, str2, str3, (String) null, (String) null);
    }

    public void error(@NonNull String str, @NonNull String str2, @NonNull String str3, String str4) {
        error(str, str2, str3, str4, (String) null);
    }

    public void error(@NonNull String str, @NonNull String str2, @NonNull String str3, String str4, String str5) {
        error(str, str2, str3, str4, (String) null, (Map<String, String>) null);
    }

    public void error(@NonNull String str, @NonNull String str2, @NonNull String str3, String str4, String str5, Map<String, String> map) {
        ErrorContent errorContent = new ErrorContent();
        errorContent.setPoint(str2);
        errorContent.setMsg(str3);
        if (TextUtils.isEmpty(str4)) {
            errorContent.setErrorCode("1000");
        } else {
            errorContent.setErrorCode(str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            errorContent.setName(str5);
        }
        if (map != null) {
            errorContent.setInfoMap(map);
        }
        UNWLoggerManager.getInstance().getLoggerByModule(str).error(errorContent);
    }

    public void success(String str, String str2) {
        SuccessContent successContent = new SuccessContent();
        successContent.setPoint(str2);
        UNWLoggerManager.getInstance().getLoggerByModule(str).succeed(successContent);
    }

    public void fail(String str, String str2, String str3) {
        UNWLogger loggerByModule = UNWLoggerManager.getInstance().getLoggerByModule(str);
        ErrorContent errorContent = new ErrorContent();
        errorContent.setType(UNWLogger.LOG_VALUE_TYPE_USER);
        errorContent.setSubType("mtop");
        errorContent.setPoint(str2);
        errorContent.setMsg(str3);
        errorContent.setErrorCode("-1000");
        loggerByModule.error(errorContent);
    }

    public void success(String str, String str2, Map<String, String> map) {
        SuccessContent successContent = new SuccessContent();
        successContent.setPoint(str2);
        successContent.setInfoMap(map);
        UNWLoggerManager.getInstance().getLoggerByModule(str).succeed(successContent);
    }

    public void fail(String str, String str2, String str3, Map<String, String> map) {
        UNWLogger loggerByModule = UNWLoggerManager.getInstance().getLoggerByModule(str);
        ErrorContent errorContent = new ErrorContent();
        errorContent.setType(UNWLogger.LOG_VALUE_TYPE_USER);
        errorContent.setSubType("mtop");
        errorContent.setPoint(str2);
        errorContent.setMsg(str3);
        errorContent.setErrorCode("-1000");
        errorContent.setInfoMap(map);
        loggerByModule.error(errorContent);
    }
}
