package com.alimama.union.app.logger;

import alimama.com.unwetaologger.base.ErrorContent;
import alimama.com.unwetaologger.base.LogContent;
import alimama.com.unwetaologger.base.UNWLogger;
import alimama.com.unwetaologger.base.UNWLoggerManager;
import android.net.http.SslError;
import android.text.TextUtils;
import com.taobao.login4android.constants.LoginStatus;
import java.util.Map;

public class NewMonitorLogger {

    public static class Update {
        private static final String MODULE = "Appupdate";

        public static void updatePostFailed(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("updatePostFailed");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.setErrorCode("-9001");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void updateNotANResponse(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("updateANResponse");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.setErrorCode("-9002");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void updateNetworkError(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("updateNetworkError");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.setErrorCode("-9003");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }
    }

    public static class Crash {
        public static final String MODULE = "crash";

        public static void crash(String str, String str2) {
            LogContent logContent = new LogContent();
            logContent.setPoint("crash");
            logContent.setName("CrashReporterInitAction");
            if (!TextUtils.isEmpty(str) && str.length() < 800 && !TextUtils.isEmpty(str2) && str2.length() < 500) {
                logContent.addInfoItem("thread", str2);
                logContent.setMsg(str);
            }
            UNWLoggerManager.getInstance().getLoggerByModule("crash").info(logContent);
        }
    }

    public static class Login {
        private static final String MODULE = "login";

        public static void init(String str, Map<String, String> map, String str2, boolean z, boolean z2) {
            loginLog("init", false, str, map, str2, z, z2);
        }

        public static void loginOther(String str, Map<String, String> map, String str2, boolean z, boolean z2) {
            loginLog(MODULE, false, str, map, str2, z, z2);
        }

        public static void loginFailed(String str, Map<String, String> map, String str2, boolean z, boolean z2) {
            loginLog("loginFailed", true, str, map, str2, z, z2);
        }

        private static void loginLog(String str, boolean z, String str2, Map<String, String> map, String str3, boolean z2, boolean z3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str2);
            errorContent.setPoint(str);
            errorContent.setMsg("check session valid: " + z2);
            errorContent.addInfoItem("process-name", str3);
            errorContent.addInfoItem("session-null", String.valueOf(z3));
            errorContent.addInfoItem("session-valid", String.valueOf(z2));
            errorContent.addInfoItem("isLogining", String.valueOf(LoginStatus.isLogining()));
            if (map != null && !map.isEmpty()) {
                for (Map.Entry next : map.entrySet()) {
                    errorContent.addInfoItem((String) next.getKey(), (String) next.getValue());
                }
            }
            UNWLogger loggerByModule = UNWLoggerManager.getInstance().getLoggerByModule(MODULE);
            if (z) {
                errorContent.setErrorCode("-3001");
                loggerByModule.error(errorContent);
                return;
            }
            loggerByModule.info(errorContent);
        }
    }

    public static class Agoo {
        private static final String MODULE = "Agoo";

