package com.alibaba.aliweex.hc.bundle;

import android.app.Activity;
import android.content.Context;
import com.alibaba.aliweex.bundle.RenderPresenter;
import com.alibaba.aliweex.bundle.WXNavBarAdapter;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alibaba.aliweex.hc.HCWXSDKInstance;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import java.util.Map;

public class HCRenderPresenter extends RenderPresenter {
    public HCRenderPresenter(Activity activity, String str, IWXRenderListener iWXRenderListener, WeexPageContract.IUTPresenter iUTPresenter, WeexPageContract.IDynamicUrlPresenter iDynamicUrlPresenter, WeexPageContract.IProgressBar iProgressBar, WXNavBarAdapter wXNavBarAdapter, WeexPageContract.IUrlValidate iUrlValidate) {
        super(activity, str, iWXRenderListener, iUTPresenter, iDynamicUrlPresenter, iProgressBar, wXNavBarAdapter, iUrlValidate);
    }

    public WXSDKInstance createWXSDKInstance(Context context) {
        HCWXSDKInstance hCWXSDKInstance = new HCWXSDKInstance(context, this.mFtag);
        hCWXSDKInstance.setWXNavBarAdapter(this.mNavBarAdapter);
        return hCWXSDKInstance;
    }

    public void startRenderByUrl(Map<String, Object> map, String str, String str2, String str3) {
        super.startRenderByUrl(map, str, str2, str3);
    }

    public void startRenderByTemplate(String str, String str2, Map<String, Object> map, String str3) {
        super.startRenderByTemplate(str, str2, map, str3);
    }
}
