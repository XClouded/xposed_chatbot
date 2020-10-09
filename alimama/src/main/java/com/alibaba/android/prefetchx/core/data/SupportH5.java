package com.alibaba.android.prefetchx.core.data;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.android.prefetchx.core.data.StorageInterface;

public class SupportH5 extends WVApiPlugin implements WVEventListener {
    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getResult".equals(str)) {
            getResult(str2, wVCallBackContext);
            return true;
        } else if ("removeResult".equals(str)) {
            removeResult(str2);
            return true;
        } else {
            WVResult wVResult = new WVResult();
            wVResult.addData(ILocatable.ERROR_MSG, "no matched method");
            wVCallBackContext.error(wVResult);
            return false;
        }
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        if (!(i != 1004 || wVEventContext == null || wVEventContext.context == null || objArr == null || objArr[1] == null)) {
            PFMtop.getInstance().prefetch(wVEventContext.context, objArr[1].toString());
        }
        return null;
    }

    private void getResult(String str, final WVCallBackContext wVCallBackContext) {
        StorageMemory.getInstance().readAsync(str, new StorageInterface.Callback() {
            public void onSuccess(String str) {
                wVCallBackContext.success(str);
            }

            public void onError(String str, String str2) {
                WVCallBackContext wVCallBackContext = wVCallBackContext;
                wVCallBackContext.error(str + "|" + str2);
            }
        });
    }

    private void removeResult(String str) {
        StorageMemory.getInstance().remove(str);
    }
}
