package com.alibaba.ha.adapter.service.bizError;

import android.content.Context;
import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Sampling;
import com.alibaba.ha.bizerrorreporter.BizErrorReporter;
import com.alibaba.ha.bizerrorreporter.BizErrorSampling;

public class BizErrorService {
    public void sendBizError(Context context, BizErrorInfo bizErrorInfo) {
        if (bizErrorInfo != null) {
            BizErrorReporter.getInstance().send(context, bizErrorInfo);
        }
    }

    public void openSampling(Sampling sampling) {
        try {
            BizErrorReporter.getInstance().openSampling(BizErrorSampling.valueOf(sampling.name()));
        } catch (Exception e) {
            Log.e(AliHaAdapter.TAG, "open biz error sampling failure ", e);
        }
    }
}
