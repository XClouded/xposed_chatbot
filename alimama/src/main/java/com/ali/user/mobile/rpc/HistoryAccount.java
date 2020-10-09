package com.ali.user.mobile.rpc;

import android.text.TextUtils;
import java.util.Comparator;

public class HistoryAccount implements Comparator {
    public String accountId;
    public String alipayCrossed;
    public long alipayHid;
    public String autologinToken;
    public String email;
    public int hasPwd = -1;
    public String headImg;
    public String loginPhone;
    public int loginSite;
    public long loginTime;
    public String loginType;
    public String mobile;
    public String nick;
    public String tokenKey;
    public long userId;
    public String userInputName;

    public String getLoginPhone() {
        return this.loginPhone;
    }

    public void setLoginPhone(String str) {
        this.loginPhone = str;
    }

    public int getLoginSite() {
        return this.loginSite;
    }

    public void setLoginSite(int i) {
        this.loginSite = i;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String str) {
        this.accountId = str;
    }

    public int isHasPwd() {
        return this.hasPwd;
    }

    public void setHasPwd(int i) {
        this.hasPwd = i;
    }

    public HistoryAccount() {
    }

    public HistoryAccount(String str, String str2, String str3, long j, long j2, String str4, long j3, String str5, String str6, String str7, String str8, String str9, int i) {
        this.userInputName = str;
        this.mobile = str2;
        this.headImg = str3;
        this.userId = j;
        this.alipayHid = j2;
        this.autologinToken = str4;
        this.loginTime = j3;
        this.tokenKey = str5;
        this.loginType = str6;
        this.nick = str7;
        this.email = str8;
        this.alipayCrossed = str9;
        this.loginSite = i;
    }

    public void update(HistoryAccount historyAccount) {
        if (this.userId == historyAccount.userId) {
            if (!TextUtils.isEmpty(historyAccount.autologinToken)) {
                this.autologinToken = historyAccount.autologinToken;
            }
            if (!TextUtils.isEmpty(historyAccount.userInputName)) {
                this.userInputName = historyAccount.userInputName;
            }
            if (!TextUtils.isEmpty(historyAccount.tokenKey)) {
                this.tokenKey = historyAccount.tokenKey;
            }
            if (!TextUtils.isEmpty(historyAccount.email)) {
                this.email = historyAccount.email;
            }
            if (!TextUtils.isEmpty(historyAccount.headImg)) {
                this.headImg = historyAccount.headImg;
            }
            if (!TextUtils.isEmpty(historyAccount.loginType)) {
                this.loginType = historyAccount.loginType;
            }
            if (!TextUtils.isEmpty(historyAccount.nick)) {
                this.nick = historyAccount.nick;
            }
            if (!TextUtils.isEmpty(historyAccount.mobile)) {
                this.mobile = historyAccount.mobile;
            }
            if (historyAccount.loginTime > 0) {
                this.loginTime = historyAccount.loginTime;
            }
            this.loginSite = historyAccount.loginSite;
            this.alipayCrossed = historyAccount.alipayCrossed;
            this.hasPwd = historyAccount.hasPwd;
        }
    }

    public int compare(Object obj, Object obj2) {
        HistoryAccount historyAccount = (HistoryAccount) obj;
        HistoryAccount historyAccount2 = (HistoryAccount) obj2;
        if (historyAccount.loginTime > historyAccount2.loginTime) {
            return -1;
        }
        return historyAccount.loginTime == historyAccount2.loginTime ? 0 : 1;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String toString() {
        return this.userInputName + "";
    }
}
