package com.alibaba.analytics;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.alibaba.mtl.appmonitor.Transaction;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.Map;

public interface IAnalytics extends IInterface {
    boolean alarm_checkSampled(String str, String str2) throws RemoteException;

    void alarm_commitFail1(String str, String str2, String str3, String str4) throws RemoteException;

    void alarm_commitFail2(String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void alarm_commitSuccess1(String str, String str2) throws RemoteException;

    void alarm_commitSuccess2(String str, String str2, String str3) throws RemoteException;

    void alarm_setSampling(int i) throws RemoteException;

    void alarm_setStatisticsInterval(int i) throws RemoteException;

    boolean counter_checkSampled(String str, String str2) throws RemoteException;

    void counter_commit1(String str, String str2, double d) throws RemoteException;

    void counter_commit2(String str, String str2, String str3, double d) throws RemoteException;

    void counter_setSampling(int i) throws RemoteException;

    void counter_setStatisticsInterval(int i) throws RemoteException;

    void destroy() throws RemoteException;

    void dispatchLocalHits() throws RemoteException;

    void enableLog(boolean z) throws RemoteException;

    String getValue(String str) throws RemoteException;

    void init() throws RemoteException;

    void initUT() throws RemoteException;

    boolean offlinecounter_checkSampled(String str, String str2) throws RemoteException;

    void offlinecounter_commit(String str, String str2, double d) throws RemoteException;

    void offlinecounter_setSampling(int i) throws RemoteException;

    void offlinecounter_setStatisticsInterval(int i) throws RemoteException;

    void register1(String str, String str2, MeasureSet measureSet) throws RemoteException;

    void register2(String str, String str2, MeasureSet measureSet, boolean z) throws RemoteException;

    void register3(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet) throws RemoteException;

    void register4(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet, boolean z) throws RemoteException;

    void removeGlobalProperty(String str) throws RemoteException;

    void saveCacheDataToLocal() throws RemoteException;

    String selfCheck(String str) throws RemoteException;

    void sessionTimeout() throws RemoteException;

    void setAppVersion(String str) throws RemoteException;

    void setChannel(String str) throws RemoteException;

    void setGlobalProperty(String str, String str2) throws RemoteException;

    void setRequestAuthInfo(boolean z, boolean z2, String str, String str2) throws RemoteException;

    void setSampling(int i) throws RemoteException;

    void setSessionProperties(Map map) throws RemoteException;

    void setStatisticsInterval1(int i) throws RemoteException;

    void setStatisticsInterval2(int i, int i2) throws RemoteException;

    void stat_begin(String str, String str2, String str3) throws RemoteException;

    boolean stat_checkSampled(String str, String str2) throws RemoteException;

    void stat_commit1(String str, String str2, double d) throws RemoteException;

    void stat_commit2(String str, String str2, DimensionValueSet dimensionValueSet, double d) throws RemoteException;

    void stat_commit3(String str, String str2, DimensionValueSet dimensionValueSet, MeasureValueSet measureValueSet) throws RemoteException;

    void stat_end(String str, String str2, String str3) throws RemoteException;

    void stat_setSampling(int i) throws RemoteException;

    void stat_setStatisticsInterval(int i) throws RemoteException;

    void transaction_begin(Transaction transaction, String str) throws RemoteException;

    void transaction_end(Transaction transaction, String str) throws RemoteException;

    void transferLog(Map map) throws RemoteException;

    void triggerUpload() throws RemoteException;

    void turnOffRealTimeDebug() throws RemoteException;

    void turnOnDebug() throws RemoteException;

    void turnOnRealTimeDebug(Map map) throws RemoteException;

    void updateMeasure(String str, String str2, String str3, double d, double d2, double d3) throws RemoteException;

    void updateSessionProperties(Map map) throws RemoteException;

    void updateUserAccount(String str, String str2, String str3) throws RemoteException;

    public static abstract class Stub extends Binder implements IAnalytics {
        private static final String DESCRIPTOR = "com.alibaba.analytics.IAnalytics";
        static final int TRANSACTION_alarm_checkSampled = 39;
        static final int TRANSACTION_alarm_commitFail1 = 42;
        static final int TRANSACTION_alarm_commitFail2 = 43;
        static final int TRANSACTION_alarm_commitSuccess1 = 40;
        static final int TRANSACTION_alarm_commitSuccess2 = 41;
        static final int TRANSACTION_alarm_enableOfflineAgg = 54;
        static final int TRANSACTION_alarm_setSampling = 38;
        static final int TRANSACTION_alarm_setStatisticsInterval = 37;
        static final int TRANSACTION_counter_checkSampled = 30;
        static final int TRANSACTION_counter_commit1 = 31;
        static final int TRANSACTION_counter_commit2 = 32;
        static final int TRANSACTION_counter_setSampling = 29;
        static final int TRANSACTION_counter_setStatisticsInterval = 28;
        static final int TRANSACTION_destroy = 18;
        static final int TRANSACTION_disableNetworkStatusChecker = 9;
        static final int TRANSACTION_dispatchLocalHits = 10;
        static final int TRANSACTION_enableLog = 14;
        static final int TRANSACTION_getValue = 12;
        static final int TRANSACTION_init = 13;
        static final int TRANSACTION_initUT = 1;
        static final int TRANSACTION_offlinecounter_checkSampled = 35;
        static final int TRANSACTION_offlinecounter_commit = 36;
        static final int TRANSACTION_offlinecounter_setSampling = 34;
        static final int TRANSACTION_offlinecounter_setStatisticsInterval = 33;
        static final int TRANSACTION_register1 = 23;
        static final int TRANSACTION_register2 = 24;
        static final int TRANSACTION_register3 = 25;
        static final int TRANSACTION_register4 = 26;
        static final int TRANSACTION_removeGlobalProperty = 57;
        static final int TRANSACTION_saveCacheDataToLocal = 11;
        static final int TRANSACTION_selfCheck = 55;
        static final int TRANSACTION_sessionTimeout = 58;
        static final int TRANSACTION_setAppVersion = 3;
        static final int TRANSACTION_setChannel = 4;
        static final int TRANSACTION_setGlobalProperty = 56;
        static final int TRANSACTION_setRequestAuthInfo = 15;
        static final int TRANSACTION_setSampling = 20;
        static final int TRANSACTION_setSessionProperties = 6;
        static final int TRANSACTION_setStatisticsInterval1 = 21;
        static final int TRANSACTION_setStatisticsInterval2 = 22;
        static final int TRANSACTION_stat_begin = 44;
        static final int TRANSACTION_stat_checkSampled = 48;
        static final int TRANSACTION_stat_commit1 = 49;
        static final int TRANSACTION_stat_commit2 = 50;
        static final int TRANSACTION_stat_commit3 = 51;
        static final int TRANSACTION_stat_end = 45;
        static final int TRANSACTION_stat_setSampling = 47;
        static final int TRANSACTION_stat_setStatisticsInterval = 46;
        static final int TRANSACTION_transaction_begin = 52;
        static final int TRANSACTION_transaction_end = 53;
        static final int TRANSACTION_transferLog = 8;
        static final int TRANSACTION_triggerUpload = 19;
        static final int TRANSACTION_turnOffRealTimeDebug = 17;
        static final int TRANSACTION_turnOnDebug = 7;
        static final int TRANSACTION_turnOnRealTimeDebug = 16;
        static final int TRANSACTION_updateMeasure = 27;
        static final int TRANSACTION_updateSessionProperties = 5;
        static final int TRANSACTION_updateUserAccount = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAnalytics asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IAnalytics)) {
                return new Proxy(iBinder);
            }
            return (IAnalytics) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: com.alibaba.mtl.appmonitor.model.MeasureSet} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: com.alibaba.mtl.appmonitor.model.MeasureSet} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v11, resolved type: com.alibaba.mtl.appmonitor.model.DimensionSet} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v28, resolved type: com.alibaba.mtl.appmonitor.model.MeasureValueSet} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v32, resolved type: com.alibaba.mtl.appmonitor.Transaction} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v35, resolved type: com.alibaba.mtl.appmonitor.Transaction} */
        /* JADX WARNING: type inference failed for: r3v0 */
        /* JADX WARNING: type inference failed for: r3v26, types: [com.alibaba.mtl.appmonitor.model.DimensionValueSet] */
        /* JADX WARNING: type inference failed for: r3v27 */
        /* JADX WARNING: type inference failed for: r3v38 */
        /* JADX WARNING: type inference failed for: r3v39 */
        /* JADX WARNING: type inference failed for: r3v40 */
        /* JADX WARNING: type inference failed for: r3v41 */
        /* JADX WARNING: type inference failed for: r3v42 */
        /* JADX WARNING: type inference failed for: r3v43 */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r16, android.os.Parcel r17, android.os.Parcel r18, int r19) throws android.os.RemoteException {
            /*
                r15 = this;
                r10 = r15
                r0 = r16
                r1 = r17
                r11 = r18
                r2 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r12 = 1
                if (r0 == r2) goto L_0x054a
                switch(r0) {
                    case 1: goto L_0x053e;
                    case 2: goto L_0x0526;
                    case 3: goto L_0x0516;
                    case 4: goto L_0x0506;
                    case 5: goto L_0x04ee;
                    case 6: goto L_0x04d6;
                    case 7: goto L_0x04ca;
                    case 8: goto L_0x04b2;
                    default: goto L_0x0010;
                }
            L_0x0010:
                r2 = 0
                r3 = 0
                switch(r0) {
                    case 10: goto L_0x04a6;
                    case 11: goto L_0x049a;
                    case 12: goto L_0x0486;
                    case 13: goto L_0x047a;
                    case 14: goto L_0x0467;
                    case 15: goto L_0x0443;
                    case 16: goto L_0x042b;
                    case 17: goto L_0x041f;
                    case 18: goto L_0x0413;
                    case 19: goto L_0x0407;
                    case 20: goto L_0x03f7;
                    case 21: goto L_0x03e7;
                    case 22: goto L_0x03d3;
                    case 23: goto L_0x03b0;
                    case 24: goto L_0x0387;
                    case 25: goto L_0x0354;
                    case 26: goto L_0x030f;
                    case 27: goto L_0x02e4;
                    case 28: goto L_0x02d4;
                    case 29: goto L_0x02c4;
                    case 30: goto L_0x02ac;
                    case 31: goto L_0x0294;
                    case 32: goto L_0x0273;
                    case 33: goto L_0x0263;
                    case 34: goto L_0x0253;
                    case 35: goto L_0x023b;
                    case 36: goto L_0x0223;
                    case 37: goto L_0x0213;
                    case 38: goto L_0x0203;
                    case 39: goto L_0x01eb;
                    case 40: goto L_0x01d7;
                    case 41: goto L_0x01bf;
                    case 42: goto L_0x01a3;
                    case 43: goto L_0x017d;
                    case 44: goto L_0x0165;
                    case 45: goto L_0x014d;
                    case 46: goto L_0x013d;
                    case 47: goto L_0x012d;
                    case 48: goto L_0x0115;
                    case 49: goto L_0x00fd;
                    case 50: goto L_0x00d2;
                    case 51: goto L_0x009f;
                    case 52: goto L_0x0080;
                    case 53: goto L_0x0061;
                    default: goto L_0x0015;
                }
            L_0x0015:
                switch(r0) {
                    case 55: goto L_0x004d;
                    case 56: goto L_0x0039;
                    case 57: goto L_0x0029;
                    case 58: goto L_0x001d;
                    default: goto L_0x0018;
                }
            L_0x0018:
                boolean r0 = super.onTransact(r16, r17, r18, r19)
                return r0
            L_0x001d:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.sessionTimeout()
                r18.writeNoException()
                return r12
            L_0x0029:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                r15.removeGlobalProperty(r0)
                r18.writeNoException()
                return r12
            L_0x0039:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.setGlobalProperty(r0, r1)
                r18.writeNoException()
                return r12
            L_0x004d:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r0 = r15.selfCheck(r0)
                r18.writeNoException()
                r11.writeString(r0)
                return r12
            L_0x0061:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x0075
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.Transaction> r0 = com.alibaba.mtl.appmonitor.Transaction.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r1)
                r3 = r0
                com.alibaba.mtl.appmonitor.Transaction r3 = (com.alibaba.mtl.appmonitor.Transaction) r3
            L_0x0075:
                java.lang.String r0 = r17.readString()
                r15.transaction_end(r3, r0)
                r18.writeNoException()
                return r12
            L_0x0080:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x0094
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.Transaction> r0 = com.alibaba.mtl.appmonitor.Transaction.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r1)
                r3 = r0
                com.alibaba.mtl.appmonitor.Transaction r3 = (com.alibaba.mtl.appmonitor.Transaction) r3
            L_0x0094:
                java.lang.String r0 = r17.readString()
                r15.transaction_begin(r3, r0)
                r18.writeNoException()
                return r12
            L_0x009f:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                int r4 = r17.readInt()
                if (r4 == 0) goto L_0x00bb
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.DimensionValueSet> r4 = com.alibaba.mtl.appmonitor.model.DimensionValueSet.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r1)
                com.alibaba.mtl.appmonitor.model.DimensionValueSet r4 = (com.alibaba.mtl.appmonitor.model.DimensionValueSet) r4
                goto L_0x00bc
            L_0x00bb:
                r4 = r3
            L_0x00bc:
                int r5 = r17.readInt()
                if (r5 == 0) goto L_0x00cb
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.MeasureValueSet> r3 = com.alibaba.mtl.appmonitor.model.MeasureValueSet.CREATOR
                java.lang.Object r1 = r3.createFromParcel(r1)
                r3 = r1
                com.alibaba.mtl.appmonitor.model.MeasureValueSet r3 = (com.alibaba.mtl.appmonitor.model.MeasureValueSet) r3
            L_0x00cb:
                r15.stat_commit3(r0, r2, r4, r3)
                r18.writeNoException()
                return r12
            L_0x00d2:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r2 = r17.readString()
                java.lang.String r4 = r17.readString()
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x00ee
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.DimensionValueSet> r0 = com.alibaba.mtl.appmonitor.model.DimensionValueSet.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r1)
                com.alibaba.mtl.appmonitor.model.DimensionValueSet r0 = (com.alibaba.mtl.appmonitor.model.DimensionValueSet) r0
                r3 = r0
            L_0x00ee:
                double r5 = r17.readDouble()
                r0 = r15
                r1 = r2
                r2 = r4
                r4 = r5
                r0.stat_commit2(r1, r2, r3, r4)
                r18.writeNoException()
                return r12
            L_0x00fd:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                double r3 = r17.readDouble()
                r15.stat_commit1(r0, r2, r3)
                r18.writeNoException()
                return r12
            L_0x0115:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r1 = r17.readString()
                boolean r0 = r15.stat_checkSampled(r0, r1)
                r18.writeNoException()
                r11.writeInt(r0)
                return r12
            L_0x012d:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.stat_setSampling(r0)
                r18.writeNoException()
                return r12
            L_0x013d:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.stat_setStatisticsInterval(r0)
                r18.writeNoException()
                return r12
            L_0x014d:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.stat_end(r0, r2, r1)
                r18.writeNoException()
                return r12
            L_0x0165:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.stat_begin(r0, r2, r1)
                r18.writeNoException()
                return r12
            L_0x017d:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r2 = r17.readString()
                java.lang.String r3 = r17.readString()
                java.lang.String r4 = r17.readString()
                java.lang.String r5 = r17.readString()
                java.lang.String r6 = r17.readString()
                r0 = r15
                r1 = r2
                r2 = r3
                r3 = r4
                r4 = r5
                r5 = r6
                r0.alarm_commitFail2(r1, r2, r3, r4, r5)
                r18.writeNoException()
                return r12
            L_0x01a3:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                java.lang.String r3 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.alarm_commitFail1(r0, r2, r3, r1)
                r18.writeNoException()
                return r12
            L_0x01bf:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.alarm_commitSuccess2(r0, r2, r1)
                r18.writeNoException()
                return r12
            L_0x01d7:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.alarm_commitSuccess1(r0, r1)
                r18.writeNoException()
                return r12
            L_0x01eb:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r1 = r17.readString()
                boolean r0 = r15.alarm_checkSampled(r0, r1)
                r18.writeNoException()
                r11.writeInt(r0)
                return r12
            L_0x0203:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.alarm_setSampling(r0)
                r18.writeNoException()
                return r12
            L_0x0213:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.alarm_setStatisticsInterval(r0)
                r18.writeNoException()
                return r12
            L_0x0223:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                double r3 = r17.readDouble()
                r15.offlinecounter_commit(r0, r2, r3)
                r18.writeNoException()
                return r12
            L_0x023b:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r1 = r17.readString()
                boolean r0 = r15.offlinecounter_checkSampled(r0, r1)
                r18.writeNoException()
                r11.writeInt(r0)
                return r12
            L_0x0253:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.offlinecounter_setSampling(r0)
                r18.writeNoException()
                return r12
            L_0x0263:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.offlinecounter_setStatisticsInterval(r0)
                r18.writeNoException()
                return r12
            L_0x0273:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r2 = r17.readString()
                java.lang.String r3 = r17.readString()
                java.lang.String r4 = r17.readString()
                double r5 = r17.readDouble()
                r0 = r15
                r1 = r2
                r2 = r3
                r3 = r4
                r4 = r5
                r0.counter_commit2(r1, r2, r3, r4)
                r18.writeNoException()
                return r12
            L_0x0294:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                double r3 = r17.readDouble()
                r15.counter_commit1(r0, r2, r3)
                r18.writeNoException()
                return r12
            L_0x02ac:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r1 = r17.readString()
                boolean r0 = r15.counter_checkSampled(r0, r1)
                r18.writeNoException()
                r11.writeInt(r0)
                return r12
            L_0x02c4:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.counter_setSampling(r0)
                r18.writeNoException()
                return r12
            L_0x02d4:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.counter_setStatisticsInterval(r0)
                r18.writeNoException()
                return r12
            L_0x02e4:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r2 = r17.readString()
                java.lang.String r3 = r17.readString()
                java.lang.String r4 = r17.readString()
                double r5 = r17.readDouble()
                double r7 = r17.readDouble()
                double r13 = r17.readDouble()
                r0 = r15
                r1 = r2
                r2 = r3
                r3 = r4
                r4 = r5
                r6 = r7
                r8 = r13
                r0.updateMeasure(r1, r2, r3, r4, r6, r8)
                r18.writeNoException()
                return r12
            L_0x030f:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r4 = r17.readString()
                java.lang.String r5 = r17.readString()
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x032c
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.MeasureSet> r0 = com.alibaba.mtl.appmonitor.model.MeasureSet.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r1)
                com.alibaba.mtl.appmonitor.model.MeasureSet r0 = (com.alibaba.mtl.appmonitor.model.MeasureSet) r0
                r6 = r0
                goto L_0x032d
            L_0x032c:
                r6 = r3
            L_0x032d:
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x033d
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.DimensionSet> r0 = com.alibaba.mtl.appmonitor.model.DimensionSet.CREATOR
                java.lang.Object r0 = r0.createFromParcel(r1)
                com.alibaba.mtl.appmonitor.model.DimensionSet r0 = (com.alibaba.mtl.appmonitor.model.DimensionSet) r0
                r7 = r0
                goto L_0x033e
            L_0x033d:
                r7 = r3
            L_0x033e:
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x0346
                r8 = 1
                goto L_0x0347
            L_0x0346:
                r8 = 0
            L_0x0347:
                r0 = r15
                r1 = r4
                r2 = r5
                r3 = r6
                r4 = r7
                r5 = r8
                r0.register4(r1, r2, r3, r4, r5)
                r18.writeNoException()
                return r12
            L_0x0354:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                int r4 = r17.readInt()
                if (r4 == 0) goto L_0x0370
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.MeasureSet> r4 = com.alibaba.mtl.appmonitor.model.MeasureSet.CREATOR
                java.lang.Object r4 = r4.createFromParcel(r1)
                com.alibaba.mtl.appmonitor.model.MeasureSet r4 = (com.alibaba.mtl.appmonitor.model.MeasureSet) r4
                goto L_0x0371
            L_0x0370:
                r4 = r3
            L_0x0371:
                int r5 = r17.readInt()
                if (r5 == 0) goto L_0x0380
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.DimensionSet> r3 = com.alibaba.mtl.appmonitor.model.DimensionSet.CREATOR
                java.lang.Object r1 = r3.createFromParcel(r1)
                r3 = r1
                com.alibaba.mtl.appmonitor.model.DimensionSet r3 = (com.alibaba.mtl.appmonitor.model.DimensionSet) r3
            L_0x0380:
                r15.register3(r0, r2, r4, r3)
                r18.writeNoException()
                return r12
            L_0x0387:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r4 = r17.readString()
                int r5 = r17.readInt()
                if (r5 == 0) goto L_0x03a2
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.MeasureSet> r3 = com.alibaba.mtl.appmonitor.model.MeasureSet.CREATOR
                java.lang.Object r3 = r3.createFromParcel(r1)
                com.alibaba.mtl.appmonitor.model.MeasureSet r3 = (com.alibaba.mtl.appmonitor.model.MeasureSet) r3
            L_0x03a2:
                int r1 = r17.readInt()
                if (r1 == 0) goto L_0x03a9
                r2 = 1
            L_0x03a9:
                r15.register2(r0, r4, r3, r2)
                r18.writeNoException()
                return r12
            L_0x03b0:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                int r4 = r17.readInt()
                if (r4 == 0) goto L_0x03cc
                android.os.Parcelable$Creator<com.alibaba.mtl.appmonitor.model.MeasureSet> r3 = com.alibaba.mtl.appmonitor.model.MeasureSet.CREATOR
                java.lang.Object r1 = r3.createFromParcel(r1)
                r3 = r1
                com.alibaba.mtl.appmonitor.model.MeasureSet r3 = (com.alibaba.mtl.appmonitor.model.MeasureSet) r3
            L_0x03cc:
                r15.register1(r0, r2, r3)
                r18.writeNoException()
                return r12
            L_0x03d3:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                int r1 = r17.readInt()
                r15.setStatisticsInterval2(r0, r1)
                r18.writeNoException()
                return r12
            L_0x03e7:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.setStatisticsInterval1(r0)
                r18.writeNoException()
                return r12
            L_0x03f7:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                r15.setSampling(r0)
                r18.writeNoException()
                return r12
            L_0x0407:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.triggerUpload()
                r18.writeNoException()
                return r12
            L_0x0413:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.destroy()
                r18.writeNoException()
                return r12
            L_0x041f:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.turnOffRealTimeDebug()
                r18.writeNoException()
                return r12
            L_0x042b:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.Class r0 = r15.getClass()
                java.lang.ClassLoader r0 = r0.getClassLoader()
                java.util.HashMap r0 = r1.readHashMap(r0)
                r15.turnOnRealTimeDebug(r0)
                r18.writeNoException()
                return r12
            L_0x0443:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x0450
                r0 = 1
                goto L_0x0451
            L_0x0450:
                r0 = 0
            L_0x0451:
                int r3 = r17.readInt()
                if (r3 == 0) goto L_0x0458
                r2 = 1
            L_0x0458:
                java.lang.String r3 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.setRequestAuthInfo(r0, r2, r3, r1)
                r18.writeNoException()
                return r12
            L_0x0467:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                int r0 = r17.readInt()
                if (r0 == 0) goto L_0x0473
                r2 = 1
            L_0x0473:
                r15.enableLog(r2)
                r18.writeNoException()
                return r12
            L_0x047a:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.init()
                r18.writeNoException()
                return r12
            L_0x0486:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r0 = r15.getValue(r0)
                r18.writeNoException()
                r11.writeString(r0)
                return r12
            L_0x049a:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.saveCacheDataToLocal()
                r18.writeNoException()
                return r12
            L_0x04a6:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.dispatchLocalHits()
                r18.writeNoException()
                return r12
            L_0x04b2:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.Class r0 = r15.getClass()
                java.lang.ClassLoader r0 = r0.getClassLoader()
                java.util.HashMap r0 = r1.readHashMap(r0)
                r15.transferLog(r0)
                r18.writeNoException()
                return r12
            L_0x04ca:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.turnOnDebug()
                r18.writeNoException()
                return r12
            L_0x04d6:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.Class r0 = r15.getClass()
                java.lang.ClassLoader r0 = r0.getClassLoader()
                java.util.HashMap r0 = r1.readHashMap(r0)
                r15.setSessionProperties(r0)
                r18.writeNoException()
                return r12
            L_0x04ee:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.Class r0 = r15.getClass()
                java.lang.ClassLoader r0 = r0.getClassLoader()
                java.util.HashMap r0 = r1.readHashMap(r0)
                r15.updateSessionProperties(r0)
                r18.writeNoException()
                return r12
            L_0x0506:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                r15.setChannel(r0)
                r18.writeNoException()
                return r12
            L_0x0516:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                r15.setAppVersion(r0)
                r18.writeNoException()
                return r12
            L_0x0526:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                java.lang.String r0 = r17.readString()
                java.lang.String r2 = r17.readString()
                java.lang.String r1 = r17.readString()
                r15.updateUserAccount(r0, r2, r1)
                r18.writeNoException()
                return r12
            L_0x053e:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r1.enforceInterface(r0)
                r15.initUT()
                r18.writeNoException()
                return r12
            L_0x054a:
                java.lang.String r0 = "com.alibaba.analytics.IAnalytics"
                r11.writeString(r0)
                return r12
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.IAnalytics.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        private static class Proxy implements IAnalytics {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void initUT() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateUserAccount(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAppVersion(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setChannel(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateSessionProperties(Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeMap(map);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSessionProperties(Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeMap(map);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void turnOnDebug() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void transferLog(Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeMap(map);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void dispatchLocalHits() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void saveCacheDataToLocal() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getValue(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void init() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enableLog(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String selfCheck(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setRequestAuthInfo(boolean z, boolean z2, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(z2 ? 1 : 0);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void turnOnRealTimeDebug(Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeMap(map);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void turnOffRealTimeDebug() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void destroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void triggerUpload() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSampling(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setStatisticsInterval1(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setStatisticsInterval2(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void register1(String str, String str2, MeasureSet measureSet) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (measureSet != null) {
                        obtain.writeInt(1);
                        measureSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void register2(String str, String str2, MeasureSet measureSet, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (measureSet != null) {
                        obtain.writeInt(1);
                        measureSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void register3(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (measureSet != null) {
                        obtain.writeInt(1);
                        measureSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (dimensionSet != null) {
                        obtain.writeInt(1);
                        dimensionSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void register4(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (measureSet != null) {
                        obtain.writeInt(1);
                        measureSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (dimensionSet != null) {
                        obtain.writeInt(1);
                        dimensionSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateMeasure(String str, String str2, String str3, double d, double d2, double d3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeDouble(d);
                    obtain.writeDouble(d2);
                    obtain.writeDouble(d3);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void counter_setStatisticsInterval(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void counter_setSampling(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean counter_checkSampled(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    boolean z = false;
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void counter_commit1(String str, String str2, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeDouble(d);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void counter_commit2(String str, String str2, String str3, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeDouble(d);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void offlinecounter_setStatisticsInterval(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void offlinecounter_setSampling(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean offlinecounter_checkSampled(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    boolean z = false;
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void offlinecounter_commit(String str, String str2, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeDouble(d);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alarm_setStatisticsInterval(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alarm_setSampling(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean alarm_checkSampled(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    boolean z = false;
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alarm_commitSuccess1(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alarm_commitSuccess2(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alarm_commitFail1(String str, String str2, String str3, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alarm_commitFail2(String str, String str2, String str3, String str4, String str5) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    obtain.writeString(str5);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_begin(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_end(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_setStatisticsInterval(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_setSampling(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean stat_checkSampled(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    boolean z = false;
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_commit1(String str, String str2, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeDouble(d);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_commit2(String str, String str2, DimensionValueSet dimensionValueSet, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (dimensionValueSet != null) {
                        obtain.writeInt(1);
                        dimensionValueSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeDouble(d);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stat_commit3(String str, String str2, DimensionValueSet dimensionValueSet, MeasureValueSet measureValueSet) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (dimensionValueSet != null) {
                        obtain.writeInt(1);
                        dimensionValueSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (measureValueSet != null) {
                        obtain.writeInt(1);
                        measureValueSet.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void transaction_begin(Transaction transaction, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        obtain.writeInt(1);
                        transaction.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void transaction_end(Transaction transaction, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        obtain.writeInt(1);
                        transaction.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setGlobalProperty(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeGlobalProperty(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sessionTimeout() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
