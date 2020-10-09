package alimama.com.unwbase.interfaces;

public interface IAppEnvironment extends IInitAction {
    String getChannelId();

    int getEnv();

    String getTTid();

    boolean isDaily();

    boolean isPre();

    boolean isProd();

    void serializeEnv(String str);

    void setEnv(String str);
}
