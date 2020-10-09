package com.taobao.android.ultron.datamodel.cache.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.android.dinamic.expressionv2.DinamicTokenizer;
import com.taobao.android.ultron.datamodel.cache.db.Entry;
import java.lang.reflect.Field;
import java.util.ArrayList;

public final class EntrySchema {
    private static final String FULL_TEXT_INDEX_SUFFIX = "_fulltext";
    private static final String[] SQLITE_TYPES = {"TEXT", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "REAL", "REAL", "NONE"};
    private static final String TAG = "EntrySchema";
    public static final int TYPE_BLOB = 7;
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_FLOAT = 5;
    public static final int TYPE_INT = 3;
    public static final int TYPE_LONG = 4;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_STRING = 0;
    private final ColumnInfo[] mColumnInfo;
    private final boolean mHasFullTextIndex;
    private final String[] mProjection;
    private final String mTableName;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r6v4, types: [int] */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public EntrySchema(java.lang.Class<? extends com.taobao.android.ultron.datamodel.cache.db.Entry> r6) {
        /*
            r5 = this;
            r5.<init>()
            com.taobao.android.ultron.datamodel.cache.db.EntrySchema$ColumnInfo[] r0 = r5.parseColumnInfo(r6)
            java.lang.String r6 = r5.parseTableName(r6)
            r5.mTableName = r6
            r5.mColumnInfo = r0
            r6 = 0
            java.lang.String[] r1 = new java.lang.String[r6]
            if (r0 == 0) goto L_0x002a
            int r1 = r0.length
            java.lang.String[] r1 = new java.lang.String[r1]
            r2 = 0
        L_0x0018:
            int r3 = r0.length
            if (r6 == r3) goto L_0x0029
            r3 = r0[r6]
            java.lang.String r4 = r3.name
            r1[r6] = r4
            boolean r3 = r3.fullText
            if (r3 == 0) goto L_0x0026
            r2 = 1
        L_0x0026:
            int r6 = r6 + 1
            goto L_0x0018
        L_0x0029:
            r6 = r2
        L_0x002a:
            r5.mProjection = r1
            r5.mHasFullTextIndex = r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.cache.db.EntrySchema.<init>(java.lang.Class):void");
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

    public <T extends Entry> T cursorToObject(Cursor cursor, T t) {
        try {
            for (ColumnInfo columnInfo : this.mColumnInfo) {
                int i = columnInfo.projectionIndex;
                Field field = columnInfo.field;
                Object obj = null;
                switch (columnInfo.type) {
                    case 0:
                        if (!cursor.isNull(i)) {
                            obj = cursor.getString(i);
                        }
                        field.set(t, obj);
                        break;
                    case 1:
                        short s = cursor.getShort(i);
                        boolean z = true;
                        if (s != 1) {
                            z = false;
                        }
                        field.setBoolean(t, z);
                        break;
                    case 2:
                        field.setShort(t, cursor.getShort(i));
                        break;
                    case 3:
                        field.setInt(t, cursor.getInt(i));
                        break;
                    case 4:
                        field.setLong(t, cursor.getLong(i));
                        break;
                    case 5:
                        field.setFloat(t, cursor.getFloat(i));
                        break;
                    case 6:
                        field.setDouble(t, cursor.getDouble(i));
                        break;
                    case 7:
                        if (!cursor.isNull(i)) {
                            obj = cursor.getBlob(i);
                        }
                        field.set(t, obj);
                        break;
                }
            }
            return t;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void objectToValues(Entry entry, ContentValues contentValues) {
        try {
            for (ColumnInfo columnInfo : this.mColumnInfo) {
                String str = columnInfo.name;
                Field field = columnInfo.field;
                switch (columnInfo.type) {
                    case 0:
                        contentValues.put(str, (String) field.get(entry));
                        break;
                    case 1:
                        contentValues.put(str, Boolean.valueOf(field.getBoolean(entry)));
                        break;
                    case 2:
                        contentValues.put(str, Short.valueOf(field.getShort(entry)));
                        break;
                    case 3:
                        contentValues.put(str, Integer.valueOf(field.getInt(entry)));
                        break;
                    case 4:
                        contentValues.put(str, Long.valueOf(field.getLong(entry)));
                        break;
                    case 5:
                        contentValues.put(str, Float.valueOf(field.getFloat(entry)));
                        break;
                    case 6:
                        contentValues.put(str, Double.valueOf(field.getDouble(entry)));
                        break;
                    case 7:
                        contentValues.put(str, (byte[]) field.get(entry));
                        break;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public long insertOrReplace(SQLiteDatabase sQLiteDatabase, Entry entry) {
        ContentValues contentValues = new ContentValues();
        objectToValues(entry, contentValues);
        if (entry.id == 0) {
            contentValues.remove("_id");
        }
        long replace = sQLiteDatabase.replace(this.mTableName, "_id", contentValues);
        entry.id = replace;
        return replace;
    }

    public void createTables(SQLiteDatabase sQLiteDatabase) {
        String str = this.mTableName;
        Utils.assertTrue(str != null);
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(str);
        sb.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT");
        for (ColumnInfo columnInfo : this.mColumnInfo) {
            if (!columnInfo.isId()) {
                sb.append(',');
                sb.append(columnInfo.name);
                sb.append(' ');
                sb.append(SQLITE_TYPES[columnInfo.type]);
                if (!TextUtils.isEmpty(columnInfo.defaultValue)) {
                    sb.append(" DEFAULT ");
                    sb.append(columnInfo.defaultValue);
                }
            }
        }
        sb.append(");");
        logExecSql(sQLiteDatabase, sb.toString());
        sb.setLength(0);
        for (ColumnInfo columnInfo2 : this.mColumnInfo) {
            if (columnInfo2.indexed) {
                sb.append("CREATE INDEX ");
                sb.append(str);
                sb.append("_index_");
                sb.append(columnInfo2.name);
                sb.append(" ON ");
                sb.append(str);
                sb.append(" (");
                sb.append(columnInfo2.name);
                sb.append(");");
                logExecSql(sQLiteDatabase, sb.toString());
                sb.setLength(0);
            }
        }
        if (this.mHasFullTextIndex) {
            String str2 = str + FULL_TEXT_INDEX_SUFFIX;
            sb.append("CREATE VIRTUAL TABLE ");
            sb.append(str2);
            sb.append(" USING FTS3 (_id INTEGER PRIMARY KEY");
            for (ColumnInfo columnInfo3 : this.mColumnInfo) {
                if (columnInfo3.fullText) {
                    String str3 = columnInfo3.name;
                    sb.append(',');
                    sb.append(str3);
                    sb.append(AVFSCacheConstants.TEXT_TYPE);
                }
            }
            sb.append(");");
            logExecSql(sQLiteDatabase, sb.toString());
            sb.setLength(0);
            StringBuilder sb2 = new StringBuilder("INSERT OR REPLACE INTO ");
            sb2.append(str2);
            sb2.append(" (_id");
            for (ColumnInfo columnInfo4 : this.mColumnInfo) {
                if (columnInfo4.fullText) {
                    sb2.append(',');
                    sb2.append(columnInfo4.name);
                }
            }
            sb2.append(") VALUES (new._id");
            for (ColumnInfo columnInfo5 : this.mColumnInfo) {
                if (columnInfo5.fullText) {
                    sb2.append(",new.");
                    sb2.append(columnInfo5.name);
                }
            }
            sb2.append(");");
            String sb3 = sb2.toString();
            sb.append("CREATE TRIGGER ");
            sb.append(str);
            sb.append("_insert_trigger AFTER INSERT ON ");
            sb.append(str);
            sb.append(" FOR EACH ROW BEGIN ");
            sb.append(sb3);
            sb.append("END;");
            logExecSql(sQLiteDatabase, sb.toString());
            sb.setLength(0);
            sb.append("CREATE TRIGGER ");
            sb.append(str);
            sb.append("_update_trigger AFTER UPDATE ON ");
            sb.append(str);
            sb.append(" FOR EACH ROW BEGIN ");
            sb.append(sb3);
            sb.append("END;");
            logExecSql(sQLiteDatabase, sb.toString());
            sb.setLength(0);
            sb.append("CREATE TRIGGER ");
            sb.append(str);
            sb.append("_delete_trigger AFTER DELETE ON ");
            sb.append(str);
            sb.append(" FOR EACH ROW BEGIN DELETE FROM ");
            sb.append(str2);
            sb.append(" WHERE _id = old._id; END;");
            logExecSql(sQLiteDatabase, sb.toString());
            sb.setLength(0);
        }
    }

    public void dropTables(SQLiteDatabase sQLiteDatabase) {
        String str = this.mTableName;
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ");
        sb.append(str);
        sb.append(DinamicTokenizer.TokenSEM);
        logExecSql(sQLiteDatabase, sb.toString());
        sb.setLength(0);
        if (this.mHasFullTextIndex) {
            sb.append("DROP TABLE IF EXISTS ");
            sb.append(str);
            sb.append(FULL_TEXT_INDEX_SUFFIX);
            sb.append(DinamicTokenizer.TokenSEM);
            logExecSql(sQLiteDatabase, sb.toString());
        }
    }

    public void deleteAll(SQLiteDatabase sQLiteDatabase) {
        logExecSql(sQLiteDatabase, "DELETE FROM " + this.mTableName + ";");
    }

    private String parseTableName(Class<?> cls) {
        Entry.Table table = (Entry.Table) cls.getAnnotation(Entry.Table.class);
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
            Entry.Column column = (Entry.Column) field.getAnnotation(Entry.Column.class);
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
                arrayList.add(new ColumnInfo(column.value(), i, column.indexed(), column.fullText(), column.defaultValue(), field, arrayList.size()));
            }
        }
    }

    public static final class ColumnInfo {
        private static final String ID_KEY = "_id";
        public final String defaultValue;
        public final Field field;
        public final boolean fullText;
        public final boolean indexed;
        public final String name;
        public final int projectionIndex;
        public final int type;

        public ColumnInfo(String str, int i, boolean z, boolean z2, String str2, Field field2, int i2) {
            this.name = str.toLowerCase();
            this.type = i;
            this.indexed = z;
            this.fullText = z2;
            this.defaultValue = str2;
            this.field = field2;
            this.projectionIndex = i2;
            field2.setAccessible(true);
        }

        public boolean isId() {
            return "_id".equals(this.name);
        }
    }
}
