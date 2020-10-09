package alimama.com.unwrouter.interfaces;

import alimama.com.unwrouter.PageInfo;
import android.net.Uri;
import android.os.Bundle;

public interface PageNeedLoginAction {
    void loginAction(PageInfo pageInfo, Uri uri, Bundle bundle, int i);

    void loginAction(String str);

    void loginAction(String str, Bundle bundle);
}
