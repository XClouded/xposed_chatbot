package org.android.netutil;

import androidx.annotation.Keep;

@Keep
public abstract class NetListener {
    long native_ptr = 0;
    NetListenerType netListenerType;

    public NetListener(NetListenerType netListenerType2) {
        this.netListenerType = netListenerType2;
    }
}
