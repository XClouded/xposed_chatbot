package com.alibaba.android.enhance.nested;

import android.content.Context;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.IEventHandler;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.enhance.nested.nested.BindingXNestHandler;
import com.alibaba.android.enhance.nested.nested.WXNestedChild;
import com.alibaba.android.enhance.nested.nested.WXNestedHeader;
import com.alibaba.android.enhance.nested.nested.WXNestedParent;
import com.alibaba.android.enhance.nested.overscroll.WXNestedOverScrollModule;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;

public class NestedPlugin {
    public static void register() {
        try {
            WXSDKEngine.registerComponent("nested-parent", (Class<? extends WXComponent>) WXNestedParent.class);
            WXSDKEngine.registerComponent("nested-header", (Class<? extends WXComponent>) WXNestedHeader.class);
            WXSDKEngine.registerComponent("nested-child", (Class<? extends WXComponent>) WXNestedChild.class);
            BindingXCore.registerGlobalEventHandler(BindingXNestHandler.NEST_PLUGIN_NAME, new BindingXCore.ObjectCreator<IEventHandler, Context, PlatformManager>() {
                public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                    return new BindingXNestHandler(context, platformManager, objArr);
                }
            });
            WXSDKEngine.registerModule("nested-overscroll", WXNestedOverScrollModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }
}
