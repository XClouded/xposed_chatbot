package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.reflect.InvocationTargetException;

public class AliWXImage extends WXImage {
    private static final String CONFIG_GROUP = "AliWXImageView";

    public AliWXImage(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public ImageView initComponentHostView(@NonNull Context context) {
        AliWXImageView aliWXImageView = new AliWXImageView(context);
        if (isOpenBitmapSwitch()) {
            aliWXImageView.setEnableBitmapAutoManage(getConfigEnableBitmapAutoManage());
        }
        aliWXImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (Build.VERSION.SDK_INT >= 16) {
            aliWXImageView.setCropToPadding(true);
        }
        aliWXImageView.holdComponent((WXImage) this);
        return aliWXImageView;
    }

    public static class Create implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new AliWXImage(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    private boolean getConfigEnableBitmapAutoManage() {
        if (isBlackHC()) {
            return false;
        }
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null ? TextUtils.equals("true", configAdapter.getConfig(CONFIG_GROUP, "globalEnableBitmapAutoManage", "false")) : false) {
            return true;
        }
        if (isMainHC() && configAdapter != null) {
            return TextUtils.equals("true", configAdapter.getConfig(CONFIG_GROUP, "hcEnableBitmapAutoManage", "false"));
        }
        if (configAdapter == null || !TextUtils.equals("true", configAdapter.getConfig(CONFIG_GROUP, "normalEnableBitmapAutoManage", "false"))) {
            return false;
        }
        return true;
    }

    private boolean isMainHC() {
        if (getInstance() == null) {
            return false;
        }
        String bundleUrl = getInstance().getBundleUrl();
        if (TextUtils.isEmpty(bundleUrl)) {
            return false;
        }
        try {
            String config = AliWeex.getInstance().getConfigAdapter().getConfig(CONFIG_GROUP, "hc_domain", "");
            if (!TextUtils.isEmpty(config)) {
                for (String str : config.split(",")) {
                    if (!TextUtils.isEmpty(bundleUrl) && bundleUrl.contains(str)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isBlackHC() {
        if (getInstance() == null) {
            return false;
        }
        String bundleUrl = getInstance().getBundleUrl();
        if (TextUtils.isEmpty(bundleUrl)) {
            return false;
        }
        try {
            String config = AliWeex.getInstance().getConfigAdapter().getConfig(CONFIG_GROUP, "black_domain", "");
            if (!TextUtils.isEmpty(config)) {
                for (String str : config.split(",")) {
                    if (!TextUtils.isEmpty(bundleUrl) && bundleUrl.contains(str)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isOpenBitmapSwitch() {
        IConfigAdapter configAdapter;
        if (getInstance() == null || (configAdapter = AliWeex.getInstance().getConfigAdapter()) == null || !TextUtils.equals("true", configAdapter.getConfig(CONFIG_GROUP, "switch_open", ""))) {
            return false;
        }
        return true;
    }
}
