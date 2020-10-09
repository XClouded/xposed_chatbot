package org.android.netutil;

import androidx.annotation.Keep;

@Keep
public abstract class AddressListener extends NetListener {
    public abstract void onNewAddress(String str);

    public AddressListener() {
        super(NetListenerType.NL_NEW_IP_ADDRESS);
    }
}
