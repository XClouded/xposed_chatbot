package com.facebook.imagepipeline.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import bolts.Task;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.logging.FLog;
import com.facebook.common.time.Clock;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.MediaVariations;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

public class MediaVariationsIndexDatabase implements MediaVariationsIndex {
    private static final long MILLIS_IN_FIVE_DAYS = TimeUnit.DAYS.toMillis(5);
    private static final long MILLIS_IN_ONE_DAY = TimeUnit.DAYS.toMillis(1);
    private static final String[] PROJECTION = {IndexEntry.COLUMN_NAME_CACHE_CHOICE, IndexEntry.COLUMN_NAME_CACHE_KEY, "width", "height"};
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS media_variations_index";
    private static final String TAG = "MediaVariationsIndexDatabase";
    private static final String WHERE_CLAUSE_DATE_BEFORE = "date < ?";
    private final Clock mClock;
    @GuardedBy("MediaVariationsIndexDatabase.class")
    private final LazyIndexDbOpenHelper mDbHelper;
    private long mLastTrimTimestamp;
    private final Executor mReadExecutor;
    private final Executor mWriteExecutor;

    public MediaVariationsIndexDatabase(Context context, Executor executor, Executor executor2, Clock clock) {
        this.mDbHelper = new LazyIndexDbOpenHelper(context);
        this.mReadExecutor = executor;
        this.mWriteExecutor = executor2;
        this.mClock = clock;
    }

