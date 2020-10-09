package com.taobao.weex.common;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.WXEnvironment;

public abstract class WXCompatModule extends WXModule implements Destroyable {
    private ModuleReceive mModuleReceive = new ModuleReceive(this);

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public WXCompatModule() {
        LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).registerReceiver(this.mModuleReceive, new IntentFilter(WXModule.ACTION_ACTIVITY_RESULT));
        LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).registerReceiver(this.mModuleReceive, new IntentFilter(WXModule.ACTION_REQUEST_PERMISSIONS_RESULT));
    }

    public void destroy() {
        LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).unregisterReceiver(this.mModuleReceive);
    }

    static class ModuleReceive extends BroadcastReceiver {
        private WXCompatModule mWXCompatModule;

        ModuleReceive(WXCompatModule wXCompatModule) {
            this.mWXCompatModule = wXCompatModule;
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x002d  */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0045  */
        /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r4, android.content.Intent r5) {
            /*
                r3 = this;
                java.lang.String r4 = r5.getAction()
                int r0 = r4.hashCode()
                r1 = 306451426(0x124413e2, float:6.1871202E-28)
                r2 = -1
                if (r0 == r1) goto L_0x001e
                r1 = 1904079688(0x717df348, float:1.2575011E30)
                if (r0 == r1) goto L_0x0014
                goto L_0x0028
            L_0x0014:
                java.lang.String r0 = "actionRequestPermissionsResult"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L_0x0028
                r4 = 1
                goto L_0x0029
            L_0x001e:
                java.lang.String r0 = "actionActivityResult"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L_0x0028
                r4 = 0
                goto L_0x0029
            L_0x0028:
                r4 = -1
            L_0x0029:
                switch(r4) {
                    case 0: goto L_0x0045;
                    case 1: goto L_0x002d;
                    default: goto L_0x002c;
                }
            L_0x002c:
                goto L_0x0056
            L_0x002d:
                java.lang.String r4 = "requestCode"
                int r4 = r5.getIntExtra(r4, r2)
                java.lang.String r0 = "permissions"
                java.lang.String[] r0 = r5.getStringArrayExtra(r0)
                java.lang.String r1 = "grantResults"
                int[] r5 = r5.getIntArrayExtra(r1)
                com.taobao.weex.common.WXCompatModule r1 = r3.mWXCompatModule
                r1.onRequestPermissionsResult(r4, r0, r5)
                goto L_0x0056
            L_0x0045:
                java.lang.String r4 = "requestCode"
                int r4 = r5.getIntExtra(r4, r2)
                java.lang.String r0 = "resultCode"
                int r0 = r5.getIntExtra(r0, r2)
                com.taobao.weex.common.WXCompatModule r1 = r3.mWXCompatModule
                r1.onActivityResult(r4, r0, r5)
            L_0x0056:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.common.WXCompatModule.ModuleReceive.onReceive(android.content.Context, android.content.Intent):void");
        }
    }
}
