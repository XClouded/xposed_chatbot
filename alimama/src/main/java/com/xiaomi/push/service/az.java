package com.xiaomi.push.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.xiaomi.channel.commonutils.logger.b;

class az implements ServiceConnection {
    final /* synthetic */ ax a;

    az(ax axVar) {
        this.a = axVar;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.a) {
            Messenger unused = this.a.b = new Messenger(iBinder);
            boolean unused2 = this.a.f877b = false;
            for (Message send : ax.a(this.a)) {
                try {
                    ax.a(this.a).send(send);
                } catch (RemoteException e) {
                    b.a((Throwable) e);
                }
            }
            ax.a(this.a).clear();
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        Messenger unused = this.a.b = null;
        boolean unused2 = this.a.f877b = false;
    }
}
