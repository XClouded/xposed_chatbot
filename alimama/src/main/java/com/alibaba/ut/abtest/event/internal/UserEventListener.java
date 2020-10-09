package com.alibaba.ut.abtest.event.internal;

import com.alibaba.ut.abtest.UTABMethod;
import com.alibaba.ut.abtest.event.Event;
import com.alibaba.ut.abtest.event.EventListener;
import com.alibaba.ut.abtest.event.LoginUser;
import com.alibaba.ut.abtest.internal.ABContext;

public class UserEventListener implements EventListener<LoginUser> {
    public void onEvent(Event<LoginUser> event) {
        ABContext.getInstance().getDecisionService().syncExperiments(true);
        if (ABContext.getInstance().getCurrentApiMethod() == UTABMethod.Push) {
            ABContext.getInstance().getPushService().syncExperiments(true);
            ABContext.getInstance().getPushService().syncWhitelist(true);
        }
    }
}
