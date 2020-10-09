package com.alimama.moon.emas.crashreporter;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.ha.bizerrorreporter.BizErrorReporter;
import com.alibaba.ha.bizerrorreporter.module.AggregationType;
import com.alibaba.ha.bizerrorreporter.module.BizErrorModule;

public class FlutterExceptionReporter {
    public static void sendError(Context context, @Nullable String str, String str2, String str3) {
        BizErrorModule bizErrorModule = new BizErrorModule();
        bizErrorModule.aggregationType = AggregationType.CONTENT;
        if (TextUtils.isEmpty(str)) {
            str = "FLUTTER_ERROR";
        }
        bizErrorModule.businessType = str;
        bizErrorModule.exceptionCode = str2;
        bizErrorModule.exceptionDetail = str3;
        bizErrorModule.exceptionVersion = "1.0";
        bizErrorModule.throwable = null;
        BizErrorReporter.getInstance().send(context, bizErrorModule);
    }
}
