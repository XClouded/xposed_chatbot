package com.alimama.moon.config;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.config.model.MidH5TabModel;
import com.alimama.moon.utils.AssetFileUtil;

public class OrangeConfigUtil {
    public static final String MID_H5_TAB_FILE_NAME = "default_mid_h5_tab_data";

    public static MidH5TabModel genDefaultMidTabModel(Context context) {
        try {
            return (MidH5TabModel) JSON.parseObject(AssetFileUtil.getAssertsFileData(context, MID_H5_TAB_FILE_NAME), MidH5TabModel.class);
        } catch (Exception unused) {
            return null;
        }
    }
}
