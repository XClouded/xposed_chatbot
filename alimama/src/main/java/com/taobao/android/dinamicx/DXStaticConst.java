package com.taobao.android.dinamicx;

public class DXStaticConst {
    private static int targetSdkVersion = -1;

    public static int getTargetSdkVersion() {
        if (targetSdkVersion == -1) {
            targetSdkVersion = DinamicXEngine.getApplicationContext().getApplicationInfo().targetSdkVersion;
        }
        return targetSdkVersion;
    }
}
