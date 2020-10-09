package alimama.com.unwwindvane;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwwindvane.etaojsbridge.DevelopTool;
import alimama.com.unwwindvane.etaojsbridge.H5LogBridge;
import alimama.com.unwwindvane.etaojsbridge.TBWeakNetStatus;
import android.taobao.windvane.jsbridge.api.WVAPI;
import android.taobao.windvane.util.TaoLog;
import com.taobao.mtop.wvplugin.MtopWVPlugin;

public class JsBridgeManager {
    public static void register() {
        TaoLog.setLogSwitcher(UNWManager.getInstance().getDebuggable());
        DevelopTool.register();
        MtopWVPlugin.register();
        H5LogBridge.register();
        TBWeakNetStatus.register();
        WVAPI.setup();
    }
}
