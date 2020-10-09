package com.alibaba.ut.abtest.internal.debug;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alibaba.ut.abtest.internal.database.ABBaseDao;
import com.alibaba.ut.abtest.internal.database.ABDataObject;
import com.taobao.weex.el.parse.Operators;

public class DebugDao extends ABBaseDao<DebugDO> {
    public static final String TABLE_NAME = "debugs";

    /* access modifiers changed from: protected */
    public String getTableName() {
        return TABLE_NAME;
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) throws Exception {
        String str = z ? "IF NOT EXISTS " : "";
        sQLiteDatabase.execSQL("CREATE TABLE " + str + "\"" + TABLE_NAME + "\" (" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "debug_key" + " VARCHAR(512) NOT NULL, " + DebugDO.COLUMN_EXPIRED_TIME + " INTEGER, " + ABDataObject.COLUMN_OWNER_ID + " VARCHAR(64) NOT NULL, " + ABDataObject.COLUMN_CREATE_TIME + " INTEGER, " + ABDataObject.COLUMN_MODIFIED_TIME + " INTEGER, " + "CONSTRAINT uq UNIQUE (" + ABDataObject.COLUMN_OWNER_ID + "," + "debug_key" + Operators.BRACKET_END_STR + Operators.BRACKET_END_STR);
        sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS debugs_debug_key_owner_id_idx ON debugs(debug_key,owner_id)");
    }

    public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean z) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(z ? "IF EXISTS " : "");
        sb.append("\"");
        sb.append(TABLE_NAME);
        sb.append("\"");
        sQLiteDatabase.execSQL(sb.toString());
    }

    /* access modifiers changed from: protected */
    public DebugDO readEntity(Cursor cursor) {
        return new DebugDO(cursor);
    }

    public long insert(DebugDO debugDO) {
        return getDatabase().insert(getTableName(), debugDO.toContentValues(), 5);
    }
}
