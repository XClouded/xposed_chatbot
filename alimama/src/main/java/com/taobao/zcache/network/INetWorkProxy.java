package com.taobao.zcache.network;

import android.content.Context;
import java.util.Map;

public interface INetWorkProxy {
    IResponse getWebResourceResponse(Context context, String str, Map<String, String> map, boolean z, boolean z2, int i);

    boolean isSupportSpdy(Context context, String str);

    void resetStat(String str);
}
