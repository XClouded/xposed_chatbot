package com.ali.user.mobile.db;

import com.ali.user.mobile.login.history.LoginHistoryManager;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.LoginHistory;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.alibaba.mtl.appmonitor.AppMonitor;
import java.util.ArrayList;
import java.util.List;

public class LoginHistoryOperater implements LoginHistoryManager {
    private static LoginHistoryOperater instance;

    public int deleteAllLoginHistory() {
        return 0;
    }

    public int deleteLoginHistory(long j) {
        return 0;
    }

    public static LoginHistoryOperater getInstance() {
        if (instance == null) {
            instance = new LoginHistoryOperater();
        }
        return instance;
    }

    private LoginHistoryOperater() {
    }

    public void saveHistory(HistoryAccount historyAccount, String str) {
        try {
            SecurityGuardManagerWraper.putLoginHistory(historyAccount, str);
        } catch (Throwable th) {
            th.printStackTrace();
            AppMonitor.Alarm.commitFail("LoginHistoryOperater", "saveHistory", "38", "exception=" + th);
        }
    }

    public void saveHistoryWithNoSalt(HistoryAccount historyAccount) {
        try {
            SecurityGuardManagerWraper.saveHistoryOnly(historyAccount);
        } catch (Throwable th) {
            th.printStackTrace();
            AppMonitor.Alarm.commitFail("LoginHistoryOperater", "saveHistory", "38", "exception=" + th);
        }
    }

    public List<HistoryAccount> getHistoryList(String str) {
        try {
            return SecurityGuardManagerWraper.getHistoryAccounts();
        } catch (Exception unused) {
            return new ArrayList();
        }
    }

    public LoginHistory getLoginHistory() {
        return SecurityGuardManagerWraper.getLoginHistory();
    }
}
