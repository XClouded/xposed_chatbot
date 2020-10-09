package alimama.com.unwbase.interfaces;

public interface IUpdateManager<T> extends IInitAction {
    void checkUpdate(boolean z, T t);

    int getIconResId();

    T getUpdateParams();

    void setHasChecked(boolean z);
}
