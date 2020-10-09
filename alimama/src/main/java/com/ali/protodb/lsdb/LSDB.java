package com.ali.protodb.lsdb;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import com.ali.protodb.NativeBridgedObject;
import com.taobao.alivfsadapter.utils.AVFSApplicationUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LSDB extends NativeBridgedObject {
    private static ConcurrentLinkedQueue<LSDB> sLSDBInstances = new ConcurrentLinkedQueue<>();
    private final String path;
    private final String tag = "ProtoDB";

    @Keep
    private native boolean nativeClose();

    @Keep
    private native boolean nativeCompact();

    @Keep
    private native boolean nativeContains(String str);

    @Keep
    private native boolean nativeDelete(String str);

    @Keep
    private native byte[] nativeGet(String str);

    @Keep
    private native ByteBuffer nativeGetBuffer(String str);

    @Keep
    private native long nativeGetDataSize(String str);

    @Keep
    private native boolean nativeInsert(String str, byte[] bArr);

    @Keep
    private native boolean nativeInsertBuffer(String str, ByteBuffer byteBuffer);

    @Keep
    private native String[] nativeKeyIterator(String str, String str2);

    @Keep
    private static native long nativeOpen(String str, LSDBConfig lSDBConfig);

    public static LSDB open(String str, LSDBConfig lSDBConfig) {
        Application application = AVFSApplicationUtils.getApplication();
        if (application == null) {
            throw new RuntimeException("failed to get android context!");
        } else if (Build.VERSION.SDK_INT < 21 || !sNativeLibraryLoaded) {
            return new LSDBDefaultImpl(application);
        } else {
            LSDB openInternal = openInternal(application.getFilesDir() + File.separator + "lsdb-" + str, lSDBConfig);
            return openInternal != null ? openInternal : new LSDBDefaultImpl(application);
        }
    }

    public static LSDB open(Context context, String str, LSDBConfig lSDBConfig) {
        if (Build.VERSION.SDK_INT < 21 || !sNativeLibraryLoaded) {
            return null;
        }
        LSDB openInternal = openInternal(context.getFilesDir() + File.separator + "lsdb-" + str, lSDBConfig);
        return openInternal != null ? openInternal : new LSDBDefaultImpl(context);
    }

    public static void compactAll() {
        Iterator<LSDB> it = sLSDBInstances.iterator();
        while (it.hasNext()) {
            it.next().forceCompact();
        }
    }

    private static LSDB openInternal(String str, LSDBConfig lSDBConfig) {
        long nativeOpen = nativeOpen(str, lSDBConfig);
        if (nativeOpen <= 0) {
            return null;
        }
        LSDB lsdb = new LSDB(nativeOpen, str);
        sLSDBInstances.add(lsdb);
        return lsdb;
    }

    LSDB(long j, String str) {
        super(j);
        this.path = str;
    }

    public boolean contains(@NonNull Key key) {
        return nativeContains(key.getStringKey());
    }

    public long getDataSize(@NonNull Key key) {
        return nativeGetDataSize(key.getStringKey());
    }

    public Iterator<Key> keyIterator() {
        return new KeyIterator(nativeKeyIterator((String) null, (String) null));
    }

    public Iterator<Key> keyIterator(@NonNull Key key, @NonNull Key key2) {
        return new KeyIterator(nativeKeyIterator(key.getStringKey(), key2.getStringKey()));
    }

    public String getString(@NonNull Key key) {
        byte[] nativeGet = nativeGet(key.getStringKey());
        if (nativeGet == null || nativeGet.length <= 0) {
            return null;
        }
        return new String(nativeGet, StandardCharsets.UTF_8);
    }

    public int getInt(@NonNull Key key) {
        byte[] nativeGet = nativeGet(key.getStringKey());
        if (nativeGet == null || nativeGet.length != 4) {
            return 0;
        }
        return ByteBuffer.wrap(nativeGet).getInt();
    }

    public long getLong(@NonNull Key key) {
        byte[] nativeGet = nativeGet(key.getStringKey());
        if (nativeGet == null || nativeGet.length != 8) {
            return 0;
        }
        return ByteBuffer.wrap(nativeGet).getLong();
    }

    public float getFloat(@NonNull Key key) {
        byte[] nativeGet = nativeGet(key.getStringKey());
        if (nativeGet == null || nativeGet.length != 4) {
            return 0.0f;
        }
        return ByteBuffer.wrap(nativeGet).getFloat();
    }

    public double getDouble(@NonNull Key key) {
        byte[] nativeGet = nativeGet(key.getStringKey());
        if (nativeGet == null || nativeGet.length != 8) {
            return 0.0d;
        }
        return ByteBuffer.wrap(nativeGet).getDouble();
    }

    public boolean getBool(@NonNull Key key) {
        byte[] nativeGet = nativeGet(key.getStringKey());
        if (nativeGet == null || ByteBuffer.wrap(nativeGet).get() == 0) {
            return false;
        }
        return true;
    }

    public byte[] getBinary(@NonNull Key key) {
        return nativeGet(key.getStringKey());
    }

    public ByteBuffer getBuffer(@NonNull Key key) {
        return nativeGetBuffer(key.getStringKey());
    }

    public boolean insertString(@NonNull Key key, @NonNull String str) {
        if (str == null) {
            return nativeInsert(key.getStringKey(), (byte[]) null);
        }
        return nativeInsert(key.getStringKey(), str.getBytes(StandardCharsets.UTF_8));
    }

    public boolean insertInt(@NonNull Key key, int i) {
        return nativeInsert(key.getStringKey(), ByteBuffer.allocate(4).putInt(i).array());
    }

    public boolean insertLong(@NonNull Key key, long j) {
        return nativeInsert(key.getStringKey(), ByteBuffer.allocate(8).putLong(j).array());
    }

    public boolean insertFloat(@NonNull Key key, float f) {
        return nativeInsert(key.getStringKey(), ByteBuffer.allocate(4).putFloat(f).array());
    }

    public boolean insertDouble(@NonNull Key key, double d) {
        return nativeInsert(key.getStringKey(), ByteBuffer.allocate(4).putDouble(d).array());
    }

    public boolean insertBool(@NonNull Key key, boolean z) {
        return nativeInsert(key.getStringKey(), new byte[]{z ? (byte) 1 : 0});
    }

    public boolean insertBinary(@NonNull Key key, byte[] bArr) {
        return nativeInsert(key.getStringKey(), bArr);
    }

    public boolean insertBuffer(@NonNull Key key, ByteBuffer byteBuffer) {
        return nativeInsertBuffer(key.getStringKey(), byteBuffer);
    }

    public boolean insertStream(@NonNull Key key, InputStream inputStream) {
        if (inputStream != null) {
            try {
                int available = inputStream.available();
                byte[] bArr = new byte[available];
                if (inputStream.read(bArr, 0, inputStream.available()) == available) {
                    return nativeInsert(key.getStringKey(), bArr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean delete(@NonNull Key key) {
        return nativeDelete(key.getStringKey());
    }

    public boolean close() {
        sLSDBInstances.remove(this);
        return nativeClose();
    }

    public boolean forceCompact() {
        Log.e("ProtoDB", "begin compacting: " + this.path);
        boolean nativeCompact = nativeCompact();
        Log.e("ProtoDB", "finish compacting: " + this.path);
        return nativeCompact;
    }
}
