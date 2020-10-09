package com.alimama.moon.usertrack;

import com.alimama.moon.config.OrangeConfigCenterManager;
import com.alimama.moon.ui.BottomNavActivity;
import org.android.agoo.common.AgooConstants;

public class BottomNavUTHelper {
    public static String parseControlNameByIndex(int i) {
        if (BottomNavActivity.sIsSwitchMidH5Tab) {
            switch (i) {
                case 0:
                    return "a2141.8285304.2001." + "discovery";
                case 1:
                    return "a2141.8285304.2001." + "tools";
                case 2:
                    return "a2141.8285304.2001." + OrangeConfigCenterManager.getInstance().getMideH5TabModel().getType();
                case 3:
                    return "a2141.8285304.2001." + AgooConstants.MESSAGE_REPORT;
                case 4:
                    return "a2141.8285304.2001." + "mine";
                default:
                    return "a2141.8285304.2001.";
            }
        } else {
            switch (i) {
                case 0:
                    return "a2141.8285304.2001." + "discovery";
                case 1:
                    return "a2141.8285304.2001." + "tools";
                case 2:
                    return "a2141.8285304.2001." + AgooConstants.MESSAGE_REPORT;
                case 3:
                    return "a2141.8285304.2001." + "mine";
                default:
                    return "a2141.8285304.2001.";
            }
        }
    }
}
