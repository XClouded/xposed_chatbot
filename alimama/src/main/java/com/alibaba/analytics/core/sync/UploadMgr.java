package com.alibaba.analytics.core.sync;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.store.ILogChangeListener;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.core.sync.UploadLog;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.delegate.BackgroundTrigger;
import com.taobao.accs.common.Constants;
import java.util.concurrent.ScheduledFuture;

public class UploadMgr implements BackgroundTrigger.AppStatusChangeCallback {
    private static final long DEFAULT_BACKGROUND_INTERVAL = 300000;
    private static final int DEFAULT_INTERVAL = 30000;
    private static final int ONE_SECOND = 1000;
    private static final String TAG = "UploadMgr";
    private static final String TAG_BACKGROUND_INTERVAL = "bu";
    private static final String TAG_FOREGROUND_INTERVAL = "fu";
    static UploadMgr mInstance = new UploadMgr();
    /* access modifiers changed from: private */
    public UploadLog.NetworkStatus mAllowedNetworkStatus = UploadLog.NetworkStatus.ALL;
    /* access modifiers changed from: private */
    public long mBatchThreshold = 50;
    /* access modifiers changed from: private */
    public UploadMode mCurrentMode = null;
    /* access modifiers changed from: private */
    public long mCurrentUploadInterval = 30000;
    /* access modifiers changed from: private */
    public long mLeftCount = 0;
    private ILogChangeListener mListener;
    /* access modifiers changed from: private */
    public long mUploadCount = 0;
    /* access modifiers changed from: private */
    public ScheduledFuture mUploadFuture;
    /* access modifiers changed from: private */
    public UploadTask mUploadTaskTask = new UploadTask();
    private long mUserSettingInterval;

    private UploadMgr() {
        BackgroundTrigger.registerCallback(this);
    }

    public static UploadMgr getInstance() {
        return mInstance;
    }

    public synchronized void start() {
        Logger.d();
        readLocalConfig();
        UploadQueueMgr.getInstance().start();
        UploadLogFromCache.getInstance().setAllowedNetworkStatus(this.mAllowedNetworkStatus);
        UploadLogFromCache.getInstance().setIUploadExcuted(new IUploadExcuted() {
            public void onUploadExcuted(long j) {
                UploadLogFromCache.getInstance().setAllowedNetworkStatus(UploadMgr.this.mAllowedNetworkStatus);
            }
        });
        if (this.mCurrentMode == null) {
            this.mCurrentMode = UploadMode.INTERVAL;
        }
        if (this.mUploadFuture != null) {
            this.mUploadFuture.cancel(true);
        }
        start(this.mCurrentMode);
    }

    private void readLocalConfig() {
        String string = AppInfoUtil.getString(Variables.getInstance().getContext(), "UTANALYTICS_UPLOAD_ALLOWED_NETWORK_STATUS");
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if ("ALL".equalsIgnoreCase(string)) {
            this.mAllowedNetworkStatus = UploadLog.NetworkStatus.ALL;
        } else if ("2G".equalsIgnoreCase(string)) {
            this.mAllowedNetworkStatus = UploadLog.NetworkStatus.TWO_GENERATION;
        } else if ("3G".equalsIgnoreCase(string)) {
            this.mAllowedNetworkStatus = UploadLog.NetworkStatus.THRID_GENERATION;
        } else if ("4G".equalsIgnoreCase(string)) {
            this.mAllowedNetworkStatus = UploadLog.NetworkStatus.FOUR_GENERATION;
        } else if ("WIFI".equalsIgnoreCase(string)) {
            this.mAllowedNetworkStatus = UploadLog.NetworkStatus.WIFI;
        }
    }

    public synchronized void stop() {
        Logger.d();
        if (this.mUploadFuture != null) {
            this.mUploadFuture.cancel(true);
        }
        this.mCurrentMode = null;
    }

    public void setMode(UploadMode uploadMode) {
        if (uploadMode != null && this.mCurrentMode != uploadMode) {
            this.mCurrentMode = uploadMode;
            start();
        }
    }

    public void setUploadInterval(long j) {
        if (j > 0) {
            this.mUserSettingInterval = j;
            if (this.mCurrentUploadInterval != calNextInterval()) {
                start();
            }
        }
    }

    public void setBatchThreshold(long j) {
        if (this.mCurrentMode == UploadMode.BATCH && j != this.mBatchThreshold) {
            start();
        }
        this.mBatchThreshold = j;
    }

    public void setAllowedNetoworkStatus(UploadLog.NetworkStatus networkStatus) {
        if (this.mAllowedNetworkStatus != networkStatus) {
            start();
        }
        this.mAllowedNetworkStatus = networkStatus;
    }

    private synchronized void start(UploadMode uploadMode) {
        Logger.d("startMode", Constants.KEY_MODE, uploadMode);
        switch (uploadMode) {
            case REALTIME:
                startRealTimeMode();
                break;
            case BATCH:
                startBatchMode();
                break;
            case LAUNCH:
                startLunchMode();
                break;
            case DEVELOPMENT:
                startDeveloperMode();
                break;
            default:
                startIntervalMode();
                break;
        }
    }

