package alimama.com.unwbase.interfaces;

import java.util.List;

public interface IResourceManager {

    public interface Recycle {
        void dismiss(IResourceManager iResourceManager, boolean z);

        void onShow(IResourceManager iResourceManager);
    }

    void dismiss();

    List<String> getBlackPageName();

    String getEndtime();

    long getFatigueTime();

    int getPriority();

    String getStarttime();

    String getType();

    String getUuid();

    List<String> getWhitePageName();

    boolean isShowing();

    boolean saveWhenConflict();

    void setRecycle(Recycle recycle);

    void show();

    void unactiveDismiss();
}
