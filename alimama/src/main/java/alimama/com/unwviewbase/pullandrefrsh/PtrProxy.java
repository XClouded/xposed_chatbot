package alimama.com.unwviewbase.pullandrefrsh;

public interface PtrProxy {
    void onCompleteUpdate(CharSequence charSequence);

    void onDisable();

    void onEnable();

    void onFreeze(boolean z, CharSequence charSequence);

    void onPull(float f);

    void onRefreshing();

    void onRelease(float f);

    void onReset();

    void onUpdateDirection(int i);
}
