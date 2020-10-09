package alimama.com.unwbase.interfaces;

import android.app.Activity;
import android.content.BroadcastReceiver;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;

public interface ILogin extends IInitAction {
    boolean checkSessionValid();

    BroadcastReceiver getLoginReceiverIntercept();

    String getNick();

    ISsoRemoteParam getSsoRemoteParam();

    String getUserId();

    boolean isSupportSso();

    void launchTaobao(Activity activity);

    void loginWithUi();

    void logout();

    void registerReceiver();
}
