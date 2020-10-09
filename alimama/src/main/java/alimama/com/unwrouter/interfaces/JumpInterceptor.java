package alimama.com.unwrouter.interfaces;

import alimama.com.unwrouter.PageInfo;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public interface JumpInterceptor {
    void onCreateIntercept(ArrayList<WeakReference<Activity>> arrayList, Activity activity);

    boolean onIntercept(ArrayList<WeakReference<Activity>> arrayList, PageInfo pageInfo, Uri uri, Bundle bundle, int i);

    void onPauseIntercept(Activity activity);

    void onResumeIntercept(Activity activity);
}