    private void startRealTimeMode() {
        if (this.mListener != null) {
            LogStoreMgr.getInstance().unRegisterChangeListener(this.mListener);
        }
        this.mListener = new ILogChangeListener() {
            public void onDelete(long j, long j2) {
            }

            public void onInsert(long j, long j2) {
                Logger.d("RealTimeMode", "count", Long.valueOf(j), "dbSize", Long.valueOf(j2));
                if (j > 0 && j2 > 0 && UploadMode.REALTIME == UploadMgr.this.mCurrentMode) {
                    ScheduledFuture unused = UploadMgr.this.mUploadFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, UploadMgr.this.mUploadTaskTask, 0);
                }
            }
        };
        LogStoreMgr.getInstance().registerLogChangeListener(this.mListener);
    }

    private void startBatchMode() {
        if (this.mListener != null) {
            LogStoreMgr.getInstance().unRegisterChangeListener(this.mListener);
        }
        UploadLogFromDB.getInstance().setIUploadExcuted((IUploadExcuted) null);
        UploadLogFromDB.getInstance().setAllowedNetworkStatus(this.mAllowedNetworkStatus);
        this.mListener = new ILogChangeListener() {
            public void onDelete(long j, long j2) {
            }

            public void onInsert(long j, long j2) {
                Logger.d("BatchMode", "count", Long.valueOf(j), "dbSize", Long.valueOf(j2));
                if (j2 >= UploadMgr.this.mBatchThreshold && UploadMode.BATCH == UploadMgr.this.mCurrentMode) {
                    UploadLogFromDB.getInstance().setAllowedNetworkStatus(UploadMgr.this.mAllowedNetworkStatus);
                    ScheduledFuture unused = UploadMgr.this.mUploadFuture = TaskExecutor.getInstance().schedule(UploadMgr.this.mUploadFuture, UploadMgr.this.mUploadTaskTask, 0);
                }
            }
        };
        LogStoreMgr.getInstance().registerLogChangeListener(this.mListener);
    }

    private void startLunchMode() {
        this.mLeftCount = LogStoreMgr.getInstance().count();
        if (this.mLeftCount > 0) {
            this.mUploadCount = 0;
            UploadLogFromDB.getInstance().setIUploadExcuted(new IUploadExcuted() {
                public void onUploadExcuted(long j) {
                    long unused = UploadMgr.this.mUploadCount = j;
                    if (UploadMode.LAUNCH == UploadMgr.this.mCurrentMode && UploadMgr.this.mUploadCount >= UploadMgr.this.mLeftCount) {
                        UploadMgr.this.mUploadFuture.cancel(false);
                    }
                }
            });
            UploadLogFromDB.getInstance().setAllowedNetworkStatus(this.mAllowedNetworkStatus);
            this.mUploadFuture = TaskExecutor.getInstance().scheduleAtFixedRate(this.mUploadFuture, this.mUploadTaskTask, 5000);
        }
    }

    private void startDeveloperMode() {
        UploadLogFromDB.getInstance().setIUploadExcuted((IUploadExcuted) null);
        this.mUploadFuture = TaskExecutor.getInstance().schedule(this.mUploadFuture, this.mUploadTaskTask, 0);
    }

    private void startIntervalMode() {
        this.mCurrentUploadInterval = calNextInterval();
        Logger.d(TAG, "startIntervalMode CurrentUploadInterval", Long.valueOf(this.mCurrentUploadInterval));
        UploadLogFromDB.getInstance().setIUploadExcuted(new IUploadExcuted() {
            public void onUploadExcuted(long j) {
                long unused = UploadMgr.this.mCurrentUploadInterval = UploadMgr.this.calNextInterval();
                Logger.d(UploadMgr.TAG, "CurrentUploadInterval", Long.valueOf(UploadMgr.this.mCurrentUploadInterval));
                UploadLogFromDB.getInstance().setAllowedNetworkStatus(UploadMgr.this.mAllowedNetworkStatus);
                ScheduledFuture unused2 = UploadMgr.this.mUploadFuture = TaskExecutor.getInstance().schedule(UploadMgr.this.mUploadFuture, UploadMgr.this.mUploadTaskTask, UploadMgr.this.mCurrentUploadInterval);
            }
        });
        this.mUploadFuture = TaskExecutor.getInstance().schedule(this.mUploadFuture, this.mUploadTaskTask, 5000);
    }

    /* access modifiers changed from: private */
    public long calNextInterval() {
        if (!AppInfoUtil.isAppOnForeground(Variables.getInstance().getContext())) {
            long j = (long) (SystemConfigMgr.getInstance().getInt(TAG_BACKGROUND_INTERVAL) * 1000);
            if (j == 0) {
                return 300000;
            }
            return j;
        }
        long j2 = (long) (SystemConfigMgr.getInstance().getInt(TAG_FOREGROUND_INTERVAL) * 1000);
        if (j2 != 0) {
            return j2;
        }
        if (this.mUserSettingInterval < 30000) {
            return 30000;
        }
        return this.mUserSettingInterval;
    }

    public void onBackground() {
        Logger.d();
        if (UploadMode.INTERVAL == this.mCurrentMode) {
            if (this.mCurrentUploadInterval != calNextInterval()) {
                start();
            }
        }
    }

    public void onForeground() {
        Logger.d();
        if (UploadMode.INTERVAL == this.mCurrentMode) {
            if (this.mCurrentUploadInterval != calNextInterval()) {
                start();
            }
        }
    }

    public long getCurrentUploadInterval() {
        return this.mCurrentUploadInterval;
    }

    public UploadMode getCurrentMode() {
        return this.mCurrentMode;
    }

    @Deprecated
    public void dispatchHits() {
        TaskExecutor.getInstance().submit(this.mUploadTaskTask);
    }
}
