package com.xiaomi.push.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.taobao.weex.analyzer.Config;
import com.xiaomi.push.gr;

public class TrafficProvider extends ContentProvider {
    private static final UriMatcher a = new UriMatcher(-1);

    /* renamed from: a  reason: collision with other field name */
    public static final Uri f798a = Uri.parse("content://com.xiaomi.push.providers.TrafficProvider/traffic");

    /* renamed from: a  reason: collision with other field name */
    private SQLiteOpenHelper f799a;

    static {
        a.addURI("com.xiaomi.push.providers.TrafficProvider", Config.TYPE_TRAFFIC, 1);
        a.addURI("com.xiaomi.push.providers.TrafficProvider", "update_imsi", 2);
    }

    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        return 0;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    public String getType(Uri uri) {
        if (a.match(uri) == 1) {
            return "vnd.android.cursor.dir/vnd.xiaomi.push.traffic";
        }
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        this.f799a = new a(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor query;
        synchronized (a.f800a) {
            if (a.match(uri) == 1) {
                query = this.f799a.getReadableDatabase().query(Config.TYPE_TRAFFIC, strArr, str, strArr2, (String) null, (String) null, str2);
            } else {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
        return query;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        if (a.match(uri) != 2 || contentValues == null || !contentValues.containsKey("imsi")) {
            return 0;
        }
        gr.a(contentValues.getAsString("imsi"));
        return 0;
    }
}
