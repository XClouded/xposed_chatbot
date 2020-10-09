package com.huawei.hms.support.api.entity.core;

import android.content.Intent;
import com.huawei.hms.core.aidl.a.a;

public class JosGetNoticeResp extends JosBaseResp {
    @a
    private Intent noticeIntent;

    private static <T> T get(T t) {
        return t;
    }

    public Intent getNoticeIntent() {
        return (Intent) get(this.noticeIntent);
    }

    public void setNoticeIntent(Intent intent) {
        this.noticeIntent = intent;
    }
}
