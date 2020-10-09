package com.alibaba.analytics.core.store;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.utils.Logger;
import java.util.List;

class LogSqliteStore implements ILogStore {
    private static final String TAG = "UTSqliteLogStore";
    String countSql = "SELECT count(*) FROM %s";
    String deleteSql = "DELETE FROM  %s where _id in ( select _id from %s  ORDER BY priority ASC ,  _id ASC LIMIT %d )";
    String querySql = "SELECT * FROM %s ORDER BY %s ASC LIMIT %d";

    LogSqliteStore(Context context) {
    }

    public synchronized boolean insert(List<Log> list) {
        Variables.getInstance().getDbMgr().insert((List<? extends Entity>) list);
        return true;
    }

    public synchronized int delete(List<Log> list) {
        return Variables.getInstance().getDbMgr().delete((List<? extends Entity>) list);
    }

    public synchronized List<Log> get(int i) {
        return Variables.getInstance().getDbMgr().find(Log.class, (String) null, "priority DESC , time DESC ", i);
    }

    public synchronized int count() {
        return Variables.getInstance().getDbMgr().count(Log.class);
    }

    public synchronized void clear() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) Log.class);
    }

    public synchronized int clearOldLogByField(String str, String str2) {
        Logger.d();
        return Variables.getInstance().getDbMgr().delete(Log.class, str + "< ?", new String[]{str2});
    }

    public int clearOldLogByCount(int i) {
        Logger.d();
        String tablename = Variables.getInstance().getDbMgr().getTablename(Log.class);
        return Variables.getInstance().getDbMgr().delete(Log.class, " _id in ( select _id from " + tablename + "  ORDER BY " + "priority" + " ASC , _id ASC LIMIT " + i + " )", (String[]) null);
    }

    public double getDbFileSize() {
        return Variables.getInstance().getDbMgr().getDbFileSize();
    }

    public synchronized void update(List<Log> list) {
        Variables.getInstance().getDbMgr().update((List<? extends Entity>) list);
    }

    public synchronized void updateLogPriority(List<Log> list) {
        Variables.getInstance().getDbMgr().updateLogPriority(list);
    }
}
