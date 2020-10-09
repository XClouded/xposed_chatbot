package com.taobao.android.dinamicx.template.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.taobao.android.dinamic.expressionv2.DinamicTokenizer;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.template.db.DXDataBaseEntry;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Field;
import java.util.ArrayList;

public final class DXDataBaseEntrySchema {
    private static final String[] SQLITE_TYPES = {"TEXT", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "REAL", "REAL", "NONE"};
    private static final String TAG = "DXDataBaseEntrySchema";
    public static final int TYPE_BLOB = 7;
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_FLOAT = 5;
    public static final int TYPE_INT = 3;
    public static final int TYPE_LONG = 4;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_STRING = 0;
    private final ColumnInfo[] mColumnInfo;
    private final String[] mProjection;
    private final String mTableName;

    public DXDataBaseEntrySchema(Class<? extends DXDataBaseEntry> cls) {
        ColumnInfo[] parseColumnInfo = parseColumnInfo(cls);
        this.mTableName = parseTableName(cls);
        this.mColumnInfo = parseColumnInfo;
        String[] strArr = new String[0];
        if (parseColumnInfo != null) {
            strArr = new String[parseColumnInfo.length];
            for (int i = 0; i != parseColumnInfo.length; i++) {
                strArr[i] = parseColumnInfo[i].name;
            }
        }
        this.mProjection = strArr;
    }

    public String getTableName() {
        return this.mTableName;
    }

    public String[] getProjection() {
        return this.mProjection;
    }

    private void logExecSql(SQLiteDatabase sQLiteDatabase, String str) {
        sQLiteDatabase.execSQL(str);
    }

    public void createTables(SQLiteDatabase sQLiteDatabase) {
        String str = this.mTableName;
        if (TextUtils.isEmpty(str)) {
            DXRemoteLog.remoteLoge("DinamicX", "DataBase", "没有用注解定义表名");
            return;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb2.append(str);
        sb2.append(" (_id INTEGER");
        for (ColumnInfo columnInfo : this.mColumnInfo) {
            if (!columnInfo.isId()) {
                sb2.append(',');
                sb2.append(columnInfo.name);
                sb2.append(' ');
                sb2.append(SQLITE_TYPES[columnInfo.type]);
                if (!TextUtils.isEmpty(columnInfo.defaultValue)) {
                    sb2.append(" DEFAULT ");
                    sb2.append(columnInfo.defaultValue);
                } else if (columnInfo.notNull) {
                    sb2.append(" NOT NULL");
                }
                if (columnInfo.primaryKey) {
                    sb.append(columnInfo.name);
                    sb.append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb2.append(", PRIMARY KEY(");
            sb2.append(sb.toString());
            sb2.append(Operators.BRACKET_END_STR);
        }
        sb2.append(");");
        logExecSql(sQLiteDatabase, sb2.toString());
        sb2.setLength(0);
        sb2.append("CREATE INDEX index_template ON ");
        sb2.append(str);
        sb2.append(Operators.BRACKET_START_STR);
        for (ColumnInfo columnInfo2 : this.mColumnInfo) {
            if (columnInfo2.indexed) {
                sb2.append(columnInfo2.name);
                sb2.append(",");
            }
        }
        sb2.deleteCharAt(sb2.length() - 1);
        sb2.append(");");
        logExecSql(sQLiteDatabase, sb2.toString());
        sb2.setLength(0);
    }

    public void dropTables(SQLiteDatabase sQLiteDatabase) {
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ");
        sb.append(this.mTableName);
        sb.append(DinamicTokenizer.TokenSEM);
        logExecSql(sQLiteDatabase, sb.toString());
        sb.setLength(0);
    }

    private String parseTableName(Class<?> cls) {
        DXDataBaseEntry.Table table = (DXDataBaseEntry.Table) cls.getAnnotation(DXDataBaseEntry.Table.class);
        if (table == null) {
            return null;
        }
        return table.value();
    }

    private ColumnInfo[] parseColumnInfo(Class<?> cls) {
        ArrayList arrayList = new ArrayList();
        for (Class<? super Object> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
            parseColumnInfo(cls2, arrayList);
        }
        ColumnInfo[] columnInfoArr = new ColumnInfo[arrayList.size()];
        arrayList.toArray(columnInfoArr);
        return columnInfoArr;
    }

    private void parseColumnInfo(Class<?> cls, ArrayList<ColumnInfo> arrayList) {
        int i;
        Field[] declaredFields = cls.getDeclaredFields();
        for (int i2 = 0; i2 != declaredFields.length; i2++) {
            Field field = declaredFields[i2];
            DXDataBaseEntry.Column column = (DXDataBaseEntry.Column) field.getAnnotation(DXDataBaseEntry.Column.class);
            if (column != null) {
                Class<?> type = field.getType();
                if (type == String.class) {
                    i = 0;
                } else if (type == Boolean.TYPE) {
                    i = 1;
                } else if (type == Short.TYPE) {
                    i = 2;
                } else if (type == Integer.TYPE) {
                    i = 3;
                } else if (type == Long.TYPE) {
                    i = 4;
                } else if (type == Float.TYPE) {
                    i = 5;
                } else if (type == Double.TYPE) {
                    i = 6;
                } else if (type == byte[].class) {
                    i = 7;
                } else {
                    throw new IllegalArgumentException("Unsupported field type for column: " + type.getName());
                }
                arrayList.add(new ColumnInfo(column.value(), i, column.indexed(), column.primaryKey(), column.defaultValue(), column.notNull(), field, arrayList.size()));
            }
        }
    }

    public static final class ColumnInfo {
        private static final String ID_KEY = "_id";
        public final String defaultValue;
        public final Field field;
        public final boolean indexed;
        public final String name;
        public final boolean notNull;
        public final boolean primaryKey;
        public final int projectionIndex;
        public final int type;

        public ColumnInfo(String str, int i, boolean z, boolean z2, String str2, boolean z3, Field field2, int i2) {
            this.name = str.toLowerCase();
            this.type = i;
            this.indexed = z;
            this.primaryKey = z2;
            this.defaultValue = str2;
            this.field = field2;
            this.projectionIndex = i2;
            this.notNull = z3;
            field2.setAccessible(true);
        }

        public boolean isId() {
            return "_id".equals(this.name);
        }
    }
}
