package com.alibaba.motu.crashreportadapter;

import android.content.Context;
import android.util.Log;
import com.alibaba.motu.crashreportadapter.module.AdapterBase;
import com.alibaba.motu.crashreportadapter.module.AdapterSendModule;
import com.alibaba.motu.crashreporter.utrestapi.UTRestReq;
import java.util.Map;

public class MotuReportAdapteHandler {
    public void adapter(Context context, AdapterBase adapterBase) {
        try {
            AdapterSendModule build = new MotuAdapteBuilder().build(context, adapterBase);
            if (build != null) {
                new Thread(new AdapterSendThread(build, context)).start();
            }
        } catch (Exception e) {
            Log.e("MotuCrashAdapter", "adapter err", e);
        }
    }

    public class AdapterSendThread implements Runnable {
        Context mContext;
        AdapterSendModule mResult;

        AdapterSendThread(AdapterSendModule adapterSendModule, Context context) {
            this.mResult = adapterSendModule;
            this.mContext = context;
        }

        public void run() {
            try {
                Integer num = this.mResult.eventId;
                String str = this.mResult.sendFlag;
                String str2 = this.mResult.sendContent;
                if (UTRestReq.sendLog(this.mContext, System.currentTimeMillis(), this.mResult.businessType, num.intValue(), str, str2, this.mResult.aggregationType, (Map<String, String>) null)) {
                    Log.i("MotuCrashAdapter", "send business err success");
                } else {
                    Log.i("MotuCrashAdapter", "send business err failure");
                }
            } catch (Exception e) {
                Log.e("MotuCrashAdapter", "adapter send err", e);
            }
        }
    }
}
