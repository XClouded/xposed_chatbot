package com.alibaba.aliweex.bundle;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.weex.WXSDKInstance;

public class UrlValidatePresenter implements Handler.Callback, WeexPageContract.IUrlValidate {
    public static final int SHOW_TIP_VIEW = 18;
    private Activity mActivity;
    private Handler mHandler = new Handler(this);
    private boolean mShowInvalidUrlTips;
    private UrlValidateToast mToast;

    public UrlValidatePresenter(Activity activity) {
        this.mActivity = activity;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void onWXViewCreated(WXSDKInstance wXSDKInstance, View view) {
        if (this.mToast == null) {
            this.mToast = new UrlValidateToast(wXSDKInstance.getContext(), view);
        }
        if (this.mShowInvalidUrlTips) {
            this.mHandler.sendEmptyMessage(18);
        }
    }

    public void checkUrlValidate(String str) {
        if (UrlValidate.shouldShowInvalidUrlTips(str)) {
            this.mShowInvalidUrlTips = true;
        }
    }

    public boolean handleMessage(Message message) {
        if (message.what != 18 || this.mToast == null) {
            return false;
        }
        this.mToast.showNotiView((Drawable) null, "检测到该网址为外部网站，外部网站打开可能存在安全隐患，请注意保护您的个人隐私", WXUtil.getActionBarHeight(this.mActivity));
        return true;
    }
}
