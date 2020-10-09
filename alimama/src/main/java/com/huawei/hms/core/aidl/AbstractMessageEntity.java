package com.huawei.hms.core.aidl;

import com.huawei.hms.core.aidl.a.a;
import com.huawei.hms.support.api.client.Status;

public class AbstractMessageEntity implements IMessageEntity {
    @a
    private Status commonStatus;

    public Status getCommonStatus() {
        return this.commonStatus;
    }

    public void setCommonStatus(Status status) {
        this.commonStatus = status;
    }
}
