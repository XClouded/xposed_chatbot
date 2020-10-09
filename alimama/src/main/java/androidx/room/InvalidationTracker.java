package androidx.room;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.WorkerThread;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import anet.channel.request.Request;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public class InvalidationTracker {
    @VisibleForTesting
    static final String CLEANUP_SQL = "DELETE FROM room_table_modification_log WHERE version NOT IN( SELECT MAX(version) FROM room_table_modification_log GROUP BY table_id)";
    private static final String CREATE_VERSION_TABLE_SQL = "CREATE TEMP TABLE room_table_modification_log(version INTEGER PRIMARY KEY AUTOINCREMENT, table_id INTEGER)";
    @VisibleForTesting
    static final String SELECT_UPDATED_TABLES_SQL = "SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;";
    private static final String TABLE_ID_COLUMN_NAME = "table_id";
    private static final String[] TRIGGERS = {"UPDATE", Request.Method.DELETE, "INSERT"};
    private static final String UPDATE_TABLE_NAME = "room_table_modification_log";
    private static final String VERSION_COLUMN_NAME = "version";
    volatile SupportSQLiteStatement mCleanupStatement;
    final RoomDatabase mDatabase;
    private volatile boolean mInitialized;
    long mMaxVersion = 0;
    private ObservedTableTracker mObservedTableTracker;
    @VisibleForTesting
    final SafeIterableMap<Observer, ObserverWrapper> mObserverMap;
    AtomicBoolean mPendingRefresh;
    Object[] mQueryArgs = new Object[1];
    @VisibleForTesting
    Runnable mRefreshRunnable;
    @VisibleForTesting
    @NonNull
    ArrayMap<String, Integer> mTableIdLookup;
    private String[] mTableNames;
    @VisibleForTesting
    @NonNull
    long[] mTableVersions;

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public InvalidationTracker(RoomDatabase roomDatabase, String... strArr) {
        this.mPendingRefresh = new AtomicBoolean(false);
        this.mInitialized = false;
        this.mObserverMap = new SafeIterableMap<>();
        this.mRefreshRunnable = new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:46:0x0091  */
            /* JADX WARNING: Removed duplicated region for block: B:61:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r6 = this;
                    androidx.room.InvalidationTracker r0 = androidx.room.InvalidationTracker.this
                    androidx.room.RoomDatabase r0 = r0.mDatabase
                    java.util.concurrent.locks.Lock r0 = r0.getCloseLock()
                    r1 = 0
                    r0.lock()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    boolean r2 = r2.ensureInitialization()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    if (r2 != 0) goto L_0x0018
                    r0.unlock()
                    return
                L_0x0018:
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    java.util.concurrent.atomic.AtomicBoolean r2 = r2.mPendingRefresh     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    r3 = 1
                    boolean r2 = r2.compareAndSet(r3, r1)     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    if (r2 != 0) goto L_0x0027
                    r0.unlock()
                    return
                L_0x0027:
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.RoomDatabase r2 = r2.mDatabase     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    boolean r2 = r2.inTransaction()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    if (r2 == 0) goto L_0x0035
                    r0.unlock()
                    return
                L_0x0035:
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.sqlite.db.SupportSQLiteStatement r2 = r2.mCleanupStatement     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    r2.executeUpdateDelete()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    java.lang.Object[] r2 = r2.mQueryArgs     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.InvalidationTracker r3 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    long r3 = r3.mMaxVersion     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    r2[r1] = r3     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.RoomDatabase r2 = r2.mDatabase     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    boolean r2 = r2.mWriteAheadLoggingEnabled     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    if (r2 == 0) goto L_0x007c
                    androidx.room.InvalidationTracker r2 = androidx.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.room.RoomDatabase r2 = r2.mDatabase     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.sqlite.db.SupportSQLiteOpenHelper r2 = r2.getOpenHelper()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    androidx.sqlite.db.SupportSQLiteDatabase r2 = r2.getWritableDatabase()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    r2.beginTransaction()     // Catch:{ all -> 0x0074 }
                    boolean r3 = r6.checkUpdatedTable()     // Catch:{ all -> 0x0074 }
                    r2.setTransactionSuccessful()     // Catch:{ all -> 0x006f }
                    r2.endTransaction()     // Catch:{ SQLiteException | IllegalStateException -> 0x006c }
                    goto L_0x008c
                L_0x006c:
                    r1 = move-exception
                    r2 = r1
                    goto L_0x0085
                L_0x006f:
                    r1 = move-exception
                    r5 = r3
                    r3 = r1
                    r1 = r5
                    goto L_0x0075
                L_0x0074:
                    r3 = move-exception
                L_0x0075:
                    r2.endTransaction()     // Catch:{ SQLiteException | IllegalStateException -> 0x0079 }
                    throw r3     // Catch:{ SQLiteException | IllegalStateException -> 0x0079 }
                L_0x0079:
                    r2 = move-exception
                    r3 = r1
                    goto L_0x0085
                L_0x007c:
                    boolean r3 = r6.checkUpdatedTable()     // Catch:{ SQLiteException | IllegalStateException -> 0x0083 }
                    goto L_0x008c
                L_0x0081:
                    r1 = move-exception
                    goto L_0x00be
                L_0x0083:
                    r2 = move-exception
                    r3 = 0
                L_0x0085:
                    java.lang.String r1 = "ROOM"
                    java.lang.String r4 = "Cannot run invalidation tracker. Is the db closed?"
                    android.util.Log.e(r1, r4, r2)     // Catch:{ all -> 0x0081 }
                L_0x008c:
                    r0.unlock()
                    if (r3 == 0) goto L_0x00bd
                    androidx.room.InvalidationTracker r0 = androidx.room.InvalidationTracker.this
                    androidx.arch.core.internal.SafeIterableMap<androidx.room.InvalidationTracker$Observer, androidx.room.InvalidationTracker$ObserverWrapper> r0 = r0.mObserverMap
                    monitor-enter(r0)
                    androidx.room.InvalidationTracker r1 = androidx.room.InvalidationTracker.this     // Catch:{ all -> 0x00ba }
                    androidx.arch.core.internal.SafeIterableMap<androidx.room.InvalidationTracker$Observer, androidx.room.InvalidationTracker$ObserverWrapper> r1 = r1.mObserverMap     // Catch:{ all -> 0x00ba }
                    java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x00ba }
                L_0x009e:
                    boolean r2 = r1.hasNext()     // Catch:{ all -> 0x00ba }
                    if (r2 == 0) goto L_0x00b8
                    java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x00ba }
                    java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x00ba }
                    java.lang.Object r2 = r2.getValue()     // Catch:{ all -> 0x00ba }
                    androidx.room.InvalidationTracker$ObserverWrapper r2 = (androidx.room.InvalidationTracker.ObserverWrapper) r2     // Catch:{ all -> 0x00ba }
                    androidx.room.InvalidationTracker r3 = androidx.room.InvalidationTracker.this     // Catch:{ all -> 0x00ba }
                    long[] r3 = r3.mTableVersions     // Catch:{ all -> 0x00ba }
                    r2.checkForInvalidation(r3)     // Catch:{ all -> 0x00ba }
                    goto L_0x009e
                L_0x00b8:
                    monitor-exit(r0)     // Catch:{ all -> 0x00ba }
                    goto L_0x00bd
                L_0x00ba:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x00ba }
                    throw r1
                L_0x00bd:
                    return
                L_0x00be:
                    r0.unlock()
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.room.InvalidationTracker.AnonymousClass1.run():void");
            }

            private boolean checkUpdatedTable() {
                Cursor query = InvalidationTracker.this.mDatabase.query(InvalidationTracker.SELECT_UPDATED_TABLES_SQL, InvalidationTracker.this.mQueryArgs);
                boolean z = false;
                while (query.moveToNext()) {
                    try {
                        long j = query.getLong(0);
                        InvalidationTracker.this.mTableVersions[query.getInt(1)] = j;
                        InvalidationTracker.this.mMaxVersion = j;
                        z = true;
                    } finally {
                        query.close();
                    }
                }
                return z;
            }
        };
        this.mDatabase = roomDatabase;
        this.mObservedTableTracker = new ObservedTableTracker(strArr.length);
        this.mTableIdLookup = new ArrayMap<>();
        int length = strArr.length;
        this.mTableNames = new String[length];
        for (int i = 0; i < length; i++) {
            String lowerCase = strArr[i].toLowerCase(Locale.US);
            this.mTableIdLookup.put(lowerCase, Integer.valueOf(i));
            this.mTableNames[i] = lowerCase;
        }
        this.mTableVersions = new long[strArr.length];
        Arrays.fill(this.mTableVersions, 0);
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void internalInit(SupportSQLiteDatabase supportSQLiteDatabase) {
        synchronized (this) {
            if (this.mInitialized) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            supportSQLiteDatabase.beginTransaction();
            try {
                supportSQLiteDatabase.execSQL("PRAGMA temp_store = MEMORY;");
                supportSQLiteDatabase.execSQL("PRAGMA recursive_triggers='ON';");
                supportSQLiteDatabase.execSQL(CREATE_VERSION_TABLE_SQL);
                supportSQLiteDatabase.setTransactionSuccessful();
                supportSQLiteDatabase.endTransaction();
                syncTriggers(supportSQLiteDatabase);
                this.mCleanupStatement = supportSQLiteDatabase.compileStatement(CLEANUP_SQL);
                this.mInitialized = true;
            } catch (Throwable th) {
                supportSQLiteDatabase.endTransaction();
                throw th;
            }
        }
    }

    private static void appendTriggerName(StringBuilder sb, String str, String str2) {
        sb.append("`");
        sb.append("room_table_modification_trigger_");
        sb.append(str);
        sb.append("_");
        sb.append(str2);
        sb.append("`");
    }

    private void stopTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String str = this.mTableNames[i];
        StringBuilder sb = new StringBuilder();
        for (String appendTriggerName : TRIGGERS) {
            sb.setLength(0);
            sb.append("DROP TRIGGER IF EXISTS ");
            appendTriggerName(sb, str, appendTriggerName);
            supportSQLiteDatabase.execSQL(sb.toString());
        }
    }

    private void startTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String str = this.mTableNames[i];
        StringBuilder sb = new StringBuilder();
        for (String str2 : TRIGGERS) {
            sb.setLength(0);
            sb.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            appendTriggerName(sb, str, str2);
            sb.append(" AFTER ");
            sb.append(str2);
            sb.append(" ON `");
            sb.append(str);
            sb.append("` BEGIN INSERT OR REPLACE INTO ");
            sb.append(UPDATE_TABLE_NAME);
            sb.append(" VALUES(null, ");
            sb.append(i);
            sb.append("); END");
            supportSQLiteDatabase.execSQL(sb.toString());
        }
    }

    @WorkerThread
    public void addObserver(@NonNull Observer observer) {
        ObserverWrapper putIfAbsent;
        String[] strArr = observer.mTables;
        int[] iArr = new int[strArr.length];
        int length = strArr.length;
        long[] jArr = new long[strArr.length];
        int i = 0;
        while (i < length) {
            Integer num = this.mTableIdLookup.get(strArr[i].toLowerCase(Locale.US));
            if (num != null) {
                iArr[i] = num.intValue();
                jArr[i] = this.mMaxVersion;
                i++;
            } else {
                throw new IllegalArgumentException("There is no table with name " + strArr[i]);
            }
        }
        ObserverWrapper observerWrapper = new ObserverWrapper(observer, iArr, strArr, jArr);
        synchronized (this.mObserverMap) {
            putIfAbsent = this.mObserverMap.putIfAbsent(observer, observerWrapper);
        }
        if (putIfAbsent == null && this.mObservedTableTracker.onAdded(iArr)) {
            syncTriggers();
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void addWeakObserver(Observer observer) {
        addObserver(new WeakObserver(this, observer));
    }

    @WorkerThread
    public void removeObserver(@NonNull Observer observer) {
        ObserverWrapper remove;
        synchronized (this.mObserverMap) {
            remove = this.mObserverMap.remove(observer);
        }
        if (remove != null && this.mObservedTableTracker.onRemoved(remove.mTableIds)) {
            syncTriggers();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean ensureInitialization() {
        if (!this.mDatabase.isOpen()) {
            return false;
        }
        if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
        }
        if (this.mInitialized) {
            return true;
        }
        Log.e("ROOM", "database is not initialized even though it is open");
        return false;
    }

    public void refreshVersionsAsync() {
        if (this.mPendingRefresh.compareAndSet(false, true)) {
            this.mDatabase.getQueryExecutor().execute(this.mRefreshRunnable);
        }
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void refreshVersionsSync() {
        syncTriggers();
        this.mRefreshRunnable.run();
    }

    /* access modifiers changed from: package-private */
    public void syncTriggers(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (!supportSQLiteDatabase.inTransaction()) {
            while (true) {
                try {
                    Lock closeLock = this.mDatabase.getCloseLock();
                    closeLock.lock();
                    try {
                        int[] tablesToSync = this.mObservedTableTracker.getTablesToSync();
                        if (tablesToSync == null) {
                            closeLock.unlock();
                            return;
                        }
                        int length = tablesToSync.length;
                        supportSQLiteDatabase.beginTransaction();
                        for (int i = 0; i < length; i++) {
                            switch (tablesToSync[i]) {
                                case 1:
                                    startTrackingTable(supportSQLiteDatabase, i);
                                    break;
                                case 2:
                                    stopTrackingTable(supportSQLiteDatabase, i);
                                    break;
                            }
                        }
                        supportSQLiteDatabase.setTransactionSuccessful();
                        supportSQLiteDatabase.endTransaction();
                        this.mObservedTableTracker.onSyncCompleted();
                        closeLock.unlock();
                    } catch (Throwable th) {
                        closeLock.unlock();
                        throw th;
                    }
                } catch (SQLiteException | IllegalStateException e) {
                    Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void syncTriggers() {
        if (this.mDatabase.isOpen()) {
            syncTriggers(this.mDatabase.getOpenHelper().getWritableDatabase());
        }
    }

    static class ObserverWrapper {
        final Observer mObserver;
        private final Set<String> mSingleTableSet;
        final int[] mTableIds;
        private final String[] mTableNames;
        private final long[] mVersions;

        ObserverWrapper(Observer observer, int[] iArr, String[] strArr, long[] jArr) {
            this.mObserver = observer;
            this.mTableIds = iArr;
            this.mTableNames = strArr;
            this.mVersions = jArr;
            if (iArr.length == 1) {
                ArraySet arraySet = new ArraySet();
                arraySet.add(this.mTableNames[0]);
                this.mSingleTableSet = Collections.unmodifiableSet(arraySet);
                return;
            }
            this.mSingleTableSet = null;
        }

        /* access modifiers changed from: package-private */
        public void checkForInvalidation(long[] jArr) {
            int length = this.mTableIds.length;
            Set set = null;
            for (int i = 0; i < length; i++) {
                long j = jArr[this.mTableIds[i]];
                if (this.mVersions[i] < j) {
                    this.mVersions[i] = j;
                    if (length == 1) {
                        set = this.mSingleTableSet;
                    } else {
                        if (set == null) {
                            set = new ArraySet(length);
                        }
                        set.add(this.mTableNames[i]);
                    }
                }
            }
            if (set != null) {
                this.mObserver.onInvalidated(set);
            }
        }
    }

    public static abstract class Observer {
        final String[] mTables;

        public abstract void onInvalidated(@NonNull Set<String> set);

        protected Observer(@NonNull String str, String... strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
            this.mTables[strArr.length] = str;
        }

        public Observer(@NonNull String[] strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length);
        }
    }

    static class ObservedTableTracker {
        static final int ADD = 1;
        static final int NO_OP = 0;
        static final int REMOVE = 2;
        boolean mNeedsSync;
        boolean mPendingSync;
        final long[] mTableObservers;
        final int[] mTriggerStateChanges;
        final boolean[] mTriggerStates;

        ObservedTableTracker(int i) {
            this.mTableObservers = new long[i];
            this.mTriggerStates = new boolean[i];
            this.mTriggerStateChanges = new int[i];
            Arrays.fill(this.mTableObservers, 0);
            Arrays.fill(this.mTriggerStates, false);
        }

        /* access modifiers changed from: package-private */
        public boolean onAdded(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = 1 + j;
                    if (j == 0) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public boolean onRemoved(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = j - 1;
                    if (j == 1) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        @Nullable
        public int[] getTablesToSync() {
            synchronized (this) {
                if (this.mNeedsSync) {
                    if (!this.mPendingSync) {
                        int length = this.mTableObservers.length;
                        int i = 0;
                        while (true) {
                            int i2 = 1;
                            if (i < length) {
                                boolean z = this.mTableObservers[i] > 0;
                                if (z != this.mTriggerStates[i]) {
                                    int[] iArr = this.mTriggerStateChanges;
                                    if (!z) {
                                        i2 = 2;
                                    }
                                    iArr[i] = i2;
                                } else {
                                    this.mTriggerStateChanges[i] = 0;
                                }
                                this.mTriggerStates[i] = z;
                                i++;
                            } else {
                                this.mPendingSync = true;
                                this.mNeedsSync = false;
                                int[] iArr2 = this.mTriggerStateChanges;
                                return iArr2;
                            }
                        }
                    }
                }
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public void onSyncCompleted() {
            synchronized (this) {
                this.mPendingSync = false;
            }
        }
    }

    static class WeakObserver extends Observer {
        final WeakReference<Observer> mDelegateRef;
        final InvalidationTracker mTracker;

        WeakObserver(InvalidationTracker invalidationTracker, Observer observer) {
            super(observer.mTables);
            this.mTracker = invalidationTracker;
            this.mDelegateRef = new WeakReference<>(observer);
        }

        public void onInvalidated(@NonNull Set<String> set) {
            Observer observer = (Observer) this.mDelegateRef.get();
            if (observer == null) {
                this.mTracker.removeObserver(this);
            } else {
                observer.onInvalidated(set);
            }
        }
    }
}
