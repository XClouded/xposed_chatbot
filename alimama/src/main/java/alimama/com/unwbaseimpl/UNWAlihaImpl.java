package alimama.com.unwbaseimpl;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAliHa;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwetaologger.base.UNWLogger;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import com.ali.alihadeviceevaluator.AliHardware;
import com.ali.alihadeviceevaluator.AliHardwareInitializer;
import com.alibaba.motu.tbrest.SendService;
import com.taobao.login4android.Login;
import com.taobao.monitor.adapter.OtherAppApmInitiator;
import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.processor.launcher.PageList;
import com.taobao.orange.OConstant;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class UNWAlihaImpl implements IAliHa {
    private List<String> blacks;
    private AliHardwareInitializer.HardwareListener listener;
    private String processName;
    private List<String> whites;

    public UNWAlihaImpl(List<String> list, List<String> list2, AliHardwareInitializer.HardwareListener hardwareListener, @NotNull String str) {
        this.blacks = list;
        this.whites = list2;
        this.listener = hardwareListener;
        this.processName = str;
    }

    public int getLevel() {
        return AliHardware.getDeviceLevel();
    }

    public void init() {
        initAPM(UNWManager.getInstance().application);
        initAliHa(UNWManager.getInstance().application);
    }

    private void initAPM(Application application) {
        IAppEnvironment iAppEnvironment;
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        if (iSecurity != null && (iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class)) != null) {
            SendService.getInstance().init(application, iSecurity.getAppId(), iSecurity.getAppkey(), UNWManager.getInstance().mVersionName, iAppEnvironment.getChannelId(), Login.getNick());
            HashMap hashMap = new HashMap();
            hashMap.put("deviceId", UNWManager.getInstance().getDeviceId());
            hashMap.put(OConstant.LAUNCH_ONLINEAPPKEY, iSecurity.getAppkey());
            hashMap.put("appVersion", UNWManager.getInstance().getAppVersion());
            hashMap.put(UNWLogger.LOG_VALUE_TYPE_PROCESS, this.processName);
            hashMap.put("ttid", iAppEnvironment.getTTid());
            hashMap.put("channel", iAppEnvironment.getChannelId());
            new OtherAppApmInitiator().init(application, hashMap);
            if (this.blacks != null) {
                for (String addBlackPage : this.blacks) {
                    PageList.addBlackPage(addBlackPage);
                }
            }
            if (this.whites != null) {
                for (String addWhitePage : this.whites) {
                    PageList.addWhitePage(addWhitePage);
                }
            }
            DynamicConstants.needFragment = true;
        }
    }

    public void initAliHa(Application application) {
        new AliHardwareInitializer().setAppContext(application).setHandler(new InitHandle()).setLevelChangedListener(this.listener).start();
    }

    private class InitHandle extends Handler {
        private InitHandle() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }
}
