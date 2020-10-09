package alimama.com.unwbase.emptyimpl;

import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.tools.UNWLog;
import java.util.Map;

public class EtaoLoggerEmptyImpl implements IEtaoLogger {
    public void fail(String str, String str2, String str3, Map<String, String> map) {
    }

    public void init() {
    }

    public void success(String str, String str2, Map<String, String> map) {
    }

    public void info(String str, String str2, String str3) {
        UNWLog.error("全链路日志未实现");
    }

    public void info(String str, String str2, String str3, Map<String, String> map) {
        UNWLog.error("2");
    }

    public void error(String str, String str2, String str3) {
        UNWLog.error("全链路日志未实现");
    }

    public void error(String str, String str2, String str3, String str4) {
        UNWLog.error("全链路日志未实现");
    }

    public void error(String str, String str2, String str3, String str4, String str5) {
        UNWLog.error("全链路日志未实现");
    }

    public void error(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        UNWLog.error("全链路日志未实现");
    }

    public void success(String str, String str2) {
        UNWLog.error("全链路日志未实现");
    }

    public void fail(String str, String str2, String str3) {
        UNWLog.error("全链路日志未实现");
    }
}
