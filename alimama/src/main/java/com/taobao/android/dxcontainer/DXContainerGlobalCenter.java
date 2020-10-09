package com.taobao.android.dxcontainer;

import com.taobao.android.dxcontainer.layout.IDXContainerLayout;
import com.taobao.android.dxcontainer.layout.impl.IDXContainerLinearLayout;
import com.taobao.android.dxcontainer.layout.impl.IDXContainerStickyLayout;
import com.taobao.android.dxcontainer.layout.impl.IDXContainerViewPagerLayout;
import com.taobao.android.dxcontainer.layout.impl.IDXContainerWaterfallLayout;
import java.util.HashMap;
import java.util.Map;

public class DXContainerGlobalCenter {
    static boolean debug;
    private static Map<String, IDXContainerLayout> globalLayoutMap = new HashMap();
    static IDXContainerRecyclerViewInterface recyclerViewInterface;

    static {
        registerIDXCLayout(new IDXContainerLinearLayout());
        registerIDXCLayout(new IDXContainerStickyLayout());
        registerIDXCLayout(new IDXContainerWaterfallLayout());
        registerIDXCLayout(new IDXContainerViewPagerLayout());
    }

    private static void registerIDXCLayout(IDXContainerLayout iDXContainerLayout) {
        if (iDXContainerLayout != null) {
            globalLayoutMap.put(iDXContainerLayout.getLayoutType(), iDXContainerLayout);
        }
    }

    public static void registerGlobalIDXCLayout(IDXContainerLayout iDXContainerLayout) {
        registerIDXCLayout(iDXContainerLayout);
    }

    public static IDXContainerLayout getDXCLayoutHelper(String str) {
        return globalLayoutMap.get(str);
    }

    static IDXContainerRecyclerViewInterface getRecyclerViewInterface() {
        return recyclerViewInterface;
    }

    public static boolean isDebug() {
        return debug;
    }
}
