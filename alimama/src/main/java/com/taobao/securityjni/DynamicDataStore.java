package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.dynamicdatastore.IDynamicDataStoreComponent;

@Deprecated
public class DynamicDataStore {
    private IDynamicDataStoreComponent proxy;

    public byte[] getByteArrayDDp(String str) {
        return null;
    }

    public String getStringDDp(String str) {
        return null;
    }

    public int putByteArrayDDp(String str, byte[] bArr) {
        return 0;
    }

    public int putStringDDp(String str, String str2) {
        return 0;
    }

    public void removeByteArrayDDp(String str) {
    }

    public void removeStringDDp(String str) {
    }

    public DynamicDataStore(ContextWrapper contextWrapper) {
        SecurityGuardManager instance = SecurityGuardManager.getInstance(contextWrapper);
        if (instance != null) {
            this.proxy = instance.getDynamicDataStoreComp();
        }
    }

    public int putString(String str, String str2) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putString(str, str2);
    }

    public int putInt(String str, int i) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putInt(str, i);
    }

    public int putFloat(String str, float f) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putFloat(str, f);
    }

    public int putBoolean(String str, boolean z) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putBoolean(str, z);
    }

    public int putLong(String str, long j) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putLong(str, j);
    }

    public int putByteArray(String str, byte[] bArr) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putByteArray(str, bArr);
    }

    public String getString(String str) {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.getString(str);
    }

    public String getStringCompat(String str) {
        return getString(str);
    }

    public int getInt(String str) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.getInt(str);
    }

    public float getFloat(String str) {
        if (this.proxy == null) {
            return -1.0f;
        }
        return this.proxy.getFloat(str);
    }

    public long getLong(String str) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.getLong(str);
    }

    public long getLongCompat(String str) {
        return getLong(str);
    }

    public boolean getBoolean(String str) {
        if (this.proxy == null) {
            return false;
        }
        return this.proxy.getBoolean(str);
    }

    public byte[] getByteArray(String str) {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.getByteArray(str);
    }

    public void removeString(String str) {
        if (this.proxy != null) {
            this.proxy.removeString(str);
        }
    }

    public void removeInt(String str) {
        if (this.proxy != null) {
            this.proxy.removeInt(str);
        }
    }

    public void removeFloat(String str) {
        if (this.proxy != null) {
            this.proxy.removeFloat(str);
        }
    }

    public void removeLong(String str) {
        if (this.proxy != null) {
            this.proxy.removeLong(str);
        }
    }

    public void removeBoolean(String str) {
        if (this.proxy != null) {
            this.proxy.removeBoolean(str);
        }
    }

    public void removeByteArray(String str) {
        if (this.proxy != null) {
            this.proxy.removeByteArray(str);
        }
    }
}
