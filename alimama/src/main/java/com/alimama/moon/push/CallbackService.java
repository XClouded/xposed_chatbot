package com.alimama.moon.push;

import com.taobao.accs.base.TaoBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackService extends TaoBaseService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) CallbackService.class);

    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        logger.info("onData-serviceId: {}, userId: {}, dataId: {}", str, str2, str3);
    }

    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        logger.info("onBind-serviceId: {}, errorCode: {}, info: {}", str, Integer.valueOf(i), extraInfo);
    }

    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        logger.info("onUnbind-serviceId: {}, errorCode: {}, info: {}", str, Integer.valueOf(i), extraInfo);
    }

    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
        logger.info("onSendData-serviceId: {}, dataId: {}, errorCode: {}, info: {}", str, str2, Integer.valueOf(i), extraInfo);
    }

    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        logger.info("onResponse-servieId: {}, dataId: {}, errorCode: {}, response: {}, info: {}", str, str2, Integer.valueOf(i), bArr, extraInfo);
    }
}
