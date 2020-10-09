package alimama.com.unwbase.interfaces;

import com.taobao.accs.ACCSClient;
import com.taobao.accs.IAppReceiver;

public interface IPush extends IInitAction {
    void accsBindApp(String str, IAppReceiver iAppReceiver);

    void accsBindUser(String str);

    void accsUnBindUser();

    ACCSClient getACCSClient();

    void registerHuaWei();

    void registerMi(String str, String str2);

    void registerOppo(String str, String str2);

    void registerViVo();

    void removeAlias();

    void setAlias(String str);
}
