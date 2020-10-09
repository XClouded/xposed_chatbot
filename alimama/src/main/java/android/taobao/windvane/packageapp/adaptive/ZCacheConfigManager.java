package android.taobao.windvane.packageapp.adaptive;

import android.content.Context;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.file.NotEnoughSpace;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.taobao.android.speed.TBSpeed;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheManager;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZCacheConfigManager {
    private static ZCacheConfigManager instance;
    /* access modifiers changed from: private */
    public String configPath = null;
    private String oldConfig = "false";
    private String slideEnable = "false";
    private AtomicBoolean updateFromLocal = new AtomicBoolean(false);
    private String zType = "3";

    public void triggerZCacheConfig() {
    }

    public static ZCacheConfigManager getInstance() {
        if (instance == null) {
            synchronized (ZCacheConfigManager.class) {
                if (instance == null) {
                    instance = new ZCacheConfigManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.zType = GlobalConfig.getInstance().isZcacheType3() ? "3" : "2";
        this.oldConfig = GlobalConfig.getInstance().useZcacheOldConfig() ? "true" : "false";
        if (CommonUtils.getProcessName(context).equals(context.getApplicationContext().getPackageName())) {
            File createFolder = FileManager.createFolder(context, "ZCache");
            this.configPath = createFolder.getPath() + File.separator + "orange";
            File file = new File(this.configPath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            triggerLocalConfig();
            try {
                initOrange();
            } catch (Throwable unused) {
            }
        }
    }

    private void triggerLocalConfig() {
        if (this.updateFromLocal.compareAndSet(false, true)) {
            String str = null;
            try {
                byte[] read = FileAccesser.read(this.configPath);
                if (read != null) {
                    str = new String(read, "utf-8");
                    if (!TextUtils.isEmpty(str)) {
                        TaoLog.i("ZCache", "get zcache local config=[" + str + Operators.ARRAY_END_STR);
                        String[] split = str.split(",");
                        if (split.length != 4) {
                            return;
                        }
                        if (TextUtils.isEmpty(split[0]) || !TextUtils.equals(GlobalConfig.getInstance().getAppVersion(), split[0])) {
                            TaoLog.i("ZCache", "skip local config for dispatching appVersion. require=[" + GlobalConfig.getInstance().getAppVersion() + "], real=[" + split[0] + Operators.ARRAY_END_STR);
                            return;
                        }
                        if (!TextUtils.isEmpty(split[1])) {
                            this.zType = split[1];
                        }
                        GlobalConfig.zType = this.zType;
                        if (!TextUtils.isEmpty(split[2])) {
                            if (TextUtils.equals("3", this.zType) && !TextUtils.equals(this.oldConfig, split[2])) {
                                TaoLog.i("ZCache", "ZCache 3.0 新旧平台切换，需要刷新本地配置");
                                ZCacheManager.instance().removeAllZCache();
                            }
                            this.oldConfig = split[2];
                        }
                        if (!TextUtils.isEmpty(split[3])) {
                            this.slideEnable = split[3];
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void initOrange() {
        OrangeConfig.getInstance().registerListener(new String[]{"ZCache"}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
            public void onConfigUpdate(String str, boolean z) {
                if (str.equals("ZCache")) {
                    String config = OrangeConfig.getInstance().getConfig("ZCache", "ZType", "2");
                    String config2 = OrangeConfig.getInstance().getConfig("ZCache", "slide", "false");
                    String config3 = OrangeConfig.getInstance().getConfig("ZCache", "oldConfig", "false");
                    TaoLog.i("ZCache", "received zcache type=[" + config + "], use old config=[" + config3 + "], enable slide=[" + config2 + Operators.ARRAY_END_STR);
                    try {
                        String access$000 = ZCacheConfigManager.this.configPath;
                        FileAccesser.write(access$000, ByteBuffer.wrap((GlobalConfig.getInstance().getAppVersion() + "," + config + "," + config3 + "," + config2).getBytes("utf-8")));
                    } catch (NotEnoughSpace e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean useOldConfig() {
        return TextUtils.equals("true", this.oldConfig);
    }

    public String getzType() {
        if ("-1".equals(this.zType)) {
            try {
                this.zType = TBSpeed.isSpeedEdition(GlobalConfig.context, "ZCache3") ? "3" : "2";
            } catch (Throwable unused) {
                this.zType = "2";
            }
        }
        return this.zType;
    }

    public boolean slideEnable() {
        return TextUtils.equals("true", this.slideEnable);
    }
}
