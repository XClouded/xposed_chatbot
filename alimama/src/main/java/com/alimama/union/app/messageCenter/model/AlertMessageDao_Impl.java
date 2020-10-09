package com.alimama.union.app.messageCenter.model;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.alimama.moon.RoomConverters;
import com.alipay.sdk.authjs.a;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import java.util.List;
import java.util.Set;

public final class AlertMessageDao_Impl implements AlertMessageDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfAlertMessage;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfAlertMessage;

    public AlertMessageDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfAlertMessage = new EntityInsertionAdapter<AlertMessage>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `AlertMessage`(`taobaoNumId`,`msgType`,`id`,`title`,`content`,`action`,`actionUrl`,`read`,`createTime`,`expireDay`,`readTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AlertMessage alertMessage) {
                if (alertMessage.taobaoNumId == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, alertMessage.taobaoNumId.longValue());
                }
                if (alertMessage.msgType == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindLong(2, (long) alertMessage.msgType.intValue());
                }
                if (alertMessage.id == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindLong(3, alertMessage.id.longValue());
                }
                if (alertMessage.title == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, alertMessage.title);
                }
                if (alertMessage.content == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, alertMessage.content);
                }
                if (alertMessage.action == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, alertMessage.action);
                }
                if (alertMessage.actionUrl == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, alertMessage.actionUrl);
                }
                if (alertMessage.read == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindLong(8, (long) alertMessage.read.intValue());
                }
                Long dateToTimestamp = RoomConverters.dateToTimestamp(alertMessage.createTime);
                if (dateToTimestamp == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindLong(9, dateToTimestamp.longValue());
                }
                if (alertMessage.expireDay == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindLong(10, (long) alertMessage.expireDay.intValue());
                }
                Long dateToTimestamp2 = RoomConverters.dateToTimestamp(alertMessage.readTime);
                if (dateToTimestamp2 == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindLong(11, dateToTimestamp2.longValue());
                }
            }
        };
        this.__updateAdapterOfAlertMessage = new EntityDeletionOrUpdateAdapter<AlertMessage>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR REPLACE `AlertMessage` SET `taobaoNumId` = ?,`msgType` = ?,`id` = ?,`title` = ?,`content` = ?,`action` = ?,`actionUrl` = ?,`read` = ?,`createTime` = ?,`expireDay` = ?,`readTime` = ? WHERE `taobaoNumId` = ? AND `msgType` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AlertMessage alertMessage) {
                if (alertMessage.taobaoNumId == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindLong(1, alertMessage.taobaoNumId.longValue());
                }
                if (alertMessage.msgType == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindLong(2, (long) alertMessage.msgType.intValue());
                }
                if (alertMessage.id == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindLong(3, alertMessage.id.longValue());
                }
                if (alertMessage.title == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, alertMessage.title);
                }
                if (alertMessage.content == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, alertMessage.content);
                }
                if (alertMessage.action == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, alertMessage.action);
                }
                if (alertMessage.actionUrl == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, alertMessage.actionUrl);
                }
                if (alertMessage.read == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindLong(8, (long) alertMessage.read.intValue());
                }
                Long dateToTimestamp = RoomConverters.dateToTimestamp(alertMessage.createTime);
                if (dateToTimestamp == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindLong(9, dateToTimestamp.longValue());
                }
                if (alertMessage.expireDay == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindLong(10, (long) alertMessage.expireDay.intValue());
                }
                Long dateToTimestamp2 = RoomConverters.dateToTimestamp(alertMessage.readTime);
                if (dateToTimestamp2 == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindLong(11, dateToTimestamp2.longValue());
                }
                if (alertMessage.taobaoNumId == null) {
                    supportSQLiteStatement.bindNull(12);
                } else {
                    supportSQLiteStatement.bindLong(12, alertMessage.taobaoNumId.longValue());
                }
                if (alertMessage.msgType == null) {
                    supportSQLiteStatement.bindNull(13);
                } else {
                    supportSQLiteStatement.bindLong(13, (long) alertMessage.msgType.intValue());
                }
            }
        };
    }

    public void insert(AlertMessage alertMessage) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfAlertMessage.insert(alertMessage);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void insert(List<AlertMessage> list) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfAlertMessage.insert(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(AlertMessage alertMessage) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfAlertMessage.handle(alertMessage);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public LiveData<AlertMessage> getLastedUnreadMessageAsync(Long l) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("select * from alertMessage where taobaoNumId=? and read=1 and CAST(strftime('%s','now') AS INTEGER)<(createTime+expireDay*60*60*24) order by createTime desc limit 1", 1);
        if (l == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindLong(1, l.longValue());
        }
        return new ComputableLiveData<AlertMessage>(this.__db.getQueryExecutor()) {
            private InvalidationTracker.Observer _observer;

            /* access modifiers changed from: protected */
            public AlertMessage compute() {
                AlertMessage alertMessage;
                Long l;
                if (this._observer == null) {
                    this._observer = new InvalidationTracker.Observer("alertMessage", new String[0]) {
                        public void onInvalidated(@NonNull Set<String> set) {
                            AnonymousClass3.this.invalidate();
                        }
                    };
                    AlertMessageDao_Impl.this.__db.getInvalidationTracker().addWeakObserver(this._observer);
                }
                Cursor query = AlertMessageDao_Impl.this.__db.query(acquire);
                try {
                    int columnIndexOrThrow = query.getColumnIndexOrThrow("taobaoNumId");
                    int columnIndexOrThrow2 = query.getColumnIndexOrThrow(a.g);
                    int columnIndexOrThrow3 = query.getColumnIndexOrThrow("id");
                    int columnIndexOrThrow4 = query.getColumnIndexOrThrow("title");
                    int columnIndexOrThrow5 = query.getColumnIndexOrThrow("content");
                    int columnIndexOrThrow6 = query.getColumnIndexOrThrow("action");
                    int columnIndexOrThrow7 = query.getColumnIndexOrThrow("actionUrl");
                    int columnIndexOrThrow8 = query.getColumnIndexOrThrow(MonitorCacheEvent.OPERATION_READ);
                    int columnIndexOrThrow9 = query.getColumnIndexOrThrow("createTime");
                    int columnIndexOrThrow10 = query.getColumnIndexOrThrow("expireDay");
                    int columnIndexOrThrow11 = query.getColumnIndexOrThrow("readTime");
                    Long l2 = null;
                    if (query.moveToFirst()) {
                        alertMessage = new AlertMessage();
                        if (query.isNull(columnIndexOrThrow)) {
                            alertMessage.taobaoNumId = null;
                        } else {
                            alertMessage.taobaoNumId = Long.valueOf(query.getLong(columnIndexOrThrow));
                        }
                        if (query.isNull(columnIndexOrThrow2)) {
                            alertMessage.msgType = null;
                        } else {
                            alertMessage.msgType = Integer.valueOf(query.getInt(columnIndexOrThrow2));
                        }
                        if (query.isNull(columnIndexOrThrow3)) {
                            alertMessage.id = null;
                        } else {
                            alertMessage.id = Long.valueOf(query.getLong(columnIndexOrThrow3));
                        }
                        alertMessage.title = query.getString(columnIndexOrThrow4);
                        alertMessage.content = query.getString(columnIndexOrThrow5);
                        alertMessage.action = query.getString(columnIndexOrThrow6);
                        alertMessage.actionUrl = query.getString(columnIndexOrThrow7);
                        if (query.isNull(columnIndexOrThrow8)) {
                            alertMessage.read = null;
                        } else {
                            alertMessage.read = Integer.valueOf(query.getInt(columnIndexOrThrow8));
                        }
                        if (query.isNull(columnIndexOrThrow9)) {
                            l = null;
                        } else {
                            l = Long.valueOf(query.getLong(columnIndexOrThrow9));
                        }
                        alertMessage.createTime = RoomConverters.fromTimestamp(l);
                        if (query.isNull(columnIndexOrThrow10)) {
                            alertMessage.expireDay = null;
                        } else {
                            alertMessage.expireDay = Integer.valueOf(query.getInt(columnIndexOrThrow10));
                        }
                        if (!query.isNull(columnIndexOrThrow11)) {
                            l2 = Long.valueOf(query.getLong(columnIndexOrThrow11));
                        }
                        alertMessage.readTime = RoomConverters.fromTimestamp(l2);
                    } else {
                        alertMessage = null;
                    }
                    return alertMessage;
                } finally {
                    query.close();
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }

    public AlertMessage getLastedMessage(Long l, Integer num) {
        AlertMessage alertMessage;
        Long l2;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("select * from alertMessage where taobaoNumId=? and msgType=? and CAST(strftime('%s','now') AS INTEGER)<(createTime+expireDay*60*60*24) order by createTime desc limit 1", 2);
        if (l == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindLong(1, l.longValue());
        }
        if (num == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindLong(2, (long) num.intValue());
        }
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("taobaoNumId");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(a.g);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("title");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("content");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("action");
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow("actionUrl");
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(MonitorCacheEvent.OPERATION_READ);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow("createTime");
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow("expireDay");
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow("readTime");
            Long l3 = null;
            if (query.moveToFirst()) {
                alertMessage = new AlertMessage();
                if (query.isNull(columnIndexOrThrow)) {
                    alertMessage.taobaoNumId = null;
                } else {
                    alertMessage.taobaoNumId = Long.valueOf(query.getLong(columnIndexOrThrow));
                }
                if (query.isNull(columnIndexOrThrow2)) {
                    alertMessage.msgType = null;
                } else {
                    alertMessage.msgType = Integer.valueOf(query.getInt(columnIndexOrThrow2));
                }
                if (query.isNull(columnIndexOrThrow3)) {
                    alertMessage.id = null;
                } else {
                    alertMessage.id = Long.valueOf(query.getLong(columnIndexOrThrow3));
                }
                alertMessage.title = query.getString(columnIndexOrThrow4);
                alertMessage.content = query.getString(columnIndexOrThrow5);
                alertMessage.action = query.getString(columnIndexOrThrow6);
                alertMessage.actionUrl = query.getString(columnIndexOrThrow7);
                if (query.isNull(columnIndexOrThrow8)) {
                    alertMessage.read = null;
                } else {
                    alertMessage.read = Integer.valueOf(query.getInt(columnIndexOrThrow8));
                }
                if (query.isNull(columnIndexOrThrow9)) {
                    l2 = null;
                } else {
                    l2 = Long.valueOf(query.getLong(columnIndexOrThrow9));
                }
                alertMessage.createTime = RoomConverters.fromTimestamp(l2);
                if (query.isNull(columnIndexOrThrow10)) {
                    alertMessage.expireDay = null;
                } else {
                    alertMessage.expireDay = Integer.valueOf(query.getInt(columnIndexOrThrow10));
                }
                if (!query.isNull(columnIndexOrThrow11)) {
                    l3 = Long.valueOf(query.getLong(columnIndexOrThrow11));
                }
                alertMessage.readTime = RoomConverters.fromTimestamp(l3);
            } else {
                alertMessage = null;
            }
            return alertMessage;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public AlertMessage getMessageById(Long l, Integer num, Long l2) {
        AlertMessage alertMessage;
        Long l3;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("select * from alertMessage where taobaoNumId=? and msgType=? and id=? order by createTime desc limit 1", 3);
        if (l == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindLong(1, l.longValue());
        }
        if (num == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindLong(2, (long) num.intValue());
        }
        if (l2 == null) {
            acquire.bindNull(3);
        } else {
            acquire.bindLong(3, l2.longValue());
        }
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("taobaoNumId");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(a.g);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("title");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("content");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("action");
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow("actionUrl");
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(MonitorCacheEvent.OPERATION_READ);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow("createTime");
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow("expireDay");
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow("readTime");
            Long l4 = null;
            if (query.moveToFirst()) {
                alertMessage = new AlertMessage();
                if (query.isNull(columnIndexOrThrow)) {
                    alertMessage.taobaoNumId = null;
                } else {
                    alertMessage.taobaoNumId = Long.valueOf(query.getLong(columnIndexOrThrow));
                }
                if (query.isNull(columnIndexOrThrow2)) {
                    alertMessage.msgType = null;
                } else {
                    alertMessage.msgType = Integer.valueOf(query.getInt(columnIndexOrThrow2));
                }
                if (query.isNull(columnIndexOrThrow3)) {
                    alertMessage.id = null;
                } else {
                    alertMessage.id = Long.valueOf(query.getLong(columnIndexOrThrow3));
                }
                alertMessage.title = query.getString(columnIndexOrThrow4);
                alertMessage.content = query.getString(columnIndexOrThrow5);
                alertMessage.action = query.getString(columnIndexOrThrow6);
                alertMessage.actionUrl = query.getString(columnIndexOrThrow7);
                if (query.isNull(columnIndexOrThrow8)) {
                    alertMessage.read = null;
                } else {
                    alertMessage.read = Integer.valueOf(query.getInt(columnIndexOrThrow8));
                }
                if (query.isNull(columnIndexOrThrow9)) {
                    l3 = null;
                } else {
                    l3 = Long.valueOf(query.getLong(columnIndexOrThrow9));
                }
                alertMessage.createTime = RoomConverters.fromTimestamp(l3);
                if (query.isNull(columnIndexOrThrow10)) {
                    alertMessage.expireDay = null;
                } else {
                    alertMessage.expireDay = Integer.valueOf(query.getInt(columnIndexOrThrow10));
                }
                if (!query.isNull(columnIndexOrThrow11)) {
                    l4 = Long.valueOf(query.getLong(columnIndexOrThrow11));
                }
                alertMessage.readTime = RoomConverters.fromTimestamp(l4);
            } else {
                alertMessage = null;
            }
            return alertMessage;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
