package com.alimama.moon.update;

import com.alibaba.android.common.ILogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateLogger implements ILogger {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) UpdateLogger.class);

    public void logd(String str, String str2) {
        logger.debug(str2);
    }

    public void logi(String str, String str2) {
        logger.info(str2);
    }

    public void logw(String str, String str2) {
        logger.warn(str2);
    }

    public void loge(String str, String str2) {
        logger.error(str2);
    }

    public void loge(String str, String str2, Exception exc) {
        logger.error(str2, (Throwable) exc);
    }
}