    public Task<MediaVariations> getCachedVariants(final String str, final MediaVariations.Builder builder) {
        try {
            return Task.call(new Callable<MediaVariations>() {
                public MediaVariations call() throws Exception {
                    return MediaVariationsIndexDatabase.this.getCachedVariantsSync(str, builder);
                }
            }, this.mReadExecutor);
        } catch (Exception e) {
            FLog.w(TAG, (Throwable) e, "Failed to schedule query task for %s", str);
            return Task.forError(e);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0082, code lost:
        return r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x009a A[SYNTHETIC, Splitter:B:43:0x009a] */
    @com.facebook.common.internal.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.facebook.imagepipeline.request.MediaVariations getCachedVariantsSync(java.lang.String r16, com.facebook.imagepipeline.request.MediaVariations.Builder r17) {
        /*
            r15 = this;
            java.lang.Class<com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase> r1 = com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase.class
            monitor-enter(r1)
            r2 = r15
            com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase$LazyIndexDbOpenHelper r0 = r2.mDbHelper     // Catch:{ all -> 0x009e }
            android.database.sqlite.SQLiteDatabase r3 = r0.getWritableDatabase()     // Catch:{ all -> 0x009e }
            r11 = 0
            r12 = 1
            r13 = 0
            java.lang.String r6 = "media_id = ?"
            java.lang.String[] r7 = new java.lang.String[r12]     // Catch:{ SQLException -> 0x008b }
            r7[r11] = r16     // Catch:{ SQLException -> 0x008b }
            java.lang.String r4 = "media_variations_index"
            java.lang.String[] r5 = PROJECTION     // Catch:{ SQLException -> 0x008b }
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r3 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLException -> 0x008b }
            int r0 = r3.getCount()     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            if (r0 != 0) goto L_0x002f
            com.facebook.imagepipeline.request.MediaVariations r0 = r17.build()     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            if (r3 == 0) goto L_0x002d
            r3.close()     // Catch:{ all -> 0x009e }
        L_0x002d:
            monitor-exit(r1)     // Catch:{ all -> 0x009e }
            return r0
        L_0x002f:
            java.lang.String r0 = "cache_key"
            int r0 = r3.getColumnIndexOrThrow(r0)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            java.lang.String r4 = "width"
            int r4 = r3.getColumnIndexOrThrow(r4)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            java.lang.String r5 = "height"
            int r5 = r3.getColumnIndexOrThrow(r5)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            java.lang.String r6 = "cache_choice"
            int r6 = r3.getColumnIndexOrThrow(r6)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
        L_0x0047:
            boolean r7 = r3.moveToNext()     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            if (r7 == 0) goto L_0x0076
            java.lang.String r7 = r3.getString(r6)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            java.lang.String r8 = r3.getString(r0)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            android.net.Uri r8 = android.net.Uri.parse(r8)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            int r9 = r3.getInt(r4)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            int r10 = r3.getInt(r5)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            boolean r14 = android.text.TextUtils.isEmpty(r7)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            if (r14 == 0) goto L_0x006b
            r7 = r17
            r14 = r13
            goto L_0x0072
        L_0x006b:
            com.facebook.imagepipeline.request.ImageRequest$CacheChoice r7 = com.facebook.imagepipeline.request.ImageRequest.CacheChoice.valueOf(r7)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            r14 = r7
            r7 = r17
        L_0x0072:
            r7.addVariant(r8, r9, r10, r14)     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            goto L_0x0047
        L_0x0076:
            r7 = r17
            com.facebook.imagepipeline.request.MediaVariations r0 = r17.build()     // Catch:{ SQLException -> 0x0085, all -> 0x0083 }
            if (r3 == 0) goto L_0x0081
            r3.close()     // Catch:{ all -> 0x009e }
        L_0x0081:
            monitor-exit(r1)     // Catch:{ all -> 0x009e }
            return r0
        L_0x0083:
            r0 = move-exception
            goto L_0x0098
        L_0x0085:
            r0 = move-exception
            r13 = r3
            goto L_0x008c
        L_0x0088:
            r0 = move-exception
            r3 = r13
            goto L_0x0098
        L_0x008b:
            r0 = move-exception
        L_0x008c:
            java.lang.String r3 = TAG     // Catch:{ all -> 0x0088 }
            java.lang.String r4 = "Error reading for %s"
            java.lang.Object[] r5 = new java.lang.Object[r12]     // Catch:{ all -> 0x0088 }
            r5[r11] = r16     // Catch:{ all -> 0x0088 }
            com.facebook.common.logging.FLog.e((java.lang.String) r3, (java.lang.Throwable) r0, (java.lang.String) r4, (java.lang.Object[]) r5)     // Catch:{ all -> 0x0088 }
            throw r0     // Catch:{ all -> 0x0088 }
        L_0x0098:
            if (r3 == 0) goto L_0x009d
            r3.close()     // Catch:{ all -> 0x009e }
        L_0x009d:
            throw r0     // Catch:{ all -> 0x009e }
        L_0x009e:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x009e }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase.getCachedVariantsSync(java.lang.String, com.facebook.imagepipeline.request.MediaVariations$Builder):com.facebook.imagepipeline.request.MediaVariations");
    }

    public void saveCachedVariant(String str, ImageRequest.CacheChoice cacheChoice, CacheKey cacheKey, EncodedImage encodedImage) {
        final String str2 = str;
        final ImageRequest.CacheChoice cacheChoice2 = cacheChoice;
        final CacheKey cacheKey2 = cacheKey;
        final EncodedImage encodedImage2 = encodedImage;
        this.mWriteExecutor.execute(new Runnable() {
            public void run() {
                MediaVariationsIndexDatabase.this.saveCachedVariantSync(str2, cacheChoice2, cacheKey2, encodedImage2);
            }
        });
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(11:2|3|4|5|6|(1:8)|9|10|11|16|17) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:12|19|20|21|22) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0099 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x009e */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x0099=Splitter:B:16:0x0099, B:21:0x009e=Splitter:B:21:0x009e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveCachedVariantSync(java.lang.String r10, com.facebook.imagepipeline.request.ImageRequest.CacheChoice r11, com.facebook.cache.common.CacheKey r12, com.facebook.imagepipeline.image.EncodedImage r13) {
        /*
            r9 = this;
            java.lang.Class<com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase> r0 = com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase.class
            monitor-enter(r0)
            com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase$LazyIndexDbOpenHelper r1 = r9.mDbHelper     // Catch:{ all -> 0x009f }
            android.database.sqlite.SQLiteDatabase r1 = r1.getWritableDatabase()     // Catch:{ all -> 0x009f }
            com.facebook.common.time.Clock r2 = r9.mClock     // Catch:{ all -> 0x009f }
            long r2 = r2.now()     // Catch:{ all -> 0x009f }
            r4 = 0
            r5 = 1
            r1.beginTransaction()     // Catch:{ Exception -> 0x008c }
            android.content.ContentValues r6 = new android.content.ContentValues     // Catch:{ Exception -> 0x008c }
            r6.<init>()     // Catch:{ Exception -> 0x008c }
            java.lang.String r7 = "media_id"
            r6.put(r7, r10)     // Catch:{ Exception -> 0x008c }
            java.lang.String r7 = "width"
            int r8 = r13.getWidth()     // Catch:{ Exception -> 0x008c }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Exception -> 0x008c }
            r6.put(r7, r8)     // Catch:{ Exception -> 0x008c }
            java.lang.String r7 = "height"
            int r13 = r13.getHeight()     // Catch:{ Exception -> 0x008c }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch:{ Exception -> 0x008c }
            r6.put(r7, r13)     // Catch:{ Exception -> 0x008c }
            java.lang.String r13 = "cache_choice"
            java.lang.String r11 = r11.name()     // Catch:{ Exception -> 0x008c }
            r6.put(r13, r11)     // Catch:{ Exception -> 0x008c }
            java.lang.String r11 = "cache_key"
            java.lang.String r13 = r12.getUriString()     // Catch:{ Exception -> 0x008c }
            r6.put(r11, r13)     // Catch:{ Exception -> 0x008c }
            java.lang.String r11 = "resource_id"
            java.lang.String r12 = com.facebook.cache.common.CacheKeyUtil.getFirstResourceId(r12)     // Catch:{ Exception -> 0x008c }
            r6.put(r11, r12)     // Catch:{ Exception -> 0x008c }
            java.lang.String r11 = "date"
            java.lang.Long r12 = java.lang.Long.valueOf(r2)     // Catch:{ Exception -> 0x008c }
            r6.put(r11, r12)     // Catch:{ Exception -> 0x008c }
            java.lang.String r11 = "media_variations_index"
            r12 = 0
            r1.replaceOrThrow(r11, r12, r6)     // Catch:{ Exception -> 0x008c }
            long r11 = r9.mLastTrimTimestamp     // Catch:{ Exception -> 0x008c }
            long r6 = MILLIS_IN_ONE_DAY     // Catch:{ Exception -> 0x008c }
            r13 = 0
            long r6 = r2 - r6
            int r13 = (r11 > r6 ? 1 : (r11 == r6 ? 0 : -1))
            if (r13 > 0) goto L_0x0083
            java.lang.String r11 = "media_variations_index"
            java.lang.String r12 = "date < ?"
            java.lang.String[] r13 = new java.lang.String[r5]     // Catch:{ Exception -> 0x008c }
            long r6 = MILLIS_IN_FIVE_DAYS     // Catch:{ Exception -> 0x008c }
            r8 = 0
            long r6 = r2 - r6
            java.lang.String r6 = java.lang.Long.toString(r6)     // Catch:{ Exception -> 0x008c }
            r13[r4] = r6     // Catch:{ Exception -> 0x008c }
            r1.delete(r11, r12, r13)     // Catch:{ Exception -> 0x008c }
            r9.mLastTrimTimestamp = r2     // Catch:{ Exception -> 0x008c }
        L_0x0083:
            r1.setTransactionSuccessful()     // Catch:{ Exception -> 0x008c }
        L_0x0086:
            r1.endTransaction()     // Catch:{ SQLiteException -> 0x0099 }
            goto L_0x0099
        L_0x008a:
            r10 = move-exception
            goto L_0x009b
        L_0x008c:
            r11 = move-exception
            java.lang.String r12 = TAG     // Catch:{ all -> 0x008a }
            java.lang.String r13 = "Error writing for %s"
            java.lang.Object[] r2 = new java.lang.Object[r5]     // Catch:{ all -> 0x008a }
            r2[r4] = r10     // Catch:{ all -> 0x008a }
            com.facebook.common.logging.FLog.e((java.lang.String) r12, (java.lang.Throwable) r11, (java.lang.String) r13, (java.lang.Object[]) r2)     // Catch:{ all -> 0x008a }
            goto L_0x0086
        L_0x0099:
            monitor-exit(r0)     // Catch:{ all -> 0x009f }
            return
        L_0x009b:
            r1.endTransaction()     // Catch:{ SQLiteException -> 0x009e }
        L_0x009e:
            throw r10     // Catch:{ all -> 0x009f }
        L_0x009f:
            r10 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x009f }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.cache.MediaVariationsIndexDatabase.saveCachedVariantSync(java.lang.String, com.facebook.imagepipeline.request.ImageRequest$CacheChoice, com.facebook.cache.common.CacheKey, com.facebook.imagepipeline.image.EncodedImage):void");
    }

    private static final class IndexEntry implements BaseColumns {
        public static final String COLUMN_NAME_CACHE_CHOICE = "cache_choice";
        public static final String COLUMN_NAME_CACHE_KEY = "cache_key";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_MEDIA_ID = "media_id";
        public static final String COLUMN_NAME_RESOURCE_ID = "resource_id";
        public static final String COLUMN_NAME_WIDTH = "width";
        public static final String TABLE_NAME = "media_variations_index";

        private IndexEntry() {
        }
    }

    private static class LazyIndexDbOpenHelper {
        private final Context mContext;
        @Nullable
        private IndexDbOpenHelper mIndexDbOpenHelper;

        private LazyIndexDbOpenHelper(Context context) {
            this.mContext = context;
        }

        public synchronized SQLiteDatabase getWritableDatabase() {
            if (this.mIndexDbOpenHelper == null) {
                this.mIndexDbOpenHelper = new IndexDbOpenHelper(this.mContext);
            }
            return this.mIndexDbOpenHelper.getWritableDatabase();
        }
    }

    private static class IndexDbOpenHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "FrescoMediaVariationsIndex.db";
        public static final int DATABASE_VERSION = 3;
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String SQL_CREATE_ENTRIES = "CREATE TABLE media_variations_index (_id INTEGER PRIMARY KEY,media_id TEXT,width INTEGER,height INTEGER,cache_choice TEXT,cache_key TEXT,resource_id TEXT UNIQUE,date INTEGER )";
        private static final String SQL_CREATE_INDEX = "CREATE INDEX index_media_id ON media_variations_index (media_id)";
        private static final String TEXT_TYPE = " TEXT";

        public IndexDbOpenHelper(Context context) {
            super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.beginTransaction();
            try {
                sQLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
                sQLiteDatabase.execSQL(SQL_CREATE_INDEX);
                sQLiteDatabase.setTransactionSuccessful();
            } finally {
                sQLiteDatabase.endTransaction();
            }
        }

        /* JADX INFO: finally extract failed */
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            sQLiteDatabase.beginTransaction();
            try {
                sQLiteDatabase.execSQL(MediaVariationsIndexDatabase.SQL_DROP_TABLE);
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                onCreate(sQLiteDatabase);
            } catch (Throwable th) {
                sQLiteDatabase.endTransaction();
                throw th;
            }
        }

        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            onUpgrade(sQLiteDatabase, i, i2);
        }
    }
}
