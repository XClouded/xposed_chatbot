package alimama.com.unwviewbase.pullandrefrsh;

import android.view.View;
import android.view.ViewGroup;

public interface PtrLoadingDelegate {
    public static final int INVALID_CONTENT_VALUE = 0;

    int getContentSize(int i);

    View getLoadingView(ViewGroup viewGroup);

    void onCompleteUpdate(CharSequence charSequence);

    void onFreeze(boolean z, CharSequence charSequence);

    void onPull(float f);

    void onRefreshing();

    void onRelease(float f);

    void onReset();

    void onUpdateDirection(int i);
}
