package com.alibaba.android.prefetchx.core.data.adapter;

import com.alibaba.android.prefetchx.PFConstant;

public class PFDataUrlKeysAdapterImpl implements PFDataUrlKeysAdapter {
    public String getKeyEnable() {
        return "data_prefetch";
    }

    public String getKeyFlag() {
        return "wh_prefetch";
    }

    public String getKeyId() {
        return "wh_prefetch_id";
    }

    public String getKeyNeedLogin() {
        return "wh_needlogin";
    }

    public String getKeyRefreshGeo() {
        return PFConstant.Data.PF_DATA_REFRESH_GEO;
    }
}
