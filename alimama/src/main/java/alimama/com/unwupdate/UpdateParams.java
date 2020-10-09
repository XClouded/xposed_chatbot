package alimama.com.unwupdate;

import android.app.Activity;
import java.lang.ref.WeakReference;

public class UpdateParams {
    public WeakReference<Activity> activityRef;
    public String appName;
    public CancelUpdateListener cancelUpdateListener;
    public String downloadChannelDesc;
    public String downloadChannelId;
    public String downloadChannelName;
    public String fileProviderAuthority;
    public String groupValue;
    public boolean isForceUpdate;
    public UpdateIntercept updateIntercept;
}
