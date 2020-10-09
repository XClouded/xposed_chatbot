package com.alibaba.ut.page;

import android.app.Activity;
import android.util.Log;
import com.alibaba.ut.biz.UTAdpater;
import com.alibaba.ut.comm.ActivityLifecycleCB;
import com.alibaba.ut.utils.Logger;
import com.taobao.android.tlog.protocol.Constants;
import com.ut.mini.UTPageHitHelper;
import java.util.List;

public class PageDestroyHandler implements ActivityLifecycleCB.ActivityDestroyCallBack {
    public void init() {
        ActivityLifecycleCB.getInstance().addDestroyCallback(this);
    }

    public void onActivityDestroyed(Activity activity) {
        Logger.i((String) null, "activity", activity);
        List<VirtualPageObject> pageObject = PageObjectMgr.getPageObject(activity);
        if (pageObject.size() > 0) {
            for (VirtualPageObject next : pageObject) {
                try {
                    UTPageHitHelper.getInstance().pageDestroyed(next);
                } catch (Throwable unused) {
                    Log.i(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED, "onActivityDestroyed is exception");
                }
                UTAdpater.removeAplusParams(next.mDelegateActivityHashcode + "");
                PageObjectMgr.clearPageObject(next);
            }
        }
    }
}
