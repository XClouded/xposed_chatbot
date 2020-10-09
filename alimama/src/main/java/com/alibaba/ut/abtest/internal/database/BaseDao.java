package com.alibaba.ut.abtest.internal.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.alibaba.ut.abtest.internal.database.DataObject;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T extends DataObject> {
    public abstract Database getDatabase();

    /* access modifiers changed from: protected */
    public abstract long getKey(T t);

    /* access modifiers changed from: protected */
    public abstract String getKeyColumn();

    /* access modifiers changed from: protected */
    public abstract String getTableName();

    /* access modifiers changed from: protected */
    public abstract boolean hasKey(T t);

    /* access modifiers changed from: protected */
    public abstract T readEntity(Cursor cursor);

    /* access modifiers changed from: protected */
    public abstract void setKey(T t, long j);

    public long insert(T t) {
        return getDatabase().insert(getTableName(), t.toContentValues(), 0);
    }

    public long[] insertInTx(List<T> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (T contentValues : list) {
                arrayList.add(contentValues.toContentValues());
            }
        }
        return getDatabase().insertInTx(getTableName(), 0, (List<ContentValues>) arrayList);
    }

    public int update(T t) {
        WhereCondition whereCondition = new WhereCondition(getKeyColumn() + "=?", Long.valueOf(getKey(t)));
        return getDatabase().update(getTableName(), t.toContentValues(), whereCondition.getText(), whereCondition.getValues());
    }

    public void save(T t) {
        if (hasKey(t)) {
            update(t);
            return;
        }
        long insert = insert(t);
        if (insert > 0) {
            setKey(t, insert);
        }
    }

    public int delete(long j) {
        WhereCondition whereCondition = new WhereCondition(getKeyColumn() + "=?", Long.valueOf(j));
        return getDatabase().delete(getTableName(), whereCondition.getText(), whereCondition.getValues());
    }

    public int delete(String str, String... strArr) {
        return getDatabase().delete(getTableName(), str, strArr);
    }

    public long count() {
        return count((String) null, (String) null, new String[0]);
    }

    public long count(String str, String str2, String... strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT COUNT");
        stringBuffer.append(Operators.BRACKET_START_STR);
        if (TextUtils.isEmpty(str)) {
            stringBuffer.append("*");
        } else {
            stringBuffer.append(str);
        }
        stringBuffer.append(") FROM ");
        stringBuffer.append(getTableName());
        if (!TextUtils.isEmpty(str2)) {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(str2);
        }
        Cursor query = getDatabase().query(stringBuffer.toString(), strArr);
        try {
            if (query.moveToNext()) {
                return query.getLong(0);
            }
            query.close();
            return -1;
        } finally {
            query.close();
        }
    }

    public T uniqueResult(String[] strArr, String str, String... strArr2) {
        return loadUniqueAndCloseCursor(getDatabase().query(getTableName(), strArr, (String) null, (String) null, str, strArr2));
    }

    public ArrayList<T> query(String[] strArr, String str, int i, int i2, String str2, String... strArr2) {
        String str3;
        if (i2 > 0) {
            str3 = (i * i2) + "," + i2;
        } else {
            str3 = null;
        }
        return loadAllAndCloseCursor(getDatabase().query(getTableName(), strArr, str, str3, str2, strArr2));
    }

    /* access modifiers changed from: protected */
    public ArrayList<T> loadAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAll(cursor);
        } finally {
            cursor.close();
        }
    }

    /* access modifiers changed from: protected */
    public ArrayList<T> loadAll(Cursor cursor) {
        ArrayList<T> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(readEntity(cursor));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public T loadUniqueAndCloseCursor(Cursor cursor) {
        try {
            return loadUnique(cursor);
        } finally {
            cursor.close();
        }
    }

    /* access modifiers changed from: protected */
    public T loadUnique(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return null;
        }
        if (cursor.isLast()) {
            return readEntity(cursor);
        }
        throw new RuntimeException("Expected unique result, but count was " + cursor.getCount());
    }
}
