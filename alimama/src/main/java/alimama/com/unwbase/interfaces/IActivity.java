package alimama.com.unwbase.interfaces;

import android.app.Activity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public interface IActivity extends IInitAction {
    void finishAll();

    ArrayList<WeakReference<Activity>> getBackStack();

    Activity getCurrentActivity();

    Class<?> getHomeClass();

    boolean isBackground();

    boolean isFirstEnterHome();

    boolean isLastPage();

    void onCreate(Activity activity);

    void onDestroy(Activity activity);

    void onPause(Activity activity);

    void onResume(Activity activity);

    void onStop(Activity activity);

    void popUpLastActivity(Activity activity);
}
