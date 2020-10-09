package com.ali.protodb.lsdb;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class LSDBDefaultImpl extends LSDB {
    private final SharedPreferences sharedPreferences;

    public boolean close() {
        return false;
    }

    public boolean forceCompact() {
        return false;
    }

    public byte[] getBinary(Key key) {
        return null;
    }

    public ByteBuffer getBuffer(@NonNull Key key) {
        return null;
    }

    public long getDataSize(@NonNull Key key) {
        return 0;
    }

    public boolean insertBinary(Key key, byte[] bArr) {
        return false;
    }

    public boolean insertBuffer(@NonNull Key key, ByteBuffer byteBuffer) {
        return false;
    }

    public boolean insertStream(Key key, InputStream inputStream) {
        return false;
    }

    public Iterator<Key> keyIterator(Key key, Key key2) {
        return null;
    }

    LSDBDefaultImpl(Context context) {
        super(0, "");
        this.sharedPreferences = context.getSharedPreferences("lsdb", 0);
    }

    public boolean contains(Key key) {
        return this.sharedPreferences.contains(key.getStringKey());
    }

    public Iterator<Key> keyIterator() {
        return new KeyIterator((String[]) this.sharedPreferences.getAll().keySet().toArray(new String[0]));
    }

    public String getString(Key key) {
        return this.sharedPreferences.getString(key.getStringKey(), "");
    }

    public int getInt(Key key) {
        return this.sharedPreferences.getInt(key.getStringKey(), 0);
    }

    public long getLong(Key key) {
        return this.sharedPreferences.getLong(key.getStringKey(), 0);
    }

    public float getFloat(Key key) {
        return this.sharedPreferences.getFloat(key.getStringKey(), 0.0f);
    }

    public double getDouble(Key key) {
        return (double) getFloat(key);
    }

    public boolean getBool(Key key) {
        return this.sharedPreferences.getBoolean(key.getStringKey(), false);
    }

    public boolean insertString(Key key, String str) {
        this.sharedPreferences.edit().putString(key.getStringKey(), str).apply();
        return true;
    }

    public boolean insertInt(Key key, int i) {
        this.sharedPreferences.edit().putInt(key.getStringKey(), i).apply();
        return true;
    }

    public boolean insertLong(Key key, long j) {
        this.sharedPreferences.edit().putLong(key.getStringKey(), j).apply();
        return true;
    }

    public boolean insertFloat(Key key, float f) {
        this.sharedPreferences.edit().putFloat(key.getStringKey(), f).apply();
        return true;
    }

    public boolean insertDouble(Key key, double d) {
        this.sharedPreferences.edit().putFloat(key.getStringKey(), (float) d).apply();
        return true;
    }

    public boolean insertBool(Key key, boolean z) {
        this.sharedPreferences.edit().putBoolean(key.getStringKey(), z).apply();
        return true;
    }

    public boolean delete(Key key) {
        this.sharedPreferences.edit().remove(key.getStringKey()).apply();
        return true;
    }
}
