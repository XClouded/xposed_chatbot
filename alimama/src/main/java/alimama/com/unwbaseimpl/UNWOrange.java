package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.IOrange;
import alimama.com.unwbase.interfaces.ISecurity;
import com.taobao.orange.OCandidate;
import com.taobao.orange.OConfig;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OConstant;
import com.taobao.orange.OrangeConfig;
import java.util.Map;

public class UNWOrange implements IOrange {
    public void init() {
        int orangeEnv = getOrangeEnv();
        OConfig.Builder builder = new OConfig.Builder();
        builder.setAppKey(((ISecurity) UNWManager.getInstance().getService(ISecurity.class)).getAppkey()).setAppVersion(UNWManager.getInstance().getAppVersion()).setEnv(orangeEnv).setIndexUpdateMode(2);
        OrangeConfig.getInstance().init(UNWManager.getInstance().application, builder.build());
    }

    private int getOrangeEnv() {
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment == null) {
            return OConstant.ENV.ONLINE.getEnvMode();
        }
        if (iAppEnvironment.isProd()) {
            return OConstant.ENV.ONLINE.getEnvMode();
        }
        if (iAppEnvironment.isPre()) {
            return OConstant.ENV.PREPARE.getEnvMode();
        }
        if (iAppEnvironment.isDaily()) {
            return OConstant.ENV.TEST.getEnvMode();
        }
        return OConstant.ENV.ONLINE.getEnvMode();
    }

    public void registerListener(String[] strArr, OConfigListener oConfigListener, boolean z) {
        OrangeConfig.getInstance().registerListener(strArr, oConfigListener, z);
    }

    public void unregisterListener(String[] strArr, OConfigListener oConfigListener) {
        OrangeConfig.getInstance().unregisterListener(strArr, oConfigListener);
    }

    public void unregisterListener(String[] strArr) {
        OrangeConfig.getInstance().unregisterListener(strArr);
    }

    public String getConfig(String str, String str2, String str3) {
        return OrangeConfig.getInstance().getConfig(str, str2, str3);
    }

    public Map<String, String> getConfigs(String str) {
        return OrangeConfig.getInstance().getConfigs(str);
    }

    public String getCustomConfig(String str, String str2) {
        return OrangeConfig.getInstance().getCustomConfig(str, str2);
    }

    public void addCandidate(OCandidate oCandidate) {
        OrangeConfig.getInstance().addCandidate(oCandidate);
    }
}
