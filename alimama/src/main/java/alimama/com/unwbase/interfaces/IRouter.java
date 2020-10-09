package alimama.com.unwbase.interfaces;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public interface IRouter<T> extends IInitAction {
    void finishAll();

    ArrayList<WeakReference<Activity>> getBackStack();

    Activity getCurrentActivity();

    T getCurrentPageInfo();

    Class<?> getHomeClass();

    ArrayList<String> getOnResumeActiList();

    void gotoPage(T t);

    void gotoPage(T t, int i);

    void gotoPage(T t, Uri uri);

    void gotoPage(T t, Bundle bundle);

    void gotoPage(T t, Bundle bundle, int i);

    void gotoPage(String str, boolean z);

    boolean gotoPage(@Nullable String str);

    boolean gotoPage(String str, Bundle bundle);

    boolean gotoPage(@Nullable String str, Bundle bundle, int i);

    void gotoPageWithUrl(T t, Uri uri, Bundle bundle, int i, boolean z);

    boolean isBackground();

    boolean isFirstEnterHome();

    boolean isLastPage();

    void onCreate(Activity activity);

    void onDestroy(Activity activity);

    void onPause(Activity activity);

    void onResume(Activity activity);

    void onStop(Activity activity);

    void popUpLastActivity(Activity activity);

    void setCurrent(Activity activity);

    void setHomeActivity(Class<?> cls);

    void turnToPage(T t, Uri uri, Bundle bundle, int i);
}
