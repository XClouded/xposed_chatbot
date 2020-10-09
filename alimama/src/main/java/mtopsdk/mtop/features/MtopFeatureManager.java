package mtopsdk.mtop.features;

import android.content.Context;
import java.util.Set;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.intf.Mtop;

public final class MtopFeatureManager {
    private static final String TAG = "mtopsdk.MtopFeatureManager";

    public static long getMtopFeatureValue(int i) {
        if (i < 1) {
            return 0;
        }
        return (long) (1 << (i - 1));
    }

    public enum MtopFeatureEnum {
        SUPPORT_RELATIVE_URL(1),
        UNIT_INFO_FEATURE(2),
        DISABLE_WHITEBOX_SIGN(3),
        SUPPORT_UTDID_UNIT(4),
        DISABLE_X_COMMAND(5),
        SUPPORT_OPEN_ACCOUNT(6);
        
        long feature;

        public long getFeature() {
            return this.feature;
        }

        private MtopFeatureEnum(long j) {
            this.feature = j;
        }
    }

    public static long getMtopTotalFeatures(Mtop mtop) {
        if (mtop == null) {
            mtop = Mtop.instance((Context) null);
        }
        long j = 0;
        try {
            for (Integer intValue : mtop.getMtopConfig().mtopFeatures) {
                j |= getMtopFeatureValue(intValue.intValue());
            }
        } catch (Exception e) {
            TBSdkLog.w(TAG, mtop.getInstanceId() + " [getMtopTotalFeatures] get mtop total features error.---" + e.toString());
        }
        return j;
    }

    public static int getMtopFeatureByFeatureEnum(MtopFeatureEnum mtopFeatureEnum) {
        if (mtopFeatureEnum == null) {
            return 0;
        }
        switch (mtopFeatureEnum) {
            case SUPPORT_RELATIVE_URL:
                return 1;
            case UNIT_INFO_FEATURE:
                return 2;
            case DISABLE_WHITEBOX_SIGN:
                return 3;
            case SUPPORT_UTDID_UNIT:
                return 4;
            case DISABLE_X_COMMAND:
                return 5;
            case SUPPORT_OPEN_ACCOUNT:
                return 6;
            default:
                return 0;
        }
    }

    public static void setMtopFeatureFlag(Mtop mtop, int i, boolean z) {
        if (mtop == null) {
            mtop = Mtop.instance((Context) null);
        }
        Set<Integer> set = mtop.getMtopConfig().mtopFeatures;
        if (z) {
            set.add(Integer.valueOf(i));
        } else {
            set.remove(Integer.valueOf(i));
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, mtop.getInstanceId() + " [setMtopFeatureFlag] set feature=" + i + " , openFlag=" + z);
        }
    }
}
