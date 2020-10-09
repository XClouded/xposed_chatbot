package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.adapter.view.CountDownText;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Map;

public class WXCountDown extends WXComponent {
    private CountDownText mCountDown;

    public WXCountDown(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(@NonNull Context context) {
        this.mCountDown = new CountDownText(getContext());
        return this.mCountDown.getView();
    }

    @WXComponentProp(name = "textColor")
    public void setTextColor(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("#")) {
            this.mCountDown.setCountDownTextColor(str);
        }
    }

    @WXComponentProp(name = "timeColor")
    public void setTimeColor(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("#")) {
            this.mCountDown.setTimeColor(str);
        }
    }

    @WXComponentProp(name = "fontSize")
    public void setFontSize(String str) {
        int i;
        int fontSize = getFontSize(str);
        if (fontSize > 0) {
            if (WXEnvironment.sDefaultWidth > WXViewUtils.getScreenWidth()) {
                i = (int) WXViewUtils.getRealPxByWidth((float) (fontSize - 3));
            } else {
                i = (int) WXViewUtils.getRealPxByWidth((float) (fontSize - 2));
            }
            this.mCountDown.setFontSize(0, i);
        }
    }

    @WXComponentProp(name = "timeFontSize")
    public void setTimeFontSize(String str) {
        int i;
        int fontSize = getFontSize(str);
        if (fontSize > 0) {
            if (WXEnvironment.sDefaultWidth > WXViewUtils.getScreenWidth()) {
                i = (int) WXViewUtils.getRealPxByWidth((float) (fontSize - 3));
            } else {
                i = (int) WXViewUtils.getRealPxByWidth((float) (fontSize - 2));
            }
            this.mCountDown.setTimeFontSize(0, i);
        }
    }

    private int getFontSize(String str) {
        int i = WXUtils.getInt(str);
        if (i <= 0) {
            return 32;
        }
        return i;
    }

    @WXComponentProp(name = "formatterValue")
    public void setFormatterValue(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mCountDown.setDateFormat(str);
        }
    }

    @WXComponentProp(name = "countdownTime")
    public void setCountdownTime(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mCountDown.setTime(str);
        }
    }

    public void updateProperties(Map map) {
        super.updateProperties(map);
        this.mCountDown.start();
    }

    public void destroy() {
        super.destroy();
        if (getHostView() != null) {
            this.mCountDown.destroy();
        }
    }
}
