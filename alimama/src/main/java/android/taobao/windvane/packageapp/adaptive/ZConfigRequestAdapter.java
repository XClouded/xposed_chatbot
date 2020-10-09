package android.taobao.windvane.packageapp.adaptive;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.packageapp.WVCustomPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.util.TaoLog;

import com.taobao.zcache.config.IZConfigRequest;

import org.json.JSONObject;

public class ZConfigRequestAdapter implements IZConfigRequest, WVEventListener {
    public IZConfigRequest.ZConfigCallback configCallback;

    public void requestZConfig(IZConfigRequest.ZConfigCallback zConfigCallback) {
        this.configCallback = zConfigCallback;
        WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0);
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        int i2 = 0;
        if (!"3".equals(GlobalConfig.zType) || i != 6002) {
            return new WVEventResult(false);
        }
        String str = "{}";
        String str2 = "SUCCESS";
        if (WVPackageAppService.getWvPackageAppConfig() != null && (WVPackageAppService.getWvPackageAppConfig() instanceof WVPackageAppConfig)) {
            str = ((WVPackageAppConfig) WVPackageAppService.getWvPackageAppConfig()).packageCfg;
        }
        TaoLog.i("ZCache", "package:" + str);
        String str3 = WVCommonConfig.getInstance().commonCfg;
        String str4 = WVCustomPackageAppConfig.getInstance().customConfig;
        String str5 = ZipPrefixesManager.getInstance().prefix;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(WVConfigManager.CONFIGNAME_PREFIXES, new JSONObject(str5));
            jSONObject.put("common", new JSONObject(str3));
            jSONObject.put(WVConfigManager.CONFIGNAME_CUSTOM, new JSONObject(str4));
            jSONObject.put(WVConfigManager.CONFIGNAME_PACKAGE, new JSONObject(str));
        } catch (Throwable th) {
            th.printStackTrace();
            i2 = 1007;
            str2 = "deserialization failed:{" + th.getMessage() + "}";
        }
        if (this.configCallback != null) {
            this.configCallback.configBack(jSONObject.toString(), i2, str2);
        }
        return new WVEventResult(true);
    }
}
