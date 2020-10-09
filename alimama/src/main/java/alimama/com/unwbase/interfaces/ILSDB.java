package alimama.com.unwbase.interfaces;

import com.ali.protodb.lsdb.Iterator;
import com.ali.protodb.lsdb.Key;

public interface ILSDB extends IInitAction {
    boolean close();

    boolean contains(Key key);

    boolean delete(Key key);

    boolean forceCompact();

    byte[] getBinary(Key key);

    boolean getBool(Key key);

    double getDouble(Key key);

    float getFloat(Key key);

    int getInt(Key key);

    long getLong(Key key);

    String getString(Key key);

    boolean insertBinary(Key key, byte[] bArr);

    boolean insertBool(Key key, boolean z);

    boolean insertDouble(Key key, double d);

    boolean insertFloat(Key key, float f);

    boolean insertInt(Key key, int i);

    boolean insertLong(Key key, long j);

    boolean insertString(Key key, String str);

    Iterator<Key> keyIterator();

    Iterator<Key> keyIterator(Key key, Key key2);
}
