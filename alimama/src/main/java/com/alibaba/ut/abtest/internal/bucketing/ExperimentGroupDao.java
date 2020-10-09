package com.alibaba.ut.abtest.internal.bucketing;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alibaba.ut.abtest.internal.database.ABBaseDao;
import com.alibaba.ut.abtest.internal.database.ABDataObject;
import com.taobao.weex.el.parse.Operators;

public class ExperimentGroupDao extends ABBaseDao<ExperimentGroupDO> {
    public static final String TABLE_NAME = "experiment_groups";

    /* access modifiers changed from: protected */
    public String getTableName() {
        return TABLE_NAME;
    }

    public static void createTable(SQLiteDatabase sQLiteDatabase, boolean z) throws Exception {
        String str = z ? "IF NOT EXISTS " : "";
        sQLiteDatabase.execSQL("CREATE TABLE " + str + "\"" + TABLE_NAME + "\" (" + "id" + " INTEGER PRIMARY KEY, " + "key" + " VARCHAR(2048) NOT NULL, " + "type" + " INTEGER NOT NULL, " + ExperimentGroupDO.COLUMN_BEGIN_TIME + " INTEGER NOT NULL, " + ExperimentGroupDO.COLUMN_END_TIME + " INTEGER NOT NULL, " + "data" + " TEXT, " + ExperimentGroupDO.COLUMN_HIT_COUNT + " INTEGER NOT NULL DEFAULT 0, " + ABDataObject.COLUMN_CREATE_TIME + " INTEGER, " + ABDataObject.COLUMN_MODIFIED_TIME + " INTEGER, " + ABDataObject.COLUMN_OWNER_ID + " VARCHAR(64) " + Operators.BRACKET_END_STR);
        sQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS experiment_groups_key_owner_id_idx ON experiment_groups(key,owner_id)");
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
    public ExperimentGroupDO readEntity(Cursor cursor) {
        return new ExperimentGroupDO(cursor);
    }
}
