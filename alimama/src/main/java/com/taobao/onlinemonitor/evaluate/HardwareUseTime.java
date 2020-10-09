package com.taobao.onlinemonitor.evaluate;

import android.taobao.windvane.cache.WVFileInfoParser;
import com.taobao.onlinemonitor.HardWareInfo;
import java.io.File;

public class HardwareUseTime implements CalScore {
    public int getScore(HardWareInfo hardWareInfo) {
        float f = 7.0f;
        try {
            File file = new File("/sdcard/Android/");
            if (file.exists()) {
                f = (float) Math.round(10.0f - (((float) (Math.abs(System.currentTimeMillis() - file.lastModified()) / WVFileInfoParser.DEFAULT_MAX_AGE)) * 0.2f));
            }
        } catch (Throwable unused) {
        }
        return (int) f;
    }
}
