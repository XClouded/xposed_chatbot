package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.ISecurity;
import android.text.TextUtils;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;
import com.taobao.wireless.security.sdk.staticdatastore.IStaticDataStoreComponent;

public class UNWSecurityImpl implements ISecurity {
    private String sAppKey = "";

    private int getAppKeyIndex() {
        return 0;
    }

    public String getWUA() {
        ISecurityBodyComponent securityBodyComp = SecurityGuardManager.getInstance(UNWManager.getInstance().application).getSecurityBodyComp();
        return securityBodyComp.getSecurityBodyData((System.currentTimeMillis() / 1000) + "");
    }

    public String getAppkey() {
        IStaticDataStoreComponent staticDataStoreComp;
        if (!TextUtils.isEmpty(this.sAppKey)) {
            return this.sAppKey;
        }
        SecurityGuardManager instance = SecurityGuardManager.getInstance(UNWManager.getInstance().application);
        if (!(instance == null || (staticDataStoreComp = instance.getStaticDataStoreComp()) == null)) {
            this.sAppKey = staticDataStoreComp.getAppKeyByIndex(getAppKeyIndex());
        }
        return this.sAppKey;
    }

    public String getAppId() {
        return this.sAppKey + "@android";
    }

    public void init() {
        SecurityGuardManager.getInitializer().initialize(UNWManager.getInstance().application);
    }
}
