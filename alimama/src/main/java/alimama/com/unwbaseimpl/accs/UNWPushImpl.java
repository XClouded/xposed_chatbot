package alimama.com.unwbaseimpl.accs;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.IPush;
import alimama.com.unwbase.interfaces.ISecurity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.ICallback;
import com.taobao.agoo.IRegister;
import com.taobao.agoo.TaobaoRegister;
import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

public class UNWPushImpl implements IPush {
    private static final String TAG = "UNWPushImpl";
    private IAppReceiver appReceiver;
    private boolean isInitAccs;
    private boolean isInitializedAccs = false;
    private boolean isInitializedAgoo = false;
    private ACCSClient sACCSClient;

    public UNWPushImpl(boolean z, IAppReceiver iAppReceiver) {
        this.isInitAccs = z;
        this.appReceiver = iAppReceiver;
    }

    public void init() {
        ISecurity iSecurity;
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment != null && (iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class)) != null) {
            initAgoo(iAppEnvironment, iSecurity);
            if (this.isInitAccs) {
                initAccs(iAppEnvironment, iSecurity);
            }
        }
    }

    public void initAgoo(IAppEnvironment iAppEnvironment, ISecurity iSecurity) {
        Application application = UNWManager.getInstance().application;
        TaobaoRegister.setEnv(application, iAppEnvironment.getEnv() - 1);
        ALog.setUseTlog(false);
        anet.channel.util.ALog.setUseTlog(false);
        try {
            TaobaoRegister.register(application, "default", iSecurity.getAppkey(), (String) null, iAppEnvironment.getTTid(), new IRegister() {
                public void onFailure(String str, String str2) {
                }

                public void onSuccess(String str) {
                }
            });
            this.isInitializedAgoo = true;
        } catch (Exception e) {
            Log.w(TAG, "init call exception ", e);
        }
    }

    public void initAccs(IAppEnvironment iAppEnvironment, ISecurity iSecurity) {
        try {
            ACCSClient.init((Context) UNWManager.getInstance().application, new AccsClientConfig.Builder().setAppKey(iSecurity.getAppkey()).setTag("default").build());
            this.sACCSClient = ACCSClient.getAccsClient("default");
            if (!TextUtils.isEmpty(iAppEnvironment.getTTid())) {
                this.sACCSClient.bindApp(iAppEnvironment.getTTid(), this.appReceiver);
            }
            this.isInitializedAccs = true;
        } catch (AccsException e) {
            Log.w(TAG, "init accs exception", e);
        }
    }

    public void registerMi(String str, String str2) {
        if (!str2.isEmpty() && !str.isEmpty()) {
            MiPushRegistar.register(UNWManager.getInstance().application, str, str2);
        }
    }

    public void registerHuaWei() {
        HuaWeiRegister.register(UNWManager.getInstance().application);
    }

    public void registerViVo() {
        VivoRegister.register(UNWManager.getInstance().application);
    }

    public void registerOppo(String str, String str2) {
        if (!str.isEmpty() && !str2.isEmpty()) {
            OppoRegister.register(UNWManager.getInstance().application, str, str2);
        }
    }

    public void setAlias(String str) {
        if (this.isInitializedAgoo && !TextUtils.isEmpty(str)) {
            TaobaoRegister.setAlias(UNWManager.getInstance().application, str, (ICallback) null);
        }
    }

    public void removeAlias() {
        if (this.isInitializedAgoo) {
            TaobaoRegister.removeAlias(UNWManager.getInstance().application, (ICallback) null);
        }
    }

    public void accsBindUser(String str) {
        if (this.isInitializedAccs && !TextUtils.isEmpty(str) && this.sACCSClient != null) {
            this.sACCSClient.bindUser(str);
        }
    }

    public void accsUnBindUser() {
        if (this.isInitializedAccs && this.sACCSClient != null) {
            this.sACCSClient.unbindUser();
        }
    }

    public void accsBindApp(String str, IAppReceiver iAppReceiver) {
        if (this.isInitializedAccs && this.sACCSClient != null) {
            this.sACCSClient.bindApp(str, iAppReceiver);
        }
    }

    public ACCSClient getACCSClient() {
        if (!this.isInitializedAccs || this.sACCSClient == null) {
            return null;
        }
        return this.sACCSClient;
    }
}
