package com.alibaba.analytics.core.config;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import java.io.File;

public class AudidConfigListener implements SystemConfigMgr.IKVChangeListener {
    private static final String AUDID_NOT_UPLOAD = "3c9b584e65e6c983";
    public static final String KEY = "audid";

    public AudidConfigListener() {
        parseConfig(SystemConfigMgr.getInstance().get("audid"));
    }

    public void onChange(String str, String str2) {
        parseConfig(str2);
    }

    private void parseConfig(String str) {
        Logger.d("AudidConfigListener", "parseConfig value", str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if ("0".equalsIgnoreCase(str)) {
            changeFile(Variables.getInstance().getContext(), true);
        } else {
            changeFile(Variables.getInstance().getContext(), false);
        }
    }

    private void changeFile(final Context context, final boolean z) {
        TaskExecutor.getInstance().submit(new Runnable() {
            public void run() {
                try {
                    File fileStreamPath = context.getFileStreamPath(AudidConfigListener.AUDID_NOT_UPLOAD);
                    if (fileStreamPath.exists()) {
                        if (!z) {
                            fileStreamPath.delete();
                        }
                    } else if (z) {
                        fileStreamPath.createNewFile();
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }
}
