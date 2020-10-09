package com.taobao.application.common.data;

import android.content.SharedPreferences;
import com.taobao.monitor.impl.common.Global;

public class AppLaunchHelper extends AbstractHelper {
    public void setLastProcessTime(long j) {
        this.preferences.putLong("lastStartProcessTime", j);
    }

    public void setStartProcessSystemTime(long j) {
        this.preferences.putLong("startProcessSystemTime", j);
        LaunchTimeUtils.setLastLaunchTime(j);
    }

    public void setStartProcessSystemClockTime(long j) {
        this.preferences.putLong("startProcessSystemClockTime", j);
    }

    public void setStartAppOnCreateSystemTime(long j) {
        this.preferences.putLong("startAppOnCreateSystemTime", j);
    }

    public void setStartAppOnCreateSystemClockTime(long j) {
        this.preferences.putLong("startAppOnCreateSystemClockTime", j);
    }

    public void setIsFullNewInstall(boolean z) {
        this.preferences.putBoolean("isFullNewInstall", z);
    }

    public void setIsFirstLaunch(boolean z) {
        this.preferences.putBoolean("isFirstLaunch", z);
    }

    public void setLaunchType(String str) {
        this.preferences.putString("launchType", str);
    }

    public static class LaunchTimeUtils {
        public static long getLastLaunchTime() {
            return Global.instance().context().getSharedPreferences("apm", 0).getLong("lastStartProcessTime", -1);
        }

        public static void setLastLaunchTime(long j) {
            SharedPreferences.Editor edit = Global.instance().context().getSharedPreferences("apm", 0).edit();
            edit.putLong("lastStartProcessTime", j);
            edit.apply();
        }
    }
}
