package com.taobao.android.dxcontainer.layout;

import android.text.TextUtils;
import com.taobao.android.dxcontainer.DXContainerGlobalCenter;
import java.util.HashMap;
import java.util.Map;

public class DXContainerLayoutManager {
    private Map<String, IDXContainerLayout> layoutMap = new HashMap();

    public void registerIDXCLayout(IDXContainerLayout iDXContainerLayout) {
        if (iDXContainerLayout != null) {
            this.layoutMap.put(iDXContainerLayout.getLayoutType(), iDXContainerLayout);
        }
    }

    public IDXContainerLayout getIDXCLayout(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "linear";
        }
        IDXContainerLayout iDXContainerLayout = this.layoutMap.get(str);
        return iDXContainerLayout == null ? DXContainerGlobalCenter.getDXCLayoutHelper(str) : iDXContainerLayout;
    }
}
