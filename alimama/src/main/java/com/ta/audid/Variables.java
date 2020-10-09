package com.ta.audid;

import android.content.Context;
import android.text.TextUtils;
import com.ta.audid.db.DBMgr;
import com.ta.audid.permission.PermissionUtils;
import com.ta.audid.upload.UtdidKeyFile;
import com.ta.audid.utils.UtdidLogger;
import java.io.File;

public class Variables {
    private static final String DATABASE_NAME = "utdid.db";
    private static final Variables mInstance = new Variables();
    private boolean bGetModeState = false;
    private volatile boolean bInit = false;
    private boolean bOldMode = false;
    private String mAppChannel = "";
    private String mAppkey = "testKey";
    private Context mContext = null;
    private DBMgr mDbMgr = null;
    private long mDeltaTime = 0;
    private File mOldModeFile = null;
    private boolean mPhoneStatePermission = false;
    private boolean mStoragePermission = false;

    private Variables() {
    }

    public static Variables getInstance() {
        return mInstance;
    }

    public synchronized void init() {
        if (!this.bInit) {
            this.mDbMgr = new DBMgr(this.mContext, DATABASE_NAME);
            this.mStoragePermission = PermissionUtils.checkStoragePermissionGranted(this.mContext);
            this.mPhoneStatePermission = PermissionUtils.checkReadPhoneStatePermissionGranted(this.mContext);
            this.bInit = true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initContext(android.content.Context r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            android.content.Context r0 = r1.mContext     // Catch:{ all -> 0x001a }
            if (r0 != 0) goto L_0x0018
            if (r2 != 0) goto L_0x0009
            monitor-exit(r1)
            return
        L_0x0009:
            android.content.Context r0 = r2.getApplicationContext()     // Catch:{ all -> 0x001a }
            if (r0 == 0) goto L_0x0016
            android.content.Context r2 = r2.getApplicationContext()     // Catch:{ all -> 0x001a }
            r1.mContext = r2     // Catch:{ all -> 0x001a }
            goto L_0x0018
        L_0x0016:
            r1.mContext = r2     // Catch:{ all -> 0x001a }
        L_0x0018:
            monitor-exit(r1)
            return
        L_0x001a:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.Variables.initContext(android.content.Context):void");
    }

    @Deprecated
    public synchronized void setOldMode(boolean z) {
        try {
            this.bOldMode = z;
            UtdidLogger.d("", Boolean.valueOf(this.bOldMode));
            if (this.mOldModeFile == null) {
                this.mOldModeFile = new File(UtdidKeyFile.getOldModeFilePath());
            }
            boolean exists = this.mOldModeFile.exists();
            if (z && !exists) {
                this.mOldModeFile.createNewFile();
            } else if (!z && exists) {
                this.mOldModeFile.delete();
            }
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        return;
    }

    public synchronized boolean getOldMode() {
        if (this.bGetModeState) {
            UtdidLogger.d("", Boolean.valueOf(this.bOldMode));
            return this.bOldMode;
        }
        try {
            if (this.mOldModeFile == null) {
                this.mOldModeFile = new File(UtdidKeyFile.getOldModeFilePath());
            }
            if (this.mOldModeFile.exists()) {
                this.bOldMode = true;
                UtdidLogger.d("", "old mode file");
                boolean z = this.bOldMode;
                this.bGetModeState = true;
                return z;
            }
        } catch (Exception e) {
            try {
                UtdidLogger.d("", e);
            } catch (Throwable th) {
                this.bGetModeState = true;
                throw th;
            }
        }
        this.bGetModeState = true;
        this.bOldMode = false;
        UtdidLogger.d("", "new mode file");
        return this.bOldMode;
    }

    public void setDebug(boolean z) {
        UtdidLogger.setDebug(z);
    }

    public Context getContext() {
        return this.mContext;
    }

    public DBMgr getDbMgr() {
        return this.mDbMgr;
    }

    public boolean userGrantStoragePermission() {
        boolean checkStoragePermissionGranted = PermissionUtils.checkStoragePermissionGranted(this.mContext);
        if (this.mStoragePermission || !checkStoragePermissionGranted) {
            this.mStoragePermission = checkStoragePermissionGranted;
            return false;
        }
        this.mStoragePermission = true;
        return true;
    }

    public boolean userGrantPhoneStatePermission() {
        boolean checkReadPhoneStatePermissionGranted = PermissionUtils.checkReadPhoneStatePermissionGranted(this.mContext);
        if (this.mPhoneStatePermission || !checkReadPhoneStatePermissionGranted) {
            this.mStoragePermission = checkReadPhoneStatePermissionGranted;
            return false;
        }
        this.mStoragePermission = true;
        return true;
    }

    public void setAppkey(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mAppkey = str;
        }
    }

    public String getAppkey() {
        return this.mAppkey;
    }

    public void setAppChannel(String str) {
        this.mAppChannel = str;
    }

    public String getAppChannel() {
        return this.mAppChannel;
    }

    public void setSystemTime(long j) {
        this.mDeltaTime = j - System.currentTimeMillis();
    }

    public long getCurrentTimeMillis() {
        return System.currentTimeMillis() + this.mDeltaTime;
    }

    public String getCurrentTimeMillisString() {
        return "" + getCurrentTimeMillis();
    }
}
