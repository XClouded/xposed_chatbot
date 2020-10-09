package com.ali.protodb;

import android.app.Application;
import android.util.Log;
import com.ali.protodb.lsdb.LSDB;
import com.ali.protodb.migration.sharedpreferences.SharedPreferencesDelegateManager;
import com.taobao.orange.OrangeConfig;
import java.util.HashMap;

public class DaemonTask {
    public static void init(Application application, HashMap<String, Object> hashMap) {
        try {
            SharedPreferencesDelegateManager.updateConfig(application);
            SharedPreferencesDelegateManager.migrateData(application);
            if ("true".equals(OrangeConfig.getInstance().getConfig(SharedPreferencesDelegateManager.ALIVFS_CONFIG_GROUP, SharedPreferencesDelegateManager.ALIVFS_CONFIG_SP_DELEGATE_COMPACT, "true"))) {
                LSDB.compactAll();
            }
        } catch (Throwable th) {
            Log.e("ProtoDB", "failed to migrate sp data to sp delegate");
            th.printStackTrace();
        }
    }
}
