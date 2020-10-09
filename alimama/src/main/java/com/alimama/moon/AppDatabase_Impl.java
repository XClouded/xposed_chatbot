package com.alimama.moon;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alimama.union.app.aalogin.model.UserDao;
import com.alimama.union.app.aalogin.model.UserDao_Impl;
import com.alimama.union.app.messageCenter.model.AlertMessageDao;
import com.alimama.union.app.messageCenter.model.AlertMessageDao_Impl;
import com.alipay.sdk.authjs.a;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import java.util.HashMap;
import java.util.HashSet;

public final class AppDatabase_Impl extends AppDatabase {
    private volatile AlertMessageDao _alertMessageDao;
    private volatile UserDao _userDao;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(8) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `AlertMessage` (`taobaoNumId` INTEGER NOT NULL, `msgType` INTEGER NOT NULL, `id` INTEGER, `title` TEXT, `content` TEXT, `action` TEXT, `actionUrl` TEXT, `read` INTEGER NOT NULL, `createTime` INTEGER NOT NULL, `expireDay` INTEGER, `readTime` INTEGER, PRIMARY KEY(`taobaoNumId`, `msgType`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `User` (`userId` TEXT NOT NULL, `userNick` TEXT, `avatarLink` TEXT, `memberId` TEXT, `type` INTEGER, `finishTypeDisp` TEXT, `finishTypeDispDateStartIndex` INTEGER, `finishTypeDispDateEndIndex` INTEGER, `gradeString` TEXT, `taskStartTime` INTEGER, `taskEndTime` INTEGER, `taskStartShowTime` TEXT, `taskEndShowTime` TEXT, `lastTaskStartTime` INTEGER, `lastTaskEndTime` INTEGER, `currentTotalOrder` INTEGER, `currentTotalUV` INTEGER, `orderFinishRate` INTEGER, `uvFinishRate` INTEGER, `nextUpdateTime` TEXT, `butlerPrivilege` INTEGER, `walletPrivilege` INTEGER, `isInRisk` INTEGER, `grade` INTEGER, `threshold_checkOrderNum` INTEGER, `threshold_validOrderNum` INTEGER, `threshold_minValidOrderNum` INTEGER, `threshold_totalUv` INTEGER, PRIMARY KEY(`userId`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"55b513afa5eb59f1d87b2b06b8cc5d6e\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `AlertMessage`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `User`");
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (AppDatabase_Impl.this.mCallbacks != null) {
                    int size = AppDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) AppDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = AppDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                AppDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (AppDatabase_Impl.this.mCallbacks != null) {
                    int size = AppDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) AppDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(11);
                hashMap.put("taobaoNumId", new TableInfo.Column("taobaoNumId", "INTEGER", true, 1));
                hashMap.put(a.g, new TableInfo.Column(a.g, "INTEGER", true, 2));
                hashMap.put("id", new TableInfo.Column("id", "INTEGER", false, 0));
                hashMap.put("title", new TableInfo.Column("title", "TEXT", false, 0));
                hashMap.put("content", new TableInfo.Column("content", "TEXT", false, 0));
                hashMap.put("action", new TableInfo.Column("action", "TEXT", false, 0));
                hashMap.put("actionUrl", new TableInfo.Column("actionUrl", "TEXT", false, 0));
                hashMap.put(MonitorCacheEvent.OPERATION_READ, new TableInfo.Column(MonitorCacheEvent.OPERATION_READ, "INTEGER", true, 0));
                hashMap.put("createTime", new TableInfo.Column("createTime", "INTEGER", true, 0));
                hashMap.put("expireDay", new TableInfo.Column("expireDay", "INTEGER", false, 0));
                hashMap.put("readTime", new TableInfo.Column("readTime", "INTEGER", false, 0));
                TableInfo tableInfo = new TableInfo("AlertMessage", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "AlertMessage");
                if (tableInfo.equals(read)) {
                    HashMap hashMap2 = new HashMap(28);
                    hashMap2.put("userId", new TableInfo.Column("userId", "TEXT", true, 1));
                    hashMap2.put("userNick", new TableInfo.Column("userNick", "TEXT", false, 0));
                    hashMap2.put("avatarLink", new TableInfo.Column("avatarLink", "TEXT", false, 0));
                    hashMap2.put(ApiConstants.ApiField.MEMBER_ID, new TableInfo.Column(ApiConstants.ApiField.MEMBER_ID, "TEXT", false, 0));
                    hashMap2.put("type", new TableInfo.Column("type", "INTEGER", false, 0));
                    hashMap2.put("finishTypeDisp", new TableInfo.Column("finishTypeDisp", "TEXT", false, 0));
                    hashMap2.put("finishTypeDispDateStartIndex", new TableInfo.Column("finishTypeDispDateStartIndex", "INTEGER", false, 0));
                    hashMap2.put("finishTypeDispDateEndIndex", new TableInfo.Column("finishTypeDispDateEndIndex", "INTEGER", false, 0));
                    hashMap2.put("gradeString", new TableInfo.Column("gradeString", "TEXT", false, 0));
                    hashMap2.put("taskStartTime", new TableInfo.Column("taskStartTime", "INTEGER", false, 0));
                    hashMap2.put("taskEndTime", new TableInfo.Column("taskEndTime", "INTEGER", false, 0));
                    hashMap2.put("taskStartShowTime", new TableInfo.Column("taskStartShowTime", "TEXT", false, 0));
                    hashMap2.put("taskEndShowTime", new TableInfo.Column("taskEndShowTime", "TEXT", false, 0));
                    hashMap2.put("lastTaskStartTime", new TableInfo.Column("lastTaskStartTime", "INTEGER", false, 0));
                    hashMap2.put("lastTaskEndTime", new TableInfo.Column("lastTaskEndTime", "INTEGER", false, 0));
                    hashMap2.put("currentTotalOrder", new TableInfo.Column("currentTotalOrder", "INTEGER", false, 0));
                    hashMap2.put("currentTotalUV", new TableInfo.Column("currentTotalUV", "INTEGER", false, 0));
                    hashMap2.put("orderFinishRate", new TableInfo.Column("orderFinishRate", "INTEGER", false, 0));
                    hashMap2.put("uvFinishRate", new TableInfo.Column("uvFinishRate", "INTEGER", false, 0));
                    hashMap2.put("nextUpdateTime", new TableInfo.Column("nextUpdateTime", "TEXT", false, 0));
                    hashMap2.put("butlerPrivilege", new TableInfo.Column("butlerPrivilege", "INTEGER", false, 0));
                    hashMap2.put("walletPrivilege", new TableInfo.Column("walletPrivilege", "INTEGER", false, 0));
                    hashMap2.put("isInRisk", new TableInfo.Column("isInRisk", "INTEGER", false, 0));
                    hashMap2.put("grade", new TableInfo.Column("grade", "INTEGER", false, 0));
                    hashMap2.put("threshold_checkOrderNum", new TableInfo.Column("threshold_checkOrderNum", "INTEGER", false, 0));
                    hashMap2.put("threshold_validOrderNum", new TableInfo.Column("threshold_validOrderNum", "INTEGER", false, 0));
                    hashMap2.put("threshold_minValidOrderNum", new TableInfo.Column("threshold_minValidOrderNum", "INTEGER", false, 0));
                    hashMap2.put("threshold_totalUv", new TableInfo.Column("threshold_totalUv", "INTEGER", false, 0));
                    TableInfo tableInfo2 = new TableInfo("User", hashMap2, new HashSet(0), new HashSet(0));
                    TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "User");
                    if (!tableInfo2.equals(read2)) {
                        throw new IllegalStateException("Migration didn't properly handle User(com.alimama.union.app.aalogin.model.User).\n Expected:\n" + tableInfo2 + "\n" + " Found:\n" + read2);
                    }
                    return;
                }
                throw new IllegalStateException("Migration didn't properly handle AlertMessage(com.alimama.union.app.messageCenter.model.AlertMessage).\n Expected:\n" + tableInfo + "\n" + " Found:\n" + read);
            }
        }, "55b513afa5eb59f1d87b2b06b8cc5d6e", "33d2348c1a2717c296b6aeb9e56dde08")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "AlertMessage", "User");
    }

    public void clearAllTables() {
        String str;
        String str2;
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `AlertMessage`");
            writableDatabase.execSQL("DELETE FROM `User`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            str = "PRAGMA wal_checkpoint(FULL)";
            writableDatabase.query(str).close();
            if (!writableDatabase.inTransaction()) {
                str2 = "VACUUM";
                writableDatabase.execSQL(str2);
            }
        }
    }

    public AlertMessageDao alertMessageDao() {
        AlertMessageDao alertMessageDao;
        if (this._alertMessageDao != null) {
            return this._alertMessageDao;
        }
        synchronized (this) {
            if (this._alertMessageDao == null) {
                this._alertMessageDao = new AlertMessageDao_Impl(this);
            }
            alertMessageDao = this._alertMessageDao;
        }
        return alertMessageDao;
    }

    public UserDao userDao() {
        UserDao userDao;
        if (this._userDao != null) {
            return this._userDao;
        }
        synchronized (this) {
            if (this._userDao == null) {
                this._userDao = new UserDao_Impl(this);
            }
            userDao = this._userDao;
        }
        return userDao;
    }
}
