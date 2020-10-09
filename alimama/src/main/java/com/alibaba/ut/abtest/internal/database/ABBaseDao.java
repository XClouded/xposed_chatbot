package com.alibaba.ut.abtest.internal.database;

import android.content.ContentValues;
import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.database.ABDataObject;
import java.util.ArrayList;
import java.util.List;

public abstract class ABBaseDao<T extends ABDataObject> extends BaseDao<T> {
    /* access modifiers changed from: protected */
    public String getKeyColumn() {
        return "id";
    }

    public Database getDatabase() {
        return ABDatabase.getInstance();
    }

    /* access modifiers changed from: protected */
    public boolean hasKey(T t) {
        return t != null && t.getId() > 0;
    }

    /* access modifiers changed from: protected */
    public void setKey(T t, long j) {
        t.setId(j);
    }

    /* access modifiers changed from: protected */
    public long getKey(T t) {
        return t.getId();
    }

    public long insert(T t) {
        ContentValues contentValues = t.toContentValues();
        long currentTimeMillis = System.currentTimeMillis();
        contentValues.put(ABDataObject.COLUMN_CREATE_TIME, Long.valueOf(currentTimeMillis));
        contentValues.put(ABDataObject.COLUMN_MODIFIED_TIME, Long.valueOf(currentTimeMillis));
        contentValues.put(ABDataObject.COLUMN_OWNER_ID, ABContext.getInstance().getUserId());
        return getDatabase().insert(getTableName(), contentValues, 5);
    }

    public long[] insertInTx(List<T> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            long currentTimeMillis = System.currentTimeMillis();
            for (T contentValues : list) {
                ContentValues contentValues2 = contentValues.toContentValues();
                contentValues2.put(ABDataObject.COLUMN_CREATE_TIME, Long.valueOf(currentTimeMillis));
                contentValues2.put(ABDataObject.COLUMN_MODIFIED_TIME, Long.valueOf(currentTimeMillis));
                contentValues2.put(ABDataObject.COLUMN_OWNER_ID, ABContext.getInstance().getUserId());
                arrayList.add(contentValues2);
            }
        }
        return getDatabase().insertInTx(getTableName(), 5, (List<ContentValues>) arrayList);
    }

    public int update(T t) {
        ContentValues contentValues = t.toContentValues();
        contentValues.put(ABDataObject.COLUMN_MODIFIED_TIME, Long.valueOf(System.currentTimeMillis()));
        contentValues.remove(ABDataObject.COLUMN_CREATE_TIME);
        contentValues.remove(ABDataObject.COLUMN_OWNER_ID);
        WhereCondition whereCondition = new WhereCondition(getKeyColumn() + "=?", Long.valueOf(getKey(t)));
        return getDatabase().update(getTableName(), contentValues, whereCondition.getText(), whereCondition.getValues());
    }

    public ArrayList<T> queryByCurrentUser(String[] strArr, String str, int i, int i2, WhereConditionCollector whereConditionCollector) {
        if (whereConditionCollector == null) {
            whereConditionCollector = new WhereConditionCollector();
        }
        if (TextUtils.isEmpty(ABContext.getInstance().getUserId())) {
            whereConditionCollector.whereAnd(new WhereCondition("owner_id IS NULL", new Object[0]), new WhereCondition[0]);
        } else {
            whereConditionCollector.whereAnd(new WhereCondition("owner_id=?", ABContext.getInstance().getUserId()), new WhereCondition[0]);
        }
        WhereCondition combine = whereConditionCollector.combine();
        return super.query(strArr, str, i, i2, combine.getText(), combine.getValues());
    }

    public int deleteByCurrentUser(WhereConditionCollector whereConditionCollector) {
        if (whereConditionCollector == null) {
            whereConditionCollector = new WhereConditionCollector();
        }
        if (TextUtils.isEmpty(ABContext.getInstance().getUserId())) {
            whereConditionCollector.whereAnd(new WhereCondition("owner_id IS NULL", new Object[0]), new WhereCondition[0]);
        } else {
            whereConditionCollector.whereAnd(new WhereCondition("owner_id=?", ABContext.getInstance().getUserId()), new WhereCondition[0]);
        }
        WhereCondition combine = whereConditionCollector.combine();
        return super.delete(combine.getText(), combine.getValues());
    }

    public T uniqueResultByCurrentUser(String[] strArr, WhereConditionCollector whereConditionCollector) {
        if (whereConditionCollector == null) {
            whereConditionCollector = new WhereConditionCollector();
        }
        if (TextUtils.isEmpty(ABContext.getInstance().getUserId())) {
            whereConditionCollector.whereAnd(new WhereCondition("owner_id IS NULL", new Object[0]), new WhereCondition[0]);
        } else {
            whereConditionCollector.whereAnd(new WhereCondition("owner_id=?", ABContext.getInstance().getUserId()), new WhereCondition[0]);
        }
        WhereCondition combine = whereConditionCollector.combine();
        return (ABDataObject) super.uniqueResult(strArr, combine.getText(), combine.getValues());
    }
}
