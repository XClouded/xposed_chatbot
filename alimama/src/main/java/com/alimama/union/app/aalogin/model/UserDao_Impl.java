package com.alimama.union.app.aalogin.model;

import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.alimama.moon.RoomConverters;
import com.alimama.union.app.personalCenter.model.GradeThreshold;

public final class UserDao_Impl implements UserDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfUser;

    public UserDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `User`(`userId`,`userNick`,`avatarLink`,`memberId`,`type`,`finishTypeDisp`,`finishTypeDispDateStartIndex`,`finishTypeDispDateEndIndex`,`gradeString`,`taskStartTime`,`taskEndTime`,`taskStartShowTime`,`taskEndShowTime`,`lastTaskStartTime`,`lastTaskEndTime`,`currentTotalOrder`,`currentTotalUV`,`orderFinishRate`,`uvFinishRate`,`nextUpdateTime`,`butlerPrivilege`,`walletPrivilege`,`isInRisk`,`grade`,`threshold_checkOrderNum`,`threshold_validOrderNum`,`threshold_minValidOrderNum`,`threshold_totalUv`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, User user) {
                if (user.getUserId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, user.getUserId());
                }
                if (user.getUserNick() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, user.getUserNick());
                }
                if (user.getAvatarLink() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, user.getAvatarLink());
                }
                if (user.getMemberId() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, user.getMemberId());
                }
                if (user.getType() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindLong(5, (long) user.getType().intValue());
                }
                if (user.getFinishTypeDisp() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, user.getFinishTypeDisp());
                }
                if (user.getFinishTypeDispDateStartIndex() == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindLong(7, (long) user.getFinishTypeDispDateStartIndex().intValue());
                }
                if (user.getFinishTypeDispDateEndIndex() == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindLong(8, (long) user.getFinishTypeDispDateEndIndex().intValue());
                }
                if (user.getGradeString() == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindString(9, user.getGradeString());
                }
                Long dateToTimestamp = RoomConverters.dateToTimestamp(user.getTaskStartTime());
                if (dateToTimestamp == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindLong(10, dateToTimestamp.longValue());
                }
                Long dateToTimestamp2 = RoomConverters.dateToTimestamp(user.getTaskEndTime());
                if (dateToTimestamp2 == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindLong(11, dateToTimestamp2.longValue());
                }
                if (user.getTaskStartShowTime() == null) {
                    supportSQLiteStatement.bindNull(12);
                } else {
                    supportSQLiteStatement.bindString(12, user.getTaskStartShowTime());
                }
                if (user.getTaskEndShowTime() == null) {
                    supportSQLiteStatement.bindNull(13);
                } else {
                    supportSQLiteStatement.bindString(13, user.getTaskEndShowTime());
                }
                Long dateToTimestamp3 = RoomConverters.dateToTimestamp(user.getLastTaskStartTime());
                if (dateToTimestamp3 == null) {
                    supportSQLiteStatement.bindNull(14);
                } else {
                    supportSQLiteStatement.bindLong(14, dateToTimestamp3.longValue());
                }
                Long dateToTimestamp4 = RoomConverters.dateToTimestamp(user.getLastTaskEndTime());
                if (dateToTimestamp4 == null) {
                    supportSQLiteStatement.bindNull(15);
                } else {
                    supportSQLiteStatement.bindLong(15, dateToTimestamp4.longValue());
                }
                if (user.getCurrentTotalOrder() == null) {
                    supportSQLiteStatement.bindNull(16);
                } else {
                    supportSQLiteStatement.bindLong(16, user.getCurrentTotalOrder().longValue());
                }
                if (user.getCurrentTotalUV() == null) {
                    supportSQLiteStatement.bindNull(17);
                } else {
                    supportSQLiteStatement.bindLong(17, user.getCurrentTotalUV().longValue());
                }
                if (user.getOrderFinishRate() == null) {
                    supportSQLiteStatement.bindNull(18);
                } else {
                    supportSQLiteStatement.bindLong(18, (long) user.getOrderFinishRate().intValue());
                }
                if (user.getUvFinishRate() == null) {
                    supportSQLiteStatement.bindNull(19);
                } else {
                    supportSQLiteStatement.bindLong(19, (long) user.getUvFinishRate().intValue());
                }
                if (user.getNextUpdateTime() == null) {
                    supportSQLiteStatement.bindNull(20);
                } else {
                    supportSQLiteStatement.bindString(20, user.getNextUpdateTime());
                }
                if (user.getButlerPrivilege() == null) {
                    supportSQLiteStatement.bindNull(21);
                } else {
                    supportSQLiteStatement.bindLong(21, (long) user.getButlerPrivilege().intValue());
                }
                if (user.getWalletPrivilege() == null) {
                    supportSQLiteStatement.bindNull(22);
                } else {
                    supportSQLiteStatement.bindLong(22, (long) user.getWalletPrivilege().intValue());
                }
                if (user.getIsInRisk() == null) {
                    supportSQLiteStatement.bindNull(23);
                } else {
                    supportSQLiteStatement.bindLong(23, (long) user.getIsInRisk().intValue());
                }
                if (user.getGrade() == null) {
                    supportSQLiteStatement.bindNull(24);
                } else {
                    supportSQLiteStatement.bindLong(24, (long) user.getGrade().intValue());
                }
                GradeThreshold threshold = user.getThreshold();
                if (threshold != null) {
                    if (threshold.getCheckOrderNum() == null) {
                        supportSQLiteStatement.bindNull(25);
                    } else {
                        supportSQLiteStatement.bindLong(25, (long) threshold.getCheckOrderNum().intValue());
                    }
                    if (threshold.getValidOrderNum() == null) {
                        supportSQLiteStatement.bindNull(26);
                    } else {
                        supportSQLiteStatement.bindLong(26, (long) threshold.getValidOrderNum().intValue());
                    }
                    if (threshold.getMinValidOrderNum() == null) {
                        supportSQLiteStatement.bindNull(27);
                    } else {
                        supportSQLiteStatement.bindLong(27, (long) threshold.getMinValidOrderNum().intValue());
                    }
                    if (threshold.getTotalUv() == null) {
                        supportSQLiteStatement.bindNull(28);
                    } else {
                        supportSQLiteStatement.bindLong(28, (long) threshold.getTotalUv().intValue());
                    }
                } else {
                    supportSQLiteStatement.bindNull(25);
                    supportSQLiteStatement.bindNull(26);
                    supportSQLiteStatement.bindNull(27);
                    supportSQLiteStatement.bindNull(28);
                }
            }
        };
    }

    public void insertUser(User user) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfUser.insert(user);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r31v0, resolved type: com.alimama.union.app.aalogin.model.User} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r31v1, resolved type: com.alimama.union.app.aalogin.model.User} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r31v3, resolved type: com.alimama.union.app.aalogin.model.User} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v53, resolved type: java.lang.Integer} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r31v4, resolved type: com.alimama.union.app.aalogin.model.User} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r31v5, resolved type: com.alimama.union.app.aalogin.model.User} */
    /* JADX WARNING: type inference failed for: r31v2, types: [java.lang.Integer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0183 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0186 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x019e A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x01a1 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01b2 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01b5 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01cd A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01d0 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x01e7 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x01ea A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0213 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0216 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x022d A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0230 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0247 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x024a A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x025d A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0260 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0273 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0276 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0289 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x028c A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x02a8 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x02ab A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x02be A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x02c1 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x02d4 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x02d7 A[Catch:{ all -> 0x0305 }] */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x02ed A[Catch:{ all -> 0x0305 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alimama.union.app.aalogin.model.User getUserById(java.lang.String r34) {
        /*
            r33 = this;
            r0 = r34
            java.lang.String r1 = "select * from user where userId=?"
            r2 = 1
            androidx.room.RoomSQLiteQuery r1 = androidx.room.RoomSQLiteQuery.acquire(r1, r2)
            if (r0 != 0) goto L_0x0011
            r1.bindNull(r2)
        L_0x000e:
            r2 = r33
            goto L_0x0015
        L_0x0011:
            r1.bindString(r2, r0)
            goto L_0x000e
        L_0x0015:
            androidx.room.RoomDatabase r0 = r2.__db
            android.database.Cursor r3 = r0.query(r1)
            java.lang.String r0 = "userId"
            int r0 = r3.getColumnIndexOrThrow(r0)     // Catch:{ all -> 0x0307 }
            java.lang.String r4 = "userNick"
            int r4 = r3.getColumnIndexOrThrow(r4)     // Catch:{ all -> 0x0307 }
            java.lang.String r5 = "avatarLink"
            int r5 = r3.getColumnIndexOrThrow(r5)     // Catch:{ all -> 0x0307 }
            java.lang.String r6 = "memberId"
            int r6 = r3.getColumnIndexOrThrow(r6)     // Catch:{ all -> 0x0307 }
            java.lang.String r7 = "type"
            int r7 = r3.getColumnIndexOrThrow(r7)     // Catch:{ all -> 0x0307 }
            java.lang.String r8 = "finishTypeDisp"
            int r8 = r3.getColumnIndexOrThrow(r8)     // Catch:{ all -> 0x0307 }
            java.lang.String r9 = "finishTypeDispDateStartIndex"
            int r9 = r3.getColumnIndexOrThrow(r9)     // Catch:{ all -> 0x0307 }
            java.lang.String r10 = "finishTypeDispDateEndIndex"
            int r10 = r3.getColumnIndexOrThrow(r10)     // Catch:{ all -> 0x0307 }
            java.lang.String r11 = "gradeString"
            int r11 = r3.getColumnIndexOrThrow(r11)     // Catch:{ all -> 0x0307 }
            java.lang.String r12 = "taskStartTime"
            int r12 = r3.getColumnIndexOrThrow(r12)     // Catch:{ all -> 0x0307 }
            java.lang.String r13 = "taskEndTime"
            int r13 = r3.getColumnIndexOrThrow(r13)     // Catch:{ all -> 0x0307 }
            java.lang.String r14 = "taskStartShowTime"
            int r14 = r3.getColumnIndexOrThrow(r14)     // Catch:{ all -> 0x0307 }
            java.lang.String r15 = "taskEndShowTime"
            int r15 = r3.getColumnIndexOrThrow(r15)     // Catch:{ all -> 0x0307 }
            java.lang.String r2 = "lastTaskStartTime"
            int r2 = r3.getColumnIndexOrThrow(r2)     // Catch:{ all -> 0x0307 }
            r16 = r1
            java.lang.String r1 = "lastTaskEndTime"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r17 = r1
            java.lang.String r1 = "currentTotalOrder"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r18 = r1
            java.lang.String r1 = "currentTotalUV"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r19 = r1
            java.lang.String r1 = "orderFinishRate"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r20 = r1
            java.lang.String r1 = "uvFinishRate"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r21 = r1
            java.lang.String r1 = "nextUpdateTime"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r22 = r1
            java.lang.String r1 = "butlerPrivilege"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r23 = r1
            java.lang.String r1 = "walletPrivilege"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r24 = r1
            java.lang.String r1 = "isInRisk"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r25 = r1
            java.lang.String r1 = "grade"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r26 = r1
            java.lang.String r1 = "threshold_checkOrderNum"
            int r1 = r3.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0305 }
            r27 = r2
            java.lang.String r2 = "threshold_validOrderNum"
            int r2 = r3.getColumnIndexOrThrow(r2)     // Catch:{ all -> 0x0305 }
            r28 = r15
            java.lang.String r15 = "threshold_minValidOrderNum"
            int r15 = r3.getColumnIndexOrThrow(r15)     // Catch:{ all -> 0x0305 }
            r29 = r14
            java.lang.String r14 = "threshold_totalUv"
            int r14 = r3.getColumnIndexOrThrow(r14)     // Catch:{ all -> 0x0305 }
            boolean r30 = r3.moveToFirst()     // Catch:{ all -> 0x0305 }
            r31 = 0
            if (r30 == 0) goto L_0x02fe
            boolean r30 = r3.isNull(r1)     // Catch:{ all -> 0x0305 }
            if (r30 == 0) goto L_0x0105
            boolean r30 = r3.isNull(r2)     // Catch:{ all -> 0x0305 }
            if (r30 == 0) goto L_0x0105
            boolean r30 = r3.isNull(r15)     // Catch:{ all -> 0x0305 }
            if (r30 == 0) goto L_0x0105
            boolean r30 = r3.isNull(r14)     // Catch:{ all -> 0x0305 }
            if (r30 != 0) goto L_0x0100
            goto L_0x0105
        L_0x0100:
            r32 = r13
            r13 = r31
            goto L_0x015c
        L_0x0105:
            r32 = r13
            com.alimama.union.app.personalCenter.model.GradeThreshold r13 = new com.alimama.union.app.personalCenter.model.GradeThreshold     // Catch:{ all -> 0x0305 }
            r13.<init>()     // Catch:{ all -> 0x0305 }
            boolean r30 = r3.isNull(r1)     // Catch:{ all -> 0x0305 }
            if (r30 == 0) goto L_0x0115
            r1 = r31
            goto L_0x011d
        L_0x0115:
            int r1 = r3.getInt(r1)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0305 }
        L_0x011d:
            r13.setCheckOrderNum(r1)     // Catch:{ all -> 0x0305 }
            boolean r1 = r3.isNull(r2)     // Catch:{ all -> 0x0305 }
            if (r1 == 0) goto L_0x0129
            r1 = r31
            goto L_0x0131
        L_0x0129:
            int r1 = r3.getInt(r2)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0305 }
        L_0x0131:
            r13.setValidOrderNum(r1)     // Catch:{ all -> 0x0305 }
            boolean r1 = r3.isNull(r15)     // Catch:{ all -> 0x0305 }
            if (r1 == 0) goto L_0x013d
            r1 = r31
            goto L_0x0145
        L_0x013d:
            int r1 = r3.getInt(r15)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0305 }
        L_0x0145:
            r13.setMinValidOrderNum(r1)     // Catch:{ all -> 0x0305 }
            boolean r1 = r3.isNull(r14)     // Catch:{ all -> 0x0305 }
            if (r1 == 0) goto L_0x0151
            r1 = r31
            goto L_0x0159
        L_0x0151:
            int r1 = r3.getInt(r14)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0305 }
        L_0x0159:
            r13.setTotalUv(r1)     // Catch:{ all -> 0x0305 }
        L_0x015c:
            com.alimama.union.app.aalogin.model.User r1 = new com.alimama.union.app.aalogin.model.User     // Catch:{ all -> 0x0305 }
            r1.<init>()     // Catch:{ all -> 0x0305 }
            java.lang.String r0 = r3.getString(r0)     // Catch:{ all -> 0x0305 }
            r1.setUserId(r0)     // Catch:{ all -> 0x0305 }
            java.lang.String r0 = r3.getString(r4)     // Catch:{ all -> 0x0305 }
            r1.setUserNick(r0)     // Catch:{ all -> 0x0305 }
            java.lang.String r0 = r3.getString(r5)     // Catch:{ all -> 0x0305 }
            r1.setAvatarLink(r0)     // Catch:{ all -> 0x0305 }
            java.lang.String r0 = r3.getString(r6)     // Catch:{ all -> 0x0305 }
            r1.setMemberId(r0)     // Catch:{ all -> 0x0305 }
            boolean r0 = r3.isNull(r7)     // Catch:{ all -> 0x0305 }
            if (r0 == 0) goto L_0x0186
            r0 = r31
            goto L_0x018e
        L_0x0186:
            int r0 = r3.getInt(r7)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x018e:
            r1.setType(r0)     // Catch:{ all -> 0x0305 }
            java.lang.String r0 = r3.getString(r8)     // Catch:{ all -> 0x0305 }
            r1.setFinishTypeDisp(r0)     // Catch:{ all -> 0x0305 }
            boolean r0 = r3.isNull(r9)     // Catch:{ all -> 0x0305 }
            if (r0 == 0) goto L_0x01a1
            r0 = r31
            goto L_0x01a9
        L_0x01a1:
            int r0 = r3.getInt(r9)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x01a9:
            r1.setFinishTypeDispDateStartIndex(r0)     // Catch:{ all -> 0x0305 }
            boolean r0 = r3.isNull(r10)     // Catch:{ all -> 0x0305 }
            if (r0 == 0) goto L_0x01b5
            r0 = r31
            goto L_0x01bd
        L_0x01b5:
            int r0 = r3.getInt(r10)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x01bd:
            r1.setFinishTypeDispDateEndIndex(r0)     // Catch:{ all -> 0x0305 }
            java.lang.String r0 = r3.getString(r11)     // Catch:{ all -> 0x0305 }
            r1.setGradeString(r0)     // Catch:{ all -> 0x0305 }
            boolean r0 = r3.isNull(r12)     // Catch:{ all -> 0x0305 }
            if (r0 == 0) goto L_0x01d0
            r0 = r31
            goto L_0x01d8
        L_0x01d0:
            long r4 = r3.getLong(r12)     // Catch:{ all -> 0x0305 }
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0305 }
        L_0x01d8:
            java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0305 }
            r1.setTaskStartTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r32
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x01ea
            r0 = r31
            goto L_0x01f2
        L_0x01ea:
            long r4 = r3.getLong(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0305 }
        L_0x01f2:
            java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0305 }
            r1.setTaskEndTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r29
            java.lang.String r0 = r3.getString(r0)     // Catch:{ all -> 0x0305 }
            r1.setTaskStartShowTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r28
            java.lang.String r0 = r3.getString(r0)     // Catch:{ all -> 0x0305 }
            r1.setTaskEndShowTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r27
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x0216
            r0 = r31
            goto L_0x021e
        L_0x0216:
            long r4 = r3.getLong(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0305 }
        L_0x021e:
            java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0305 }
            r1.setLastTaskStartTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r17
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x0230
            r0 = r31
            goto L_0x0238
        L_0x0230:
            long r4 = r3.getLong(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0305 }
        L_0x0238:
            java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0305 }
            r1.setLastTaskEndTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r18
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x024a
            r0 = r31
            goto L_0x0252
        L_0x024a:
            long r4 = r3.getLong(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0305 }
        L_0x0252:
            r1.setCurrentTotalOrder(r0)     // Catch:{ all -> 0x0305 }
            r0 = r19
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x0260
            r0 = r31
            goto L_0x0268
        L_0x0260:
            long r4 = r3.getLong(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Long r0 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x0305 }
        L_0x0268:
            r1.setCurrentTotalUV(r0)     // Catch:{ all -> 0x0305 }
            r0 = r20
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x0276
            r0 = r31
            goto L_0x027e
        L_0x0276:
            int r0 = r3.getInt(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x027e:
            r1.setOrderFinishRate(r0)     // Catch:{ all -> 0x0305 }
            r0 = r21
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x028c
            r0 = r31
            goto L_0x0294
        L_0x028c:
            int r0 = r3.getInt(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x0294:
            r1.setUvFinishRate(r0)     // Catch:{ all -> 0x0305 }
            r0 = r22
            java.lang.String r0 = r3.getString(r0)     // Catch:{ all -> 0x0305 }
            r1.setNextUpdateTime(r0)     // Catch:{ all -> 0x0305 }
            r0 = r23
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x02ab
            r0 = r31
            goto L_0x02b3
        L_0x02ab:
            int r0 = r3.getInt(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x02b3:
            r1.setButlerPrivilege(r0)     // Catch:{ all -> 0x0305 }
            r0 = r24
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x02c1
            r0 = r31
            goto L_0x02c9
        L_0x02c1:
            int r0 = r3.getInt(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x02c9:
            r1.setWalletPrivilege(r0)     // Catch:{ all -> 0x0305 }
            r0 = r25
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x02d7
            r0 = r31
            goto L_0x02df
        L_0x02d7:
            int r0 = r3.getInt(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
        L_0x02df:
            r1.setIsInRisk(r0)     // Catch:{ all -> 0x0305 }
            r0 = r26
            boolean r2 = r3.isNull(r0)     // Catch:{ all -> 0x0305 }
            if (r2 == 0) goto L_0x02ed
        L_0x02ea:
            r0 = r31
            goto L_0x02f6
        L_0x02ed:
            int r0 = r3.getInt(r0)     // Catch:{ all -> 0x0305 }
            java.lang.Integer r31 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0305 }
            goto L_0x02ea
        L_0x02f6:
            r1.setGrade(r0)     // Catch:{ all -> 0x0305 }
            r1.setThreshold(r13)     // Catch:{ all -> 0x0305 }
            r31 = r1
        L_0x02fe:
            r3.close()
            r16.release()
            return r31
        L_0x0305:
            r0 = move-exception
            goto L_0x030a
        L_0x0307:
            r0 = move-exception
            r16 = r1
        L_0x030a:
            r3.close()
            r16.release()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.aalogin.model.UserDao_Impl.getUserById(java.lang.String):com.alimama.union.app.aalogin.model.User");
    }

    public LiveData<User> getUserByIdAsync(String str) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("select * from user where userId=?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        return new ComputableLiveData<User>(this.__db.getQueryExecutor()) {
            private InvalidationTracker.Observer _observer;

            /* access modifiers changed from: protected */
            /* JADX WARNING: Removed duplicated region for block: B:36:0x0193 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:37:0x0196 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:40:0x01ae A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:41:0x01b1 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:44:0x01c2 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:45:0x01c5 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:48:0x01dd A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:49:0x01e0 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:52:0x01f7 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:53:0x01fa A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:56:0x0223 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:57:0x0226 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:60:0x023d A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:61:0x0240 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:64:0x0257 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:65:0x025a A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:68:0x026d A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:69:0x0270 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:72:0x0283 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:73:0x0286 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:76:0x0299 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:77:0x029c A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:80:0x02b8 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:81:0x02bb A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:84:0x02ce A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:85:0x02d1 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:88:0x02e4 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:89:0x02e7 A[Catch:{ all -> 0x0313 }] */
            /* JADX WARNING: Removed duplicated region for block: B:93:0x02fd A[Catch:{ all -> 0x0313 }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public com.alimama.union.app.aalogin.model.User compute() {
                /*
                    r32 = this;
                    r1 = r32
                    androidx.room.InvalidationTracker$Observer r0 = r1._observer
                    if (r0 != 0) goto L_0x0021
                    com.alimama.union.app.aalogin.model.UserDao_Impl$2$1 r0 = new com.alimama.union.app.aalogin.model.UserDao_Impl$2$1
                    java.lang.String r2 = "user"
                    r3 = 0
                    java.lang.String[] r3 = new java.lang.String[r3]
                    r0.<init>(r2, r3)
                    r1._observer = r0
                    com.alimama.union.app.aalogin.model.UserDao_Impl r0 = com.alimama.union.app.aalogin.model.UserDao_Impl.this
                    androidx.room.RoomDatabase r0 = r0.__db
                    androidx.room.InvalidationTracker r0 = r0.getInvalidationTracker()
                    androidx.room.InvalidationTracker$Observer r2 = r1._observer
                    r0.addWeakObserver(r2)
                L_0x0021:
                    com.alimama.union.app.aalogin.model.UserDao_Impl r0 = com.alimama.union.app.aalogin.model.UserDao_Impl.this
                    androidx.room.RoomDatabase r0 = r0.__db
                    androidx.room.RoomSQLiteQuery r2 = r0
                    android.database.Cursor r2 = r0.query(r2)
                    java.lang.String r0 = "userId"
                    int r0 = r2.getColumnIndexOrThrow(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.String r3 = "userNick"
                    int r3 = r2.getColumnIndexOrThrow(r3)     // Catch:{ all -> 0x0313 }
                    java.lang.String r4 = "avatarLink"
                    int r4 = r2.getColumnIndexOrThrow(r4)     // Catch:{ all -> 0x0313 }
                    java.lang.String r5 = "memberId"
                    int r5 = r2.getColumnIndexOrThrow(r5)     // Catch:{ all -> 0x0313 }
                    java.lang.String r6 = "type"
                    int r6 = r2.getColumnIndexOrThrow(r6)     // Catch:{ all -> 0x0313 }
                    java.lang.String r7 = "finishTypeDisp"
                    int r7 = r2.getColumnIndexOrThrow(r7)     // Catch:{ all -> 0x0313 }
                    java.lang.String r8 = "finishTypeDispDateStartIndex"
                    int r8 = r2.getColumnIndexOrThrow(r8)     // Catch:{ all -> 0x0313 }
                    java.lang.String r9 = "finishTypeDispDateEndIndex"
                    int r9 = r2.getColumnIndexOrThrow(r9)     // Catch:{ all -> 0x0313 }
                    java.lang.String r10 = "gradeString"
                    int r10 = r2.getColumnIndexOrThrow(r10)     // Catch:{ all -> 0x0313 }
                    java.lang.String r11 = "taskStartTime"
                    int r11 = r2.getColumnIndexOrThrow(r11)     // Catch:{ all -> 0x0313 }
                    java.lang.String r12 = "taskEndTime"
                    int r12 = r2.getColumnIndexOrThrow(r12)     // Catch:{ all -> 0x0313 }
                    java.lang.String r13 = "taskStartShowTime"
                    int r13 = r2.getColumnIndexOrThrow(r13)     // Catch:{ all -> 0x0313 }
                    java.lang.String r14 = "taskEndShowTime"
                    int r14 = r2.getColumnIndexOrThrow(r14)     // Catch:{ all -> 0x0313 }
                    java.lang.String r15 = "lastTaskStartTime"
                    int r15 = r2.getColumnIndexOrThrow(r15)     // Catch:{ all -> 0x0313 }
                    java.lang.String r1 = "lastTaskEndTime"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r16 = r1
                    java.lang.String r1 = "currentTotalOrder"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r17 = r1
                    java.lang.String r1 = "currentTotalUV"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r18 = r1
                    java.lang.String r1 = "orderFinishRate"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r19 = r1
                    java.lang.String r1 = "uvFinishRate"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r20 = r1
                    java.lang.String r1 = "nextUpdateTime"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r21 = r1
                    java.lang.String r1 = "butlerPrivilege"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r22 = r1
                    java.lang.String r1 = "walletPrivilege"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r23 = r1
                    java.lang.String r1 = "isInRisk"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r24 = r1
                    java.lang.String r1 = "grade"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r25 = r1
                    java.lang.String r1 = "threshold_checkOrderNum"
                    int r1 = r2.getColumnIndexOrThrow(r1)     // Catch:{ all -> 0x0313 }
                    r26 = r15
                    java.lang.String r15 = "threshold_validOrderNum"
                    int r15 = r2.getColumnIndexOrThrow(r15)     // Catch:{ all -> 0x0313 }
                    r27 = r14
                    java.lang.String r14 = "threshold_minValidOrderNum"
                    int r14 = r2.getColumnIndexOrThrow(r14)     // Catch:{ all -> 0x0313 }
                    r28 = r13
                    java.lang.String r13 = "threshold_totalUv"
                    int r13 = r2.getColumnIndexOrThrow(r13)     // Catch:{ all -> 0x0313 }
                    boolean r29 = r2.moveToFirst()     // Catch:{ all -> 0x0313 }
                    r30 = 0
                    if (r29 == 0) goto L_0x030d
                    boolean r29 = r2.isNull(r1)     // Catch:{ all -> 0x0313 }
                    if (r29 == 0) goto L_0x0115
                    boolean r29 = r2.isNull(r15)     // Catch:{ all -> 0x0313 }
                    if (r29 == 0) goto L_0x0115
                    boolean r29 = r2.isNull(r14)     // Catch:{ all -> 0x0313 }
                    if (r29 == 0) goto L_0x0115
                    boolean r29 = r2.isNull(r13)     // Catch:{ all -> 0x0313 }
                    if (r29 != 0) goto L_0x0110
                    goto L_0x0115
                L_0x0110:
                    r31 = r12
                    r12 = r30
                    goto L_0x016c
                L_0x0115:
                    r31 = r12
                    com.alimama.union.app.personalCenter.model.GradeThreshold r12 = new com.alimama.union.app.personalCenter.model.GradeThreshold     // Catch:{ all -> 0x0313 }
                    r12.<init>()     // Catch:{ all -> 0x0313 }
                    boolean r29 = r2.isNull(r1)     // Catch:{ all -> 0x0313 }
                    if (r29 == 0) goto L_0x0125
                    r1 = r30
                    goto L_0x012d
                L_0x0125:
                    int r1 = r2.getInt(r1)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0313 }
                L_0x012d:
                    r12.setCheckOrderNum(r1)     // Catch:{ all -> 0x0313 }
                    boolean r1 = r2.isNull(r15)     // Catch:{ all -> 0x0313 }
                    if (r1 == 0) goto L_0x0139
                    r1 = r30
                    goto L_0x0141
                L_0x0139:
                    int r1 = r2.getInt(r15)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0313 }
                L_0x0141:
                    r12.setValidOrderNum(r1)     // Catch:{ all -> 0x0313 }
                    boolean r1 = r2.isNull(r14)     // Catch:{ all -> 0x0313 }
                    if (r1 == 0) goto L_0x014d
                    r1 = r30
                    goto L_0x0155
                L_0x014d:
                    int r1 = r2.getInt(r14)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0313 }
                L_0x0155:
                    r12.setMinValidOrderNum(r1)     // Catch:{ all -> 0x0313 }
                    boolean r1 = r2.isNull(r13)     // Catch:{ all -> 0x0313 }
                    if (r1 == 0) goto L_0x0161
                    r1 = r30
                    goto L_0x0169
                L_0x0161:
                    int r1 = r2.getInt(r13)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0313 }
                L_0x0169:
                    r12.setTotalUv(r1)     // Catch:{ all -> 0x0313 }
                L_0x016c:
                    com.alimama.union.app.aalogin.model.User r1 = new com.alimama.union.app.aalogin.model.User     // Catch:{ all -> 0x0313 }
                    r1.<init>()     // Catch:{ all -> 0x0313 }
                    java.lang.String r0 = r2.getString(r0)     // Catch:{ all -> 0x0313 }
                    r1.setUserId(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.String r0 = r2.getString(r3)     // Catch:{ all -> 0x0313 }
                    r1.setUserNick(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.String r0 = r2.getString(r4)     // Catch:{ all -> 0x0313 }
                    r1.setAvatarLink(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.String r0 = r2.getString(r5)     // Catch:{ all -> 0x0313 }
                    r1.setMemberId(r0)     // Catch:{ all -> 0x0313 }
                    boolean r0 = r2.isNull(r6)     // Catch:{ all -> 0x0313 }
                    if (r0 == 0) goto L_0x0196
                    r0 = r30
                    goto L_0x019e
                L_0x0196:
                    int r0 = r2.getInt(r6)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x019e:
                    r1.setType(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.String r0 = r2.getString(r7)     // Catch:{ all -> 0x0313 }
                    r1.setFinishTypeDisp(r0)     // Catch:{ all -> 0x0313 }
                    boolean r0 = r2.isNull(r8)     // Catch:{ all -> 0x0313 }
                    if (r0 == 0) goto L_0x01b1
                    r0 = r30
                    goto L_0x01b9
                L_0x01b1:
                    int r0 = r2.getInt(r8)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x01b9:
                    r1.setFinishTypeDispDateStartIndex(r0)     // Catch:{ all -> 0x0313 }
                    boolean r0 = r2.isNull(r9)     // Catch:{ all -> 0x0313 }
                    if (r0 == 0) goto L_0x01c5
                    r0 = r30
                    goto L_0x01cd
                L_0x01c5:
                    int r0 = r2.getInt(r9)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x01cd:
                    r1.setFinishTypeDispDateEndIndex(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.String r0 = r2.getString(r10)     // Catch:{ all -> 0x0313 }
                    r1.setGradeString(r0)     // Catch:{ all -> 0x0313 }
                    boolean r0 = r2.isNull(r11)     // Catch:{ all -> 0x0313 }
                    if (r0 == 0) goto L_0x01e0
                    r0 = r30
                    goto L_0x01e8
                L_0x01e0:
                    long r3 = r2.getLong(r11)     // Catch:{ all -> 0x0313 }
                    java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0313 }
                L_0x01e8:
                    java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0313 }
                    r1.setTaskStartTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r31
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x01fa
                    r0 = r30
                    goto L_0x0202
                L_0x01fa:
                    long r3 = r2.getLong(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0313 }
                L_0x0202:
                    java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0313 }
                    r1.setTaskEndTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r28
                    java.lang.String r0 = r2.getString(r0)     // Catch:{ all -> 0x0313 }
                    r1.setTaskStartShowTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r27
                    java.lang.String r0 = r2.getString(r0)     // Catch:{ all -> 0x0313 }
                    r1.setTaskEndShowTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r26
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x0226
                    r0 = r30
                    goto L_0x022e
                L_0x0226:
                    long r3 = r2.getLong(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0313 }
                L_0x022e:
                    java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0313 }
                    r1.setLastTaskStartTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r16
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x0240
                    r0 = r30
                    goto L_0x0248
                L_0x0240:
                    long r3 = r2.getLong(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0313 }
                L_0x0248:
                    java.util.Date r0 = com.alimama.moon.RoomConverters.fromTimestamp(r0)     // Catch:{ all -> 0x0313 }
                    r1.setLastTaskEndTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r17
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x025a
                    r0 = r30
                    goto L_0x0262
                L_0x025a:
                    long r3 = r2.getLong(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0313 }
                L_0x0262:
                    r1.setCurrentTotalOrder(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r18
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x0270
                    r0 = r30
                    goto L_0x0278
                L_0x0270:
                    long r3 = r2.getLong(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Long r0 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0313 }
                L_0x0278:
                    r1.setCurrentTotalUV(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r19
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x0286
                    r0 = r30
                    goto L_0x028e
                L_0x0286:
                    int r0 = r2.getInt(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x028e:
                    r1.setOrderFinishRate(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r20
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x029c
                    r0 = r30
                    goto L_0x02a4
                L_0x029c:
                    int r0 = r2.getInt(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x02a4:
                    r1.setUvFinishRate(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r21
                    java.lang.String r0 = r2.getString(r0)     // Catch:{ all -> 0x0313 }
                    r1.setNextUpdateTime(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r22
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x02bb
                    r0 = r30
                    goto L_0x02c3
                L_0x02bb:
                    int r0 = r2.getInt(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x02c3:
                    r1.setButlerPrivilege(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r23
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x02d1
                    r0 = r30
                    goto L_0x02d9
                L_0x02d1:
                    int r0 = r2.getInt(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x02d9:
                    r1.setWalletPrivilege(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r24
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x02e7
                    r0 = r30
                    goto L_0x02ef
                L_0x02e7:
                    int r0 = r2.getInt(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                L_0x02ef:
                    r1.setIsInRisk(r0)     // Catch:{ all -> 0x0313 }
                    r0 = r25
                    boolean r3 = r2.isNull(r0)     // Catch:{ all -> 0x0313 }
                    if (r3 == 0) goto L_0x02fd
                L_0x02fa:
                    r0 = r30
                    goto L_0x0306
                L_0x02fd:
                    int r0 = r2.getInt(r0)     // Catch:{ all -> 0x0313 }
                    java.lang.Integer r30 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0313 }
                    goto L_0x02fa
                L_0x0306:
                    r1.setGrade(r0)     // Catch:{ all -> 0x0313 }
                    r1.setThreshold(r12)     // Catch:{ all -> 0x0313 }
                    goto L_0x030f
                L_0x030d:
                    r1 = r30
                L_0x030f:
                    r2.close()
                    return r1
                L_0x0313:
                    r0 = move-exception
                    r2.close()
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.aalogin.model.UserDao_Impl.AnonymousClass2.compute():com.alimama.union.app.aalogin.model.User");
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        }.getLiveData();
    }
}
