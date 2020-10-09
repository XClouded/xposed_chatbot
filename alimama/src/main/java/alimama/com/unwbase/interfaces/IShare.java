package alimama.com.unwbase.interfaces;

import android.content.Context;

public interface IShare<T> extends IInitAction {
    void registerShareView(String str, Object obj);

    void share(Context context, String str, T t);

    void share(String str, T t);
}
