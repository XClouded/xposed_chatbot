package com.taobao;

import com.taobao.statistic.TBS;

public class AdvAdapter {
    private final TBS.Adv mAdv;

    public AdvAdapter(TBS.Adv adv) {
        this.mAdv = adv;
    }

    @Deprecated
    public static String getUtsid() {
        return TBS.Adv.getUtsid();
    }
}
