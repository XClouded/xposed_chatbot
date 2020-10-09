package com.alimama.moon.update;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.anynetwork.core.ANResponse;
import com.alibaba.android.update.IUpdateCallback;
import com.alibaba.android.update4mtl.UpdatePriority;
import com.alibaba.android.update4mtl.Utils;
import com.alibaba.android.update4mtl.data.ResponseData;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.utils.PhoneInfo;
import com.alimama.union.app.logger.NewMonitorLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultUpdateCallback implements IUpdateCallback {
    private static final String TAG = "DefaultUpdateCallback";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) DefaultUpdateCallback.class);
    private IEventBus eventBus;

    public DefaultUpdateCallback(IEventBus iEventBus) {
        this.eventBus = iEventBus;
    }

    public void execute(Context context, Object obj) {
        logger.info("execute: {}", (Object) obj.toString());
    }

    public void onPreExecute(Context context) {
        logger.info("onPreExecute");
    }

    public void onPostExecute(Context context, Object obj) {
        if (context == null || obj == null) {
            logger.error("请求失败或者返回数据为空");
            NewMonitorLogger.Update.updatePostFailed(TAG, "请求失败或者返回数据为空");
            return;
        }
        logger.info("onPostExecute: {}", (Object) obj.toString());
        if (!(obj instanceof ANResponse)) {
            logger.error("response类型不正确");
            NewMonitorLogger.Update.updateNotANResponse(TAG, "response类型不正确");
            return;
        }
        ANResponse aNResponse = (ANResponse) obj;
        if (!aNResponse.getNetworkIsSuccess()) {
            logger.error("网络异常");
            NewMonitorLogger.Update.updateNetworkError(TAG, "网络异常");
            return;
        }
        ResponseData byte2Object = Utils.byte2Object(aNResponse.getNetworkResponseByteBody());
        if (!byte2Object.hasAvailableUpdate) {
            logger.debug("没有新版本");
            return;
        }
        String str = byte2Object.updateInfo.pri;
        if (TextUtils.equals(UpdatePriority.FORCE_TYPE.getValue(), str) || (TextUtils.equals(UpdatePriority.FORCE_WHEN_WIFI_TYPE.getValue(), str) && PhoneInfo.isWifiConnected(context))) {
            this.eventBus.post(new NewVersionForceUpdateEvent(byte2Object.updateInfo));
        } else {
            this.eventBus.post(new NewVersionRemindEvent(byte2Object.updateInfo));
        }
    }
}
