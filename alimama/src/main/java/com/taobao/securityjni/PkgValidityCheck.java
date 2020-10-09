package com.taobao.securityjni;

import android.content.Context;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.pkgvaliditycheck.IPkgValidityCheckComponent;

@Deprecated
public class PkgValidityCheck {
    public static int FLAG_DEX_FILE = 1;
    public static int FLAG_DEX_MANIFEST;

    public int checkEnvAndFiles(String... strArr) {
        return 0;
    }

    public boolean isPackageValid(String str) {
        return false;
    }

    public PkgValidityCheck(Context context) {
    }

    public String getDexHash(String str, String str2, int i) {
        IPkgValidityCheckComponent packageValidityCheckComp = SecurityGuardManager.getInstance(GlobalInit.getGlobalContext()).getPackageValidityCheckComp();
        if (packageValidityCheckComp != null) {
            return packageValidityCheckComp.getDexHash(str, str2, i);
        }
        return null;
    }
}
