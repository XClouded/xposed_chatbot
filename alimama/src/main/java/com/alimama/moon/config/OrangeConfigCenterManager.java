package com.alimama.moon.config;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.config.model.MidH5TabModel;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import java.util.Map;

public class OrangeConfigCenterManager {
    private static final String NAME_SPACE = "moon_config";
    public static OrangeConfigCenterManager sInstance = null;
    private static boolean sIsPullOrangeConfigOnHomePage = false;
    private String mBulletinInfo;
    private Context mContext;
    private boolean mIsAppForceUpdate;
    private MidH5TabModel mMidH5TabModel;

    private OrangeConfigCenterManager() {
    }

    public static OrangeConfigCenterManager getInstance() {
        synchronized (OrangeConfigCenterManager.class) {
            if (sInstance == null) {
                sInstance = new OrangeConfigCenterManager();
            }
        }
        return sInstance;
    }

    public void initOrange(Context context) {
        this.mContext = context;
        asyncPullAppForceUpdateConfig();
        asyncPullMidH5TabConfig();
        syncPullAllConfig();
        asyncPullBulletinBoardConfig();
    }

    public void pullOrangeConfig() {
        if (!sIsPullOrangeConfigOnHomePage) {
            asyncPullAppForceUpdateConfig();
            asyncPullMidH5TabConfig();
            syncPullAllConfig();
            asyncPullBulletinBoardConfig();
            sIsPullOrangeConfigOnHomePage = true;
        }
    }

    public MidH5TabModel getMideH5TabModel() {
        return this.mMidH5TabModel;
    }

    public boolean isAppForceUpdate() {
        return this.mIsAppForceUpdate;
    }

    public String fetchBulletinBoardData() {
        return this.mBulletinInfo;
    }

    public boolean isSwitchMidH5Tab() {
        if (!isMidH5TabModelValid()) {
            this.mMidH5TabModel = OrangeConfigUtil.genDefaultMidTabModel(this.mContext);
        }
        return this.mMidH5TabModel != null && !TextUtils.equals(this.mMidH5TabModel.getIsSwitchMidH5Tab(), "false");
    }

    private void asyncPullMidH5TabConfig() {
        OrangeConfig.getInstance().registerListener(new String[]{NAME_SPACE}, new OConfigListener() {
            public void onConfigUpdate(String str, Map<String, String> map) {
                if (OrangeConfigCenterManager.NAME_SPACE.equals(str)) {
                    OrangeConfigCenterManager.this.parseMidH5TabConfigJson(OrangeConfig.getInstance().getConfig(OrangeConfigCenterManager.NAME_SPACE, "homeTabInfo", ""));
                }
            }
        }, false);
    }

    private void asyncPullAppForceUpdateConfig() {
        OrangeConfig.getInstance().registerListener(new String[]{NAME_SPACE}, new OConfigListener() {
            public void onConfigUpdate(String str, Map<String, String> map) {
                if (OrangeConfigCenterManager.NAME_SPACE.equals(str)) {
                    OrangeConfigCenterManager.this.parseAppForceUpdateJson(OrangeConfig.getInstance().getConfig(OrangeConfigCenterManager.NAME_SPACE, "appUpdate", ""));
                }
            }
        }, false);
    }

    private void asyncPullBulletinBoardConfig() {
        OrangeConfig.getInstance().registerListener(new String[]{NAME_SPACE}, new OConfigListener() {
            public void onConfigUpdate(String str, Map<String, String> map) {
                if (OrangeConfigCenterManager.NAME_SPACE.equals(str)) {
                    OrangeConfigCenterManager.this.parseBulletinBoardJson(OrangeConfig.getInstance().getConfig(OrangeConfigCenterManager.NAME_SPACE, "bulletinInfo", ""));
                }
            }
        }, false);
    }

    private void syncPullAllConfig() {
        new Thread(new Runnable() {
            public void run() {
                OrangeConfigCenterManager.this.parseMidH5TabConfigJson(OrangeConfig.getInstance().getConfig(OrangeConfigCenterManager.NAME_SPACE, "homeTabInfo", ""));
                OrangeConfigCenterManager.this.parseAppForceUpdateJson(OrangeConfig.getInstance().getConfig(OrangeConfigCenterManager.NAME_SPACE, "appUpdate", ""));
                OrangeConfigCenterManager.this.parseBulletinBoardJson(OrangeConfig.getInstance().getConfig(OrangeConfigCenterManager.NAME_SPACE, "bulletinInfo", ""));
            }
        }).start();
    }

    private boolean isMidH5TabModelValid() {
        return this.mMidH5TabModel != null && !TextUtils.isEmpty(this.mMidH5TabModel.getIsSwitchMidH5Tab()) && !TextUtils.isEmpty(this.mMidH5TabModel.getSchema()) && !TextUtils.isEmpty(this.mMidH5TabModel.getTitle());
    }

    /* access modifiers changed from: private */
    public void parseAppForceUpdateJson(String str) {
        try {
            if (TextUtils.equals(JSON.parseObject(str).getString("isAppForceUpdate"), "true")) {
                this.mIsAppForceUpdate = true;
            } else {
                this.mIsAppForceUpdate = false;
            }
        } catch (Exception unused) {
            this.mIsAppForceUpdate = false;
        }
    }

    /* access modifiers changed from: private */
    public void parseBulletinBoardJson(String str) {
        try {
            this.mBulletinInfo = str;
        } catch (Exception unused) {
            this.mBulletinInfo = "";
        }
    }

    /* access modifiers changed from: private */
    public void parseMidH5TabConfigJson(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                this.mMidH5TabModel = (MidH5TabModel) JSON.parseObject(str, MidH5TabModel.class);
            } catch (Exception unused) {
                this.mMidH5TabModel = null;
            }
        }
        if (!isMidH5TabModelValid()) {
            this.mMidH5TabModel = OrangeConfigUtil.genDefaultMidTabModel(this.mContext);
        }
    }
}
