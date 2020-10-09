package com.alibaba.aliweex.adapter.adapter;

import android.text.TextUtils;
import com.taobao.soloader.LogUtils;
import com.taobao.soloader.SoLoader;
import com.taobao.soloader.SoLoaderConstants;
import com.taobao.weex.adapter.IWXSoLoaderAdapter;
import com.taobao.weex.utils.WXLogUtils;

public class WXSoLoaderAdapter implements IWXSoLoaderAdapter {
    private boolean hasSoLoader;

    public WXSoLoaderAdapter() {
        try {
            Class.forName(SoLoader.class.getName());
            this.hasSoLoader = true;
        } catch (Throwable unused) {
            this.hasSoLoader = false;
        }
    }

    public void doLoadLibrary(final String str) {
        if (!this.hasSoLoader) {
            try {
                System.loadLibrary(str);
            } catch (Throwable th) {
                WXLogUtils.e(WXLogUtils.getStackTrace(th));
            }
        } else {
            SoLoader.loadLibrary(str, new SoLoader.LoadSoCallBack() {
                public void onSucceed() {
                    LogUtils.log_test("so Loader " + str + " success");
                }

                public void onFailed(SoLoaderConstants.SoLoaderError soLoaderError) {
                    String str = "";
                    if (soLoaderError != null && !TextUtils.isEmpty(soLoaderError.msg)) {
                        str = soLoaderError.msg;
                    }
                    LogUtils.log_test("so Loader " + str + " failed " + str);
                }
            });
        }
    }

    public void doLoad(String str) {
        if (!this.hasSoLoader) {
            try {
                System.load(str);
            } catch (Throwable th) {
                WXLogUtils.e(WXLogUtils.getStackTrace(th));
            }
        } else {
            SoLoader.load(str, new SoLoader.LoadSoCallBack() {
                public void onFailed(SoLoaderConstants.SoLoaderError soLoaderError) {
                }

                public void onSucceed() {
                }
            });
        }
    }
}
