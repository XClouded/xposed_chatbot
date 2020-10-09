package com.alimama.moon.alipay;

import android.app.Activity;
import android.text.TextUtils;
import com.alipay.sdk.app.PayTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlipayAdapter {
    /* access modifiers changed from: private */
    public static final Logger logger = LoggerFactory.getLogger((Class<?>) AlipayAdapter.class);

    public interface AlipayCallback {
        void payComplete(String str, PayResult payResult);
    }

    public static void payV2(final Activity activity, final String str, final AlipayCallback alipayCallback) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    PayResult payResult = new PayResult(new PayTask(activity).payV2(str, true));
                    String str = "-1";
                    if (!TextUtils.isEmpty(payResult.getResultStatus())) {
                        str = payResult.getResultStatus();
                    }
                    if (alipayCallback != null) {
                        alipayCallback.payComplete(str, payResult);
                    }
                } catch (Exception e) {
                    AlipayAdapter.logger.error("alipay payV2 ex: {}", (Object) e.getMessage());
                }
            }
        }).start();
    }
}
