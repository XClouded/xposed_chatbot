package com.alimama.moon.utils;

import android.content.Context;
import com.taobao.phenix.intf.Phenix;
import com.taobao.tcommon.log.FLog;

public class PhenixHelper {
    public static void initPhenixForCart(Context context) {
        Phenix.instance().with(context);
        FLog.setMinLevel(5);
        Phenix.instance().build();
    }
}
