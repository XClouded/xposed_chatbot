package com.taobao.android.ultron.common;

import com.taobao.android.ultron.common.model.IDMComponent;

public class ValidateResult {
    private IDMComponent mDMComponent;
    private String mValidateFailedMsg;
    private boolean mValidateState = true;

    public void setValidateFailedMsg(String str) {
        this.mValidateFailedMsg = str;
    }

    public String getValidateFailedMsg() {
        return this.mValidateFailedMsg;
    }

    public boolean getValidateState() {
        return this.mValidateState;
    }

    public void setValidateState(boolean z) {
        this.mValidateState = z;
    }

    public IDMComponent getValidateFailedComponent() {
        return this.mDMComponent;
    }

    public void setValidateFailedComponent(IDMComponent iDMComponent) {
        this.mDMComponent = iDMComponent;
    }
}
