package androidx.room.paging;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.paging.PositionalDataSource;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class LimitOffsetDataSource<T> extends PositionalDataSource<T> {
    private final String mCountQuery;
    private final RoomDatabase mDb;
    private final boolean mInTransaction;
    private final String mLimitOffsetQuery;
    private final InvalidationTracker.Observer mObserver;
    private final RoomSQLiteQuery mSourceQuery;

    /* access modifiers changed from: protected */
    public abstract List<T> convertRows(Cursor cursor);

    protected LimitOffsetDataSource(RoomDatabase roomDatabase, SupportSQLiteQuery supportSQLiteQuery, boolean z, String... strArr) {
        this(roomDatabase, RoomSQLiteQuery.copyFrom(supportSQLiteQuery), z, strArr);
    }

    protected LimitOffsetDataSource(RoomDatabase roomDatabase, RoomSQLiteQuery roomSQLiteQuery, boolean z, String... strArr) {
        this.mDb = roomDatabase;
        this.mSourceQuery = roomSQLiteQuery;
        this.mInTransaction = z;
        this.mCountQuery = "SELECT COUNT(*) FROM ( " + this.mSourceQuery.getSql() + " )";
        this.mLimitOffsetQuery = "SELECT * FROM ( " + this.mSourceQuery.getSql() + " ) LIMIT ? OFFSET ?";
        this.mObserver = new InvalidationTracker.Observer(strArr) {
            public void onInvalidated(@NonNull Set<String> set) {
                LimitOffsetDataSource.this.invalidate();
            }
        };
        roomDatabase.getInvalidationTracker().addWeakObserver(this.mObserver);
    }

    public int countItems() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(this.mCountQuery, this.mSourceQuery.getArgCount());
        acquire.copyArgumentsFrom(this.mSourceQuery);
        Cursor query = this.mDb.query(acquire);
        try {
            if (query.moveToFirst()) {
                return query.getInt(0);
            }
            query.close();
            acquire.release();
            return 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isInvalid() {
        this.mDb.getInvalidationTracker().refreshVersionsSync();
        return LimitOffsetDataSource.super.isInvalid();
    }

    public void loadInitial(@NonNull PositionalDataSource.LoadInitialParams loadInitialParams, @NonNull PositionalDataSource.LoadInitialCallback<T> loadInitialCallback) {
        int countItems = countItems();
        if (countItems == 0) {
            loadInitialCallback.onResult(Collections.emptyList(), 0, 0);
            return;
        }
        int computeInitialLoadPosition = computeInitialLoadPosition(loadInitialParams, countItems);
        int computeInitialLoadSize = computeInitialLoadSize(loadInitialParams, computeInitialLoadPosition, countItems);
        List loadRange = loadRange(computeInitialLoadPosition, computeInitialLoadSize);
        if (loadRange == null || loadRange.size() != computeInitialLoadSize) {
            invalidate();
        } else {
            loadInitialCallback.onResult(loadRange, computeInitialLoadPosition, countItems);
        }
    }

    public void loadRange(@NonNull PositionalDataSource.LoadRangeParams loadRangeParams, @NonNull PositionalDataSource.LoadRangeCallback<T> loadRangeCallback) {
        List loadRange = loadRange(loadRangeParams.startPosition, loadRangeParams.loadSize);
        if (loadRange != null) {
            loadRangeCallback.onResult(loadRange);
        } else {
            invalidate();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0054  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<T> loadRange(int r6, int r7) {
        /*
            r5 = this;
            java.lang.String r0 = r5.mLimitOffsetQuery
            androidx.room.RoomSQLiteQuery r1 = r5.mSourceQuery
            int r1 = r1.getArgCount()
            int r1 = r1 + 2
            androidx.room.RoomSQLiteQuery r0 = androidx.room.RoomSQLiteQuery.acquire(r0, r1)
            androidx.room.RoomSQLiteQuery r1 = r5.mSourceQuery
            r0.copyArgumentsFrom(r1)
            int r1 = r0.getArgCount()
            int r1 = r1 + -1
            long r2 = (long) r7
            r0.bindLong(r1, r2)
            int r7 = r0.getArgCount()
            long r1 = (long) r6
            r0.bindLong(r7, r1)
            boolean r6 = r5.mInTransaction
            if (r6 == 0) goto L_0x0060
            androidx.room.RoomDatabase r6 = r5.mDb
            r6.beginTransaction()
            r6 = 0
            androidx.room.RoomDatabase r7 = r5.mDb     // Catch:{ all -> 0x004e }
            android.database.Cursor r7 = r7.query(r0)     // Catch:{ all -> 0x004e }
            java.util.List r6 = r5.convertRows(r7)     // Catch:{ all -> 0x004c }
            androidx.room.RoomDatabase r1 = r5.mDb     // Catch:{ all -> 0x004c }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x004c }
            if (r7 == 0) goto L_0x0043
            r7.close()
        L_0x0043:
            androidx.room.RoomDatabase r7 = r5.mDb
            r7.endTransaction()
            r0.release()
            return r6
        L_0x004c:
            r6 = move-exception
            goto L_0x0052
        L_0x004e:
            r7 = move-exception
            r4 = r7
            r7 = r6
            r6 = r4
        L_0x0052:
            if (r7 == 0) goto L_0x0057
            r7.close()
        L_0x0057:
            androidx.room.RoomDatabase r7 = r5.mDb
            r7.endTransaction()
            r0.release()
            throw r6
        L_0x0060:
            androidx.room.RoomDatabase r6 = r5.mDb
            android.database.Cursor r6 = r6.query(r0)
            java.util.List r7 = r5.convertRows(r6)     // Catch:{ all -> 0x0071 }
            r6.close()
            r0.release()
            return r7
        L_0x0071:
            r7 = move-exception
            r6.close()
            r0.release()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.room.paging.LimitOffsetDataSource.loadRange(int, int):java.util.List");
    }
}
