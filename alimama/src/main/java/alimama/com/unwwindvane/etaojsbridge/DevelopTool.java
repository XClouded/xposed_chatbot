package alimama.com.unwwindvane.etaojsbridge;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.WVDevelopTool;
import com.taobao.orange.ConfigCenter;

public class DevelopTool extends WVDevelopTool {
    public static void register() {
        WVPluginManager.registerPlugin("WVDevelopTool", (Class<? extends WVApiPlugin>) DevelopTool.class, true);
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"configCenterData".equals(str)) {
            return true;
        }
        configCenterData(str2, wVCallBackContext);
        return true;
    }

    public final void configCenterData(String str, WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        wVResult.setData(ConfigCenter.getInstance().getIndexAndConfigs());
        wVCallBackContext.success(wVResult);
    }
}
