package android.taobao.windvane.runtimepermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.taobao.weex.common.WXModule;

import java.util.ArrayList;

public final class PermissionProposer {
    /* access modifiers changed from: private */
    public static PermissionRequestTask sCurrentPermissionRequestTask;

    public static synchronized PermissionRequestTask buildPermissionTask(Context context, String[] strArr) {
        PermissionRequestTask permissionRequestTask;
        synchronized (PermissionProposer.class) {
            if (context != null) {
                if (strArr != null) {
                    if (strArr.length != 0) {
                        permissionRequestTask = new PermissionRequestTask();
                        Context unused = permissionRequestTask.context = context;
                        String[] unused2 = permissionRequestTask.permissions = strArr;
                    }
                }
                throw new NullPointerException("permissions can not be null");
            }
            throw new NullPointerException("context can not be null");
        }
        return permissionRequestTask;
    }

    public static synchronized PermissionRequestTask buildSystemAlertPermissionTask(Activity activity) {
        PermissionRequestTask buildPermissionTask;
        synchronized (PermissionProposer.class) {
            buildPermissionTask = buildPermissionTask(activity, new String[]{"android.permission.SYSTEM_ALERT_WINDOW"});
        }
        return buildPermissionTask;
    }

    static void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (sCurrentPermissionRequestTask != null) {
            sCurrentPermissionRequestTask.onPermissionGranted(verifyPermissions(iArr));
            sCurrentPermissionRequestTask = null;
        }
    }

    static void onActivityResult(int i, int i2, Intent intent) {
        if (Build.VERSION.SDK_INT >= 23) {
            sCurrentPermissionRequestTask.onPermissionGranted(Settings.canDrawOverlays(sCurrentPermissionRequestTask.getContext()));
        }
        sCurrentPermissionRequestTask = null;
    }

    private static boolean verifyPermissions(int[] iArr) {
        if (iArr.length < 1) {
            return false;
        }
        for (int i : iArr) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static class PermissionRequestTask {
        /* access modifiers changed from: private */
        public Context context;
        private String explain;
        private Runnable permissionDeniedRunnable;
        private Runnable permissionGrantedRunnable;
        /* access modifiers changed from: private */
        public String[] permissions;

        public Context getContext() {
            return this.context;
        }

        public PermissionRequestTask setRationalStr(String str) {
            this.explain = str;
            return this;
        }

        public PermissionRequestTask setTaskOnPermissionGranted(Runnable runnable) {
            if (runnable != null) {
                this.permissionGrantedRunnable = runnable;
                return this;
            }
            throw new NullPointerException("permissionGrantedRunnable is null");
        }

        public PermissionRequestTask setTaskOnPermissionDenied(Runnable runnable) {
            this.permissionDeniedRunnable = runnable;
            return this;
        }

        public void execute() {
            int i = 0;
            if (Build.VERSION.SDK_INT >= 23) {
                if (this.permissions.length != 1 || !this.permissions[0].equals("android.permission.SYSTEM_ALERT_WINDOW")) {
                    ArrayList arrayList = new ArrayList();
                    String[] strArr = this.permissions;
                    int length = strArr.length;
                    while (i < length) {
                        String str = strArr[i];
                        try {
                            if (ActivityCompat.checkSelfPermission(this.context, str) != 0) {
                                arrayList.add(str);
                            }
                            i++;
                        } catch (Throwable unused) {
                            this.permissionGrantedRunnable.run();
                            return;
                        }
                    }
                    if (arrayList.size() == 0) {
                        this.permissionGrantedRunnable.run();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setClass(this.context, PermissionActivity.class);
                    if (!(this.context instanceof Activity)) {
                        intent.addFlags(268435456);
                    }
                    intent.putExtra(WXModule.PERMISSIONS, this.permissions);
                    intent.putExtra("explain", this.explain);
                    PermissionRequestTask unused2 = PermissionProposer.sCurrentPermissionRequestTask = this;
                    this.context.startActivity(intent);
                } else if (!Settings.canDrawOverlays(this.context)) {
                    Intent intent2 = new Intent();
                    intent2.setClass(this.context, PermissionActivity.class);
                    intent2.putExtra(WXModule.PERMISSIONS, this.permissions);
                    PermissionRequestTask unused3 = PermissionProposer.sCurrentPermissionRequestTask = this;
                    this.context.startActivity(intent2);
                } else {
                    this.permissionGrantedRunnable.run();
                }
            } else if (Build.VERSION.SDK_INT < 18) {
                try {
                    this.permissionGrantedRunnable.run();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else if (ActivityCompat.checkSelfPermission(this.context, this.permissions[0]) == 0) {
                this.permissionGrantedRunnable.run();
            } else {
                this.permissionDeniedRunnable.run();
            }
        }

        /* access modifiers changed from: package-private */
        public void onPermissionGranted(boolean z) {
            if (z) {
                if (this.permissionGrantedRunnable != null) {
                    this.permissionGrantedRunnable.run();
                }
            } else if (this.permissionDeniedRunnable != null) {
                this.permissionDeniedRunnable.run();
            }
            destroy();
        }

        private void destroy() {
            this.context = null;
            this.permissionGrantedRunnable = null;
            this.permissionDeniedRunnable = null;
        }
    }
}
