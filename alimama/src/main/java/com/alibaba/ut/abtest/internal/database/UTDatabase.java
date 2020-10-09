package com.alibaba.ut.abtest.internal.database;

import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.db.SqliteHelper;
import com.alibaba.ut.abtest.internal.ABContext;

public class UTDatabase extends Database {
    private static UTDatabase instance;

    private UTDatabase() {
        super(new SqliteHelper(ABContext.getInstance().getContext(), Constants.Database.DATABASE_NAME));
    }

    public static synchronized UTDatabase getInstance() {
        UTDatabase uTDatabase;
        synchronized (UTDatabase.class) {
            if (instance == null) {
                instance = new UTDatabase();
            }
            uTDatabase = instance;
        }
        return uTDatabase;
    }
}
