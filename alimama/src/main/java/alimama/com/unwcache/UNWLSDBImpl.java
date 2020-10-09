package alimama.com.unwcache;

import alimama.com.unwbase.interfaces.ILSDB;
import androidx.annotation.NonNull;
import com.ali.protodb.lsdb.Iterator;
import com.ali.protodb.lsdb.Key;
import com.ali.protodb.lsdb.LSDB;
import com.ali.protodb.lsdb.LSDBConfig;

public class UNWLSDBImpl implements ILSDB {
    private LSDBConfig config = new LSDBConfig();
    private LSDB lsdb;
    private String module = "union";

    public UNWLSDBImpl(String str, LSDBConfig lSDBConfig) {
        this.module = str;
        this.config = lSDBConfig;
    }

    public boolean contains(Key key) {
        return getLsdb().contains(key);
    }

    public Iterator<Key> keyIterator() {
        return getLsdb().keyIterator();
    }

    public Iterator<Key> keyIterator(Key key, Key key2) {
        return getLsdb().keyIterator(key, key2);
    }

    public String getString(Key key) {
        return getLsdb().getString(key);
    }

    public int getInt(Key key) {
        return getLsdb().getInt(key);
    }

    public long getLong(Key key) {
        return getLsdb().getLong(key);
    }

    public float getFloat(Key key) {
        return getLsdb().getFloat(key);
    }

    public double getDouble(Key key) {
        return getLsdb().getDouble(key);
    }

    public boolean getBool(Key key) {
        return getLsdb().getBool(key);
    }

    public byte[] getBinary(Key key) {
        return getLsdb().getBinary(key);
    }

    public boolean insertString(Key key, String str) {
        return getLsdb().insertString(key, str);
    }

    public boolean insertInt(Key key, int i) {
        return getLsdb().insertFloat(key, (float) i);
    }

    public boolean insertLong(Key key, long j) {
        return getLsdb().insertFloat(key, (float) j);
    }

    public boolean insertFloat(Key key, float f) {
        return getLsdb().insertFloat(key, f);
    }

    public boolean insertDouble(Key key, double d) {
        return getLsdb().insertDouble(key, d);
    }

    public boolean insertBool(Key key, boolean z) {
        return getLsdb().insertBool(key, z);
    }

    public boolean insertBinary(Key key, byte[] bArr) {
        return getLsdb().insertBinary(key, bArr);
    }

    public boolean delete(Key key) {
        return getLsdb().delete(key);
    }

    public boolean close() {
        return getLsdb().close();
    }

    public boolean forceCompact() {
        return getLsdb().forceCompact();
    }

    public void init() {
        this.lsdb = LSDB.open(this.module, this.config);
    }

    @NonNull
    private LSDB getLsdb() {
        if (this.lsdb == null) {
            this.lsdb = LSDB.open(this.module, this.config);
        }
        return this.lsdb;
    }
}
