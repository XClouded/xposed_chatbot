package com.alibaba.ha.adapter.service.ut;

import com.alibaba.ha.adapter.Sampling;
import com.alibaba.mtl.appmonitor.AppMonitor;

public class UtAppMonitor {
    public void changeSampling(Sampling sampling) {
        if (sampling.equals(Sampling.All)) {
            AppMonitor.setSampling(10000);
        } else if (sampling.equals(Sampling.OneTenth)) {
            AppMonitor.setSampling(1000);
        } else if (sampling.equals(Sampling.OnePercent)) {
            AppMonitor.setSampling(100);
        } else if (sampling.equals(Sampling.OneThousandth)) {
            AppMonitor.setSampling(10);
        } else if (sampling.equals(Sampling.OneTenThousandth)) {
            AppMonitor.setSampling(1);
        } else if (sampling.equals(Sampling.Zero)) {
            AppMonitor.setSampling(0);
        }
    }
}
