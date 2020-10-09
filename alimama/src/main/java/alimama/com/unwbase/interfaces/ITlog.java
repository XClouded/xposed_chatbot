package alimama.com.unwbase.interfaces;

public interface ITlog extends IInitAction {
    void loge(String str, String str2, String str3);

    void loge(String str, String str2, Throwable th);

    void logv(String str, String str2, String str3);

    void logw(String str, String str2, String str3);

    void logw(String str, String str2, Throwable th);

    void upload();
}