        public static void registerFailed(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("registerFailed");
            errorContent.setName(str);
            errorContent.setMsg("registerFailed");
            errorContent.addInfoItem("s", str2);
            errorContent.addInfoItem("s1", str3);
            errorContent.setErrorCode("-4001");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void registerCrash(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("registerCrash");
            errorContent.setName(str);
            errorContent.setMsg("crash");
            if (str2 != null) {
                errorContent.addInfoItem("Exception", str2);
            }
            errorContent.setErrorCode("-4002");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void intentServiceError(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("intentServiceError");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.setErrorCode("-4003");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void pushCrash(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("pushCrash");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.addInfoItem("e", str2);
            errorContent.setErrorCode("-4004");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void pushNotShow(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("pushNotShow");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.addInfoItem("message", str2);
            errorContent.setErrorCode("-4005");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void thirdPushCrash(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("thirdPushCrash");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.addInfoItem("e", str2);
            errorContent.setErrorCode("-4006");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void thirdPushNotShow(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("thirdPushNotShow");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.addInfoItem("message", str2);
            errorContent.setErrorCode("-4007");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }
    }

    public static class WebView {
        private static final String MODULE = "webview";

        public static void onInterceptException(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("interceptException");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.setErrorCode("-6001");
            errorContent.addInfoItem("url", str3);
            UNWLoggerManager.getInstance().getLoggerByModule("webview").error(errorContent);
        }

        public static void onEmptyUrl(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("emptyUrl");
            errorContent.setName(str);
            errorContent.setMsg(str2);
            errorContent.setErrorCode("-6002");
            UNWLoggerManager.getInstance().getLoggerByModule("webview").error(errorContent);
        }

        public static void onReceivedError(String str, ErrorContent errorContent, int i, String str2, String str3) {
            errorContent.setName(str);
            errorContent.setPoint("onReceivedError");
            errorContent.setErrorCode(String.valueOf(i));
            errorContent.setMsg(str2);
            errorContent.addInfoItem("url", str3);
            UNWLoggerManager.getInstance().getLoggerByModule("webview").error(errorContent);
        }

        public static void onReceivedSslError(String str, ErrorContent errorContent, com.uc.webview.export.WebView webView, SslError sslError) {
            errorContent.setName(str);
            errorContent.setPoint("onReceivedSslError");
            errorContent.setErrorCode(String.valueOf(sslError.getPrimaryError()));
            errorContent.setMsg(sslError.toString());
            if (webView != null) {
                errorContent.addInfoItem("url", webView.getUrl());
            }
            UNWLoggerManager.getInstance().getLoggerByModule("webview").error(errorContent);
        }
    }

    public static class Accs {
        private static final String MODULE = "accs";

        public static void bindAppFailed(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("bindAppFailed");
            errorContent.setErrorCode(str2);
            errorContent.setMsg(str3);
            errorContent.setName(str);
            UNWLoggerManager.getInstance().getLoggerByModule("accs").error(errorContent);
        }

        public static void bindUserFailed(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setPoint("bindUserFailed");
            errorContent.setErrorCode(str2);
            errorContent.setMsg(str3);
            errorContent.setName(str);
            UNWLoggerManager.getInstance().getLoggerByModule("accs").error(errorContent);
        }
    }

    public static class MTOP {
        private static final String MODULE = "mtop";
        private static final String POINT = "mtopResponse";

        public static void afterMtopSuccess(String str, String str2, String str3, String str4, String str5, String str6) {
            UNWLogger loggerByModule = UNWLoggerManager.getInstance().getLoggerByModule("mtop");
            LogContent logContent = new LogContent();
            logContent.setType(UNWLogger.LOG_VALUE_TYPE_USER);
            logContent.setSubType("mtop");
            logContent.setPoint(POINT);
            logContent.setName(str);
            logContent.setMsg(str2 + "-" + str5);
            logContent.addInfoItem("api", str2);
            logContent.addInfoItem("params", str4);
            logContent.addInfoItem("traceid", str6);
            logContent.addInfoItem("version", str3);
            loggerByModule.info(logContent);
        }

        public static void afterMtopFailed(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
            UNWLogger loggerByModule = UNWLoggerManager.getInstance().getLoggerByModule("mtop");
            ErrorContent errorContent = new ErrorContent();
            errorContent.setType(UNWLogger.LOG_VALUE_TYPE_USER);
            errorContent.setSubType("mtop");
            errorContent.setPoint(POINT);
            errorContent.setName(str);
            errorContent.setMsg(str2 + "-" + str6);
            errorContent.addInfoItem("api", str2);
            errorContent.addInfoItem("params", str4);
            errorContent.addInfoItem("traceid", str5);
            errorContent.addInfoItem("version", str3);
            errorContent.addInfoItem("respCode", String.valueOf(str8));
            errorContent.setErrorCode(str7);
            loggerByModule.error(errorContent);
        }
    }

    public static class ConfigCenter {
        private static final String MODULE = "configCenter";

        public static void configJsonError(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setErrorCode("-1201");
            errorContent.setPoint("configJsonError");
            errorContent.setName(str);
            errorContent.addInfoItem("configKey", str2);
            errorContent.addInfoItem("errorJsonStr", str3);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void getConfigError(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setMsg(str2);
            errorContent.setName(str);
            errorContent.setPoint("getLocalConfig");
            errorContent.setErrorCode("-1202");
            errorContent.addInfoItem("configKey", str3);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }
    }

    public static class Weex {
        private static final String MODULE = "weex";

        public static void loadException(String str, String str2, String str3, String str4, String str5) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setMsg(str2);
            errorContent.setName(str);
            errorContent.setPoint("loadException");
            errorContent.setErrorCode("-7001");
            errorContent.addInfoItem("uri", str3);
            errorContent.addInfoItem("s", str4);
            errorContent.addInfoItem("s1", str5);
            UNWLoggerManager.getInstance().getLoggerByModule("weex").error(errorContent);
        }
    }
}
