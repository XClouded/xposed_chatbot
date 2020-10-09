package com.ali.user.mobile.login.history;

import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.LoginHistory;
import java.util.List;

public interface LoginHistoryManager {
    int deleteAllLoginHistory();

    int deleteLoginHistory(long j);

    List<HistoryAccount> getHistoryList(String str);

    LoginHistory getLoginHistory();

    void saveHistory(HistoryAccount historyAccount, String str);

    void saveHistoryWithNoSalt(HistoryAccount historyAccount);
}
