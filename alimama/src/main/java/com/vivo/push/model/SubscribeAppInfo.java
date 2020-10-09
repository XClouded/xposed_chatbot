package com.vivo.push.model;

import com.taobao.weex.el.parse.Operators;

public class SubscribeAppInfo {
    public static final int SUBSCIRBE = 1;
    public static final int SUBSCIRBE_CANCLE = 2;
    private int mActualStatus;
    private String mName;
    private int mTargetStatus;

    public SubscribeAppInfo(String str, int i, int i2) {
        this.mName = str;
        this.mTargetStatus = i;
        this.mActualStatus = i2;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public int getTargetStatus() {
        return this.mTargetStatus;
    }

    public void setTargetStatus(int i) {
        this.mTargetStatus = i;
    }

    public int getActualStatus() {
        return this.mActualStatus;
    }

    public void setActualStatus(int i) {
        this.mActualStatus = i;
    }

    public int hashCode() {
        return (((this.mName == null ? 0 : this.mName.hashCode()) + 31) * 31) + this.mTargetStatus;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SubscribeAppInfo subscribeAppInfo = (SubscribeAppInfo) obj;
        if (this.mName == null) {
            if (subscribeAppInfo.mName != null) {
                return false;
            }
        } else if (!this.mName.equals(subscribeAppInfo.mName)) {
            return false;
        }
        return this.mTargetStatus == subscribeAppInfo.mTargetStatus;
    }

    public String toString() {
        return "SubscribeAppInfo [mName=" + this.mName + ", mTargetStatus=" + this.mTargetStatus + ", mActualStatus=" + this.mActualStatus + Operators.ARRAY_END_STR;
    }
}
