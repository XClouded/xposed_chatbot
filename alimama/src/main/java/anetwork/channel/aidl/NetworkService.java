package anetwork.channel.aidl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.RemoteNetwork;
import anetwork.channel.degrade.DegradableNetworkDelegate;
import anetwork.channel.http.HttpNetworkDelegate;

public class NetworkService extends Service {
    private static final String TAG = "anet.NetworkService";
    private Context context;
    /* access modifiers changed from: private */
    public RemoteNetwork.Stub degradeableNetwork = null;
    /* access modifiers changed from: private */
    public RemoteNetwork.Stub httpNetwork = null;
    IRemoteNetworkGetter.Stub stub = new IRemoteNetworkGetter.Stub() {
        public RemoteNetwork get(int i) throws RemoteException {
            return i == 1 ? NetworkService.this.degradeableNetwork : NetworkService.this.httpNetwork;
        }
    };

    public void onDestroy() {
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 2;
    }

    public IBinder onBind(Intent intent) {
        this.context = getApplicationContext();
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "onBind:" + intent.getAction(), (String) null, new Object[0]);
        }
        this.degradeableNetwork = new DegradableNetworkDelegate(this.context);
        this.httpNetwork = new HttpNetworkDelegate(this.context);
        if (IRemoteNetworkGetter.class.getName().equals(intent.getAction())) {
            return this.stub;
        }
        return null;
    }
}
