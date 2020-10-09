package com.taobao.orange.aidl;

import android.os.RemoteException;
import com.taobao.orange.OBaseListener;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfigListener;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.orange.aidl.ParcelableConfigListener;
import java.util.HashMap;
import java.util.Map;

public class OrangeConfigListenerStub extends ParcelableConfigListener.Stub {
    private boolean append = true;
    private OBaseListener mListener;

    public OrangeConfigListenerStub(OBaseListener oBaseListener) {
        this.mListener = oBaseListener;
    }

    public OrangeConfigListenerStub(OBaseListener oBaseListener, boolean z) {
        this.append = z;
        this.mListener = oBaseListener;
    }

    public boolean isAppend() {
        return this.append;
    }

    public void onConfigUpdate(String str, Map map) throws RemoteException {
        if (this.mListener instanceof OrangeConfigListener) {
            ((OrangeConfigListener) this.mListener).onConfigUpdate(str);
        } else if (this.mListener instanceof OrangeConfigListenerV1) {
            ((OrangeConfigListenerV1) this.mListener).onConfigUpdate(str, Boolean.parseBoolean((String) ((HashMap) map).get("fromCache")));
        } else if (this.mListener instanceof OConfigListener) {
            ((OConfigListener) this.mListener).onConfigUpdate(str, (HashMap) map);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.mListener.equals(((OrangeConfigListenerStub) obj).mListener);
    }

    public int hashCode() {
        return this.mListener.hashCode();
    }
}
