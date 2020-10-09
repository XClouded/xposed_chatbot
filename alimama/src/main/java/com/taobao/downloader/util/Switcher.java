package com.taobao.downloader.util;

import android.text.TextUtils;
import com.taobao.downloader.Configuration;
import com.taobao.downloader.download.protocol.DLConfig;
import com.taobao.downloader.download.protocol.DLConnection;
import com.taobao.downloader.download.protocol.huc.HUCConnection;
import com.taobao.downloader.request.Item;

public class Switcher {
    public static boolean useUniformTmpDir() {
        if (Configuration.cloundConfigAdapter == null) {
            return false;
        }
        return !"".equals(Configuration.cloundConfigAdapter.getConfig("uniform_tmpdir"));
    }

    public static DLConnection getConnection(Item item, DLConfig dLConfig) {
        if (Configuration.dlConnectionClazz == null) {
            return new HUCConnection();
        }
        if (canUseAnetConnection(item.size, dLConfig)) {
            try {
                return (DLConnection) Configuration.dlConnectionClazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return new HUCConnection();
    }

    private static boolean canUseAnetConnection(long j, DLConfig dLConfig) {
        if (Configuration.cloundConfigAdapter == null) {
            return true;
        }
        boolean equals = "".equals(Configuration.cloundConfigAdapter.getConfig("dlconnection_anet"));
        String config = Configuration.cloundConfigAdapter.getConfig("sizeSwitch_anet");
        boolean z = !"".equals(Configuration.cloundConfigAdapter.getConfig("lastUseHuc_anet"));
        int intValue = (TextUtils.isEmpty(config) || !TextUtils.isDigitsOnly(config)) ? 0 : Integer.valueOf(config).intValue();
        if (equals && (0 == j || j > ((long) intValue))) {
            if (!z) {
                return true;
            }
            if (dLConfig.isLastConnect() || dLConfig.isLastRead()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static int getDLReadBufferSize() {
        if (Configuration.cloundConfigAdapter == null) {
            return DLConfig.LARGE_BUFFER_SIZE;
        }
        String config = Configuration.cloundConfigAdapter.getConfig("dl_buffersize");
        if (TextUtils.isEmpty(config) || !TextUtils.isDigitsOnly(config)) {
            return DLConfig.LARGE_BUFFER_SIZE;
        }
        return Integer.valueOf(config).intValue();
    }
}
