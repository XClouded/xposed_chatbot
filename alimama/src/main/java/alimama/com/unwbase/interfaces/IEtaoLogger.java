package alimama.com.unwbase.interfaces;

import java.util.Map;

public interface IEtaoLogger extends IInitAction {
    void error(String str, String str2, String str3);

    void error(String str, String str2, String str3, String str4);

    void error(String str, String str2, String str3, String str4, String str5);

    void error(String str, String str2, String str3, String str4, String str5, Map<String, String> map);

    void fail(String str, String str2, String str3);

    void fail(String str, String str2, String str3, Map<String, String> map);

    void info(String str, String str2, String str3);

    void info(String str, String str2, String str3, Map<String, String> map);

    void success(String str, String str2);

    void success(String str, String str2, Map<String, String> map);
}
