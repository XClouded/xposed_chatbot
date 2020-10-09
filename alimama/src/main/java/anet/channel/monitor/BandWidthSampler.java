package anet.channel.monitor;

import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;

public class BandWidthSampler {
    static final int FAST = 5;
    static final int SLOW = 1;
    private static final String TAG = "awcn.BandWidthSampler";
    private static volatile boolean isNetworkMeterStarted = false;
    static long mKalmanDataSize = 0;
    static long mKalmanTimeUsed = 0;
    static int mReceivedDataCount = 0;
    static long mReceivedRequestFinishedTimePre = 0;
    static long mReceivedRequestStartTimePre = 0;
    static long mSpeedKalmanCount = 0;
    static double speed = 0.0d;
    static double speedPre = 0.0d;
    static double speedPrePre = 0.0d;
    static double speedThreshold = 40.0d;
    /* access modifiers changed from: private */
    public int currentNetworkSpeed;
    /* access modifiers changed from: private */
    public NetWorkKalmanFilter mNetWorkKalmanFilter;
    /* access modifiers changed from: private */
    public int mSinceLastNotification;

    static /* synthetic */ int access$208(BandWidthSampler bandWidthSampler) {
        int i = bandWidthSampler.mSinceLastNotification;
        bandWidthSampler.mSinceLastNotification = i + 1;
        return i;
    }

    static class StaticHolder {
        static BandWidthSampler instance = new BandWidthSampler();

        StaticHolder() {
        }
    }

    public static BandWidthSampler getInstance() {
        return StaticHolder.instance;
    }

    private BandWidthSampler() {
        this.currentNetworkSpeed = 5;
        this.mSinceLastNotification = 0;
        this.mNetWorkKalmanFilter = new NetWorkKalmanFilter();
        NetworkStatusHelper.addStatusChangeListener(new NetworkStatusHelper.INetworkStatusChangeListener() {
            public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
                BandWidthSampler.this.mNetWorkKalmanFilter.ResetKalmanParams();
                BandWidthSampler.mSpeedKalmanCount = 0;
                BandWidthSampler.this.startNetworkMeter();
            }
        });
    }

    public int getNetworkSpeed() {
        if (NetworkStatusHelper.getStatus() == NetworkStatusHelper.NetworkStatus.G2) {
            return 1;
        }
        return this.currentNetworkSpeed;
    }

    public double getNetSpeedValue() {
        return speed;
    }

    public synchronized void startNetworkMeter() {
        try {
            ALog.i(TAG, "[startNetworkMeter]", (String) null, "NetworkStatus", NetworkStatusHelper.getStatus());
            if (NetworkStatusHelper.getStatus() == NetworkStatusHelper.NetworkStatus.G2) {
                isNetworkMeterStarted = false;
                return;
            }
            isNetworkMeterStarted = true;
        } catch (Exception e) {
            ALog.w(TAG, "startNetworkMeter fail.", (String) null, e, new Object[0]);
        }
        return;
    }

    public void stopNetworkMeter() {
        isNetworkMeterStarted = false;
    }

    public void onDataReceived(long j, long j2, long j3) {
        if (isNetworkMeterStarted) {
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "onDataReceived", (String) null, "mRequestStartTime", Long.valueOf(j), "mRequestFinishedTime", Long.valueOf(j2), "mRequestDataSize", Long.valueOf(j3));
            }
            if (j3 > TBToast.Duration.MEDIUM && j < j2) {
                final long j4 = j3;
                final long j5 = j2;
                final long j6 = j;
                ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                    public void run() {
                        BandWidthSampler.mReceivedDataCount++;
                        BandWidthSampler.mKalmanDataSize += j4;
                        if (BandWidthSampler.mReceivedDataCount == 1) {
                            BandWidthSampler.mKalmanTimeUsed = j5 - j6;
                        }
                        if (BandWidthSampler.mReceivedDataCount >= 2 && BandWidthSampler.mReceivedDataCount <= 3) {
                            if (j6 >= BandWidthSampler.mReceivedRequestFinishedTimePre) {
                                BandWidthSampler.mKalmanTimeUsed += j5 - j6;
                            } else if (j6 < BandWidthSampler.mReceivedRequestFinishedTimePre && j5 >= BandWidthSampler.mReceivedRequestFinishedTimePre) {
                                BandWidthSampler.mKalmanTimeUsed += j5 - j6;
                                BandWidthSampler.mKalmanTimeUsed -= BandWidthSampler.mReceivedRequestFinishedTimePre - j6;
                            }
                        }
                        BandWidthSampler.mReceivedRequestStartTimePre = j6;
                        BandWidthSampler.mReceivedRequestFinishedTimePre = j5;
                        if (BandWidthSampler.mReceivedDataCount == 3) {
                            BandWidthSampler.speed = (double) ((long) BandWidthSampler.this.mNetWorkKalmanFilter.addMeasurement((double) BandWidthSampler.mKalmanDataSize, (double) BandWidthSampler.mKalmanTimeUsed));
                            BandWidthSampler.mSpeedKalmanCount++;
                            BandWidthSampler.access$208(BandWidthSampler.this);
                            if (BandWidthSampler.mSpeedKalmanCount > 30) {
                                BandWidthSampler.this.mNetWorkKalmanFilter.ResetKalmanParams();
                                BandWidthSampler.mSpeedKalmanCount = 3;
                            }
                            double d = (BandWidthSampler.speed * 0.68d) + (BandWidthSampler.speedPre * 0.27d) + (BandWidthSampler.speedPrePre * 0.05d);
                            BandWidthSampler.speedPrePre = BandWidthSampler.speedPre;
                            BandWidthSampler.speedPre = BandWidthSampler.speed;
                            if (BandWidthSampler.speed < BandWidthSampler.speedPrePre * 0.65d || BandWidthSampler.speed > BandWidthSampler.speedPrePre * 2.0d) {
                                BandWidthSampler.speed = d;
                            }
                            int i = 5;
                            if (ALog.isPrintLog(1)) {
                                ALog.d(BandWidthSampler.TAG, "NetworkSpeed", (String) null, "mKalmanDataSize", Long.valueOf(BandWidthSampler.mKalmanDataSize), "mKalmanTimeUsed", Long.valueOf(BandWidthSampler.mKalmanTimeUsed), "speed", Double.valueOf(BandWidthSampler.speed), "mSpeedKalmanCount", Long.valueOf(BandWidthSampler.mSpeedKalmanCount));
                            }
                            if (BandWidthSampler.this.mSinceLastNotification > 5 || BandWidthSampler.mSpeedKalmanCount == 2) {
                                BandWidthListenerHelper.getInstance().onNetworkSpeedValueNotify(BandWidthSampler.speed);
                                int unused = BandWidthSampler.this.mSinceLastNotification = 0;
                                BandWidthSampler bandWidthSampler = BandWidthSampler.this;
                                if (BandWidthSampler.speed < BandWidthSampler.speedThreshold) {
                                    i = 1;
                                }
                                int unused2 = bandWidthSampler.currentNetworkSpeed = i;
                                ALog.i(BandWidthSampler.TAG, "NetworkSpeed notification!", (String) null, "Send Network quality notification.");
                            }
                            BandWidthSampler.mKalmanTimeUsed = 0;
                            BandWidthSampler.mKalmanDataSize = 0;
                            BandWidthSampler.mReceivedDataCount = 0;
                        }
                    }
                });
            }
        }
    }
}
