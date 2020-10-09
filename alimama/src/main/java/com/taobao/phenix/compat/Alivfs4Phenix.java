package com.taobao.phenix.compat;

import com.taobao.phenix.builder.DiskCacheBuilder;
import com.taobao.phenix.cache.disk.DiskCacheSupplier;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.compat.alivfs.AlivfsDiskCacheSupplier;
import com.taobao.phenix.intf.Phenix;

public class Alivfs4Phenix {
    public static void setupDiskCache(Integer num, Integer num2, Integer num3, Integer num4, Integer num5) {
        AlivfsDiskCacheSupplier alivfsDiskCacheSupplier = new AlivfsDiskCacheSupplier();
        alivfsDiskCacheSupplier.ensureInitialized();
        try {
            DiskCacheBuilder with = Phenix.instance().diskCacheBuilder().with((DiskCacheSupplier) alivfsDiskCacheSupplier);
            if (num != null) {
                with.maxSize(17, num.intValue());
            }
            if (num2 != null) {
                with.maxSize(34, num2.intValue());
            }
            if (num3 != null) {
                with.maxSize(51, num3.intValue());
            }
            if (num4 != null) {
                with.maxSize(68, num4.intValue());
            }
            if (num5 != null) {
                with.maxSize(85, num5.intValue());
            }
            UnitedLog.i("Alivfs4Phenix", "disk cache setup, top1=%s top2=%s top3=%s top4=%s top5=%s", num, num2, num3, num4, num5);
        } catch (RuntimeException e) {
            UnitedLog.e("Alivfs4Phenix", "disk cache setup error=%s", e);
        }
    }

    public static void setupDiskCache() {
        setupDiskCache((Integer) null, (Integer) null, (Integer) null, (Integer) null, (Integer) null);
    }
}
