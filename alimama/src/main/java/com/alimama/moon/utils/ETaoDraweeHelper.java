package com.alimama.moon.utils;

import android.content.Context;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.base.etaodrawee.IETaoDraweeHelperAction;
import com.alimama.unionwl.base.etaodrawee.ImageConfigData;
import com.facebook.drawee.backends.pipeline.Fresco;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ETaoDraweeHelper {
    private static final int FRESCO_MEMORY = 12;
    private static final int MAXGIF = 1;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ETaoDraweeHelper.class);

    public static void initFresco(Context context) {
        try {
            EtaoDraweeView.init(context, new IETaoDraweeHelperAction() {
                public void fetchNetImgSizeLargeMonitorAction(int i) {
                }

                public void triggerFrescoMaxMemMonitorAction() {
                }

                public ImageConfigData getDraweeViewConfigData() {
                    return new ImageConfigData(12, 1);
                }
            });
        } catch (Exception unused) {
            Fresco.initialize(context);
            logger.info("Fresco initialize error");
            UTHelper.sendCustomUT("ETaoDraweeHelper", "Fresco initialize error");
        }
    }
}
