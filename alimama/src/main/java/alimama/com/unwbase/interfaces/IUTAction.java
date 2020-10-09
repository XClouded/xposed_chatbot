package alimama.com.unwbase.interfaces;

import android.app.Activity;
import android.net.Uri;
import com.ut.mini.UTPageStatus;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface IUTAction extends IInitAction {
    void addSpmUrl(String str, Map<String, String> map);

    void ctrlClicked(String str, String str2);

    void ctrlClicked(String str, String str2, Map<String, String> map);

    void customEvent(String str, String str2, Map<String, String> map);

    void expoTrack(@NotNull String str, String str2, String str3, String str4, Map<String, String> map);

    boolean isInit();

    void pageAppear(Activity activity, String str);

    void pageAppearDonotSkip(Activity activity, String str);

    void pageDisappear(Activity activity, String str);

    void skipPage(Activity activity);

    void updateAccount(String str, String str2, String str3);

    void updatePageStatus(Object obj, UTPageStatus uTPageStatus);

    void updatePageUrl(Object obj, Uri uri);
}
