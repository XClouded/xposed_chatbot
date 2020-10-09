package com.taobao.android.dinamicx;

import com.alibaba.android.umbrella.export.UmbrellaServiceFetcher;
import com.alibaba.android.umbrella.link.UMLinkLogInterface;
import com.alibaba.android.umbrella.link.UMRefContext;
import com.alibaba.android.umbrella.link.export.UMUserData;
import com.taobao.android.dinamicx.monitor.DXAbsUmbrella;
import java.util.Map;

public class DXUmbrellaImpl extends DXAbsUmbrella {
    private UMLinkLogInterface umLogInstance = UmbrellaServiceFetcher.getUmbrella();

    public void commitSuccess(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        if (this.umLogInstance != null) {
            this.umLogInstance.commitSuccess(str, str2, str3, str4, str5, map);
        }
    }

    public void commitFailure(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        if (this.umLogInstance != null) {
            this.umLogInstance.commitFailure(str, str2, str3, str4, str5, map, str6, str7);
        }
    }

    public void logError(String str, String str2, String str3, String str4, String str5, String str6, Map<String, Object> map, Map<String, Object> map2) {
        if (this.umLogInstance != null) {
            UMUserData uMUserData = null;
            if (map2 != null) {
                uMUserData = UMUserData.fromMap(map2);
            }
            this.umLogInstance.logErrorRawDim(str, str2, str3, (UMRefContext) null, str5, str6, map, uMUserData);
        }
    }
}
