package com.ali.alihadeviceevaluator;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import com.ali.alihadeviceevaluator.old.ActivityLifecycle;
import com.ali.alihadeviceevaluator.old.HardWareInfo;
import com.ali.alihadeviceevaluator.util.Global;

public class AliHardwareInitializer {
    private HardwareListener hardwareListener;

    public interface HardwareListener {
        void onDeviceLevelChanged(int i, float f);
    }

    public AliHardwareInitializer setAppContext(Application application) {
        Global.context = application;
        return this;
    }

    public AliHardwareInitializer setHandler(Handler handler) {
        Global.handler = handler;
        return this;
    }

    public AliHardwareInitializer setLevelChangedListener(HardwareListener hardwareListener2) {
        this.hardwareListener = hardwareListener2;
        return this;
    }

    public void start() {
        if (Global.context == null) {
            Log.e(Global.TAG, "you must setContext before start!");
            return;
        }
        AliAIHardware aliAIHardware = new AliAIHardware();
        aliAIHardware.setHardwareListener(this.hardwareListener);
        aliAIHardware.start();
        AliHardware.setDelegate(new HardwareDelegate(aliAIHardware));
        AliLifecycle.registerLifeCycle(Global.context, aliAIHardware);
        DeviceInfoReporter.setAIHardware(aliAIHardware);
    }

    public static HardWareInfo lazyLoad() {
        if (Global.context == null) {
            return null;
        }
        HardWareInfo hardWareInfo = new HardWareInfo(Global.context);
        Global.context.registerActivityLifecycleCallbacks(new ActivityLifecycle(hardWareInfo));
        AliHAHardware.getInstance().getOutlineInfo();
        int score = hardWareInfo.getScore();
        if (score > 0) {
            AliHAHardware.getInstance().setDeviceScore(score);
        }
        DeviceInfoReporter.reportDeviceInfo(hardWareInfo);
        return hardWareInfo;
    }
}
