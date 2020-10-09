package com.alibaba.ut.page;

import android.app.Activity;
import android.content.Context;
import com.alibaba.ut.utils.Logger;

public class VirtualPageObject extends Activity {
    public boolean isSPA = false;
    private Context mContext;
    public int mDelegateActivityHashcode = -1;

    public VirtualPageObject(boolean z, Context context) {
        this.isSPA = z;
        this.mDelegateActivityHashcode = context.hashCode();
        if (Logger.isDebug()) {
            this.mContext = context;
        }
    }

    public String toString() {
        return "VirtualPageObject{" + "isSPA=" + this.isSPA + ", mDelegateActivityHashcode=" + this.mDelegateActivityHashcode + ", mContext=" + this.mContext + '}';
    }
}
