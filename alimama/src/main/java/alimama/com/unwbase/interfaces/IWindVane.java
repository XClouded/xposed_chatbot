package alimama.com.unwbase.interfaces;

import android.taobao.windvane.jsbridge.WVApiPlugin;

public interface IWindVane extends IInitAction {
    void registerPlugin(String str, Class<? extends WVApiPlugin> cls);
}
