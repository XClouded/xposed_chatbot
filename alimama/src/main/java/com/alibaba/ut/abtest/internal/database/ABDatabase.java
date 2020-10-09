package com.alibaba.ut.abtest.internal.database;

import com.alibaba.ut.abtest.internal.ABContext;

public class ABDatabase extends Database {
    private static ABDatabase instance;

    private ABDatabase() {
        super(new ABDatabaseHelper(ABContext.getInstance().getContext()));
    }

    public static synchronized ABDatabase getInstance() {
        ABDatabase aBDatabase;
        synchronized (ABDatabase.class) {
            if (instance == null) {
                instance = new ABDatabase();
            }
            aBDatabase = instance;
        }
        return aBDatabase;
    }
}
