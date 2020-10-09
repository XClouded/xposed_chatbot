package com.ali.user.mobile.rpc.login.model;

import java.util.Comparator;

public class SessionModel extends AliUserResponseData implements Comparator {
    public String showLoginId;
    public int site;

    public int compare(Object obj, Object obj2) {
        SessionModel sessionModel = (SessionModel) obj;
        SessionModel sessionModel2 = (SessionModel) obj2;
        if (sessionModel.loginTime > sessionModel2.loginTime) {
            return -1;
        }
        return sessionModel.loginTime == sessionModel2.loginTime ? 0 : 1;
    }
}
