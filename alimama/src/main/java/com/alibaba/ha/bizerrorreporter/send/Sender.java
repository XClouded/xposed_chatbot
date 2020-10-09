package com.alibaba.ha.bizerrorreporter.send;

import android.content.Context;
import android.util.Log;
import com.alibaba.ha.bizerrorreporter.BizErrorBuilder;
import com.alibaba.ha.bizerrorreporter.BizErrorReporter;
import com.alibaba.ha.bizerrorreporter.BizErrorSampling;
import com.alibaba.ha.bizerrorreporter.module.BizErrorModule;
import com.alibaba.ha.bizerrorreporter.module.SendModule;
import com.alibaba.motu.tbrest.SendService;
import java.util.Map;

public class Sender implements Runnable {
    BizErrorModule bizErrorModule;
    Context mContext;

    public Sender(Context context, BizErrorModule bizErrorModule2) {
        this.mContext = context;
        this.bizErrorModule = bizErrorModule2;
    }

    public void run() {
        SendModule build;
        try {
            if (this.bizErrorModule.businessType == null) {
                Log.i("MotuCrashAdapter", "business type cannot null");
            } else if ((BizErrorReporter.getInstance().sampling == null || canSend().booleanValue()) && (build = new BizErrorBuilder().build(this.mContext, this.bizErrorModule)) != null) {
                Integer num = build.eventId;
                String str = build.sendFlag;
                String str2 = build.sendContent;
                if (SendService.getInstance().sendRequest((String) null, System.currentTimeMillis(), build.businessType, num.intValue(), str, str2, build.aggregationType, (Map<String, String>) null).booleanValue()) {
                    Log.i("MotuCrashAdapter", "send business err success");
                } else {
                    Log.i("MotuCrashAdapter", "send business err failure");
                }
            }
        } catch (Exception e) {
            Log.e("MotuCrashAdapter", "send business err happen ", e);
        }
    }

    private Boolean canSend() {
        BizErrorSampling bizErrorSampling = BizErrorReporter.getInstance().sampling;
        int randomNumber = getRandomNumber(0, 10000);
        if (bizErrorSampling == BizErrorSampling.OneTenth) {
            if (randomNumber >= 0 && randomNumber < 1000) {
                return true;
            }
        } else if (bizErrorSampling == BizErrorSampling.OnePercent) {
            if (randomNumber >= 0 && randomNumber < 100) {
                return true;
            }
        } else if (bizErrorSampling == BizErrorSampling.OneThousandth) {
            if (randomNumber >= 0 && randomNumber < 10) {
                return true;
            }
        } else if (bizErrorSampling == BizErrorSampling.OneTenThousandth) {
            if (randomNumber >= 0 && randomNumber < 1) {
                return true;
            }
        } else if (bizErrorSampling == BizErrorSampling.Zero) {
            return false;
        } else {
            if (bizErrorSampling == BizErrorSampling.All) {
                return true;
            }
        }
        return false;
    }

    private int getRandomNumber(int i, int i2) {
        try {
            double random = Math.random();
            double d = (double) ((i2 - i) + 1);
            Double.isNaN(d);
            return i + ((int) (random * d));
        } catch (Exception e) {
            Log.e("MotuCrashAdapter", "get random number err", e);
            return 0;
        }
    }
}
