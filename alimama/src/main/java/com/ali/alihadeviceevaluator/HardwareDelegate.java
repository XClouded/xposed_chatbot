package com.ali.alihadeviceevaluator;

import android.util.Log;
import com.ali.alihadeviceevaluator.util.Global;

class HardwareDelegate {
    private AliAIHardware mAliAIHardware;

    HardwareDelegate(AliAIHardware aliAIHardware) {
        this.mAliAIHardware = aliAIHardware;
    }

    public int getDeviceLevel() {
        int deviceLevel = AliAIHardware.getDeviceLevel(this.mAliAIHardware.getDeviceScore());
        if (deviceLevel == -2 || deviceLevel == -3) {
            int deviceLevelForAI = AliHAHardware.getInstance().getOutlineInfo().getDeviceLevelForAI();
            Log.d(Global.TAG, "get device level using outline, level = " + deviceLevelForAI);
            return deviceLevelForAI;
        }
        Log.d(Global.TAG, "get device level using ai, level = " + deviceLevel);
        return deviceLevel;
    }

    public int getDeviceScore() {
        float deviceScore = this.mAliAIHardware.getDeviceScore();
        if (deviceScore < 0.0f) {
            deviceScore = 80.0f;
        }
        return (int) deviceScore;
    }
}
