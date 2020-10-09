package org.android.agoo.message;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.UTMini;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.MsgDO;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageService {
    private static final String ACCS_SPACE_NAME = "accs_message";
    private static final String ADD_MESSAGE = "accs.add_agoo_message";
    private static final String BODY_CODE = "body_code";
    private static final String CREATE_TIME = "create_time";
    private static final String DATABASE_NAME = "message_accs_db";
    private static final int DATABASE_VERSION = 3;
    private static final String DEAL_MESSAGE = "accs.dealMessage";
    private static final String ID = "id";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_TARGET_TIME = "target_time";
    private static final String MESSAGE_TARGET_TIME_INTERVAL = "interval";
    public static final String MSG_ACCS_NOTIFY_CLICK = "8";
    public static final String MSG_ACCS_NOTIFY_DISMISS = "9";
    public static final String MSG_ACCS_READY_REPORT = "4";
    public static final String MSG_DB_COMPLETE = "100";
    public static final String MSG_DB_NOTIFY_CLICK = "2";
    public static final String MSG_DB_NOTIFY_DISMISS = "3";
    public static final String MSG_DB_NOTIFY_REACHED = "1";
    public static final String MSG_DB_READY_REPORT = "0";
    private static final int NOTICE = 1;
    private static final String NOTIFY = "notify";
    private static final String REPORT = "report";
    private static final String SPACE_NAME = "message";
    private static final String STATE = "state";
    private static final String TAG = "MessageService";
    private static final String TYPE = "type";
    private static Context mContext;
    private static Map<String, Integer> messageStores;
    private volatile SQLiteOpenHelper sqliteOpenHelper = null;

    public void init(Context context) {
        messageStores = new HashMap();
        mContext = context;
        this.sqliteOpenHelper = new MessageDBHelper(context);
    }

    private static class MessageDBHelper extends SQLiteOpenHelper {
        private String getCreateBodyCodeIndexSQL() {
            return "CREATE INDEX body_code_index ON message(body_code)";
        }

        private String getCreateIdIndexSQL() {
            return "CREATE INDEX id_index ON message(id)";
        }

        public MessageDBHelper(Context context) {
            super(context, MessageService.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
        }

        public SQLiteDatabase getWritableDatabase() {
            if (!AdapterUtilityImpl.checkIsWritable(super.getWritableDatabase().getPath(), 102400)) {
                return null;
            }
            return super.getWritableDatabase();
        }

        private String createmMessageTableSQL() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("create table accs_message");
            stringBuffer.append(Operators.BRACKET_START_STR);
            stringBuffer.append("id text UNIQUE not null,");
            stringBuffer.append("state text,");
            stringBuffer.append("message text,");
            stringBuffer.append("create_time date");
            stringBuffer.append(");");
            return stringBuffer.toString();
        }

        private String getCreateTableSQL() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("create table message");
            stringBuffer.append(Operators.BRACKET_START_STR);
            stringBuffer.append("id text UNIQUE not null,");
            stringBuffer.append("state integer,");
            stringBuffer.append("body_code integer,");
            stringBuffer.append("report long,");
            stringBuffer.append("target_time long,");
            stringBuffer.append("interval integer,");
            stringBuffer.append("type text,");
            stringBuffer.append("message text,");
            stringBuffer.append("notify integer,");
            stringBuffer.append("create_time date");
            stringBuffer.append(");");
            return stringBuffer.toString();
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            if (sQLiteDatabase != null) {
                try {
                    sQLiteDatabase.execSQL(getCreateTableSQL());
                    sQLiteDatabase.execSQL(getCreateIdIndexSQL());
                    sQLiteDatabase.execSQL(getCreateBodyCodeIndexSQL());
                    sQLiteDatabase.execSQL(createmMessageTableSQL());
                } catch (Throwable th) {
                    ALog.e(MessageService.TAG, "messagedbhelper create", th, new Object[0]);
                }
            }
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            if (sQLiteDatabase != null) {
                try {
                    sQLiteDatabase.execSQL("delete from message where create_time< date('now','-7 day') and state=1");
                } catch (Throwable th) {
                    ALog.e(MessageService.TAG, "MessageService onUpgrade is error", th, new Object[0]);
                    return;
                }
            }
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS accs_message");
            sQLiteDatabase.execSQL(createmMessageTableSQL());
            return;
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0088 A[Catch:{ all -> 0x007d }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateAccsMessage(java.lang.String r8, java.lang.String r9) {
        /*
            r7 = this;
            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.I
            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)
            r1 = 0
            if (r0 == 0) goto L_0x002e
            java.lang.String r0 = "MessageService"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "updateAccsMessage sqlite3--->["
            r2.append(r3)
            r2.append(r8)
            java.lang.String r3 = ",state="
            r2.append(r3)
            r2.append(r9)
            java.lang.String r3 = "]"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.Object[] r3 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.i(r0, r2, r3)
        L_0x002e:
            r0 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Throwable -> 0x007f }
            if (r2 != 0) goto L_0x007c
            boolean r2 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x007f }
            if (r2 == 0) goto L_0x003c
            goto L_0x007c
        L_0x003c:
            android.database.sqlite.SQLiteOpenHelper r2 = r7.sqliteOpenHelper     // Catch:{ Throwable -> 0x007f }
            android.database.sqlite.SQLiteDatabase r2 = r2.getWritableDatabase()     // Catch:{ Throwable -> 0x007f }
            if (r2 != 0) goto L_0x004a
            if (r2 == 0) goto L_0x0049
            r2.close()
        L_0x0049:
            return
        L_0x004a:
            java.lang.String r0 = "1"
            boolean r0 = android.text.TextUtils.equals(r9, r0)     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r3 = 1
            r4 = 2
            if (r0 == 0) goto L_0x0065
            java.lang.String r0 = "UPDATE accs_message set state = ? where id = ? and state = ?"
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r5[r1] = r9     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r5[r3] = r8     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            java.lang.String r8 = "0"
            r5[r4] = r8     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r2.execSQL(r0, r5)     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            goto L_0x0070
        L_0x0065:
            java.lang.String r0 = "UPDATE accs_message set state = ? where id = ?"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r4[r1] = r9     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r4[r3] = r8     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
            r2.execSQL(r0, r4)     // Catch:{ Throwable -> 0x0079, all -> 0x0076 }
        L_0x0070:
            if (r2 == 0) goto L_0x00d2
            r2.close()
            goto L_0x00d2
        L_0x0076:
            r8 = move-exception
            r0 = r2
            goto L_0x00d3
        L_0x0079:
            r8 = move-exception
            r0 = r2
            goto L_0x0080
        L_0x007c:
            return
        L_0x007d:
            r8 = move-exception
            goto L_0x00d3
        L_0x007f:
            r8 = move-exception
        L_0x0080:
            com.taobao.accs.utl.ALog$Level r9 = com.taobao.accs.utl.ALog.Level.E     // Catch:{ all -> 0x007d }
            boolean r9 = com.taobao.accs.utl.ALog.isPrintLog(r9)     // Catch:{ all -> 0x007d }
            if (r9 == 0) goto L_0x00b5
            java.lang.String r9 = "MessageService"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x007d }
            r2.<init>()     // Catch:{ all -> 0x007d }
            java.lang.String r3 = "updateAccsMessage error,e--->["
            r2.append(r3)     // Catch:{ all -> 0x007d }
            r2.append(r8)     // Catch:{ all -> 0x007d }
            java.lang.String r3 = "]"
            r2.append(r3)     // Catch:{ all -> 0x007d }
            java.lang.String r3 = ",ex="
            r2.append(r3)     // Catch:{ all -> 0x007d }
            java.lang.StackTraceElement[] r3 = r8.getStackTrace()     // Catch:{ all -> 0x007d }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x007d }
            r2.append(r3)     // Catch:{ all -> 0x007d }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x007d }
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x007d }
            com.taobao.accs.utl.ALog.e(r9, r2, r1)     // Catch:{ all -> 0x007d }
        L_0x00b5:
            com.taobao.accs.utl.UTMini r1 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ all -> 0x007d }
            r2 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r3 = "accs.add_agoo_message"
            android.content.Context r9 = mContext     // Catch:{ all -> 0x007d }
            java.lang.String r4 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r9)     // Catch:{ all -> 0x007d }
            java.lang.String r5 = "updateAccsMessageFailed"
            java.lang.String r6 = r8.toString()     // Catch:{ all -> 0x007d }
            r1.commitEvent(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x007d }
            if (r0 == 0) goto L_0x00d2
            r0.close()
        L_0x00d2:
            return
        L_0x00d3:
            if (r0 == 0) goto L_0x00d8
            r0.close()
        L_0x00d8:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.updateAccsMessage(java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ad A[Catch:{ all -> 0x00f9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addAccsMessage(java.lang.String r11, java.lang.String r12, java.lang.String r13) {
        /*
            r10 = this;
            com.taobao.accs.utl.ALog$Level r0 = com.taobao.accs.utl.ALog.Level.I
            boolean r0 = com.taobao.accs.utl.ALog.isPrintLog(r0)
            r1 = 0
            if (r0 == 0) goto L_0x0036
            java.lang.String r0 = "MessageService"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "addAccsMessage sqlite3--->["
            r2.append(r3)
            r2.append(r11)
            java.lang.String r3 = ",message="
            r2.append(r3)
            r2.append(r12)
            java.lang.String r3 = ",state="
            r2.append(r3)
            r2.append(r13)
            java.lang.String r3 = "]"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.Object[] r3 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.i(r0, r2, r3)
        L_0x0036:
            r0 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            if (r2 != 0) goto L_0x009f
            boolean r2 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            if (r2 == 0) goto L_0x0044
            goto L_0x009f
        L_0x0044:
            android.database.sqlite.SQLiteOpenHelper r2 = r10.sqliteOpenHelper     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            android.database.sqlite.SQLiteDatabase r2 = r2.getWritableDatabase()     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            if (r2 != 0) goto L_0x0052
            if (r2 == 0) goto L_0x0051
            r2.close()
        L_0x0051:
            return
        L_0x0052:
            java.lang.String r3 = "select count(1) from accs_message where id = ?"
            r4 = 1
            java.lang.String[] r5 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x009b, all -> 0x0097 }
            r5[r1] = r11     // Catch:{ Throwable -> 0x009b, all -> 0x0097 }
            android.database.Cursor r3 = r2.rawQuery(r3, r5)     // Catch:{ Throwable -> 0x009b, all -> 0x0097 }
            if (r3 == 0) goto L_0x007d
            boolean r0 = r3.moveToFirst()     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            if (r0 == 0) goto L_0x007d
            int r0 = r3.getInt(r1)     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            if (r0 <= 0) goto L_0x007d
            r3.close()     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            if (r3 == 0) goto L_0x0073
            r3.close()
        L_0x0073:
            if (r2 == 0) goto L_0x0078
            r2.close()
        L_0x0078:
            return
        L_0x0079:
            r11 = move-exception
            goto L_0x0099
        L_0x007b:
            r11 = move-exception
            goto L_0x009d
        L_0x007d:
            java.lang.String r0 = "INSERT INTO accs_message VALUES(?,?,?,date('now'))"
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            r5[r1] = r11     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            r5[r4] = r13     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            r11 = 2
            r5[r11] = r12     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            r2.execSQL(r0, r5)     // Catch:{ Throwable -> 0x007b, all -> 0x0079 }
            if (r3 == 0) goto L_0x0091
            r3.close()
        L_0x0091:
            if (r2 == 0) goto L_0x00f8
            r2.close()
            goto L_0x00f8
        L_0x0097:
            r11 = move-exception
            r3 = r0
        L_0x0099:
            r0 = r2
            goto L_0x00fa
        L_0x009b:
            r11 = move-exception
            r3 = r0
        L_0x009d:
            r0 = r2
            goto L_0x00a5
        L_0x009f:
            return
        L_0x00a0:
            r11 = move-exception
            r3 = r0
            goto L_0x00fa
        L_0x00a3:
            r11 = move-exception
            r3 = r0
        L_0x00a5:
            com.taobao.accs.utl.ALog$Level r12 = com.taobao.accs.utl.ALog.Level.E     // Catch:{ all -> 0x00f9 }
            boolean r12 = com.taobao.accs.utl.ALog.isPrintLog(r12)     // Catch:{ all -> 0x00f9 }
            if (r12 == 0) goto L_0x00d6
            java.lang.String r12 = "MessageService"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f9 }
            r13.<init>()     // Catch:{ all -> 0x00f9 }
            java.lang.String r2 = "addAccsMessage error,e--->["
            r13.append(r2)     // Catch:{ all -> 0x00f9 }
            r13.append(r11)     // Catch:{ all -> 0x00f9 }
            java.lang.String r2 = "]"
            r13.append(r2)     // Catch:{ all -> 0x00f9 }
            java.lang.String r2 = ",ex="
            r13.append(r2)     // Catch:{ all -> 0x00f9 }
            java.lang.String r2 = r10.getStackMsg(r11)     // Catch:{ all -> 0x00f9 }
            r13.append(r2)     // Catch:{ all -> 0x00f9 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x00f9 }
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x00f9 }
            com.taobao.accs.utl.ALog.e(r12, r13, r1)     // Catch:{ all -> 0x00f9 }
        L_0x00d6:
            com.taobao.accs.utl.UTMini r4 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ all -> 0x00f9 }
            r5 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r6 = "accs.add_agoo_message"
            android.content.Context r12 = mContext     // Catch:{ all -> 0x00f9 }
            java.lang.String r7 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r12)     // Catch:{ all -> 0x00f9 }
            java.lang.String r8 = "addAccsMessageFailed"
            java.lang.String r9 = r11.toString()     // Catch:{ all -> 0x00f9 }
            r4.commitEvent(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x00f9 }
            if (r3 == 0) goto L_0x00f3
            r3.close()
        L_0x00f3:
            if (r0 == 0) goto L_0x00f8
            r0.close()
        L_0x00f8:
            return
        L_0x00f9:
            r11 = move-exception
        L_0x00fa:
            if (r3 == 0) goto L_0x00ff
            r3.close()
        L_0x00ff:
            if (r0 == 0) goto L_0x0104
            r0.close()
        L_0x0104:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.addAccsMessage(java.lang.String, java.lang.String, java.lang.String):void");
    }

    private String getStackMsg(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                stringBuffer.append(stackTraceElement.toString());
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }

    public void addMessage(String str, String str2, String str3, int i) {
        addMessage(str, str2, str3, 1, -1, -1, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x0128 A[Catch:{ all -> 0x011c }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x015f A[SYNTHETIC, Splitter:B:46:0x015f] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x01a4 A[SYNTHETIC, Splitter:B:55:0x01a4] */
    /* JADX WARNING: Removed duplicated region for block: B:68:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addMessage(java.lang.String r7, java.lang.String r8, java.lang.String r9, int r10, long r11, int r13, int r14) {
        /*
            r6 = this;
            java.lang.String r0 = "MessageService"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "add sqlite3--->["
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = "]"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            java.lang.Object[] r3 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.d(r0, r1, r3)
            r0 = 0
            boolean r1 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Throwable -> 0x011f }
            if (r1 == 0) goto L_0x0029
            java.lang.String r8 = ""
            r1 = -1
            goto L_0x002d
        L_0x0029:
            int r1 = r8.hashCode()     // Catch:{ Throwable -> 0x011f }
        L_0x002d:
            boolean r3 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x011f }
            if (r3 == 0) goto L_0x0035
            java.lang.String r9 = ""
        L_0x0035:
            java.util.Map<java.lang.String, java.lang.Integer> r3 = messageStores     // Catch:{ Throwable -> 0x011f }
            boolean r3 = r3.containsKey(r7)     // Catch:{ Throwable -> 0x011f }
            if (r3 != 0) goto L_0x0074
            java.util.Map<java.lang.String, java.lang.Integer> r3 = messageStores     // Catch:{ Throwable -> 0x011f }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x011f }
            r3.put(r7, r4)     // Catch:{ Throwable -> 0x011f }
            com.taobao.accs.utl.ALog$Level r3 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x011f }
            boolean r3 = com.taobao.accs.utl.ALog.isPrintLog(r3)     // Catch:{ Throwable -> 0x011f }
            if (r3 == 0) goto L_0x0074
            java.lang.String r3 = "MessageService"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x011f }
            r4.<init>()     // Catch:{ Throwable -> 0x011f }
            java.lang.String r5 = "addMessage,messageId="
            r4.append(r5)     // Catch:{ Throwable -> 0x011f }
            r4.append(r7)     // Catch:{ Throwable -> 0x011f }
            java.lang.String r5 = ",messageStores＝"
            r4.append(r5)     // Catch:{ Throwable -> 0x011f }
            java.util.Map<java.lang.String, java.lang.Integer> r5 = messageStores     // Catch:{ Throwable -> 0x011f }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x011f }
            r4.append(r5)     // Catch:{ Throwable -> 0x011f }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x011f }
            java.lang.Object[] r5 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x011f }
            com.taobao.accs.utl.ALog.i(r3, r4, r5)     // Catch:{ Throwable -> 0x011f }
        L_0x0074:
            android.database.sqlite.SQLiteOpenHelper r3 = r6.sqliteOpenHelper     // Catch:{ Throwable -> 0x011f }
            android.database.sqlite.SQLiteDatabase r3 = r3.getWritableDatabase()     // Catch:{ Throwable -> 0x011f }
            if (r3 != 0) goto L_0x00c1
            if (r3 == 0) goto L_0x00c0
            r3.close()     // Catch:{ Throwable -> 0x0082 }
            goto L_0x00c0
        L_0x0082:
            r7 = move-exception
            com.taobao.accs.utl.ALog$Level r8 = com.taobao.accs.utl.ALog.Level.E
            boolean r8 = com.taobao.accs.utl.ALog.isPrintLog(r8)
            if (r8 == 0) goto L_0x00a8
            java.lang.String r8 = "MessageService"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "addMessage,db.close(),error,e--->["
            r9.append(r10)
            r9.append(r7)
            java.lang.String r10 = "]"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object[] r10 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.e(r8, r9, r10)
        L_0x00a8:
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()
            r1 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r2 = "accs.add_agoo_message"
            android.content.Context r8 = mContext
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r8)
            java.lang.String r4 = "addMessageDBcloseFailed"
            java.lang.String r5 = r7.toString()
            r0.commitEvent(r1, r2, r3, r4, r5)
        L_0x00c0:
            return
        L_0x00c1:
            java.lang.String r0 = "INSERT INTO message VALUES(?,?,?,?,?,?,?,?,?,date('now'))"
            r4 = 9
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r2] = r7     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 1
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r7] = r10     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 2
            java.lang.Integer r10 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r7] = r10     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 3
            java.lang.Integer r10 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r7] = r10     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 4
            java.lang.Long r10 = java.lang.Long.valueOf(r11)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r7] = r10     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 5
            java.lang.Integer r10 = java.lang.Integer.valueOf(r13)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r7] = r10     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 6
            r4[r7] = r9     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 7
            r4[r7] = r8     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r7 = 8
            java.lang.Integer r8 = java.lang.Integer.valueOf(r14)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r4[r7] = r8     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            r3.execSQL(r0, r4)     // Catch:{ Throwable -> 0x0119, all -> 0x0115 }
            if (r3 == 0) goto L_0x01a1
            r3.close()     // Catch:{ Throwable -> 0x0104 }
            goto L_0x01a1
        L_0x0104:
            r7 = move-exception
            com.taobao.accs.utl.ALog$Level r8 = com.taobao.accs.utl.ALog.Level.E
            boolean r8 = com.taobao.accs.utl.ALog.isPrintLog(r8)
            if (r8 == 0) goto L_0x0189
            java.lang.String r8 = "MessageService"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            goto L_0x0173
        L_0x0115:
            r7 = move-exception
            r0 = r3
            goto L_0x01a2
        L_0x0119:
            r7 = move-exception
            r0 = r3
            goto L_0x0120
        L_0x011c:
            r7 = move-exception
            goto L_0x01a2
        L_0x011f:
            r7 = move-exception
        L_0x0120:
            com.taobao.accs.utl.ALog$Level r8 = com.taobao.accs.utl.ALog.Level.E     // Catch:{ all -> 0x011c }
            boolean r8 = com.taobao.accs.utl.ALog.isPrintLog(r8)     // Catch:{ all -> 0x011c }
            if (r8 == 0) goto L_0x0145
            java.lang.String r8 = "MessageService"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x011c }
            r9.<init>()     // Catch:{ all -> 0x011c }
            java.lang.String r10 = "addMessage error,e--->["
            r9.append(r10)     // Catch:{ all -> 0x011c }
            r9.append(r7)     // Catch:{ all -> 0x011c }
            java.lang.String r10 = "]"
            r9.append(r10)     // Catch:{ all -> 0x011c }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x011c }
            java.lang.Object[] r10 = new java.lang.Object[r2]     // Catch:{ all -> 0x011c }
            com.taobao.accs.utl.ALog.e(r8, r9, r10)     // Catch:{ all -> 0x011c }
        L_0x0145:
            com.taobao.accs.utl.UTMini r8 = com.taobao.accs.utl.UTMini.getInstance()     // Catch:{ all -> 0x011c }
            r9 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r10 = "accs.add_agoo_message"
            android.content.Context r11 = mContext     // Catch:{ all -> 0x011c }
            java.lang.String r11 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r11)     // Catch:{ all -> 0x011c }
            java.lang.String r12 = "addMessageFailed"
            java.lang.String r13 = r7.toString()     // Catch:{ all -> 0x011c }
            r8.commitEvent(r9, r10, r11, r12, r13)     // Catch:{ all -> 0x011c }
            if (r0 == 0) goto L_0x01a1
            r0.close()     // Catch:{ Throwable -> 0x0163 }
            goto L_0x01a1
        L_0x0163:
            r7 = move-exception
            com.taobao.accs.utl.ALog$Level r8 = com.taobao.accs.utl.ALog.Level.E
            boolean r8 = com.taobao.accs.utl.ALog.isPrintLog(r8)
            if (r8 == 0) goto L_0x0189
            java.lang.String r8 = "MessageService"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
        L_0x0173:
            java.lang.String r10 = "addMessage,db.close(),error,e--->["
            r9.append(r10)
            r9.append(r7)
            java.lang.String r10 = "]"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object[] r10 = new java.lang.Object[r2]
            com.taobao.accs.utl.ALog.e(r8, r9, r10)
        L_0x0189:
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()
            r1 = 66002(0x101d2, float:9.2489E-41)
            java.lang.String r2 = "accs.add_agoo_message"
            android.content.Context r8 = mContext
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r8)
            java.lang.String r4 = "addMessageDBcloseFailed"
            java.lang.String r5 = r7.toString()
            r0.commitEvent(r1, r2, r3, r4, r5)
        L_0x01a1:
            return
        L_0x01a2:
            if (r0 == 0) goto L_0x01e6
            r0.close()     // Catch:{ Throwable -> 0x01a8 }
            goto L_0x01e6
        L_0x01a8:
            r8 = move-exception
            com.taobao.accs.utl.ALog$Level r9 = com.taobao.accs.utl.ALog.Level.E
            boolean r9 = com.taobao.accs.utl.ALog.isPrintLog(r9)
            if (r9 == 0) goto L_0x01ce
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "addMessage,db.close(),error,e--->["
            r9.append(r10)
            r9.append(r8)
            java.lang.String r10 = "]"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object[] r10 = new java.lang.Object[r2]
            java.lang.String r11 = "MessageService"
            com.taobao.accs.utl.ALog.e(r11, r9, r10)
        L_0x01ce:
            com.taobao.accs.utl.UTMini r0 = com.taobao.accs.utl.UTMini.getInstance()
            r1 = 66002(0x101d2, float:9.2489E-41)
            android.content.Context r9 = mContext
            java.lang.String r3 = com.taobao.accs.utl.AdapterUtilityImpl.getDeviceId(r9)
            java.lang.String r5 = r8.toString()
            java.lang.String r2 = "accs.add_agoo_message"
            java.lang.String r4 = "addMessageDBcloseFailed"
            r0.commitEvent(r1, r2, r3, r4, r5)
        L_0x01e6:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.addMessage(java.lang.String, java.lang.String, java.lang.String, int, long, int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x003b A[SYNTHETIC, Splitter:B:26:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void deleteCacheMessage() {
        /*
            r6 = this;
            r0 = 0
            android.database.sqlite.SQLiteOpenHelper r1 = r6.sqliteOpenHelper     // Catch:{ Throwable -> 0x0026, all -> 0x0021 }
            android.database.sqlite.SQLiteDatabase r1 = r1.getWritableDatabase()     // Catch:{ Throwable -> 0x0026, all -> 0x0021 }
            if (r1 != 0) goto L_0x000f
            if (r1 == 0) goto L_0x000e
            r1.close()     // Catch:{ Throwable -> 0x000e }
        L_0x000e:
            return
        L_0x000f:
            java.lang.String r0 = "delete from message where create_time< date('now','-7 day') and state=1"
            r1.execSQL(r0)     // Catch:{ Throwable -> 0x001f }
            java.lang.String r0 = "delete from accs_message where create_time< date('now','-1 day') "
            r1.execSQL(r0)     // Catch:{ Throwable -> 0x001f }
            if (r1 == 0) goto L_0x0037
        L_0x001b:
            r1.close()     // Catch:{ Throwable -> 0x0037 }
            goto L_0x0037
        L_0x001f:
            r0 = move-exception
            goto L_0x002a
        L_0x0021:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x0039
        L_0x0026:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
        L_0x002a:
            java.lang.String r2 = "MessageService"
            java.lang.String r3 = "deleteCacheMessage sql Throwable"
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0038 }
            com.taobao.accs.utl.ALog.e(r2, r3, r0, r4)     // Catch:{ all -> 0x0038 }
            if (r1 == 0) goto L_0x0037
            goto L_0x001b
        L_0x0037:
            return
        L_0x0038:
            r0 = move-exception
        L_0x0039:
            if (r1 == 0) goto L_0x003e
            r1.close()     // Catch:{ Throwable -> 0x003e }
        L_0x003e:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.deleteCacheMessage():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0138, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0139, code lost:
        r4 = null;
        r0 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x013c, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x013d, code lost:
        r4 = null;
        r0 = r3;
        r3 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0138 A[ExcHandler: all (r3v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:12:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0153 A[Catch:{ all -> 0x018a }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x016d A[SYNTHETIC, Splitter:B:70:0x016d] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0175 A[Catch:{ Throwable -> 0x0171 }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x018d A[SYNTHETIC, Splitter:B:82:0x018d] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0195 A[Catch:{ Throwable -> 0x0191 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<org.android.agoo.common.MsgDO> getUnReportMsg() {
        /*
            r15 = this;
            r0 = 0
            r1 = 0
            android.database.sqlite.SQLiteOpenHelper r2 = r15.sqliteOpenHelper     // Catch:{ Throwable -> 0x0146, all -> 0x0141 }
            android.database.sqlite.SQLiteDatabase r2 = r2.getReadableDatabase()     // Catch:{ Throwable -> 0x0146, all -> 0x0141 }
            if (r2 != 0) goto L_0x0032
            if (r2 == 0) goto L_0x0031
            r2.close()     // Catch:{ Throwable -> 0x0010 }
            goto L_0x0031
        L_0x0010:
            r2 = move-exception
            com.taobao.accs.utl.ALog$Level r3 = com.taobao.accs.utl.ALog.Level.E
            boolean r3 = com.taobao.accs.utl.ALog.isPrintLog(r3)
            if (r3 == 0) goto L_0x0031
            java.lang.String r3 = "MessageService"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "getUnReportMsg close cursor or db, e: "
            r4.append(r5)
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.e(r3, r2, r1)
        L_0x0031:
            return r0
        L_0x0032:
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ Throwable -> 0x013c, all -> 0x0138 }
            r3.<init>()     // Catch:{ Throwable -> 0x013c, all -> 0x0138 }
            java.lang.String r4 = "select * from accs_message where state = ? or state = ? or state = ?"
            r5 = 3
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ Throwable -> 0x0133, all -> 0x0138 }
            java.lang.String r6 = "0"
            r5[r1] = r6     // Catch:{ Throwable -> 0x0133, all -> 0x0138 }
            r6 = 1
            java.lang.String r7 = "2"
            r5[r6] = r7     // Catch:{ Throwable -> 0x0133, all -> 0x0138 }
            r6 = 2
            java.lang.String r7 = "3"
            r5[r6] = r7     // Catch:{ Throwable -> 0x0133, all -> 0x0138 }
            android.database.Cursor r4 = r2.rawQuery(r4, r5)     // Catch:{ Throwable -> 0x0133, all -> 0x0138 }
            if (r4 == 0) goto L_0x0102
            java.lang.String r5 = "id"
            int r5 = r4.getColumnIndex(r5)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r6 = "state"
            int r6 = r4.getColumnIndex(r6)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r7 = "message"
            int r7 = r4.getColumnIndex(r7)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r8 = "create_time"
            int r8 = r4.getColumnIndex(r8)     // Catch:{ Throwable -> 0x0100 }
        L_0x0068:
            boolean r9 = r4.moveToNext()     // Catch:{ Throwable -> 0x0100 }
            if (r9 == 0) goto L_0x0102
            java.lang.String r9 = r4.getString(r7)     // Catch:{ Throwable -> 0x0100 }
            boolean r9 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0100 }
            if (r9 != 0) goto L_0x0102
            java.lang.String r9 = r4.getString(r6)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r10 = r4.getString(r7)     // Catch:{ Throwable -> 0x0100 }
            com.taobao.accs.utl.ALog$Level r11 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x0100 }
            boolean r11 = com.taobao.accs.utl.ALog.isPrintLog(r11)     // Catch:{ Throwable -> 0x0100 }
            if (r11 == 0) goto L_0x00c0
            java.lang.String r11 = "MessageService"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0100 }
            r12.<init>()     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r13 = "state: "
            r12.append(r13)     // Catch:{ Throwable -> 0x0100 }
            r12.append(r9)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r13 = " ,cursor.message:"
            r12.append(r13)     // Catch:{ Throwable -> 0x0100 }
            r12.append(r10)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r13 = " ,cursor.id:"
            r12.append(r13)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r13 = r4.getString(r5)     // Catch:{ Throwable -> 0x0100 }
            r12.append(r13)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r13 = " ,cursor.time:"
            r12.append(r13)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r13 = r4.getString(r8)     // Catch:{ Throwable -> 0x0100 }
            r12.append(r13)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r12 = r12.toString()     // Catch:{ Throwable -> 0x0100 }
            java.lang.Object[] r13 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0100 }
            com.taobao.accs.utl.ALog.i(r11, r12, r13)     // Catch:{ Throwable -> 0x0100 }
        L_0x00c0:
            java.lang.String r11 = "0"
            boolean r11 = android.text.TextUtils.equals(r11, r9)     // Catch:{ Throwable -> 0x0100 }
            if (r11 == 0) goto L_0x00cb
            java.lang.String r9 = "4"
            goto L_0x00e2
        L_0x00cb:
            java.lang.String r11 = "2"
            boolean r11 = android.text.TextUtils.equals(r11, r9)     // Catch:{ Throwable -> 0x0100 }
            if (r11 == 0) goto L_0x00d6
            java.lang.String r9 = "8"
            goto L_0x00e2
        L_0x00d6:
            java.lang.String r11 = "3"
            boolean r9 = android.text.TextUtils.equals(r11, r9)     // Catch:{ Throwable -> 0x0100 }
            if (r9 == 0) goto L_0x00e1
            java.lang.String r9 = "9"
            goto L_0x00e2
        L_0x00e1:
            r9 = r0
        L_0x00e2:
            org.android.agoo.common.MsgDO r11 = new org.android.agoo.common.MsgDO     // Catch:{ Throwable -> 0x0100 }
            r11.<init>()     // Catch:{ Throwable -> 0x0100 }
            boolean r11 = android.text.TextUtils.isEmpty(r10)     // Catch:{ Throwable -> 0x0100 }
            if (r11 != 0) goto L_0x0068
            boolean r11 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0100 }
            if (r11 != 0) goto L_0x0068
            org.android.agoo.common.MsgDO r9 = r15.createMsg(r10, r9)     // Catch:{ Throwable -> 0x0100 }
            java.lang.String r10 = "cache"
            r9.messageSource = r10     // Catch:{ Throwable -> 0x0100 }
            r3.add(r9)     // Catch:{ Throwable -> 0x0100 }
            goto L_0x0068
        L_0x0100:
            r0 = move-exception
            goto L_0x014b
        L_0x0102:
            if (r4 == 0) goto L_0x010a
            r4.close()     // Catch:{ Throwable -> 0x0108 }
            goto L_0x010a
        L_0x0108:
            r0 = move-exception
            goto L_0x0111
        L_0x010a:
            if (r2 == 0) goto L_0x0189
            r2.close()     // Catch:{ Throwable -> 0x0108 }
            goto L_0x0189
        L_0x0111:
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.E
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            if (r2 == 0) goto L_0x0189
            java.lang.String r2 = "MessageService"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
        L_0x0120:
            java.lang.String r5 = "getUnReportMsg close cursor or db, e: "
            r4.append(r5)
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.e(r2, r0, r1)
            goto L_0x0189
        L_0x0133:
            r4 = move-exception
            r14 = r4
            r4 = r0
            r0 = r14
            goto L_0x014b
        L_0x0138:
            r3 = move-exception
            r4 = r0
            r0 = r3
            goto L_0x018b
        L_0x013c:
            r3 = move-exception
            r4 = r0
            r0 = r3
            r3 = r4
            goto L_0x014b
        L_0x0141:
            r2 = move-exception
            r4 = r0
            r0 = r2
            r2 = r4
            goto L_0x018b
        L_0x0146:
            r2 = move-exception
            r3 = r0
            r4 = r3
            r0 = r2
            r2 = r4
        L_0x014b:
            com.taobao.accs.utl.ALog$Level r5 = com.taobao.accs.utl.ALog.Level.E     // Catch:{ all -> 0x018a }
            boolean r5 = com.taobao.accs.utl.ALog.isPrintLog(r5)     // Catch:{ all -> 0x018a }
            if (r5 == 0) goto L_0x016b
            java.lang.String r5 = "MessageService"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x018a }
            r6.<init>()     // Catch:{ all -> 0x018a }
            java.lang.String r7 = "getUnReportMsg, e: "
            r6.append(r7)     // Catch:{ all -> 0x018a }
            r6.append(r0)     // Catch:{ all -> 0x018a }
            java.lang.String r0 = r6.toString()     // Catch:{ all -> 0x018a }
            java.lang.Object[] r6 = new java.lang.Object[r1]     // Catch:{ all -> 0x018a }
            com.taobao.accs.utl.ALog.e(r5, r0, r6)     // Catch:{ all -> 0x018a }
        L_0x016b:
            if (r4 == 0) goto L_0x0173
            r4.close()     // Catch:{ Throwable -> 0x0171 }
            goto L_0x0173
        L_0x0171:
            r0 = move-exception
            goto L_0x0179
        L_0x0173:
            if (r2 == 0) goto L_0x0189
            r2.close()     // Catch:{ Throwable -> 0x0171 }
            goto L_0x0189
        L_0x0179:
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.E
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)
            if (r2 == 0) goto L_0x0189
            java.lang.String r2 = "MessageService"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            goto L_0x0120
        L_0x0189:
            return r3
        L_0x018a:
            r0 = move-exception
        L_0x018b:
            if (r4 == 0) goto L_0x0193
            r4.close()     // Catch:{ Throwable -> 0x0191 }
            goto L_0x0193
        L_0x0191:
            r2 = move-exception
            goto L_0x0199
        L_0x0193:
            if (r2 == 0) goto L_0x01b9
            r2.close()     // Catch:{ Throwable -> 0x0191 }
            goto L_0x01b9
        L_0x0199:
            com.taobao.accs.utl.ALog$Level r3 = com.taobao.accs.utl.ALog.Level.E
            boolean r3 = com.taobao.accs.utl.ALog.isPrintLog(r3)
            if (r3 == 0) goto L_0x01b9
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "getUnReportMsg close cursor or db, e: "
            r3.append(r4)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r3 = "MessageService"
            com.taobao.accs.utl.ALog.e(r3, r2, r1)
        L_0x01b9:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.getUnReportMsg():java.util.ArrayList");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0062, code lost:
        if (r4 != null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0064, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006a, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x006c, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x006d, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0080, code lost:
        if (r4 == null) goto L_0x0083;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x006c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0002] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0070 A[SYNTHETIC, Splitter:B:42:0x0070] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0075 A[Catch:{ Throwable -> 0x0078 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x007d A[SYNTHETIC, Splitter:B:52:0x007d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasMessageDuplicate(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            r1 = 0
            java.util.Map<java.lang.String, java.lang.Integer> r2 = messageStores     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            boolean r2 = r2.containsKey(r9)     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            r3 = 1
            if (r2 == 0) goto L_0x002d
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.E     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            if (r2 == 0) goto L_0x002b
            java.lang.String r2 = "MessageService"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            r4.<init>()     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            java.lang.String r5 = "hasMessageDuplicate,msgid="
            r4.append(r5)     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            r4.append(r9)     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
            com.taobao.accs.utl.ALog.e(r2, r4, r5)     // Catch:{ Throwable -> 0x0079, all -> 0x006c }
        L_0x002b:
            r2 = 1
            goto L_0x002e
        L_0x002d:
            r2 = 0
        L_0x002e:
            android.database.sqlite.SQLiteOpenHelper r4 = r8.sqliteOpenHelper     // Catch:{ Throwable -> 0x006a, all -> 0x006c }
            android.database.sqlite.SQLiteDatabase r4 = r4.getReadableDatabase()     // Catch:{ Throwable -> 0x006a, all -> 0x006c }
            if (r4 != 0) goto L_0x003c
            if (r4 == 0) goto L_0x003b
            r4.close()     // Catch:{ Throwable -> 0x003b }
        L_0x003b:
            return r2
        L_0x003c:
            java.lang.String r5 = "select count(1) from message where id = ?"
            java.lang.String[] r6 = new java.lang.String[r3]     // Catch:{ Throwable -> 0x007b, all -> 0x0068 }
            r6[r1] = r9     // Catch:{ Throwable -> 0x007b, all -> 0x0068 }
            android.database.Cursor r9 = r4.rawQuery(r5, r6)     // Catch:{ Throwable -> 0x007b, all -> 0x0068 }
            if (r9 == 0) goto L_0x005d
            boolean r0 = r9.moveToFirst()     // Catch:{ Throwable -> 0x005b, all -> 0x0056 }
            if (r0 == 0) goto L_0x005d
            int r0 = r9.getInt(r1)     // Catch:{ Throwable -> 0x005b, all -> 0x0056 }
            if (r0 <= 0) goto L_0x005d
            r2 = 1
            goto L_0x005d
        L_0x0056:
            r0 = move-exception
            r7 = r0
            r0 = r9
            r9 = r7
            goto L_0x006e
        L_0x005b:
            r0 = r9
            goto L_0x007b
        L_0x005d:
            if (r9 == 0) goto L_0x0062
            r9.close()     // Catch:{ Throwable -> 0x0083 }
        L_0x0062:
            if (r4 == 0) goto L_0x0083
        L_0x0064:
            r4.close()     // Catch:{ Throwable -> 0x0083 }
            goto L_0x0083
        L_0x0068:
            r9 = move-exception
            goto L_0x006e
        L_0x006a:
            r4 = r0
            goto L_0x007b
        L_0x006c:
            r9 = move-exception
            r4 = r0
        L_0x006e:
            if (r0 == 0) goto L_0x0073
            r0.close()     // Catch:{ Throwable -> 0x0078 }
        L_0x0073:
            if (r4 == 0) goto L_0x0078
            r4.close()     // Catch:{ Throwable -> 0x0078 }
        L_0x0078:
            throw r9
        L_0x0079:
            r4 = r0
            r2 = 0
        L_0x007b:
            if (r0 == 0) goto L_0x0080
            r0.close()     // Catch:{ Throwable -> 0x0083 }
        L_0x0080:
            if (r4 == 0) goto L_0x0083
            goto L_0x0064
        L_0x0083:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.hasMessageDuplicate(java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0081, code lost:
        if (r4 != null) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0083, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0089, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008b, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x008c, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x009f, code lost:
        if (r4 == null) goto L_0x00a2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008b A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0002] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x008f A[SYNTHETIC, Splitter:B:44:0x008f] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0094 A[Catch:{ Throwable -> 0x0097 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x009c A[SYNTHETIC, Splitter:B:54:0x009c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasMessageDuplicate(java.lang.String r9, int r10) {
        /*
            r8 = this;
            r0 = 0
            r1 = 0
            java.util.Map<java.lang.String, java.lang.Integer> r2 = messageStores     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            boolean r2 = r2.containsKey(r9)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            r3 = 1
            if (r2 == 0) goto L_0x0039
            java.util.Map<java.lang.String, java.lang.Integer> r2 = messageStores     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            boolean r2 = r2.containsValue(r4)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            if (r2 == 0) goto L_0x0039
            com.taobao.accs.utl.ALog$Level r2 = com.taobao.accs.utl.ALog.Level.I     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            boolean r2 = com.taobao.accs.utl.ALog.isPrintLog(r2)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            if (r2 == 0) goto L_0x0037
            java.lang.String r2 = "MessageService"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            r4.<init>()     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            java.lang.String r5 = "addMessage,messageStores hasMessageDuplicate,msgid="
            r4.append(r5)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            r4.append(r9)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
            com.taobao.accs.utl.ALog.i(r2, r4, r5)     // Catch:{ Throwable -> 0x0098, all -> 0x008b }
        L_0x0037:
            r2 = 1
            goto L_0x003a
        L_0x0039:
            r2 = 0
        L_0x003a:
            android.database.sqlite.SQLiteOpenHelper r4 = r8.sqliteOpenHelper     // Catch:{ Throwable -> 0x0089, all -> 0x008b }
            android.database.sqlite.SQLiteDatabase r4 = r4.getReadableDatabase()     // Catch:{ Throwable -> 0x0089, all -> 0x008b }
            if (r4 != 0) goto L_0x0048
            if (r4 == 0) goto L_0x0047
            r4.close()     // Catch:{ Throwable -> 0x0047 }
        L_0x0047:
            return r2
        L_0x0048:
            java.lang.String r5 = "select count(1) from message where id = ? and body_code=? create_time< date('now','-1 day')"
            r6 = 2
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            r6[r1] = r9     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            r9.<init>()     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            java.lang.String r7 = ""
            r9.append(r7)     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            r9.append(r10)     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            r6[r3] = r9     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            android.database.Cursor r9 = r4.rawQuery(r5, r6)     // Catch:{ Throwable -> 0x009a, all -> 0x0087 }
            if (r9 == 0) goto L_0x007c
            boolean r10 = r9.moveToFirst()     // Catch:{ Throwable -> 0x007a, all -> 0x0076 }
            if (r10 == 0) goto L_0x007c
            int r10 = r9.getInt(r1)     // Catch:{ Throwable -> 0x007a, all -> 0x0076 }
            if (r10 <= 0) goto L_0x007c
            r2 = 1
            goto L_0x007c
        L_0x0076:
            r10 = move-exception
            r0 = r9
            r9 = r10
            goto L_0x008d
        L_0x007a:
            r0 = r9
            goto L_0x009a
        L_0x007c:
            if (r9 == 0) goto L_0x0081
            r9.close()     // Catch:{ Throwable -> 0x00a2 }
        L_0x0081:
            if (r4 == 0) goto L_0x00a2
        L_0x0083:
            r4.close()     // Catch:{ Throwable -> 0x00a2 }
            goto L_0x00a2
        L_0x0087:
            r9 = move-exception
            goto L_0x008d
        L_0x0089:
            r4 = r0
            goto L_0x009a
        L_0x008b:
            r9 = move-exception
            r4 = r0
        L_0x008d:
            if (r0 == 0) goto L_0x0092
            r0.close()     // Catch:{ Throwable -> 0x0097 }
        L_0x0092:
            if (r4 == 0) goto L_0x0097
            r4.close()     // Catch:{ Throwable -> 0x0097 }
        L_0x0097:
            throw r9
        L_0x0098:
            r4 = r0
            r2 = 0
        L_0x009a:
            if (r0 == 0) goto L_0x009f
            r0.close()     // Catch:{ Throwable -> 0x00a2 }
        L_0x009f:
            if (r4 == 0) goto L_0x00a2
            goto L_0x0083
        L_0x00a2:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.message.MessageService.hasMessageDuplicate(java.lang.String, int):boolean");
    }

    private MsgDO createMsg(String str, String str2) {
        boolean z;
        String str3 = str;
        int i = 0;
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "msgRecevie,message--->[" + str3 + Operators.ARRAY_END_STR + ",utdid=" + AdapterUtilityImpl.getDeviceId(mContext), new Object[0]);
        }
        if (TextUtils.isEmpty(str)) {
            UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, DEAL_MESSAGE, AdapterUtilityImpl.getDeviceId(mContext), "message==null");
            if (ALog.isPrintLog(ALog.Level.I)) {
                ALog.i(TAG, "handleMessage message==null,utdid=" + AdapterUtilityImpl.getDeviceId(mContext), new Object[0]);
            }
            return null;
        }
        MsgDO msgDO = new MsgDO();
        try {
            JSONArray jSONArray = new JSONArray(str3);
            int length = jSONArray.length();
            new Bundle();
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            String str4 = null;
            int i2 = 0;
            while (i2 < length) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                if (jSONObject != null) {
                    String string = jSONObject.getString("p");
                    String string2 = jSONObject.getString(UploadQueueMgr.MSGTYPE_INTERVAL);
                    String string3 = jSONObject.getString("b");
                    long j = jSONObject.getLong("f");
                    sb.append(string2);
                    if (!jSONObject.isNull(ApiConstants.ApiField.EXT)) {
                        str4 = jSONObject.getString(ApiConstants.ApiField.EXT);
                    }
                    int i3 = length - 1;
                    if (i2 < i3) {
                        sb.append(",");
                    }
                    msgDO.msgIds = string2;
                    msgDO.extData = str4;
                    msgDO.messageSource = "accs";
                    msgDO.type = "cache";
                    if (TextUtils.isEmpty(string3)) {
                        msgDO.errorCode = "11";
                    } else if (TextUtils.isEmpty(string)) {
                        msgDO.errorCode = "12";
                    } else if (j == -1) {
                        msgDO.errorCode = "13";
                    } else if (!checkPackage(mContext, string)) {
                        ALog.d(TAG, "ondata checkpackage is del,pack=" + string, new Object[i]);
                        UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, DEAL_MESSAGE, AdapterUtilityImpl.getDeviceId(mContext), "deletePack", string);
                        sb3.append(string);
                        sb2.append(string2);
                        msgDO.removePacks = string;
                        if (i2 < i3) {
                            sb3.append(",");
                            sb2.append(",");
                        }
                    } else {
                        String string4 = getFlag(j, msgDO).getString(AgooConstants.MESSAGE_ENCRYPTED);
                        if (!mContext.getPackageName().equals(string)) {
                            z = true;
                        } else if (TextUtils.equals(Integer.toString(0), string4) || TextUtils.equals(Integer.toString(4), string4)) {
                            z = false;
                        } else {
                            msgDO.errorCode = "15";
                            ALog.e(TAG, "error encrypted: " + string4, new Object[0]);
                        }
                        msgDO.agooFlag = z;
                        if (!TextUtils.isEmpty(str2)) {
                            msgDO.msgStatus = str2;
                            i2++;
                            i = 0;
                        }
                    }
                }
                String str5 = str2;
                i2++;
                i = 0;
            }
        } catch (Throwable th) {
            if (ALog.isPrintLog(ALog.Level.E)) {
                ALog.e(TAG, "createMsg is error,e: " + th, new Object[0]);
            }
        }
        return msgDO;
    }

    public static final boolean checkPackage(Context context, String str) {
        try {
            if (context.getPackageManager().getApplicationInfo(str, 0) != null) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
        }
    }

    private static Bundle getFlag(long j, MsgDO msgDO) {
        Bundle bundle = new Bundle();
        try {
            char[] charArray = Long.toBinaryString(j).toCharArray();
            if (charArray != null && 8 <= charArray.length) {
                if (8 <= charArray.length) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(Integer.parseInt("" + charArray[1] + charArray[2] + charArray[3] + charArray[4], 2));
                    bundle.putString(AgooConstants.MESSAGE_ENCRYPTED, sb.toString());
                    if (charArray[6] == '1') {
                        bundle.putString("report", "1");
                        msgDO.reportStr = "1";
                    }
                    if (charArray[7] == '1') {
                        bundle.putString("notify", "1");
                    }
                }
                if (9 <= charArray.length && charArray[8] == '1') {
                    bundle.putString(AgooConstants.MESSAGE_HAS_TEST, "1");
                }
                if (10 <= charArray.length && charArray[9] == '1') {
                    bundle.putString(AgooConstants.MESSAGE_DUPLICATE, "1");
                }
                if (11 <= charArray.length && charArray[10] == '1') {
                    bundle.putInt("popup", 1);
                }
            }
        } catch (Throwable unused) {
        }
        return bundle;
    }
}
