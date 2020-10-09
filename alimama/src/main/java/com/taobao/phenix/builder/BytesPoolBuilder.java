package com.taobao.phenix.builder;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import com.taobao.phenix.bytes.LinkedBytesPool;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.intf.Phenix;
import com.taobao.tcommon.core.BytesPool;
import com.taobao.tcommon.core.Preconditions;

public class BytesPoolBuilder implements Builder<BytesPool> {
    private BytesPool mBytesPool;
    private ComponentCallbacks2 mComponentCallbacks;
    private boolean mHaveBuilt;
    private Integer mMaxSize;

    public BytesPoolBuilder with(BytesPool bytesPool) {
        Preconditions.checkState(!this.mHaveBuilt, "BytesPoolBuilder has been built, not allow with() now");
        this.mBytesPool = bytesPool;
        return this;
    }

    public BytesPoolBuilder maxSize(Integer num) {
        Preconditions.checkState(!this.mHaveBuilt, "BytesPoolBuilder has been built, not allow maxSize() now");
        this.mMaxSize = num;
        return this;
    }

    public synchronized BytesPool build() {
        if (this.mHaveBuilt) {
            return this.mBytesPool;
        }
        this.mHaveBuilt = true;
        if (this.mBytesPool == null) {
            this.mBytesPool = new LinkedBytesPool(this.mMaxSize != null ? this.mMaxSize.intValue() : 1048576);
        } else if (this.mMaxSize != null) {
            this.mBytesPool.resize(this.mMaxSize.intValue());
        }
        return registerComponentCallbacks(this.mBytesPool);
    }

    private BytesPool registerComponentCallbacks(final BytesPool bytesPool) {
        Context applicationContext = Phenix.instance().applicationContext();
        if (applicationContext != null && Build.VERSION.SDK_INT >= 14) {
            this.mComponentCallbacks = new ComponentCallbacks2() {
                public void onConfigurationChanged(Configuration configuration) {
                }

                public void onLowMemory() {
                }

                public void onTrimMemory(int i) {
                    UnitedLog.d("BytesPool", "ComponentCallbacks2 onTrimMemory, level=%d", Integer.valueOf(i));
                    if (i >= 60) {
                        bytesPool.clear();
                        UnitedLog.w("BytesPool", "clear all at TRIM_MEMORY_MODERATE", new Object[0]);
                    }
                }
            };
            applicationContext.registerComponentCallbacks(this.mComponentCallbacks);
        }
        return bytesPool;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Context applicationContext;
        try {
            super.finalize();
            applicationContext = Phenix.instance().applicationContext();
            if (applicationContext == null || this.mComponentCallbacks == null) {
                return;
            }
        } catch (Throwable th) {
            Context applicationContext2 = Phenix.instance().applicationContext();
            if (!(applicationContext2 == null || this.mComponentCallbacks == null)) {
                applicationContext2.unregisterComponentCallbacks(this.mComponentCallbacks);
            }
            throw th;
        }
        applicationContext.unregisterComponentCallbacks(this.mComponentCallbacks);
    }
}
