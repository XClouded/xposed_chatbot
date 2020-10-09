package org.android.spdy;

import android.util.LruCache;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

class HTTPHeaderPool {
    private static HTTPHeaderPool instance = new HTTPHeaderPool();
    private LruCache<ByteBuffer, String> lruCache = new LruCache<>(128);

    private HTTPHeaderPool() {
    }

    public String GetValueString(ByteBuffer byteBuffer) {
        String str = this.lruCache.get(byteBuffer);
        if (str != null) {
            return str;
        }
        try {
            str = new String(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit() - byteBuffer.position(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.lruCache.put(byteBuffer, str);
        return str;
    }

    public static HTTPHeaderPool getInstance() {
        return instance;
    }
}
