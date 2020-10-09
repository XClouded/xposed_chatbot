package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.IMtop;
import anet.channel.util.ALog;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopSetting;

public class UNWMtop implements IMtop {
    private Mtop mMTop;

    public void init() {
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment != null) {
            if (UNWManager.getInstance().getDebuggable()) {
                TBSdkLog.setTLogEnabled(false);
                TBSdkLog.setPrintLog(true);
                TBSdkLog.setLogEnable(TBSdkLog.LogEnable.DebugEnable);
                ALog.setUseTlog(false);
            }
            MtopSetting.setAppKeyIndex(Mtop.Id.INNER, 0, 2);
            MtopSetting.setAppVersion(Mtop.Id.INNER, UNWManager.getInstance().getAppVersion());
            String tTid = iAppEnvironment.getTTid();
            this.mMTop = Mtop.instance(Mtop.Id.INNER, UNWManager.getInstance().application.getBaseContext(), tTid);
            this.mMTop.registerTtid(tTid);
            this.mMTop.switchEnvMode(getMtopEnv());
        }
    }

    private EnvModeEnum getMtopEnv() {
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment == null) {
            return EnvModeEnum.TEST;
        }
        EnvModeEnum envModeEnum = EnvModeEnum.TEST;
        if (iAppEnvironment.isPre()) {
            return EnvModeEnum.PREPARE;
        }
        return iAppEnvironment.isProd() ? EnvModeEnum.ONLINE : envModeEnum;
    }

    public Mtop getMtop() {
        return this.mMTop;
    }
}
