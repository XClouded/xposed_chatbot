package mtopsdk.xstate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.xstate.aidl.IXState;

public class XStateService extends Service {
    private static final String TAG = "mtopsdk.XStateService";
    Object lock = new Object();
    IXState.Stub stub = null;

    public int onStartCommand(Intent intent, int i, int i2) {
        return 2;
    }

    public IBinder onBind(Intent intent) {
        synchronized (this.lock) {
            if (this.stub == null) {
                this.stub = new XStateStub();
                try {
                    this.stub.init();
                } catch (RemoteException e) {
                    TBSdkLog.e(TAG, "[onBind]init() exception", (Throwable) e);
                } catch (Throwable th) {
                    TBSdkLog.e(TAG, "[onBind]init() error", th);
                }
            }
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[onBind] XStateService  stub= " + this.stub.hashCode());
        }
        return this.stub;
    }

    public void onDestroy() {
        super.onDestroy();
        synchronized (this.lock) {
            if (this.stub != null) {
                try {
                    this.stub.unInit();
                } catch (RemoteException e) {
                    TBSdkLog.e(TAG, "[onDestroy]unInit() exception", (Throwable) e);
                } catch (Throwable th) {
                    TBSdkLog.e(TAG, "[onDestroy]unInit() error", th);
                }
            }
        }
    }

    class XStateStub extends IXState.Stub {
        public XStateStub() {
        }

        public String removeKey(String str) throws RemoteException {
            return XStateDelegate.removeKey(str);
        }

        public void setValue(String str, String str2) throws RemoteException {
            XStateDelegate.setValue(str, str2);
        }

        public void init() throws RemoteException {
            XStateDelegate.init(XStateService.this.getBaseContext());
        }

        public void unInit() throws RemoteException {
            XStateDelegate.unInit();
        }

        public String getValue(String str) throws RemoteException {
            return XStateDelegate.getValue(str);
        }
    }
}
